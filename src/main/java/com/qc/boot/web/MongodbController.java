package com.qc.boot.web;

import com.qc.boot.dao.StudentsDao;
import com.qc.boot.entity.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/8 10:47
 */

@RestController
@RequestMapping("/mongodb")
public class MongodbController {

    @Autowired
    StudentsDao studentsDao;


    @GetMapping("/saveStudent")
    public Map<String,Object> saveStudent(){
        Students student = new Students();
        student.setId(1L);
        student.setUsername("zhangsan");
        student.setPassword("715705");
        studentsDao.saveStudent(student);
        return  Map.of("msg","success","student",student);
    }

    @GetMapping("/getAll")
    public List<Students> getAll(){
        return  studentsDao.findAll();
    }


    @GetMapping("/getStudentByName")
    public Students getStudentByName(){
         return  studentsDao.findStudentByName("zhangsan");
    }

    //更新
    @GetMapping("/updateStudent")
    public Students updateStudent(){
        Students student = new Students();
        student.setId(1L);
        student.setUsername("zhangsan");
        student.setPassword("715");

        var num =  studentsDao.updateStudent(student);
        System.out.println("num"+num);

        return  student;
    }

    //删除
    @GetMapping("/deleteStudent")
    public Map<String,Object> deleteStudent(){
        studentsDao.deleteStudentById(1L);
        return  Map.of("msg","success");
    }





}
