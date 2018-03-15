package com.eduboss.dto;

import java.util.Collection;

public class GridResponse {
	private int currentPage;
	private int totalPage;
	private int totalRecord;
	private Collection gridData;
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public Collection getGridData() {
		return gridData;
	}
	public void setGridData(Collection gridData) {
		this.gridData = gridData;
	}
	
}
