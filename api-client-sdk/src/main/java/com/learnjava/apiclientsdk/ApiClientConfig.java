package com.learnjava.apiclientsdk;

import com.learnjava.apiclientsdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("api.client")
@Data
@ComponentScan
public class ApiClientConfig {
    //由配置中设置
    private String accessKey;

    private String secretKey;

    @Bean
    public ApiClient getClient() {
        return new ApiClient(accessKey, secretKey);
    }
}