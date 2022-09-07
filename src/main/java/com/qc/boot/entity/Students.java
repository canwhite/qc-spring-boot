package com.qc.boot.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/7 15:35
 */
@Data
@Document(indexName = "students")
public class Students implements Serializable {
    @Id
    private String uId;
    private String name;
    private Integer age;
    private String address;
}
