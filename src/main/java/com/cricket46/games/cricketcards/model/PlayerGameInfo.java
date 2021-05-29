package com.cricket46.games.cricketcards.model;

import java.util.List;

public class PlayerGameInfo {
    private boolean gameFinished;
    private int cardCount;
    private PlayerInfo playerInfo;

    private boolean activePlayer;

    private List<CricketAthleteModel> nextCards;

    private CricketAthleteModel currentCard;

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public boolean isActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(boolean activePlayer) {
        this.activePlayer = activePlayer;
    }

    public List<CricketAthleteModel> getNextCards() {
        return nextCards;
    }

    public void setNextCards(List<CricketAthleteModel> nextCards) {
        this.nextCards = nextCards;
    }

    public CricketAthleteModel getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(CricketAthleteModel currentCard) {
        this.currentCard = currentCard;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    @Override
    public String toString() {
        return "PlayerGameInfo{" +
                "gameFinished=" + gameFinished +
                ", cardCount=" + cardCount +
                ", playerInfo=" + playerInfo +
                ", activePlayer=" + activePlayer +
                ", nextCards=" + nextCards +
                ", currentCard=" + currentCard +
                '}';
    }
}
