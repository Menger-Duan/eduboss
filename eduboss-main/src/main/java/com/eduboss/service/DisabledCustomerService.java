package com.eduboss.service;

import org.springframework.stereotype.Service;

import com.eduboss.domain.DisabledCustomer;
import com.eduboss.dto.DataPackage;

@Service
public interface DisabledCustomerService {

    DisabledCustomer findDisabledCustomerByContact(String contact, int udpateEnabled);
    
    DisabledCustomer saveOrUpdateDisabledCustomer(DisabledCustomer disCus);
    
    /**
     * 客户管理--无效电话列表 
     * @param dataPackage
     * @return
     */
    public DataPackage getDisCusForJqGrid(DataPackage dataPackage, String contact);
    
}
