package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.AuditOperation;
import com.eduboss.common.BonusDistributeType;
import com.eduboss.common.RefundAuditStatus;
import com.eduboss.common.RefundFormType;

public class RefundWorkflowVo {

	private String id;
	private RefundFormType formType; // 表单类型
	private String refundCampusId;// 退费校区
	private String refundCampusName;
	private String applicantId; // 申请人
	private String applicantName;
	private Integer applicantEnableFlg; //申请人状态
	private String applicantCampusId;
	private String applicantCampusName;
	private String refundCauseId; // 退费原因
	private String refundCauseName;
	private String refundEvidence; // 退费凭证
	private String refundDetailReason; // 退费详细原因
	private String refundStudentId; // 退费学生
	private String refundStudentName;
	private String refundContractProductId; // 退费合同产品
	private String intoCampusId; // 转入校区
	private String intoCampusName;
	private String intoStudentId; // 转入学生
	private String intoStudentName; // 转入学生
	private String refundAccount; // 退费账号
	private String refundAccName; // 退费账号名
	private String bankInfo; // 银行支行信息
	private BigDecimal basicOperateAmount; // 基本账户操作金额
	private BigDecimal promotionOperateAmount; // 赠送账户操作金额
	private BigDecimal specialOperateAmount; // 特殊账户操作金额

	private String firstRefundDutyCampusId; // 退费责任校区1
	private String firstRefundDutyCampusName;
	private BigDecimal firstRefundDutyAmountCampus; // 校区退费责任金额1
	private BonusDistributeType firstSubBonusDistributeTypeCampus;//校区分配类型1

	private String secondRefundDutyCampusId; // 退费责任校区2
	private String secondRefundDutyCampusName;
	private BigDecimal secondRefundDutyAmountCampus; // 退费责任金额2
	private BonusDistributeType secondSubBonusDistributeTypeCampus;//校区分配类型2

	private String thirdRefundDutyCampusId; // 退费责任校区3
	private String thirdRefundDutyCampusName;
	private BigDecimal thirdRefundDutyAmountCampus; // 退费责任金额3
	private BonusDistributeType thirdSubBonusDistributeTypeCampus;//校区分配类型3

	private String fourthRefundDutyCampusId;
	private String fourthRefundDutyCampusName;
	private BigDecimal fourthRefundDutyAmountCampus;
	private BonusDistributeType fourthSubBonusDistributeTypeCampus;//校区分配类型4

	private String fifthRefundDutyCampusId;
	private String fifthRefundDutyCampusName;
	private BigDecimal fifthRefundDutyAmountCampus; // 退费责任校区金额5
	private BonusDistributeType fifthSubBonusDistributeTypeCampus;//校区分配类型5


	private String firstRefundDutyPersonId; // 退费责任人1
	private String firstRefundDutyPersonName;
	private BigDecimal firstRefundDutyAmountPerson; // 责任人退费责任金额1
	private BonusDistributeType firstSubBonusDistributeTypePerson; //责任人分配类型1

	private String secondRefundDutyPersonId; // 退费责任人2
	private String secondRefundDutyPersonName;
	private BigDecimal secondRefundDutyAmountPerson; // 退费责任人金额2
	private BonusDistributeType secondSubBonusDistributeTypePerson; //责任人分配类型2

	private String thirdRefundDutyPersonId; // 退费责任人3
	private String thirdRefundDutyPersonName;
	private BigDecimal thirdRefundDutyAmountPerson; // 退费责任人金额3
	private BonusDistributeType thirdSubBonusDistributeTypePerson; //责任人分配类型3

	private String fourthRefundDutyPersonId; // 退费责任人4
	private String fourthRefundDutyPersonName; // 退费责任人
	private BigDecimal fourthRefundDutyAmountPerson; // 退费责任人金额4
	private BonusDistributeType fourthSubBonusDistributeTypePerson; //责任人分配类型4

	private String fifthRefundDutyPersonId; // 退费责任人5
	private String fifthRefundDutyPersonName; // 退费责任人5
	private BigDecimal fifthRefundDutyAmountPerson; // 退费责任人金额5
	private BonusDistributeType fifthSubBonusDistributeTypePerson; //责任人分配类型5

