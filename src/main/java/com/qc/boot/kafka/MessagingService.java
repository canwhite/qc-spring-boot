package com.qc.boot.kafka;

import com.alibaba.fastjson.JSON;
import com.qc.boot.kafka.messaging.LoginMessage;
import com.qc.boot.kafka.messaging.RegistrationMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/8/31 11:10
 */
@Component
public class MessagingService {
    @Autowired /**是个泛型类 */
    KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 发送消息时，需指定Topic名称，消息正文。
     *
     * 然后在send方法中：
     * 这里我们没有使用MessageConverter来转换JavaBean，
     * 而是直接把消息类型作为Header添加到消息中，Header名称为type，值为Class全名。
     *
     * 消息正文是序列化的JSON
     *
     * */

    public  void  sendRegistrationMessage(RegistrationMessage msg) throws IOException{
        send("topic_registration", msg);
    }

    public  void  sendLoginMessage(LoginMessage msg) throws  IOException{
        send("topic_login", msg);
    }

    private  void send(String topic,Object msg) throws  IOException{
        /** 消息正文放序列化的json */
        ProducerRecord<String, String> pr = new ProducerRecord<>(topic, JSON.toJSONString(msg));
        /** 消息类型放入header ,主要是为了方便后期重写转为javabean*/
        pr.headers().add("type",msg.getClass().getName().getBytes(StandardCharsets.UTF_8));
        /** 发送消息 */
        kafkaTemplate.send(pr);
    }
}
