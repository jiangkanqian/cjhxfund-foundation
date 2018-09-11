package com.cjhxfund.foundation.annotation.data;

import java.util.ArrayList;
import java.util.List;

import com.cjhxfund.foundation.annotation.LanhanParam;

/**
 * 生成API的数据类
 * @author xiejiesheng
 * @date 2015年5月13日
 */
public class DocData {
	
//	private DocAuthor author; //作者信息
	private String projName;//系统名称
	private String url;//相对地址
	private String apiName;//请求接口名称, 对应RequestMapping的name值
//	private String methodName;//对应执行方法名
//	private String className;//对应执行类名
	private String apiType;//API类型，默认Controller 
	private String method;//请求方法
	private String parentModuleName;//模块名称
	private String parentModuleNameEn;//模块英文名称
	private String subModuleName;//子模块名称
	private String subModuleNameEn;//子模块英文名称
	private String sessionType;//session类型（英文）
	private String sessionDesc;//session类型描述
	private String returnValue;//正常返回值
	private String returnFail;//错误返回值
	private String returnDesc;//返回值简介描述
	private String returnModel;//返回值字段模型
	private String paramsDesc;//参数总说明
	private String version = "1.0";//版本号
	private String specialApi;//特殊API，如文件上传
	private String author;//创建或者修改者名称
	private String editDate;//创建或者修改时间
	private String fileUrl;//文件附件
	
	private List<ParamItem> params=new ArrayList<ParamItem>();//具体请求参数信息
	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/** 系统名称 */
	public String getProjName() {
		return projName;
	}

