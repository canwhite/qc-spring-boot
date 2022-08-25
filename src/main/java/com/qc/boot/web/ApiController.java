package com.qc.boot.web;

import com.qc.boot.entity.User;
import com.qc.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 功能描述: api
 *
 * @author lijinhua
 * @date 2022/8/25 09:13
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<User> users(){
        return  userService.getUesrs();
    }

    /** 根据路径id去请求 */
    @GetMapping("/users/{id}")
    public  User user(@PathVariable("id") long id){
        return  userService.getUserById(id);
    }

    @PostMapping("/signin")
    public Map<String,Object> signin(@RequestBody SignInRequest signInRequest){
        try{
            User user = userService.signin(signInRequest.email,signInRequest.password);
            return  Map.of("user",user);
        }catch (Exception e){
            return Map.of("error", "SIGNIN_FAILED", "message", e.getMessage());
        }
    }

    /**在动态类里放静态类可以，但是反过来不行
     * 动态类可以push静态类
     * */
    public  static  class  SignInRequest{
        public  String email;
        public  String password;
    }

}
