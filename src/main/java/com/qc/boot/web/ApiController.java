package com.qc.boot.web;

import com.qc.boot.entity.User;
import com.qc.boot.service.UserMappperService;
// import io.micrometer.core.instrument.step.StepCounter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

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

    
    // @Autowired
    // UserService userService;

    // @Autowired
    // private UsersMapper usersMapper;

    /* 基于dao重新封装了一层，慢慢替换上边的mapper */
    @Autowired
    UserMappperService userMappperService;

    // private final Logger logger = LoggerFactory.getLogger(getClass());


    /** --mp查 */
    @GetMapping("/users")
    public List<User> users(){
        /** 这里改为mp自具备接口*/
        return  userMappperService.getUsers();
    }

    /** --mp查，用get方法通过email查*/
    @GetMapping("/getUserByEmail")
    public User getUserByEmail(@RequestParam Map<String,Object> params){
        for (Map.Entry<String,Object> entry: params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            /** 构造条件语句*/
            if(key.equals("email")){
                return userMappperService.getUserByEmail(value.toString());
            }
        }
        return null;
    }

    /** --mp增
     * @RequestParam 针对的是key=Value?key=value传参的模式
     * formData和json传过来的值可以用@RequestBody处理
     * */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestParam Map<String, String> params){
        //尝试一下
        String email ="";
        String password = "";
        String name = "";

        //通过forin拿到对应entry,java也有entries
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
        User user =  userMappperService.rigister(email, password, name);
        return  Map.of("user",user);
    }

    /** 删，根据email拿到id去删除,直接根据id的不用写了*/
    @PostMapping("deleteUserByEmail")
    public Map<String, Object> deleteUserByEmail(@RequestParam Map<String,Object> params){
        userMappperService.deleteUserByMap(params);
        return Map.of("code",200,"msg","success");
    }



    /** 更新，通过email去更新，在这里可以试一下lambda */
    @GetMapping("/updateUser")
    public Map<String, Object> updateUser(@RequestParam Map<String,Object> params){
        User user = userMappperService.getUserByMap(params);
        if(user != null){
            User nUser = userMappperService.updateUser(user, "zack");
            return Map.of("user",nUser);
        }else{
            return Map.of("msg","update error");
        }
    }


    /** 根据路径id去请求 */
    /** openApi的注释参数
     * @Operation对api进行注释
     * @Parameter对参数进行注释，往参数中直接注入
     * */
    @Operation(summary = "Get specific user object by it's id.")
    @GetMapping("/users/{id}")
    public  User user(@Parameter(description = "id of the user.") @PathVariable("id") long id){
        return  userMappperService.getUserById(id);
    }

    
    /** 基于json去请求的
     * curl ： 
     * curl -H 'Content-Type:application/json'  -d '{"email":"886@qq.com","password":"715705qc"}' http://127.0.0.1:8080/api/signin
     */
    @PostMapping("/signin")
    public Map<String,Object> signin(@RequestBody SignInRequest signInRequest){
        try{
            // User user = userService.signin(signInRequest.email,signInRequest.password);
            User user = userMappperService.login(signInRequest.email, signInRequest.password);
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
