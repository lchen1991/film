package com.filmresource.cn.OssData;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.filmresource.cn.net.manager.RequestManager.RequestListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chenlei on 16/3/5.
 */
public class OssLoadControler implements OSSCompletedCallback<GetObjectRequest, GetObjectResult> {

    private OssResultListenerX ossResultListenerX;

    private int mAction = 0;

    public OssLoadControler(int mActionId,OssResultListenerX ossResultListenerX)
    {
        this.mAction = mActionId;
        this.ossResultListenerX = ossResultListenerX;
    }


    @Override
    public void onSuccess(GetObjectRequest getObjectRequest, GetObjectResult getObjectResult) {
        // 请求成功
        InputStream inputStream = getObjectResult.getObjectContent();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                sb.append(line);
            }
             reader.close();
            ossResultListenerX.onOssSuccess(sb.toString(), mAction);
            Log.d("asyncGetObjectSample", "download success.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            ossResultListenerX.onOssFinish(mAction);
        }

    }

    @Override
    public void onFailure(GetObjectRequest getObjectRequest, ClientException e, ServiceException e1) {
        ossResultListenerX.onOssError(getObjectRequest,e,e1);
        ossResultListenerX.onOssFinish(mAction);
    }

}
