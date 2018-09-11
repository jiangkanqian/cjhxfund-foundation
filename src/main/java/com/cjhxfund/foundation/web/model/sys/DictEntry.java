package com.cjhxfund.foundation.web.model.sys;

import java.io.Serializable;

import com.cjhxfund.foundation.annotation.LanhanParam;
import com.cjhxfund.foundation.annotation.LanhanParam.ParamType;
import com.cjhxfund.foundation.web.model.BaseModel;

import org.springframework.stereotype.Component;
/**
 * 数据字典信息 
 * @author xiejiesheng
 * @date 2016-08-14 12:16:34 
 */
 @Component
public class DictEntry extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = 201608141216291872L;
	private Long dictId;//字典主键
	private String dictKey;//字典key
	private String dictValue;//字典value
	private Long typeId;//字典类型
	private String extKey;//拓展key
	private String extValue;//拓展value
	
	
	/** 字典主键 */
	public Long getDictId() {
		return dictId;
	}
	/** 字典主键 */
	@LanhanParam(field="dictId",name="字典主键",maxLength=10, checkMethod={ "update:1", },type=ParamType.Long)
	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}
	/** 字典key */
	public String getDictKey() {
		return dictKey;
	}
	/** 字典key */
	@LanhanParam(field="dictKey",name="字典key",maxLength=20, checkMethod={"query:0", "update:1", "add:1", })
	public void setDictKey(String dictKey) {
		this.dictKey = dictKey== null||dictKey.equals("") ? null : dictKey.trim();
	}
	/** 字典value */
	public String getDictValue() {
		return dictValue;
	}
	/** 字典value */
	@LanhanParam(field="dictValue",name="字典value",maxLength=100, checkMethod={ "update:1", "add:1", })
	public void setDictValue(String dictValue) {
		this.dictValue = dictValue== null||dictValue.equals("") ? null : dictValue.trim();
	}
	/** 字典类型 */
	public Long getTypeId() {
		return typeId;
	}
	/** 字典类型 */
	@LanhanParam(field="typeId",name="字典类型",maxLength=10, checkMethod={"query:0", "update:1", "add:1",},type=ParamType.Long)
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	/** 拓展key */
	public String getExtKey() {
		return extKey;
	}
	/** 拓展key */
	@LanhanParam(field="extKey",name="拓展key",maxLength=20, checkMethod={ "update:0", "add:0", })
	public void setExtKey(String extKey) {
		this.extKey = extKey== null||extKey.equals("") ? null : extKey.trim();
	}
	/** 拓展value */
	public String getExtValue() {
		return extValue;
	}
	/** 拓展value */
	@LanhanParam(field="extValue",name="拓展value",maxLength=100, checkMethod={ "update:0", "add:0", })
	public void setExtValue(String extValue) {
		this.extValue = extValue== null||extValue.equals("") ? null : extValue.trim();
	}
	
	
	public String toString() {
		 return "{ dictId:"+getDictId()+ ",  dictKey:"+getDictKey()+ ",  dictValue:"+getDictValue()+ ",  typeId:"+getTypeId()+ ",  extKey:"+getExtKey()+ ",  extValue:"+getExtValue()+ ",  createdate:"+getCreatedate()+ ",  createtime:"+getCreatetime()+ ",  updatedate:"+getUpdatedate()+ ",  updatetime:"+getUpdatetime()+ ",  opId:"+getOpId()+ ",  remark:"+getRemark()+ ",  extra:"+getExtra()+ ", }";
	}

}

