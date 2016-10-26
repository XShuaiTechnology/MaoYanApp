package com.mao.movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mao.movie.R;
import com.mao.movie.activity.CollectionActivity;
import com.mao.movie.activity.FeedbackActivity;
import com.mao.movie.activity.HistoryActivity;
import com.mao.movie.activity.SettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的Fragment
 */
public class UserFragment extends Fragment {

    @BindView(R.id.userIconTextView)
    ImageView mUserIconTextView;
    @BindView(R.id.loginButton)
    Button mLoginButton;
    @BindView(R.id.collectionLayout)
    RelativeLayout mCollectionLayout;
    @BindView(R.id.historyLayout)
    RelativeLayout mHistoryLayout;
    @BindView(R.id.settingLayout)
    RelativeLayout mSettingLayout;
    @BindView(R.id.feedbackLayout)
    RelativeLayout mFeedbackLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, null);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick({R.id.userIconTextView, R.id.loginButton, R.id.collectionLayout, R.id.historyLayout, R.id.settingLayout, R.id.feedbackLayout})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.userIconTextView:
                break;
            case R.id.loginButton:
                break;
            case R.id.collectionLayout:
                intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.historyLayout:
                intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.settingLayout:
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.feedbackLayout:
                intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }
}
