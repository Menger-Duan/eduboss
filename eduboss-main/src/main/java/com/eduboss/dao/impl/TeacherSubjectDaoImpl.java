package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.TeacherSubjectDao;
import com.eduboss.domain.TeacherSubject;
import com.eduboss.utils.StringUtil;

@Repository
public class TeacherSubjectDaoImpl extends GenericDaoImpl<TeacherSubject, Integer> implements TeacherSubjectDao {
	
	@Override
	public TeacherSubject findOneTeacherSubject(TeacherSubject teacherSubject) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TeacherSubject where 1=1 ";
		if (teacherSubject.getTeacher() != null && StringUtil.isNotBlank(teacherSubject.getTeacher().getUserId())) {
			hql += " and teacher.userId = :teacherId ";
			params.put("teacherId", teacherSubject.getTeacher().getUserId());
		}
		if (teacherSubject.getSubject() != null && StringUtil.isNotBlank(teacherSubject.getSubject().getId())) {
			hql += " and subject.id = :subjectId ";
			params.put("subjectId", teacherSubject.getSubject().getId());
		}
		if (teacherSubject.getGrade() != null && StringUtil.isNotBlank(teacherSubject.getGrade().getId())) {
			hql += " and grade.id = :gradeId ";
			params.put("gradeId", teacherSubject.getGrade().getId());
		}
		List<TeacherSubject> list = super.findAllByHQL(hql, params);
		return list.size() > 0 ? list.get(0) : null;
	}
	
}
