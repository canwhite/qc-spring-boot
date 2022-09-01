package com.qc.boot.dao.impl;

import com.qc.boot.dao.UserDao;
import com.qc.boot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/1 15:29
 */
@Repository
public class UserDaoJdbcTemplateImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;


    @Override
    public int add(User user) {

        /**
        String sql = "insert into t_author(id,real_name,nick_name) " +
                "values(:id,:realName,:nickName)";
        Map<String, Object> param = new HashMap<>();
        param.put("id",author.getId());
        param.put("realName", author.getRealName());
        param.put("nickName", author.getNickName());

        return (int) jdbcTemplate.update(sql, param);

         */
        return  0;
    }
}

