package com.xshuai.netvideoplayer;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.xshuai.netplayer.R;
import com.xshuai.netplayer.entities.AlbumMovie;
import com.xshuai.netplayer.entities.MovieHistory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class NVideoPlayerActivity extends Activity {
	private NVideoPlayerViewHolder videoPlayerViewHolder = null;
	private NVideoPlayerListener videoPlayerListener = null;
	private AlbumMovie movie;
	public DbUtils db;

	public int lastTime = -1;

	public AlbumMovie getMovie() {
		return movie;
	}

	public void setMovie(AlbumMovie movie) {
		this.movie = movie;
	}

	public boolean exitFlag = false;

	// 视频控制
	protected LinearLayout playControlLayout;

	public void exitPlayer() {
		if (exitFlag) {
			return;
		}
		videoPlayerListener.removeTimer();
		exitFlag = true;
		videoPlayerListener.stopPlayback();
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = com.bench.utils.DBUtils.getDbUtils(getApplicationContext());
		setContentView(R.layout.net_video_player);
		try {
			movie = (AlbumMovie) getIntent().getParcelableExtra("curmovie");
		} catch (Exception e) {
		}
		videoPlayerViewHolder = new NVideoPlayerViewHolder(this);
		videoPlayerViewHolder.findViews();
		videoPlayerListener = new NVideoPlayerListener(this, videoPlayerViewHolder);
		if (null == movie) {
			exitPlayer();
			return;
		}
		videoPlayerListener.addSurfaceHolderCallback();
		String title = movie.getName();
		try {
			MovieHistory mh = db.findFirst(Selector.from(MovieHistory.class).where("movieid", "=", title));
			if (null != mh) {
				lastTime = mh.getLastTime();
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		videoPlayerViewHolder.setTitleTextView(title);
		videoPlayerViewHolder.showLoadingDialog();
	}

	public boolean getExitFlag() {
		return exitFlag;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		videoPlayerViewHolder.pause();
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		exitPlayer();
	}

	@Override
	protected void onDestroy() {
		movie = null;
		videoPlayerViewHolder = null;
		videoPlayerListener = null;
		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			return videoPlayerViewHolder.processOkKey(KeyEvent.KEYCODE_ENTER, false);
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER: {
			return videoPlayerViewHolder.processOkKey(keyCode, true);
		}
		case KeyEvent.KEYCODE_BACK: {
			if (videoPlayerViewHolder.processBackKey(keyCode, event)) {
				return true;
			}
		}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: {
			videoPlayerListener.fastControl(-1);
			try {
				videoPlayerViewHolder.getProgressBar().setFocusable(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case KeyEvent.KEYCODE_DPAD_RIGHT: {
			videoPlayerListener.fastControl(1);
			try {
				videoPlayerViewHolder.getProgressBar().setFocusable(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case KeyEvent.KEYCODE_DPAD_DOWN: {
			break;
		}
		case KeyEvent.KEYCODE_DPAD_UP: {
			break;
		}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public NVideoPlayerListener getListener() {
		return videoPlayerListener;
	}

}
