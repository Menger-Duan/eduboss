package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.ChargePayType;
import com.eduboss.common.FundsPayType;

/**
 * 学生资金变更记录Vo
 * */

public class StudentMoneyChangesVo {

	/**
	 * id
	 * */
	private String id;
	
	/**
	 * 类型（扣费还是收费）
	 * */
	private String type;
	
	/**
	 * 发生时间
	 * */
	private String happenedTime;

	/**
	 * 操作人姓名
	 */
	private String operateUserName;
	
	/**
	 * 学生ID
	 * */
	private String studentId;
	
	/**
	 * 学生名字
	 * */
	private String studentName;
	
	/**
	 * 校区
	 * */
	private String campusName;
	
	/**
	 * 学管名称
	 * */
	private String studyManagerName;
	
	/**
	 * 合同ID
	 * */
	private String contractId;
	
	/**
	 * 合同产品ID
	 * */
	private String contractProductId;
	
	/**
	 * 金额
	 * */
	private BigDecimal amount;
	
	/**
	 * 途径
	 * */
	private String channel;
	
	/**
	 * 扣费类型
	 * */
	private String chargeType;
	
	/**
	 * 产品类型
	 * */
	private String productTypeName;
	
	/**
	 * 一对一Id
	 * */
	private String courseId;
	
	/**
	 * 一对一名称
	 * */
	private String courseName;
	
	/**
	 * 小班
	 * */
	private String miniClassCourseId;
	
	/**
	 * 小班名称
	 * */
	private String miniClassName;
	
	/**
	 * 目标班月结记录
	 * */
	private String promiseCourseId;
	
	/**
	 * 目标班名称
	 * */
	private String promiseClassName;
	
	/**
	 * 一对多课程ID
	 */
	private String otmClassCourseId;
	
	/**
	 * 一对多班级
	 */
	private String otmClassName;
	
	/**
	 * 课程名称
	 * */
	private String className;
	
	private FundsPayType fundsPayType; // 收款，冲销类型
	
	private ChargePayType chargePayType; // 扣费，冲销类型
	
	private String washReason; // 冲销原因

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHappenedTime() {
		return happenedTime;
	}

	public void setHappenedTime(String happenedTime) {
		this.happenedTime = happenedTime;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getStudyManagerName() {
		return studyManagerName;
	}

	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getMiniClassCourseId() {
		return miniClassCourseId;
	}

	public void setMiniClassCourseId(String miniClassCourseId) {
		this.miniClassCourseId = miniClassCourseId;
	}

	public String getPromiseCourseId() {
		return promiseCourseId;
	}

	public void setPromiseCourseId(String promiseCourseId) {
		this.promiseCourseId = promiseCourseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getClassName() {
		if(this.courseName!=null){
			return this.courseName;
		}else if(this.miniClassName!=null){
			return this.miniClassName;
		}else if(this.promiseClassName!=null){
			return this.promiseClassName;
		} else if (this.otmClassName!=null) {
			return this.otmClassName;
		}
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMiniClassName() {
		return miniClassName;
	}

	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}

	public String getPromiseClassName() {
		return promiseClassName;
	}

	public void setPromiseClassName(String promiseClassName) {
		this.promiseClassName = promiseClassName;
	}

	public String getOtmClassCourseId() {
		return otmClassCourseId;
	}

	public void setOtmClassCourseId(String otmClassCourseId) {
		this.otmClassCourseId = otmClassCourseId;
	}

	public String getOtmClassName() {
		return otmClassName;
	}

	public void setOtmClassName(String otmClassName) {
		this.otmClassName = otmClassName;
	}

	public FundsPayType getFundsPayType() {
		return fundsPayType;
	}

	public void setFundsPayType(FundsPayType fundsPayType) {
		this.fundsPayType = fundsPayType;
	}

	public ChargePayType getChargePayType() {
		return chargePayType;
	}

	public void setChargePayType(ChargePayType chargePayType) {
		this.chargePayType = chargePayType;
	}

	public String getOperateUserName() {
		return operateUserName;
	}

	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}

	public String getWashReason() {
		return washReason;
	}

	public void setWashReason(String washReason) {
		this.washReason = washReason;
	}
	
}
