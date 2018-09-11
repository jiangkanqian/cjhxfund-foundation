package com.cjhxfund.foundation.web.model.sys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cjhxfund.foundation.annotation.LanhanParam;
import com.cjhxfund.foundation.annotation.LanhanParam.ParamType;
import com.cjhxfund.foundation.web.model.BaseModel;
/**
 * 字典类型 
 * @author xiejiesheng
 * @date 2016-08-14 12:16:34 
 */
 @Component
public class DictType extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = 201608141216296999L;
	private Long typeId;//类型主键
	private String typeName;//类型名
	private Long parentId;//父类主键
	private String typeNo;//类型名(英文)
	
	private Long dproId;//关联主键  用于删除
	
	private Long projId;//关联项目系统
	
	List<DictEntry> dictList = new ArrayList<DictEntry>();
	
	//用于显示
	private String projname;//项目英文名
	private String projnameChs;//项目中文名
	
	
	
	
	public Long getProjId() {
		return projId;
	}
	public void setProjId(Long projId) {
		this.projId = projId;
	}
	public List<DictEntry> getDictList() {
		return dictList;
	}
	public void setDictList(List<DictEntry> dictList) {
		this.dictList = dictList;
	}
	public Long getDproId() {
		return dproId;
	}
	public void setDproId(Long dproId) {
		this.dproId = dproId;
	}
	public String getProjname() {
		return projname;
	}
	public void setProjname(String projname) {
		this.projname = projname;
	}
	
	public void setProjnameChs(String projnameChs) {
		this.projnameChs = projnameChs;
	}
	public String getProjnameChs() {
		return projnameChs;
	}
	/** 类型主键 */
	public Long getTypeId() {
		return typeId;
	}
	/** 类型主键 */
	@LanhanParam(field="typeId",name="类型主键",maxLength=10, checkMethod={ "update:1", },type=ParamType.Long)
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	/** 类型名 */
	public String getTypeName() {
		return typeName;
	}
	/** 类型名 */
	@LanhanParam(field="typeName",name="类型名",maxLength=100, checkMethod={"query:0", "update:1", "add:1", })
	public void setTypeName(String typeName) {
		this.typeName = typeName== null||typeName.equals("") ? null : typeName.trim();
	}
	/** 父类主键 */
	public Long getParentId() {
		return parentId;
	}
	/** 父类主键 */
	@LanhanParam(field="parentId",name="父类主键",maxLength=10, checkMethod={  },type=ParamType.Long)
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/** 类型名(英文) */
	public String getTypeNo() {
		return typeNo;
	}
	/** 类型名(英文) */
	@LanhanParam(field="typeNo",name="类型名(英文)",maxLength=100, checkMethod={"query:0", "update:1", "add:1", })
	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo== null||typeNo.equals("") ? null : typeNo.trim();
	}
	
	
	@Override
	public String toString() {
		return "DictType [typeId=" + typeId + ", typeName=" + typeName + ", typeNo=" + typeNo + ", dproId=" + dproId + ", projId=" + projId + ", dictList=" + dictList + "]";
	}

}

