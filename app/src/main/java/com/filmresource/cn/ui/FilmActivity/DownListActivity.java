package com.filmresource.cn.ui.FilmActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.filmresource.cn.R;
import com.filmresource.cn.activity.BaseActivity;
import com.filmresource.cn.adapter.base.TorrentListAdapter;
import com.filmresource.cn.bean.TorrentFileInfo;
import com.filmresource.cn.common.Constant;
import com.filmresource.cn.utils.DateUtil;
import com.filmresource.cn.utils.FileUtils;
import com.filmresource.cn.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * Created by ql on 2016/3/31.
 */
public class DownListActivity extends BaseActivity{

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 0:
                    Looper.prepare();
                    ToastUtil.showShort(DownListActivity.this, "文件不存在！");
                    Looper.loop();
                    break;
                case 1:
                    torrentListAdapter.appendToList(torrentFileInfos);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private  Thread mScanDownLoadTorrens;
    @Bind(R.id.downlist_recycler)
    RecyclerView mTorrensRecycler;
    private List<TorrentFileInfo> torrentFileInfos;
    private TorrentListAdapter torrentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("下载");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.common_back_icon_selector);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownListActivity.this.finish();
            }
        });
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        torrentFileInfos = new ArrayList<TorrentFileInfo>();
        torrentListAdapter = new TorrentListAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTorrensRecycler.setLayoutManager(linearLayoutManager);
        mTorrensRecycler.setAdapter(torrentListAdapter);

        mScanDownLoadTorrens = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!FileUtils.isFolderExist(Constant.storagePath))
                {
                    Message message = Message.obtain();
                    message.what = 0;
                    mHandler.dispatchMessage(message);
                }
                else {
                    File file = new File(Constant.storagePath);
                    if (file != null)
                    {
                        File[] subFiles = file.listFiles();
                        for (File sFile:subFiles)
                        {
                            TorrentFileInfo torrentFileInfo = new TorrentFileInfo();
                            torrentFileInfo.setFileName(sFile.getName());
                            torrentFileInfo.setFilePath(sFile.getAbsolutePath());
                            torrentFileInfo.setFileCreateTime(DateUtil.getFormatTime(new Date(sFile.lastModified())));
                            torrentFileInfos.add(torrentFileInfo);
                        }
                        Message message = Message.obtain();
                        message.what = 1;
                        mHandler.dispatchMessage(message);
                    }
                }
            }
        });

        mScanDownLoadTorrens.start();

    }
}
