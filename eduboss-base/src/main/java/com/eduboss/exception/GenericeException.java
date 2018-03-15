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
public class GenericeException extends RuntimeException {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/**  @return the errorCode */
	private ErrorCode errorCode;
	
	private String errorMsg; 
	
    public GenericeException(ErrorCode errorCode, String errorMsg) {
        super(errorCode.getValue() + ": " + errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
	
	/**
	 * @return the errorCode
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	} 
	
	/**
	 * @see java.lang.Throwable#printStackTrace()
	 */
	public void printStackTrace() {
		System.out.println(this.getClass() + ":" + errorCode.getValue());
		super.printStackTrace();
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
