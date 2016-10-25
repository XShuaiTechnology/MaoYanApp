package com.mao.movie.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.mao.movie.R;
import com.mao.movie.fragment.CommentFragment;
import com.mao.movie.fragment.MainFragment;
import com.mao.movie.fragment.UserFragment;
import com.mao.movie.model.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.tabLayout)
    CommonTabLayout mTabLayout;
    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private String[] mTitles = {"首页", "热评", "我的"};
    private int[] mIconUnselectIds = {R.drawable.ic_tabbar_home_n,
            R.drawable.ic_tabbar_take_n, R.drawable.ic_tabbar_mine_n};
    private int[] mIconSelectIds = {R.drawable.ic_tabbar_home_h,
            R.drawable.ic_tabbar_take_h, R.drawable.ic_tabbar_mine_h};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        for (String title : mTitles) {
            mFragments.add(new MainFragment());
            mFragments.add(new CommentFragment());
            mFragments.add(new UserFragment());
        }
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mTabLayout.setTabData(mTabEntities, this, R.id.container, mFragments);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mTabLayout.setCurrentTab(0);
    }

}
