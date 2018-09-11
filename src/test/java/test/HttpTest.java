package test;

import java.io.File;
import java.util.List;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.http.RequestUtil;
import com.cjhxfund.foundation.util.io.ReadFileUtil;

public class HttpTest {

	public static void main(String[] args) {
		String url = "http://192.168.23.1:8080/sysmgr/sysmgr/util/getAccessToken.action";
		 url = "http://192.168.23.1:8080/sysmgr/sysmgr/dictType/delete.action";
//		Map<String,> header = new HashMap<String,String>();
//		String token = "Stringa77b18a38fb44078a51ca46281471d7e";
//		header.put("Cookie", "JSESSIONID="+token);
//		header.put("Content-Type", "application/json,ext/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
////		header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
////		header.put("Upgrade-Insecure-Requests", "1");
//		header.put("Accept", "application/json,text/javascript");
//		String result = RequestUtil.httpRequest(url,header);
//		System.out.println(result);
//		System.out.println(token);
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("typeName", "这是个问题");
//		params.put("typeNo", "123");
//		params.put("typeId", "123");
//		List<Long> idList = new ArrayList<Long>();
//		idList.add(1L);
//		idList.add(2L);
////		String result = HttpClientUtil.post(url, header, JsonUtil.toJson(params));
//		String result = HttpClientUtil.delete(url, JsonUtil.toJson(idList));
//		System.out.println(result);
//		 Map<String,Object> params = new HashMap<String,Object>();
//		 params.put("age", 123);
//		 params.put("name", "jason");
//		url = "http://tesets";
//		 url = RequestUtil.doFormUrlEncode(url, params, null);
//		 System.out.println(url);
//		 UserInfo user = new UserInfo();
//		 user.setOp(0, 100L);;
//		 LanhanSessionContext.createSession("123123", user);
////		 LanhanSessionContext.invalid("123123");
//		 new SessionListener().start();
//		 LanhanSessionContext.createSession("1231234", user);
//		 LanhanSessionContext.createSession("1231235", user);
//		 UserInfo user2 = new UserInfo();
//		 user2.setOp(0, 1000L);;
//		 LanhanSessionContext.createSession("12312356", user2);
		 
//		 System.out.println(SpringContext.getLocalIp());
		 
//		 List<String> ipList = ReadFileUtil.readLines(new File("e:/ipConfig.txt"));
		 
//		 System.out.println(ipList);
//		 System.out.println(DataUtil.getRandStr(6));
		 url = "http://10.201.200.44:8078/ipss/";
		 boolean isvalid = RequestUtil.isValidLink(url);
		 System.out.println(isvalid);
		 SysLogger.close();
		 
	}
}
 