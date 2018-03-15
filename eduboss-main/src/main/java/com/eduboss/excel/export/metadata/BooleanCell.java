/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.export.metadata;

import com.eduboss.excel.export.CellVistor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author xiangshaoxu 2016年6月3日上午10:57:38
 * @version 1.0.0
 */
public class BooleanCell extends ExcelCell {
	private Boolean content;
	
	public BooleanCell(int index, ExcelRow parent) {
		super(index, parent);
	}
	
	@Override
	public void accept(CellVistor vistor) {
		vistor.visit(this);
	}
	public Boolean getContent() {
		return content;
	}
	public void setContent(Boolean content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}