package com.eduboss.service;

import com.eduboss.domain.TeacherFunctionVersion;

/**
 * 2016-12-22
 * @author lixuejun
 *
 */
public interface TeacherFunctionVersionService {

	/**
	 * 保存或修改教师职能关联
	 * @param teacherFunctionVersion
	 */
	void editTeacherFunctionVersion(TeacherFunctionVersion teacherFunctionVersion);
	
	/**
	 * 删除教师职能关联
	 * @param teacherFunctionVersion
	 */
	void deleteTeacherFunctionVersion(TeacherFunctionVersion teacherFunctionVersion);
	
}
