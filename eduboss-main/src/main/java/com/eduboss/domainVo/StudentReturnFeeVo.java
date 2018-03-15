package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.utils.PropertiesUtils;


public class StudentReturnFeeVo {
	
	private String id;
	private String studentName;//学生
	private String studentId;//学生
	private String gradeId;
	private String gradeName;
	private String contractId;//合同
	private String contractProductId;//合同产品
	private String contractProductName;//合同产品
	private BigDecimal returnAmount;//退费金额
	private BigDecimal returnNormalAmount; //实收退费
	private BigDecimal returnPromotionAmount; //优惠退费
	private BigDecimal returnSpecialAmount; //超额退费
	private String returnType;//退费类型
	private String returnTypeId;//退费类型
	private String returnReason;//退费原因
	private String accountName; ////账户名
	private String account; // 账号
	private String createUser;//
	private String createUserId;//
	private String createTime;//
	private String modifyUser;//
	private String modifyUserId;//
	private String modifyTime;//
	private String campus;//校区
	private String campusId;//校区
	private String fundsChangeHistoryId;
	private String fundsChangeHistoryImage;

	private String productType; //产品类型
	
	private String aliPath;
	
	
	public String getAliPath() {
		return PropertiesUtils.getStringValue("oss.access.url.prefix")+""+fundsChangeHistoryImage;
	}

	public void setAliPath(String aliPath) {
		this.aliPath = aliPath;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getContractProductId() {
		return contractProductId;
	}
	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}
	public BigDecimal getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(BigDecimal returnAmount) {
		this.returnAmount = returnAmount;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getReturnTypeId() {
		return returnTypeId;
	}
	public void setReturnTypeId(String returnTypeId) {
		this.returnTypeId = returnTypeId;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getContractProductName() {
		return contractProductName;
	}
	public void setContractProductName(String contractProductName) {
		this.contractProductName = contractProductName;
	}
	public String getFundsChangeHistoryId() {
		return fundsChangeHistoryId;
	}
	public void setFundsChangeHistoryId(String fundsChangeHistoryId) {
		this.fundsChangeHistoryId = fundsChangeHistoryId;
	}
	public String getFundsChangeHistoryImage() {
		return fundsChangeHistoryImage;
	}
	public void setFundsChangeHistoryImage(String fundsChangeHistoryImage) {
		this.fundsChangeHistoryImage = fundsChangeHistoryImage;
	}

	public BigDecimal getReturnNormalAmount() {
		return returnNormalAmount;
	}

	public void setReturnNormalAmount(BigDecimal returnNormalAmount) {
		this.returnNormalAmount = returnNormalAmount;
	}

	public BigDecimal getReturnPromotionAmount() {
		return returnPromotionAmount;
	}

	public void setReturnPromotionAmount(BigDecimal returnPromotionAmount) {
		this.returnPromotionAmount = returnPromotionAmount;
	}

	public BigDecimal getReturnSpecialAmount() {
		return returnSpecialAmount;
	}

	public void setReturnSpecialAmount(BigDecimal returnSpecialAmount) {
		this.returnSpecialAmount = returnSpecialAmount;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
}
