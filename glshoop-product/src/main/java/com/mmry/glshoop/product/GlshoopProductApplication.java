package com.mmry.glshoop.product;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "")
@SpringBootApplication
@MapperScan("com/mmry/glshoop/product/dao")
@EnableDiscoveryClient
public class GlshoopProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlshoopProductApplication.class, args);
    }

}
