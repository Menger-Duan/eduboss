package com.eduboss.domain;

import javax.persistence.*;

@Entity
@Table(name = "TWO_TEACHER_CLASS_STUDENT")
public class TwoTeacherClassStudent implements java.io.Serializable {


	private int id;
	private TwoTeacherClassTwo classTwo;
	private Student student;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private ContractProduct contractProduct;
	private String firstSchoolTime;//首次上课时间


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASS_TWO_ID", nullable = false)
	public TwoTeacherClassTwo getClassTwo() {
		return classTwo;
	}

	public void setClassTwo(TwoTeacherClassTwo classTwo) {
		this.classTwo = classTwo;
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
	@JoinColumn(name = "STUDENT_ID", nullable = false)
	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
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
}