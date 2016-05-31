/**
 * 
 */
package com.wizarpos.holdlib.pay;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.holdlib.common.ICallBack;
import com.wizarpos.paymentrouter.aidl.IWizarPayment;
import com.wizarpos.paymentrouter.aidl.IWizarPaymentSetting;
import com.wizarpos.paymentrouter.service.PaymentRouter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Description 收单路由调用的基类
 * @author harlen
 * @date 2015年12月15日 下午2:23:48
 */
public abstract class POSPayBase implements IPOSPay {

	protected final static String request_code_login = "login";
	protected final static String request_code_getPOSInfo = "getPOSInfo";
	protected final static String request_code_payCash = "payCash";
	protected final static String request_code_setPaymentApp0 = "setPaymentApp0";
	protected final static String request_code_payCash2 = "payCash2";
	protected final static String request_code_payCashWithSign = "payCashWithSign";
	protected final static String request_code_doReverse = "doReverse";
	protected final static String request_code_doReverseWithSign = "doReverseWithSign";
	protected final static String request_code_getPayInfo = "getPayInfo";
	protected final static String request_code_settle = "settle";
	protected final static String request_code_queryBalance = "queryBalance";
	protected final static String request_code_consumeCancel = "consumeCancel";

	protected Context mContext;
	protected Handler mHandler;
	protected PaymentRouter router;
	protected IWizarPayment mWizarPayment;// 调用收单的接口

	// 切换支付应用 - 仅调试才需要
	protected IWizarPaymentSetting mPaymentSetting;// 调用收单的接口，来进行收单应用的设置
	final protected ServiceConnection mConnSetting = new SettingConnection();

	public POSPayBase(Context mContext, Handler mHandler) {
		super();
		this.mHandler = mHandler;
		this.mContext = mContext;
	}

	@Override
	public void payCash2(JSONObject params, ICallBack<Object> callBack) {
		final JSONObject jsonObject = new JSONObject();
		setParam4payCash2(jsonObject);
		createAsyncTask(callBack).execute(request_code_payCash2, jsonObject.toString());

	}

	@Override
	public void payCashWithSign(JSONObject params, ICallBack<Object> callBack) {
		final JSONObject jsonObject = new JSONObject();
		setParam4payCashWithSign(jsonObject);
		createAsyncTask(callBack).execute(request_code_payCashWithSign, jsonObject.toString());
	}

	@Override
	public void doReverse(JSONObject params, ICallBack<Object> callBack) {
		final JSONObject jsonObject = new JSONObject();
		setParam4doReverse(jsonObject);
		createAsyncTask(callBack).execute(request_code_doReverse, jsonObject.toString());

	}

	@Override
	public void doReverseWithSign(JSONObject params, ICallBack<Object> callBack) {
		final JSONObject jsonObject = new JSONObject();
		setParam4doReverseWithSign(jsonObject);
		createAsyncTask(callBack).execute(request_code_doReverseWithSign, jsonObject.toString());

	}

	@Override
	public void getPayInfo(JSONObject params, ICallBack<Object> callBack) {
		final JSONObject jsonObject = new JSONObject();
		setParam4getPayInfo(jsonObject);
		createAsyncTask(callBack).execute(request_code_getPayInfo, jsonObject.toString());

	}

	@Override
	public void settle(JSONObject params, ICallBack<Object> callBack) {
		final JSONObject jsonObject = new JSONObject();
		setParam4settle(jsonObject);
		createAsyncTask(callBack).execute(request_code_settle, jsonObject.toString());

	}

	@Override
	public void queryBalance(JSONObject params, ICallBack<Object> callBack) {
		final JSONObject jsonObject = new JSONObject();
		setParam4queryBalance(jsonObject);
		createAsyncTask(callBack).execute(request_code_queryBalance, jsonObject.toString());

	}

	@Override
	public void consumeCancel(JSONObject params, ICallBack<Object> callBack) {
		final JSONObject jsonObject = new JSONObject();
		setParam4consumeCancel(jsonObject);
		createAsyncTask(callBack).execute(request_code_consumeCancel, jsonObject.toString());

	}

