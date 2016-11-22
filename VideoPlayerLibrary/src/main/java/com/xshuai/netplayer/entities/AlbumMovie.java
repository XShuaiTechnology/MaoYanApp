package com.xshuai.netplayer.entities;

import com.google.gson.annotations.SerializedName;
import android.os.Parcel;
import android.os.Parcelable;

public class AlbumMovie implements Parcelable {

	/**
	 * 
	 */
	@SerializedName("id")
	private String id;
	@SerializedName("name")
	private String name;// 影片名称
	@SerializedName("label")
	private String type;
	@SerializedName("rating")
	private String score;// 评分
	@SerializedName("description")
	private String description;// 影片描述
	@SerializedName("url")
	private String poster;// 海报(竖版)
	@SerializedName("intentData")
	private String movieUrl;// 电影播放地址
	@SerializedName("tuiJianType")
	private String source; //枚举: 1/华数 ;2/电视�?;3/BAIDU;4/网页解析
	@SerializedName("intentComponentPackage")
	private String intentComponentPackage;
	@SerializedName("intentComponentClass")
	private String intentComponentClass;
	@SerializedName("intentData")
	private String intentData;
	@SerializedName("apkUrl")
	private String apkUrl;

	@SerializedName("subtitleUrl")
	private String subtitleUrl;// 外挂字幕

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getMovieUrl() {
		return movieUrl;
	}

	public void setMovieUrl(String movieUrl) {
		this.movieUrl = movieUrl;
	}

	public String getIntentComponentPackage() {
		return intentComponentPackage;
	}

	public void setIntentComponentPackage(String intentComponentPackage) {
		this.intentComponentPackage = intentComponentPackage;
	}

	public String getIntentComponentClass() {
		return intentComponentClass;
	}

	public void setIntentComponentClass(String intentComponentClass) {
		this.intentComponentClass = intentComponentClass;
	}

	public String getIntentData() {
		return intentData;
	}

	public void setIntentData(String intentData) {
		this.intentData = intentData;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	public String getSubtitleUrl() {
		return subtitleUrl;
	}

	public void setSubtitleUrl(String subtitleUrl) {
		this.subtitleUrl = subtitleUrl;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(name);
		out.writeString(type);
		out.writeString(score);
		out.writeString(description);
		out.writeString(poster);
		out.writeString(movieUrl);
		out.writeString(source);
		out.writeString(intentComponentPackage);
		out.writeString(intentComponentClass);
		out.writeString(intentData);
		out.writeString(apkUrl);
		out.writeString(subtitleUrl);
	}

	public static final Creator<AlbumMovie> CREATOR = new Creator<AlbumMovie>() {
		@Override
		public AlbumMovie[] newArray(int size) {
			return new AlbumMovie[size];
		}

		@Override
		public AlbumMovie createFromParcel(Parcel in) {
			return new AlbumMovie(in);
		}
	};

	public AlbumMovie(Parcel in) {
		id = in.readString();
		name = in.readString();
		type = in.readString();
		score = in.readString();
		description = in.readString();
		poster = in.readString();
		movieUrl = in.readString();
		source = in.readString();
		intentComponentPackage = in.readString();
		intentComponentClass = in.readString();
		intentData = in.readString();
		apkUrl = in.readString();
		subtitleUrl = in.readString();
	}

	public AlbumMovie() {
		super();
	}
}