package com.qc.boot.dao;

import com.qc.boot.entity.Students;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/8 15:39
 */

public interface StudentsRepository extends MongoRepository<Students,Long> {
    /** 注意自定义方法的语法和结构，只有首字母大写，即为User，后边的name根据自己的定义情况，
     * n小写就小写，不要自己随意发挥
     * By后边跟的就是属性名
     */
    Students findByUsername(String name);
    Page<Students> findAll(Pageable var1);

    /**
     * PS: 关键的关键，就是创建的时候有规则，使用repo是带着镣铐跳舞
     * */

}
