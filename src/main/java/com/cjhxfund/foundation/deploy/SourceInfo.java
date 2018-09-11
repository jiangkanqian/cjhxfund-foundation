package com.cjhxfund.foundation.deploy;

/**
 * @author Jason
 * @date 2016年4月27日
 */
public class SourceInfo {

	private String name;
	
	private long length;
	
	private String lastUpdateTime;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	@Override
	public String toString() {
		return "FileInfo [name=" + name + ", length=" + length + ", lastUpdateTime="
				+ lastUpdateTime + "]";
	}
	
	
}
