package com.cricket46.games.cricketcards.domain;

public class Team {
    private long id;
    private String name;
    private Ref athletes;
    private String location;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ref getAthletes() {
        return athletes;
    }

    public void setAthletes(Ref athletes) {
        this.athletes = athletes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
