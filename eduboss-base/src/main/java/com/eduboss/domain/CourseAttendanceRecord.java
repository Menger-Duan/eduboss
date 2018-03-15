package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * CourseAttendanceRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "course_attendance_record")
public class CourseAttendanceRecord implements java.io.Serializable {

	// Fields

	private String id;
	private Course course;
	private BigDecimal courseHours;
	private User checkUser;
	private Role checkUserRole;
	private String oprateTime;

	// Constructors

	/** default constructor */
	public CourseAttendanceRecord() {
	}

	/** full constructor */
	public CourseAttendanceRecord(String id, Course course,
			BigDecimal courseHours, User checkUser, Role checkUserRole,
			String oprateTime) {
		this.id = id;
		this.course = course;
		this.courseHours = courseHours;
		this.checkUser = checkUser;
		this.checkUserRole = checkUserRole;
		this.oprateTime = oprateTime;
	}

	// Property accessors
//	@Id
//	@GenericGenerator(name = "generator", strategy = "uuid.hex")
//	@GeneratedValue(generator = "generator")
//	@Column(name = "ID", unique = true, nullable = false, length = 32)
	@Id
//	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
//	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID")
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Column(name = "COURSE_HOURS", nullable = false, precision = 10)
	public BigDecimal getCourseHours() {
		return this.courseHours;
	}

	public void setCourseHours(BigDecimal courseHours) {
		this.courseHours = courseHours;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHECK_USER_ID")
	public User getCheckUser() {
		return this.checkUser;
	}

	public void setCheckUser(User checkUser) {
		this.checkUser = checkUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHECK_USER_ROLE_ID")
	public Role getCheckUserRole() {
		return this.checkUserRole;
	}

	public void setCheckUserRole(Role checkUserRole) {
		this.checkUserRole = checkUserRole;
	}

	@Column(name = "OPRATE_TIME", nullable = false, length = 20)
	public String getOprateTime() {
		return this.oprateTime;
	}

	public void setOprateTime(String oprateTime) {
		this.oprateTime = oprateTime;
	}

}