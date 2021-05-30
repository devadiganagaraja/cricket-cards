package com.cricket46.games.cricketcards.model;

public class PlayerInfo {
    private String displayName;
    private long playerId;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
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
