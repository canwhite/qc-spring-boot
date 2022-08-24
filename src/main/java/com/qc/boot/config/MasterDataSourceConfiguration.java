package com.qc.boot.config;

import com.qc.boot.config.context.RoutingDataSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * 功能描述: config
 *
 * @author lijinhua
 * @date 2022/8/24 14:28
 */
//注意这里没有bean标识，最后由routing统一装配
public class MasterDataSourceConfiguration {
    @Bean("masterDataSourceProperties")
    //（1）从yml拿到属性值映射一下
    @ConfigurationProperties("spring.datasource-master")
    DataSourceProperties dataSourceProperties(){
        return  new DataSourceProperties();
    }
    //（2）根据properties创建data source
    @Bean(RoutingDataSourceContext.MASTER_DATASOURCE) //这里只是从context状态机里取个名字
    DataSource dataSource(@Autowired @Qualifier("masterDataSourceProperties") DataSourceProperties props){
        return  props.initializeDataSourceBuilder().build();
    }
}
