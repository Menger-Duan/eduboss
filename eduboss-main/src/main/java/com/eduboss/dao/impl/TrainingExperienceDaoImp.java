package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.TrainingExperienceDao;
import com.eduboss.domain.TrainingExperience;

@Repository("TrainingExperienceDao")
public class TrainingExperienceDaoImp extends GenericDaoImpl<TrainingExperience, String> implements TrainingExperienceDao {

	/**
	 * 根据员工信息ID获取其培训经历列表
	 * @param userInfoId
	 * @return
	 */
	@Override
	public List<TrainingExperience> getTrainingExperienceByUserInfoId(
			String userInfoId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TrainingExperience where userInfoId = :userInfoId ";
		params.put("userInfoId", userInfoId);
		return super.findAllByHQL(hql, params);
	}

}
