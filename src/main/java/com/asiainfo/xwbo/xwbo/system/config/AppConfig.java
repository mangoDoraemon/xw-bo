/**
 * 
 */
package com.asiainfo.xwbo.xwbo.system.config;

import com.asiainfo.xwbo.xwbo.system.SignInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author t-xiabin
 *
 */
//@Configuration
public class AppConfig implements WebMvcConfigurer {

	@Bean
	public SignInterceptor signInterceptor() {
		return new SignInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(signInterceptor()).addPathPatterns("/**");
	}
	
}
