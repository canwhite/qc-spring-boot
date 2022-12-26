package com.qc.boot.entity;

/**
 * 功能描述: image item
 *
 * @author lijinhua
 * @date 2022/8/26 08:52
 */


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 {
 "id": "0",
 "author": "Alejandro Escamilla",
 "width": 5616,
 "height": 3744,
 "url": "https://unsplash.com/photos/yC-Yzbqy7PY",
 "download_url": "https://picsum.photos/id/0/5616/3744"
 }
 */

/** lombok的使用 */
@Data //生成getter、setter等函数
@AllArgsConstructor //生成全参数构造函数
@NoArgsConstructor //生成无参数构造函数
//@Builder //解决一个类有过多构造函数的问题
public class ImageItem {
    private  String id;
    private  String author;
    private  long width;
    private  long height;
    private  String url;
    private  String download_url;

}
