package com.mao.movie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mao.movie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热评Fragment
 */
public class HotCommentFragment extends Fragment {

    @BindView(R.id.backButton)
    ImageButton mBackButton;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hot_comment, null);
        ButterKnife.bind(this, v);

        mBackButton.setVisibility(View.INVISIBLE);
        mTitleTextView.setText("热评");
        return v;
    }
}