package com.filmresource.cn;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.filmresource.cn.OssData.OssGetObjectData;
import com.filmresource.cn.OssData.OssResultListener;
import com.filmresource.cn.activity.BaseActivity;
import com.filmresource.cn.adapter.FilmAdapter;
import com.filmresource.cn.bean.BtHomePageInfo;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.bean.MovieClassify;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.widget.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.filmresource.cn.bean.BtHomePageInfo.BTHOMEPAGE_FILMINFOLIST;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OssResultListener {

    private OSS oss;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private FilmAdapter filmAdapter;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    //@Bind(R.id.shimmer_layout)
    //ShimmerFrameLayout shimmerFrameLayout;

    private void initAliOss() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.accessKeyId, Constant.accessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();

        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), Constant.endpoint, credentialProvider, conf);


    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
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

//        recyclerView.addItemDecoration(new MarginDecoration(this));

        filmAdapter = new FilmAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(filmAdapter);
        initAliOss();

        OssGetObjectData getObjectSamples = new OssGetObjectData(oss, "bttiantang", "homePage", this);
        getObjectSamples.asyncGetObjectSample();


//        Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/fresco-logo.png");
//        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
//        draweeView.setImageURI(uri);


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
        // Inflate the menu; this adds items to the action bar if it is present.
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
                for (MovieClassify movieClassify:movieClassifies)
                {
                    tabLayout.addTab(tabLayout.newTab().setText(Html.fromHtml(movieClassify.getClassify())));
                }

                List<FilmInfo> filmInfos = btHomePageInfo.getFilmMapList().get(BTHOMEPAGE_FILMINFOLIST);
                filmAdapter.appendToList(filmInfos);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

//        boolean isPlaying = shimmerFrameLayout.isAnimationStarted();
//        // Reset all parameters of the shimmer animation
//        shimmerFrameLayout.useDefaults();
//        shimmerFrameLayout.setDuration(5000);
//        shimmerFrameLayout.setRepeatMode(ObjectAnimator.REVERSE);
//        if (isPlaying) {
//            shimmerFrameLayout.startShimmerAnimation();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // shimmerFrameLayout.stopShimmerAnimation();
    }
}
