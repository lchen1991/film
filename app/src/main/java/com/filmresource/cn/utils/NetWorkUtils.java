package com.filmresource.cn.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetWorkUtils {

	private static String LOG_TAG = "NetWorkHelper";

	/** 没有网络 */
	public static final int NETWORKTYPE_INVALID = 0;
	/** wap网络 */
	public static final int NETWORKTYPE_WAP = 1;
	/** 2G网络 */
	public static final int NETWORKTYPE_2G = 2;
	/** 3G */
	public static final int NETWORKTYPE_3G = 3;
	public static final int NETWORKTYPE_4G = 4;
	/** wifi网络 */
	public static final int NETWORKTYPE_WIFI = 5;
	
	/**
	 * 判断是否有可用的网络
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {
			LogUtil.w(LOG_TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].isAvailable()) {
						LogUtil.w(LOG_TAG, "network is available");
						return true;
					}
				}
			}
		}
		LogUtil.w(LOG_TAG, "network is not available");
		return false;
	}

	/**
	 * 是否有可连接的网络
	 * @param context
	 * @return
	 */
	public static boolean checkNetState(Context context) {
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}

	/**
	 * 判断网络是否为漫游
	 */
	public static boolean isNetworkRoaming(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.w(LOG_TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null
					&& info.getType() == ConnectivityManager.TYPE_MOBILE) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				if (tm != null && tm.isNetworkRoaming()) {
					Log.d(LOG_TAG, "network is roaming");
					return true;
				} else {
					Log.d(LOG_TAG, "network is not roaming");
				}
			} else {
				Log.d(LOG_TAG, "not using mobile network");
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isMobileDataEnable(Context context) throws Exception {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isMobileDataEnable = false;

		isMobileDataEnable = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

		return isMobileDataEnable;
	}

	/**
	 * 判断wifi 是否可用
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isWifiDataEnable(Context context) throws Exception {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isWifiDataEnable = false;
		isWifiDataEnable = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		return isWifiDataEnable;
	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}
	
	public static int GetNetworkType(Context context)
	{
	    ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.w(LOG_TAG, "couldn't get connectivity manager");
		} else {
			   NetworkInfo networkInfo =connectivity.getActiveNetworkInfo();
			    if (networkInfo != null && networkInfo.isConnected())
			    {
			        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
			        {
			            return NETWORKTYPE_WIFI;
			        }
			        else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
			        {
			            String _strSubTypeName = networkInfo.getSubtypeName();
			            
			            Log.w(LOG_TAG,"Network getSubtypeName : " + _strSubTypeName);
			            // TD-SCDMA   networkType is 17
			            int networkType = networkInfo.getSubtype();
			            switch (networkType) {
			                case TelephonyManager.NETWORK_TYPE_GPRS:
			                case TelephonyManager.NETWORK_TYPE_EDGE:
			                case TelephonyManager.NETWORK_TYPE_CDMA:
			                case TelephonyManager.NETWORK_TYPE_1xRTT:
			                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
			                	return NETWORKTYPE_2G;
			                case TelephonyManager.NETWORK_TYPE_UMTS:
			                case TelephonyManager.NETWORK_TYPE_EVDO_0:
			                case TelephonyManager.NETWORK_TYPE_EVDO_A:
			                case TelephonyManager.NETWORK_TYPE_HSDPA:
			                case TelephonyManager.NETWORK_TYPE_HSUPA:
			                case TelephonyManager.NETWORK_TYPE_HSPA:
			                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
			                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
			                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
			                	return NETWORKTYPE_3G;
			                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
			                	return NETWORKTYPE_4G;
			                default:
			                	// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
			                    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") ||
			                    		_strSubTypeName.equalsIgnoreCase("WCDMA") ||
			                    		_strSubTypeName.equalsIgnoreCase("CDMA2000")) 
			                    {
			                    	return NETWORKTYPE_3G;
			                    }
			                    else
			                    {
			                    	Log.w(LOG_TAG,"other Network getSubtypeName : " + _strSubTypeName);
			                    }
			                    break;
			             }
			             
			        }
			    }
		}
	    
	    return -1;
	}

}
