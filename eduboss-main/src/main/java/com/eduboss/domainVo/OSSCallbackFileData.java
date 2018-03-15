package com.eduboss.domainVo;

import java.io.Serializable;
import java.util.List;

public class OSSCallbackFileData implements Serializable  {

	private static final long serialVersionUID = 1L;

	private String uploadId;
	private boolean checkedPC;
	private boolean checkedH5;
	private int count;
	private List<ItemFile> fileInfo;
	private String oldCount;
	private boolean needClose;
	
	public String getUploadId() {
		return uploadId;
	}
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<ItemFile> getFileInfo() {
		return fileInfo;
	}
	public void setFileInfo(List<ItemFile> fileInfo) {
		this.fileInfo = fileInfo;
	}
	public boolean isCheckedPC() {
		return checkedPC;
	}
	public void setCheckedPC(boolean checkedPC) {
		this.checkedPC = checkedPC;
	}
	public boolean isCheckedH5() {
		return checkedH5;
	}
	public void setCheckedH5(boolean checkedH5) {
		this.checkedH5 = checkedH5;
	}
	public String getOldCount() {
		return oldCount;
	}
	public void setOldCount(String oldCount) {
		this.oldCount = oldCount;
	}
	public boolean isNeedClose() {
		return needClose;
	}
	public void setNeedClose(boolean needClose) {
		this.needClose = needClose;
	}
}
