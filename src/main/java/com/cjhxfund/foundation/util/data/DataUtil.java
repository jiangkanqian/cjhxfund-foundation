package com.cjhxfund.foundation.util.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.SerializationUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cjhxfund.foundation.util.reflect.ClassUtil;
import com.cjhxfund.foundation.util.str.JsonUtil;



/**
 * 处理 数据的转换，验证，并返回结果
 * @author Xiejs
 * @date 2015年1月5日
 * @import：都是java自带的jar包
 * 
 */
public class DataUtil {

	/**
	 * 判断字符串是否为空或者null
	 * @param str 要验证的参数
	 * @return boolean
	 */
	public static boolean isValidStr(String str) {
		if (null == str ||str.length()==0|| "".equals(str) || "null".equals(str)) {
			return false;
		}
		return true;
	}
	
	public static boolean isEmptyStr(String str) {
		return !isValidStr(str);
	}

	/**
	 * 判断Map是否为空或者null
	 * @param Map:要验证的参数
	 * @return boolean
	 */
	public static boolean isValidMap(Map map) {
		if (null == map || map.size() == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 将list对象集合转化为map
	 * @author Jason
	 * @date 2016年4月29日
	 * @param vList 对象集合
	 * @param kName 对象中的某一属性名称
	 * @return Map集合
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> listToMap(List<V> vList, String kName) {
		Map<K, V> map = new HashMap<K, V>();
		if (vList == null || kName == null || vList.size() == 0) {
			return map;
		}
		Field kField = ClassUtil.getField(vList.get(0).getClass(), kName);
		for (V v : vList) {
			try {
				map.put((K) kField.get(v), v);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	
	/**
	 * 过滤掉value为空或者为null的键值
	 * @param map
	 */
	public static void filterMap(Map map){
		if(DataUtil.isValidMap(map)){
			Iterator<Map.Entry<Object, Object>> it = map.entrySet().iterator();
			while(it.hasNext()){
				Entry<Object,Object> entry=it.next();
				if(!DataUtil.isValidStr(entry.getValue().toString())){
					it.remove();
				}
			}
		}
	}

	/**
	 * 首字母大写
	 * @param src
	 * @return
	 */
	public static String upperFirestChar(String src) {
		if(!isValidStr(src)){
			return "";
		}
		return src.substring(0, 1).toUpperCase().concat(src.substring(1));
	}

	/**
	 * 首字母小写
	 * @param src
	 * @return
	 */
	public static String lowerFirestChar(String src) {
		if(!isValidStr(src)){
			return "";
		}
		return src.substring(0, 1).toLowerCase().concat(src.substring(1));
	}
		
	/**
	 * 如果list为null，或者size=0，则返回false
	 * @param list
	 * @return boolean
	 */
	public static boolean isValidList(List list) {
		if (null == list || list.size() == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 如果set为null，或者size=0，则返回false
	 * @param set
	 * @return boolean
	 */
	public static boolean isValidSet(Set set) {
		if (null == set || set.size() == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 如果set为null，或者size=0，则返回false
	 * @param set
	 * @return boolean
	 */
	public static boolean isValidArr(Object[] arr) {
		if (null == arr || arr.length == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 做测试用，分行打印数据
	 * @param objs
	 */
	public static <T> void print(List<T> objs) {
		if(DataUtil.isValidList(objs)){
			for (T obj : objs) {
				System.out.println(JsonUtil.toJson(obj));
			}
		}
	}
	
	/**
	 * 做测试用，分行打印数据
	 * @param objs
	 */
	public static <T> void print(Set<T> objs) {
		for (T obj : objs) {
			System.out.println(JsonUtil.toJson(obj));
		}
	}

	/**
	 * 做测试用，分行打印数据
	 * @param objs
	 */
	public static <T> void print(T[] objs) {
		for (T obj : objs) {
			System.out.println(JsonUtil.toJson(obj));
		}
	}

	/**
	 * 做测试用，分行打印数据
	 * @param objs
	 */
	public static void print(Map map) {
		Iterator it = map.entrySet().iterator();
//		System.out.println(map.entrySet().size());
		String key;
		Object value;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			key = entry.getKey().toString();
			value = entry.getValue();
			if(value instanceof List){
				System.out.println("size:" + ((List)value).size());
			}
			System.out.print(key + ":");
			System.out.println( JsonUtil.toJson(value));
		}
	}
	
	

	/**
	 * 计算text的长度（一个中文算两个字符）
	 * @param text
	 * @return int： text的长度
	 */
	public static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length / 2;
	}

	

	/** 深复制 */
	public static Object deepClone(Object o) {
		/* 写入当前对象的二进制流 */
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(o);
			/* 读出二进制流产生的新对象 */
			bis = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** 深复制 更加保险 */
	public static Object clone(Serializable o){
		return SerializationUtils.clone(o);
	}

	

	/**
	 * 将前台传过来的ids字符串转换成List<Object> param str 要转换的字符串 param split 字符串str按照split切分
	 * 
	 */
	public static List<String> strToList(String str, String split) {
		if (str == null || str == "") {
			return null;
		}
		List<String> ids = new ArrayList<String>();
		String[] list = str.split(split);
		for (int i = 0; i < list.length; i++) {
			ids.add(list[i].trim());
		}
		return ids;

	}
	


	/**
	 * 服务： 字符串是否含数字
	 * @auther xiejason
	 * @param content
	 */
	public static boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;

	}
	
	public static boolean isInt(String num){
		if (!DataUtil.isValidStr(num)) {
			return false;
		}
//		return StringUtils.isNumeric(num);
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(num).matches();
	}

	/**
	 *  判断字符串str是否是数字，各种数字
	 * @auther xiejason
	 * @param str
	 */
	public static boolean isNumeric(String str) {
		if (!DataUtil.isValidStr(str)) {
			return false;
		}
		boolean isNum = false;
		Pattern pattern = Pattern.compile("[0-9]*");
		if (pattern.matcher(str).matches()) {
			isNum = true;
		} else if (str.contains(".") || (str.contains("-")&&!str.trim().equals("-"))) {
			String[] strArr = str.split("\\.");
			String[] strArr2 = str.split("-");
			int isNav = str.indexOf("-");
			int isNav2 = str.indexOf(".");
			if (strArr.length > 2 || strArr2.length > 2 || isNav > 0 || isNav2 == 0 || isNav2 == str.length() - 1) {
				isNum = false;
			} else {
				String str1 = str.replace(".", "");
				String str2 = str.replace("-", "");
				String str3 = str1.replace("-", "");
				if (pattern.matcher(str1).matches()) {
					isNum = true;
				}
				if (pattern.matcher(str2).matches()) {
					isNum = true;
				}
				if (pattern.matcher(str3).matches()) {
					isNum = true;
				}
			}
		}
		return isNum;
	}

	/**
	 * 如果字符串为空或者null，则返回null，否则返回本身
	 * @author xiejs
	 * @date 2015年5月6日
	 * @param str
	 * @return null，或则str.trim()
	 */
	public static String getStr(String str) {
		if (null == str || str.trim().equals("")) {
			return null;
		} else {
			return str.trim();
		}
	}
	

	/**
	 * 
	 * @author xiejs
	 * @date 2015年5月6日
	 * @return 随机字符串，32位
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}


	/**
	 * 
	 * 判断字符串是有汉字
	 * @auther xiejason
	 * @param str
	 * @param isAll true:判断该字符串是否是汉字，false:判断该字符串是否包含汉字
	 */
	public static boolean hasChinese(String str, boolean isAll) {
		Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher m = p_str.matcher(str);
		if (isAll) {
			if (m.find() && m.group(0).equals(str)) {
				return true;
			}
			return false;
		}
		else{
			if (m.find()) {
				return true;
			}
			return false;
		}
	}
	
	/**
	 * 获取随机数字串
	 * @author xiejs
	 * @date 2015年5月6日
	 * @param len 返回字符创的长度
	 * @return 随机数字，长度为len
	 */
	public static String getRandNum(int len) {
		StringBuilder randStr = new StringBuilder();
		Random random = new Random();
		for(int i = 0; i < len; i++) {
			randStr.append(random.nextInt(10));
		}
		
		return randStr.toString();
	}
	
	/**
	 * 获取随机字符串，有字母大小写和数字组成
	 * @author xiejs
	 * @date 2015年5月6日
	 * @return 随机字符串，长度为len
	 */
	public static String getRandStr(int len){
		StringBuilder buf = new StringBuilder("a,b,c,d,e,f,g,h,i,g,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");// 26个
		buf.append(",A,B,C,D,E,F,G,H,I,G,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z");
//		buf.append(",~,@,#,$,%,^,&,*,(,),_,+,|,`,.");//15个
		buf.append(",1,2,3,4,5,6,7,8,9,0");//10 个
		String[] arr = buf.toString().split(",");// 
		StringBuilder b = new StringBuilder();
 
		java.util.Random r;
		int k ;
		for(int i=0;i<len;i++){
			 r = new java.util.Random();
			 k = r.nextInt();
			 b.append(String.valueOf(arr[Math.abs(k % 61)]));//字符数-1
		}
		return b.toString();
	}
	
	/**
	 * 获取随机字符串，有字母大小写和数字组成和特殊字符组成
	 * @author xiejs
	 * @date 2015年5月6日
	 * @return 随机字符串，长度为len
	 */
	public static String getRandStrWith$(int len){
		StringBuilder buf = new StringBuilder("a,b,c,d,e,f,g,h,i,g,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");// 26个
		buf.append(",A,B,C,D,E,F,G,H,I,G,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z");
		buf.append(",~,@,#,$,%,^,&,*,(,),_,+,|,`,.");//15个
		buf.append(",1,2,3,4,5,6,7,8,9,0");//10 个
		String[] arr = buf.toString().split(",");// 
		StringBuilder b = new StringBuilder();
		java.util.Random r;
		int k ;
		for(int i=0;i<len;i++){
			r = new java.util.Random();
			k = r.nextInt();
			b.append(String.valueOf(arr[Math.abs(k % 75)]));//字符数-1
		}
		return b.toString();
	}
	
	
	/**
	 * 获取异常信息.
	 * @author xiejiesheng
	 * @date 2015年5月25日
	 * @param t catch参数 如：catch(Throwable t){log.error(getTrace(t);}
	 * @return String 抛出的异常信息
	 */
	public static String getExceptionTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
	
	/**
	 * 对比两个字符串，如果都是null，返回true;如果其中一个是null 则直接返回false，否则返回对比后的值
	 * @author xiejs
	 * @date 2015年9月19日
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isEqual(String str1, String str2){
		if(str1==null && str2!=null){
			return false;
		}
		else if(str1!=null && str2==null){
			return false;
		}
		else if(str1 == null && str2==null){
			return true;
		}
		else{
			return str1.equals(str2);
		}
	}
	

	/**
	 * 对汉字字符串数组进行排序，但是只适用常规的汉字，收归在GB2313里面的汉字，大约7000多个
	 * @param arr
	 */
	public static void sortChinese(String[] arr){
		Arrays.sort(arr, Collator.getInstance(Locale.CHINA));
	}
	
	public static void parseXML(String xml, Map<String,String> container) throws DocumentException{
		if(xml.startsWith("<") && xml.endsWith(">")){
			
			SAXReader reader = new SAXReader();
			Document document = reader.read(xml);
			Element root = document.getRootElement();
			// 得到根元素的所有子节点
			@SuppressWarnings("unchecked")
			List<Element> elementList = root.elements();
			// 遍历所有子节点
			for (Element e : elementList){
				container.put(e.getName(), e.getText());
			}
		}
	}
	
	public static Map<String,String> parseXML(String xml) throws DocumentException, UnsupportedEncodingException{
		Map<String,String> container = new HashMap<String,String>();
		if(xml.startsWith("<") && xml.endsWith(">")){
			SAXReader reader = new SAXReader();
			Document document = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			Element root = document.getRootElement();
			// 得到根元素的所有子节点
			@SuppressWarnings("unchecked")
			List<Element> elementList = root.elements();
			// 遍历所有子节点
			for (Element e : elementList){
				container.put(e.getName(), e.getText());
			}
		}
		return container;
	}
	
	
	
	    
	public static void main(String[] args) {
		System.out.println(isInt("32"));
		System.out.println(getRandStrWith$(20));
	}

	/**
	 * 转换性别
	 * @author xiejs
	 * @date 2015年10月22日
	 * @param sex
	 * @return
	 */
	public static String getSex(Integer sex) {
		if(sex == null){
			return "";
		}
		if(sex.intValue() == 0){
			return "未知";
		}
		else if(sex.intValue() == 1){
			return "男";
		}
		else if(sex.intValue() == 2){
			return "女";
		}
		else if(sex.intValue() == 3){
			return "妖妖";
		}
		return "";
	}


}
