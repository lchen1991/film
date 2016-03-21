package com.filmresource.cn.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.filmresource.cn.R;
import com.filmresource.cn.adapter.base.AdapterBase;
import com.filmresource.cn.adapter.base.ViewHolderBase;
import com.filmresource.cn.imageloader.fresco.instrumentation.InstrumentedDraweeView;

import butterknife.Bind;

public class GalleryAdapter extends AdapterBase<String>
{

    public GalleryAdapter(Context mContext) {
        super(mContext);
    }

    private int calcDesiredSize(int parentWidth, int parentHeight) {
        int orientation = mContext.getResources().getConfiguration().orientation;
        int desiredSize = (orientation == Configuration.ORIENTATION_LANDSCAPE) ?
                parentWidth / 2 : parentWidth / 3;
        return Math.min(desiredSize, parentWidth);
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.film_image_item, null);
        FilmViewHolder viewHolder = new FilmViewHolder(mItemView);
        GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(mContext.getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        viewHolder.simpleDraweeView.setHierarchy(gdh);

        int size = calcDesiredSize(parent.getWidth(), parent.getHeight());
        updateViewLayoutParams(viewHolder.simpleDraweeView, size, size);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBase holder, int position) {
        FilmViewHolder filmViewHolder = (FilmViewHolder) holder;

        ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(mList.get(position)))
                        .setResizeOptions(
                                new ResizeOptions(filmViewHolder.simpleDraweeView.getLayoutParams().width,
                                        filmViewHolder.simpleDraweeView.getLayoutParams().height))
                        .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(filmViewHolder.simpleDraweeView.getController())
                .setControllerListener(filmViewHolder.simpleDraweeView.getListener())
                .setAutoPlayAnimations(true)
                .build();
        filmViewHolder.simpleDraweeView.setController(draweeController);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private static void updateViewLayoutParams(View view, int width, int height) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams == null || layoutParams.height != width || layoutParams.width != height) {
            layoutParams = new FrameLayout.LayoutParams(width, height);
            view.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        return null;
    }


    class FilmViewHolder extends ViewHolderBase {
        public View itemView;
        @Bind(R.id.my_image_view)
        InstrumentedDraweeView simpleDraweeView;

        public FilmViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public void shutDown() {
        super.clear();
        Fresco.shutDown();
    }
}  