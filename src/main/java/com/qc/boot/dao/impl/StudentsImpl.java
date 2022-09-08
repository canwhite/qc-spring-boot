package com.qc.boot.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.bulk.UpdateRequest;
import com.qc.boot.dao.StudentsDao;
import com.qc.boot.entity.Students;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/8 10:43
 */
@Service
public class StudentsImpl implements StudentsDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** 需要注入mongoTemplate*/
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void saveStudent(Students student) {
        //用save方法，原生方法应该是insert
        mongoTemplate.save(student);
    }

    @Override
    public Students findStudentByName(String name) {
        //这个Query是mongodb给的
        Query query = new Query(Criteria.where("username").is(name));
        Students student = mongoTemplate.findOne(query,Students.class);
        return student;
    }

    /** 查询所有 */
    @Override
    public List<Students> findAll() {
        return mongoTemplate.findAll(Students.class);
    }

    @Override
    public long updateStudent(Students student) {
        //where操作
        Query query = new Query(Criteria.where("id").is(student.getId()));
        //set操作
        Update update = new Update().set("username",student.getUsername())
                .set("password",student.getPassword());
        //拿到结果
        UpdateResult result = mongoTemplate.updateFirst(query,update,Students.class);

        if(result!=null)
            return result.getMatchedCount();
        else
            return 0;
    }

    @Override
    public void deleteStudentById(Long id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,Students.class);
    }



}
