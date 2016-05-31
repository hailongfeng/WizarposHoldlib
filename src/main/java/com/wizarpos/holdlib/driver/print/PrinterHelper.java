package com.wizarpos.holdlib.driver.print;

import android.util.Log;

import com.cloudpos.jniinterface.PrinterInterface;

public class PrinterHelper {
	/* 等待打印缓冲刷新的时间 */
	private static final int PRINTER_BUFFER_FLUSH_WAITTIME = /* 300 */150;

	private static PrinterHelper _instance;

	private PrinterHelper() {
	}

	synchronized public static PrinterHelper getInstance() {
		if (null == _instance)
			_instance = new PrinterHelper();
		return _instance;
	}

	
	synchronized public void printString(String data) {
		Log.e("APP", "------------------printString()------------------");
		try {
			PrinterInterface.open();
			PrinterInterface.begin();
			byte[] fdata=data.getBytes("GB2312");
			printNormalFont();
			PrinterInterface.write(fdata,fdata.length);
			printSmallFont();
			PrinterInterface.write(fdata,fdata.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PrinterInterface.end();
			PrinterInterface.close();
		}
	}
	/**
	 * 打印空行
	 * @param num 空行数
	 */
	private void printEmptyLine(int num){
		byte[] CmdLf=PrinterCommand.getCmdLf();
		for (int i = 0; i < num; i++) {
			PrinterInterface.write(CmdLf,CmdLf.length);
		}
	}
	private void printNormalFont(){
		byte [] cmds = new byte[]{ 0x1B, 0x21, 0x00};
		PrinterInterface.write(cmds, cmds.length );
	}
	
	private void printSmallFont(){
		byte [] cmds = new byte[]{ 0x1B, 0x21, 0x01};
		PrinterInterface.write(cmds, cmds.length );
	}
	
}