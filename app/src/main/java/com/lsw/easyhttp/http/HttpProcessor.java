package com.lsw.easyhttp.http;

import com.lsw.easyhttp.interfaces.ICallBack;
import com.lsw.easyhttp.interfaces.IhttpProcessor;

import java.util.Map;

/**
 * Created by sweeneyliu on 2019/1/4.
 */
public class HttpProcessor implements IhttpProcessor{

    private static volatile HttpProcessor instance = null;
    private static IhttpProcessor mIhttpProcessor;

    public static HttpProcessor getInstance() {
        if(instance == null){
            synchronized (HttpProcessor.class){
                if(instance == null){
                    instance = new HttpProcessor();
                }
            }
        }
        return instance;
    }

    public void init(IhttpProcessor httpProcessor){
        mIhttpProcessor = httpProcessor;
    }

    @Override
    public void get(String url, Map<String, Object> params, ICallBack callback) {
        mIhttpProcessor.get(url,params,callback);
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallBack callback) {
        mIhttpProcessor.post(url,params,callback);
    }
}
