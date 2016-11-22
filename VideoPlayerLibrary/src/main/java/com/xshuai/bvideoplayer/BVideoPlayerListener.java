package com.xshuai.bvideoplayer;

import java.io.IOException;
import java.util.Timer;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnNetworkSpeedListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPositionUpdateListener;
import com.baidu.cyberplayer.core.BVideoView.OnSeekCompleteListener;
import com.baidu.cyberplayer.core.BVideoView.OnTotalCacheUpdateListener;
import com.bench.utils.BaiduPCSUtils;
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
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class BVideoPlayerListener {

	protected int activeOption;

	protected int playState;
	protected int progress = 0;// 当前的时间
	protected int ErrorMsg;
	public static final int HIDE_CONTROLBAR_INTERVAL = 5;
	public static final int HIDE_PLAYSTATUS_INTERVAL = 3000;
	private BVideoPlayerActivity videoPlayerActivity = null;
	private BVideoPlayerViewHolder videoPlayerViewHolder = null;
	private BVideoView playView = null;
	private final int UI_EVENT_UPDATE_CURRPOSITION = 1;
	private String path = "";
	// 隐藏视频控制  
	public static final int HIDE_PLAYER_CONTROL = 13;
	public final int SHOW_PLAYER_CONTROL = 14;
	private Timer timerTask = null;
	private boolean isPrepared = false;
	public boolean FORWARD_BACKWARD_MODE = false;
	public double ratio = 1;
	public final int FORWARD_BACKWARD_STEP = 5000;
	public final int FORWARD_BACKWARD_DELAY = 500;
	BaseSettings mh = null;
	/***
	 * 是否正常退出
	 */
	private boolean isNormalFinish = true;
	private final int NORMAL_FINISH_TIMEOUT = 3000;

	public Handler playNormalFinishHandler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				isNormalFinish = true;
			} else if (msg.what == 1) {
				start();
			}
		};
	};

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

	public BVideoPlayerListener(BVideoPlayerActivity activity, BVideoPlayerViewHolder viewHolder) {
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
			mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
			playView.pause();
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			int iseekPos = seekBar.getProgress();
			playView.seekTo(iseekPos);
			playView.resume();
			mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
		}
	};

	public void removeTimer() {
		removeTimerTask();
		if (playstatusHandler != null) {
			seekBarHideCancel();
		}
		if (controlBarHideHandler != null) {
			controlBarHideHandler.removeMessages(HIDE_PLAYER_CONTROL);
		}
		if (controlBarShowHandler != null) {
			controlBarShowHandler.removeMessages(SHOW_PLAYER_CONTROL);
		}

		if (null != mUIHandler) {
			mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
		}
		if (null != mNetworkFlashHandler) {
			mNetworkFlashHandler.removeMessages(0);
		}
	}

	public void setPath(String path) {
		this.path = path;
	}

	boolean ishisseek = false;
	Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/**
			 * 更新进度及时间
			 */
			case UI_EVENT_UPDATE_CURRPOSITION:
				loadingHander.sendEmptyMessage(1);
				int currPosition = playView.getCurrentPosition();
				int duration = playView.getDuration();
				progress = currPosition;
				videoPlayerViewHolder.setCurrentPosition(BenchUtils.formatsecond(currPosition));
				videoPlayerViewHolder.setDuration(BenchUtils.formatsecond(duration));
				videoPlayerViewHolder.getProgressBar().setMax(duration);
				if (playView.isPlaying()) {
					videoPlayerViewHolder.getProgressBar().setProgress(currPosition);
				}
				if (!ishisseek && videoPlayerActivity.lastTime != -1) {
					ishisseek = true;
					mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
					playView.pause();
					AlertDialog.Builder builder = new AlertDialog.Builder(videoPlayerActivity); //先得到构造器  
					builder.setTitle("提示"); //设置标题  
					builder.setMessage("上次播放到" + BenchUtils.formatsecond(videoPlayerActivity.lastTime) + ",继续播放?"); //设置内容  
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮  
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss(); //关闭dialog
							try {
								if (videoPlayerActivity.lastTime <= playView.getDuration()) {
									playView.seekTo(videoPlayerActivity.lastTime);
								}
								playView.resume();
								canStartNetwork = true;
								mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮  
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							playView.resume();
							canStartNetwork = true;
							mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
						}
					});
					builder.create().show();
				} else {
					if (!network) {
						if (playView.getDuration() > 0) {
							network = true;
							try {
								BenchUtils.doLog("videoPlayerActivity.lastTime=" + videoPlayerActivity.lastTime);
								BenchUtils.doLog("playView.getDuration()=" + playView.getDuration());
								if (videoPlayerActivity.lastTime <= playView.getDuration()) {
									playView.seekTo(videoPlayerActivity.lastTime);
								}
								playView.resume();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					canStartNetwork = true;
					ishisseek = true;
					mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
				}
				break;
			default:
				break;
			}
		}
	};

	boolean network = true;
	boolean canStartNetwork = false;
	Handler mNetworkFlashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			BenchUtils.doLog("mNetworkFlashHandler msg.what = " + msg.what);
			if (what == 0) {
				try {
					mNetworkFlashHandler.removeMessages(0);
					int pos = playView.getCurrentPosition();
					if (pos > 0) {
						videoPlayerActivity.lastTime = pos;
						network = false;
					}
					BenchUtils.doLog(
							"mNetworkFlashHandler videoPlayerActivity.lastTime = " + videoPlayerActivity.lastTime);
					playView.pause();
					isNormalFinish = false;
					//TODO
					playNormalFinishHandler.sendEmptyMessageDelayed(0, NORMAL_FINISH_TIMEOUT);
					mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
					playView.stopPlayback();
					pathHandler.removeMessages(0);
					pathHandler.sendEmptyMessageDelayed(0, 2000);
				} catch (Exception e) {
				}
			}
		}
	};

	private int curNetSpeed = 0;
	private int curPer = 0;

	private void play() throws IOException {
		try {
			playView.setOnPreparedListener(mOnPreparedListener);
			playView.setOnSeekCompleteListener(new OnSeekCompleteListener() {

				@Override
				public void onSeekComplete() {
					mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 500);
				}
			});
			playView.setLogLevel(0);
			playView.setDecodeMode(BVideoView.DECODE_SW);
			playView.setOnErrorListener(mOnErrorListener);
			//playView.setVideoPath("http://pl.youku.com/partner/m3u8?vid=CNDIxOTQ3Ng==&type=mp4&ep=DZ5cdBMFxb1AH0nE6wjcpBa0XnDcmDy1nfOsSLl8%2FVnwOKyFeF9cvg%3D%3D&sid=2466768100625864e1026&token=8232&ctype=86&ev=1&oip=1863149221");
			playView.setVideoPath(path);
			//设置缓冲时间
			playView.setCacheTime(4);
			if (null != videoPlayerActivity.rm && videoPlayerActivity.rm.getVideoScalingMode() != 0) {
				playView.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
			} else {
				playView.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
			}
			playView.setKeepScreenOn(true);
			playView.setOnPositionUpdateListener(new OnPositionUpdateListener() {

				@Override
				public boolean onPositionUpdate(long position) {
					BenchUtils.doLog("OnPositionUpdateListener position = " + position);
					return false;
				}
			});
			playView.setRetainLastFrame(true);
			playView.setOnInfoListener(new OnInfoListener() {

				@Override
				public boolean onInfo(int what, int extra) {
					BenchUtils.doLog("oninfo:" + what + ":" + extra);
					switch (what) {
					case BVideoView.MEDIA_INFO_PLAYING_QUALITY:
						BenchUtils.doLog("BVideoView.MEDIA_INFO_PLAYING_QUALITY:" + extra);
						break;
					case BVideoView.MEDIA_INFO_BAD_INTERLEAVING:
						BenchUtils.doLog("BVideoView.MEDIA_INFO_BAD_INTERLEAVING");
						break;
					case BVideoView.MEDIA_INFO_METADATA_UPDATE:
						BenchUtils.doLog("BVideoView.MEDIA_INFO_METADATA_UPDATE");
						break;
					case BVideoView.MEDIA_INFO_NOT_SEEKABLE:
						BenchUtils.doLog("BVideoView.MEDIA_INFO_NOT_SEEKABLE");
						break;
					case BVideoView.MEDIA_INFO_UNKNOWN:
						BenchUtils.doLog("BVideoView.MEDIA_INFO_UNKNOWN");
						break;
					case BVideoView.MEDIA_INFO_VIDEO_TRACK_LAGGING:
						BenchUtils.doLog("BVideoView.MEDIA_INFO_VIDEO_TRACK_LAGGING");
						break;
					/**
					 * 开始缓冲
					 */
					case BVideoView.MEDIA_INFO_BUFFERING_START:
						if (canStartNetwork) {
							mNetworkFlashHandler.removeMessages(0);
							mNetworkFlashHandler.sendEmptyMessageDelayed(0, 15 * 1000);
						}
						mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
						loadingHander.sendEmptyMessage(0);
						BenchUtils.doLog("BVideoView.MEDIA_INFO_BUFFERING_START");
						break;
					/**
					 * 结束缓冲
					 */
					case BVideoView.MEDIA_INFO_BUFFERING_END:
						mNetworkFlashHandler.removeMessages(0);
						mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 100);
						BenchUtils.doLog("BVideoView.MEDIA_INFO_BUFFERING_END");
						break;
					default:
						break;
					}
					return true;
				}
			});
			playView.setOnNetworkSpeedListener(new OnNetworkSpeedListener() {

				@Override
				public void onNetworkSpeedUpdate(int speed) {
					curNetSpeed = speed / 1024;
					loadingHander.sendEmptyMessage(2);
					BenchUtils.doLog("NetworkSpeed = " + curNetSpeed + "kb/s");
				}
			});
			playView.setOnPlayingBufferCacheListener(new OnPlayingBufferCacheListener() {

				@Override
				public void onPlayingBufferCache(int per) {
					curPer = per;
					loadingHander.sendEmptyMessage(2);
				}
			});
			playView.setOnTotalCacheUpdateListener(new OnTotalCacheUpdateListener() {

				@Override
				public void onTotalCacheUpdate(long arg0) {
					BenchUtils.doLog("onTotalCacheUpdate arg0 = " + arg0);
				}

			});
			// P2p缓存地址
			playView.setEnableP2p(false);
			playView.showCacheInfo(false);
			playView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion() {
					BenchUtils.doLog("onCompletion##############");
					if (isNormalFinish) {
						playNextHandler.removeMessages(0);
						playNextHandler.sendEmptyMessageDelayed(0, 2000);
					}
				}
			});
			if (network) {
				playView.start();
			} else {
				playView.start();
				playView.pause();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	boolean isBaidu = false;

	public void start() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (videoPlayerActivity.getMovie().getMovieUrl().startsWith("/apps")) {
					path = BaiduPCSStreamingURL.buildStreamUrl(
							BaiduPCSUtils.getInstance(videoPlayerActivity.getApplicationContext()).getAccessToken(),
							videoPlayerActivity.getMovie().getMovieUrl());
					isBaidu = true;
				} else {
					path = videoPlayerActivity.getMovie().getMovieUrl();
				}
				//曦威胜 p2p
				//				BenchUtils.doLog("MasterService.streamnet = " + MasterService.streamnet);
				//				if(null != MasterService.streamnet){
				//					Map<String, String> para = new HashMap<String, String>();
				//					path = MasterService.streamnet.getPlayURL(path, para);
				//				}
				BenchUtils.doLog("path = " + path);
				pathHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	public void stopPlayback() {
		isPrepared = false;
		removeTimerTask();
		if (playView != null) {
			try {
				String movieid = videoPlayerActivity.getMovie().getMovieUrl();
				if (playView.getCurrentPosition() > 2 * 60) {
					MovieHistory mh = videoPlayerActivity.db
							.findFirst(Selector.from(MovieHistory.class).where("movieid", "=", movieid));
					if (null != mh) {
						videoPlayerActivity.db.delete(mh);
					}
					mh = new MovieHistory();
					mh.setLastTime(playView.getCurrentPosition());
					mh.setMovieid(movieid);
					videoPlayerActivity.db.saveOrUpdate(mh);
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
			try {
				playView.stopPlayback();
			} catch (Exception e) {
			}
		}

	}

	public void stop() {
		isPrepared = false;
		removeTimerTask();
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
	 * 获取总时长
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
				videoPlayerViewHolder.setLoadingProgressPer(" " + curPer + "%\r\n当前网速为" + curNetSpeed + "kb/s");
			} else if (msg.what == 3) {
				BenchUtils.showToast(videoPlayerActivity, msg.arg1, Toast.LENGTH_SHORT);
			} else if (msg.what == 4) {
				videoPlayerViewHolder.setLoadingProgressPer("观看人数过多，正在切换其他源\r\n请耐心等待······");
			}
		};
	};

	Handler tokenErrorHander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				tokenErrorHander.sendEmptyMessage(1);
			} else if (msg.what == 1) {
				try {
					playView.pause();
					isNormalFinish = false;
					playNormalFinishHandler.sendEmptyMessageDelayed(0, NORMAL_FINISH_TIMEOUT);
					mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
					playView.stopPlayback();
					network = false;
					start();
				} catch (Exception e) {
				}
			}
		};
	};
	BVideoView.OnPreparedListener mOnPreparedListener = new BVideoView.OnPreparedListener() {
		@Override
		public void onPrepared() {
			if (videoPlayerActivity == null) {
				return;
			}
			if (videoPlayerViewHolder == null) {
				return;
			}
			if (playView == null) {
				return;
			}
			loadingHander.sendEmptyMessage(1);
			progress = 0; // 重置影片当前时间
			videoPlayerViewHolder.setInfoFlag(true);// 每次进来设置该�?�，如果为true，点击进入电影信息时就重新获取信�?
			isPrepared = true;
			mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
			if (network) {
				controlBarShowHandler.sendEmptyMessage(SHOW_PLAYER_CONTROL);
			}
			videoPlayerActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					String title = "";
					if (null != videoPlayerActivity.getMovie().getDescription()
							&& !"".equals(videoPlayerActivity.getMovie().getDescription())) {
						title += videoPlayerActivity.getMovie().getDescription() + "  ";
					}
					title += videoPlayerActivity.getMovie().getName();
					videoPlayerViewHolder.setTitleTextView(title);
				}
			});
		}
	};

	/**
	 * 错误监听
	 */
	BVideoView.OnErrorListener mOnErrorListener = new BVideoView.OnErrorListener() {

		@Override
		public boolean onError(int what, int extra) {
			BenchUtils.doLog("onError:::what=" + what + ";extra=" + extra);
			Message msg = new Message();
			if (what == BVideoView.MEDIA_ERROR_INVALID_INPUTFILE) {
				if (isBaidu) {
					loadingHander.sendEmptyMessage(4);
					BaiduPCSUtils.getInstance(videoPlayerActivity.getApplicationContext()).initToken(tokenErrorHander,
							BaiduPCSUtils.XSHUAI_ACCESS_TOKEN_URL + "&fresh=1");
					return true;
				} else {
					msg.what = 3;
					msg.arg1 = R.string.video_error_realize_url_fail;
					loadingHander.sendMessage(msg);
				}
			}
			msg.what = 3;
			if (null != videoPlayerActivity) {
				loadingHander.sendEmptyMessage(1);
			}
			ErrorMsg = R.string.videoFormatError;
			switch (what) {
			case BVideoView.MEDIA_ERROR_DISPLAY: {
				if (null != playView) {
					try {
						videoPlayerActivity.exitPlayer();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			}
			case MediaPlayer.MEDIA_ERROR_SERVER_DIED: {
				if (null != videoPlayerActivity) {
					if (!videoPlayerActivity.getExitFlag()) {
						msg.arg1 = R.string.video_error_server_died;
						loadingHander.sendMessage(msg);
					}
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
						if (!videoPlayerActivity.getExitFlag()) {
							msg.arg1 = errorStr;
							loadingHander.sendMessage(msg);
						}
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
					if (!videoPlayerActivity.getExitFlag()) {
						msg.arg1 = errorStr;
						loadingHander.sendMessage(msg);
					}
				}
				break;
			case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
				if (null != videoPlayerActivity) {
					if (!videoPlayerActivity.getExitFlag()) {
						msg.arg1 = R.string.video_not_valid_for_progressive_playback;
						loadingHander.sendMessage(msg);
					}
				}
				break;
			default:
				if (null != videoPlayerActivity) {
					if (what == 0 && progress > 0) {
						break;
					}
					if (!videoPlayerActivity.getExitFlag()) {
						msg.arg1 = R.string.video_error_other_unknown;
						loadingHander.sendMessage(msg);
					}
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
	 * 进度条监听器
	 */
	protected Handler fastBarHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			SeekBar progressBar = videoPlayerViewHolder.getProgressBar();
			if (msg.what == 1) {
				mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
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
					videoPlayerViewHolder.setCurrentPosition(BenchUtils.formatsecond(progress));
				}
			} else if (msg.what == 2) {
				if (FORWARD_BACKWARD_MODE) {
					ratio = 1;
					progressBar.setProgress(progress);
					videoPlayerViewHolder.getSurfaceView().seekTo(progress);
					loadingHander.sendEmptyMessage(0);
					FORWARD_BACKWARD_MODE = false;
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
	 * 按主菜单弹出界面显示的动画.
	 */
	public void StartShowMenuAnimation() {
		StartShowPlaySettingDownAnimation();
		StartShowPlayTipDownAnimation();
		ControlBarHideHandler();
	}

	/*
	 * 按主菜单,隐藏界面显示的动
	 */
	public void StartHideMenuAnimation() {
		StartHidePlaySettingDownAnimation();
		StartHidePlayTipUpAnimation();
	}

	/**
	 * 显示主菜单
	 */
	public void StartShowPlaySettingDownAnimation() {
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
		if (!videoPlayerViewHolder.videoMenuIsVisible()) {
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

	public void removeTimerTask() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}

	/**
	 * 跳到下个
	 */
	public Handler playNextHandler = new Handler() {
		public void handleMessage(Message msg) {
			BenchUtils.doLog("********playNextHandler!!!!!!!");
			playNextHandler.removeMessages(0);
			int curInt = -1;
			if (null != msg.obj) {
				curInt = (Integer) msg.obj;
			} else {
				curInt = videoPlayerActivity.getIndex() + 1;
			}
			if (null != videoPlayerActivity && !videoPlayerActivity.getExitFlag()
					&& null != videoPlayerActivity.getAmList()
					&& videoPlayerActivity.getIndex() < (videoPlayerActivity.getAmList().size() - 1)) {
				if (playView.isPlaying()) {
					playView.pause();
					try {
						playView.stopPlayback();
					} catch (Exception e) {
					}
				}
				videoPlayerActivity.setIndex(curInt);
				videoPlayerActivity.setMovie(videoPlayerActivity.getAmList().get(curInt));
				videoPlayerViewHolder.setLoadingProgressPer(
						"即将为您播放\r\n" + videoPlayerActivity.getAmList().get(curInt).getName() + "\r\n请耐心等待······");
				loadingHander.sendEmptyMessage(0);
				mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
				isNormalFinish = false;
				playNormalFinishHandler.removeMessages(0);
				playNormalFinishHandler.removeMessages(1);
				playNormalFinishHandler.sendEmptyMessageDelayed(1, 2000);
				playNormalFinishHandler.sendEmptyMessageDelayed(0, NORMAL_FINISH_TIMEOUT);
			} else {
				if (null != videoPlayerActivity) {
					videoPlayerActivity.exitPlayer();
				} else {
					stop();
				}
			}
		}
	};

}
