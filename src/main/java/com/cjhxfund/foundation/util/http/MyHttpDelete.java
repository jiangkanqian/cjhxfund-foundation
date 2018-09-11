package com.cjhxfund.foundation.util.http;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * @author lanhan
 * @date 2016年4月16日
 */
public class MyHttpDelete extends HttpEntityEnclosingRequestBase{

	 public static final String METHOD_NAME = "DELETE";

	    public String getMethod() {
	        return METHOD_NAME;
	    }

	    public MyHttpDelete(final String uri) {
	        super();
	        setURI(URI.create(uri));
	    }

	    public MyHttpDelete(final URI uri) {
	        super();
	        setURI(uri);
	    }

	    public MyHttpDelete() {
	        super();
	    }
}
