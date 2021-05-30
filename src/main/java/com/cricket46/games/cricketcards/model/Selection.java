package com.cricket46.games.cricketcards.model;

public class Selection {
    private long gameId;
    private long playerId;
    private int position;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Selection{" +
                "gameId=" + gameId +
                ", playerId=" + playerId +
                ", position=" + position +
                '}';
    }
}
