package com.example.cqrs.demo;

import com.example.cqrs.demo.common.xxljob.XxlJobRibbonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
//@EnableDiscoveryClient
@RibbonClients({
        @RibbonClient(name = "xxljob", configuration = XxlJobRibbonConfig.class)
})
public class CqrsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CqrsDemoApplication.class, args);
    }

}
