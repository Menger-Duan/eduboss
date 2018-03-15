package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "course_conflict")
public class CourseConflict {
	private Long id;
	private String courseId;
	private MiniClass miniClass;
	private Long startTime;
	private Long endTime;
	private User teacher;
	private Student student;
	private String blCampusId;
	private OtmClass otmClass;
	private Integer classTwoId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(name = "course_id")
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	// @Column(name = "mini_class_id")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mini_class_id")
	public MiniClass getMiniClass() {
		return miniClass;
	}

	public void setMiniClass(MiniClass miniClass) {
		this.miniClass = miniClass;
	}

	@Column(name = "start_time")
	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time")
	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	// @Column(name = "teacher_id")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "teacher_id")
	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	// @Column(name = "student_id")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "student_id")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Column(name = "bl_campus_id")
	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "otm_class_id")
	public OtmClass getOtmClass() {
		return otmClass;
	}

	public void setOtmClass(OtmClass otmClass) {
		this.otmClass = otmClass;
	}

	@Column(name = "class_two_id")
    public Integer getClassTwoId() {
        return classTwoId;
    }

    public void setClassTwoId(Integer classTwoId) {
        this.classTwoId = classTwoId;
    }

}
