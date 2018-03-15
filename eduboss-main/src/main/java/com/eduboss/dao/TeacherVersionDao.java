package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.Organization;
import com.eduboss.domain.TeacherVersion;
import com.eduboss.domainVo.TeacherVersionVo;
import com.eduboss.dto.DataPackage;

/**
 * 2016-12-15
 * @author lixuejun
 *
 */
public interface TeacherVersionDao extends GenericDAO<TeacherVersion, Integer> {

	/**
	 * 分页查找已编制教师
	 * @param teacherVersionVo
	 * @param dp
	 * @return
	 */
	DataPackage getPageTeacherVersion(TeacherVersionVo teacherVersionVo, Organization org, DataPackage dp);
	
	/**
	 * 根据老师ID查找所有版本的teacherVersion
	 * @param teacherId
	 * @return
	 */
	List<TeacherVersion> getTeacherVersionList(String teacherId);
	
	/**
	 * 根据科目、年级和校区级别查询可教老师
	 * @param gradeId
	 * @param subjetId
	 * @return
	 */
	List<Map<Object, Object>> getTeacherByGradeSubject(String gradeId, String subjetId, String orgId, String orgLevel);
	
	/**
	 * 根据用户来查询编制老师的操作权限
	 * @param userId
	 * @return
	 */
	@Deprecated
	List<Map<Object, Object>> getTeacherVersionAuthTags(String userId);

	List<Map<Object, Object>> getTeacherVersionAuthTagsNew(String userId);
	
	/**
	 * 根据用户id，courseDate查找最符合的老师版本
	 * @param teacherId
	 * @param courseDate
	 * @return
	 */
	List<Map<Object, Object>> getclosedTeacherByUserIdAndCourseDate(String teacherId, String courseDate);
	
}
