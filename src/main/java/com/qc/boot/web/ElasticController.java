package com.qc.boot.web;

import com.qc.boot.elastic.StudentsDao;
import com.qc.boot.entity.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/7 16:06
 */
@RestController
@RequestMapping("/elastic")
public class ElasticController {
    @Autowired
    StudentsDao studentsDao;

    @GetMapping("/testInsert")
    public  Map<String, String> testInsert(){
        Students user = new Students();
        user.setUId("1");
        user.setName("zhangsna");
        user.setAge(101);
        user.setAddress("广东省深圳市");
        studentsDao.save(user);

        user.setUId("2");
        user.setName("lisi");
        user.setAge(32);
        user.setAddress("广东省深圳市");
        studentsDao.save(user);

        user.setUId("3");
        user.setName("wangwu");
        user.setAge(34);
        user.setAddress("广东省深圳市");
        studentsDao.save(user);

        return Map.of("msg","success");
    }

    @GetMapping("/testDelete")
    public Map<String, String> testDelete() {
        Students user = new Students();
        user.setUId("1");
        studentsDao.delete(user);
        return Map.of("msg","success");
    }

    @GetMapping("/testGetAll")
    public List<Students> testGetAll() {
        List<Students> list = new ArrayList<>();
        Iterable<Students> iterable = studentsDao.getAll();
        iterable.forEach(e -> list.add(e));
        return list;
    }




    



}
