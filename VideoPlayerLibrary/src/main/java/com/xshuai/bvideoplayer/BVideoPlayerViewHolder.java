package com.xshuai.bvideoplayer;

import com.baidu.cyberplayer.core.BVideoView;
import com.bench.utils.BenchUtils;
import com.xshuai.netplayer.R;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * VideoPlayActivity
 * 
 * @author
 * @category: .
 */
public class BVideoPlayerViewHolder {
	private static final String TAG = "VideoPlayerViewHolder";
	private BVideoPlayerActivity videoPlayerActivity;
	private ImageView videoTimeImageView; //
	private ImageView videoInformationImageView; //
	private ImageView setImageView; //
	private TextView videoCurrentPositionTextView;
	private ImageView videoPlayerImageViewTop;
	private LinearLayout videoPlayerLinearLayoutBottom;
	private ImageView rewindImageView; //
	private LinearLayout videoPlayerProgressLinearLayout;
	public BVideoView videoPlayerSurfaceView;
	private LinearLayout videoPlayerTipLayout; //
	private TextView videoPlayerTitleTextView; //
	private SeekBar videoPlayerProgressBar; //
	private TextView videoTotalTimeTextView; //
	private RelativeLayout playSettingRelativeLayout;
	private RelativeLayout SeekBarLayout;
	private ImageView PlayArrowsImg;
	private ProgressBar mLoadingprogressBar;
	private TextView loading_progress_per;

	public BVideoPlayerViewHolder(BVideoPlayerActivity videoPlayerActivity) {
		super();
		this.videoPlayerActivity = videoPlayerActivity;
	}

	View curDefinitionView;

	public View getCurDefinitionView() {
		return curDefinitionView;
	}

	public void setCurDefinitionView(View v) {
		curDefinitionView = v;
	}

