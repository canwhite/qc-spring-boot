package com.qc.boot.amqp;

import com.qc.boot.amqp.messaging.LoginMessage;
import com.qc.boot.amqp.messaging.RegistrationMessage;
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

    /**
     * 第一个参数是exchange,
     * 第二个参数可以理解为sub-key，看作路由
     * 最后一个参数就是msg
     * */
    public void  sendRegistrationMessage(RegistrationMessage msg){
        rabbitTemplate.convertAndSend("registration","",msg);
    }

    public  void  sendLoginMessage(LoginMessage msg){
        String routingKey = msg.success ? "":"login_failed";
        rabbitTemplate.convertAndSend("login", routingKey, msg);
    }

}
