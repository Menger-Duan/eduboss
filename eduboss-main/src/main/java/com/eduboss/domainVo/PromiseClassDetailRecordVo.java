package com.eduboss.domainVo;

import java.math.BigDecimal;

import javax.persistence.Table;

/**
 * 目标班月结详细记录表
 * @author laiyongchang
 * */


public class PromiseClassDetailRecordVo implements java.io.Serializable{
	
	/**
	 * 目标班详细月结ID
	 * */
	private String id;
	
	/**
	 * 目标班月结记录ID
	 * */
	private String promiseClassRecordId;
	
	/**
	 * 课程类型
	 * */
	private String classType;
	
	/**
	 * 科目
	 * */
	private String subject;
	
	/**
	 * 课时 
	 * */
	private String courseHours;
	
	/**
	 * 扣款金额
	 * */
	private BigDecimal chargeAmount;
	
	/**
	 * 上课老师
	 * */
	private String teacher;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPromiseClassRecordId() {
		return promiseClassRecordId;
	}

	public void setPromiseClassRecordId(String promiseClassRecordId) {
		this.promiseClassRecordId = promiseClassRecordId;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCourseHours() {
		return courseHours;
	}

	public void setCourseHours(String courseHours) {
		this.courseHours = courseHours;
	}

	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
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
	
	

}
