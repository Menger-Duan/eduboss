package com.eduboss.domain;

import javax.persistence.*;

import com.eduboss.common.CustomerStudentStatus;

/**
 * CustomerStudent entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "customer_student_relation")
public class CustomerStudent implements java.io.Serializable {

	private static final long serialVersionUID = 5991915391232773486L;
	
	// Fields
	private int id;
	private String relation;
	private Student student;
	private Customer customer;
	private String createTime;
	private User createUser;
	private String modifyTime;
	private User modifyUser;
	private CustomerStudentStatus customerStudentStatus;
	private Boolean isDeleted;//逻辑删除学生

	// Constructors

	/** default constructor */
	public CustomerStudent() {
	}


	/** full constructor */
	public CustomerStudent(int id, String relation,
			Student student, Customer customer, String createTime,
			User createUser, String modifyTime, User modifyUser,
			CustomerStudentStatus customerStudentStatus) {
		super();
		this.id = id;
		this.relation = relation;
		this.student = student;
		this.customer = customer;
		this.createTime = createTime;
		this.createUser = createUser;
		this.modifyTime = modifyTime;
		this.modifyUser = modifyUser;
		this.customerStudentStatus = customerStudentStatus;
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

	@Column(name = "RELATION", length = 32)
	public String getRelation() {
		return this.relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CUSTOMER_ID")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_USER_ID")
	public User getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "CUSTOMER_STUDENT_STATUS", length = 32)
	public CustomerStudentStatus getCustomerStudentStatus() {
		return customerStudentStatus;
	}

	public void setCustomerStudentStatus(CustomerStudentStatus customerStudentStatus) {
		this.customerStudentStatus = customerStudentStatus;
	}
	
	@Column(name = "IS_DELETED")
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	

}