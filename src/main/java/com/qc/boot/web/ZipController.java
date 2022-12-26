package com.qc.boot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qc.boot.service.ZipService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/zip")//统一声明顶级路由，然后再区分get和post
public class ZipController {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ZipService zipService;

    //然后处理zip文件上传
    // @PostMapping("/send")

    



}
