package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.TeacherSubjectVersion;

/**
 * 2016-12-15
 * @author lixuejun
 *
 */
public interface TeacherSubjectVersionDao extends GenericDAO<TeacherSubjectVersion, Integer> {

	/**
	 * 查找月份有效的老师科目
	 * @param subjectId
	 * @param versionMonth
	 * @return
	 */
	List<Map<Object, Object>> findTeacherSubjectVersionBySubject(String campusId, String subjectId, int versionMonth);
	
	/**
	 * 根据老师和年级获取当前可教老师版本
	 * @param teacherId
	 * @param gradeId
	 * @return
	 */
	List<TeacherSubjectVersion> getCanTaughtSubjectByTeacherAndGrade(String teacherId, String gradeId);
	
}
