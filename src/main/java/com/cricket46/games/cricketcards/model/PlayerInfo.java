package com.cricket46.games.cricketcards.model;

public class PlayerInfo {
    private String displayName;
    private int playerId;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "displayName='" + displayName + '\'' +
                ", playerId=" + playerId +
                '}';
    }
}
