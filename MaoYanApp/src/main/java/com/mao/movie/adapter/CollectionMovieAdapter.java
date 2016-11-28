package com.mao.movie.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gao.android.util.ListUtils;
import com.mao.movie.R;
import com.mao.movie.model.History;
import com.mao.movie.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GaoMatrix on 2016/10/27.
 * 我的收藏-视频
 */
public class CollectionMovieAdapter extends RecyclerView.Adapter {
    private List<Movie> mDataList;

    /**
     * 是否是编辑模式
     */
    private boolean mIsEditMode = false;
    /**
     * 选中的位置数组
     */
    SparseBooleanArray mSelectedPositions = new SparseBooleanArray();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection_movie, parent, false);
        return new CollectionMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CollectionMovieViewHolder viewHolder = (CollectionMovieViewHolder) holder;
        Movie movie = mDataList.get(position);

        viewHolder.mActorsTextView.setText(movie.getActors());
        viewHolder.mNameTextView.setText(movie.getTitle());
        viewHolder.mDirectorsTextView.setText(movie.getDirectors());
        viewHolder.mTimeTextView.setText(movie.getTime());

        viewHolder.mCheckBox.setVisibility(mIsEditMode ? View.VISIBLE : View.GONE);
        if (mIsEditMode) {
            viewHolder.mCheckBox.setChecked(isItemChecked(position));
        }

        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItemChecked(position)) {
                    setItemChecked(position, false);
                } else {
                    setItemChecked(position, true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }


    private void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
    }

    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    public void setDataList(List<Movie> dataList) {
        if (ListUtils.isEmpty(dataList)) {
            notifyDataSetChanged();
            return;
        }
        this.mDataList = dataList;
        mSelectedPositions.clear();
        for (int i = 0; i < dataList.size(); i++) {
            setItemChecked(i, false);
        }
        notifyDataSetChanged();
    }

    /**
     * 切换编辑模式
     */
    public void changeEditMode(boolean mode) {
        mIsEditMode = mode;
        notifyDataSetChanged();
    }

    public boolean isEditMode() {
        return mIsEditMode;
    }

    /**
     * @param isSelectAll true 全选，false 反选
     */
    public void changeSelectAllMode(boolean isSelectAll) {
        for (int i = 0; i < mSelectedPositions.size(); i++) {
            setItemChecked(i, isSelectAll);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除选中的所有
     */
    public void deleteAll() {
        List<Movie> removeList = new ArrayList<>();
        for (int i = 0; i < mSelectedPositions.size(); i++) {
            if (isItemChecked(i)) {
                removeList.add(mDataList.get(i));
            }
        }
        mDataList.removeAll(removeList);
        setDataList(mDataList);
    }

    static class CollectionMovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkBox)
        CheckBox mCheckBox;
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
