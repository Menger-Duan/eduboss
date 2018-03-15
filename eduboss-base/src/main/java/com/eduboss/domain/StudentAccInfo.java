package com.eduboss.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "student_acc_info")
public class StudentAccInfo implements java.io.Serializable {

	// Fields

	private String id;
	private Student student;
	private BigDecimal accountAmount;
	private int version;
	private String createTime;
	private String modifyTime;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", insertable=true, updatable=false)
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}


	@Column(name = "create_time", insertable=false, updatable=false)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "modify_time", insertable=false, updatable=false)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "account_amount", precision = 10)
	public BigDecimal getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(BigDecimal accountAmount) {
		this.accountAmount = accountAmount;
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