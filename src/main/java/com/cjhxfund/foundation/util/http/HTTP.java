package com.cjhxfund.foundation.util.http;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.io.PathUtil;
import com.cjhxfund.foundation.util.io.ReadFileUtil;

public class HTTP {

	public static class ContentTypeWithEncoding {
		public final String contentType;
		public final String encoding;

		public ContentTypeWithEncoding(String contentType, String encoding) {
			this.contentType = contentType;
			this.encoding = encoding;
		}
	}

	public static ContentTypeWithEncoding parseContentType(String contentType) {
		if (contentType == null) {
			return new ContentTypeWithEncoding("text/html".intern(), null);
		} else {
			String[] contentTypeParts = contentType.split(";");
			String _contentType = contentTypeParts[0].trim().toLowerCase();
			String _encoding = null;
			// check for encoding-info
			if (contentTypeParts.length >= 2) {
				String[] encodingInfoParts = contentTypeParts[1].split(("="));
				if (encodingInfoParts.length == 2 && encodingInfoParts[0].trim().equalsIgnoreCase("charset")) {
					// encoding-info was found in request
					_encoding = encodingInfoParts[1].trim();

					if (StringUtils.isNotBlank(_encoding) && ((_encoding.startsWith("\"") && _encoding.endsWith("\"")) || (_encoding.startsWith("'") && _encoding.endsWith("'")))) {
						_encoding = _encoding.substring(1, _encoding.length() - 1).trim();
					}
				}
			}
			return new ContentTypeWithEncoding(_contentType, _encoding);
		}

	}

//	private static final Map<String, String> lower2UppercaseHttpHeaders = initLower2UppercaseHttpHeaders();

	private static Map<String, String> initLower2UppercaseHttpHeaders() {
		Map<String, String> map = new HashMap<String, String>();

		// String path = "/play/utils/http_headers.properties";
		String path = PathUtil.getSrcPath("http_headers.properties");
		InputStream in = HTTP.class.getResourceAsStream(path);
		if (in == null) {
			throw new RuntimeException("Error reading " + path);
		}
		List<String> lines = ReadFileUtil.readLines(in);
		for (String line : lines) {
			line = line.trim();
			if (!line.startsWith("#")) {
				map.put(line.toLowerCase(), line);
			}
		}

		return Collections.unmodifiableMap(map);
	}

	/**
	 * Use this method to make sure you have the correct caseing of a http header name. eg: fixes 'content-type' to 'Content-Type'
	 */
	public static String fixCaseForHttpHeader(String headerName) {
		if (headerName == null) {
			return null;
		}
		String correctCase = initLower2UppercaseHttpHeaders().get(headerName.toLowerCase());
		if (correctCase != null) {
			return correctCase;
		}
		// Didn't find it - return it as it is
		return headerName;
	}

	/**
	 * 获取当前网络ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		if (!DataUtil.isValidStr(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (!DataUtil.isValidStr(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (!DataUtil.isValidStr(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("X-Real-IP");
		}
		if (!DataUtil.isValidStr(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) {
			// "***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
}
