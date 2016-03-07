package com.filmresource.cn.htmlparser.bttiantang;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.filmresource.cn.bean.BtHomePageInfo;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.bean.MovieClassify;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HtmlParseFromBttt {

	public static String mBaaseUrl = "http://www.bttiantang.com";
	public static Gson gson = new Gson();

	/**
	 * bT天堂首页  
	 * @param url
	 * @throws IOException
	 */
	public BtHomePageInfo getHtmlResourcePage(String url) throws IOException {
		Document document = Jsoup.connect(url).timeout(5000).get();
		BtHomePageInfo btHomePageInfo = new BtHomePageInfo();
		
		Map<Integer,List<FilmInfo>> filmMapList = new HashMap<Integer, List<FilmInfo>>();
		btHomePageInfo.setFilmMapList(filmMapList);
		
		// 电影分类
		Elements elementsTitle = document.getElementsByClass("Btitle");
		List<MovieClassify> movieClassifys = new ArrayList<MovieClassify>();
		for (Element elements : elementsTitle) {
			Elements mTagForA = elements.getElementsByTag("a");
			for (Element elementa : mTagForA) {
				MovieClassify movieClassify = new MovieClassify();
				String href = elementa.attr("href");
				movieClassify.setClassifyHref(mBaaseUrl+href);
				String title = elementa.attr("title");
				movieClassify.setClassifyName(title);
				String value = elementa.html();
				movieClassify.setClassify(value);
				movieClassifys.add(movieClassify);
			}
		}
		
		btHomePageInfo.setMovieClassifys(movieClassifys);
		
		// 电影列表
		List<FilmInfo> filmHomePageFilmList  = getFilmListInfo(url, document);
		filmMapList.put(BtHomePageInfo.BTHOMEPAGE_FILMINFOLIST, filmHomePageFilmList);
		
		//hotlst 火热下载
		List<FilmInfo> filmHotlst = getHotOrNrList(document,"hotlst");
		filmMapList.put(BtHomePageInfo.BTHOMEPAGE_FILMINFOLIST_HOT, filmHotlst);
		
		//nrlst  新片推荐
		List<FilmInfo> filmNrlst = getHotOrNrList(document,"nrlst");
		filmMapList.put(BtHomePageInfo.BTHOMEPAGE_FILMINFOLIST_NR, filmNrlst);

		return btHomePageInfo;
	}

	/**
	 * 解析列表
	 * @param url
	 * @param mDocument
	 */
	public List<FilmInfo> getFilmListInfo(String url, Document mDocument) {
		Document document = mDocument;
		List<FilmInfo> mFilmInfos = new ArrayList<FilmInfo>();
		if(document == null)
		{
			try {
				document = Jsoup.connect(url).timeout(5000).get();
			} catch (IOException e) {
				e.printStackTrace();
				return mFilmInfos;
			}
		}
		Elements elementsFilmItemList = document.getElementsByClass("item");
		for (Element elementItem : elementsFilmItemList) {
			FilmInfo filmInfo = new FilmInfo();
			//介绍信息
			Elements elementsFilmList = elementItem.getElementsByClass("title");
			for (Element elements : elementsFilmList) {
				Elements mTagForP = elements.getElementsByTag("p");
				if(mTagForP.size()>=3)
				{
					String title = mTagForP.get(0).text();
					String regEx="\\d{4}/\\d{2}/\\d{2}";
					Pattern p = Pattern.compile(regEx);
					Matcher m = p.matcher(title);
					if(m.find()){
					    String result=m.group();
					    /**发布日期**/
					    filmInfo.setFilmPublishTime(result);
					    title = title.replace(result,"");
					}
					filmInfo.setFilmName(title);
					String titleName = mTagForP.get(1).text();
					filmInfo.setFilmTitle(titleName);
					String filmPerformers = mTagForP.get(2).text();
					String[] filStrings = filmPerformers.split("/");
					if(filStrings.length > 0)
					{
						filmInfo.setFilmZone(filStrings[0]);
						List<String> filmStarreds  = new ArrayList<String>();;
						for (int i = 1; i < filStrings.length; i++) {
							filmStarreds.add(filStrings[i]);
						}
						filmInfo.setFilmStarred(filmStarreds);
					}
					String filmScore = mTagForP.get(3).text();
					filmInfo.setFilmScore(filmScore);
				}
			}
			//图片
			Elements elementsPic= elementItem.getElementsByClass("litpic");
			for (Element element : elementsPic) {
				Elements elementsa = element.getElementsByTag("a");
				if (elementsa.size() > 0) {
					String mDetailUrl = elementsa.get(0).attr("href");
					Elements elementsImg = elementsa.get(0).getElementsByTag("img");
					if(elementsImg.size()>0)
					{
						String mFilmPic = elementsImg.get(0).attr("src");
						filmInfo.setFilmPoster(mFilmPic);
					}
					filmInfo.setFimHref(mBaaseUrl + mDetailUrl);
				}
			}
			mFilmInfos.add(filmInfo);
		}
		return mFilmInfos;
	}

	/**
	 * 火热下载 "hotlst" || 新片推荐 "nrlst"
	 * @param document
	 * @param lstId
	 */
	private List<FilmInfo> getHotOrNrList(Document document,String lstId) {
		List<FilmInfo> mFilmInfos = new ArrayList<FilmInfo>();
		Element elementsHot= document.getElementById(lstId);
		Elements elementsHotList = elementsHot.getElementsByTag("li");
		for (Element elementsH:elementsHotList) {
			FilmInfo filmInfo = new FilmInfo();
			//图片
			Elements elementsA = elementsH.getElementsByTag("a");
			if(elementsA.size() > 0)
			{
				Element subA = elementsA.get(0);
				filmInfo.setFimHref(mBaaseUrl + subA.attr("href"));
				Elements elementsTagImgs = subA.getElementsByTag("img");
				if(elementsTagImgs.size() > 0)
				{
					String src = elementsTagImgs.attr("src");
					filmInfo.setFilmPoster(src);
				}
			}
			//评分
			Elements elementsSpan = elementsH.getElementsByClass("tit");
			String titleOrspan = elementsSpan.text();
			String[] titles = titleOrspan.split(" ");
			if(titles.length > 0)
			{
				filmInfo.setFilmTitle(titles[0]);
			}
			if(titles.length > 1)
			{
				filmInfo.setFilmScore(titles[1]);
			}
			mFilmInfos.add(filmInfo);
		}
		return mFilmInfos;
	}
	
	/**
	 * 页面详情
	 * @param filmInfo
	 * @param url
	 * @return
	 */
	public FilmInfo getHtmlResourceContent(FilmInfo filmInfo,String url)
	{
		Document document = null;
		try {
			document = Jsoup.connect(url).timeout(5000).get();
			Elements elementsDetailList = document.getElementsByClass("moviedteail_list");
			for (Element elementList:elementsDetailList) {
				Elements elementslis = elementList.getElementsByTag("li");
				for (Element elementLi:elementslis) {
					String li = elementLi.text();
					String[] lis = li.split(":");
					String lisTag = "";
					if(lis.length > 0)
					{
						lisTag = lis[0];
					}
					if(lis.length > 1)
					{
						if("又名".equals(lisTag))
						{
							filmInfo.setFilmName(lis[1]);
						}
						else if("标签".equals(lisTag))
						{
							Elements elementsa = elementLi.getElementsByTag("a");
							List<String> mFilmClassify = new ArrayList<String>();
							for (Element element:elementsa) {
								mFilmClassify.add(element.attr("title"));
							}
							//List
							filmInfo.setFilmClassify(mFilmClassify);
						}
						else if("地区".equals(lisTag))
						{
							filmInfo.setFilmZone(lis[1]);
						}
						else if("年份".equals(lisTag))
						{
							filmInfo.setFilmScreensTime(lis[1]);
						}
						else if("导演".equals(lisTag))
						{
							filmInfo.setFilmDirector(lis[1]);
						}
						else if("编剧".equals(lisTag))
						{
							filmInfo.setFilmScreenWriter(lis[1]);
						}
						else if("详情".equals(lisTag))
						{
							filmInfo.setFilmSynopsis(lis[1]);
						}
					}
				}
			}

			Elements elementsTinfo = document.getElementsByClass("tinfo");
			LinkedHashMap<String,String> downloadTorrentList = new LinkedHashMap<String, String>();
			for (Element elementLi:elementsTinfo) {
				Elements elementsA = elementLi.getElementsByTag("a");
				String title = elementsA.attr("title");
				String href = elementsA.attr("href");
				downloadTorrentList.put(title, mBaaseUrl+href);
			}
			filmInfo.setTorrentDownloadList(downloadTorrentList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filmInfo;
	}
	
	/**
	 * 种子文件下载
	 * @param url
	 * @param id
	 * @param uhash
	 */
	public void downloadtTorrent(String url,String id,String uhash,String filepath)
	{
		//action=download&id=27477&uhash=4934020f2d77e465d9c266ad&imageField.x=10&imageField.y=10
		try {
			URL httpUrl = new URL(url) ;
			HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
			connection.setConnectTimeout(5000);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.connect();

			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(("action=download&id="+id+"&uhash="+uhash+"&imageField.x=68&imageField.y=28").getBytes());
			
			if(connection.getResponseCode() == 200)
			{
				Map<String, List<String>> headers = connection.getHeaderFields();
				//获取文件名
				String fileName = UUID.randomUUID()+".torrent";
				List<String> dispositionList = headers.get("Content-Disposition");
				String s = "filename=\"";
				String e = "torrent";
				for (String string:dispositionList) {
					int nameStart = string.indexOf(s);
					int end = string.lastIndexOf(e);
					fileName = string.substring(nameStart+s.length(), end+e.length());
					fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
				}
				BufferedInputStream inputStreamReader = new BufferedInputStream(connection.getInputStream());
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filepath+fileName));
				byte[] data = new byte[1024];
				int len = -1;
				while ((len = inputStreamReader.read(data))!=-1) {
					bufferedOutputStream.write(data, 0, len);
					bufferedOutputStream.flush();
				}
				inputStreamReader.close();
				bufferedOutputStream.close();
			}
			else
			{
				System.out.println("下载失败！");
			}
			
			outputStream.close();
		} catch (Exception e) {
			System.out.println("下载失败！"+e.getMessage());
		}
		
	}
}
