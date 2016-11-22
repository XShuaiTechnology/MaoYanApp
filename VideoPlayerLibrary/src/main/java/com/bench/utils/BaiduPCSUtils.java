/** 
 * Date:2016年2月27日上午10:36:44 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */
package com.bench.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
import com.xshuai.netplayer.entities.BdPcsToken;

import android.content.Context;
import android.os.Handler;

public class BaiduPCSUtils {
	public static String ACCESS_TOKEN = ""
			+ "23.054b12134056106fa6c8378502ae2b4c.2592000.1467187232.3627134014-1698855";
	public static final String XSHUAI_ACCESS_TOKEN_URL = "http://api.xshuai.com/xs_movies/getThirdAPIVal?apinm=bd_pcs_token";
	public static final String PCS_DOWN_URL = "https://pcs.baidu.com/rest/2.0/pcs/file?method=download";
	public final static long FRESH_TIMEOUT = 1 * 60 * 1000;
	private final String BAIDU_TOKEN_STRING = "bd_pcs_token";
	static BaiduPCSUtils bps;
	HttpHandler<String> httpHandler = null;
	private HttpUtils hUtils;

	Gson gson = new Gson();
	DbUtils db;

	public BaiduPCSUtils(Context context) {
		super();
		db = DBUtils.getDbUtils(context.getApplicationContext());
		hUtils = new HttpUtils();
		hUtils.configDefaultHttpCacheExpiry(FRESH_TIMEOUT);
		BdPcsToken bt = null;
		try {
			bt = db.findFirst(Selector.from(BdPcsToken.class).where("apinm", "=", BAIDU_TOKEN_STRING));
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (null == bt) {
			initToken();
		}else{
			ACCESS_TOKEN = bt.getApival();
		}
	}
	public void initToken(final Handler handler, final String url) {
		if (httpHandler != null && httpHandler.getState() != State.FAILURE && httpHandler.getState() != State.SUCCESS
				&& httpHandler.getState() != State.CANCELLED) {
			return;
		}
		httpHandler = hUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				arg0.printStackTrace();
				BenchUtils.doLog("url " + url + " failed");
			}

			@Override
			public void onCancelled() {
				super.onCancelled();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String res = arg0.result;
				if (!BenchUtils.isNull(res)) {
					try {
						BdPcsToken bt = gson.fromJson(res, BdPcsToken.class);
						if (null != bt) {
							BenchUtils.doLog("bt.getApival() = " + bt.getApival());
							ACCESS_TOKEN = bt.getApival();
							try {
								db.saveOrUpdate(bt);
							} catch (DbException e) {
								e.printStackTrace();
							}
							if(null != handler){
								handler.sendEmptyMessage(0);
							}
						}
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void initToken() {
		initToken(null, XSHUAI_ACCESS_TOKEN_URL);
	}

	public static BaiduPCSUtils getInstance(Context context) {
		if (null == bps) {
			bps = new BaiduPCSUtils(context);
		}
		return bps;
	}
	
	
	public String getAccessToken() {
		return ACCESS_TOKEN;
	}

}