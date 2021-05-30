package com.cricket46.games.cricketcards.services;

import com.cricket46.games.cricketcards.model.*;
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

    @Autowired
    CricketCardUserService cricketCardUserService;

    public CricketAthleteModel getRandomElement(List<CricketAthleteModel> cricketAthleteModelList, Set<Integer> selected)
    {
        Random rand = new Random();
        int random = rand.nextInt(cricketAthleteModelList.size());
        while(selected.contains(random)) random = rand.nextInt(cricketAthleteModelList.size());

        selected.add(random);
        return cricketAthleteModelList.get(random);
    }


    public GameInfo createGame(long gameId, long player1Id, long player2Id) {


        GameInfo gameInfo = new GameInfo();

        gameInfo.setGameId(gameId);


        List<CricketAthleteModel> cricketAthleteModelList = cricketAthleteService.fetchCricketAthletes().stream().filter(cricketAthleteModel -> cricketAthleteModel.getTotalMatches() > 100).collect(Collectors.toList());

        Set<Integer> selected = new HashSet<>();

        PlayerGameInfo gamePlayer1 = new PlayerGameInfo();
        gamePlayer1.setActivePlayer(true);
        PlayerInfo player1 = new PlayerInfo();
        player1.setPlayerId(player1Id);
        User user1 = cricketCardUserService.getUser(player1Id);

        player1.setDisplayName(user1.getUserName());
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
        player2.setPlayerId(player2Id);
        User user2 = cricketCardUserService.getUser(player2Id);

        player2.setDisplayName(user2.getUserName());
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


    public PlayerGameInfo getGamePageInfo(long  gameId, long player1Id, long player2Id) {

        GameInfo gameInfo = liveGames.get(gameId);


        if(null == gameInfo){
            gameInfo = createGame(gameId, player1Id, player2Id);
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

        if(player1Id == gameInfo.getPlayer1Info().getPlayerInfo().getPlayerId())
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

                    gameInfo.getPlayer1Info().setLastResultMessage("You Won!");
                    gameInfo.getPlayer2Info().setLastResultMessage("You Lost!");

                    StringBuilder player1Str = new StringBuilder("");
                    player1Str.append("Your card: "+player1card.getFullName()).append(" [").append(player1card.getTotalMatches()).append(" ] ")
                            .append( " won against ").append(player2card.getFullName()).append(" [").append(player2card.getTotalMatches()).append(" ] on matches.");

                    StringBuilder player2Str = new StringBuilder("");
                    player2Str.append("Your card: "+player2card.getFullName()).append(" [").append(player2card.getTotalMatches()).append(" ] ")
                            .append( " lost against ").append(player1card.getFullName()).append(" [").append(player1card.getTotalMatches()).append(" ] on matches.");

                    gameInfo.getPlayer1Info().setLastResultDescription(player1Str.toString());
                    gameInfo.getPlayer2Info().setLastResultDescription(player2Str.toString());
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

                    gameInfo.getPlayer2Info().setLastResultMessage("You Won!");
                    gameInfo.getPlayer1Info().setLastResultMessage("You Lost!");

                    StringBuilder player1Str = new StringBuilder("");
                    player1Str.append("Your card: "+player1card.getFullName()).append(" [").append(player1card.getTotalMatches()).append(" ] ")
                            .append( " lost against ").append(player2card.getFullName()).append(" [").append(player2card.getTotalMatches()).append(" ]  on matches.");

                    StringBuilder player2Str = new StringBuilder("");
                    player2Str.append("Your card: "+player2card.getFullName()).append(" [").append(player2card.getTotalMatches()).append(" ] ")
                            .append( " won against ").append(player1card.getFullName()).append(" [").append(player1card.getTotalMatches()).append(" ] on matches.");

                    gameInfo.getPlayer1Info().setLastResultDescription(player1Str.toString());
                    gameInfo.getPlayer2Info().setLastResultDescription(player2Str.toString());
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

                    gameInfo.getPlayer1Info().setLastResultMessage("You Won!");
                    gameInfo.getPlayer2Info().setLastResultMessage("You Lost!");

                    StringBuilder player1Str = new StringBuilder("");
                    player1Str.append("Your card: "+player1card.getFullName()).append(" [").append(player1card.getTotalRuns()).append(" ] ")
                            .append( " won against ").append(player2card.getFullName()).append(" [").append(player2card.getTotalRuns()).append(" ]  on runs.");

                    StringBuilder player2Str = new StringBuilder("");
                    player2Str.append("Your card: "+player2card.getFullName()).append(" [").append(player2card.getTotalRuns()).append(" ] ")
                            .append( " lost against ").append(player1card.getFullName()).append(" [").append(player1card.getTotalRuns()).append(" ] on runs.");

                    gameInfo.getPlayer1Info().setLastResultDescription(player1Str.toString());
                    gameInfo.getPlayer2Info().setLastResultDescription(player2Str.toString());
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


                    gameInfo.getPlayer2Info().setLastResultMessage("You Won!");
                    gameInfo.getPlayer1Info().setLastResultMessage("You Lost!");

                    StringBuilder player1Str = new StringBuilder("");
                    player1Str.append("Your card: "+player1card.getFullName()).append(" [").append(player1card.getTotalRuns()).append(" ] ")
                            .append( " lost against ").append(player2card.getFullName()).append(" [").append(player2card.getTotalRuns()).append(" ] on runs.");

                    StringBuilder player2Str = new StringBuilder("");
                    player2Str.append("Your card: "+player2card.getFullName()).append(" [").append(player2card.getTotalRuns()).append(" ] ")
                            .append( " won against ").append(player1card.getFullName()).append(" [").append(player1card.getTotalRuns()).append(" ] on runs.");

                    gameInfo.getPlayer1Info().setLastResultDescription(player1Str.toString());
                    gameInfo.getPlayer2Info().setLastResultDescription(player2Str.toString());
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

                    gameInfo.getPlayer1Info().setLastResultMessage("You Won!");
                    gameInfo.getPlayer2Info().setLastResultMessage("You Lost!");

                    StringBuilder player1Str = new StringBuilder("");
                    player1Str.append("Your card: "+player1card.getFullName()).append(" [").append(player1card.getTotalWickets()).append(" ] ")
                            .append( " won against ").append(player2card.getFullName()).append(" [").append(player2card.getTotalWickets()).append(" ] on wickets.");

                    StringBuilder player2Str = new StringBuilder("");
                    player2Str.append("Your card: "+player2card.getFullName()).append(" [").append(player2card.getTotalWickets()).append(" ] ")
                            .append( " lost against ").append(player1card.getFullName()).append(" [").append(player1card.getTotalWickets()).append(" ] on wickets.");

                    gameInfo.getPlayer1Info().setLastResultDescription(player1Str.toString());
                    gameInfo.getPlayer2Info().setLastResultDescription(player2Str.toString());
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


                    gameInfo.getPlayer2Info().setLastResultMessage("You Won!");
                    gameInfo.getPlayer1Info().setLastResultMessage("You Lost!");

                    StringBuilder player1Str = new StringBuilder("");
                    player1Str.append("Your card: "+player1card.getFullName()).append(" [").append(player1card.getTotalWickets()).append(" ] ")
                            .append( " lost against ").append(player2card.getFullName()).append(" [").append(player2card.getTotalWickets()).append(" ] on wickets.");

                    StringBuilder player2Str = new StringBuilder("");
                    player2Str.append("Your card: "+player2card.getFullName()).append(" [").append(player2card.getTotalWickets()).append(" ] ")
                            .append( " won against ").append(player1card.getFullName()).append(" [").append(player1card.getTotalWickets()).append(" ] on wickets.");

                    gameInfo.getPlayer1Info().setLastResultDescription(player1Str.toString());
                    gameInfo.getPlayer2Info().setLastResultDescription(player2Str.toString());
                }
            }
            return true;
        }
        return false;
    }
}
