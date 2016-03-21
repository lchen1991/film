/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.filmresource.cn.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

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
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.imageloader.fresco.instrumentation.InstrumentedDraweeView;
import com.filmresource.cn.imageloader.fresco.instrumentation.PerfListener;
import com.filmresource.cn.utils.StringUtils;

import butterknife.Bind;

/**
 * Base class for the list view adapter.
 * <p/>
 * <p>Subclasses are responsible for downloading images in the correct image loader,
 * and creating Views that can host that loader's views.
 * <p/>
 * <p>The {@link #clear()} method should also be overridden to also clear the
 * loader's memory cache.
 */
public class FilmListAdapter extends AdapterBase<FilmInfo> implements View.OnClickListener {


    private final PerfListener mPerfListener;
    public onRecyclerViewItemClick onRecyclerViewItemClick;

    public FilmListAdapter(Context context, PerfListener perfListener) {
        super(context);
        mPerfListener = perfListener;
    }

    public void setOnRecyclerViewItemClick(onRecyclerViewItemClick onRecyclerViewItemClick)
    {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    private int calcDesiredSize(int parentWidth, int parentHeight) {
        int orientation = mContext.getResources().getConfiguration().orientation;
        int desiredSize = (orientation == Configuration.ORIENTATION_LANDSCAPE) ?
                parentWidth / 2 : parentWidth / 3;
        return Math.min(desiredSize, parentWidth);
    }

    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_movie, null);
        FilmViewHolder viewHolder = new FilmViewHolder(mItemView);
        GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(mContext.getResources())
//                .setPlaceholderImage(Drawables.sPlaceholderDrawable)
//                .setFailureImage(Drawables.sErrorDrawable)
//                .setRoundingParams(RoundingParams.asCircle())
                .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        viewHolder.simpleDraweeView.setHierarchy(gdh);

        int size = calcDesiredSize(parent.getWidth(), parent.getHeight());
        updateViewLayoutParams(viewHolder.simpleDraweeView, size, size);
        if (onRecyclerViewItemClick != null) {
            mItemView.setOnClickListener(this);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBase holder, int position) {
        FilmViewHolder filmViewHolder = (FilmViewHolder) holder;
        FilmInfo filmInfo = mList.get(position);
        if (filmInfo != null && filmInfo.getFimHref()!=null) {
            ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(filmInfo.getFilmPoster()))
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

            String score = filmInfo.getFilmScore();
            if (!StringUtils.isEmpty(score)) {
                int tags = score.indexOf("：");
                if (tags > -1) {
                    String[] scores = score.split("：");
                    if (score.length() > 2) {
                        filmInfo.setFilmScore(scores[1]);
                    }
                }
            }
            filmViewHolder.filmScore.setText(filmInfo.getFilmScore());
            filmViewHolder.textView.setText(filmInfo.getFilmName());
            filmViewHolder.itemView.setTag(filmInfo);
        }

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
        @Bind(R.id.item_movie_title)
        TextView textView;
        @Bind(R.id.filmScore)
        TextView filmScore;

        public FilmViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewItemClick != null) {
            onRecyclerViewItemClick.setOnItemClick(v, (FilmInfo) v.getTag());
        }
    }

    public interface onRecyclerViewItemClick {
        void setOnItemClick(View v, FilmInfo filmInfo);
    }

    public void shutDown() {
        super.clear();
        Fresco.shutDown();
    }
}
