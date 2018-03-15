/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported;

import com.eduboss.excel.anno.ExcelIgnore;

import java.io.Serializable;

/**
 * ExcelImportedEo excelObject
 * 
 * 数值型可能被设置为null,建议不要使用基本类型
 * 
 * @author xiangshaoxu 2016年6月2日下午3:50:12
 * @version 1.0.0
 */
public abstract class GenericExcelImportedEo implements Serializable {
	
	@ExcelIgnore
	private static final long serialVersionUID = 2027522529903435204L;

	/**此vo属于excel哪行*/
	@ExcelIgnore
	Integer excelRowNum;
	
	/**此vo数据验证是否通过*/
	@ExcelIgnore
	Boolean validated = true;
	
	/**如果数据导入验证不通过，此字段记录错误信息*/
	@ExcelIgnore
	StringBuffer validateMsg = new  StringBuffer();
	

	public Integer getExcelRowNum() {
		return excelRowNum;
	}

	public void setExcelRowNum(Integer excelRowNum) {
		this.excelRowNum = excelRowNum;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	public String getValidateMsg() {
		return validateMsg.toString();
	}
	
	public void appendValidateMsg(String validateMsg) {
		if (this.validateMsg.length() == 0) {
			this.validateMsg.append(validateMsg);
		} else {
			this.validateMsg.append(", ").append(validateMsg);
		}
	}
}
