package com.cricket46.games.cricketcards.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "cricket_players")
public class CricketAthleteAggregate {

    @Id
    private long playerId;
    private String fullName;

    public String getBestBowlingFigures() {
        return bestBowlingFigures;
    }

    public void setBestBowlingFigures(String bestBowlingFigures) {
        this.bestBowlingFigures = bestBowlingFigures;
    }

    private int totalRuns;
    private int totalWickets;
    private int totalMatches;

    @Override
    public String toString() {
        return "CricketAthleteAggregate{" +
                "playerId=" + playerId +
                ", fullName='" + fullName + '\'' +
                ", totalRuns=" + totalRuns +
                ", totalWickets=" + totalWickets +
                ", totalMatches=" + totalMatches +
                ", highestScore='" + highestScore + '\'' +
                ", bestBowlingFigures='" + bestBowlingFigures + '\'' +
                '}';
    }

    private String highestScore;
    private String bestBowlingFigures;

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

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public int getTotalWickets() {
        return totalWickets;
    }

    public String getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(String highestScore) {
        this.highestScore = highestScore;
    }

    public void setTotalWickets(int totalWickets) {
        this.totalWickets = totalWickets;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }
}
