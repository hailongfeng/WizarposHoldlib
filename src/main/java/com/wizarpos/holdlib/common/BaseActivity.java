package com.wizarpos.holdlib.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {

	protected Context mContext;
	protected ImageLoader imageLoader;
	protected ProgressDialog dialog;
	protected DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		BaseApplication.getInstance().addActivity(this);
		imageLoader = ImageLoader.getInstance();
		mContext = this;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
		MobclickAgent.onPause(this);
	}
	/* (non-Javadoc)
	 * @see android.support.v7.app.AppCompatActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BaseApplication.getInstance().removeActivity(this);
	}
	
}
