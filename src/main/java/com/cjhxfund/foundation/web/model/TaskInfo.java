package com.cjhxfund.foundation.web.model;

public class TaskInfo {

	private String taskNo;
	private String desc;
	private String execTime;
	private int hour;
	private int minute;
	private int second = 0;
	private boolean startAuto;
	private boolean status = true;
	protected String doTaskIP;//执行任务的IP，如果是多节点部署；
	
	
	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	public boolean isStartAuto() {
		return startAuto;
	}
	public void setStartAuto(boolean startAuto) {
		this.startAuto = startAuto;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDoTaskIP() {
		return doTaskIP;
	}
	public void setDoTaskIP(String doTaskIP) {
		this.doTaskIP = doTaskIP;
	}
	
	
	
	
}
