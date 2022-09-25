package com.qc.boot.service;

import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.Punycode;
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


    /** 
     * todo
     * 接下来要做的事可能就是把一些操作类的给提到service类
     * 尽量让controller调用更清爽就可以了
     */


    //基于mapper完成一些操作
    public List<User> getUsers(){
        return userMapperDao.getUsers();
    }


    //通过email获取用户
    public User getUserByEmail(String email){
        return userMapperDao.getUserByEmail(email);
    }

    //注册用户
    public User rigister(String email,String password ,String name){
        return userMapperDao.register(email, password, name); 
    }

    //删除用户
    public void deleteUserByMap(Map<String,Object> map){
        //不需要返回
        userMapperDao.deleteUserByMap(map);
    }


    //--登陆,实际上登陆时最接近service本身功能的实现，做原子操作的组合
    public User login(String email,String password){    
        
        User user = getUserByEmail(email);
        //user.getPassword
        if("715705qc".equals(password)){
            return  user;
        }
        throw new RuntimeException("login failed");
    }


    public User updateUser(User user,String name){
        User nu  = userMapperDao.updateUser(user,name);
        return nu;
    }


    public User getUserById(long id){
        return userMapperDao.getUserById(id);
    }

    public User getUserByMap(Map<String,Object> map){
        List<User> users = userMapperDao.selectUserByMap(map);
        if(users.size() > 0){
            return users.get(0);
        }
        return null;
    }
    
}
