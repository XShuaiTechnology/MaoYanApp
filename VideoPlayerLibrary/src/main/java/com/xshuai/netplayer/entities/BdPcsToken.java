/** 
 * Date:2016�?3�?21日下�?4:20:44 
 * Copyright (c) 2016, benchone@163.com All Rights Reserved. 
 * 
 */
package com.xshuai.netplayer.entities;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "tbl_bdpcstoken")
public class BdPcsToken {
	@Id(column = "id")
	private String id;
	@Column(column = "apinm")
	private String apinm;
	@Column(column = "apival")
	private String apival;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApinm() {
		return apinm;
	}

	public void setApinm(String apinm) {
		this.apinm = apinm;
	}

	public String getApival() {
		return apival;
	}

	public void setApival(String apival) {
		this.apival = apival;
	}

}