	private RefundAuditStatus auditStatus; // 审批状态
	private String auditUserId; // 审批人
	private String auditUserName;
	private String auditCampusId;
	private String auditCampusName;
	private String auditUserDept;
	private long flowId; // 工作流ID
	private String refundTitle; // 标题
	private String initiateTime; // 发起时间
	private String acceptTime; // 接收时间
	
	private BigDecimal cpBasicAmount;
	private BigDecimal cpBasicMaxAmount;
	private BigDecimal cpPromotionAmount;
	private BigDecimal cpPromotionMaxAmount;
	
	private BigDecimal accRemainAmount;
	private BigDecimal accMaxAmount;
	
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	private String startDate;
	private String endDate;
	
	private AuditOperation operation;  //操作
	private int  actionId;//对应工作流的action
	
	private String operateType;//操作类型   1为单纯的修改，2为修改并且提交审批
	
	private String auditRemark;
	
	private String historyUserId;
	
	private int stepId;
	
	private String isManager; // true:是 false:不是
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public RefundFormType getFormType() {
		return formType;
	}
	public void setFormType(RefundFormType formType) {
		this.formType = formType;
	}
	public String getRefundCampusId() {
		return refundCampusId;
	}
	public void setRefundCampusId(String refundCampusId) {
		this.refundCampusId = refundCampusId;
	}
	public String getRefundCampusName() {
		return refundCampusName;
	}
	public void setRefundCampusName(String refundCampusName) {
		this.refundCampusName = refundCampusName;
	}
	public String getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	public String getApplicantName() {
		return applicantName;
	}
	
	public Integer getApplicantEnableFlg() {
		return applicantEnableFlg;
	}
	public void setApplicantEnableFlg(Integer applicantEnableFlg) {
		this.applicantEnableFlg = applicantEnableFlg;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getApplicantCampusId() {
		return applicantCampusId;
	}
	public void setApplicantCampusId(String applicantCampusId) {
		this.applicantCampusId = applicantCampusId;
	}
	public String getApplicantCampusName() {
		return applicantCampusName;
	}
	public void setApplicantCampusName(String applicantCampusName) {
		this.applicantCampusName = applicantCampusName;
	}
	public String getRefundCauseId() {
		return refundCauseId;
	}
	public void setRefundCauseId(String refundCauseId) {
		this.refundCauseId = refundCauseId;
	}
	public String getRefundCauseName() {
		return refundCauseName;
	}
	public void setRefundCauseName(String refundCauseName) {
		this.refundCauseName = refundCauseName;
	}
	public String getRefundEvidence() {
		return refundEvidence;
	}
	public void setRefundEvidence(String refundEvidence) {
		this.refundEvidence = refundEvidence;
	}
	public String getRefundDetailReason() {
		return refundDetailReason;
	}
	public void setRefundDetailReason(String refundDetailReason) {
		this.refundDetailReason = refundDetailReason;
	}
	public String getRefundStudentId() {
		return refundStudentId;
	}
	public void setRefundStudentId(String refundStudentId) {
		this.refundStudentId = refundStudentId;
	}
	public String getRefundStudentName() {
		return refundStudentName;
	}
	public void setRefundStudentName(String refundStudentName) {
		this.refundStudentName = refundStudentName;
	}
	public String getRefundContractProductId() {
		return refundContractProductId;
	}
	public void setRefundContractProductId(String refundContractProductId) {
		this.refundContractProductId = refundContractProductId;
	}
	public String getIntoCampusId() {
		return intoCampusId;
	}
	public void setIntoCampusId(String intoCampusId) {
		this.intoCampusId = intoCampusId;
	}
	public String getIntoCampusName() {
		return intoCampusName;
	}
	public void setIntoCampusName(String intoCampusName) {
		this.intoCampusName = intoCampusName;
	}
	public String getIntoStudentId() {
		return intoStudentId;
	}
	public void setIntoStudentId(String intoStudentId) {
		this.intoStudentId = intoStudentId;
	}
	public String getIntoStudentName() {
		return intoStudentName;
	}
	public void setIntoStudentName(String intoStudentName) {
		this.intoStudentName = intoStudentName;
	}
	public String getRefundAccount() {
		return refundAccount;
	}
	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}
	public String getRefundAccName() {
		return refundAccName;
	}
	public void setRefundAccName(String refundAccName) {
		this.refundAccName = refundAccName;
	}
	public String getBankInfo() {
		return bankInfo;
	}
	public void setBankInfo(String bankInfo) {
		this.bankInfo = bankInfo;
	}
	public BigDecimal getBasicOperateAmount() {
		return basicOperateAmount;
	}
	public void setBasicOperateAmount(BigDecimal basicOperateAmount) {
		this.basicOperateAmount = basicOperateAmount;
	}
	public BigDecimal getPromotionOperateAmount() {
		return promotionOperateAmount;
	}
	public void setPromotionOperateAmount(BigDecimal promotionOperateAmount) {
		this.promotionOperateAmount = promotionOperateAmount;
	}
	public BigDecimal getSpecialOperateAmount() {
		return specialOperateAmount;
	}
	public void setSpecialOperateAmount(BigDecimal specialOperateAmount) {
		this.specialOperateAmount = specialOperateAmount;
	}
	public String getFirstRefundDutyCampusId() {
		return firstRefundDutyCampusId;
	}
	public void setFirstRefundDutyCampusId(String firstRefundDutyCampusId) {
		this.firstRefundDutyCampusId = firstRefundDutyCampusId;
	}
	public String getFirstRefundDutyCampusName() {
		return firstRefundDutyCampusName;
	}
	public void setFirstRefundDutyCampusName(String firstRefundDutyCampusName) {
		this.firstRefundDutyCampusName = firstRefundDutyCampusName;
	}

