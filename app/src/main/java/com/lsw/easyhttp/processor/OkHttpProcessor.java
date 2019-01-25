package com.lsw.easyhttp.processor;

import com.lsw.easyhttp.interfaces.ICallBack;
import com.lsw.easyhttp.interfaces.IhttpProcessor;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sweeneyliu on 2019/1/4.
 */
public class OkHttpProcessor implements IhttpProcessor{

    private static OkHttpClient mOkHttpClient;

    public OkHttpProcessor() {
        mOkHttpClient = new OkHttpClient();
    }

    //get异步调用
    @Override
    public void get(String url, Map<String, Object> params, final ICallBack callback) {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed(call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body().toString());
                    } else {
                        callback.onFailed(response.message());
                    }
                }
            }
        });
    }

    //todo get同步调用

    @Override
    public void post(String url, Map<String, Object> params, final ICallBack callback) {
        RequestBody requestBody = appendBody(params);

        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed(e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response != null) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body().toString());
                    } else {
                        callback.onFailed(response.message());
                    }
                }
            }
        });
    }

    //传入参数，返回添加头信息
    private RequestBody appendBody( Map<String, Object> params){
        FormBody.Builder body = new FormBody.Builder();
        if(params == null || params.isEmpty()){
            return body.build();
        }
        for(Map.Entry<String, Object> entry : params.entrySet()){
            body.add(entry.getKey(),entry.getValue().toString());
        }
        return body.build();
    }
}
