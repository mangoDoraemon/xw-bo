package com.asiainfo.xwbo.xwbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
public class XwBoApplication {

    public static void main(String[] args) {
        SpringApplication.run(XwBoApplication.class, args);
    }

}