	protected void findViews() {
		BVideoView.setAKSK("46c99272ab634d79ab8edadfb4fcd2dc", "be13e6d1bbfc4216a9d8fdd8ec688c32");
		SeekBarLayout = (RelativeLayout) videoPlayerActivity.findViewById(R.id.PlayerShowTip);
		videoPlayerTipLayout = (LinearLayout) videoPlayerActivity.findViewById(R.id.PlayFrameTop);
		videoPlayerTitleTextView = (TextView) videoPlayerActivity.findViewById(R.id.FilmName);
		videoPlayerSurfaceView = (BVideoView) videoPlayerActivity.findViewById(R.id.bvideoview);
		videoPlayerSurfaceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (videoMenuIsVisible()) {
					// Utils.simulateKeystroke(KeyEvent.KEYCODE_BACK);
				} else {
				}
			}
		});
		videoPlayerSurfaceView.setKeepScreenOn(true);
		videoPlayerProgressBar = (SeekBar) videoPlayerActivity.findViewById(R.id.ProgressSeekbar);
		loading_progress_per = (TextView) videoPlayerActivity.findViewById(R.id.loading_progress_per);
		playSettingRelativeLayout = (RelativeLayout) videoPlayerActivity.findViewById(R.id.linearCtrolMenuBg);
		videoCurrentPositionTextView = (TextView) videoPlayerActivity.findViewById(R.id.CurrentTime);
		videoTotalTimeTextView = (TextView) videoPlayerActivity.findViewById(R.id.TotalTime);
		PlayArrowsImg = (ImageView) videoPlayerActivity.findViewById(R.id.PlayArrowsImg);
		mLoadingprogressBar = (ProgressBar) videoPlayerActivity.findViewById(R.id.LoadingprogressBar);
		playSettingRelativeLayout.getBackground().setAlpha(180);
		PlayArrowsImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pauseOrStartPlayer(true);
			}
		});
		hideAllViews();
	}

	// 显示加载对话
	public void showLoadingDialog() {
		Log.i(TAG, "showLoadingDialog");
		mLoadingprogressBar.setVisibility(View.VISIBLE);
		loading_progress_per.setVisibility(View.VISIBLE);
	}

	public void loadingDialogDismiss() {
		mLoadingprogressBar.setVisibility(View.GONE);
		loading_progress_per.setVisibility(View.GONE);
		loading_progress_per.setText("");
	}

	public boolean isShowloadingDialog() {
		return mLoadingprogressBar.getVisibility() == View.VISIBLE;
	}

	public void setLoadingProgressPer(String val) {
		loading_progress_per.setText(val);
	}

	public RelativeLayout getVideoPlaySettingRelativeLayout() {
		return playSettingRelativeLayout;
	}

	// 获取文件
	public LinearLayout getVideoPlayTipListLayout() {
		return videoPlayerTipLayout;
	}

	public RelativeLayout getSeekBarLayout() {
		return SeekBarLayout;
	}

	public void pauseOrStartPlayer(boolean pause) {
		BVideoPlayerListener listener = videoPlayerActivity.getListener();
		if (!videoMenuIsVisible()) {
			listener.StartShowMenuAnimation();
		}
		if (videoPlayerSurfaceView.isPlaying()) {
			if (pause) {
				videoPlayerSurfaceView.pause();
				PlayArrowsImg.setImageResource(R.drawable.zanting);
			}
			// 关闭主菜单的延时隐藏功能
			listener.ControlBarHideCancel();
			listener.ControlBarShowCancel();
		} else {
			if (pause) {
				PlayArrowsImg.setImageResource(R.drawable.play);
				videoPlayerSurfaceView.resume();
			}
			listener.delayControlBarHide();
		}
	}

	public void setInfoFlag(boolean b) {
	}

	// 隐藏进度条
	public void SeekBarLayoutHide() {
		SeekBarLayout.setVisibility(View.GONE);
	}

	// 显示进度条
	public void SeekBarLayoutShow() {
		SeekBarLayout.setVisibility(View.VISIBLE);
	}

	public BVideoView getSurfaceView() {
		return videoPlayerSurfaceView;
	}

	public SeekBar getProgressBar() {
		return videoPlayerProgressBar;
	}

	public ImageView getImageViewTop() {
		return videoPlayerImageViewTop;
	}

	public void clearwindows() {
	}

	/*
	 *  
	 */
	public TextView getTitleTextView() {
		return videoPlayerTitleTextView;
	}

	public void setTitleTextView(String text) {
		videoPlayerTitleTextView.setText(text);
	}

	public LinearLayout getProgressLinearLayout() {
		return videoPlayerProgressLinearLayout;
	}

	public ImageView getRewindImageView() {
		return rewindImageView;
	}

	public ImageView getVideoTimeImageView() {
		return videoTimeImageView;
	}

	public ImageView getVideoInformationImageView() {
		return videoInformationImageView;
	}

	public ImageView getSetImageView() {
		return setImageView;
	}

	public LinearLayout getLinearLayoutBottom() {
		return videoPlayerLinearLayoutBottom;
	}

	/**
	 * 把当前的时间设置到textbox上面
	 */
	public void setCurrentPosition(String currentPosition) {
		videoCurrentPositionTextView.setText(currentPosition);
	}

	// 显示总时间
	public void setDuration(String duration) {
		videoTotalTimeTextView.setText(duration);
	}

	/**
	 * 
	 * 非动画隐藏主菜单
	 */
	public void setVideoMenuGone() {
		if (playSettingRelativeLayout.isShown()) {
			setVideoPlaySettingMenuGone();
		}
	}

	/**
	 * 主菜单隐
	 */
	public void setVideoPlaySettingMenuGone() {
		playSettingRelativeLayout.setVisibility(View.GONE);
		videoPlayerTipLayout.setVisibility(View.GONE);
		SeekBarLayout.setVisibility(View.GONE);
	}

	public boolean videoMenuIsVisible() {
		if ((playSettingRelativeLayout.isShown()) && (videoPlayerTipLayout.getVisibility() == View.VISIBLE)
				&& playSettingRelativeLayout.getVisibility() == View.VISIBLE) {
			return true;
		}
		return false;
	}

	public void hideAllViews() {
		if (null != videoPlayerTipLayout) {
			videoPlayerTipLayout.setVisibility(View.GONE);
		}

		if (null != SeekBarLayout) {
			SeekBarLayout.setVisibility(View.GONE);
		}

		if (null != playSettingRelativeLayout) {
			playSettingRelativeLayout.setVisibility(View.GONE);
		}
	}

	public void pause() {
		BVideoPlayerListener listener = videoPlayerActivity.getListener();
		videoPlayerSurfaceView.pause();
		listener.ControlBarHideHandler();
		listener.seekBarHideCancel();
		listener.DelaySeekBarHide();
	}

	public boolean processOkKey(int keyCode, boolean pause) {
		pauseOrStartPlayer(pause);
		return true;
	}

	long waitTime = 2000;
	long touchTime = 0;

	/*
	 * 返回
	 */
	public boolean processBackKey(int keyCode, KeyEvent event) {
		BVideoPlayerListener listener = videoPlayerActivity.getListener();
		listener.delayControlBarHide();
		if (videoMenuIsVisible()) {
			setVideoMenuGone();
		} else {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				BenchUtils.showToast(videoPlayerActivity, R.string.exit_player, Toast.LENGTH_SHORT);
				touchTime = currentTime;
			} else {
				videoPlayerActivity.exitPlayer();
			}
		}
		return true;
	}
}
