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

import com.eduboss.common.BaseStatus;
import com.eduboss.common.LectureClassAttendanceStatus;
import com.eduboss.common.LectureClassStatus;
import com.eduboss.common.LectureClassStudentChargeStatus;

@Entity
@Table(name = "LECTURE_CLASS_STUDENT")
public class LectureClassStudent implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private LectureClass lectureClass;
	private Student student;
	private LectureClassStudentChargeStatus chargeStatus;
	private LectureClassAttendanceStatus auditStatus;
	private BaseStatus hasTeacherAttendance;
	private BigDecimal auditHours;
	private User teacher;
	private User chargeUser;
	private String auditTime;
	private String chargeTime;
	private String createTime;
	private String createUser;
	private String modifyTime;
	private String modifyUser;
	
	private ContractProduct contractProduct;
	
	

	public LectureClassStudent() {
	}

	@Id
	@GenericGenerator(name="generator", strategy="uuid.hex")
	@GeneratedValue(generator="generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LECTURE_ID")
	public LectureClass getLectureClass() {
		return lectureClass;
	}

	public void setLectureClass(LectureClass lectureClass) {
		this.lectureClass = lectureClass;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="CHARGE_STATUS")
	public LectureClassStudentChargeStatus getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(LectureClassStudentChargeStatus chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="AUDIT_STATUS")
	public LectureClassAttendanceStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(LectureClassAttendanceStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="HAS_TEACHER_ATTENDANCE")
	public BaseStatus getHasTeacherAttendance() {
		return hasTeacherAttendance;
	}

	public void setHasTeacherAttendance(BaseStatus hasTeacherAttendance) {
		this.hasTeacherAttendance = hasTeacherAttendance;
	}

	@Column(name = "AUDIT_HOURS")
	public BigDecimal getAuditHours() {
		return auditHours;
	}

	public void setAuditHours(BigDecimal auditHours) {
		this.auditHours = auditHours;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CHARGE_USER_ID")
	public User getChargeUser() {
		return chargeUser;
	}

	public void setChargeUser(User chargeUser) {
		this.chargeUser = chargeUser;
	}

	@Column(name = "AUDIT_TIME", length = 20)
	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	@Column(name = "CHARGE_TIME", length = 20)
	public String getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(String chargeTime) {
		this.chargeTime = chargeTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return this.teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER", length = 32)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER", length = 32)
	public String getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_PRODUCT_ID")
	public ContractProduct getContractProduct() {
		return contractProduct;
	}

	public void setContractProduct(ContractProduct contractProduct) {
		this.contractProduct = contractProduct;
	}
	
}