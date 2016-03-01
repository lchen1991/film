package com.filmresource.cn;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.filmresource.cn.OssData.OssGetObjectData;
import com.filmresource.cn.OssData.OssResultListener;
import com.filmresource.cn.adapter.base.FilmAdapter;
import com.filmresource.cn.bean.BtHomePageInfo;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.common.Constant;
import com.google.gson.Gson;

import java.util.List;

import static com.filmresource.cn.bean.BtHomePageInfo.BTHOMEPAGE_FILMINFOLIST;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OssResultListener {

    private OSS oss;

    private  GridView gridView ;
    private  FilmAdapter filmAdapter;


     private void initAliOss()
     {
         OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.accessKeyId, Constant.accessKeySecret);

         ClientConfiguration conf = new ClientConfiguration();

         conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
         conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
         conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
         conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
         OSSLog.enableLog();
         oss = new OSSClient(getApplicationContext(), Constant.endpoint, credentialProvider, conf);

     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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


        gridView = (GridView) findViewById(R.id.gv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridView.setNestedScrollingEnabled(true);
        }
        filmAdapter = new FilmAdapter(this);
        gridView.setAdapter(filmAdapter);
        initAliOss();

        OssGetObjectData getObjectSamples = new OssGetObjectData(oss,"bttiantang","homePage",this);
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
                List<FilmInfo> filmInfos =btHomePageInfo.getFilmMapList().get(BTHOMEPAGE_FILMINFOLIST);
                filmAdapter.appendToList(filmInfos);
            }
        });
    }




}
