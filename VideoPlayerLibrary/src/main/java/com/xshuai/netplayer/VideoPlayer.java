package com.xshuai.netplayer;

import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/** 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */

import com.bench.utils.BenchUtils;
import com.bench.utils.DBUtils;
import com.bench.utils.VideoAnalysis;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.xshuai.AppApplication;
import com.xshuai.bvideoplayer.BVideoPlayerActivity;
import com.xshuai.netplayer.entities.AlbumMovie;
import com.xshuai.netplayer.entities.BdPcsToken;
import com.xshuai.netplayer.service.MasterService;
import com.xshuai.netvideoplayer.NVideoPlayerActivity;
import com.xshuai.tv.TvInfoActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class VideoPlayer extends Activity {

	AlbumMovie movie = new AlbumMovie();
	Gson gson = new Gson();
	DbUtils db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = DBUtils.getDbUtils(getApplicationContext());
		BenchUtils.doLog("VideoPlayer onCreate ");
		Intent intent = getIntent();
		if (null != intent) {
			String name = intent.getStringExtra("name");
			String url = intent.getStringExtra("movieurl");
			if (BenchUtils.isNull(url)) {
				url = intent.getData().toString();
			}
			final String movieurl = url;
			if (null != movieurl && !"".equals(movieurl)) {
				if (null == name) {
					try {
						name = getMovieName(movieurl);
					} catch (Exception e) {
					}
				}
				movie.setName((null == name) ? "" : name);
				if (movieurl.startsWith("/apps/")) {
					Intent it = new Intent();
					if (movieurl.endsWith("/")) {
						BenchUtils.doLog("name = " + name);
						it.setClass(this, TvInfoActivity.class);
						it.putExtra("tv_url", movieurl);
						it.putExtra("name", name);
						startActivity(it);
						this.finish();
						return;
					} else {
						boolean islive = isServiceRunning(this, "com.xshuai.netplayer.service.MasterService");
						if (!islive) {
							Intent i = new Intent(this, MasterService.class);
							BenchUtils.doLog("start com.xshuai.netplayer.service.MasterService####");
							this.startService(i);
						} else {
							BenchUtils.doLog("com.xshuai.netplayer.service.MasterService is living####");
						}
						it.setClass(this, BVideoPlayerActivity.class);
					}
					movie.setMovieUrl(movieurl);
					it.putExtra("curmovie", movie);
					startActivity(it);
					this.finish();
				} else {
					new Thread(new Runnable() {

						@Override
						public void run() {
							if (BenchUtils.isNull(AppApplication.getInstance().analysisUrl)) {
								Connection connect = Jsoup.connect(VideoAnalysis.ANALYSIS_URL).ignoreContentType(true);
								try {
									Document d = connect.timeout(10000).validateTLSCertificates(false).get();
									if (null != d && null != d.text()) {
										BdPcsToken bt = gson.fromJson(d.text(), BdPcsToken.class);
										if (null != bt) {
											AppApplication.getInstance().analysisUrl = bt.getApival();
											try {
												db.saveOrUpdate(bt);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (BenchUtils.isNull(AppApplication.getInstance().analysisUrl)) {
								try {
									BdPcsToken bt = db.findFirst(Selector.from(BdPcsToken.class).where("apinm", "=",
											VideoAnalysis.ANALYSIS_KEY));
									if (null != bt) {
										AppApplication.getInstance().analysisUrl = bt.getApival();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							Map<String, Object> result = VideoAnalysis.getVideoPath(movieurl, VideoPlayer.this);
							Message msg = new Message();
							msg.what = 0;
							msg.obj = result;
							turnPageHandler.sendMessage(msg);
						}
					}).start();
				}
			}
		}
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

	Handler turnPageHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			Map<String, Object> result = (Map<String, Object>) msg.obj;
			String finalpath = (String) result.get("path");
			boolean m3u8 = false;
			try {
				m3u8 = (Boolean) result.get("m3u8");
			} catch (Exception e) {
			}
			if (BenchUtils.isNull(finalpath)) {
				Toast.makeText(VideoPlayer.this, getResources().getString(R.string.MSG_VIDEO_PATH_ERROR),
						Toast.LENGTH_SHORT).show();
				VideoPlayer.this.finish();
				return;
			}
			BenchUtils.doLog("finalpath = " + finalpath);
			Intent it = new Intent();
			m3u8 = true;
			if (m3u8) {
				it.setClass(VideoPlayer.this, NVideoPlayerActivity.class);
			} else {
				boolean islive = isServiceRunning(VideoPlayer.this, "com.xshuai.netplayer.service.MasterService");
				if (!islive) {
					Intent i = new Intent(VideoPlayer.this, MasterService.class);
					BenchUtils.doLog("start com.xshuai.netplayer.service.MasterService####");
					VideoPlayer.this.startService(i);
				} else {
					BenchUtils.doLog("com.xshuai.netplayer.service.MasterService is living####");
				}
				it.setClass(VideoPlayer.this, BVideoPlayerActivity.class);
			}
			movie.setMovieUrl(finalpath);
			it.putExtra("curmovie", movie);
			startActivity(it);
			VideoPlayer.this.finish();
		};
	};

	/**
	 * 用来判断服务是否运行.
	 * @param context
	 * @param className 判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

}
