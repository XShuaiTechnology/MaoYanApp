/** 
 * Date:2016年5月30日下午3:32:14 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */
package com.bench.entities;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ReadMe implements Serializable {

	public ReadMe() {
		super();
		setOrderBy(0);
		setVideoScalingMode(0);
		setCache(0);
	}

	@SerializedName("order")
	private int orderBy;

	@SerializedName("videoscalingmode")
	private int videoScalingMode;

	@SerializedName("cache")
	private int cache;

	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	public int getVideoScalingMode() {
		return videoScalingMode;
	}

	public void setVideoScalingMode(int videoScalingMode) {
		this.videoScalingMode = videoScalingMode;
	}

	public int getCache() {
		return cache;
	}

	public void setCache(int cache) {
		this.cache = cache;
	}

}
