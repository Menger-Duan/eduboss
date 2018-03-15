package com.eduboss.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduboss.domain.TeacherSubject;
import com.eduboss.domain.TeacherSubjectId;
import com.eduboss.domain.User;
import com.eduboss.domainVo.UserVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TeacherSubjectRequestVo;

public interface TeacherSubjectService {

	public DataPackage getTeacherSubjectList(TeacherSubject teacherSubject, DataPackage dataPackage);

	public void deleteTeacherSubject(TeacherSubject teacherSubject);

	public void saveOrUpdateTeacherSubject(TeacherSubject teacherSubject);

	public TeacherSubject findOneTeacherSubject(TeacherSubject teacherSubject);

	public TeacherSubject disableTeacherSubjectById(String teacherSubjectId);

	public List<UserVo> getTeacherSubjectByGradeSubject(String gradeId, String subjetId);
	
	public List<User> getTeacherSubjectByGradesSubjects(Set<TeacherSubjectRequestVo> teachersrv);

    public List<UserVo> getTeacherSubjectByStudentSubject(String studentId, String subjetId);
	
	/**
	 * 查询跨校区老师public List<UserVo> getTeacherSubjectByGradeSubjectCampusId(String gradeId, String subjetId,String campusId)
	 * @param gradeId
	 * @param subjetId
	 * @return
	 */
	public List<UserVo> getOtherOrganizationTeacherSubjectByGradeSubject(String gradeId, String subjetId);
	
	public List<User> getOtherOrganizationTeacherSubjectByGradesSubjects(Set<TeacherSubjectRequestVo> teachersrv);

	/**
	 * 查询跨校区老师
	 * @param studentId
	 * @param subjetId
	 * @return
	 */
	public List<UserVo> getOtherOrganizationTeacherSubjectByStudentSubject(String studentId, String subjetId);

	/**
	 * 获取老师信息
	 * @return
	 */
	public List<User> getTeacherList(String teacherName);
	
	public List<UserVo> getTeacherListByCampusId(String campusId);

	@Deprecated
	public List<UserVo> getOtherOrganizationTeacherByCampusId(String campusId);

	public List<UserVo> getOtherOrganizationTeacherByCampusIdNew(String campusId);
	
	/**
	 * 根据年级，科目，校区id查询老师
	 */
	public List<UserVo> getTeacherSubjectByGradeSubjectCampusId(String gradeId, String subjetId,String campusId);
	public List<UserVo> getOtherOrganizationTeacherSubjectByGradeSubjectCampusId(String gradeId, String subjetId,String campusId);

	@Deprecated
	public Map<String,Object> getTeacherListByBranchId();

	public Map<String,Object> getTeacherListByBranchIdNew();

	public List getTeacherListForSelect(String gradeId, String subjetId,String[] campusId,String brenchId);
	
	public List getTeachersForSelect(String gradeId, String subjetId,String[] campusId,String brenchId);


	public List getTeachersForSelectNew(String gradeId, String subjetId,String[] campusId,String brenchId);
}
