package com.mao.movie;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by GaoMatrix on 2016/11/16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // zxing
        ZXingLibrary.initDisplayOpinion(this);
    }
}

