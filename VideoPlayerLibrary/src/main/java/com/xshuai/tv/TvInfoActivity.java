package com.xshuai.tv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.baidu.pcs.BaiduPCSActionInfo.PCSCommonFileInfo;
import com.baidu.pcs.BaiduPCSActionInfo.PCSListInfoResponse;
import com.baidu.pcs.BaiduPCSClient;
import com.bench.entities.ReadMe;
import com.bench.utils.BaiduPCSUtils;
import com.bench.utils.BenchUtils;
import com.bench.utils.DBUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xshuai.AppApplication;
import com.xshuai.bvideoplayer.BVideoPlayerActivity;
import com.xshuai.netplayer.R;
import com.xshuai.netplayer.entities.AlbumMovie;
import com.xshuai.netplayer.entities.MovieHistory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class TvInfoActivity extends Activity {
	Point windowPoint = new Point();
	RelativeLayout rl_bginfo;
	ImageView iv_bginfo;
	ImageView iv_background;
	Button btnTvPlay;
	Button btnTvSelect;
	LinearLayout ll_tv;
	AppApplication app;
	Gson gson = new Gson();
	private ProgressBar mLoadingprogressBar;
	List<AlbumMovie> amList = new ArrayList<AlbumMovie>();
	BaiduPCSClient api;
	public ExecutorService executorService;
	private TvListDialog tvListDialog;
	HttpHandler<String> httpHandler = null;
	private HttpUtils hUtils;
	int i = 0;
	String path;
	public DbUtils db;
	private ReadMe rm;
	static String tvName = "";
	Handler mhandle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i(BenchUtils.TAG, "TvInfoActivity mhandle msg.what = " + msg.what);
			if (msg.what == 0) {
				initBtnData();
			} else if (msg.what == 1) {
				loadBaiduData();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			path = getIntent().getStringExtra("tv_url");
			tvName = getIntent().getStringExtra("name");
		} catch (Exception e) {
		}
		app = (AppApplication) TvInfoActivity.this.getApplicationContext();
		hUtils = new HttpUtils();
		hUtils.configDefaultHttpCacheExpiry(BaiduPCSUtils.FRESH_TIMEOUT);
		db = DBUtils.getDbUtils(getApplicationContext());
		setContentView(R.layout.tv_info);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		if (null != rm && null != api) {
			mhandle.sendEmptyMessageDelayed(1, 500);
		}
		super.onResume();
	}

	/**
	 * 
	 */
	private void initBtnData() {
		if (amList.size() > 0) {
			try {
				MovieHistory mh = db.findFirst(
						Selector.from(MovieHistory.class).where("movieid", " like ", path + "%").orderBy("id", true));
				if (null != mh) {
					for (int i = 0; i < amList.size(); i++) {
						AlbumMovie am = amList.get(i);
						if (null != mh && mh.getMovieid().equals(am.getMovieUrl())) {
							index = i;
							btnTvPlay.setText(getMovieName(mh.getMovieid()));
						}
					}

				}
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	}

	int failNum = 0;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i(BenchUtils.TAG, "TvInfoActivity mHandler msg.what = " + msg.what);
			if (msg.what == 0) {
				// 允许有限次数获取 token失败
				if (failNum < 10) {
					initData();
				}
			}
		};
	};
	protected int index = 0;
	MovieHistory mh = null;

	@Override
	protected void onStop() {
		executorService.shutdownNow();
		super.onStop();
	}

	protected void loadBaiduData() {
		if (null != executorService && !executorService.isShutdown()) {
			executorService.shutdown();
		}
		executorService = Executors.newCachedThreadPool();
		api = new BaiduPCSClient();
		api.setAccessToken(BaiduPCSUtils.getInstance(getApplicationContext()).getAccessToken());
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				PCSListInfoResponse plir = api.list(path, "name", (rm.getOrderBy() == 0) ? "asc" : "desc");
				boolean hastoken = true;
				if (null == plir) {
					Log.i(BenchUtils.TAG, "PCSListInfoResponse is null ");
					hastoken = false;
				}
				List<PCSCommonFileInfo> list = plir.list;
				if (null == list) {
					hastoken = false;
					Log.i(BenchUtils.TAG, "PCSListInfoResponse list is null ");
				}
				if (!hastoken) {
					failNum++;
					BaiduPCSUtils.getInstance(getApplicationContext()).initToken(mHandler,
							BaiduPCSUtils.XSHUAI_ACCESS_TOKEN_URL + "&fresh=1");
					return;
				}
				if (list.size() > 0) {
					if (amList.size() > 0)
						amList.clear();
					try {
						mh = db.findFirst(Selector.from(MovieHistory.class).where("movieid", " like ", path + "%")
								.orderBy("id", true));
					} catch (DbException e1) {
						e1.printStackTrace();
					}
					BenchUtils.doLog("list.size() = " + list.size());
					for (int i = 0; i < list.size(); i++) {
						PCSCommonFileInfo pcfi = list.get(i);
						String ext = getExtension(pcfi.path);
						if (VIDEO_EXTENSIONS.contains(ext)) {
							AlbumMovie am = new AlbumMovie();
							if (null != mh && mh.getMovieid().equals(pcfi.path)) {
								index = i;
							}
							String name = getMovieName(pcfi.path);
							am.setName(name);
							am.setDescription(getTvName(path));
							am.setMovieUrl(pcfi.path);
							amList.add(am);
						}
					}
					TvInfoActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							tvListDialog = new TvListDialog(TvInfoActivity.this, amList, rm);
							btnTvSelect.setOnKeyListener(btnKeyListener);
							btnTvSelect.setOnTouchListener(btnOnTouchListener);
							btnTvPlay.setOnKeyListener(btnKeyListener);
							btnTvPlay.setOnTouchListener(btnOnTouchListener);
							if (null != mh) {
								btnTvPlay.setText(getMovieName(mh.getMovieid()));
							}
							app.displayImage(getRealUrl(getTvName(path) + "front"), iv_bginfo,
									new ImageLoadingListener() {

								@Override
								public void onLoadingStarted(String arg0, View arg1) {

								}

								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

								}

								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
									LayoutParams rl = (LayoutParams) rl_bginfo.getLayoutParams();
									rl.height = windowPoint.x * arg2.getHeight() / arg2.getWidth();
									rl_bginfo.setVisibility(View.VISIBLE);
									loadingDialogDismiss();
								}

								@Override
								public void onLoadingCancelled(String arg0, View arg1) {
								}
							});
							app.displayImage(getRealUrl(getTvName(path) + "background"), iv_background);
						}
					});
				}
			}
		});
	}

	/**
	 * 
	 */
	private void initData() {
		if (httpHandler != null && httpHandler.getState() != State.FAILURE && httpHandler.getState() != State.SUCCESS
				&& httpHandler.getState() != State.CANCELLED) {
			return;
		}
		httpHandler = hUtils.send(HttpMethod.GET, getRealUrl(getTvName(path) + "readme"),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						rm = new ReadMe();
						loadBaiduData();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String res = arg0.result;
						BenchUtils.doLog("res = " + res);
						if (!BenchUtils.isNull(res)) {
							try {
								rm = gson.fromJson(res, ReadMe.class);
								BenchUtils.doLog("rm.getCache() = " + rm.getCache());
								if (rm.getCache() == 0) {
									app.clearUniversalImageLoaderCache();
								}
							} catch (Exception e) {
								rm = new ReadMe();
							}
						} else {
							rm = new ReadMe();
						}
						loadBaiduData();
					}
				});
	}

	private void initView() {
		Display display = getWindowManager().getDefaultDisplay();
		display.getSize(windowPoint);
		iv_background = (ImageView) findViewById(R.id.iv_background);
		rl_bginfo = (RelativeLayout) findViewById(R.id.rl_bginfo);
		iv_bginfo = (ImageView) findViewById(R.id.iv_bginfo);
		mLoadingprogressBar = (ProgressBar) findViewById(R.id.LoadingprogressBar);
		showLoadingDialog();
		LayoutParams lp = (LayoutParams) rl_bginfo.getLayoutParams();
		rl_bginfo.setLayoutParams(lp);
		btnTvPlay = (Button) findViewById(R.id.btn_tv_play);
		btnTvSelect = (Button) findViewById(R.id.btn_tv_select);
	}

	OnTouchListener btnOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Button btn = null;
			if (v instanceof Button) {
				btn = (Button) v;
			}
			if (KeyEvent.ACTION_DOWN == event.getAction()) {
				v.setBackgroundResource(R.drawable.btn_pressed);
				if (null != btn) {
					btn.setTextColor(Color.WHITE);
				}
			} else if (KeyEvent.ACTION_UP == event.getAction()) {
				v.setBackgroundResource(R.drawable.selector_btn);
				if (null != btn) {
					btn.setTextColor(getResources().getColor(R.color.selector_btn_text));
				}
				if (btn == btnTvSelect) {
					if (tvListDialog.isShowing()) {
						tvListDialog.dismiss();
					} else {
						tvListDialog.show();
					}
				} else {
					Intent it = new Intent();
					it.setClass(TvInfoActivity.this, BVideoPlayerActivity.class);
					it.putExtra("curindex", index);
					it.putExtra("readme", rm);
					it.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) amList);
					TvInfoActivity.this.startActivity(it);
				}
			}
			return false;
		}
	};

	OnKeyListener btnKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (KeyEvent.KEYCODE_ENTER == keyCode || KeyEvent.KEYCODE_DPAD_CENTER == keyCode) {
				Button btn = null;
				if (v instanceof Button) {
					btn = (Button) v;
				}

				if (KeyEvent.ACTION_DOWN == event.getAction()) {
					v.setBackgroundResource(R.drawable.btn_pressed);
					if (null != btn) {
						btn.setTextColor(Color.WHITE);
					}
					return true;
				} else if (KeyEvent.ACTION_UP == event.getAction()) {
					v.setBackgroundResource(R.drawable.selector_btn);
					if (null != btn) {
						btn.setTextColor(getResources().getColor(R.color.selector_btn_text));
					}
					if (btn == btnTvSelect) {
						if (tvListDialog.isShowing()) {
							tvListDialog.dismiss();
						} else {
							tvListDialog.show();
						}
						return true;
					} else {
						Intent it = new Intent();
						it.setClass(TvInfoActivity.this, BVideoPlayerActivity.class);
						it.putExtra("curindex", index);
						it.putExtra("readme", rm);
						it.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) amList);
						TvInfoActivity.this.startActivity(it);
					}
				}
			}
			return false;
		}
	};

	public final static HashSet<String> VIDEO_EXTENSIONS;

	static {
		String[] video_extensions = { ".3g2", ".3gp", ".3gp2", ".3gpp", ".amv", ".asf", ".avi", ".dat", ".divx", ".drc",
				".dv", ".f4v", ".flv", ".gvi", ".gxf", ".iso", ".m1v", ".m2v", ".m2t", ".m2ts", ".m3u8", ".m4v", ".mkv",
				".mov", ".mp2v", ".mp4", ".mp4v", ".mpe", ".mpeg", ".mpeg1", ".mpeg2", ".mpeg4", ".mpg", ".mpv2",
				".mts", ".mtv", ".mxf", ".mxg", ".nsv", ".nuv", ".ogm", ".ogv", ".ogx", ".ps", ".rec", ".rm", ".rmvb",
				".tod", ".tp", ".trp", ".ts", ".tts", ".vob", ".vro", ".webm", ".wm", ".wmv", ".wtv", ".xesc" };
		VIDEO_EXTENSIONS = new HashSet<String>();
		for (String item : video_extensions) {
			VIDEO_EXTENSIONS.add(item);
		}
	}

	public static String getExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf(".");
		if (dotIndex != -1) {
			return fileName.substring(dotIndex).toLowerCase();
		}
		return " ";
	}

	public static String getMovieName(String fileName) {
		int index = fileName.lastIndexOf("/");
		if (index != -1) {
			String totalnm = fileName.substring(index + 1);
			int dotIndex = totalnm.lastIndexOf(".");
			String result = totalnm.substring(0, dotIndex);
			return result;
		}
		return " ";
	}

	public static String getTvName(String fileName) {
		if (!BenchUtils.isNull(tvName)) {
			return tvName;
		}
		try {
			String name = fileName.substring(0, fileName.length() - 1);
			int index = name.lastIndexOf("/");
			if (index != -1) {
				String result = name.substring(index + 1);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return " ";
	}

	public String getRealUrl(String p) {
		try {
			//			StringBuilder sb = new StringBuilder();
			//			sb.append(BaiduPCSUtils.PCS_DOWN_URL);
			//			sb.append("&access_token=");
			//			sb.append(BaiduPCSUtils.getInstance(getApplicationContext()).getAccessToken());
			//			sb.append("&path=");
			//			sb.append(path);
			//			return sb.toString();
			String result = BenchUtils.QINIU_URL + p;
			BenchUtils.doLog("result = " + result);
			return result;
		} catch (Exception e) {
			return "";
		}
	}

	// 显示加载对话
	public void showLoadingDialog() {
		mLoadingprogressBar.setVisibility(View.VISIBLE);
	}

	public void loadingDialogDismiss() {
		mLoadingprogressBar.setVisibility(View.GONE);
	}

	public boolean isShowloadingDialog() {
		return mLoadingprogressBar.getVisibility() == View.VISIBLE;
	}

}
