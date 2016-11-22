package com.mao.movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mao.movie.App;
import com.mao.movie.R;
import com.mao.movie.activity.MovieDetailActivity;
import com.mao.movie.adapter.VideoPlayerTestAdapter;
import com.mao.movie.model.RecommendMovie;
import com.mao.movie.retrofit.ApiService;
import com.mao.movie.retrofit.RetrofitClient;
import com.mao.movie.util.OpenMovie;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 首页的频道Fragment
 */
public class MainTestFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    VideoPlayerTestAdapter mAdapter = new VideoPlayerTestAdapter();
    List<RecommendMovie.RowsBean> mMovieList = new ArrayList<>();
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, null);
        ButterKnife.bind(this, view);

        init();
        return view;
    }


    private void init() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        mAdapter.setClickListener(new VideoPlayerTestAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Logger.d("click---------");
                Toast.makeText(getActivity(), "click" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.MOVIE_DATA, mMovieList.get(position));
                startActivity(intent);

                // Intent intent = OpenMovie.getInstance().getIntent(getActivity(), mMovieList.get(position));
                // startActivity(intent);

                RetrofitClient.getClient(ApiService.class)
                        .pushMovieToDevice(App.getInstance().regId, intent.toUri(0))
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Logger.d(response);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Logger.d("onFailure");
                            }
                        });
            }
        });

        getData();
    }

    private void getData() {
        RetrofitClient.getClient(ApiService.class).getRecommendMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<RecommendMovie>() {
                    @Override
                    public void call(RecommendMovie recommendMovie) {
                        for (RecommendMovie.RowsBean bean : recommendMovie.getRows()) {
                            if (null != bean.getIntentExtras() && bean.getIntentExtras().size() > 0) {
                                StringBuilder sb = new StringBuilder();
                                for (String string : bean.getIntentExtras()) {
                                    sb.append(string + OpenMovie.MOVIE_INFO_SEPARATOR);
                                }
                                String str = sb.toString();
                                if (str.length() > 0) {
                                    str = str.substring(0, str.length() - 3);
                                }
                                bean.setIntentExtrasStr(str);
                            }
                        }
                    }
                })
                .subscribe(new Subscriber<RecommendMovie>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(RecommendMovie recommendMovie) {
                        mMovieList = recommendMovie.getRows();
                        mAdapter.setData(mMovieList);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

}
