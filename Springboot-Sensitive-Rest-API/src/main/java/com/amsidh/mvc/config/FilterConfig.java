package com.amsidh.mvc.config;

import com.amsidh.mvc.filter.DecryptRequestFilter;
import com.amsidh.mvc.filter.EncryptResponseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean(name = "decryptRequestFilterRegistrationBean")
    public FilterRegistrationBean<DecryptRequestFilter> newFilterRegistration(DecryptRequestFilter decryptRequestFilter) {
        FilterRegistrationBean<DecryptRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(decryptRequestFilter);
        registration.addUrlPatterns("/persons");
        registration.setOrder(1);
        return registration;
    }

    @Bean(name = "encryptResponseFilterRegistrationBean")
    public FilterRegistrationBean<EncryptResponseFilter> encryptResponseFilter(EncryptResponseFilter encryptResponseFilter) {
        FilterRegistrationBean<EncryptResponseFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(encryptResponseFilter);
        registrationBean.addUrlPatterns("/persons"); // Apply filter to specific URL patterns
        registrationBean.setOrder(100); // Ensure it runs after other filters
        return registrationBean;
    }

}
