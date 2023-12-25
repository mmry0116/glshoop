package com.mmry.glshoop.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@EnableFeignClients("com.mmry.glshoop.ware.feign")
@MapperScan("com.mmry.glshoop.ware.dao")
@EnableDiscoveryClient
@EnableTransactionManagement
@SpringBootApplication
public class GlshoopWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlshoopWareApplication.class, args);
    }

}
