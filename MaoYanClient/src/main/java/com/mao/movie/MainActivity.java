package com.mao.movie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CodeUtils;

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
        String textContent = mQRContentEditText.getText().toString();
        if (TextUtils.isEmpty(textContent)) {
            Toast.makeText(MainActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        mQRContentEditText.setText("");
        mBitmap = CodeUtils.createImage(textContent, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mQRContentImageView.setImageBitmap(mBitmap);
    }
}
