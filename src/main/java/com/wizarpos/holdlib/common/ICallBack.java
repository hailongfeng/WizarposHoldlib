/**
 * 
 */
package com.wizarpos.holdlib.common;

/**
 * @Description:通用回调接口
 * @author harlen
 * @date 2015年12月15日 上午11:46:33
 */
public interface ICallBack<T> {
	public static final int code_success = 0;
	public static final int code_fail = 1;

	void onSuccess(T data);

	void onFail(T data);
}
