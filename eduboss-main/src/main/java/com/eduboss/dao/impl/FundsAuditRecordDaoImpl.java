package com.eduboss.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.FundsAuditRecordDao;
import com.eduboss.domain.FundsAuditRecord;
import com.eduboss.dto.DataPackage;
import com.google.common.collect.Maps;

/**
 * 
 * @author lixuejun
 *
 */
@Repository("FundsAuditRecordDao")
public class FundsAuditRecordDaoImpl extends GenericDaoImpl<FundsAuditRecord, Integer> implements FundsAuditRecordDao {

	/**
	 * 根据收款id查找收款审核流水
	 */
	@Override
	public DataPackage findFundsAuditRecordByFundsId(String fundsId, DataPackage dp) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("fundsId", fundsId);
		String hql = " from FundsAuditRecord where fundsChangeHistoryId = :fundsId order by createTime desc ";
		return super.findPageByHQL(hql, dp,true,params);
	}
	
}
