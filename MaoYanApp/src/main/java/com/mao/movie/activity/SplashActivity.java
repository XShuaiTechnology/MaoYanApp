package com.mao.movie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gao.android.util.PreferencesUtils;
import com.mao.movie.R;
import com.mao.movie.consts.PrefConst;

/**
 * Created by GaoMatrix on 2016/10/24.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isFirstOpenApp = PreferencesUtils.getBoolean(this, PrefConst.FIRST_OPEN_APP, true);
        if (isFirstOpenApp) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
