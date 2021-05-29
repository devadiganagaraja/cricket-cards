package com.cricket46.games.cricketcards.services;

import com.cricket46.games.cricketcards.model.CricketAthleteModel;
import com.cricket46.games.cricketcards.model.GameInfo;
import com.cricket46.games.cricketcards.model.PlayerGameInfo;
import com.cricket46.games.cricketcards.model.PlayerInfo;
import com.cricket46.games.cricketcards.utils.CricketCardGameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class CricketCardGameService {


    @Autowired
    Map<Long, GameInfo> liveGames;

    @Autowired
    CricketAthleteService cricketAthleteService;


    public GameInfo createGame(long gameId) {


        GameInfo gameInfo = new GameInfo();

        gameInfo.setGameId(gameId);

        PlayerGameInfo gamePlayer1 = new PlayerGameInfo();
        gamePlayer1.setActivePlayer(true);
        PlayerInfo player1 = new PlayerInfo();
        player1.setPlayerId(1);
        player1.setDisplayName("Player 1");
        gamePlayer1.setPlayerInfo(player1);
        gamePlayer1.setActivePlayer(true);
        gamePlayer1.setCurrentCard(getDummyCard(1, "Sachin Tendulkar", 300, 10000, 104));
        gamePlayer1.setNextCards(new ArrayList<>());
        gamePlayer1.getNextCards().add(getDummyCard(2, "Sachin Tendulkar", 300, 10000, 104));
        gamePlayer1.getNextCards().add(getDummyCard(3, "Sachin Tendulkar", 300, 10000, 104));


        gamePlayer1.setCardCount(gamePlayer1.getNextCards().size());
        if(null != gamePlayer1.getCurrentCard())
            gamePlayer1.setCardCount(gamePlayer1.getCardCount()+1);

        gameInfo.setPlayer1Info(gamePlayer1);

        PlayerGameInfo gamePlayer2 = new PlayerGameInfo();
        PlayerInfo player2 = new PlayerInfo();
        player2.setPlayerId(2);
        player2.setDisplayName("Player 2");
        gamePlayer2.setPlayerInfo(player1);
        gamePlayer2.setActivePlayer(true);
        gamePlayer2.setCurrentCard(getDummyCard(4, "Anil Kumble", 310, 1000, 304));
        gamePlayer2.setNextCards(new ArrayList<>());
        gamePlayer2.getNextCards().add(getDummyCard(5, "Anil Kumble", 310, 1000, 304));
        gamePlayer2.getNextCards().add(getDummyCard(6, "Anil Kumble", 310, 1000, 304));


        gamePlayer2.setCardCount(gamePlayer2.getNextCards().size());
        if(null != gamePlayer2.getCurrentCard())
            gamePlayer2.setCardCount(gamePlayer2.getCardCount()+1);



        gameInfo.setPlayer2Info(gamePlayer2);

        return gameInfo;
    }

    private CricketAthleteModel getDummyCard(long playerId, String fullName, int matches, int runs, int wickets) {
        CricketAthleteModel cricketAthleteModel = new CricketAthleteModel();
        cricketAthleteModel.setPlayerId(playerId);
        cricketAthleteModel.setFullName(fullName);
        cricketAthleteModel.setTotalMatches(matches);
        cricketAthleteModel.setTotalRuns(runs);
        cricketAthleteModel.setTotalWickets(wickets);
        return cricketAthleteModel;
    }


    public PlayerGameInfo getGamePageInfo(long  gameId, long playerId) {

        GameInfo gameInfo = liveGames.get(gameId);


        if(null == gameInfo){
            gameInfo = createGame(gameId);
            liveGames.put(gameId, gameInfo);

        }

        if(gameInfo.getPlayer1Info().getCardCount() ==0 || gameInfo.getPlayer2Info().getCardCount() == 0) {
            gameInfo.getPlayer1Info().setGameFinished(true);
            gameInfo.getPlayer2Info().setGameFinished(true);
        }

        System.out.println("gameInfo: "+gameInfo);
        System.out.println("playerId: "+playerId);

        if(playerId == gameInfo.getPlayer1Info().getPlayerInfo().getPlayerId())
            return gameInfo.getPlayer1Info();
        else
            return gameInfo.getPlayer2Info();

    }
}
