package com.cjhxfund.foundation.util.str;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public final class JsonUtil {

	/**
	 * 将对象转换成json字符串
	 * 
	 **/
	public static String toJson(Object obj) {
		// Gson gson = new Gson();
		// Type type=new TypeToken<Object>(){}.getType();
		if(obj == null){
			return null;
		}
		return new Gson().toJson(obj);
	}

	/**
	 * 将内容装换为json，html不会转义
	 * @author xiejs
	 * @date 2015年9月5日
	 * @param obj
	 * @return
	 */
	public static String toJsonContent(Object obj) {
		if(obj == null){
			return null;
		}
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(obj);
	}

	/**
	 * 将json字符串转换为单个对象
	 * @param <T>
	 * 
	 **/
	public static <T> T jsonToObj(String json, Class<T> clazz) {
		// Gson gson = new Gson();
		return new Gson().fromJson(json, clazz);
	}

	/**
	 * 将json字符串转换为对象集合
	 * @param <T>
	 **/
	@SuppressWarnings("unchecked")
	public static <T> List<T> jsonToList(String json, Class<T> clazz) {
		String name = clazz.getSimpleName();
		if (name.equals("Integer")) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<Integer>>() {}.getType();
			List<Integer> data = gson.fromJson(json, type);
			return (List<T>) data;
		} else if (name.equals("Long")) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<Long>>() {}.getType();
			List<Long> data = gson.fromJson(json, type);
			return (List<T>) data;
		} 
		else if (name.equals("Double")) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<Double>>() {}.getType();
			List<Double> data = gson.fromJson(json, type);
			return (List<T>) data;
		}
		else if (name.equals("String")) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<String>>() {}.getType();
			List<String> data = gson.fromJson(json, type);
			return (List<T>) data;
		}
		else {
			//转化的时候，尽量不要用泛型，否则会报错，而且这种模式比下面那种要慢很多
//			Gson gson = new Gson();
//			Type type = new TypeToken<List<T>>() {}.getType();
//			return gson.fromJson(json, type);
			JsonArray ja = new JsonParser().parse(json).getAsJsonArray();
			List<Object> data = new ArrayList<Object>();
			for (int i = 0; i < ja.size(); i++) {
				data.add(jsonToObj(ja.get(i).toString(), clazz));
			}
			return (List<T>) data;
		}
	}
	

	/**
	 * 少用，仅将单个对象转换
	 */
	public static Map<String, Object> jsonToMap(String json) {
		// Type type=new TypeToken<List<T>>(){}.getType();
		return new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {
		}.getType());
	}

	/**
	 * json对象转换为Map对象
	 * @param jo
	 * @return
	 */
	public static Map<String, String> joToMap(JsonObject jo) {
		Map<String, String> condi = new HashMap<String, String>();
		Set<Entry<String, JsonElement>> Entrys = jo.entrySet();
		Iterator<Entry<String, JsonElement>> jsonIt = Entrys.iterator();
		while (jsonIt.hasNext()) {
			Entry<String, JsonElement> entry = jsonIt.next();
			if (!entry.getValue().isJsonArray()) {
				String elem = entry.getValue().getAsString().trim();
				if (null == elem || elem.length() == 0) {
					jsonIt.remove();
				} else {
					condi.put(entry.getKey(), elem);
				}
			} else {
				condi.put(entry.getKey(), entry.getValue().toString());
			}
		}
		return condi;
	}

	/**
	 * 少用，仅将单个对象转换
	 */
	public static Map<String, String> jsonToMapStr(String json) {
		// Type type=new TypeToken<List<T>>(){}.getType();
		return new Gson().fromJson(json, new TypeToken<Map<String, String>>() {
		}.getType());
	}
	

	public static Map<Object, Object> objToMap(Object obj) {
		// Type type=new TypeToken<List<T>>(){}.getType();
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		return gson.fromJson(json, new TypeToken<Map<Object, Object>>() {
		}.getType());
	}

	public static JsonObject decodeJson(String content) {
		return new JsonParser().parse(content).getAsJsonObject();
	}
	
	public static JsonArray decodeJsonArr(String content) {
		return new JsonParser().parse(content).getAsJsonArray();
	}
	
	/**
	 * 如果是json格式的字符串，则会返回json元素，否则返回null;
	 * @param content
	 * @return
	 */
	public static JsonElement getJsonObj(String content) {
		try{
			return new JsonParser().parse(content);
		}
		catch(JsonParseException e){
			return null;
		}
	}

	public static String getEasyUIDgData(Object obj, int total) {
		// if (total == 0) {
		// return JsonUtil.toJson(obj);
		// }
		// else {
		// }
		return "{\"total\":" + total + " , \"rows\":" + JsonUtil.toJson(obj) + "}";
	}

}
