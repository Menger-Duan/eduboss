package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 办公场地
 * @author LiXueJun
 *
 */
@Entity
@Table(name = "OFFICE_SPACE_MANAGE")
public class OfficeSpaceManage {

	private String id; // ID
	private String officeSpace; // 场地名称
	private int status; // 状态：0 无效 1 有效
	private Organization organization; // 所属组织架构：只可选集团，分公司，校区三级架构信息
	private String openingDate; // 开业日期
	private String address; // 地址
	private String phone; // 联系电话
	private String principal; // 负责人
	private String principalPhone; // 负责人联系电话
	private BigDecimal spaceArea; // 办公场地面积（平方米）
	private Integer boothNumber; // 卡座数
	private Integer classroomNumber; // 教室数
	private Integer deskNumber; // 教室课桌数
	private BigDecimal oneOnOnePreProduction; // 一对一预计产值
	private BigDecimal miniClassPreProduction; // 小班预计产值
	private BigDecimal preSquarePerformance; // 预估平效
	private BigDecimal realSquarePerformance; // 实际平效
	private BigDecimal fullRate; // 满座率
	private String spaceRemark; // 场地备注
	private String lessor; // 出租方
	private String lessorPhone; // 出租方联系电话
	private BigDecimal monthlyRent; // 租金（月）
	private BigDecimal yearlyRent; // 租金（年）
	private BigDecimal administrativeFee; // 管理费
	private BigDecimal airConditionFee; // 空调费
	
	private String startDate; // 合同开始时间
	private String endDate; // 合同结束时间
	private String leaseRemark; // 租赁备注
	private User creator; // 创建人
	private String createTime; // 创建时间
	private User modifier; // 修改人
	private String modifyTime; // 修改时间
	
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
	
	@Column(name = "OFFICE_SPACE")
	public String getOfficeSpace() {
		return officeSpace;
	}
	public void setOfficeSpace(String officeSpace) {
		this.officeSpace = officeSpace;
	}
	
	@Column(name = "STATUS")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@JoinColumn(name = "ORGANIZATION_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	@Column(name = "OPENING_DATE", length = 10)
	public String getOpeningDate() {
		return openingDate;
	}
	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}
	
	@Column(name = "ADDRESS", length = 100)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "PHONE", length = 16)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(name = "PRINCIPAL", length = 20)
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	@Column(name = "PRINCIPAL_PHONE", length = 16)
	public String getPrincipalPhone() {
		return principalPhone;
	}
	public void setPrincipalPhone(String principalPhone) {
		this.principalPhone = principalPhone;
	}
	
	@Column(name = "SPACE_AREA", precision = 10)
	public BigDecimal getSpaceArea() {
		return spaceArea;
	}
	public void setSpaceArea(BigDecimal spaceArea) {
		this.spaceArea = spaceArea;
	}
	
	@Column(name = "BOOTH_NUMBER")
	public Integer getBoothNumber() {
		return boothNumber;
	}
	public void setBoothNumber(Integer boothNumber) {
		this.boothNumber = boothNumber;
	}
	
	@Column(name = "CLASSROOM_NUMBER")
	public Integer getClassroomNumber() {
		return classroomNumber;
	}
	public void setClassroomNumber(Integer classroomNumber) {
		this.classroomNumber = classroomNumber;
	}
	
	@Column(name = "DESK_NUMBER")
	public Integer getDeskNumber() {
		return deskNumber;
	}
	public void setDeskNumber(Integer deskNumber) {
		this.deskNumber = deskNumber;
	}
	

	@Column(name = "ONE_ON_ONE_PRE_PRODUCTION", precision = 10)
	public BigDecimal getOneOnOnePreProduction() {
		return oneOnOnePreProduction;
	}
	public void setOneOnOnePreProduction(BigDecimal oneOnOnePreProduction) {
		this.oneOnOnePreProduction = oneOnOnePreProduction;
	}
	
	@Column(name = "MINI_CLASS_PRE_PRODUCTION", precision = 10)
	public BigDecimal getMiniClassPreProduction() {
		return miniClassPreProduction;
	}
	public void setMiniClassPreProduction(BigDecimal miniClassPreProduction) {
		this.miniClassPreProduction = miniClassPreProduction;
	}
	
	@Column(name = "PRE_SQUARE_PERFORMANCE", precision = 10)
	public BigDecimal getPreSquarePerformance() {
		return preSquarePerformance;
	}
	public void setPreSquarePerformance(BigDecimal preSquarePerformance) {
		this.preSquarePerformance = preSquarePerformance;
	}
	
	@Column(name = "REAL_SQUARE_PERFORMANCE", precision = 10)
	public BigDecimal getRealSquarePerformance() {
		return realSquarePerformance;
	}
	public void setRealSquarePerformance(BigDecimal realSquarePerformance) {
		this.realSquarePerformance = realSquarePerformance;
	}
	
	@Column(name = "FULL_RATE", precision = 10)
	public BigDecimal getFullRate() {
		return fullRate;
	}
	public void setFullRate(BigDecimal fullRate) {
		this.fullRate = fullRate;
	}
	
	@Column(name = "SPACE_REMARK", length=500)
	public String getSpaceRemark() {
		return spaceRemark;
	}
	public void setSpaceRemark(String spaceRemark) {
		this.spaceRemark = spaceRemark;
	}
	
	@Column(name = "LESSOR", length = 50)
	public String getLessor() {
		return lessor;
	}
	public void setLessor(String lessor) {
		this.lessor = lessor;
	}
	
	@Column(name = "LESSOR_PHONE", length = 16)
	public String getLessorPhone() {
		return lessorPhone;
	}
	public void setLessorPhone(String lessorPhone) {
		this.lessorPhone = lessorPhone;
	}
	
	@Column(name = "MONTHLY_RENT", precision = 10)
	public BigDecimal getMonthlyRent() {
		return monthlyRent;
	}
	public void setMonthlyRent(BigDecimal monthlyRent) {
		this.monthlyRent = monthlyRent;
	}
	
	@Column(name = "YEARLY_RENT", precision = 10)
	public BigDecimal getYearlyRent() {
		return yearlyRent;
	}
	public void setYearlyRent(BigDecimal yearlyRent) {
		this.yearlyRent = yearlyRent;
	}
	
	@Column(name = "ADMINISTRATIVE_FEE", precision = 10)
	public BigDecimal getAdministrativeFee() {
		return administrativeFee;
	}
	public void setAdministrativeFee(BigDecimal administrativeFee) {
		this.administrativeFee = administrativeFee;
	}
	
	@Column(name = "AIR_CONDITION_FEE", precision = 10)
	public BigDecimal getAirConditionFee() {
		return airConditionFee;
	}
	public void setAirConditionFee(BigDecimal airConditionFee) {
		this.airConditionFee = airConditionFee;
	}
	
	@Column(name = "START_DATE", length = 10)
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "END_DATE", length = 10)
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	@Column(name = "LEASE_REMARK", length=500)
	public String getLeaseRemark() {
		return leaseRemark;
	}
	public void setLeaseRemark(String leaseRemark) {
		this.leaseRemark = leaseRemark;
	}
	
	@JoinColumn(name = "CREATE_USER_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@JoinColumn(name = "MODIFY_USER_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	public User getModifier() {
		return modifier;
	}
	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
