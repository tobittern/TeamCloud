package com.euler.common.web.config;

import com.euler.common.web.core.RepeatedlyReadFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 *

 **/
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean repeatedlyReadFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RepeatedlyReadFilter());
        registration.addUrlPatterns("/*");
        registration.setName("RepeatedlyReadFilter");
        registration.setOrder(Integer.MAX_VALUE);
        registration.setEnabled(true);
        return registration;
    }
}
