package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.WorkExperienceDao;
import com.eduboss.domain.WorkExperience;


@Repository("WorkExperienceDao")
public class WorkExperienceDaoImpl extends GenericDaoImpl<WorkExperience, String> implements WorkExperienceDao {

	/**
	 * 根据员工信息ID获取其工作经历列表
	 * @param userInfoId
	 * @return
	 */
	@Override
	public List<WorkExperience> getWorkExperienceByUserInfoId(String userInfoId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from WorkExperience where userInfoId = :userInfoId ";
		params.put("userInfoId", userInfoId);
		return super.findAllByHQL(hql, params);
	}
	
}
