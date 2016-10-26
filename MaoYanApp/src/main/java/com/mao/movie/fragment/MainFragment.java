package com.mao.movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mao.movie.R;
import com.mao.movie.activity.HistoryActivity;
import com.mao.movie.activity.SearchActivity;
import com.mao.movie.adapter.MyFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页Fragmnet
 */
public class MainFragment extends Fragment {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.historyImageButton)
    ImageButton mHistoryImageButton;
    @BindView(R.id.searchImageButton)
    ImageButton mSearchImageButton;

    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, view);


        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mMyFragmentPagerAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText("推荐"));
        mTabLayout.addTab(mTabLayout.newTab().setText("频道"));

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);

        return view;
    }

    @OnClick({R.id.historyImageButton, R.id.searchImageButton})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.historyImageButton:
                intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.searchImageButton:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}