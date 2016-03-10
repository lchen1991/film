package com.filmresource.cn.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.filmresource.cn.OssData.OssLoadControler;
import com.filmresource.cn.OssData.OssResultListenerX;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.global.BaseApplication;
import com.filmresource.cn.net.manager.LoadControler;
import com.filmresource.cn.net.manager.RequestManager;
import com.filmresource.cn.net.manager.RequestManager.RequestListener;
import com.filmresource.cn.utils.LogUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NetBaseActivity extends BaseActivity implements RequestListener,OssResultListenerX{

	public HashMap<Integer, LoadControler> requests = null;
	private Map<Integer,OSSAsyncTask> mOssAsynTaskMap = null;
	private  GetObjectRequest get;
	protected Gson gson = new Gson();

	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requests = new HashMap<Integer, LoadControler>();
		mOssAsynTaskMap = new HashMap<Integer,OSSAsyncTask>();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		if (requests != null && requests.size() > 0) {
			Set<Integer> netkeys = requests.keySet();
			for (Integer key:netkeys)
			{
				LoadControler loadControler = requests.get(key);
				loadControler.cancel();
				requests.remove(loadControler);
			}
		}
		if (mOssAsynTaskMap!=null&&mOssAsynTaskMap.size() > 0)
		{
			Set<Integer> osskeys = mOssAsynTaskMap.keySet();
			for (Integer key:osskeys)
			{
				OSSAsyncTask ossAsyncTask = mOssAsynTaskMap.get(key);
				ossAsyncTask.cancel();
				requests.remove(ossAsyncTask);
			}
		}
		super.onDestroy();
	}

	@Override
	public void onRequest() {
	}

	@Override
	public final void onSuccess(Object response, Map<String, String> headers,
			String url, int actionId) {
		LogUtil.e(TAG, "move request:" + actionId);
		moveRequest(actionId);
		onNetResponseSuccess(response, headers, url, actionId);
	}

	@Override
	public final void onError(String errorMsg, String url, int actionId) {
		moveRequest(actionId);
		LogUtil.e(TAG, "move request:" + actionId);
		onNetResponseError(errorMsg, url, actionId);
	}

	/**
	 * get 
	 * @param url
	 * @param requestListener
	 * @param respClass
	 * @param shouldCache
	 * @param actionId
	 */
	public final synchronized void addGetNetRequest(String url,HashMap<String,String> params,
			RequestListener requestListener, Class<?> respClass,
			boolean shouldCache,int actionId) {
		LogUtil.e(TAG, "add addGetNetRequest:" + actionId);
		if (requests != null) {
				requests.put(actionId,
						RequestManager.getInstance().get(getUrl(url, params), requestListener,
								respClass, shouldCache, actionId));
		}
	}

	public final void addGetNetRequest(HashMap<String,String> params,
			RequestListener requestListener, Class<?> respClass,
			boolean shouldCache, int actionId) {
		addGetNetRequest(Constant.BASE_URL, params, requestListener, respClass, shouldCache, actionId);
	}

	
	public final void moveRequest(int actionid) {
		if (requests != null && requests.containsKey(actionid)) {
			requests.remove(actionid);
		}
	}

	public void onNetResponseSuccess(Object response,
			Map<String, String> headers, String url, int actionId) {
	}

	public void onNetResponseError(String errorMsg, String url, int actionId) {
	}

	/**
	 * get url
	 * @param url
	 * @param params
	 * @return
	 */
	private String getUrl(String url, HashMap<String, String> params) {
		if (params != null) {
			StringBuffer sb = null;
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (sb == null) {
					sb = new StringBuffer();
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(key);
				sb.append("=");
				sb.append(value);
			}
			url += sb.toString();
		}
		return url;
	}

	public void asyncGetObject(int actionId,String mBucket,String mObject,boolean showProgress) {

		if(showProgress)
		{
			showLoadProgressDialog();
		}
		try
		{
			if(get == null)
			{
				get = new GetObjectRequest(mBucket, mObject);
			}
			if(BaseApplication.oss == null)
			{
				return;
			}
			OSSAsyncTask ossAsyncTask = mOssAsynTaskMap.get(actionId);
			if(ossAsyncTask!=null)
			{
				ossAsyncTask.cancel();
			}
			ossAsyncTask = BaseApplication.oss.asyncGetObject(get, new OssLoadControler(actionId, this));
			mOssAsynTaskMap.put(actionId,ossAsyncTask);
		}catch (Exception e)
		{
			dismissLoadProgressDialog();
		}

	}

	@Override
	public void onOssSuccess(Object response, int actionId) {

	}

	@Override
	public void onOssError(OSSRequest ossRequest, ClientException e, ServiceException e1) {

	}

	@Override
	public void onOssFinish(int actionId) {
		if(mOssAsynTaskMap.containsKey(actionId))
		{
			mOssAsynTaskMap.remove(actionId);
			if(mOssAsynTaskMap.size()==0)
			{
				dismissLoadProgressDialog();
			}
		}
	}


}
