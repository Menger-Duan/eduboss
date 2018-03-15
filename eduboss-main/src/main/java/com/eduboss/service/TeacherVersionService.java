package com.eduboss.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduboss.domain.DataDict;
import com.eduboss.domain.TeacherVersion;
import com.eduboss.domainVo.TeacherVersionVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;

/**
 * 2016-12-20
 * @author lixuejun
 *
 */
public interface TeacherVersionService {

	/**
	 * 保存或修改编制老师版本
	 * @param teacherVersionVo
	 */
	void editTeacherVersion(TeacherVersionVo teacherVersionVo) throws ApplicationException;
	
	/**
	 * 分页查找已编制教师
	 * @param teacherVersionVo
	 * @param dp
	 * @return
	 */
	DataPackage getPageTeacherVersion(TeacherVersionVo teacherVersionVo, DataPackage dp);
	
	/**
	 * 根据老师ID查找所有版本的teacherVersion
	 * @param teacherId
	 * @return
	 */
	List<TeacherVersion> getTeacherVersionList(String teacherId);
	
	/**
	 * 根据id查找老师版本
	 * @param teacherVersionId
	 * @param versionDate
	 * @return
	 */
	TeacherVersionVo findTeacherVersionById(int teacherVersionId, String versionDate);
	
	/**
	 * 根据科目、年级和校区级别查询可教老师
	 * @param gradeId
	 * @param subjetId
	 * @param isOtherOrganizationTeacher
	 * @param campusId
	 * @return
	 */
	List<Map<Object, Object>> getTeacherByGradeSubject(String gradeId, String subjetId, Boolean isOtherOrganizationTeacher, String campusId);
	
	/**
	 * 根据学生、科目和校区级别查询可教老师
	 * @param studentId
	 * @param subjetId
	 * @param isOtherOrganizationTeacher
	 * @param campusId
	 * @return
	 */
	List<Map<Object, Object>> getTeacherByStudentSubject(String studentId, String subjetId, Boolean isOtherOrganizationTeacher, String campusId);
	
	/**
	 * 根据老师和年级获取可教科目
	 * @param teacherId
	 * @param gradeId
	 * @return
	 */
	Set<DataDict> getCanTaughtSubjectByTeacherAndGrade(String teacherId, String gradeId);
	
	/**
	 * 根据老师和学生获取可教科目
	 * @param teacherId
	 * @param studentId
	 * @return
	 */
	Set<DataDict> getCanTaughtSubjectByTeacherAndStudent(String teacherId, String studentId);
	
	/**
	 * 根据校区获取该校区或跨校区的老师
	 * @param campusId
	 * @param isOtherOrganizationTeacher
	 * @return
	 */
	List<Map<Object, Object>> getTeachersByCampusId(String campusId, Boolean isOtherOrganizationTeacher);
	
	/**
	 * 获取当前用户的编制老师操作权限
	 * @return
	 */
	String getTeacherVersionAuthTags();
	
	/**
	 * 根据用户id，courseDate查找老师类型
	 * @param userId
	 * @param teacherId
	 * @return
	 */
	String getTeacherTypeByUserIdAndCourseDate(String teacherId, String courseDate);
	
}
