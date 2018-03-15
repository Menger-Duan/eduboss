/**
 * 
 */
package com.eduboss.service;

import org.springframework.stereotype.Service;

import com.eduboss.common.CustomerEventType;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.User;
import com.eduboss.dto.DataPackage;


/**
 * @classname	CommonService.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2013-11-13 12:04:19
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */
@Service
public interface CustomerEventService {
	
	/**
	 * 根据条件查询客户动态
	 * @param cds
	 * @param dp
	 * @return
	 */
	public DataPackage findCustomerDynamicStatus(CustomerDynamicStatus cds, DataPackage dp);

	/**
	 * 根据客户Id查询动态
	 * @param customerId
	 * @param dp
	 * @return
	 */
	public DataPackage findCustomerDynamicStatusByCustomerId(String customerId, DataPackage dp);
	
	/**
	 * 保存新的客户动态
	 * @param cds
	 */
	public void saveCustomerDynamicStatus(CustomerDynamicStatus cds);
	
	/**
	 * 增加动态
	 * @param customerId
	 * @param dynamicStatusType
	 * @param description
	 * @param referUrl
	 */
	public void saveCustomerDynamicStatus(String customerId, CustomerEventType dynamicStatusType, String description, String referUrl);
	
	public void saveCustomerDynamicStatus(String customerId, CustomerEventType dynamicStatusType, String description);
	
	
	public void saveCustomerDynamicStatus(String customerId, CustomerEventType dynamicStatusType, String description,User referUser, String referUrl);
	
	/**
	 * 用于增加资源量和上门数量
	 * 
	 * @param customerId  客户Id
	 * @param dynamicStatusType RESOURCES 资源量  VISITCOME 上门数
	 * @param description 描述
	 * @param referUser   增加记录的User
	 * @param resEntrance 被统计的资源入口
	 * @param statusNum   取值为+1-1 +1表示增加一条统计记录  -1表示减少一条统计记录 
	 */
	public void saveCustomerDynamicStatus(String customerId, CustomerEventType dynamicStatusType, String description,User referUser, String resEntrance,Integer statusNum);
	
	
	public void addCustomerDynameicStatus(Customer customer,CustomerDynamicStatus dynamicStatus,User user);
	
	public void addCustomerDynameicStatus(Customer customer,CustomerDynamicStatus dynamicStatus);
	
	public int findCustomerDynameicStatusCount(String sql);
	
	
}
