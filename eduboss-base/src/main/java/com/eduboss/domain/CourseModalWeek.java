package com.eduboss.domain;

import com.eduboss.common.WeekDay;

import javax.persistence.*;


@Entity
@Table(name = "course_modal_week")
public class CourseModalWeek implements java.io.Serializable {

	// Fields
	private int id;
	private CourseModal courseModal;
	private WeekDay courseWeek; //周数

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Enumerated(EnumType.STRING)
	@Column(name = "COURSE_WEEK", length = 32)
	public WeekDay getCourseWeek() {
		return courseWeek;
	}

	public void setCourseWeek(WeekDay courseWeek) {
		this.courseWeek = courseWeek;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "modal_id")
	public CourseModal getCourseModal() {
		return courseModal;
	}

	public void setCourseModal(CourseModal courseModal) {
		this.courseModal = courseModal;
	}
}