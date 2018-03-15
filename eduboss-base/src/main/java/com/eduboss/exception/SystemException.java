package com.eduboss.exception;


public class SystemException extends GenericeException{
	
	public SystemException() {
		super(ErrorCode.SYSTEM_ERROR, "");
	}
	
	public SystemException(String errorMsg) {
		super(ErrorCode.SYSTEM_ERROR, errorMsg);
	}
	
	public SystemException(ErrorCode errorCode) {
		super(errorCode, "");
	}
	
	public SystemException(ErrorCode errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}
	
}
