package com.filmresource.cn.net.manager;

import android.app.Activity;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * RequestListener Holder to avoid memory leak!
 * 
 */
public class RequestListenerHolder implements LoadListener {

	protected static final String CHARSET_UTF_8 = "UTF-8";

	protected WeakReference<RequestManager.RequestListener> mRequestListenerRef;

	protected RequestManager.RequestListener mRequestListener;

	public RequestListenerHolder(){}
	
	public RequestListenerHolder(RequestManager.RequestListener requestListener) {
		if (requestListener instanceof Activity) {
			this.mRequestListenerRef = new WeakReference<RequestManager.RequestListener>(
					requestListener);
		} else {
			this.mRequestListener = requestListener;
		}
	}

	@Override
	public void onStart() {
		if (mRequestListenerRef != null) {
			RequestManager.RequestListener requestListener = mRequestListenerRef.get();
			if (requestListener != null) {
				requestListener.onRequest();
				return;
			}
		}

		if (this.mRequestListener != null) {
			this.mRequestListener.onRequest();
		}
	}

	@Override
	public void onSuccess(byte[] data,Class<?> respClass, Map<String, String> headers, String url,
			int actionId) {
		String parsed = null;
		try {
			parsed = new String(data, CHARSET_UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (mRequestListenerRef != null) {
			RequestManager.RequestListener requestListener = mRequestListenerRef.get();
			if (requestListener != null) {
				onSuccessToParser(requestListener,parsed,respClass, headers, url, actionId);
				return;
			}
		}

		if (this.mRequestListener != null) {
			onSuccessToParser(mRequestListener, parsed,respClass, headers, url, actionId);
		}
	}

	@Override
	public void onError(String errorMsg, String url, int actionId) {
		if (mRequestListenerRef != null) {
			RequestManager.RequestListener requestListener = mRequestListenerRef.get();
			if (requestListener != null) {
				requestListener.onError(errorMsg, url, actionId);
				return;
			}
		}

		if (this.mRequestListener != null) {
			this.mRequestListener.onError(errorMsg, url, actionId);
		}
	}
	
	public void  onSuccessToParser(RequestManager.RequestListener requestListener,String parsed,Class<?> respClass,Map<String, String> headers, String url,
			int actionId)
	{
		requestListener.onSuccess(parsed, headers, url, actionId);
	}
}