package com.filmresource.cn.net.manager;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/**
 * ByteArrayLoadControler implements Volley Listener & ErrorListener
 * 
 */
public class ByteArrayLoadControler extends AbsLoadControler implements
		Listener<NetworkResponse>, ErrorListener {

	private LoadListener mOnLoadListener;

	private Class<?> respClass = null;
	
	private int mAction = 0;

	public ByteArrayLoadControler(LoadListener requestListener,Class<?> respClass, int actionId) {
		this.mOnLoadListener = requestListener;
		this.respClass = respClass;
		this.mAction = actionId;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		String errorMsg = null;
		if (error.getMessage() != null) {
			errorMsg = error.getMessage();
		} else {
			try {
				errorMsg = "Server Response Error ("+ error.networkResponse.statusCode + ")";
			} catch (Exception e) {
				errorMsg = "Server Response Error";
			}
		}
		this.mOnLoadListener.onError(errorMsg, getOriginUrl(), this.mAction);
	}

	@Override
	public void onResponse(NetworkResponse response) {
		this.mOnLoadListener.onSuccess(response.data,respClass, response.headers,
				getOriginUrl(), this.mAction);
	}
}