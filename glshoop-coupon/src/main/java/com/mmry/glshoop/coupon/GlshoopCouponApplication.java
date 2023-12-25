package com.mmry.glshoop.coupon;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GlshoopCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlshoopCouponApplication.class, args);
    }

}
