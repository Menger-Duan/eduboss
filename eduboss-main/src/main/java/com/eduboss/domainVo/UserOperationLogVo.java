package com.eduboss.domainVo;


/**
 * @author lixuejun
 * @version v1.0
 * 2015-09-11
 *
 */
public class UserOperationLogVo {
	
	// Fields
	private String id;
	private String userId;
	private String userName;
	private String operationTime;
	private String accessMethodName;
	private String chinessMethodName;
	private String parameterInput; //PARAMETER_INPUT
	private String ouptParameter; 
	private String  browserInfo;//BROWSER_INFO
	private String  operatSystemInfo;
	private String  ipAddress;
	
	private String responseTime;
	private String processDuration;
	private Integer responseDataSize;
	
    public UserOperationLogVo() {
		// TODO Auto-generated constructor stub
	}
    
    
    public UserOperationLogVo(String userId,String userName,String operationTime,String accessMethodName,String chinessMethodName,String parameterInput,String ouptParameter,String browserInfo,String operatSystemInfo,String ipAddress) {
  		// TODO Auto-generated constructor stub
    	//this.id=id;
    	this.userId=userId;
    	this.userName=userName;
    	this.operationTime=operationTime;
    	this.accessMethodName=accessMethodName;
    	this.chinessMethodName=chinessMethodName;
    	this.parameterInput=parameterInput;
    	this.ouptParameter=ouptParameter;
    	this.browserInfo=browserInfo;
    	this.operatSystemInfo=operatSystemInfo;
    	this.ipAddress=ipAddress;
  	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}

	public String getAccessMethodName() {
		return accessMethodName;
	}
	public void setAccessMethodName(String accessMethodName) {
		this.accessMethodName = accessMethodName;
	}

	public String getChinessMethodName() {
		return chinessMethodName;
	}
	public void setChinessMethodName(String chinessMethodName) {
		this.chinessMethodName = chinessMethodName;
	}

	public String getParameterInput() {
		return parameterInput;
	}

	public void setParameterInput(String parameterInput) {
		this.parameterInput = parameterInput;
	}

	public String getOuptParameter() {
		return ouptParameter;
	}
	public void setOuptParameter(String ouptParameter) {
		this.ouptParameter = ouptParameter;
	}

	public String getBrowserInfo() {
		return browserInfo;
	}
	public void setBrowserInfo(String browserInfo) {
		this.browserInfo = browserInfo;
	}

	public String getOperatSystemInfo() {
		return operatSystemInfo;
	}
	public void setOperatSystemInfo(String operatSystemInfo) {
		this.operatSystemInfo = operatSystemInfo;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getProcessDuration() {
		return processDuration;
	}
	public void setProcessDuration(String processDuration) {
		this.processDuration = processDuration;
	}

	public Integer getResponseDataSize() {
		return responseDataSize;
	}
	public void setResponseDataSize(Integer responseDataSize) {
		this.responseDataSize = responseDataSize;
	}

}
