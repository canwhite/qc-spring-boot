package com.qc.boot.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 功能描述: storage configration'
 *
 * @author lijinhua
 * @date 2022/8/24 10:42
 */


@Configuration
//注意大批量的拿属性值，这里用的是properties
@ConfigurationProperties("storage.local")
/** Spring Boot中配置可以写成Bean，这样可以由编译器检查类型，无需编写重复的@Value注解*/
public class StorageConfiguration {
    //属性要和配置文件中的一一对应,但是可以选择驼峰
    /**
    storage:
        type: ${STORAGE_TYPE:local}
        local:
            root-dir: ${STORAGE_LOCAL_ROOT:/var/storage}
            max-size: ${STORAGE_LOCAL_MAX_SIZE:102400}
            allow-empty: false
            allow-types: jpg, png, gif
     */
    private  String rootDir;
    private  int maxSize;
    private  boolean allowEmpty;
    private List<String> allowTypes;

    //get方法
    public String getRootDir(){
        return  rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public  boolean isAllowEmpty(){
        return  allowEmpty;
    }

    public void setAllowEmpty(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    public List<String> getAllowTypes() {
        return allowTypes;
    }

    public void setAllowTypes(List<String> allowTypes) {
        this.allowTypes = allowTypes;
    }
}
