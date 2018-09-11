package com.cjhxfund.foundation.annotation.data;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 系统模块数据加载
 * @author xiejs
 * @date 2015年7月5日
 */
public class DocModule {
	
	private  Map<String, String> parentModule = new LinkedHashMap<String, String>();
	private  Map<String, Map<String, String>> subModule = new LinkedHashMap<String, Map<String, String>>();
	/**
	 * @param parentModule
	 * @param subModule
	 */
	public DocModule(Map<String, String> parentModule, Map<String, Map<String, String>> subModule) {
		this.parentModule = parentModule;
		this.subModule = subModule;
	}
	public Map<String, String> getParentModule() {
		return parentModule;
	}
	public void setParentModule(Map<String, String> parentModule) {
		this.parentModule = parentModule;
	}
	public Map<String, Map<String, String>> getSubModule() {
		return subModule;
	}
	public void setSubModule(Map<String, Map<String, String>> subModule) {
		this.subModule = subModule;
	}
	
	 
	

}
