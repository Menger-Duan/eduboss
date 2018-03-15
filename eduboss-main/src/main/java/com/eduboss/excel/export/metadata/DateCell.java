/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.export.metadata;

import com.eduboss.excel.export.CellVistor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 
 * @author xiangshaoxu 2016年6月3日上午10:58:00
 * @version 1.0.0
 */
public class DateCell extends ExcelCell {

	private Date content;
	private String dateformat;

	public DateCell(int index, ExcelRow parent) {
		super(index, parent);
	}

	@Override
	public void accept(CellVistor vistor) {
		vistor.visit(this);
	}

	public Date getContent() {
		return content;
	}

	public void setContent(Date content) {
		this.content = content;
	}

	public String getDateformat() {
		return dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}