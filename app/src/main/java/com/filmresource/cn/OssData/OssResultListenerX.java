package com.filmresource.cn.OssData;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;

import java.util.Map;

/**
 * Created by chenlei on 16/3/5.
 */
public interface OssResultListenerX  {

    void onOssSuccess(Object response,int actionId);

    void onOssError(OSSRequest ossRequest, ClientException e, ServiceException e1);

     void onOssFinish(int actionId);
}
