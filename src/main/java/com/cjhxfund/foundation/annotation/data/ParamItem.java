package com.cjhxfund.foundation.annotation.data;

import java.util.ArrayList;
import java.util.List;

import com.cjhxfund.foundation.annotation.LanhanParam;
import com.cjhxfund.foundation.annotation.LanhanParam.ParamType;

public class ParamItem {

	private String apiId;// API编号
	private String field;// 参数字段
	private String name;// 字段名称
	private String defautlValue;// 默认值
	private String paramType;// 参数类型
	private String typeName;// 参数类型说明
	private String paramDesc;// 参数说明
	private String paramRegex;// 参数匹配规则
	private int required;// 是否必须传
	private int maxLength = -1;// 最大长度
	private int minLength = -1;// 最小长度
	private int matchType = 0;// 匹配类型，和正则相关,匹配模式：0：严格全部内容匹配，1：部分内容匹配
	private int maxValue = -1;// 最大值
	private int minValue = -1;// 最小值

	private List<ParamItem> subParams = new ArrayList<ParamItem>();// 子参数信息
	
	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public List<ParamItem> getSubParams() {
		return subParams;
	}

	public void setSubParams(List<ParamItem> subParams) {
		this.subParams = subParams;
	}

	/** API编号 */
	public String getApiId() {
		return apiId;
	}

	/** API编号 */
	@LanhanParam(field = "apiId", name = "API编号", maxLength = 34, checkMethod = { "query:0", })
	public void setApiId(String apiId) {
		this.apiId = apiId == null || apiId.equals("") ? null : apiId.trim();
	}

	/** 参数字段 */
	public String getField() {
		return field;
	}

	/** 参数字段 */
	@LanhanParam(field = "field", name = "参数字段", maxLength = 34, checkMethod = {})
	public void setField(String field) {
		this.field = field == null || field.equals("") ? null : field.trim();
	}

	/** 字段名称 */
	public String getName() {
		return name;
	}

	/** 字段名称 */
	@LanhanParam(field = "name", name = "字段名称", maxLength = 34, checkMethod = {})
	public void setName(String name) {
		this.name = name == null || name.equals("") ? null : name.trim();
	}

	/** 默认值 */
	public String getDefautlValue() {
		return defautlValue;
	}

	/** 默认值 */
	@LanhanParam(field = "defautlValue", name = "默认值", maxLength = 34, checkMethod = {})
	public void setDefautlValue(String defautlValue) {
		this.defautlValue = defautlValue == null || defautlValue.equals("") ? null : defautlValue.trim();
	}

	/** 参数类型 */
	public String getParamType() {
		return paramType;
	}

	/** 参数类型 */
	@LanhanParam(field = "paramType", name = "参数类型", maxLength = 34, checkMethod = {})
	public void setParamType(String paramType) {
		this.paramType = paramType == null || paramType.equals("") ? null : paramType.trim();
	}

	/** 参数类型说明 */
	public String getTypeName() {
		return typeName;
	}

	/** 参数类型说明 */
	@LanhanParam(field = "typeName", name = "参数类型说明", maxLength = 34, checkMethod = {})
	public void setTypeName(String typeName) {
		this.typeName = typeName == null || typeName.equals("") ? null : typeName.trim();
	}

	/** 参数说明 */
	public String getParamDesc() {
		return paramDesc;
	}

	/** 参数说明 */
	@LanhanParam(field = "paramDesc", name = "参数说明", maxLength = 34, checkMethod = {})
	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc == null || paramDesc.equals("") ? null : paramDesc.trim();
	}

	/** 参数匹配规则 */
	public String getParamRegex() {
		return paramRegex;
	}

	/** 参数匹配规则 */
	@LanhanParam(field = "paramRegex", name = "参数匹配规则", maxLength = 34, checkMethod = {})
	public void setParamRegex(String paramRegex) {
		this.paramRegex = paramRegex == null || paramRegex.equals("") ? null : paramRegex.trim();
	}

	/** 是否必须传 */
	public int getRequired() {
		return required;
	}

	/** 是否必须传 */
	@LanhanParam(field = "required", name = "是否必须传", maxLength = 2, checkMethod = {}, type = ParamType.Integer)
	public void setRequired(int required) {
		this.required = required;
	}

	/** 最大长度 */
	public int getMaxLength() {
		return maxLength;
	}

	/** 最大长度 */
	@LanhanParam(field = "maxLength", name = "最大长度", maxLength = 6, checkMethod = {}, type = ParamType.Integer)
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	/** 最小长度 */
	public int getMinLength() {
		return minLength;
	}

	/** 最小长度 */
	@LanhanParam(field = "minLength", name = "最小长度", maxLength = 6, checkMethod = {}, type = ParamType.Integer)
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	/** 匹配类型，和正则相关 */
	public int getMatchType() {
		return matchType;
	}

	/** 匹配类型，和正则相关 */
	@LanhanParam(field = "matchType", name = "匹配类型，和正则相关", maxLength = 6, checkMethod = {}, type = ParamType.Integer)
	public void setMatchType(int matchType) {
		this.matchType = matchType;
	}

}
