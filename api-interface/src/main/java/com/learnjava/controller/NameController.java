package com.learnjava.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.learnjava.apiclientsdk.client.ApiClient;
import com.learnjava.apiclientsdk.model.User;
import com.learnjava.apiclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 名称API
 * 模拟接口
 * 8123:api/name/...
 */
@RestController
@RequestMapping("/useApi")
public class NameController {

    @Resource
    private ApiClient apiClient;

    //返回api名字与请求参数
    @GetMapping("/apiName")
    public String getNameByGet(String name) {
        return "这个api名字：getNameByGet，请求参数：" + name;
    }


    @PostMapping("/post")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest httpServletRequest) {
/*        String accessKey = httpServletRequest.getHeader("accessKey");
        String nonce = httpServletRequest.getHeader("nonce");
        String sign = httpServletRequest.getHeader("sign");
        String timestamp = httpServletRequest.getHeader("timestamp");
        String body = httpServletRequest.getHeader("body");*/
        /*

        //todo 实际要先查是否有这个用户，还要看这个用户的状态, API的校验实际也要到数据库中查,这里就先简单举一个例子
        if(!"admin".equals(accessKey)) {
            throw new RuntimeException("accessKey is error");
        }

        //todo 随机数可以用hashmap，redis等来存
        if(Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }

        //时间戳：与当前时间不能超过5分钟）
        long currentTime = System.currentTimeMillis()/1000; //当前秒数
        long FIVE_MIN = 60 * 5L;
        if((currentTime - Long.parseLong(timestamp))/1000/60 < FIVE_MIN) {
            throw new RuntimeException("error");
        }
         //todo 校验签名，与客户端一样先生成签名，再比对
        //这里出了问题
        String serverSign = SignUtils.getSign(body, "abcdefgh");//这里sK实际要从数据库里查
        String md5Hex1 = DigestUtil.md5Hex(body + "abcdefgh");
        //实在不行就用yupi一样的生成签名的方式，这里报错

        String serverSign = SignUtils.getSign(body, "abcdefgh");
        if(!serverSign.equals(sign)) {
            throw new RuntimeException("无权限");
        }
        */

        String success = "getUserNameByPost 调用成功";
        //调用成功

        return success;
    }

    //博天api的随机毒鸡汤
    @GetMapping("/chickenSoup")
    public String getChickenSoup() {
        String soup = apiClient.generateText();
        return "鸡汤来咯：" + soup;
    }

}
