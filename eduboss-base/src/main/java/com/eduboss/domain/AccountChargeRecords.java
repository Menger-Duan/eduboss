package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.ChargePayType;
import com.eduboss.common.ChargeType;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;

/**
 * ContractProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCOUNT_CHARGE_RECORDS")
public class AccountChargeRecords implements java.io.Serializable {

	// Fields
	private String id;
	private Contract contract;
	private BigDecimal amount;
	private String payChannel;
	private String payTime;
	private Course course;
	private Product product;
	private User operateUser;
	private Student student;
	private String remark;
	private MiniClass miniClass;
	private MiniClassCourse miniClassCourse;
	private ProductType productType;
	private BigDecimal quality;
	private ContractProduct contractProduct;
	private ChargeType chargeType;
	private PromiseClassRecord promiseClassRecord; //目标班月结记录
	private Organization blCampusId;
	
	private String transactionId; 
	private PayType payType; //资金来源（REAL：实收金额，PROMOTION：优惠金额）
	private String transactionTime;
	
	// 一对多
	private OtmClass otmClass;
	private OtmClassCourse otmClassCourse;
	
	private BigDecimal courseMinutes; // 扣费课程分钟数
	
	private LectureClassStudent lectureClassStudent;
	
	//老师
	private User teacher;
	
	private ChargePayType chargePayType; // 扣费，冲销类型
	
	private String isWashed; // 是否发生过冲销  TRUE: 已冲销， FALSE: 没冲销

	private TwoTeacherClassStudentAttendent twoTeacherClassStudentAttendent; //双师考勤记录

	private Curriculum curriculum;//直播课程
	
	// Constructors
	/** default constructor */
	public AccountChargeRecords() {
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID")
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_ID")
	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@Column(name = "AMOUNT", precision = 10)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "PAY_CHANNEL", length = 32)
	public String getPayChannel() {
		return this.payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	@Column(name = "PAY_TIME", length = 20)
	public String getPayTime() {
		return this.payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	@Column(name = "REMARK", length = 1024)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OPERATE_USER_ID")
	public User getOperateUser() {
		return operateUser;
	}

	public void setOperateUser(User operateUser) {
		this.operateUser = operateUser;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MINI_CLASS_ID")
	public MiniClass getMiniClass() {
		return miniClass;
	}

	public void setMiniClass(MiniClass miniClass) {
		this.miniClass = miniClass;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MINI_CLASS_COURSE_ID")
	public MiniClassCourse getMiniClassCourse() {
		return miniClassCourse;
	}

	public void setMiniClassCourse(MiniClassCourse miniClassCourse) {
		this.miniClassCourse = miniClassCourse;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "PRODUCT_TYPE", length = 32)
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	@Column(name = "QUANTITY", precision = 10)
	public BigDecimal getQuality() {
		return quality;
	}

	public void setQuality(BigDecimal quality) {
		this.quality = quality;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_PRODUCT_ID")
	public ContractProduct getContractProduct() {
		return contractProduct;
	}

	public void setContractProduct(ContractProduct contractProduct) {
		this.contractProduct = contractProduct;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "CHARGE_TYPE", length = 32)
	public ChargeType getChargeType() {
		return chargeType;
	}

	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROMISE_CLASS_RECORD_ID")
	public PromiseClassRecord getPromiseClassRecord() {
		return promiseClassRecord;
	}

	public void setPromiseClassRecord(PromiseClassRecord promiseClassRecord) {
		this.promiseClassRecord = promiseClassRecord;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BL_CAMPUS_ID")
	public Organization getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(Organization blCampusId) {
		this.blCampusId = blCampusId;
	}

	@Column(name = "TRANSACTION_ID", length = 100)
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "PAY_TYPE", length = 32)
	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	@Column(name = "TRANSACTION_TIME", length = 20)
	public String getTransactionTime() {
		return transactionTime;
	}
	
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OTM_CLASS_ID")
	public OtmClass getOtmClass() {
		return otmClass;
	}

	public void setOtmClass(OtmClass otmClass) {
		this.otmClass = otmClass;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OTM_CLASS_COURSE_ID")
	public OtmClassCourse getOtmClassCourse() {
		return otmClassCourse;
	}

	public void setOtmClassCourse(OtmClassCourse otmClassCourse) {
		this.otmClassCourse = otmClassCourse;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	@Column(name = "COURSE_MINUTES", precision = 10)
	public BigDecimal getCourseMinutes() {
		return courseMinutes;
	}

	public void setCourseMinutes(BigDecimal courseMinutes) {
		this.courseMinutes = courseMinutes;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LECTURE_CLASS_STUDENT_ID")
	public LectureClassStudent getLectureClassStudent() {
		return lectureClassStudent;
	}

	public void setLectureClassStudent(LectureClassStudent lectureClassStudent) {
		this.lectureClassStudent = lectureClassStudent;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "CHARGE_PAY_TYPE", length = 32)
	public ChargePayType getChargePayType() {
		return chargePayType;
	}

	public void setChargePayType(ChargePayType chargePayType) {
		this.chargePayType = chargePayType;
	}

	@Column(name = "IS_WASHED", length = 5)
	public String getIsWashed() {
		return isWashed;
	}

	public void setIsWashed(String isWashed) {
		this.isWashed = isWashed;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TWO_TEACHER_STUDENT_ATTENDENT")
	public TwoTeacherClassStudentAttendent getTwoTeacherClassStudentAttendent() {
		return twoTeacherClassStudentAttendent;
	}

	public void setTwoTeacherClassStudentAttendent(TwoTeacherClassStudentAttendent twoTeacherClassStudentAttendent) {
		this.twoTeacherClassStudentAttendent = twoTeacherClassStudentAttendent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curriculum")
	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}
}