package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.common.RoleCode;
import com.eduboss.domain.CourseAttendanceRecord;

@Repository
public interface CourseAttendanceRecordDao extends GenericDAO<CourseAttendanceRecord, String> {
	
	public void save(CourseAttendanceRecord courseAttendanceRecord);

	/**
	 * 查找课程ID相关的所有记录
	 * @param courseId
	 * @return
	 */
	public List<CourseAttendanceRecord> getCourseAttendanceRecordListByCourseId(String courseId);

	/**
	 * 查找课程ID、角色相关的某一记录
	 * @param courseId
	 * @param courseStatus
	 * @return
	 */
	public CourseAttendanceRecord getCourseAttendanceRecordByCourseIdAndRole(
			String courseId, RoleCode roleCode);
	
}
