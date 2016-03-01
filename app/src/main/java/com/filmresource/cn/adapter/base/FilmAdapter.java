package com.filmresource.cn.adapter.base;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.filmresource.cn.R;
import com.filmresource.cn.bean.FilmInfo;

/**
 * Created by chenlei on 16/3/1.
 */
public class FilmAdapter extends AdapterBase<FilmInfo> {

    public FilmAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_movie,null);
            viewHolder.simpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.my_image_view);
            viewHolder.textView = (TextView)convertView.findViewById(R.id.item_movie_title);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FilmInfo filmInfo = (FilmInfo)getItem(position);
        if(filmInfo!=null)
        {
            if(filmInfo.getFilmPoster()!=null)
            {
                Uri uri = Uri.parse(filmInfo.getFilmPoster());
                viewHolder.simpleDraweeView.setImageURI(uri);
            }
          viewHolder.textView.setText(filmInfo.getFilmName());
        }
        return convertView;
    }

    class ViewHolder
    {
        SimpleDraweeView simpleDraweeView;
        TextView textView;
    }
}
