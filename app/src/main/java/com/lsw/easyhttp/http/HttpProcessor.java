package com.lsw.easyhttp.http;

import com.lsw.easyhttp.interfaces.ICallBack;
import com.lsw.easyhttp.interfaces.IProcessor;

import java.util.Map;

/**
 * Created by sweeneyliu on 2019/1/4.
 */
public class HttpProcessor implements IProcessor {

    private static volatile HttpProcessor instance = null;
    private static IProcessor mIProcessor;

    private HttpProcessor() {
    }

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

    public void init(IProcessor httpProcessor){
        mIProcessor = httpProcessor;
    }

    @Override
    public void get(String url, Map<String, Object> params, ICallBack callback) {
        mIProcessor.get(url,params,callback);
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallBack callback) {
        mIProcessor.post(url,params,callback);
    }
}
