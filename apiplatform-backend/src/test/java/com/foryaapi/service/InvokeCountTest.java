package com.foryaapi.service;


import com.foryaapi.MyApplication;
import com.foryaapicommon.service.GatewayUserApiInfoService;
import org.junit.jupiter.api.Assertions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyApplication.class})
public class InvokeCountTest {
    @Autowired
    private GatewayUserApiInfoService gatewayUserApiInfoService;

    @Test
    public void test() {
        boolean b = gatewayUserApiInfoService.invokeCount(1L, 1L);

        Assertions.assertTrue(b);
    }

}
