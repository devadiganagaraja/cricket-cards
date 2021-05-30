package com.cricket46.games.cricketcards.model;

import java.util.List;

public class GameInfo {
    private String gameId;
    private PlayerGameInfo player1Info;
    private PlayerGameInfo player2Info;

    @Override
    public String toString() {
        return "GameInfo{" +
                "gameId=" + gameId +
                ", player1Info=" + player1Info +
                ", player2Info=" + player2Info +
                '}';
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public PlayerGameInfo getPlayer1Info() {
        return player1Info;
    }

    public void setPlayer1Info(PlayerGameInfo player1Info) {
        this.player1Info = player1Info;
    }

    public PlayerGameInfo getPlayer2Info() {
        return player2Info;
    }

    public void setPlayer2Info(PlayerGameInfo player2Info) {
        this.player2Info = player2Info;
    }
}
