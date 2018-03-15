package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.TeacherSubjectVersionDao;
import com.eduboss.domain.TeacherSubjectVersion;

/**
 * 2016-12-15
 * @author lixuejun
 *
 */
@Repository
public class TeacherSubjectVersionDaoImpl extends GenericDaoImpl<TeacherSubjectVersion, Integer> implements TeacherSubjectVersionDao {

	@Override
	public List<Map<Object, Object>> findTeacherSubjectVersionBySubject(
			String campusId, String subjectId, int versionMonth) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select distinct tsv.SUBJECT_ID subjectId, ifnull(concat(group_concat(if(u.ENABLE_FLAG = 0, u.name, CONCAT(u.name, '(无效)'))), ''), '') teacherName from "
				+ " (SELECT max(tsv.VERSION_MONTH), tsv.SUBJECT_ID, tsv.TEACHER_ID, tsv.TEACHER_VERSION_ID "
				+ "	FROM teacher_subject_version tsv " 
				+ " WHERE tsv.IS_MONTH_VERSION =1 AND tsv.VERSION_MONTH <= :versionMonth AND tsv.BL_CAMPUS_ID = :blCampusId "
				+ " GROUP BY tsv.SUBJECT_ID, tsv.TEACHER_ID) tsv "
				+ " left join user u on tsv.TEACHER_ID = u.USER_ID where 1=1 ";
		params.put("versionMonth", versionMonth);
		params.put("blCampusId", campusId);
		if (StringUtils.isNotBlank(subjectId)) {
			sql += " and tsv.SUBJECT_ID = :subjectId ";
			params.put("subjectId", subjectId);
		}
		return super.findMapBySql(sql, params);
	}
	
	/**
	 * 根据老师和年级获取当前可教老师版本
	 */
	@Override
	public List<TeacherSubjectVersion> getCanTaughtSubjectByTeacherAndGrade(
			String teacherId, String gradeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " SELECT DISTINCT tsv.* FROM user u "
				+ " INNER JOIN teacher_version tv ON u.USER_ID = tv.TEACHER_ID AND tv.IS_CURRENT_VERSION = 1 "
				+ " LEFT JOIN teacher_subject_version tsv ON tv.ID = tsv.TEACHER_VERSION_ID "
				+ " LEFT JOIN user_dept_job udj ON u.USER_ID = udj.USER_ID AND udj.JOB_ID = 'USE0000000004' "
				+ " WHERE u.ENABLE_FLAG = '0' ";
		if (StringUtils.isNotBlank(teacherId)) {
			sql += " AND u.USER_ID = :teacherId ";
			params.put("teacherId", teacherId);
		}
		if (StringUtils.isNotBlank(gradeId)) {
			sql += " AND tsv.GRADE_ID = :gradeId ";
			params.put("gradeId", gradeId);
		}
		return super.findBySql(sql, params);
	}

}
