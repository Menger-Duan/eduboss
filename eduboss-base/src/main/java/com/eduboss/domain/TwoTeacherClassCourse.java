package com.eduboss.domain;

import com.eduboss.common.AuditStatus;
import com.eduboss.common.CourseStatus;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "TWO_TEACHER_CLASS_COURSE")
public class TwoTeacherClassCourse implements java.io.Serializable {

	private int courseId;
	private TwoTeacherClass twoTeacherClass;
	private String courseName;
	private String courseTime;
	private BigDecimal courseMinutes;// 课程分钟数
	private CourseStatus courseStatus;
	private String courseDate;
	private Double courseHours;
	private String courseEndTime;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String attendacePicName;
	private AuditStatus auditStatus;//财务审批状态
	private String teacherAttendTime;  //老师考勤时间
	private String studyManageChargeTime; //教务扣费时间
	private String firstAttendTime; //老师第一次考勤时间
	private User teacher;
	private int courseNum;//课程序号

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COURSE_ID", unique = true, nullable = false)
	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASS_ID")
	public TwoTeacherClass getTwoTeacherClass() {
		return twoTeacherClass;
	}

	public void setTwoTeacherClass(TwoTeacherClass twoTeacherClass) {
		this.twoTeacherClass = twoTeacherClass;
	}



	@Column(name = "COURSE_TIME", length = 20)
	public String getCourseTime() {
		return this.courseTime;
	}

	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}

	@Column(name = "COURSE_MINUTES", precision = 10)
	public BigDecimal getCourseMinutes() {
		return courseMinutes;
	}

	public void setCourseMinutes(BigDecimal courseMinutes) {
		this.courseMinutes = courseMinutes;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "COURSE_STATUS")
	public CourseStatus getCourseStatus() {
		return this.courseStatus;
	}

	public void setCourseStatus(CourseStatus courseStatus) {
		this.courseStatus = courseStatus;
	}


	@Column(name = "COURSE_DATE", length = 20)
	public String getCourseDate() {
		return this.courseDate;
	}

	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}

	@Column(name = "COURSE_HOURS", precision = 9)
	public Double getCourseHours() {
		return this.courseHours;
	}

	public void setCourseHours(Double courseHours) {
		this.courseHours = courseHours;
	}

	@Column(name = "COURSE_END_TIME", length = 20)
	public String getCourseEndTime() {
		return this.courseEndTime;
	}

	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
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

	@Column(name = "ATTENDANCE_PIC_NAME", length = 50)
	public String getAttendacePicName() {
		return attendacePicName;
	}

	public void setAttendacePicName(String attendacePicName) {
		this.attendacePicName = attendacePicName;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "AUDIT_STATUS", length = 32)
	public AuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(AuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	@Column(name = "TEACHING_ATTEND_TIME", length = 20)
	public String getTeacherAttendTime() {
		return teacherAttendTime;
	}

	public void setTeacherAttendTime(String teacherAttendTime) {
		this.teacherAttendTime = teacherAttendTime;
	}

	@Column(name = "STUDY_MANAGER_CHARGE_TIME", length = 20)
	public String getStudyManageChargeTime() {
		return studyManageChargeTime;
	}

	public void setStudyManageChargeTime(String studyManageChargeTime) {
		this.studyManageChargeTime = studyManageChargeTime;
	}

	@Column(name = "FIRST_ATTENDENT_TIME", length = 20)	
	public String getFirstAttendTime() {
		return firstAttendTime;
	}

	public void setFirstAttendTime(String firstAttendTime) {
		this.firstAttendTime = firstAttendTime;
	}

	@Column(name = "COURSE_NAME", length = 50)
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Column(name = "COURSE_NUM")
	public int getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id")
	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}
}