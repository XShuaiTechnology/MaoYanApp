package com.xshuai;

import java.util.concurrent.atomic.AtomicInteger;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Application;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

/**
 * Application
 */
public class AppApplication extends Application {
	
	/**
	 * 动态创建View时生成id用
	 */
	private final AtomicInteger idGenerator = new AtomicInteger(Integer.MAX_VALUE);

	static AppApplication app;

	public static AppApplication getInstance(){
		return app;
	}
	public String analysisUrl = "";
	
	/**
	 * ImageLoader的DisplayImageOptions
	 */
	private static DisplayImageOptions DISPLAY_IMAGE_OPTIONS //
	= new DisplayImageOptions.Builder() //
			.cacheInMemory(false) //
			.cacheOnDisk(true) //
			.bitmapConfig(Bitmap.Config.RGB_565) //
			.build();

	/**
	 * Universal ImageLoader
	 */
	private ImageLoader imageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
	}

	@Override
	public void onTerminate() {
		imageLoader.destroy();
		super.onTerminate();
	}

	/**
	 * 为View设置唯一ID
	 * 
	 * @param view
	 *            View
	 */
	public void setViewId(View view) {
		view.setId(idGenerator.getAndDecrement());
	}

	/**
	 * 显示图片
	 * 
	 * @param uri
	 *            图片URI
	 * @param imageView
	 *            ImageView
	 */
	public void displayImage(String uri, ImageView imageView) {
		imageLoader.displayImage(uri, imageView, DISPLAY_IMAGE_OPTIONS);
	}

	/**
	 * 清除UniversalImageLoader的缓存
	 */
	@SuppressWarnings("deprecation")
	public void clearUniversalImageLoaderCache() {
		try {
			imageLoader.clearMemoryCache();
			imageLoader.clearDiscCache();
			imageLoader.clearDiskCache();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 显示图片 带回调接口的
	 *
	 * @param uri
	 *            图片URI
	 * @param imageView
	 *            ImageView
	 * @listener listener
	 */
	public void displayImage(String uri, ImageView imageView, ImageLoadingListener listener) {
		imageLoader.displayImage(uri, imageView, DISPLAY_IMAGE_OPTIONS, listener);
	}

}
