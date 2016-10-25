package com.mao.movie.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mao.movie.R;
import com.mao.movie.adapter.MyFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页Fragmnet
 */
public class MainFragment extends Fragment {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

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
        //mViewPager.setCurrentItem(1);
        return view;
    }

}