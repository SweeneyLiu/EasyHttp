package com.lsw.easyhttp.interfaces;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by sweeneyliu on 2019/1/30.
 */
public interface IHttpCallback {
    void onSuccess(Call call, Response response) throws IOException;
    void onFailure(Call call, IOException e);
}
