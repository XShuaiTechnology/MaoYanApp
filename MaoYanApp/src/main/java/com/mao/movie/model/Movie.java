package com.mao.movie.model;

/**
 * Created by GaoMatrix on 2016/10/27.
 */
public class Movie {
    private String imageUrl;
    private String name;
    private String rating;
    private String directors;
    private String actors;
    private String time;

    public Movie(String imageUrl, String name, String rating, String directors, String actors, String time) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.rating = rating;
        this.directors = directors;
        this.actors = actors;
        this.time = time;
    }

    public Movie(String name, String rating, String directors, String actors, String time) {
        this.name = name;
        this.rating = rating;
        this.directors = directors;
        this.actors = actors;
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
