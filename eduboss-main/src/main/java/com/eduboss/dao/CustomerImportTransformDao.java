package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.CustomerImportTransform;
import com.eduboss.dto.DataPackage;

@Repository
public interface CustomerImportTransformDao extends GenericDAO<CustomerImportTransform, String> {
	public DataPackage getCustomerImportInfo(CustomerImportTransform cif,String startDate,String endDate,String gradeName,
			DataPackage dp);
}
