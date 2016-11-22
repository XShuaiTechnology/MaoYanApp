/** 
 * Date:2016年5月3日下午2:37:02 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */
package com.xshuai.tv;

import java.util.ArrayList;
import java.util.List;

import com.bench.utils.BenchUtils;
import com.bench.utils.DensityUtil;
import com.xshuai.netplayer.R;
import com.xshuai.netplayer.entities.AlbumMovie;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class TvEpisodeListDialog extends Dialog {

	private Context mContext;
	private Point windowPoint = new Point();
	private float scaleXSize;
	private float scaleYSize;
	private float scale;
	List<AlbumMovie> amList = new ArrayList<AlbumMovie>();
	private float VELOCITY_SCALE = 0.6f;

	/**
	 * @param context
	 * @param theme
	 */
	public TvEpisodeListDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	public TvEpisodeListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	ListView listView;

	public TvEpisodeListDialog(Context context, List<AlbumMovie> list, final Handler mhandler) {
		super(context, R.style.appdialog);
		mContext = context;
		amList = list;
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		display.getSize(windowPoint);
		scaleXSize = (float) (windowPoint.x / 1280.0);
		scaleYSize = (float) (windowPoint.y / 800.0);
		scale = DensityUtil.getInstance(mContext.getApplicationContext()).getScale();
		this.getWindow().setWindowAnimations(R.style.appRightdialogAnim);
		View mview = (View) View.inflate(context, R.layout.tv_episode_list, null);
		this.setContentView(mview,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		listView = (ListView) findViewById(R.id.lv_tv_list);
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		win.setAttributes(lp);
		listView.setVelocityScale(VELOCITY_SCALE);
		LayoutParams gv_lp = (LayoutParams) listView.getLayoutParams();
		gv_lp.setMargins(0, 0, 0, 0);
		listView.setFocusableInTouchMode(true);
		listView.setAdapter(new MoviesAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (null != mhandler) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = position;
					mhandler.sendMessage(msg);
					dismiss();
				}
			}
		});
	}

	public void show(int sel) {
		listView.setSelection(sel);
		BenchUtils.doLog("sel = " + sel);
		show();
	}

	public void refreshData() {
		MoviesAdapter ma = (MoviesAdapter) listView.getAdapter();
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.tv_episode_item, listView, false);
				movieView = new MovieViewHolder();
				movieView.textView = (TextView) convertView.findViewById(R.id.tv_name);
				movieView.textView.setTextSize((scaleXSize * 25.0f) / scale);
				LayoutParams btn_sel_lp = (LayoutParams) movieView.textView.getLayoutParams();
				btn_sel_lp.height = (int) (scaleYSize * 60.0f);
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
