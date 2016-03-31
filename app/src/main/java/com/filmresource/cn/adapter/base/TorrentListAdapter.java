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


package com.filmresource.cn.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.filmresource.cn.R;
import com.filmresource.cn.bean.TorrentFileInfo;

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
public class TorrentListAdapter extends AdapterBase<TorrentFileInfo> implements View.OnClickListener {


    public onRecyclerViewItemClick onRecyclerViewItemClick;

    public TorrentListAdapter(Context context) {
        super(context);
    }

    public void setOnRecyclerViewItemClick(onRecyclerViewItemClick onRecyclerViewItemClick)
    {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }


    @Override
    public ViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_torrent, parent,false);
        FilmViewHolder viewHolder = new FilmViewHolder(mItemView);

        if (onRecyclerViewItemClick != null) {
            mItemView.setOnClickListener(this);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBase holder, int position) {
        FilmViewHolder filmViewHolder = (FilmViewHolder) holder;
        TorrentFileInfo filmInfo = mList.get(position);
         filmViewHolder.tvTime.setText(filmInfo.getFileCreateTime());
         filmViewHolder.tvName.setText(filmInfo.getFileName());
         filmViewHolder.itemView.setTag(filmInfo);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        return null;
    }


    class FilmViewHolder extends ViewHolderBase {
        public View itemView;
        @Bind(R.id.item_torrent_time)
        TextView tvTime;
        @Bind(R.id.item_torrent_name)
        TextView tvName;

        public FilmViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewItemClick != null) {
            onRecyclerViewItemClick.setOnItemClick(v, (TorrentFileInfo) v.getTag());
        }
    }

    public interface onRecyclerViewItemClick {
        void setOnItemClick(View v, TorrentFileInfo filmInfo);
    }

}
