package com.cricket46.games.cricketcards.services;

import com.cricket46.games.cricketcards.domain.CricketAthleteAggregate;
import com.cricket46.games.cricketcards.model.CricketAthleteModel;
import com.cricket46.games.cricketcards.repository.CricketAthleteRepository;
import com.cricket46.games.cricketcards.utils.CricketAthleteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CricketAthleteService {

    @Autowired
    CricketAthleteRepository cricketAthleteRepository;

    @Autowired
    CricketAthleteUtils cricketAthleteUtils;

    public List<CricketAthleteModel> fetchCricketAthletes(int count){
        List<CricketAthleteModel> cricketAthletes = new ArrayList<>();


        List<CricketAthleteAggregate> athletes = cricketAthleteRepository.findAll();

        if(athletes != null)
            athletes.stream().forEach(cricketAthleteAggregate -> 
                    cricketAthletes.add(cricketAthleteUtils.populateCricketAthleteModel(cricketAthleteAggregate)));
        return cricketAthletes;
    }

    public CricketAthleteModel fetchCricketAthlete(Long athleteId) {
        Optional<CricketAthleteAggregate> cricketAthleteAggregateOpt = cricketAthleteRepository.findById(athleteId);
        if(cricketAthleteAggregateOpt.isPresent()){
            return cricketAthleteUtils.populateCricketAthleteModel(cricketAthleteAggregateOpt.get());
        }

        throw new RuntimeException("Athlete not found with athlete id :"+athleteId);
    }

    public Boolean populateCricketIplAthletes(long athleteId){

        return cricketAthleteUtils.createCricketPlayerDetailsFromSource(athleteId);
    }
}
