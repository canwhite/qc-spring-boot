package com.qc.boot.dao.impl;

import com.qc.boot.dao.UserDao;
import com.qc.boot.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/3 09:49
 */

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    //直接LoggerFactory打头
    final  Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public User register(String email, String password, String name) {
        //注册操作
        User user = new User();
        //然后一通设置
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());

        String sql = "INSERT INTO users(email,password,name,createdAt) VALUES(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int num =  jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                // 指定主键
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setObject(1, user.getEmail());
                preparedStatement.setObject(2, user.getPassword());
                preparedStatement.setObject(3, user.getName());
                preparedStatement.setObject(4, user.getCreatedAt());
                return preparedStatement;
            }
        }, keyHolder);

        if(num != 1){
            throw new RuntimeException("register failed");
        }
        logger.info("num" + num);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }
}