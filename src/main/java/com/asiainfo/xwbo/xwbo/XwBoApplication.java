package com.asiainfo.xwbo.xwbo;

import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
@Slf4j
public class XwBoApplication {

    public static void main(String[] args) {

        ParserConfig.getGlobalInstance().setSafeMode(true);
        SpringApplication.run(XwBoApplication.class, args);
    }

}
