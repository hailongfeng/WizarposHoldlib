/**
 * 
 */
package com.wizarpos.holdlib.utils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: GsonUtils
 * @author harlen
 * @date 2015年3月20日 上午11:07:36
 *
 */
public class GsonUtils {
	public static <T> T getBean(String jsonString, Class<T> cls) {
		T t = null;
		try {
			t = JSON.parseObject(jsonString, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public static <T> List<T> getBeans(String jsonString, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		try {
			list.addAll(JSON.parseArray(jsonString, clazz));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
