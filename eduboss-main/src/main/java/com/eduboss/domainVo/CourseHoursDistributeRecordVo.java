package com.eduboss.domainVo;

import java.math.BigDecimal;

public class CourseHoursDistributeRecordVo implements java.io.Serializable {

	private static final long serialVersionUID = -2511055987587968657L;
	
	private int id;
	private String contractProductId;
	private String subjectId;
	private String subjectName;
	private String typeName;
	private String typeValue;
	private BigDecimal transactionHours;
	private BigDecimal distributedHours;
	private BigDecimal consumeHours;
	private BigDecimal remainHours;
	private String createUserId;
	private String createUserName;
	private String createTime;
	private String blCampusId;
	private String blCampusName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContractProductId() {
		return contractProductId;
	}
	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeValue() {
		return typeValue;
	}
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
	public BigDecimal getTransactionHours() {
		return transactionHours;
	}
	public void setTransactionHours(BigDecimal transactionHours) {
		this.transactionHours = transactionHours;
	}
	public BigDecimal getDistributedHours() {
		return distributedHours;
	}
	public void setDistributedHours(BigDecimal distributedHours) {
		this.distributedHours = distributedHours;
	}
	public BigDecimal getConsumeHours() {
		return consumeHours;
	}
	public void setConsumeHours(BigDecimal consumeHours) {
		this.consumeHours = consumeHours;
	}
	public BigDecimal getRemainHours() {
		return remainHours;
	}
	public void setRemainHours(BigDecimal remainHours) {
		this.remainHours = remainHours;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	public String getBlCampusName() {
		return blCampusName;
	}

	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
}
