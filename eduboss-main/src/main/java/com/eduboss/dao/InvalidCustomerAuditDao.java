package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.InvalidCustomerRecord;
import com.eduboss.dto.DataPackage;

/**
 * @Description 无效客户审核操作
 * @author	xiaojinwang
 * @Date	2016-8-8 
 */
@Repository
public interface InvalidCustomerAuditDao extends GenericDAO<InvalidCustomerRecord, String>{
    
	//保存 无效客户审核记录
	public Long saveInvalidCustomerRecord(InvalidCustomerRecord record);
	
	//修改 无效客户审核记录  
	public Long updateInvalidCustomerRecord(InvalidCustomerRecord record);
	
	//查询 无效审核记录
	public DataPackage getInvalidCustomerRecords(String hql,DataPackage dp,String countHql);
	//根据customerId获取记录 
	public List<InvalidCustomerRecord> getRecordsByCustomerId(String customerId);
	
	//根据记录id获取记录
	public InvalidCustomerRecord getRecordById(Long id);
}
