package com.qc.boot.amqp;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述: configration
 *
 * @author lijinhua
 * @date 2022/8/30 17:49
 */
public class RabbitMQConfigration {
    @Bean
    MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();//看这名字怕不是植入了jackson
    }
}
