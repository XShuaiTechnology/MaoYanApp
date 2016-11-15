package com.mao.movie.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mao.movie.R;
import com.mao.movie.model.RecommendMovie;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerTestAdapter extends RecyclerView.Adapter<VideoPlayerTestAdapter.MyViewHolder> {

    private OnItemClickListener mClickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public static interface OnItemClickListener {
        void onClick(View view, int position);
    }

    List<RecommendMovie.RowsBean> mMovieList = new ArrayList<>();

    public VideoPlayerTestAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_video_player_test, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Logger.d("onBindViewHolder [" + holder.imageView.hashCode() + "] position=" + position);

        Glide.with(holder.imageView.getContext())
                .load(mMovieList.get(position).getUrl())
                .into(holder.imageView);
        holder.textView.setText(mMovieList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setData(List<RecommendMovie.RowsBean> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView rootView;
        TextView textView;

        public MyViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.videoPlayer);
            rootView = (CardView) itemView.findViewById(R.id.rootView);
            textView = (TextView) itemView.findViewById(R.id.titleTextView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.d("onClick");
                    if (mClickListener != null) {
                        mClickListener.onClick(itemView, getAdapterPosition());
                    }
                }
            });
        }
    }

}
