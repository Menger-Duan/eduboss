/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.export.metadata;

import com.eduboss.excel.export.CellVistor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author xiangshaoxu 2016年6月3日上午10:58:23
 * @version 1.0.0
 */
public class UrlCell extends ExcelCell {

	private String linkLabel;
	private String linkUrl;

	public UrlCell(int index, ExcelRow parent) {
		super(index, parent);
	}

	@Override
	public void accept(CellVistor vistor) {
		vistor.visit(this);
	}

	public String getLinkLabel() {
		return linkLabel;
	}

	public void setLinkLabel(String linkLabel) {
		this.linkLabel = linkLabel;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}