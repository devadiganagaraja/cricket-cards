package com.cricket46.games.cricketcards.services;

import com.cricket46.games.cricketcards.model.CricketAthleteModel;
import com.cricket46.games.cricketcards.model.GameInfo;
import com.cricket46.games.cricketcards.model.PlayerGameInfo;
import com.cricket46.games.cricketcards.model.PlayerInfo;
import com.cricket46.games.cricketcards.utils.CricketCardGameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CricketCardGameService {


    @Autowired
    Map<Long, GameInfo> liveGames;

    @Autowired
    CricketAthleteService cricketAthleteService;

    public CricketAthleteModel getRandomElement(List<CricketAthleteModel> cricketAthleteModelList, Set<Integer> selected)
    {
        Random rand = new Random();
        int random = rand.nextInt(cricketAthleteModelList.size());
        while(selected.contains(random)) random = rand.nextInt(cricketAthleteModelList.size());

        selected.add(random);
        return cricketAthleteModelList.get(random);
    }


    public GameInfo createGame(long gameId) {


        GameInfo gameInfo = new GameInfo();

        gameInfo.setGameId(gameId);


        List<CricketAthleteModel> cricketAthleteModelList = cricketAthleteService.fetchCricketAthletes().stream().filter(cricketAthleteModel -> cricketAthleteModel.getTotalMatches() > 100).collect(Collectors.toList());

        Set<Integer> selected = new HashSet<>();

        PlayerGameInfo gamePlayer1 = new PlayerGameInfo();
        gamePlayer1.setActivePlayer(true);
        PlayerInfo player1 = new PlayerInfo();
        player1.setPlayerId(1);
        player1.setDisplayName("Player 1");
        gamePlayer1.setPlayerInfo(player1);
        gamePlayer1.setActivePlayer(true);
        gamePlayer1.setNextCards(new LinkedList<>());

        for(int i =0; i < 10; i++){
            CricketAthleteModel randomAthlete = getRandomElement(cricketAthleteModelList, selected);
            gamePlayer1.getNextCards().add(getCardDetails(randomAthlete.getPlayerId(), randomAthlete.getFullName(), randomAthlete.getTotalMatches(), randomAthlete.getTotalRuns(), randomAthlete.getTotalWickets()));
        }

        gamePlayer1.setCardCount(gamePlayer1.getNextCards().size());

        gameInfo.setPlayer1Info(gamePlayer1);

        PlayerGameInfo gamePlayer2 = new PlayerGameInfo();
        PlayerInfo player2 = new PlayerInfo();
        player2.setPlayerId(2);
        player2.setDisplayName("Player 2");
        gamePlayer2.setPlayerInfo(player1);
        gamePlayer2.setActivePlayer(false);
        gamePlayer2.setNextCards(new LinkedList<>());


        for(int i =0; i < 10; i++){
            CricketAthleteModel randomAthlete = getRandomElement(cricketAthleteModelList, selected);
            gamePlayer2.getNextCards().add(getCardDetails(randomAthlete.getPlayerId(), randomAthlete.getFullName(), randomAthlete.getTotalMatches(), randomAthlete.getTotalRuns(), randomAthlete.getTotalWickets()));
        }

        gamePlayer2.setCardCount(gamePlayer2.getNextCards().size());



        gameInfo.setPlayer2Info(gamePlayer2);

        return gameInfo;
    }

    private CricketAthleteModel getCardDetails(long playerId, String fullName, int matches, int runs, int wickets) {
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
            gameInfo.getPlayer1Info().setCurrentCard(gameInfo.getPlayer1Info().getNextCards().get(0));
            gameInfo.getPlayer1Info().getNextCards().remove(0);
            gameInfo.getPlayer2Info().setCurrentCard(gameInfo.getPlayer2Info().getNextCards().get(0));
            gameInfo.getPlayer2Info().getNextCards().remove(0);
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


    public boolean selectStat(long gameId, long playerId, int position) {
        if(liveGames.containsKey(gameId)){
            GameInfo gameInfo = liveGames.get(gameId);
            CricketAthleteModel player1card = gameInfo.getPlayer1Info().getCurrentCard();
            CricketAthleteModel player2card = gameInfo.getPlayer2Info().getCurrentCard();
            if(position == 1){
                if(player1card.getTotalMatches() >player2card.getTotalMatches()){
                    gameInfo.getPlayer1Info().getNextCards().add(player1card);
                    gameInfo.getPlayer1Info().getNextCards().add(player2card);
                    gameInfo.getPlayer1Info().setCardCount( gameInfo.getPlayer1Info().getCardCount()+1);
                    gameInfo.getPlayer2Info().setCardCount( gameInfo.getPlayer2Info().getCardCount()-1);
                    gameInfo.getPlayer1Info().setCurrentCard(gameInfo.getPlayer1Info().getNextCards().get(0));
                    gameInfo.getPlayer1Info().getNextCards().remove(0);
                    gameInfo.getPlayer2Info().setCurrentCard(gameInfo.getPlayer2Info().getNextCards().get(0));
                    gameInfo.getPlayer2Info().getNextCards().remove(0);
                    gameInfo.getPlayer1Info().setActivePlayer(true);
                    gameInfo.getPlayer2Info().setActivePlayer(false);
                }else{
                    gameInfo.getPlayer2Info().getNextCards().add(player1card);
                    gameInfo.getPlayer2Info().getNextCards().add(player2card);
                    gameInfo.getPlayer1Info().setCardCount( gameInfo.getPlayer1Info().getCardCount()-1);
                    gameInfo.getPlayer2Info().setCardCount( gameInfo.getPlayer2Info().getCardCount()+1);
                    gameInfo.getPlayer1Info().setCurrentCard(gameInfo.getPlayer1Info().getNextCards().get(0));
                    gameInfo.getPlayer1Info().getNextCards().remove(0);
                    gameInfo.getPlayer2Info().setCurrentCard(gameInfo.getPlayer2Info().getNextCards().get(0));
                    gameInfo.getPlayer2Info().getNextCards().remove(0);
                    gameInfo.getPlayer1Info().setActivePlayer(false);
                    gameInfo.getPlayer2Info().setActivePlayer(true);
                }
            }else if(position == 2){
                if(player1card.getTotalRuns() >player2card.getTotalRuns()){
                    gameInfo.getPlayer1Info().getNextCards().add(player1card);
                    gameInfo.getPlayer1Info().getNextCards().add(player2card);
                    gameInfo.getPlayer1Info().setCardCount( gameInfo.getPlayer1Info().getCardCount()+1);
                    gameInfo.getPlayer2Info().setCardCount( gameInfo.getPlayer2Info().getCardCount()-1);
                    gameInfo.getPlayer1Info().setCurrentCard(gameInfo.getPlayer1Info().getNextCards().get(0));
                    gameInfo.getPlayer1Info().getNextCards().remove(0);
                    gameInfo.getPlayer2Info().setCurrentCard(gameInfo.getPlayer2Info().getNextCards().get(0));
                    gameInfo.getPlayer2Info().getNextCards().remove(0);
                    gameInfo.getPlayer1Info().setActivePlayer(true);
                    gameInfo.getPlayer2Info().setActivePlayer(false);
                }else{
                    gameInfo.getPlayer2Info().getNextCards().add(player1card);
                    gameInfo.getPlayer2Info().getNextCards().add(player2card);
                    gameInfo.getPlayer1Info().setCardCount( gameInfo.getPlayer1Info().getCardCount()-1);
                    gameInfo.getPlayer2Info().setCardCount( gameInfo.getPlayer2Info().getCardCount()+1);
                    gameInfo.getPlayer1Info().setCurrentCard(gameInfo.getPlayer1Info().getNextCards().get(0));
                    gameInfo.getPlayer1Info().getNextCards().remove(0);
                    gameInfo.getPlayer2Info().setCurrentCard(gameInfo.getPlayer2Info().getNextCards().get(0));
                    gameInfo.getPlayer2Info().getNextCards().remove(0);
                    gameInfo.getPlayer1Info().setActivePlayer(false);
                    gameInfo.getPlayer2Info().setActivePlayer(true);
                }
            }else if(position == 3){
                if(player1card.getTotalWickets() >player2card.getTotalWickets()){
                    gameInfo.getPlayer1Info().getNextCards().add(player1card);
                    gameInfo.getPlayer1Info().getNextCards().add(player2card);
                    gameInfo.getPlayer1Info().setCardCount( gameInfo.getPlayer1Info().getCardCount()+1);
                    gameInfo.getPlayer2Info().setCardCount( gameInfo.getPlayer2Info().getCardCount()-1);
                    gameInfo.getPlayer1Info().setCurrentCard(gameInfo.getPlayer1Info().getNextCards().get(0));
                    gameInfo.getPlayer1Info().getNextCards().remove(0);
                    gameInfo.getPlayer2Info().setCurrentCard(gameInfo.getPlayer2Info().getNextCards().get(0));
                    gameInfo.getPlayer2Info().getNextCards().remove(0);
                    gameInfo.getPlayer1Info().setActivePlayer(true);
                    gameInfo.getPlayer2Info().setActivePlayer(false);
                }else{
                    gameInfo.getPlayer2Info().getNextCards().add(player1card);
                    gameInfo.getPlayer2Info().getNextCards().add(player2card);
                    gameInfo.getPlayer1Info().setCardCount( gameInfo.getPlayer1Info().getCardCount()-1);
                    gameInfo.getPlayer2Info().setCardCount( gameInfo.getPlayer2Info().getCardCount()+1);
                    gameInfo.getPlayer1Info().setCurrentCard(gameInfo.getPlayer1Info().getNextCards().get(0));
                    gameInfo.getPlayer1Info().getNextCards().remove(0);
                    gameInfo.getPlayer2Info().setCurrentCard(gameInfo.getPlayer2Info().getNextCards().get(0));
                    gameInfo.getPlayer2Info().getNextCards().remove(0);
                    gameInfo.getPlayer1Info().setActivePlayer(false);
                    gameInfo.getPlayer2Info().setActivePlayer(true);
                }
            }
            return true;
        }
        return false;
    }
}
