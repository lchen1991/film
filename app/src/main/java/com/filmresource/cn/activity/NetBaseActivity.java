package com.filmresource.cn.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.filmresource.cn.common.Constant;
import com.filmresource.cn.net.manager.LoadControler;
import com.filmresource.cn.net.manager.RequestManager;
import com.filmresource.cn.net.manager.RequestManager.RequestListener;
import com.filmresource.cn.utils.LogUtil;

public class NetBaseActivity extends BaseActivity implements RequestListener {

	public HashMap<Integer, LoadControler> requests = null;

	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requests = new HashMap<Integer, LoadControler>();
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void onDestroy() {
		if (requests != null && requests.size() > 0) {
			Collection<LoadControler> loadControlers = requests.values();
			for (LoadControler loadControler : loadControlers) {
				loadControler.cancel();
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
		LogUtil.e(TAG, "move request:"+actionId);
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
		LogUtil.e(TAG, "add addGetNetRequest:"+actionId);
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
}
