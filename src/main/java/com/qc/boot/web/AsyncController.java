package com.qc.boot.web;

// import aj.org.objectweb.asm.TypeReference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
// import com.fasterxml.jackson.annotation.JsonAlias;
import com.qc.boot.entity.CardItem;
import com.qc.boot.entity.CardsResult;
import com.qc.boot.entity.ImageItem;
import com.qc.boot.entity.User;
import com.qc.boot.service.UserService;
import com.qc.boot.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import java.io.IOException;
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

/**
 * fastjson使用心得
 *
 * parseArray得到的是JSONArray，将它转化为List<xxx>需要自己去遍历把JSONObject转化为对应Entity再add
 *
 * parseObject得到JSONObject之后再get拿到的都是Object，是Java通用类型，
 * 如果有数组还需要反向转换，只要注意类型的变化就可以了
 *
 *
 * */
public class AsyncController {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserService userService;

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


    /** 我们只是返回Callable，然后boot帮忙完成的操作就有submit和get两步骤，最终直接返回了json结果 */
    @GetMapping("/images")
    public Callable<List<ImageItem>> images(){
        return  () -> {
            String result = HttpUtils.get("https://picsum.photos/v2/list");
            /** 这里是直接对对象数组进行操作，先将json字符串转化为JSONArray，然后遍历item转换*/
            var arr = JSON.parseArray(result);
            List<ImageItem> images = new ArrayList<>();
            for (var item : arr){
                images.add(JSON.parseObject(
                        JSON.toJSONString(item),//将遍历的JSONObject再转化为JSON字符串，以便parseObject的使用
                        ImageItem.class)//entity
                );
            }
            return  images;
        };
    }




    @GetMapping("/cards")
    public  Callable<CardsResult> cards(){

        return  ()->{
            /** 对于相对复杂的json，我们可以按需取用再封装 */
            /** 下边这个是标准的json字符串*/
            String s = "{\"age\":18,\"cards\":[{\"cardName\":\"招商\",\"cardTime\":\"2021-09-09 20:12:26\"},{\"cardName\":\"浦发\",\"cardTime\":\"2021-09-09 20:12:26\"}],\"name\":\"兵长\"}";
            JSONObject object = JSON.parseObject(s);
            //这样获取的都是对象Object，已经是java对象了,和上边parseArray的还有区别
            var age = object.get("age");
            var name = object.get("name").toString();
            var list = object.get("cards");
            System.out.println("age:"+age);
            System.out.println("name:"+name);
            System.out.println("list"+list); //这里是一个object，不能for循环

            JSONArray nlist = JSON.parseArray(JSON.toJSONString(list));
            CardsResult result = new CardsResult();
            List<CardItem> cards = new ArrayList<>();

            //对象数组，先循环输出一下现在的是什么
            for (var item: nlist
                 ) {
                cards.add(JSON.parseObject(JSON.toJSONString(item),CardItem.class));
            }

            result.setCards(cards);
            result.setMsg("success");
            result.setCode(200);
            //将obejct转化为long，转成string作为中介
            //这里有一个装箱的过程valueOf,可以将Of看作是装箱标志，里边的，xxxValue是拆箱的
            result.setAge(Long.valueOf(age.toString()));
            result.setName(name.toString());

            return  result;
        };
    }






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
