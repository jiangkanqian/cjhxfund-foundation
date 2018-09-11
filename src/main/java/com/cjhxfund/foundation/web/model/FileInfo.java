package com.cjhxfund.foundation.web.model;

import org.apache.commons.io.FilenameUtils;

import com.cjhxfund.foundation.util.data.NumberUtil;
import com.cjhxfund.foundation.util.str.DateUtil;

/**
 * 上传文件信息
 * @author lanhan
 * @date 2015年11月26日
 */
public class FileInfo  extends BaseModel{
	
	private String fileName;//上传的文件名
	private String ext;//拓展名
	private Long size;//实际大小
	private String sizeStr;//实际大小 显示大小
	private String genName;//保存的文件名
	private String updateDate;//最后修改时间
	private String path;//实际路径,包含保存的文件名和拓展名
	
	public FileInfo(){}
	
	public FileInfo(String fileName,  Long size, Long updatedate, String path) {
		this.size = size;
		this.sizeStr = NumberUtil.formatDoubleStr(size/1024.0, "0.00")+" kb";
		this.fileName = fileName;
		this.ext = FilenameUtils.getExtension(fileName);
		this.path = path;
		this.updateDate = DateUtil.getDateStr(updatedate);
	}
	
	
	public String getSizeStr() {
		return sizeStr;
	}

	public void setSizeStr(String sizeStr) {
		this.sizeStr = sizeStr;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext.toLowerCase();
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getGenName() {
		return genName;
	}
	public void setGenName(String genName) {
		this.genName = genName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	@Override
	public String toString() {
		return "FileInfo [ext=" + ext + ", size=" + size + ", fileName=" + fileName + ", genName="
				+ genName + ", path=" + path + "]";
	}
	
	
	
	
	
	
	

	
	

}
