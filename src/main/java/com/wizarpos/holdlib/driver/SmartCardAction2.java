/**
 * 
 */
package com.wizarpos.holdlib.driver;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.cloudpos.jniinterface.SmartCardEvent;
import com.cloudpos.jniinterface.SmartCardInterface;
import com.cloudpos.jniinterface.SmartCardSlotInfo;
import com.wizarpos.holdlib.system.WizarposDevice;

/**
 * @Description:TODO(类的作用) 
 * @author harlen
 * @date 2015年12月14日 下午5:08:47
 */
public class SmartCardAction2 extends ConstantAction{


    /**
	 * @param mContext
	 * @param mHandler
	 */
	public SmartCardAction2(Context mContext, Handler mHandler) {
		super(mContext, mHandler);
		// TODO Auto-generated constructor stub
	}

	private int slotIndex = 1;
    private static boolean isRun = false;
    private int handle = 0;

    public void queryMaxNumber() {
        int result = getData(new DataAction() {

            @Override
            public int getResult() {
                return SmartCardInterface.queryMaxNumber();
            }
        });
        if (result >= 0) {
//            mCallback.sendSuccessMsg("Max Slot Number = " + result);
        }
    }

    public void queryPresence() {
        int result = getData(new DataAction() {

            @Override
            public int getResult() {
                return SmartCardInterface.queryPresence(slotIndex);
            }
        });
        if (result >= 0) {
            String msg = String.format("SlotIndex : %d Event : %s", slotIndex,
                    result == 0 ? "Absence" : "Presence");
//            mCallback.sendSuccessMsg(msg);
        }
    }

    public void open() {
        if (isOpened) {
//            mCallback.sendFailedMsg(mContext.getResources().getString(R.string.device_opened));
        } else {
            try {
                int result = SmartCardInterface.open(slotIndex);
                if (result < 0) {
                } else {
                    isOpened = true;
                    handle = result;
//                    CallBackThread presentThread = new CallBackThread(callback,
//                            SmartCardInterface.objPresent);
//                    presentThread.start();
//                    CallBackThread absentThread = new CallBackThread(callback,
//                            SmartCardInterface.objAbsent);
//                    absentThread.start();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
//        cancelRequest();
        checkOpenedAndGetData(new DataAction() {

            @Override
            public int getResult() {
                isOpened = false;
                Log.e(TAG, "handle = " + handle);
                int result = SmartCardInterface.close(handle);
                return result;
            }
        });
    }

    public void powerOn() {
        final SmartCardSlotInfo slotInfo = new SmartCardSlotInfo();
        final byte[] arryATR = new byte[64];

        checkOpenedAndGetData(new DataAction() {

            @Override
            public int getResult() {
                int result = SmartCardInterface.powerOn(handle, arryATR, slotInfo);
                if (result >= 0) {
//                    mCallback.sendSuccessMsg("Data = "
//                            + StringUtility.ByteArrayToString(arryATR, result));
                }
                return result;
            }
        });
    }

    public void powerOff() {
//        setParams(param, callback);
        checkOpenedAndGetData(new DataAction() {

            @Override
            public int getResult() {
                return SmartCardInterface.powerOff(handle);
            }
        });
    }

    public void transmit() {
        final byte[] arryAPDU = new byte[] {
                0x00, (byte) 0x84, 0x00, 0x00, 0x08
        };
        final byte[] arryResponse = new byte[32];
        checkOpenedAndGetData(new DataAction() {

            @Override
            public int getResult() {
                int result = SmartCardInterface.transmit(handle, arryAPDU, arryResponse);
                if (result >= 0) {
//                    mCallback.sendSuccessMsg("APDUResponse = "
//                            + StringUtility.ByteArrayToString(arryResponse, result));
                }
                return result;
            }
        });
    }

    public void verify() {
        final byte[] arryKey = {
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
        };
        checkOpenedAndGetData(new DataAction() {

            @Override
            public int getResult() {
                return SmartCardInterface.verify(handle, arryKey);
            }
        });
    }

    public void read() {
        final byte[] arryData = new byte[16];
        checkOpenedAndGetData(new DataAction() {

            @Override
            public int getResult() {
                int result = SmartCardInterface.read(handle, 0, arryData, 0);
                if (result >= 0) {
//                    mCallback.sendSuccessMsg("Read data = "
//                            + StringUtility.ByteArrayToString(arryData, result));
                }
                return result;
            }
        });
    }

    public void write() {
        final byte[] arryData = WizarposDevice.createMasterKey(16);
        checkOpenedAndGetData(new DataAction() {

            @Override
            public int getResult() {
                int result = SmartCardInterface.read(handle, 0, arryData, 0);
                if (result >= 0) {
//                    mCallback.sendSuccessMsg("Written data = "
//                            + StringUtility.ByteArrayToString(arryData, result));
                }
                return result;
            }
        });
    }

    public void cancelRequest() {
        if (isRun) {
            synchronized (SmartCardInterface.objPresent) {
                SmartCardInterface.event = new SmartCardEvent();
                SmartCardInterface.event.nEventID = EVENT_ID_CANCEL;
                SmartCardInterface.objPresent.notifyAll();
            }
            synchronized (SmartCardInterface.objAbsent) {
                SmartCardInterface.event = new SmartCardEvent();
                SmartCardInterface.event.nEventID = EVENT_ID_CANCEL;
                SmartCardInterface.objAbsent.notifyAll();
            }
        } else {
//            mCallback.sendFailedMsg(mContext.getResources().getString(R.string.operation_failed));
        }
    }

    class CallBackThread extends Thread {
        private Object object;

        public CallBackThread(Object object) {
            this.object = object;
        }

        @Override
        public void run() {
            isRun = true;
            while (isRun) {
                synchronized (object) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                if (SmartCardInterface.event.nEventID == EVENT_ID_CANCEL) {
                    break;
                } else if (SmartCardInterface.event.nEventID == SmartCardEvent.SMART_CARD_EVENT_INSERT_CARD) {
                    String msg = String.format("SlotIndex : %d Event : %s",
                            SmartCardInterface.event.nSlotIndex, "Inserted");
//                    mCallback.sendSuccessMsg(msg);
                } else if (SmartCardInterface.event.nEventID == SmartCardEvent.SMART_CARD_EVENT_REMOVE_CARD) {
                    String msg = String.format("SlotIndex : %d Event : %s",
                            SmartCardInterface.event.nSlotIndex, "Removed");
//                    mCallback.sendSuccessMsg(msg);
                }
            }
            isRun = false;
        }
    }



}
