package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.common.CustomerEventType;
import com.eduboss.dao.CustomerDynamicStatusDao;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.dto.DataPackage;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.eduboss.domain.AppUser
 * @author MyEclipse Persistence Tools
 */
@Repository("CustomerDynamicStatusDao")
public class CustomerDynamicStatusDaoImpl extends GenericDaoImpl<CustomerDynamicStatus, String> implements CustomerDynamicStatusDao {
	
	public DataPackage findByCustomerId(String customerId, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
//		String sql = "select * from customer_dynamic_status where CUSTOMER_ID = :customerId and DYNAMIC_STATUS_TYPE not in('"+CustomerEventType.RESOURCES+"','"
//				+CustomerEventType.VISITCOME+"','"+CustomerEventType.RELEASECUSTOMER+"' ) order by OCCOUR_TIME desc ";
		String sql = "select * from customer_dynamic_status where CUSTOMER_ID = :customerId and VISIT_FLAG = 1 order by OCCOUR_TIME desc ";
		params.put("customerId", customerId);
		return super.findPageBySql(sql, dp, true, params);
	}
	
}
