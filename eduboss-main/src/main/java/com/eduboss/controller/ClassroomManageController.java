package com.eduboss.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.ClassroomManage;
import com.eduboss.domainVo.ClassroomManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.service.ClassroomManageService;
import com.eduboss.utils.StringUtil;

@Controller
@RequestMapping(value = "/ClassroomManageController")
public class ClassroomManageController {
	
	@Autowired
	private ClassroomManageService classroomManageService;
	
	/**
	 * 查询
	 * @param request
	 * @param gridRequest
	 * @param classroomManageVo
	 * @return
	 */
	@RequestMapping(value = "/getClassroomManageList")
	@ResponseBody
	public DataPackageForJqGrid getClassroomManageList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest,ClassroomManageVo classroomManageVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String branchId = "", organizationId = "",classroom = "",areaStart = "",areaEnd = "",status = "",classType="";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationId = request.getParameter("organizationIdFinder").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("classroomFinder"))){
			classroom = request.getParameter("classroomFinder").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("areaStart"))){
			areaStart = request.getParameter("areaStart").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("areaEnd"))){
			areaEnd = request.getParameter("areaEnd").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("classTypeFinder"))){
			classType = request.getParameter("classTypeFinder").trim();
		}		
		if(StringUtil.isNotBlank(request.getParameter("statusFinder"))){
			if(!request.getParameter("statusFinder").trim().equals("2")){
				status = request.getParameter("statusFinder").trim();
			}
		}else{
			status = "1";
		}
		if (StringUtil.isNotBlank(request.getParameter("branchId"))){
			branchId = request.getParameter("branchId").trim();
		}

		params.put("organizationId", organizationId);
		params.put("branchId", branchId);
		params.put("classroom", classroom);
		params.put("areaStart", areaStart);
		params.put("areaEnd", areaEnd);
		params.put("status", status);
		params.put("classType", classType);
		
		dataPackage = classroomManageService.getClassroomManageList(dataPackage, classroomManageVo, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}

	/**
	 * 新增
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveClassroomManage", method =  RequestMethod.GET)
	@ResponseBody	
	public Response saveClassroomManage(HttpServletRequest request){
		ClassroomManageVo classroomManageVo = new ClassroomManageVo();
		classroomManageVo.setClassroom(request.getParameter("classroomN").trim());
		classroomManageVo.setOrganizationId(request.getParameter("organizationIdN").trim());
		if(StringUtil.isNotBlank(request.getParameter("classTypeN"))){
			classroomManageVo.setClassTypeId(request.getParameter("classTypeN").trim());
		}
		if(StringUtil.isNotBlank(request.getParameter("areaN"))){
			classroomManageVo.setClassArea(new BigDecimal(request.getParameter("areaN").trim()));
		}		
		if(StringUtil.isNotBlank(request.getParameter("memberN"))){
			classroomManageVo.setClassMember(new Integer(request.getParameter("memberN").trim()));
		}		
		
		classroomManageVo.setClassEquitment(request.getParameter("classroomEquitmentN"));
		classroomManageVo.setRemark(request.getParameter("remarkN"));
		classroomManageVo.setStatus(new Integer(request.getParameter("statusN")));
		
		classroomManageService.saveClassroomManage(classroomManageVo);
		
		return new Response();
		
	}
	
	/**
	 * 查找1条
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/findClassroomById", method =  RequestMethod.GET)
	@ResponseBody	
	public ClassroomManageVo findClassroomById(@RequestParam String id){
		return classroomManageService.findClassroomById(id);
	}

	/**
	 * 修改
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyClassroomManage", method =  RequestMethod.GET)
	@ResponseBody	
	public Response modifyClassroomManage(HttpServletRequest request){
		ClassroomManageVo classroomManageVo = new ClassroomManageVo();
		classroomManageVo.setId(request.getParameter("idN").trim());
		classroomManageVo.setClassroom(request.getParameter("classroomN").trim());
		classroomManageVo.setOrganizationId(request.getParameter("organizationIdN").trim());
		if(StringUtil.isNotBlank(request.getParameter("classTypeN"))){
			classroomManageVo.setClassTypeId(request.getParameter("classTypeN").trim());
		}
		if(StringUtil.isNotBlank(request.getParameter("areaN"))){
			classroomManageVo.setClassArea(new BigDecimal(request.getParameter("areaN").trim()));
		}		
		if(StringUtil.isNotBlank(request.getParameter("memberN"))){
			classroomManageVo.setClassMember(new Integer(request.getParameter("memberN").trim()));
		}		
		
		classroomManageVo.setClassEquitment(request.getParameter("classroomEquitmentN"));
		classroomManageVo.setRemark(request.getParameter("remarkN"));
		classroomManageVo.setStatus(new Integer(request.getParameter("statusN")));
		classroomManageVo.setCreatorId(request.getParameter("creatorIdN"));
		classroomManageVo.setCreateTime(request.getParameter("createTimeN"));
		classroomManageService.modifyClassroomManage(classroomManageVo);
		
		return new Response();
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteClassroomManage", method =  RequestMethod.GET)
	@ResponseBody	
	public Response deleteClassroomManage(@RequestParam String id){
		classroomManageService.deleteClassroomManage(id);
		return new Response();
	}	
	
	/**
	 * 修改教室状态
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyClassroomManageStatus", method =  RequestMethod.GET)
	@ResponseBody	
	public Response modifyClassroomManageStatus(@RequestParam String id,@RequestParam String statusChanger){
		ClassroomManageVo classroomManageVo = classroomManageService.findClassroomById(id);
		classroomManageVo.setStatus(new Integer(statusChanger));
		classroomManageService.modifyClassroomManage(classroomManageVo);
		
		return new Response();
	}

	/**
	 * 查找教室列表（自动搜索）放回value-label的list 可以用来用于autocomplete的set值
	 * @param checkType
	 * @param organizationId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getClassroomAutoComplateEnhanced", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getClassroomAutoComplateEnhanced(String checkType, String organizationId) throws Exception {
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse();
		try {
			List<ClassroomManage> classroomList = classroomManageService.getClassroomAutoComplate(checkType);
			if(classroomList != null)
				for (ClassroomManage classroom : classroomList) {
					selectOptionResponse.getValue().put(classroom.getId(), classroom.getClassroom());
				}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return selectOptionResponse;
	}

	/**
	 *
	 * @param campusId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getClassroomByCampus", method =  RequestMethod.GET)
	@ResponseBody
	public List<NameValue> getClassroomByCampus(String campusId) throws Exception {
		List<ClassroomManage> voList = null;
		voList = classroomManageService.getClassroomByCampus(campusId);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for (ClassroomManage crm : voList) {
			nvs.add(SelectOptionResponse.buildNameValue(crm.getClassroom(), crm.getId()));
		}
		return nvs;
	}

	/**
	 * 根据校区Id查找所属教室, for auto completion, 可用于init select的公共方法
	 * @param campusId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getClassroomResponseByCampus", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getClassroomResponseByCampus(String campusId) throws Exception {
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse();
		try {
			List<ClassroomManage> classroomList = classroomManageService.getClassroomByCampus(campusId);
			for (ClassroomManage classroom : classroomList) {
				selectOptionResponse.getValue().put(classroom.getId(), classroom.getClassroom());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return selectOptionResponse;
	}

	/**
	 *获取当前用户所属组织架构的所有的教室
	 * @param campusId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAllCurrentClassroom", method =  RequestMethod.POST)
	@ResponseBody
	public List getAllCurrentClassroom(String campusId,String brenchId) throws Exception {
		String[] campuss=null;
		if(StringUtils.isNotBlank(campusId)) {
			 campuss = campusId.split(",");
		}
		return classroomManageService.getAllCurrentClassroom(campuss,brenchId);
	}
}
