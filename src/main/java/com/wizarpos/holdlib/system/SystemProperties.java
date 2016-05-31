package com.wizarpos.holdlib.system;

import android.util.Log;

public class SystemProperties {
	public static String getsystemPropertie(String key) {
		Object bootloaderVersion = null;
		try {
			Class<?> systemProperties = Class.forName("android.os.SystemProperties");
			Log.i("systemProperties", systemProperties.toString());
			bootloaderVersion = systemProperties.getMethod("get", new Class[] { String.class, String.class }).invoke(systemProperties, new Object[] { key, "unknown" });
			Log.i("bootloaderVersion", bootloaderVersion.getClass().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bootloaderVersion.toString();
	}
}
