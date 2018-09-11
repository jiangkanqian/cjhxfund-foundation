package test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cjhxfund.foundation.util.str.JsonUtil;

public class LogTest {
	
	final static Logger logger = LoggerFactory.getLogger(LogTest.class);

	private static void testParams(String str1, String...strings){
		System.out.println(str1);
		if(str1.contains("{}")){
			String[] strArr = str1.split("\\{\\}");
//			System.out.println(Arrays.toString(strArr));
			StringBuffer buf = new StringBuffer();
			System.out.println(strArr.length);
			for(int i=0, length = strings.length; i<length; i++){
				buf.append(strArr[i]).append(strings[i]);
			}
			System.out.println(buf.toString());
		}
		
//		System.out.println(Arrays.toString(strings));
	}
	
	public static void main(String[] args) {
		String json = "[1,2,3,5,8]";
		List<Integer> intList = JsonUtil.jsonToList(json, Integer.class);
		System.out.println(intList);
		json = "[1.2,2.4,3,5.6,8]";
		List<Double> doubleList = JsonUtil.jsonToList(json, Double.class);
		System.out.println(doubleList);
		List<String> strList = JsonUtil.jsonToList(json, String.class);
		System.out.println(strList);
	}
	
	
	
}
