package com.qc.boot.dao.impl;

import com.qc.boot.dao.UserDao;
import com.qc.boot.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
    /** logger */
    final  Logger logger = LoggerFactory.getLogger(getClass());
    /** ORM把关系型的表数据映射为java对象,参数是要映射成的Entity*/
    RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);


    /** 整体使用概括
     * forObject查的时候，可以选择将orm映射工具放中间，然后后边我们补充查询参数
     * 当然直接query的时候我们用不着用查询参数
     * 其他的增删改都是update
     * 增的时候如果最后需要拿到主键，需要keyHolder和preparedStatement
    */


    @Override
    public User getUserById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?",userRowMapper,id);
    }


    /** 通过email获取user*/
    @Override
    public User getUserByEmail(String email) {
        return  jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?",userRowMapper,email);
    }

    /** 获取所有users*/
    @Override
    public List<User> getUesrs() {
        /** 查询所有的用query，queryForObject是针对有参数的情况来说的*/
        return  jdbcTemplate.query("SELECT * FROM users",userRowMapper);
    }


    /** 注册，增删改都用update */
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

        //当时报错，最后网上找的mysql版本，刚开始写密码一直报错，可能是密码难度有问题没有设置成功，最后换了比较难的一种
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


    @Override
    public int updateUser(User user) {
        //updte会返回更新的数字
        return jdbcTemplate.update("UPDATE users SET name = ? WHERE id=?",user.getName(),user.getId());
    }




}