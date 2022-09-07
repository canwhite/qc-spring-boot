package com.qc.boot.elastic;

import com.qc.boot.entity.Students;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/7 15:45
 */

public interface StudentsDao {
    long count();
    Students save(Students student);
    void delete(Students student);
    Iterable<Students> getAll();
    List<Students> getByName(String name);
    Page<Students> pageQuery(Integer pageNo, Integer pageSize, String kw);
}
