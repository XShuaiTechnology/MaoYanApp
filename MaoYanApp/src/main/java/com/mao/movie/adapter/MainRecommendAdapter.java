package com.mao.movie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mao.movie.R;
import com.mao.movie.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GaoMatrix on 2016/10/27.
 */
public class MainRecommendAdapter extends RecyclerView.Adapter {
    private List<Movie> mMovieList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_recommend, parent, false);
        return new MainRecommendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainRecommendViewHolder mainRecommendViewHolder = (MainRecommendViewHolder) holder;
        Movie movie = mMovieList.get(position);
        mainRecommendViewHolder.mTitleTextView.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return mMovieList == null ? 0 : mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList) {
        this.mMovieList = movieList;
        notifyDataSetChanged();
    }

    static class MainRecommendViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageUrlImageView)
        ImageView mImageUrlImageView;
        @BindView(R.id.titleTextView)
        TextView mTitleTextView;

        MainRecommendViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}