package com.cjhxfund.foundation.web.model.sys;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.cjhxfund.foundation.annotation.LanhanParam;
import com.cjhxfund.foundation.annotation.LanhanParam.ParamType;
import com.cjhxfund.foundation.web.model.BaseModel;

/**
 * 系统用户信息 
 * @author xiejiesheng
 * @date 2016-08-17 
 */
@Component
public class UserInfo extends BaseModel implements Serializable {

	private static final long serialVersionUID = 201608171952086561L;
	private String userPwd;// 密码
	private Long empId;// 员工主键
	private Long projId;// 系统主键
	private Long roleId;// 角色主键
	private String authMode;// 授权模式
	private String userType;// 用户类型，见数据字典
	private Integer userState;// 用户状态,1:规则用户，2：零时用户
	private String email;// 邮箱
	private String phoneNo;// 移动电话
	private String teleNo;// 固定电话
	private Integer privFlag;// 是否分配权限, 默认0, 1：有单独分配权限
	private Integer userLevel;// 用户等级, 0:零时用户，1：正式用户
	private String userName;// 用户名
	private String nickName;// 昵称
	private String o32Name;// 对应o32的用户
	private String userTypeName;// 用户类型名称

	private String isRemb;// 是否记住用户名，用于登陆
	private String checkCode;// 验证码，用于登陆
	private String roleName;// 验证码，用于登陆
	private String activeTimeStr;// 激活时间字符串
	private String userNo;// 用户编码
	private String roleNo;// 角色编号

	private Integer startDate;// 激活时间
	private Integer endDate;//失效时间
	private SysRole role;//

	// 用于显示
	private String projname;// 项目英文名
	private String projnameChs;// 项目中文名

	private Long loginTime;// 登录时间，system.currentTIme

	
	
	
	public String getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getO32Name() {
		return o32Name;
	}

	public void setO32Name(String o32Name) {
		this.o32Name = o32Name;
	}

	public String getActiveTimeStr() {
		return activeTimeStr;
	}

	public void setActiveTimeStr(String activeTimeStr) {
		this.activeTimeStr = activeTimeStr;
	}

	/** 用户等级, 0:零时用户，1：正式用户 */
	public Integer getUserLevel() {
		return userLevel;
	}

