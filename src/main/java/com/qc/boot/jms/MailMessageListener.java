package com.qc.boot.jms;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * 功能描述: listener
 *
 * @author lijinhua
 * @date 2022/8/30 10:46
 */
@Component
public class MailMessageListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 订阅者
     * 订阅到消息后触发最终执行人：MailService
     * 让其发送邮件
     * */


    @Autowired
    MailService mailService;

    @JmsListener(destination = "jms/queue/mail", concurrency = "10")
    public void onMailMessageReceived(Message message) throws Exception {
        logger.info("received message: " + message);
        if (message instanceof TextMessage) {
            String text = ((TextMessage) message).getText();
            //MailMessage mm = objectMapper.readValue(text, MailMessage.class);
            MailMessage mm = JSON.parseObject(text,MailMessage.class);
            mailService.sendRegistrationMail(mm);
        } else {
            logger.error("unable to process non-text message!");
        }
    }

}
