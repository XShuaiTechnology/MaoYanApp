/** 
 * Date:2016年5月3日下午2:37:02 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */
package com.xshuai.tv;

import java.util.ArrayList;
import java.util.List;

import com.bench.entities.ReadMe;
import com.bench.utils.BenchUtils;
import com.bench.utils.DensityUtil;
import com.xshuai.bvideoplayer.BVideoPlayerActivity;
import com.xshuai.netplayer.R;
import com.xshuai.netplayer.entities.AlbumMovie;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Parcelable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class TvListDialog extends Dialog {

	private static final int GRIDVIEW_GNUM_COLUMNS = 5;
	private Context mContext;
	private Point windowPoint = new Point();
	private float scaleXSize;
	private float scaleYSize;
	private float scale;
	List<AlbumMovie> amList = new ArrayList<AlbumMovie>();

	/**
	 * @param context
	 * @param theme
	 */
	public TvListDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	public TvListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	ReadMe rm;
	GridView gv_tv_list;
	MovieViewHolder lastView = null;

	public TvListDialog(Context context, List<AlbumMovie> list, ReadMe rme) {
		super(context, R.style.appdialog);
		mContext = context;
		amList = list;
		this.rm = rme;
		Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
		display.getSize(windowPoint);
		scaleXSize = (float) (windowPoint.x / 1280.0);
		scaleYSize = (float) (windowPoint.y / 800.0);
		scale = DensityUtil.getInstance(mContext.getApplicationContext()).getScale();
		this.getWindow().setWindowAnimations(R.style.appdialogAnim);
		View mview = (View) View.inflate(context, R.layout.tv_list, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(mview,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.getWindow().setLayout(windowPoint.x * 2 / 3, windowPoint.y * 2 / 3);
		gv_tv_list = (GridView) findViewById(R.id.gv_tv_list);
		LayoutParams gv_lp = (LayoutParams) gv_tv_list.getLayoutParams();
		int marginx = (int) (scaleXSize * 2.0f / 3.0f * 80.0f);
		int marginy = (int) (scaleYSize * 2.0f / 3.0f * 60.0f);
		gv_lp.setMargins(marginx, marginy, marginx, marginy);
		gv_tv_list.setFocusableInTouchMode(true);
		gv_tv_list.setNumColumns(GRIDVIEW_GNUM_COLUMNS);
		gv_tv_list.setVerticalSpacing((int) (scaleYSize * 20f));
		gv_tv_list.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (gv_tv_list.getSelectedItemPosition() == 0) {
						MovieViewHolder movieView = (MovieViewHolder) gv_tv_list.getSelectedView().getTag();
						movieView.textView.setActivated(true);
						lastView = movieView;
					}
				}
			}
		});
		gv_tv_list.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				BenchUtils.doLog("onItemSelected position = " + position);
				if (null != lastView) {
					lastView.textView.setActivated(false);
				}
				if (null != view && null != view.getTag()) {
					BenchUtils.doLog("null != view && null != view.getTag()   true");
					MovieViewHolder movieView = (MovieViewHolder) view.getTag();
					movieView.textView.setActivated(true);
					lastView = movieView;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		gv_tv_list.setAdapter(new MoviesAdapter());
		gv_tv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent it = new Intent();
				it.setClass(mContext, BVideoPlayerActivity.class);
				it.putExtra("curindex", position);
				it.putExtra("readme", rm);
				it.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) amList);
				mContext.startActivity(it);
				dismiss();
			}
		});
	}

	@Override
	public void show() {
		super.show();
		try {
			if (gv_tv_list.getSelectedItemPosition() == 0) {
				gv_tv_list.clearFocus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshData() {
		MoviesAdapter ma = (MoviesAdapter) gv_tv_list.getAdapter();
		ma.notifyDataSetChanged();
	}

	/**
	 * GridView的适配器
	 */
	public class MoviesAdapter extends BaseAdapter {
		public int getCount() {
			return amList.size();
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

		public Object getItem(int position) {
			return amList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			AlbumMovie am = amList.get(position);
			MovieViewHolder movieView;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.tv_item, gv_tv_list, false);
				movieView = new MovieViewHolder();
				movieView.textView = (TextView) convertView.findViewById(R.id.tv_name);
				movieView.textView.setTextSize((scaleXSize * 18.0f) / scale);
				LayoutParams btn_sel_lp = (LayoutParams) movieView.textView.getLayoutParams();
				btn_sel_lp.height = (int) (scaleYSize * 55.0f);
				btn_sel_lp.width = (int) (scaleXSize * 130.0f);
				convertView.setTag(movieView);
			} else {
				movieView = (MovieViewHolder) convertView.getTag();
			}
			movieView.textView.setText(am.getName());
			return convertView;
		}
	}

	/**
	 * ViewHolder
	 */
	private class MovieViewHolder {
		TextView textView;
	}

}
