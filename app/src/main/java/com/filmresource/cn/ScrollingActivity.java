package com.filmresource.cn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.filmresource.cn.activity.NetBaseActivity;
import com.filmresource.cn.adapter.GalleryAdapter;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.global.BaseApplication;
import com.filmresource.cn.htmlparser.bttiantang.HtmlParseFromBttt;
import com.filmresource.cn.ui.FilmActivity.SnifferActivity;
import com.filmresource.cn.utils.LogUtil;
import com.filmresource.cn.utils.MD5Util;
import com.filmresource.cn.utils.SPUtils;
import com.filmresource.cn.utils.StringUtils;
import com.filmresource.cn.utils.ToastUtil;
import com.filmresource.cn.widget.WrappingLinearLayoutManager;

import java.util.List;

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
    @Bind(R.id.filmSynopsis)
    TextView filmSynopsis;
    @Bind(R.id.detail_fav_iv)
    ImageView detailFav;
    @Bind(R.id.detail_share_layout)
    LinearLayout shareLayout;
    @Bind(R.id.filmScore)
    TextView filmScore;

    @Bind(R.id.top_image)
    ImageView topImage;

    @Bind(R.id.detail_image_view)
    SimpleDraweeView simpleDraweeView;
    private Object asynOssData;
    private HtmlParseFromBttt htmlParseFromBttt;

    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @Bind(R.id.rv_film_imags)
    RecyclerView rv_film_imags;

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
        if(filmInfo!=null && !StringUtils.isEmpty(filmInfo.getFilmName()))
        {
            toolbarLayout.setTitle(filmInfo.getFilmName());
        }
        else
        {
            toolbarLayout.setTitle("");
        }
        //通过CollapsingToolbarLayout修改字体颜色
        toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.view_normal));//设置还没收缩时状态下字体颜色
        toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
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

            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(filmInfo.getFilmPoster()))
                    .setProgressiveRenderingEnabled(true)
                                    .build();

            ImagePipeline imagePipeline = Fresco.getImagePipeline();

            DataSource<CloseableReference<CloseableImage>>
                    dataSource = imagePipeline.fetchDecodedImage(imageRequest, ScrollingActivity.this);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                     @Override
                                     public void onNewResultImpl(final Bitmap bitmap) {

                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 Bitmap bm = NativeStackBlur.process(bitmap, 2);
                                                 topImage.setImageBitmap(bm);
                                             }
                                         });
                                     }

                                     @Override
                                     public void onFailureImpl(DataSource dataSource) {
                                         // No cleanup required here.
                                     }
                                 },
                    CallerThreadExecutor.getInstance());
//            topImage.setImageResource(R.drawable.film_title_bg);


//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) simpleDraweeView.getLayoutParams();
//            TypedValue tv = new TypedValue();
//            int actionBarHeight = 0;
//            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
//                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
//            }
//            actionBarHeight = actionBarHeight + DensityUtils.dp2px(this, 8);
//            layoutParams.setMargins(DensityUtils.dp2px(this,20),actionBarHeight,0,0);
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

            filmClassify.setText("类型：" + filmInfo.getFilmClassify().toString());
            filmZone.setText("地区："+filmInfo.getFilmZone());
            filmScreensTime.setText("时间："+filmInfo.getFilmScreensTime());

            filmDirector.setText(""+filmInfo.getFilmDirector());
            filmScreenWriter.setText(""+filmInfo.getFilmScreenWriter());
            filmStarred.setText(""+filmInfo.getFilmStarred().toString());
            filmSynopsis.setText(""+filmInfo.getFilmSynopsis());
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

            if(filmInfo.getFilmImages()!=null&&filmInfo.getFilmImages().size()>0)
            {
                initMaterialLeanBackView(filmInfo.getFilmImages());
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


    public void initMaterialLeanBackView(List<String> filmImages)
    {
        WrappingLinearLayoutManager linearLayoutManager = new WrappingLinearLayoutManager(this);
        linearLayoutManager.setOrientation(WrappingLinearLayoutManager.HORIZONTAL);
        rv_film_imags.setLayoutManager(linearLayoutManager);
        rv_film_imags.setNestedScrollingEnabled(false);
        rv_film_imags.setHasFixedSize(false);
        GalleryAdapter  mAdapter = new GalleryAdapter(this);
        rv_film_imags.setAdapter(mAdapter);
        mAdapter.appendToList(filmImages);
    }

}
