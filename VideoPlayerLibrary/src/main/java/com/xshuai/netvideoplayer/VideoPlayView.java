package com.xshuai.netvideoplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceView;
@SuppressWarnings("unused")
public class VideoPlayView extends SurfaceView {
	private int mDuration;
	private static final int STATE_ERROR = -1;
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREPARING = 1;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PAUSED = 4;

	private int mCurrentState = STATE_IDLE;

	private MediaPlayer mMediaPlayer = null;

	private int mCurrentBufferPercentage;

	public VideoPlayView(Context context) {
		super(context);
	}

	public VideoPlayView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VideoPlayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void start() {
		if (isInPlaybackState()) {
			mMediaPlayer.start();
			mCurrentState = STATE_PLAYING;
		}
	}

	public void pause() {
		if (isInPlaybackState()) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mCurrentState = STATE_PAUSED;
			}
		}
	}

	// cache duration as mDuration for faster access
	public int getDuration() {
		if (isInPlaybackState()) {
			if (mDuration > 0) {
				return mDuration;
			}
			mDuration = mMediaPlayer.getDuration();
			return mDuration;
		}
		mDuration = -1;
		return mDuration;
	}

	public int getCurrentPosition() {
		if (isInPlaybackState()) {
			return mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	public void seekTo(int msec) {
		if (isInPlaybackState()) {
			mMediaPlayer.seekTo(msec);
		} else {
		}
	}

	public void setVolume(float val) {
		if (mMediaPlayer != null) {
			mMediaPlayer.setVolume(val, val);
		}
	}

	public void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			try {
				mMediaPlayer.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mMediaPlayer = null;
		}
	}

	public void reset() {
		if (mMediaPlayer != null) {
			try {
				mMediaPlayer.reset();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mMediaPlayer = null;
		}
	}

	public void stop() {
		if (mMediaPlayer != null) {
			try {
				mMediaPlayer.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isPlaying() {
		return isInPlaybackState() && mMediaPlayer.isPlaying();
	}

	public int getBufferPercentage() {
		if (mMediaPlayer != null) {
			return mCurrentBufferPercentage;
		}
		return 0;
	}

	private boolean isInPlaybackState() {
		return (mMediaPlayer != null);
	}

	// 控制外面乱改内部状态
	public MediaPlayer getMediaPlayer() {
		return mMediaPlayer;
	}

	/**
	 * Register a callback to be invoked
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setPlayerCallbackListener(playerCallback l) {
	}

	// 用户回调接口.
	public interface playerCallback {
		//
		boolean onError(MediaPlayer mp, int framework_err, int impl_err);

		//
		void onCompletion(MediaPlayer mp);

		//
		boolean onInfo(MediaPlayer mp, int what, int extra);

		//
		void onBufferingUpdate(MediaPlayer mp, int percent);

		//
		void onPrepared(MediaPlayer mp);

		//
		void onSeekComplete(MediaPlayer mp);

	}

	public void creatMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
		}
		mMediaPlayer = new MediaPlayer();
	}

	/**
	 * 播放器
	 */
	public void stopMediaPlayer() {
		if (mMediaPlayer != null) {
			try {
				mMediaPlayer.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
