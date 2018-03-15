package com.eduboss.dto;

import java.io.Serializable;
import java.util.Collection;

public class DataPackage extends Response implements Serializable {
	
	private static final long serialVersionUID = -3215516372610210134L;

	private int rowCount;

	private int pageSize = 20;

	private int pageNo = 0;

    private String roleQLConfigName;
	
	private String sord;  
	private String sidx;

	private Collection datas;
	
	public DataPackage() {}
	
	public DataPackage(GridRequest gridRequest) {
		this.setPageNo(gridRequest.getPage());
		int pageNum = gridRequest.getPage() - 1;
		this.setPageSize(gridRequest.getRows());
		if (pageSize == 0)
			pageSize = 20;
		if (pageNum < 0)
			pageNum = 0;
		this.setPageNo(pageNum);
		this.setSidx(gridRequest.getSidx());
		this.setSord(gridRequest.getSord());
	}
	
	public DataPackage(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	
	public DataPackage(String pageNo, String pageSize) {
		this.pageNo = Integer.valueOf(pageNo);
		this.pageSize = Integer.valueOf(pageSize);
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Collection getDatas() {
		return datas;
	}

	public void setDatas(Collection datas) {
		this.datas = datas;
	}

    public String getRoleQLConfigName() {
        return roleQLConfigName;
    }

    public void setRoleQLConfigName(String roleQLConfigName) {
        this.roleQLConfigName = roleQLConfigName;
    }

    /**
	 * @return the sord
	 */
	public String getSord() {
		return sord;
	}

	/**
	 * @param sord the sord to set
	 */
	public void setSord(String sord) {
		this.sord = sord;
	}

	/**
	 * @return the sidx
	 */
	public String getSidx() {
		return sidx;
	}

	/**
	 * @param sidx the sidx to set
	 */
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
}
