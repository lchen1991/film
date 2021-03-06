package com.filmresource.cn.ui.FilmFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.filmresource.cn.OssData.OssGetObjectData;
import com.filmresource.cn.OssData.OssResultListener;
import com.filmresource.cn.R;
import com.filmresource.cn.ScrollingActivity;
import com.filmresource.cn.adapter.FilmListAdapter;
import com.filmresource.cn.adapter.base.SpacesItemDecoration;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.fragment.LazyFragment;
import com.filmresource.cn.global.BaseApplication;
import com.filmresource.cn.imageloader.fresco.instrumentation.PerfListener;
import com.filmresource.cn.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;

/**
 * Created by ql on 2016/3/3.
 */
public  class FilmListFragment extends LazyFragment {

    private FilmListAdapter filmAdapter = null;
    private PerfListener perfListener;
    @Bind(R.id.swipe_layout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private Gson gson = new Gson();
    private List<FilmInfo> filmInfos = null;
    private Handler handler = new Handler();
    private  OssGetObjectData ossGetObjectData;

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
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            String href =  bundle.getString("href");
            String mObjHref = Constant.bucketObj + "&" +href;
            ossGetObjectData = new OssGetObjectData(BaseApplication.getInstance().oss, Constant.bucket,mObjHref,new OssResultListener()
            {
                @Override
                public void onResult(final String data) {
                    LogUtil.e("info", data);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }, 600);
                            filmInfos = gson.fromJson(data, new TypeToken<List<FilmInfo>>() {
                            }.getType());
                            filmAdapter.appendToList(filmInfos);
                        }
                    });

                }

                @Override
                public void onFailure(ClientException clientExcepion, ServiceException serviceException) {
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 600);
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace();
                        Snackbar.make(mainView, "网络连接失败!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                    if (serviceException != null) {
                        // 服务异常
                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                        Snackbar.make(mainView, "未知异常!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                ossGetObjectData.asyncGetObjectSample();
            }
        });
    }

    @Override
    protected void setUpView() {
        perfListener = new PerfListener();

        filmAdapter = new FilmListAdapter(mContext,perfListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.setAdapter(filmAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ossGetObjectData.asyncGetObjectSample();
            }
        });
        filmAdapter.setOnRecyclerViewItemClick(new FilmListAdapter.onRecyclerViewItemClick() {
            @Override
            public void setOnItemClick(View v, FilmInfo filmInfo) {

                if(filmInfo == null ||TextUtils.isEmpty(filmInfo.getFimHref()))
                {
                    Snackbar.make(mainView, "资源不存在!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    Intent intent = new Intent(mContext,ScrollingActivity.class);
                    intent.putExtra("filminfo",filmInfo);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
//        filmAdapter.shutDown();
        super.onDestroyView();
    }
}
