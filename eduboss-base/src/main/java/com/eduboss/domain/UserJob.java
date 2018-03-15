package com.eduboss.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.dto.UserMobileVo;
import com.eduboss.domainVo.DistributableUserJobVo;
import com.eduboss.dto.SelectOptionResponse.NameValue;

/**
 * Role entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "USER_JOB")
public class UserJob implements java.io.Serializable, NameValue {

	private String id;
	private String jobName;
	private int flag;
	private String remark;
	private Integer userCount;
	private Integer realCount;
	private String jobSign;

	
	
	private String isCustomerFollow;
	private Integer resourceNum;
	private Integer returnCycle;
	private String cycleType;
	private String returnNode;

	private Short jobLevel;
	
	
	
	private List<UserMobileVo> userList=new ArrayList<UserMobileVo>();
	
	
	//新增集合 管理 该职位可以分配的职位 xiaojinwang 20170302
	private Set<DistributableUserJobVo> distributableUserJobs =  new HashSet<DistributableUserJobVo>();


	/** default constructor */
	public UserJob() {
	}

	/** minimal constructor */
	public UserJob(String id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "JOB_NAME", length = 100)
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Column(name = "FLAG")
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Column(name = "REMARK", length = 200)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Transient
	public String getName() {
		return this.jobName;
	}

	@Transient
	public String getValue() {
		return this.id;
	}

	@Transient
	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
	
	@Transient
	public Integer getRealCount() {
		return realCount;
	}

	public void setRealCount(Integer realCount) {
		this.realCount = realCount;
	}
	
	@Column(name = "JOB_SIGN", length = 32)
	public String getJobSign() {
		return jobSign;
	}

	public void setJobSign(String jobSign) {
		this.jobSign = jobSign;
	}

	@Transient
	public List<UserMobileVo> getUserList() {
		return userList;
	}

	public void setUserList(List<UserMobileVo> userList) {
		this.userList = userList;
	}

	@Column(name = "IS_CUSTOMER_FOLLOW", length = 1)
	public String getIsCustomerFollow() {
		return isCustomerFollow;
	}

	public void setIsCustomerFollow(String isCustomerFollow) {
		this.isCustomerFollow = isCustomerFollow;
	}

	@Column(name = "RESOURCE_NUM")
	public Integer getResourceNum() {
		return resourceNum;
	}

	public void setResourceNum(Integer resourceNum) {
		this.resourceNum = resourceNum;
	}

	@Column(name = "RETURN_CYCLE")
	public Integer getReturnCycle() {
		return returnCycle;
	}

	public void setReturnCycle(Integer returnCycle) {
		this.returnCycle = returnCycle;
	}

	@Column(name = "CYCLE_TYPE", length = 32)
	public String getCycleType() {
		return cycleType;
	}

	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}

	@Column(name = "RETURN_NODE", length = 32)
	public String getReturnNode() {
		return returnNode;
	}

	public void setReturnNode(String returnNode) {
		this.returnNode = returnNode;
	}

	@Column(name = "job_level")
	public Short getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(Short jobLevel) {
		this.jobLevel = jobLevel;
	}

	@Transient
	public Set<DistributableUserJobVo> getDistributableUserJobs() {
		return distributableUserJobs;
	}

	public void setDistributableUserJobs(Set<DistributableUserJobVo> distributableUserJobs) {
		this.distributableUserJobs = distributableUserJobs;
	}

	
	
	
}