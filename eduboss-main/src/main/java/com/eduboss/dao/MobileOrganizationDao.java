package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.MobileOrganization;



@Repository
public interface MobileOrganizationDao extends GenericDAO<MobileOrganization, String> {
	
	List<MobileOrganization> getOrganizationBoy(String parentId,Integer level);

	MobileOrganization getOrganizationByParentId(String parentId,Integer level);
	
}
