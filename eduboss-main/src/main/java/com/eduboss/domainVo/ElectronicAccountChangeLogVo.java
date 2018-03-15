package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.domain.User;

public class ElectronicAccountChangeLogVo {

	private String id;
	private String studentId;
	private String typeValue;
	private String typeName;
	private String changeType;
	private BigDecimal changeAmount;
	private BigDecimal afterAmount;
	private String changeTime;
	private String changeUserId;
	private String changeUserName;
	private String remark;

	private String studentName;

	private String blcampusId;

	private String branchId;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public BigDecimal getChangeAmount() {
		return changeAmount;
	}
	public void setChangeAmount(BigDecimal changeAmount) {
		this.changeAmount = changeAmount;
	}
	public BigDecimal getAfterAmount() {
		return afterAmount;
	}
	public void setAfterAmount(BigDecimal afterAmount) {
		this.afterAmount = afterAmount;
	}
	public String getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}

	public String getChangeUserId() {
		return changeUserId;
	}
	public void setChangeUserId(String changeUserId) {
		this.changeUserId = changeUserId;
	}
	public String getChangeUserName() {
		return changeUserName;
	}
	public void setChangeUserName(String changeUserName) {
		this.changeUserName = changeUserName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTypeValue() {
		return typeValue;
	}
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBlcampusId() {
		return blcampusId;
	}

	public void setBlcampusId(String blcampusId) {
		this.blcampusId = blcampusId;
	}
}
