package com.amsidh.mvc.config;

import com.amsidh.mvc.filter.DecryptRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<DecryptRequestFilter> decryptRequestFilterRegistration() {
        FilterRegistrationBean<DecryptRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DecryptRequestFilter());
        registrationBean.addUrlPatterns("/*"); // Add URL patterns as needed
        registrationBean.setOrder(1); // Set the order if necessary
        return registrationBean;
    }
}
