package com.cjhxfund.foundation.web.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cjhxfund.foundation.annotation.Author;
import com.cjhxfund.foundation.annotation.LanhanParam;
import com.cjhxfund.foundation.annotation.LanhanParam.ParamType;
import com.cjhxfund.foundation.annotation.LanhanParams;
import com.cjhxfund.foundation.annotation.ModuleInfo;
import com.cjhxfund.foundation.annotation.ReturnData;
import com.cjhxfund.foundation.annotation.SessionType;
import com.cjhxfund.foundation.annotation.SessionType.SType;
import com.cjhxfund.foundation.util.data.DocUtil;
import com.cjhxfund.foundation.util.io.HtmlUtil;
import com.cjhxfund.foundation.util.io.PathUtil;
import com.cjhxfund.foundation.web.context.SpringContext;
import com.cjhxfund.foundation.web.model.APIMonitor;
import com.cjhxfund.foundation.web.model.CodeTipMsg;
import com.cjhxfund.foundation.web.model.HttpCtrl;
import com.cjhxfund.foundation.web.service.IUtilSV;

/**
 * 获取系统管理的数据信息
 * @author xiejs
 * @date 2015年6月27日
 */
@RestController
@RequestMapping("/sysmgr/util")
@ModuleInfo(value="sysmgr",name="系统管理",subModule="util",subModName="系统帮助")
public class UtilController extends BaseController{
	
	
	
	@Autowired
	IUtilSV utilSv;
	
	@Author(value={"Jason"},date={"2015-12-16"})
	@SessionType(value = SType.DISPENSABLE)
	@LanhanParams({
		@LanhanParam(field="beanName",name="bean名称123",required=true)
	})
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/init", method = RequestMethod.GET,name="初始化系统信息")
	public  void init(HttpCtrl httpCtrl) {
		utilSv.init(httpCtrl);
	}
	
	@Author(value={"Jason"},date={"2017-09-16"})
	@SessionType(value = SType.COMPELLED)
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/getInitList", method = RequestMethod.GET,name="获取初始化请求列表")
	public  void getInitList(HttpCtrl httpCtrl) {
		utilSv.getInitList(httpCtrl);
	}
	
	
	@Author(value={"Jason"},date={"2017-08-10"})
	@SessionType(value = SType.NOT_COMPELLED)
	@LanhanParams({
		@LanhanParam(field="imgInfo",name="base64图片字符串",required=true),
		@LanhanParam(field="fileName",name="文件名字",required=true)
	})
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/echartToImg", method = RequestMethod.POST, name="echart的图片转为img保存，导出pdf")
	public  void echartToImg(HttpCtrl httpCtrl) {
		String imgInfo = httpCtrl.getAsString("imgInfo");
		String fileName = httpCtrl.getAsString("fileName")+".png";
		imgInfo = imgInfo.replaceAll(" ", "+");
		String imgRelativePath = "static"+File.separator+"images"+File.separator+"echart"+File.separator+fileName;
		String filePath = PathUtil.getAppPath()+imgRelativePath;
		HtmlUtil.decodeBase64(imgInfo, filePath);
		filePath = SpringContext.root+imgRelativePath;
		httpCtrl.sendJson(1,fileName);
	}
	
	@Author(value={"Jason"},date={"2017-06-26"})
	@SessionType(value = SType.COMPELLED)
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/listThread", method = RequestMethod.GET,name="获取系统线程列表")
	public  void listThread(HttpCtrl httpCtrl) {
		utilSv.listThread(httpCtrl);
	}
	
	@Author(value={"Jason"},date={"2017-06-13"})
	@SessionType(value = SType.COMPELLED)
	@LanhanParams({
		@LanhanParam(field="filePath",name="绝对路径",required=true),
		@LanhanParam(field="fileName",name="文件名",required=true)
	})
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/loadFile", method = RequestMethod.GET, name="下载系统文件")
	public void loadFile(HttpCtrl httpCtrl) {
		String filePath = httpCtrl.getAsString("filePath");
		String fileName = httpCtrl.getAsString("fileName");
		httpCtrl.sendFile(filePath, fileName);
	}
	
