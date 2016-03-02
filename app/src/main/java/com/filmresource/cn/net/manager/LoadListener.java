package com.filmresource.cn.net.manager;

import java.util.Map;

/**
 * LoadListener special for ByteArrayLoadControler
 * 
 */
public interface LoadListener {
	
	void onStart();

	void onSuccess(byte[] data, Class<?> respClass, Map<String, String> headers, String url, int actionId);

	void onError(String errorMsg, String url, int actionId);
	
}
