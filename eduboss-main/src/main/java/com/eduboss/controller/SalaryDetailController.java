package com.eduboss.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.Organization;
import com.eduboss.domainVo.AutoCompleteOptionVo;
import com.eduboss.domainVo.SalaryDetailVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.SalaryDetailService;
import com.eduboss.utils.StringUtil;

@Controller
@RequestMapping(value = "/SalaryDetailController")
public class SalaryDetailController {
	
	@Autowired
	private SalaryDetailService salaryDetailService;
	
	/**
	 * 查找表单
	 * @param request
	 * @param gridRequest
	 * @param summaryLogVo
	 * @return
	 */
	@RequestMapping(value = "/getSalaryDetailList")
	@ResponseBody
	public DataPackageForJqGrid getSalaryDetailList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest,SalaryDetailVo salaryDetailVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String startDate = "",endDate="",staff="",organizationId="";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("startDate"))){
			startDate =request.getParameter("startDate");
		}
		if(StringUtil.isNotBlank(request.getParameter("endDate"))){
			endDate = request.getParameter("endDate");
		}
		if(StringUtil.isNotBlank(request.getParameter("staffNameFinder"))){
			staff = request.getParameter("staffNameFinder").trim();
		}
		if(StringUtil.isNotBlank(request.getParameter("organizationIdFinder"))){
			organizationId = request.getParameter("organizationIdFinder").trim();
		}
		
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("staff", staff);
		params.put("organizationId", organizationId);
	
		dataPackage = salaryDetailService.getsalaryDetailVoList(dataPackage, salaryDetailVo, params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;

	}
	
	/**
	 * 通过userid查找所属组织机构信息
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getOrganizationByUserId")
	@ResponseBody	
	public Organization getOrganizationByUserId(@RequestParam(required=false) String userId){
		return salaryDetailService.getOrganizationByUserId(userId);
	}
	
	/**
	 * 新增一条
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveSalaryDetailNew")
	@ResponseBody	
	public Response saveSalaryDetailNew(HttpServletRequest request){
		SalaryDetailVo salaryDetailVo = new SalaryDetailVo();
		salaryDetailVo.setStaffId(request.getParameter("staffIdN").trim());
		salaryDetailVo.setOrganizationId(request.getParameter("organizationIdN").trim());
		salaryDetailVo.setSalaryBase(new BigDecimal(request.getParameter("salaryBaseN")));
		salaryDetailVo.setSalaryBonus(new BigDecimal(request.getParameter("salaryBonusN")));
		salaryDetailVo.setSalaryOther(new BigDecimal(request.getParameter("salaryOtherN")));
		salaryDetailVo.setOtherDescribe(request.getParameter("otherDescribeN").trim());
		salaryDetailVo.setSalaryTime(request.getParameter("salaryTimeN"));
		
		salaryDetailService.saveSalaryDetailNew(salaryDetailVo);
		return new Response();
	}	
	
	/**
	 * 查找一条信息by id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findSalaryDetailById")
	@ResponseBody	
	public SalaryDetailVo findSalaryDetailById(@RequestParam(required=false) String id){
		
		return salaryDetailService.findSalaryDetailById(id);
	}
	
	/**
	 * 删除1条
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteSalaryDetail")
	@ResponseBody	
	public Response deleteSalaryDetail(@RequestParam(required=false) String id){
		salaryDetailService.deleteSalaryDetail(id);
		return new Response();
	}
	
	/**
	 * 修改一条
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifySalaryDetail")
	@ResponseBody	
	public Response modifySalaryDetail(HttpServletRequest request){
		SalaryDetailVo salaryDetailVo = new SalaryDetailVo();
		salaryDetailVo.setId(request.getParameter("idN").trim());
		salaryDetailVo.setStaffId(request.getParameter("staffIdN").trim());
		salaryDetailVo.setOrganizationId(request.getParameter("organizationIdN").trim());
		salaryDetailVo.setSalaryBase(new BigDecimal(request.getParameter("salaryBaseN")));
		salaryDetailVo.setSalaryBonus(new BigDecimal(request.getParameter("salaryBonusN")));
		salaryDetailVo.setSalaryOther(new BigDecimal(request.getParameter("salaryOtherN")));
		salaryDetailVo.setOtherDescribe(request.getParameter("otherDescribeN").trim());
		salaryDetailVo.setSalaryTime(request.getParameter("salaryTimeN"));
		
		salaryDetailService.saveSalaryDetailNew(salaryDetailVo);  	//添加id后，保存数据将覆盖旧数据故复用save方法
		return new Response();
	}		
	
	/**
	 * 归档
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/fileSalaryDetail")
	@ResponseBody	
	public Response fileSalaryDetail(@RequestParam(required=false) String id){
		salaryDetailService.fileSalaryDetail(id);
		return new Response();
	}
	
	/**
	 * 查找用户组织架构、主职位（数据权限限定）
	 * @param term
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getUserForPerformanceLimited", method =  RequestMethod.GET)
	@ResponseBody
	public List<AutoCompleteOptionVo> getUserForPerformanceLimited(@RequestParam String term) throws Exception {
		return salaryDetailService.getLimitUserAutoComplate(term);
	}
	
}
