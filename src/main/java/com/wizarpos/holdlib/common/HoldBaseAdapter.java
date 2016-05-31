package com.wizarpos.holdlib.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class HoldBaseAdapter extends BaseAdapter {
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;
	protected Context mContext;
	protected LayoutInflater inflater;

	public HoldBaseAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		imageLoader = ImageLoader.getInstance();
		// options = new DisplayImageOptions.Builder()
		// .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
		// .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
		// .showImageForEmptyUri(R.drawable.defaul_pic)
		// .build();// 构建完成
	}

}
