package com.cricket46.games.cricketcards.utils;

import com.cricket46.games.cricketcards.domain.Athlete;
import com.cricket46.games.cricketcards.domain.AthleteListing;
import com.cricket46.games.cricketcards.domain.CricketAthleteAggregate;
import com.cricket46.games.cricketcards.model.CricketAthleteModel;
import com.cricket46.games.cricketcards.repository.CricketAthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class CricketAthleteUtils {


    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    CricketAthleteRepository cricketAthleteRepository;


    public CricketAthleteModel populateCricketAthleteModel(CricketAthleteAggregate cricketAthleteAggregate) {

        CricketAthleteModel cricketAthleteModel = new CricketAthleteModel();

       /* cricketAthleteModel.setPlayerId(cricketAthleteAggregate.getPlayerId());
        cricketAthleteModel.setFirstName(cricketAthleteAggregate.getFirstName());
        cricketAthleteModel.setLastName(cricketAthleteAggregate.getLastName());
        cricketAthleteModel.setFullName(cricketAthleteAggregate.getFullName());
        cricketAthleteModel.setDateOfBirth(cricketAthleteAggregate.getDateOfBirth());
        cricketAthleteModel.setCatches(cricketAthleteAggregate.getCatches());
        cricketAthleteModel.setTotalMatches(cricketAthleteAggregate.getTotalMatches());
        cricketAthleteModel.setTotalRuns(cricketAthleteAggregate.getTotalRuns());
        cricketAthleteModel.setTotalWickets(cricketAthleteAggregate.getTotalWickets());
        cricketAthleteModel.setHighestScore(cricketAthleteAggregate.getHighestScore());
        cricketAthleteModel.setBestBowling(cricketAthleteAggregate.getBestBowling());
        cricketAthleteModel.setStumps(cricketAthleteAggregate.getStumps());
*/
        return cricketAthleteModel;
    }


    public boolean createCricketPlayerDetailsFromSource(long cricketPlayerId){
        CricketAthleteAggregate cricketAthleteAggregate = new CricketAthleteAggregate();
        String ref = "http://core.espnuk.org/v2/sports/cricket/athletes/" + cricketPlayerId;
        Athlete athlete = restTemplate.getForObject(ref, Athlete.class);

        if(null != athlete) {

            cricketAthleteAggregate.setPlayerId(athlete.getId());
            cricketAthleteAggregate.setFullName(athlete.getDisplayName());
            cricketAthleteAggregate.setFirstName(athlete.getFirstName());
            cricketAthleteAggregate.setLastName(athlete.getLastName());
            cricketAthleteAggregate.setDateOfBirth(athlete.getDateOfBirthStr());

            cricketAthleteRepository.save(cricketAthleteAggregate);
            return true;
        }


        throw new RuntimeException("exception while populating athlete:"+cricketPlayerId);
    }
}
