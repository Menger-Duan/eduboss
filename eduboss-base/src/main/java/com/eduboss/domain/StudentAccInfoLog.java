package com.eduboss.domain;

import com.eduboss.common.ElectronicAccChangeType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "student_acc_info_log")
public class StudentAccInfoLog implements java.io.Serializable {

	// Fields

	private String id;
	private Student student;
	private ElectronicAccChangeType type;
	private BigDecimal changeAmount;
	private BigDecimal afterAmount;
	private int logStatus;
	private Student fromStudent;
	private String createTime;
	private String modifyTime;
	private String createUserId;
	private String modifyUserId;
	private String remark;
	private User user;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "CHANGE_AMOUNT", precision = 10)
	public BigDecimal getChangeAmount() {
		return this.changeAmount;
	}

	public void setChangeAmount(BigDecimal changeAmount) {
		this.changeAmount = changeAmount;
	}

	@Column(name = "AFTER_AMOUNT", precision = 10)
	public BigDecimal getAfterAmount() {
		return this.afterAmount;
	}

	public void setAfterAmount(BigDecimal afterAmount) {
		this.afterAmount = afterAmount;
	}

	@Column(name = "REMARK", length = 20)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE", length = 32)
	public ElectronicAccChangeType getType() {
		return type;
	}

	public void setType(ElectronicAccChangeType type) {
		this.type = type;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", insertable=true, updatable=false)
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Column(name = "log_status")
	public int getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(int logStatus) {
		this.logStatus = logStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_student_id", insertable=true, updatable=false)
	public Student getFromStudent() {
		return fromStudent;
	}

	public void setFromStudent(Student fromStudent) {
		this.fromStudent = fromStudent;
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

	@Column(name = "create_user_id", insertable=true, updatable=false)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "modify_user_id")
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sign_staff_id", insertable=true, updatable=false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}