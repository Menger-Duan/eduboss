package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.DeliverTargetChangeDao;
import com.eduboss.domain.DeliverTargetChangeRecord;

@Repository("deliverTargetChangeDao")
public class DeliverTargetChangeDaoImpl extends GenericDaoImpl<DeliverTargetChangeRecord,String> implements DeliverTargetChangeDao{

	@Override
	public void saveDeliverTargetChangeRecord(DeliverTargetChangeRecord record) {
		this.save(record);
	}

}
