package com.eduboss.dao.impl;

import java.util.List;

import org.hibernate.criterion.Expression;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.EducationExperienceDao;
import com.eduboss.domain.EducationExperience;

@Repository("EducationExperienceDao")
public class EducationExperienceDaoImp extends GenericDaoImpl<EducationExperience, String> implements EducationExperienceDao {

	/**
	 * 根据员工信息ID获取其教育经历列表
	 * @param userInfoId
	 * @return
	 */
	@Override
	public List<EducationExperience> getEducationExperienceByUserInfoId(
			String userInfoId) {
		return this.findByCriteria(Expression.eq("userInfoId", userInfoId));
	}

}
