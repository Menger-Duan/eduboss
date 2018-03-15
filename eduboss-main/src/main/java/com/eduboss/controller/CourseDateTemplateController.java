package com.eduboss.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.CourseDateTemplate;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDetailsImpl;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.CourseDateTemplateService;

/**
 * 排课日期模板
 * 
 * @author ndd 2014-8-18
 */
@Controller
@RequestMapping(value="/CourseDateTemplateController")
public class CourseDateTemplateController {

	@Autowired
	private CourseDateTemplateService courseDateTemplateService;

	@RequestMapping(value = "/getCourseDateTemplateList")
	@ResponseBody
	public DataPackageForJqGrid getCourseDateTemplateList(GridRequest gridRequest, CourseDateTemplate courseDateTemplate) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = courseDateTemplateService.getCourseDateTemplateList(courseDateTemplate, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 根据用户id 和日期查询排课日期模板
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value = "/getCourseDateTemplateByUserIdAndDate")
	@ResponseBody
	public List<CourseDateTemplate> getCourseDateTemplateByUserIdAndDate(String userId,String startDate,String endDate) {
		if(StringUtils.isEmpty(userId)){
			//User user = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
			//userId=user.getUserId();
			new ApplicationException(ErrorCode.PARAMETER_EMPTY);
			return null;
		}
		 List<CourseDateTemplate> list=null;
		try {
			 list=courseDateTemplateService.getCourseDateTemplateByUserIdAndDate(userId,startDate,endDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return list;
	}

	@RequestMapping(value = "/editCourseDateTemplate")
	@ResponseBody
	public Response editCourseDateTemplate(GridRequest gridRequest, CourseDateTemplate courseDateTemplate) {
		courseDateTemplateService.saveOrUpdateCourseDateTemplate(courseDateTemplate);
		return new Response();
	}

	@RequestMapping(value = "/deleteCourseDateTemplate")
	@ResponseBody
	public Response deleteCourseDateTemplate(GridRequest gridRequest, CourseDateTemplate courseDateTemplate) {
		if (courseDateTemplate == null || courseDateTemplate.getTemplateId() == null) {
			throw new ApplicationException(ErrorCode.PARAMETER_EMPTY);
		}
		courseDateTemplateService.deleteCourseDateTemplate(courseDateTemplate);
		return new Response();
	}
}
