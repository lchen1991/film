package com.filmresource.cn.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.filmresource.cn.R;
import com.filmresource.cn.bean.BaseUI;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.global.BaseApplication;
import com.filmresource.cn.widget.CustomDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements BaseUI {

	public static final String TAG = BaseActivity.class.getSimpleName();
	private BaseApplication mApplication;
	private View emptyView;
	private CustomDialog loadProgressDialog;

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

	public void showLoadProgressDialog()
	{
		showLoadProgressDialog(null);
	}

	public void showLoadProgressDialog(String message)
	{
		if(loadProgressDialog == null)
		{
			loadProgressDialog = CustomDialog.createLoadProgressDialog(this);
		}

		loadProgressDialog.setMessage(message);
		if(!this.isFinishing()&&!loadProgressDialog.isShowing())
		{
			loadProgressDialog.show();
		}
	}

	public void dismissLoadProgressDialog()
	{
		if(!this.isFinishing()&&loadProgressDialog!=null&&loadProgressDialog.isShowing())
		{
			loadProgressDialog.dismiss();
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
