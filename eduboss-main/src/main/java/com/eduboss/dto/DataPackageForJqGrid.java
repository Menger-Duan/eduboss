package com.eduboss.dto;

import java.io.Serializable;
import java.util.Collection;

public class DataPackageForJqGrid extends Response implements Serializable {
	
	private static final long serialVersionUID = -3215516372610210134L;
	
	// 总页数
	private int total;
	// 当前页
	private int page;
	// 记录总数
	private int records;
	// 实际返回的数据
	private Collection rows;
	
	public DataPackageForJqGrid() {}
	
	public DataPackageForJqGrid(DataPackage dp) {
		super.setResultCode(dp.getResultCode());
		super.setResultMessage(dp.getResultMessage());
		if (dp.getPageSize() == 0) {
			dp.setPageSize(20);
		}
		this.page = dp.getPageNo() + 1;
		this.total = dp.getRowCount() / dp.getPageSize();
		if (dp.getRowCount() % dp.getPageSize() > 0) {
			this.total ++;
		}
		this.records = dp.getRowCount();
		setRows(dp.getDatas());
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the records
	 */
	public int getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(int records) {
		this.records = records;
	}

	/**
	 * @return the rows
	 */
	public Collection getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(Collection rows) {
		this.rows = rows;
	}

	
}
