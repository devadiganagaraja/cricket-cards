package com.cricket46.games.cricketcards.domain;

import java.util.List;

public class Splits {
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Splits{" +
                "categories=" + categories +
                '}';
    }
}
