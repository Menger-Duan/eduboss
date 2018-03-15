/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.export;

import com.eduboss.utils.Assert;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xiangshaoxu 2016年6月3日下午11:44:14
 * @version 1.0.0
 */
public class StyleGenerator {
	
	private XSSFWorkbook workbook = new XSSFWorkbook(); 
	
	private XSSFCellStyle defaultHeaderStyle = null;
	private XSSFCellStyle defaultStringCellStyle = null;
	private XSSFCellStyle defaultUrlCellStyle = null;
	private XSSFCellStyle defaultDateCellStyle = null;
	private Map<String, XSSFCellStyle> dateCellStyleMap = new HashMap<>();
	
	public StyleGenerator(XSSFWorkbook workbook) {
		this.workbook = workbook;
	}
	
	
	public XSSFCellStyle defaultHeaderStyle() {
		if (defaultHeaderStyle == null) {
			
			defaultHeaderStyle = workbook.createCellStyle();
			defaultHeaderStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			defaultHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
			defaultHeaderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			defaultHeaderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			defaultHeaderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			defaultHeaderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			
			defaultHeaderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			XSSFFont font = workbook.createFont();
			font.setColor(HSSFColor.VIOLET.index);
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			defaultHeaderStyle.setFont(font);
		}
		
		return defaultHeaderStyle;
	}
	
	public XSSFCellStyle defaultStringCellStyle() {
		if (defaultStringCellStyle == null) {
			
			defaultStringCellStyle = workbook.createCellStyle();
			defaultStringCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			defaultStringCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
			defaultStringCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			defaultStringCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			defaultStringCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			defaultStringCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			defaultStringCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			defaultStringCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			
			XSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			defaultStringCellStyle.setFont(font);
		}
		return defaultStringCellStyle;
	}
	
	public XSSFCellStyle defaultUrlCellStyle() {
		if (defaultUrlCellStyle == null) {
			
			defaultUrlCellStyle = workbook.createCellStyle();
			
			defaultUrlCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			defaultUrlCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			defaultUrlCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			defaultUrlCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			defaultUrlCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			defaultUrlCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			defaultUrlCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			defaultUrlCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			
			XSSFFont font = workbook.createFont();
			font.setBoldweight(XSSFFont.U_SINGLE);
			font.setColor(HSSFColor.BLACK.index);
			defaultUrlCellStyle.setFont(font);
		}
	      
		return defaultUrlCellStyle;
	}
	
	public XSSFCellStyle defaultDateCellStyle() {
		if (defaultDateCellStyle == null) {
			
			defaultDateCellStyle = workbook.createCellStyle();
			defaultDateCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			defaultDateCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			defaultDateCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			defaultDateCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			defaultDateCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			defaultDateCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			defaultDateCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			defaultDateCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			
			XSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			defaultDateCellStyle.setFont(font);
			defaultDateCellStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-MM-dd hh:mm"));
		}
		
		return defaultDateCellStyle;
	}
	
	public XSSFCellStyle dateCellStyle(String dateformat) {
		Assert.isNotBlank(dateformat);
		
		XSSFCellStyle cellStyle = dateCellStyleMap.get(dateformat);
		if (cellStyle == null) {
			cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			
			XSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			cellStyle.setFont(font);
			cellStyle.setDataFormat(workbook.createDataFormat().getFormat(dateformat));
			dateCellStyleMap.put(dateformat, cellStyle);
		}
		
		return cellStyle;
	}
}
