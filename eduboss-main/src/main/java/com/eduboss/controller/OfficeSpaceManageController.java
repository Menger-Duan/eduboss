package com.eduboss.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.OrganizationType;
import com.eduboss.domainVo.OfficeSpaceManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.OfficeSpaceManageService;
import com.eduboss.utils.StringUtil;

@Controller
@RequestMapping(value = "/OfficeSpaceManageController")
public class OfficeSpaceManageController {
	
	@Autowired
	private OfficeSpaceManageService officeSpaceManageService;
	
	/**
	 * 查询
	 * @param request
	 * @param gridRequest
	 * @param classroomManageVo
	 * @return
	 */
	@RequestMapping(value = "/getOfficeSpaceManageList")
	@ResponseBody
	public DataPackageForJqGrid getOfficeSpaceManageList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest, OfficeSpaceManageVo officeSpaceManageVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String organizationId = "",officeSpace = "",areaStart = "",areaEnd = "",status = "";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationId = request.getParameter("organizationIdFinder").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("officeSpaceFinder"))){
			officeSpace = request.getParameter("officeSpaceFinder").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("areaStart"))){
			areaStart = request.getParameter("areaStart").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("areaEnd"))){
			areaEnd = request.getParameter("areaEnd").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("statusFinder"))){
			if(!request.getParameter("statusFinder").trim().equals("2")){
				status = request.getParameter("statusFinder").trim();
			}
		}else{
			status = "1";
		}
		params.put("organizationId", organizationId);
		params.put("officeSpace", officeSpace);
		params.put("areaStart", areaStart);
		params.put("areaEnd", areaEnd);
		params.put("status", status);
		
		dataPackage = officeSpaceManageService.getOfficeSpaceManageList(dataPackage, officeSpaceManageVo, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}

	/**
	 * 新增
	 * @param request
	 * @param officeSpaceManageVo
	 * @return
	 */
	@RequestMapping(value = "/saveOfficeSpaceManage", method =  RequestMethod.GET)
	@ResponseBody	
	public Response saveOfficeSpaceManage(HttpServletRequest request, @ModelAttribute OfficeSpaceManageVo officeSpaceManageVo){
		officeSpaceManageService.saveOfficeSpaceManage(officeSpaceManageVo);
		return new Response();
	}
	
	/**
	 * 查找1条
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/findOfficeSpaceById", method =  RequestMethod.GET)
	@ResponseBody	
	public OfficeSpaceManageVo findOfficeSpaceById(@RequestParam String id){
		return officeSpaceManageService.findOfficeSpaceById(id);
	}

	/**
	 * 修改
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyOfficeSpaceManage", method =  RequestMethod.GET)
	@ResponseBody	
	public Response modifyOfficeSpaceManage(HttpServletRequest request, @ModelAttribute OfficeSpaceManageVo officeSpaceManageVo){
		officeSpaceManageService.modifyOfficeSpaceManage(officeSpaceManageVo);
		return new Response();
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteOfficeSpaceManage", method =  RequestMethod.GET)
	@ResponseBody	
	public Response deleteOfficeSpaceManage(@RequestParam String id){
		officeSpaceManageService.deleteOfficeSpaceManage(id);
		return new Response();
	}	
	
	/**
	 * 修改教室状态
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyOfficeSpaceManageStatus", method =  RequestMethod.GET)
	@ResponseBody	
	public Response modifyOfficeSpaceManageStatus(@RequestParam String id,@RequestParam String statusChanger){
		OfficeSpaceManageVo officeSpaceManageVo = officeSpaceManageService.findOfficeSpaceById(id);
		officeSpaceManageVo.setStatus(new Integer(statusChanger));
		officeSpaceManageService.modifyOfficeSpaceManage(officeSpaceManageVo);
		return new Response();
	}
	
	/**
	 * 场地信息统计
	 * 
	 */
	@RequestMapping(value = "/officeSpaceCountList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid officeSpaceCountList(@ModelAttribute GridRequest gridRequest, String campusType,String organizationIdFinder,OrganizationType organizationType){
		DataPackage dataPackage = new DataPackage(gridRequest);		
		dataPackage = officeSpaceManageService.officeSpaceCountList(dataPackage, campusType,organizationIdFinder,organizationType);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}
	
	/**
	 * 添加场地前判断
	 */
	@RequestMapping("/beforSaveOfficeSpace")
	@ResponseBody
	public Response beforSaveOfficeSpace(@RequestParam String orgId,String space,String fid) throws Exception{
		officeSpaceManageService.beforSaveOfficeSpace(orgId, space,fid);
		return new Response();
	}
	
}
