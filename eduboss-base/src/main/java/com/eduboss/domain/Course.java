package com.eduboss.domain;


import com.eduboss.common.AttendanceDetailsType;
import com.eduboss.common.AuditStatus;
import com.eduboss.common.CourseAttenceType;
import com.eduboss.common.CourseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Course entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "course")
public class Course implements java.io.Serializable {

	// Fields

	private String courseId; 
	private CourseSummary courseSummary;
	private DataDict subject;
    private DataDict grade;
    private String courseDate;
    private String courseTime;
	private BigDecimal courseMinutes;// 课程分钟数
    private Student student;
    private User teacher;
    private User arranger;
    private User studyManager;
    private BigDecimal planHours;
    private BigDecimal realHours;
    private BigDecimal auditHours;
    private CourseStatus courseStatus;
    private String studentAttendTime;
	private String teachingAttendTime;
	private String teachingManagerAuditId;
	private String teachingManagerAuditTime;
	private String studyManagerAuditId;
	private String studyManagerAuditTime;
	private String createTime;
	private User createUser;
	private String modifyTime;
	private User modifyUser;
	private String studentCrashInfo;
	private String teacherCrashInfo;
	private Organization blCampusId;
    private Product product;
	private String attendacePicName; // 考勤图片
	
	private String firstAttendentTime;//第一次考勤时间
	
	private int version;//hibernate 乐观锁
	
    private AuditStatus auditStatus;//财务审批状态
	
	private Set<CourseAttendanceRecord> courseAttendanceRecords = new HashSet<CourseAttendanceRecord>();
	// Constructors
	
	//一对一考勤类型
	private CourseAttenceType courseAttenceType;
	
	private int cVersion;//课程版本
	
	private AttendanceDetailsType attendanceDetail;

	/** default constructor */
	public Course() {
	}
	
	public Course(String courseId) {
		this.courseId = courseId;
	}

	/** full constructor */
	public Course(CourseSummary courseSummary, DataDict subject, DataDict grade, String courseDate, String courseTime,BigDecimal courseMinutes ,Student student,
			User teacher, User arranger, User studyManager, BigDecimal planHours, BigDecimal realHours, BigDecimal auditHours,
			CourseStatus courseStatus, String studentAttendTime, String teachingAttendTime,  String createTime, User createUser, String modifyTime, User modifyUser,
			String studentCrashInfo, String teacherCrashInfo) {
		this.courseSummary = courseSummary;
		this.subject = subject;
		this.grade = grade;
		this.courseDate = courseDate;
		this.courseTime = courseTime;
		this.courseMinutes = courseMinutes;
		this.student = student;
		this.teacher = teacher;
		this.arranger = arranger;
		this.studyManager = studyManager;
		this.planHours = planHours;
		this.realHours = realHours;
		this.auditHours = auditHours;
		this.courseStatus = courseStatus;
		this.studentAttendTime = studentAttendTime;
		this.teachingAttendTime = teachingAttendTime;
		this.createTime = createTime;
		this.createUser = createUser;
		this.modifyTime = modifyTime;
		this.modifyUser = modifyUser;
		this.studentCrashInfo = studentCrashInfo;
		this.teacherCrashInfo = teacherCrashInfo;
	}

	// Property accessors
	@Id
//	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
//	@GeneratedValue(generator = "generator")
	@Column(name = "COURSE_ID", unique = true, nullable = false, length = 32)
	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	@Version
	@Column(name = "VERSION", nullable=false,unique=true)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COURSE_SUMMARY_ID")
	public CourseSummary getCourseSummary() {
		return this.courseSummary;
	}

	public void setCourseSummary(CourseSummary courseSummary) {
		this.courseSummary = courseSummary;
	}

//	@Column(name = "SUBJECT", length = 8)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT")
	public DataDict getSubject() {
		return this.subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}

//	@Column(name = "GRADE", length = 8)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE")
	public DataDict getGrade() {
		return this.grade;
	}

	public void setGrade(DataDict grade) {
		this.grade = grade;
	}

	@Column(name = "COURSE_DATE", length = 10)
	public String getCourseDate() {
		return this.courseDate;
	}

	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
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

	//	@Column(name = "STUDENT_ID", length = 32)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

//	@Column(name = "TEACHER_ID", length = 32)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return this.teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

//	@Column(name = "ARRANGER_ID", length = 32)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARRANGER_ID")
	public User getArranger() {
		return this.arranger;
	}

	public void setArranger(User arranger) {
		this.arranger = arranger;
	}

//	@Column(name = "STUDY_MANAGER_ID", length = 32)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_MANAGER_ID")
	public User getStudyManager() {
		return this.studyManager;
	}

	public void setStudyManager(User studyManager) {
		this.studyManager = studyManager;
	}

	@Column(name = "PLAN_HOURS", precision = 4)
	public BigDecimal getPlanHours() {
		return this.planHours;
	}

