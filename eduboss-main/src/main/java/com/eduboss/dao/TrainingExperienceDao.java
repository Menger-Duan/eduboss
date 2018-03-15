package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.TrainingExperience;

/**
 * @Description
 * @author	lixuejun
 * @Date	2015-9-2 17:28
 * 
 */
@Repository
public interface TrainingExperienceDao extends GenericDAO<TrainingExperience, String> {

	/**
	 * 根据员工信息ID获取其培训经历列表
	 * @param userInfoId
	 * @return
	 */
	public List<TrainingExperience> getTrainingExperienceByUserInfoId(String userInfoId);
	
}
