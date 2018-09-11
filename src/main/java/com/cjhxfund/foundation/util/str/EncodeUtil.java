package com.cjhxfund.foundation.util.str;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * 采用MD5加密解密
 * @author 谢杰生
 * @datetime 2011-10-13
 */
public class EncodeUtil {

	private EncodeUtil(){
		throw new Error("该类不能被实例化！");
	}
	
	/**
	 * MD5加码 生成32位md5码,不可逆的
	 * 
	 */
	//不可逆的
	public final static String md5Encode(String s) {      
		  char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',      
		    'a', 'b', 'c', 'd', 'e', 'f' };      
		  try {      
			   byte[] strTemp = s.getBytes();      
			   MessageDigest mdTemp = MessageDigest.getInstance("MD5");      
			   mdTemp.update(strTemp);      
			   byte[] md = mdTemp.digest();      
			   int j = md.length;      
			   char str[] = new char[j * 2];      
			   int k = 0;      
			   for (int i = 0; i < j; i++) {      
			    byte byte0 = md[i];      
			    str[k++] = hexDigits[byte0 >>> 4 & 0xf];      
			    str[k++] = hexDigits[byte0 & 0xf];      
		   }      
			   return new String(str);      
		  } catch (Exception e) {    
			  return null;      
		  }      
		} 
	
	/**
	 * MD5加码 生成32位md5码,不可逆的
	 */
	public static String strToMD5(String inStr){
		MessageDigest md5 = null;
		try{
			md5 = MessageDigest.getInstance("MD5");
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++){
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */ 
	public static String convertMD5(String inStr){
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++){
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}
	
	
	/**
	 * utf-8 解码
	 * @param source
	 * @return
	 */
	public static String encodeUTF8(String source) {
		String result = source;
		try {
//			source = StringEscapeUtils.escapeJson(source);
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * utf-8 解码
	 * @param source
	 * @return
	 */
	public static String decodeUTF8(String source) {
		String result = source;
		try {
//			System.out.println(source);
			result = java.net.URLDecoder.decode(source, "gbk");
//			result = HtmlUtils.htmlUnescape(result);
//			System.out.println(result);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	

	// 测试主函数
	public static void main(String args[]) {
		String s = new String("46546");
		System.out.println("原始：" + s);
//		System.out.println("不可逆：" + md5Encode(s));
//		System.out.println("MD5后：" + string2MD5(s));
		System.out.println("加密的：" + convertMD5(s));
		System.out.println("解密的：" + convertMD5(convertMD5(s)));
		System.out.println("解密的：" + convertMD5("@BA@1"));
		System.exit(0);
	}
}


