package com.mishchenkov.leonbets_parser.servive;

import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Flux;

public interface WebClientService {

   <T> Flux<T> client(String url, ParameterizedTypeReference<T> listType);

   <T> Flux<T> client(String url, Class<T> type);

}
