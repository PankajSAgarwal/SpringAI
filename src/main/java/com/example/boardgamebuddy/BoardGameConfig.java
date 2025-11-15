package com.example.boardgamebuddy;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Configuration
public class BoardGameConfig {
    @Bean
    RestClientCustomizer logBookCustomizer(LogbookClientHttpRequestInterceptor interceptor) {
        return restClient -> restClient.requestInterceptor(interceptor);

    }
}
