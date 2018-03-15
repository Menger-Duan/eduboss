package com.eduboss.domain;

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
import com.eduboss.common.OtmClassAttendanceStatus;
import com.eduboss.common.OtmClassStudentChargeStatus;

/**
 * 
 * OtmClassStudentAttendent Entity @author lixuejun
 *
 */
@Entity
@Table(name = "otm_class_student_attendent")
public class OtmClassStudentAttendent implements java.io.Serializable {

	// Fields
	
	private String id;
	private OtmClassCourse otmClassCourse;
	private String studentId;
	private String courseDateTime;
	private User attendentUser;
	private OtmClassStudentChargeStatus chargeStatus;
	private OtmClassAttendanceStatus otmClassAttendanceStatus;
	private BaseStatus hasTeacherAttendance;
	private User studyManager;
	private String createTime;
	private User createUser;
	private String modifyTime;
	private User modifyUser;
	
	
	// Constructors

	/** default constructor */
	public OtmClassStudentAttendent() {
		super();
	}

	/** minimal constructor */
	public OtmClassStudentAttendent(String id, OtmClassCourse otmClassCourse) {
		super();
		this.id = id;
		this.otmClassCourse = otmClassCourse;
	}

	/** full constructor */
	public OtmClassStudentAttendent(String id, OtmClassCourse otmClassCourse,
			String studentId, String courseDateTime, User attendentUser,
			OtmClassStudentChargeStatus chargeStatus,
			OtmClassAttendanceStatus otmClassAttendanceStatus,
			BaseStatus hasTeacherAttendance, String createTime,
			User createUser, String modifyTime, User modifyUser) {
		super();
		this.id = id;
		this.otmClassCourse = otmClassCourse;
		this.studentId = studentId;
		this.courseDateTime = courseDateTime;
		this.attendentUser = attendentUser;
		this.chargeStatus = chargeStatus;
		this.otmClassAttendanceStatus = otmClassAttendanceStatus;
		this.hasTeacherAttendance = hasTeacherAttendance;
		this.createTime = createTime;
		this.createUser = createUser;
		this.modifyTime = modifyTime;
		this.modifyUser = modifyUser;
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OTM_CLASS_COURSE_ID")
	public OtmClassCourse getOtmClassCourse() {
		return otmClassCourse;
	}

	public void setOtmClassCourse(OtmClassCourse otmClassCourse) {
		this.otmClassCourse = otmClassCourse;
	}

	@Column(name = "STUDENT_ID", length = 32)
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Column(name = "COURSE_DATE_TIME", length = 20)
	public String getCourseDateTime() {
		return courseDateTime;
	}

	public void setCourseDateTime(String courseDateTime) {
		this.courseDateTime = courseDateTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTENDENT_USER_ID")
	public User getAttendentUser() {
		return attendentUser;
	}

	public void setAttendentUser(User attendentUser) {
		this.attendentUser = attendentUser;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "CHARGE_STATUS", length = 32)
	public OtmClassStudentChargeStatus getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(OtmClassStudentChargeStatus chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ATTENDENT_STATUS", length = 32)
	public OtmClassAttendanceStatus getOtmClassAttendanceStatus() {
		return otmClassAttendanceStatus;
	}

	public void setOtmClassAttendanceStatus(
			OtmClassAttendanceStatus otmClassAttendanceStatus) {
		this.otmClassAttendanceStatus = otmClassAttendanceStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "HAS_TEACHER_ATTENDANCE", length = 12)
	public BaseStatus getHasTeacherAttendance() {
		return hasTeacherAttendance;
	}

	public void setHasTeacherAttendance(BaseStatus hasTeacherAttendance) {
		this.hasTeacherAttendance = hasTeacherAttendance;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_MANAGER_ID")
	public User getStudyManager() {
		return studyManager;
	}

	public void setStudyManager(User studyManager) {
		this.studyManager = studyManager;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

//	@Column(name = "CREATE_USER_ID", length = 32)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

//	@Column(name = "MODIFY_USER_ID", length = 32)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_USER_ID")
	public User getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

}
