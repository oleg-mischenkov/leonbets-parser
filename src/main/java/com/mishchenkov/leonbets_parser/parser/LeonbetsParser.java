package com.mishchenkov.leonbets_parser.parser;

import com.mishchenkov.leonbets_parser.entity.*;
import com.mishchenkov.leonbets_parser.servive.WebClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Set;

@Component
public class LeonbetsParser implements Parser {

    private final WebClientService webClientService;
    private final String sportList;
    private final String topLeaguesUrl;
    private final String eventUrl;
    private final String marketUrl;
    private final Integer threadCount;

    public LeonbetsParser(WebClientService webClientService,
                          @Value("${spring.parser.settings.sports}") String sportList,
                          @Value("${spring.parser.api.top-leagues}") String topLeagues,
                          @Value("${spring.parser.api.events}") String eventUrl,
                          @Value("${spring.parser.api.markets}") String marketUrl,
                          @Value("${spring.parser.settings.threads}") Integer threadCount) {
        this.webClientService = webClientService;
        this.sportList = sportList;
        this.topLeaguesUrl = topLeagues;
        this.eventUrl = eventUrl;
        this.marketUrl = marketUrl;
        this.threadCount = threadCount;
    }

    @Override
    public void doParse() {
        Set<String> sportPredictSet = Set.of(sportList.split(","));

         webClientService.client(topLeaguesUrl, new ParameterizedTypeReference<Sport>(){})
                 .filter(sport -> sportPredictSet.contains( sport.name() ))
                 .map(sport -> Tuples.of(new ConsoleViewTemplate(sport.name(), null, null, null, null, null), sport.regions()))
                 .parallel(threadCount)
                 .runOn(Schedulers.parallel())
                 .flatMap(this::extractRegion)
                 .flatMap(this::extractLeague)
                 .flatMap(this::getEventFromApi)
                 .flatMap(this::extractEvent)
                 .flatMap(this::getMarketFromApi)
                 .sequential()
                 .subscribe(System.out::println);
    }

    private Flux<ConsoleViewTemplate> getMarketFromApi(ConsoleViewTemplate cTemplate) {
        var sportName = cTemplate.sportName();
        var regionName = cTemplate.regionName();
        var leagueName = cTemplate.leagueName();
        var leagueId = cTemplate.leagueNameId();
        var event = cTemplate.event();
        var url = String.format(marketUrl, event.id());
        return webClientService.client(url, MarketHolder.class).map(mHolder ->
                new ConsoleViewTemplate(sportName, leagueId, leagueName, regionName, event, mHolder));
    }

    private Flux<ConsoleViewTemplate> extractEvent(Tuple2<ConsoleViewTemplate, EventHolder> tHolder) {
        var template = tHolder.getT1();
        var sportName = template.sportName();
        var regionName = template.regionName();
        var leagueName = template.leagueName();
        var leagueId = template.leagueNameId();

        var eventList = tHolder.getT2().events().stream().limit(2L).toList();
        return Flux.fromIterable(eventList)
                .map(event -> new ConsoleViewTemplate(sportName, leagueId, leagueName, regionName, event, null));
    }

    private Flux<Tuple2<ConsoleViewTemplate, EventHolder>> getEventFromApi(Tuple2<ConsoleViewTemplate, League> tLeague) {
        var lgId = tLeague.getT2().id();
        var url = String.format(eventUrl, lgId);
        return webClientService.client(url, EventHolder.class)
                .map(eventHolder -> Tuples.of(tLeague.getT1(), eventHolder));
    }

    private Flux<Tuple2<ConsoleViewTemplate, League>> extractLeague(Tuple2<ConsoleViewTemplate, Region> tRegion) {
        return Flux.fromIterable(tRegion.getT2().leagues()).filter(League::top).map(league -> {
            var template = tRegion.getT1();
            var sportName = template.sportName();
            var regionName = template.regionName();
            var leagueName = league.name();
            var leagueId = league.id();
            var conTemplate = new ConsoleViewTemplate(sportName, leagueId, leagueName, regionName, null, null);

            return Tuples.of(conTemplate, league);
        });
    }

    private Flux<Tuple2<ConsoleViewTemplate, Region>> extractRegion(Tuple2<ConsoleViewTemplate, List<Region>> tRegions) {
        return Flux.fromIterable(tRegions.getT2()).map(region -> {
            var sportName = tRegions.getT1().sportName();
            var regionName = region.name();
            var conTemplate = new ConsoleViewTemplate(sportName, null, null, regionName, null, null);

            return Tuples.of(conTemplate, region);
        });
    }
}
