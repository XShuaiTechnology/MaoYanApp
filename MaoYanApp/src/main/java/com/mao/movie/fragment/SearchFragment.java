package com.mao.movie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mao.movie.R;
import com.mao.movie.activity.SearchActivity;
import com.mao.movie.adapter.SearchHistoryAdapter;
import com.mao.movie.adapter.SearchHotAdapter;
import com.mao.movie.model.SearchHistory;
import com.mao.movie.model.SearchHot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GaoMatrix on 2016/10/27.
 */
public class SearchFragment extends Fragment {

    @BindView(R.id.searchHisttoryRecyclerView)
    RecyclerView mSearchHisttoryRecyclerView;
    @BindView(R.id.searchHotRecyclerView)
    RecyclerView mSearchHotRecyclerView;

    private SearchActivity mActivity;
    private SearchHistoryAdapter mSearchHistoryAdapter = new SearchHistoryAdapter();
    private SearchHotAdapter mSearchHotAdapter = new SearchHotAdapter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (SearchActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        init();
        mockData();

        return view;
    }

    private void init() {
        mSearchHisttoryRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mSearchHisttoryRecyclerView.setAdapter(mSearchHistoryAdapter);

        mSearchHotRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mSearchHotRecyclerView.setAdapter(mSearchHotAdapter);
    }

    /**
     * 模拟数据
     */
    private void mockData() {
        List<SearchHistory> searchHistotyList = new ArrayList<SearchHistory>();
        searchHistotyList.add(new SearchHistory("冰与火之歌"));
        searchHistotyList.add(new SearchHistory("权利的游戏"));
        searchHistotyList.add(new SearchHistory("心事走肉"));
        searchHistotyList.add(new SearchHistory("破产姐妹"));
        searchHistotyList.add(new SearchHistory("越狱"));
        mSearchHistoryAdapter.setSearchHistoryList(searchHistotyList);

        List<SearchHot> searchHotList = new ArrayList<SearchHot>();
        searchHotList.add(new SearchHot("血族"));
        searchHotList.add(new SearchHot("毒枭"));
        searchHotList.add(new SearchHot("老友记"));
        searchHotList.add(new SearchHot("Shit"));
        searchHotList.add(new SearchHot("Fuck"));
        mSearchHotAdapter.setSearchHotList(searchHotList);
    }

}
