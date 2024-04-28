package com.learnjava.apigateway;

//import com.foryaapi.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableDubbo
public class ApiGatewayApplication {
    //
    //@DubboReference
    //private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApiGatewayApplication.class, args);
        ApiGatewayApplication application = context.getBean(ApiGatewayApplication.class);
        //String doSayHello2 = application.doSayHello2();
        //System.out.println(doSayHello2);
    }

    //public String doSayHello2() {
    //    return demoService.sayHello2();
    //}

    //@Bean
    //public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    //    return builder.routes()
    //            .route("tobaidu", r -> r.path("/baidu")
    //                    .uri("https://www.baidu.com"))
    //            .route("host_route", r -> r.path("*.myhost.org")
    //                    .uri("http://httpbin.org"))
    //            .build();
    //}
}