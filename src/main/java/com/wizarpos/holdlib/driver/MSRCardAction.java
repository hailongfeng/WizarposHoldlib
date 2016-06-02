/**
 *
 */
package com.wizarpos.holdlib.driver;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.cloudpos.jniinterface.MSRInterface;
import com.wizarpos.holdlib.common.ICallBack;

/**
 * @Description:磁条卡刷卡
 * @author harlen
 * @date 2015年12月14日 下午4:09:39
 */
public class MSRCardAction extends ConstantAction {




	/**
	 * @param mContext
	 * @param mHandler
	 */
	public MSRCardAction(Context mContext, Handler mHandler) {
		super(mContext, mHandler);
		// TODO Auto-generated constructor stub
	}

	public void open(final ICallBack<Object> callBack) {
		System.out.print("current open status is "+isOpened);
		CallBackThread thread = new CallBackThread(1, callBack);
		thread.start();

	}

	public void close() {
		cancelCallBack();
		checkOpenedAndGetData(new DataAction() {
			@Override
			public int getResult() {
				int result = 0;
				result = MSRInterface.close();
				isOpened = false;
				System.out.print("关闭成功");
				return result;
			}
		});
	}

	class CallBackThread extends Thread {
		private int trackNo;
		private ICallBack<Object> callBack;

		public CallBackThread(int trackNo, ICallBack<Object> callBack) {
			super();
			this.trackNo = trackNo;
			this.callBack = callBack;
		}

		@Override
		public void run() {
			if(isOpened){
				close();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			int result = MSRInterface.open();
			if (result < 0) {
				System.out.print("result:" + result + "打开失败。。。");
				callBack(false, callBack, "设备打开失败");
				return;
			} else {
				isOpened = true;
			}
			synchronized (MSRInterface.object) {
				try {
					MSRInterface.object.wait();
				} catch (InterruptedException e) {
					close();
					e.printStackTrace();
				}
			}
			if (MSRInterface.eventID == MSRInterface.CONTACTLESS_CARD_EVENT_FOUND_CARD) {
				System.out.print("Find a card");
				result = getTrackError(trackNo);
				if (result < 0) {
					System.out.print("TrackError：" + result);
					close();
					callBack(false, callBack, "getTrack Error");
					return;
				}
				result = getTrackDataLength(trackNo);
				if (result < 0) {
					System.out.print("获取长度出错：" + result);
					close();
					callBack(false, callBack, "getTrackDataLength Error");
					return;
				}
				byte[] arryTrackData = new byte[result];
				result = getTrackData(trackNo, arryTrackData);
				if (result < 0) {
					System.out.print("获取数据出错：" + result);
					close();
					callBack(false, callBack, "getTrackData Error");
					return;
				}
				String cardNum = new String(arryTrackData);
				System.out.print("result=" + result + "，磁道：" + trackNo + "，数据：" + cardNum);
				close();
				callBack(true, callBack, cardNum);
			} else if (MSRInterface.eventID == EVENT_ID_CANCEL) {
				close();
				System.out.print("Cancel notifier");
				return;
			}
		}
	}

	private <T> void callBack(boolean success, final ICallBack<T> callBack, final T data) {
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

	public void cancelCallBack() {
		synchronized (MSRInterface.object) {
			Log.i("MSRCard", "notify");
			MSRInterface.object.notifyAll();
			MSRInterface.eventID = EVENT_ID_CANCEL;
		}
	}

	private int getTrackData(final int trackNo, final byte[] arryData) {
		int result = checkOpenedAndGetData(new DataAction() {

			@Override
			public int getResult() {
				int result = MSRInterface.getTrackData(trackNo, arryData, arryData.length);
				return result;
			}
		});
		return result;
	}

	private int getTrackDataLength(final int trackNo) {
		int result = checkOpenedAndGetData(new DataAction() {

			@Override
			public int getResult() {
				int result = MSRInterface.getTrackDataLength(trackNo);
				return result;
			}
		});
		return result;
	}

	private int getTrackError(final int trackNo) {
		int result = checkOpenedAndGetData(new DataAction() {

			@Override
			public int getResult() {
				int result = MSRInterface.getTrackError(trackNo);
				return result;
			}
		});
		return result;
	}
}
