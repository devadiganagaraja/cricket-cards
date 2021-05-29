package com.cricket46.games.cricketcards.model;

public class CricketAthleteModel {
    private long playerId;
    private String fullName;

    private int totalMatches;
    private int totalRuns;
    private int totalWickets;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public int getTotalWickets() {
        return totalWickets;
    }

    public void setTotalWickets(int totalWickets) {
        this.totalWickets = totalWickets;
    }

    @Override
    public String toString() {
        return "CricketAthleteModel{" +
                "playerId=" + playerId +
                ", fullName='" + fullName + '\'' +
                ", totalMatches=" + totalMatches +
                ", totalRuns=" + totalRuns +
                ", totalWickets=" + totalWickets +
                '}';
    }
}
