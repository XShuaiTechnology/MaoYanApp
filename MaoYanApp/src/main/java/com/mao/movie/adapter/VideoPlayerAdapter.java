package com.mao.movie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mao.movie.R;
import com.mao.movie.consts.VideoConstant;
import com.mao.movie.ui.ijkplayer.JCVideoPlayerCustom;
import com.orhanobut.logger.Logger;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class VideoPlayerAdapter extends RecyclerView.Adapter<VideoPlayerAdapter.MyViewHolder> {

    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public VideoPlayerAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_video_player, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Logger.d("onBindViewHolder [" + holder.jcVideoPlayer.hashCode() + "] position=" + position);

        holder.jcVideoPlayer.setUp(
                VideoConstant.videoUrls[position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                VideoConstant.videoTitles[position]);
        Glide.with(holder.jcVideoPlayer.getContext())
                .load(VideoConstant.videoThumbs[position])
                .into(holder.jcVideoPlayer.thumbImageView);
    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JCVideoPlayerCustom jcVideoPlayer;

        public MyViewHolder(View itemView) {
            super(itemView);
            jcVideoPlayer = (JCVideoPlayerCustom) itemView.findViewById(R.id.videoPlayer);
        }
    }

}
