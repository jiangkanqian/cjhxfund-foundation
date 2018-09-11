package com.cjhxfund.foundation.web.model.gencode;

import java.util.ArrayList;
import java.util.List;

import com.cjhxfund.foundation.util.data.NumberUtil;
import com.cjhxfund.foundation.util.str.BeanUtil;
import com.cjhxfund.foundation.util.str.DateUtil;

/**
 * @author lanhan
 * @date 2015年11月9日
 */
public class TemplateData {
	
	//对应的表和对象名称信息
	private String className;
	private String classObjName;
	private String pkName;
	private String pkCol;
	private String pkType;
	private String autoIncrement = "0";
	private String createTime = DateUtil.getToday();
	private String suid = NumberUtil.getOrderNo(4)+"L";
	
	private String tableName;
	private String subModName;
	private String packName;
	private String moduName;
	private String comment;
	private String hasDel = "0";
	private String hasVersion = "0";
	private String orderBy = "0";
	
	//工程模块信息
	private String author = "lanhan";
	
	private String dbType = "oracle";//数据库类型
	
	private List<TableToClass> tcList = new ArrayList<TableToClass>();
	
	
	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getModuName() {
		return moduName;
	}

	public void setModuName(String moduName) {
		this.moduName = moduName;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
		String[] strArr = packName.split("\\.");
		this.moduName = strArr[strArr.length-1];
//		System.out.println(this.moduName);
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	
	public String getSuid() {
		return suid;
	}
	public String getHasDel() {
		return hasDel;
	}
	public void setHasDel(String hasDel) {
		this.hasDel = hasDel;
	}
	public String getHasVersion() {
		return hasVersion;
	}
	public void setHasVersion(String hasVersion) {
		this.hasVersion = hasVersion;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * 设置表名，真实的表名，不做任何转化
	 * @param tableName
	 */
	public void setTableNameOrigin(String tableName) {
		this.tableName = tableName;
		this.className = BeanUtil.getClassName(tableName);
		this.classObjName = BeanUtil.lowerFirestChar(this.className);
	}
	
	/**
	 * 先转化为小写，然后再做处理
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		tableName = tableName.toLowerCase();
		this.className = BeanUtil.getClassName(tableName);
		this.classObjName = BeanUtil.lowerFirestChar(this.className);
		this.tableName = tableName;
	}
	public String getSubModName() {
		return subModName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
		this.subModName = comment.split("::")[0];
	}
	
	public String getPkCol() {
		return pkCol;
	}
	public void setPkCol(String pkCol, String pkType) {
		this.pkCol = pkCol;
		if(pkCol.startsWith("l_")||pkCol.startsWith("d_")||pkCol.startsWith("c_")){
			pkCol = pkCol.substring(2);
		}
		else if(pkCol.startsWith("vc_")||pkCol.startsWith("en_")
				||pkCol.startsWith("bl_")||pkCol.startsWith("dt_")){
			pkCol = pkCol.substring(3);
		}
		this.pkName = BeanUtil.deleteUnderline(pkCol);
		this.pkType = pkType;
	}
	public String getAutoIncrement() {
		return autoIncrement;
	}
	public void setAutoIncrement(String autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	public String getClassObjName() {
		return classObjName;
	}
	public String getClassName() {
		return className;
	}
	public String getPkName() {
		return pkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	public String getPkType() {
		return pkType;
	}
	public void setPkType(String pkType) {
		this.pkType = pkType;
	}
	public List<TableToClass> getTcList() {
		return tcList;
	}
	
	public void setTcList(List<TableToClass> tcList) {
		this.tcList = tcList;
	}
	public void addTcList(TableToClass tc) {
		this.tcList.add(tc);
	}
	@Override
	public String toString() {
		return "TempData [className=" + className + ", classObjName=" + classObjName + ", pkName="
				+ pkName + ", pkCol=" + pkCol + ", pkType=" + pkType + ", autoIncrement="
				+ autoIncrement + ", tableName=" + tableName 
				+ ", subModName=" + subModName + ", comment=" + comment 
				+ ", tcList=" + tcList + "]";
	}

	
	
	
	
	
	

}
