package com.mishchenkov.leonbets_parser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Market(
        long id,
        String name,
        boolean open,
        List<Runner> runners
) {
}
