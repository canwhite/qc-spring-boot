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
public class SlaveDataSourceConfiguration {

    @Bean("slaveDataSourceProperties")
    @ConfigurationProperties("spring.datasource-slave")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    //根据上边配置映射的props，去创建data source
    @Bean(RoutingDataSourceContext.SLAVE_DATASOURCE)
    DataSource dataSource(@Autowired @Qualifier("slaveDataSourceProperties") DataSourceProperties props){
        return  props.initializeDataSourceBuilder().build();
    }

}
