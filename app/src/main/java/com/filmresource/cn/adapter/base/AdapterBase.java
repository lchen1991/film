package com.filmresource.cn.adapter.base;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AdapterBase<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mList = new LinkedList<T>();

	public AdapterBase(Context mContext) {
		this.mContext = mContext;
	}

	public List<T> getList() {
		return mList;
	}

	public void appendToList(List<T> list) {
		if (list == null) {
			return;
		}
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void cleanAddAll(List<T> list) {
		if (list == null) {
			return;
		}
		mList.clear();
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void appendToTopList(List<T> list) {
		if (list == null) {
			return;
		}
		mList.addAll(0, list);
		notifyDataSetChanged();
	}

	public void appendToTop(T t) {
		if (t == null) {
			return;
		}
		mList.add(0, t);
		notifyDataSetChanged();
	}

	public void append(T t) {
		if (t == null) {
			return;
		}
		mList.add(t);
		notifyDataSetChanged();
	}

	public void removeToList(int positon) {
		if (mList.size() <= positon) {
			return;
		}
		mList.remove(positon);
		notifyDataSetChanged();
	}

	public void clear() {
		mList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (position > mList.size() - 1) {
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getExView(position, convertView, parent);
	}

	protected abstract View getExView(int position, View convertView,
			ViewGroup parent);

}
