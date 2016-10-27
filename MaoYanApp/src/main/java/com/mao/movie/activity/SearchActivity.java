package com.mao.movie.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.mao.movie.R;
import com.mao.movie.fragment.SearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GaoMatrix on 2016/10/26.
 */
public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.backButton)
    ImageButton mBackButton;
    @BindView(R.id.searchEditText)
    EditText mSearchEditText;
    @BindView(R.id.searchLineView)
    View mSearchLineView;
    @BindView(R.id.deleteSearchImageButton)
    ImageButton mDeleteSearchImageButton;
    @BindView(R.id.searchImageButton)
    ImageButton mSearchImageButton;
    @BindView(R.id.searchFrameLayout)
    FrameLayout mSearchFrameLayout;

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    private SearchFragment mSearchFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        showFragmnet(1);
    }

    private void showFragmnet(int type) {
        if (type == 1) {
            mSearchFragment = new SearchFragment();
            mFragmentTransaction.replace(R.id.searchFrameLayout, mSearchFragment, "SearchFragmnet");
        }
        mFragmentTransaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.backButton, R.id.deleteSearchImageButton, R.id.searchImageButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                break;
            case R.id.deleteSearchImageButton:
                break;
            case R.id.searchImageButton:
                break;
        }
    }
}
