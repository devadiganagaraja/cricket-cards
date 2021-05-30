package com.cricket46.games.cricketcards.controller;

import com.cricket46.games.cricketcards.model.PlayerGameInfo;
import com.cricket46.games.cricketcards.model.Selection;
import com.cricket46.games.cricketcards.services.CricketCardGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class CricketCardGameController {



    @Autowired
    CricketCardGameService cricketCardGameService;

    @GetMapping("/games/{gameId}/players/{playerId}")
    public PlayerGameInfo getGamePageInfo(@PathVariable("gameId") long gameId, @PathVariable("playerId") long playerId){
        return cricketCardGameService.getGamePageInfo(gameId, playerId);
    }


    @PostMapping("/selectstat")
    public ResponseEntity<Boolean> authenticateUser(@RequestBody Selection selection) {
        System.out.println("selection--"+selection);

        boolean userLoggedIn = cricketCardGameService.selectStat(selection.getGameId(), selection.getPlayerId(), selection.getPosition());
        return new ResponseEntity<Boolean>(userLoggedIn, HttpStatus.OK);
    }


}
