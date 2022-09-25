package com.qc.boot.dao;

import java.util.List;
import java.util.Map;

import com.qc.boot.entity.User;


/**
 * 注意mp这一套的顺序
 * 从原子化的dao =>到原子组合的service =>面向客户的controller
 */

/**
 * Dao层只做基本的增删改查
 * 这些数据库的原子操作
 * 然后service的时将原子组合组成业务
 * mp的语法从语义层面理解就能记住
 * 增删改查
 * insert、delete、update、select
 * 这一点和传统意义上的jdbc还是有区别
 * jdbc
 * 增删改全部算作update
 * 查是query
 * 又分为查所有的query和根据数据查的queryForObject
 */

public interface UserMapperDao {
    public User getUserById(long id);
    public User getUserByEmail(String email);
    public List<User> getUsers();
    //signIn这个算作原子组合，不应该出现在Dao层，而应该出现在服务层
    //public User signin(String email,String password); 
    public User register(String email,String password,String name);
    public User updateUser(User user,String name);
    public void deleteUserByMap(Map<String,Object> map);
    public List<User> selectUserByMap(Map<String,Object>map);
    
}
