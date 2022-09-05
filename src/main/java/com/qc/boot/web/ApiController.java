package com.qc.boot.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qc.boot.entity.User;
import com.qc.boot.mapper.UsersMapper;
import com.qc.boot.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private UsersMapper usersMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    /** --mp查 */
    @GetMapping("/users")
    public List<User> users(){
        /** 这里改为mp自具备接口*/
        return  usersMapper.selectList(null);
    }

    /** --mp查，用get方法通过email查*/
    @GetMapping("/getUserByEmail")
    public User getUserByEmail(@RequestParam Map<String,Object> params){
        System.out.println("params" + params);
        //构造查询wrapper，查询对应数据
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //从params里拿到email,然后构造条件语句
        for (Map.Entry<String,Object> entry: params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            /** 构造条件语句*/
            if(key.equals("email")){
                wrapper.eq("email",value);
            }
        }
        //理论上我们只能查出来一个值
        return usersMapper.selectOne(wrapper);
    }

    /** --mp增
     * @RequestParam 针对的是key=Value?key=value传参的模式
     * formData和json传过来的值可以用@RequestBody处理
     * */
    @PostMapping("/register")
    public Map<String, String> register(@RequestParam Map<String, String> params){
        //尝试一下
        System.out.println("--params--" + params);
        String email ="";
        String password = "";
        String name = "";

        //通过forin拿到对应entry
        for (Map.Entry<String,String> entry: params.entrySet()) {
            String key = entry.getKey();//从entry里getKey
            String value = entry.getValue();//从entry里getValue
            //给单个赋值
            if(key.equals("email")){
                email = value;
            }
            if(key.equals("password")){
                password = value;
            }
            if(key.equals("name")){
                name = value;
            }
        }

        //通过insert新增用户
        User user = new User();
        //然后一通设置
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());

        int insert =  usersMapper.insert(user);
        if(insert != 1){
            throw new RuntimeException("register failed");
        }
        //Map.of这样很好用啊
        //同样的有时候为了方便我们可以使用
        //List.of("apple", "pear", "banana");
        return  Map.of("email",email,"name",name);
    }

    /** 删，根据email去删除*/



    /** 更新，通过email去更新 */







    /** 根据路径id去请求 */
    /** openApi的注释参数
     * @Operation对api进行注释
     * @Parameter对参数进行注释，往参数中直接注入
     * */
    @Operation(summary = "Get specific user object by it's id.")
    @GetMapping("/users/{id}")
    public  User user(@Parameter(description = "id of the user.") @PathVariable("id") long id){
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

    public static class RegisterRequest{
        //User user = userService.register(email, password, name);
        public String email;
        public String password;
        public String name;
    }

}
