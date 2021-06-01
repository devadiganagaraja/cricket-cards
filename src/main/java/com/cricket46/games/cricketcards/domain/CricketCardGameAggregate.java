package com.cricket46.games.cricketcards.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.jdo.annotations.Index;
import java.util.Date;

@Document(collection = "cricket_cards_games")
public class CricketCardGameAggregate {

    @Id
    public String gameId;

    @Indexed
    public long player1;
    private String player1Name;

    @Indexed
    public long player2;

    private String player2Name;

    public long winner;
    private String winnerName;

    private Date date;

    private int player1CardCount;

    private int player2CardCount;

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public long getPlayer1() {
        return player1;
    }

    public void setPlayer1(long player1) {
        this.player1 = player1;
    }

    public int getPlayer1CardCount() {
        return player1CardCount;
    }

    public void setPlayer1CardCount(int player1CardCount) {
        this.player1CardCount = player1CardCount;
    }

    public int getPlayer2CardCount() {
        return player2CardCount;
    }

    public void setPlayer2CardCount(int player2CardCount) {
        this.player2CardCount = player2CardCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getPlayer2() {
        return player2;
    }

    public void setPlayer2(long player2) {
        this.player2 = player2;
    }

    public long getWinner() {
        return winner;
    }

    public void setWinner(long winner) {
        this.winner = winner;
    }
}
