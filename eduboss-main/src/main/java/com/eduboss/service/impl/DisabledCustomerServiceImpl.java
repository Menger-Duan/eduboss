package com.eduboss.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.DisabledCustomerDao;
import com.eduboss.domain.DisabledCustomer;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.DisabledCustomerService;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Service("disabledCustomerService")
public class DisabledCustomerServiceImpl implements DisabledCustomerService {

    @Autowired
    private DisabledCustomerDao disabledCustomerDao;
    
    @Override
    public DisabledCustomer findDisabledCustomerByContact(String contact, int udpateEnabled) {
        DisabledCustomer result = null;
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from DisabledCustomer where contact = :contact and udpateEnabled = :udpateEnabled ";
        params.put("contact", contact);
        params.put("udpateEnabled", udpateEnabled);
        List<DisabledCustomer> list = disabledCustomerDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            result = list.get(0); 
        }
        return result;
    }

    @Override
    public DisabledCustomer saveOrUpdateDisabledCustomer(DisabledCustomer disCus) {
        disabledCustomerDao.save(disCus);
        return disCus;
    }
    
    /**
     * 客户管理--无效电话列表 
     * @param dataPackage
     * @param contact
     * @return
     */
    public DataPackage getDisCusForJqGrid(DataPackage dataPackage, String contact) {
        Map<String, Object> params = null;
        String sql = " select * from disabled_customer where 1=1 and udpate_enabled = 0 ";
        if (StringUtil.isNotBlank(contact)) {
            params = Maps.newHashMap();
            params.put("contact", contact);
            sql += " and contact = :contact ";
        }
        disabledCustomerDao.findPageBySql(sql, dataPackage, true, params);
        return dataPackage;
    }

}
