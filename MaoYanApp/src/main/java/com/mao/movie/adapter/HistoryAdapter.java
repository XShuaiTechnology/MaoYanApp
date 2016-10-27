package com.mao.movie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mao.movie.R;
import com.mao.movie.model.History;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GaoMatrix on 2016/10/27.
 * 观看历史
 */
public class HistoryAdapter extends RecyclerView.Adapter {
    private List<History> mHistoryList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryViewHolder searchHistoryViewHolder = (HistoryViewHolder) holder;
        History history = mHistoryList.get(position);
        searchHistoryViewHolder.mHistoryNameTextView.setText(history.getName());
    }

    @Override
    public int getItemCount() {
        return mHistoryList == null ? 0 : mHistoryList.size();
    }

    public void setHistoryList(List<History> historyList) {
        this.mHistoryList = historyList;
        notifyDataSetChanged();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.historyNameTextView)
        TextView mHistoryNameTextView;
        @BindView(R.id.lastWatchTextView)
        TextView mLastWatchTextView;

        HistoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
