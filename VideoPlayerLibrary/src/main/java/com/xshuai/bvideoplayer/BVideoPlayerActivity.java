package com.xshuai.bvideoplayer;

import java.util.List;

import com.bench.entities.ReadMe;
import com.bench.utils.BenchUtils;
import com.bench.utils.DBUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.xshuai.netplayer.R;
import com.xshuai.netplayer.entities.AlbumMovie;
import com.xshuai.netplayer.entities.MovieHistory;
import com.xshuai.tv.TvEpisodeListDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class BVideoPlayerActivity extends Activity {
	private BVideoPlayerViewHolder videoPlayerViewHolder = null;
	private BVideoPlayerListener videoPlayerListener = null;
	private AlbumMovie movie;
	List<AlbumMovie> amList;
	public ReadMe rm;
	private TvEpisodeListDialog tvListDialog;
	int index = 0;
	public DbUtils db;

	public int lastTime = -1;

	public AlbumMovie getMovie() {
		return movie;
	}

	public void setMovie(AlbumMovie movie) {
		this.movie = movie;
	}

	public boolean exitFlag = false;

	protected LinearLayout playControlLayout;

	public void exitPlayer() {
		if (exitFlag) {
			return;
		}
		videoPlayerListener.removeTimer();
		exitFlag = true;
		videoPlayerListener.stopPlayback();
		BenchUtils.doLog("exitPlayer finish()");
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = DBUtils.getDbUtils(getApplicationContext());
		setContentView(R.layout.video_player);
		videoPlayerViewHolder = new BVideoPlayerViewHolder(this);
		videoPlayerViewHolder.findViews();
		videoPlayerListener = new BVideoPlayerListener(this, videoPlayerViewHolder);
		try {
			Intent it = getIntent();
			if (null != it) {
				amList = it.getParcelableArrayListExtra("list");
				if (null != amList && amList.size() > 0) {
					index = it.getIntExtra("curindex", 0);
					if (index >= 0 && index < amList.size()) {
						movie = amList.get(index);
					}
					tvListDialog = new TvEpisodeListDialog(this, amList, videoPlayerListener.playNextHandler);
				} else {
					movie = (AlbumMovie) it.getParcelableExtra("curmovie");
				}
				try {
					rm = (ReadMe) it.getSerializableExtra("readme");
				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == movie) {
			exitPlayer();
			return;
		}
		try {
			MovieHistory mh = db
					.findFirst(Selector.from(MovieHistory.class).where("movieid", "=", movie.getMovieUrl()));
			if (null != mh) {
				lastTime = mh.getLastTime();
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		videoPlayerViewHolder.showLoadingDialog();
		videoPlayerListener.start();
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
			try {
				videoPlayerViewHolder.getProgressBar().setFocusable(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			videoPlayerListener.fastControl(-1);
			break;
		}
		case KeyEvent.KEYCODE_DPAD_RIGHT: {
			try {
				videoPlayerViewHolder.getProgressBar().setFocusable(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			videoPlayerListener.fastControl(1);
			break;
		}
		case KeyEvent.KEYCODE_DPAD_DOWN: {
			break;
		}
		case KeyEvent.KEYCODE_DPAD_UP: {
			break;
		}
		case KeyEvent.KEYCODE_MENU: {
			if (null != tvListDialog) {
				if (tvListDialog.isShowing()) {
					tvListDialog.dismiss();
				} else {
					tvListDialog.show(index);
				}
			}
			break;
		}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public BVideoPlayerListener getListener() {
		return videoPlayerListener;
	}

	public List<AlbumMovie> getAmList() {
		return amList;
	}

	public void setAmList(List<AlbumMovie> amList) {
		this.amList = amList;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
