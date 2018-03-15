package com.eduboss.domainVo;

import java.util.List;

import javax.persistence.Table;

/**
 * 目标班月结记录表
 * @author laiyongchang
 * */

public class PromiseClassRecordVo implements java.io.Serializable{

	/**
	 * 目标班月结记录ID
	 * */
	private String id;
	
	/**
	 * 目标班ID
	 * */
	private String promiseClassId;
	
	/**
	 * 学生ID
	 * */
	private String studentId;
	
	/**
	 * 上课年份
	 * */
	private String classYear;
	
	/**
	 * 上课月份
	 * */
	private String classMonth;
	
	/**
	 * 扣费总课时
	 * */
	private Double chargeHours;
	
	/**
	 * 扣费总费用
	 * */
	private Double chargeAmount;
	
	/**
	 * 状态
	 * */
	private String status;
	
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
	 * 记录修改人ID
	 * */
	private String modifyUserId;
	
	private String chargeCampusId;
	
	private String chargeCampusName;
	
	/**
	 * 月结详细
	 * */
	private PromiseClassDetailRecordVo classDetail;

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

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getClassYear() {
		return classYear;
	}

	public void setClassYear(String classYear) {
		this.classYear = classYear;
	}

	public String getClassMonth() {
		return classMonth;
	}

	public void setClassMonth(String classMonth) {
		this.classMonth = classMonth;
	}

	public Double getChargeHours() {
		return chargeHours;
	}

	public void setChargeHours(Double chargeHours) {
		this.chargeHours = chargeHours;
	}

	public Double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(Double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public PromiseClassDetailRecordVo getClassDetail() {
		return classDetail;
	}

	public void setClassDetail(PromiseClassDetailRecordVo classDetail) {
		this.classDetail = classDetail;
	}

	@Override
	public String toString() {
		return "PromiseClassRecord [chargeAmount=" + chargeAmount
				+ ", chargeHours=" + chargeHours + ", classMonth=" + classMonth
				+ ", classYear=" + classYear + ", createTime=" + createTime
				+ ", createUserId=" + createUserId + ", id=" + id
				+ ", modifyTime=" + modifyTime + ", modifyUserId="
				+ modifyUserId + ", promiseClassId=" + promiseClassId
				+ ", studentId=" + studentId + "]";
	}

	public String getChargeCampusId() {
		return chargeCampusId;
	}

	public void setChargeCampusId(String chargeCampusId) {
		this.chargeCampusId = chargeCampusId;
	}

	public String getChargeCampusName() {
		return chargeCampusName;
	}

	public void setChargeCampusName(String chargeCampusName) {
		this.chargeCampusName = chargeCampusName;
	}
	
	
}
