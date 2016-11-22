/** 
 * Date:2016年2月25日下午3:12:08 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */
package com.bench.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.xshuai.AppApplication;
import com.xshuai.netplayer.entities.XDYY;
import com.xshuai.netplayer.entities.XDYY.filelist;

import android.content.Context;

public class VideoAnalysis {

	static Gson gson = new Gson();

	public static String ANALYSIS_KEY = "analysisurl";
	public static String ANALYSIS_HEAD = "?apikey=36f7d4c5bb4bc835&mode=iphone";
	public static String ANALYSIS_URL = "http://api.xshuai.com/xs_movies/getThirdAPIVal?apinm=" + ANALYSIS_KEY;

	public static Map<String, Object> getVideoPath(String url, Context context) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (BenchUtils.isNull(AppApplication.getInstance().analysisUrl)) {
			result.put("path", "");
			result.put("m3u8", false);
		} else {

			String path = "";
			boolean ism3u8 = false;
			if (!BenchUtils.isNull(url)) {
				BenchUtils.doLog("url = " + url);
				String[] urls = url.split("\\$");
				String finalUrl = "";
				if (urls.length == 1) {
					finalUrl = AppApplication.getInstance().analysisUrl + ANALYSIS_HEAD + "&url=" + url;
				} else if (urls.length == 2) {
					String str = urls[1];
					finalUrl = AppApplication.getInstance().analysisUrl + ANALYSIS_HEAD + "&type=" + urls[0];
					if (str.startsWith("http") || str.startsWith("magnet")) {
						finalUrl += "&url=" + urls[1];
					} else {
						finalUrl += "&vid=" + urls[1];
					}
				}
				Connection connect = Jsoup.connect(finalUrl).ignoreContentType(true);
				try {
					Document d = connect.timeout(10000).validateTLSCertificates(false).get();
					BenchUtils.doLog("d.text() = " + d.text());
					XDYY xdyy = gson.fromJson(d.text(), XDYY.class);
					if (null != xdyy) {
						if ("200".equals(xdyy.getCode())) {
							if (null != xdyy.getResult()) {
								if ("m3u8".equals(xdyy.getResult().getPlay_type())
										|| "mp4".equals(xdyy.getResult().getPlay_type())) {
									ism3u8 = true;
								}
								if (!BenchUtils.isNull(xdyy.getResult().getFiles())) {
									path = xdyy.getResult().getFiles();
								} else if (null != xdyy.getResult().getFilelist()
										&& xdyy.getResult().getFilelist().length > 0) {
									filelist fl = xdyy.getResult().getFilelist()[0];
									path = fl.getUrl();
								}
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			result.put("path", path);
			result.put("m3u8", ism3u8);
		}
		return result;
	}
}
