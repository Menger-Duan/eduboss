package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.TeacherVersionDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.TeacherVersion;
import com.eduboss.domainVo.TeacherVersionVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.utils.DateTools;

/**
 * 2016-12-15
 * @author lixuejun
 *
 */
@Repository
public class TeacherVersionDaoImpl extends GenericDaoImpl<TeacherVersion, Integer> implements TeacherVersionDao {

	/**
	 * 分页查找已编制教师
	 */
	@Override
	public DataPackage getPageTeacherVersion(TeacherVersionVo teacherVersionVo, Organization org,
			DataPackage dp) {
		String currentMonth = DateTools.getCurrentDate().substring(0, 4) + DateTools.getCurrentDate().substring(5,7);
		int searchVersion = Integer.parseInt(currentMonth);
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select DISTINCT tv.ID id, u.NAME teacherName, u.ENABLE_FLAG enableFlg, u.WORK_TYPE workType, u.employee_No jobNumber, tv.SUBJECT_DES subjectDes, tv.TEACHER_LEVEL teacherLevel, "
				+ " tv.MAIN_SUBJECT_ID mainSubjectId, tv.GRADE_DES gradeDes, tv.MAIN_GRADE_ID mainGradeId, "
				+ " (select name from data_dict where id = sg.NAME) as subjectGradeName, tv.PREPARATION_TYPE preparationType, tv.TEACHER_TYPE teacherType, "
				+ " tv.TEACHER_FUNCTION_DES teacherFuncationDes, tv.MAIN_TEACHER_FUNCTIONS mainTeacherFunctions, "
				+ " tv.FUNCTIONS_LEVEL_DES functionsLevelDes, tv.MAIN_FUNCTIONS_LEVEL mainFunctionLevel, o.NAME blCampusName "
				+ "  from teacher_version tv "
				+ " left join user u on tv.TEACHER_ID = u.USER_ID left join organization o on tv.BL_CAMPUS_ID = o.ID "
				+ " left join ref_subject_group rsg on tv.MAIN_SUBJECT_ID = rsg.SUBJECT_ID and rsg.version = :searchVersion"
				+ " left join subject_group sg on rsg.GROUP_ID = sg.ID "
				+ " and sg.CAMPUS_ID = tv.BL_CAMPUS_ID where 1=1 ";
		sql += " and tv.IS_CURRENT_VERSION = 1 AND (sg.ID IS NOT NULL OR tv.MAIN_SUBJECT_ID IS NULL OR tv.BL_CAMPUS_ID = '000001') ";
		params.put("searchVersion", searchVersion);
		if (teacherVersionVo.getEnableFlg() != null) {
		    sql += " and u.ENABLE_FLAG = :enableFlg ";
            params.put("enableFlg", teacherVersionVo.getEnableFlg());
		}
		if (StringUtils.isNotBlank(teacherVersionVo.getSubjectId())) {
			sql += " and tv.SUBJECT_DES like :subjectId ";
			params.put("subjectId", "%" + teacherVersionVo.getSubjectId() + "%");
		}
		if (StringUtils.isNotBlank(teacherVersionVo.getGradeId())) {
			sql += " and tv.GRADE_DES like :gradeId ";
			params.put("gradeId", "%" + teacherVersionVo.getGradeId() + "%");
		}
		if (StringUtils.isNotBlank(teacherVersionVo.getSubjectGroupId())) {
			sql += " and sg.ID = :subjectGroupId ";
			params.put("subjectGroupId", teacherVersionVo.getSubjectGroupId());
		}
		if (teacherVersionVo.getPreparationType() != null) {
			sql += " and tv.PREPARATION_TYPE = :preparationType ";
			params.put("preparationType", teacherVersionVo.getPreparationType());
		}
		if (teacherVersionVo.getTeacherType() != null) {
			sql += " and tv.TEACHER_TYPE = :teacherType ";
			params.put("teacherType", teacherVersionVo.getTeacherType());
		}
		if (teacherVersionVo.getSearchTeacherFunctions() != null) {
			sql += " and tv.TEACHER_FUNCTION_DES like :teacherFunctions ";
			params.put("teacherFunctions", "%" + teacherVersionVo.getSearchTeacherFunctions() + "%");
		}
		if (teacherVersionVo.getSearchFunctionsLevel() != null) {
			sql += " and tv.FUNCTIONS_LEVEL_DES like :functionsLevel ";
			params.put("functionsLevel", "%" + teacherVersionVo.getSearchFunctionsLevel() + "%");
		}
		if (StringUtils.isNotBlank(teacherVersionVo.getBlCampusId())) {
			sql += " and tv.BL_CAMPUS_ID = :blCampusId ";
			params.put("blCampusId", teacherVersionVo.getBlCampusId());
		}
		if (teacherVersionVo.getTeacherLevel() != null) {
			sql += " and tv.TEACHER_LEVEL = '" + teacherVersionVo.getTeacherLevel() + "' ";
		}
		sql+=" and u.organizationID in (select id from organization where orgLevel like :orgLevel )";
		params.put("orgLevel",  org.getOrgLevel() + "%");
		if (StringUtils.isNotBlank(teacherVersionVo.getTeacherName())) {
			sql += " and u.NAME like :teacherName ";
			params.put("teacherName", "%" + teacherVersionVo.getTeacherName() + "%");
		}
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			sql+=" order by "+dp.getSidx()+" "+dp.getSord();
		} 
		dp = super.findMapPageBySQL(sql, dp, false, params);
		dp.setRowCount(findCountSql("select count(*)  from (" + sql + ") tmp", params));
		return dp;
	}

	/**
	 * 根据老师ID查找所有版本的teacherVersion
	 */
	@Override
	public List<TeacherVersion> getTeacherVersionList(String teacherId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TeacherVersion where 1=1 ";
		hql += " and teacher.userId = :teacherId ";
		hql += " order by versionDate ";
		params.put("teacherId", teacherId);
		return super.findAllByHQL(hql, params);
	}

	/**
	 * 根据科目、年级和校区级别查询本小区或跨校区可教老师
	 */
	@Override
	public List<Map<Object, Object>> getTeacherByGradeSubject(String gradeId,
			String subjectId, String orgId, String orgLevel) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " SELECT DISTINCT u.USER_ID userId, u.`NAME` name, u.CONTACT contract FROM user u "
				+ " INNER JOIN teacher_version tv ON u.USER_ID = tv.TEACHER_ID AND tv.IS_CURRENT_VERSION = 1 "
				+ " LEFT JOIN teacher_subject_version tsv ON tv.ID = tsv.TEACHER_VERSION_ID "
				+ " LEFT JOIN user_dept_job udj ON u.USER_ID = udj.USER_ID AND udj.JOB_ID = 'USE0000000004' "
				+ " WHERE u.ENABLE_FLAG = '0' ";
		if (StringUtils.isNotBlank(subjectId)) {
			sql	+= " AND tsv.SUBJECT_ID = :subjectId ";
			params.put("subjectId", subjectId);
		}
		if (StringUtils.isNotBlank(gradeId)) {
			sql	+= " AND tsv.GRADE_ID = :gradeId ";
			params.put("gradeId", gradeId);
		}
		if (StringUtils.isNotBlank(orgId)) {
			sql	+= " AND udj.DEPT_ID != :orgId ";
			params.put("orgId", orgId);
		} else {
			sql	+= " AND udj.isMajorRole = 0 ";
		}
		sql	+= " AND udj.DEPT_ID IN (SELECT ID FROM organization where orgLevel like :orgLevel) ";
		params.put("orgLevel",  orgLevel + "%");
 		return super.findMapBySql(sql, params);
	}

	/**
	 * 根据用户来查询编制老师的操作权限
	 */
	@Override
	public List<Map<Object, Object>> getTeacherVersionAuthTags(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select RTAG from resource where RTYPE='BUTTON' and RTAG like 'TEACHER_VERSION%' "
				+ " and id in (select resourceID from role_resource where roleID in "
				+ "(select roleID from user_role where userID = :userId)) ";
		params.put("userId", userId);
		return super.findMapBySql(sql, params);
	}

	@Override
	public List<Map<Object, Object>> getTeacherVersionAuthTagsNew(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select RTAG from resource where RTYPE='BUTTON' and RTAG like 'TEACHER_VERSION%' "
				+ " and id in (select resourceID from role_resource where roleID in "
				+ "(select role_ID from user_organization_role where user_ID = :userId)) ";
		params.put("userId", userId);
		return super.findMapBySql(sql, params);
	}

	/**
	 * 根据用户id，courseDate查找最符合的老师版本
	 */
	@Override
	public List<Map<Object, Object>> getclosedTeacherByUserIdAndCourseDate(
			String teacherId, String courseDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "SELECT TEACHER_TYPE FROM  teacher_version WHERE VERSION_DATE = "
				+ " (SELECT MAX(VERSION_DATE) FROM teacher_version WHERE VERSION_DATE <= :courseDate AND TEACHER_ID = :teacherId1) AND TEACHER_ID = :teacherId2";
		params.put("courseDate", courseDate);
		params.put("teacherId1", teacherId);
		params.put("teacherId2", teacherId);
		return super.findMapBySql(sql, params);
	}

}
