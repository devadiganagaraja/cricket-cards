package com.cricket46.games.cricketcards.utils;

import com.cricket46.games.cricketcards.domain.*;
import com.cricket46.games.cricketcards.model.CricketAthleteModel;
import com.cricket46.games.cricketcards.repository.CricketAthleteRepository;
import com.cricket46.games.cricketcards.repository.CricketTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class CricketAthleteUtils {


    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    CricketAthleteRepository cricketAthleteRepository;

    @Autowired
    CricketTeamRepository cricketTeamRepository;


    public CricketAthleteModel populateCricketAthleteModel(CricketAthleteAggregate cricketAthleteAggregate) {

        CricketAthleteModel cricketAthleteModel = new CricketAthleteModel();

        cricketAthleteModel.setPlayerId(cricketAthleteAggregate.getPlayerId());
        cricketAthleteModel.setFullName(cricketAthleteAggregate.getFullName());
        cricketAthleteModel.setTotalMatches(cricketAthleteAggregate.getTotalMatches());
        cricketAthleteModel.setTotalRuns(cricketAthleteAggregate.getTotalRuns());
        cricketAthleteModel.setTotalWickets(cricketAthleteAggregate.getTotalWickets());
        cricketAthleteModel.setHighestScore(cricketAthleteAggregate.getHighestScore());
        cricketAthleteModel.setBestBowling(cricketAthleteAggregate.getBestBowlingFigures());
        return cricketAthleteModel;
    }


    public void createLeaguePlayerDetailsFromSource(long leagueId){

        String ref = "http://core.espnuk.org/v2/sports/cricket/leagues/"+leagueId+"/teams";

        Listing teamListing = restTemplate.getForObject(ref, Listing.class);

        if(null != teamListing){
            teamListing.getItems().forEach(teamRef -> {
                populateLeagueTeam(teamRef.get$ref());
            });
        }


    }

    private void populateLeagueTeam(String $ref) {
        Team team = restTemplate.getForObject($ref, Team.class);
        if(null != team){

            CricketTeamAggregate cricketTeamAggregate = new CricketTeamAggregate();
            cricketTeamAggregate.setTeamId(team.getId());
            cricketTeamAggregate.setLocation(team.getLocation());
            cricketTeamAggregate.setTeamName(team.getName());
            cricketTeamRepository.save(cricketTeamAggregate);
            populateLeagueTeamAthletes(team.getAthletes().get$ref());

        }


    }


    private void populateLeagueTeamAthletes(String $ref) {

        Listing athleteListing = restTemplate.getForObject($ref, Listing.class);

        if(null != athleteListing){
            athleteListing.getItems().forEach(athleteRef -> {
                populateAndSaveCricketAthleteAggregate(athleteRef.get$ref());
            });
        }

    }


    public void createCricketPlayerDetailsFromSource(long cricketPlayerId){

        String ref = "http://core.espnuk.org/v2/sports/cricket/athletes/" + cricketPlayerId;
        populateAndSaveCricketAthleteAggregate(ref);
    }

    private void populateAndSaveCricketAthleteAggregate(String $ref) {

        CricketAthleteAggregate cricketAthleteAggregate = new CricketAthleteAggregate();
        Athlete athlete = restTemplate.getForObject($ref, Athlete.class);

        if(null != athlete) {

            cricketAthleteAggregate.setPlayerId(athlete.getId()*13);
            cricketAthleteAggregate.setFullName(athlete.getDisplayName());

            String $statsRef = "http://core.espnuk.org/v2/sports/cricket/athletes/"+athlete.getId()+"/statistics?internationalClassId=2";

            AthleteStats athleteStats = restTemplate.getForObject($statsRef, AthleteStats.class);

            if(null != athleteStats && athleteStats.getSplits() != null){
                if(athleteStats.getSplits().getCategories() != null){
                    if(athleteStats.getSplits().getCategories().get(0) != null){
                        Category category = athleteStats.getSplits().getCategories().get(0);
                        if(category.getStats() != null){
                            category.getStats().forEach(stat -> {
                                if(stat.getName().equalsIgnoreCase("matches")) {
                                    cricketAthleteAggregate.setTotalMatches(stat.getValue());
                                }else if(stat.getName().equalsIgnoreCase("runs")) {
                                    cricketAthleteAggregate.setTotalRuns(stat.getValue());
                                }if(stat.getName().equalsIgnoreCase("wickets")) {
                                    cricketAthleteAggregate.setTotalWickets(stat.getValue());
                                }if(stat.getName().equalsIgnoreCase("highScore")) {
                                    cricketAthleteAggregate.setHighestScore(stat.getDisplayValue());
                                }if(stat.getName().equalsIgnoreCase("bestBowlingFigures")) {
                                    cricketAthleteAggregate.setBestBowlingFigures(stat.getDisplayValue());
                                }

                            });
                        }
                    }
                }
            }




            cricketAthleteRepository.save(cricketAthleteAggregate);
        }


    }
}
