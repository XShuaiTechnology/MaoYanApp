package com.mao.movie.util;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.mao.movie.model.BaseSettings;
import com.mao.movie.model.BdPcsToken;
import com.mao.movie.model.MovieHistory;

public class DBUtils {

	private static final String DB_NAME = "other_settings.db";
	public static final String VIDEO_DEFINITION_SD = "M3U8_AUTO_480";
	public static final String VIDEO_DEFINITION_HD = "M3U8_AUTO_720";
	private static final int DB_VERSION = 1;
	public static final String TAG = "bench";
	public static void initDB(Context context) {
		DbUtils db = getDbUtils(context);
		try {
			db.createTableIfNotExist(BdPcsToken.class);
			db.createTableIfNotExist(MovieHistory.class);
			db.createTableIfNotExist(BaseSettings.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public static DbUtils getDbUtils(Context context) {
		return DbUtils.create(context, DB_NAME, DB_VERSION, null);
	}

}
