package com.eduboss.dto;

public class GridRequest {
	private int page;
	private int rows;
	
	private String sord;  
	private String sidx;
	private String oper;
	private String _search;
//	private int numOfRecordsLimitation = 1000;
	private int numOfRecordsLimitation = 10000;
	
	public String get_search() {
		return _search;
	}
	public void set_search(String _search) {
		this._search = _search;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}
	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getNumOfRecordsLimitation() {
		return numOfRecordsLimitation;
	}
	public void setNumOfRecordsLimitation(int numOfRecordsLimitation) {
		this.numOfRecordsLimitation = numOfRecordsLimitation;
	}
}