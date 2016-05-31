package com.wizarpos.holdlib.driver;

import android.content.Context;
import android.os.Handler;

import com.cloudpos.jniinterface.LEDInterface;
import com.wizarpos.holdlib.common.ICallBack;

public class LEDAction extends ConstantAction {
	/**
	 * @param mContext
	 * @param mHandler
	 */
	public LEDAction(Context mContext, Handler mHandler) {
		super(mContext, mHandler);
	}

	private static int index = 0;

	public void open( final ICallBack<Object> callBack) {
		if (isOpened) {
			callBack.onFail("设备已经打开");
		} else {
			try {
				int result = LEDInterface.open();
				if (result < 0) {
					callBack.onFail("设备打开失败");
				} else {
					isOpened = true;
					callBack.onFail("设备打开成功");
				}
			} catch (Throwable e) {
				e.printStackTrace();
				callBack.onFail("设备打开失败");
			}
		}
	}

	public void close( final ICallBack<Object> callBack) {
		checkOpenedAndGetData(new DataAction() {

			@Override
			public int getResult() {
				isOpened = false;
				int result = LEDInterface.close();
				return result;
			}
		});
	}

	public void turnOn( final ICallBack<Object> callBack) {
		checkOpenedAndGetData(new DataAction() {

			@Override
			public int getResult() {
				return LEDInterface.turnOn(index);
			}
		});
	}

	public void turnOff( final ICallBack<Object> callBack) {
		checkOpenedAndGetData(new DataAction() {

			@Override
			public int getResult() {
				return LEDInterface.turnOff(index);
			}
		});
	}

	public void getStatus( final ICallBack<Object> callBack) {
		checkOpenedAndGetData(new DataAction() {

			@Override
			public int getResult() {
				int result = LEDInterface.getStatus(index);
				if (result == 0) {
					callBack.onSuccess(false);
				} else if (result > 0) {
					callBack.onSuccess(true);
				}
				return result;
			}
		});
	}
}
