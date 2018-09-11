package com.cjhxfund.foundation.util.http;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.str.EncodeUtil;


public class CookieUtil {
	
	//根据cookie name删除cookie 
	public static void deleteCookie(String cookieName, HttpServletRequest request,HttpServletResponse response){
		Cookie[] cookies = request.getCookies(); 
		Cookie c = null ;
	       for(int i=0;i<cookies.length;i++){
	           c = cookies[i] ;
	           if(c.getName().equals(cookieName)){
	              c.setMaxAge(0);
	              response.addCookie(c) ;
	           }
	       }
	}
	
	//根据cookie name和path删除cookie 
	public static void deleteCookie(String cookieName, String path, HttpServletRequest request,HttpServletResponse response){
		Cookie[] cookies = request.getCookies(); 
		Cookie c = null ;
		for(int i=0;i<cookies.length;i++){
			c = cookies[i] ;
			if(c.getName().equals(cookieName)){
				c.setMaxAge(0);
				c.setPath(path);
				response.addCookie(c) ;
			}
		}
	}
	
	//根据cookie name获取cookie
	public static Cookie getCookie(String cookieName,HttpServletRequest request){
		Cookie[] cookies = request.getCookies(); 
		if(cookies==null||cookies.length==0){
			return null;
		}
		else{
			for(int i=0;i<cookies.length;i++){
				if(cookies[i].getName().equals(cookieName)){
					return cookies[i];
				}
			}
			return null;
		}
	}
	
	//根据cookie name获取cookie value
	public static String getCookieValue(String cookieName,HttpServletRequest request){
		Cookie cookie = getCookie(cookieName,request);
		if(cookie != null){
			return cookie.getValue();
		}
		return null;
	}
	
	/**
	 * 根据cookie name获取cookie,并转为 map对象
	 * @author xiejs
	 * @date 2015年10月26日
	 * @param cookieName
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getCookieToMap(String cookieName,HttpServletRequest request){
		Map<String, Object> map = null;
		Cookie[] cookies = request.getCookies(); 
		if(cookies!=null&&cookies.length!=0){
			String value = null;
			for(int i=0;i<cookies.length;i++){
				if(cookies[i].getName().equals(cookieName)){
					value = cookies[i].getValue();
				}
			}
			if(value!=null){
				String[] strArr = value.split(",");
				String[] arrInfo = null;
				map = new HashMap<String,Object>();
				for(int i=0; i<strArr.length;i++){
					arrInfo = strArr[i].split(":");
					if(arrInfo!=null && arrInfo.length>1){
						if (DataUtil.isInt(value)) {
							map.put(arrInfo[0], Long.parseLong(arrInfo[1]));
						}
						else if(DataUtil.hasChinese(value,false)){
							map.put(arrInfo[0], EncodeUtil.decodeUTF8(arrInfo[1].toString()));
						}
						else {
							map.put(arrInfo[0], arrInfo[1]);
						}
						
					}
				}
			}
		}
		return map;
	}
	
	public static void testCookies(Cookie[] cookies){
		if (cookies!=null){
			for(int i=0;i<cookies.length;i++){
				Cookie cookie = cookies[i];
				System.out.println("cookie name:"+cookie.getName()+"  value:"+cookie.getValue()+"  expires:"+cookie.getMaxAge());
//				System.out.println();
			}
		}
		
	}

}
