package com.mao.movie.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mao.movie.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GaoMatrix on 2016/10/25.
 */
public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.backButton)
    ImageButton mBackButton;
    @BindView(R.id.networkLayout)
    RelativeLayout mNetworkLayout;
    @BindView(R.id.definitionChangeLayout)
    RelativeLayout mDefinitionChangeLayout;
    @BindView(R.id.aboutLayout)
    RelativeLayout mAboutLayout;
    @BindView(R.id.cacheSizeTextView)
    TextView mCacheSizeTextView;
    @BindView(R.id.clearCacheLayout)
    RelativeLayout mClearCacheLayout;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        mTitleTextView.setText("设置");
    }

    @OnClick({R.id.backButton, R.id.networkLayout, R.id.definitionChangeLayout, R.id.aboutLayout, R.id.cacheSizeTextView, R.id.clearCacheLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.networkLayout:
                break;
            case R.id.definitionChangeLayout:
                break;
            case R.id.aboutLayout:
                break;
            case R.id.cacheSizeTextView:
                break;
            case R.id.clearCacheLayout:
                break;
        }
    }
}
