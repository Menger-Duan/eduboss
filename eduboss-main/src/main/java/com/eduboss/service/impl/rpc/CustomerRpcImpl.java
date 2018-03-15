package com.eduboss.service.impl.rpc;

import org.boss.rpc.base.dto.CustomerRpcVo;
import org.boss.rpc.eduboss.service.CustomerRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.domainVo.CustomerVo;
import com.eduboss.service.CustomerService;
import com.eduboss.utils.HibernateUtils;

@Service("customerRpcImpl")
public class CustomerRpcImpl implements CustomerRpc {

    @Autowired
    private CustomerService customerService;
    
    @Override
    public CustomerRpcVo findCustomerByPhone(String phone) {
        CustomerRpcVo result = null;
        CustomerVo vo = customerService.findCustomerByPhone(phone);
        if (vo != null) {
            result = HibernateUtils.voObjectMapping(vo, CustomerRpcVo.class);
        }
        return result;
    }

}
