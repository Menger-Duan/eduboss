package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.PersonnelTransfer;

/**
 * @Description
 * @author	lixuejun
 * @Date	2015-9-2 17:28
 * 
 */
@Repository
public interface PersonnelTransferDao extends GenericDAO<PersonnelTransfer, String> {

	/**
	 * 根据员工信息ID获取其人事调动列表
	 * @param userInfoId
	 * @return
	 */
	public List<PersonnelTransfer> getPersonnelTransferByUserInfoId(String userInfoId);
	
}
