package com.eduboss.domainVo;

import com.eduboss.common.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class BasicOperationQueryVo {

	private String startDate;
	private String endDate;
	private String countDate;
	private String blCampusId;
	private String organizationId;
	private String brenchId;
	private String teacherId;
	private String studyManegerId;
	private String miniClassName;
	private RoleCode role;
	private OrganizationType organizationType;
	private BasicOperationQueryLevelType basicOperationQueryLevelType;
	private ContractType contractType;
	private MiniClassStatus miniClassStatus;
	private Dimensionality dimensionality;
	
    private String staticCampusId;
    private String yearId;
    private String monthId;
    
    private String conTypeOrProType;//类型切换  
    
    private String otmClassName;
    private OtmClassStatus otmClassStatus;
    
    private ProductType productType;// 产品类型

	private String miniClassTypeId;  //班级类型
	
	private ContractPaidStatus paidStatus;//合同支付状态
	
	private FundsChangeAuditStatus auditStatus;//审核状态
	
	private EvidenceAuditStatus evidenceAuditStatus;// 凭证审核状态
	
	private String subject; //课程科目
	
	private String isFilterZero; // 是否过滤所有为0的数据 TRUE : 过滤， FALSE: 不用过滤
	
	private String studentId;
	
	private String teacherType;

	private BonusType bonusType;

    public String getStaticCampusId() {
        return staticCampusId;
    }

    public void setStaticCampusId(String staticCampusId) {
        this.staticCampusId = staticCampusId;
    }

	@NotBlank(message="开始时间不能为空")
    public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	@NotBlank(message="结束时间不能为空")
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCountDate() {
		return countDate;
	}
	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public OrganizationType getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}
	public BasicOperationQueryLevelType getBasicOperationQueryLevelType() {
		return basicOperationQueryLevelType;
	}
	public void setBasicOperationQueryLevelType(
			BasicOperationQueryLevelType basicOperationQueryLevelType) {
		this.basicOperationQueryLevelType = basicOperationQueryLevelType;
	}
	public RoleCode getRole() {
		return role;
	}
	public void setRole(RoleCode role) {
		this.role = role;
	}
	public ContractType getContractType() {
		return contractType;
	}
	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	public String getYearId() {
		return yearId;
	}

	public void setYearId(String yearId) {
		this.yearId = yearId;
	}

	public String getMonthId() {
		return monthId;
	}

	public void setMonthId(String monthId) {
		this.monthId = monthId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getStudyManegerId() {
		return studyManegerId;
	}

	public void setStudyManegerId(String studyManegerId) {
		this.studyManegerId = studyManegerId;
	}

	public String getMiniClassName() {
		return miniClassName;
	}

	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}

	public MiniClassStatus getMiniClassStatus() {
		return miniClassStatus;
	}

	public void setMiniClassStatus(MiniClassStatus miniClassStatus) {
		this.miniClassStatus = miniClassStatus;
	}

	public Dimensionality getDimensionality() {
		return dimensionality;
	}

	public void setDimensionality(Dimensionality dimensionality) {
		this.dimensionality = dimensionality;
	}

	public String getOtmClassName() {
		return otmClassName;
	}

	public void setOtmClassName(String otmClassName) {
		this.otmClassName = otmClassName;
	}

	public OtmClassStatus getOtmClassStatus() {
		return otmClassStatus;
	}

	public void setOtmClassStatus(OtmClassStatus otmClassStatus) {
		this.otmClassStatus = otmClassStatus;
	}

	public String getMiniClassTypeId() {
		return miniClassTypeId;
	}

	public void setMiniClassTypeId(String miniClassTypeId) {
		this.miniClassTypeId = miniClassTypeId;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public String getConTypeOrProType() {
		return conTypeOrProType;
	}

	public void setConTypeOrProType(String conTypeOrProType) {
		this.conTypeOrProType = conTypeOrProType;
	}

	public ContractPaidStatus getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(ContractPaidStatus paidStatus) {
		this.paidStatus = paidStatus;
	}

	public FundsChangeAuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(FundsChangeAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public EvidenceAuditStatus getEvidenceAuditStatus() {
		return evidenceAuditStatus;
	}

	public void setEvidenceAuditStatus(EvidenceAuditStatus evidenceAuditStatus) {
		this.evidenceAuditStatus = evidenceAuditStatus;
	}

	public String getIsFilterZero() {
		return isFilterZero;
	}

	public void setIsFilterZero(String isFilterZero) {
		this.isFilterZero = isFilterZero;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getBrenchId() {
		return brenchId;
	}

	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}

	public String getTeacherType() {
		return teacherType;
	}

	public void setTeacherType(String teacherType) {
		this.teacherType = teacherType;
	}

	public BonusType getBonusType() {
		return bonusType;
	}

	public void setBonusType(BonusType bonusType) {
		this.bonusType = bonusType;
	}
}
