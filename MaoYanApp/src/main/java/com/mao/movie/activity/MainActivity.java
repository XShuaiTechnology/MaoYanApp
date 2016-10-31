package com.mao.movie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gao.android.util.PreferencesUtils;
import com.google.gson.Gson;
import com.mao.movie.R;
import com.mao.movie.consts.PrefConst;
import com.mao.movie.fragment.HotCommentFragment;
import com.mao.movie.fragment.MainFragment;
import com.mao.movie.fragment.UserFragment;
import com.mao.movie.model.TabEntity;
import com.mao.movie.model.WxUserInfo;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareAPI;

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

    public UMShareAPI mShareAPI = null;
    public WxUserInfo mWxUserInfo = new WxUserInfo();
    /**标示用户是否登录*/
    public boolean mIsUserLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();

        mShareAPI = UMShareAPI.get(this);

        initWxUserInfo();
    }

    private void initWxUserInfo() {
        String wxUserInfoJson = PreferencesUtils.getString(this, PrefConst.WX_USER_INFO, "");
        if (!TextUtils.isEmpty(wxUserInfoJson)) {
            mWxUserInfo = new Gson().fromJson(wxUserInfoJson, WxUserInfo.class);
            Logger.d(mWxUserInfo.toString());
            mIsUserLogin = true;
        }
    }

    private void initData() {
        for (String title : mTitles) {
            mFragments.add(new MainFragment());
            mFragments.add(new HotCommentFragment());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

}
