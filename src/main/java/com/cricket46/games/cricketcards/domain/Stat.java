package com.cricket46.games.cricketcards.domain;

public class Stat {
    private String name;
    private int value;
    private String displayValue;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    @Override
    public String toString() {
        return "Stat{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", displayValue='" + displayValue + '\'' +
                '}';
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

}
