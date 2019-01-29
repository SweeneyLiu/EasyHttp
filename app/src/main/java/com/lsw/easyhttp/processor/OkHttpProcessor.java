package com.lsw.easyhttp.processor;

import com.lsw.easyhttp.interfaces.ICallBack;
import com.lsw.easyhttp.interfaces.IhttpProcessor;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

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
        okhttp3.OkHttpClient.Builder ClientBuilder=new okhttp3.OkHttpClient.Builder();
        ClientBuilder.readTimeout(20, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(6, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(60, TimeUnit.SECONDS);//写入超时
        //todo 支持HTTPS请求，跳过证书验证
        mOkHttpClient=ClientBuilder.build();
    }

    /**
     * get请求，同步方式，获取网络数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     * @param url
     * @return
     */
    public  Response getDataSynFromNet(String url) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        Request request=builder.get().url(url).build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * get请求，异步方式，获取网络数据，是在子线程中执行的，需要切换到主线程才能更新UI
     * @param url
     * @param callback
     * @return
     */
    public  void getDataAsynFromNet(String url, final ICallBack callback) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        Request request=builder.get().url(url).build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                myNetCall.failed(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                myNetCall.success(call,response);
            }
        });
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

    /**
     * post请求，同步方式，提交数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     * @param url
     * @param bodyParams
     * @return
     */
    public Response postDataSynToNet(String url, Map<String, String> bodyParams) {
        //1构造RequestBody
        RequestBody body = setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * post请求，异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     * @param url
     * @param bodyParams
     * @param callback
     */
    public  void postDataAsynToNet(String url, Map<String,String> bodyParams, final ICallBack callback) {
        //1构造RequestBody
        RequestBody body=setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                myNetCall.failed(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                myNetCall.success(call,response);

            }
        });
    }

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

    /**
     * post的请求参数，构造RequestBody
     * @param BodyParams
     * @return
     */
    private RequestBody setRequestBody(Map<String, String> BodyParams){
        RequestBody body=null;
        okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
        if(BodyParams != null){
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
            }
        }
        body=formEncodingBuilder.build();
        return body;

    }
}
