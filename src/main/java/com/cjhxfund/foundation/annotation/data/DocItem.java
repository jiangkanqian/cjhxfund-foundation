package com.cjhxfund.foundation.annotation.data;

public class DocItem {
	public String controllerName;
	public String methodName;
	public String requestType;
	public String requestUrl;
	public Class<?>[] methodParmaTypes;

	public DocItem(String controllerName, String methodName, String requestType, String requestUrl,
			Class<?>[] methodParmaTypes) {
		super();
		this.controllerName = controllerName;
		this.methodName = methodName;
		this.requestType = requestType;
		this.requestUrl = requestUrl;
		this.methodParmaTypes = methodParmaTypes;
	}

}
