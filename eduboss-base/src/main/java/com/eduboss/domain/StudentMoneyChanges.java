package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.ChargePayType;
import com.eduboss.common.ChargeType;
import com.eduboss.common.FundsPayType;
import com.eduboss.common.PayWay;
import com.eduboss.common.ProductType;

/**
 * 学生资金变更记录
 * */

@Entity
@Table( name = "MONEY_CHANGE_RECORD")
public class StudentMoneyChanges implements java.io.Serializable{
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
	 * 操作人
	 */
	private User operateUser;
	
	/**
	 * 学生ID
	 * */
	private Student student;
	
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
	private PayWay channel;
	
	/**
	 * 扣费类型
	 * */
	private ChargeType chargeType;
	
	/**
	 * 产品类型
	 * */
	private ProductType productType;
	
	/**
	 * 一对一
	 * */
	private Course course;
	
	/**
	 * 小班
	 * */
	private MiniClassCourse miniClass;
	
	/**
	 * 目标班月结记录
	 * */
	private PromiseClassRecord promiseClassRecord;
	
	/**
	 * 一对多
	 */
	private OtmClassCourse otmClassCourse;
	
	private FundsPayType fundsPayType; // 收款，冲销类型
	
	private ChargePayType chargePayType; // 扣费，冲销类型
	
	private Organization campus;

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column( name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column( name = "HAPPEN_TIME")
	public String getHappenedTime() {
		return happenedTime;
	}

	public void setHappenedTime(String happenedTime) {
		this.happenedTime = happenedTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "operate_user_id")
	public User getOperateUser() {
		return operateUser;
	}

	public void setOperateUser(User operateUser) {
		this.operateUser = operateUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Column( name = "CONTRACT_ID")
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	@Column( name = "CONTRACT_PRODUCT_ID")
	public String getContractProductId() {
		return contractProductId;
	}

	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}

	@Column( name = "AMOUNT")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column( name = "CHANNEL")
	public PayWay getChannel() {
		return channel;
	}

	public void setChannel(PayWay channel) {
		this.channel = channel;
	}

	@Column( name = "CHARGE_TYPE")
	public ChargeType getChargeType() {
		return chargeType;
	}

	

	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}

	@Column( name = "PRODUCT_TYPE")
	public ProductType getProductType() {
		return productType;
	}
	
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "COURSE_ID")
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn( name = "MINI_CLASS_COURSE_ID")
	public MiniClassCourse getMiniClass() {
		return miniClass;
	}

	public void setMiniClass(MiniClassCourse miniClass) {
		this.miniClass = miniClass;
	}

	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn( name = "PROMISE_CLASS_RECORD_ID")
	public PromiseClassRecord getpromiseClassRecord() {
		return promiseClassRecord;
	}

	public void setpromiseClassRecord(PromiseClassRecord promiseClassRecord) {
		this.promiseClassRecord = promiseClassRecord;
	}

	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn( name = "OTM_CLASS_COURSE_ID")
	public OtmClassCourse getOtmClassCourse() {
		return otmClassCourse;
	}

	public void setOtmClassCourse(OtmClassCourse otmClassCourse) {
		this.otmClassCourse = otmClassCourse;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "FUNDS_PAY_TYPE", length = 32)
	public FundsPayType getFundsPayType() {
		return fundsPayType;
	}

	public void setFundsPayType(FundsPayType fundsPayType) {
		this.fundsPayType = fundsPayType;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "CHARGE_PAY_TYPE", length = 32)
	public ChargePayType getChargePayType() {
		return chargePayType;
	}

	public void setChargePayType(ChargePayType chargePayType) {
		this.chargePayType = chargePayType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "campus_id")
	public Organization getCampus() {
		return campus;
	}

	public void setCampus(Organization campus) {
		this.campus = campus;
	}
	
	
	
	/*
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn( name = "COURSE_ID")
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn( name = "MINI_CLASS_COURSE_ID")
	public MiniClassCourse getMiniClassCourse() {
		return miniClassCourse;
	}

	public void setMiniClassCourse(MiniClassCourse miniClassCourse) {
		this.miniClassCourse = miniClassCourse;
	}

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn( name = "PROMISE_CLASS_RECORD_ID")
	public PromiseClassRecord getPromiseCourse() {
		return promiseCourse;
	}

	public void setPromiseCourse(PromiseClassRecord promiseCourse) {
		this.promiseCourse = promiseCourse;
	}
	
	*/
	
	
}
