package com.learnjava.apiclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.learnjava.apiclientsdk.utils.SignUtils;
import com.learnjava.apiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用第三方接口的客户端
 *
 * @author xyc
 */

public class ApiClient {
    private static final String GATEWAY_HOST = "http://localhost:8080";

    private String accessKey;

    private String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    //模拟header, 鉴权信息
    private Map<String, String> getHeaderMap(long id,String body, String method,String url) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        // 接口ID
        hashMap.put("apiId", String.valueOf(id));
        // 一定不能直接发送
        // hashMap.put("secretKey", secretKey);
        // 只能一次请求只能调用一次接口，用后请求作废
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        // 请求一定时间内有效
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        // 签名
        hashMap.put("sign", SignUtils.getSign(body, secretKey));
        // 处理参数中文问题
        body = URLUtil.encode(body, CharsetUtil.CHARSET_UTF_8);
        hashMap.put("body", body);
        // 下面这三个是寻找调用接口的关键
        hashMap.put("method", method);
        //hashMap.put("path",path);
        hashMap.put("url",url);
        return hashMap;
    }
    private Map<String, String> getHeaderMap(String body) {

        HashMap<String, String> header = new HashMap<>();
        header.put("accessKey", accessKey);
//        header.put("secretKey", secretKey); 不能发给后端
        header.put("nonce", RandomUtil.randomNumbers(5));
        header.put("body", body);
        header.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));///1000总秒数
        header.put("sign", SignUtils.getSign(body, secretKey));
        return header;
    }

    //第一个get接口
    public String getNameByGet(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result3= HttpUtil.get(GATEWAY_HOST, paramMap);

        System.out.println(result3);
        return result3;
    }

    //第二个接口
    public String getUserNameByPost(User user) {
        String jsonStr = JSONUtil.toJsonStr(user);

        Map<String, String> header = getHeaderMap(jsonStr);

        HttpResponse httpresponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .body(jsonStr)
                .addHeaders(header)
                .execute();
        //下面这里出现问题
        //System.out.println(httpresponse.getStatus());
        String body = httpresponse.body();
        //System.out.println(body);
        return body;
    }

    //第三个接口：博天的api，返回毒鸡汤
    public String generateText() {
        String result = HttpUtil.get("https://api.btstu.cn/yan/api.php");
        return result;
    }

    /**
     * 支持调用方法为post和get的接口，把请求导向网关
     * @param params 接口参数
     * @param url 接口地址
     * @param method 接口使用方法
     * @return 接口调用结果
     */
    public String invokeInterface(long id,String params, String url, String method) {
        //log.info("SDK正在转发至GATEWAY_HOST:{}",GATEWAY_HOST);


        if ("post".equals(method)) {
            HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST)
                    // 处理中文编码
                    .header("Accept-Charset", CharsetUtil.UTF_8)
                    .addHeaders(getHeaderMap(id,params, method,url))
                    .body(params)
                    .execute();

            String body = httpResponse.body();
            // 可以在SDK处理接口404的情况
            if(httpResponse.getStatus()==404){
                body = String.format("{\"code\": %d,\"msg\":\"%s\",\"data\":\"%s\"}",
                        httpResponse.getStatus(), "接口请求路径不存在", "null");
                //log.info("响应结果：" + body);
            }
            // 将返回的JSON结果格式化，其实就是加换行符
            return JSONUtil.formatJsonStr(body);
        }
        //log.info("SDK调用接口完成，响应数据：{}",result);

        if ("get".equals(method)) {
            try(
                    HttpResponse httpGetResponse = HttpRequest.get(GATEWAY_HOST)
                            .header("Accept-Charset", CharsetUtil.UTF_8)
                            .addHeaders(getHeaderMap(id, params, method, url))
                            .body(params)
                            .execute()
            )
            {
                String body = httpGetResponse.body();
                // 可以在SDK处理接口404的情况
                if(httpGetResponse.getStatus()==404){
                    body = String.format("{\"code\": %d,\"msg\":\"%s\",\"data\":\"%s\"}",
                            httpGetResponse.getStatus(), "接口请求路径不存在", "null");
                    //log.info("响应结果：" + body);
                }
                // 将返回的JSON结果格式化，其实就是加换行符
                return JSONUtil.formatJsonStr(body);
            }
        }
        //只写了get和post，其他就返回null表示不支持
        return null;
    }
}