	@Author(value={"Jason"},date={"2017-10-17"})
	@SessionType(value = SType.COMPELLED)
	@LanhanParams({
		@LanhanParam(field="logDateStr",name="日志日期", required=true),
		@LanhanParam(field="fileType",name="日志文件类型", required=true, desc="data,task,start,error")
	})
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/logger", method = RequestMethod.GET, name="获取指定日期的日志")
	public void logger(HttpCtrl httpCtrl) {
		utilSv.loggerFile(httpCtrl);
	}
	
	@Author(value={"Jason"},date={"2017-06-13"})
	@SessionType(value = SType.COMPELLED)
	@LanhanParams({
		@LanhanParam(field="fileType",name="文件类型", desc="app：项目系统内部,tomcat：tomcat内部", required=true),
		@LanhanParam(field="fileDateStr",name="日志日期"),
		@LanhanParam(field="dateType",name="文件日期格式",desc="yyyy-mm-dd, 还是yyyy/mm/dd"),
		@LanhanParam(field="filePath",name="相对路径", desc="如果是app，则相对app目录，比如：/ips/，如果是tomcat，则相对系统目录",required=true)
	})
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/fileList", method = RequestMethod.GET,name="获取系统文件")
	public void fileList(HttpCtrl httpCtrl) {
		utilSv.fileList(httpCtrl);
	}
	
	
	@Author(value={"Jason"},date={"2017-06-12"})
	@SessionType(value = SType.COMPELLED)
	@LanhanParams({
		@LanhanParam(field="file", type=ParamType.File, name="系统文件",required=true,
		desc={"104857600", "*", "noPath","config","genName"}),
		@LanhanParam(field="fileType",name="文件类型", desc="config:配置文件，static：页面静态文件", required=true),
		@LanhanParam(field="filePath",name="相对路径", desc="如果是config，则相对classes目录，如果是static，则相对系统目录，比如：/ips/"),
	})
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST,name="上传系统文件")
	public  void uploadFile(HttpCtrl httpCtrl) {
		utilSv.uploadFile(httpCtrl);
	}
	
	
	@Author(value={"xiejs"},date={"2015-07-03"})
	@SessionType(value = SType.COMPELLED)
	@LanhanParams({
		@LanhanParam(field="apiName",name="请求名称" ),
		@LanhanParam(field="sessionType",name="API类型", type=ParamType.PositiveInt, desc="是否需要登录，1：是，其他不是" ),
		@LanhanParam(field="qryMethod",name="请求方法")
	})
	@ReturnData(value="[total:2,rows[{},{}]",desc="total:满足条件的总数; rows:List<DocData>的json数据")
	@RequestMapping(value = "/getDoc", method = RequestMethod.GET,name="获取对外开放的API接口")
	public  void getDoc(HttpCtrl httpCtrl) {
		utilSv.getDoc(httpCtrl);
	}
	
	
	@Author(value={"xiejs"},date={"2017-10-25"})
	@SessionType(value = SType.COMPELLED)
	@LanhanParams({
		@LanhanParam(field="url",name="请求路径" ,required=true),
		@LanhanParam(field="state",name="API状态", type=ParamType.PositiveInt, desc="0：停止运行，1：运行正常，2：调试运行" ,required=true),
		@LanhanParam(field="method",name="请求方法",required=true)
	})
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/changeAPI", method = RequestMethod.GET,name="停止或者重启API")
	public  void changeAPI(HttpCtrl httpCtrl) {
		String url = httpCtrl.getAsString("url");
		String method = httpCtrl.getAsString("method");
		int state = httpCtrl.getAsInteger("state");
		DocUtil.changeAPI(method, url, state);
		List<APIMonitor> docs = DocUtil.getMonitorList();
		httpCtrl.sendData(docs);
	}
	
	
	

	

}
