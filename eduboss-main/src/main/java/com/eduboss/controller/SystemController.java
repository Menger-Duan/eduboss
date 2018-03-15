package com.eduboss.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.eduboss.common.*;
import com.eduboss.domainVo.*;
import com.eduboss.service.*;
import com.eduboss.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eduboss.dao.SystemNoticeDao;
import com.eduboss.domain.DistributableUserJob;
import com.eduboss.domain.DistributableUserJobPk;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Resource;
import com.eduboss.domain.ResourcePool;
import com.eduboss.domain.Role;
import com.eduboss.domain.SchedulerExecuteLog;
import com.eduboss.domain.SystemConfig;
import com.eduboss.domain.SystemNotice;
import com.eduboss.domain.SystemNoticeReply;
import com.eduboss.domain.SystemNoticeUser;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domain.UserJob;
import com.eduboss.domain.UserOperationLog;
import com.eduboss.dto.CurrentLoginUserResponse;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;

@Controller
@RequestMapping(value = "/SystemAction")
@GrayClassAnnotation
public class SystemController  {
	private final static String userArchives = "/tmp/uploadfile/userArchives";
	private final static Logger log = Logger.getLogger(SystemController.class);
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	@InitBinder("systemNotice")  
    public void initBinder1(WebDataBinder binder){  //默认设置是256，公告的组织架构超过了256故有此设置
		binder.setAutoGrowCollectionLimit(1000);
    } 
	
	@Autowired
	private UserService userService;
	
	@Autowired  
	private  HttpServletRequest request;
	
	@Autowired
	private SystemNoticeService systemNoticeService;
	
	@Autowired
	private SystemConfigService systemConfigService;
	
//	@Autowired
//	private SystemService systemService;
	@Autowired
	private DataDictService dataDictService;
	
	@Autowired
	private SystemNoticeDao systemNoticeDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private SystemNoticeReplyService systemNoticeReplyService;

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private UserOperationLogService userOperationLogService;
	
	@Autowired
	private SchedulerExecuteLogService schedulerExecuteLogService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private UserJobService userJobService;
	
	@Autowired
	private MyCollectionService myCollectionService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserDeptJobService userDeptjobService;
	
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private RegionService regionService;
	
	@Autowired
	private DistributableUserJobService distributableUserJobService;
	
	@Autowired
	private ResourcePoolService resourcePoolService;

	@Autowired
	SchedulerCountService schedulerCountService;



	/**
	 * 获取省份列表（省份内含下属城市）
	 * @return
	 */
	@RequestMapping("getRegionList")
	@ResponseBody
	public List<RegionVo> getRegionList(){
		return regionService.getAllProvinces();
	}

	@RequestMapping(value = "/getCitys", method =  RequestMethod.GET)
	@ResponseBody
	public List<RegionVo> getCitys(String provinceID){
		List<RegionVo> list=regionService.getCitys(provinceID);
		return list;
	}

	@RequestMapping(value = "/getCounties", method =  RequestMethod.GET)
	@ResponseBody
	public List<RegionVo> getCounties(String cityId){
		List<RegionVo> counties = regionService.getCounties(cityId);
		return counties;
	}
	
	/**
	 * 获取登录用户信息
	 * @returnl
	 */
	@RequestMapping(value = "/getLoginUserInfo", method =  RequestMethod.GET)
	@ResponseBody
	public CurrentLoginUserResponse getLoginUserInfo() {
		User usr = userService.getCurrentLoginUser();
		CurrentLoginUserResponse userInfo = new CurrentLoginUserResponse();
		userInfo.setUserId(usr.getUserId());
		userInfo.setName(usr.getName());
		userInfo.setAccount(usr.getAccount());
		userInfo.setParentOrgId(userService.getOrganizationById(usr.getOrganizationId()).getParentId());
		userInfo.setOrgType(userService.getOrganizationById(usr.getOrganizationId()).getOrgType().toString());
		userInfo.setOrgLevel(userService.getOrganizationById(usr.getOrganizationId()).getOrgLevel());
		userInfo.setOrganizationId(usr.getOrganizationId());
		userInfo.setOrganizationType(userService.getBelongCampus().getOrgType());
		userInfo.setServerDate(DateTools.getCurrentDate());
		userInfo.setServerTime(DateTools.getCurrentTime());
		userInfo.setAliPath(PropertiesUtils.getStringValue("oss.access.url.prefix"));//阿里云路径
		userInfo.setAliVideoPath(PropertiesUtils.getStringValue("oss.access.host"));
	    userInfo.setCcpAppId(PropertiesUtils.getStringValue("ccpAppId"));//应用Id
	    userInfo.setCcpAppToken(PropertiesUtils.getStringValue("ccpAppToken"));//应用token
		userInfo.setCcpAccount(usr.getCcpAccount());
		userInfo.setCcpPwd(usr.getCcpPwd());
		userInfo.setCcpStatus(usr.getCcpStatus());
		userInfo.setInstitution(PropertiesUtils.getStringValue("institution"));
		//获取归属集团
		Organization grounp = userService.getBelongGroup();
		if (grounp != null) {
			userInfo.setGrounpId(grounp.getId());
			userInfo.setGrounpName(grounp.getName());
			
			if (OrganizationType.GROUNP.equals(grounp.getOrgType())) {
				userInfo.setIsBlGrounp("true");
			}
		}
		
		//获取归属分公司
		Organization brench = userService.getBelongBranch();
		if (brench != null) {
			userInfo.setBrenchId(brench.getId());
			userInfo.setBrenchName(brench.getName());
			
			if (OrganizationType.BRENCH.equals(brench.getOrgType())) {
				userInfo.setIsBlBrench("true");
			}
		}
		//获取归属校区
//		Organization campus = organizationService.findById(usr.getOrganizationId());
//		if (campus != null) {
//			userInfo.setCampusId(campus.getId());
//			userInfo.setCampusName(campus.getName());
//			
//			if (OrganizationType.CAMPUS.equals(campus.getOrgType())) {
//				userInfo.setIsBlCampus("true");
//			}
//		}
		Organization belongCampus = userService.getBelongCampus();
		if (belongCampus != null) {
			userInfo.setCampusId(belongCampus.getId());
			userInfo.setCampusName(belongCampus.getName());
			
			if (OrganizationType.CAMPUS==belongCampus.getOrgType()) {
				userInfo.setIsBlCampus("true");
			}
		}
		
		//把所有权限拼起来
		List<Role> roles = userService.getRoleByUserId(usr.getUserId());
					
		
		//判断当前用户是否具有前台和其他非基本权限的角色 
		Boolean flag = false;
		Boolean hasReceptionist = false;
		
		String roleIds = "";
		String roleNames = "";
		for (Role role : roles) {
			if(!flag){
				if(role.getRoleCode()!=null && role.getRoleCode()==RoleCode.RECEPTIONIST){
					hasReceptionist = true;
				}
				if(hasReceptionist){
					flag = true;
				}
			}
			roleIds += role.getRoleId() + ",";
			roleNames += role.getName() + ",";
		}
		if(roles.size()>2 && hasReceptionist){
//			userInfo.setIsReceptionist(true);
			userInfo.setReceptionist(true);
			boolean modeValue = userService.currentLoginUserIsReceptionistMode(roles);
			if (modeValue){
				userInfo.setReceptionistValue(0);//前台模式
			}else {
				userInfo.setReceptionistValue(1);//普遍模式
			}
		}else {
//			userInfo.setIsReceptionist(false);
			userInfo.setReceptionist(false);
		}
		if (roleIds.length() > 0) {
			roleIds = roleIds.substring(0, roleIds.length() - 1);
		}
		if (roleNames.length() > 0) {
			roleNames = roleNames.substring(0, roleNames.length() - 1);
		}
		userInfo.setRoleId(roleIds);
		userInfo.setRoleName(roleNames);
		userInfo.setRoleCode(usr.getRoleCode());
		List<UserDeptJob> userDeptJobs = userDeptjobService.findDeptJobByUserId(usr.getUserId());
		Map<String, Integer> deptJobs = new HashMap<String, Integer>();
		for (UserDeptJob userDeptJob : userDeptJobs) {
			Organization org = organizationService.findById(userDeptJob.getDeptId());
			String belongId = org.getBelong() != null ? org.getBelong() : org.getId();
			deptJobs.put(belongId + "_" + userDeptJob.getJobId(), 1);
		}
		userInfo.setDeptJobs(deptJobs);
		return userInfo;
	}
	
