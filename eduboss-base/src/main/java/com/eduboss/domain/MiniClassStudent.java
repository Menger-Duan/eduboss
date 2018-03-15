package com.eduboss.domain;

import javax.persistence.*;

import com.eduboss.common.MiniClassStatus;
import com.eduboss.common.MiniClassStudentChargeStatus;

/**
 * MiniClassStudent entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "mini_class_student")
public class MiniClassStudent implements java.io.Serializable {

	// Fields

	private int id;
	private MiniClass miniClass;
	private Student student;
	private String signUpDate;
	private MiniClassStatus miniClassStudyStatus;
	private MiniClassStudentChargeStatus  miniClassStudentChargeStatus;//�ѿ۷�ʵ������Ԥ�۷� ������û��Ԥ�۷ѣ�
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private Contract contract;
	private ContractProduct contractProduct;
	private String firstSchoolTime;//首次上课时间
	private int version;

	// Constructors

	/** default constructor */
	public MiniClassStudent() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MINI_CLASS_ID")
	public MiniClass getMiniClass() {
		return this.miniClass;
	}

	public void setMiniClass(MiniClass miniClass) {
		this.miniClass = miniClass;
	}

	@Column(name = "SIGN_UP_DATE", length = 20)
	public String getSignUpDate() {
		return this.signUpDate;
	}

	public void setSignUpDate(String signUpDate) {
		this.signUpDate = signUpDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MINI_CLASS_STUDY_STATUS", length = 32)
	public MiniClassStatus getMiniClassStudyStatus() {
		return this.miniClassStudyStatus;
	}

	public void setMiniClassStudyStatus(MiniClassStatus miniClassStudyStatus) {
		this.miniClassStudyStatus = miniClassStudyStatus;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_ID")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MINI_CLASS_CHARGE_STATUS", length = 32)
	public MiniClassStudentChargeStatus getMiniClassStudentChargeStatus() {
		return miniClassStudentChargeStatus;
	}

	public void setMiniClassStudentChargeStatus(
			MiniClassStudentChargeStatus miniClassStudentChargeStatus) {
		this.miniClassStudentChargeStatus = miniClassStudentChargeStatus;
	}

	@Column(name = "FIRST_SCHOOL_TIME", length = 20)
	public String getFirstSchoolTime() {
		return firstSchoolTime;
	}

	public void setFirstSchoolTime(String firstSchoolTime) {
		this.firstSchoolTime = firstSchoolTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_PRODUCT_ID")
	public ContractProduct getContractProduct() {
		return contractProduct;
	}

	public void setContractProduct(ContractProduct contractProduct) {
		this.contractProduct = contractProduct;
	}

	@Version
	@Column(name = "VERSION", nullable=false,unique=true)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}