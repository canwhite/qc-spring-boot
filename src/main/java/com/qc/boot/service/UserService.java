package com.qc.boot.service;

import com.qc.boot.dao.impl.UserDaoImpl;
import com.qc.boot.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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

    @Autowired
    UserDaoImpl userDao;


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
        return  userDao.getUserById(id);
    }

    public User getUserByEmail(String email){
        return  userDao.getUserByEmail(email);
    }

    //获取所有users
    public List<User> getUesrs(){
        return  userDao.getUesrs();
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
        return  userDao.register(email,password,name);
    }


    /** 改和删 都是用的update，只是语句不同而已
     * update这种情况可以不加new Object，直接在后边跟值
     * 也不需要rowMapper，因为不用返回值
     * return nums
     * */

    public void updateUser(User user){
        if(1 != userDao.updateUser(user)){
            throw new RuntimeException("User not found by id");
        }
    }

}
