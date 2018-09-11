package com.cjhxfund.foundation.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cjhxfund.foundation.annotation.Author;
import com.cjhxfund.foundation.annotation.LanhanParam;
import com.cjhxfund.foundation.annotation.LanhanParam.ParamType;
import com.cjhxfund.foundation.annotation.LanhanParams;
import com.cjhxfund.foundation.annotation.ReturnData;
import com.cjhxfund.foundation.annotation.SessionType;
import com.cjhxfund.foundation.annotation.SessionType.SType;
import com.cjhxfund.foundation.web.context.SpringContext;
import com.cjhxfund.foundation.web.model.CodeTipMsg;
import com.cjhxfund.foundation.web.model.HttpCtrl;
import com.cjhxfund.foundation.web.model.TaskInfo;
import com.cjhxfund.foundation.web.service.BaseSchedule;
import com.cjhxfund.foundation.web.service.IUtilSV;

/**
 * 获取系统管理的数据信息
 * @author xiejs
 * @date 2015年6月27日
 */
@RestController
@RequestMapping("/sysmgr/task")
public class TaskController extends BaseController{
	
	
	@Autowired
	IUtilSV utilSv;
	
	
	@Author(value={"Jason"},date={"2017-05-16"})
	@SessionType(value = SType.COMPELLED)
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/page", method = RequestMethod.GET,name="获取系统定时任务")
	public void page(HttpCtrl httpCtrl) {
		List<Object> beans = new ArrayList<Object>(SpringContext.appContext.getBeansOfType(BaseSchedule.class, false, true).values());
		int beanCount = beans.size();
		List<TaskInfo> data = new ArrayList<TaskInfo>();
		for (int i = 0; i < beanCount; i++) {
			Object bean = beans.get(i);
			BaseSchedule init = (BaseSchedule) bean;
			data.add(init.toTaskInfo());
		}
		httpCtrl.sendJson(data, data.size());
	}
	
	@Author(value={"Jason"},date={"2017-07-25"})
	@SessionType(value = SType.COMPELLED)
	@LanhanParams({
		@LanhanParam(field="taskNo",name="任务编号", desc="", required=true),
		@LanhanParam(field="doTaskIP",name="执行节点", desc="", required=true),
		@LanhanParam(field="hour",name="几点", required=true, type=ParamType.PositiveInt),
		@LanhanParam(field="minute",name="几分", required=true, type=ParamType.PositiveInt),
		@LanhanParam(field="second",name="几秒", required=true, type=ParamType.PositiveInt)
	})
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "", method = RequestMethod.PUT,name="修改系统定时任务")
	public  void updateTask(HttpCtrl httpCtrl) {
		String taskNo = httpCtrl.getAsString("taskNo");
		BaseSchedule task = (BaseSchedule) SpringContext.getSpringBean(taskNo);
		if(null != task){
			int hour = httpCtrl.getAsInteger("hour");
			int minute = httpCtrl.getAsInteger("minute");
			int second = httpCtrl.getAsInteger("second");
			String ip = httpCtrl.getAsString("doTaskIP");
			if(!task.getDoTaskIP().equals(ip)){
				task.setDoTaskIP(ip);
			}
			task.resetTime(hour, minute, second);
			httpCtrl.sendJson(1,"重置定时任务成功！");
		}
		else{
			httpCtrl.sendJson(0,"找不到定时任务！");
		}
	}
	
	@Author(value={"Jason"},date={"2017-07-26"})
	@SessionType(value = SType.COMPELLED)
	@LanhanParams({
		@LanhanParam(field="taskNo",name="任务编号", desc="", required=true),
	})
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/do", method = RequestMethod.GET,name="手动执行系统定时任务")
	public  void doTask(HttpCtrl httpCtrl) {
		String taskNo = httpCtrl.getAsString("taskNo");
		BaseSchedule task = (BaseSchedule) SpringContext.getSpringBean(taskNo);
		if(null != task){
			task.doTask();
			httpCtrl.sendJson(1,"执行定时任务成功！");
		}
		else{
			httpCtrl.sendJson(0,"找不到定时任务！");
		}
	}
	
	@Author(value={"Jason"},date={"2017-07-26"})
	@SessionType(value = SType.COMPELLED)
	@LanhanParams({
		@LanhanParam(field="taskNo",name="任务编号", desc="", required=true),
	})
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "/reset", method = RequestMethod.GET,name="手动执行系统定时任务")
	public  void reset(HttpCtrl httpCtrl) {
		String taskNo = httpCtrl.getAsString("taskNo");
		BaseSchedule task = (BaseSchedule) SpringContext.getSpringBean(taskNo);
		if(null != task){
			task.reset();
			httpCtrl.sendJson(1,"重置定时任务成功！");
		}
		else{
			httpCtrl.sendJson(0,"找不到定时任务！");
		}
	}
	
	@Author(value={"Jason"},date={"2017-07-25"})
	@SessionType(value = SType.COMPELLED)
	@ReturnData(desc="", opFail=CodeTipMsg.COMMON_FAIL, opOk=CodeTipMsg.COMMON_SUCCESS)
	@RequestMapping(value = "", method = RequestMethod.DELETE,name="删除系统定时任务")
	public  void deleteTask(HttpCtrl httpCtrl) {
		List<String> data = httpCtrl.jsonArrToList(String.class);
		for(String taskNo : data){
			BaseSchedule task = (BaseSchedule) SpringContext.getSpringBean(taskNo);
			if(null != task){
				task.cancel();
				httpCtrl.sendJson(1,"取消定时任务成功！");
			}
			else{
				httpCtrl.sendJson(0,"找不到定时任务！");
				return;
			}
		}
	}
	
	
	
	
	
	
	

	

}
