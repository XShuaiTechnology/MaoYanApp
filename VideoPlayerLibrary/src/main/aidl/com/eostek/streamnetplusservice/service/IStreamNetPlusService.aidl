package com.eostek.streamnetplusservice.service;

import com.eostek.streamnetplusservice.service.MyMap;
import com.eostek.streamnetplusservice.service.IResultListener;
import com.eostek.streamnetplusservice.service.ITaskListener;
import com.eostek.streamnetplusservice.service.IEventListener;
import com.eostek.streamnetplusservice.service.TaskInfoInternal;

interface IStreamNetPlusService{


	void init(boolean memoryOnly, boolean isSDCARD, String cachePath,
			String downloadPath);
	/**
	 * 释放资源
	 */
	void destory() ;
	
	/**
	 * 获取播放地址
	 * 
	 * @param orgUrl
	 *            原网络播放地址
	 * @return 返回StreamNet能力平台能识别的播放地址
	 */
	String getPlayURL(String orgUrl, in MyMap extra);
	
	String getPlayURL2(String orgUrl);
	
	/**
	 * 停止播放
	 */
	void stopPlay();
	
	/**
	 * 获取全局下载速度 (单位：Bytes/s)
	 * 
	 * @return 全局下载速度
	 */
	int getDownloadSpeed();
	
	/**
	 * ֹͣ停止下载
	 * 
	 * @param url
	 */
	void stopDownload(String url);
	
	/**
	 * 开始下载
	 * 
	 * @param url
	 */
	void startDownload(String url);
	
	
	/**
	 * 创建任务
	 * 
	 * @param url
	 *            下载地址
	 * @param storagePath
	 *            存储地址
	 * @param urlType
	 *            url类型{@link TaskURLType}
	 * @param listener
	 *            异步回调监听对象
	 */
	void createDownloadTask(String url, String resumePath, String storagePath,
			int urlType, IResultListener listener, in MyMap extra);
			
	void push(String url, int urlType, in MyMap extra);
			
	/**
	 * 设置单个任务监听
	 * 
	 * @param url
	 * @param listener
	 * @param listen
	 */
	boolean setTaskListener(String url, ITaskListener listener, boolean listen);
	
	/**
	 * 设置全局监听，监听下载速度
	 * 
	 * @param listener
	 */
	void setEventListener(IEventListener listener, boolean listen);
	
	
	/**
	 * 设置tracker
	 * 
	 * e.g:http://ip:port/***
	 * 
	 * @param tracker
	 */
	void setTrackers(String tracker);
	
	void setUploadSpeedLimit(int speed);
	
	void setDownloadSpeedLimit(int speed);
	
	void setMaxConnectionCount(int count);
	
	/**
	 * 删除任务
	 * 
	 * @param url
	 */
	void removeTask(String url);
	
	void removeTaskAndFile(String url);
	
	/**
	 * 改变状态״̬
	 * @param url
	 * @param state
	 */
	void changeTaskState(String url, int state);
	
	void updatePlayingURL(String newUrl);
	
	void updateDownloadURL(String oldUrl, String newUrl);
	
	TaskInfoInternal getTaskInfo(String url);
	
	TaskInfoInternal getPlayingTaskInfo();
	
	boolean IsDiskReady(String path);
	
	boolean addDiskPath(String path);
	
	boolean removeDiskPath(String path);
	
	List<String> getDownloadTaskList();
	
	int getUploadSpeedLimit();
	
	int getDownloadSpeedLimit();
	
	int getMaxConnectionCount();
	
	void clearCache();
}

