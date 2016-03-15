package com.filmresource.cn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.cocosw.bottomsheet.BottomSheet;
import com.commit451.nativestackblur.NativeStackBlur;
import com.facebook.drawee.view.SimpleDraweeView;
import com.filmresource.cn.activity.NetBaseActivity;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.global.BaseApplication;
import com.filmresource.cn.htmlparser.bttiantang.HtmlParseFromBttt;
import com.filmresource.cn.ui.FilmActivity.SnifferActivity;
import com.filmresource.cn.utils.DensityUtils;
import com.filmresource.cn.utils.LogUtil;
import com.filmresource.cn.utils.MD5Util;
import com.filmresource.cn.utils.SPUtils;
import com.filmresource.cn.utils.StringUtils;
import com.filmresource.cn.utils.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;

public class ScrollingActivity extends NetBaseActivity  {

    public static final String FILM_FAVORITES = "FILM_FAVORITES";
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
    @Bind(R.id.detail_fav_iv)
    ImageView detailFav;
    @Bind(R.id.detail_share_layout)
    LinearLayout shareLayout;
    @Bind(R.id.filmScore)
    TextView filmScore;

    @Bind(R.id.top_image)
    ImageView topImage;

//    @Bind(R.id.top_layout)
//    LinearLayout topLayout;
    @Bind(R.id.my_image_view)
    SimpleDraweeView simpleDraweeView;
    private Object asynOssData;
    private HtmlParseFromBttt htmlParseFromBttt;

    @OnClick({R.id.detail_share_layout,R.id.detail_fav_layout,R.id.sniffer_go})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.detail_share_layout:
                new BottomSheet.Builder(this).grid().sheet(R.menu.share_group).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.qq:
                                //q.toast("Help me!");
                                break;
                        }
                    }
                }).show();

                break;
            case R.id.detail_fav_layout:
                if(!StringUtils.isEmpty(filmInfo.getFimHref())) {
                    String key = FILM_FAVORITES + filmInfo.getFimHref();
                    boolean favorites = (boolean)SPUtils.get(this, key,false);
                    if(favorites)
                    {
                        detailFav.setImageResource(R.drawable.relax_item_click_fav_normal);
                        SPUtils.put(this,key,false);
                    }else
                    {
                        detailFav.setImageResource(R.drawable.relax_item_click_fav_selected);
                        SPUtils.put(this, key, true);
                    }
                }else
                {
                    ToastUtil.showLong(this,"收藏失败！");
                }
                break;
            case R.id.sniffer_go:
                Intent intent = new Intent(this, SnifferActivity.class);
                intent.putExtra("filmInfo", filmInfo);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //com.mtime.util.APIStac
        setContentView(R.layout.activity_scrolling);
        initData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.common_back_icon_selector);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollingActivity.this.finish();
            }
        });

        getAsynOssData();
    }

    static Bitmap drawableToBitmap(Drawable drawable,int width,int height) // drawable 转换成bitmap
    {
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);// 把drawable内容画到画布中
        return bitmap;
    }

    public  void initData()
    {
        try {
           Bundle bundle =  getIntent().getExtras();
           filmInfo = (FilmInfo) bundle.getSerializable("filminfo");
            filmName.setText(filmInfo.getFilmName());
            simpleDraweeView.setImageURI(Uri.parse(filmInfo.getFilmPoster()));
            Drawable drawable = simpleDraweeView.getDrawable();
            Bitmap srbm =  drawableToBitmap(drawable,Constant.screenW , Constant.screenH/3);
            Bitmap bm = NativeStackBlur.process(srbm, 60);
            topImage.setImageBitmap(bm);

            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) simpleDraweeView.getLayoutParams();
            TypedValue tv = new TypedValue();
            int actionBarHeight = 0;
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
            actionBarHeight = actionBarHeight + DensityUtils.dp2px(this, 8);
            layoutParams.setMargins(DensityUtils.dp2px(this,20),actionBarHeight,0,0);
//            simpleDraweeView.getLayoutParams().
            if(!StringUtils.isEmpty(filmInfo.getFimHref()))
            {
                String key = FILM_FAVORITES + filmInfo.getFimHref();
                boolean favorites = (boolean)SPUtils.get(this,key,false);
                if(favorites)
                {
                    detailFav.setImageResource(R.drawable.relax_item_click_fav_selected);
                }
                else
                {
                    detailFav.setImageResource(R.drawable.relax_item_click_fav_normal);
                }
            }
        }catch (Exception e)
        {
            LogUtil.e("info",e.getMessage());
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
                    asyncGetObject(R.id.request_bttiantang_detail, Constant.bucket_detail, md5href,true);
                }
                else
                {
                    showLoadProgressDialog();
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
                                        dismissLoadProgressDialog();
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
            String score = filmInfo.getFilmScore().trim();
            if(!StringUtils.isEmpty(score))
            {
                int tags = score.indexOf("：");
                if(tags > -1)
                {
                    String[] scores = score.split("：");
                    if(score.length() > 2)
                    {
                        filmInfo.setFilmScore(scores[1]);
                    }
                }
            }
            filmScore.setText(filmInfo.getFilmScore());
            if(filmInfo.getTorrentDownloadList()!=null&&filmInfo.getTorrentDownloadList().size()>0)
            {
                downloadLayout.removeAllViews();
                for (final String torrentName:filmInfo.getTorrentDownloadList().keySet())
                {
                    TextView textView = new TextView(this);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            if(htmlParseFromBttt ==null)
//                            {
//                                htmlParseFromBttt = new HtmlParseFromBttt();
//                            }
//                            String durl = (String) v.getTag();
//                            int starIndex = durl.indexOf("id=");
//                            durl = durl.substring(starIndex);
//                            int endIndex = durl.indexOf("&");
//                            final String id = durl.substring("id=".length(), endIndex);
//                            starIndex = durl.indexOf("uhash=");
//                            final String uhash = durl.substring(starIndex+"uhash=".length(), durl.length());
//                            final String path = Constant.storagePath+File.separator+torrentName.trim().toString();
//                            if(FileUtils.isFolderExist(path))
//                            {
//                                ToastUtil.showLong(ScrollingActivity.this,"文件已下载！");
//                                return;
//                            }
//
//                            if(SDCardUtils.isSDCardEnable())
//                            {
//                                new Thread()
//                                {
//                                    @Override
//                                    public void run() {
//
//                                        FileUtils.makeDirs(path);
//                                        htmlParseFromBttt.downloadtTorrent(Constant.bttiantang_downloadurl, id, uhash, path);
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                ToastUtil.showLong(ScrollingActivity.this,"下载成功！");
//                                            }
//                                        });
//                                    }
//                                }.start();
//                                }
//                            else
//                            {
//                                ToastUtil.showLong(ScrollingActivity.this,"磁盘存储不能使用！");
//                            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_search:
                Intent intent = new Intent(this, SnifferActivity.class);
                intent.putExtra("filmInfo", filmInfo);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
