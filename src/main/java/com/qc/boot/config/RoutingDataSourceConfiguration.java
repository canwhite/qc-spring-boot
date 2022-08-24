package com.qc.boot.config;

import com.qc.boot.config.context.RoutingDataSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.Map;


/**
 * 功能描述: config
 *
 * @author lijinhua
 * @date 2022/8/24 14:36
 */
public class RoutingDataSourceConfiguration {
    @Primary
    @Bean
    DataSource dataSource(@Autowired @Qualifier(RoutingDataSourceContext.MASTER_DATASOURCE) DataSource masterDataSource,
                          @Autowired @Qualifier(RoutingDataSourceContext.SLAVE_DATASOURCE) DataSource slaveDataSource){
        //啥消息给他一讲，实际上就谁都知道了
        var ds = new RoutingDataSource();
        ds.setTargetDataSources(Map.of(RoutingDataSourceContext.MASTER_DATASOURCE, masterDataSource,
                RoutingDataSourceContext.SLAVE_DATASOURCE, slaveDataSource));
        ds.setDefaultTargetDataSource(masterDataSource);
        return ds;
    }

    @Bean
    JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource){
        return  new JdbcTemplate(dataSource);
    }

    @Bean
    DataSourceTransactionManager dataSourceTransactionManager(@Autowired DataSource dataSource){
        return  new DataSourceTransactionManager(dataSource);
    }

}


/** 切换工作主要是这个类实现，然后对外的默认dataSource bean是上一个类
 * 切换依赖于ano => aspect => context => routingDataSource(可以叫它dataSource路由)
 * */
class  RoutingDataSource extends AbstractRoutingDataSource{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    /** 确定当前的查询键 */
    protected Object determineCurrentLookupKey() {
        return RoutingDataSourceContext.getDataSourceRoutingKey();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        /** 输出一下当前切换值 */
        DataSource ds = super.determineTargetDataSource();
        logger.info("determin target datasource: {}", ds);
        return ds;
    }
}



