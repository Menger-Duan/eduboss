package com.eduboss.service;

import org.springframework.stereotype.Service;

import com.eduboss.domain.CustomerAllocateObtain;

@Service
public interface CustomerAllocateObtainService {

	CustomerAllocateObtain findAllocateObtainByCustomerId(String customerId);
    
	CustomerAllocateObtain saveOrUpdateCustomerAllocateObtain(CustomerAllocateObtain customerAllocateObtain);
    
}
