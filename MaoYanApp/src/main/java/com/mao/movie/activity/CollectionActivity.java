package com.mao.movie.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mao.movie.R;
import com.mao.movie.fragment.CollectionArticleFragment;
import com.mao.movie.fragment.CollectionMovieFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GaoMatrix on 2016/10/26.
 */
public class CollectionActivity extends AppCompatActivity {

    @BindView(R.id.backButton)
    ImageButton mBackButton;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.editTextView)
    TextView mEditTextView;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private CollectionFragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mTitleTextView.setText("我的收藏");
        mEditTextView.setVisibility(View.VISIBLE);

        mAdapter = new CollectionFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText("电影"));
        mTabLayout.addTab(mTabLayout.newTab().setText("文章"));

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
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

    class CollectionFragmentPagerAdapter extends FragmentPagerAdapter {
        private String[] mTitles = new String[]{"电影", "文章"};

        public CollectionFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CollectionMovieFragment();
                case 1:
                    return new CollectionArticleFragment();
                default:
                    return new CollectionMovieFragment();
            }
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
