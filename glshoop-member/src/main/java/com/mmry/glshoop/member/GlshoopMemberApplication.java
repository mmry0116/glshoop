package com.mmry.glshoop.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.mmry.glshoop.member.feign")
public class GlshoopMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlshoopMemberApplication.class, args);
    }

}
