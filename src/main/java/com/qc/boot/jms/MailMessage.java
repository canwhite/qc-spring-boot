package com.qc.boot.jms;

/**
 * 功能描述: entity for jms
 *
 * @author lijinhua
 * @date 2022/8/30 10:36
 */
public class MailMessage {

    //静态enum
    public static enum  Type{
        REGISTRATION, SIGNIN;
    }

    public Type type;
    public String email;
    public String name;
    public long timestasmp;

    //再实现一个静态方法
    public  static  MailMessage registration(String email,String name){
        var msg = new MailMessage();
        msg.email = email;
        msg.name = name;
        msg.type = Type.REGISTRATION;
        msg.timestasmp = System.currentTimeMillis();
        return  msg;
    }
}
