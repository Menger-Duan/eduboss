package com.eduboss.domainVo;

public class CustomerDataImport {

	private String result;
	private String fileUrl;
	private Boolean isOk;
	private Integer improtNum;
	private Integer failNum;
	private String resultMessage;
	
	public Boolean getIsOk() {
		return isOk;
	}
	public void setIsOk(Boolean isOk) {
		this.isOk = isOk;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public Integer getImprotNum() {
		return improtNum;
	}
	public void setImprotNum(Integer improtNum) {
		this.improtNum = improtNum;
	}
		
	public Integer getFailNum() {
		return failNum;
	}
	public void setFailNum(Integer failNum) {
		this.failNum = failNum;
	}
	
	public CustomerDataImport(String result, String fileUrl, Boolean isOk, Integer improtNum, Integer failNum,
			String resultMessage) {
		super();
		this.result = result;
		this.fileUrl = fileUrl;
		this.isOk = isOk;
		this.improtNum = improtNum;
		this.failNum = failNum;
		this.resultMessage = resultMessage;
	}
	public CustomerDataImport(String result, String fileUrl,Boolean isOk,Integer improtNum,Integer failNum) {
		this.result = result;
		this.fileUrl = fileUrl;
		this.isOk=isOk;
		this.improtNum=improtNum;
		this.failNum=failNum;
	}
	public CustomerDataImport() {
		
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
}
