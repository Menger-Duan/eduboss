package com.eduboss.domainVo;

import java.io.Serializable;

public class ItemFile implements Serializable {

	private static final long serialVersionUID = 1645421698999620850L;
	
	private String filePath;
	private String fileSize;
	private String realFileName;
	private String type;
	
	public ItemFile(){}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getRealFileName() {
		return realFileName;
	}
	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
