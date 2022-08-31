package com.qc.boot.kafka;

import com.alibaba.fastjson.JSON;
import com.qc.boot.kafka.messaging.LoginMessage;
import com.qc.boot.kafka.messaging.RegistrationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/8/31 11:10
 */
@Component
public class TopicMessageListener {
    /**
     *  接收消息可以使用@KafkaListener注解
     *
     *  如果有分组监听，每组都分到全部信息，像是一个cold observable
     *  如果多个同组名监听，每组分到部分信息，合并为整体，像是一个hot observable
     * */
    private final Logger logger  = LoggerFactory.getLogger(getClass());


    @KafkaListener(topics = "topic_registration" ,groupId =  "group1")
    //使用@Payload表示传入的是消息正文，使用@Header可传入消息的指定Header
    public void onRegistrationMessage(@Payload String message, @Header("type") String type) throws Exception{
        RegistrationMessage msg = JSON.parseObject(message,getType(type));
        logger.info("received registration message: {}", msg);
    }

    @KafkaListener(topics = "topic_login", groupId = "group1")
    public void onLoginMessage(@Payload String message, @Header("type") String type) throws Exception {
        LoginMessage msg = JSON.parseObject(message,getType(type));
        logger.info("received login message: {}", msg);
    }

    @KafkaListener(topics = "topic_login", groupId = "group2")
    public void processLoginMessage(@Payload String message, @Header("type") String type) throws Exception {
        LoginMessage msg = JSON.parseObject(message,getType(type));
        logger.info("process login message: {}", msg);
    }

    /** 需要一个解析类名的工具 */
    @SuppressWarnings("unchecked")
    private static <T> Class<T> getType(String type){
        //todo,use cache
        try {
            //从名字获取class
            return (Class<T>) Class.forName(type);
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

}
