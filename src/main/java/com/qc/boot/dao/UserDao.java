package com.qc.boot.dao;

import com.qc.boot.entity.User;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/1 15:27
 */
//如果使用dao，dao就相当于一个viewModal层了，service层就是一个简单套接
public interface UserDao {
    /** register */
    User register(String email,String password,String name);

    /** login */




}


