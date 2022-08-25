package com.qc.boot;

import com.qc.boot.config.MasterDataSourceConfiguration;
import com.qc.boot.config.RoutingDataSourceConfiguration;
import com.qc.boot.config.SlaveDataSourceConfiguration;
import com.qc.boot.interceptor.BlacklistInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 功能描述: application
 *
 * @author lijinhua
 * @date 2022/8/22 14:57
 */
@RestController
/** 因为这里我们要自己创建datasource，所以我们先对这项禁用，然后再自己引入 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class) //上一行已经包含该注解了，所以不用单独加
//注入自己实现
@Import({MasterDataSourceConfiguration.class, SlaveDataSourceConfiguration.class, RoutingDataSourceConfiguration.class})

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