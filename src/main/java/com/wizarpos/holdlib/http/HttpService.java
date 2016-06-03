package com.wizarpos.holdlib.http;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xutils.common.Callback;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/31.
 * 如果需要
 */
public class HttpService {

    private final static int HTTPTYPE_OKHTTP = 1;
    private final static int HTTPTYPE_XUTILS = 2;
    private final static int HTTPTYPE = HTTPTYPE_OKHTTP;

    public interface IHttpCallBack {
        public void onSuccess(MsgResponse response);

        public void onError(MsgResponse response);
    }

    /**
     * 异步post请求
     *
     * @param heads       请求头
     * @param bodyContent 请求内容 body中的
     * @param callBack
     */
    public static void post(String url, Map<String, String> heads, String bodyContent, final IHttpCallBack callBack) {
        switch (HTTPTYPE) {
            case HTTPTYPE_OKHTTP:
                new HttpService().OKHttpPost(url, heads, bodyContent, callBack);
                break;
            case HTTPTYPE_XUTILS:
                new HttpService().xutilsPost(url, heads, bodyContent, callBack);
                break;
        }
    }

    public static String postSync(String url, Map<String, String> heads, String bodyContent) throws Throwable {
        String res = null;
        switch (HTTPTYPE) {
            case HTTPTYPE_OKHTTP:
                res = new HttpService().OKHttpPostSync(url, heads, bodyContent);
                break;
            case HTTPTYPE_XUTILS:
                res = new HttpService().xutilsPostSync(url, heads, bodyContent);
                break;
        }
        return res;
    }

    public static void get(String url, Map<String, String> heads, final IHttpCallBack callBack) {

        switch (HTTPTYPE) {
            case HTTPTYPE_OKHTTP:
                new HttpService().OKHttpGet(url, heads, callBack);
                break;
            case HTTPTYPE_XUTILS:
                new HttpService().xutilsGet(url, heads, callBack);
                break;
        }
    }

    public static String getSync(String url, Map<String, String> heads) throws Throwable {
        String res = null;
        switch (HTTPTYPE) {
            case HTTPTYPE_OKHTTP:
                res = new HttpService().OKHttpGetSync(url, heads);
                break;
            case HTTPTYPE_XUTILS:
                res = new HttpService().xutilsGetSync(url, heads);
                break;
        }
        return res;

    }

    ////////////////////////////////////////xutils 实现的http请求/////////////////////////////////////
    private void xutilsPost(String url, Map<String, String> heads, String bodyContent, final IHttpCallBack callBack) {
        Callback.CommonCallback<String> commonCallback = new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(new MsgResponse(result));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onError(new MsgResponse(-1, null, ex.getMessage()));
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        new XutilsHttpUtil().post(url, heads, bodyContent, commonCallback);
    }

    private String xutilsPostSync(String url, Map<String, String> heads, String bodyContent) throws Throwable {
        return new XutilsHttpUtil().postSync(url, heads, bodyContent, String.class);
    }

    private void xutilsGet(String url, Map<String, String> heads, final IHttpCallBack callBack) {
        Callback.CommonCallback<String> commonCallback = new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                callBack.onSuccess(new MsgResponse(result));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onError(new MsgResponse(-1, null, ex.getMessage()));
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        new XutilsHttpUtil().get(url, heads, null, commonCallback);
    }

    private String xutilsGetSync(String url, Map<String, String> heads) throws Throwable {
        return new XutilsHttpUtil().getSync(url, heads, null, String.class);
    }
////////////////////////////////////////xutils 实现的http请求/////////////////////////////////////


    ////////////////////////////////////////OKHttp 实现的http请求/////////////////////////////////////
    private void OKHttpPost(String url, Map<String, String> heads, String bodyContent, final IHttpCallBack iCallBack) {

        com.squareup.okhttp.Callback callback = new com.squareup.okhttp.Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                iCallBack.onError(new MsgResponse(-1, null, e.getMessage()));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                iCallBack.onSuccess(new MsgResponse(response.body().string()));
            }
        };

        OKHttpUtil.getInstance().post(url, heads, bodyContent, callback);
    }

    private String OKHttpPostSync(String url, Map<String, String> heads, String bodyContent) throws Throwable {
        Response response = OKHttpUtil.getInstance().postSync(url, heads, bodyContent);
        return response.body().string();
    }

    private void OKHttpGet(String url, Map<String, String> heads, final IHttpCallBack iCallBack) {
        com.squareup.okhttp.Callback callback = new com.squareup.okhttp.Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                iCallBack.onError(new MsgResponse(-1, e.getMessage(), null));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                iCallBack.onSuccess(new MsgResponse(response.body().string()));
            }
        };

        OKHttpUtil.getInstance().get(url, heads, callback);
    }

    private String OKHttpGetSync(String url, Map<String, String> heads) throws Throwable {
        Response response = OKHttpUtil.getInstance().getSync(url, heads);
        return response.body().string();
    }
////////////////////////////////////////OKHttp 实现的http请求/////////////////////////////////////

}
