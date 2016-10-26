package com.mao.movie.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mao.movie.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GaoMatrix on 2016/10/26.
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.backButton)
    ImageButton mBackButton;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.feedbackContentEditText)
    EditText mFeedbackContentEditText;
    @BindView(R.id.feedbackContentCountTextView)
    TextView mFeedbackContentCountTextView;
    @BindView(R.id.feedbackFromEditText)
    EditText mFeedbackFromEditText;
    @BindView(R.id.feedbackButton)
    Button mFeedbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        mTitleTextView.setText("意见反馈");
    }

    @OnClick({R.id.backButton, R.id.feedbackButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.feedbackButton:
                break;
        }
    }
}
