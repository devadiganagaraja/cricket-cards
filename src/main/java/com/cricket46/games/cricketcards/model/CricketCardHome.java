package com.cricket46.games.cricketcards.model;

import java.util.List;

public class CricketCardHome {
    private User player;
    List<User> friends;
    List<GameRequest> gameRequests;

    List<GameHistory> gameHistory;

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "CricketCardHome{" +
                "player=" + player +
                ", friends=" + friends +
                ", gameRequests=" + gameRequests +
                ", gameHistory=" + gameHistory +
                '}';
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }


    public List<GameRequest> getGameRequests() {
        return gameRequests;
    }

    public void setGameRequests(List<GameRequest> gameRequests) {
        this.gameRequests = gameRequests;
    }

    public List<GameHistory> getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(List<GameHistory> gameHistory) {
        this.gameHistory = gameHistory;
    }
}
