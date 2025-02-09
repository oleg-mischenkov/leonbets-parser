package com.mishchenkov.leonbets_parser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Event(
        long id,
        String name,
        Long kickoff,
        Long lastUpdated
) {}
