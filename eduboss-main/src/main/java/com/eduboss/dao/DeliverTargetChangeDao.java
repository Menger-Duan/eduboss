package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.DeliverTargetChangeRecord;

/**
 * @classname	DeliverTargetChangeDao.java 
 * @Description 操作分配目标变动记录表
 * @author	xiaojinwang
 * @Date	2016-9-02 
 */
@Repository
public interface DeliverTargetChangeDao  extends GenericDAO<DeliverTargetChangeRecord, String>{
   
	public void saveDeliverTargetChangeRecord(DeliverTargetChangeRecord record);
}
