package com.example.bytemonk.services;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ApiLoggingFilter> loggingFilter() {
        FilterRegistrationBean<ApiLoggingFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new ApiLoggingFilter());
        registrationBean.addUrlPatterns("/api/*"); // Specify your API path if needed

        return registrationBean;
    }
}