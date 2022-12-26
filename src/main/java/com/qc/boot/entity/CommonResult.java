package com.qc.boot.entity;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult {
    
    private long code;
    private String msg;
    private Map<String,Object> data;
    
}
