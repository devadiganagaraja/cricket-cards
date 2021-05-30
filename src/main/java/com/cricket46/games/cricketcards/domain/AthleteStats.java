package com.cricket46.games.cricketcards.domain;

public class AthleteStats {
    private Splits splits;

    public Splits getSplits() {
        return splits;
    }

    public void setSplits(Splits splits) {
        this.splits = splits;
    }

    @Override
    public String toString() {
        return "AthleteStats{" +
                "splits=" + splits +
                '}';
    }
}
