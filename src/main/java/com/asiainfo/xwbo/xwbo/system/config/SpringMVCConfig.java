package com.asiainfo.xwbo.xwbo.system.config;

/**
 * @author jiahao jin
 * @create 2020-05-12 14:26
 */
import com.asiainfo.xwbo.xwbo.system.FilterConfig;
import com.asiainfo.xwbo.xwbo.system.SignInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@SuppressWarnings("deprecation")
@SpringBootConfiguration
public class SpringMVCConfig extends WebMvcConfigurerAdapter{
    @Autowired
    private FilterConfig filterConfig;

    @Bean
    public SignInterceptor signInterceptor() {
        return new SignInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(filterConfig).addPathPatterns("/**");
        registry.addInterceptor(signInterceptor()).addPathPatterns("/**");
    }
}
