package com.mishchenkov.leonbets_parser.scheduler;

import com.mishchenkov.leonbets_parser.parser.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LeonbetsScheduler implements Scheduler {

    private final Parser leonbetsParser;

    public LeonbetsScheduler(Parser leonbetsParser) {
        this.leonbetsParser = leonbetsParser;
    }

    @Scheduled(fixedDelayString = "${spring.scheduler.leonbets.fixed-delay}")
    @Override
    public void performTask() {
        log.info("### Parsing starts ###");
        leonbetsParser.doParse();
    }
}
