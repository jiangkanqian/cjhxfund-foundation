package com.cjhxfund.foundation.util.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 处理request工具类
 * 
 * @author xiejs
 * @date 2015年5月13日
 */
public class RequestUtil {

	private RequestUtil() {
		throw new Error("该类不能被实例化！");
	}

	private static JLogger logger = SysLogger.getLogger();

	// public static Map<String, Long> checkLoadTime = new HashMap<String, Long>();
	// public static Map<String, String> checkLoadUrl = new HashMap<String, String>();

	public static String getLoadDownFileName(HttpServletRequest request, String filename) {
		String name = filename;
		try {
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
				name = java.net.URLEncoder.encode(filename, "UTF-8");
			} else {
				name = new String(filename.getBytes("UTF-8"), "ISO8859-1");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取requestBody
	 * 
	 * @author xiejs
	 * @date 2015年5月18日
	 * @param br
	 *            request.getReader();
	 * @return
	 */
	public static String getBodyString(BufferedReader br) {
		String inputLine;
		StringBuffer str = new StringBuffer();
		try {
			if(null != br && br.ready()){
				while ((inputLine = br.readLine()) != null) {
					str.append(inputLine);
				}
				br.close();
			}
		} catch (IOException e) {
			System.out.println("request body IO: " + e);
		}
		return str.toString();
	}

	/**
	 * 通过head请求验证链接是否有效
	 * @author xiejs
	 * @date 2015年6月10日
	 * @param strLink 链接地址, 默认2秒
	 * @return
	 */
	public static boolean isValidLink(String strLink) {
		URL url = null;
		try {
			url = new URL(strLink);
			HttpURLConnection connt = (HttpURLConnection) url.openConnection();
			connt.setRequestMethod("GET");
			connt.setConnectTimeout(2000);
			String strMessage = connt.getResponseMessage();
			logger.info(strLink+" : "+strMessage);
			if (strMessage.contains("Not Found") || strMessage.contains("403")) {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+" url:"+strLink);
			return false;
		}
		return true;
	}
	
	/**
	 * 判断网络地址是否有效
	 * @param strLink 网络地址
	 * @param timeout 超时时间
	 * @return
	 */
	public static boolean isValidLink(String strLink, int timeout) {
		URL url = null;
		try {
			url = new URL(strLink);
			HttpURLConnection connt = (HttpURLConnection) url.openConnection();
			connt.setRequestMethod("GET");
			connt.setConnectTimeout(timeout);
			String strMessage = connt.getResponseMessage();
			logger.info(strLink+" : "+strMessage);
			if (strMessage.contains("Not Found") || strMessage.contains("403")) {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+" url:"+strLink);
			return false;
		}
		return true;
	}

	/**
	 * 获取链接内容，一般用于调用接口内容，get方式请求
	 * 
	 * @param urlRequest
	 * @return
	 */
	public static String httpRequest(String urlRequest) {
		return httpRequest(urlRequest, null, null);
	}

	/**
	 * 获取链接内容，一般用于调用接口内容，get方式请求
	 * 
	 * @param urlRequest
	 * @return
	 */
	public static InputStream getRequestStream(String urlRequest) {
		InputStream is = null;
		try {
			URL urlGet = new URL(urlRequest);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			// http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			// http.setDoOutput(true);
			http.setDoInput(true);
			// System.out.println(http.getHeaderFields());
			http.connect();
			// 获取文件转化为byte流
			is = http.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return is;
	}

	/**
	 * 获取链接内容，一般用于调用接口内容，get方式请求
	 * @param urlRequest
	 * @return
	 */
	public static String httpRequest(String urlRequest, Charset charset) {
		return httpRequest(urlRequest, null, charset);
	}

	// public static String redirectJson(String urlRequest, Map<String, Object> params) {
	// Map<String, String> requestProperty = new HashMap<String, String>();
	// requestProperty.put("Content-Type", "ext/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	// requestProperty.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
	// requestProperty.put("Cookie", "JSESSIONID="+DataUtil.getUUID()+"sessionId");
	// requestProperty.put("Upgrade-Insecure-Requests", "1");
	// Map<String, String> toParams = new HashMap<String, String>();
	// for (Entry<String, Object> entry : params.entrySet()) {
	// toParams.put(entry.getKey(), entry.getValue().toString());
	// }
	// urlRequest = doFormUrlEncode(urlRequest, toParams, "");
	// // System.out.println(urlRequest);
	// return RequestUtil.httpRequest(urlRequest, requestProperty,Charsets.UTF_8);
	// }

	/**
	 * 获取链接内容，一般用于调用接口内容，get方式请求
	 * 
	 * @param urlRequest
	 * @param requestProperty
	 *            请求头属性 比如：Content-Type：application/x-www-form-urlencoded,charset=utf8
	 * @return
	 */
	public static String httpRequest(String urlRequest, Map<String, String> requestProperty) {
		return httpRequest(urlRequest, requestProperty, null);
	}

	/**
	 * 获取链接内容，一般用于调用接口内容，get方式请求
	 * 
	 * @param urlRequest
	 * @param requestProperty
	 *            请求头属性 比如：Content-Type：application/x-www-form-urlencoded,charset=utf8
	 * @return
	 */
	public static String httpRequest(String urlRequest, Map<String, String> requestProperty, Charset charset) {
		StringBuffer buf = new StringBuffer();
		try {
			if (charset == null) {
				charset = Charset.forName("UTF-8");
			}
			// 建立连接
			URL url = new URL(urlRequest);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			if (DataUtil.isValidMap(requestProperty)) {
				for (Entry<String, String> entry : requestProperty.entrySet()) {
					con.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			con.setDoInput(true);
			con.setRequestMethod("GET");
			con.connect();

			// 获取输入流
			InputStream input = con.getInputStream();
			InputStreamReader reader = new InputStreamReader(input, charset);
			BufferedReader bufRead = new BufferedReader(reader);

			// 读取返回结果
			String str = null;
			while ((str = bufRead.readLine()) != null) {
				buf.append(str);
			}

			// 关闭流和连接
			bufRead.close();
			reader.close();
			input.close();
			input = null;
			con.disconnect();

		} catch (java.lang.NullPointerException nullEx) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
		return buf.toString();
	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JsonObject httpSSLRequest(String requestUrl, String requestMethod, String outputStr) {
		JsonObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (DataUtil.isValidStr(outputStr)) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = new JsonParser().parse(buffer.toString()).getAsJsonObject();
		} catch (ConnectException ce) {
			logger.error("Weixin server connection timed out,msg:" + ce.getMessage());
		} catch (Exception e) {
			logger.error("https request error:" + e.getMessage());
		}
		return jsonObject;
	}

	/**
	 * 将数据同post请求转到另一个服务去
	 * 
	 * @author xiejs
	 * @date 2015年6月10日
	 * @param content
	 *            主体内容，字节形式 ，如果是有效的content
	 * @param url
	 *            接口地址
	 * @param params
	 *            请求参数
	 * @return 返回请求参数
	 */
	public static String requestServer(byte[] content, String url, Map<String, String> params) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 如果有参数，则添加参数
		if (DataUtil.isValidMap(params)) {
			// List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			// for (Entry<String, String> entry : params.entrySet()) {
			// pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			// }
			// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, Consts.UTF_8);
			// post.setEntity(entity);
			// 参数只能放到地址上，因为post数据实体数据已经有了
			url = doFormUrlEncode(url, params, Consts.UTF_8.name());
		}
		HttpPost post = new HttpPost(url);
		if (content != null && content.length > 0) {
			post.setEntity(new ByteArrayEntity(content));
		}
		HttpResponse response = null;
		int status = 0;
		try {
			response = httpclient.execute(post);
			status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				// InputStream is = response.getEntity().getContent();
				// String retString = InputStreamUtil.InputStreamTOString(is);
				String retString = IOUtils.toString(response.getEntity().getContent());
				return retString;

			} else {
				throw new HttpException("字节内容上传失败，错误的HTTP Status:" + status);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将参数转为get方式请求，放到url后面，并utf8加密
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String doFormUrlEncode(String url, Map params, String charset) {
		if (!DataUtil.isValidStr(charset)) {
			charset = Charsets.UTF_8.name();
		}
		StringBuffer buf = new StringBuffer();
		int i = 0;
		Set<String> keySet = params.keySet();
		Iterator<String> keyit = keySet.iterator();
		String value = null;
		while (keyit.hasNext()) {
			String key = (String) keyit.next();
			value = null;
			if (params.get(key) != null) {
				value = params.get(key).toString();
			}
			URLCodec codec = new URLCodec();
			if (i > 0) {
				buf.append("&");
			}
			try {
				buf.append(codec.encode(key, charset));
				buf.append("=");
				if (value != null) {
					buf.append(codec.encode(value, charset));
				}
				i++;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (url.contains("?")) {
			return url + buf.toString();
		} else {
			return url + "?" + buf.toString();
		}
	}

	public static boolean isPCRequest(HttpServletRequest request) {
		if (HttpRequestDeviceUtil.getRequestDevice(request).equalsIgnoreCase("pc")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMobileRequest(HttpServletRequest request) {
		if (HttpRequestDeviceUtil.getRequestDevice(request).equalsIgnoreCase("mobile")) {
			return true;
		} else {
			return false;
		}
	}

	public static String removeQueryStringFromURL(String paramURL) {
		if (DataUtil.isValidStr(paramURL)) {
			int i = paramURL.lastIndexOf('?');
			String localUrl = paramURL.substring(0, i);
			return localUrl;
		}
		return null;
	}

	public static URL removeQueryStringFromURL(URL paramURL) {
		URL localURL = paramURL;
		if (localURL != null) {
			String str = localURL.toString();
			int i = str.lastIndexOf('?');
			if (i != -1)
				try {
					localURL = new URL(str.substring(0, i));
				} catch (MalformedURLException localMalformedURLException) {
					localMalformedURLException.printStackTrace();
				}
		}
		return localURL;
	}

	/**
	 * 是否是重定向请求
	 * 
	 * @author xiejs
	 * @date 2015年7月21日
	 * @param paramInt
	 * @return
	 */
	public static boolean isRedirect(int paramInt) {
		return (paramInt >= 300) && (paramInt <= 305) && (paramInt != 304);
	}

	public static void uploadImg(byte[] file, String fileName, String serverUrl, Map<String, String> resultMap) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fileName", fileName);
		String result = requestServer(file, serverUrl, params);
		logger.info("图片存储结果：" + result);
		resultMap = new Gson().fromJson(result, new TypeToken<Map<String, String>>() {
		}.getType());
	}

	public static String getMedia(String requestUrl, String savePath) {
		String filePath = "";
		// 拼接请求地址
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");

			if (!savePath.endsWith("/")) {
				savePath += "/";
			}
			String filename = conn.getHeaderField("Content-disposition").split(";")[1].replaceAll("filename=|>|\"", "").trim();
			System.out.println(filename);
			// 根据内容类型获取扩展名
			// String fileExt = CommonUtil.getFileExt(conn.getHeaderField("Content-Type"));
			// 将mediaId作为文件名
			filePath = savePath + filename;
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[4096];
			int size = 0;
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
			bis.close();

			conn.disconnect();
			logger.info("下载媒体文件成功，filePath=" + filePath);
		} catch (Exception e) {
			filePath = null;
			logger.error("下载媒体文件失败：" + e.getMessage());
		}
		return filePath;
	}

	/**
	 * 根据内容类型判断文件扩展名
	 * 
	 * @param contentType
	 *            内容类型
	 * @return
	 */
	public static String getFileExt(String contentType) {
		String fileExt = "";
		if ("image/jpeg".equals(contentType))
			fileExt = ".jpg";
		else if ("audio/mpeg".equals(contentType))
			fileExt = ".mp3";
		else if ("audio/amr".equals(contentType))
			fileExt = ".amr";
		else if ("video/mp4".equals(contentType))
			fileExt = ".mp4";
		else if ("video/mpeg4".equals(contentType))
			fileExt = ".mp4";
		return fileExt;
	}

	/**
	 * 通过表单的形式发送请求
	 * 
	 * @author xiejs
	 * @date 2015年8月21日
	 * @param requestUrl
	 *            请求路径，一般都要加上token
	 * @param fileUrl
	 *            文件路径
	 * @param type
	 *            1：上传发送图片，2：上传媒体获取mediaId，其他返回原本字符串
	 * @return
	 */
	public static String makeFormRequest(String requestUrl, String fileUrl, int type) {
		String result = null;
		String boundary = "------------" + DataUtil.getRandStr(10);
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection uploadConn = (HttpURLConnection) url.openConnection();
			uploadConn.setDoOutput(true);
			uploadConn.setDoInput(true);
			uploadConn.setRequestMethod("POST");
			// 设置请求头Content-Type
			uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			// 获取媒体文件上传的输出流（往微信服务器写数据）
			OutputStream outputStream = uploadConn.getOutputStream();
			URL mediaUrl = new URL(fileUrl);
			HttpURLConnection meidaConn = (HttpURLConnection) mediaUrl.openConnection();
			meidaConn.setDoOutput(true);
			meidaConn.setRequestMethod("GET");
			// 从请求头中获取内容类型
			String contentType = meidaConn.getHeaderField("Content-Type");
			// 根据内容类型判断文件扩展名
			String fileExt = RequestUtil.getFileExt(contentType);
			// 请求体开始
			outputStream.write(("--" + boundary + "\r\n").getBytes());
			outputStream.write(String.format("Content-Disposition: form-data; name=\"media\";filename=\"file123%s\"\r\n", fileExt).getBytes());
			outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());
			// 获取媒体文件的输入流（读取文件）
			BufferedInputStream bis = new BufferedInputStream(meidaConn.getInputStream());
			byte[] buf = new byte[4096];
			int size = 0;
			while ((size = bis.read(buf)) != -1) {
				// 将媒体文件写到输出流（往微信服务器写数据）
				outputStream.write(buf, 0, size);
			}
			// 请求体结束
			outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
			outputStream.close();
			bis.close();
			meidaConn.disconnect();
			// 获取媒体文件上传的输入流（从微信服务器读数据）
			InputStream inputStream = uploadConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			uploadConn.disconnect();
			JsonObject jo = new JsonParser().parse(buffer.toString()).getAsJsonObject();
			logger.info("上传媒体结果：" + jo.toString());
			if (type == 1) {
				// 上传图片，获取图片的url
				if (jo.get("url") != null) {
					result = jo.get("url").getAsString();
				}
			} else if (type == 2) {
				// 上传媒体文件，获取mediaId
				if (jo.get("media_id") != null) {
					result = jo.get("media_id").getAsString();
				}
			} else {
				result = buffer.toString();
			}
		} catch (Exception e) {
			logger.error("上传媒体文件失败："+ e.getMessage());
		}

		return result;
	}

	public static void main(String[] args) {
		String url = "http://weixin.qq.com/r/OtJAWC3EUXAurWk794co";
		// String url = "http://weixin.qq.com/cgi-bin/readtemplate?check=false&t="
		// + "weixin_getdownurl_sms&s=download&from=100&stype=10037102";
		// url="http://m2.nnwhy.com/auth";
		System.out.println(RequestUtil.httpRequest(url));
	}

}
