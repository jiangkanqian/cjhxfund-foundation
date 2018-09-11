package com.cjhxfund.foundation.web.listener;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cjhxfund.foundation.log.constants.Constant;
import com.cjhxfund.foundation.log.constants.StartLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.APIDocUtil;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.io.HtmlUtil;
import com.cjhxfund.foundation.util.io.PathUtil;
import com.cjhxfund.foundation.util.io.ReadFileUtil;
import com.cjhxfund.foundation.util.io.XMLUtil;
import com.cjhxfund.foundation.web.context.SpringContext;

/**
 * web容器启动初始化, 必须在web.xml配置，否则无法正常启动
 * @author lanhan
 * @date 2015年11月23日
 */
@WebListener()
public class WebContextLoaderListener extends ContextLoaderListener {

	private static JLogger logger = StartLogger.getLogger();

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			SpringContext.JunitTest = false;
			long now = System.currentTimeMillis();
			logger.info("开始初始化web容器……");
			ServletContext context = event.getServletContext();
			SpringContext.root = context.getContextPath();
			loadContextParam(context);
			//初始化bean加载等信息
			super.contextInitialized(event);
			//必须要初始化之后才有值
			SpringContext.appContext = WebApplicationContextUtils.getWebApplicationContext(context);
			//SpringContext.root = SpringContext.appContext.getApplicationName();

			logger.info("webContextInit 初始化完成,time/ms :" + (System.currentTimeMillis() - now));
			logger.info("当前系统根路径为：" + SpringContext.root);
			logger.info("日志目录:" + Constant.CFG_LOG_PATH);
			showServerConnect();
			now = System.currentTimeMillis();
			APIDocUtil.initCtrlAnno();
			logger.info("controller 加载完成,time/ms :" + (System.currentTimeMillis() - now));
			now = System.currentTimeMillis();
			APIDocUtil.initData();
			logger.info("初始化数据 加载完成,time/ms :" + (System.currentTimeMillis() - now));
			SpringContext.isIniting = false;//防止下次再调用post
			String url = "http://" + SpringContext.SYS_INFO.get("localIP") + ":" + SpringContext.SYS_INFO.get("port") + SpringContext.root + "/";
			logger.info("当前访问地址：" + url);
			if (SpringContext.isDebug) {
				//如果是debug，默认打开浏览器访问地址
				HtmlUtil.openDefaultBrowser(url);
			}
		}
		catch (RuntimeException re) {
			logger.error(re);
			throw re;
		}
		catch (Throwable ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		}
	}

	private void loadContextParam(ServletContext context) {
		String suffix = context.getInitParameter("suffix");
		String location = context.getInitParameter("contextConfigLocation");
		if (DataUtil.isValidStr(location)) {
			String contextConfigLocation = location.replace("classpath*:", "");
			String fileName = PathUtil.getSrcPath(contextConfigLocation);
			NodeList nodes = XMLUtil.getNodeList(fileName, "context:component-scan");
			Element element = (Element) nodes.item(0);
			String basePackage = element.getAttribute("base-package");
			SpringContext.basePackage = basePackage.replace(".", "/").replace("//", "/").replace("*", "");
			logger.info("basePackage:" + SpringContext.basePackage);
		}
		if (DataUtil.isValidStr(suffix)) {
			SpringContext.suffixName = suffix;
			logger.info("数据请求-访问后缀:" + SpringContext.suffixName);
		}
		String msg = "当前运行环境：生产环境";
		String debugStr = context.getInitParameter("debug");
		if (DataUtil.isValidStr(debugStr) && debugStr.equals("true")) {
			SpringContext.isDebug = true;
			msg = "当前运行环境：开发/测试环境";
		}
		else {
			SpringContext.isDebug = false;
		}
		String requestModel = context.getInitParameter("requestModel");
		if (DataUtil.isValidStr(requestModel) && requestModel.equals("free")) {
			SpringContext.requestModel = requestModel;
			msg = msg + ",当前系统访问模式为：免登陆模式 ";
		}
		else {
			msg = msg + ",当前系统访问模式为：登陆模式 ";
		}
		logger.info("初始化context-param成功！" + msg);
	}

	private void showServerConnect() {
		//		Map<String, String> map = new HashMap<String, String>();
		String rootPath = PathUtil.getAppPath();
		File root = new File(rootPath);
		String webappsPath = root.getParent();
		SpringContext.SYS_INFO.put("localIP", SpringContext.getLocalIp());
		SpringContext.SYS_INFO.put("webappPath", webappsPath);
		SpringContext.SYS_INFO.put("rootPath", rootPath);
		tomcatInfo(webappsPath);
		logger.info("SYS_INFO信息-----");
		for (Entry<String, String> entry : SpringContext.SYS_INFO.entrySet()) {
			logger.info(entry.getKey() + ":" + entry.getValue());
		}
		logger.info("---------------");
	}

	private void tomcatInfo(String webappsPath) {
		File webPath = new File(webappsPath);
		String fileName = webPath.getParent() + "/scripts/start.sh";//生产环境规范
		logger.info("fileName:" + fileName);
		File file = new File(fileName);
		if (file.exists()) {
			List<String> configs = ReadFileUtil.readLines(file);
			for (String line : configs) {
				if (line.trim().startsWith("HTTP_PORT")) {
					SpringContext.SYS_INFO.put("port", line.replace("HTTP_PORT=", ""));
				}
				else if (line.trim().startsWith("maxThreads")) {
					SpringContext.SYS_INFO.put("maxThreads", line.replace("maxThreads=", ""));
				}
				else if (line.trim().startsWith("SHUTDOWN_PORT")) {
					SpringContext.SYS_INFO.put("shutPort", line.replace("SHUTDOWN_PORT=", ""));
				}
			}
		}
		else {
			fileName = System.getProperty("catalina.base") + File.separator + "conf" + File.separator + "server.xml";
			NodeList nodes = XMLUtil.getNodeList(fileName, "Connector");
			NamedNodeMap nodeAttrs = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				NamedNodeMap attrs = element.getAttributes();
				for (int j = 0; j < attrs.getLength(); j++) {
					Node node = attrs.item(j);
					if (node.getNodeName().equals("protocol") && node.getNodeValue().contains("HTTP")) {
						nodeAttrs = attrs;
						break;
					}
				}
				if (nodeAttrs != null) {
					break;
				}
			}
			for (int j = 0, length = nodeAttrs.getLength(); j < length; j++) {
				Node node = nodeAttrs.item(j);
				SpringContext.SYS_INFO.put(node.getNodeName(), node.getNodeValue());
			}
		}
	}

}
