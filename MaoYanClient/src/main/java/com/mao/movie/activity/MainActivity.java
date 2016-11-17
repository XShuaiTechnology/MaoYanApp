package com.mao.movie.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.gao.android.util.PreferencesUtils;
import com.mao.movie.R;
import com.mao.movie.service.PushMovieService;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.QRContentImageView)
    ImageView mQRContentImageView;

    private ProgressDialog mProgressDialog;

    private Bitmap mBitmap = null;
    private String mRegID = "";

    private static final int MESSAGE_GET_REGID_FAIL = 0;
    private static final int MESSAGE_GET_REGID_SUCCESS = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_GET_REGID_FAIL:
                    Toast.makeText(MainActivity.this, "生成二维码失败，点击请重新生成", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    break;
                case MESSAGE_GET_REGID_SUCCESS:
                    makeQR(mRegID);
                    startPushMovieService();
                    mProgressDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void startPushMovieService() {
        Intent intent = new Intent(this, PushMovieService.class);
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressDialog = ProgressDialog.show(this,
                "正在生成二维码", "请稍等", true, false);
        mProgressDialog.setCancelable(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mRegID = PreferencesUtils.getString(MainActivity.this, "REG_ID");
                if (TextUtils.isEmpty(mRegID)) {
                    mRegID = JPushInterface.getRegistrationID(MainActivity.this);
                }
                if (TextUtils.isEmpty(mRegID)) {
                    mHandler.sendEmptyMessage(MESSAGE_GET_REGID_FAIL);
                } else {
                    mHandler.sendEmptyMessage(MESSAGE_GET_REGID_SUCCESS);
                }
            }
        }).start();

    }

    private void makeQR(String regID) {
        mBitmap = CodeUtils.createImage(regID, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mQRContentImageView.setImageBitmap(mBitmap);
        mHandler.sendEmptyMessage(MESSAGE_GET_REGID_SUCCESS);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
