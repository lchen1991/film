package com.filmresource.cn.ui.FilmActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.filmresource.cn.R;
import com.filmresource.cn.activity.NetBaseActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chenlei on 16/3/13.
 */
public class AuthActivity extends NetBaseActivity {

    @Bind(R.id.btn_qq_fast_login)
    RelativeLayout mQQLoginLayout;

    @Bind(R.id.btn_weixin_login)
    RelativeLayout mWeixinLoginLayout;

    @Bind(R.id.btn_sina_login)
    RelativeLayout mSinaLoginLayout;

    @OnClick({R.id.btn_qq_fast_login,R.id.btn_weixin_login,R.id.btn_sina_login,R.id.logout})
    public  void  onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.btn_qq_fast_login:
                break;
            case R.id.btn_weixin_login:
                if(mShareAPI!=null)
                {
                    mShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                }
                break;
            case R.id.btn_sina_login:
                break;
            case R.id.logout:
                AuthActivity.this.finish();
                break;
        }
    }

    UMShareAPI mShareAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("登录IFilm");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mShareAPI = UMShareAPI.get(this);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            Toast.makeText(getApplicationContext(), "Authorize succeed"+data.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
