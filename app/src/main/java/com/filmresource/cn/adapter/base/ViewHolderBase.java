package com.filmresource.cn.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by ql on 2016/3/2.
 */
public class ViewHolderBase extends RecyclerView.ViewHolder {
    public ViewHolderBase(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
