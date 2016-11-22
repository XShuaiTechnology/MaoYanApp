package com.bench.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {

	// 当前屏幕的densityDpi  
	private float dmDensityDpi = 0.0f;
	private static DisplayMetrics dm;
	private static DensityUtil du;
	private static float scale = 0.0f;

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		DensityUtil.scale = scale;
	}

	public static DensityUtil getInstance(Context context) {
		if (null == du) {
			du = new DensityUtil(context);
		}
		return du;
	}

	/** 
	 *  
	 * 根据构函数获得当前手机的屏幕系数 
	 *  
	 * */
	public DensityUtil(Context context) {
		// 获取当前屏幕  
		dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		// 设置DensityDpi  
		setDmDensityDpi(dm.densityDpi);
		// 密度因子  
		scale = getDmDensityDpi() / 160;
	}

	/** 
	 * 当前屏幕的density因子 
	 *  
	 * @param DmDensity 
	 * @retrun DmDensity Getter 
	 * */
	public float getDmDensityDpi() {
		return dmDensityDpi;
	}

	/** 
	 * 当前屏幕的density因子 
	 *  
	 * @param DmDensity 
	 * @retrun DmDensity Setter 
	 * */
	public void setDmDensityDpi(float dmDensityDpi) {
		this.dmDensityDpi = dmDensityDpi;
	}

	/** 
	 * 密度转换像素 
	 * */
	public static int dip2px(float dipValue) {

		return (int) (dipValue * scale + 0.5f);

	}

	/** 
	 * 像素转换密度 
	 * */
	public int px2dip(float pxValue) {
		return (int) (pxValue / scale + 0.5f);
	}

	@Override
	public String toString() {
		return " dmDensityDpi:" + dmDensityDpi;
	}

}
