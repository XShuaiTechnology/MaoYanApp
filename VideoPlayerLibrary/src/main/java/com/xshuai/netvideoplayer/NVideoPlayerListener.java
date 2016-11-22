package com.xshuai.netvideoplayer;

import java.io.IOException;

import com.bench.utils.BenchUtils;
import com.bench.utils.DBUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.xshuai.netplayer.R;
import com.xshuai.netplayer.entities.BaseSettings;
import com.xshuai.netplayer.entities.MediaError;
import com.xshuai.netplayer.entities.MovieHistory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class NVideoPlayerListener {

	protected int playState;
	protected int progress = 0;
	protected int ErrorMsg;
	public static final int HIDE_CONTROLBAR_INTERVAL = 5 * 1000;
	private NVideoPlayerActivity videoPlayerActivity = null;
	private NVideoPlayerViewHolder videoPlayerViewHolder = null;
	private VideoPlayView playView = null;
	private final int UI_EVENT_UPDATE_CURRPOSITION = 1;
	private String path = "";
	// 隐藏视频控制
	public static final int HIDE_PLAYER_CONTROL = 13;
	public final int SHOW_PLAYER_CONTROL = 14;
	public boolean isPrepared = false;
	public boolean FORWARD_BACKWARD_MODE = false;
	public double ratio = 1;
	public final int FORWARD_BACKWARD_STEP = 5000;
	public final int FORWARD_BACKWARD_DELAY = 500;

	private int mVideoWidth = 0; //
	private int mVideoHeight = 0; //
	public int videoScreenMode = SURFACE_16_9;

	BaseSettings mh = null;
	Handler pathHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				try {
					play();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};

	protected void addSurfaceHolderCallback() {
		playView.getHolder().addCallback(new Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				BenchUtils.doLog("surfaceCreated ");
				start();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// after we return from this we can't use the surface any more
				removeTimer();
				if (videoPlayerViewHolder != null) {
					videoPlayerViewHolder.getSurfaceView().stopMediaPlayer();
					videoPlayerViewHolder.getSurfaceView().releaseMediaPlayer();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				BenchUtils.doLog("surfaceChanged format=" + format + "width=" + width + "height=" + height);
			}
		});
	}

	public NVideoPlayerListener(NVideoPlayerActivity activity, NVideoPlayerViewHolder viewHolder) {
		super();
		this.videoPlayerActivity = activity;
		this.videoPlayerViewHolder = viewHolder;
		playView = viewHolder.getSurfaceView();
		try {
			mh = videoPlayerActivity.db.findFirst(Selector.from(BaseSettings.class).where("id", "=", 1));
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		if (null == mh) {
			mh = new BaseSettings();
			mh.setId(1);
			mh.setDefinition(DBUtils.VIDEO_DEFINITION_HD);
			try {
				videoPlayerActivity.db.save(mh);
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		viewHolder.getProgressBar().setOnSeekBarChangeListener(osbc1);
	}

	OnSeekBarChangeListener osbc1 = new OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			int iseekPos = seekBar.getProgress();
			playView.seekTo(iseekPos);
			progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
			progressBarHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
		}
	};

	public void removeTimer() {
		if (playstatusHandler != null) {
			seekBarHideCancel();
		}
		if (controlBarHideHandler != null) {
			controlBarHideHandler.removeMessages(HIDE_PLAYER_CONTROL);
		}
		if (controlBarShowHandler != null) {
			controlBarShowHandler.removeMessages(SHOW_PLAYER_CONTROL);
		}
		if (null != progressBarHandler) {
			progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
		}
	}

	public void setPath(String path) {
		this.path = path;
	}

	private int curPer = 0;

	private void play() throws IOException {
		if (videoPlayerViewHolder.getSurfaceView().getMediaPlayer() != null) {
			videoPlayerViewHolder.getSurfaceView().releaseMediaPlayer();
		}
		try {
			videoPlayerViewHolder.getSurfaceView().creatMediaPlayer();
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().reset();
			isPrepared = false;
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().setOnPreparedListener(mOnPreparedListener);
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().setOnInfoListener(mOnInfoListener);
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().setOnSeekCompleteListener(mOnSeekCompleteListener);
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().setOnCompletionListener(mOnCompletionListener);
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().setOnErrorListener(mOnErrorListener);
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer()
					.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().setDataSource(path);
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().setDisplay(playView.getHolder());
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().setScreenOnWhilePlaying(true);
			videoPlayerViewHolder.getSurfaceView().getMediaPlayer().prepareAsync();// 异步加载
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(videoPlayerActivity, R.string.the_document_read_mistake_please_inspect_the_data_pool,
					Toast.LENGTH_SHORT).show();
			return;
		}
	}

	public void start() {
		path = videoPlayerActivity.getMovie().getMovieUrl();
		BenchUtils.doLog("path = " + path);
		pathHandler.sendEmptyMessage(0);
	}

	public void stopPlayback() {
		progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
		isPrepared = false;
		if (playView != null) {
			try {
				String movieid = videoPlayerActivity.getMovie().getName();
				if (playView.getCurrentPosition() > 0) {
					MovieHistory mh = videoPlayerActivity.db
							.findFirst(Selector.from(MovieHistory.class).where("movieid", "=", movieid));
					if (null != mh) {
						mh.setLastTime(playView.getCurrentPosition());
					} else {
						mh = new MovieHistory();
						mh.setLastTime(playView.getCurrentPosition());
						mh.setMovieid(movieid);
					}
					videoPlayerActivity.db.saveOrUpdate(mh);
				}
				playView.stop();
			} catch (DbException e) {
				e.printStackTrace();
			}
			try {
				playView.stopMediaPlayer();
			} catch (Exception e) {
			}
		}

	}

	public void stop() {
		isPrepared = false;
		if (playView == null) {
			return;
		}
		try {
			stopPlayback();
		} catch (Exception e) {
		}
	}

	public int mCurrentPositionForHis = 0;

	/*
	 * 获取总时�?
	 */
	public int getDuration() {
		if (playView != null && isPrepared) {
			return playView.getDuration();
		}
		return 0;
	}

	Handler loadingHander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if (videoPlayerActivity != null) {
					if (!videoPlayerViewHolder.isShowloadingDialog()) {
						videoPlayerViewHolder.showLoadingDialog();
					}
				}
			} else if (msg.what == 1) {
				if (videoPlayerActivity != null) {
					if (videoPlayerViewHolder.isShowloadingDialog()) {
						videoPlayerViewHolder.loadingDialogDismiss();
					}
				}
			} else if (msg.what == 2) {
				if (curPer == 0) {
					videoPlayerViewHolder.setLoadingProgressPer(" ");
				} else {
					videoPlayerViewHolder.setLoadingProgressPer(" " + curPer + "%");
				}
			}
		};
	};

	/**
	 * 进度条监听器
	 */
	protected Handler progressBarHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == UI_EVENT_UPDATE_CURRPOSITION) {
				SeekBar progressBar = videoPlayerViewHolder.getProgressBar();
				if (videoPlayerViewHolder.getSurfaceView() != null && isPrepared
						&& videoPlayerViewHolder.getSurfaceView().isPlaying()) {
					progress = videoPlayerViewHolder.getSurfaceView().getCurrentPosition();
					BenchUtils.doLog("progress = " + progress);
					progressBar.setProgress(progress);
					try {
						videoPlayerViewHolder.setDuration(
								BenchUtils.formatMillisecond(videoPlayerViewHolder.getSurfaceView().getDuration()));
					} catch (Exception e) {
					}
					videoPlayerViewHolder.setCurrentPosition(BenchUtils.formatMillisecond(progress));
				}
				progressBarHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 500);
			}
		}
	};

	/**
	 * 进度条监听器
	 */
	protected Handler fastBarHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			SeekBar progressBar = videoPlayerViewHolder.getProgressBar();
			if (msg.what == 1) {
				progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
				if (FORWARD_BACKWARD_MODE) {
					if (ratio < 15) {
						ratio += 1;
					}
					progress += (int) (HIDE_CONTROLBAR_INTERVAL * ratio) * msg.arg1;
					if (msg.arg1 > 0) {
						progress = (progress > getDuration() ? getDuration() : progress);
					} else {
						progress = (progress > 0 ? progress : 0);
					}
					progressBar.setProgress(progress);
					videoPlayerViewHolder.setCurrentPosition(BenchUtils.formatMillisecond(progress));
				}
			} else if (msg.what == 2) {
				if (FORWARD_BACKWARD_MODE) {
					ratio = 1;
					progressBar.setProgress(progress);
					videoPlayerViewHolder.getSurfaceView().seekTo(progress);
					loadingHander.sendEmptyMessage(0);
					FORWARD_BACKWARD_MODE = false;
					progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
					progressBarHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 500);
				}
			}
		}
	};

	protected Handler playstatusHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (videoPlayerViewHolder.getSeekBarLayout().isShown()
					&& !videoPlayerViewHolder.getVideoPlaySettingRelativeLayout().isShown()) // seekbar
			{
				StartHideSeekBarDownAnimation();
			}
		}
	};

	public void seekBarHideCancel() {
		if (playstatusHandler != null)
			playstatusHandler.removeMessages(0);
	}

	public void DelaySeekBarHide() {
		if (playstatusHandler != null)
			playstatusHandler.sendEmptyMessageDelayed(0, HIDE_CONTROLBAR_INTERVAL);
	}

	public void seekBarHideImmediatly() {
		if (playstatusHandler != null)
			playstatusHandler.sendEmptyMessage(0);
	}

	/**
	 * 按主菜单,弹出界面显示的动画.
	 */
	public void StartShowMenuAnimation() {
		StartShowPlaySettingDownAnimation();
		StartShowPlayTipDownAnimation();
		ControlBarHideHandler();
	}

	/*
	 * 按主菜单,隐藏界面显示的动�?.
	 */
	public void StartHideMenuAnimation() {
		StartHidePlaySettingDownAnimation();
		StartHidePlayTipUpAnimation();
	}

	/**
	 * 显示主菜�?
	 */
	public void StartShowPlaySettingDownAnimation() {
		BenchUtils.doLog("*******StartShowPlaySettingDownAnimation********");
		Animation DownTocurrentAnimation = AnimationUtils.loadAnimation(videoPlayerActivity, R.anim.down_to_current);
		DownTocurrentAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				videoPlayerViewHolder.getVideoPlaySettingRelativeLayout().setVisibility(View.VISIBLE);
				videoPlayerViewHolder.getSeekBarLayout().setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
			}
		});
		videoPlayerViewHolder.getVideoPlaySettingRelativeLayout().startAnimation(DownTocurrentAnimation);
	}

	/**
	 * 隐藏主菜�?
	 */
	public void StartHidePlaySettingDownAnimation() {
		if (!videoPlayerViewHolder.getVideoPlaySettingRelativeLayout().isShown()) // 如果没有显示就不用启动动�?
			return;
		Animation currentToDownAnimation = AnimationUtils.loadAnimation(videoPlayerActivity, R.anim.current_to_down);
		// videoPlayerViewHolder.getSeekBarLayout().startAnimation(currentToDownAnimation);

		currentToDownAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				videoPlayerViewHolder.getVideoPlaySettingRelativeLayout().setVisibility(View.GONE);
				seekBarHideCancel();
				seekBarHideImmediatly();// 菜单隐藏后，立刻隐藏进度条和提示文字
			}
		});
		videoPlayerViewHolder.getVideoPlaySettingRelativeLayout().startAnimation(currentToDownAnimation);
	}

	/**
	 * 显示窗口眉头(从屏幕上方出来的动画)
	 */
	public void StartShowPlayTipDownAnimation() {
		if (videoPlayerViewHolder.getVideoPlayTipListLayout().isShown()) // 如果已经显示了就不用启动动画
			return;
		Animation currentToDownAnimation = AnimationUtils.loadAnimation(videoPlayerActivity, R.anim.up_to_current);
		currentToDownAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				videoPlayerViewHolder.getVideoPlayTipListLayout().setVisibility(View.VISIBLE);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// videoPlayerViewHolder.getVideoPlayTipListLayout().setVisibility(View.VISIBLE);

			}
		});
		videoPlayerViewHolder.getVideoPlayTipListLayout().startAnimation(currentToDownAnimation);
	}

	/**
	 * 隐藏窗口眉头
	 */
	public void StartHidePlayTipUpAnimation() {
		if (!videoPlayerViewHolder.getVideoPlayTipListLayout().isShown()) // 如果本来是隐藏的就不�?要再做动�?
			return;
		Animation currentToUpAnimation = AnimationUtils.loadAnimation(videoPlayerActivity, R.anim.current_to_up);
		currentToUpAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				videoPlayerViewHolder.getVideoPlayTipListLayout().setVisibility(View.GONE);
			}
		});
		videoPlayerViewHolder.getVideoPlayTipListLayout().startAnimation(currentToUpAnimation);
	}

	public void fastControl(int i) {
		ControlBarHideCancel();
		BenchUtils.doLog("videoPlayerViewHolder.videoMenuIsVisible() = " + videoPlayerViewHolder.videoMenuIsVisible());
		if (!videoPlayerViewHolder.videoMenuIsVisible()) {
			videoPlayerViewHolder.getVideoPlaySettingRelativeLayout().setVisibility(View.VISIBLE);
			StartShowMenuAnimation();
		}
		FORWARD_BACKWARD_MODE = true;
		Message msg = new Message();
		msg.what = 1;
		msg.arg1 = i;
		fastBarHandler.sendMessage(msg);
		fastBarHandler.removeMessages(2);
		fastBarHandler.sendEmptyMessageDelayed(2, FORWARD_BACKWARD_DELAY);
		delayControlBarHide();
	}

	/**
	 * 隐藏进度�?
	 */
	public void StartHideSeekBarDownAnimation() {
		if (!videoPlayerViewHolder.getVideoPlaySettingRelativeLayout().isShown())
			videoPlayerViewHolder.SeekBarLayoutHide();
	}

	protected Handler controlBarHideHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == HIDE_PLAYER_CONTROL) {
				StartHideMenuAnimation();
			}
		}
	};

	protected Handler controlBarShowHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == SHOW_PLAYER_CONTROL) {
				StartShowMenuAnimation();
			}

		}
	};

	protected void delayControlBarHide() {
		if (controlBarHideHandler != null) {
			controlBarHideHandler.removeMessages(HIDE_PLAYER_CONTROL);
			controlBarHideHandler.sendEmptyMessageDelayed(HIDE_PLAYER_CONTROL, 5000);
		}
	}

	/**
	 * 显示菜单延时（播放成功后3秒显示菜单）
	 */
	public void ControlBarShow() {
		// StartShowMenuAnimation();
		if (controlBarShowHandler != null)
			controlBarShowHandler.sendEmptyMessageDelayed(SHOW_PLAYER_CONTROL, 3000);
	}

	/**
	 * 取消延时显示菜单的消�?
	 */
	public void ControlBarShowCancel() {
		if (controlBarShowHandler != null)
			controlBarShowHandler.removeMessages(SHOW_PLAYER_CONTROL);
	}

	/**
	 * 设置 隐藏菜单 的延时消息（5秒）
	 */
	public void ControlBarHideHandler() {
		if (controlBarHideHandler != null)
			controlBarHideHandler.sendEmptyMessageDelayed(HIDE_PLAYER_CONTROL, 5000);
	}

	/**
	 * 取消隐藏菜单的延时消�?
	 */
	public void ControlBarHideCancel() {
		if (controlBarHideHandler != null)
			controlBarHideHandler.removeMessages(HIDE_PLAYER_CONTROL);
	}

	OnSeekCompleteListener mOnSeekCompleteListener = new OnSeekCompleteListener() {

		@Override
		public void onSeekComplete(MediaPlayer mp) {
			videoPlayerViewHolder.loadingDialogDismiss();
			videoPlayerViewHolder.getSurfaceView().start();
		}
	};

	OnInfoListener mOnInfoListener = new OnInfoListener() {

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			BenchUtils.doLog("==========onInfo============: (" + what + ", " + extra + ")");

			// TODO Auto-generated method stub
			switch (what) {
			case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
				BenchUtils.doLog("MEDIA_INFO_BAD_INTERLEAVING");
				break;
			case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
				BenchUtils.doLog("MEDIA_INFO_METADATA_UPDATE");
				break;
			case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
				BenchUtils.doLog("MEDIA_INFO_VIDEO_TRACK_LAGGING");
				break;
			case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
				BenchUtils.doLog("MEDIA_INFO_NOT_SEEKABLE");
				if (videoPlayerActivity != null && videoPlayerViewHolder != null) {
					BenchUtils.showToast(videoPlayerActivity, R.string.video_file_have_no_index, Toast.LENGTH_SHORT);
				}
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				if (videoPlayerActivity != null) {
					videoPlayerViewHolder.showLoadingDialog();
					progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
				}
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				videoPlayerViewHolder.getSurfaceView().start();
				if (videoPlayerViewHolder != null) {
					videoPlayerViewHolder.loadingDialogDismiss();
					progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
					progressBarHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
				}
				break;
			default:
				break;
			}

			return true;
		}
	};

	OnPreparedListener mOnPreparedListener = new OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer mp) {
			isPrepared = true;
			if (videoPlayerActivity == null) {
				BenchUtils.doLog("videoPlayerActivity == null");
				return;
			}
			if (videoPlayerViewHolder == null) {
				BenchUtils.doLog("videoPlayerViewHolder == null");
				return;
			}
			if (videoPlayerViewHolder.getSurfaceView().getMediaPlayer() == null) {
				BenchUtils.doLog("videoPlayerViewHolder.getSurfaceView().getMediaPlayer() == null");
				return;
			}
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();
			BenchUtils.doLog("mVideoWidth = " + mVideoWidth + ";mVideoHeight=" + mVideoHeight);

			videoPlayerViewHolder.loadingDialogDismiss();
			progress = 0; // 重置影片当前时间
			isPrepared = true;
			setVideoScreenMode(SURFACE_FIT_TO_CROP);
			int totalTime = getDuration();// 获取总时
			videoPlayerViewHolder.getProgressBar().setMax(totalTime);// 设置进度条的总时
			String duration = BenchUtils.formatMillisecond(totalTime);
			videoPlayerViewHolder.setDuration(duration);
			if (videoPlayerActivity.lastTime != -1) {
				AlertDialog.Builder builder = new AlertDialog.Builder(videoPlayerActivity); //先得到构造器  
				builder.setTitle("提示"); //设置标题  
				builder.setMessage("上次播放到" + BenchUtils.formatMillisecond(videoPlayerActivity.lastTime) + ",继续播放?"); //设置内容  
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();//关闭dialog
						try {
							try {
								if (videoPlayerActivity.lastTime <= playView.getDuration()) {
									playView.seekTo(videoPlayerActivity.lastTime);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							playView.start();
							progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
							progressBarHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 500);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						playView.start();
						progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
						progressBarHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 500);
					}
				});
				builder.create().show();
			} else {
				try {
					playView.start();
					progressBarHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
					progressBarHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 500);
					StartShowMenuAnimation();
				} catch (Exception e) {
				}
			}
		}
	};

	OnVideoSizeChangedListener mOnVideoSizeChangedListener = new OnVideoSizeChangedListener() {
		@Override
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

		}
	};

	/**
	 * 播放完成的监听器
	 */
	OnCompletionListener mOnCompletionListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			try {
				if (!videoPlayerActivity.exitFlag) {
					OnCompletion();
				}
			} catch (Exception e) {
			}
		}
	};

	private void OnCompletion() {
		if (videoPlayerViewHolder != null) {
			videoPlayerViewHolder.getProgressBar().setProgress(0);
			videoPlayerViewHolder.setCurrentPosition(BenchUtils.formatMillisecond(getDuration()));
		}
		if (videoPlayerActivity != null && videoPlayerViewHolder != null) {
			videoPlayerActivity.exitPlayer();
		}
	}

	/**
	 * 错误监听
	 */
	OnErrorListener mOnErrorListener = new OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			ErrorMsg = R.string.videoFormatError;
			switch (what) {
			case MediaPlayer.MEDIA_ERROR_SERVER_DIED: {
				if (null != videoPlayerActivity) {
					if (!videoPlayerActivity.getExitFlag())
						BenchUtils.showToast(videoPlayerActivity, R.string.video_error_server_died, Toast.LENGTH_SHORT);
				}
				break;
			}
			case MediaPlayer.MEDIA_ERROR_UNKNOWN:
				int errorStr;
				switch (extra) {
				case MediaError.ERROR_MALFORMED:
					errorStr = R.string.video_error_malformed;
					break;
				case MediaError.ERROR_IO:
					errorStr = R.string.video_error_io;
					break;
				case MediaError.ERROR_UNSUPPORTED:
					errorStr = R.string.video_unsupport;
					break;
				case MediaError.ERROR_FILE_FORMAT_UNSUPPORT:
					errorStr = R.string.video_file_format_support;
					break;
				case MediaError.ERROR_NOT_CONNECTED:
					errorStr = R.string.video_not_connected;
					break;
				case MediaError.ERROR_AUDIO_UNSUPPORT: {
					errorStr = R.string.video_audio_unsupport;
					if (videoPlayerActivity != null) {
						if (!videoPlayerActivity.getExitFlag())
							BenchUtils.showToast(videoPlayerActivity, errorStr, Toast.LENGTH_SHORT);
					}
					return true;
				}
				case MediaError.ERROR_VIDEO_UNSUPPORT:
					errorStr = R.string.video_video_unsupport;
					break;
				case MediaError.ERROR_DRM_NO_LICENSE:
					errorStr = R.string.video_drm_no_license;
					break;
				case MediaError.ERROR_DRM_LICENSE_EXPIRED:
					errorStr = R.string.video_drm_license_expired;
					break;
				default:
					errorStr = R.string.video_error_unknown;
					break;
				}
				if (null != videoPlayerActivity) {
					if (!videoPlayerActivity.getExitFlag())
						BenchUtils.showToast(videoPlayerActivity, errorStr, Toast.LENGTH_SHORT);
				}
				break;
			case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
				if (null != videoPlayerActivity) {
					if (!videoPlayerActivity.getExitFlag())
						BenchUtils.showToast(videoPlayerActivity, R.string.video_not_valid_for_progressive_playback,
								Toast.LENGTH_SHORT);
				}
				break;
			default:
				if (null != videoPlayerActivity) {
					if (what == 0 && progress > 0) {
						break;
					}
					if (!videoPlayerActivity.getExitFlag())
						BenchUtils.showToast(videoPlayerActivity, R.string.video_error_other_unknown,
								Toast.LENGTH_SHORT);
				}
				break;
			}

			if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
				if (null != videoPlayerActivity) {
					videoPlayerActivity.exitPlayer();
				} else {
					stop();
				}
				return true;
			}
			return true;
		}
	};

	/**
	 * 网络数据缓冲监听
	 */
	OnBufferingUpdateListener mOnBufferingUpdateListener = new OnBufferingUpdateListener() {
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			curPer = percent;
			BenchUtils.doLog("percent = " + percent);
			loadingHander.sendEmptyMessage(2);
		}
	};

	/**
	 * 切换画面比例.
	 * 
	 * @param mode
	 *            画面比例模式.
	 */
	public void setVideoScreenMode(int mode) {
		if (mVideoWidth <= 0 || mVideoHeight <= 0 || !isPrepared) {
			return;
		}
		videoScreenMode = mode;
		changeSurfaceSize();
	}

	private static final int SURFACE_4_3 = 0;
	private static final int SURFACE_16_9 = 1;
	private static final int SURFACE_ORIGINAL = 2;
	private static final int SURFACE_FIT_TO_CROP = 3;

	@SuppressWarnings("deprecation")
	private void changeSurfaceSize() {

		// get screen size
		int dw = videoPlayerActivity.getWindowManager().getDefaultDisplay().getWidth();
		int dh = videoPlayerActivity.getWindowManager().getDefaultDisplay().getHeight();
		BenchUtils.doLog(mVideoWidth + ":" + mVideoHeight + ":" + dw + ":" + dh + ":");
		// calculate aspect ratio
		double ar = (double) mVideoWidth / (double) mVideoHeight;
		// calculate display aspect ratio
		double dar = (double) dw / (double) dh;

		switch (videoScreenMode) {
		case SURFACE_16_9:
			ar = 16.0 / 9.0;
			if (dar < ar)
				dh = (int) (dw / ar);
			else
				dw = (int) (dh * ar);
			break;
		case SURFACE_4_3:
			ar = 4.0 / 3.0;
			if (dar < ar)
				dh = (int) (dw / ar);
			else
				dw = (int) (dh * ar);
			break;
		case SURFACE_ORIGINAL:
			dh = mVideoHeight;
			dw = mVideoWidth;
			break;
		case SURFACE_FIT_TO_CROP:
			float scalex = (float) mVideoWidth / (float) dw;
			float scaley = (float) mVideoHeight / (float) dh;
			float finalScale = Math.max(scalex, scaley);

			dh = (int) ((float) mVideoHeight / finalScale);
			dw = (int) ((float) mVideoWidth / finalScale);
			;
			break;
		}
		videoPlayerViewHolder.getSurfaceView().getHolder().setFixedSize(dw, dh);
		videoPlayerViewHolder.getSurfaceView().invalidate();
	}

}
