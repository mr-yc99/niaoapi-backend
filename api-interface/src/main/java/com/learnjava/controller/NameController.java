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
