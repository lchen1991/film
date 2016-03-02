package com.filmresource.cn.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filmresource.cn.R;
import com.filmresource.cn.bean.BaseUI;

public class BaseFragment extends Fragment implements BaseUI {

	protected FragmentActivity mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this.getActivity();
	}

	@Override
	public void onUICallback(int type, Object object) {

	}
}
