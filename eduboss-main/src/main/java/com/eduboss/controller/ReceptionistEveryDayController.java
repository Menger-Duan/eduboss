package com.eduboss.controller;

import java.math.BigDecimal;
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

import com.eduboss.domainVo.ReceptionistEveryDayVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ReceptionistEveryDayService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

@Controller
@RequestMapping(value="/receptionistEveryDayController")
public class ReceptionistEveryDayController {
	@Autowired
	private ReceptionistEveryDayService receptionistService;
	
	/**
	 * 
	 * @param request
	 * @param gridRequest
	 * @param receptionistEveryDayVo
	 * @return
	 */
	@RequestMapping(value = "/getReceptionistList")
	@ResponseBody
	public DataPackageForJqGrid getReceptionistList(HttpServletRequest request,
			@ModelAttribute GridRequest gridRequest,ReceptionistEveryDayVo receptionistEveryDayVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String startDate="",endDate="",organizationIdFinder="",loginId="";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("startDate"))){
			startDate=request.getParameter("startDate").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("endDate"))){
			endDate=request.getParameter("endDate").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationIdFinder=request.getParameter("organizationIdFinder");
		}
		if(StringUtil.isNotBlank(request.getParameter("loginId"))){
			loginId=request.getParameter("loginId");
		}
		
		params.put("startDate",startDate);
		params.put("endDate",endDate);
		params.put("organizationIdFinder", organizationIdFinder);
		params.put("loginId", loginId);
		
		dataPackage = receptionistService.getReceptionistEveryDays(dataPackage, receptionistEveryDayVo, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
		
	}
	
	/**
	 * 新增，修改
	 * @return
	 */
	@RequestMapping(value = "/saveReceptionist", method =  RequestMethod.GET)
	@ResponseBody
	public Response saveReceptionist(HttpServletRequest request) throws ApplicationException {
		ReceptionistEveryDayVo reception=new ReceptionistEveryDayVo();
		if(StringUtil.isNotBlank(request.getParameter("recId"))){
			reception.setId(request.getParameter("recId"));
		}
		reception.setLoginDate(request.getParameter("loginDate"));
		reception.setOrganizationId(request.getParameter("organizationId"));
		reception.setUserId(request.getParameter("userId"));
		reception.setTelNum(new BigDecimal(request.getParameter("telNum")));
		reception.setTelNumw(new BigDecimal(request.getParameter("telNumw")));
		reception.setVisitNumy(new BigDecimal(request.getParameter("visitNumy")));
		reception.setVisitNumz(new BigDecimal(request.getParameter("visitNumz")));
		reception.setWriteTime(DateTools.getCurrentDateTime());
		receptionistService.saveOrUpdateReceptionist(reception);
		return new Response();
	}
	
	/**
	 * 查询一条记录
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/selOneReceptionist",method = RequestMethod.GET)
	@ResponseBody
	public ReceptionistEveryDayVo selOneReceptionist(@RequestParam(required=true) String id){
		return receptionistService.findReceptionById(id);
	}
	
	/**
	 * 校区资源利用情况
	 */
	@RequestMapping(value="/shoolResourceList",method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid shoolResourceList(HttpServletRequest request,
			@ModelAttribute GridRequest gridRequest,ReceptionistEveryDayVo receptionistEveryDayVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String startDate="",endDate="",organizationIdFinder="", searchType="";
		Map<String,Object> params = new HashMap<String,Object>();
		
		if(StringUtil.isNotBlank(request.getParameter("startDate"))){
			startDate=request.getParameter("startDate").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("endDate"))){
			endDate=request.getParameter("endDate").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationIdFinder=request.getParameter("organizationIdFinder");
		}
		if(StringUtil.isNotBlank(request.getParameter("searchType"))){
			searchType=request.getParameter("searchType");
		}
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("organizationIdFinder", organizationIdFinder);
		params.put("organizationType", receptionistEveryDayVo.getBasicOperationQueryLevelType());

		params.put("searchType", searchType);
		
		dataPackage = receptionistService.shoolResourceList(dataPackage, receptionistEveryDayVo, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}

	
	
	
	

}
