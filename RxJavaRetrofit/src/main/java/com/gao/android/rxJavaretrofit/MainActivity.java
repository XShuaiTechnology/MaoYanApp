package com.gao.android.rxjavaretrofit;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gao.android.rxjavaretrofit.fragment.CacheFragment;
import com.gao.android.rxjavaretrofit.fragment.ElementaryFragment;
import com.gao.android.rxjavaretrofit.fragment.LambdaFragment;
import com.gao.android.rxjavaretrofit.fragment.MultiRecyclerViewFragment;
import com.gao.android.rxjavaretrofit.fragment.TestFragment;
import com.gao.android.rxjavaretrofit.fragment.TokenAdvancedFragment;
import com.gao.android.rxjavaretrofit.fragment.TokenFragmnet;
import com.gao.android.rxjavaretrofit.fragment.MapFragment;
import com.gao.android.rxjavaretrofit.fragment.ZipFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);

        initData();

    }

    private void initData() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 6:
                        return ElementaryFragment.newInstance();
                    case 7:
                        return TestFragment.newInstance();
                    case 8:
                        return LambdaFragment.newInstance();
                    case 1:
                        return MapFragment.newInstance();
                    case 2:
                        return ZipFragment.newInstance();
                    case 3:
                        return TokenFragmnet.newInstance();
                    case 4:
                        return TokenAdvancedFragment.newInstance();
                    case 5:
                        return CacheFragment.newInstance();
                    case 0:
                        return MultiRecyclerViewFragment.newInstance();
                    default:
                        return ElementaryFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 9;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 6:
                        return getString(R.string.title_elementary);
                    case 7:
                        return "测试";
                    case 8:
                        return "Lambda";
                    case 1:
                        return getString(R.string.title_map);
                    case 2:
                        return getString(R.string.title_zip);
                    case 3:
                        return getString(R.string.title_token);
                    case 4:
                        return getString(R.string.title_token_advanced);
                    case 5:
                        return getString(R.string.title_cache);
                    case 0:
                        return "多个RecyclerView";
                    default:
                        return getString(R.string.title_elementary);
                }
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
    }

}
