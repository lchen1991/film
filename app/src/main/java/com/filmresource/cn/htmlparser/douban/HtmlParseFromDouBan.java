package com.filmresource.cn.htmlparser.douban;

import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.bean.SearchInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlParseFromDouBan {

	public static String mBaaseUrl = "https://movie.douban.com";
	public static Gson gson = new Gson();
	
	public FilmInfo searChFilmInfo(FilmInfo filmInfo,String str)
	{
		//https://movie.douban.com/j/subject_suggest?q=
		try {
			URL httpUrl = new URL("https://movie.douban.com/j/subject_suggest?q="+str) ;
			HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
			connection.setConnectTimeout(5000);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			//connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.connect();
			
			if(connection.getResponseCode() == 200)
			{
				InputStream in = connection.getInputStream();
				StringBuffer sb = new StringBuffer();
				InputStreamReader inputStreamReader = new InputStreamReader(in);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String resData = null;

				while ((resData = bufferedReader.readLine()) != null) {
					sb.append(resData);
				}
				in.close();
				inputStreamReader.close();
				 
				List<SearchInfo> searchInfos = gson.fromJson(sb.toString(),new TypeToken<List<SearchInfo>>(){}.getType());
				//System.out.println(searchInfos.toString());
				if(searchInfos.size() > 0)
				{
					System.out.println(searchInfos.get(0).getTitle());
					getHtmlResourceContent(filmInfo, searchInfos.get(0).getUrl());
				}
			}
			else
			{
				System.out.println("查询失败！--"+connection.getResponseCode());
			}
			
		} catch (Exception e) {
			System.out.println("下载失败！"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 页面详情
	 * @param filmInfo
	 * @param url
	 * @return
	 */
	public FilmInfo getHtmlResourceContent(FilmInfo filmInfo,String url)
	{
		if(filmInfo == null)
		{
			filmInfo = new FilmInfo();
		}
		Document document = null;
		try {
			System.out.println("豆瓣url:--"+url);
			document = Jsoup.connect(url).timeout(5000).get();
			
			Element elementInfos = document.getElementById("info");
			String[] infos = elementInfos.text().toString().split(":");
			String tag = "";
			for (String sub:infos) {
				if("".equals(tag))
				{
					tag = sub;
					continue;
				}
				if("导演".equals(tag))
				{
					tag = "编剧";
					String director = sub.replace(tag, "").trim();
					if(director!=null&&!director.equals(filmInfo.getFilmDirector().trim()))
					{
						return filmInfo;
					}
					filmInfo.setFilmDirector(director);
					System.out.println("导演：" + filmInfo.getFilmDirector());
				}
				else if("编剧".equals(tag))
				{
					tag = "主演";
					filmInfo.setFilmScreenWriter(sub.replace(tag, ""));
					System.out.println("编剧:" + filmInfo.getFilmScreenWriter());
				}
				else if("主演".equals(tag))
				{
					tag = "类型";
				
					String classifys = sub.trim().replace(tag, "");
					String[] Starreds = classifys.split("/");
					List<String> mStarreds =  Arrays.asList(Starreds);
					filmInfo.setFilmStarred(mStarreds);
					
					System.out.println("主演:" + filmInfo.getFilmStarred().toString());
				}
				else if("类型".equals(tag))
				{
					tag = "制片国家/地区";
					
					String classifys = sub.trim().replace(tag, "");
					String[] Classify = classifys.split("/");
					List<String> mClassifys =  Arrays.asList(Classify);
					filmInfo.setFilmClassify(mClassifys);
					
					System.out.println("类型:" + filmInfo.getFilmClassify().toString());
				}
				else if("制片国家/地区".equals(tag))
				{
					tag = "语言";
					filmInfo.setFilmZone(sub.replace(tag, ""));
					System.out.println("制片国家/地区:" + filmInfo.getFilmZone());
				}
				else if("语言".equals(tag))
				{
					tag = "上映日期";
					filmInfo.setFilmLanguage(sub.replace(tag, ""));
					System.out.println("语言" + filmInfo.getFilmLanguage());
				}
				else if("上映日期".equals(tag))
				{
					tag = "片长";
					filmInfo.setFilmScreensTime(sub.replace(tag, ""));
					System.out.println("上映日期" + filmInfo.getFilmScreensTime());
				}
				else if("片长".equals(tag))
				{
					tag = "又名";
					filmInfo.setFilmDuration(sub.replace(tag, ""));
					System.out.println("片长:" + filmInfo.getFilmDuration());
				}
				else if("又名".equals(tag))
				{
					tag = "IMDb链接";
					filmInfo.setFilmAlias(sub.replace(tag, ""));
					System.out.println("又名:" + filmInfo.getFilmAlias());
				}
			}
			//海报
			Elements elementSnbgnbg = document.getElementsByClass("nbgnbg");
			for (Element element:elementSnbgnbg) {
				Elements elementImg = element.getElementsByTag("img");
				if(elementImg.size()>0)
				{
				  String poster = elementImg.get(0).attr("src");
				  filmInfo.setFilmPoster(poster);
				  System.out.println(poster);
				}
			}
			
			//related-info
			
			Elements elementRelated = document.getElementsByClass("related-info");
			if(elementRelated.size()>0)
			{
				filmInfo.setFilmSynopsis("详情："+elementRelated.get(0).text());
			}
			
			//related-pic-bd narrow 剧照
			Elements elementNarrow = document.getElementsByClass("related-pic");
			List<String> mImages = new ArrayList<String>();
			if(elementNarrow.size() > 0)
			{
				Elements elementImg = elementNarrow.get(0).getElementsByTag("img");
				for (Element eImg:elementImg) {
					mImages.add(eImg.attr("src"));
					System.out.println(eImg.attr("src"));
				}
				filmInfo.setFilmImages(mImages);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filmInfo;
	}
	
}
