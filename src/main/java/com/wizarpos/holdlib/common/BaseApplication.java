/**
 * 
 */
package com.wizarpos.holdlib.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wizarpos.holdlib.pay.IPOSPay;
import com.wizarpos.holdlib.pay.POSPayByAIDL;

/**
 * @author harlen
 * @date 2015年5月20日 上午9:34:44
 */
public class BaseApplication extends Application {
	private HashMap<String, Object> myCache = new HashMap<String, Object>();
	private static BaseApplication application;
	private List< Activity> activityList=new ArrayList<Activity>();
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		x.Ext.init(this);
	    x.Ext.setDebug(true); // 是否输出debug日志
	    initImageDown(this);
	}
	/**
	 * 保存Activity以便完全退出
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}
	public List< Activity> getActivitys() {
		return activityList;
	}

	/**
	 * 应用程序退出
	 */
	public void exit() {
		for (Activity activity : activityList) {
			if(activity!=null)
				activity.finish();
		}
		System.exit(0);
	}
	@SuppressWarnings("deprecation")
	void initImageDown(Context context) {
		File cacheDir = context.getExternalFilesDir("image");
		// File cacheDir =StorageUtils.getOwnCacheDirectory(this,
		// dir.getAbsolutePath());
		LogUtil.d("图片缓存路径：" + cacheDir.getAbsolutePath());
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPoolSize(3);// 线程池内加载的数量
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCache(new UnlimitedDiskCache(cacheDir));
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}

	public static BaseApplication getInstance() {
		return application;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public void saveMyCache(String key, Object value) {
		this.myCache.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getMyCache(String key,Class<T> clazz) {
		return (T)myCache.get(key);
	}

	@SuppressWarnings("unchecked")
	public void removeMyCache(String key) {
		myCache.remove(key);
	}
}
