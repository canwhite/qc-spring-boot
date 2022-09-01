package com.qc.boot.service;

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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

    /** 引入统一配置文件 */
    @Autowired
    StorageConfiguration storageConfig;



    /** ORM把关系型的表数据映射为java对象
     * 参数是要映射成的Entity
     * */
    RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);


    //然后我们在这里加一个初始化方法
    @PostConstruct
    public  void  init(){
        logger.info("Load configuration: root-dir = {}", storageConfig.getRootDir());
        logger.info("Load configuration: max-size = {}", storageConfig.getMaxSize());
        logger.info("Load configuration: allowed-types = {}", storageConfig.getAllowTypes());
        /**
         * 启动之后我们能看到输出
         * 2022-08-24 11:02:36.808  INFO 47514 --- [  restartedMain] com.qc.boot.service.UserService          : Load configuration: root-dir = /var/storage
         * 2022-08-24 11:02:36.808  INFO 47514 --- [  restartedMain] com.qc.boot.service.UserService          : Load configuration: max-size = 102400
         * 2022-08-24 11:02:36.808  INFO 47514 --- [  restartedMain] com.qc.boot.service.UserService          : Load configuration: allowed-types = [jpg, png, gif]
         * */
    }





    /** 查  */
    public User getUserById(long id){
        return  jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?",new Object[] {id},userRowMapper);
    }

    public User getUserByEmail(String email){
        return  jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?",new Object[] {email},userRowMapper);
    }

    //获取所有users
    public List<User> getUesrs(){
        return  jdbcTemplate.query("SELECT * FROM users",userRowMapper);
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


        /** prepareStatement
         * 如果需要返回主键，需要加第二个参数Statement.RETURN_GENERATED_KEYS
         * 占位符也是?
         * */
        /**
        //看来是返回nums
        jdbcTemplate.update((conn)->{
            var ps = conn.prepareStatement("INSERT INTO users(email,password,name,createdAt) VALUES(?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            //set Object从1开始
            ps.setObject(1, user.getEmail());
            ps.setObject(2, user.getPassword());
            ps.setObject(3, user.getName());
            ps.setObject(4, user.getCreatedAt());
            logger.info("----------ps:{}",ps);
            return ps;
        },holder);
         */

        String sql = "INSERT INTO users(email,password,name,createdAt) VALUES(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
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

        user.setId(keyHolder.getKey().longValue());
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
