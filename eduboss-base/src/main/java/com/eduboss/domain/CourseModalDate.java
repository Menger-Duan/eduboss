package com.eduboss.domain;

import javax.persistence.*;


@Entity
@Table(name = "course_modal_date")
public class CourseModalDate extends BasicDomain implements java.io.Serializable {

	private int id;
	private int modalId;  //模板Id
	private String courseDate;    //课程日期

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "MODAL_ID")
	public int getModalId() {
		return modalId;
	}

	public void setModalId(int modalId) {
		this.modalId = modalId;
	}

	@Column(name = "COURSE_DATE")
	public String getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
}