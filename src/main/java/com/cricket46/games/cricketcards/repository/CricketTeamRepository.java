package com.cricket46.games.cricketcards.repository;

import com.cricket46.games.cricketcards.domain.CricketAthleteAggregate;
import com.cricket46.games.cricketcards.domain.CricketTeamAggregate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CricketTeamRepository extends MongoRepository<CricketTeamAggregate, Long>, QuerydslPredicateExecutor<CricketTeamAggregate> {
}
