package com.filmresource.cn.bean;

import java.util.List;
import java.util.Map;


public class BtHomePageInfo extends HomePageInfo{
	
	public static  final int BTHOMEPAGE_FILMINFOLIST = 0;
	public static  final int BTHOMEPAGE_FILMINFOLIST_HOT = 1;
	public static  final int BTHOMEPAGE_FILMINFOLIST_NR = 2;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<Integer,List<FilmInfo>> filmMapList ;

	public Map<Integer, List<FilmInfo>> getFilmMapList() {
		return filmMapList;
	}

	public void setFilmMapList(Map<Integer, List<FilmInfo>> filmMapList) {
		this.filmMapList = filmMapList;
	}
	
}
