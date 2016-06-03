package com.wizarpos.holdlib.http;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/5/31.
 */
public class OKHttpUtil {

    private static OKHttpUtil mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;


    private static final String TAG = "OkHttpClientManager";

    private OKHttpUtil() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mOkHttpClient.setConnectTimeout(10000L, TimeUnit.SECONDS);
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OKHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (OKHttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new OKHttpUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 异步post请求
     *
     * @param heads       请求头
     * @param bodyContent 请求内容 body中的
     * @param callback
     */
    public void post(String url, Map<String, String> heads, String bodyContent, Callback callback) {
        Request request = buildPostRequest(url, heads, bodyContent);
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    //同步
    public Response postSync(String url, Map<String, String> heads, String bodyContent) throws Throwable {
        Request request = buildPostRequest(url, heads, bodyContent);
        return mOkHttpClient.newCall(request).execute();
    }

    //异步
    public void get(String url, Map<String, String> heads, Callback callback) {
        Headers.Builder builder = mapToHeaderBuilder(heads);
        final Request request = new Request.Builder()
                .url(url)
                .headers(builder.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    //同步
    public Response getSync(String url, Map<String, String> heads) throws Throwable {
        Headers.Builder builder = mapToHeaderBuilder(heads);
        final Request request = new Request.Builder()
                .url(url)
                .headers(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    private Headers.Builder mapToHeaderBuilder(Map<String, String> headers) {
        Headers.Builder builder = new Headers.Builder();
        if (headers != null) {
            Iterator<String> iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = headers.get(key);
                builder.add(key, value);
            }
        }
        return builder;
    }

    private Request buildPostRequest(String url, Map<String, String> headers, String content) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        Headers.Builder builder1 = mapToHeaderBuilder(headers);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, content);
        Headers headers1 = builder1.build();
        return new Request.Builder()
                .url(url)
                .headers(headers1)
                .post(body)
                .build();
    }

    private void deliveryResult(final Callback callBack, Request request) {

    }

}
