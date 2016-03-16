package com.filmresource.cn.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

public class CommonUtils {

	//横竖屏计算图片宽高
	public static int calcDesiredSize(Context context,int parentWidth, int parentHeight) {
		int orientation = context.getResources().getConfiguration().orientation;
		int desiredSize = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? parentHeight / 2 : parentHeight / 3;
		return Math.min(desiredSize, parentWidth);
	}

	//横竖屏设置图片宽高
	public static void updateViewLayoutParams(View view, int width, int height) {
		if (view.getLayoutParams() == null || view.getLayoutParams().height != width || view.getLayoutParams().width != height) {
			view.getLayoutParams().width = width;
			view.getLayoutParams().height = height;
		}
	}
}
