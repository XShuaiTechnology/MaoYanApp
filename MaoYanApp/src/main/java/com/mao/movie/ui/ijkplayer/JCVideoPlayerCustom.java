package com.mao.movie.ui.ijkplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mao.movie.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 自定义的播放界面
 */
public class JCVideoPlayerCustom extends JCVideoPlayerStandard {

    public ImageView shareButton;

    public JCVideoPlayerCustom(Context context) {
        super(context);
    }

    public JCVideoPlayerCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        shareButton = (ImageView) findViewById(R.id.share);
        shareButton.setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_video_player_custom;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.share) {
            // 弹出信源切换
            Toast.makeText(getContext(), "Whatever the icon means", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean setUp(String url, int screen, Object... objects) {
        if (super.setUp(url, screen, objects)) {
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                shareButton.setVisibility(View.VISIBLE);
            } else {
                shareButton.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        return false;
    }
//    @Override
//    public void setUiWitStateAndScreen(int state) {
//        super.setUiWitStateAndScreen(state);
//        if (mIfCurrentIsFullscreen) {
//            shareButton.setVisibility(View.VISIBLE);
//        } else {
//            shareButton.setVisibility(View.INVISIBLE);
//        }
//    }
}
