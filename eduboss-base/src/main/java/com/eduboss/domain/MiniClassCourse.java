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
 * MiniClassCourse entity. @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "mini_class_course")
public class MiniClassCourse implements java.io.Serializable {

	// Fields

	private String miniClassCourseId;
	private MiniClass miniClass;
	private String courseTime;
	private BigDecimal courseMinutes;// 课程分钟数
	private CourseStatus courseStatus;
	private String miniClassName;
	private String courseDate;
	private Double courseHours;
	private String courseEndTime;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private DataDict subject;
    private DataDict grade;
	private User teacher;
	private User studyHead;  // 班主任
//	private Set miniClassRecords = new HashSet(0);
//	private Set miniClassStudentAttendents = new HashSet(0);
	private ClassroomManage classroom;

	private String attendacePicName;
	// Constructors
	
	private AuditStatus auditStatus;//财务审批状态
	
	private String teacherAttendTime;  //老师考勤时间
	private String studyManageChargeTime; //教务扣费时间
	private String firstAttendTime; //老师第一次考勤时间

	private String courseName;
	private int courseNum;//课程序号

	/** default constructor */
	public MiniClassCourse() {
	}
	
	/** default constructor */
	public MiniClassCourse(String miniClassCourseId) {
		this.miniClassCourseId = miniClassCourseId;
	}

	/** minimal constructor */
	public MiniClassCourse(String miniClassCourseId, MiniClass miniClass) {
		this.miniClassCourseId = miniClassCourseId;
		this.miniClass = miniClass;
	}

	/** full constructor */
	public MiniClassCourse(String miniClassCourseId, MiniClass miniClass,
			String courseTime,BigDecimal courseMinutes , CourseStatus courseStatus, String miniClassName,
			String courseDate, Double courseHours, String courseEndTime,
			String createTime, String createUserId, String modifyTime, String modifyUserId) {
		this.miniClassCourseId = miniClassCourseId;
		this.miniClass = miniClass;
		this.courseTime = courseTime;
		this.courseMinutes = courseMinutes;
		this.courseStatus = courseStatus;
		this.miniClassName = miniClassName;
		this.courseDate = courseDate;
		this.courseHours = courseHours;
		this.courseEndTime = courseEndTime;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
//		this.miniClassRecords = miniClassRecords;
//		this.miniClassStudentAttendents = miniClassStudentAttendents;
	}

	// Property accessors

	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "MINI_CLASS_COURSE_ID", unique = true, nullable = false, length = 32)
	public String getMiniClassCourseId() {
		return this.miniClassCourseId;
	}

	public void setMiniClassCourseId(String miniClassCourseId) {
		this.miniClassCourseId = miniClassCourseId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MINI_CLASS_ID")
	public MiniClass getMiniClass() {
		return this.miniClass;
	}

	public void setMiniClass(MiniClass miniClass) {
		this.miniClass = miniClass;
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

	@Column(name = "MINI_CLASS_NAME", length = 32)
	public String getMiniClassName() {
		return this.miniClassName;
	}

	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
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

	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}
	
	@ManyToOne
	@JoinColumn(name="STUDY_MANEGER_ID")
	public User getStudyHead() {
		return studyHead;
	}

	public void setStudyHead(User studyHead) {
		this.studyHead = studyHead;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT")
	public DataDict getSubject() {
		return this.subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE")
	public DataDict getGrade() {
		return this.grade;
	}

	public void setGrade(DataDict grade) {
		this.grade = grade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASSROOM_ID")
	public ClassroomManage getClassroom() {
		return classroom;
	}

	public void setClassroom(ClassroomManage classroom) {
		this.classroom = classroom;
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

	//	public Set getMiniClassRecords() {
//		return this.miniClassRecords;
//	}
//
//	public void setMiniClassRecords(Set miniClassRecords) {
//		this.miniClassRecords = miniClassRecords;
//	}
//
//	public Set getMiniClassStudentAttendents() {
//		return this.miniClassStudentAttendents;
//	}
//
//	public void setMiniClassStudentAttendents(Set miniClassStudentAttendents) {
//		this.miniClassStudentAttendents = miniClassStudentAttendents;
//	}

}