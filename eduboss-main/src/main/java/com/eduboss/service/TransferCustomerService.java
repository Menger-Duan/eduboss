package com.eduboss.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eduboss.domain.Customer;
import com.eduboss.domain.TransferCustomerRecord;
import com.eduboss.domainVo.AuditRecordVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.TransferCustomerRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

/**
 * @Description 转介绍客户的审核（审核用于修改客户的资源入口）
 * @author	xiaojinwang
 * @Date	2016-07-31 
 */
@Service
public interface TransferCustomerService {
    
	//分页查询 审核者查询所有的审核记录
	public DataPackage getTransferCustomers(TransferCustomerRecordVo tCustomerRecordVo, DataPackage dp);
	
	
	//分页查询   查询转介绍登记人提交的登记 转介绍审核记录
	public DataPackage getTransferAuditRecords(TransferCustomerRecordVo tCustomerRecordVo, DataPackage dp);
	
	
	//审核转介绍客户
	public Response auditTransferCustomer(TransferCustomerRecordVo tCustomerRecordVo,CustomerVo customerVo);
	
	
	//保存 一条转介绍客户审核记录
	public Response saveTransferCustomerAuditRecord(TransferCustomerRecordVo tCustomerRecordVo);
	
	//检测被介绍人是否已经存在待审核列表中  根据手机号码查询待审核列表记录
	public List<TransferCustomerRecord> getTransferRecordByContactAndStuName(String contact,String studentName);
	
	
	//根据记录id 查询审核结果
	public TransferCustomerRecordVo loadTransferCustomerResult(String id);
	//根据父审核记录id获取历史子审核记录 
	public List<AuditRecordVo> getAuditRecordById(String parentId);
	
	//转介绍审核下拉框的对象
	public List getTransferTargetByCampus(String showBelong,String[] job,String transferId) ;
	
	//转介绍审核不通过的时候分配客户
	public Response allocateTransferCustomer(TransferCustomerRecordVo tCustomerRecordVo);
	
	public Map<String,Object> checkTransferCustomer(Customer customer,String stuName) throws Exception;
		
	
}
