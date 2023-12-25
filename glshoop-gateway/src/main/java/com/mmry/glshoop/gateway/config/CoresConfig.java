package com.mmry.glshoop.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CoresConfig {

    /**
     * 这个全局filter作用:
     * 专门处理前端/api/**请求跨域请求
     * @return
     */
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("http://localhost:8001");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        //是否允许携带cookie跨域
        corsConfiguration.setAllowCredentials(true);
        //匹配前端的请求做跨域请求处理
        source.registerCorsConfiguration("/api/**",corsConfiguration);
        return new CorsWebFilter(source);
    }
}
