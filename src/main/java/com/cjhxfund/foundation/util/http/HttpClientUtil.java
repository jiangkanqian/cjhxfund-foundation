package com.cjhxfund.foundation.util.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.cjhxfund.foundation.util.data.DataUtil;

public class HttpClientUtil {

	/**
	 * url json post 数据 请求 paramUrl
	 * 
	 * @param url
	 *            url请求地址
	 * @param header
	 *            请求头信息
	 * @param json
	 *            json数据请求
	 * @return
	 */
	public static String post(String url, Map<String, String> header, String json) {
		HttpPost httpPost = new HttpPost(url);
		if(DataUtil.isValidStr(json)){
			StringEntity entity = getEntity(json,Charsets.UTF_8.name(),"application/json");
			httpPost.setEntity(entity);
		}
//		httpPost.setHeader("Content-Type", "multipart/form-data;boundary=------------------12314");
		addHeader(httpPost,header);
		// 如果设置了链接超时等属性
		return getResult(httpPost);
	}
	
	public static String get(String url, Map<String, String> header, Map params) {
		if (null != params && params.size() > 0) {
			url = RequestUtil.doFormUrlEncode(url, params, null);
		}
		HttpGet httpget = new HttpGet(url);
		addHeader(httpget,header);
		// 如果设置了链接超时等属性
		return getResult(httpget);
	}
	
	public static String get(String url) {
		HttpGet httpget = new HttpGet(url);
		// 如果设置了链接超时等属性
		return getResult(httpget);
	}

	/**
	 * url post 数据 请求 paramUrl
	 * 
	 * @param url
	 *            url请求地址
	 * @param header
	 *            请求头信息
	 * @param params
	 *            key-value数据请求
	 * @return
	 */
	public static String post(String url, Map<String, String> header, Map<String, Object> params) {
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		Set<String> keySet = params.keySet();
		Iterator<String> keyit = keySet.iterator();
		while (keyit.hasNext()) {
			String key = (String) keyit.next();
			formparams.add(new BasicNameValuePair(key, params.get(key).toString()));
		}
		UrlEncodedFormEntity uefEntity = null;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setEntity(uefEntity);
		addHeader(httpPost,header);
		return getResult(httpPost);
	}
	
	/**
	 * 上传文件
	 * @param url
	 * @param content
	 * @param header
	 * @param params
	 * @return
	 */
	public static String post(String url, byte[] content, Map<String, String> header, Map<String, Object> params) {
		if (null != params && params.size() > 0) {
			url = RequestUtil.doFormUrlEncode(url, params, null);
		}
		HttpPost httpPost = new HttpPost(url);
		if (content != null && content.length > 0) {
			httpPost.setEntity(new ByteArrayEntity(content));
		}
		addHeader(httpPost,header);
		return getResult(httpPost);
	}
	
	public static String put(String url, byte[] content, Map<String, String> header, Map<String, Object> params) {
		if (null != params && params.size() > 0) {
			url = RequestUtil.doFormUrlEncode(url, params, null);
		}
		HttpPut put = new HttpPut(url);
		if (content != null && content.length > 0) {
			put.setEntity(new ByteArrayEntity(content));
		}
		addHeader(put,header);
		return getResult(put);
	}

	/**
	 * 获取请求配置的Client
	 * @param httpType
	 * @return
	 */
	private static CloseableHttpClient getConfigClient( HttpRequestBase httpType) {
		 RequestConfig config = RequestConfig.custom()
		 .setConnectionRequestTimeout(60000).setConnectTimeout(60000)
		 .setSocketTimeout(60000).build();
		 httpType.setConfig(config);
		// 如果设置了链接超时等属性
		 CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(null).build();//设置进去
		return httpClient;
	}
	
	public static String put(String url, String content) {
		return put(url, null, content);
	}
	
	
	public static String put(String url, Map<String, String> header, String content) {
		HttpPut put = new HttpPut(url);
		addHeader(put,header);
		if(DataUtil.isValidStr(content)){
			StringEntity entity = getEntity(content,Charsets.UTF_8.name(),"application/json");
			put.setEntity(entity);
		}
		return getResult(put);
	}

	public static String delete(String url, String content) {
		return delete(url, null, content);
	}
	
	public static String delete(String url, Map<String, String> header, String content) {
		MyHttpDelete delete = new MyHttpDelete(url);
		addHeader(delete,header);
//		StringEntity entity = new StringEntity(content);
		if(DataUtil.isValidStr(content)){
			StringEntity entity = getEntity(content,Charsets.UTF_8.name(),"application/json");
			delete.setEntity(entity);
		}
		return getResult(delete);
	}
	
	/**
	 * 获取数据封装，编码模式
	 * @param content
	 * @param charset
	 * @param contentType
	 * @return
	 */
	private static StringEntity getEntity(String content, String charset, String contentType){
		StringEntity entity = new StringEntity(content, charset);// 解决中文乱码问题
		entity.setContentEncoding(charset);
		entity.setContentType(contentType);
		return entity;
	}
	
	/**
	 * 添加请求头信息
	 * @param httpType
	 * @param header
	 */
	private static void addHeader(HttpRequestBase httpType, Map<String, String> header){
		if (null != header && header.size() > 0) {
			Set<String> headerkey = header.keySet();
			Iterator<String> headerkeyit = headerkey.iterator();
			while (headerkeyit.hasNext()) {
				String key = (String) headerkeyit.next();
				httpType.setHeader(key, header.get(key).toString());
			}
		}
	}
	

	/**
	 * 默认的httpClient 请求配置
	 * @param httpType
	 * @return
	 */
	private static String getResult(HttpRequestBase httpType) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		return getResult(httpClient, httpType);
	}
	
	/**
	 * 自定义的httpClient 请求配置
	 * @param httpType
	 * @return
	 */
	private static String getResult(CloseableHttpClient httpClient, HttpRequestBase httpType) {
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpType);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				InputStream is = response.getEntity().getContent();
				String retString = InputStreamTOString(is, Charsets.UTF_8.name());
				is.close();
				return retString;

			} else {
				throw new HttpException("字节内容上传失败，错误的HTTP Status:" + status);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	final static int BUFFER_SIZE = 4096;
	private static String InputStreamTOString(InputStream in, String encoding) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		try {
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
				outStream.write(data, 0, count);
			String result=new String(outStream.toByteArray(), encoding);
			in.close();
			outStream.flush();
			outStream.close();
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
	}

}
