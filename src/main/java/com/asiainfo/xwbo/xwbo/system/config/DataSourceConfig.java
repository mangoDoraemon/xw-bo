package com.asiainfo.xwbo.xwbo.system.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


/**
 * 多数据配置
 * @author t-xiabin
 *
 */
@Configuration
public class DataSourceConfig {

    /**
     * 配置库数据源
     * @return
     */
    @Bean(name = "baseDataSource")
    @Qualifier("baseDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.base")
    public DataSource baseDataSource() {
        return new DruidDataSource();
    }


    @Bean(name = "baseNamedJdbcTemplate")
    @Primary
    public NamedParameterJdbcTemplate baseNamedJdbcTemplate(@Qualifier("baseDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "baseTransaction")
    public PlatformTransactionManager baseTransaction(@Qualifier("baseDataSource") DataSource dataSource)  {
        return new DataSourceTransactionManager(dataSource);
    }
}