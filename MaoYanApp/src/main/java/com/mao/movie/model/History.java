package com.mao.movie.model;

/**
 * Created by GaoMatrix on 2016/10/27.
 */
public class History {

    private String name;
    private String lastWatchTime;

    public History(String name, String lastWatchTime) {
        this.name = name;
        this.lastWatchTime = lastWatchTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastWatchTime() {
        return lastWatchTime;
    }

    public void setLastWatchTime(String lastWatchTime) {
        this.lastWatchTime = lastWatchTime;
    }
}
