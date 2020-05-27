/**
 * 
 */
package com.asiainfo.xwbo.xwbo.system.config;

import com.asiainfo.xwbo.xwbo.system.RequestWrapperFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author t-xiabin
 *
 */
@Configuration
public class RequestWrapperFilterConfig {

	@Bean
	public FilterRegistrationBean httpServletRequestReplacedRegistration() {
	    FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setFilter(new RequestWrapperFilter());
	    registration.addUrlPatterns("/*");
	    registration.addInitParameter("paramName", "paramValue");
	    registration.setName("requestWrapperFilter");
	    registration.setOrder(1);
	    return registration;
	}

}
