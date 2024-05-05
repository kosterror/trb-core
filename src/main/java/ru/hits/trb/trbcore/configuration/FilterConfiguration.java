package ru.hits.trb.trbcore.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hits.trb.trbcore.filter.RandomErrorFilter;
import ru.hits.trb.trbcore.filter.RequestLoggingFilter;

@Configuration
@RequiredArgsConstructor
public class FilterConfiguration {

    private final RandomErrorProperties randomErrorProperties;

    @Bean
    public FilterRegistrationBean<RandomErrorFilter> randomErrorFilterFilterRegistrationBean() {
        var registrationBean = new FilterRegistrationBean<RandomErrorFilter>();
        var filter = new RandomErrorFilter(randomErrorProperties);

        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilterFilterRegistrationBean() {
        var registrationBean = new FilterRegistrationBean<RequestLoggingFilter>();
        var filter = new RequestLoggingFilter();

        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }

}
