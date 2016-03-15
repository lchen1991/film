package com.filmresource.cn.ui.FilmActivity;

import android.os.Bundle;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.filmresource.cn.OssData.OssResultListener;
import com.filmresource.cn.R;
import com.filmresource.cn.activity.NetBaseActivity;

/**
 * Created by chenlei on 16/3/13.
 */
public class FilmLikeActivity extends NetBaseActivity implements OssResultListener {


//    private CardSlidePanel.CardSwitchListener cardSwitchListener;
//    @Bind(R.id.image_slide_panel)
//    CardSlidePanel slidePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_layout);
        initView();
    }



    private void initView() {
//        cardSwitchListener = new CardSlidePanel.CardSwitchListener() {
//
//            @Override
//            public void onShow(int index) {
//            }
//
//            @Override
//            public void onCardVanish(int index, int type) {
//            }
//        };
//        slidePanel.setCardSwitchListener(cardSwitchListener);
//
//
//        OssGetObjectData getObjectSamples = new OssGetObjectData(BaseApplication.getInstance().oss, Constant.bucket, Constant.bucketObj, this);
//        getObjectSamples.asyncGetObjectSample();
//        showLoadProgressDialog();

    }

    @Override
    public void onResult(String s) {

//        Gson gson = new Gson();
//        final BtHomePageInfo btHomePageInfo = gson.fromJson(s, BtHomePageInfo.class);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                List<FilmInfo> filmInfos = btHomePageInfo.getFilmMapList().get(BtHomePageInfo.BTHOMEPAGE_FILMINFOLIST_HOT);
//                if(filmInfos!=null)
//                {
//                    slidePanel.fillData(filmInfos);
//                }
//                dismissLoadProgressDialog();
//            }
//        });

    }

    @Override
    public void onFailure(ClientException clientExcepion, ServiceException serviceException) {

    }
}
