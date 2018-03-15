/**
 * 
 */
package com.eduboss.exception;

/**
 * @classname	ConnetionErrorException.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2011-5-3  10:12:37
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */
public class ApplicationException extends GenericeException {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public ApplicationException() {
		super(ErrorCode.SYSTEM_ERROR, ErrorCode.SYSTEM_ERROR.getErrorString());
	}
	
	public ApplicationException(String errorMsg) {
		super(ErrorCode.SYSTEM_ERROR, errorMsg);
	}
	
	public ApplicationException(ErrorCode errorCode) {
		super(errorCode, errorCode.getErrorString());
	}
	
	public ApplicationException(ErrorCode errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}
	
}
