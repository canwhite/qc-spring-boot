package com.qc.boot.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * 功能描述: config
 *
 * @author lijinhua
 * @date 2022/8/29 16:48
 */
@ConfigurationProperties("spring.redis")
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * @Component和@Bean
 * @Bean主要为三方工具创建bean，而且必须设置在配置类里
 *
 * */
public class RedisConfiguration {
    private String host;
    private int port;
    private String password;
    private int database;

    @Bean
    RedisClient redisClient() {
        RedisURI uri = RedisURI.Builder.redis(this.host, this.port).withPassword(this.password)
                .withDatabase(this.database).build();
        return RedisClient.create(uri);
    }

}
