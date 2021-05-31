package com.cricket46.games.cricketcards.model;

import java.util.Date;

public class GameRequest {
    private String gameId;
    private String opponentName;
    private long opponentId;
    private String gameRef;
    private Date requestDate;


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

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
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
