package com.qc.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能描述: card item
 *
 * @author lijinhua
 * @date 2022/8/26 10:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardItem {
//    "cardName":"招商","cardTime":"2021-09-09 20:12:26"
    private  String cardName;
    private  String cardTime;
}
