/**
 * 
 */
package com.wizarpos.holdlib.pay;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.wizarpos.holdlib.common.ICallBack;
import com.wizarpos.paymentrouter.aidl.IWizarPayment;
import com.wizarpos.paymentrouter.aidl.IWizarPaymentSetting;
import com.wizarpos.paymentrouter.service.PaymentRouter;

/**
 * @Description:调用收单路由的封装类(通过内嵌方式调用)
 * @author harlen
 * @date 2015年12月15日 上午10:06:33
 */
public class POSPayByInLine extends POSPayBase {

	/**
	 * @param mContext
	 * @param mHandler
	 * @param isInline
	 */
	public POSPayByInLine(Context mContext, Handler mHandler) {
		super(mContext, mHandler);
		if (router == null) {
			router = new PaymentRouter(mContext); // ctx 为 Activity/Service/甚至任何 Context 实例
		}
	}

	public void bindRouterSetting(JSONObject  params, ICallBack<Object> callBack) {
		if (mPaymentSetting == null) {
			Intent intent = new Intent(IWizarPaymentSetting.class.getName());
			mPaymentSetting = IWizarPaymentSetting.Stub.asInterface(router.onBind(intent));
		}
	}

	public void unbindRouterSetting(JSONObject  params, ICallBack<Object> callBack) {
		if (mPaymentSetting != null) {
			router.onUnbind(new Intent(IWizarPaymentSetting.class.getName())); // 按需断开
			mPaymentSetting = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wizarpos.pay.rooter.IPOSPay#bindPaymentRouter(com.wizarpos.pay.rooter
	 * .ICommonCallBack)
	 */
	@Override
	public void bindPaymentRouter(final ICallBack<Boolean> callBack) {
		if (mWizarPayment != null) {
			Log.d("19.bindPaymentRouter", "Skipped for mWizarPayment != null");
		} else {
			Intent intent = new Intent(IWizarPayment.class.getName());
			Log.d("10.bindPaymentRouter", "binding: " + intent);
			mWizarPayment = IWizarPayment.Stub.asInterface(router.onBind(intent));
			if (mWizarPayment != null) {
				callBack(true, callBack, true);
			} else {
				callBack(false, callBack, false);
			}
		}
	}

	public void unbindPaymentRouter(JSONObject  params, ICallBack<Object> callBack) {
		if (mWizarPayment == null) {
			Log.d("29.unbindPaymentRouter", "Skipped for mWizarPayment != null");
		} else {
			Log.d("20.unbindPaymentRouter", "begin");
			router.onUnbind(new Intent(IWizarPayment.class.getName())); // 按需断开
			Log.d("23.unbindPaymentRouter", "end");
			mWizarPayment = null;
		}
	}

}
