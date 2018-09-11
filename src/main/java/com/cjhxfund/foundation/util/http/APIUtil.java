package com.cjhxfund.foundation.util.http;

import java.util.HashMap;
import java.util.Map;

public class APIUtil {

	/**
	 * 统一API请求方式处理
	 * @param url 详细地址
	 * @param content 请求数据内容，如果为get方式，可以不用
	 * @param token 请求token
	 * @param method 如果为get方式，可以不用
	 * @return
	 */
	public static String doAPI(String url, String content, String token, String method){
		Map<String,String> header = new HashMap<String,String>();
		header.put("Content-Type", "application/json");
//		header.put("Accept", "application/json,text/javascript");
		header.put("authToken", token);
		method = method.toLowerCase();
		if(method.equals("post")){
			return HttpClientUtil.post(url, header, content);
		}
		else if(method.equals("put")){
			return HttpClientUtil.put(url, header, content);
		}
		else if(method.equals("delete")){
			return HttpClientUtil.delete(url, header, content);
		}
		else{
			return HttpClientUtil.get(url, header, null);
		}
	}
	
	/**
	 * get方式，参数包含在url中
	 * @param url
	 * @param token
	 * @return
	 */
	public static String doAPI(String url, String token){
		Map<String,String> header = new HashMap<String,String>();
		header.put("Content-Type", "application/json");
//		header.put("Accept", "application/json,text/javascript");
		header.put("authToken", token);
		return HttpClientUtil.get(url, header, null);
	}
	
	/**
	 * get方式请求数据
	 * @param url
	 * @param token
	 * @param params 请求参数
	 * @return
	 */
	public static String doAPI(String url, String token, Map params){
		Map<String,String> header = new HashMap<String,String>();
		header.put("Content-Type", "application/json");
//		header.put("Accept", "application/json,text/javascript");
		header.put("authToken", token);
		return HttpClientUtil.get(url, header, params);
	}
}
