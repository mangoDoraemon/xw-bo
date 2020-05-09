package com.asiainfo.xwbo.xwbo.system.config;


import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = {"com.asiainfo.xwbo.xwbo.dao.base"}, sqlSessionFactoryRef = "baseSqlSessionFactory")
public class MybatisBaseConfig {
    @Autowired
    @Qualifier("baseDataSource")
    private DataSource baseDataSource;

    @Bean
    public SqlSessionFactory baseSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(baseDataSource); // 使用titan数据源, 连接titan库
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("pageSizeZero", "true");//分页尺寸为0时查询所有纪录不再执行分页
        properties.setProperty("reasonable", "false");//页码<=0 查询第一页，页码>=总页数查询最后一页
        properties.setProperty("supportMethodsArguments", "true");//支持通过 Mapper 接口参数来传递分页参数
        pageHelper.setProperties(properties);

        //添加插件
        factoryBean.setPlugins(new Interceptor[]{pageHelper});
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return factoryBean.getObject();

    }

    @Bean
    public SqlSessionTemplate baseSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(baseSqlSessionFactory()); // 使用上面配置的Factory
        return template;
    }
}
