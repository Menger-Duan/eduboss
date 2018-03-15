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

import com.eduboss.common.AuditStatus;
import com.eduboss.common.CourseStatus;

import java.math.BigDecimal;

/**
 * 
 * OtmClass Entity @author lixuejun
 *
 */
@Entity
@Table(name = "otm_class_course")
public class OtmClassCourse implements java.io.Serializable {

	private String otmClassCourseId;
	private OtmClass otmClass; // 一对多班级ID
	private String otmClassName; // 一对多班级名称
	private String courseDate; // 课程日期
	private String courseTime; // 课程时间
	private BigDecimal courseMinutes;// 课程分钟数
	private CourseStatus courseStatus;
	private String courseEndTime; // 课程结束时间
	private Double courseHours; // 课时数
	private User teacher;
	private DataDict subject;
	private DataDict grade;
	private String attendacePicName; // 课程考勤图片
	private AuditStatus auditStatus;//财务审批状态
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	
	private String teacherAttendTime;  //老师考勤时间
	private String studyManageChargeTime; //教务扣费时间
	
	private String firstAttendTime; //老师第一次考勤时间
	/** default constructor */
	public OtmClassCourse() {
		super();
	}
	
	/** default constructor */
	public OtmClassCourse(String otmClassCourseId) {
		super();
		this.otmClassCourseId = otmClassCourseId;
	}

	public OtmClassCourse(String otmClassCourseId, OtmClass otmClass) {
		super();
		this.otmClassCourseId = otmClassCourseId;
		this.otmClass = otmClass;
	}

	/** full constructor */
	public OtmClassCourse(String otmClassCourseId, OtmClass otmClass,
			String otmClassName, String courseDate, String courseTime,BigDecimal courseMinutes, CourseStatus courseStatus,
			String courseEndTime, Double courseHours, User teacher, DataDict subject,
			DataDict grade, String attendacePicName, AuditStatus auditStatus,
			String createTime, String createUserId, String modifyTime,
			String modifyUserId) {
		super();
		this.otmClassCourseId = otmClassCourseId;
		this.otmClass = otmClass;
		this.otmClassName = otmClassName;
		this.courseDate = courseDate;
		this.courseTime = courseTime;
		this.courseMinutes = courseMinutes;
		this.courseStatus = courseStatus;
		this.courseEndTime = courseEndTime;
		this.courseHours = courseHours;
		this.teacher = teacher;
		this.subject = subject;
		this.grade = grade;
		this.attendacePicName = attendacePicName;
		this.auditStatus = auditStatus;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
	}
	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "OTM_CLASS_COURSE_ID", unique = true, nullable = false, length = 32)
	public String getOtmClassCourseId() {
		return otmClassCourseId;
	}
	public void setOtmClassCourseId(String otmClassCourseId) {
		this.otmClassCourseId = otmClassCourseId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OTM_CLASS_ID")
	public OtmClass getOtmClass() {
		return otmClass;
	}
	public void setOtmClass(OtmClass otmClass) {
		this.otmClass = otmClass;
	}
	
	@Column(name = "OTM_CLASS_NAME", length = 32)
	public String getOtmClassName() {
		return otmClassName;
	}
	public void setOtmClassName(String otmClassName) {
		this.otmClassName = otmClassName;
	}
	
	@Column(name = "COURSE_DATE", length = 20)
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	
	@Column(name = "COURSE_TIME", length = 20)
	public String getCourseTime() {
		return courseTime;
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
	
	@Column(name = "COURSE_END_TIME", length = 20)
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}
	
	@Column(name = "COURSE_HOURS", precision = 9)
	public Double getCourseHours() {
		return this.courseHours;
	}
	public void setCourseHours(Double courseHours) {
		this.courseHours = courseHours;
	}
	
	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	public User getTeacher() {
		return teacher;
	}
	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT")
	public DataDict getSubject() {
		return subject;
	}
	public void setSubject(DataDict subject) {
		this.subject = subject;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE")
	public DataDict getGrade() {
		return grade;
	}
	public void setGrade(DataDict grade) {
		this.grade = grade;
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
	
	

	
}
