package com.mao.movie.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mao.movie.R;
import com.mao.movie.adapter.CollectionMovieAdapter;
import com.mao.movie.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GaoMatrix on 2016/10/29.
 * 全网热播
 */
public class HotPlayActivity extends AppCompatActivity {

    @BindView(R.id.backButton)
    ImageButton mBackButton;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.editTextView)
    TextView mEditTextView;

    // TODO: 2016/10/29 暂时使用CollectionMovieAdapter
    private CollectionMovieAdapter mAdapter = new CollectionMovieAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_recommend);
        ButterKnife.bind(this);

        init();
        mockData();
    }

    private void init() {
        mTitleTextView.setText("全网热播");
        mEditTextView.setVisibility(View.VISIBLE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void mockData() {
        List<Movie> movieList = new ArrayList<Movie>();
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        mAdapter.setDataList(movieList);
    }

    @OnClick(R.id.backButton)
    public void onClick() {
        finish();
    }
}
