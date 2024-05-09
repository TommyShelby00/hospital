package com.hp.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YourFeignClientConfiguration {

    @Bean
    public CustomFeignClientInterceptor customFeignClientInterceptor() {
        return new CustomFeignClientInterceptor();
    }
}
