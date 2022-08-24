package com.qc.boot.service.impl;

import com.qc.boot.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * 功能描述: cloud
 *
 * @author lijinhua
 * @date 2022/8/24 09:01
 */
@Component
@Profile("!default")
public class CloudStorageService implements StorageService {


    @Value("${storage.cloud.bucket:}")
    String bucket;

    @Value("${storage.cloud.access-key:}")
    String accessKey;

    @Value("${storage.cloud.access-secret:}")
    String accessSecret;

    final Logger logger = LoggerFactory.getLogger(getClass());

    //初始化
    @PostConstruct
    public  void  init(){
        // TODO:
        logger.info("Initializing cloud storage...");
    }

    //方法实现
    @Override
    public InputStream openInputStream(String uri) throws IOException {
        throw new IOException("File not found: " + uri);
    }

    @Override
    public String store(String extName, InputStream input) throws IOException {
        throw new IOException("Unable to access cloud storage.");
    }
}
