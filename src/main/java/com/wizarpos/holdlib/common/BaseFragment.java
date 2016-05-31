package com.wizarpos.holdlib.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragment extends Fragment {
	/** Fragment当前状态是否可见 */
	protected ProgressDialog dialog;
	protected Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart(this.getClass().getName()); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd(this.getClass().getName()); 
	}
}
