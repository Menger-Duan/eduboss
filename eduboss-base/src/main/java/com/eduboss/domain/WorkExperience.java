package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author lixuejun
 * 员工工作经历表
 */
@Entity
@Table(name = "user_info_work_experience")
public class WorkExperience {

	private String id;
	private String userInfoId; //员工信息ID
	private String campanyName; //公司名称
	private String startDate; //工作开始日期
	private String endDate; //工作结束日期
	private String position; //岗位
	private BigDecimal salary; //薪资
	private String leaveCause; //离职原因
	private String references; //证明人
	private String referencesPhone; //证明人联系电话
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime; 
	
	/** default constructor */
	public WorkExperience() {
	}
	
	public WorkExperience(String id) {
		this.id = id;
	}
	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "USER_INFO_ID", length = 32)
	public String getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}
	
	@Column(name = "CAMPANY_NAME", length = 50)
	public String getCampanyName() {
		return campanyName;
	}
	public void setCampanyName(String campanyName) {
		this.campanyName = campanyName;
	}
	
	@Column(name = "START_DATE", length = 20)
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "END_DATE", length = 20)
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	@Column(name = "POSITION", length = 50)
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Column(name = "SALARY", precision = 10)
	public BigDecimal getSalary() {
		return salary;
	}
	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
	
	@Column(name = "LEAVE_CAUSE", length = 100)
	public String getLeaveCause() {
		return leaveCause;
	}
	public void setLeaveCause(String leaveCause) {
		this.leaveCause = leaveCause;
	}
	
	@Column(name = "REFERENCE", length = 20)
	public String getReferences() {
		return references;
	}
	public void setReferences(String references) {
		this.references = references;
	}
	
	@Column(name = "REFERENCE_PHONE", length = 20)
	public String getReferencesPhone() {
		return referencesPhone;
	}
	public void setReferencesPhone(String referencesPhone) {
		this.referencesPhone = referencesPhone;
	}
	
	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
