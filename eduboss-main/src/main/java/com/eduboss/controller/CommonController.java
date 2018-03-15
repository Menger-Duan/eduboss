package com.eduboss.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.eduboss.domainVo.OrganizationVo;

import com.eduboss.utils.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.Constants;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.PrintType;
import com.eduboss.common.ProductType;
import com.eduboss.common.RoleCode;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.domain.User;
import com.eduboss.domain.UserJob;
import com.eduboss.domainVo.ProductVo;
import com.eduboss.domainVo.SelectVo;
import com.eduboss.domainVo.StudentSchoolVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.LoginResponse;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.CommonService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.ProductService;
import com.eduboss.service.UserJobService;
import com.eduboss.service.UserService;

/**
 * @classname	CommonAction.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2013-3-16 01:09:47
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/CommonAction")
public class CommonController {
	
	/**
	 * 日志
	 */
	private final static Logger log = Logger.getLogger(CommonController.class);
	
	/**
	 * common service
	 */
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	

	@Autowired
	private UserJobService userJobService;
	
	@Autowired 
	private OrganizationService organizationService;
	
	@RequestMapping(value = "/login", method =  RequestMethod.GET)
	@ResponseBody
	public LoginResponse login(@RequestParam String userName, @RequestParam String password) {
		LoginResponse loginRsp = new LoginResponse();
		try {
			if (userName == null || password == null) {
				throw new ApplicationException(ErrorCode.SYSTEM_ERROR);
			}
			
			loginRsp = commonService.login(userName, password,null);
			loginRsp.setToken(TokenUtil.genToken(loginRsp.getUser()));

			loginRsp.setResultCode(0);
		} catch(ApplicationException e) {
			e.printStackTrace();
			loginRsp.setResultCode(Integer.valueOf(e.getErrorCode().getErrorCode()));
			loginRsp.setResultMessage("请求失败：" + e.getErrorCode().getErrorString());
		} catch(Exception e) {
			loginRsp.setResultCode(999);
			loginRsp.setResultMessage("请求失败" + e.getMessage());
		}
		return loginRsp;
	}
	
	@RequestMapping(value = "/getSelectOption", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getSelectOption(String selectOptionCategory, String parentId) {
		return commonService.getSelectOption(selectOptionCategory, parentId);
	}
	
	@RequestMapping(value = "/getBelongBranchByCampusId", method =  RequestMethod.GET)
	@ResponseBody
	public Organization getBelongBranchByCampusId(String campusId) {
		return userService.getBelongBranchByCampusId(campusId);
	}
	
	@RequestMapping(value = "/getCapumsForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getCapumsForSelection( @ModelAttribute String parentOrgId) {
		String parentOrgLevel = "";
		if (StringUtils.isEmpty(parentOrgId)) {
			Organization org = userService.getBelongCampus();
			if (org != null) {
				parentOrgLevel = org.getOrgLevel();
			}
		}  else {
			Organization org = userService.getOrganizationById(parentOrgId);
			if (org != null) {
				parentOrgLevel = org.getOrgLevel();
			}
		}
		List<Organization> orgs = commonService.getCapumsByOrgLevel(userService.getCurrentLoginUser().getName(), parentOrgLevel);
		
		List<NameValue> nvs = new ArrayList<NameValue>();
		for (Organization so : orgs) {
			nvs.add(so);
		}
		return new SelectOptionResponse(nvs);
	}
	
	@RequestMapping(value = "/getBrenchForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getBrenchForSelection(String organizationId) {
		List<Organization> orgs = commonService.getAllBrench(organizationId);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for (Organization so : orgs) {
			nvs.add(so);
		}
		return new SelectOptionResponse(nvs);
	}
	
	@RequestMapping(value = "/getStaffForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getStaffForSelection(@RequestParam String roleId, @RequestParam String parentOrgId) {
		SelectOptionResponse selectOptionResponse = null;
		try {
			if (roleId == null) {
				throw new ApplicationException(ErrorCode.SYSTEM_ERROR);
			}
			if (StringUtils.isBlank(parentOrgId)) {//如果不传，则默认用员工当前的组织架构
				//User user = userService.getCurrentLoginUser();
				//parentOrgId = user.getOrganizationId();
				parentOrgId = userService.getBelongCampus().getId();
			}
			
			List<User> users = userService.getStaffByRoldIdAndOrgId(roleId, parentOrgId, true, 20);
			
			List<NameValue> nvs = new ArrayList<NameValue>();
			for (User so : users) {
				nvs.add(so);
			}
			
			selectOptionResponse = new SelectOptionResponse(nvs);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return selectOptionResponse;
	}
	
	/**
	 * 根据角色Code 和组织架构查询下
	 * @param roleCode
	 * @param parentOrgId
	 * @return
	 */
	@RequestMapping(value = "/getStaffForByRoleCodeSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getStaffForByRoleCodeSelection( String roleCode,  String parentOrgId) {
		SelectOptionResponse selectOptionResponse = null;
		try {
			if (roleCode == null) {
				roleCode=RoleCode.STUDY_MANAGER.toString();
			}
			if (StringUtils.isBlank(parentOrgId)) {//如果不传，则默认用员工当前的组织架构
				//User user = userService.getCurrentLoginUser();
				//parentOrgId = user.getOrganizationId();
				parentOrgId = userService.getBelongCampus().getId();
				
			}
			
			List<User> users = userService.getStaffByRoldCodeAndOrgId(roleCode, parentOrgId, true, 20);
			
			List<NameValue> nvs = new ArrayList<NameValue>();
			if(users!=null)
				for (User so : users) {
					nvs.add(so);
				}
			
			selectOptionResponse = new SelectOptionResponse(nvs);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return selectOptionResponse;
	}
	
	@RequestMapping(value = "/getStaffForAutoCompeleteByRoleCode", method =  RequestMethod.GET)
	@ResponseBody
	public String getStaffForAutoCompeleteByRoleCode(@RequestParam String term, String roleCode) throws Exception {
		String autoCompeleteData = "";
		
		Organization organization = userService.getBelongBranch();
		if (organization == null) {
			throw new ApplicationException(ErrorCode.USRR_ORG_NOT_FOUND);
		}
		
		List<User> users = userService.getStaffByNameAndRoleCodeAndOrgId(term, roleCode, organization.getId(), true, 20);
		
		for (User user : users) {
			autoCompeleteData += "\"" + user.getName() + "-" + user.getUserId() + "\","; 
		}
		if (autoCompeleteData.length() > 0) {
			autoCompeleteData = autoCompeleteData.substring(0, autoCompeleteData.length() - 1);
		}
		
		return "["+autoCompeleteData+"]";
	}
	
	@RequestMapping(value = "/getReceptionistFollowUpTargetForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getReceptionistFollowUpTargetForSelection() {
		return commonService.getReceptionistFollowUpTargetForSelection();
	}
	

	
	/**
	 * @return
	 */
	@RequestMapping(value = "/getReceptionistGroupByCampus", method =  RequestMethod.GET)
	@ResponseBody
	public List getReceptionistGroupByCampus(String showBelong,@RequestParam(value="job[]",required=false)String[] job) {
		return commonService.getReceptionistGroupByCampus(showBelong,job);
	}

	
	// no reuqest parameter ?
	@RequestMapping(value = "/getRoleForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getRoleForSelection() {
		DataPackage dataPackage = new DataPackage(0, 9999);
		dataPackage = userService.getRoleListForSelection(new Role(), dataPackage);
		
		List<Role> roles = (List<Role>)dataPackage.getDatas();
		List<NameValue> nvs = new ArrayList<NameValue>();
		for (Role so : roles) {
			nvs.add(so);
		}
		return new SelectOptionResponse(nvs);
	}
	
		@RequestMapping(value = "/getUserJobForSelection", method =  RequestMethod.GET)
		@ResponseBody
		public SelectOptionResponse getUserJobForSelection() {
			DataPackage dataPackage = new DataPackage(0, 9999);
			dataPackage = userJobService.getJobListForSelection(new UserJob(), dataPackage);
			
			List<UserJob> userJob = (List<UserJob>)dataPackage.getDatas();
			List<NameValue> nvs = new ArrayList<NameValue>();
			for (UserJob so : userJob) {
				nvs.add(so);
			}
			return new SelectOptionResponse(nvs);
		}
	
	/**
	 * 查询产品
	 * @param productType
	 * @return
	 */
	@RequestMapping(value = "/getProductByTypeSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getProductByTypeSelection(ProductType productType) {
		SelectOptionResponse selectOptionResponse =  new SelectOptionResponse();
		try {
			List<ProductVo> products = productService.getProductByTypeSelection(productType);
			if(products!=null)
				for (ProductVo pro : products) {
					selectOptionResponse.getValue().put( pro.getId(), pro.getName());
				}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return selectOptionResponse;
	}
	
	/**
	 * 获得角色可赋权级别
	 * @return
	 */
	@RequestMapping(value = "/getRoleLevelForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getRoleLevelForSelection() {
		List<NameValue> nvs = new ArrayList<NameValue>();
		User user = userService.getCurrentLoginUser();
		if(user.getRoleLevel()==null){
			user.setRoleLevel(new Short("2"));
		}
		for(int i=1;i<7;i++){
			if(user.getRoleLevel()<=i)
				nvs.add(SelectOptionResponse.buildNameValue(""+i, ""+i));
		}
		return new SelectOptionResponse(nvs);
	}
	
	/**
	 * 根据roleCode 查询的角色
	 * @return
	 */
	@RequestMapping(value = "/getRoleByRoleCodeForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getRoleByRoleCodeForSelection(@RequestParam RoleCode roleCode) {
		List<Role> roles= userService.getRoleByRoleCode(roleCode);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for(Role role : roles){
				nvs.add(SelectOptionResponse.buildNameValue(role.getName(), role.getRoleId()));
		}
		return new SelectOptionResponse(nvs);
	}


	@RequestMapping(value = "/selectOrgOptionOfBranch", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse selectOrgOptionOfBranch(){
		List<Organization> list= commonService.selectOrgOptionOfBranch();
		List<NameValue> nvs = new ArrayList<NameValue>();
		for(Organization org : list){
			nvs.add(SelectOptionResponse.buildNameValue(org.getName(), org.getId()));
		}
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		return selectOptionResponse;

	}

	/**
	 * 获取id及id下属 指定类型 的组织架构数据
	 * @param orgType 默认 分公司BRENCH
	 * @param orgId 默认当前登录用户所属组织架构分公司id
	 * @return
	 */
	@RequestMapping(value = "/selectOrgByIdAndTypeOption", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse selectOrgByIdAndTypeOption(String orgType, String orgId) {
		if (StringUtil.isEmpty(orgType)) {
			orgType = OrganizationType.BRENCH.getValue();
		}
		
		List<OrganizationType> orgTypes = new ArrayList<OrganizationType>();
		for(String type : orgType.split(",")){
			orgTypes.add(OrganizationType.valueOf(type));
		}
		List<Organization> list=commonService.getOrganizatonByOrgIdAndType(orgTypes, orgId);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for(Organization org : list){
				nvs.add(SelectOptionResponse.buildNameValue(org.getName(), org.getId()));
		}
		//nvs.add(SelectOptionResponse.buildNameValue("请选择",""));
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		selectOptionResponse.getValue().put("", "请选择");
		return selectOptionResponse;
	}
	
	
	/**
	 * 获取id及id下属 指定类型 的组织架构数据
	 * @param orgType 默认 分公司BRENCH
	 * @param orgId 默认当前登录用户所属组织架构分公司id
	 * @return
	 */
	@RequestMapping(value = "/getOrgByIdAndTypeOption", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getOrgByIdAndTypeOption(String orgType, String orgId) {
		if (StringUtil.isEmpty(orgType)) {
			orgType = OrganizationType.BRENCH.getValue();
		}
		
		List<Organization> list=commonService.loadOrganizatonByOrgIdAndType(orgType, orgId);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for(Organization org : list){
			nvs.add(SelectOptionResponse.buildNameValue(org.getName(), org.getId()));
		}
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		selectOptionResponse.getValue().put("", "请选择");
		return selectOptionResponse;
	}
	
	
	/**
	 * 获取id及id下属 指定类型 的组织架构数据
	 * @param orgType 默认 分公司BRENCH
	 * @param orgId 默认当前登录用户所属组织架构分公司id
	 * @return
	 */
	@RequestMapping(value = "/getOrgByIdAndTypeList", method =  RequestMethod.GET)
	@ResponseBody
	public List<Organization> getOrgByIdAndTypeList(OrganizationType orgType, String orgId) {
		if (orgType == null) {
			orgType = OrganizationType.BRENCH;
		}
		return commonService.getOrganizatonByOrgIdAndType(orgType, orgId);
	}
	
	/**
	 * 获取当前校区下指定角色的用户
	 * @param roleCode 多个用，号隔开
	 * @return
	 */
	@RequestMapping(value = "/getUserByRoldCodesSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getUserByRoldCodesSelection(HttpServletRequest request, String roleCode,String organizationId) {
		
		SelectOptionResponse selectOptionResponse = null;
        List<User> users = null;
		try {
			if (roleCode == null) {
				roleCode=RoleCode.STUDY_MANAGER.toString();
			}
            if(StringUtils.isNotBlank(organizationId)){
                users =userService.getUserByRoldCodesAndOrgId(roleCode,organizationId);
            }else{
                users =userService.getUserByRoldCodes(roleCode);
            }

			List<NameValue> nvs = new ArrayList<NameValue>();
			if(users!=null){
				nvs.addAll(users);
			}
			selectOptionResponse = new SelectOptionResponse(nvs);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return selectOptionResponse;
	}
	
	
	/**
	 * 获取当前校区下指定角色的用户
	 * @param roleCode 多个用，号隔开
	 * @return
	 */
	@RequestMapping(value = "/getUserByRoldCodesSelection2", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getUserByRoldCodesSelection2(HttpServletRequest request, String organizationId) {
		
		SelectOptionResponse selectOptionResponse = null;
        List<User> users = null;
		try {

			users =userService.getUserByRoldCodesAndOrgId2(organizationId);

			List<NameValue> nvs = new ArrayList<NameValue>();
			if(users!=null){
				nvs.addAll(users);
			}
			selectOptionResponse = new SelectOptionResponse(nvs);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return selectOptionResponse;
	}
	
	@RequestMapping(value = "/getDataDictSelectionByParentId", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getDataDictSelectionByParentId(@RequestParam String parentId){
		SelectOptionResponse selectOptionResponse = null;
		List<NameValue> nvs = new ArrayList<NameValue>();
			List<DataDict> datas =commonService.getDataDictByParentId(parentId);
			if(datas!=null)
				for (DataDict data : datas) {
					nvs.add(SelectOptionResponse.buildNameValue(data.getName(), data.getId()));
				}
			selectOptionResponse = new SelectOptionResponse(nvs);
			selectOptionResponse.getValue().put("", "请选择");
		
		return selectOptionResponse;
	}

	@RequestMapping(value ="/getDataDictAllCity",method = RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getDataDictAllCity(){
		SelectOptionResponse selectOptionResponse = null;
		List<NameValue> nvs = new ArrayList<NameValue>();
		List<DataDict> cities = commonService.getDataDictAllCity();
		for(DataDict dataDict:cities){
			nvs.add(SelectOptionResponse.buildNameValue(dataDict.getName(), dataDict.getId()));
		}
		selectOptionResponse=new SelectOptionResponse(nvs);
		//selectOptionResponse.getValue().put("", "请选择");
		return selectOptionResponse;
	}
	
	/**
	 * 记录打印信息
	 * @param printType
	 * @param businessId
	 */
	@RequestMapping(value = "/recordPrintInfo", method =  RequestMethod.GET)
	@ResponseBody
	public Response recordPrintInfo(@RequestParam String printType, @RequestParam String businessId) {
		commonService.recordPrintInfo(PrintType.valueOf(printType), businessId);
		return new Response();
	}
	
	/**
	 * 分公司所有校区
	 * @return
	 */
	@RequestMapping(value = "/getBrenchCampusByLimit")
	@ResponseBody
	public List<List<Organization>> getBrenchCampusByLimit(String organizationId){
		
		return commonService.getBrenchCampusByLimit(organizationId);
	}
	
	/**
	 * 当前登录用户所属组织架构下的所有校区
	 * @return
	 */
	@RequestMapping(value = "/getCampusByLoginUser")
	@ResponseBody
	public List<List<OrganizationVo>> getCampusByLoginUser() {
		List<List<Organization>> campusByLoginUser;
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			campusByLoginUser = commonService.getCampusByLoginUserNew();
		}else
		{
			campusByLoginUser = commonService.getCampusByLoginUser();
		}
		List<List<OrganizationVo>> result = commonService.mappingOrganizationVo(campusByLoginUser);
		return result;
	}

	/**
	 * #1826分公司建班人小班管理权限优化
	 * 优化：如果一个用户分配了“分公司建班人”的角色，
	 * 那么这个用户的数据权限范围无论是在“分公司”还是在“校区”，
	 * 都可以在小班管理界面建立用户所在分公司所有校区的小班，
	 * 并且可以查看其所在分公司的所有校区的小班；
	 * @return
	 */
	@RequestMapping(value = "/getCampusForSmallClassCourse")
	@ResponseBody
	public List<List<OrganizationVo>> getCampusForSmallClassCourse(){
		List<List<Organization>> campusForSmallClassCourse = commonService.getCampusForSmallClassCourse();
		List<List<OrganizationVo>> result = commonService.mappingOrganizationVo(campusForSmallClassCourse);
		return result;
	}



	/**
	 * 当前登录用户所属组织架构下的所有校区
	 * @return
	 */
	@RequestMapping(value = "/getCampusByLoginUserNew")
	@ResponseBody
	public List<List<OrganizationVo>> getCampusByLoginUserNew() {
		List<List<Organization>> campusByLoginUser = commonService.getCampusByLoginUserNew();
		List<List<OrganizationVo>> result = commonService.mappingOrganizationVo(campusByLoginUser);
		return result;
	}
	
	/**
	 * 获取用户组织下所属分公司包括跨校区分公司
	 */
	@RequestMapping(value="/brenchIncludeOtherCampus")
	@ResponseBody
	public SelectOptionResponse brenchIncludeOtherCampus(String orgType){
		if (StringUtil.isEmpty(orgType)) {
			orgType = OrganizationType.BRENCH.getValue();
		}
		
		List<OrganizationType> orgTypes = new ArrayList<OrganizationType>();
		for(String type : orgType.split(",")){
			orgTypes.add(OrganizationType.valueOf(type));
		}
		List<Organization> list=commonService.getBrenchIncludeOtherCampus(OrganizationType.valueOf(orgType));
		List<NameValue> nvs = new ArrayList<NameValue>();
		for(Organization org : list){
				nvs.add(SelectOptionResponse.buildNameValue(org.getName(), org.getId()));
		}
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		selectOptionResponse.getValue().put("", "请选择");
		return selectOptionResponse;
	}
	
	/**
	 * 根据城市获取学校
	 */
	@RequestMapping(value="/getSchoolByBlCampus")
	@ResponseBody
	public SelectOptionResponse getSchoolByBlCampus(String blCampus){		
		List<StudentSchoolVo> list=commonService.getStuSchools(blCampus);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for(StudentSchoolVo school : list){
				nvs.add(SelectOptionResponse.buildNameValue(school.getName()+"（"+school.getSchoolTypeName()+"）", school.getId()));
		}
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		selectOptionResponse.getValue().put("", "请选择学校");
		return selectOptionResponse;
	}

	/**
	 *
	 * @param provinceId
	 * @param cityId
	 * @return
	 */
	@RequestMapping(value="/getStudentSchoolByRegionId")
	@ResponseBody
	public List<StudentSchoolVo> getStudentSchoolByRegionId(String provinceId, String cityId){
		List<StudentSchoolVo> studentSchool = commonService.getStudentSchool(provinceId, cityId);
		return studentSchool;
	}

	/**
	 *
	 * @param provinceId
	 * @param cityId
	 * @return
	 */
	@RequestMapping(value="/getCampusByRegionId")
	@ResponseBody
	public List<OrganizationVo> getCampusByRegionId(String provinceId, String cityId){
		return commonService.getOrganizationByRegionId(provinceId, cityId);
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = "/getTransactionUUID")
	@ResponseBody
	public String getTransactionUUID() {
		return IdGenerator.idGenerator().nextId();
	}
	
	/**
	 * 当前登录用户所属组织架构下的所有校区
	 * @return
	 */
	@RequestMapping(value = "/getAllStudentCampus")
	@ResponseBody
	public List<List<Organization>> getAllStudentCampus(String studentId) {
		return commonService.getAllStudentCampus(studentId);
	}
	
	
	/**
	 * 获取当前时间
	 * @return
	 */
	@RequestMapping(value = "/getCurrentDateTime")
	@ResponseBody
	public String getCurrentDateTime() {
		return commonService.getCurrentDateTime();
	}

	/**
	 * 获取所有的校区及其对应的校区
	 * @return
	 */
	@RequestMapping(value = "/getBranchCampus",method = RequestMethod.GET)
	@ResponseBody
	public List<SelectVo> getBranchCampus() {
		return commonService.getBranchCampus();
	}
	
	@RequestMapping(value = "/getCampusForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getCampusForSelection( @ModelAttribute String parentOrgId) {
		String parentOrgLevel = "";
		if (StringUtils.isEmpty(parentOrgId)) {
			Organization org = userService.getBelongCampus();
			if (org != null) {
				parentOrgLevel = org.getOrgLevel();
			}
		}  else {
			Organization org = userService.getOrganizationById(parentOrgId);
			if (org != null) {
				parentOrgLevel = org.getOrgLevel();
			}
		}
		List<Organization> orgs = commonService.getCapumsByOrgLevel(userService.getCurrentLoginUser().getName(), parentOrgLevel);
		
		List<NameValue> nvs = new ArrayList<NameValue>();
		for (Organization so : orgs) {
			nvs.add(so);
		}
		return new SelectOptionResponse(nvs);
	}
	
	@RequestMapping(value = "/getUsersByRoleSign", method =  RequestMethod.GET)
	@ResponseBody
	public List getUsersByRoleSign(String showBelong,@RequestParam(value="job[]",required=false)String[] job) {
		return commonService.getUsersByRoleSign(showBelong, job);
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/getAllCampusForSelection",method =  RequestMethod.GET)
	@ResponseBody
	public List getAllCampusForSelection(String orgType){
		//修改接口 如果orgType为空则获取原来的全部分公司 如果orgType不为空 则获取当前登陆者所在的分公司 
		//xiaojinwang 20170808 
		return commonService.getAllCampusForSelection(orgType);
	}


	@RequestMapping(value = "/sendVerifyCode",method =  RequestMethod.GET)
	@ResponseBody
	int sendVerifyCode(String contact,String phoneNum){
		return commonService.sendVerifyCode(contact,phoneNum);
	}

	@RequestMapping(value = "/verifyUserCode",method =  RequestMethod.GET)
	@ResponseBody
	Response verifyUserCode(String customerId, @RequestParam String verifyCode,@RequestParam  int id) {
		return commonService.verifyUserCode(customerId,verifyCode,id);
	}
	
	//跟进校区id获取校区电话	
	@RequestMapping(value = "/getCampusContact",method =  RequestMethod.GET)
	@ResponseBody
	public String getCampusContact(String campusId) {
		Organization campus = organizationService.findById(campusId);
		if(campus!=null){
			return campus.getContact();
		}else{
			return null;
		}
	}
	
	/**
    * 获取验证码
    * @param request
    * @param phone
    * @return
    */
    @RequestMapping("/getVerification")
    @ResponseBody
    public Response getVerification(HttpServletRequest request, String phone) {
        commonService.getVerification(request, phone);
        return new Response();
    }

    /**
     * 校验验证码
     * @param request
     * @param response
     * @param phone
     * @param verification
     * @return
     */
    @RequestMapping(value="/checkVerification", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkVerification(HttpServletRequest request, @RequestParam(value="phone",required=true) String phone,
                                                        @RequestParam(value="verification",required=true) String verification)
    {
        //对用户输入的账号密码进行转码
        phone = StringEscapeUtils.escapeSql(phone);
        verification = StringEscapeUtils.escapeSql(verification);
        // 手动校验
        if (!JedisUtil.exists(Constants.REDIS_PRE_VERIFICATION_INVALID_KEY + request.getSession().getId() + phone)) {
            throw new ApplicationException(ErrorCode.VERIFICATION_INVALID);
        }
        if (!JedisUtil.get(Constants.REDIS_PRE_VERIFICATION_INVALID_KEY + request.getSession().getId() + phone).equals(verification)) {
            throw new ApplicationException(ErrorCode.VERIFICATION_ERROR);
        }
        return true;

    }


}
