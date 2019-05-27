package com.knowledge.config;

import com.knowledge.filter.MobileFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfiguration {

    @Bean
    public MobileFilter mobileFilter() {
        return new MobileFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(@Qualifier("mobileFilter") MobileFilter mobileFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(mobileFilter);
        registration.addUrlPatterns("/mobile/*");
        registration.setName("mobileFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }
}
