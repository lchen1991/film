package com.filmresource.cn.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.filmresource.cn.OssData.OssResultListenerX;
import com.filmresource.cn.R;
import com.filmresource.cn.bean.BaseUI;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.global.BaseApplication;
import com.filmresource.cn.widget.CustomDialog;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements BaseUI {

	public static final String TAG = BaseActivity.class.getSimpleName();
	private BaseApplication mApplication;
	private View emptyView;


	@SuppressLint("UseSparseArrays") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		Constant.screenW = displayMetrics.widthPixels;
		Constant.screenH = displayMetrics.heightPixels;
		
		mApplication = BaseApplication.getInstance();
		mApplication.addActivity(this);

	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ButterKnife.bind(this);
	}

	@Override
	protected void onDestroy() {
		mApplication.removeActivity(this);
		super.onDestroy();
	}
	
	
	public void setListEmptyView()
	{
		loadEmptyView(R.layout.empty_view);
	}
	
	public void loadEmptyView(int resLayoutiId)
	{
		if(emptyView == null)
		{
			emptyView = getLayoutInflater().inflate(resLayoutiId, null);
			emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
			emptyView.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onUICallback(int type, Object object) {
	}

}
