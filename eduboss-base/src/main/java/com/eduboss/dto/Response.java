package com.eduboss.dto;


/**
 * 
 * @author Chenguiban
 * @version 0.1, 2012-05-04
 */
public class Response {
	

	/**
	 */
	private int resultCode = 0;
	
	/**
	 */
	private String resultMessage = "";
	
	private Object data;
	
	public Response() {
		super();
	}
	
	public Response(int resultCode, String resultMessage) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
	


	public Response(int resultCode, String resultMessage, Object data) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.data = data;
	}

	/**
	 * @return the resultCode
	 */
	public int getResultCode() {
		return resultCode;
	}

	
	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	
	/**
	 * @return the resultMessage
	 */
	public String getResultMessage() {
		return resultMessage;
	}

	
	/**
	 * @param resultMessage the resultMessage to set
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


	
}
