package com.filmresource.cn.ui.FilmFragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.filmresource.cn.OssData.OssGetObjectData;
import com.filmresource.cn.OssData.OssResultListener;
import com.filmresource.cn.R;
import com.filmresource.cn.activity.BaseActivity;
import com.filmresource.cn.adapter.FilmAdapter;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.fragment.BaseFragment;
import com.filmresource.cn.fragment.LazyFragment;
import com.filmresource.cn.global.BaseApplication;
import com.filmresource.cn.utils.DensityUtils;
import com.filmresource.cn.utils.LogUtil;
import com.filmresource.cn.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.Bind;

/**
 * Created by ql on 2016/3/3.
 */
public  class FilmListFragment extends LazyFragment {

    private FilmAdapter filmAdapter = null;
    @Bind(R.id.swipe_layout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private Gson gson = new Gson();
    private List<FilmInfo> filmInfos = null;
    private Handler handler = new Handler();

    public static FilmListFragment getInstance(String href)
    {
        FilmListFragment filmListFragment = new FilmListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("href",href);
        filmListFragment.setArguments(bundle);
        return  filmListFragment;
    }
    @Override
    protected int createViewByLayoutId() {
        return R.layout.content_main;
    }

    @Override
    protected void onLazyLoad() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            String href =  bundle.getString("href");
            String mObjHref = Constant.bucketObj + "&" +href;
            OssGetObjectData ossGetObjectData = new OssGetObjectData(BaseApplication.getInstance().oss, Constant.bucket,mObjHref,new OssResultListener()
            {
                @Override
                public void onResult(final String data) {
                    LogUtil.e("info", data);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            swipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });

                            filmInfos = gson.fromJson(data, new TypeToken<List<FilmInfo>>() {
                            }.getType());
                            filmAdapter.appendToList(filmInfos);
                        }
                    });

                }

                @Override
                public void onFailure() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            ToastUtil.showLong(mContext, "数据获取失败！");
                        }
                    });
                }
            });
            ossGetObjectData.asyncGetObjectSample();
        }

    }

    @Override
    protected void setUpView() {
        filmAdapter = new FilmAdapter(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.setAdapter(filmAdapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

    }


}
