package com.mishchenkov.leonbets_parser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public ExchangeStrategies exchangeStrategies(
            @Value("${spring.parser.web-client.max-json-size}") Integer memSize) {
        return ExchangeStrategies.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(memSize))
                .build();
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder,
                               ExchangeStrategies exchangeStrategies,
                               @Value("${spring.parser.main-url}") String mainUrl) {
        return builder
                .baseUrl(mainUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

}
