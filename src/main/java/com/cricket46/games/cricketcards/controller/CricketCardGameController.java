package com.cricket46.games.cricketcards.controller;

import com.cricket46.games.cricketcards.model.PlayerGameInfo;
import com.cricket46.games.cricketcards.services.CricketCardGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CricketCardGameController {



    @Autowired
    CricketCardGameService cricketCardGameService;

    @GetMapping("/games/{gameId}/players/{playerId}")
    public PlayerGameInfo getGamePageInfo(@PathVariable("gameId") long gameId, @PathVariable("playerId") long playerId){
        return cricketCardGameService.getGamePageInfo(gameId, playerId);
    }


}
