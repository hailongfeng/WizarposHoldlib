package com.wizarpos.holdlib.driver;

import android.content.Context;
import android.os.Handler;


public class ConstantAction {
	public static String TAG = null;
	// public static boolean isOpened = false;
	public boolean isOpened = false;
	protected static final int EVENT_ID_CANCEL = -1;

	protected Context mContext;
	protected Handler mHandler;


	public ConstantAction(Context mContext, Handler mHandler) {
		super();
		this.mContext = mContext;
		this.mHandler = mHandler;
	}

	int checkOpenedAndGetData(DataAction action) {
		int result = -1;
		if (!isOpened) {
			System.out.print("没有打开设备");
		} else {
			result = action.getResult();
			if (result < 0) {
				System.out.print("result：" + result + ",error");
			} else {
				System.out.print("result：" + result + ",success");
			}
		}
		return result;
	}

	int getData(DataAction action) {
		int result = 0;
		try {
			result = action.getResult();
			if (result < 0) {
			} else {
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return result;
	}

}

interface DataAction {
	int getResult();
}
