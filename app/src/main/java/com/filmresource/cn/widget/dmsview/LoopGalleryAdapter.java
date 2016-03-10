package com.filmresource.cn.widget.dmsview;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LoopGalleryAdapter extends BaseAdapter {
    private NavigationGallery mNavigationGallery;
    
    public LoopGalleryAdapter(NavigationGallery navigationGallery) {
        mNavigationGallery = navigationGallery;
    }
    
    /**
     * @see android.widget.Adapter#getCount()
     *
     */
    @Override
    public int getCount() {
        int count = 0;
        if(getRealCount() == 1) {
            count = 1;
        } else if(getRealCount() == 0) {
            count = 0;
        } else {
            count = Integer.MAX_VALUE;
        }
        return count;
    }
    
    public int getRealCount() {
        return 0;
    }
    
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     *
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     *
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

    public int getRealPosition(int position) {
        int realPosition = 0;
        if(getRealCount() != 0) {
            realPosition = position % getRealCount();
        }
        return realPosition;
    }
    
    public int nextPosition(int position) {
        position++;
        position %= getRealCount();
        return position;
    }
    
    public int lastPosition(int position) {
        position--;
        position %= getRealCount();
        if(position == -1) {
            position = getRealCount() - 1;
        }
        return position;
    }
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(getCount() > 1) {
            mNavigationGallery.setSelection(getRealCount() * 1000);
        }
    }
    
}
