package com.mao.movie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mao.movie.R;
import com.mao.movie.adapter.CollectionArticleAdapter;
import com.mao.movie.model.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热评Fragment
 */
public class HotCommentFragment extends Fragment {

    @BindView(R.id.backButton)
    ImageButton mBackButton;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CollectionArticleAdapter mAdapter = new CollectionArticleAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hot_comment, null);
        ButterKnife.bind(this, v);

        init();
        mockData();
        return v;
    }

    private void init() {
        mBackButton.setVisibility(View.INVISIBLE);
        mTitleTextView.setText("热评");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void mockData() {
        List<Article> articleList = new ArrayList<Article>();
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        mAdapter.setArticleList(articleList);
    }
}