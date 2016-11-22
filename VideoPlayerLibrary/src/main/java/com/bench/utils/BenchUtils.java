/** 
 * Date:2016年2月24日下午4:46:18 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */
package com.bench.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class BenchUtils {

	public static final String TAG = "bench";
	public static boolean isLog = true;
	private static Toast mToast = null;
	public static final String QINIU_URL = "http://o97fphxi3.bkt.clouddn.com/";

	/**
	 * Format milliseconds to hh:mm:ss
	 */
	public static String formatsecond(int second) {
		int hh = second / 3600;
		int mm = second % 3600 / 60;
		int ss = second % 60;
		return String.format("%02d:%02d:%02d", hh, mm, ss);
	}

	/**
	 * Format milliseconds to hh:mm:ss
	 */
	public static String formatMillisecond(long millisecond) {
		long time = millisecond / 1000;
		if (time <= 0) {
			return "00:00:00";
		}

		return String.format("%02d:%02d:%02d", time / 60 / 60, time / 60 % 60, time % 60);
	}

	public static void doLog(String log) {
		if (isLog) {
			Log.i(TAG, log);
		}
	}

	public static void showToast(Context context, int resId, int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(context, resId, duration);
		} else {
			mToast.cancel();
			mToast = Toast.makeText(context, resId, duration);
		}

		mToast.show();
	}

	public static void showToast(Context context, String text, int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, duration);
		} else {
			mToast.cancel();
			mToast = Toast.makeText(context, text, duration);
		}

		mToast.show();
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		return str == null || "".equals(str);
	}

}
