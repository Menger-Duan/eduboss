package com.eduboss.service;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.eduboss.common.AppointmentType;
import com.eduboss.domain.CustomerFolowup;
import com.eduboss.domainVo.CustomerFolowupVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.TimeVo;


/**
 * 
 * @author xiaojinwang
 * @Description  不同的service层调用 使用service进行调用，把dao装起来
 * @date 2016-08-11
 */

@Service
public interface CustomerFolowupService {
	
	 public List<CustomerFolowup> findByHql(String hql,Map<String, Object> params);
	 
	 public List<CustomerFolowup> findBySql(String sql,Map<String, Object> params);
	
     public List<CustomerFolowup> findByCriteria(List<Order> orders,Criterion ...criterion);
     
     //封装方法 查询某一段时间内是否有登记记录
     public Boolean hasFollowupRecordWithDate(String beginDate,String endDate,String customerId);
     
     //封装在多少天之内是否有登记记录
     public Boolean hasFollowupRecordWithinDays(String date,int days,String customerId);
     
     //客户是否有过登记记录
     public Boolean hasFollowupRecord(String customerId);
     
     //前台工作台 分配客户列表 分配和更改咨询师 修改分配记录状态
     public void updateFollowupDealStatus(String followupId);
     
     //根据userId和customerId的分配记录 
     public List<CustomerFolowup> findRecordbyUserIdAndCustomerId(String userId,String customerId,AppointmentType appointmentType);
    
 	//查询客户是否已经预约到访登记
 	public Response getCustomerMeetingConfirm(String customerId);
 	//根据customerId获取最新的预约客户记录
 	public CustomerFolowupVo getLastAppointmentRecord(String customerId);
 	//更新预约上门记录
 	public void updateAppointCustomer(CustomerFolowupVo follow);
}
