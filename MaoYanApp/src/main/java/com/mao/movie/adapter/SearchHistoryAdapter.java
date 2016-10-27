package com.mao.movie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mao.movie.R;
import com.mao.movie.model.SearchHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GaoMatrix on 2016/10/27.
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter {
    private List<SearchHistory> mSearchHistoryList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
        return new SearchHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchHistoryViewHolder searchHistoryViewHolder = (SearchHistoryViewHolder) holder;
        SearchHistory searchHistory = mSearchHistoryList.get(position);
        searchHistoryViewHolder.mHistoryNameTextView.setText(searchHistory.getName());
    }

    @Override
    public int getItemCount() {
        return mSearchHistoryList == null ? 0 : mSearchHistoryList.size();
    }

    public void setSearchHistoryList(List<SearchHistory> searchHistoryList) {
        this.mSearchHistoryList = searchHistoryList;
        notifyDataSetChanged();
    }

    static class SearchHistoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.historyNameTextView)
        TextView mHistoryNameTextView;
        @BindView(R.id.deleteHistotyImageButton)
        ImageButton mDeleteHistotyImageButton;

        SearchHistoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
