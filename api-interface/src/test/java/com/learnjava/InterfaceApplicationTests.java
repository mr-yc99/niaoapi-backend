package com.learnjava;

import com.learnjava.apiclientsdk.client.ApiClient;
import com.learnjava.apiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InterfaceApplicationTests {
    @Autowired
    private ApiClient apiClient;


    @Test
    void contextLoads() {
        User user = new User();
        user.setUserName("admin");
        String userNameByPost = apiClient.getUserNameByPost(user);
        System.out.println(userNameByPost);

    }
}
