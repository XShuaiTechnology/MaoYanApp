package com.mao.movie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gao.android.util.NetWorkUtils;
import com.mao.movie.R;
import com.mao.movie.adapter.VideoPlayerAdapter;
import com.mao.movie.adapter.VideoPlayerTestAdapter;
import com.mao.movie.model.RecommendMovie;
import com.mao.movie.retrofit.ApiService;
import com.mao.movie.retrofit.RetrofitClient;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页的频道Fragment
 */
public class MainTestFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    VideoPlayerTestAdapter mAdapter = new VideoPlayerTestAdapter();
    List<RecommendMovie.RowsBean> mMovieList = new ArrayList<>();


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

        mAdapter.setClickListener(new VideoPlayerTestAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Logger.d("click---------");
                Toast.makeText(getActivity(), "click" + position, Toast.LENGTH_SHORT).show();
            }
        });

        RetrofitClient.getClient(ApiService.class).getRecommendMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RecommendMovie>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RecommendMovie recommendMovie) {
                        mMovieList = recommendMovie.getRows();
                        mAdapter.setData(mMovieList);
                    }
                });

        /*mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if (JCVideoPlayerManager.getFirst() != null) {
                    JCVideoPlayer videoPlayer = (JCVideoPlayer) JCVideoPlayerManager.getFirst();
                    if (((ViewGroup) view).indexOfChild(videoPlayer) != -1 && videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                        JCVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });*/
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}
