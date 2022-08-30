package com.qc.boot.amqp.messaging;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/8/30 17:57
 */
public class RegistrationMessage extends AbstractMessage{

    public static RegistrationMessage of(String email, String name) {
        var msg = new RegistrationMessage();
        msg.email = email;
        msg.name = name;
        msg.timestamp = System.currentTimeMillis();
        return msg;
    }

    @Override
    public String toString() {
        return String.format("[RegistrationMessage: email=%s, name=%s, timestamp=%s]", email, name, timestamp);
    }
}
