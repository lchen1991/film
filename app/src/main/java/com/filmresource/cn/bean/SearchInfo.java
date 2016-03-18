package com.filmresource.cn.bean;

public class SearchInfo {

	private String episode;
	private String img;
	private String title;
	private String url;
	private String sub_title;
	private String year;
	private String type;
	public String getEpisode() {
		return episode;
	}
	public void setEpisode(String episode) {
		this.episode = episode;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSub_title() {
		return sub_title;
	}
	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "episode:"+episode+"\n"
				+"img:"+img+"\n"
				+"title"+title+"\n"
				+"url"+url+"\n"
				+"sub_title"+sub_title+"\n"
						+"year"+year+"\n"
				;
	}
}
