/**
 * 
 */
package com.wizarpos.holdlib.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Description:反射工具类
 * @author harlen
 * @date 2015年12月22日 下午1:30:03
 */
public class ReflectUtil {

	/**
	 * 通过构造函数实例化对象
	 * 
	 * @param className
	 *            类的全路径名称
	 * @param parameterTypes
	 *            参数类型
	 * @param initargs
	 *            参数值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object constructorNewInstance(String className, Class[] parameterTypes, Object[] initargs) {
		try {
			Constructor<?> constructor = (Constructor<?>) Class.forName(className).getDeclaredConstructor(
					parameterTypes); // 暴力反射
			constructor.setAccessible(true);
			return constructor.newInstance(initargs);
		} catch (Exception ex) {
			throw new RuntimeException();
		}

	}

	/**
	 * 根据对象，返回一个class对象，用于获取方法
	 */
	public static Class<?> getClass(Object obj) {
		try {
			return Class.forName(obj.getClass().getName());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据对象，获取某个方法
	 * 
	 * @param obj
	 *            对象
	 * @param methodName
	 *            方法名
	 * @param parameterTypes
	 *            该方法需传的参数类型，如果不需传参，则不传
	 */
	public static Method getMethod(Object obj, String methodName, Class<?>... parameterTypes) {
		try {
			Method method = getClass(obj).getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Method getMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
		try {
			Method method = cls.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 暴力反射获取字段值
	 * 
	 * @param fieldName
	 *            属性名
	 * @param obj
	 *            实例对象
	 * @return 属性值
	 */
	public static Object getFieldValue(String propertyName, Object obj) {
		try {
			Field field = obj.getClass().getDeclaredField(propertyName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 设置字段值
	 * 
	 * @param propertyName
	 *            字段名
	 * @param obj
	 *            实例对象
	 * @param value
	 *            新的字段值
	 * @return
	 */
	public static void setFieldValue(Object obj, String propertyName, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(propertyName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 直接传入对象、方法名、参数，即可使用该对象的隐藏方法
	 * 
	 * @param obj
	 * @param methodName
	 * @param parameter
	 */
	public static Object invoke(Object obj, String methodName, Object... parameter) {
		Class<?>[] parameterTypes = new Class<?>[parameter.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			parameterTypes[i] = parameter[i].getClass();
		}
		try {
			return getMethod(obj, methodName, parameterTypes).invoke(obj, parameter);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 直接传入类名、方法名、参数，即可使用该对象的隐藏静态方法
	 * 
	 * @param cls
	 * @param methodName
	 * @param parameter
	 */
	public static Object invoke(Class<?> cls, String methodName, Object... parameter) {
		Class<?>[] parameterTypes = new Class<?>[parameter.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			parameterTypes[i] = parameter[i].getClass();
		}
		try {
			return getMethod(cls, methodName, parameterTypes).invoke(null, parameter);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 设置字段值
	 * 
	 * @param className
	 *            类的全路径名称
	 * @param methodName
	 *            调用方法名
	 * @param parameterTypes
	 *            参数类型
	 * @param values
	 *            参数值
	 * @param object
	 *            实例对象
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object methodInvoke(String className, String methodName, Class[] parameterTypes, Object[] values,
			Object object) {
		try {
			Method method = Class.forName(className).getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method.invoke(object, values);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

}
