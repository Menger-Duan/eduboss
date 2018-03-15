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

import com.eduboss.common.CourseRequirementCetegory;
import com.eduboss.common.CourseRequirementStatus;

/**
 * CourseRequirement entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "course_requirement")
public class CourseRequirement implements java.io.Serializable {

	// Fields

	private String id;
	private Student student;
	private User teacher;
	private User studyManager;
	private DataDict grade;
	private DataDict subject;
	private DataDict courseTime;
	private String startDate;
	private String endDate;
	private Double courseHours;
	private String courseDateDesciption;
	private User courseArranger;
	private CourseRequirementStatus requirementStatus;
	private String lastArrangeTime;
	private String remark;
	private String createTime;
	private User createUser;
	private String modifyTime;
	private User modifyUser;
	private CourseRequirementCetegory requirementCetegory;
//	private Organization blCampusId;

	// Constructors

	/** default constructor */
	public CourseRequirement() {
	}

	/** full constructor */
	public CourseRequirement(Student student, User teacher, User studyManager, DataDict grade, DataDict subject, DataDict courseTime, String courseDateDesciption, String startDate,
			String endDate, Double courseHours, User courseArranger, CourseRequirementStatus requirementStatus, String lastArrangeTime, String remark, String createTime,
			User createUser, String modifyTime, User modifyUser) {
		this.student = student;
		this.teacher = teacher;
		this.studyManager = studyManager;
		this.grade = grade;
		this.subject = subject;
		this.courseTime = courseTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.courseHours = courseHours;
		this.courseDateDesciption = courseDateDesciption;
		this.courseArranger = courseArranger;
		this.requirementStatus = requirementStatus;
		this.lastArrangeTime = lastArrangeTime;
		this.remark = remark;
		this.createTime = createTime;
		this.createUser = createUser;
		this.modifyTime = modifyTime;
		this.modifyUser = modifyUser;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
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

//	@Column(name = "GRADE", length = 32)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE")
	public DataDict getGrade() {
		return this.grade;
	}

	public void setGrade(DataDict grade) {
		this.grade = grade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT")
//	@Column(name = "SUBJECT", length = 32)
	public DataDict getSubject() {
		return this.subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}

//	@Column(name = "COURSE_TIME", length = 32)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_TIME")
	public DataDict getCourseTime() {
		return this.courseTime;
	}

	public void setCourseTime(DataDict courseTime) {
		this.courseTime = courseTime;
	}

	@Column(name = "COURSE_DATE_DESCIPTION", length = 256)
	public String getCourseDateDesciption() {
		return courseDateDesciption;
	}

	public void setCourseDateDesciption(String courseDateDesciption) {
		this.courseDateDesciption = courseDateDesciption;
	}

	@Column(name = "START_DATE", length = 10)
	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Column(name = "END_DATE", length = 10)
	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Column(name = "COURSE_HOURS", precision = 10)
	public Double getCourseHours() {
		return this.courseHours;
	}

	public void setCourseHours(Double courseHours) {
		this.courseHours = courseHours;
	}

//	@Column(name = "COURSE_ARRANGER_ID", length = 32)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ARRANGER_ID")
	public User getCourseArranger() {
		return this.courseArranger;
	}

	public void setCourseArranger(User courseArranger) {
		this.courseArranger = courseArranger;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "REQUIREMENT_STATUS", length = 32)
	public CourseRequirementStatus getRequirementStatus() {
		return this.requirementStatus;
	}

	public void setRequirementStatus(CourseRequirementStatus requirementStatus) {
		this.requirementStatus = requirementStatus;
	}

	@Column(name = "REMARK", length = 512)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	/**
	 * @return the requirementCetegory
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "REQUIREMENT_CETEGORY", length = 32)
	public CourseRequirementCetegory getRequirementCetegory() {
		return this.requirementCetegory;
	}

	/**
	 * @param requirementCetegory the requirementCetegory to set
	 */
	public void setRequirementCetegory(CourseRequirementCetegory requirementCetegory) {
		this.requirementCetegory = requirementCetegory;
	}

	@Column(name = "LAST_ARRANGE_TIME", length = 32)
	public String getLastArrangeTime() {
		return lastArrangeTime;
	}

	public void setLastArrangeTime(String lastArrangeTime) {
		this.lastArrangeTime = lastArrangeTime;
	}

//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="BL_CAMPUS_ID")
//	public Organization getBlCampusId() {
//		return blCampusId;
//	}
//
//	public void setBlCampusId(Organization blCampusId) {
//		this.blCampusId = blCampusId;
//	}

}