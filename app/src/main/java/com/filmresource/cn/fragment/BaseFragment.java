package com.filmresource.cn.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filmresource.cn.R;
import com.filmresource.cn.bean.BaseUI;

public class BaseFragment extends Fragment implements BaseUI {

	@Override
	public void onUICallback(int type, Object object) {
		
	}
}
