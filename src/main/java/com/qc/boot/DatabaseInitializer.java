package com.qc.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 功能描述: database init
 *
 * @author lijinhua
 * @date 2022/8/22 15:51
 */
@Component
public class DatabaseInitializer {
    @Autowired
    JdbcTemplate jdbcTemplate;


    @PostConstruct
    public void init() {
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS users (" //
                + "id BIGINT AUTO_INCREMENT NOT NULL, "
                + "email VARCHAR(100) NOT NULL, "
                + "password VARCHAR(100) NOT NULL, "
                + "name VARCHAR(100) NOT NULL, "
                + "createdAt BIGINT NOT NULL, "
                + "PRIMARY KEY (id),"
                + "UNIQUE KEY (email))"
                + "ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;"
        ) ;
    }
}
