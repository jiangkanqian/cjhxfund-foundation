package com.cjhxfund.foundation.util.http.useragent;

public enum ApplicationType {

	WEBMAIL("Webmail client"), UNKNOWN("unknown");

	private String name;

	private ApplicationType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
