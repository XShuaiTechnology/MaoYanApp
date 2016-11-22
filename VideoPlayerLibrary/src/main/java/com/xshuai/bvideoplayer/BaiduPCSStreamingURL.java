/** 
 * Date:2016年3月30日上午11:16:04 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */
package com.xshuai.bvideoplayer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BaiduPCSStreamingURL {
	public static String buildStreamUrl(String token, String path) {
		StringBuilder sb = new StringBuilder();
		sb.append("https://pcs.baidu.com/rest/2.0/pcs/file?method=streaming&access_token=");
		sb.append(token);
		sb.append("&path=");
		try {
			sb.append(URLEncoder.encode(path, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			sb.append(path);
		}
		sb.append("&type=M3U8_AUTO_720&display=1");
		return sb.toString();
	}
	
	public static String buildDownUrl(String token, String path) {
		StringBuilder sb = new StringBuilder();
		sb.append("https://pcs.baidu.com/rest/2.0/pcs/file?method=download&access_token=");
		sb.append(token);
		sb.append("&path=");
		try {
			sb.append(URLEncoder.encode(path, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			sb.append(path);
		}
		return sb.toString();
	}
}
