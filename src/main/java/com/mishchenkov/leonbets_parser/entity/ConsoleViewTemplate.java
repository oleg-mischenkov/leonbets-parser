package com.mishchenkov.leonbets_parser.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static java.lang.String.format;

public record ConsoleViewTemplate(
        String sportName,
        Long leagueNameId,
        String leagueName,
        String regionName,
        Event event,
        MarketHolder marketHolder) implements ViewTemplate<String> {

    @Override
    public String print() {
        return toString();
    }

    @Override
    public String toString() {
        var result = new StringBuilder();

        var title = format("%s, %s %s %n", sportName, regionName, leagueName);
        result.append(title);

        var time = event.kickoff();
        var eventName = event.name();
        var eventId = event.id();
        var eventLine = format("\t%s, %s UTC, %d %n", eventName, getUtcDate(time), eventId);
        result.append(eventLine);

        var marketList = getFormattedMarkets();
        result.append(marketList);

        return result.toString();
    }

    private String getFormattedMarkets() {
        var result = new StringBuilder();

        var marketSet = marketHolder.combineMarkets();
        var marketList = marketSet.keySet().stream()
                        .map(mkt -> {
                            var sb = new StringBuilder();
                            var mTitle = format("\t\t %s %n", mkt);
                            sb.append(mTitle);

                            var runnerLines = marketSet.get(mkt).stream()
                                    .filter(Runner::open)
                                    .map(rnr -> format("\t\t\t %-15s, %5s, %20d %n", rnr.name(), rnr.priceStr(), rnr.id()))
                                    .collect(Collectors.joining());
                            sb.append(runnerLines);

                            return sb.toString();
                        }).collect(Collectors.joining());

        result.append(marketList);

        return result.toString();
    }

    private String getUtcDate(long timestamp) {
        var instant = Instant.ofEpochMilli(timestamp);
        var formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("UTC"));

        return formatter.format(instant);
    }

}
