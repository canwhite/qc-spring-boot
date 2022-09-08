package com.qc.boot.dao;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/8 10:22
 */

import com.qc.boot.entity.Students;

import java.util.List;

/** dao可以理解为一种抽象工厂模式，所以它可以和任意工具联合使用*/
public interface StudentsDao {
    public  void  saveStudent(Students student);
    public  Students findStudentByName(String name);
    public  long updateStudent(Students student);
    public  void  deleteStudentById(Long id);
    public  List<Students> findAll();
}
