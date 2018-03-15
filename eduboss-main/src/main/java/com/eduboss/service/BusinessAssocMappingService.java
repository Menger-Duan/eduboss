package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.BusinessAssocMapping;

public interface BusinessAssocMappingService {

    void saveBusinessAssocMapping(BusinessAssocMapping businessAssocMapping);
    
    List<BusinessAssocMapping> listBusinessAssocMappingByBusinessIds(String[] businessIds);
    
}
