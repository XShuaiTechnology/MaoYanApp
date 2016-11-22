package com.xshuai.netplayer.service;

import com.bench.utils.BaiduPCSUtils;
import com.bench.utils.BenchUtils;
import com.eostek.streamnetplusservice.service.IStreamNetPlusService;
import com.eostek.streamnetplusservice.service.StreamNetManage;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

public class MasterService extends Service {
	private static boolean isBindStreamnetService = false;
	public static StreamNetManage streamnet;
	private static MasterService myself = null;
	/**
	 * 网络注册器
	 */
	private BroadcastReceiver netChangeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				BenchUtils.doLog("connectivityReceiver.onReceive()...");
				String action = intent.getAction();
				BenchUtils.doLog("action=" + action);
				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
				if (networkInfo != null) {
					if (networkInfo.isConnected()) {
						BaiduPCSUtils.getInstance(getApplicationContext()).initToken();
					}
				}
			}
		}
	};

	@Override
	public void onCreate() {
		// 注册网络接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(netChangeReceiver, filter);
		BenchUtils.doLog("MasterService onCreate");
		myself = this;
		bindStreamnetService();
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// 销毁
		unregisterReceiver(netChangeReceiver);
		Intent localIntent = new Intent();
		localIntent.setClass(this, MasterService.class); //销毁时重新启动Service
		this.startService(localIntent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	* 定义用来连接到服务后的处理函数
	*/
	private ServiceConnection mConn = new ServiceConnection() {
		// 绑定成功,SN返回binder对象转换成IStreamNetPlusService,即可调用SN中的下载的接口
		public void onServiceConnected(ComponentName className, IBinder service) {
			streamnet = new StreamNetManage(IStreamNetPlusService.Stub.asInterface(service));

			streamnet.setStreamNetManageListener(new StreamNetManage.StreamNetManageListener() {
				@Override
				public void reInit() {
					if (!isBindStreamnetService) {
						try {
							myself.getApplicationContext().unbindService(mConn);
						} catch (Exception e) {
						}
						isBindStreamnetService = false;
						bindStreamnetService();
					}
				}
			});
		}

		public void onServiceDisconnected(ComponentName className) {
			// sd卡拔掉后不一定会走这里,基本上是意外挂掉比如正在下载并拔掉sd卡,会重新连
			streamnet = null;
			isBindStreamnetService = false;
			bindStreamnetService();
		}
	};

	/**
	 * 绑定SN服务
	 */
	private synchronized void bindStreamnetService() {
		if (isBindStreamnetService) {
			return;
		}
		isBindStreamnetService = true;
		try {
			myself.getApplicationContext().unbindService(mConn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent intent = new Intent("com.eostek.streamnetplusservice.service.StreamNetService");
		myself.getApplicationContext().bindService(intent, mConn, Context.BIND_AUTO_CREATE);

	}
}
