package com.cjhxfund.foundation.annotation.data;

/**
 * 方法编写人员信息
 * @author xiejs
 * @date 2015年7月3日
 */
public class DocAuthor {
	private String creator;//创建者
	private String date;//创建日期
	private String lastEditor;//最后修改
	private String lastDate;//最后修改时间
	/**创建者*/
	public String getCreator() {
		return creator;
	}
	/**创建者*/
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**创建日期*/
	public String getDate() {
		return date;
	}
	/**创建日期*/
	public void setDate(String date) {
		this.date = date;
	}
	/**最后修改*/
	public String getLastEditor() {
		return lastEditor;
	}
	/**最后修改*/
	public void setLastEditor(String lastEditor) {
		this.lastEditor = lastEditor;
	}
	/**最后修改时间*/
	public String getLastDate() {
		return lastDate;
	}
	/**最后修改时间*/
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	@Override
	public String toString() {
		return "DocAuthor [creator=" + creator + ", date=" + date + ", lastEditor=" + lastEditor + ", lastDate=" + lastDate + "]";
	}

}
