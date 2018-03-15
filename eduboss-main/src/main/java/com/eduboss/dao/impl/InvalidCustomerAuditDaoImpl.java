package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.InvalidCustomerAuditDao;
import com.eduboss.domain.InvalidCustomerRecord;
import com.eduboss.dto.DataPackage;
import com.google.common.collect.Maps;

@Repository("invalidCustomerAuditDao")
public class InvalidCustomerAuditDaoImpl  extends GenericDaoImpl<InvalidCustomerRecord, String> implements InvalidCustomerAuditDao{

	private static final Logger log = LoggerFactory.getLogger(InvalidCustomerAuditDaoImpl.class);
	@Override
	public Long saveInvalidCustomerRecord(InvalidCustomerRecord record) {
	  log.debug("保存无效客户审核的记录");		
	  super.save(record); 
	  return record.getId();
		
	}

	@Override
	public Long updateInvalidCustomerRecord(InvalidCustomerRecord record) {
		log.debug("更新无效客户审核的记录");	
		//调用saveorupdate方法
		super.merge(record);
		return record.getId();
	}

	@Override
	public DataPackage getInvalidCustomerRecords(String hql, DataPackage dp, String countHql) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<InvalidCustomerRecord> getRecordsByCustomerId(String customerId){
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId", customerId);
		List<InvalidCustomerRecord> list =null;
		list=super.findAllByHQL("from InvalidCustomerRecord as r where r.customerId= :customerId order by r.createTime desc",params);
        return list;
	}

	@Override
	public InvalidCustomerRecord getRecordById(Long id) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		return super.findOneByHQL("from InvalidCustomerRecord as r where r.id = :id ",params);
	}

}
