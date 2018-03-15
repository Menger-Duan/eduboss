package com.eduboss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.BusinessAssocMappingDao;
import com.eduboss.domain.BusinessAssocMapping;
import com.eduboss.service.BusinessAssocMappingService;

@Service
public class BusinessAssocMappingServiceImpl implements BusinessAssocMappingService {

    @Autowired
    private BusinessAssocMappingDao businessAssocMappingDao;
    
    @Override
    public void saveBusinessAssocMapping(
            BusinessAssocMapping businessAssocMapping) {
        businessAssocMappingDao.save(businessAssocMapping);
    }

    @Override
    public List<BusinessAssocMapping> listBusinessAssocMappingByBusinessIds(String[] businessIds) {
        return businessAssocMappingDao.listBusinessAssocMappingByBusinessIds(businessIds);
    }

}
