/** 
 * Date:2016�?3�?26日下�?3:12:11 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */
package com.mao.movie.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "tbl_base_settings")
public class BaseSettings {

	@Id(column = "id")
	private int id;

	@Column(column = "definition")
	private String definition;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

}
