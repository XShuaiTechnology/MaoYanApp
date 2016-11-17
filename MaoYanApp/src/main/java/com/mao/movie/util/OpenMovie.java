/**
 * Date:2016年3月3日下午5:45:07
 */
package com.mao.movie.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mao.movie.model.RecommendMovie;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OpenMovie {
    public static final String MOVIE_INFO_SEPARATOR = "@_@";
    /**
     * 电视家的包名
     */
    private static final String DIAN_SHI_JIA_PACKAGE_NAME = "com.fanshi.tvbrowser";

    static OpenMovie openMovie;
    RecommendMovie.RowsBean mMovie;
    Context mContext;
    Gson gson = new Gson();

    public static OpenMovie getInstance() {
        if (null == openMovie) {
            openMovie = new OpenMovie();
        }
        return openMovie;
    }


    /**
     * 点击图标时执行的Intent
     *
     * @param context Context
     * @return Intent
     */
    public Intent getIntent(Context context, RecommendMovie.RowsBean movie) {
        String intentComponentPackage = movie.getIntentComponentPackage();
        String intentComponentClass = movie.getIntentComponentClass();
        String intentExtrasStr = movie.getIntentExtrasStr();
        List<String> intentExtras = new ArrayList<String>();
        if (null != intentExtrasStr && intentExtrasStr.length() > 0) {
            String[] strs = intentExtrasStr.split(MOVIE_INFO_SEPARATOR);
            for (int i = 0; i < strs.length; i++) {
                intentExtras.add(strs[i]);
            }
        }
       // String intentAction = movie.getIntentAction();
        String intentData = movie.getIntentData();
        if (/*TextUtils.isEmpty(intentAction) &&*/ TextUtils.isEmpty(intentComponentPackage)
                && TextUtils.isEmpty(intentComponentClass) && (intentExtras == null || intentExtras.size() < 3)) {
            return null;
        }
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(intentComponentPackage)) {
            if (TextUtils.isEmpty(intentComponentClass)) {
                intent.setPackage(intentComponentPackage);
            } else {
                intent.setClassName(intentComponentPackage, intentComponentClass);
            }
        } else {
            if (!TextUtils.isEmpty(intentComponentClass)) {
                intent.setClassName(context, intentComponentClass);
            }
        }

        // action
        /*if (!TextUtils.isEmpty(intentAction)) {
            intent.setAction(intentAction);
        }*/
        if (!TextUtils.isEmpty(intentData)) {
            Uri dataUri = Uri.parse(intentData);
            intent.setDataAndNormalize(dataUri);
        }
        if (intentExtras.size() > 0) {
            int i = 0;
            while (i + 2 < intentExtras.size()) {
                String extraType = intentExtras.get(i++).trim();
                String extraName = intentExtras.get(i++).trim();
                String extraValue = intentExtras.get(i++).trim();
                if (TextUtils.isEmpty(extraName))
                    continue;

                if (TextUtils.isEmpty(extraType))
                    extraType = "string"; // 默认string
                extraType = extraType.toLowerCase(Locale.US);
                // 8个基本类型和string及对应数组
                if (extraType.equals("byte")) {
                    intent.putExtra(extraName, Byte.parseByte(extraValue));
                } else if (extraType.equals("short")) {
                    intent.putExtra(extraName, Short.parseShort(extraValue));
                } else if (extraType.equals("int") || extraType.equals("integer")) {
                    intent.putExtra(extraName, Integer.parseInt(extraValue));
                } else if (extraType.equals("long")) {
                    intent.putExtra(extraName, Long.parseLong(extraValue));
                } else if (extraType.equals("float")) {
                    intent.putExtra(extraName, Float.parseFloat(extraValue));
                } else if (extraType.equals("double")) {
                    intent.putExtra(extraName, Double.parseDouble(extraValue));
                } else if (extraType.equals("boolean")) {
                    intent.putExtra(extraName, Boolean.parseBoolean(extraValue));
                } else if (extraType.equals("char") || extraType.equals("character")) {
                    intent.putExtra(extraName, extraValue.charAt(0));
                } else if (extraType.equals("string")) {
                    intent.putExtra(extraName, extraValue);
                } else if (extraType.equals("byte[]")) {
                    intent.putExtra(extraName, gson.fromJson(extraValue, byte[].class));
                } else if (extraType.equals("short[]")) {
                    intent.putExtra(extraName, gson.fromJson(extraValue, short[].class));
                } else if (extraType.equals("int[]") || extraType.equals("integer[]")) {
                    intent.putExtra(extraName, gson.fromJson(extraValue, int[].class));
                } else if (extraType.equals("long[]")) {
                    intent.putExtra(extraName, gson.fromJson(extraValue, long[].class));
                } else if (extraType.equals("float[]")) {
                    intent.putExtra(extraName, gson.fromJson(extraValue, float[].class));
                } else if (extraType.equals("double[]")) {
                    intent.putExtra(extraName, gson.fromJson(extraValue, double[].class));
                } else if (extraType.equals("boolean[]")) {
                    intent.putExtra(extraName, gson.fromJson(extraValue, boolean[].class));
                } else if (extraType.equals("char[]") || extraType.equals("character[]")) {
                    intent.putExtra(extraName, gson.fromJson(extraValue, char[].class));
                } else if (extraType.equals("string[]")) {
                    intent.putExtra(extraName, gson.fromJson(extraValue, String[].class));
                }
            }
        }
        // 电视家为猫眼单独做的处理，点击播放进入电视家的界面，按返回返回到猫眼而不是留在电视家
        if (null != movie.getIntentComponentPackage()
                && DIAN_SHI_JIA_PACKAGE_NAME.equals(movie.getIntentComponentPackage())) {
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.putExtra("extra_partner", "xiaoshuai");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Logger.d(intent);
        Logger.d(intent.toUri(0));
        return intent;
    }


}
