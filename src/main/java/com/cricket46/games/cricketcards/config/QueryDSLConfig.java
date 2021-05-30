package com.cricket46.games.cricketcards.config;

import com.cricket46.games.cricketcards.domain.CricketAthleteAggregate;
import com.cricket46.games.cricketcards.domain.QCricketCardUserAggregate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDSLConfig {


    @Bean
    public QCricketCardUserAggregate qCricketCardUserAggregate(){
        return new QCricketCardUserAggregate("cricket_cards_users");
    }
}
