package com.wizarpos.holdlib.http;

/**
 * Created by Administrator on 2016/5/31.
 */
public class MsgResponse {
    private int code;
    private String msg;
    private String result;

    public MsgResponse(int code, String result, String msg) {
        this.code = code;
        this.result = result;
        this.msg = msg;
    }

    public MsgResponse(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
