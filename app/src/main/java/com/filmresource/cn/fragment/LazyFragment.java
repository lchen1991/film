package com.filmresource.cn.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filmresource.cn.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class LazyFragment extends BaseFragment {

    private View mainView;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    protected boolean isVisible;

    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mainView == null) {
            int layoutId = createViewByLayoutId();
            if (layoutId == 0) {
                throw new IllegalArgumentException( "you need createViewByLayoutId ");
            }
            mainView = inflater.inflate(layoutId, container, false);
            ButterKnife.bind(this, mainView);
            isPrepared = true;
            lazyLoad();
        }

        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup)mainView.getParent();
        if(parent != null) {
            parent.removeView(mainView);
        }
        return mainView;
    }

    /**
     * 返回fragemtn要创建的View 不必重写 onCreateView 只需从写改方法
     * */
    protected abstract int createViewByLayoutId();

    protected void onVisible(){
        lazyLoad();
    }

    protected void lazyLoad(){
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        mHasLoadedOnce = true;
        onLazyLoad();
    }

    protected abstract void onLazyLoad();

    protected void onInvisible(){}
}