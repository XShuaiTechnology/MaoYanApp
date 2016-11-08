package com.mao.movie.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mao.movie.App;
import com.mao.movie.R;
import com.mao.movie.model.BannerModel;
import com.mao.movie.model.Movie;
import com.mao.movie.retrofit.ApiService;
import com.mao.movie.retrofit.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by GaoMatrix on 2016/11/8.
 */
public class MainRecommendMultiRecyclerViewAdapter extends RecyclerView.Adapter
        implements BGABanner.OnItemClickListener, BGABanner.Adapter {
    // TODO: 2016/11/8 使用枚举
    public enum Type {
        BANNER,
        RECOMMEND,
        HOT
    }

    private List<Movie> mRecommendMovieList;
    private List<Movie> mHotMovieList;
    private BannerModel mBannerModel;

    private static MainRecommendAdapter mRecommendAdapter = new MainRecommendAdapter();
    private static MainRecommendAdapter mHotAdapter = new MainRecommendAdapter();

    public void setData(List<Movie> recommendMovieList, List<Movie> hotMovieList,
                        BannerModel bannerModel) {
        mRecommendMovieList = recommendMovieList;
        mHotMovieList = hotMovieList;
        mBannerModel = bannerModel;
        mRecommendAdapter.setMovieList(mRecommendMovieList);
        mHotAdapter.setMovieList(mHotMovieList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_layout, parent, false);
                return new BannerViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_layout, parent, false);
                return new RecommendViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_layout, parent, false);
                return new HotViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_layout, parent, false);
                return new HotViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    // ----------------------begin ViewHolder-------------------------
    class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner)
        BGABanner mBanner;

        BannerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mBanner.setAdapter(MainRecommendMultiRecyclerViewAdapter.this);
            mBanner.startAutoPlay();

            RetrofitClient.getClient(ApiService.class).fetchItemsWithItemCount(5).enqueue(new Callback<BannerModel>() {
                @Override
                public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                    BannerModel bannerModel = response.body();
                    mBanner.setData(mBannerModel.imgs, mBannerModel.tips);
                    mBanner.startAutoPlay();
                }

                @Override
                public void onFailure(Call<BannerModel> call, Throwable t) {
                    Toast.makeText(App.getInstance(), "网络数据加载失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class RecommendViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recommendMoreTextView)
        TextView mRecommendMoreTextView;
        @BindView(R.id.recommendRecyclerView)
        RecyclerView mRecommendRecyclerView;

        RecommendViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mRecommendRecyclerView.setLayoutManager(new GridLayoutManager(App.getInstance(), 2));
            mRecommendRecyclerView.setAdapter(mRecommendAdapter);
        }
    }

    class HotViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.hotMoreTextView)
        TextView mHotMoreTextView;
        @BindView(R.id.hotRecyclerView)
        RecyclerView mHotRecyclerView;

        HotViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mHotRecyclerView.setLayoutManager(new GridLayoutManager(App.getInstance(), 3));
            mHotRecyclerView.setAdapter(mHotAdapter);
        }
    }

    // ----------------------end ViewHolder-------------------------

    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
        Glide.with(App.getInstance())
                .load(model)
                .placeholder(R.drawable.holder)
                .error(R.drawable.holder)
                .into((ImageView) view);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
        Toast.makeText(App.getInstance(), "点击了第" + (position + 1) + "页", Toast.LENGTH_SHORT).show();
    }
}
