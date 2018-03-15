package com.eduboss.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domain.PlanManagement;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.PlanManagementVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.Response;
import com.eduboss.service.PlanManagementService;

/**
 * 计划管理
 * @author ndd
 */
@Controller
@RequestMapping(value="/PlanManagementController")
public class PlanManagementController {
	
	@Autowired
	private PlanManagementService planManagementService;
	
	@ResponseBody
	@RequestMapping(value="/getOrganizationByCampusAbove")
	public List<Map> getOrganizationByCampusAbove(){
		return planManagementService.getOrganizationByCampusAbove();
	}
	
	@RequestMapping(value="/getPlanManagementList")
	@ResponseBody
	public DataPackageForJqGrid getPlanManagementList(GridRequest gridRequest, PlanManagement planManagement){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = planManagementService.getPlanManagementList(planManagement, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	

	@RequestMapping(value = "/editPlanManagement")
	@ResponseBody
	public Response editPlanManagement( GridRequest gridRequest,PlanManagement planManagement) {
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			planManagementService.deletePlanManagement(planManagement);
		} else {
			return planManagementService.saveOrUpdatePlanManagement(planManagement);
		}
		return new Response();
	}
	
	@ResponseBody
	@RequestMapping(value = "/findPlanManagementById")
	public PlanManagementVo findPlanManagementById(@RequestParam String id) {
		return planManagementService.findPlanManagementById(id);
	}
}