	class SettingConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName compName, IBinder binder) {
			mPaymentSetting = IWizarPaymentSetting.Stub.asInterface(binder);
			Log.d("30.onServiceConnected", "compName: " + compName.getClassName() + " service: "
					+ mPaymentSetting.getClass().getName()); // 除BUG: 居然用
																// mWizarPayment!
																// DuanCS@[201506
			System.out.print("Ok");
		}

		@Override
		public void onServiceDisconnected(ComponentName compName) {
			System.out.print("99.onServiceDisconnected "+"compName: " + compName.getClassName());
			mPaymentSetting = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wizarpos.pay.rooter.IPOSPay#login(java.util.Map,
	 * com.wizarpos.pay.rooter.ICommonCallBack)
	 */
	@Override
	public void login(JSONObject params, final ICallBack<Object> callBack) {
		final JSONObject jsonObject = new JSONObject();
		setParam4login(jsonObject);
		createAsyncTask(callBack).execute(request_code_login, jsonObject.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wizarpos.pay.rooter.IPOSPay#payCash(java.util.Map,
	 * com.wizarpos.pay.rooter.ICommonCallBack)
	 */
	@Override
	public void payCash(JSONObject params, final ICallBack<Object> callBack) {
		final JSONObject jsonObject = new JSONObject();
		setParam4payCash(jsonObject);
		createAsyncTask(callBack).execute(request_code_payCash, params.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wizarpos.pay.rooter.IPOSPay#getPOSInfo(java.util.Map,
	 * com.wizarpos.pay.rooter.ICommonCallBack)
	 */
	@Override
	public void getPOSInfo(JSONObject params, final ICallBack<Object> callBack) {
//		final JSONObject jsonObject = new JSONObject();
//		setParam4getPOSInfo(jsonObject);
		createAsyncTask(callBack).execute(request_code_getPOSInfo, params.toString());
	}

	private void setParam4login(JSONObject jsonObject) {
		try {
			jsonObject.put("AppID", "50006");
			jsonObject.put("AppName", "test账户");
			jsonObject.put("OptCode", "01");
			jsonObject.put("OptPass", "0000");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setParam4setPaymentApp(JSONObject jsonObject) {
		try {
			jsonObject.put("PkgName", "");
			jsonObject.put("StartService", "");
			jsonObject.put("Timeout", 3);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setParam4payCash(JSONObject jsonObject) {
		try {
			jsonObject.put("AppID", "12498423");
			jsonObject.put("AppName", "test账户");
			jsonObject.put("TrxID", "12498423");
			jsonObject.put("TransType", 1);
			jsonObject.put("TransAmount", "1000"); // 提交金额信息会跳过金额输入流程
			jsonObject.put("TransIndexCode", "14526855");
			jsonObject.put("ReqTransDate", "140421");
			jsonObject.put("ReqTransTime", "100445");
			jsonObject.put("CardControl", "1");
		} catch (JSONException e) {

			e.printStackTrace();
		}
		// jsonObject.put("cardInfo2", "6225000000000170=301010100"); //
		// 提交磁道信息会跳过刷卡流程
		// jsonObject.put("cardInfo3",
		// "6225000000000170=30101010000=0=0=0=0=0=0=0=0=0=0=0=0=0=0=0=0=0=0=0=0=00");
		// jsonObject.put("TerminalID", "100");
		// jsonObject.put("MerchantID", "100");
		// jsonObject.put("KeyIndex", "100");
	}

	private void setParam4payCash2(JSONObject jsonObject) {
		try {
			jsonObject.put("AppID", "12498423"); // appID, appName, transType,
													// reqTransDate,
													// reqTransTime
			jsonObject.put("AppName", "test账户");
			jsonObject.put("TrxID", "12498423");
			jsonObject.put("TransType", 1);
			jsonObject.put("TransAmount", "1000"); // 提交金额信息会跳过金额输入流程
			jsonObject.put("TransIndexCode", "14526855");
			jsonObject.put("ReqTransDate", "140421");
			jsonObject.put("ReqTransTime", "100445");

			jsonObject.put("CardControl", "2");
			final String key = "CardNum";
			jsonObject.put(key, "6214180100000148511");
		} catch (JSONException e) {

			e.printStackTrace();
		} // 先写默认值, 再覆盖
			// jsonObject.put(key, "6228481970474977215");
	}

	private void setParam4payCashWithSign(JSONObject jsonObject) {

	}

	private void setParam4doReverse(JSONObject jsonObject) {
		try {
			jsonObject.put("AppID", "12498423");
			jsonObject.put("AppName", "test账户");
			jsonObject.put("OriginTransType", 1);
			jsonObject.put("OriginTransAmount", "1000");
			jsonObject.put("OriginTransIndexCode", "14526855");
			jsonObject.put("ReqTransDate", "140421");
			jsonObject.put("ReqTransTime", "100445");
		} catch (JSONException e) {

			e.printStackTrace();
		}
		// jsonObject.put("cardInfo2", "100");
		// jsonObject.put("cardInfo3", "100");
		// jsonObject.put("TerminalID", "100");
		// jsonObject.put("MerchantID", "100");
		// jsonObject.put("KeyIndex", "100");
	}

	private void setParam4doReverseWithSign(JSONObject jsonObject) {
	}

	private void setParam4getPOSInfo(JSONObject jsonObject) {
		try {
			jsonObject.put("AppID", "50006");
			// jsonObject.put("TrxID", "12498423");
			jsonObject.put("AppName", "test账户");
			jsonObject.put("TransType", 3);
			jsonObject.put("ReqTransDate", "140421");
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	private void setParam4getPayInfo(JSONObject jsonObject) {
		try {
			jsonObject.put("appID", "12498423");
			jsonObject.put("TrxID", "12498423");
			jsonObject.put("appName", "test账户");
			jsonObject.put("TransType", 3);
			jsonObject.put("transIndexCode", "14526855");
			jsonObject.put("reqTransDate", "140421");
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	private void setParam4settle(JSONObject jsonObject) {
		try {
			jsonObject.put("AppID", "12498423");
			jsonObject.put("TrxID", "12498423");
			jsonObject.put("AppName", "test账户");
			jsonObject.put("TransType", 23);
			jsonObject.put("ReqTransDate", "140421");
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	private void setParam4queryBalance(JSONObject jsonObject) {
		try {
			jsonObject.put("AppID", "12498423");
			jsonObject.put("TrxID", "12498423");
			jsonObject.put("AppName", "test账户");
			jsonObject.put("TransType", 23);
			jsonObject.put("ReqTransDate", "140421");
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	private void setParam4consumeCancel(JSONObject jsonObject) {
		try {
			jsonObject.put("AppID", "12498423");
			jsonObject.put("TrxID", "12498423");
			jsonObject.put("AppName", "test账户");
			jsonObject.put("TransType", 23);
			jsonObject.put("ReqTransDate", "140421");
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	private AsyncTask<String, Void, String> createAsyncTask(final ICallBack<Object> callBack) {
		return new AsyncTask<String, Void, String>() {
			protected String doInBackground(String... paras) {
				String code = paras[0];
				String para = paras[1];
				String result = null;
				try {
					if (code.equals(request_code_login)) {
						result = mWizarPayment.login(para);
					} else if (code.equals(request_code_getPOSInfo)) {
						result = mWizarPayment.getPOSInfo(para);
					} else if (code.equals(request_code_payCash)) {
						result = mWizarPayment.payCash(para);
					} else if (code.equals(request_code_payCash2)) {
						result = mWizarPayment.payCash(para);
					} else if (code.equals(request_code_payCashWithSign)) {
						result = mWizarPayment.payCashWithSign(para);
					} else if (code.equals(request_code_getPayInfo)) {
						result = mWizarPayment.getPayInfo(para);
					} else if (code.equals(request_code_queryBalance)) {
						result = mWizarPayment.balanceQuery(para);
					} else if (code.equals(request_code_doReverse)) {
						result = mWizarPayment.doReverse(para);
					} else if (code.equals(request_code_doReverseWithSign)) {
						result = mWizarPayment.doReverseWithSign(para);
					} else if (code.equals(request_code_consumeCancel)) {
						result = mWizarPayment.consumeCancel(para);
					} else if (code.equals(request_code_setPaymentApp0)) {
						result = mWizarPayment.setPaymentApp(para);
					} else if (code.equals(request_code_settle)) {
						result = mWizarPayment.settle(para);
					}
					System.out.print("支付路由返回："+result);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return result;
			}

			protected void onPostExecute(String result) {
				if (!TextUtils.isEmpty(result)) {
					JSONObject res;
					try {
						res = new JSONObject(result);
						String RespCode = res.optString("RespCode");
						if (!TextUtils.isEmpty(RespCode) && "00".equals(RespCode)) {
							callBack.onSuccess(result);
						} else {
							callBack.onFail(result);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						callBack.onFail(result);
					}
				} else {
					callBack.onFail(result);
				}
			}
		};
	}

	protected <T> void callBack(boolean success, final ICallBack<T> callBack, final T data) {
		if (success) {
			this.mHandler.post(new Runnable() {

				@Override
				public void run() {
					callBack.onSuccess(data);
				}
			});
		} else {
			this.mHandler.post(new Runnable() {

				@Override
				public void run() {
					callBack.onFail(data);
				}
			});
		}
	}
}
