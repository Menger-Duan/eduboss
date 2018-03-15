package com.eduboss.dto;

import java.util.List;

import com.eduboss.domain.Course;
import com.eduboss.domain.CourseSummary;

public class CourseGenerationRequest {
	
	private CourseSummary courseSummary;
	private List<Course> courseList;

	/**
	 * @return the courseList
	 */
	public List<Course> getCourseList() {
		return courseList;
	}

	/**
	 * @param courseList the courseList to set
	 */
	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}

	/**
	 * @return the courseSummary
	 */
	public CourseSummary getCourseSummary() {
		return courseSummary;
	}

	/**
	 * @param courseSummary the courseSummary to set
	 */
	public void setCourseSummary(CourseSummary courseSummary) {
		this.courseSummary = courseSummary;
	}
	
}
