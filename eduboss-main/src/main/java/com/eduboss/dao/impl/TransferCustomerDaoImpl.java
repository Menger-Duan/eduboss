package com.eduboss.dao.impl;


import java.util.HashMap;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.TransferCustomerDao;
import com.eduboss.domain.TransferCustomerRecord;
import com.eduboss.dto.DataPackage;


@Repository("transferCustomerDao")
public class TransferCustomerDaoImpl extends GenericDaoImpl<TransferCustomerRecord, String> implements TransferCustomerDao{

	private static final Logger log = LoggerFactory.getLogger(TransferCustomerDaoImpl.class);

	@Override
	public void saveTransferCustomerRecord(TransferCustomerRecord tRecord) {
       log.debug("保存转介绍审核记录");		
       super.save(tRecord);       
	}

	@Override
	public void updateTransferCustomerRecord(TransferCustomerRecord tRecord) {
		log.debug("更新转介绍审核记录");	
		//调用saveorupdate方法
		super.save(tRecord);
	}

	@Override
	public DataPackage getTransferCustomerRecords(String hql, DataPackage dp, String countHql) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setDatas(q.list());
		dp.setRowCount(findCountHql(countHql, new HashMap<String, Object>()));
		return dp;
	}

}
