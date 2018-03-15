package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.TransferCustomerRecord;
import com.eduboss.dto.DataPackage;


/**
 * @Description 转介绍客户操作
 * @author	xiaojinwang
 * @Date	2016-7-31 
 */
@Repository
public interface TransferCustomerDao extends GenericDAO<TransferCustomerRecord, String>{
   //保存 审核转介绍客户记录
	public void saveTransferCustomerRecord(TransferCustomerRecord tRecord);
   //修改 审核转介绍客户记录
   public void updateTransferCustomerRecord(TransferCustomerRecord tRecord);
   //查询 审核转介绍客户记录
   public DataPackage getTransferCustomerRecords(String hql,DataPackage dp,String countHql);
}
