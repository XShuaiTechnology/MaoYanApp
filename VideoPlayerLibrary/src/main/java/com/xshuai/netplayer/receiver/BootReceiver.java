package com.xshuai.netplayer.receiver;

import com.xshuai.netplayer.service.MasterService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (intent.getAction().equals("com.xshuai.netplayer.init")) {
			// 启动消息服务
			Thread serviceThread = new Thread(new Runnable() {
				@Override
				public void run() {
					intent.setClass(context, MasterService.class);
					context.startService(intent);
				}
			});
			serviceThread.start();
		}
	}

}
