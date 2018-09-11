package com.cjhxfund.foundation.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cjhxfund.foundation.log.constants.StartLogger;
import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.http.RespDataUtil;
import com.cjhxfund.foundation.web.context.CommonCache;
import com.cjhxfund.foundation.web.context.SpringContext;
import com.cjhxfund.foundation.web.listener.SessionUser;

/**
 * 控制action请求访问权限
 * @author xiejiesheng
 */
public class ActionFilter implements Filter {

	private static JLogger logger = SysLogger.getLogger();

	public void init(FilterConfig filterConfig) throws ServletException {
		StartLogger.getLogger().info("init action filter……");
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		if(SpringContext.requestModel.equals("free")){
			logger.info("免登陆模式访问API……");
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String requestURI = request.getRequestURI();
		String sessionId = request.getSession().getId();
		// String userAgent = request.getHeader("User-Agent");
//		String remoteIp = HTTP.getIpAddr(request);
		// if(DataUtil.isValidStr(userAgent)){
		// //sessionID 为浏览器+ip来定，判断请求是否和法时，以这个为主，但是还需要校验cookie:webToken
		// userAgent = userAgent.replaceAll(" |,|;|\\(|\\)|\\.|/", "");
		// sessionId = userAgent + "-" + remoteIp;
		// }

//		 System.out.println("sessionId:"+sessionId);
//		AccessToken token = CommonCache.tokenCache.get(remoteIp);
		// System.out.println(JsonUtil.toJson(token));
		
//		DataUtil.print(CommonCache.freePathHolder);

		if (null != sessionId) {
			// System.out.println(sessionId);

			// String webToken = CookieUtil.getCookieValue("webToken", request);
			// if (LanhanSessionContext.containsKey(sessionId) && DataUtil.isValidStr(webToken)) {
			if (SessionUser.containsKey(sessionId)) {
				// String webToken = CookieUtil.getCookieValue("webToken", request);

				HttpSession session = SessionUser.setLastAccesstime(sessionId);
				// //必须验证登录token是否存在
				// if(session.getWebToken().equals(webToken)){
				// }
				// 特殊接口 获取session的 直接返回session数据
				if (request.getMethod().equalsIgnoreCase("get") && requestURI.endsWith("getUserSession.action")) {
					logger.info("获取session数据……");
					RespDataUtil.sendJson(session.getAttribute(SessionUser.USER_INFO), response, 0);
					return;
				}

				chain.doFilter(servletRequest, servletResponse);
				return;
			} else if (CommonCache.freePathHolder.contains(requestURI)) {
				logger.info("free API : " + requestURI);
				chain.doFilter(servletRequest, servletResponse);
				return;
			}
		}

		String respJson = "{\"msg\":\"回话过期！请刷新页面重新登录!\",\"code\":400500}";
		// response.sendRedirect(SpringContext.root);
		logger.info(respJson);
		RespDataUtil.respData(response, respJson);

	}

	public void destroy() {
		logger.info("正在销毁 filter...,关闭日志线程……");
	}

}
