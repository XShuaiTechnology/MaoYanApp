package com.mao.movie.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mao.movie.fragment.MainChannelFragment;
import com.mao.movie.fragment.MainRecommendFragment;

/**
 * Created by GaoMatrix on 2016/10/25.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"推荐", "频道"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainRecommendFragment();
            case 1:
                return new MainChannelFragment();
            default:
                return new MainRecommendFragment();
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
