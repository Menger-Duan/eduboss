/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.export.metadata;

import com.eduboss.excel.export.CellVistor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author xiangshaoxu 2016年6月3日上午10:53:39
 * @version 1.0.0
 */
public class StringCell extends ExcelCell {

	private String content;

	public StringCell(int index, ExcelRow parent) {
		super(index, parent);
	}

	@Override
	public void accept(CellVistor vistor) {
		vistor.visit(this);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}