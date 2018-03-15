package com.eduboss.dao.impl;

import java.util.List;

import org.hibernate.criterion.Expression;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.ContractSigningDao;
import com.eduboss.domain.ContractSigning;

@Repository("ContractSigningDao")
public class ContractSigningDaoImp extends GenericDaoImpl<ContractSigning, String> implements ContractSigningDao {

	/**
	 * 根据员工信息ID获取其合同签订列表
	 * @param userInfoId
	 * @return
	 */
	@Override
	public List<ContractSigning> getContractSigningByUserInfoId(
			String userInfoId) {
		// TODO Auto-generated method stub
		return this.findByCriteria(Expression.eq("userInfoId", userInfoId));
	}

}
