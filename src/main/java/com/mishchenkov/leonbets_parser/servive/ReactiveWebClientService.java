package com.mishchenkov.leonbets_parser.servive;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class ReactiveWebClientService implements WebClientService {

    private final WebClient webClient;

    public ReactiveWebClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public <T> Flux<T> client(String url, ParameterizedTypeReference<T> listType) {
        return getResp(url)
                .bodyToFlux(listType);
    }

    @Override
    public <T> Flux<T> client(String url, Class<T> type) {
        return getResp(url).bodyToFlux(type);
    }

    private WebClient.ResponseSpec getResp(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve();
    }
}
