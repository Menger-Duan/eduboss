package com.eduboss.domain;

import javax.persistence.*;

import com.eduboss.common.OtmClassStatus;
import com.eduboss.common.OtmClassStudentChargeStatus;

/**
 * 
 * OtmClass Entity @author lixuejun
 *
 */
@Entity
@Table(name = "otm_class_student")
public class OtmClassStudent implements java.io.Serializable {

	private int id;
	private OtmClass otmClass;
	private Student student;
	private String signUpDate; // 报班日期
	private OtmClassStatus otmClassStudyStatus;
	private OtmClassStudentChargeStatus otmClassStudentChargeStatus;
	private String firstSchoolTime; //首次上课时间
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	
	/** default constructor */
	public OtmClassStudent() {
		super();
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
	@JoinColumn(name = "OTM_CLASS_ID")
	public OtmClass getOtmClass() {
		return otmClass;
	}
	public void setOtmClass(OtmClass otmClass) {
		this.otmClass = otmClass;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	@Column(name = "SIGN_UP_DATE", length = 20)
	public String getSignUpDate() {
		return signUpDate;
	}
	public void setSignUpDate(String signUpDate) {
		this.signUpDate = signUpDate;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "OTM_CLASS_STUDY_STATUS", length = 32)
	public OtmClassStatus getOtmClassStudyStatus() {
		return otmClassStudyStatus;
	}
	public void setOtmClassStudyStatus(OtmClassStatus otmClassStudyStatus) {
		this.otmClassStudyStatus = otmClassStudyStatus;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "OTM_CLASS_CHARGE_STATUS", length = 32)
	public OtmClassStudentChargeStatus getOtmClassStudentChargeStatus() {
		return otmClassStudentChargeStatus;
	}
	public void setOtmClassStudentChargeStatus(
			OtmClassStudentChargeStatus otmClassStudentChargeStatus) {
		this.otmClassStudentChargeStatus = otmClassStudentChargeStatus;
	}
	
	@Column(name = "FIRST_SCHOOL_TIME", length = 20)
	public String getFirstSchoolTime() {
		return firstSchoolTime;
	}
	public void setFirstSchoolTime(String firstSchoolTime) {
		this.firstSchoolTime = firstSchoolTime;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	
	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
}
