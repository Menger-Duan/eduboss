/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel;

import com.eduboss.exception.EdubossException;

/**
 * 导入时，检查到excel单元格数据，专门指单元格内数据不合格，会提示在导出的excel中
 * @author xiangshaoxu 2016年6月6日下午9:35:17
 * @version 1.0.0
 */
public class InvalidExcelCellDataException extends EdubossException {

	private static final long serialVersionUID = 6991415621131694018L;

	private String validateMsg;


    /**
     *
     */
    public InvalidExcelCellDataException(String validateMsg) {
    	setValidateMsg(validateMsg);
    }

    /**
     * @param message
     */
    public InvalidExcelCellDataException(String message, String validateMsg) {
        super(message);
        setValidateMsg(validateMsg);
    }

    /**
     * @param cause
     */
    public InvalidExcelCellDataException(Throwable cause, String validateMsg) {
        super(cause);
        setValidateMsg(validateMsg);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidExcelCellDataException(String message, Throwable cause, String validateMsg) {
        super(message, cause);
        setValidateMsg(validateMsg);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InvalidExcelCellDataException(String message, Throwable cause, boolean enableSuppression,
                                         boolean writableStackTrace, String validateMsg) {
        super(message, cause, enableSuppression, writableStackTrace);
        setValidateMsg(validateMsg);
    }

	
	public String getValidateMsg() {
		return validateMsg;
	}

	private void setValidateMsg(String cellErrorMsg) {
		this.validateMsg = cellErrorMsg;
	}
	
	
}
