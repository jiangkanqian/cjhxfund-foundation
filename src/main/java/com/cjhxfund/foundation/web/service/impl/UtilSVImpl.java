package com.cjhxfund.foundation.web.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.cjhxfund.foundation.annotation.data.DocData;
import com.cjhxfund.foundation.log.constants.Constant;
import com.cjhxfund.foundation.log.constants.SysLogger;
import com.cjhxfund.foundation.log.strategy.JLogger;
import com.cjhxfund.foundation.util.data.APIDocUtil;
import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.data.DocUtil;
import com.cjhxfund.foundation.util.data.InitBeanData;
import com.cjhxfund.foundation.util.io.FileOpUtil;
import com.cjhxfund.foundation.util.io.InputStreamUtil;
import com.cjhxfund.foundation.util.io.PathUtil;
import com.cjhxfund.foundation.util.io.ReadFileUtil;
import com.cjhxfund.foundation.web.context.CommonCache;
import com.cjhxfund.foundation.web.context.SpringContext;
import com.cjhxfund.foundation.web.controller.UtilController;
import com.cjhxfund.foundation.web.model.APIMonitor;
import com.cjhxfund.foundation.web.model.CodeTipMsg;
import com.cjhxfund.foundation.web.model.FileInfo;
import com.cjhxfund.foundation.web.model.HttpCtrl;
import com.cjhxfund.foundation.web.service.BaseInit;
import com.cjhxfund.foundation.web.service.IUtilSV;

@Service("utilSv")
public class UtilSVImpl implements IUtilSV{

	private static JLogger logger = SysLogger.getLogger(UtilController.class);
	
	
	
	@Override
	public void init(HttpCtrl httpCtrl) {
		String beanName = httpCtrl.getAsString("beanName");
		if(beanName.equals("refreshSpringContext")){
			SpringContext.refreshContext();
			httpCtrl.sendJson(CodeTipMsg.COMMON_SUCCESS,"重新加载了SpringContext！");
		}
		else if(beanName.equals("tomcatusers")){
			String userPath = System.getProperty("catalina.base")+"/conf/tomcat-users.xml";
			String content = ReadFileUtil.getFileContent(userPath);
			httpCtrl.sendJson(content);
		}
		else if(beanName.equals("tomcatapps")){
			File root = new File(System.getProperty("catalina.base")+"/webapps");
			String[] rootList = root.list();
			httpCtrl.sendJson(Arrays.toString(rootList));
		}
		else{
			InitBeanData data = CommonCache.initBeanCache.get(beanName);
			if(null != data){
				BaseInit init = data.getInitBean();
				init.initData(httpCtrl);
			}
			else{
				httpCtrl.sendJson(CodeTipMsg.COMMON_FAIL,"没有找到名称为："+httpCtrl.getAsString("beanName")+"的bean");
			}
		}
	}

	@Override
	public void fileList(HttpCtrl httpCtrl) {
		String type = httpCtrl.getAsString("fileType");
		String path = httpCtrl.getAsString("filePath");
		String dateType = httpCtrl.getAsString("dateType");
		String parent = PathUtil.getSrcPath("");
		if(type.equals("tomcat")||path.startsWith("/app/")){
			String fileDateStr = httpCtrl.getAsString("fileDateStr");
			parent = System.getProperty("catalina.base") +File.separator;
			if(path.startsWith("/app/")){
				path = path  + fileDateStr;
			}
			else if(path.startsWith("flogger/")){
				path = parent+ path  + fileDateStr;
			}
			else{
				if(dateType.equals("yyyy-mm-dd")){
					path = parent+ path + fileDateStr;
				}
				else if(dateType.equals("yyyy/mm/dd")){
					String[] dateArr = fileDateStr.split("-");
					path = parent+ path + dateArr[0] + File.separator + dateArr[1] + File.separator +dateArr[2];
				}
				else{
					path = parent+ path;
				}
			}
		}
		else{
			if(path.equals("lib/")){
				path = parent.replace("classes", "lib");
			}
			else if(path.startsWith("classes")){
				if(path.equals("classes/")){
					path = parent;
				}
				else{
					path = parent.split("classes")[0]+path;
				}
			}
			else{
				path = PathUtil.getAppPath()+path;
			}
		}
		logger.info("path:"+path);
		List<File> fileList = FileOpUtil.getListFiles(path, "", false);
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		for(File file : fileList){
			FileInfo fi = new FileInfo(file.getName(), file.length(), file.lastModified(), file.getPath());
			fileInfoList.add(fi);
		}
		httpCtrl.sendData(fileInfoList);
	}

	

