package com.cjhxfund.foundation.web.model.sys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cjhxfund.foundation.annotation.LanhanParam;
import com.cjhxfund.foundation.annotation.LanhanParam.ParamType;
import com.cjhxfund.foundation.web.model.BaseModel;
/**
 * 各系统角色 
 * @author xiejiesheng
 * @date 2016-10-13 
 */
 @Component
public class SysRole extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = 201610131953474798L;
	private String roleNo;//角色编号，方便编写程序
	private Long roleId;//主键
	private String roleName;//角色名称
	private Long parentId;//父角色主键
	private Long projId;//项目主键
	private String roleType;//角色类型，见数据字典
	private String roleTypeName;//角色类型，见数据字典
	private Integer grantType;//是否可以对其他角色进行授权
	private Integer loginType;//该角色用户是否可以登陆其他系统
	
	List<UserInfo> userList = new ArrayList<UserInfo>();
	
	//用于显示
	private String projname;//项目英文名
	private String projnameChs;//项目中文名
	
	
	public List<UserInfo> getUserList() {
		return userList;
	}
	public void setUserList(List<UserInfo> userList) {
		this.userList = userList;
	}
	public Integer getLoginType() {
		return loginType;
	}
	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
	public Integer getGrantType() {
		return grantType;
	}
	public void setGrantType(Integer grantType) {
		this.grantType = grantType;
	}
	public String getRoleTypeName() {
		return roleTypeName;
	}
	public void setRoleTypeName(String roleTypeName) {
		this.roleTypeName = roleTypeName;
	}
	public String getProjname() {
		return projname;
	}
	public void setProjname(String projname) {
		this.projname = projname;
	}
	public String getProjnameChs() {
		return projnameChs;
	}
	public void setProjnameChs(String projnameChs) {
		this.projnameChs = projnameChs;
	}
	
	
	/** 角色编号，方便编写程序 */
	public String getRoleNo() {
		return roleNo;
	}
	/** 角色编号，方便编写程序 */
	@LanhanParam(field="roleNo",name="角色编号，方便编写程序",maxLength=50, checkMethod={"query:0" })
	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo== null||roleNo.equals("") ? null : roleNo.trim();
	}
	/** 主键 */
	public Long getRoleId() {
		return roleId;
	}
	/** 主键 */
	@LanhanParam(field="roleId",name="主键",maxLength=10, checkMethod={ "update:1", },type=ParamType.Long)
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/** 角色名称 */
	public String getRoleName() {
		return roleName;
	}
	/** 角色名称 */
	@LanhanParam(field="roleName",name="角色名称",maxLength=100, checkMethod={"query:0", "update:1", "add:1", })
	public void setRoleName(String roleName) {
		this.roleName = roleName== null||roleName.equals("") ? null : roleName.trim();
	}
	/** 父角色主键 */
	public Long getParentId() {
		return parentId;
	}
	/** 父角色主键 */
	@LanhanParam(field="parentId",name="父角色主键",maxLength=10, checkMethod={  },type=ParamType.Long)
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/** 项目主键 */
	public Long getProjId() {
		return projId;
	}
	/** 项目主键 */
	@LanhanParam(field="projId",name="项目主键",maxLength=10, checkMethod={},type=ParamType.Long)
	public void setProjId(Long projId) {
		this.projId = projId;
	}
	/** 角色类型，见数据字典 */
	public String getRoleType() {
		return roleType;
	}
	/** 角色类型，见数据字典 */
	@LanhanParam(field="roleType",name="角色类型，见数据字典",maxLength=100, checkMethod={"query:0", "update:1", "add:1", })
	public void setRoleType(String roleType) {
		this.roleType = roleType== null||roleType.equals("") ? null : roleType.trim();
	}
	
	
	
	public String toString() {
		 return "{ roleNo:"+getRoleNo()+ ",  roleId:"+getRoleId()+ ",  roleName:"+getRoleName()+ ",  parentId:"+getParentId()+ ",  projId:"+getProjId()+ ",  roleType:"+getRoleType()+ ",  createdate:"+getCreatedate()+ ",  createtime:"+getCreatetime()+ ",  updatedate:"+getUpdatedate()+ ",  updatetime:"+getUpdatetime()+ ",  opId:"+getOpId()+ ",  remark:"+getRemark()+ ", }";
	}

}

