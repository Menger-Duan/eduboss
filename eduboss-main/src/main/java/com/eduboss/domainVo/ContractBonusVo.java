package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.domain.Contract;
import com.eduboss.domain.User;

public class ContractBonusVo {
	// Fields
	private String id;
	private String fundsChangeHistoryId;
	private String bonusStaffName;
	private String bonusStaffId;
	private BigDecimal bonusAmount;//提成人金额
	private BigDecimal campusAmount;//校区业绩
	private String organizationId;
	private String organizationName;
	private String createTime;
	private String bonusType;
	private String bonusTypeId;
	private String studentReturnFeeId;
	
	private String bonusStaffCampusId;
	private String bonusStaffCampusName;
	
	private String contractCampusId;
	private String contractCampusName;
	
	private String typeName;
	private String typeId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFundsChangeHistoryId() {
		return fundsChangeHistoryId;
	}
	public void setFundsChangeHistoryId(String fundsChangeHistoryId) {
		this.fundsChangeHistoryId = fundsChangeHistoryId;
	}
	public String getBonusStaffName() {
		return bonusStaffName;
	}
	public void setBonusStaffName(String bonusStaffName) {
		this.bonusStaffName = bonusStaffName;
	}
	public String getBonusStaffId() {
		return bonusStaffId;
	}
	public void setBonusStaffId(String bonusStaffId) {
		this.bonusStaffId = bonusStaffId;
	}
	public BigDecimal getBonusAmount() {
		return bonusAmount;
	}
	public void setBonusAmount(BigDecimal bonusAmount) {
		this.bonusAmount = bonusAmount;
	}
	
	public BigDecimal getCampusAmount() {
		return campusAmount;
	}
	public void setCampusAmount(BigDecimal campusAmount) {
		this.campusAmount = campusAmount;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getBonusType() {
		return bonusType;
	}
	public void setBonusType(String bonusType) {
		this.bonusType = bonusType;
	}
	public String getBonusTypeId() {
		return bonusTypeId;
	}
	public void setBonusTypeId(String bonusTypeId) {
		this.bonusTypeId = bonusTypeId;
	}
	public String getStudentReturnFeeId() {
		return studentReturnFeeId;
	}
	public void setStudentReturnFeeId(String studentReturnFeeId) {
		this.studentReturnFeeId = studentReturnFeeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getBonusStaffCampusId() {
		return bonusStaffCampusId;
	}
	public void setBonusStaffCampusId(String bonusStaffCampusId) {
		this.bonusStaffCampusId = bonusStaffCampusId;
	}
	public String getBonusStaffCampusName() {
		return bonusStaffCampusName;
	}
	public void setBonusStaffCampusName(String bonusStaffCampusName) {
		this.bonusStaffCampusName = bonusStaffCampusName;
	}
	public String getContractCampusId() {
		return contractCampusId;
	}
	public void setContractCampusId(String contractCampusId) {
		this.contractCampusId = contractCampusId;
	}
	public String getContractCampusName() {
		return contractCampusName;
	}
	public void setContractCampusName(String contractCampusName) {
		this.contractCampusName = contractCampusName;
	}
	
	
	
}
