package com.filmresource.cn.global;

import android.app.Application;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
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
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseApplication extends Application{

	private static BaseApplication mApplication = null;
	private List<BaseActivity> mActivities = new ArrayList<BaseActivity>();
	private List<BaseFragment> mFragments = new ArrayList<BaseFragment>();

	public static  OSS oss = null;

	@Override
	public void onCreate() {
		RequestManager.getInstance().init(this);
		LogUtil.e("info", "初始化！！！！！！！！！！");
		initAliOss();
		//第二个参数是appkey，就是百川应用创建时候的appkey
		FeedbackAPI.initAnnoy(this,"23332226");
		/*
 * 可以设置UI自定义参数，如主题色等,具体为：
 * enableAudio(是否开启语音 1：开启 0：关闭)
 * bgColor(消息气泡背景色 "#ffffff")，
 * color(消息内容文字颜色)，
 * avatar(当前登录账号的头像)，
 * toAvatar(客服账号的头像)
 * themeColor(标题栏自定义颜色 "#ffffff")
 */
		Map<String,String> customUI = new HashMap<String,String>();
		customUI.put("themeColor","#33444b");
		FeedbackAPI. setUICustomInfo(customUI);
		initOnShare();
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

	private void initOnShare()
	{
		PlatformConfig.setWeixin("wxe209a32608094b8b","f2737767202521407c3e269bca0ec9bc");
		//微信 appid appsecret
//		PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
		//新浪微博 appkey appsecret
//		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
		// QQ和Qzone appid appkey
	}

}
