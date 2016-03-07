package com.filmresource.cn.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.filmresource.cn.R;

public class CustomDialog extends Dialog {
	private Context context = null;
	private static CustomDialog customDialog = null;

	public CustomDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public static CustomDialog createLoadProgressDialog(Context context) {
		customDialog = new CustomDialog(context, R.style.LoadProgressDialog);
		View progressView = View.inflate(context, R.layout.loading_progress_dialog, null);
		customDialog.setContentView(progressView);
		customDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customDialog.setCanceledOnTouchOutside(false);
		customDialog.setCancelable(true);
		progressView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		return customDialog;
	}

}
