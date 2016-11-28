package com.gao.android.rxjavaretrofit.model;

/**
 * Created by GaoMatrix on 2016/11/26.
 *
 * Simple data class, keeps track of when it was created
 * so that it knows when the its gone stale.
 *
 */
public class Data {
    public static final long STALE_MS = 5 * 1000;// Data is stale after 5 seconds

    public final String value;

    private final long timestamp;

    public Data(String value) {
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isUpToDate() {
        return System.currentTimeMillis() - timestamp < STALE_MS;
    }
}