	@RequestMapping(value = "/getMenuList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackage getMenuList() throws Exception {
		log.info("getMenuList() start.");
		DataPackage dataPackage = new DataPackage();
		dataPackage.setDatas(userService.getUserMenus());
		log.info("getMenuList() end.");
		return dataPackage;
	}

	/**
	 * 获取按钮权限资源
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getButtonAuthTags", method =  RequestMethod.GET)
	@ResponseBody
	public String getButtonAuthTags() throws Exception {
		log.info("getButtonAuthTags() start.");
		String authFuntionTagsStr = "";
		List<Resource> reses = userService.getUserButtons();
		for (Resource res : reses) {
			authFuntionTagsStr += res.getRtag() + ",";
		}
		log.info("getButtonAuthTags() end.");
		return authFuntionTagsStr;
	}

	/**
	 * 获取菜单权限资源
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMenuAuthTags", method =  RequestMethod.GET)
	@ResponseBody
	public String getMenuAuthTags() throws Exception {
		log.info("getMenuAuthTags() start.");
		String authFuntionTagsStr = "";
		List<Resource> reses = userService.getUserMenus();
		for (Resource res : reses) {
			authFuntionTagsStr += res.getRtag() + ",";
		}
		log.info("getMenuAuthTags() end.");
		return authFuntionTagsStr;
	}
	
	@RequestMapping(value = "/getUserList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getUserList(HttpServletRequest request, @ModelAttribute User user, @ModelAttribute GridRequest gridRequest) throws Exception {
		log.info("getUserList() start.");
		/**********如果是本校区老师管理，则只查询职位是老师，并且是本校区的老师 2015-01-15******************/
		if(StringUtil.isNotBlank(request.getParameter("isLocalTeacher")) && "true".equals(request.getParameter("isLocalTeacher"))){
			user.setRoleId("ROL00000002");
			user.setOrganizationId(userService.getCurrentLoginUser().getOrganizationId());
		}
		
		/************************************************************************************/
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = userService.getUserList(user, dataPackage);
		
