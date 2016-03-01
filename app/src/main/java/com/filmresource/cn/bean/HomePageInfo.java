package com.filmresource.cn.bean;

import java.io.Serializable;
import java.util.List;

public class HomePageInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<MovieClassify> movieClassifys;

	public List<MovieClassify> getMovieClassifys() {
		return movieClassifys;
	}

	public void setMovieClassifys(List<MovieClassify> movieClassifys) {
		this.movieClassifys = movieClassifys;
	}
	
}
