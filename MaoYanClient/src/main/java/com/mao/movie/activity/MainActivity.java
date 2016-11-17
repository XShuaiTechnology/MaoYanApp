package com.mao.movie.activity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mao.movie.App;
import com.mao.movie.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.QRContentEditText)
    EditText mQRContentEditText;
    @BindView(R.id.makeQRLogoButton)
    Button mMakeQRLogoButton;
    @BindView(R.id.makeQRButton)
    Button mMakeQRButton;
    @BindView(R.id.QRContentImageView)
    ImageView mQRContentImageView;

    private Bitmap mBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        registerMessageReceiver();
    }

    @OnClick({R.id.makeQRLogoButton, R.id.makeQRButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.makeQRLogoButton:
                makeQRLogo();
                break;
            case R.id.makeQRButton:
                makeQR();
                break;
        }
    }

    private void makeQR() {
        mQRContentEditText.setText(App.sInstance.regId);
        String textContent = mQRContentEditText.getText().toString();
        if (TextUtils.isEmpty(textContent)) {
            Toast.makeText(MainActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        mQRContentEditText.setText("");
        mBitmap = CodeUtils.createImage(textContent, 400, 400, null);
        mQRContentImageView.setImageBitmap(mBitmap);
    }

    private void makeQRLogo() {
        mQRContentEditText.setText(App.sInstance.regId);
        String textContent = mQRContentEditText.getText().toString();
        if (TextUtils.isEmpty(textContent)) {
            Toast.makeText(MainActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        mQRContentEditText.setText("");
        mBitmap = CodeUtils.createImage(textContent, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mQRContentImageView.setImageBitmap(mBitmap);
    }

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
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
