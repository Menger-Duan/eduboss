package com.eduboss.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eduboss.domain.Customer;
import com.eduboss.domain.InvalidCustomerRecord;
import com.eduboss.domainVo.AuditRecordVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.InvalidCustomerRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

/**
 * @Description 无效客户的审核
 * @author	xiaojinwang
 * @Date	2016-08-08 
 */
@Service
public interface InvalidCustomerAuditService {
	//保存 无效客户审核记录
	public Long saveInvalidCustomerRecord(InvalidCustomerRecord record);
	
	//审核 无效客户审核记录  
	public Response auditInvalidCustomer(InvalidCustomerRecordVo recordVo,CustomerVo customerVo);
	
	//查询 无效审核记录
	public DataPackage getInvalidCustomerRecords(InvalidCustomerRecordVo recordVo,DataPackage dp);
	
	//根据用户的记录id获取 审核结果记录
	public InvalidCustomerRecordVo loadInvalidCustomerResult(String id);
	
	//将客户设置为无效客户
	public Long setCustomerInvalid(Customer customer,String remark);
	//根据父审核记录id获取历史子审核记录 
	public List<AuditRecordVo> getAuditRecordById(String parentId);
	
}
