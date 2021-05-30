package com.cricket46.games.cricketcards.model;

public class GameRequest {
    private String gameId;
    private String opponentName;
    private long opponentId;
    private String gameRef;


    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getGameRef() {
        return gameRef;
    }

    public void setGameRef(String gameRef) {
        this.gameRef = gameRef;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public long getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(long opponentId) {
        this.opponentId = opponentId;
    }
}
