package com.gao.android.rxjavaretrofit;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by GaoMatrix on 2016/10/24.
 */
public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // Logger
        Logger.init("Gao")                 // default PRETTYLOGGER or use just init()
                .methodCount(1)                 // default 2
                .hideThreadInfo()  ;             // default shown
        //.logLevel(LogLevel.NONE)        // default LogLevel.FULL
        //.methodOffset(2);                // default 0
        //.logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
    }

    public static App getInstance() {
        return sInstance;
    }
}
