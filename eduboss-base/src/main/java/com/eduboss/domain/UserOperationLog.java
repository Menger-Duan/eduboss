package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author qin.jingkai
 * @version v1.0
 * 2014-09-28
 *
 */
@Entity
@Table(name = "USER_OPERATION_LOG")
public class UserOperationLog {
	
	// Fields
	private String id;
	private String userId;
	private String userName;
	private String operationTime;
	private String accessMethodName;
	private String inputParameter; //PARAMETER_INPUT
	private String ouptParameter; 
	private String  browserInfo;//BROWSER_INFO
	private String  operatSystemInfo;
	private String  ipAddress;
	
	private String responseTime;
	private String processDuration;
	private Integer responseDataSize;
	
    public UserOperationLog() {
		// TODO Auto-generated constructor stub
	}
    
    
    public UserOperationLog(String userId,String userName,String operationTime,String accessMethodName,String inputParameter,String ouptParameter,String browserInfo,String operatSystemInfo,String ipAddress) {
  		// TODO Auto-generated constructor stub
    	//this.id=id;
    	this.userId=userId;
    	this.userName=userName;
    	this.operationTime=operationTime;
    	this.accessMethodName=accessMethodName;
    	this.inputParameter=inputParameter;
    	this.ouptParameter=ouptParameter;
    	this.browserInfo=browserInfo;
    	this.operatSystemInfo=operatSystemInfo;
    	this.ipAddress=ipAddress;
  	}
	
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "USER_ID", length = 32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "USER_NAME", length = 32)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name = "OPERATION_TIME", length = 30)
	public String getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}
	
	@Column(name = "ACCESS_METHOD_NAME", length = 256)
	public String getAccessMethodName() {
		return accessMethodName;
	}
	public void setAccessMethodName(String accessMethodName) {
		this.accessMethodName = accessMethodName;
	}
	
	@Column(name = "PARAMETER_INPUT", length = 60000)
	public String getInputParameter() {
		return inputParameter;
	}
	public void setInputParameter(String inputParameterTime) {
		this.inputParameter = inputParameterTime;
	}
	
	@Column(name = "PARAMETER_OUTPUT", length = 20)
	public String getOuptParameter() {
		return ouptParameter;
	}
	public void setOuptParameter(String ouptParameter) {
		this.ouptParameter = ouptParameter;
	}
	
	@Column(name = "BROWSER_INFO", length = 256)
	public String getBrowserInfo() {
		return browserInfo;
	}
	public void setBrowserInfo(String browserInfo) {
		this.browserInfo = browserInfo;
	}
	
	@Column(name = "PC_OPERATION_INFO", length = 256)
	public String getOperatSystemInfo() {
		return operatSystemInfo;
	}
	public void setOperatSystemInfo(String operatSystemInfo) {
		this.operatSystemInfo = operatSystemInfo;
	}
	
	@Column(name = "IP_ADDRESS", length = 20)
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


	/**
	 * @return the responseTime
	 */
	@Column(name = "RESPONSE_TIME", length = 20)
	public String getResponseTime() {
		return responseTime;
	}


	/**
	 * @param responseTime the responseTime to set
	 */
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}


	/**
	 * @return the processDuration
	 */
	@Column(name = "PROCESS_DURATION", length = 20)
	public String getProcessDuration() {
		return processDuration;
	}


	/**
	 * @param processDuration the processDuration to set
	 */
	public void setProcessDuration(String processDuration) {
		this.processDuration = processDuration;
	}


	/**
	 * @return the responseDataSize
	 */
	@Column(name = "RESPONSE_DATA_SIZE", length = 20)
	public Integer getResponseDataSize() {
		return responseDataSize;
	}


	/**
	 * @param responseDataSize the responseDataSize to set
	 */
	public void setResponseDataSize(int responseDataSize) {
		this.responseDataSize = responseDataSize;
	}
	
	

}
