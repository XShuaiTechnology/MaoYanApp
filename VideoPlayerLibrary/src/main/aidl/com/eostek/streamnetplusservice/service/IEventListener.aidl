package com.eostek.streamnetplusservice.service;


interface IEventListener {
	/**
	 * Set information listener
	 * @param what:		info type
	 * @param detail:	info detail
	 * @param extra:	info extra
	 */
	void OnInfo(int what, int detail, String extra);
	
	/**
	 * Set error listener
	 * @param code:		error code
	 * @param detail:	detail information of error
	 */
	void OnError(int code, String detail);
	
}