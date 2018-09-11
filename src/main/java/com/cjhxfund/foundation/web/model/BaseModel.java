package com.cjhxfund.foundation.web.model;

import com.cjhxfund.foundation.util.str.DateUtil;

/**
 * 模型数据的公有字段
 * v1.2 : 修改baseModel中的时间显示，所有时间显示，已数据的形式存储
 * @author xiejs
 * @date 2015年6月14日
 */
public class BaseModel {

	private Long userId; // 用户编号，无需填写，注册时自动绑定；如果是系统管理员代为注册，则必须填写
	private Integer createdate;//创建日期
	private Integer createtime;//创建时间
	private Integer updatedate;//修改日期
	private Integer updatetime;//修改时间
	private Integer delFlag; // 删除标识
	private Integer version; // 版本号
	private Integer oldVersion; // 原版本号，防止并发修改数据
	private Long opId; // 操作人
	private Long optime; // 操作时间
	private String remark; // 备注
	private String extra; //
	private String extra1; //
	private String extra2; //
	
	public Long getOptime() {
		return optime;
	}

	public void setOptime(Long optime) {
		this.optime = optime;
	}

	private String sessionId; //
	
	private Integer queryTotal; // 分页查询的查询总数
	
	private String originalId;  //原始id
	
	/**web token cookie 缓存*/
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Integer createdate) {
		this.createdate = createdate;
	}

	public Integer getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Integer createtime) {
		this.createtime = createtime;
	}

	public Integer getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Integer updatedate) {
		this.updatedate = updatedate;
	}

	public Integer getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Integer updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getQueryTotal() {
		return queryTotal;
	}

	public void setQueryTotal(Integer queryTotal) {
		this.queryTotal = queryTotal;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	/** 获取用户编号，无需填写，注册时自动绑定；如果是系统管理员代为注册，则必须填写 */
	public Long getUserId() {
		return this.userId;
	}

	/** 赋值用户编号，无需填写，注册时自动绑定；如果是系统管理员代为注册，则必须填写 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/** 拓展字段 */
	public String getExtra() {
		return extra;
	}

	/** 拓展字段 */
	public void setExtra(String extra) {
		this.extra = extra == null || extra.equals("") ? null : extra.trim();
	}

	

	/**  原版本号，防止并发修改数据 */
	public Integer getOldVersion() {
		return this.oldVersion;
	}

	/**  原版本号，防止并发修改数据 */
	public void setOldVersion(Integer oldVersion) {
		this.oldVersion = oldVersion;
	}


	/** 删除标识 */
	public Integer getDelFlag() {
		return this.delFlag;
	}

	/** 删除标识 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	/** 版本号 */
	public Integer getVersion() {
		return this.version;
	}

	/** 版本号 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/** 操作人 */
	public Long getOpId() {
		return this.opId;
	}

	/** 操作人 */
	public void setOpId(Long opId) {
		this.opId = opId;
	}

	/** 备注 */
	public String getRemark() {
		return this.remark;
	}

	/** 备注 */
	public void setRemark(String remark) {
		this.remark = remark == null || remark.equals("") ? null : remark.trim();
	}

	/** 拓展字段1 */
	public String getExtra1() {
		return this.extra1;
	}

	/** 拓展字段1 */
	public void setExtra1(String extra1) {
		this.extra1 = extra1 == null || extra1.equals("") ? null : extra1.trim();
	}

	/** 拓展字段2 */
	public String getExtra2() {
		return this.extra2;
	}

	/** 拓展字段2 */
	public void setExtra2(String extra2) {
		this.extra2 = extra2 == null || extra2.equals("") ? null : extra2.trim();
	}

	/**
	 * 根据操作类型，自动添加公共字段的值
	 * @author xiejs
	 * @date 2015年7月31日
	 * @param op 操作类型，0：添加，1：修改
	 * @param userId 用户编号
	 */
	public void setOp(int op, Long userId) {
		
		if(userId != null){
			this.opId = userId;
			if(this.userId == null){
				this.userId = userId;
			}
		}
		Integer thisTime = DateUtil.getTimeNum();
		Integer thisDate = DateUtil.getDateNum();
		if (0 == op) {
			this.createtime = thisTime;
			this.createdate = thisDate;
			this.updatetime = thisTime;
			this.updatedate = thisDate;
			this.delFlag = 0;
			this.version = 1;
		} else if (1 == op) {
			this.updatetime = thisTime;
			this.updatedate = thisDate;
			if (this.version != null) {
				this.oldVersion = this.version;
				this.version++;
			}
		}

	}

}
