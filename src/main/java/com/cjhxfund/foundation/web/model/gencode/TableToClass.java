package com.cjhxfund.foundation.web.model.gencode;

import com.cjhxfund.foundation.util.str.BeanUtil;



/**
 * @author lanhan
 * @date 2015年11月9日
 */
public class TableToClass {

	private String colName;
	private String fieldName;
	private String upperFieldName;
	
	private String colType;
	private String typeName;
	
	private boolean isNull;
	private String nullFlag;
	
	private String comment;
	private String comName;
	private String extra;
	private String defaultValue;
	
	private boolean isPk;
	private boolean isAuto;
	
	private String colLength;
	private String modelValue;
	
	private Integer queryType;//是否必传，0：非必传，1：必传
	private Integer addType;//添加标志，0：表示选填，1：表示要必填，2：表示只读
	private Integer updateType;//修改标志，1：表示要必填，2：表示只读
	
	//如果是oracle number类型，则会有以下数字
	private Integer precision;//整数部分
	private Integer scale;//小数部分
	
	
	
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public Integer getPrecision() {
		return precision;
	}
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
	public Integer getScale() {
		return scale;
	}
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	public Integer getQueryType() {
		return queryType;
	}
	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}
	public Integer getAddType() {
		return addType;
	}
	public void setAddType(Integer addType) {
		this.addType = addType;
	}
	public Integer getUpdateType() {
		return updateType;
	}
	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}
	public String getModelValue() {
		return modelValue;
	}
	public void setModelValue(String modelValue) {
		this.modelValue = modelValue;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getUpperFieldName() {
		return upperFieldName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public String getColLength() {
		return colLength;
	}
	public void setColLength(String colLength) {
		this.colLength = colLength;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType, String type, int length) {
		colType = colType.toLowerCase();
		this.colType = colType;
		this.colLength = length+"";
		this.typeName = BeanUtil.typeTrans(colType, type, length);
		if(this.precision!=null && this.precision > 0){
			if(this.scale!=null && this.scale > 0){
				this.typeName = "Double";
			}
			else if(this.precision >= 10){
				this.typeName = "Long";
			}
			else {
				this.typeName = "Integer";
			}
		}
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		colName = colName.toLowerCase();
		this.colName = colName;
		if(colName.startsWith("l_")||colName.startsWith("d_")||colName.startsWith("c_")){
			colName = colName.substring(2);
		}
		else if(colName.startsWith("vc_")||colName.startsWith("en_")
				||colName.startsWith("bl_")||colName.startsWith("dt_")){
			colName = colName.substring(3);
		}
		this.fieldName = BeanUtil.deleteUnderline(colName);
		this.upperFieldName = BeanUtil.upperFirestChar(this.fieldName);
	}
	public boolean isNull() {
		return isNull;
	}
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
	public String getNullFlag() {
		return nullFlag;
	}
	public void setNullFlag(String nullFlag) {
		this.nullFlag = nullFlag;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		if(null != comment && comment.length() > 0){
			this.comment = comment;
			this.comName = comment.split("::")[0];
		}
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public boolean isPk() {
		return isPk;
	}
	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}
	
	public boolean isAuto() {
		return isAuto;
	}
	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}
	
	@Override
	public String toString() {
		return "TableToClass [colName=" + colName + ", fieldName=" + fieldName
				+ ", upperFieldName=" + upperFieldName + ", colType=" + colType + ", typeName="
				+ typeName + ", isNull=" + isNull + ", nullFlag=" + nullFlag + ", comment="
				+ comment + ", extra=" + extra + ", defaultValue=" + defaultValue + ", isPk="
				+ isPk + ", colLength=" + colLength + "]";
	}
	
	
	
	
	
	
}
