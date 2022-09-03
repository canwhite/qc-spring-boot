package com.qc.boot.dao;

import com.qc.boot.entity.User;

import java.util.List;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/1 15:27
 */
//如果使用dao，dao就相当于一个viewModal层了，service层就是一个简单套接
public interface UserDao {
    User register(String email,String password,String name);
    User getUserById(long id);
    User getUserByEmail(String email);
    List<User> getUesrs();
    int updateUser(User user);

}


