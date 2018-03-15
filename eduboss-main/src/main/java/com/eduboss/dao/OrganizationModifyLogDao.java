package com.eduboss.dao;

import com.eduboss.domain.OrganizationModifyLog;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrganizationModifyLogDao extends GenericDAO<OrganizationModifyLog, String> {
	List<OrganizationModifyLog> findAllByOrgId(String orgId);
}
