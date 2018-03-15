package com.eduboss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domainVo.AppVersionVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.AppVersionService;

@RequestMapping(value="/AppVersionController")
@Controller
public class AppVersionController {

	@Autowired 
	private AppVersionService appVersionService;
	
	/**
	 * 得到appversion列表信息
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value="/getAppVersionList")
	@ResponseBody
	public DataPackageForJqGrid getAppVersionList(@ModelAttribute GridRequest gridRequest,AppVersionVo appVersionVo,
			String startDate,String endDate){
		DataPackage dataPackage=new DataPackage(gridRequest);
		dataPackage =this.appVersionService.getAppVersionList(dataPackage,appVersionVo,startDate,endDate);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 添加，修改，(删除)新版本信息
	 */
	@RequestMapping(value="/editAppVersion")
	@ResponseBody
	public Response editAppVersion(@ModelAttribute GridRequest gridRequest,@ModelAttribute AppVersionVo appVersionVo){
		String appVersionId="";
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			appVersionService.deleteAppVersion(appVersionVo.getId());
		}else{
			appVersionId=appVersionService.saveOrUpdateAppVersion(appVersionVo);
		}		
		return new Response(0,appVersionId);
	}
	
	/**
	 * 查询单个版本信息
	 */
	
	@RequestMapping(value="/findAppVersionById")
	@ResponseBody
	public AppVersionVo findAppVersionById(@RequestParam String id){		
		return appVersionService.findAppVersionVoById(id);
	}
	
	/**
	 * App版本升级推送消息到手机端
	 */
	@RequestMapping(value="/pushAppVersionToMobile", method =  RequestMethod.POST)
	@ResponseBody
	public Response pushAppVersionToMobile(String versionStr,String mobileType){	
		appVersionService.pushAppVersionToMobile(versionStr,mobileType);
		return new Response();
	}
	
	
	
	
	
	
}
