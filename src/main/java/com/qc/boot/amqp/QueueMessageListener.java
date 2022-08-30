package com.qc.boot.amqp;

import com.qc.boot.amqp.messaging.LoginMessage;
import com.qc.boot.amqp.messaging.RegistrationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/8/30 17:53
 */
@Component
public class QueueMessageListener {
    /** 针对两个exchange，是两组状态 ，要分组单独去写*/
    private  final Logger logger = LoggerFactory.getLogger(getClass());

    static final String QUEUE_MAIL = "q_mail";
    static final String QUEUE_SMS = "q_sms";
    static final String QUEUE_APP = "q_app";


    /** -------对应 exchange registration -------*/
    @RabbitListener(queues = QUEUE_MAIL)
    public void  onRegistrationMessageFromMailQueue(RegistrationMessage message) throws  Exception{
        logger.info("queue {} received registration message: {}", QUEUE_MAIL, message);
    }

    @RabbitListener(queues = QUEUE_SMS)
    public void onRegistrationMessageFromSmsQueue(RegistrationMessage message) throws Exception {
        logger.info("queue {} received registration message: {}", QUEUE_SMS, message);
    }


    /** ------对应 exchange login --------*/
    @RabbitListener(queues = QUEUE_MAIL)
    public void onLoginMessageFromMailQueue(LoginMessage message) throws Exception {
        logger.info("queue {} received message: {}", QUEUE_MAIL, message);
    }

    @RabbitListener(queues = QUEUE_SMS)
    public void onLoginMessageFromSmsQueue(LoginMessage message) throws Exception {
        logger.info("queue {} received message: {}", QUEUE_SMS, message);
    }

    @RabbitListener(queues = QUEUE_APP)
    public void onLoginMessageFromAppQueue(LoginMessage message) throws Exception {
        logger.info("queue {} received message: {}", QUEUE_APP, message);
    }

}
