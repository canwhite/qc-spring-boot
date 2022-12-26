package com.qc.boot.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
// import com.mysql.cj.Query;
import com.qc.boot.dao.UserMapperDao;
import com.qc.boot.entity.User;
import com.qc.boot.mapper.UsersMapper;


@Repository
public class UserMapperDaoImpl implements UserMapperDao{
    
    @Autowired
    UsersMapper usersMapper;

    /**
     * 查询所有和查询部分
     * selectList
     * selectOne
     */
    @Override
    public List<User> getUsers() {
        // selectList查询所有
        return  usersMapper.selectList(null); 
    }

    @Override
    public User getUserByEmail(String email) {
        // selectOne查询一个
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        return usersMapper.selectOne(wrapper);
    }

    @Override
    public User getUserById(long id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        return usersMapper.selectOne(wrapper);
    }

    /** 增：insert */
    @Override
    public User register(String email, String password, String name) {
        // insert
        User user = new User();
        user.setCreatedAt(System.currentTimeMillis());
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        int insert =  usersMapper.insert(user);
        if(insert != 1){
            throw new RuntimeException("register failed");
        }
        return user;
    }

    /** 改： update*/
    @Override
    public User updateUser(User user,String name) {
        //update
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email", user.getEmail());
        user.setName(name);
        int num =  usersMapper.update(user, updateWrapper);
        if(num != 1){
            throw  new RuntimeException("update error");
        }
        return user;
    }
    /** 删：delete or deleteXXX*/
    @Override
    public void deleteUserByMap(Map<String, Object> map) {
        /** 引入试了一下有返回值 */
        int num =  usersMapper.deleteByMap(map);
        if(num != 1){
            throw  new RuntimeException("update error");
        }
    }

    @Override
    public List<User> selectUserByMap(Map<String, Object> map) {
        return usersMapper.selectByMap(map);
    }

}