	public BigDecimal getFirstRefundDutyAmountCampus() {
		return firstRefundDutyAmountCampus;
	}

	public void setFirstRefundDutyAmountCampus(BigDecimal firstRefundDutyAmountCampus) {
		this.firstRefundDutyAmountCampus = firstRefundDutyAmountCampus;
	}

	public BonusDistributeType getFirstSubBonusDistributeTypeCampus() {
		return firstSubBonusDistributeTypeCampus;
	}

	public void setFirstSubBonusDistributeTypeCampus(BonusDistributeType firstSubBonusDistributeTypeCampus) {
		this.firstSubBonusDistributeTypeCampus = firstSubBonusDistributeTypeCampus;
	}

	public String getSecondRefundDutyCampusId() {
		return secondRefundDutyCampusId;
	}
	public void setSecondRefundDutyCampusId(String secondRefundDutyCampusId) {
		this.secondRefundDutyCampusId = secondRefundDutyCampusId;
	}
	public String getSecondRefundDutyCampusName() {
		return secondRefundDutyCampusName;
	}
	public void setSecondRefundDutyCampusName(String secondRefundDutyCampusName) {
		this.secondRefundDutyCampusName = secondRefundDutyCampusName;
	}

	public String getThirdRefundDutyCampusId() {
		return thirdRefundDutyCampusId;
	}
	public void setThirdRefundDutyCampusId(String thirdRefundDutyCampusId) {
		this.thirdRefundDutyCampusId = thirdRefundDutyCampusId;
	}
	public String getThirdRefundDutyCampusName() {
		return thirdRefundDutyCampusName;
	}
	public void setThirdRefundDutyCampusName(String thirdRefundDutyCampusName) {
		this.thirdRefundDutyCampusName = thirdRefundDutyCampusName;
	}

	public String getFirstRefundDutyPersonId() {
		return firstRefundDutyPersonId;
	}
	public void setFirstRefundDutyPersonId(String firstRefundDutyPersonId) {
		this.firstRefundDutyPersonId = firstRefundDutyPersonId;
	}
	public String getFirstRefundDutyPersonName() {
		return firstRefundDutyPersonName;
	}
	public void setFirstRefundDutyPersonName(String firstRefundDutyPersonName) {
		this.firstRefundDutyPersonName = firstRefundDutyPersonName;
	}

	public BigDecimal getFirstRefundDutyAmountPerson() {
		return firstRefundDutyAmountPerson;
	}

