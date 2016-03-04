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

import butterknife.Bind;

/**
 * Created by chenlei on 16/3/1.
 */
public class FilmAdapter extends AdapterBase<FilmInfo> {

    public FilmAdapter(Context mContext) {
      this.mContext = mContext;
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_movie,null);
        FilmViewHolder viewHolder = new FilmViewHolder(mItemView);
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
                Uri uri = Uri.parse(filmInfo.getFilmPoster());
                filmViewHolder.simpleDraweeView.setImageURI(uri);
            }
            filmViewHolder.textView.setText(filmInfo.getFilmName());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class FilmViewHolder extends  ViewHolderBase
    {
        @Bind(R.id.my_image_view)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.item_movie_title)
        TextView textView;
        public FilmViewHolder(View itemView) {
            super(itemView);
        }
    }
}
