package com.mao.movie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mao.movie.R;
import com.mao.movie.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GaoMatrix on 2016/10/27.
 * 我的收藏-视频
 */
public class CollectionMovieAdapter extends RecyclerView.Adapter {
    private List<Movie> mMovieList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection_movie, parent, false);
        return new CollectionMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CollectionMovieViewHolder collectionMovieViewHolder = (CollectionMovieViewHolder) holder;
        Movie movie = mMovieList.get(position);

        collectionMovieViewHolder.mActorsTextView.setText(movie.getActors());
        collectionMovieViewHolder.mNameTextView.setText(movie.getTitle());
        collectionMovieViewHolder.mDirectorsTextView.setText(movie.getDirectors());
        collectionMovieViewHolder.mTimeTextView.setText(movie.getTime());
    }

    @Override
    public int getItemCount() {
        return mMovieList == null ? 0 : mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList) {
        this.mMovieList = movieList;
        notifyDataSetChanged();
    }

    static class CollectionMovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.radioButton)
        RadioButton mRadioButton;
        @BindView(R.id.imageUrlImageView)
        ImageView mImageUrlImageView;
        @BindView(R.id.nameTextView)
        TextView mNameTextView;
        @BindView(R.id.ratingTextView)
        TextView mRatingTextView;
        @BindView(R.id.timeTextView)
        TextView mTimeTextView;
        @BindView(R.id.directorsTextView)
        TextView mDirectorsTextView;
        @BindView(R.id.actorsTextView)
        TextView mActorsTextView;

        CollectionMovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
