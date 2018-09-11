package com.cjhxfund.foundation.web.controller;

import java.util.List;
import java.util.Map;

import com.cjhxfund.foundation.util.data.DataType;
import com.cjhxfund.foundation.web.model.BaseModel;
import com.cjhxfund.foundation.web.model.CodeTipMsg;
import com.cjhxfund.foundation.web.model.HttpCtrl;
import com.cjhxfund.foundation.web.service.IBaseSV;

/**
 * @author lanhan
 * @date 2015年12月6日
 */
public class BaseController {

	/**
	 * 分页查询，获取分页总数
	 * @param condition 查询条件
	 * @param baseSV 引入的Service
	 * @param defaultReturn 默认返回数
	 * @return
	 */
	public int getTotal(Map<String, Object> condition, IBaseSV<?> baseSV, int defaultReturn) {
		int total = defaultReturn;
		// 如果有分页参数，而且查询出来的总数等于该页总数，并且页数大于1，则说明总数一页显示也不来
		if (condition.get("rows") != null && total == DataType.getAsInt(condition.get("rows")) || DataType.getAsInt(condition.get("page")) > 1) {
			total = baseSV.count(condition);
		}
		return total;
	}

	/**
	 * 分页查询数据，用于通用的分页查询
	 * @param httpCtrl 请求数据容器
	 * @param baseSV 引入的Service
	 */
	public void queryPage(HttpCtrl httpCtrl, IBaseSV<?> baseSV) {
		Map<String, Object> condition = httpCtrl.getQueryMap();
		List<?> data = baseSV.query(condition);
		int total = data.size();
		// int total = getTotal(condition, baseSV, data.size());
		// Object o = data.get(0);
		if (total > 0) {
			BaseModel model = (BaseModel) data.get(0);
			if (model.getQueryTotal() != null && model.getQueryTotal().intValue() > 0) {
				total = model.getQueryTotal();// 高本版使用
			} else {
				total = getTotal(condition, baseSV, data.size());
			}
		}
		httpCtrl.sendJson(data, total);
	}

	/**
	 * 返回添加数据的提示信息
	 * @param updateNum 数据库修改数量
	 * @param error 默认返回的错误消息
	 * @return
	 */
	public String getAddMsg(int updateNum, String error) {
		String msg = "";
		if (updateNum > 0) {
			msg = CodeTipMsg.COMMON_SUCCESS.toString();
		} else if (updateNum == -1) {
			msg = CodeTipMsg.ADD_FAIl.toString(error);
		} else {
			msg = CodeTipMsg.ADD_FAIl.toString("修改0条数据！");
		}
		return msg;
	}

	/**
	 * 返回添加数据的提示信息
	 * @param updateNum 数据库修改数量
	 * @param error 默认返回的错误消息
	 * @param success 默认返回的成功提示消息
	 * @return
	 */
	public String getAddMsg(int updateNum, String error, String success) {
		String msg = "";
		if (updateNum > 0) {
			msg = CodeTipMsg.COMMON_SUCCESS.toString(success);
		} else if (updateNum == -1) {
			msg = CodeTipMsg.ADD_FAIl.toString(error);
		} else {
			msg = CodeTipMsg.ADD_FAIl.toString("添加0条数据！");
		}
		return msg;
	}

	/**
	 * 返回修改数据的提示信息
	 * @param updateNum 数据库修改数量, >0，返回默认的成功信息，-1返回自定义错误消息，其他说明没有修改数据
	 * @param error 默认返回的错误消息
	 * @return
	 */
	public String getUpdateMsg(int updateNum, String error) {
		String msg = "";
		if (updateNum > 0) {
			msg = CodeTipMsg.COMMON_SUCCESS.toString();
		} else if (updateNum == -1) {
			msg = CodeTipMsg.UPDATE_FAIl.toString(error);
		} else {
			msg = CodeTipMsg.UPDATE_FAIl.toString(error);
		}
		return msg;
	}

	/**
	 * 返回修改数据的提示信息
	 * @param updateNum 数据库修改数量
	 * @param error 默认返回的错误消息
	 * @param success 默认返回的成功提示消息
	 * @return
	 */
	public String getUpdateMsg(int updateNum, String error, String success) {
		String msg = "";
		if (updateNum > 0) {
			msg = CodeTipMsg.COMMON_SUCCESS.toString(success);
		} else if (updateNum == -1) {
			msg = CodeTipMsg.UPDATE_FAIl.toString(error);
		} else {
			msg = CodeTipMsg.UPDATE_FAIl.toString(error);
		}
		return msg;
	}
	
	public void action(HttpCtrl httpCtrl, String methodName){
		//根据方法名称实现调用具体的controller方法
		
	}

}
