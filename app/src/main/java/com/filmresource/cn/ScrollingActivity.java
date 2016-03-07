package com.filmresource.cn;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.filmresource.cn.OssData.OssResultListenerX;
import com.filmresource.cn.activity.BaseActivity;
import com.filmresource.cn.activity.NetBaseActivity;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.global.BaseApplication;
import com.filmresource.cn.htmlparser.bttiantang.HtmlParseFromBttt;
import com.filmresource.cn.utils.DensityUtils;
import com.filmresource.cn.utils.FileUtils;
import com.filmresource.cn.utils.LogUtil;
import com.filmresource.cn.utils.MD5Util;
import com.filmresource.cn.utils.SDCardUtils;
import com.filmresource.cn.utils.ToastUtil;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import butterknife.Bind;

public class ScrollingActivity extends NetBaseActivity  {

    private  FilmInfo filmInfo;
    @Bind(R.id.filmName)
    TextView filmName;
    @Bind(R.id.filmClassify)
    TextView filmClassify;
    @Bind(R.id.filmZone)
    TextView filmZone;
    @Bind(R.id.filmScreensTime)
    TextView filmScreensTime;
    @Bind(R.id.filmDirector)
    TextView filmDirector;
    @Bind(R.id.filmScreenWriter)
    TextView filmScreenWriter;
    @Bind(R.id.filmStarred)
    TextView filmStarred;
    @Bind(R.id.downloadlayout)
    LinearLayout downloadLayout;

    @Bind(R.id.my_image_view)
    SimpleDraweeView simpleDraweeView;
    private Object asynOssData;
    private HtmlParseFromBttt htmlParseFromBttt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
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
        initData();
        getAsynOssData();
    }

    public  void initData()
    {
        try {
           Bundle bundle =  getIntent().getExtras();
           filmInfo = (FilmInfo) bundle.getSerializable("filminfo");
            filmName.setText(filmInfo.getFilmName());

            simpleDraweeView.setImageURI(Uri.parse(filmInfo.getFilmPoster()));
        }catch (Exception e)
        {

        }
    }


    public void getAsynOssData() {
        if(BaseApplication.oss!=null&&filmInfo!=null)
        {
            boolean isExist = false;
            final String md5href = MD5Util.string2MD5(filmInfo.getFimHref());
            try {
                isExist = BaseApplication.oss.doesObjectExist(Constant.bucket_detail,md5href);
                if(isExist)
                {
                    LogUtil.e("info","aliyun request");
                    asyncGetObject(R.id.request_bttiantang_detail, Constant.bucket_detail, md5href);
                }
                else
                {
                    LogUtil.e("info","bttt request");
                    new Thread()
                    {
                        @Override
                        public void run() {
                            // 爬取数据，并保存
                            HtmlParseFromBttt htmlParseFromBttt = new HtmlParseFromBttt();
                            htmlParseFromBttt.getHtmlResourceContent(filmInfo, filmInfo.getFimHref());
                            PutObjectRequest putObjectRequest = new PutObjectRequest(
                                    Constant.bucket_detail,md5href, gson.toJson(filmInfo).getBytes());
                            try {
                                BaseApplication.oss.putObject(putObjectRequest);
                            } catch (ClientException e) {
                                e.printStackTrace();
                            } catch (ServiceException e) {
                                e.printStackTrace();
                            }finally {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setViewData(filmInfo);
                                    }
                                });
                            }
                        }
                    }.start();
                }
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }catch (Exception e)
            {

            }

        }
    }

    @Override
    public void onOssSuccess(Object response, int actionId) {
        super.onOssSuccess(response, actionId);
        switch (actionId)
        {
            case R.id.request_bttiantang_detail:
                if(response!=null)
                {
                    filmInfo =  gson.fromJson(String.valueOf(response),FilmInfo.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setViewData(filmInfo);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onOssError(OSSRequest ossRequest, ClientException e, ServiceException e1) {
        super.onOssError(ossRequest, e, e1);
    }

    public void setViewData(FilmInfo filmInfo)
    {
        if(filmInfo!=null)
        {
            filmName.setText(filmInfo.getFilmName());
            simpleDraweeView.setImageURI(Uri.parse(filmInfo.getFilmPoster()));
            filmClassify.setText(filmInfo.getFilmClassify().toString());
            filmZone.setText(filmInfo.getFilmZone());
            filmScreensTime.setText(filmInfo.getFilmScreensTime());
            filmDirector.setText(filmInfo.getFilmDirector());
            filmScreenWriter.setText(filmInfo.getFilmScreenWriter());
            filmStarred.setText(filmInfo.getFilmStarred().toString());

            if(filmInfo.getTorrentDownloadList()!=null&&filmInfo.getTorrentDownloadList().size()>0)
            {
                downloadLayout.removeAllViews();
                for (final String torrentName:filmInfo.getTorrentDownloadList().keySet())
                {
                    TextView textView = new TextView(this);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(htmlParseFromBttt ==null)
                            {
                                htmlParseFromBttt = new HtmlParseFromBttt();
                            }
                            String durl = (String) v.getTag();
                            int starIndex = durl.indexOf("id=");
                            durl = durl.substring(starIndex);
                            int endIndex = durl.indexOf("&");
                            final String id = durl.substring("id=".length(), endIndex);
                            starIndex = durl.indexOf("uhash=");
                            final String uhash = durl.substring(starIndex+"uhash=".length(), durl.length());
                            final String path = Constant.storagePath+File.separator+torrentName.trim().toString();
                            if(FileUtils.isFolderExist(path))
                            {
                                ToastUtil.showLong(ScrollingActivity.this,"文件已下载！");
                                return;
                            }

                            if(SDCardUtils.isSDCardEnable())
                            {
                                new Thread()
                                {
                                    @Override
                                    public void run() {

                                        FileUtils.makeDirs(path);
                                        htmlParseFromBttt.downloadtTorrent(Constant.bttiantang_downloadurl, id, uhash, path);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showLong(ScrollingActivity.this,"下载成功！");
                                            }
                                        });
                                    }
                                }.start();
                                }
                            else
                            {
                                ToastUtil.showLong(ScrollingActivity.this,"磁盘存储不能使用！");
                            }
                        }
                    });
                    int padding = DensityUtils.dp2px(this, 10);
                    textView.setText(torrentName);
                    textView.setTag(filmInfo.getTorrentDownloadList().get(torrentName));
                    textView.setPadding(padding, padding, padding, padding);
                    downloadLayout.addView(textView);
                }
            }

        }
    }
}
