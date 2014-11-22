package com.zoopa.brain.model;

public class Series {
    private long id;
    private String name;
    private int season;
    private int episode;

    public Series(String name, int season, int episode) {
        this.name = name;
        this.season = season;
        this.episode = episode;
    }

    public Series(int id, String name, int season, int episode) {
        this.id = id;
        this.name = name;
        this.season = season;
        this.episode = episode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season > 0 ? season : 1;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode > 0 ? episode : 1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                this.name +
                ";" +
                this.season +
                ";" +
                this.episode;
    }
}
