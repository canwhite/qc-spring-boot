package com.qc.boot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 功能描述: user entity
 *
 * @author lijinhua
 * @date 2022/8/22 15:09
 */
@TableName("users") //使用mp的时候，如果entity的名字和表名不一致，这里需要as一下
/** 此处最好和表名一致，如果不一致，就用上述方式，写下真实表名 */
public class User {
    private Long id;

    private String email;
    /**  如果密码不能返回，我们对它进行处理 */
    @TableField(select = false) //注解进行排除
    private String password;
    private String name;
    /** mp会自动将驼峰转化为下划线,下边一行是不让它转，当然我们也可以集体配置不转
     *  在yml里配置
     * */
//    @TableField(value = "createdAt")
    private long createdAt; //mp默认下划线处理而不是驼峰，但是我们可以在yml中关闭这个

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedDateTime() {
        return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getImageUrl() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(this.email.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
            return "https://www.gravatar.com/avatar/" + String.format("%032x", new BigInteger(1, hash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("User[id=%s, email=%s, name=%s, password=%s, createdAt=%s, createdDateTime=%s]", getId(),
                getEmail(), getName(), getPassword(), getCreatedAt(), getCreatedDateTime());
    }
}
