package com.mao.movie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mao.movie.R;
import com.mao.movie.adapter.CollectionArticleAdapter;
import com.mao.movie.model.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GaoMatrix on 2016/10/27.
 */
public class CollectionArticleFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.selectAllTextView)
    TextView mSelectAllTextView;
    @BindView(R.id.unSelectAllTextView)
    TextView mUnSelectAllTextView;
    @BindView(R.id.deleteAllTextView)
    TextView mDeleteAllTextView;
    @BindView(R.id.selectArticleOperationLayout)
    LinearLayout mSelectArticleOperationLayout;

    public static CollectionArticleFragment newInstance() {

        Bundle args = new Bundle();

        CollectionArticleFragment fragment = new CollectionArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CollectionArticleAdapter mAdapter = new CollectionArticleAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_article, null);
        ButterKnife.bind(this, view);

        init();
        mockData();
        return view;
    }

    private void mockData() {
        List<Article> articleList = new ArrayList<Article>();
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        articleList.add(new Article("苍井空用一部惊辣片诠释了自己的演技"));
        mAdapter.setDataList(articleList);
    }

    private void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void changeEditMode(boolean mode) {
        mAdapter.changeEditMode(mode);
        if (mode) {
            mSelectArticleOperationLayout.setVisibility(View.VISIBLE);
        } else {
            mSelectArticleOperationLayout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.selectAllTextView, R.id.unSelectAllTextView, R.id.deleteAllTextView, R.id.selectArticleOperationLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.unSelectAllTextView:
                mAdapter.changeSelectAllMode(false);
                break;
            case R.id.selectAllTextView:
                mAdapter.changeSelectAllMode(true);
                break;
            case R.id.deleteAllTextView:
                mAdapter.deleteAll();
                break;
            case R.id.selectArticleOperationLayout:
                break;
        }
    }
}
