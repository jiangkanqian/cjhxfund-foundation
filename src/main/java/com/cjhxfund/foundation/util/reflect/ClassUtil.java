package com.cjhxfund.foundation.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjhxfund.foundation.util.data.DataUtil;

/**
 * @author Jason
 * @date 2016年4月29日
 */
public class ClassUtil {
	
	
	
	
	/**获取一个泛型的实际类型*/
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getGenricClassType(Class<?> clz){
		
		Type type = clz.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] types = ((ParameterizedType) type).getActualTypeArguments();
			if(types.length>0 && types[0] instanceof Class){
				return (Class<T>)types[0];
			}
		}
		
		return (Class<T>) Object.class;
	}

	/**
	 * 获取key对应的类型字段
	 * 
	 * @param clazz
	 *            对象类型
	 * @param kName
	 *            对象属性名
	 * @return Field 字段类型
	 */
	public static <V> Field getField(Class<V> clazz, String kName) {
		if (clazz == null || kName == null) {
			return null;
		}
		for (Field fieldElem : clazz.getDeclaredFields()) {
			fieldElem.setAccessible(true);
			if (fieldElem.getName().equals(kName)) {
				return fieldElem;
			}
		}
		return null;

	}

	

	/**
	 * 将Map中的对象值，直接转化为对象
	 * @param data 数据源Map
	 * @param o 要转化的实体对象
	 */
	public static  void mapToObj(Map<String, Object> data, Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		int length = fields.length;
		String fieldName = "";
		String methodName = "";
//		Object value = null;
		Class<?> classType = o.getClass();
		try {
			for (int i = 0; i < length; i++) {
				fieldName = fields[i].getName();
				if (data.containsKey(fieldName)) {
					methodName = "set" + DataUtil.upperFirestChar(fieldName);
					Method setMethod = classType.getMethod(methodName, new Class[] { fields[i].getType() });
//					value = data
					setMethod.invoke(o, data.get(fieldName));
				}

			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取对象属性名
	 * */
	public static List<String> getFieldNames(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		List<String> fieldNames = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			// System.out.println(fields[i].getType());
			fieldNames.add(fields[i].getName());
		}
		return fieldNames;
	}

	/**
	 * 获取对象属性的类型
	 * */
	public static Map<String, String> getFieldType(Class<?> clazz) {
		Map<String, String> types = new HashMap<String, String>();
		if (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				// fieldTypes.add(fields[i].getType());
				types.put(fields[i].getName(), fields[i].getType().getSimpleName());
			}
		}
		return types;

	}

	/**
	 * 获取对象的所有属性值
	 * 
	 * */
	public static Map<String, Object> getPropValue(Object o) {
		List<String> fieldNames = getFieldNames(o);
		Map<String, Object> obj = new HashMap<String, Object>();
		for (String fieldName : fieldNames) {
			Object data = getFieldValueByName(fieldName, o);
			if(data != null){
				obj.put(fieldName, data);
			}
		}
		return obj;
	}
	
	

	/**
	 * 根据对象的属性取出对应属性的值
	 * */
	private static Object getFieldValueByName(String fieldName, Object o) {
		Object value = null;
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			if(!getter.equals("getSerialVersionUID")){
				Method method = o.getClass().getMethod(getter, new Class[] {});
				value = method.invoke(o, new Object[] {});
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return value;
	}
}
