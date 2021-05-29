package com.cricket46.games.cricketcards;

import com.cricket46.games.cricketcards.model.GameInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class CricketCardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CricketCardsApplication.class, args);
	}




	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Qualifier("liveGames")
	public Map<Long, GameInfo> liveGames(){
		return new ConcurrentHashMap<>();
	}

}
