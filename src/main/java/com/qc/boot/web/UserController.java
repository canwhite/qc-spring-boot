package com.qc.boot.web;

import com.alibaba.fastjson.JSON;
import com.qc.boot.amqp.AmqpMessagingService;
import com.qc.boot.amqp.messaging.LoginMessage;
import com.qc.boot.amqp.messaging.RegistrationMessage;
import com.qc.boot.config.annotation.RoutingWithSlave;
import com.qc.boot.entity.User;
import com.qc.boot.jms.MailMessage;
import com.qc.boot.jms.MessagingService;
import com.qc.boot.redis.RedisService;
import com.qc.boot.service.StorageService;
import com.qc.boot.service.UserService;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述: user controller
 *
 * @author lijinhua
 * @date 2022/8/22 15:12
 */
@Controller
public class UserController {

    public static final String KEY_USER_ID = "__userid__";

    public static final String KEY_USER = "__user__";

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    /** 注释一下jms */
    /**
    @Autowired
    MessagingService messagingService;
     */

    @Autowired
    AmqpMessagingService amqpMessagingService;


    private void putUserIntoRedis(User user) throws Exception {
        /** hset 三个参数key，sub-key，value，将user转化为json字符串存起来
         *  原来是jackson(writeValueAsString)，现在改为fastjson(toJSONString)
         *  说实在的更喜欢fastjson的语法习惯
         * */
        //redisService.hset(KEY_USER, user.getId().toString(), objectMapper.writeValueAsString(user));
        redisService.hset(KEY_USER,user.getId().toString(),JSON.toJSONString(user));

    }

    private User getUserFromRedis(HttpSession session) throws Exception {
        /** 换成取id获取，判断session */
        Long id = (Long) session.getAttribute(KEY_USER_ID);//
        if (id != null) {
            String s = redisService.hget(KEY_USER, id.toString());
            if (s != null) {
                /** jackson将字符串转化为对象*/
                //return objectMapper.readValue(s, User.class);
                /** 这里换成用fastJson将字符串转化为对象*/
                return  JSON.parseObject(s,User.class);
            }
        }
        return null;
    }


    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleUnknowException(Exception ex) {
        return new ModelAndView("500.html", Map.of("error", ex.getClass().getSimpleName(), "message", ex.getMessage()));
    }

    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        Map<String, Object> model = new HashMap<>();
        if (user != null) {
            model.put("user", model);
        }
        return new ModelAndView("index.html", model);
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register.html");
    }

    @PostMapping("/register")
    public ModelAndView doRegister(@RequestParam("email") String email, @RequestParam("password") String password,
                                   @RequestParam("name") String name) {
        try {
            User user = userService.register(email, password, name);
            logger.info("user registered: {}", user.getEmail());
            //messagingService.sendMailMessage(MailMessage.registration(user.getEmail(),user.getName()));
            amqpMessagingService.sendRegistrationMessage(RegistrationMessage.of(user.getEmail(), user.getName()));

        } catch (RuntimeException e) {
            return new ModelAndView("register.html", Map.of("email", email, "error", "Register failed"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/signin");
    }

    @GetMapping("/signin")
    public ModelAndView signin(HttpSession session) throws Exception{
        //User user = (User) session.getAttribute(KEY_USER);
        /** 改为从redis获取user*/
        User user = getUserFromRedis(session);
        //加一个redis操作
        if (user != null) {
            return new ModelAndView("redirect:/profile");
        }
        return new ModelAndView("signin.html");
    }


    /** signin前端发起的操作发生在这里，然后是路由的控制在上边
     *  只有这一个地方是set的，其他地方都是get数据用来操作
     * */
    @PostMapping("/signin")
    public ModelAndView doSignin(@RequestParam("email") String email, @RequestParam("password") String password,
                                 HttpSession session) throws Exception{
        try {
            User user = userService.signin(email, password);
            amqpMessagingService.sendLoginMessage(LoginMessage.of(user.getEmail(),user.getName(),true));
            /** session，Set改为宏id*/
            session.setAttribute(KEY_USER_ID, user.getId());
            /** redis set*/
            putUserIntoRedis(user);
        } catch (RuntimeException e) {
            amqpMessagingService.sendLoginMessage(LoginMessage.of(email, "(unknown)", false));
            return new ModelAndView("signin.html", Map.of("email", email, "error", "Signin failed"));
        }
        return new ModelAndView("redirect:/profile");
    }

    @GetMapping("/profile")
    //@RoutingWithSlave //切换database
    public ModelAndView profile(HttpSession session) throws Exception {
        //User user = (User) session.getAttribute(KEY_USER);
        /** 改为从redis获取user */
        User user = getUserFromRedis(session);
        if (user == null) {
            return new ModelAndView("redirect:/signin");
        }
        // 测试是否走slave数据库:
        user = userService.getUserByEmail(user.getEmail());
        return new ModelAndView("profile.html", Map.of("user", user));
    }

    @GetMapping("/signout")
    public String signout(HttpSession session) {
        session.removeAttribute(KEY_USER_ID);
        return "redirect:/signin";
    }

    @GetMapping("/resetPassword")
    public ModelAndView resetPassword() {
        throw new UnsupportedOperationException("Not supported yet!");
    }
}
