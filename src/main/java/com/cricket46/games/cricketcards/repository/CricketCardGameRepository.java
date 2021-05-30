package com.cricket46.games.cricketcards.repository;

import com.cricket46.games.cricketcards.domain.CricketCardGameAggregate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CricketCardGameRepository  extends MongoRepository<CricketCardGameAggregate, String>, QuerydslPredicateExecutor<CricketCardGameAggregate> {
}
