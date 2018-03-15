package com.eduboss.dao;

import com.eduboss.domain.CourseRequirement;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.dto.CourseRequirementSearchInputVo;
import com.eduboss.dto.DataPackage;

public interface CourseRequirementDao extends GenericDAO<CourseRequirement, String> {
	
	// 学管排课需求
	public DataPackage getCourseRequirementList(
			CourseRequirementSearchInputVo courseRequirementSearchInputVo,
			DataPackage dp, String currentUserBlCampusId, User currentLoginUser
			, Organization currentLoginUserCampus);
}
