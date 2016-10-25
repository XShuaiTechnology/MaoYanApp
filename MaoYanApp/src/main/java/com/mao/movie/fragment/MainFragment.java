package com.mao.movie.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

    private AppCompatActivity mActivity;
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, view);

        mActivity = (AppCompatActivity) getActivity();

        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(mActivity.getSupportFragmentManager());
        mViewPager.setAdapter(mMyFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

}