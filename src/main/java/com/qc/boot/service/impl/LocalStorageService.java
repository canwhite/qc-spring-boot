package com.qc.boot.service.impl;

import com.qc.boot.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * 功能描述: local
 *
 * @author lijinhua
 * @date 2022/8/24 08:45
 */
@Component
//@Profile("default")
/**
 * 上边profile局限性太大，不能太精细化的分工，但是boot有条件装配呀，小汽车就是小汽车
@ConditionalOnProperty：如果有指定的配置，条件生效；
@ConditionalOnBean：如果有指定的Bean，条件生效；
@ConditionalOnMissingBean：如果没有指定的Bean，条件生效；
@ConditionalOnMissingClass：如果没有指定的Class，条件生效；
@ConditionalOnWebApplication：在Web环境中条件生效；
@ConditionalOnExpression：根据表达式判断条件是否生效。
*/

//设定为local时，启用LocalStorageService：
@ConditionalOnProperty(value = "storage.type",havingValue = "local",matchIfMissing = true)
public class LocalStorageService implements StorageService {
    @Value("${storage.local:/var/static}")
    String localStorageRootDir;
    final Logger logger = LoggerFactory.getLogger(getClass());
    private File localStorageRoot;


    //初始化工作
    @PostConstruct
    public  void  init(){
        logger.info("init local storage with root dir:{}",this.localStorageRoot);
        this.localStorageRoot = new File(this.localStorageRootDir);
    }

    @Override
    public InputStream openInputStream(String uri) throws IOException {
        File targetFile = new File(this.localStorageRoot,uri);
        return new BufferedInputStream(new FileInputStream(targetFile));
    }

    @Override
    public String store(String extName, InputStream input) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + extName;
        File targetFile = new File(this.localStorageRoot,fileName);
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile))) {
            input.transferTo(output);
        }
        return fileName;
    }
}
