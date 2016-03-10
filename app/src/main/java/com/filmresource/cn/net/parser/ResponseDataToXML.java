package com.filmresource.cn.net.parser;

import com.filmresource.cn.net.manager.RequestListenerHolder;
import com.filmresource.cn.net.manager.RequestManager;

import java.util.Map;

public class ResponseDataToXML extends RequestListenerHolder {

	public ResponseDataToXML(){}
	public ResponseDataToXML(RequestManager.RequestListener requestListener) {
		super(requestListener);
	}
	
	@Override
	public void onSuccessToParser(RequestManager.RequestListener requestListener,
			String parsed, Class<?> respClass, Map<String, String> headers,
			String url, int actionId) {

		super.onSuccessToParser(requestListener, parsed, respClass, headers, url, actionId);
	}

}
