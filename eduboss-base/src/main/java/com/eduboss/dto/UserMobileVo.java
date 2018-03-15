package com.eduboss.dto;

import java.util.List;
import java.util.Map;


public class UserMobileVo  implements java.io.Serializable{
	
	private String userId;
	private String name;
	private String account;
	private String contact;
	private String deptName;
	private String jobName;
	private String emailAccount;
	private String ccpAccount;
	private List<Map> jobDept;
	private String deptId;
	private String sex;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getEmailAccount() {
		return emailAccount;
	}
	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}
	public String getCcpAccount() {
		return ccpAccount;
	}
	public void setCcpAccount(String ccpAccount) {
		this.ccpAccount = ccpAccount;
	}
	public List<Map> getJobDept() {
		return jobDept;
	}
	public void setJobDept(List<Map> jobDept) {
		this.jobDept = jobDept;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	
}
