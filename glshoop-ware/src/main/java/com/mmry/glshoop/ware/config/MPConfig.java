package com.mmry.glshoop.ware.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : mmry
 * @date : 2023/12/20 14:59
 */
@Configuration
public class MPConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor interceptor = new PaginationInterceptor();
        //请求超过最大页后操作，true：返回首页 false:继续请求
        interceptor.setOverflow(false);
        //设置访问最大单页设置 默认为500，-1不限制
        interceptor.setLimit(1000);
        return interceptor;
    }
}
