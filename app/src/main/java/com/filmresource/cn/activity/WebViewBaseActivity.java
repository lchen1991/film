package com.filmresource.cn.activity;
import java.lang.reflect.InvocationTargetException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.filmresource.cn.R;
import com.filmresource.cn.activity.BaseActivity;
import com.filmresource.cn.utils.LogUtil;
import com.filmresource.cn.utils.NetWorkUtils;

import butterknife.Bind;

public class WebViewBaseActivity extends BaseActivity {

	@Bind(R.id.webview)
	WebView mWebView;

	private String url;

	// 在WebView加载页面之前，可以选同步Cookie，然后再加载网页
	private void setWebCookies() {
		// print cookie
		// System.out.println(cookieManager.getCookie("http://xxx.xxx.xxx"));
		// ad.a("WebViewBaseActivity", "set cookie..." + str);
		CookieSyncManager.createInstance(this);
		CookieManager mCookieManager = CookieManager.getInstance();
		mCookieManager.setAcceptCookie(true);
		mCookieManager.setCookie("app.zz.com", "uin=");
		mCookieManager.setCookie("app.zz.com", "skey=");
		CookieSyncManager.getInstance().sync();
	}

	// 清除所有cookie
	// private void removeAllCookie() {
	// CookieSyncManager cookieSyncManager = CookieSyncManager
	// .createInstance(this);
	// CookieManager cookieManager = CookieManager.getInstance();
	// cookieManager.setAcceptCookie(true);
	// cookieManager.removeSessionCookie();
	// String testcookie1 = cookieManager.getCookie("");
	// cookieManager.removeAllCookie();
	// cookieSyncManager.sync();
	// String testcookie2 = cookieManager.getCookie("");
	// }

	@SuppressLint("NewApi")
	public void setWebSetting() {
		if (NetWorkUtils.isNetworkAvailable(this)) {
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放
			mWebView.getSettings().setLoadWithOverviewMode(true);// 缩放至屏幕的大小
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // webview中缓存
			mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			mWebView.setHorizontalScrollBarEnabled(false);
			mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(
					true);// 支持通过JS打开新窗口
			mWebView.requestFocusFromTouch();
			if (Build.VERSION.SDK_INT >= 11)// 防止对象注入漏洞
				mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
			mWebView.setWebChromeClient(new CustomChromeClient());
			mWebView.setWebViewClient(new CustomWebClient());
		} else {
			mWebView.getSettings().setCacheMode(
					WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
	}

	protected void onDestroy() {
		LogUtil.e("WebViewBaseActivity", "onDestroy");
		mWebView.clearAnimation();
		mWebView.freeMemory();
		mWebView.destroyDrawingCache();
		System.gc();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.e("WebViewBaseActivity", "onKeyDown");
		if ((keyCode == KeyEvent.KEYCODE_BACK) && (mWebView.canGoBack()))
		{
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onPause() {
		super.onPause();
		LogUtil.e("WebViewBaseActivity", "onPause");
		mWebView.pauseTimers();
		if (isFinishing()) {
			mWebView.loadUrl("about:blank");
			setContentView(new FrameLayout(this));
		}
		doMethod("onPause");
	}

	protected void onResume() {
		super.onResume();
		mWebView.resumeTimers();
		doMethod("onResume");
	}

	private void doMethod(String method) {
		if (mWebView != null) {
			try {
				WebView.class.getMethod(method).invoke(mWebView, new Class[0]);
			} catch (NoSuchMethodException localNoSuchMethodException) {
				Log.i("No such method: " + method,
						localNoSuchMethodException.toString());
			} catch (IllegalAccessException localIllegalAccessException) {
				Log.i("Illegal Access: " + method,
						localIllegalAccessException.toString());
			} catch (InvocationTargetException localInvocationTargetException) {
				Log.i("Invocation Target Exception: " + method,
						localInvocationTargetException.toString());
			}
		}
	}

	class CustomWebClient extends WebViewClient
	{

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			LogUtil.w("WebViewBaseActivity", "onPageStarted-->" + url);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			WebViewBaseActivity.this.url = url;
			LogUtil.w("WebViewBaseActivity", "onPageFinished-->" + url);
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
									   SslError error) {
			//handler.cancel(); 默认的处理方式，WebView变成空白页
			handler.proceed();//接受证书
			//view.loadUrl("file:///android_asset/error_page.htm");
			super.onReceivedSslError(view, handler, error);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl) {
			view.loadUrl("file:///android_asset/error_page.htm");
//			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}
	}

	class CustomChromeClient extends WebChromeClient
	{
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
//				if(mPullToRefreshWebView!=null)
//				{
//					mPullToRefreshWebView.onRefreshComplete();
//				}
			}
			super.onProgressChanged(view, newProgress);
		}
	}

//	@Override
//	public void onRefresh(PullToRefreshBase<WebView> refreshView) {
//		if(url!=null&&!"".equals(url))
//		{
//			mWebView.loadUrl(url);
//		}
//	}

}