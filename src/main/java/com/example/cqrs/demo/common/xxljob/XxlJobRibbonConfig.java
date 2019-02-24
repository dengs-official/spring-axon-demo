package com.example.cqrs.demo.common.xxljob;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;

@Configuration
@Slf4j
public class XxlJobRibbonConfig {

    @Value("${xxl.job.url}")
    private String url;

    @Value("${xxl.job.port}")
    private int port;

    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        BaseLoadBalancer balancer = new BaseLoadBalancer();
        balancer.setServersList(Collections.singletonList(new Server(url, port)));
        return balancer;
    }
}
