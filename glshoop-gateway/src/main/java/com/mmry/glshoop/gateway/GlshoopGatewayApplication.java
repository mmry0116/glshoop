package com.mmry.glshoop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GlshoopGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlshoopGatewayApplication.class, args);
    }

}
