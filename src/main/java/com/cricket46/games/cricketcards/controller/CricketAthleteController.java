package com.cricket46.games.cricketcards.controller;

import com.cricket46.games.cricketcards.model.CricketAthleteModel;
import com.cricket46.games.cricketcards.services.CricketAthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class CricketAthleteController {

    @Autowired
    CricketAthleteService cricketAthleteService;


    @GetMapping("/athletes")
    public List<CricketAthleteModel> listCricketPlayers(){
        return cricketAthleteService.fetchCricketAthletes(0);
    }

    @GetMapping("/athletes/{athleteId}")
    public CricketAthleteModel getCricketPlayer(@PathVariable String athleteId){
        return cricketAthleteService.fetchCricketAthlete(Long.valueOf(athleteId));
    }

    @PostMapping("/athletes/{athleteId}")
    public Boolean postCricketPlayer(@PathVariable String athleteId){
        return cricketAthleteService.populateCricketIplAthletes(Long.valueOf(athleteId));
    }


}
