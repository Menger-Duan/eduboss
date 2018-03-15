package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.ContractRecordDao;
import com.eduboss.domain.ContractRecord;

@Repository
public class ContractRecordDaoImpl extends GenericDaoImpl<ContractRecord, String> implements  ContractRecordDao{
	private static final Logger log = LoggerFactory.getLogger(ContractRecordDaoImpl.class);
	
	
	/* (non-Javadoc)
	 * @see com.eduboss.dao.ContractRecordDao#findLastRecordByContractId(java.lang.String)
	 */
	@Override
	public List<ContractRecord> findLastRecordByContractId(String contractId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("contractId", contractId);
		return this.findAllByHQL(" from ContractRecord where contract.id= :contractId order by createTime desc", params);

	}
}
