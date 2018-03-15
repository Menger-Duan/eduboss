package com.eduboss.domainVo;

import java.math.BigDecimal;

public class OfficeSpaceManageVo {

	private String id; // ID
	private String officeSpace; // 场地名称
	private int status; // 状态：0 无效 1 有效
	private String statusName; // 状态名称
	
	/* 组织机构*/
	private String organizationId; 
	private String organizationName;
	
	private String openingDate; // 建立日期
	private String address; // 地址
	private String phone; // 联系电话
	private String principal; // 负责人
	private String principalPhone; // 负责人联系电话
	private BigDecimal spaceArea; // 办公场地面积（平方米）
	private Integer boothNumber; // 卡座数
	private Integer classroomNumber; // 教室数
	private Integer deskNumber; // 教室课桌数
	private String spaceRemark; // 场地备注
	private String lessor; // 出租方
	private String lessorPhone; // 出租方联系电话
	private BigDecimal monthlyRent; // 租金（月）
	private BigDecimal yearlyRent; // 租金（年）
	private BigDecimal oneOnOnePreProduction; // 一对一预计产值
	private BigDecimal miniClassPreProduction; // 小班预计产值
	private BigDecimal preSquarePerformance; // 预估平效
	private BigDecimal realSquarePerformance; // 实际平效
	private BigDecimal fullRate; // 满座率
	private BigDecimal administrativeFee; // 管理费
	private BigDecimal airConditionFee; // 空调费
	private String startDate; // 合同开始时间
	private String endDate; // 合同结束时间
	private String leaseRemark; // 租赁备注
	
	/* 创建人*/
	private String creatorId;
	private String creatorName;
	
	/* 创建时间*/
	private String createTime;
	
	/* 修改人*/
	private String modifierId;
	private String modifierName;
	
	/* 修改时间*/
	private String modifyTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOfficeSpace() {
		return officeSpace;
	}

	public void setOfficeSpace(String officeSpace) {
		this.officeSpace = officeSpace;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

	public String getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getPrincipalPhone() {
		return principalPhone;
	}

	public void setPrincipalPhone(String principalPhone) {
		this.principalPhone = principalPhone;
	}

	public BigDecimal getSpaceArea() {
		return spaceArea;
	}

	public void setSpaceArea(BigDecimal spaceArea) {
		this.spaceArea = spaceArea;
	}

	public Integer getBoothNumber() {
		return boothNumber;
	}

	public void setBoothNumber(Integer boothNumber) {
		this.boothNumber = boothNumber;
	}

	public Integer getClassroomNumber() {
		return classroomNumber;
	}

	public void setClassroomNumber(Integer classroomNumber) {
		this.classroomNumber = classroomNumber;
	}

	public Integer getDeskNumber() {
		return deskNumber;
	}

	public void setDeskNumber(Integer deskNumber) {
		this.deskNumber = deskNumber;
	}

	public String getSpaceRemark() {
		return spaceRemark;
	}

	public void setSpaceRemark(String spaceRemark) {
		this.spaceRemark = spaceRemark;
	}

	public String getLessor() {
		return lessor;
	}

	public void setLessor(String lessor) {
		this.lessor = lessor;
	}

	public String getLessorPhone() {
		return lessorPhone;
	}

	public void setLessorPhone(String lessorPhone) {
		this.lessorPhone = lessorPhone;
	}

	public BigDecimal getMonthlyRent() {
		return monthlyRent;
	}

	public void setMonthlyRent(BigDecimal monthlyRent) {
		this.monthlyRent = monthlyRent;
	}

	public BigDecimal getYearlyRent() {
		return yearlyRent;
	}

	public void setYearlyRent(BigDecimal yearlyRent) {
		this.yearlyRent = yearlyRent;
	}

	public BigDecimal getOneOnOnePreProduction() {
		return oneOnOnePreProduction;
	}

	public void setOneOnOnePreProduction(BigDecimal oneOnOnePreProduction) {
		this.oneOnOnePreProduction = oneOnOnePreProduction;
	}

	public BigDecimal getMiniClassPreProduction() {
		return miniClassPreProduction;
	}

	public void setMiniClassPreProduction(BigDecimal miniClassPreProduction) {
		this.miniClassPreProduction = miniClassPreProduction;
	}

	public BigDecimal getPreSquarePerformance() {
		return preSquarePerformance;
	}

	public void setPreSquarePerformance(BigDecimal preSquarePerformance) {
		this.preSquarePerformance = preSquarePerformance;
	}

	public BigDecimal getRealSquarePerformance() {
		return realSquarePerformance;
	}

	public void setRealSquarePerformance(BigDecimal realSquarePerformance) {
		this.realSquarePerformance = realSquarePerformance;
	}

	public BigDecimal getFullRate() {
		return fullRate;
	}

	public void setFullRate(BigDecimal fullRate) {
		this.fullRate = fullRate;
	}

	public BigDecimal getAdministrativeFee() {
		return administrativeFee;
	}

	public void setAdministrativeFee(BigDecimal administrativeFee) {
		this.administrativeFee = administrativeFee;
	}

	public BigDecimal getAirConditionFee() {
		return airConditionFee;
	}

	public void setAirConditionFee(BigDecimal airConditionFee) {
		this.airConditionFee = airConditionFee;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getLeaseRemark() {
		return leaseRemark;
	}

	public void setLeaseRemark(String leaseRemark) {
		this.leaseRemark = leaseRemark;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifierId() {
		return modifierId;
	}

	public void setModifierId(String modifierId) {
		this.modifierId = modifierId;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

}
