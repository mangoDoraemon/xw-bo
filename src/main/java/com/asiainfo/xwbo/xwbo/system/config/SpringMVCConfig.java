package com.asiainfo.xwbo.xwbo.system.config;

/**
 * @author jiahao jin
 * @create 2020-05-12 14:26
 */
import com.asiainfo.xwbo.xwbo.system.FilterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@SuppressWarnings("deprecation")
@SpringBootConfiguration
public class SpringMVCConfig extends WebMvcConfigurerAdapter{
    @Autowired
    private FilterConfig filterConfig;

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(filterConfig).addPathPatterns("/**");
    }
}