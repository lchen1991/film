package com.filmresource.cn.global;

import android.app.Application;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.filmresource.cn.activity.BaseActivity;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.fragment.BaseFragment;
import com.filmresource.cn.net.manager.RequestManager;
import com.filmresource.cn.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application{

	private static BaseApplication mApplication = null;
	private List<BaseActivity> mActivities = new ArrayList<BaseActivity>();
	private List<BaseFragment> mFragments = new ArrayList<BaseFragment>();

	public static  OSS oss = null;

	@Override
	public void onCreate() {
		RequestManager.getInstance().init(this);
		LogUtil.e("info","初始化！！！！！！！！！！");
		initAliOss();
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

	public void initAliOss() {
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.accessKeyId, Constant.accessKeySecret);
		ClientConfiguration conf = new ClientConfiguration();

		conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
		conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
		conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
		conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
		OSSLog.enableLog();
		oss = new OSSClient(getApplicationContext(),Constant.endpoint, credentialProvider, conf);

		LogUtil.e("info",oss.toString());
	}
}
