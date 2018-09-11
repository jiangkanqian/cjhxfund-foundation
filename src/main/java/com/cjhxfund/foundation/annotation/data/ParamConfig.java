package com.cjhxfund.foundation.annotation.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jason
 * 2017年8月15日
 * 功能：参数配置文件中使用
 */
public class ParamConfig extends ParamItem{

	List<String> cmList = new ArrayList<String>();

	public List<String> getCmList() {
		return cmList;
	}

	public void setCmList(List<String> cmList) {
		this.cmList = cmList;
	}
	
	public ParamItem clone(){
		ParamItem item = new ParamItem();
		item.setApiId(getApiId());
		item.setField(getField());
		item.setName(getName());
		item.setDefautlValue(getDefautlValue());
		item.setParamType(getParamType());
		item.setTypeName(getTypeName());
		item.setParamDesc(getParamDesc());
		item.setParamRegex(getParamRegex());
		item.setRequired(getRequired());
		item.setMatchType(getMatchType());
		item.setMaxLength(getMaxLength());
		item.setMinLength(getMinLength());
		item.setMinValue(getMinValue());
		item.setMaxValue(getMaxValue());
		return item;
	}
	
}
