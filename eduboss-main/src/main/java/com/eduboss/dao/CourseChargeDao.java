package com.eduboss.dao;

import java.math.BigDecimal;

import com.eduboss.domain.Course;


public interface CourseChargeDao extends GenericDAO<Object, String>{
	
	
	/**
	 * “老师1对1课消 年级分布” 和 “1对1 课消科目分布”
	 * @param course
	 */
	public void teacherCourseCharge(Course course);
	
	/**
	 * 一对一审批无效操作，更新“老师1对1课消 年级分布” 和 “1对1 课消科目分布”
	 * @param course
	 */
	public void unValidateTeacherCourseCharge(Course course);
    
	/**
	 * 校区1对1课消 
	 * @param course
	 */
	public void schoolCourseCharge(Course course,BigDecimal courseAmout);
	
	



}


