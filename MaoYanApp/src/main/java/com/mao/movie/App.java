package com.mao.movie;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;

/**
 * Created by GaoMatrix on 2016/10/24.
 */
public class App extends Application{

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //微信 appid appsecret
        PlatformConfig.setWeixin("wx6b92efe46c83d339", "25e5a842a6385cd4623db051675f968b");
    }

    public static App getInstance() {
        return sInstance;
    }
}
