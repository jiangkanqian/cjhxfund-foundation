package com.cjhxfund.foundation.web.model;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.cjhxfund.foundation.annotation.data.DocData;
import com.cjhxfund.foundation.annotation.data.ParamItem;
import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.DataType;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.http.CookieUtil;
import com.cjhxfund.foundation.util.http.HTTP;
import com.cjhxfund.foundation.util.http.RequestMVCUtil;
import com.cjhxfund.foundation.util.http.RespDataUtil;
import com.cjhxfund.foundation.util.http.useragent.UserAgent;
import com.cjhxfund.foundation.util.io.WriteUtil;
import com.cjhxfund.foundation.util.str.JsonUtil;
import com.cjhxfund.foundation.web.context.CommonCache;
import com.cjhxfund.foundation.web.listener.SessionUser;
import com.cjhxfund.foundation.web.model.sys.UserInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * springmvc 数据容器
 * 
 * @author xiejs
 * @date 2015年9月26日
 */
public class HttpCtrl implements Serializable {
	
	private static final long serialVersionUID = 354547826833885440L;
	
	private static JLogger logger = SysLogger.getLogger();
	
	// 将请求参数转化为map对象，方便查询
	private Map<String, Object> queryMap = new HashMap<String, Object>();
	
	// 如果全部确认为String类型的接收数据，使用该Map，如果请求数据格式是xml，就会使用该Map作为该数据容器
	private Map<String, String> queryMapStr = new HashMap<String, String>();
	
	private Object data; // 必要时转化为对象
	private String requestJson;// 将请求参数转化为json
	private String responseJson; // 发送的json数据
	private HttpServletRequest request;//请求对象
	private HttpServletResponse response;//相应对象
	private transient InputStream is;//请求主体中的数据流，数据流只能获取一次，因而需要装载
	private byte[] contentBytes;//浏览器http请求的content部分二进制内容
	private byte[] fileBytes;//单独设置的文件流

	private String requestType = "local";//本地系统请求，还有sys api请求
	private String sessionId = null;
	private CtrlBean ctrlBean;//初始化时讲本次请求的API信息也加进去来
	
	
	
	

	/**
	 * @param request
	 * @param response
	 */
	public HttpCtrl(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.sessionId = request.getSession().getId();
		this.ctrlBean = CommonCache.ctrlBeanCache.get(getCheckKey());
//		this.sessionId = "";
//		String userAgent = request.getHeader("User-Agent");
//		if(DataUtil.isValidStr(userAgent)){
//			//sessionID 为浏览器+ip来定，判断请求是否和法时，以这个为主，但是还需要校验cookie:webToken
//			userAgent = userAgent.replaceAll(" |,|;|\\(|\\)|\\.|/", "");
//			this.sessionId = userAgent + "-" + getRemoteIP();
//		}
	}
	
	/**
	 * 将指定cookie的键值存放到请求参数容器的queryMap中
	 * @param cookieName
	 */
	public void fillCookie(String cookieName){
		Map<String,Object> cookieData = CookieUtil.getCookieToMap(cookieName, this.request);
		if(DataUtil.isValidMap(cookieData)){
			queryMap.putAll(cookieData);
		}
	}
	/**
	 * 删除cookie
	 * @param cookieName
	 */
	public void deleteCookie(String cookieName){
		if(DataUtil.isValidStr(cookieName)){
			CookieUtil.deleteCookie(cookieName, this.request, this.response);
		}
	}
	
