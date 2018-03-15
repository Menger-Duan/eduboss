package com.eduboss.dto;

import com.eduboss.domain.Course;

/**
 * 专门用于一对一排课星期都选的课程Vo
 * @author lixuejun
 *
 */
public class MutilCourseVo {
	
	private String weekDay; 
	private Course[] courseArr;
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public Course[] getCourseArr() {
		return courseArr;
	}
	public void setCourseArr(Course[] courseArr) {
		this.courseArr = courseArr;
	}
	
}
