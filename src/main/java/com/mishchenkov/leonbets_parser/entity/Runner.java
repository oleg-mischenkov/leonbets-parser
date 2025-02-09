package com.mishchenkov.leonbets_parser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Runner(
        long id,
        String name,
        boolean open,
        String priceStr
) {}
