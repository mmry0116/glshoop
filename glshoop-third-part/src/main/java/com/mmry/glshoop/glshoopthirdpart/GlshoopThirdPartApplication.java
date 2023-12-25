package com.mmry.glshoop.glshoopthirdpart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class GlshoopThirdPartApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlshoopThirdPartApplication.class, args);
    }

}
