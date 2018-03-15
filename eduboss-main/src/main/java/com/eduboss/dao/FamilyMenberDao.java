package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.FamilyMenber;


/**
 * @Description
 * @author	lixuejun
 * @Date	2015-9-2 17:28
 * 
 */
@Repository
public interface FamilyMenberDao extends GenericDAO<FamilyMenber, String> {

	/**
	 * 根据员工信息ID获取其家庭成员列表
	 * @param userInfoId
	 * @return
	 */
	public List<FamilyMenber> getFamilyMenberByUserInfoId(String userInfoId);
	
}
