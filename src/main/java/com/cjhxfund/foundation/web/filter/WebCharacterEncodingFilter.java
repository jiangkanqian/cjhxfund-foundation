package com.cjhxfund.foundation.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cjhxfund.foundation.log.constants.StartLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;

/**
 * 对请求进行转码
 * @author xiejs
 * @date 2015年10月17日
 */
@WebFilter(filterName = "encodingFilter", urlPatterns = "/*", 
initParams = {
		@WebInitParam(name = "encoding", value = "UTF-8"), 
		@WebInitParam(name = "forceEncoding", value = "true")})
public class WebCharacterEncodingFilter implements Filter {

	private static JLogger logger = StartLogger.getLogger();

	private String encoding;

	private boolean forceEncoding = false;

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (this.encoding != null && (this.forceEncoding || httpRequest.getCharacterEncoding() == null)) {
			httpRequest.setCharacterEncoding(this.encoding);
			if (this.forceEncoding) {
				httpResponse.setCharacterEncoding(this.encoding);
			}
		}
		filterChain.doFilter(httpRequest, httpResponse);

	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.setEncoding(filterConfig.getInitParameter("encoding"));
		String fnStr = filterConfig.getInitParameter("forceEncoding");
		boolean forceEncoding = null != fnStr && fnStr.equals("true") ? true : false;
		// SpringContext.isDebug = null!=debugStr&&debugStr.equals("true")?true:false;
		this.setForceEncoding(forceEncoding);
		logger.info("tomcat访问编码:" + this.encoding + ", 是否强制转换访问编码:" + fnStr  + ",当前系统编码:" + System.getProperty("file.encoding"));
		logger.info("数据初始化完成！启动成功！");

	}

}