	/** 系统名称 */
	@LanhanParam(field = "projName", name = "系统名称", maxLength = 128, checkMethod = {})
	public void setProjName(String projName) {
		this.projName = projName == null || projName.equals("") ? null : projName.trim();
	}
	/** 相对地址 */
	public String getUrl() {
		return url;
	}
	/** 相对地址 */
	@LanhanParam(field="url",name="相对地址",maxLength=256, checkMethod={   })
	public void setUrl(String url) {
		this.url = url== null||url.equals("") ? null : url.trim();
	}
	/** 接口名称 */
	public String getApiName() {
		return apiName;
	}
	/** 接口名称 */
	@LanhanParam(field = "apiName", name = "接口名称", maxLength = 128, checkMethod = { "query:0" })
	public void setApiName(String apiName) {
		this.apiName = apiName == null || apiName.equals("") ? null : apiName.trim();
	}
	/** API类型，默认Controller */
	public String getApiType() {
		return apiType;
	}
	/** API类型，默认Controller */
	@LanhanParam(field="apiType",name="API类型，默认Controller",maxLength=32, checkMethod={   })
	public void setApiType(String apiType) {
		this.apiType = apiType== null||apiType.equals("") ? null : apiType.trim();
	}
	/** 请求方法 */
	public String getMethod() {
		return method;
	}
	/** 请求方法 */
	@LanhanParam(field="reqMethod",name="请求方法",maxLength=10, checkMethod={"query:0", })
	public void setMethod(String reqMethod) {
		this.method = reqMethod== null||reqMethod.equals("") ? null : reqMethod.trim();
	}
	/** 模块名称 */
	public String getParentModuleName() {
		return parentModuleName;
	}
	/** 模块名称 */
	@LanhanParam(field="parentModuleName",name="模块名称",maxLength=128, checkMethod={   })
	public void setParentModuleName(String parentModuleName) {
		this.parentModuleName = parentModuleName== null||parentModuleName.equals("") ? null : parentModuleName.trim();
	}
	/** 模块英文名称 */
	public String getParentModuleNameEn() {
		return parentModuleNameEn;
	}
	/** 模块英文名称 */
	@LanhanParam(field="parentModuleNameEn",name="模块英文名称",maxLength=128, checkMethod={   })
	public void setParentModuleNameEn(String parentModuleNameEn) {
		this.parentModuleNameEn = parentModuleNameEn== null||parentModuleNameEn.equals("") ? null : parentModuleNameEn.trim();
	}
	/** 子模块名称 */
	public String getSubModuleName() {
		return subModuleName;
	}
	/** 子模块名称 */
	@LanhanParam(field="subModuleName",name="子模块名称",maxLength=128, checkMethod={   })
	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName== null||subModuleName.equals("") ? null : subModuleName.trim();
	}
	/** 子模块英文名称 */
	public String getSubModuleNameEn() {
		return subModuleNameEn;
	}
	/** 子模块英文名称 */
	@LanhanParam(field="subModuleNameEn",name="子模块英文名称",maxLength=128, checkMethod={   })
	public void setSubModuleNameEn(String subModuleNameEn) {
		this.subModuleNameEn = subModuleNameEn== null||subModuleNameEn.equals("") ? null : subModuleNameEn.trim();
	}
	/** session类型（英文） */
	public String getSessionType() {
		return sessionType;
	}
	/** session类型（英文） */
	@LanhanParam(field="sessionType",name="session类型（英文）",maxLength=64, checkMethod={   })
	public void setSessionType(String sessionType) {
		this.sessionType = sessionType== null||sessionType.equals("") ? null : sessionType.trim();
	}
	/** session类型描述 */
	public String getSessionDesc() {
		return sessionDesc;
	}
	/** session类型描述 */
	@LanhanParam(field="sessionDesc",name="session类型描述",maxLength=128, checkMethod={   })
	public void setSessionDesc(String sessionDesc) {
		this.sessionDesc = sessionDesc== null||sessionDesc.equals("") ? null : sessionDesc.trim();
	}
	/** 正常返回值 */
	public String getReturnValue() {
		return returnValue;
	}
	/** 正常返回值 */
	@LanhanParam(field="returnValue",name="正常返回值",maxLength=256, checkMethod={   })
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue== null||returnValue.equals("") ? null : returnValue.trim();
	}
	/** 错误返回值 */
	public String getReturnFail() {
		return returnFail;
	}
	/** 错误返回值 */
	@LanhanParam(field="returnFail",name="错误返回值",maxLength=256, checkMethod={   })
	public void setReturnFail(String returnFail) {
		this.returnFail = returnFail== null||returnFail.equals("") ? null : returnFail.trim();
	}
	/** 返回值简介描述 */
	public String getReturnDesc() {
		return returnDesc;
	}
	/** 返回值简介描述 */
	@LanhanParam(field="returnDesc",name="返回值简介描述",maxLength=256, checkMethod={   })
	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc== null||returnDesc.equals("") ? null : returnDesc.trim();
	}
	/** 返回值字段模型 */
	public String getReturnModel() {
		return returnModel;
	}
	/** 返回值字段模型 */
	@LanhanParam(field="returnModel",name="返回值字段模型",maxLength=512, checkMethod={   })
	public void setReturnModel(String returnModel) {
		this.returnModel = returnModel== null||returnModel.equals("") ? null : returnModel.trim();
	}
	/** 参数总说明 */
	public String getParamsDesc() {
		return paramsDesc;
	}
	/** 参数总说明 */
	@LanhanParam(field="paramsDesc",name="参数总说明",maxLength=256, checkMethod={   })
	public void setParamsDesc(String paramsDesc) {
		this.paramsDesc = paramsDesc== null||paramsDesc.equals("") ? null : paramsDesc.trim();
	}
	/** 特殊API，如文件上传 */
	public String getSpecialApi() {
		return specialApi;
	}
	/** 特殊API，如文件上传 */
	@LanhanParam(field="specialApi",name="特殊API，如文件上传",maxLength=64, checkMethod={   })
	public void setSpecialApi(String specialApi) {
		this.specialApi = specialApi== null||specialApi.equals("") ? null : specialApi.trim();
	}
	/** 创建或者修改者名称 */
	public String getAuthor() {
		return author;
	}
	/** 创建或者修改者名称 */
	@LanhanParam(field="author",name="创建或者修改者名称",maxLength=64, checkMethod={   })
	public void setAuthor(String author) {
		this.author = author== null||author.equals("") ? null : author.trim();
	}
	/** 创建或者修改时间 */
	public String getEditDate() {
		return editDate;
	}
	/** 创建或者修改时间 */
	@LanhanParam(field="editDate",name="创建或者修改时间",maxLength=32, checkMethod={   })
	public void setEditDate(String editDate) {
		this.editDate = editDate== null||editDate.equals("") ? null : editDate.trim();
	}
	
		

	public List<ParamItem> getParams() {
		return params;
	}

	public void setParams(List<ParamItem> params) {
		this.params = params;
	}
	public void addParams(ParamItem param) {
		this.params.add(param);
	}

	
	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	@Override
	public String toString() {
		return "DocData [projName=" + projName + ", url=" + url + ", apiName=" + apiName + ", apiType=" + apiType + ", method=" + method + ", parentModuleName=" + parentModuleName + ", parentModuleNameEn=" + parentModuleNameEn + ", subModuleName=" + subModuleName + ", subModuleNameEn=" + subModuleNameEn + ", sessionType=" + sessionType + ", sessionDesc=" + sessionDesc + ", returnValue=" + returnValue + ", returnFail=" + returnFail + ", returnDesc=" + returnDesc + ", returnModel=" + returnModel + ", paramsDesc=" + paramsDesc + ", specialApi=" + specialApi + ", author=" + author + ", editDate=" + editDate + ", params=" + params + "]";
	}

	
	









	


	
	
	
	

}


