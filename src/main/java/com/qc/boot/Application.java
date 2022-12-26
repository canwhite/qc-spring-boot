package com.qc.boot;

import com.qc.boot.interceptor.BlacklistInterceptor;
import com.qc.boot.redis.RedisConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 功能描述: application
 *
 * @author lijinhua
 * @date 2022/8/22 14:57
 */
@RestController
/** 因为这里我们要自己创建datasource，所以我们先对这项禁用，然后再自己引入 */
@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class) //上一行已经包含该注解了，所以不用单独加
//注入自己实现
@Import({ RedisConfiguration.class})
@MapperScan("com.qc.boot.mapper")
public class Application implements WebMvcConfigurer{


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
    // -- Mvc configuration ---------------------------------------------------

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

     @Override
     public void addInterceptors(InterceptorRegistry registry) {
        /**添加interceptor */
        registry.addInterceptor(new BlacklistInterceptor()).addPathPatterns("/api/*");
     }

}