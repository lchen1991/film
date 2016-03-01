package com.filmresource.cn.bean;

import java.io.Serializable;


public class MovieClassify implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String classify;
	private String classifyName;
	private String classifyHref;
	
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getClassifyName() {
		return classifyName;
	}
	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}
	public String getClassifyHref() {
		return classifyHref;
	}
	public void setClassifyHref(String classifyHref) {
		this.classifyHref = classifyHref;
	}
	
}
