package com.qc.boot.jms;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 功能描述: send
 *
 * @author lijinhua
 * @date 2022/8/30 10:48
 */

/**
 * 发布者：
 * 由具体的业务逻辑触发，这里是注册
 * */
@Component
public class MessagingService {


    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMailMessage(MailMessage msg) throws Exception {
        //String text = objectMapper.writeValueAsString(msg);
        String text = JSON.toJSONString(msg);
        jmsTemplate.send("jms/queue/mail", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(text);
            }
        });
    }
}
