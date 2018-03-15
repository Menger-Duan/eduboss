package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.BusinessAssocMappingDao;
import com.eduboss.domain.BusinessAssocMapping;
import com.google.common.collect.Maps;

@Repository
public class BusinessAssocMappingDaoImpl extends GenericDaoImpl<BusinessAssocMapping, Long> implements BusinessAssocMappingDao {

    @Override
    public List<BusinessAssocMapping> listBusinessAssocMappingByBusinessIds(
            String[] businessIds) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from BusinessAssocMapping where 1=1 ";
        hql += " and businessId in (:businessIds) ";
        params.put("businessIds", businessIds);
        return super.findAllByHQL(hql, params);
    }

}
