package com.filmresource.cn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.filmresource.cn.OssData.OssGetObjectData;
import com.filmresource.cn.OssData.OssResultListener;
import com.filmresource.cn.activity.NetBaseActivity;
import com.filmresource.cn.adapter.FragmentTabAdapter;
import com.filmresource.cn.bean.BtHomePageInfo;
import com.filmresource.cn.bean.MovieClassify;
import com.filmresource.cn.bean.Trailer;
import com.filmresource.cn.bean.TrailerList;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.global.BaseApplication;
import com.filmresource.cn.net.manager.RequestManager;
import com.filmresource.cn.net.parser.ResponseDataToJSON;
import com.filmresource.cn.ui.FilmActivity.FilmLikeActivity;
import com.filmresource.cn.ui.FilmFragment.FilmListFragment;
import com.filmresource.cn.utils.LogUtil;
import com.filmresource.cn.utils.ToastUtil;
import com.filmresource.cn.widget.BlurNavigationDrawer.v7.BlurActionBarDrawerToggle;
import com.filmresource.cn.widget.dmsview.LoopGalleryAdapter;
import com.filmresource.cn.widget.dmsview.NavigationGallery;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


public class MainActivity extends NetBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OssResultListener {

    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.filmPage_main)
    ViewPager viewPager;
    private Context mContext;

    @Bind(R.id.product_ttd_vp)
     NavigationGallery dmsVp;
    private ActiveAdapter activeAdapter;

    @OnClick(R.id.fab)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Snackbar.make(view, "Replace with your own adapterction", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        BlurActionBarDrawerToggle toggle = new BlurActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        ((Button)findViewById(R.id.btn)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
//                startActivity(intent);
//            }
//        });

        dmsVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dmsVp.requestFocus();
                return false;
            }
        });

        OssGetObjectData getObjectSamples = new OssGetObjectData(BaseApplication.getInstance().oss, Constant.bucket, Constant.bucketObj, this);
        getObjectSamples.asyncGetObjectSample();
        showLoadProgressDialog();

        RequestManager.getInstance().setParser(new ResponseDataToJSON());
        addGetNetRequest("http://api.m.mtime.cn/PageSubArea/TrailerList.api",null,this, TrailerList.class,false,R.id.request_top_trailer);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera actio

        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, FilmLikeActivity.class));

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        Snackbar.make(drawer, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        return true;
    }

    @Override
    public void onResult(String s) {
        Gson gson = new Gson();
        final BtHomePageInfo btHomePageInfo = gson.fromJson(s, BtHomePageInfo.class);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                List<MovieClassify> movieClassifies = btHomePageInfo.getMovieClassifys();
                List<Fragment> fragments = new ArrayList<Fragment>();
                List<String> mClassifys = new ArrayList<String>();
                for (MovieClassify movieClassify : movieClassifies) {
                    tabLayout.addTab(tabLayout.newTab().setText(Html.fromHtml(movieClassify.getClassify())));
                    fragments.add(FilmListFragment.getInstance(movieClassify.getClassify()));
                    mClassifys.add(movieClassify.getClassify());
                    LogUtil.e("info", movieClassify.getClassify());
                }
                FragmentTabAdapter fragmentTabAdapter = new FragmentTabAdapter(getSupportFragmentManager(),
                        movieClassifies, fragments);
                viewPager.setAdapter(fragmentTabAdapter);
                tabLayout.setupWithViewPager(viewPager);
                dismissLoadProgressDialog();
            }
        });
    }

    @Override
    public void onFailure(ClientException clientExcepion, ServiceException serviceException) {
        // 请求异常
        if (clientExcepion != null) {
            // 本地异常如网络异常等
            clientExcepion.printStackTrace();
            Snackbar.make(viewPager, "网络连接失败!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }
        if (serviceException != null) {
            // 服务异常
            Log.e("ErrorCode", serviceException.getErrorCode());
            Log.e("RequestId", serviceException.getRequestId());
            Log.e("HostId", serviceException.getHostId());
            Log.e("RawMessage", serviceException.getRawMessage());
            Snackbar.make(viewPager, "未知异常!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    /** 广告位 */
    private void setActiveViewContext(List<Trailer> contents) {
        if (contents != null && contents.size() > 0) {
            activeAdapter = new ActiveAdapter(dmsVp, contents);
            dmsVp.setAdapter(activeAdapter);
            dmsVp.setHoldTime(5000);
            dmsVp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Trailer info = (Trailer) parent.getAdapter().getItem(position);
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String type = "video/* ";
                        Uri uri = Uri.parse(info.getUrl());
                        intent.setDataAndType(uri, type);
                        startActivity(intent);
                    }catch (Exception e)
                    {
                        ToastUtil.showLong(mContext, info.getVideoTitle());
                    }

                }

            });
            activeAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 轮播图适配器
     *
     * @author ql
     *
     */
    private class ActiveAdapter extends LoopGalleryAdapter {
        private String IMG_TAG = "img";
        private int realSize;
        private List<Trailer> contents = new ArrayList<Trailer>();
        private NavigationGallery navigationGallery;

        public ActiveAdapter(NavigationGallery navigationGallery,
                             List<Trailer> activeList) {
            super(navigationGallery);
            realSize = activeList.size();
            this.navigationGallery = navigationGallery;
            this.contents.addAll(activeList);
        }

        public int getRealCount() {
            return realSize;
        }

        @Override
        public Trailer getItem(int position) {
            position = getRealPosition(position);
            return contents.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.top_item_movie,null);
                holder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.my_image_view);
                holder.imageView.getLayoutParams().width = Constant.screenW;
               // holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.textView = (TextView)convertView.findViewById(R.id.item_movie_title);
                holder.textView.getLayoutParams().width = Constant.screenW;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            position = this.getRealPosition(position);
            final Trailer info = contents.get(position);
            holder.imageView.setTag(info.getCoverImg());
            holder.imageView.setImageURI(Uri.parse(info.getCoverImg()));
            holder.textView.setText(info.getMovieName());
            // 预加载后一张和最后一张
            int nextPosition = nextPosition(position);
            String nextUrl = contents.get(nextPosition).getCoverImg();

            if (nextUrl != null && !nextUrl.isEmpty()) {
                SimpleDraweeView image = (SimpleDraweeView) navigationGallery.findViewWithTag(nextUrl);
                if (image != null) {
                    image.setImageURI(Uri.parse(nextUrl));
                }
            }
            int lastPosition = lastPosition(position);
            String topUrl = contents.get(lastPosition).getCoverImg();
            if (topUrl != null && !topUrl.isEmpty()) {
                SimpleDraweeView image = (SimpleDraweeView) navigationGallery.findViewWithTag(lastPosition);
                if (image != null) {
                    System.out.println(topUrl);
                    image.setImageURI(Uri.parse(topUrl));
                }
            }
            return convertView;
        }

        class ViewHolder {
            SimpleDraweeView imageView;
            TextView textView;
        }
    }

    @Override
    public void onNetResponseSuccess(Object response, Map<String, String> headers, String url, int actionId) {
        switch (actionId)
        {
            case R.id.request_top_trailer:
                TrailerList trailerList = (TrailerList)response;
                List<Trailer> trailers = trailerList.getTrailers();
                List<Trailer> ts = new ArrayList<Trailer>();
                Collections.shuffle(trailers);
                int size = 6 ;
                for (int i = 0;i < size && i < trailers.size();i++)
                {
                    ts.add(trailers.get(i));
                }
                setActiveViewContext(ts);
            break;

        }
        super.onNetResponseSuccess(response, headers, url, actionId);
    }

    @Override
    public void onNetResponseError(String errorMsg, String url, int actionId) {
        super.onNetResponseError(errorMsg, url, actionId);
    }
}