	public void setPlanHours(BigDecimal planHours) {
		this.planHours = planHours;
	}

	@Column(name = "REAL_HOURS", precision = 4)
	public BigDecimal getRealHours() {
		return this.realHours;
	}

	public void setRealHours(BigDecimal realHours) {
		this.realHours = realHours;
	}

	@Column(name = "AUDIT_HOURS", precision = 4)
	public BigDecimal getAuditHours() {
		return this.auditHours;
	}

	public void setAuditHours(BigDecimal auditHours) {
		this.auditHours = auditHours;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "COURSE_STATUS", length = 32)
//	@Column(name = "COURSE_STATUS", length = 32)
	public CourseStatus getCourseStatus() {
		return this.courseStatus;
	}

	public void setCourseStatus(CourseStatus courseStatus) {
		this.courseStatus = courseStatus;
	}

	@Column(name = "STUDENT_ATTEND_TIME", length = 20)
	public String getStudentAttendTime() {
		return this.studentAttendTime;
	}

	public void setStudentAttendTime(String studentAttendTime) {
		this.studentAttendTime = studentAttendTime;
	}

	@Column(name = "TEACHING_ATTEND_TIME", length = 20)
	public String getTeachingAttendTime() {
		return this.teachingAttendTime;
	}

	public void setTeachingAttendTime(String teachingAttendTime) {
		this.teachingAttendTime = teachingAttendTime;
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

	@Column(name = "STUDENT_CRASH_INFO", length = 128)
	public String getStudentCrashInfo() {
		return this.studentCrashInfo;
	}

	public void setStudentCrashInfo(String studentCrashInfo) {
		this.studentCrashInfo = studentCrashInfo;
	}

	@Column(name = "TEACHER_CRASH_INFO", length = 128)
	public String getTeacherCrashInfo() {
		return this.teacherCrashInfo;
	}

	public void setTeacherCrashInfo(String teacherCrashInfo) {
		this.teacherCrashInfo = teacherCrashInfo;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
	public Set<CourseAttendanceRecord> getCourseAttendanceRecords() {
		return courseAttendanceRecords;
	}

	public void setCourseAttendanceRecords(
			Set<CourseAttendanceRecord> courseAttendanceRecords) {
		this.courseAttendanceRecords = courseAttendanceRecords;
	}

	@Column(name = "TEACHING_MANAGER_AUDIT_ID", length = 32)
	public String getTeachingManagerAuditId() {
		return this.teachingManagerAuditId;
	}

	public void setTeachingManagerAuditId(String teachingManagerAuditId) {
		this.teachingManagerAuditId = teachingManagerAuditId;
	}

	@Column(name = "TEACHING_MANAGER_AUDIT_TIME", length = 20)
	public String getTeachingManagerAuditTime() {
		return this.teachingManagerAuditTime;
	}

	public void setTeachingManagerAuditTime(String teachingManagerAuditTime) {
		this.teachingManagerAuditTime = teachingManagerAuditTime;
	}

	@Column(name = "STADUY_MANAGER_AUDIT_ID", length = 32)
	public String getStudyManagerAuditId() {
		return studyManagerAuditId;
	}

	public void setStudyManagerAuditId(String studyManagerAuditId) {
		this.studyManagerAuditId = studyManagerAuditId;
	}

	@Column(name = "STADUY_MANAGER_AUDIT_TIME", length = 20)
	public String getStudyManagerAuditTime() {
		return studyManagerAuditTime;
	}

	public void setStudyManagerAuditTime(String studyManagerAuditTime) {
		this.studyManagerAuditTime = studyManagerAuditTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BL_CAMPUS_ID")
	public Organization getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(Organization blCampusId) {
		this.blCampusId = blCampusId;
	}

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PRODUCT_ID")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

	@Column(name = "FIRST_ATTENDENT_TIME", length = 20)
	public String getFirstAttendentTime() {
		return firstAttendentTime;
	}

	public void setFirstAttendentTime(String firstAttendentTime) {
		this.firstAttendentTime = firstAttendentTime;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "COURSE_ATTENCE_TYPE", length = 32)
	public CourseAttenceType getCourseAttenceType() {
		return courseAttenceType;
	}

	public void setCourseAttenceType(CourseAttenceType courseAttenceType) {
		this.courseAttenceType = courseAttenceType;
	}

	@Column(name = "course_version")
	public int getcVersion() {
		return cVersion;
	}

	public void setcVersion(int cVersion) {
		this.cVersion = cVersion;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ATTENDANCE_DETAIL", length = 32)
	public AttendanceDetailsType getAttendanceDetail() {
		return attendanceDetail;
	}

	public void setAttendanceDetail(AttendanceDetailsType attendanceDetail) {
		this.attendanceDetail = attendanceDetail;
	}


    
	
	
	
}