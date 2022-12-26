package com.qc.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 功能描述: res Result
 *
 * @author lijinhua
 * @date 2022/8/26 10:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CardsResult {
    private  long code;
    private  String msg;
    private  List<CardItem> cards;
    private  String name;
    private  long age;
}
