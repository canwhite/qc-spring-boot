package com.qc.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qc.boot.dao.UserMapperDao;
import com.qc.boot.entity.User;

@Component
@Transactional
/** 
 * 实际上service也可以写成类似dao的模式
 * 只是它知识原子组合没必要写的这般麻烦
 */
public class UserMappperService {
    @Autowired
    UserMapperDao userMapperDao;


    //基于mapper完成一些操作
    public List<User> getUsers(){
        return userMapperDao.getUsers();
    }








}
