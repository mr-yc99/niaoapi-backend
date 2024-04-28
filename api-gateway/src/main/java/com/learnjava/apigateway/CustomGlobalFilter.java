package com.learnjava.apigateway;

import com.foryaapicommon.model.entity.ApiInfo;
import com.foryaapicommon.model.entity.User;
import com.foryaapicommon.service.GatewayApiInfoService;
import com.foryaapicommon.service.GatewayUserApiInfoService;
import com.foryaapicommon.service.GatewayUserService;
import com.learnjava.apiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤
 * @return
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private GatewayUserService gatewayUserService;

    @DubboReference
    private GatewayApiInfoService gatewayApiInfoService;

    @DubboReference
    private GatewayUserApiInfoService gatewayUserApiInfoService;

    private static final String HOST = "http://localhost:8123";

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1", "0:0:0:0:0:0:0:1");
    //编写业务逻辑
    //异步的操作
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("custom global filter");
        //1请求日志
        ServerHttpRequest request = exchange.getRequest();

        String method = request.getMethod().toString();
        // path不包括host和port，例如/api/name/get，可以从数据库中读取所有的url放到hashmap，再做拼接，这里就先用http://localhost:8123
        String path = HOST + request.getPath().value() ;
        String queryParams = request.getQueryParams().toString();
        String sourceAddress = request.getLocalAddress().getHostString();

        log.info("请求唯一标识" + request.getId());
        log.info("请求来源地址" + sourceAddress); // 只是有主机地址
        log.info("请求来源地址" + request.getRemoteAddress()); // 主机地址 + 端口号
        log.info("请求方法" + method);
        log.info("请求路径" + path); // eg：目标host+端口/api/useApi
        log.info("请求参数" + queryParams);

        //2控制访问白名单
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            // 直接返回状态码
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        //3 用户鉴权（ak sk是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey =headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String sign = headers.getFirst("sign");
        String timestamp = headers.getFirst("timestamp");
        String body = headers.getFirst("body");

        //get user
        User user = gatewayUserService.getInvokeUser(accessKey);
        String secretKey = user.getSecretKey();

        if(user == null) {
            return handleNoAuth(response);//也可以编写全局异常处理器
        }

        if(Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }

        //时间戳与当前时间不能超过5分钟
        long currentTime = System.currentTimeMillis()/1000; //当前秒数
        final long FIVE_MIN = 60 * 5L;
        if((currentTime - Long.parseLong(timestamp))/1000/60 < FIVE_MIN) {
            return handleNoAuth(response);
        }

        // 校验签名，与客户端一样先生成签名，再比对
        String serverSign = SignUtils.getSign(body, secretKey);
        if(!serverSign.equals(sign)) {
            return handleNoAuth(response);
        }

        //4请求模拟接口是否存在（网关项目没有引入MyBatis，如果操作比较复杂，可以有backend中的接口来完成（HTTP（用HTTPClient等）、RPC（Dubbo））
        ApiInfo apiInfo = gatewayApiInfoService.getApiInfo(path, method);
        Long apiId = apiInfo.getId();
        if(apiInfo == null) {
            return handleNoAuth(response);//也可以编写全局异常处理器
        }

        //5 判断是否还有调用次数
        Integer num = gatewayUserApiInfoService.invokeLeftNum(apiId, user.getId());
        if(num <= 0) {
            return handleNoAuth(response);
        }

        //6 请求转发, 统计次数, 响应日志
        //Mono<Void> filter = chain.filter(exchange);
        //return filter;
        Long apiInfoId = apiInfo.getId();
        Long userId = user.getId();
        return handleResponseLog(exchange, chain, apiInfoId, userId);

    }

    @Override
    public int getOrder() {
        return -1;
    }


    //ServerHttpResponseDecorator

    /**
     * 处理响应
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponseLog(ServerWebExchange exchange, GatewayFilterChain chain, long apiInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            //缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                return chain.filter(exchange);//降级处理返回数据
            }
            //装饰器，增强能力
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                //下面的方法就是返回我们要增强的事情,并且调用接口后才会执行
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        //writeWith：往返回值写数据的
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            //调用成功，更改次数
                            //这里用try catch包裹，防止出现异常时网关直接挂掉
                            try {
                                gatewayUserApiInfoService.invokeCount(apiInfoId, userId);
                            } catch (Exception e) {
                                log.info("invokeCount error: " + e);

                            }

                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer buff = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[buff.readableByteCount()];
                            buff.read(content);
                            DataBufferUtils.release(buff);//释放掉内存
                            // 构建返回日志
                            StringBuffer sb2 = new StringBuffer(200);
                            //sb2.append("<--- {} {} \n");
                            List<Object> rspArgs = new ArrayList<>();
                            rspArgs.add(originalResponse.getStatusCode());

                            String data = new String(content, StandardCharsets.UTF_8);
                            sb2.append(data);

                            //打印日志
                            log.info("响应结果：" + sb2.toString(), rspArgs.toArray());
                            return bufferFactory.wrap(content);
                        }));
                    } else {
                        log.error("<-- {} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            //这里设置response对象为装饰过的
            return chain.filter(exchange.mutate().response(decoratedResponse).build());

        } catch (Exception e) {
            //网关处理响应异常
            log.error("网关处理响应异常: " + e);
            return chain.filter(exchange);
        }
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        System.out.println("fail----------------------------------------");
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleError(ServerHttpResponse response) {
        System.out.println("fail----------------------------------------");
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

}