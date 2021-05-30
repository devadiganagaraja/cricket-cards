package com.cricket46.games.cricketcards.controller;

import com.cricket46.games.cricketcards.model.PlayerGameInfo;
import com.cricket46.games.cricketcards.model.Selection;
import com.cricket46.games.cricketcards.model.User;
import com.cricket46.games.cricketcards.services.CricketCardGameService;
import com.cricket46.games.cricketcards.services.CricketCardUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class CricketCardGameController {



    @Autowired
    CricketCardGameService cricketCardGameService;


    @Autowired
    CricketCardUserService cricketCardUserService;

    @GetMapping("/games/{gameId}/player1/{player1Id}/player2/{player2Id}")
    public PlayerGameInfo getGamePageInfo(@PathVariable("gameId") long gameId, @PathVariable("player1Id") long player1Id, @PathVariable("player2Id") long player2Id){
        return cricketCardGameService.getGamePageInfo(gameId, player1Id, player2Id);
    }


    @PostMapping("/selectstat")
    public ResponseEntity<Boolean> authenticateUser(@RequestBody Selection selection) {
        System.out.println("selection--"+selection);

        boolean userLoggedIn = cricketCardGameService.selectStat(selection.getGameId(), selection.getPlayerId(), selection.getPosition());
        return new ResponseEntity<Boolean>(userLoggedIn, HttpStatus.OK);
    }

    @GetMapping("/players/{playerId}/friends")
    public List<User> getUserList(@PathVariable("playerId") long playerId){
        return cricketCardUserService.getFriendList(playerId);
    }


}
