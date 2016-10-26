package com.mao.movie.activity;

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
public class HistoryActivity extends BaseActivity {

    @BindView(R.id.backButton)
    ImageButton mBackButton;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.editTextView)
    TextView mEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        mTitleTextView.setText("观看历史");
        mEditTextView.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.backButton, R.id.editTextView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.editTextView:
                break;
        }
    }
}
