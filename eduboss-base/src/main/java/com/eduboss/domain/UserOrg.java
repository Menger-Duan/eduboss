package com.eduboss.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "userOrg")
public class UserOrg implements Serializable {

	private String userId;
	private String name;
	private String jobName;
	private String dept;
	private String campus;
	private String brench;
	private String deptId;
	private Integer enableFlg;
	private String brenchId;
	private String campusId;
	
	@Id
	@Column(name = "USER_ID", unique = true, nullable = false, length = 32)
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "NAME", length = 32)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "JOB_NAME", length = 32)
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	@Column(name = "Dept", length = 32)
	public String getDept() {
		return dept;
	}
	
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	@Column(name = "CAMPUS", length = 32)
	public String getCampus() {
		return campus;
	}
	
	public void setCampus(String campus) {
		this.campus = campus;
	}
	
	@Column(name = "BRENCH", length = 32)
	public String getBrench() {
		return brench;
	}
	
	public void setBrench(String brench) {
		this.brench = brench;
	}

	@Column(name = "organizationID", length = 32)
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Column(name = "ENABLE_FLAG")
	public Integer getEnableFlg() {
		return enableFlg;
	}

	public void setEnableFlg(Integer enableFlg) {
		this.enableFlg = enableFlg;
	}

	@Column(name = "brenchId")
	public String getBrenchId() {
		return brenchId;
	}

	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}

	@Column(name = "campusId")
	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	
	
}
