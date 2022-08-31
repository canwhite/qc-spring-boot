package com.qc.boot.kafka.messaging;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/8/31 11:12
 */
public class LoginMessage extends AbstractMessage{
    public boolean success;

    public static LoginMessage of(String email, String name, boolean success) {
        var msg = new LoginMessage();
        msg.email = email;
        msg.name = name;
        msg.success = success;
        msg.timestamp = System.currentTimeMillis();
        return msg;
    }

    @Override
    public String toString() {
        return String.format("[LoginMessage: email=%s, name=%s, success=%s, timestamp=%s]", email, name, success,
                timestamp);
    }
}