		log.info("getUserList() end.");
		return new DataPackageForJqGrid(dataPackage);
	}

	@RequestMapping(value = "/editUserTeacherAttribute", method =  RequestMethod.POST)
	public void editUserTeacherAttribute(UserTeacherAttributeVo attributeVo){
		if (StringUtil.isBlank(attributeVo.getUserId())){
			throw new ApplicationException("用户id不能为空");
		}
		userService.editUserTeacherAttribute(attributeVo);
	}

	@RequestMapping(value = "/findUserTeacherAttribute", method =  RequestMethod.GET)
	@ResponseBody
	public UserTeacherAttributeVo findUserTeacherAttribute(@RequestParam String userId){
		return userService.findUserTeacherAttribute(userId);
	}
	
	@RequestMapping(value = "/editUser", method =  RequestMethod.GET)
	@ResponseBody
	public Response editUser(@ModelAttribute GridRequest gridRequest, @ModelAttribute User user, HttpServletRequest request){

		//校区营运主任、咨询师转校区 释放客户资源资源
		Organization organization = null;
		ResourcePool resourcePool = null;
		if(StringUtil.isNotBlank(user.getUserId())){
			List<Role> roles =userService.getRoleByUserId(user.getUserId());
			int zxs_size =0;int xqyyzr_size =0;
			if(roles!=null && roles.size()>0){
				for(Role role:roles){	
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.CONSULTOR)){
						zxs_size ++;
					}
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.CAMPUS_OPERATION_DIRECTOR)){
						xqyyzr_size ++;
					}
				}
				if(zxs_size>0||xqyyzr_size>0){
					organization = userService.getBelongCampusByUserId(user.getUserId());
					resourcePool = resourcePoolService.getBelongBranchResourcePool(user.getUserId(), OrganizationType.CAMPUS.getValue());
				}
			}
		}
		
		//String loginAddress = request.getRequestURL().toString();
		//String address = loginAddress.substring(0, loginAddress.indexOf("/eduboss"))+"/eduboss/index.html";
		String address = PropertiesUtils.getStringValue("requestURL_address")+"/eduboss/index.html";
		user.setLoginAddress(address);

		
		Response response=new Response();
		try{
			if ("del".equalsIgnoreCase(gridRequest.getOper())) {
				userService.deleteUsers(user.getUserId());
			} else {
				if(StringUtils.isNotBlank(user.getUserId()) && mailService.isMailSysInUse() == true) {  //修改开启了邮件系统的用户	
						User dbUser = userService.getUserById(user.getUserId());
						String oldUserName = dbUser.getRealName();
						String oldUserDeptId = dbUser.getDeptId();
						String oldUserJobId = dbUser.getJobId();
						//旧的部门+职位
						List<UserDeptJob> oldDeptJobList = userService.getUserDeptJobList(user.getUserId());
						userService.saveOrUpdateUser(user);
						if(StringUtils.isNotBlank(dbUser.getMailAddr())) {
						    User newUser = userService.getUserById(user.getUserId());
							//新
							String newUserName = newUser.getRealName();
							String newUserDeptId = newUser.getDeptId();  //新的部门
							String newUserJobId = newUser.getJobId();
							//新的部门+职位
							List<UserDeptJob> newDeptJobList = userService.getUserDeptJobList(newUser.getUserId());
							 //更新用户姓名，或换了主部门, 主职位变了
							if(StringUtils.isNotBlank(newUserName) && !newUserName.equals(oldUserName) 
								|| StringUtils.isNotBlank(newUserDeptId) && !newUserDeptId.equals(oldUserDeptId)
								|| StringUtils.isNotBlank(newUserJobId) && !newUserJobId.equals(oldUserJobId) ){
								mailService.updateMailUser(newUser);
							}
							//部门职位变了
							if(judgeUserDeptJobIsChange(oldDeptJobList, newDeptJobList) == true) {							
								mailService.updateMailListUserOnDeptJobChange(newUser, oldDeptJobList, newDeptJobList);
							}
						}				
				}else{
					userService.saveOrUpdateUser(user);
					
					if(organization!=null){
						Organization org = userService.getBelongCampusByUserId(user.getUserId());
						if(org!=null && resourcePool!=null && !org.getId().equals(organization.getId())){
							//换校区了 放进队列里面处理						
							JedisUtil.lpush("changeCampusUserInfo".getBytes(), JedisUtil.ObjectToByte(user.getUserId()+"&"+resourcePool.getOrganizationId())); 
						}
					}
					
				}
			}
		}catch(DataIntegrityViolationException e){ 
			response.setResultCode(-1);
			response.setResultMessage("登录账号已存在！");
		}
		return response;
	}

	@RequestMapping(value = "/editUserForAdvance", method =  RequestMethod.GET)
	@ResponseBody
	public Response editUserForAdvance(@ModelAttribute GridRequest gridRequest, @ModelAttribute User user, HttpServletRequest request){

		//校区营运主任、咨询师转校区 释放客户资源资源
		Organization organization = null;
		ResourcePool resourcePool = null;
		if(StringUtil.isNotBlank(user.getUserId())){
			List<Role> roles =userService.getRoleByUserId(user.getUserId());
			int zxs_size =0;int xqyyzr_size =0;
			if(roles!=null && roles.size()>0){
				for(Role role:roles){
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.CONSULTOR)){
						zxs_size ++;
					}
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.CAMPUS_OPERATION_DIRECTOR)){
						xqyyzr_size ++;
					}
				}
				if(zxs_size>0||xqyyzr_size>0){
					organization = userService.getBelongCampusByUserId(user.getUserId());
					resourcePool = resourcePoolService.getBelongBranchResourcePool(user.getUserId(), OrganizationType.CAMPUS.getValue());
				}
			}
		}

		//String loginAddress = request.getRequestURL().toString();
		//String address = loginAddress.substring(0, loginAddress.indexOf("/eduboss"))+"/eduboss/index.html";
		String address = PropertiesUtils.getStringValue("requestURL_address")+"/eduboss/index.html";
		user.setLoginAddress(address);


		Response response=new Response();
		try{
			if ("del".equalsIgnoreCase(gridRequest.getOper())) {
				userService.deleteUsers(user.getUserId());
			} else {
				if(StringUtils.isNotBlank(user.getUserId()) && mailService.isMailSysInUse() == true) {  //修改开启了邮件系统的用户
					User dbUser = userService.getUserById(user.getUserId());
					String oldUserName = dbUser.getRealName();
					String oldUserDeptId = dbUser.getDeptId();
					String oldUserJobId = dbUser.getJobId();
					//旧的部门+职位
					List<UserDeptJob> oldDeptJobList = userService.getUserDeptJobList(user.getUserId());
					//userService.saveOrUpdateUser(user);
					userService.saveOrUpdateUserForAdvance(user);
					if(StringUtils.isNotBlank(dbUser.getMailAddr())) {
						User newUser = userService.getUserById(user.getUserId());
						//新
						String newUserName = newUser.getRealName();
						String newUserDeptId = newUser.getDeptId();  //新的部门
						String newUserJobId = newUser.getJobId();
						//新的部门+职位
						List<UserDeptJob> newDeptJobList = userService.getUserDeptJobList(newUser.getUserId());
						//更新用户姓名，或换了主部门, 主职位变了
						if(StringUtils.isNotBlank(newUserName) && !newUserName.equals(oldUserName)
								|| StringUtils.isNotBlank(newUserDeptId) && !newUserDeptId.equals(oldUserDeptId)
								|| StringUtils.isNotBlank(newUserJobId) && !newUserJobId.equals(oldUserJobId) ){
							mailService.updateMailUser(newUser);
						}
						//部门职位变了
						if(judgeUserDeptJobIsChange(oldDeptJobList, newDeptJobList) == true) {
							mailService.updateMailListUserOnDeptJobChange(newUser, oldDeptJobList, newDeptJobList);
						}
					}
				}else{
					userService.saveOrUpdateUserForAdvance(user);

					if(organization!=null){
						Organization org = userService.getBelongCampusByUserId(user.getUserId());
						if(org!=null && resourcePool!=null && !org.getId().equals(organization.getId())){
							//换校区了 放进队列里面处理
							JedisUtil.lpush("changeCampusUserInfo".getBytes(), JedisUtil.ObjectToByte(user.getUserId()+"&"+resourcePool.getOrganizationId()));
						}
					}

				}
			}
		}catch(DataIntegrityViolationException e){
			response.setResultCode(-1);
			response.setResultMessage("登录账号已存在！");
		}
		return response;
	}

	
	private boolean judgeUserDeptJobIsChange(List<UserDeptJob> oldDeptJobList, List<UserDeptJob> newDeptJobList) {
		boolean updateMailListFalg = false;
		if((oldDeptJobList == null || oldDeptJobList.size() == 0) && (newDeptJobList != null && newDeptJobList.size() > 0)) {
			updateMailListFalg = true;
		}else if((oldDeptJobList != null && oldDeptJobList.size() > 0) && (newDeptJobList != null && newDeptJobList.size() > 0)){
			 if(oldDeptJobList.size() != newDeptJobList.size()){
					updateMailListFalg = true;
			 }else {
				 for(UserDeptJob oldUdj : oldDeptJobList) {
					 boolean matchFalg = false;
					 String oldDeptId = oldUdj.getDeptId();
					 String oldJobId = oldUdj.getJobId();
					 for(UserDeptJob newUdj : newDeptJobList){
						 String newDeptId = newUdj.getDeptId();
						 String newJobId = newUdj.getJobId();
						 if(oldDeptId.equals(newDeptId) && oldJobId.equals(newJobId)) {
							 matchFalg = true;
						 }
					 }
					 if(matchFalg == false) {  //不一样
						 updateMailListFalg = true;
						 break;
					 }
				 }
			 }
		}
		return updateMailListFalg;
	}
	
	@RequestMapping(value = "/editUserArchives", method =  RequestMethod.POST)
	@ResponseBody
	public Response editUserArchives(@ModelAttribute GridRequest gridRequest, @ModelAttribute User user,@RequestParam("userArchives") MultipartFile userArchives) throws Exception {
		if(userArchives != null && !userArchives.isEmpty()){
			File file = new File(SystemController.userArchives);
			if(!file.exists()){
				file.mkdir();
			}
			String fileName = SystemController.userArchives+"/"+DateTools.getCurrentDateTime().replace(" ", "").replaceAll(":", "").replaceAll("-", "")+userArchives.getOriginalFilename();
			boolean bool = com.eduboss.utils.FileUtil.readInputStreamToFile(userArchives.getInputStream(), fileName);
			if(bool){
				user.setArchivesPath(fileName);
			}else{
				throw new ApplicationException("档案上传失败，请稍后再试");
			}
		}
		Response response=new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			userService.deleteUsers(user.getUserId());
		} else {
			if(userService.isUserByAccount(user.getUserId(),user.getAccount())){
				response.setResultCode(1);
				response.setResultMessage("登录账号已存在！");
				return response;
			}
			
			userService.saveOrUpdateUser(user); 
		}
		

		return response;
	}
	
	/**
	 * 个人信息维护
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/editUserPersonal")
	public Response editUserPersonal(User user){
		userService.saveUserPersonal(user); 
		
		return new Response();		
	}
	
	@RequestMapping(value = "/findUserById", method =  RequestMethod.GET)
	@ResponseBody
	public User findUserById(@RequestParam(required=false) String userId) throws Exception {
		return userService.getUserById(userId);
	}
	
	@RequestMapping(value = "/resetPassword", method =  RequestMethod.GET)
	@ResponseBody
	public Response resetPassword(@RequestParam String userId) throws Exception {

		userService.resetPassword(userId);

		return new Response();
	}
	
	@RequestMapping(value = "/disableUser", method =  RequestMethod.GET)
	@ResponseBody
	public Response disableUser(@RequestParam String userId) throws Exception {

		userService.setUserEnable(userId, false); 

		return new Response();
	}
	
	@RequestMapping(value = "/endableUser", method =  RequestMethod.GET)
	@ResponseBody
	public Response endableUser(@RequestParam String userId) throws Exception {
		
		userService.setUserEnable(userId, true); 
		
		return new Response();
	}
	
	/**
	 * 启用/禁用用户企业邮箱
	 */
	@RequestMapping(value = "/modifyMailStatus")
	@ResponseBody
	public Response modifyMailStatus(User user) throws Exception {
		if(mailService.isMailSysInUse() == true) { //开启邮件系统
			if(user!=null && StringUtils.isNotBlank(user.getUserId())) {
				if(StringUtils.isNotBlank(user.getMailAddr())) {
					boolean flag = mailService.changeMailUserStatus(user.getUserId(), user.getMailStatus());
					if(flag == true) {
						userService.setUserMailStatus(user.getUserId(), user.getMailStatus());
					}
				}else {  //创建企业邮箱
					user = userService.getUserById(user.getUserId());
					//判断用户所在部门是否为空
					if(user != null && StringUtil.isNotBlank(user.getDeptId())) {
						String newMailAddr = mailService.createMailUser(user);
						mailService.createMailExternalContract(user);
						if(StringUtil.isNotBlank(newMailAddr)) {
							userService.createUserMailAddr(user.getUserId(), newMailAddr);
						}
					}else{
						throw new ApplicationException("请先配置用户关联的部门");
					}
				}
			}
		}else {
			throw new ApplicationException(ErrorCode.MAIL_SYS_DISABLED);
		}		
		return new Response();
		
	}
	@RequestMapping(value = "/getOrgnizationList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOrgnizationList(@ModelAttribute Organization org, @ModelAttribute GridRequest gridRequest) throws Exception {
		log.info("getOrgnizationList() start.");
//		Organization org = new Organization();
//		org.setId(orgId);
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = userService.getOrganizationList(org, dataPackage);
		log.info("getOrgnizationList() end.");
		return new DataPackageForJqGrid(dataPackage) ;
	}
	
	@RequestMapping(value = "/editOrganization", method =  RequestMethod.GET)
	@ResponseBody
	public Response editOrganization(@ModelAttribute Organization org, @ModelAttribute GridRequest gridRequest) throws Exception {
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			userService.deleteOrganizations(org.getId());
		} else {
			if(StringUtils.isNotBlank(org.getId())) {  //更新
				Organization dbOrg = userService.getOrganizationById(org.getId());
				dbOrg.setName(org.getName());
				dbOrg.setParentId(org.getParentId());
//				dbOrg.setProvinceId(org.getProvinceId());
//				dbOrg.setRegionId(org.getRegionId());
				dbOrg.setProvince(org.getProvince());
				dbOrg.setCity(org.getCity());
				dbOrg.setOrgType(org.getOrgType());
				dbOrg.setBelong(org.getBelong());
				dbOrg.setContact(org.getContact());
				dbOrg.setOrgOrder(org.getOrgOrder());
				dbOrg.setAddress(org.getAddress());
				dbOrg.setLon(org.getLon());
				dbOrg.setLat(org.getLat());
				if(mailService.isMailSysInUse() == true) {  //开启邮件系统
					String orgName = dbOrg.getName();
					Integer orgOrder = dbOrg.getOrgOrder();
					userService.saveOrUpdateOrganization(dbOrg); 
					//更新邮件系统的部门名称
					if(StringUtils.isNotBlank(org.getName()) && !org.getName().equals(orgName)) {
						mailService.updateUnitName(org.getId(), org.getName());
					}
					//更新排序
					if(org.getOrgOrder() != null && !org.getOrgOrder().equals(orgOrder)
							|| orgOrder != null && !orgOrder.equals(org.getOrgOrder()) 
							|| org.getOrgOrder() == null && orgOrder != null){
						mailService.updateMailUnitOrder(org.getId(), org.getOrgOrder());
					}
				}else {
					userService.saveOrUpdateOrganization(dbOrg); 
				}				
			}else {    //新增
				userService.saveOrUpdateOrganization(org);
/*				if(mailService.isMailSysInUse() == true) { //开启邮件系统
					//同步组织到邮件系统
					mailService.createMailUnit(org);
				}*/
			}	
		}
		return new Response();
	}
	
	@RequestMapping(value = "/findOrganizationById", method =  RequestMethod.GET)
	@ResponseBody
	public Organization findOrganizationById(@RequestParam String id) {
		return userService.getOrganizationAndRegionById(id);
	}
	
	/**
	 * 根据学校ID获取对应的组织架构
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/findOrganizationBySchoolId", method =  RequestMethod.GET)
	@ResponseBody
	public OrganizationVo findOrganizationBySchoolId(@RequestParam String id) {
		return userService.findOrganizationBySchoolId(id);
	}
	
	

	@RequestMapping(value = "/getAllCampusForMap")
	@ResponseBody
	public List<Organization> getAllCampusForMap(){
		return userService.getAllCampusForMap();
	}
		
	private DataPackage gridRequest2Datapackage(GridRequest gridRequest) {
		DataPackage dataPackage = new DataPackage();
		int pageNum = gridRequest.getPage() - 1;
		if (pageNum < 0) {
			pageNum = 0;
		}
		if (gridRequest.getRows() == 0) {
			gridRequest.setRows(999);
		}
		dataPackage.setPageNo(pageNum);
		dataPackage.setPageSize(gridRequest.getRows());
		dataPackage.setSidx(gridRequest.getSidx());
		dataPackage.setSord(gridRequest.getSord());
		
		return dataPackage;
	}

	@RequestMapping(value = "/deleteOrganizationByIds", method =  RequestMethod.GET)
	@ResponseBody
	public Response deleteOrganizationByIds(@ModelAttribute Organization org) {
		if (org == null || StringUtils.isEmpty(org.getId())) {
			throw new ApplicationException(ErrorCode.ORG_ID_EMPTY);
		}
		userService.deleteOrganizations(org.getId()); 
		return new Response();
	}
	
	@RequestMapping(value = "/getAllOrganizationForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getAllOrganizationForSelection(@ModelAttribute Organization org) {
		if (org == null) {
			org = new Organization();
		}
		DataPackage dataPackage = userService.getOrganizationList(org, new DataPackage(0, 99999));
		
		List<Organization> orgs = (List<Organization>)dataPackage.getDatas();
		List<NameValue> nvs = new ArrayList<NameValue>();
		nvs.add(SelectOptionResponse.buildNameValue("请选择", ""));
		for (Organization so : orgs) {
			nvs.add(so);
		}
		
		return new SelectOptionResponse(nvs);
	}
	
	@RequestMapping(value = "/getRoleList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getRoleList(@ModelAttribute Role role, @ModelAttribute GridRequest gridRequest) {
		log.info("getRoleList() start.");
		DataPackage dataPackage = userService.getRoleList(role, gridRequest2Datapackage(gridRequest));
		log.info("getRoleList() end.");
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value = "/editRole", method =  RequestMethod.POST)
	@ResponseBody
	public Response editRole(@ModelAttribute Role role, @ModelAttribute String oper) {			
		if ("del".equalsIgnoreCase(oper)) {
			userService.deleteRoles(role.getRoleId());
		} else {			
			userService.saveOrUpdateRole(role); 
		}		
		return new Response();
	}
	
	@RequestMapping(value = "/editRoleNew", method =  RequestMethod.POST)
	@ResponseBody
	public Response editRoleNew(@RequestBody Role role) {			
		userService.saveOrUpdateRole(role); 
		return new Response();
	}
	
	@RequestMapping(value = "/findRoleById", method =  RequestMethod.GET)
	@ResponseBody
	public Role findRoleById(@ModelAttribute Role role) {
		return userService.getRoleById(role.getRoleId());
	}
	
	@RequestMapping(value = "/deleteRoleByIds", method =  RequestMethod.GET)
	@ResponseBody
	public Response deleteRoleByIds(@ModelAttribute Role role) {
		userService.deleteRoles(role.getRoleId());
		return new Response();
	}
	
	@RequestMapping(value = "/getResourceList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getResourceList(@ModelAttribute Resource res, @ModelAttribute GridRequest gridRequest, @ModelAttribute String includeNonRes) {
		log.info("getResourceList() start.");
		String includeNonRes1 = request.getParameter("includeNonRes");
		DataPackage dataPackage = userService.getResourceList(res, gridRequest2Datapackage(gridRequest), "true".equalsIgnoreCase(includeNonRes1)?true:false);
		log.info("getResourceList() end.");
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value = "/editResource", method =  RequestMethod.GET)
	@ResponseBody
	public Response editResource(@ModelAttribute Resource res, @ModelAttribute String oper) {
		if ("del".equalsIgnoreCase(oper)) {
			userService.deleteResources(res.getId());
		} else {
			userService.saveOrUpdateResource(res); 
		}
		return new Response();
	}
	
	@RequestMapping(value = "/findResourceById", method =  RequestMethod.GET)
	@ResponseBody
	public Resource findResourceById(@ModelAttribute Resource res) {
		if (res == null || StringUtils.isEmpty(res.getId())) {
			throw new ApplicationException(ErrorCode.ORG_ID_EMPTY);
		}
		return userService.getResourceById(res.getId());
	}
	
	@RequestMapping(value = "/deleteResourceByIds", method =  RequestMethod.GET)
	@ResponseBody
	public Response deleteResourceByIds(@ModelAttribute Resource res) {
		if (res == null || StringUtils.isEmpty(res.getId())) {
			throw new ApplicationException(ErrorCode.RES_NOT_FOUND);
		}
		userService.deleteResources(res.getId());
		return new Response();
	}
	
	@RequestMapping(value = "/getAllResourceForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getAllResourceForSelection(@ModelAttribute Resource res) {
		if (res == null) {
			res = new Resource();
		}
		DataPackage dataPackage = userService.getResourceList(res, new DataPackage(0, 9999));
		
		List<Resource> orgs = (List<Resource>)dataPackage.getDatas();
		List<NameValue> nvs = new ArrayList<NameValue>();
		nvs.add(SelectOptionResponse.buildNameValue("请选择", ""));
		for (Resource so : orgs) {
			nvs.add(so);
		}
		
		return new SelectOptionResponse(nvs);
	}
	
	@RequestMapping(value = "/getResourceBoy")
	@ResponseBody
	public List<Organization> getOrganizationBoy(Organization org,String checkAllPermit){
		 List<Organization> list=userService.getOrganizationBoy(org,checkAllPermit);
		return list;
	}
	
	
	/**
	 * 得到所属的所有组织架构列表
	 * @return
	 */
	@RequestMapping(value = "/getBelongOrg")
	@ResponseBody
	public List<Organization> getBelongOrg(){
		 List<Organization> list=userService.getBelongOrg();
		return list;
	}
	
	/**
	 * 查询当前用户所在组织架构下虚拟老师
	 * @param workType
	 * @return
	 */
	@RequestMapping(value = "/getUserByWorkType")
	@ResponseBody
	public List<UserVo> getUserByWorkType(UserWorkType workType){
		if(workType==null) {
			workType = UserWorkType.DUMMY;
		}
		User user = userService.getCurrentLoginUser();
		return userService.getUserByWorkTypeAndOrgId(workType, null, true);
	}
	
	/**
	 * 保存虚拟老师
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/saveUserDummy")
	@ResponseBody
	public Response saveUserDummy(User user){
		Response response=new Response();
		if(user==null || StringUtils.isEmpty(user.getName())){
			response.setResultCode(1);
			response.setResultMessage("用户名不能为空");
			return response;
		}
		User u=userService.getUserByName(user.getName());
		if(u!=null){
			response.setResultCode(1);
			response.setResultMessage("用户名已存在");
			return response;
		}
		user.setWorkType(UserWorkType.DUMMY);
		user.setOrganizationId(userService.getCurrentLoginUser().getOrganizationId());
		userService.saveOrUpdateUser(user);
		return response;
	}
	
	/**
	 * 删除虚拟老师
	 * 已排课的老师不允许删除
	 * @param user
	 * @return
	 */
	@RequestMapping("/deleteUserDummy")
	@ResponseBody
	public Response deleteUserDummy(User user){
		if(user==null || StringUtils.isEmpty(user.getUserId())){
			throw new ApplicationException(ErrorCode.PARAMETER_EMPTY);
		}
		return userService.deleteDummyUser(user);
	}
	
	@RequestMapping(value = "/findUserByRoleForAutoCompelete", method =  RequestMethod.GET)
	@ResponseBody
	public String findUserByRoleForAutoCompelete(String term, RoleCode roleCode) {
		
		List<UserVo> vos = userService.findUserByRoleForAutoCompelete(term, roleCode);
		
		String autoCompeleteData = "";
		for (UserVo user : vos) {
			autoCompeleteData += "\"" + user.getName() + "(" + user.getUserId() + ")\",";
		}
		return "[" + StringUtil.cutLastChar(autoCompeleteData) + "]";
	}
	
	
	/**
	 * 修改密码  account 为空：修改当前登录用户密码
	 * @param account
	 * @param oldPassWord
	 * @param newPassWord
	 * @return
	 */
	@RequestMapping(value = "/updatePersonalUserPassword")
	@ResponseBody
	public  Response updatePersonalUserPassword(String account,@RequestParam String oldPassWord,@RequestParam String newPassWord){
		if(StringUtils.isEmpty(account)){
			User user = userService.getCurrentLoginUser();
			account=user.getAccount();
		}
		return userService.updateUserPassword(account,oldPassWord,newPassWord);
	}
	
	/**
	 * 获取当前登录用户的rolecode  多个角色用，隔开
	 * @return
	 */
	@RequestMapping(value = "/getCurrentUserCode")
	@ResponseBody
	public String getCurrentUserCode(){
		User user = userService.getCurrentLoginUser();
		return user.getRoleCode();
	}
	
	@ResponseBody
	@RequestMapping(value="/getOrganizationTree")
	public List<Organization> getOrganizationTree(String organizationTypes){
		return userService.getOrganizationTree(organizationTypes);
	}
	
	/**
	 * 用户职位列表
	 * */
	@RequestMapping(value = "/getUserJobList")
	@ResponseBody
	public DataPackageForJqGrid getUserJobList(UserJob userJob,GridRequest gridRequest){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = userJobService.findPageUserJob(dataPackage, userJob);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 新增或修改职位
	 * */
	@RequestMapping(value = "/saveOrEditUserJob")
	@ResponseBody
	public Response saveOrEditUserJob(@RequestBody UserJob userJob){
		
		userJobService.saveOrUpdateUserJob(userJob);
		
		User currentUser = userService.getCurrentLoginUser();
		
		//获取主职位id
		String jobId = userJob.getId();
		if(StringUtils.isNotBlank(jobId)){
			//删除老的职位
			distributableUserJobService.deleteAllRelateUuerJobByMainJobId(jobId);
		}
		Set<DistributableUserJobVo> distributableUserJobs = userJob.getDistributableUserJobs();
		for(DistributableUserJobVo dUserJob:distributableUserJobs){
			//保存新的关联职位
			DistributableUserJob  distributableUserJob= new DistributableUserJob();
			DistributableUserJobPk dUserJobPk = new DistributableUserJobPk();
			dUserJobPk.setJobId(jobId);
			dUserJobPk.setRelateJobId(dUserJob.getRelateJobId());
			distributableUserJob.setId(dUserJobPk);
			distributableUserJob.setJobName(userJob.getJobName());
			distributableUserJob.setRelateJobName(dUserJob.getRelateJobName());
			distributableUserJob.setCreateUserId(currentUser.getUserId());
			distributableUserJob.setCreateTime(DateTools.getCurrentDateTime());
			distributableUserJob.setModifyUserId(currentUser.getUserId());
			distributableUserJob.setModifyTime(DateTools.getCurrentDateTime());
			distributableUserJobService.save(distributableUserJob);			
		}

		
		
		
		
		if(mailService.isMailSysInUse() == true) { //开启了邮箱功能
			if(StringUtils.isNotBlank(userJob.getId())) {  //更新
				UserJob userJobIndb = userJobService.findUserJobById(userJob.getId());
				int oldStatus = userJobIndb.getFlag();
				int newStatus = userJob.getFlag();
				String oldJobName = userJobIndb.getJobName();
				String newJobName = userJob.getJobName();
				if(oldStatus != newStatus || (StringUtils.isNotBlank(newJobName) && !newJobName.equals(oldJobName))) {
					mailService.updateMailListNameOrStatus(userJob);
				}
			}
		}
		
		return new Response();
	}
	
	/**
	 * 删除职位
	 */
	@RequestMapping(value = "/deleteUserJob")
	@ResponseBody
	public Response deleteUserJob(String id){
		if(mailService.isMailSysInUse() == true) { //开启了邮箱功能
			mailService.deleteMailList(id);
		}
		return userJobService.deleteUserJob(id);
	}
	
	/**
	 * 获取职位
	 */
	@RequestMapping(value = "/getUserJobById")
	@ResponseBody
	public UserJob getUserJobById(String id){
		return userJobService.findUserJobById(id);
	}
	
	
	/***********************************************************系统公告开始****************************************************************************************/
	/**
	 * 系统公告管理 列表
	 * */
	@RequestMapping(value = "/getSystemNoticeList")
	@ResponseBody
	public DataPackageForJqGrid getSystemNoticeList(SystemNoticeVo systemNoticeVo,GridRequest gridRequest){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = systemNoticeService.getSystemNoticeList(systemNoticeVo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	
	
	/***********************************************************系统公告开始****************************************************************************************/
	/**
	 * 系统公告管理 列表
	 * */
	@RequestMapping(value = "/getSystemNoticeListByRoles")
	@ResponseBody
	public DataPackageForJqGrid getSystemNoticeListByRoles(SystemNoticeVo systemNoticeVo,GridRequest gridRequest){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = systemNoticeService.getSystemNoticeListByRoles(systemNoticeVo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 新增或修改公告
	 * @isAllOrganization TRUE 整个集团
	 * */
	@RequestMapping(value = "/saveOrEditSystemNotice")
	@ResponseBody
	public Response saveOrEditSystemNotice(SystemNotice systemNotice,@RequestParam(value = "file1",required=false) MultipartFile myfile1, String isAllOrganization){
		//如果公告类型为App新闻的话则判断是不是图片，是图片判断大小与长宽情况
		systemNoticeService.saveOrEditSystemNotice(systemNotice,myfile1, isAllOrganization);
		return new Response();
	}
	
	//20151204 新增
	public void SaveFileFromInputStream(InputStream stream,String path,String filename) throws IOException    
    {          
        FileOutputStream fs=new FileOutputStream( path + "/"+ filename);    
        byte[] buffer =new byte[1024*1024];    
        int bytesum = 0;    
        int byteread = 0;     
        while ((byteread=stream.read(buffer))!=-1)    
        {    
           bytesum+=byteread;    
           fs.write(buffer,0,byteread);    
           fs.flush();    
        }     
        fs.close();    
        stream.close();          
    }          
	
	/**
	 * 删除公告
	 * */
	@RequestMapping(value = "/deleteSystemNoticeById")
	@ResponseBody
	public Response deleteSystemNoticeById(SystemNotice systemNotice,String isDeleteOtherFile){
		if("true".equals(isDeleteOtherFile)){
			SystemNotice syst = systemNoticeDao.findById(systemNotice.getId());
			syst.setFilePath(null);
			//systemNoticeDao.save(syst);
			systemNoticeService.saveSystemNotice(syst);
		}else{
			systemNoticeService.deleteSystemNoticeById(systemNotice);
		}
		//删除相应的附件
		if(StringUtils.isNotBlank(systemNotice.getFilePath())){
			// FileUtil.deleteContents(new File(systemNotice.getFilePath()));	
			AliyunOSSUtils.remove(systemNotice.getFilePath());
		}
		return new Response();
	}
    
	/**
	 * 新增或修改公告回复（暂时只新增）
	 * */
	@RequestMapping(value = "/saveOrEditSystemNoticeReply")
	@ResponseBody
	public Response saveOrEditSystemNoticeReply(SystemNoticeReply systemNoticeReply){
	//public Response saveOrEditSystemNotice(SystemNotice systemNotice){
		systemNoticeReplyService.saveOrEditSystemNoticeReply(systemNoticeReply);
		return new Response();
	}
	
	/**
	 * 查询公告回复
	 * */
	@RequestMapping(value = "/getSystemNoticeReplyList")
	@ResponseBody
	public DataPackageForJqGrid getSystemNoticeReplyList(SystemNoticeReplyVo systemNoticeReplyVo,GridRequest gridRequest){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage=systemNoticeReplyService.getSystemNoticeReplyList(systemNoticeReplyVo,dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 删除系统公告回复
	 */
	@RequestMapping(value = "/deleteSystemNoticeReply")
	@ResponseBody
	public Response deleteSystemNoticeReply(SystemNoticeReply reply){
		systemNoticeReplyService.deleteSystemNoticeReply(reply);
		return new Response();
	}
	
	/**
	 * 查询当前登录用户未读的必读公告
	 * */
	@RequestMapping(value = "/getSystemNoticeUserList")
	@ResponseBody
	public List<WelcomeNoticeVo> getSystemNoticeUserList(SystemNoticeUserVo systemNoticeUserVo){
		List<WelcomeNoticeVo> noticeVoList = systemNoticeService.getSystemNoticeUserList(systemNoticeUserVo);
//		List<WelcomeNoticeVo> welcomeVoList = HibernateUtils.voListMapping(noticeVoList, WelcomeNoticeVo.class);
//		HibernateUtils.voListMapping(noticeVoList, WelcomeNoticeVo.class);
//		for (SystemNoticeVo noticeVo: noticeVoList) {
//			WelcomeNoticeVo welcomeVo = HibernateUtils.voObjectMapping(noticeVo, WelcomeNoticeVo.class);
//			welcomeVoList.add(welcomeVo);
//		}
		return noticeVoList;
	}
	
	/**
	 * 保存已读公告
	 * */
	@RequestMapping(value = "/saveHasReadedNotice")
	@ResponseBody
	public Response saveHasReadedNotice(SystemNoticeUser systemNoticeUser){
		systemNoticeService.saveHasReadedNotice(systemNoticeUser);
		return new Response();
	}
	
	/**
	 * 查询已读公告
	 * */
	@RequestMapping(value = "/getHasReadedSystemNoticeUsers")
	@ResponseBody
	public List<WelcomeNoticeUserVo> getHasReadedSystemNoticeUsers(SystemNoticeUserVo systemNoticeUserVo){
		List<WelcomeNoticeUserVo> welcomeVoList = new ArrayList<WelcomeNoticeUserVo>();
		List<SystemNoticeUserVo> systemVoList =  systemNoticeService.getHasReadedSystemNoticeUsers(systemNoticeUserVo);
		for (SystemNoticeUserVo systemVo: systemVoList) {
			WelcomeNoticeUserVo welcomeVo = HibernateUtils.voObjectMapping(systemVo, WelcomeNoticeUserVo.class);
			welcomeVoList.add(welcomeVo);
		}
		return welcomeVoList;
	}
	/***********************************************************系统公告结束****************************************************************************************/
	
	/***********************************************************页面配置说明开始****************************************************************************************/
	
	/**
	 * 获取页面说明列表
	 * @param resourceVo
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value = "/getGuideLineList")
	@ResponseBody
	public DataPackageForJqGrid getGuideLineList(ResourceVo resourceVo,GridRequest gridRequest){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		
		dataPackage = resourceService.getGuideLineList(resourceVo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	
	/**
	 * 获取所有页面说明
	 */
	@RequestMapping(value = "/getAllGuideLine")
	@ResponseBody
	public DataPackage getAllGuideLine(){
		return resourceService.getAllGuideLine();
	}
	
	/**
	 * 保存页面说明信息
	 * @param resource
	 * @return
	 */
	@RequestMapping(value = "/saveOrEditGuideLine")
	@ResponseBody
	public Response saveOrEditResource(Resource resource){
		resourceService.saveOrEditGuideLine(resource);
		return new Response();
	}
	
	/**
	 * 更新状态，设置为无效
	 * @param resource
	 * @return
	 */
	@RequestMapping(value = "/updateGuideLine")
	@ResponseBody
	public Response updateGuideLine(String id, String status){
		resourceService.updateGuideLineById(id, status);
		return new Response();
	}
	
	/**
	 * 通过ID删除页面说明
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value = "/deleteGuideLineById")
	@ResponseBody
	public Response deleteGuideLineById(String resourceId){
		resourceService.deleteGuideLineById(resourceId);
		return new Response();
	}

	/**
	 * 通过URL获取页面说明
	 * @param resourceId
	 * @return
	 */
	@RequestMapping(value = "/findResourceVoByURL")
	@ResponseBody
	public ResourceVo findResourceVoByURL(String url){
		ResourceVo resource = resourceService.findResourceVoByURL(url);
		return resource;
	}
	
	/***********************************************************页面说明配置结束****************************************************************************************/
	
	/***********************************************************系统信息配置开始****************************************************************************************/
	/**
	 * 增加或更新系统信息配置
	 * */
	@RequestMapping(value = "/saveOrUpdateSystemConfig")
	@ResponseBody
	public Response saveOrUpdateSystemConfig(SystemConfig systemConfig){
//		String rootPath=getClass().getResource("/").getFile().toString();
		//if("systemInfo".equals(systemConfig.getTag())){
//			String[] picType = myfile1.getOriginalFilename().split(".");
//			String picType1 = picType[picType.length-1];
//			String uploadFile=rootPath.substring(0,rootPath.lastIndexOf("WEB-INF"))+"images/system/systemIcon."+picType1;
//			uploadFile=new File(uploadFile).getPath().replaceAll("%20", " ");
//			try {
//				boolean  isUploadFinish = FileUtil.readInputStreamToFile(myfile1.getInputStream(),uploadFile);
//				if(isUploadFinish){
//					systemConfigService.savaOrUpdateSystemConfig(systemConfig);
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
			//FileUtils.copyInputStreamToFile(myfile1, new File("webapp/images","systemIcon."+picType1));
		//}else if("systemHome".equals(systemConfig.getTag())){
			
		//}else 
		if("receipt".equals(systemConfig.getTag()) || "smsNumber".equals(systemConfig.getTag()) || "contractGrade".equals(systemConfig.getTag())){
			systemConfigService.savaOrUpdateSystemConfig(systemConfig);
		}else if("classHour".equals(systemConfig.getTag())){
			systemConfigService.savaOrUpdateSystemConfig(systemConfig);
		}
		
		return new Response();
	}
	
	/**
	 * 增加或更新系统信息配置
	 * @throws Exception 
	 * */
	@RequestMapping(value = "/saveOrUpdateSystemConfigForImg")
	public ModelAndView saveOrUpdateSystemConfigForImg(SystemConfig systemConfig,@RequestParam("image1") MultipartFile myfile1,
			@RequestParam(value = "image2",required=false) MultipartFile myfile2,String systemPath,String systemPathId,
			HttpSession session) throws Exception{
		
		//systemConfig.setGroup(userService.getBelongGrounp());
		
		
		/*
		//获取虚拟路径
		SystemConfig sc = new SystemConfig();
		sc.setTag("systemPath");
		sc.setGroup(systemConfig.getGroup());
		String realPath = "/tmp/uploadfile/image/";//默认路径
		SystemConfig path = systemConfigService.getSystemPath(sc);
		if(path!=null && StringUtils.isNotBlank(path.getValue())){
			realPath = path.getValue();
		}
		 
		//获取图片名字
		String picType = "";
		if(myfile1 != null && !myfile1.isEmpty()){
			String pic = myfile1.getOriginalFilename(); 
			picType = pic.substring(pic.lastIndexOf("."));//图片的格式 如jpg
		}
		String picType2 = "";
		if(myfile2 != null && !myfile2.isEmpty()){
			String pic2 = myfile2.getOriginalFilename(); 
			picType2 = pic2.substring(pic2.lastIndexOf("."));//图片的格式 如jpg
		}
		String result = "失败";
		//信息配置()
		if("systemInfo".equals(systemConfig.getTag())){
			String fileName = systemConfig.getGroup().getId()+"_systemIcon"+picType;
			try {
				if(!myfile1.isEmpty()){
					boolean  isUploadFinish = FileUtil.readInputStreamToFile(myfile1.getInputStream(), realPath+fileName);//将表单提交过来的图片保存到TOMCAT下
					if(isUploadFinish){
						systemConfig.setRemark(realPath+fileName);//保存图片路径
					}
				}
				systemConfigService.savaOrUpdateSystemConfig(systemConfig);
				result = "成功";
				
				if(StringUtils.isNotBlank(systemPath)){ //保存虚拟路径
					SystemConfig conf = new SystemConfig();
					if(StringUtils.isBlank(systemPathId)){conf.setId(null);}else{conf.setId(systemPathId);}
					conf.setName("文件虚拟路径");
					conf.setValue(systemPath);
					conf.setTag("systemPath");
					conf.setGroup(systemConfig.getGroup());
					systemConfigService.savaOrUpdateSystemConfig(conf);
					result = "成功";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		//首页配置
		}else if("systemHome".equals(systemConfig.getTag())){
			String fileName1 = "loginLogo"+picType;
			String fileName2 = "loginBigPic"+picType2;
			try {
				String url1="",url2="";
				boolean  isUploadFinish=true,isUploadFinish2=true;
				if(!myfile1.isEmpty()){
					isUploadFinish = FileUtil.readInputStreamToFile(myfile1.getInputStream(),realPath+fileName1);//首页图标
					if(isUploadFinish) url1=realPath+fileName1;
				}
				if(!myfile2.isEmpty()){
					isUploadFinish2 = FileUtil.readInputStreamToFile(myfile2.getInputStream(),realPath+fileName2);//首页图片
					if(isUploadFinish2) url2=realPath+fileName2;
				}
				if(isUploadFinish && isUploadFinish2 ){
					systemConfig.setName("首页图标");
					systemConfig.setRemark(url1+","+url2);//保存图片路径
					systemConfigService.savaOrUpdateSystemConfig(systemConfig);
					result = "成功";
				}else{
					throw new ApplicationException("系统繁忙，请稍后再试");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		*/
		String result = "失败";
		if("systemInfo".equals(systemConfig.getTag())){
			result = systemConfigService.saveOrUpdateSystemConfigForImg(systemConfig, myfile1, systemPath, systemPathId);
		}else if("systemHome".equals(systemConfig.getTag())){
			result = systemConfigService.saveOrUpdateSystemConfigForImg(systemConfig, myfile1, myfile2);
		}
		session.setAttribute("result", result);
		return new ModelAndView("redirect:/function/systemConfig/systemResult.jsp");
	}
	
	/**
	 * 查询系统信息配置
	 * */
	@RequestMapping(value = "/getSystemConfigList")
	@ResponseBody
	public List<SystemConfigVo> getSystemConfigList(SystemConfig systemConfig){
		return systemConfigService.getSystemConfigList(systemConfig);
	}
	
	/**
	 * 更新系统信息配置List
	 */
	@RequestMapping(value="/updateSystemConfigValueList")
	@ResponseBody
	public Response updateSystemConfigValueList(@RequestParam String sysConfListJsonStr) {
		SystemConfig[] sysConfList=null;
		try {
			sysConfList= objectMapper.readValue(sysConfListJsonStr, SystemConfig[].class);
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.PARAMETER_FORMAT_ERROR);
		}
		if(sysConfList != null) {
			systemConfigService.updateSystemConfigValueList(sysConfList);
		}
		
		return new Response();
	}
	
	/**
	 * 邮件系统，初始化部门标识
	 */
	@RequestMapping(value="/initOrgSignForMailSys")
	@ResponseBody
	public Response initOrgSignForMailSys(@RequestParam String sysId, @RequestParam String orgSignsJsonStr) {
		Organization[] orgList=null;
		try {
			orgList= objectMapper.readValue(orgSignsJsonStr, Organization[].class);
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.PARAMETER_FORMAT_ERROR);
		}
		if(orgList != null && orgList.length > 0 && StringUtils.isNotBlank(sysId)) {
			systemConfigService.initOrgSignForMailSys(sysId, orgList);
		}
		return new Response();
	}
	
	/**
	 * 邮件系统，初始化职位标识
	 */	
	@RequestMapping(value="/initJobSignForMailSys")
	@ResponseBody
	public Response initJobSignForMailSys(@RequestParam String sysId, @RequestParam String jobSignsJsonStr) {
		UserJob[] jobList=null;
		try {
			jobList= objectMapper.readValue(jobSignsJsonStr, UserJob[].class);
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.PARAMETER_FORMAT_ERROR);
		}
		if(jobList != null && jobList.length > 0 && StringUtils.isNotBlank(sysId)) {
			systemConfigService.initJobSignForMailSys(sysId, jobList);
		}
		return new Response();
	}
	/***********************************************************系统信息配置结束****************************************************************************************/
	
	
	
	
	/************************************************************库存管理开始****************************************************************************************/
	
	/**
	 * 库存管理列表
	 * */
//	@RequestMapping(value = "/getInventoryRecordForGrid")
//	@ResponseBody
//	public DataPackageForJqGrid getInventoryRecordForGrid(InventoryRecordVo inventoryRecordVo, @ModelAttribute GridRequest gridRequest){
//		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
//		dataPackage = systemService.getInventoryRecordForGrid(inventoryRecordVo, dataPackage);
//		return new DataPackageForJqGrid(dataPackage);
//	}
	
	
	/************************************************************库存管理结束****************************************************************************************/
	
	/************************************************************我的收藏begin****************************************************************************************/	
	/**
	 * 添加我的收藏
	 */
	@RequestMapping(value = "/addMyCollection")
	@ResponseBody
	public Response addMyCollection(MyCollectionVo myCollectionVo){
		myCollectionService.saveOrUpdateMyCollection(myCollectionVo);
		return new Response();
	}
	
	/**
	 * 删除我的收藏
	 */
	@RequestMapping(value = "/delMyCollection")
	@ResponseBody
	public Response delMyCollection(@RequestParam String id){
		myCollectionService.deleteById(id);
		return new Response();
	}
	
	/**
	 * 根据资源删除我的收藏
	 */
	@RequestMapping(value = "/delMyCollectionByResId")
	@ResponseBody
	public Response delMyCollectionByResId(@RequestParam String resId){
		myCollectionService.deleteByResId(resId);
		return new Response();
	}
	
	
	/**
	 * 列出我的收藏
	 */
	@RequestMapping(value = "/listMyCollection")
	@ResponseBody
	public Map<String, List<MyCollectionVo>> listMyCollection(){
		User user = userService.getCurrentLoginUser();
        return myCollectionService.getMyCollectionByUserId(user.getUserId());
	}
	/************************************************************我的收藏end****************************************************************************************/
	
	/**
	 * 批量升级学生
	 */
	
	@RequestMapping(value="/studentUpgradeDo",method=RequestMethod.GET)
	@ResponseBody
	public Response studentUpgradeDo(@RequestParam(required=true)String orgnizationId,String cla,String course,
			String gradeNames) {
		studentService.upGrade(cla, course, orgnizationId, gradeNames);
		
		return new Response();
	}
	
	/**
	 * 用户操作日志
	 * */
	@RequestMapping(value = "/getUserOperList")
	@ResponseBody
	public DataPackageForJqGrid getUserOperList(UserOperationLog userOperationLog,GridRequest gridRequest){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = userOperationLogService.getOperationLogList(userOperationLog, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 批处理执行日志
	 * */
	@RequestMapping(value = "/getSchedulerExecuteLogList")
	@ResponseBody
	public DataPackageForJqGrid getSchedulerExecuteLogList(SchedulerExecuteLog schedulerExecuteLog,GridRequest gridRequest){
		DataPackage dp = gridRequest2Datapackage(gridRequest);
		dp = schedulerExecuteLogService.getSchedulerExecuteLogList(schedulerExecuteLog, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 获取所有存储过程
	 * */
	@RequestMapping(value = "/getProcedureList")
	@ResponseBody
	public DataPackageForJqGrid getProcedureList(String enumNameLike, String enumValueLike){
		DataPackage dataPackage = new DataPackage(0,999);
		List<NameValue> list=EnumHelper.getEnumOptions("ProcedureList");
		List<NameValue> returnList = new ArrayList<NameValue>();
		if (StringUtil.isNotBlank(enumNameLike) && StringUtil.isNotBlank(enumValueLike)) {
			for (NameValue nv : list) {
				if (-1 != nv.getName().indexOf(enumNameLike.toUpperCase()) && -1 != nv.getValue().indexOf(enumValueLike.toUpperCase())) {
					returnList.add(nv);
				}
			}
		} else if (StringUtil.isNotBlank(enumNameLike)){
			for (NameValue nv : list) {
				if (-1 != nv.getName().indexOf(enumNameLike.toUpperCase())) {
					returnList.add(nv);
				}
			}
		} else if (StringUtil.isNotBlank(enumValueLike)) {
			for (NameValue nv : list) {
				if (-1 != nv.getValue().indexOf(enumValueLike.toUpperCase())) {
					returnList.add(nv);
				}
			}
		} else {
			returnList = list;
		}
		dataPackage.setDatas(returnList);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	 /**
	  * 更换用户组织架构判断该用户是否还有学生
	  */
	 @RequestMapping("/befroChangeStudyManegerOrg")
		@ResponseBody
		public Response  befroChangeStudyManegerOrg(String orgIds,String userId) throws Exception{
		 studentService.befroChangeStudyManegerOrg(orgIds,userId);
			return new Response();
		}
	 
	 /**
	  * pc端向手机端推送公告信息
	  * @param isAllOrganization TRUE:整个集团
	  */
	 @RequestMapping(value = "/pushSystemNoticeToMobile", method =  RequestMethod.POST)
	 @ResponseBody
	 public Response pushSystemNoticeToMobile(SystemNotice systemNotice, String isAllOrganization){
		 systemNoticeService.pushSystemNoticeToMobile(systemNotice, isAllOrganization);
		 return new Response();
	 }
	 
	 /**
	  * 获取登录人具体部门职位对应的组织架构
	  */
	 @RequestMapping(value = "/getOrgByUserJob", method =  RequestMethod.POST)
	 @ResponseBody
	 public String getOrgByUserJob(@RequestParam String deptJobId,@RequestParam String orgType){
		 return  userService.getOrgByUserJob(deptJobId, orgType);
	 }
	 
	 /**
	  * 根据职位code找到users
	  */
	 @RequestMapping(value = "/getUsersByJobCode", method =  RequestMethod.GET)
	 @ResponseBody
	 public List<Map<String, String>> getUsersByJobCode(@RequestParam String jobCode,@RequestParam String orgType,@RequestParam String campusId){
		 return  userService.getUsersByJobCode(jobCode,orgType,campusId);
	 }
	 
	 /**
	  * 过季小班清理
	  */
	 @RequestMapping(value = "/cleanOutExpiredSmallClass", method = RequestMethod.GET)
	 @ResponseBody
	 public Response cleanOutExpiredSmallClass(@RequestParam String productVersion, 
			 @RequestParam String productQuarter, @RequestParam String searchClean){
		 
		 return	productService.cleanOutExpiredSmallClass(productVersion, productQuarter, searchClean);
		
	 }
	  
	 
	 /**
	  * 获取登录人具体部门职位对应的组织架构
	  */
	 @RequestMapping(value = "/changeUserRole", method =  RequestMethod.POST)
	 @ResponseBody
	 public Response changeUserRole(String password,String type){		 
		 return  userService.changeUserRole(password,type);
	 }


	/**
	 * 获取Domain的集合
	 */
	@RequestMapping(value = "/getGrayPublish", method =  RequestMethod.GET)
	@ResponseBody
	public Set<String> getGrayPublish(String type){
		if(type==null) {
			return ClassUtil.getClassesName("com.eduboss.domain", type);
		}

		return ClassUtil.getClassesName("com.eduboss.controller",type);
	}

	/**
	 * 保存domain灰度发布规则
	 */
	@RequestMapping(value = "/saveGrayPublish", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveGrayPublish(String type,@RequestParam("publishValues[]") List<String> publishValues) throws IOException {
		byte[] key=ObjectUtil.objectToBytes(type);
		Set<String> setValues=new HashSet<String>();
		for(String value:publishValues){
			setValues.add(value);
		}
		JedisUtil.set(key,ObjectUtil.objectToBytes(setValues));
		return new Response();
	}

	@RequestMapping(value = "/getGrayPublishProject", method =  RequestMethod.GET)
	@ResponseBody
	@GrayMethodAnnotation(id="asdf",name="获取灰度发布Domain列表")
	public Set<String> getGrayPublishProject(String type) throws IOException {

		byte[] values=JedisUtil.get(ObjectUtil.objectToBytes(type));
		if(values==null){
			return null;
		}
		Object o =JedisUtil.ByteToObject(values);
		return (Set<String>)o;
	}

	@RequestMapping(value = "/getMethodByClass", method =  RequestMethod.GET)
	@ResponseBody
	public Set<Map<String,String>> getMethodByClass(String classes){

		try {
			Class cla=Class.forName(classes);
			Set<Map<String,String>> set =ClassUtil.getNeedGrayPublishControllerMethod(cla);
			for(Map<String,String> map :set){
				System.out.print(map.get("value"));
				System.out.print(map.get("name"));
			}
			return set;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/findSystemDegradeVoList", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid findSystemDegradeVoList(SystemDegradeVo vo, GridRequest gridRequest){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = systemConfigService.findSystemDegradeVoList(vo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}

	@RequestMapping(value="/saveSystemDegrade",method = RequestMethod.POST)
	@ResponseBody
	public Response saveSystemDegrade(SystemDegradeVo vo){
		systemConfigService.saveSystemDegrade(vo);
		return new Response();
	}

	@RequestMapping(value = "/findSystemDegradeById", method = RequestMethod.GET)
	@ResponseBody
	public SystemDegradeVo findSystemDegradeById(int id){
		return systemConfigService.findSystemDegradeById(id);
	}


	@RequestMapping(value = "/findOrganization")
	@ResponseBody
	public  List<Map<String, Object>> findOrganization(String branchId, String campusId, String stateOfEmergency){
		return organizationService.findOrganization(branchId, campusId, stateOfEmergency);
	}

	@RequestMapping(value = "/getAllBranch")
	@ResponseBody
	public List<Map<String, Object>> getAllBranch(){
		return organizationService.getAllBranch();
	}

	@RequestMapping(value = "/getDeptAndCampusByBranchId")
	@ResponseBody
	public List<Map<String, Object>> getDeptAndCampusByBranchId(@RequestParam String organizationId){
		return organizationService.getDeptAndCampusByBranchId(organizationId);
	}

	@RequestMapping(value = "/existUserId")
	@ResponseBody
	public boolean existUserId(@RequestParam String userId) {
		try {
			return JedisUtil.exists(Constants.STATUS_EMERGENCY_+userId);
		}catch (Exception e){
			return true;
		}
	}

	@RequestMapping(value = "/updateOrgStatusById")
	@ResponseBody
	public void updateOrgStatusById(@RequestParam String organizationId){
		organizationService.updateOrgStatusById(organizationId);
	}

	@RequestMapping(value = "/updateBatchOrgStatusById")
	@ResponseBody
	public void updateBatchOrgStatusById(@RequestParam String branchId, @RequestParam String stateOfEmergency){
		organizationService.updateBatchOrgStatusById(branchId, stateOfEmergency);
	}
	/**
	 * 修改角色状态
	 * @author: duanmenrun
	 * @Title: updateRoleStatusById 
	 * @Description: TODO 
	 * @param roleId
	 * @param roleStatus
	 * @return
	 */
	@RequestMapping(value = "/updateRoleStatusById", method =  RequestMethod.POST)
	@ResponseBody
	public Response updateRoleStatusById(@RequestParam String roleId, @RequestParam ValidStatus roleStatus) {
		Response res = new Response();
		res = userService.updateRoleStatusById(roleId, roleStatus);
		return res;
	}
}
