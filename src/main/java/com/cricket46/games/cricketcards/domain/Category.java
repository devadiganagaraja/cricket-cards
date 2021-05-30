package com.cricket46.games.cricketcards.domain;

import java.util.List;

public class Category {
    private List<Stat> stats;

    public List<Stat> getStats() {
        return stats;
    }

    public void setStats(List<Stat> stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "Category{" +
                "stats=" + stats +
                '}';
    }
}
