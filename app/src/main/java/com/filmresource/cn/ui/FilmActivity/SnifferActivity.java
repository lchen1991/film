package com.filmresource.cn.ui.FilmActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.cocosw.bottomsheet.BottomSheet;
import com.filmresource.cn.R;
import com.filmresource.cn.activity.WebViewBaseActivity;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.htmlparser.bttiantang.HtmlParseFromBttt;
import com.filmresource.cn.utils.FileUtils;
import com.filmresource.cn.utils.SDCardUtils;
import com.filmresource.cn.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ql on 2016/3/8.
 */
public class SnifferActivity extends WebViewBaseActivity {

    private String title = "";
    private FilmInfo filmInfo = null;
    private LinkedHashMap<String,String> torrentDownloadList = null;
    private Handler handler = new Handler();
    private File downfile ;
    private boolean isPageFinished = false;

    @Bind(R.id.sniffer_go)
    ImageView sniffer_go ;
    private HtmlParseFromBttt htmlParseFromBttt;

    @OnClick({R.id.sniffer_go})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.sniffer_go:
                if(!isPageFinished)
                {
                    ToastUtil.showLong(SnifferActivity.this,"页面加载中...");
                    return;
                }
                postSnifferGo();
                break;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.common_back_icon_selector);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnifferActivity.this.finish();
            }
        });

        Intent intent = getIntent();
        if(intent!=null)
        {
            Bundle bundle = intent.getExtras();
            if(bundle!=null)
            {
                filmInfo = (FilmInfo) bundle.getSerializable("filmInfo");
                title = filmInfo.getFilmName();
                url = Constant.baiduurl + title;
            }
        }

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                isPageFinished = true;
                super.onPageFinished(view, url);
            }
        });
    }

    public void postSnifferGo()
    {
        showLoadProgressDialog("嗅探中...");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissLoadProgressDialog();
                BottomSheet.Builder sheetdown = new BottomSheet.Builder(SnifferActivity.this);
                final List<String> keys = new ArrayList<String>();
                int i = 0;
                for (String torrent : filmInfo.getTorrentDownloadList().keySet()) {
                    sheetdown.sheet(i++, getResources().getDrawable(R.drawable.bt_download_manager_bt_icon), torrent);
                    keys.add(torrent);
                }
                sheetdown.listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String key = keys.get(which);
                        String torrentAdds = filmInfo.getTorrentDownloadList().get(key);

                        if (htmlParseFromBttt == null) {
                            htmlParseFromBttt = new HtmlParseFromBttt();
                        }
                        String durl = torrentAdds;
                        int starIndex = durl.indexOf("id=");
                        durl = durl.substring(starIndex);
                        int endIndex = durl.indexOf("&");
                        final String id = durl.substring("id=".length(), endIndex);
                        starIndex = durl.indexOf("uhash=");
                        final String uhash = durl.substring(starIndex + "uhash=".length(), durl.length());
                        final String path = Constant.storagePath + File.separator + key.trim().toString();

                        if (SDCardUtils.isSDCardEnable()) {
                            showLoadProgressDialog("下载中...");
                            new Thread() {
                                @Override
                                public void run() {

                                    FileUtils.makeDirs(path);
                                    downfile = htmlParseFromBttt.downloadtTorrent(SnifferActivity.this, Constant.bttiantang_downloadurl, id, uhash, path);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //跳转
                                            ToastUtil.showShort(SnifferActivity.this, "下载成功！");
                                            openFile(downfile);
                                            dismissLoadProgressDialog();
                                        }
                                    });
                                }
                            }.start();
                        } else {
                            ToastUtil.showLong(SnifferActivity.this, "磁盘存储不能使用！");
                        }

                    }
                }).show();
            }
        }, 1500);
    }

    private void openFile(File file) {
        try{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setDataAndType(Uri.fromFile(file), "application/x-bittorrent");
            intent.setComponent(new ComponentName("android", "com.android.internal.app.ResolverActivity"));
            startActivity(intent);
        }catch(Exception e)
        {
            ToastUtil.showLong(SnifferActivity.this, "您还没有安装迅雷！");
        }
    }

}
