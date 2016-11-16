package com.mao.movie;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by GaoMatrix on 2016/10/24.
 */
public class App extends Application{
    public String regId = "";
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //微信 appid appsecret
        PlatformConfig.setWeixin("wx6b92efe46c83d339", "25e5a842a6385cd4623db051675f968b");

        // 极光
        JPushInterface.setDebugMode(true);     // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);             // 初始化 JPush

        // zxing
        ZXingLibrary.initDisplayOpinion(this);
    }

    public static App getInstance() {
        return sInstance;
    }
}