	@Override
	public void getDoc(HttpCtrl httpCtrl) {
		Map<String, Object> condition = httpCtrl.getQueryMap();
		if(!DataUtil.isValidMap(CommonCache.ctrlBeanCache)||condition.get("clear")!=null){
			logger.info("重新加载api缓存");
			APIDocUtil.reloadCtrlAnno();
			httpCtrl.sendJson(CodeTipMsg.COMMON_SUCCESS,"重新加载api缓存");
			return;
		}
//		DataUtil.print(DocUtil.subModule);
//		String alldoc = JsonUtil.getEasyUIDgData(CommonCache.docDataCache, CommonCache.docDataCache.size());
//		String path = PathUtil.getSrcPath("mapperConfig");
//		System.out.println(path);
//		WriteUtil.writeFile(path, alldoc, "doc.js");
		int type = httpCtrl.getAsInteger("type");
		if(type == 1){
//			int sessionType = httpCtrl.getAsInteger("sessionType");
//			List<APIMonitor> docs = DocUtil.getMonitorList(httpCtrl.getAsString("qryMethod"), httpCtrl.getAsString("apiName"), sessionType);
			List<APIMonitor> docs = DocUtil.getMonitorList();
			httpCtrl.sendData(docs);
		}
		else if(type == 2){
			int sessionType = httpCtrl.getAsInteger("sessionType");
			List<DocData> docs = DocUtil.getDocList(httpCtrl.getAsString("qryMethod"), httpCtrl.getAsString("apiName"), sessionType);
			httpCtrl.sendJson(docs,docs.size());
		}
	}

	@Override
	public void uploadFile(HttpCtrl httpCtrl) {

		String type = httpCtrl.getAsString("fileType");
		String path = httpCtrl.getAsString("filePath");
		path = null == path ? "":path;
		if(type.equals("config")){
			if(path.startsWith("classes")){
				path = path.replace("classes/", "");
			}
			path = PathUtil.getSrcPath(path);
		}
		else{
			path = PathUtil.getSrcPath("").split("WEB-INF")[0]+path;
		}
		logger.info("path:"+path);
		InputStream is = InputStreamUtil.byteTOInputStream(httpCtrl.getFileBytes());
		InputStreamUtil.streamToFile(is, path);
		httpCtrl.sendJson("1","上传成功", path);
	}

	@Override
	public void listThread(HttpCtrl httpCtrl) {
//		Map<String,String> data = ThreadUtil.getThreadInfo();
//		long freeMemory = Runtime.getRuntime().freeMemory();
//		long totalMemory = Runtime.getRuntime().totalMemory();
//		long maxMemory = Runtime.getRuntime().maxMemory();
//		System.out.println(freeMemory/(1024*1024));
//		System.out.println(totalMemory/(1024*1024));
//		System.out.println(maxMemory/(1024*1024));
		httpCtrl.sendJson(1,"ok");
	}
	

	@Override
	public void getInitList(HttpCtrl httpCtrl) {
		List<InitBeanData> dataList = new ArrayList<InitBeanData>();
		for(Entry<String,InitBeanData> entry : CommonCache.initBeanCache.entrySet()){
			dataList.add(entry.getValue().cloneValue());
		}
		httpCtrl.sendData(dataList);
	}

	@Override
	public void loggerFile(HttpCtrl httpCtrl) {
		String parent = Constant.CFG_LOG_PATH;
		String fileType = httpCtrl.getAsString("fileType");
		String logDateStr = httpCtrl.getAsString("logDateStr");
		String filePath = "";
		if(fileType.equals("start")){
			 filePath = parent + "/"+logDateStr+"/StartLogger-info.log";
		}
		else if(fileType.equals("data")){
			 filePath = parent + "/"+logDateStr+"/DataLogger-info.log";
		}
		else if(fileType.equals("task")){
			 filePath = parent + "/"+logDateStr+"/TaskLogger-info.log";
		}
		else if(fileType.equals("error")){
			 filePath = parent + "/"+logDateStr+"/SysLogger-error.log";
		}
		if(DataUtil.isValidStr(filePath)){
			File file = new File(filePath);
			String content = "该日志文件在当天不存在！";
//			System.out.println(filePath);
			
			if(file.exists()){
				 content = ReadFileUtil.getFileContent(file,"UTF-8").replace("\r\n", " <br>").replace("\n", " <br> ")
						 .replaceAll("\\\\", "/").replace("\"", "\\\"").replace("\t", " ");
			}
			String data = CodeTipMsg.COMMON_SUCCESS.toString(content);
			httpCtrl.sendJson(data);
		}
		else{
			httpCtrl.sendJson(0, "文件类型错误");
		}
	}

	
	

}
