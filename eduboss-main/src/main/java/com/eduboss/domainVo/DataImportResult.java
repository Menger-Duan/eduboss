package com.eduboss.domainVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataImportResult {

	private String result;
	private Integer surplus;
	private List<Integer> li;
	private String resultCode;
	private Integer failNum;
	private int yetImport = 0;
	private int maxNumber = 0;
	
	public DataImportResult(){
		surplus=0;
		li=new ArrayList<Integer>();
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Integer getSurplus() {
		return surplus;
	}
	public void setSurplus(Integer surplus) {
		this.surplus = surplus;
	}
	public List<Integer> getLi() {
		return li;
	}
	public void setLi(List<Integer> li) {
		this.li = li;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public Integer getFailNum() {
		return failNum;
	}
	public void setFailNum(Integer failNum) {
		this.failNum = failNum;
	}
	public int getYetImport() {
		return yetImport;
	}
	public void setYetImport(int yetImport) {
		this.yetImport = yetImport;
	}
	public int getMaxNumber() {
		return maxNumber;
	}
	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

	
}
