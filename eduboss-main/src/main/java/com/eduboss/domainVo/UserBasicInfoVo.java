package com.eduboss.domainVo;

import java.io.Serializable;

public class UserBasicInfoVo implements Serializable{

	private String userId;
	private String name;
	private String job;
	private String department;
	private String campus;
	private String brench;
	
	public UserBasicInfoVo()
	{
	}
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
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getBrench() {
		return brench;
	}
	public void setBrench(String brench) {
		this.brench = brench;
	}
	
}
