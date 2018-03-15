package com.eduboss.domainVo;

import com.eduboss.common.PromiseAuditStatus;
import com.eduboss.common.StudentPromiseStatus;
import com.eduboss.domain.PromiseClassEndAudit;

import java.math.BigDecimal;
import java.util.List;

/**
 * 目标班学生管理（班主任）
 * @author laiyongchang
 * */

public class PromiseStudentVo {

	/**
	 * ID
	 * */
	private String id;

	/**
	 * 目标班ID
	 * */
	private String promiseClassId;

	/**
	 * 目标班名称
	 * */
	private String promiseClassName;

	private String promiseClassCampusName;

	/**
	 * 学生ID
	 * */
	private String studentId;

	/**
	 * 学生名称
	 * */
	private String studentName;

	/**
	 * 合同产品ID
	 * */
	private String contractProductId;

	/**
	 * 上课时间
	 * */
	private String courseDate;

	/**
	 * 上课状态
	 * */
	private String courseStatus;

	/**
	 * 完结状态
	 * */
	private String resultStatus;

	/**
	 * 是否退班
	 * */
	private String abortClass;

	/**
	 * 记录创建时间
	 * */
	private String createTime;

	/**
	 * 记录创建者ID
	 * */
	private String createUserId;

	/**
	 * 记录修改时间
	 * */
	private String modifyTime;

	/**
	 * 记录修改者ID
	 * */
	private String modifyUserId;

	/**
	 * 已付金额
	 * */
	private BigDecimal paidAmount;

	/**
	 * 消费金额
	 * */
	private BigDecimal consumeAmount;

	/**
	 * 剩余金额
	 * */
	private BigDecimal remainingAmount;

	private BigDecimal planAmount;

	/**
	 * 总课时数
	 * */
	private BigDecimal quantity;

	/**
	 * 消费课时数
	 * */
	private BigDecimal consumeQuantity;

	/**
	 * 保底资金比例
	 * */
	private BigDecimal promiseClassDiscount;

	/**
	 * 课程进度中的消费课时数改取月结扣费课时
	 * @return
	 */
	private BigDecimal monthHours;

	private String contractProductStatus;//合同产品状态

	private String paidStatus;//合同产品支付状态

	private PromiseClassEndAudit audit;

	private String productType;

	private String  courseSeriesId;

	private List subjectDetail;

	private String studentGrade;

	private PromiseAuditStatus auditStatus;

	private String productName;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStudentGrade() {
		return studentGrade;
	}

	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}

	public List getSubjectDetail() {
		return subjectDetail;
	}

	public void setSubjectDetail(List subjectDetail) {
		this.subjectDetail = subjectDetail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPromiseClassId() {
		return promiseClassId;
	}

	public void setPromiseClassId(String promiseClassId) {
		this.promiseClassId = promiseClassId;
	}

	public String getPromiseClassName() {
		return promiseClassName;
	}

	public void setPromiseClassName(String promiseClassName) {
		this.promiseClassName = promiseClassName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getContractProductId() {
		return contractProductId;
	}

	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}

	public String getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getAbortClass() {
		return abortClass;
	}

	public void setAbortClass(String abortClass) {
		this.abortClass = abortClass;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public BigDecimal getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getConsumeQuantity() {
		return consumeQuantity;
	}

	public void setConsumeQuantity(BigDecimal consumeQuantity) {
		this.consumeQuantity = consumeQuantity;
	}


	public BigDecimal getPromiseClassDiscount() {
		return promiseClassDiscount;
	}

	public void setPromiseClassDiscount(BigDecimal promiseClassDiscount) {
		this.promiseClassDiscount = promiseClassDiscount;
	}

	/**
	 * 剩余资金
	 * */
	public BigDecimal getRemainingFunds() {
		if(paidAmount!=null && consumeAmount!=null && promiseClassDiscount !=null){
			return (paidAmount.subtract(consumeAmount));
		}else{
			return new BigDecimal("0");
		}
	}

	/**
	 * 课程进度
	 * */
	public String getClassProgress() {
		return monthHours+" / "+quantity;
	}

	public BigDecimal getMonthHours() {
		return monthHours;
	}

	public void setMonthHours(BigDecimal monthHours) {
		this.monthHours = monthHours;
	}

	public String getContractProductStatus() {
		return contractProductStatus;
	}

	public void setContractProductStatus(String contractProductStatus) {
		this.contractProductStatus = contractProductStatus;
	}

	public PromiseClassEndAudit getAudit() {
		return audit;
	}

	public void setAudit(PromiseClassEndAudit audit) {
		this.audit = audit;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getCourseSeriesId() {
		return courseSeriesId;
	}

	public void setCourseSeriesId(String courseSeriesId) {
		this.courseSeriesId = courseSeriesId;
	}

	public String getPromiseClassCampusName() {
		return promiseClassCampusName;
	}

	public void setPromiseClassCampusName(String promiseClassCampusName) {
		this.promiseClassCampusName = promiseClassCampusName;
	}

	public String getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(String paidStatus) {
		this.paidStatus = paidStatus;
	}

	public PromiseAuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(PromiseAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
}
