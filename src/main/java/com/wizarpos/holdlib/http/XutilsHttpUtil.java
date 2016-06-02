package com.wizarpos.holdlib.http;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.body.RequestBody;
import org.xutils.http.body.StringBody;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/14.
 */
public class XutilsHttpUtil {
//    private String url = "http://58.213.92.226:9092/store-server/service";
    private int timeOut = 10000;
    private String charSet = "UTF-8";

    public XutilsHttpUtil() {
    }



    public Map<String, String> getCommonHeads() {
        Map<String, String> heads = new HashMap<>();
        heads.put("te_method", "doAction");
        heads.put("version", "V1_0");
        heads.put("mobileType", "111");
        return heads;
    }

    private void setHeads(RequestParams params, Map<String, String> heads) {
        Iterator<String> iterator = heads.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = heads.get(key);
            params.addHeader(key, value);
        }
    }

    private RequestParams getCommonPostRequestParams(String url,Map<String, String> heads, String bodyContent) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setConnectTimeout(timeOut);
        requestParams.setCharset(charSet);
        if (heads != null && !heads.isEmpty()) {
            setHeads(requestParams, heads);
        }
        try {
            RequestBody requestBody = new StringBody(bodyContent, charSet);
            requestParams.setAsJsonContent(true);
            requestParams.setRequestBody(requestBody);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    private RequestParams getCommonGetRequestParams(String url,Map<String, String> heads, Map<String, String> param) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setConnectTimeout(timeOut);
        requestParams.setCharset(charSet);
        if (heads != null && !heads.isEmpty()) {
            setHeads(requestParams, heads);
        }
        if (param != null && !param.isEmpty()) {
            Iterator<String> iterator = param.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = param.get(key);
                requestParams.addParameter(key, value);
            }
        }

        return requestParams;
    }

    /**
     * 异步post请求
     *
     * @param heads          请求头
     * @param bodyContent    请求内容 body中的
     * @param commonCallback
     */
    public void post(String url,Map<String, String> heads, String bodyContent, Callback.CommonCallback commonCallback) {
        RequestParams requestParams = getCommonPostRequestParams(url,heads, bodyContent);
        x.http().post(requestParams, commonCallback);
    }

    public <T> T postSync(String url,Map<String, String> heads, String bodyContent, Class<T> clazz) throws Throwable {
        RequestParams requestParams = getCommonPostRequestParams(url,heads, bodyContent);
        return x.http().postSync(requestParams, clazz);
    }

    public void get(String url,Map<String, String> heads, Map<String, String> param, Callback.CommonCallback commonCallback) {
        RequestParams requestParams = getCommonGetRequestParams(url,heads, param);
        x.http().get(requestParams, commonCallback);
    }

    public <T> T getSync(String url,Map<String, String> heads, Map<String, String> param, Class<T> clazz) throws Throwable {
        RequestParams requestParams = getCommonGetRequestParams(url,heads, param);
        return x.http().getSync(requestParams, clazz);
    }

}
