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

import com.eduboss.common.AttendanceType;

/**
 * StudentAttendanceRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDENT_ATTENDANCE_RECORD")
public class StudentAttendanceRecord implements java.io.Serializable {

	// Fields

	private String id;
	private Student student;
	private String courseIds;
	private AttendanceType attendanceType;
	private String attendanceNo;
	private String attendanceTime;

	// Constructors

	/** default constructor */
	public StudentAttendanceRecord() {
	}

	/** full constructor */
	public StudentAttendanceRecord(Student student, String courseIds, AttendanceType attendanceType, String attendanceNo, String attendanceTime) {
		this.student = student;
		this.courseIds = courseIds;
		this.attendanceType = attendanceType;
		this.attendanceNo = attendanceNo;
		this.attendanceTime = attendanceTime;
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
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Column(name = "COURSE_IDS", length = 512)
	public String getCourseIds() {
		return this.courseIds;
	}

	public void setCourseIds(String courseIds) {
		this.courseIds = courseIds;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ATTENDANCE_TYPE", length = 32)
	public AttendanceType getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(AttendanceType attendanceType) {
		this.attendanceType = attendanceType;
	}

	@Column(name = "ATTENDANCE_NO", length = 32)
	public String getAttendanceNo() {
		return attendanceNo;
	}

	public void setAttendanceNo(String attendanceNo) {
		this.attendanceNo = attendanceNo;
	}

	@Column(name = "ATTENDANCE_TIME", nullable = false, length = 20)
	public String getAttendanceTime() {
		return this.attendanceTime;
	}

	public void setAttendanceTime(String attendanceTime) {
		this.attendanceTime = attendanceTime;
	}

}