	public void setFirstRefundDutyAmountPerson(BigDecimal firstRefundDutyAmountPerson) {
		this.firstRefundDutyAmountPerson = firstRefundDutyAmountPerson;
	}

	public BonusDistributeType getFirstSubBonusDistributeTypePerson() {
		return firstSubBonusDistributeTypePerson;
	}

	public void setFirstSubBonusDistributeTypePerson(BonusDistributeType firstSubBonusDistributeTypePerson) {
		this.firstSubBonusDistributeTypePerson = firstSubBonusDistributeTypePerson;
	}

	public String getSecondRefundDutyPersonId() {
		return secondRefundDutyPersonId;
	}
	public void setSecondRefundDutyPersonId(String secondRefundDutyPersonId) {
		this.secondRefundDutyPersonId = secondRefundDutyPersonId;
	}
	public String getSecondRefundDutyPersonName() {
		return secondRefundDutyPersonName;
	}
	public void setSecondRefundDutyPersonName(String secondRefundDutyPersonName) {
		this.secondRefundDutyPersonName = secondRefundDutyPersonName;
	}

	public String getThirdRefundDutyPersonId() {
		return thirdRefundDutyPersonId;
	}
	public void setThirdRefundDutyPersonId(String thirdRefundDutyPersonId) {
		this.thirdRefundDutyPersonId = thirdRefundDutyPersonId;
	}
	public String getThirdRefundDutyPersonName() {
		return thirdRefundDutyPersonName;
	}
	public void setThirdRefundDutyPersonName(String thirdRefundDutyPersonName) {
		this.thirdRefundDutyPersonName = thirdRefundDutyPersonName;
	}

