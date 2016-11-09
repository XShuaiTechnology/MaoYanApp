package com.mao.movie.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mao.movie.App;
import com.mao.movie.R;
import com.mao.movie.adapter.MainRecommendMultiRecyclerViewAdapter;
import com.mao.movie.model.BannerModel;
import com.mao.movie.model.Movie;
import com.mao.movie.retrofit.ApiService;
import com.mao.movie.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页的推荐Fragment
 */
public class MainRecommendFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainRecommendMultiRecyclerViewAdapter mAdapter =
            new MainRecommendMultiRecyclerViewAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, null);
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);
//        mRecyclerView.setFocusable(false);

        init();
        return view;
    }

    private void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        getData();
    }

    private void getData() {
        RetrofitClient.getClient(ApiService.class).fetchItemsWithItemCount(5).enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                BannerModel bannerModel = response.body();
                mAdapter.setData(getMovieList(), getMovieList(), bannerModel);
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                Toast.makeText(App.getInstance(), "网络数据加载失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<Movie> getMovieList() {
        List<Movie> movieList = new ArrayList<Movie>();
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        movieList.add(new Movie("寒战", "9.9", "高成全 刘德华", "高成全", "2312-23-12 美国"));
        return movieList;
    }

}
