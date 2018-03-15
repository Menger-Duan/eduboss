package com.eduboss.dao.impl;

import java.util.List;

import org.hibernate.criterion.Expression;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.PersonnelTransferDao;
import com.eduboss.domain.PersonnelTransfer;

@Repository("PersonnelTransferDao")
public class PersonnelTransferDaoImp extends GenericDaoImpl<PersonnelTransfer, String> implements PersonnelTransferDao {

	/**
	 * 根据员工信息ID获取其人事调动列表
	 * @param userInfoId
	 * @return
	 */
	@Override
	public List<PersonnelTransfer> getPersonnelTransferByUserInfoId(
			String userInfoId) {
		return this.findByCriteria(Expression.eq("userInfoId", userInfoId));
	}

}
