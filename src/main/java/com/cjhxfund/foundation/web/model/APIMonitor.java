package com.cjhxfund.foundation.web.model;

import com.cjhxfund.foundation.util.str.DateUtil;

/**
 * @author xiejiesheng
 * 系统API监控信息
 */
public class APIMonitor {

	private String apiName;
	private String url;//api url，运行时不需要赋值，落地时需要
	private String method;
	
	private String lastReqIP;// 最后访问API；
	private int status;// 运行状况，0：停止运行，1：运行正常，2：调试运行
	private long lastReqTime;// 最后访问时间
	private long minRespTime;// 最小响应时间
	private long maxRespTime;// 最大响应时间
	private int requestTimes;// 当天请求次数

	private String errorMsg;//错误简要消息
	private String errorStack;//错误堆栈
	
	@SuppressWarnings("unused")
	private String lastReqTimeFormat;//最后访问时间格式化
	
	public APIMonitor() {
		
	}
	
	public APIMonitor clone(String method, String apiName, String url) {
		APIMonitor monitor = new APIMonitor();
		monitor.setUrl(url);
		monitor.setMethod(method);
		monitor.setApiName(apiName);
		monitor.setErrorMsg(this.errorMsg);
		monitor.setErrorStack(this.errorStack);
		monitor.setLastReqIP(this.lastReqIP);
		monitor.setLastReqTime(this.lastReqTime);
		monitor.setMinRespTime(this.minRespTime);
		monitor.setMaxRespTime(this.maxRespTime);
		monitor.setRequestTimes(this.requestTimes);
		monitor.setStatus(this.status);
		monitor.setLastReqTimeFormat(this.getLastReqTimeFormat());
		return monitor;
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public APIMonitor(int status) {
		this.status = status;
	}

	public String getLastReqIP() {
		return lastReqIP;
	}

	public void setLastReqIP(String lastReqIP) {
		this.lastReqIP = lastReqIP;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getLastReqTime() {
		return lastReqTime;
	}

	public void setLastReqTime(long lastReqTime) {
		this.lastReqTime = lastReqTime;
	}

	public long getMinRespTime() {
		return minRespTime;
	}

	public void setRespTime(long respTime) {
		this.minRespTime = respTime;
		if(this.minRespTime == 0L && this.maxRespTime == 0L){
			this.minRespTime = respTime;
			this.maxRespTime = respTime;
		}
		else{
			if(respTime > this.maxRespTime){
				this.maxRespTime = respTime;
			}
			else if(respTime < this.minRespTime){
				this.minRespTime = respTime;
			}
		}
	}

	public long getMaxRespTime() {
		return maxRespTime;
	}


	public int getRequestTimes() {
		return requestTimes;
	}

	public void addTimes() {
		this.requestTimes++;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorStack() {
		return errorStack;
	}

	public void setErrorStack(String errorStack) {
		this.errorStack = errorStack;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setMinRespTime(long minRespTime) {
		this.minRespTime = minRespTime;
	}

	public void setMaxRespTime(long maxRespTime) {
		this.maxRespTime = maxRespTime;
	}

	public void setRequestTimes(int requestTimes) {
		this.requestTimes = requestTimes;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getLastReqTimeFormat() {
		if(lastReqTime == 0){
			return null;
		}
		return DateUtil.getDateStr(lastReqTime);
	}

	public void setLastReqTimeFormat(String lastReqTimeFormat) {
		this.lastReqTimeFormat = lastReqTimeFormat;
	}
	
	

}
