package com.filmresource.cn.ui.FilmActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.cocosw.bottomsheet.BottomSheet;
import com.filmresource.cn.R;
import com.filmresource.cn.activity.BaseActivity;
import com.filmresource.cn.activity.WebViewBaseActivity;
import com.filmresource.cn.bean.FilmInfo;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.utils.ArrayUtils;
import com.filmresource.cn.utils.StringUtils;
import com.filmresource.cn.utils.ToastUtil;
import com.filmresource.cn.widget.CustomDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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
    private CustomDialog customDialog ;
    @Bind(R.id.sniffer_go)
    ImageView sniffer_go ;

    @OnClick({R.id.sniffer_go})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.sniffer_go:
                postSnifferGo();
                break;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.common_back_icon_selector);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Sub");
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


        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url) {
                //postSnifferGo();
                super.onPageFinished(view, url);
            }
        });
    }

    public void postSnifferGo()
    {
        if(customDialog == null)
        {
            customDialog = CustomDialog.createLoadProgressDialog(SnifferActivity.this);
        }
        customDialog.setMessage("嗅探中...");
        customDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                customDialog.dismiss();
                BottomSheet.Builder sheetdown = new BottomSheet.Builder(SnifferActivity.this);
                final List<String> keys = new ArrayList<String>();
                int i = 0;
                for (String torrent : filmInfo.getTorrentDownloadList().keySet()) {
                    sheetdown.sheet(i,getResources().getDrawable(R.drawable.bt_download_manager_bt_icon),torrent);
                    keys.add(torrent);
                }
                sheetdown.listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String key = keys.get(which);
                        String torrentAdds = filmInfo.getTorrentDownloadList().get(key);
                        if (!StringUtils.isEmpty(torrentAdds)) {
                            ToastUtil.showLong(SnifferActivity.this, "当前种子：" + torrentAdds);
                        }
                    }
                }).show();
            }
        }, 1500);
    }
}
