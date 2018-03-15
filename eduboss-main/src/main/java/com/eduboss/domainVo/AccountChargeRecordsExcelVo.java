package com.eduboss.domainVo;

import com.eduboss.common.ChargePayType;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class AccountChargeRecordsExcelVo implements java.io.Serializable {

	// Fields
	private String transactionId;
	private String contractId;
	private String blCampusName;
	private String chargeTypeName; //扣费类型
	private String payTypeName;  //资金类型
	private BigDecimal payHour=BigDecimal.ZERO;
	private BigDecimal amount=BigDecimal.ZERO;
	private String payTime;
	private String courseTime;//上课时间
	private String transactionTime;//业务日期
	private String teacherName;
	private String studentName;
	private String operateUserName;
	private String productTypeName;
	private String productName;	
	private String courseMinutes; //课时时长
	private String miniClassName;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public BigDecimal getPayHour() {
		if(payHour==null)
			return BigDecimal.ZERO;
			else
		return payHour;
	}
	public void setPayHour(BigDecimal payHour) {
		this.payHour = payHour;
	}
	public BigDecimal getAmount() {
		if(amount==null)
			return BigDecimal.ZERO;
			else
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getCourseTime() {
		if(courseTime==null)
			return "";
		else
			return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	
	public String getTransactionTime() {
		if(transactionTime==null)
			return "";
		else
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	public String getTeacherName() {
		if(teacherName==null)
			return "";
		else
			return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getOperateUserName() {
		return operateUserName;
	}
	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}
	public String getProductTypeName() {
		if(StringUtils.isNotBlank(productTypeName)) {
			return ProductType.valueOf(productTypeName).getName();
		}
		return "";
	}
	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}
	public String getProductName() {
		if(productName==null)
			return "";
		else
			return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMiniClassName() {
		if(miniClassName==null)
			return "";
		else
		return miniClassName;
	}
	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}
	
	public String getCourseMinutes() {
		if(courseMinutes==null)
			return "";
		else
		return courseMinutes;
	}
	public void setCourseMinutes(String courseMinutes) {
		this.courseMinutes = courseMinutes;
	}
	public String getChargeTypeName() {
		if(StringUtils.isNotBlank(chargeTypeName)) {
			return ChargePayType.valueOf(chargeTypeName).getName();
		}
		return "";
	}
	public void setChargeTypeName(String chargeTypeName) {
		this.chargeTypeName = chargeTypeName;
	}
	public String getPayTypeName() {
		if(StringUtils.isNotBlank(payTypeName)) {
			return PayType.valueOf(payTypeName).getName();
		}
		return "";
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}

}
