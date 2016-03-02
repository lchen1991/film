package com.filmresource.cn.global;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Application;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.filmresource.cn.activity.BaseActivity;
import com.filmresource.cn.fragment.BaseFragment;
import com.filmresource.cn.net.manager.RequestManager;

public class BaseApplication extends Application{

	private static BaseApplication mApplication = null;
	private List<BaseActivity> mActivities = new ArrayList<BaseActivity>();
	private List<BaseFragment> mFragments = new ArrayList<BaseFragment>();
	
	@Override
	public void onCreate() {
		RequestManager.getInstance().init(this);
		super.onCreate();
	}
	public static BaseApplication getInstance()
	{
		if(mApplication == null)
		{
			mApplication = new BaseApplication();
		}
		return mApplication;
	}
	
	public void onUICallBackAll(int type,Object object)
	{
		for (BaseActivity activity:mActivities) {
			activity.onUICallback(type, object);
		}
		for (BaseFragment fragment:mFragments) {
			fragment.onUICallback(type, object);
		}
	}
	
	public void addActivity(BaseActivity activity)
	{
		mActivities.add(activity);
	}
	
	public void removeActivity(BaseActivity activity)
	{
		mActivities.remove(activity);
	}
	
//	 CookieSyncManager.createInstance(this);
//	    CookieManager localCookieManager = CookieManager.getInstance();
//	    localCookieManager.setAcceptCookie(true);
//	    localCookieManager.removeSessionCookie();
//	    CookieSyncManager.getInstance().sync();
}
