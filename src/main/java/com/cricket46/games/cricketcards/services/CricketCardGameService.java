package com.cricket46.games.cricketcards.services;

import com.cricket46.games.cricketcards.domain.CricketCardGameAggregate;
import com.cricket46.games.cricketcards.domain.QCricketCardGameAggregate;
import com.cricket46.games.cricketcards.model.*;
import com.cricket46.games.cricketcards.repository.CricketCardGameRepository;
import com.cricket46.games.cricketcards.utils.CricketCardGameUtils;
import com.mysema.codegen.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CricketCardGameService {


    @Autowired
    Map<String, GameInfo> liveGames;

    Random random = new Random();

    @Autowired
    CricketAthleteService cricketAthleteService;

    @Autowired
    CricketCardUserService cricketCardUserService;


    @Autowired
    CricketCardGameRepository cricketCardGameRepository;

    @Autowired
    QCricketCardGameAggregate qCricketCardGameAggregate;

    public CricketAthleteModel getRandomElement(List<CricketAthleteModel> cricketAthleteModelList, Set<Integer> selected)
    {
        Random rand = new Random();
        int random = rand.nextInt(cricketAthleteModelList.size());
        while(selected.contains(random)) random = rand.nextInt(cricketAthleteModelList.size());

        selected.add(random);
        return cricketAthleteModelList.get(random);
    }


    public GameInfo createGame(long player1Id, long player2Id) {


        GameInfo gameInfo = new GameInfo();




        List<CricketAthleteModel> cricketAthleteModelList = cricketAthleteService.fetchCricketAthletes().stream().filter(cricketAthleteModel -> cricketAthleteModel.getTotalMatches() > 100).collect(Collectors.toList());

        Set<Integer> selected = new HashSet<>();

        PlayerGameInfo gamePlayer1 = new PlayerGameInfo();
        gamePlayer1.setActivePlayer(true);
        PlayerInfo player1 = new PlayerInfo();
        player1.setPlayerId(player1Id);
        User user1 = cricketCardUserService.getUser(player1Id);

        player1.setDisplayName(user1.getUserName());
        gamePlayer1.setPlayerInfo(player1);
        if(random.nextBoolean())
            gamePlayer1.setActivePlayer(true);
        gamePlayer1.setNextCards(new LinkedList<>());

        for(int i =0; i < 6; i++){
            CricketAthleteModel randomAthlete = getRandomElement(cricketAthleteModelList, selected);
            gamePlayer1.getNextCards().add(getCardDetails(randomAthlete.getPlayerId(), randomAthlete.getFullName(), randomAthlete.getTotalMatches(), randomAthlete.getTotalRuns(), randomAthlete.getTotalWickets(), randomAthlete.getHighestScore(), randomAthlete.getBestBowling()));
        }

        gamePlayer1.setCardCount(gamePlayer1.getNextCards().size());

        if(gamePlayer1.getNextCards() != null && gamePlayer1.getNextCards().size()  > 0) {
            gamePlayer1.setCurrentCard(gamePlayer1.getNextCards().get(0));
            gamePlayer1.getNextCards().remove(0);
        }

        gameInfo.setPlayer1Info(gamePlayer1);

        PlayerGameInfo gamePlayer2 = new PlayerGameInfo();
        PlayerInfo player2 = new PlayerInfo();
        player2.setPlayerId(player2Id);
        User user2 = cricketCardUserService.getUser(player2Id);

        player2.setDisplayName(user2.getUserName());
        gamePlayer2.setPlayerInfo(player1);
        gamePlayer2.setActivePlayer(!gamePlayer1.isActivePlayer());
        gamePlayer2.setNextCards(new LinkedList<>());


        CricketCardGameAggregate cricketCardGameAggregate = new CricketCardGameAggregate();
        cricketCardGameAggregate.setPlayer1(player1Id);
        cricketCardGameAggregate.setPlayer2(player2Id);

        cricketCardGameAggregate.setPlayer1Name(user1.getUserName());
        cricketCardGameAggregate.setPlayer2Name(user2.getUserName());
        cricketCardGameAggregate.setDate(new Date());



        cricketCardGameAggregate =cricketCardGameRepository.save(cricketCardGameAggregate);

        gameInfo.setGameId(cricketCardGameAggregate.getGameId());


        for(int i =0; i < 6; i++){
            CricketAthleteModel randomAthlete = getRandomElement(cricketAthleteModelList, selected);
            gamePlayer2.getNextCards().add(getCardDetails(randomAthlete.getPlayerId(), randomAthlete.getFullName(), randomAthlete.getTotalMatches(), randomAthlete.getTotalRuns(), randomAthlete.getTotalWickets(), randomAthlete.getHighestScore(), randomAthlete.getBestBowling()));
        }

        gamePlayer2.setCardCount(gamePlayer2.getNextCards().size());



        if(gamePlayer2.getNextCards() != null && gamePlayer2.getNextCards().size()  > 0) {

            gamePlayer2.setCurrentCard(gamePlayer2.getNextCards().get(0));
            gamePlayer2.getNextCards().remove(0);
        }



        gameInfo.setPlayer2Info(gamePlayer2);

        return gameInfo;
    }

    private CricketAthleteModel getCardDetails(long playerId, String fullName, int matches, int runs, int wickets, String highestScore, String bestBowling) {
        CricketAthleteModel cricketAthleteModel = new CricketAthleteModel();
        cricketAthleteModel.setPlayerId(playerId);
        cricketAthleteModel.setFullName(fullName);
        cricketAthleteModel.setTotalMatches(matches);
        cricketAthleteModel.setTotalRuns(runs);
        cricketAthleteModel.setTotalWickets(wickets);
        cricketAthleteModel.setHighestScore(highestScore);
        cricketAthleteModel.setBestBowling(bestBowling);
        return cricketAthleteModel;
    }


    public PlayerGameInfo getGamePageInfo(String  gameId, long player1Id, long player2Id) {

        GameInfo gameInfo = null;
        if(gameId == null && gameId.trim().length() == 0 && !liveGames.containsKey(gameId)) {

            throw  new RuntimeException("Game not setup properly....");

        }

        gameInfo = liveGames.get(gameId);




        if(gameInfo.getPlayer1Info().getCardCount() ==0 || gameInfo.getPlayer2Info().getCardCount() == 0) {
            gameInfo.getPlayer1Info().setGameFinished(true);
            gameInfo.getPlayer2Info().setGameFinished(true);
            Optional<CricketCardGameAggregate> cricketCardGameAggregateOpt = cricketCardGameRepository.findOne(qCricketCardGameAggregate.gameId.eq(gameId));
            if(cricketCardGameAggregateOpt.isPresent()){
                CricketCardGameAggregate cricketCardGameAggregate = cricketCardGameAggregateOpt.get();
                if(gameInfo.getPlayer1Info().getCardCount() ==0){
                    cricketCardGameAggregate.setWinner(gameInfo.getPlayer2Info().getPlayerInfo().getPlayerId());
                    cricketCardGameAggregate.setWinnerName(gameInfo.getPlayer2Info().getPlayerInfo().getDisplayName());
                }else{
                    cricketCardGameAggregate.setWinner(gameInfo.getPlayer1Info().getPlayerInfo().getPlayerId());
                    cricketCardGameAggregate.setWinnerName(gameInfo.getPlayer1Info().getPlayerInfo().getDisplayName());
                }

                cricketCardGameAggregate.setPlayer1CardCount(gameInfo.getPlayer1Info().getCardCount());
                cricketCardGameAggregate.setPlayer2CardCount(gameInfo.getPlayer2Info().getCardCount());
                cricketCardGameRepository.save(cricketCardGameAggregate);
            }
        }

        System.out.println("gameInfo: "+gameInfo);

        if(player1Id == gameInfo.getPlayer1Info().getPlayerInfo().getPlayerId())
            return gameInfo.getPlayer1Info();
        else
            return gameInfo.getPlayer2Info();

    }


    public boolean selectStat(String gameId, long playerId, int position) {
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
            } else if(position == 4){
                int player1Score = Integer.valueOf(player1card.getHighestScore().replace("*", ""));
                int player2Score = Integer.valueOf(player2card.getHighestScore().replace("*", ""));

                boolean check =false;
                if(player1Score > player2Score) check =true;
                else if(player1Score < player2Score) check =false;
                else if(player1card.getHighestScore().contains("*")) check=true;



                if(check){
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
                    player1Str.append("Your card: "+player1card.getFullName()).append(" [").append(player1card.getHighestScore()).append(" ] ")
                            .append( " won against ").append(player2card.getFullName()).append(" [").append(player2card.getHighestScore()).append(" ] on Highest Score.");

                    StringBuilder player2Str = new StringBuilder("");
                    player2Str.append("Your card: "+player2card.getFullName()).append(" [").append(player2card.getHighestScore()).append(" ] ")
                            .append( " lost against ").append(player1card.getFullName()).append(" [").append(player1card.getHighestScore()).append(" ] on Highest Score.");

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
                    player1Str.append("Your card: "+player1card.getFullName()).append(" [").append(player1card.getHighestScore()).append(" ] ")
                            .append( " lost against ").append(player2card.getFullName()).append(" [").append(player2card.getHighestScore()).append(" ] on Highest Score.");

                    StringBuilder player2Str = new StringBuilder("");
                    player2Str.append("Your card: "+player2card.getFullName()).append(" [").append(player2card.getHighestScore()).append(" ] ")
                            .append( " won against ").append(player1card.getFullName()).append(" [").append(player1card.getHighestScore()).append(" ] on Highest Score.");

                    gameInfo.getPlayer1Info().setLastResultDescription(player1Str.toString());
                    gameInfo.getPlayer2Info().setLastResultDescription(player2Str.toString());
                }
            }


            else if(position == 5){
                if(player1card.getBestBowling().compareTo(player2card.getBestBowling()) > 0){
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
                    player1Str.append("Your card: "+player1card.getFullName()).append(" [").append(player1card.getBestBowling()).append(" ] ")
                            .append( " won against ").append(player2card.getFullName()).append(" [").append(player2card.getBestBowling()).append(" ] on Best Bowling.");

                    StringBuilder player2Str = new StringBuilder("");
                    player2Str.append("Your card: "+player2card.getFullName()).append(" [").append(player2card.getBestBowling()).append(" ] ")
                            .append( " lost against ").append(player1card.getFullName()).append(" [").append(player1card.getBestBowling()).append(" ] on Best Bowling.");

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
                    player1Str.append("Your card: "+player1card.getFullName()).append(" [").append(player1card.getBestBowling()).append(" ] ")
                            .append( " lost against ").append(player2card.getFullName()).append(" [").append(player2card.getBestBowling()).append(" ] on Best Bowling.");

                    StringBuilder player2Str = new StringBuilder("");
                    player2Str.append("Your card: "+player2card.getFullName()).append(" [").append(player2card.getBestBowling()).append(" ] ")
                            .append( " won against ").append(player1card.getFullName()).append(" [").append(player1card.getBestBowling()).append(" ] on Best Bowling.");

                    gameInfo.getPlayer1Info().setLastResultDescription(player1Str.toString());
                    gameInfo.getPlayer2Info().setLastResultDescription(player2Str.toString());
                }
            }
            return true;
        }
        return false;
    }


    public List<GameRequest> getGameRequests(long playerId){



        Iterable<CricketCardGameAggregate> cricketCardGamItr = cricketCardGameRepository.findAll(qCricketCardGameAggregate.player2.eq(playerId).and(qCricketCardGameAggregate.winner.loe(0)));

        List<GameRequest> gameRequests = new ArrayList<>();
        Iterator<CricketCardGameAggregate> it = cricketCardGamItr.iterator();

        while (it.hasNext()){
            CricketCardGameAggregate gameAggregate = it.next();
            if(gameAggregate.getDate() != null && DateUtils.isSameDay(gameAggregate.getDate(), new Date())) {
                GameRequest gameRequest = new GameRequest();
                gameRequest.setGameId(gameAggregate.getGameId());
                gameRequest.setOpponentId(gameAggregate.getPlayer1());
                gameRequest.setOpponentName(gameAggregate.getPlayer1Name());
                gameRequest.setGameRef("/games/" + gameAggregate.gameId + "/player1/" + gameAggregate.getPlayer1() + "/player2/" + gameAggregate.getPlayer2());
                gameRequest.setRequestDate(gameAggregate.getDate());
                gameRequests.add(gameRequest);
            }
        }

        gameRequests.sort(Comparator.comparing(GameRequest::getRequestDate, Comparator.reverseOrder()));

        return gameRequests;

    }

    public List<GameHistory> getGameHistory(long playerId) {
        Iterable<CricketCardGameAggregate> cricketCardGamItr = cricketCardGameRepository.findAll(qCricketCardGameAggregate.winner.gt(0).and(qCricketCardGameAggregate.player1.eq(playerId).or(qCricketCardGameAggregate.player2.eq(playerId))));

        List<GameHistory> gameHistories = new ArrayList<>();
        Iterator<CricketCardGameAggregate> it = cricketCardGamItr.iterator();

        while (it.hasNext()){
            CricketCardGameAggregate gameAggregate = it.next();
            if(gameAggregate.getDate() != null) {
                GameHistory gameHistory = new GameHistory();
                gameHistory.setGameId(gameAggregate.getGameId());
                gameHistory.setPlayer1Name(gameAggregate.getPlayer1Name());
                gameHistory.setPlayer2Name(gameAggregate.getPlayer2Name());
                gameHistory.setWinnerName(gameAggregate.getWinnerName());
                gameHistory.setDate(gameAggregate.getDate());
                gameHistories.add(gameHistory);
            }
        }

        gameHistories.sort(Comparator.comparing(GameHistory::getDate, Comparator.reverseOrder()));
        gameHistories = gameHistories.stream().limit(10).collect(Collectors.toList());

        return gameHistories;
    }

    public String inviteFriend(long player1Id, long player2Id) {
        GameInfo gameInfo = createGame(player1Id, player2Id);
        liveGames.put(gameInfo.getGameId(), gameInfo);

        return "/games/"+gameInfo.getGameId()+"/player1/"+player2Id+"/player2/"+player1Id;

    }
}
