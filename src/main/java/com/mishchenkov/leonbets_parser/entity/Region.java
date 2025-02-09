package com.mishchenkov.leonbets_parser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Region(
        long id,
        String name,
        String family,
        String url,
        List<League> leagues
) { }
