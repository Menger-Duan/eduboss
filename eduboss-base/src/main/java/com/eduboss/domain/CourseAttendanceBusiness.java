package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COURSE_ATTENDANCE_BUSINESS")
public class CourseAttendanceBusiness {

	private int id;

	public CourseAttendanceBusiness() {
		super();
	}

	public CourseAttendanceBusiness(int id) {
		super();
		this.id = id;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
