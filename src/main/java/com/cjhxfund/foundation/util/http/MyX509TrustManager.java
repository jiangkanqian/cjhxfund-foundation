package com.cjhxfund.foundation.util.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * @author Xie Jason
 *证书信任管理器（用于https请求） 
 * @date 2014年11月22日
 * 
 * 这个证书管理器的作用就是让它信任我们指定的证书
 */
public class MyX509TrustManager implements X509TrustManager {
	
	  public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
	    }  
	  
	    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
	    }  
	  
	    public X509Certificate[] getAcceptedIssuers() {  
	        return null;  
	    }  
}

