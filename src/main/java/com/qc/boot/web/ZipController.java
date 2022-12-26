package com.qc.boot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qc.boot.entity.CommonResult;
import com.qc.boot.service.ZipService;

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/zip")//统一声明顶级路由，然后再区分get和post
public class ZipController {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ZipService zipService;

    //然后处理zip文件上传
    //easy function
    @GetMapping("sendZip")
    public Callable<CommonResult> sendZip(){
        //返回callable,要看这是不是个异步操作了
        return  ()->{
            CommonResult result = new CommonResult();
            result.setCode(200);
            result.setMsg("success");
            result.setData(Map.of("result","success"));
            
            //callable内部返回值
            return result;
        };
    }
    

    



}
