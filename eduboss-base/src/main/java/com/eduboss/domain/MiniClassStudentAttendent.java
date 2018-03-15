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
import com.eduboss.common.MiniClassAttendanceStatus;
import com.eduboss.common.MiniClassStudentChargeStatus;

/**
 * MiniClassStudentAttendent entity. @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "MINI_CLASS_STUDENT_ATTENDENT")
public class MiniClassStudentAttendent implements java.io.Serializable {

	// Fields

	private String id;
	private MiniClassCourse miniClassCourse;
	private String studentId;
	private String courseDateTime;
	private User attendentUser;
	private MiniClassStudentChargeStatus chargeStatus;
	private MiniClassAttendanceStatus miniClassAttendanceStatus;
	private BaseStatus hasTeacherAttendance;
	private String createTime;
	private User createUser;
	private String modifyTime;
	private User modifyUser;
	private String absentRemark; // 缺勤备注
	private String supplementDate; // 补课日期
	
	

	// Constructors

	/** default constructor */
	public MiniClassStudentAttendent() {
	}

	/** minimal constructor */
	public MiniClassStudentAttendent(String id, MiniClassCourse miniClassCourse) {
		this.id = id;
		this.miniClassCourse = miniClassCourse;
	}

	/** full constructor */
	public MiniClassStudentAttendent(String id,
			MiniClassCourse miniClassCourse, String studentId,
			String courseDateTime, User attendentUser, MiniClassStudentChargeStatus chargeStatus,
			MiniClassAttendanceStatus miniClassAttendanceStatus, 
			String createTime, User createUser, String modifyTime,
			User modifyUser, String absentRemark, String supplementDate) {
		this.id = id;
		this.miniClassCourse = miniClassCourse;
		this.studentId = studentId;
		this.courseDateTime = courseDateTime;
		this.attendentUser = attendentUser;
		this.chargeStatus = chargeStatus;
		this.miniClassAttendanceStatus = miniClassAttendanceStatus;
		this.createTime = createTime;
		this.createUser = createUser;
		this.modifyTime = modifyTime;
		this.modifyUser = modifyUser;
		this.absentRemark = absentRemark;
		this.supplementDate = supplementDate;
	}

	// Property accessors

	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MINI_CLASS_COURSE_ID")
	public MiniClassCourse getMiniClassCourse() {
		return this.miniClassCourse;
	}

	public void setMiniClassCourse(MiniClassCourse miniClassCourse) {
		this.miniClassCourse = miniClassCourse;
	}

	@Column(name = "STUDENT_ID", length = 32)
	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Column(name = "COURSE_DATE_TIME", length = 20)
	public String getCourseDateTime() {
		return this.courseDateTime;
	}

	public void setCourseDateTime(String courseDateTime) {
		this.courseDateTime = courseDateTime;
	}

//	@Column(name = "ATTENDENT_USER_ID", length = 32)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTENDENT_USER_ID")
	public User getAttendentUser() {
		return this.attendentUser;
	}

	public void setAttendentUser(User attendentUser) {
		this.attendentUser = attendentUser;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "CHARGE_STATUS", length = 32)
	public MiniClassStudentChargeStatus getChargeStatus() {
		return this.chargeStatus;
	}

	public void setChargeStatus(MiniClassStudentChargeStatus chargeStatus) {
		this.chargeStatus = chargeStatus;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "ATTENDENT_STATUS", length = 32)
	public MiniClassAttendanceStatus getMiniClassAttendanceStatus() {
		return miniClassAttendanceStatus;
	}

	public void setMiniClassAttendanceStatus(
			MiniClassAttendanceStatus miniClassAttendanceStatus) {
		this.miniClassAttendanceStatus = miniClassAttendanceStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "HAS_TEACHER_ATTENDANCE", length = 12)
	public BaseStatus getHasTeacherAttendance() {
		return hasTeacherAttendance;
	}

	public void setHasTeacherAttendance(BaseStatus hasTeacherAttendance) {
		this.hasTeacherAttendance = hasTeacherAttendance;
	}

	@Column(name = "ABSENT_REMARK", length = 32)
	public String getAbsentRemark() {
		return absentRemark;
	}

	public void setAbsentRemark(String absentRemark) {
		this.absentRemark = absentRemark;
	}

	@Column(name = "SUPPLEMENT_DATE", length = 10)
	public String getSupplementDate() {
		return supplementDate;
	}

	public void setSupplementDate(String supplementDate) {
		this.supplementDate = supplementDate;
	}

	
}