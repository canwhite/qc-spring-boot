package com.qc.boot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    /**
     * 
     * curl 
     *  -X POST 
     *  -H "Content-Type: multipart/form-data" 
     *  -F "file=@/Users/zack/Desktop/package.zip" 
     *  http://localhost:8080/zip/upload
     */


    @PostMapping("upload")
    public Callable<CommonResult> sendZip(@RequestParam("file") MultipartFile file) {
        //返回callable,要看这是不是个异步操作了
        return  ()->{
            
            zipService.upload(file);

            CommonResult result = new CommonResult();
            result.setCode(200);
            result.setMsg("success");
            //返回文件地址
            result.setData(Map.of("path",file.getOriginalFilename()));


            //callable内部返回值
            return result;
        };
    }




}
