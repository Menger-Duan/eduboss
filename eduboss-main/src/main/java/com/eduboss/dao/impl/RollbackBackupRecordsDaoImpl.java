package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.common.ProductType;
import com.eduboss.dao.RollbackBackupRecordsDao;
import com.eduboss.domain.RollbackBackupRecords;
import com.eduboss.dto.DataPackage;

@Repository("RollbackBackupRecordsDao")
public class RollbackBackupRecordsDaoImpl extends GenericDaoImpl<RollbackBackupRecords, String> implements RollbackBackupRecordsDao {

	@Override
	public DataPackage getRollbackBackupRecordsByTransactionId(
			String transactionId, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql="from RollbackBackupRecords  c";
		hql+=" left join c.course  cou ";
		hql+=" left join cou.teacher couTeach ";
		hql+=" left join c.miniClassCourse mcc ";
		hql+=" left join mcc.teacher mccTeach ";
		hql+=" left join c.otmClassCourse occ ";
		hql+=" left join occ.teacher occTeach ";
		hql+= " left join c.contract cont ";
		String hqlWhere=" and c.chargeType not in('TRANSFER_NORMAL_TO_ELECT_ACC','TRANSFER_PROMOTION_TO_ELECT_ACC','PROMOTION_RETURN')";
		hqlWhere+=" and c.transactionId = :transactionId ";
		params.put("transactionId", transactionId);
        if(!hqlWhere.equals("")){
            hqlWhere=hqlWhere.replaceFirst("and", "where"); //将第一个and替换为where
        }
        hql=hql+hqlWhere;
		dp=this.findPageByHQL(hql, dp, true, params);
		return dp;
	}

	
	@Override
	public List<RollbackBackupRecords> getReCordByContractId(String contractId,ProductType productType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from RollbackBackupRecords where contract.id = :contractId ";
		params.put("contractId", contractId);
		if(productType!=null){
			hql+=" and productType = :productType ";
			params.put("productType", productType);
		}
		return this.findAllByHQL(hql, params);
	}
	
}
