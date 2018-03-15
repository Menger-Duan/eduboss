package com.eduboss.service;

import org.springframework.stereotype.Service;

import com.eduboss.domain.DeliverTargetChangeRecord;

/**
 * @classname	DeliverTargetChangeDao.java 
 * @Description 操作分配目标变动记录表
 * @author	xiaojinwang
 * @Date	2016-9-02 
 */
@Service
public interface DeliverTargetChangeService {
	public String saveDeliverTargetChangeRecord(DeliverTargetChangeRecord record);
	
	//获取某个customerId的最新的变动记录 用于还原
	public DeliverTargetChangeRecord getLastChangeRecord(String customerId);
}
