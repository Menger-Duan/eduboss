package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.WorkExperience;


/**
 * @Description
 * @author	lixuejun
 * @Date	2015-8-31 17:28
 * 
 */

@Repository
public interface WorkExperienceDao extends GenericDAO<WorkExperience, String> {
	
	/**
	 * 根据员工信息ID获取其工作经历列表
	 * @param userInfoId
	 * @return
	 */
	public List<WorkExperience> getWorkExperienceByUserInfoId(String userInfoId);
	
}
