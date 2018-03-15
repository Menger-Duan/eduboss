package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.BusinessAssocMapping;

public interface BusinessAssocMappingDao extends GenericDAO<BusinessAssocMapping, Long> {

    List<BusinessAssocMapping> listBusinessAssocMappingByBusinessIds(String[] businessIds);
    
}