	/** 用户等级, 0:零时用户，1：正式用户 */
	@LanhanParam(field = "userLevel", name = "用户等级", maxLength = 2, checkMethod = { "update:0", "add:0" }, type = ParamType.Integer)
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}


	public Integer getStartDate() {
		return startDate;
	}

	public void setStartDate(Integer startDate) {
		this.startDate = startDate;
	}

	public Integer getEndDate() {
		return endDate;
	}

	public void setEndDate(Integer endDate) {
		this.endDate = endDate;
	}

	/** 昵称 */
	public String getNickName() {
		return nickName;
	}

	/** 昵称 */
	@LanhanParam(field = "nickName", name = "昵称", maxLength = 64, checkMethod = { "update:1", "add:1" })
	public void setNickName(String nickName) {
		this.nickName = nickName == null || nickName.equals("") ? null : nickName.trim();
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public Long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Long loginTime) {
		this.loginTime = loginTime;
	}

	public String getProjname() {
		return projname;
	}

	public String getProjnameChs() {
		return projnameChs;
	}

	public void setProjname(String projnameChs, String projname) {
		this.projnameChs = projnameChs;
		this.projname = projname;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getIsRemb() {
		return isRemb;
	}

	public void setIsRemb(String isRemb) {
		this.isRemb = isRemb;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public SysRole getRole() {
		return role;
	}

	public void setRole(SysRole role) {
		this.role = role;
	}

	/** 邮箱 */
	public String getEmail() {
		return email;
	}

	/** 邮箱 */
	@LanhanParam(field = "email", name = "邮箱", maxLength = 100, checkMethod = { "update:1", "add:1" })
	public void setEmail(String email) {
		this.email = email == null || email.equals("") ? null : email.trim();
	}

	/** 移动电话 */
	public String getPhoneNo() {
		return phoneNo;
	}

	/** 移动电话 */
	@LanhanParam(field = "phoneNo", name = "移动电话", maxLength = 50, checkMethod = { "update:0", "add:0", })
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo == null || phoneNo.equals("") ? null : phoneNo.trim();
	}

	/** 固定电话 */
	public String getTeleNo() {
		return teleNo;
	}

	/** 固定电话 */
	@LanhanParam(field = "teleNo", name = "固定电话", maxLength = 50, checkMethod = { "update:0", "add:0", })
	public void setTeleNo(String teleNo) {
		this.teleNo = teleNo == null || teleNo.equals("") ? null : teleNo.trim();
	}

	/** 用户名 */
	public String getUserName() {
		return userName;
	}

	/** 用户名 */
	@LanhanParam(field = "userName", name = "用户名", maxLength = 50, checkMethod = { "query:0", "update:1", "add:1", })
	public void setUserName(String userName) {
		this.userName = userName == null || userName.equals("") ? null : userName.trim();
	}

	/** 密码 */
	public String getUserPwd() {
		return userPwd;
	}

	/** 密码 */
	@LanhanParam(field = "userPwd", name = "密码", maxLength = 50, checkMethod = {})
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd == null || userPwd.equals("") ? null : userPwd.trim();
	}

	/** 员工主键 */
	public Long getEmpId() {
		return empId;
	}

	/** 员工主键 */
	@LanhanParam(field = "empId", name = "员工主键", maxLength = 10, checkMethod = { "update:0", "add:0", }, type = ParamType.Long)
	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	/** 角色主键 */
	public Long getRoleId() {
		return roleId;
	}

	/** 角色主键 */
	@LanhanParam(field = "roleId", name = "角色主键", maxLength = 10, checkMethod = { "query:0", "update:1", "add:1", }, type = ParamType.Long)
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/** 项目系统主键 */
	public Long getProjId() {
		return projId;
	}

	/** 项目系统主键 */
	@LanhanParam(field = "projId", name = "项目系统主键", maxLength = 10, checkMethod = { "query:0", "update:1", "add:1", }, type = ParamType.Long)
	public void setProjId(Long projId) {
		this.projId = projId;
	}

	/** 授权模式 */
	public String getAuthMode() {
		return authMode;
	}

	/** 授权模式 */
	@LanhanParam(field = "authMode", name = "授权模式", maxLength = 50, checkMethod = { "update:1", "add:1", })
	public void setAuthMode(String authMode) {
		this.authMode = authMode == null || authMode.equals("") ? null : authMode.trim();
	}

	/** 用户状态 */
	public Integer getUserState() {
		return userState;
	}

	/** 用户状态,1:规则用户，2：零时用户 */
	@LanhanParam(field = "userState", name = "用户状态", maxLength = 2, checkMethod = { "update:0", "add:0", }, type = ParamType.Integer)
	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	/** 用户类型 */
	public String getUserType() {
		return userType;
	}

	/** 用户类型 */
	@LanhanParam(field = "userType", name = "用户类型", maxLength = 50, checkMethod = { "update:1", "add:1", })
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/** 是否分配权限, 默认0, 1：有单独分配权限 */
	public Integer getPrivFlag() {
		return privFlag;
	}

	/** 是否分配权限, 默认0, 1：有单独分配权限 */
	@LanhanParam(field = "privFlag", name = "是否分配权限", maxLength = 1, checkMethod = {}, type = ParamType.Integer)
	public void setPrivFlag(Integer privFlag) {
		this.privFlag = privFlag;
	}

	public String toString() {
		return "{ userId:" + getUserId() + ",  userName:" + getUserName() + ",  nickName:" + getNickName() + ",  userPwd:" + getUserPwd() + ",  empId:" + getEmpId() + ",  projId:" + getProjId() + ",  roleId:" + getRoleId() + ",  authMode:" + getAuthMode() + ",  userType:" + getUserType() + ",  userState:" + getUserState() + ",  email:" + getEmail() + ",  phoneNo:" + getPhoneNo() + ",  teleNo:" + getTeleNo() + ",  privFlag:" + getPrivFlag() + ",  createdate:" + getCreatedate() + ",  createtime:" + getCreatetime() + ",  updatedate:" + getUpdatedate() + ",  updatetime:" + getUpdatetime() + ",  opId:" + getOpId() + ",  remark:" + getRemark() + ",  delFlag:" + getDelFlag() + "}";
	}

}
