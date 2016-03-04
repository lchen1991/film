package com.filmresource.cn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Html;
import android.view.ViewGroup;

import com.filmresource.cn.bean.MovieClassify;
import com.filmresource.cn.ui.FilmFragment.FilmListFragment;
import com.filmresource.cn.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ql on 2016/3/2.
 */
public class FragmentTabAdapter extends FragmentStatePagerAdapter {

    private List<MovieClassify> tabs ;                            //tab名的列表
    private List<Fragment> mFragments ;

    public FragmentTabAdapter(FragmentManager fm,List<MovieClassify> tabs,List<Fragment> mFragments) {
        super(fm);
        this.tabs = tabs;
        this.mFragments = mFragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getClassify();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        super.destroyItem(container, position, object);
    }
}
