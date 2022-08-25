package com.qc.boot.web;

import com.qc.boot.entity.User;
import com.qc.boot.service.UserService;
import com.qc.boot.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 功能描述: async
 *
 * @author lijinhua
 * @date 2022/8/25 15:09
 */
@RestController
@RequestMapping("/async")
public class AsyncController {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserService userService;

    @GetMapping("/users")
    /**
    当我们提交一个Callable任务后，我们会同时获得一个Future对象
    任务的提交和future的get方法spring已经帮我们做了
    Callable是个接口，我们实现Callable任务也是继承这个接口
    当然也可以采用返回值确定+lambda的形式实现这个接口
    */
    public Callable<List<User>> users() {
        return () -> {
            try {

                /**
                 * //这里也可以发起请求，将请求结果返回
                 * //json转化的时候利用一下jackson包就可以
                 * //从请求结果读取主要用readValue
                 *
                 * ObjectMapper mapper = new ObjectMapper();
                 * // java对象转换为json字符换
                 * String Json =  mapper.writeValueAsString(student1);
                 * // json字符串转换为java对象
                 * Student student2 = mapper.readValue(Json, Student.class);
                 *
                 * */


                /**
                {
                    "id": "0",
                        "author": "Alejandro Escamilla",
                        "width": 5616,
                        "height": 3744,
                        "url": "https://unsplash.com/photos/yC-Yzbqy7PY",
                        "download_url": "https://picsum.photos/id/0/5616/3744"
                }
                 */
                String result = HttpUtils.get("https://picsum.photos/v2/list");
                logger.info("----------"+result + "--------------");


                Thread.sleep(3000);
                //只要callable返回
                return userService.getUesrs();

            } catch (InterruptedException e) {
                //如果报错就返回为空
                List<User> users = new ArrayList<>();
                return  users;
            }
        };
    }
}
