package com.qc.boot.service;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ZipService {

    final Logger logger = LoggerFactory.getLogger(getClass());


    //上传操作
    public void upload(MultipartFile file) throws IllegalStateException, IOException{
        File dest = new File("/Users/zack/Desktop/path/to/save/" + file.getOriginalFilename());
        //transferTo用于将上传的文件写入指定的路径
        file.transferTo(dest);
    }
    //执行脚本，修改文件源地址
    public void execShell(){
        //TODO

    }

    //修改nginx
    public void changeNginxConfig(){
        //TODO


    }


}
