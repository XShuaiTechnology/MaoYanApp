package com.mao.movie;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.mao.movie.fragment.HotCommentFragment;
import com.mao.movie.fragment.MainFragment;
import com.mao.movie.fragment.MyFragment;
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
    private int[] mIconUnselectIds = {R.mipmap.tab_home_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {R.mipmap.tab_home_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
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
            mFragments.add(new HotCommentFragment());
            mFragments.add(new MyFragment());
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
