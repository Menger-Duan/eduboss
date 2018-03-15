package com.eduboss.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.CustomerAllocateObtainDao;
import com.eduboss.domain.CustomerAllocateObtain;
import com.eduboss.service.CustomerAllocateObtainService;
import com.google.common.collect.Maps;

@Service("customerAllocateObtainService")
public class CustomerAllocateObtainServiceImpl implements CustomerAllocateObtainService {

    @Autowired
    private CustomerAllocateObtainDao customerAllocateObtainDao;
    
	@Override
	public CustomerAllocateObtain findAllocateObtainByCustomerId(String customerId) {
		// TODO Auto-generated method stub
		CustomerAllocateObtain result = null;
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from CustomerAllocateObtain where customer_id = :customerId ";
        params.put("customerId", customerId);
        List<CustomerAllocateObtain> list = customerAllocateObtainDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            result = list.get(0); 
        }
        return result;
	}

	@Override
	public CustomerAllocateObtain saveOrUpdateCustomerAllocateObtain(CustomerAllocateObtain customerAllocateObtain) {
		// TODO Auto-generated method stub
		customerAllocateObtainDao.save(customerAllocateObtain);
        return customerAllocateObtain;
	}

}
