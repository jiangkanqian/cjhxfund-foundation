package test;

import com.cjhxfund.foundation.util.http.RequestUtil;



/**
 * FLogger吞吐量测试类
 * @author yunfeng.cheng
 * @create 2017-02-08
 */
public class FloggerThroughputTest {
	
	public static void main(String[] args){
		String url = "http://localhost:8080";
		url = "http://10.201.235.43:8078/ips";
		boolean result = RequestUtil.isValidLink(url);
		System.out.println(result);
	}

}
