package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.ContractSigning;

/**
 * @Description
 * @author	lixuejun
 * @Date	2015-9-2 17:28
 * 
 */
@Repository
public interface ContractSigningDao extends GenericDAO<ContractSigning, String> {

	/**
	 * 根据员工信息ID获取其合同签订列表
	 * @param userInfoId
	 * @return
	 */
	public List<ContractSigning> getContractSigningByUserInfoId(String userInfoId);
	
}
