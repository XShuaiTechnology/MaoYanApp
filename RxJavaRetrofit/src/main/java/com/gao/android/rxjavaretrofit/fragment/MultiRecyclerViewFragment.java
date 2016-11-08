package com.gao.android.rxjavaretrofit.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gao.android.rxjavaretrofit.R;
import com.gao.android.rxjavaretrofit.adapter.MultiRecyclerViewAdapter;
import com.gao.android.rxjavaretrofit.model.USListBean;
import com.gao.android.rxjavaretrofit.model.WaitExpctBean;
import com.gao.android.rxjavaretrofit.model.WaitListBean;
import com.gao.android.rxjavaretrofit.network.Network;
import com.orhanobut.logger.Logger;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * Created by GaoMatrix on 2016/10/24.
 */
public class MultiRecyclerViewFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private Subscription mSubscription;
    private MultiRecyclerViewAdapter mAdapter;
    private StickyRecyclerHeadersDecoration mStickyRecyclerHeadersDecoration;

    //预告片
    public static List<USListBean.DataBean.ComingBean> mRecomData = new ArrayList<>();

    //近期最受期待
    public static List<WaitExpctBean.DataBean.MoviesBean> mExpectData = new ArrayList<>();

    //下部分
    public static List<WaitListBean.DataBean.ComingBean> mListData = new ArrayList<>();

    private Observer<Boolean> mObserver = new Observer<Boolean>() {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Boolean success) {
            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter = new MultiRecyclerViewAdapter(mRecomData, mExpectData, mListData);
            mRecyclerView.setAdapter(mAdapter);
            mStickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(mAdapter);
            mRecyclerView.addItemDecoration(mStickyRecyclerHeadersDecoration);
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
                @Override
                public void onChanged() {
                    //刷新数据的时候回刷新头部
                    mStickyRecyclerHeadersDecoration.invalidateHeaders();
                }
            });
            Logger.d(mRecomData);
            Logger.d(mExpectData);
            Logger.d(mListData);
//            mAdapter.setData(mRecomData, mExpectData, mListData);

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_recyclerview, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getData();
        return view;
    }


    private void getData() {
        mSwipeRefreshLayout.setRefreshing(true);
        unSubscribe();
        mSubscription = rx.Observable.zip(
                Network.getMultiRecyclerViewApi().getWaitRecommend(),
                Network.getMultiRecyclerViewApi().getWaitExpct(),
                Network.getMultiRecyclerViewApi().getWaitList(),
                new Func3<USListBean, WaitExpctBean, WaitListBean, Boolean>() {
                    @Override
                    public Boolean call(USListBean usListBean, WaitExpctBean waitExpctBean, WaitListBean waitListBean) {
                        mRecomData = usListBean.getData().getComing();
                        mExpectData = waitExpctBean.getData().getMovies();
                        mListData = waitListBean.getData().getComing();
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    private void unSubscribe() {
    }
}
