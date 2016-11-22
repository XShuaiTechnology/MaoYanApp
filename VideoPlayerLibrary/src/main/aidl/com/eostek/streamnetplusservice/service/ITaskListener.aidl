package com.eostek.streamnetplusservice.service;

interface ITaskListener {
	/**
	 * Set information listener
	 * @param progress:	current download progress in percent
	 * @param speed:	current download speed(Unit:Bytes/s)
	 */
	void OnInfo(int progress, int speed);
	
	/**
	 * Set complete listener
	 */
	void OnComplete();
	
	/**
	 * Set task change listener
	 * @param state:	current state of task
	 */
	void OnTaskChanged(int state);
	
	/**
	 * Set error listener
	 * @param code:		error code
	 * @param detail:	detail information of error
	 */
	void OnError(int code, String detail);
}