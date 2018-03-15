package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.ResourcePoolRole;
import com.eduboss.domainVo.ResourcePoolRoleVo;

/**
 * 
 * @author duanmenrun
 *
 */
public interface ResourcePoolRoleDao extends GenericDAO<ResourcePoolRole, String> {

	List<ResourcePoolRole> getResourcePoolRoleList(ResourcePoolRoleVo resourcePoolroleVo);

	void deleteResourcePoolRoleById(String organizationId);
	
	
}
