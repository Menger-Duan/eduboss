package com.eduboss.dao.impl;

import java.util.List;

import org.hibernate.criterion.Expression;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.FamilyMenberDao;
import com.eduboss.domain.EducationExperience;
import com.eduboss.domain.FamilyMenber;

@Repository("FamilyMenberDao")
public class FamilyMenberDaoImp extends GenericDaoImpl<FamilyMenber, String> implements FamilyMenberDao {

	/**
	 * 根据员工信息ID获取其家庭成员列表
	 * @param userInfoId
	 * @return
	 */
	@Override
	public List<FamilyMenber> getFamilyMenberByUserInfoId(
			String userInfoId) {
		return this.findByCriteria(Expression.eq("userInfoId", userInfoId));
	}
	
}
