package com.lsw.easyhttp;

import android.app.Application;

import com.lsw.easyhttp.http.HttpProcessor;
import com.lsw.easyhttp.processor.OkHttpProcessor;
import com.lsw.easyhttp.processor.VolleyProcessor;


public class EasyHttpApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Volley方式网络请求代理
//        HttpProcessor.getInstance().init(new VolleyProcessor(this));

        //初始化Okhttp方式网络请求代理
        HttpProcessor.getInstance().init(new OkHttpProcessor());
    }


}
