package com.wizarpos.holdlib;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.ex.DbException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wizarpos.holdlib.driver.print.PrinterBitmapUtil;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	
	public void doPrint(View v) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// PrinterHelper.getInstance().printString("Git是一个分布式的版本控制系统\n");
				// printPurchaseBillItem(MainActivity.this);
				Bitmap bm = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.test);
				PrinterBitmapUtil.printBitmap(bm, 0, 0);

			}
		}).start();
	}
}
