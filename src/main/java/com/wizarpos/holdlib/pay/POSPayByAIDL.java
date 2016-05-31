/**
 * 
 */
package com.wizarpos.holdlib.pay;

import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.wizarpos.holdlib.common.ICallBack;
import com.wizarpos.paymentrouter.aidl.IWizarPayment;
import com.wizarpos.paymentrouter.aidl.IWizarPaymentSetting;

/**
 * @Description:调用收单路由的封装类(通过AIDL调用)
 * @author harlen
 * @date 2015年12月15日 上午10:06:33
 */
public class
POSPayByAIDL extends POSPayBase implements IPOSPay {
	final ServiceConnection mConnPayment = new PaymentConnection();
	
	public POSPayByAIDL(Context mContext, Handler mHandler) {
		super(mContext, mHandler);
	}

	public void bindRouterSetting(JSONObject  params, ICallBack<Object> callBack) {
		if (mPaymentSetting == null) {
			Intent intent = new Intent(IWizarPaymentSetting.class.getName());
			mContext.bindService(intent, mConnSetting, Context.BIND_AUTO_CREATE);
		}
	}

	public void unbindRouterSetting(JSONObject  params, ICallBack<Object> callBack) {
		if (mPaymentSetting != null) {
			mContext.unbindService(mConnSetting);
			mPaymentSetting = null;
		}
	}
	class PaymentConnection implements ServiceConnection {
		ICallBack<Boolean> callBack;
		/**
		 * @param callBack
		 */
		public void setCallBack(ICallBack<Boolean> callBack) {
			this.callBack=callBack;
		}

		@Override
		public void onServiceConnected(ComponentName compName, IBinder binder) {
			mWizarPayment = IWizarPayment.Stub.asInterface(binder);
			Log.d("31.onServiceConnected", "compName: " + compName.getClassName() + " service: "
					+ mWizarPayment.getClass().getName());
			System.out.print("Ok");
			callBack(true, callBack, true);
		}

		@Override
		public void onServiceDisconnected(ComponentName compName) {
			System.out.print("98.onServiceDisconnected "+"compName: " + compName.getClassName());
			mWizarPayment = null;
			callBack(true, callBack, false);
		}
	};
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wizarpos.pay.rooter.IPOSPay#bindPaymentRouter(com.wizarpos.pay.rooter
	 * .ICommonCallBack)
	 */
	@Override
	public void bindPaymentRouter(final ICallBack<Boolean> callBack) {
		((PaymentConnection)mConnPayment).setCallBack(callBack);
		if (mWizarPayment != null) {
			Log.d("19.bindPaymentRouter", "Skipped for mWizarPayment != null");
		} else {
			Intent intent = new Intent(IWizarPayment.class.getName());
			Log.d("10.bindPaymentRouter", "binding: " + intent);
			final Boolean result = mContext.bindService(intent, mConnPayment, Context.BIND_AUTO_CREATE);
			Log.d("11.bindPaymentRouter", "bind = " + result);
		}
	}

	public void unbindPaymentRouter(JSONObject  params, ICallBack<Object> callBack) {
		if (mWizarPayment == null) {
			Log.d("29.unbindPaymentRouter", "Skipped for mWizarPayment != null");
		} else {
			Log.d("20.unbindPaymentRouter", "begin");
			mContext.unbindService(mConnPayment);
			Log.d("23.unbindPaymentRouter", "end");
			mWizarPayment = null;
		}
	}
}
