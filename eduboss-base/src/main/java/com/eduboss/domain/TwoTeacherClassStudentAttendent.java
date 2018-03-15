package com.eduboss.domain;

import com.eduboss.common.*;

import javax.persistence.*;


@Entity
@Table(name = "TWO_TEACHER_CLASS_STUDENT_ATTENDENT")
public class TwoTeacherClassStudentAttendent implements java.io.Serializable {

	private int id;
	private TwoTeacherClassCourse twoTeacherClassCourse;
	private TwoTeacherClassTwo twoTeacherClassTwo;//辅班
	private Student student;
	private String courseDateTime;
	private User attendentUser;
	private MiniClassStudentChargeStatus chargeStatus;
	private MiniClassAttendanceStatus miniClassAttendanceStatus;
	private CourseStatus courseStatus;//课程状态
	private AuditStatus auditStatus;//财务审批状态
	private BaseStatus hasTeacherAttendance;
	private String createTime;
	private User createUser;
	private String modifyTime;
	private User modifyUser;
	private String absentRemark; // 缺勤备注
	private String supplementDate; // 补课日期
	private String attendacePicName;//考勤图片地址
	private Integer version;





	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TWO_CLASS_COURSE_ID")
	public TwoTeacherClassCourse getTwoTeacherClassCourse() {
		return twoTeacherClassCourse;
	}

	public void setTwoTeacherClassCourse(TwoTeacherClassCourse twoTeacherClassCourse) {
		this.twoTeacherClassCourse = twoTeacherClassCourse;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASS_TWO_ID")
	public TwoTeacherClassTwo getTwoTeacherClassTwo() {
		return twoTeacherClassTwo;
	}

	public void setTwoTeacherClassTwo(TwoTeacherClassTwo twoTeacherClassTwo) {
		this.twoTeacherClassTwo = twoTeacherClassTwo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}



	@Column(name = "COURSE_DATE_TIME", length = 20)
	public String getCourseDateTime() {
		return this.courseDateTime;
	}

	public void setCourseDateTime(String courseDateTime) {
		this.courseDateTime = courseDateTime;
	}

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

	@Column(name = "ATTENDANCE_PIC_NAME", length = 50)
	public String getAttendacePicName() {
		return attendacePicName;
	}

	public void setAttendacePicName(String attendacePicName) {
		this.attendacePicName = attendacePicName;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "COURSE_STATUS")
	public CourseStatus getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(CourseStatus courseStatus) {
		this.courseStatus = courseStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "AUDIT_STATUS", length = 32)
	public AuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(AuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}