package com.cricket46.games.cricketcards.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "cricket_players")
public class CricketAthleteAggregate {

    @Id
    private long playerId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String dateOfBirth;

    private int totalRuns;
    private int totalWickets;
    private int totalMatches;
    private int highestScore;
    private int bestBowling;
    private int catches;
    private int stumps;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getBestBowling() {
        return bestBowling;
    }

    public void setBestBowling(int bestBowling) {
        this.bestBowling = bestBowling;
    }

    public int getCatches() {
        return catches;
    }

    public void setCatches(int catches) {
        this.catches = catches;
    }

    public int getStumps() {
        return stumps;
    }

    public void setStumps(int stumps) {
        this.stumps = stumps;
    }
}
