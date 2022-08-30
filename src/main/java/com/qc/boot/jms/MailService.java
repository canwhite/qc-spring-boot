package com.qc.boot.jms;

/**
 * 功能描述: mail service
 *
 * @author lijinhua
 * @date 2022/8/30 10:50
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** mail service
 *  执行者：接收消息后发送邮件的执行人
 *  是为了接收到监听的时候往外发邮件
 *  这里主要是做一个模拟，它承接的是listener
 *  是接收消息之后最终的执行事件
 * */
@Component
public class MailService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** 发送邮件的方法 */
    public  void  sendRegistrationMail(MailMessage mm){
        logger.info("[send mail] sending registration mail to {}...", mm.email);
        // TODO: simulate a long-time task:
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        logger.info("[send mail] registration mail was sent to {}.", mm.email);

    }
}
