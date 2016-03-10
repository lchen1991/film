package com.filmresource.cn.net.parser;

import com.alibaba.fastjson.JSON;
import com.filmresource.cn.net.manager.RequestListenerHolder;
import com.filmresource.cn.net.manager.RequestManager.RequestListener;

import java.util.Map;

public class ResponseDataToJSON extends RequestListenerHolder {

	public ResponseDataToJSON(){}
	public ResponseDataToJSON(RequestListener requestListener) {
		super(requestListener);
	}

	@Override
	public void onSuccessToParser(RequestListener requestListener,
			String parsed, Class<?> respClass, Map<String, String> headers,
			String url, int actionId) {

		Object object = null;
		try {
			   object = JSON.parseObject(parsed,respClass);
		} catch (Exception e) {
		}
		finally
		{
			if(object == null) onError("", url, actionId);
		}
		requestListener.onSuccess(object, headers, url, actionId);
	}
}
