package com.mao.movie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mao.movie.R;
import com.mao.movie.model.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GaoMatrix on 2016/10/27.
 * 我的收藏-视频
 */
public class CollectionArticleAdapter extends RecyclerView.Adapter {
    private List<Article> mArticleList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection_article, parent, false);
        return new CollectionArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CollectionArticleViewHolder collectionMovieViewHolder = (CollectionArticleViewHolder) holder;
        Article article = mArticleList.get(position);

        collectionMovieViewHolder.mTitleTextView.setText(article.getTitle());
    }

    @Override
    public int getItemCount() {
        return mArticleList == null ? 0 : mArticleList.size();
    }

    public void setArticleList(List<Article> articleList) {
        this.mArticleList = articleList;
        notifyDataSetChanged();
    }

    static class CollectionArticleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageUrlImageView)
        ImageView mImageUrlImageView;
        @BindView(R.id.titleTextView)
        TextView mTitleTextView;

        CollectionArticleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
