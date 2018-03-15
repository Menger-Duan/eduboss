package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.EducationExperience;

/**
 * @Description
 * @author	lixuejun
 * @Date	2015-9-2 17:28
 * 
 */

@Repository
public interface EducationExperienceDao extends GenericDAO<EducationExperience, String>{

	/**
	 * 根据员工信息ID获取其教育经历列表
	 * @param userInfoId
	 * @return
	 */
	public List<EducationExperience> getEducationExperienceByUserInfoId(String userInfoId);
	
}