	/**
	 * 检查当前请求的json数组元素是否有效
	 * @return
	 */
	public String checkJsonArray(){
		return checkJsonArray(getCheckKey());
	}
	/**
	 * 检查传递过来的数组元素是否有效
	 * @param key API缓存的key
	 * @return
	 */
	public String checkJsonArray(String key){
		DocData checkdb = getDocData(key);
		List<ParamItem> paramList = checkdb.getParams();
		String result = null;
		if(DataUtil.isValidList(paramList)){
			Object value = null;
			JsonArray ja=new JsonParser().parse(getRequestJson()).getAsJsonArray();
			for(int i=0; i<ja.size(); i++){
				JsonObject jo=ja.get(i).getAsJsonObject();
				Map<String, String> propValue = JsonUtil.joToMap(jo);
				for (ParamItem item : paramList) {
					value = propValue.get(item.getField());
					if (value == null) {
						continue;
					}
					result = RequestMVCUtil.checkVal(value.toString(),item);
					if(DataUtil.isValidStr(result)){
						return result;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取当前系统请求地址
	 * @return
	 */
	public String getRequestURI(){
		return this.request.getRequestURI();
	}
	
	/**获取地址全名，http开头*/
	public String getRequestURL(){
		return this.request.getRequestURL().toString();
	}
	/**
	 * 获取当前API缓存的key
	 * @return
	 */
	public String getCheckKey(){
		return getRequestURI()+"-"+getRequestMethod();
	}
	
	/**获取当前请求方法*/
	public String getRequestMethod(){
		return this.request.getMethod();
	}
	
	/**
	 * 获取当前API缓存信息
	 * @return
	 */
	public DocData getDocData(){
		if(ctrlBean == null){
			return null;
		}
		return ctrlBean.getDoc();
	}
	
	public CtrlBean getCtrlBean(){
		return ctrlBean;
	}
	
	public CtrlBean getCtrlBean(String key){
		return CommonCache.ctrlBeanCache.get(key);
	}
	
	/**获取指定接口的缓存信息*/
	public DocData getDocData(String key){
		CtrlBean bean = getCtrlBean(key);
		if(bean == null){
			return null;
		}
		return bean.getDoc();
	}
	
	/**
	 * 判断请求是否是ajax请求
	 * @return
	 */
	public boolean isAjax() {
		String header = request.getHeader("x-requested-with");
        if (StringUtils.isEmpty(header)) {
            return false;
        }
        return "XMLHttpRequest".equals(header);
    }
	
	public void loadDownFile(String filePath, String showName){
		WriteUtil.sendLoadDownFile(filePath, showName, getResponse());
	}
	
	public void addCookie(Cookie c) {
		this.response.addCookie(c);
	}
	
	/**
	 * 添加cookie 
	 * @param name cookie名称
	 * @param value 数据
	 * @param maxAge 最大期限
	 * @param path 路径，一般是网站的根目录
	 */
	public void addCookie(String name,String value, int maxAge, String path) {
		Cookie c = new Cookie(name, value);
		c.setMaxAge(maxAge);
		c.setPath(path);
		addCookie(c);
	}

	public Map<String, String> getQueryMapStr() {
		return queryMapStr;
	}

	public void setQueryMapStr(Map<String, String> queryMapStr) {
		this.queryMapStr = queryMapStr;
	}
	
	public Map<String, String> toQueryMapStr() {
		for(Entry<String,Object> entry : queryMap.entrySet()){
			this.queryMapStr.put(entry.getKey(), entry.getValue().toString());
		}
		return this.queryMapStr;
	}
	
	/**
	 * 判断请求的类型：json,xml,form等；
	 * @return
	 */
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}


	/**
     * 获取浏览器http请求的content部分二进制内容
     */
    public byte[] getContentBytes() throws Throwable {
        if (null == contentBytes) {
            int len = getRequest().getContentLength(); // 注意，某些客户端请求信息不完整，此处的长度仅供参考，不可以真的相信客户端传入的大小
            if (len == -1) { // -1表示没有body数据,返回空字符串
                contentBytes = new byte[0];
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(len);
                InputStream in = getRequest().getInputStream();
                int tmp;
                while ((tmp = in.read()) != -1) {
                    baos.write(tmp);
                }
                contentBytes = baos.toByteArray();
            }
        }
        return contentBytes;
    }




    /**
     * 获取文件上传的二进制数组
     * @return
     */
	public byte[] getFileBytes() {
		return fileBytes;
	}


	public void setFileBytes(byte[] fileBytes) {
		this.fileBytes = fileBytes;
	}

	public HttpServletRequest getRequest() {
		return request;
	}


	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpCtrl() {}
	
	public HttpCtrl(Map<String, Object> queryMap, String requestJson) {
		this.queryMap = queryMap;
		this.requestJson = requestJson;
	}

	public HttpCtrl(Map<String, Object> queryMap, String requestJson, String responseJson) {
		this.queryMap = queryMap;
		this.requestJson = requestJson;
		this.responseJson = responseJson;
	}

	public HttpCtrl(Map<String, Object> queryMap, Object data, String requestJson,
			String responseJson) {
		this.queryMap = queryMap;
		this.data = data;
		this.requestJson = requestJson;
		this.responseJson = responseJson;
	}
	
	
	/**
	 * 获取整个请求的json字符串
	 * @return
	 */
	public String getRequestJson() {
		return requestJson;
	}

	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String data) {
		this.responseJson = data;
		if (data.length() > 100) {
			logger.info("ajax data：" + data.subSequence(0, 98));
		} else {
			logger.info("ajax data：" + data);
		}
	}

	public Map<String, Object> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, Object> queryMap) {
		this.queryMap = queryMap;
	}
	
	/**
	 * 请求中是否包含该参数
	 * @param paramName
	 * @return
	 */
	public boolean containsParam(String paramName){
		return this.queryMap.containsKey(paramName);
	}
	
	/**
	 * 请求中是否不包含该参数
	 * @param paramName
	 * @return
	 */
	public boolean withoutParam(String paramName){
		return !containsParam(paramName);
	}
	
	public void addParam(String key, Object value){
		this.queryMap.put(key, value);
	}
	public void removeParam(String key){
		this.queryMap.remove(key);
	}

	public UserInfo getUserInfo() {
		return SessionUser.getUserInfo(getSessionId());
	}
	
	public Long getOpId() {
		return getAsLong("opId");
	}
	
	/**
	 * 获取请求中的数据流
	 * @return
	 */
	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	//以下三种字符串转换为对象，80%的情况会用到，如果有特殊数据，另外从新设计
	/**将json字符串转为具体的一个对象*/
	public <T> T jsonToObj(Class<T> clazz){
		return JsonUtil.jsonToObj(getRequestJson(), clazz);
	}
	
	/**将json数组字符串转为list*/
	public <T> List<T> jsonArrToList(Class<T> clazz ){
		return JsonUtil.jsonToList(getRequestJson(), clazz);
	}
	
	/**将json数组字符串转为list*/
	public <T> List<T> jsonToList(String json , Class<T> clazz){
		return JsonUtil.jsonToList(json, clazz);
	}
	
	/**将json数组字符串转为list对象，并验证，在验证不符合要求的数据后，不再执行后面的数据*/
	public <T> String jsonToListObj(List<T> data,Class<T> clazz, String key){
		Object value = null;
		String result = null;
		DocData checkdb = getDocData(key);
		List<ParamItem> paramList = checkdb.getParams();
		JsonArray ja=new JsonParser().parse(getRequestJson()).getAsJsonArray();
		for(int i=0; i<ja.size(); i++){
			JsonObject jo=ja.get(i).getAsJsonObject();
			Map<String, String> propValue = JsonUtil.joToMap(jo);
			for (ParamItem item : paramList) {
				value = propValue.get(item.getField());
				if (value == null) {
					continue;
				}
				result = RequestMVCUtil.checkVal(value.toString(),item);
				if(DataUtil.isValidStr(result)){
					return result;
				}
			}
			data.add(JsonUtil.jsonToObj(jo.toString(), clazz));
		}
		return result;
	}
	
	//以下为发送数据的方法，基本上都是用json发送，如果有其他的方式，请使用RespDataUtil
	/**
	 * 发送数据
	 * @param obj
	 * @param total total为0，就意味着只是返回obj的json数据，如果不为0，则返回easyui datagrid的数据格式
	 */
	public void sendJson(Object obj,  int total){
		setResponseJson(JsonUtil.getEasyUIDgData(obj, total));
	}
	
	/**发送数据，任何对象类型，将转为json字符串发送*/
	public void sendJson(Object obj){
		if(obj != null){
			setResponseJson(JsonUtil.toJson(obj));
		}
		else{
			sendJson(0, "no Data！");
		}
	}
	
	/**
	 * 对外统一提供的数据格式！，默认是成功返回的数据
	 * @param data 数据对象，如果数据为空则会返回错误提示消息，见respVo的setData方法
	 */
	public void sendData( Object data){
		RespVo resp = new RespVo(data);
		setResponseJson(JsonUtil.toJson(resp));
	}
	
	/**
	 * 对外统一提供的数据格式！，默认是成功返回的数据
	 * @param msg 消息提示
	 * @param data 数据对象，如果数据为空则会返回错误提示消息，见respVo的setData方法
	 */
	public void sendData(String msg, Object data){
		RespVo resp = new RespVo(msg, data);
		setResponseJson(JsonUtil.toJson(resp));
	}
	
	/**
	 * 对外统一提供的数据格式
	 * @param code 错位代码
	 * @param msg 消息提示
	 * @param data 数据对象，
	 */
	public void sendData(int code, String msg, Object data){
		RespVo resp = new RespVo(msg, data, code);
		setResponseJson(JsonUtil.toJson(resp));
	}
	
	/**
	 * 下载文件数据
	 * @param fis 文件流
	 * @param fileName 下载显示的文件名
	 */
	public void sendFile(InputStream fis, String fileName){
		WriteUtil.sendLoadDownFile(fis, fileName, getResponse());
	}
	
	/**
	 * 下载文件数据
	 * @param fileFullName 文件地址
	 * @param fileName 下载显示的文件名
	 */
	public void sendFile(String fileFullName, String fileName){
		WriteUtil.sendLoadDownFile(fileFullName, fileName, getResponse());
	}
	
	
	/**
	 * 发送消息主体和判断状态
	 * @param code 状态码
	 * @param msg 消息内容
	 */
	public void sendJson(int code,String msg){
		String respJson="{\"msg\":\""+msg+"\",\"code\":"+code+"}";
		setResponseJson(respJson);
	}
	
	public void sendJson(int code,BaseModel msg){
		String respJson="{\"msg\":"+JsonUtil.toJson(msg)+",\"code\":"+code+"}";
		setResponseJson(respJson);
	}
	
	/**
	 * 返回对应的数据，可能用于将状态码和msg互换，避免被页面的ajax函数拦截
	 * @param code 状态码
	 * @param msg 消息内容
	 */
	public void sendJson(String code,String msg){
		String respJson="{\"msg\":\""+msg+"\",\"code\":"+code+"}";
		setResponseJson(respJson);
	}
	
	
	public void sendJson(String data){
		if(StringUtils.isNotEmpty(data)){
			setResponseJson(data);
		}
	}
	
	/**
	 * 发送Msg对象数据，
	 * @param code 代码 1：正常，0：错误
	 * @param msg 一般是Java对象或者集合
	 */
	public void sendJson(int code,Object msg){
		String respJson="{\"msg\":"+JsonUtil.toJson(msg)+",\"code\":"+code+"}";
		setResponseJson(respJson);
	}
	
	/**
	 * 返回对应的数据
	 * @param code 状态信息
	 * @param msg	 消息内容
	 * @param result	消息结果
	 */
	public void sendJson(int code,String msg,String result){
		String respJson= "{\"result\":\""+result+"\",\"msg\":\""+msg+"\",\"code\":"+code+"}";
		setResponseJson(respJson);
	}
	
	/**
	 * 返回对应的数据，可能用于将状态码和msg互换，避免被页面的ajax函数拦截
	 * @param code 状态码
	 * @param msg	 消息内容
	 * @param result	消息结果
	 */
	public void sendJson(String code,String msg,String result){
		String respJson= "{\"result\":\""+result+"\",\"msg\":\""+msg+"\",\"code\":\""+code+"\"}";
		setResponseJson(respJson);
	}
	
	public void sendJson(CodeTipMsg tip,String msg){
		setResponseJson(tip.toString(msg));
	}
	
	public void sendData(String data,String contentType){
		if (data.length() > 100) {
			logger.info("ajax data：" + data.subSequence(0, 98) + "……");
		} else {
			logger.info("ajax data：" + data + "……");
		}
		RespDataUtil.respData(getResponse(), data, contentType);
	}
	
	
    
    /**
     * 获取请求来源客户端的IP，如果是转发请求会从header中获取原始IP，一般建议用本方法
     */
    public String getRemoteIP() {
	  return HTTP.getIpAddr(getRequest()); 
    }
	
    /**
     * 获取请求来源客户端的端口
     */
    public int getRemotePort() {
        return getRequest().getRemotePort();
    }
    
    /**
     * 获取请求来源客户端的IP和端口
     */
    public String getRemoteIPAndPort() {
        return getRemoteIP() + ":" + getRemotePort();
    }

    /**
     * 获取客户端请求的服务器的IP，在多网卡的服务器上可用于判断用户从哪个线路请求进来
     */
    public String getLocalIP() {
        return request.getLocalAddr();
    }

    /**
     * 获取客户端请求的服务器的端口
     */
    public int getLocalPort() {
        return request.getLocalPort();
    }

    /**
     * 获取客户端请求的服务器的IP和端口
     */
    public String getLocalIPAndPort() {
        return getLocalIP() + ":" + getLocalPort();
    }

    /**
     * 返回给前端的HTTP响应码
     */
    public String getStatus() {
        return String.valueOf(getStatusInt());
    }
    
    public UserAgent getUserAgent() {
    	String agentStr = this.request.getHeader("User-Agent");
    	UserAgent agent = UserAgent.parseUserAgentString(agentStr);
    	return agent;
    }

    /**
     * 返回给前端的HTTP响应码，数字形式
     */
   public int getStatusInt() {
        return response.getStatus();
    }
    
    public Long getAsLong(String paramName){
    	Long data = DataType.getAsLong(getQueryMap().get(paramName));
    	return data;
    }
    
    public Integer getAsInteger(String paramName){
    	Integer data = DataType.getAsInt(getQueryMap().get(paramName));
    	return data;
    }
    
    public Double getAsDouble(String paramName){
    	Double data = DataType.getAsDouble(getQueryMap().get(paramName));
    	return data;
    }
    
    public String getAsString(String paramName){
    	if(getQueryMap().get(paramName) == null){
    		return null;
    	}
    	return getQueryMap().get(paramName).toString().trim();
    }
    
    
	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public HttpSession getSession() {
		return this.request.getSession();
	}
	
	
	
//	public HttpSession getSession(){
//		return this.request.getSession();
//	}
	
	


	

	

}
