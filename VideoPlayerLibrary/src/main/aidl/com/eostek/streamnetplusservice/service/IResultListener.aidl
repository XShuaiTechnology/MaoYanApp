package com.eostek.streamnetplusservice.service;

import com.eostek.streamnetplusservice.service.TaskInfoInternal;
interface IResultListener {
	/**
	 * Set result listener
	 * @param taskList:	task list
	 */
	void OnCreated(in List<TaskInfoInternal> taskList);
	
	/**
	 * Set error listener
	 * @param code:		error code
	 * @param taskID:	detail information of error
	 */
	void OnError(int code, String detail);
}
