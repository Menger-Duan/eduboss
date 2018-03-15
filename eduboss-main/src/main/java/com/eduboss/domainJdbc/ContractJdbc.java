package com.eduboss.domainJdbc;

import java.math.BigDecimal;

import com.eduboss.common.ContractPaidStatus;
import com.eduboss.common.ContractStatus;
import com.eduboss.common.ContractType;

/**
 * 
 * @author lixuejun
 * 2016-08-13
 *
 */
public class ContractJdbc {
	
	// Fields
	private String id;
	private String studentId;
	private String studentName;
	private ContractType contractType;
	private String referenceContractId;
	private String customerId;
	private String signStaffId;
	private String signStaffName;
	
	private ContractStatus contractStatus;
	private ContractPaidStatus paidStatus;

	private String gradeId;//合同年级   
	private String gradeName;
	private String createTime;
	private String createByStaff;
	private String modifyTime;
	private String modifyByStaff;
	private String remark;
	private BigDecimal paidAmount;
	
	private String blCampusId;  //签单人归属校区
	private String school;
	private boolean ecsContract; //是否目标班合同
	private int isNarrow;//是否缩单  ： 0：否    1：是
	
	// Constructors
	/** default constructor */
	public ContractJdbc() {
	}
	
	public ContractJdbc(String contractId) {
		this.id = contractId;
	}

	public ContractJdbc(String id, String studentId, String studentName,
			ContractType contractType, String referenceContractId,
			String customerId, String signStaffId, String signStaffName,
			ContractStatus contractStatus, ContractPaidStatus paidStatus,
			String gradeId, String gradeName, String createTime,
			String createByStaff, String modifyTime, String modifyByStaff,
			String remark, BigDecimal paidAmount, String blCampusId,
			String school, boolean ecsContract, int isNarrow) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.studentName = studentName;
		this.contractType = contractType;
		this.referenceContractId = referenceContractId;
		this.customerId = customerId;
		this.signStaffId = signStaffId;
		this.signStaffName = signStaffName;
		this.contractStatus = contractStatus;
		this.paidStatus = paidStatus;
		this.gradeId = gradeId;
		this.gradeName = gradeName;
		this.createTime = createTime;
		this.createByStaff = createByStaff;
		this.modifyTime = modifyTime;
		this.modifyByStaff = modifyByStaff;
		this.remark = remark;
		this.paidAmount = paidAmount;
		this.blCampusId = blCampusId;
		this.school = school;
		this.ecsContract = ecsContract;
		this.isNarrow = isNarrow;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public ContractType getContractType() {
		return this.contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}
	
	public String getReferenceContractId() {
		return referenceContractId;
	}

	public void setReferenceContractId(String referenceContractId) {
		this.referenceContractId = referenceContractId;
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getSignStaffId() {
		return signStaffId;
	}

	public void setSignStaffId(String signStaffId) {
		this.signStaffId = signStaffId;
	}	
	
	public ContractStatus getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(ContractStatus contractStatus) {
		this.contractStatus = contractStatus;
	}
	
	public ContractPaidStatus getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(ContractPaidStatus paidStatus) {
		this.paidStatus = paidStatus;
	}
	
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getCreateByStaff() {
		return createByStaff;
	}

	public void setCreateByStaff(String createByStaff) {
		this.createByStaff = createByStaff;
	}

	
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public String getModifyByStaff() {
		return modifyByStaff;
	}
	
	public void setModifyByStaff(String modifyByStaff) {
		this.modifyByStaff = modifyByStaff;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}	
	
	public BigDecimal getPaidAmount() {
		return this.paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	
	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	
	public String getSchool() {
		return this.school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public boolean isEcsContract() {
		return ecsContract;
	}

	public void setEcsContract(boolean ecsContract) {
		this.ecsContract = ecsContract;
	}

	public int getIsNarrow() {
		return isNarrow;
	}

	public void setIsNarrow(int isNarrow) {
		this.isNarrow = isNarrow;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getSignStaffName() {
		return signStaffName;
	}

	public void setSignStaffName(String signStaffName) {
		this.signStaffName = signStaffName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	
}