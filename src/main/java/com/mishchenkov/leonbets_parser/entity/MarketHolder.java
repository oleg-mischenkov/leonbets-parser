package com.mishchenkov.leonbets_parser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MarketHolder(
        long id,
        String name,
        List<Market> markets
) {

    public Map<String, List<Runner>> combineMarkets() {
        return markets.stream()
                .collect(Collectors.groupingBy(
                        Market::name,
                        TreeMap::new,
                        Collectors.flatMapping(market -> market.runners().stream(), Collectors.toList())
                ));
    }

}
