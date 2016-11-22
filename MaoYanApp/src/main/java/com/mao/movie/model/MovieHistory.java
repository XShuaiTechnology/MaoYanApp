/** 
 * Date:2016�?3�?24日下�?3:45:17 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */
package com.mao.movie.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "tbl_movie_history")
public class MovieHistory {

	@Id(column = "id")
	private int id;
	@Column(column = "movieid")
	private String movieid;
	@Column(column = "lastTime")
	private int lastTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMovieid() {
		return movieid;
	}

	public void setMovieid(String movieid) {
		this.movieid = movieid;
	}

	public int getLastTime() {
		return lastTime;
	}

	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}

}
