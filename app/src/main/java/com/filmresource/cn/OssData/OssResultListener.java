package com.filmresource.cn.OssData;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;

public  interface  OssResultListener {
        void onResult(String s);
        void onFailure(ClientException clientExcepion, ServiceException serviceException);
    }