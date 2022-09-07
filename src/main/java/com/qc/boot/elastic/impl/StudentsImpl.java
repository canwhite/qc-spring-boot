package com.qc.boot.elastic.impl;

import com.qc.boot.elastic.StudentsDao;
import com.qc.boot.elastic.StudentsRepository;
import com.qc.boot.entity.Students;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/7 15:41
 */
@Service
public class StudentsImpl implements StudentsDao{

    @Autowired
    private StudentsRepository studentsRepository;


    @Override
    public long count() {
        return studentsRepository.count();
    }

    @Override
    public Students save(Students student) {
        return studentsRepository.save(student);
    }

    @Override
    public void delete(Students student) {
        studentsRepository.delete(student);
    }

    @Override
    public Iterable<Students> getAll() {
        return studentsRepository.findAll();
    }


    /** 以下两个是条件查询*/
    @Override
    public List<Students> getByName(String name) {
        List<Students> list = new ArrayList<>();
        /** 创造条件*/
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name",name);
        /** search方法 */
        Iterable<Students> iterable = studentsRepository.search(matchQueryBuilder);
        iterable.forEach(e->list.add(e)); /** 填充list*/
        return list;
    }

    @Override
    public Page<Students> pageQuery(Integer pageNo, Integer pageSize, String kw) {
        /**NativeSearchQuery是经常用的
         * 而且链式语法很赞
         * */
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchPhraseQuery("name", kw))
                .withPageable(PageRequest.of(pageNo,pageSize))
                .build();
        return  studentsRepository.search(searchQuery);
    }
}
