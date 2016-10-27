package com.mao.movie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mao.movie.R;
import com.mao.movie.adapter.CollectionMovieAdapter;
import com.mao.movie.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GaoMatrix on 2016/10/27.
 */
public class CollectionMovieFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CollectionMovieAdapter mAdapter = new CollectionMovieAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_movie, null);
        ButterKnife.bind(this, view);

        init();
        mockData();
        return view;
    }

    private void mockData() {
        List<Movie> movieList = new ArrayList<Movie>();
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        mAdapter.setMovieList(movieList);
    }

    private void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }
}
