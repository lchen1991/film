package com.filmresource.cn.bean;

import java.io.Serializable;
import java.util.List;

public class FilmInfo extends  BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String filmName; // 电影名称
	private String fimHref; // 电影页面地址
	private String filmTitle;// 电影title
	private List<String> filmClassify;// 分类
	private String filmZone;// 地域
	private String filmScreensTime;// 上映时间时间
	private String filmPublishTime;// 发布时间
	private String filmDirector; //导演
	private String filmScreenWriter; //编剧
	private List<String> filmStarred;// 电影主演
	private String filmState;// 电影状态
	private List<String> filmImages;// 电影图集
	private String filmPoster;// 电影海报
	private String filmSynopsis;// 电影简介
	private String filmScore;
	private List<String> torrentDownloadList;

	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}

	public String getFimHref() {
		return fimHref;
	}

	public void setFimHref(String fimHref) {
		this.fimHref = fimHref;
	}

	public String getFilmTitle() {
		return filmTitle;
	}

	public void setFilmTitle(String filmTitle) {
		this.filmTitle = filmTitle;
	}

	public List<String> getFilmClassify() {
		return filmClassify;
	}

	public void setFilmClassify(List<String> filmClassify) {
		this.filmClassify = filmClassify;
	}

	public String getFilmZone() {
		return filmZone;
	}

	public void setFilmZone(String filmZone) {
		this.filmZone = filmZone;
	}

	public String getFilmScreensTime() {
		return filmScreensTime;
	}

	public void setFilmScreensTime(String filmScreensTime) {
		this.filmScreensTime = filmScreensTime;
	}

	public String getFilmPublishTime() {
		return filmPublishTime;
	}

	public void setFilmPublishTime(String filmPublishTime) {
		this.filmPublishTime = filmPublishTime;
	}

	public List<String> getFilmStarred() {
		return filmStarred;
	}

	public void setFilmStarred(List<String> filmStarred) {
		this.filmStarred = filmStarred;
	}

	public String getFilmState() {
		return filmState;
	}

	public void setFilmState(String filmState) {
		this.filmState = filmState;
	}

	public List<String> getFilmImages() {
		return filmImages;
	}

	public void setFilmImages(List<String> filmImages) {
		this.filmImages = filmImages;
	}

	public String getFilmPoster() {
		return filmPoster;
	}

	public void setFilmPoster(String filmPoster) {
		this.filmPoster = filmPoster;
	}

	public String getFilmSynopsis() {
		return filmSynopsis;
	}

	public void setFilmSynopsis(String filmSynopsis) {
		this.filmSynopsis = filmSynopsis;
	}

	public String getFilmScore() {
		return filmScore;
	}

	public void setFilmScore(String filmScore) {
		this.filmScore = filmScore;
	}

	public List<String> getTorrentDownloadList() {
		return torrentDownloadList;
	}

	public void setTorrentDownloadList(List<String> torrentDownloadList) {
		this.torrentDownloadList = torrentDownloadList;
	}

	public String getFilmDirector() {
		return filmDirector;
	}

	public void setFilmDirector(String filmDirector) {
		this.filmDirector = filmDirector;
	}

	public String getFilmScreenWriter() {
		return filmScreenWriter;
	}

	public void setFilmScreenWriter(String filmScreenWriter) {
		this.filmScreenWriter = filmScreenWriter;
	}

}
