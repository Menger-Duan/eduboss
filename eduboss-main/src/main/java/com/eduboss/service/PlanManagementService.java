package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.Organization;
import com.eduboss.domain.PlanManagement;
import com.eduboss.domainVo.PlanManagementVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

public interface PlanManagementService {

	public List<Map> getOrganizationByCampusAbove();

	public DataPackage getPlanManagementList(PlanManagement planManagement, DataPackage dataPackage);

	public void deletePlanManagement(PlanManagement planManagement);

	public Response saveOrUpdatePlanManagement(PlanManagement planManagement);
	
	public PlanManagementVo findPlanManagementById(String id);
}
