package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.Customer;
import com.eduboss.domainVo.CustomerAppointmentVo;
import com.eduboss.domainVo.CustomerVo;


/**
 * @classname	CustomerDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface CustomerDao extends GenericDAO<Customer, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	public int getCustomerCount(CustomerVo vo);
	
	public int getCustomerCountByPhone(CustomerVo vo);
	
	public List<CustomerVo> getCustomerNames(CustomerVo vo);
	
	public void setCustomerNextFollowupTime(CustomerAppointmentVo appointment);

	/**
	 *
	 * @param contact
	 * @param customerId
	 * @return
	 */
    int countCustomerNumsExceptSelf(String contact, String customerId);
}
