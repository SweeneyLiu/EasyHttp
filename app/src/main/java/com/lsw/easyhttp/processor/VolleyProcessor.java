package com.lsw.easyhttp.processor;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lsw.easyhttp.interfaces.ICallBack;
import com.lsw.easyhttp.interfaces.IhttpProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sweeneyliu on 2019/1/4.
 */
public class VolleyProcessor implements IhttpProcessor{

    private RequestQueue mRequestQueue;

    public VolleyProcessor(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void get(String url, Map<String, Object> params, final ICallBack callback) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed(error.toString());
            }
        });

        mRequestQueue.add(stringRequest);
    }

    @Override
    public void post(String url, final Map<String, Object> params, final ICallBack callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> map = new HashMap<>();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    map.put(entry.getKey(), (String) (entry.getValue()));
                }
                return map;
            }
        };

        mRequestQueue.add(stringRequest);
    }
}
