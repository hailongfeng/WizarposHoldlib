/**
 * 
 */
package com.wizarpos.holdlib.pay;

import org.json.JSONObject;

import com.wizarpos.holdlib.common.ICallBack;

/**
 * @Description:调用支付路由的统一接口
 * @author harlen
 * @date 2015年12月15日 下午2:10:36
 */
public interface IPOSPay {

	/**
	 * 首先绑定支付路由，得到服务接口
	 */
	public abstract void bindPaymentRouter(ICallBack<Boolean> callBack);

	/**
	 * POS签到签到功能
	 * @param params
	 * @param callBack
	 */
	public abstract void login(JSONObject  params, ICallBack<Object> callBack);

	/**
	 * 调用支付接口
	 * @param params
	 * @param callBack
	 */
	public abstract void payCash(JSONObject  params, ICallBack<Object> callBack);

	/**
	 * 获得POS信息
	 * @param params
	 * @param callBack
	 */
	public abstract void getPOSInfo(JSONObject  params, ICallBack<Object> callBack);
	/**
	 * 解绑支付路由
	 * @param params
	 * @param callBack
	 */
	public void unbindPaymentRouter(JSONObject  params, ICallBack<Object> callBack);
	/**
	 * 绑定支付路由来进行设置
	 * @param params
	 * @param callBack
	 */
	void bindRouterSetting(JSONObject  params, ICallBack<Object> callBack);
	/**
	 * 解绑支付路由设置
	 * @param params
	 * @param callBack
	 */
	void unbindRouterSetting(JSONObject  params, ICallBack<Object> callBack) ;
	void payCash2(JSONObject  params, ICallBack<Object> callBack) ;
	void payCashWithSign(JSONObject  params, ICallBack<Object> callBack) ;
	void doReverse(JSONObject  params, ICallBack<Object> callBack) ;
	void doReverseWithSign(JSONObject  params, ICallBack<Object> callBack) ;
	void getPayInfo(JSONObject  params, ICallBack<Object> callBack) ;
	/**
	 * 结算接口
	 * @param params
	 * @param callBack
	 */
	void settle(JSONObject  params, ICallBack<Object> callBack) ;
	void queryBalance(JSONObject  params, ICallBack<Object> callBack) ;
	void consumeCancel(JSONObject  params, ICallBack<Object> callBack) ;
}