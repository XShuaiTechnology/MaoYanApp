package com.mao.movie.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mao.movie.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GaoMatrix on 2016/10/26.
 */
public class CollectionActivity extends Activity {

    @BindView(R.id.backButton)
    ImageButton mBackButton;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.editTextView)
    TextView mEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);

        mTitleTextView.setText("我的收藏");
        mEditTextView.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.backButton, R.id.editTextView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                break;
            case R.id.editTextView:
                break;
        }
    }
}
