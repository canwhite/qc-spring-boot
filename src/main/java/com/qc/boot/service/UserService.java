package com.qc.boot.service;

import com.qc.boot.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Statement;

/**
 * 功能描述: user service
 *
 * @author lijinhua
 * @date 2022/8/22 15:11
 */
@Component
@Transactional
public class UserService {
    /** slf4j */
    final  Logger logger = LoggerFactory.getLogger(getClass());

    /** 引入template模版方法*/
    @Autowired
    JdbcTemplate jdbcTemplate;


    /** ORM把关系型的表数据映射为java对象
     * 参数是要映射成的Entity
     * */
    RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);


    /** 查  */
    public User getUserById(long id){
        return  jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?",new Object[] {id},userRowMapper);
    }

    public User getUserByEmail(String email){
        return  jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?",new Object[] {email},userRowMapper);
    }

    public User signin(String email,String password){
        logger.info("try login by {}...", email);
        User user = getUserByEmail(email);
        if(user.getPassword().equals(password)){
            return  user;
        }
        throw new RuntimeException("login failed");
    }

    /** 增 */
    public User register(String email,String password,String name){
        logger.info("try register by {}...", email);

        User user = new User();
        //然后一通设置
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());

        /** KeyHolder用来后边拿主键 */
        KeyHolder holder = new GeneratedKeyHolder();
        //看来是返回nums
        if(1 != jdbcTemplate.update((conn)->{
            /** prepareStatement
             * 如果需要返回主键，需要加第二个参数Statement.RETURN_GENERATED_KEYS
             * 占位符也是?
             * */
            var ps = conn.prepareStatement("INSERT INTO users(email,password,name,createdAt) VALUES(?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            //set Object从1开始
            ps.setObject(1, user.getEmail());
            ps.setObject(2, user.getPassword());
            ps.setObject(3, user.getName());
            ps.setObject(4, user.getCreatedAt());
            return ps;
        },holder)){
            throw new RuntimeException("Insert failed.");
        }

        user.setId(holder.getKey().longValue());
        return user;
    }



    /** 改和删 都是用的update，只是语句不同而已
     * update这种情况可以不加new Object，直接在后边跟值
     * 也不需要rowMapper，因为不用返回值
     * return nums
     * */


    public void updateUser(User user){
        if(1 != jdbcTemplate.update("UPDATE users SET name = ? WHERE id=?", user.getName(), user.getId())){
            throw new RuntimeException("User not found by id");
        }
    }

}
