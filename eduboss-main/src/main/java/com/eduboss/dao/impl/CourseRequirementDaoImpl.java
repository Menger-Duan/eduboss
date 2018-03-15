package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.CourseRequirementDao;
import com.eduboss.domain.CourseRequirement;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.dto.CourseRequirementSearchInputVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;

@Repository("CourseRequirement")
public class CourseRequirementDaoImpl extends GenericDaoImpl<CourseRequirement, String>
		implements CourseRequirementDao {

    @Autowired
    private RoleQLConfigService roleQLConfigService;

	// 学管排课需求
	@Override
	public DataPackage getCourseRequirementList(
			CourseRequirementSearchInputVo courseRequirementSearchInputVo,
			DataPackage dp, String currentUserBlCampusId, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql=new StringBuffer();
		hql.append(" from CourseRequirement  where 1=1 ");
		if (StringUtils.isNotBlank(courseRequirementSearchInputVo.getStartDate())) {
			hql.append(" and startDate > :startDate ");
			params.put("startDate", courseRequirementSearchInputVo.getStartDate());
		}
		if (StringUtils.isNotBlank(courseRequirementSearchInputVo.getEndDate())) {
			hql.append(" and endDate < :endDate ");
			params.put("endDate", courseRequirementSearchInputVo.getEndDate());
		}
		if (StringUtils.isNotBlank(courseRequirementSearchInputVo.getCreateTimeStart())) {
			hql.append(" and createTime > :createTimeStart ");
			params.put("createTimeStart", courseRequirementSearchInputVo.getCreateTimeStart());
		}
		if (StringUtils.isNotBlank(courseRequirementSearchInputVo.getCreateTimeEnd())) {
			hql.append(" and createTime < :createTimeEnd ");
			params.put("createTimeEnd", courseRequirementSearchInputVo.getCreateTimeEnd());
		}
		if (StringUtils.isNotBlank(courseRequirementSearchInputVo.getStudentName())) {
			hql.append(" and student.name like :studentName ");
			params.put("studentName", "%" + courseRequirementSearchInputVo.getStudentName() + "%");
		}
		if (StringUtils.isNotBlank(courseRequirementSearchInputVo.getTeacherName())) {
			hql.append(" and teacher.name like :teacherName ");
			params.put("teacherName", "%" + courseRequirementSearchInputVo.getTeacherName() + "%");
		}
		if (StringUtils.isNotBlank(courseRequirementSearchInputVo.getGrade())) {
			hql.append(" and grade.value = :grade ");
			params.put("grade", courseRequirementSearchInputVo.getGrade());
		}
		if (StringUtils.isNotBlank(courseRequirementSearchInputVo.getSubject())) {
			hql.append(" and subject.value = :subject ");
			params.put("subject", courseRequirementSearchInputVo.getSubject());
		}
		if (courseRequirementSearchInputVo.getRequirementStatus() != null) {
			hql.append(" and requirementStatus = :requirementStatus ");
			params.put("requirementStatus", courseRequirementSearchInputVo.getRequirementStatus());
		}
		if (courseRequirementSearchInputVo.getRequirementCetegory() != null) {
			hql.append(" and requirementCetegory = :requirementCetegory ");
			params.put("requirementCetegory", courseRequirementSearchInputVo.getRequirementCetegory());
		}
		hql.append(roleQLConfigService.getAppendSqlByAllOrg("学管排课需求","hql","student.blCampus.id"));
		hql.append(" order by  requirementStatus DESC, createTime DESC ");
		return super.findPageByHQL(hql.toString(), dp, true, params);
	}


}
