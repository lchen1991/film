package com.filmresource.cn.common;


import android.os.Environment;

import java.io.File;

public class Constant {

	public static String BASE_URL = "";
	/**屏幕宽高*/
	public static int screenH;
	public static int screenW;
	/**数据库名�?*/
	public static final String DATA_BASE_NAME = ".db";
	/**缓存图片路径*/
	public static final String CACHE_PATH = "_image";

	/*** aliyun oss config **/
	// 运行sample前需要配置以下字段为有效的值
	public static String endpoint = "oss-cn-beijing.aliyuncs.com";
	public static String accessKeyId = "ubuSFRjBoEkKJgfC";
	public static String accessKeySecret = "SIVkFeOyOFGWZ9syuDCrni4mxWBavb";

	public static String bucket = "bttiantang";
	public static String bucket_detail = "bttiantang-detail";
	public static String bucketObj = "homePage";

	public static String bttiantang_downloadurl = "http://www.bttiantang.com/download1.php";
	public static String storagePath = Environment.getExternalStorageDirectory()+ File.separator+"filmResource";
	public static String baiduurl = "https://m.baidu.com/s?word=";



}
