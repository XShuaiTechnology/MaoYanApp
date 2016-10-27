package com.mao.movie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mao.movie.R;
import com.mao.movie.model.SearchHot;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GaoMatrix on 2016/10/27.
 */
public class SearchHotAdapter extends RecyclerView.Adapter {
    private List<SearchHot> mSearchHotList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_hot, parent, false);
        return new SearchHotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchHotViewHolder searchHistoryViewHolder = (SearchHotViewHolder) holder;
        SearchHot searchHot = mSearchHotList.get(position);
        searchHistoryViewHolder.mSearchHotNameTextView.setText(searchHot.getName());
    }

    @Override
    public int getItemCount() {
        return mSearchHotList == null ? 0 : mSearchHotList.size();
    }

    public void setSearchHotList(List<SearchHot> searchHotList) {
        this.mSearchHotList = searchHotList;
        notifyDataSetChanged();
    }

    static class SearchHotViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.searchHotNumImageView)
        ImageView mSearchHotNumImageView;
        @BindView(R.id.searchHotNumTextView)
        TextView mSearchHotNumTextView;
        @BindView(R.id.searchHotNameTextView)
        TextView mSearchHotNameTextView;

        SearchHotViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
