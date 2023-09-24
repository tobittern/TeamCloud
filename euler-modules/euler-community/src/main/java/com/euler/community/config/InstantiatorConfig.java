package com.euler.community.config;


import com.euler.community.instantiator.LoginInstantiator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InstantiatorConfig implements WebMvcConfigurer {

    @Bean
    public LoginInstantiator setBean() {
        return new LoginInstantiator();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(setBean()).addPathPatterns("/**").excludePathPatterns("/common/userBlockingDispatcher", "/static/**");
    }
}
