package com.qc.boot.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/8 10:21
 */
@Data
public class Students implements Serializable {
    private Long id;
    private String username;
    private String password;
}