	public RefundAuditStatus getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(RefundAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}
	public String getAuditUserName() {
		return auditUserName;
	}
	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
	public String getAuditCampusId() {
		return auditCampusId;
	}
	public void setAuditCampusId(String auditCampusId) {
		this.auditCampusId = auditCampusId;
	}
	public String getAuditCampusName() {
		return auditCampusName;
	}
	public void setAuditCampusName(String auditCampustName) {
		this.auditCampusName = auditCampustName;
	}
	public String getAuditUserDept() {
		return auditUserDept;
	}
	public void setAuditUserDept(String auditUserDept) {
		this.auditUserDept = auditUserDept;
	}
	public long getFlowId() {
		return flowId;
	}
	public void setFlowId(long flowId) {
		this.flowId = flowId;
	}
	public String getRefundTitle() {
		return refundTitle;
	}
	public void setRefundTitle(String refundTitle) {
		this.refundTitle = refundTitle;
	}
	public String getInitiateTime() {
		return initiateTime;
	}
	public void setInitiateTime(String initiateTime) {
		this.initiateTime = initiateTime;
	}
	public String getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
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
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public AuditOperation getOperation() {
		return operation;
	}
	public void setOperation(AuditOperation operation) {
		this.operation = operation;
	}
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public String getHistoryUserId() {
		return historyUserId;
	}
	public void setHistoryUserId(String historyUserId) {
		this.historyUserId = historyUserId;
	}
	public int getStepId() {
		return stepId;
	}
	public void setStepId(int stepId) {
		this.stepId = stepId;
	}
	public BigDecimal getCpBasicAmount() {
		return cpBasicAmount;
	}
	public void setCpBasicAmount(BigDecimal cpBasicAmount) {
		this.cpBasicAmount = cpBasicAmount;
	}
	public BigDecimal getCpBasicMaxAmount() {
		return cpBasicMaxAmount;
	}
	public void setCpBasicMaxAmount(BigDecimal cpBasicMaxAmount) {
		this.cpBasicMaxAmount = cpBasicMaxAmount;
	}
	public BigDecimal getCpPromotionAmount() {
		return cpPromotionAmount;
	}
	public void setCpPromotionAmount(BigDecimal cpPromotionAmount) {
		this.cpPromotionAmount = cpPromotionAmount;
	}
	public BigDecimal getCpPromotionMaxAmount() {
		return cpPromotionMaxAmount;
	}
	public void setCpPromotionMaxAmount(BigDecimal cpPromotionMaxAmount) {
		this.cpPromotionMaxAmount = cpPromotionMaxAmount;
	}
	public String getIsManager() {
		return isManager;
	}
	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}
	public BigDecimal getAccRemainAmount() {
		return accRemainAmount;
	}
	public void setAccRemainAmount(BigDecimal accRemainAmount) {
		this.accRemainAmount = accRemainAmount;
	}
	public BigDecimal getAccMaxAmount() {
		return accMaxAmount;
	}
	public void setAccMaxAmount(BigDecimal accMaxAmount) {
		this.accMaxAmount = accMaxAmount;
	}


	public BonusDistributeType getSecondSubBonusDistributeTypeCampus() {
		return secondSubBonusDistributeTypeCampus;
	}

	public void setSecondSubBonusDistributeTypeCampus(BonusDistributeType secondSubBonusDistributeTypeCampus) {
		this.secondSubBonusDistributeTypeCampus = secondSubBonusDistributeTypeCampus;
	}

	public BonusDistributeType getThirdSubBonusDistributeTypeCampus() {
		return thirdSubBonusDistributeTypeCampus;
	}

	public void setThirdSubBonusDistributeTypeCampus(BonusDistributeType thirdSubBonusDistributeTypeCampus) {
		this.thirdSubBonusDistributeTypeCampus = thirdSubBonusDistributeTypeCampus;
	}

	public String getFourthRefundDutyCampusId() {
		return fourthRefundDutyCampusId;
	}

	public void setFourthRefundDutyCampusId(String fourthRefundDutyCampusId) {
		this.fourthRefundDutyCampusId = fourthRefundDutyCampusId;
	}

	public String getFourthRefundDutyCampusName() {
		return fourthRefundDutyCampusName;
	}

	public void setFourthRefundDutyCampusName(String fourthRefundDutyCampusName) {
		this.fourthRefundDutyCampusName = fourthRefundDutyCampusName;
	}

	public BigDecimal getFourthRefundDutyAmountCampus() {
		return fourthRefundDutyAmountCampus;
	}

	public void setFourthRefundDutyAmountCampus(BigDecimal fourthRefundDutyAmountCampus) {
		this.fourthRefundDutyAmountCampus = fourthRefundDutyAmountCampus;
	}

	public BonusDistributeType getFourthSubBonusDistributeTypeCampus() {
		return fourthSubBonusDistributeTypeCampus;
	}

	public void setFourthSubBonusDistributeTypeCampus(BonusDistributeType fourthSubBonusDistributeTypeCampus) {
		this.fourthSubBonusDistributeTypeCampus = fourthSubBonusDistributeTypeCampus;
	}

	public String getFifthRefundDutyCampusId() {
		return fifthRefundDutyCampusId;
	}

	public void setFifthRefundDutyCampusId(String fifthRefundDutyCampusId) {
		this.fifthRefundDutyCampusId = fifthRefundDutyCampusId;
	}

	public String getFifthRefundDutyCampusName() {
		return fifthRefundDutyCampusName;
	}

	public void setFifthRefundDutyCampusName(String fifthRefundDutyCampusName) {
		this.fifthRefundDutyCampusName = fifthRefundDutyCampusName;
	}

	public BigDecimal getFifthRefundDutyAmountCampus() {
		return fifthRefundDutyAmountCampus;
	}

	public void setFifthRefundDutyAmountCampus(BigDecimal fifthRefundDutyAmountCampus) {
		this.fifthRefundDutyAmountCampus = fifthRefundDutyAmountCampus;
	}

	public BonusDistributeType getFifthSubBonusDistributeTypeCampus() {
		return fifthSubBonusDistributeTypeCampus;
	}

	public void setFifthSubBonusDistributeTypeCampus(BonusDistributeType fifthSubBonusDistributeTypeCampus) {
		this.fifthSubBonusDistributeTypeCampus = fifthSubBonusDistributeTypeCampus;
	}

	public BigDecimal getSecondRefundDutyAmountPerson() {
		return secondRefundDutyAmountPerson;
	}

	public void setSecondRefundDutyAmountPerson(BigDecimal secondRefundDutyAmountPerson) {
		this.secondRefundDutyAmountPerson = secondRefundDutyAmountPerson;
	}

	public BonusDistributeType getSecondSubBonusDistributeTypePerson() {
		return secondSubBonusDistributeTypePerson;
	}

	public void setSecondSubBonusDistributeTypePerson(BonusDistributeType secondSubBonusDistributeTypePerson) {
		this.secondSubBonusDistributeTypePerson = secondSubBonusDistributeTypePerson;
	}

	public BigDecimal getThirdRefundDutyAmountPerson() {
		return thirdRefundDutyAmountPerson;
	}

	public void setThirdRefundDutyAmountPerson(BigDecimal thirdRefundDutyAmountPerson) {
		this.thirdRefundDutyAmountPerson = thirdRefundDutyAmountPerson;
	}

	public BonusDistributeType getThirdSubBonusDistributeTypePerson() {
		return thirdSubBonusDistributeTypePerson;
	}

	public void setThirdSubBonusDistributeTypePerson(BonusDistributeType thirdSubBonusDistributeTypePerson) {
		this.thirdSubBonusDistributeTypePerson = thirdSubBonusDistributeTypePerson;
	}

	public String getFourthRefundDutyPersonId() {
		return fourthRefundDutyPersonId;
	}

	public void setFourthRefundDutyPersonId(String fourthRefundDutyPersonId) {
		this.fourthRefundDutyPersonId = fourthRefundDutyPersonId;
	}

	public String getFourthRefundDutyPersonName() {
		return fourthRefundDutyPersonName;
	}

	public void setFourthRefundDutyPersonName(String fourthRefundDutyPersonName) {
		this.fourthRefundDutyPersonName = fourthRefundDutyPersonName;
	}

	public BigDecimal getFourthRefundDutyAmountPerson() {
		return fourthRefundDutyAmountPerson;
	}

	public void setFourthRefundDutyAmountPerson(BigDecimal fourthRefundDutyAmountPerson) {
		this.fourthRefundDutyAmountPerson = fourthRefundDutyAmountPerson;
	}

	public BonusDistributeType getFourthSubBonusDistributeTypePerson() {
		return fourthSubBonusDistributeTypePerson;
	}

	public void setFourthSubBonusDistributeTypePerson(BonusDistributeType fourthSubBonusDistributeTypePerson) {
		this.fourthSubBonusDistributeTypePerson = fourthSubBonusDistributeTypePerson;
	}

	public String getFifthRefundDutyPersonId() {
		return fifthRefundDutyPersonId;
	}

	public void setFifthRefundDutyPersonId(String fifthRefundDutyPersonId) {
		this.fifthRefundDutyPersonId = fifthRefundDutyPersonId;
	}

	public String getFifthRefundDutyPersonName() {
		return fifthRefundDutyPersonName;
	}

	public void setFifthRefundDutyPersonName(String fifthRefundDutyPersonName) {
		this.fifthRefundDutyPersonName = fifthRefundDutyPersonName;
	}

	public BigDecimal getFifthRefundDutyAmountPerson() {
		return fifthRefundDutyAmountPerson;
	}

	public void setFifthRefundDutyAmountPerson(BigDecimal fifthRefundDutyAmountPerson) {
		this.fifthRefundDutyAmountPerson = fifthRefundDutyAmountPerson;
	}

	public BonusDistributeType getFifthSubBonusDistributeTypePerson() {
		return fifthSubBonusDistributeTypePerson;
	}

	public void setFifthSubBonusDistributeTypePerson(BonusDistributeType fifthSubBonusDistributeTypePerson) {
		this.fifthSubBonusDistributeTypePerson = fifthSubBonusDistributeTypePerson;
	}

	public BigDecimal getSecondRefundDutyAmountCampus() {
		return secondRefundDutyAmountCampus;
	}

	public void setSecondRefundDutyAmountCampus(BigDecimal secondRefundDutyAmountCampus) {
		this.secondRefundDutyAmountCampus = secondRefundDutyAmountCampus;
	}

	public BigDecimal getThirdRefundDutyAmountCampus() {
		return thirdRefundDutyAmountCampus;
	}

	public void setThirdRefundDutyAmountCampus(BigDecimal thirdRefundDutyAmountCampus) {
		this.thirdRefundDutyAmountCampus = thirdRefundDutyAmountCampus;
	}
}
