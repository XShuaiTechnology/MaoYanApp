package com.mao.movie.service;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.net.URISyntaxException;

/**
 * Created by GaoMatrix on 2016/11/17.
 */
public class PushMovieService extends Service {
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("onCreate");
        registerMessageReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("onDestory");
        unRegisterMessageReceiver();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public void unRegisterMessageReceiver() {
        unregisterReceiver(mMessageReceiver);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                Toast.makeText(context, messge, Toast.LENGTH_LONG).show();

                try {
                    Intent intent1 = Intent.getIntent(messge);
                    startActivity(intent1);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }

              /*if (!ExampleUtil.isEmpty(extras)) {
                  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
              }
              setCostomMsg(showMsg.toString());*/
            }
        }
    }
}
