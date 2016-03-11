package com.filmresource.cn.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.filmresource.cn.R;
import com.filmresource.cn.adapter.base.AdapterBase;
import com.filmresource.cn.adapter.base.ViewHolderBase;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.utils.DensityUtils;

import butterknife.Bind;

/**
 * Created by chenlei on 16/3/1.
 */
public class FilmAdapter extends AdapterBase<FilmInfo> implements View.OnClickListener{

    public FilmAdapter(Context mContext) {
      this.mContext = mContext;
    }

    public  onRecyclerViewItemClick onRecyclerViewItemClick;

    public void setOnRecyclerViewItemClick(onRecyclerViewItemClick onRecyclerViewItemClick)
    {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_movie,null);
        FilmViewHolder viewHolder = new FilmViewHolder(mItemView);
        if(onRecyclerViewItemClick!=null)
        {
            mItemView.setOnClickListener(this);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBase holder, int position) {
        FilmViewHolder filmViewHolder = (FilmViewHolder)holder;
        FilmInfo filmInfo = mList.get(position);
        if(filmInfo!=null)
        {
            if(filmInfo.getFilmPoster()!=null)
            {
                int dmsWidth = ((Constant.screenW - DensityUtils.dp2px(mContext, 17)) * 2 / 6);
                int dmsHeight = ((Constant.screenH - DensityUtils.dp2px(mContext, 17)) * 2 / 9);
                filmViewHolder.simpleDraweeView.getLayoutParams().width = dmsWidth;
                filmViewHolder.simpleDraweeView.getLayoutParams().height = dmsHeight;
                Uri uri = Uri.parse(filmInfo.getFilmPoster());
                filmViewHolder.simpleDraweeView.setImageURI(uri);
            }
            filmViewHolder.textView.setText(filmInfo.getFilmName());
            filmViewHolder.itemView.setTag(filmInfo);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class FilmViewHolder extends  ViewHolderBase
    {
        public View itemView;
        @Bind(R.id.my_image_view)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.item_movie_title)
        TextView textView;
        public FilmViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    @Override
    public void onClick(View v) {
        if(onRecyclerViewItemClick!=null) {
            onRecyclerViewItemClick.setOnItemClick(v,(FilmInfo)v.getTag());
        }
    }

    public interface onRecyclerViewItemClick
    {
        void setOnItemClick(View v,FilmInfo filmInfo);
    }
}
