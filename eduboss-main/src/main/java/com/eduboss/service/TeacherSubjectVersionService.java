package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.TeacherSubjectVersion;

/**
 * 2016-12-17
 * @author lixuejun
 *
 */
public interface TeacherSubjectVersionService {

	/**
	 * 查找月份有效的老师科目
	 * @param subjectId
	 * @param versionMonth
	 * @return
	 */
	List<Map<Object, Object>> findTeacherSubjectVersionBySubject(String campusId, String subjectId, int versionMonth);
	
	/**
	 * 科组通过科目和月份版本查找老师名称
	 * @param subjectId
	 * @param versionMonth
	 * @return
	 */
	String findAssociatedTeacherNameDes(String campusId, String subjectId, int versionMonth);
	
	/**
	 * 保存或修改授课老师科目关联
	 * @param teacherSubjectVersion
	 */
	void editTeacherSubjectVersion(TeacherSubjectVersion teacherSubjectVersion);
	
	/**
	 * 删除授课老师科目关联
	 * @param teacherSubjectVersion
	 */
	void deleteTeacherSubjectVersion(TeacherSubjectVersion teacherSubjectVersion);
	
	/**
	 * 根据老师和年级获取当前可教老师版本
	 * @param teacherId
	 * @param gradeId
	 * @return
	 */
	List<TeacherSubjectVersion> getCanTaughtSubjectByTeacherAndGrade(String teacherId, String gradeId);
	
}
