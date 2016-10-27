package com.mao.movie.model;

/**
 * Created by GaoMatrix on 2016/10/27.
 */
public class Article {
    private String imageUrl;
    private String title;

    public Article(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public Article(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
