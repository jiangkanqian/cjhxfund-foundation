package com.cjhxfund.foundation.util.str;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtil {
	
	/**匹配所有字母A-Za-z+*/
	public static final String MATCH_LETTRE = "[(A-Za-z)]+";
	/**匹配所有小写字母a-z+*/
	public static final String MATCH_LETTRE_LOWER = "[(a-z)]+";
	/**匹配所有大写字母A-Z+*/
	public static final String MATCH_LETTRE_UPPER = "[(A-Z)]+";
	/**匹配所有数字0-9+*/
	public static final String MATCH_INT = "[(0-9)]+";
	/**匹配所有中文+*/
	public static final String MATCH_CHS = "[(\\u4e00-\\u9fa5)]+";
	/**匹配所有常规字符A-Za-z0-9+*/
	public static final String MATCH_WORD = "[(\\w)]+";
	
	/**严格匹配时间字符串格式为：yyyyMMddHHmmss*/
	public static final String MATCH_yyyyMMddHHmmss = "^\\d{14}$";
	
	/**严格匹配时间字符串格式为：yyyyMMddHHmmss*/
	public static final String MATCH_yyyyMMdd = "^\\d{8}$";
	
	/**严格匹配时间字符串格式为：yyyy-MM-dd HH:mm:ss*/
	public static final String MATCH_yyyy_MM_dd_HH_mm_ss = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
	
	/**匹配URL链接*/
	public static final String MATCH_URL = "((((f|ht)tps?:)?//)?([a-zA-Z0-9!#$%&'*+-/=?^_`{|}~]+(:[^ @:]+)?@)?((([a-zA-Z0-9\\-]{1,255}|xn--"
			+ "[a-zA-Z0-9\\-]+)\\.)+(xn--[a-zA-Z0-9\\-]+|[a-zA-Z]{2,6}|\\d{1,3})|localhost|(%[0-9a-fA-F]{2})+|[0-9]+)"
			+ "(:[0-9]{1,5})?([/\\?][^ \\s/]*)*)";
	
	
	/**
	 * 判断字符串是否为常规单词（数字和字母）
	 * @param str 字符串
	 * @param regex 正则
	 * @param contains 是否完全匹配：true: 是，false:否
	 * @return
	 */
	public final static boolean checkStr(String str, String regex, boolean matchType){
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if(matchType){
			return matcher.matches();
		}
		else{
			return matcher.find();
		}
	}
	
	

	
	public static void main(String[] args) {
		
		String str = "http://localhost:8080/wadmin/";
		str = "0123gAd@";
		
		System.out.println("find(): " +checkStr(str, RegexUtil.MATCH_WORD, false));
		System.out.println("matches(): " + checkStr(str, RegexUtil.MATCH_WORD, true));
		
	}
}
