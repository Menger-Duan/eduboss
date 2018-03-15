package com.eduboss.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.dto.DataPackage;
import com.eduboss.service.MiniClassCourseScheduleDayService;
import com.eduboss.utils.StringUtil;
/**
 * 小班课表（日期）controller
 * @author win 7
 *
 */
@Controller
@RequestMapping(value = "/MiniClassCourseScheduleDayController")
public class MiniClassCourseScheduleDayController {
	
	@Autowired
	MiniClassCourseScheduleDayService miniClassCourseScheduleDayService;

	/**
	 * 查询
	 * @param request
	 * @param gridRequest
	 * @param classroomManageVo
	 * @return
	 */
	@RequestMapping(value = "/getMiniClassCourseScheduleDayList")
	@ResponseBody
	public DataPackage getMiniClassCourseScheduleDayList(HttpServletRequest request){
		DataPackage dataPackage = new DataPackage();
		String organizationId = "",date = "",classroom = "",miniClass = "",miniClassTypeId="";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("organizationId"))){
			organizationId = request.getParameter("organizationId").trim();
		}else{
			organizationId = "";
		}
		if(StringUtil.isNotBlank(request.getParameter("date_search"))){
			date = request.getParameter("date_search").trim();
		}	
		if(StringUtil.isNotBlank(request.getParameter("classroom"))){
			classroom = request.getParameter("classroom").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("miniClass"))){
			miniClass = request.getParameter("miniClass").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("miniClassTypeId"))){
			miniClassTypeId = request.getParameter("miniClassTypeId").trim();
		}
		params.put("organizationId", organizationId);
		params.put("date", date);
		params.put("classroom", classroom);
		params.put("miniClass", miniClass);
		params.put("miniClassTypeId",miniClassTypeId);
		return miniClassCourseScheduleDayService.getMiniClassCourseScheduleDayList(dataPackage, params);
	}
	
}
