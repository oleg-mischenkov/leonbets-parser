package com.mishchenkov.leonbets_parser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record League(
        long id,
        String name,
        String url,
        boolean top
) {
}
