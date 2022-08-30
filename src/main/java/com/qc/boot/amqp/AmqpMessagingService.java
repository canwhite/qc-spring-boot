package com.qc.boot.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/8/30 17:53
 */
@Component
public class AmqpMessagingService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //第一个参数是exchange





}
