package com.eduboss.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eduboss.common.*;
import com.eduboss.domainVo.*;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.dto.*;
import com.eduboss.intenetpay.SybPayService;
import com.eduboss.service.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.util.Base64;
import org.directwebremoting.guice.RequestParameters;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.common.utils.HttpHeaders;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.AppVersion;
import com.eduboss.domain.Course;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.CustomerFolowup;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.DisabledCustomer;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MobileOrganization;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.Organization;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.PersonWorkScheduleRecord;
import com.eduboss.domain.ResourcePool;
import com.eduboss.domain.Role;
import com.eduboss.domain.SmsRecord;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudentComment;
import com.eduboss.domain.StudentFollowUp;
import com.eduboss.domain.SystemConfig;
import com.eduboss.domain.TwoTeacherClassCourse;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domain.UserDetailsImpl;
import com.eduboss.domain.UserJob;
import com.eduboss.domain.UserOrg;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.jedis.RedisDataSource;
import com.eduboss.mail.pojo.MailBoxMsg;
import com.eduboss.mail.pojo.MailInfoView;
import com.eduboss.utils.AliyunAppOssUtil;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.CertUtil;
import com.eduboss.utils.CookieUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.EmojiConvertUtil;
import com.eduboss.utils.FileDownLoadUtil;
import com.eduboss.utils.FileUtil;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.HttpClientUtils;
import com.eduboss.utils.ImageSizer;
import com.eduboss.utils.MessagePushingUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.SpringMvcUtils;
import com.eduboss.utils.StringUtil;
import com.eduboss.utils.TokenUtil;
import com.eduboss.utils.ValidationUtil;
import com.eduboss.utils.WebClientDevWrapper;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Controller
@RequestMapping(value ="/MobileInterface")
public class MobileInterfaceController {
	
	private final static Logger log = Logger.getLogger(MobileInterfaceController.class);
	
	private final static String SHARE_PLATFORM_URL = "http://120.24.61.179/o2o/user/login";
	
    private static final String WhiteOrgList = PropertiesUtils.getStringValue("WhiteOrgList");
    private static final String WhiteUserList = PropertiesUtils.getStringValue("WhiteUserList");
    private static final String IsOpenShaoYiShao = PropertiesUtils.getStringValue("IsOpenShaoYiShao");
    
    @Autowired
    private AppUploadCallBackService appUploadCallBackService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private ContractService  contractService;
	
	@Autowired
	private SmallClassService smallClassService;
	
	@Autowired
	private AppFunction appFunction;

	@Autowired
	private MobilePushMsgService mobilePushMsgService; 
	
	@Autowired
	private ChargeService chargeService; 
	
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private MobileUserService mobileUserService; 
	
	@Autowired
	private MobilePushMsgUserRecordService mobilePushMsgUserRecordService; 
	
	@Autowired
	private RealTimeReportService realTimeReportService;
	
	@Autowired
	private StudentDynamicStatusService studentDynamicStatusService; 
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	
	private MailService mailService;
	
	@Autowired
	private UserJobService userJobService;

	@Autowired
	private MoneyReadyToPayService moneyReadyToPayService;
	
	@Autowired
	private SystemNoticeService systemNoticeService;
	
	@Autowired
	private UserDeptJobService userDeptJobService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private PersonWorkService personWorkService; //不在boss 这边使用 在OA使用
	
	@Autowired
	private ReportFormsService reportFormsService;
	
	@Autowired
	private OtmClassService otmClassService;

	@Autowired
	private ResourcePoolService resourcePoolService;
	
	@Autowired
	private CustomerEventService customerEventService;
	
	@Autowired
	private AppNewsManageService appNewsManageService;
	
	@Autowired
	private SentRecordService sentRecordService;

    @Autowired
    private RedisDataSource redisDataSource;


	@Autowired
	private StaffBonusDayService staffBonusDayService;
	
	@Autowired
	private InitDataDeleteService initDataDeleteService;
	
	
	@Autowired
	private OdsMonthPaymentRecieptService odsMonthPaymentRecieptService;

	@Autowired
	private SchedulerCountService schedulerCountService;
	
	@Autowired
	private TransferCustomerService transferCustomerService;
	
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private TwoTeacherClassService twoTeacherClassService;

	@Autowired
	private OperationCountService operationCountService;
	
	@Autowired
    private SystemConfigService systemConfigService;
	
	@Autowired
	private DisabledCustomerService disabledCustomerService;
	
	@Autowired
	private UploadFileService uploadFileService;
	
	@Autowired
	private ItemInstanceFileService itemInstanceFileService;

	@Autowired
	private RegionService regionService;
	
	/**
	 * 员工登陆账号
	 * @param account
	 * @param passwordMd5
	 * @return
	 */
	@RequestMapping(value ="/login")
	@ResponseBody
	public Response login(String account, String passwordMd5,MobileUserType mobileUserType) {
		CurrentLoginUserResponse response = new CurrentLoginUserResponse();
		log.info("mobile login, account:" + account);
		
		Integer flag = 0;
		Map<String, String> resultmap = null;
		
		LoginResponse loginRsp = commonService.login(account, passwordMd5,mobileUserType);
		if(loginRsp.getResultCode()==0){
			try {
				response.setOssAccessUrl(PropertiesUtils.getStringValue("oss.access.url.prefix"));
				response.setUserId(loginRsp.getUser().getUserId());
				response.setAccount(loginRsp.getUser().getAccount());
				response.setName(loginRsp.getUser().getName());
				if(loginRsp.getEmployeeNo()!=null) {//培优为空
					response.setEmployeeNo(loginRsp.getEmployeeNo());//员工工号
				}
				Organization org = userService.getOrganizationById(loginRsp.getUser().getOrganizationId());
				response.setOrganizationType(org.getOrgType());
				
				response.setRoleName(loginRsp.getUser().getRoleName());
				response.setOrganizationName(loginRsp.getUser().getOrganizationName());
				response.setOrganizationId(loginRsp.getUser().getOrganizationId());
				response.setContact(loginRsp.getUser().getContact());//手机号
				
				response.setMobileUserId(loginRsp.getMobileUser().getId());
		        response.setContact(loginRsp.getUser().getContact());
		        
		        if(loginRsp.getMobileUser()!=null){
					response.setPlatFormChannelId(loginRsp.getMobileUser().getPlatFormChannelId());
					response.setPlatFormUserId(loginRsp.getMobileUser().getPlatFormUserId());
				}
		        if(loginRsp.getMobileUser().getMobileType()!=null){
		        	response.setMobileType(loginRsp.getMobileUser().getMobileType().toString());
		        }
		        
		        
		        for (Role role : loginRsp.getUser().getRole()) {//是否是咨询师或者咨询主管
					if(role.getRoleCode()!=null && (RoleCode.CONSULTOR.equals(role.getRoleCode()) || RoleCode.CONSULTOR_DIRECTOR.equals(role.getRoleCode()))){
						response.setIsConsultor("1");
					}
				}
		        
		        String mailAddress = loginRsp.getUser().getMailAddr();
	            if (StringUtil.isBlank(mailAddress)) {
	                mailAddress = mailService.appendDomain(loginRsp.getUser().getAccount());
	            }
	            response.setEmailAccount(mailAddress);//email 帐号
		        response.setCcpAccount(loginRsp.getUser().getCcpAccount());//荣联帐号
		        response.setCcpPwd(loginRsp.getUser().getCcpPwd());//荣联密码
		        response.setCcpStatus(loginRsp.getUser().getCcpStatus());//状态
		        response.setMenuList(loginRsp.getMenuList());//权限列表
		        
		        String imagePath = PropertiesUtils.getStringValue("oss.access.url.prefix")+"MOBILE_HEADER_BIG_"+loginRsp.getUser().getUserId()+".jpg";
		        response.setHeadImagePath(imagePath);//头像路径
		        
		        response.setJobName(loginRsp.getUser().getJobName());//主职位
		        response.setDeptName(loginRsp.getUser().getDeptName());//主部门
		        
		        //获取当前用户 所拥有的rolecode
				List<Role> currentUserRoles = loginRsp.getUser().getRole();
				//本人所拥有的rolecode
				List<RoleCode> currentRoleCode = new ArrayList<RoleCode>(); //本人所拥有的rolecode
				for (Role role : currentUserRoles) {
					currentRoleCode.add(role.getRoleCode());
				}
				response.setUserRoleCode(currentRoleCode);	
		      		
		        response.setToken(TokenUtil.genToken(loginRsp.getUser()));
		
		        //把所有权限拼起来
				List<Role> roles = userService.getRoleByUserId(loginRsp.getUser().getUserId());      		
				String roleNames = "";
				for (Role role : roles) {
					roleNames += role.getName() + ",";
				}      		
				if (roleNames.length() > 0) roleNames = roleNames.substring(0, roleNames.length() - 1);      		
				response.setAllRoleName(roleNames);
		      		
				List<Organization> organizations = userService.findOrganizationByUserId(loginRsp.getUser().getUserId());      		
				String organizationNames = "";
				for (Organization organization : organizations) {
					organizationNames += organization.getName() + ",";
				}      		
				if (organizationNames.length() > 0) organizationNames = organizationNames.substring(0, organizationNames.length() - 1);      
				response.setAllOrganizationName(organizationNames);	
				
		        TokenUtil.checkToken(response.getToken());
		       
		        
		    	//为了兼容扫一扫 临时启用	             
		        Boolean isOpen = false;//2000没开通扫一扫，2001开通扫一扫
		        String userId = loginRsp.getUser().getUserId();
		        isOpen = checkCodeForShaoYiShao(isOpen, userId);
		        response.setOpenScan(isOpen);
		        
		        //返回当前登录者所属分公司id
	            Organization blanch = userService.getBelongBranchByUserId(userId);
	            if(blanch!=null) {
	            	response.setBrenchId(blanch.getId());
	            }
	            
	            //增加返回字段
	            response.setNoneBossUser(false);

			} catch (NullPointerException ex) {
				//
				flag = 1;				
			} catch (Exception ex){			
				//
				flag = 1;
				
			}
			log.info("response :" + response.toString());
			return response;
		}else {
			flag = 2;
			//再次请求OA接口
			resultmap = commonService.checkLoginInfoByLoginPlat(account, passwordMd5,"oa");			
			if(resultmap.get("account")==null){
				Response res=new Response();
				res.setResultCode(loginRsp.getResultCode());
				res.setResultMessage(loginRsp.getResultMessage());
				log.info("response :" + res.toString());
				return res;
			}
		}
		
		if(flag==1||flag ==2){
			//返回特定的信息给APP
			//返回固定的一套数据给APP
			List<ResourceVo> resourcelist = new ArrayList<>();
			response.setMenuList(resourcelist);
			response.setBrenchId(null);
			response.setCcpStatus(0);//ccpAccount ccpPwd ccpStatus
			response.setDeptName(null);
			response.setEmailAccount(null);
			response.setIsConsultor(null);
			response.setJobName(null);
			response.setOpenScan(false);
			response.setMobileUserId(null);
			response.setOssAccessUrl(PropertiesUtils.getStringValue("oss.access.url.prefix"));
			response.setUserRoleCode(null);
			response.setToken("HRMSTOKEN");
			if(flag==1){
				response.setNoneBossUser(false);
				response.setUserId(loginRsp.getUser().getUserId());
				response.setAccount(loginRsp.getUser().getAccount());
				response.setName(loginRsp.getUser().getName());
				response.setContact(loginRsp.getUser().getContact());//手机号
				response.setEmployeeNo(loginRsp.getEmployeeNo());//员工号
			}else{
				response.setNoneBossUser(true);
				response.setAccount(resultmap.get("account"));
				response.setName(resultmap.get("name"));
				response.setContact(resultmap.get("phone"));
				response.setEmployeeNo(resultmap.get("employeeNo"));//员工号
			}
		}
		
		
		
		log.info("response :" + response.toString());
		return response;
	}
	
	private Boolean checkCodeForShaoYiShao(Boolean isOpen,String userId){       
        if(StringUtil.isNotBlank(userId)){	        	
        	Boolean isOpenShaoYiShao = Boolean.valueOf(IsOpenShaoYiShao);
        	if(!isOpenShaoYiShao){
        		Organization blCampus = userService.getBelongCampusByUserId(userId);
                if(blCampus!=null){
                	if(StringUtil.isNotBlank(WhiteOrgList)){
                		String orgId = blCampus.getId();
                		String[] orgIds = WhiteOrgList.split(",");
                		for(String id:orgIds){
                			if(id.equals(orgId)){
                				isOpen = true;
                				break;
                			}
                		}
                	}
                }		        	
	        	if(!isOpen){
		        	if(StringUtil.isNotBlank(WhiteUserList)){
		        		String[] userIds= WhiteUserList.split(",");
		        		//白名单里面的可以开通
		        		for(String id:userIds){
		        			if(id.equals(userId)){
		        				isOpen = true;
		        				break;
		        			}
		        		}		        		
		        	}
	        	}
	        	
        	}else{
        		//开通了就不需要校验userList 和orgList
        		isOpen = true;
        	}	        	
        }
        return isOpen;
	}

	@RequestMapping(value ="/getBossLoginUserMenuList",method = RequestMethod.POST)
	@ResponseBody
	public Response login(HttpServletRequest request) {
		Response res = new Response();
		String token = request.getHeader("token");
		log.info("菜单Token, token:" + token);
		if(TokenUtil.checkInterfaceToken(token,request)) {
			res.setData(userService.getResourcesByEmployeeNo(request.getHeader("X-XH-EmployeeNo"), RoleResourceType.APP.getValue()));
		}else{
			res.setResultCode(-1);
			res.setResultMessage("token校验失败！");
		}
		return res;
	}
	
	@RequestMapping(value ="/autoCompleteEcsClass")
	@ResponseBody
	public void autoCompleteEcsClass() {
		try {
			schedulerCountService.autoCompleteEcsClass();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前用户 所拥有的rolecode
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value ="/getRoleCodeByTokenUserId")
	@ResponseBody
	public List<String> getRoleCodeByTokenUserId(@RequestParam String token, @RequestParam String userId) {
		if(!TokenUtil.checkTokenUserIdWithException(token, userId))
		{
			throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
		}
		//获取当前用户 所拥有的rolecode
		List<Role> currentUserRoles = TokenUtil.getTokenUser(token).getRole();
		//本人所拥有的rolecode
		List<String> currentRoleCode = new ArrayList<String>(); //本人所拥有的rolecode
		for (Role role : currentUserRoles) {
			if(role.getRoleCode()==null){
				currentRoleCode.add(RoleCode.TRAINING_COMMER_USER.getValue());
			}else if(role.getRoleCode() != RoleCode.TRAINING_ADMIN && role.getRoleCode() != RoleCode.TRAINING_SYSTEM_ADMIN && role.getRoleCode() != RoleCode.TRAINING_SUPER_ADMIN){
				currentRoleCode.add(RoleCode.TRAINING_COMMER_USER.getValue());
			}else{
				currentRoleCode.add(role.getRoleCode().getValue());
			}
		}
		return currentRoleCode;
	}
	
	/**
	 * 批量获取用户信息
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value ="/getBatchUserInfoById")
	@ResponseBody
	public List<UserInfoForMobileVo> getBatchUserInfoById(@RequestParam String userIds) {
		List<UserInfoForMobileVo> result = new ArrayList<UserInfoForMobileVo>();
		
		String imagePath = PropertiesUtils.getStringValue("oss.access.url.prefix")+"MOBILE_HEADER_BIG_";
		String[] ids = userIds.split(",");
		for(String id:ids)
		{
			UserInfoForMobileVo userInfoVo = new UserInfoForMobileVo(); 
			User user = userService.getUserById(id);
			userInfoVo.setUserId(id);
			userInfoVo.setName(user.getName());
			userInfoVo.setHeadImagePath(imagePath+id+".jpg");
			
			result.add(userInfoVo);
		}
		
		return result;
	}
	
	/**
	 * 批量获取用户基本信息
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value ="/getBatchUserInfoByIds", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, List<UserOrg>> getBatchUserInfoByIds(@RequestParam String userIds) {
		Map<String, List<UserOrg>> map = new HashMap<String, List<UserOrg>>();
		map.put("userInfos", userService.getBatchUserInfoByIds(userIds));
		return map;
	}
	
	@RequestMapping(value ="/getUserBasiInfo")
	@ResponseBody
	public Map<String, List<UserOrg>> getUserBasicInfo()
	{
		Map<String, List<UserOrg>> map = new HashMap<String, List<UserOrg>>();
		List<UserOrg> userOrgs = userService.getAllUserInfo();
		map.put("userOrgs", userOrgs);
		return map;
		
	}
	
	/**
	 * 获取登录用户的信息
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value ="/getLoginUserInfoByParam")
	@ResponseBody
	public Response getLoginUserInfoByParam(@RequestParam String userId, @RequestParam String token) {
		if(!TokenUtil.checkTokenUserIdWithException(token, userId))
		{
			throw new ApplicationException("请先登录！");
		}
		User user = TokenUtil.getTokenUser(token);

		CurrentLoginUserResponse response = new CurrentLoginUserResponse();
		
		response.setUserId(user.getUserId());
		response.setAccount(user.getAccount());
		response.setName(user.getName());
		
		String imagePath = PropertiesUtils.getStringValue("oss.access.url.prefix")+"MOBILE_HEADER_BIG_"+user.getUserId()+".jpg";
        response.setHeadImagePath(imagePath);//头像路径
        
        //获取当前用户 所拥有的rolecode
		List<Role> currentUserRoles = user.getRole();
		//本人所拥有的rolecode
		List<RoleCode> currentRoleCode = new ArrayList<RoleCode>(); //本人所拥有的rolecode
		if(currentUserRoles==null || currentUserRoles.size()==0){
			currentUserRoles=userService.getRoleByUserId(user.getUserId());	
		}
		for (Role role : currentUserRoles) {
			currentRoleCode.add(role.getRoleCode());
		}
		response.setUserRoleCode(currentRoleCode);	
        response.setToken(token);
		
		return response;
	}
	
	/**
	 * 共享平台登录接口
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value ="/sharePlatformLogin")
	@ResponseBody
	public Response sharePlatformLogin() throws UnsupportedEncodingException {
		Map<String, String> param = new HashMap<String, String>();
		
		Response rsp = new Response();
		User user = userService.getCurrentLoginUser();
		if(user==null){
			rsp.setResultCode(-1);
			rsp.setResultMessage("请重新登录！");
			return rsp;
		}

		Organization org = organizationDao.findById(user.getOrganizationId());
		
		param.put("oname", org.getName());
		param.put("oid", org.getId());
		param.put("orgLevel", org.getOrgLevel());
		param.put("orgType", org.getOrgType().getValue());
		param.put("parent", org.getParentId());
		param.put("id", user.getUserId());
		param.put("password", user.getPassword());
		param.put("name", user.getName());
		param.put("username", user.getAccount());
		
		JSONObject jsonParam = new JSONObject(param);
		
		String url = SHARE_PLATFORM_URL + "?info="+ URLEncoder.encode(jsonParam.toString(), "utf-8");
        
		rsp.setResultMessage(url);
        return rsp;
	}
	
	
	/**
	 * 一对一的考勤登记
	 * @param token
	 * @param courseId
	 * @param courseHours
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/submitCourseAttendance")
	@ResponseBody
	public Response submitCourseAttendance(@RequestParam String token, @RequestParam String courseId, @RequestParam String courseHours,String courseAttenceType,String courseVersion,String attendanceDetail) throws Exception {
		TokenUtil.checkTokenWithException(token);
		ValidationUtil.checkNumeric(courseHours);
		OneOnOneBatchAttendanceEditVo vo = new OneOnOneBatchAttendanceEditVo();
		vo.setCourseId(courseId);
		vo.setRealHours(Double.valueOf(courseHours));
		vo.setOperteType("teacherAudit");
		if(StringUtil.isNotBlank(courseAttenceType)){
			vo.setCourseAttenceType(courseAttenceType);
		}	
		if(StringUtil.isNotBlank(courseVersion)){
			vo.setCourseVersion(courseVersion);
		}
		
		if(StringUtil.isNotBlank(attendanceDetail)){
			vo.setAttendanceDetail(attendanceDetail);
		}
		courseService.oneOnOneBatchAttendanceEdit(vo);
		return new Response();
	}
	
	/**
	 * 上传 一对一 的 考勤 签名图片
	 * @param token
	 * @param courseId
	 * @param attendancePicFile
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/submitCourseAttendancePic", method = RequestMethod.POST)
	@ResponseBody
	public Response submitCourseAttendancePic(@RequestParam String token, @RequestParam String courseId, @RequestParam("attendancePic") MultipartFile attendancePicFile,HttpServletRequest request) throws Exception {
		TokenUtil.checkTokenWithException(token);
		String servicePath = request.getSession().getServletContext().getRealPath("/");
		
		Course course = courseService.getCourseByCourseId(courseId);
		if(course !=null) {
			// 统一使用 文件名 来 标明签名文件
			//String fileName = UUID.randomUUID().toString();
			String fileName =  String.format("ONE_ON_ONE_ATTEND_PIC_%s.jpg", courseId);
			course.setAttendacePicName(fileName);
			
			String folder=servicePath+PropertiesUtils.getStringValue("save_file_path");//系统路径
			isNewFolder(folder);			 
			
			String bigFileName = "BIG_"+fileName;//阿里云上面的文件名 大   默认是JPG 
			String midFileName = "MID_"+fileName;//阿里云上面的文件名 中
			String smallFileName = "SMALL_"+fileName;//阿里云上面的文件名 小					 
			
			String relFileName=folder+"/realFile_"+courseId+UUID.randomUUID().toString()+".jpg";
			File realFile=new File(relFileName);
			File bigFile=new File(folder+"/"+bigFileName);
			File midFile=new File(folder+"/"+midFileName);
			File smallFile=new File(folder+"/"+smallFileName);			
			
			try {				
				attendancePicFile.transferTo(realFile);
				ImageSizer.compressImage(relFileName, smallFile.getAbsolutePath(), 60);//转换图片大小
				ImageSizer.compressImage(relFileName, midFile.getAbsolutePath(), 200);//转换图片大小
				ImageSizer.compressImage(relFileName, bigFile.getAbsolutePath(), 600);//转换图片大小
				AliyunOSSUtils.put(bigFileName, bigFile);//传到阿里云
				AliyunOSSUtils.put(midFileName, midFile);//传到阿里云
				AliyunOSSUtils.put(smallFileName, smallFile);//传到阿里云				
				AliyunOSSUtils.put(fileName, realFile);//传原图到阿里云		
			} catch (IllegalStateException e) {
				e.printStackTrace();
				throw new IllegalStateException();
			} catch (IOException e) {
				e.printStackTrace();
				throw new ApplicationException();
			}finally{				
				bigFile.delete();
				midFile.delete();
				smallFile.delete();
				realFile.delete();
			}	
			
			courseService.updateCourse(course);
			
			//mobilePushMsgService.saveFileToRemoteServer(attendancePicFile, fileName);
		}
//		
//		courseService.saveCourseAttendancePic(courseId, attendancePicFile,servicePath);
		return new Response();
	}
	private void isNewFolder(String folder){
		File f1=new File(folder);
		if(!f1.exists())
		{
			f1.mkdirs();
		}
	}
	
	
	/**
	 * 获取courseID相应的  一对一的课程信息
	 * @param token
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value="/findCourseById")
	@ResponseBody
	public CourseVo findCourseById(@RequestParam String token, @RequestParam String courseId) {
		TokenUtil.checkTokenWithException(token);
		return courseService.findCourseById(courseId);
	}
	
	/**
	 * 获取一对一的考勤记录
	 * @param token
	 * @param dataPackage
	 * @return
	 */
	@RequestMapping(value="/getAttendanceRecordVos")
	@ResponseBody
	public DataPackage getAttendanceRecordVos(@RequestParam String token, @ModelAttribute DataPackage dataPackage) {
		TokenUtil.checkTokenWithException(token);
		CourseAttendanceRecordVo record = new CourseAttendanceRecordVo();
		record.setCheckUserId(TokenUtil.getTokenUser(token).getUserId());
		return courseService.getAttendanceRecordVos(record, dataPackage);
	}
	
	/**
	 * 获取 一对一的 课程的课程列表， 根据搜索条件 
	 * @param token
	 * @param searchInputVo
	 * @param dataPackage
	 * @return
	 */
	@RequestMapping(value="/getCourseList")
	@ResponseBody
	public DataPackage getCourseList(@RequestParam String token, @ModelAttribute CourseVo courseVo, @ModelAttribute DataPackage dp) {
		TokenUtil.checkTokenWithException(token);
//		return courseService.getCourseList(searchInputVo, dataPackage);
		//return courseService.findPageCourseForOneWeek(searchInputVo, dataPackage);
		return dp = courseService.getOneOnOneBatchAttendanceList(courseVo, dp);
	}
	
	/**
	 * 获取 一对一的 课程的课程列表， 根据搜索条件， 是   ### 学生 app 的查询 ###
	 * @param token
	 * @param searchInputVo
	 * @param dataPackage
	 * @return
	 */
	@RequestMapping(value="/getCourseListForStudent")
	@ResponseBody
	public List<CourseWeekVo> getCourseListForStudent(@RequestParam String token, @ModelAttribute CourseSearchInputVo searchInputVo, @ModelAttribute DataPackage dataPackage) {
		TokenUtil.checkTokenWithException(token);
		List<CourseWeekVo> courseVoList = (List<CourseWeekVo>) courseService.findPageCourseForOneWeekForStudent(searchInputVo, dataPackage).getDatas();
		return courseVoList;
		
	}

    /**
     * 根据考勤 类型 获取到相应的课程记录
     * @param token
     * @param strAttendanceType
     * @param attendanceNo
     * @param date
     * @return
     * @throws Exception
     */
	@Deprecated
    @RequestMapping(value="/getCourseListByAttendanceType")
    @ResponseBody
    public DataPackage getCourseListByAttendanceType(String token, String strAttendanceType, String attendanceNo, String date) throws Exception {
        AttendanceType attendanceType = AttendanceType.valueOf(strAttendanceType);
        if(AttendanceType.SYSTEM!=attendanceType) {
            TokenUtil.checkTokenWithException(token);
        }
        if (AttendanceType.IC_CARD == attendanceType ) {
            date=DateTools.getCurrentDate();//打卡是系统时间
            return courseService.readCourseListByAttendanceType(AttendanceType.IC_CARD, attendanceNo, date);
        } else if (AttendanceType.FINGERPRINT == attendanceType ) {
            date=DateTools.getCurrentDate();//打卡是系统时间
            return courseService.readCourseListByAttendanceType(AttendanceType.FINGERPRINT, attendanceNo, date);
        } else if(AttendanceType.SYSTEM==attendanceType){
            return courseService.readCourseListByAttendanceType(AttendanceType.SYSTEM,attendanceNo,date);
        }else{
            throw new ApplicationException("找不到匹配的考勤类型，请检查！");
        }
    }
	
	

	
	
	
	/**
	 * 保存电话事件
	 * @param token 令牌
	 * @param phone 电话号码
	 * @param callsTime 来电时间
	 * @param phoneType 来电类型
	 * @param phoneEvent 来电时间
	 * @return
	 */
	@RequestMapping(value = "/savePhoneLogEvent")
	@ResponseBody
	public String savePhoneLogEvent(String token,String phone,String callsTime, PhoneType phoneType,PhoneEvent phoneEvent){
		if(!TokenUtil.checkToken(token)){
			return "token_error";
		}
		customerService.saveCustomerCallsLog(phone,callsTime,phoneType ,phoneEvent);
		return "true";
	}
	
	/**
	 * 根据电话查询客户
	 * @param token 令牌
	 * @param phone 电话
	 * @param callsStatus 0 新的来电 做日志处理
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findCustomerByPhone")
	@ResponseBody
	public CustomerVo findCustomerByPhone(@RequestParam String token,@RequestParam String phone,@RequestParam String callsStatus,@RequestParam String callsTime,HttpServletRequest request,HttpServletResponse response) throws Exception {
		if(!TokenUtil.checkToken(token)){
			CustomerVo vo=new CustomerVo();
			vo.setId("token_error");
			CookieUtil.putCookieCallsPhoneCheckFail(request, response, phone, callsTime);
			return vo;
		}
//		if("0".equals(callsStatus)){
//			customerService.saveCustomerCallsLog(phone,callsTime);
//		}
		CustomerVo customerVo =  customerService.findCustomerByPhone(phone);
		if (customerVo == null) {
		    DisabledCustomer disCus = disabledCustomerService.findDisabledCustomerByContact(phone, 0);
		    if (disCus != null) {
		        customerVo = new CustomerVo();
		        customerVo.setContact(phone);
		        customerVo.setRemark(disCus.getRemark());
		    }
		}
		return customerVo;
	}
	
	@RequestMapping(value = "/getSelectOption")
	@ResponseBody
	public SelectOptionResponse getSelectOption(@RequestParam String selectOptionCategory) {
		SelectOptionResponse selectOptionResponse = commonService.getSelectOption(selectOptionCategory, null);
		selectOptionResponse.getValue().remove("");
		return selectOptionResponse;
	}

	/**
	 * 保存来电信息
	 * @param token
	 * @param follow
	 * @param cus
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveFollowRecord")
	@ResponseBody
	public Response saveFollowRecord(@RequestParam String token,@ModelAttribute CustomerFolowupVo follow, Customer cus) throws Exception {
		if(!TokenUtil.checkToken(token)){
			return new Response(1,"令牌失效！");
		}
		Response res=new Response();
		try {
			if(StringUtils.isNotEmpty(follow.getCustomerId())){
				if (!StringUtils.isBlank(follow.getRemark())) {
					follow.setRemark(URLDecoder.decode(follow.getRemark(), "utf-8"));
				}
				customerService.saveCustomerFollowRecord(follow); 
			}else{
				customerService.saveOrUpdateCustomer(cus);
				//saveOrUpdateCustomer方法中已经有保存跟进记录
//				follow=new CustomerFolowupVo();
//				follow.setCustomerId(cus.getId());
//				follow.setRemark(cus.getRemark());
//				customerService.saveCustomerFollowRecord(follow); 
			}
		} catch(ApplicationException e){
			e.printStackTrace();
			res.setResultCode(1);
			if(e.getErrorCode()!=null && e.getErrorCode().getValue()!=null){
				String value=e.getErrorCode().getValue();
				if(value.indexOf("-")!=-1){
					value=value.substring(value.indexOf("-")+1);
				}
				res.setResultMessage(value);
			}else{
				res.setResultMessage("操作失败请联系管理员");
			}
		}
		return res;
	}
	
	@RequestMapping(value = "/getFollowingRecord")
	@ResponseBody
//	传入的变量要商量一下
	public DataPackageForJqGrid getFollowingRecord(@ModelAttribute GridRequest gridRequest, @ModelAttribute Customer cus) throws Exception {
		DataPackageForJqGrid dataPackageForJqGrid = null;
		DataPackage dataPackage = new DataPackage(0,999);
		dataPackage = customerService.gtCustomerFollowingRecords(cus, dataPackage); 
		dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}
	
	@RequestMapping(value = "/getCookieCallsPhoneCheckFail", method =  RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getCookieCallsPhoneCheckFail(HttpServletRequest request,HttpServletResponse response){
		try {
			return CookieUtil.getCookieCallsPhoneCheckFail(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/getCapumsForSelection")
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
	
	/**
	 * 根据角色Code 和组织架构查询下
	 * @param roleCode
	 * @param parentOrgId
	 * @return
	 */
	@RequestMapping(value = "/getStaffForByRoleCodeSelection")
	@ResponseBody
	public SelectOptionResponse getStaffForByRoleCodeSelection( String roleCode,  String parentOrgId) {
		SelectOptionResponse selectOptionResponse = null;
		try {
			if (roleCode == null) {
				roleCode=RoleCode.STUDY_MANAGER.toString();
			}
			
			if (StringUtils.isBlank(parentOrgId)) {//如果不传，则默认用员工当前的组织架构
				User user = userService.getCurrentLoginUser();
				parentOrgId = user.getOrganizationId();
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
	
	
	/** 学生家长端APP接口 */
	
	/**
	 * 学生登录与注册
	 * 根据学号查询个人信息：姓名、年级、学校、归属校区、校区电话、班主任、电话、一对一剩余课时数, 在登录接口中查询了
	 */
	@RequestMapping(value = "/studentLogin")
	@ResponseBody
	public AppStudentLoginResponse studentLogin(String studentId, String appPassowrd) {
		AppStudentLoginResponse response = studentService.appLogin(studentId, appPassowrd);
		//生成token时，绑定学生的班主任和学生自己
		response.setToken(TokenUtil.genStudentToken(studentService.getStudentStudyManager(studentId), response.getStudent()));
		//throw new ApplicationException("Test 错误信息");
		
		return response;
	}

	
	
	/**
	 * 
	 * 根据学号查询校区联系人列表
	 */
	public List<UserVo> getStudentReferenceCampusContact(String token, String studentId) {
		return studentService.getStudentReferenceCampusContact(studentId);
	}
	
	/**
	 * 新增转介绍
	 */
	public Response introduceCustomer(@RequestParam String token, String cusName, String cusContact, String remark) {
		TokenUtil.checkTokenWithException(token);
		Customer cus = new Customer();
		cus.setCusType(new DataDict("INTRODUCE"));
		cus.setContact(cusContact);
		cus.setName(cusName);
		cus.setRemark(remark);
		cus.setIntroducer(TokenUtil.getTokenStudent(token).getName());
		customerService.saveOrUpdateCustomer(cus);
		return new Response();
	}
	
	/**
	 * 根据姓名查询转介绍列表
	 */
	public DataPackage getIntroduceCustomerPage(String token, DataPackage dataPackage) {
		CustomerVo cusVo = new CustomerVo();
		cusVo.setCusType("INTRODUCE");
		cusVo.setIntroducer(TokenUtil.getTokenStudent(token).getName());
		return customerService.getCustomers(cusVo, dataPackage);
	}
	
	/**
	 * 根据学号查询学生成绩列表
	 */
	
	
	/**
	 * 市场APP使用
	 * @param gridRequest
	 * @param cus
	 * @param onlyShowUndelive
	 * @return
	 */
	@RequestMapping(value = "/getCustomersForJqGrid")
	@ResponseBody
	public DataPackageForJqGrid getCustomersForJqGrid(@RequestParam String token,@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerVo cus, @RequestParam(required=false) String onlyShowUndelive) {
		log.info("getCustomersForJqGrid() start.");
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.getCustomers(cus, dataPackage, "true".equalsIgnoreCase(onlyShowUndelive)?true:false);
		
		log.info("getCustomersForJqGrid() end.");
		return new DataPackageForJqGrid(dataPackage);
	}
	
//	/**
//	 * 
//	 * @param gridRequest
//	 * @param cus
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/editCustomer")
//	@ResponseBody
//	public Response editCustomer(@RequestParam String token,@ModelAttribute GridRequest gridRequest, @ModelAttribute Customer cus) throws Exception {
//		TokenUtil.checkTokenWithException(token);
//		if (cus == null) {
//			throw new ApplicationException(ErrorCode.RES_NOT_FOUND);
//		}
//		//if (StringUtils.isBlank(cus.getId())) {
//		//	cus.setId("");
//		//}
//		String customerId = "";
//		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
//			customerService.deleteCustomer(cus.getId());
//		} else {
//			customerId = customerService.saveOrUpdateCustomer(cus); 
//		}
//		return new Response(0, customerId);
//	}
	
	@RequestMapping(value = "/findCustomerById")
	@ResponseBody
	public CustomerVo findCustomerById(@RequestParam String token,@ModelAttribute GridRequest gridRequest, @ModelAttribute Customer cus) throws Exception {
		TokenUtil.checkTokenWithException(token);
		return customerService.findCustomerById(cus.getId()); 
	}


	/**
	 *
	 * @param token
	 * @param id
     * @return
     */
	@RequestMapping(value = "/customerById")
	@ResponseBody
	public CustomerLessVo customerById(@RequestParam String token, @RequestParam String id){
		TokenUtil.checkTokenWithException(token);
		return customerService.findCustomerLessVoById(id);
	}
	
	@RequestMapping(value = "/getStaffForSelection")
	@ResponseBody
	public SelectOptionResponse getStaffForSelection(@RequestParam String roleId, @RequestParam String parentOrgId) {
		SelectOptionResponse selectOptionResponse = null;
		try {
			if (roleId == null) {
				throw new ApplicationException(ErrorCode.SYSTEM_ERROR);
			}
			
			if (StringUtils.isBlank(parentOrgId)) {//如果不传，则默认用员工当前的组织架构
				User user = userService.getCurrentLoginUser();
				parentOrgId = user.getOrganizationId();
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
	
	/**
	 * 修改学生指纹
	 * @param token
	 * @param studentId
	 * @param fingerInfo
	 * @return 0 失败 1成功
	 */
	@RequestMapping(value = "/uploadStudentFingerInfo")
	@ResponseBody
	public String uploadStudentFingerInfo(@RequestParam String token,@RequestParam String studentFingerNo, @RequestParam String studentId,@RequestParam String fingerInfo){
		if(TokenUtil.checkToken(token) && StringUtils.isNotEmpty(studentId) && StringUtils.isNotEmpty(fingerInfo)){
			return studentService.uploadStudentFingerInfo(studentFingerNo,studentId,fingerInfo);
		}
		return "0";
	}
	
	@RequestMapping(value = "/getAllStudentFingerInfoDownloadUrl")
	public void getAllStudentFingerInfoDownloadUrl(HttpServletResponse response){
		//判断是否为debug模式，如果是生产环境即去阿里云下载
		 if(!Constants.DEBUG){//生产环境
		     //跳转到阿里云文件
			String url=PropertiesUtils.getStringValue("oss.access.url.prefix")+"fingerInfo.zip";
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		  }else{//debug模式就下载本地文件
			  String filePath=new File(getClass().getResource("/").getFile().toString()).getParentFile().getParent()+"/uploadfile/fingerInfo.zip";
			  FileDownLoadUtil.down(response,filePath);
		  }		
		
	}
	
	//@Scheduled(cron="0 42/16 * * * ?")  
	@Scheduled(cron="0 0/30 * * * ?")  
	@RequestMapping(value = "/produceAllStudentFingerInfo")
	public void produceAllStudentFingerInfo(){
//		List<Object[]> list=studentService.getAllStudentFingerInfo();
//		String filePath=new File(getClass().getResource("/").getFile().toString()).getParentFile().getParent()+"/uploadfile/fingerInfo.text";
//		try{
//			filePath=URLDecoder.decode(filePath,"UTF-8");
//			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
//			for(Object[] obj :list){
//				writer.write(obj[0]+","+obj[1]+","+obj[2]+"\r\n");
//			}
//		    writer.close();
//		    FileUtil.zipSingleFile(filePath, filePath.substring(0,filePath.length()-4)+"zip");
//		}catch(Exception e){
//	
//		}	
		/**按校区进行存放*/
		//查询所有校区ID
//		List<Object> organizationList=studentService.getAllStudentOrganization();				
//		List<Map> studentIdList=null;
//		StudentFingerInfo studentFingerInfo=null;//指纹记录
//		Object studentId=null;
//		for(Object organizationId :organizationList){
//			String filePath=new File(getClass().getResource("/").getFile().toString()).getParentFile().getParent()+"/uploadfile/"+organizationId+"_fingerInfo.text";
//			try{
//				filePath=URLDecoder.decode(filePath,"UTF-8");
//				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
//				//查询该校区下所有学生状态为"上课中"的学生ID				
//				studentIdList=studentService.getStudentIdByOrganizationId(organizationId.toString());
//				for(Map student_map :studentIdList){		
//					studentId=student_map.get("ID");
//					if(studentId!=null){
//						 studentFingerInfo=studentService.getStudentFingerInfoByStudentId(studentId.toString());
//						//studentFingerNo,studentId,fingerInfo
//						if(studentFingerInfo!=null){
//							writer.write(studentFingerInfo.getStudentFingerNo()+","+studentFingerInfo.getStudentId()+","+studentFingerInfo.getFingerInfo()+"\r\n");
//						}	
//					}
//									
//				}
//			    writer.close();
//			    FileUtil.zipSingleFile(filePath, filePath.substring(0,filePath.length()-4)+"zip");
//			}catch(Exception e){
//		
//			}			
//		}
		
		
		/**拿所有学生状态为“上课中”的学生指纹*/
//		List<Object[]> list=studentService.getAllStudentFingerInfoByStatus();
//		String filePath=new File(getClass().getResource("/").getFile().toString()).getParentFile().getParent()+"/uploadfile/fingerInfo.text";
//		try{
//			filePath=URLDecoder.decode(filePath,"UTF-8");
//			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
//			for(Object[] obj :list){
//				writer.write(obj[0]+","+obj[1]+","+obj[2]+"\r\n");
//			}
//		    writer.close();
//		    String zip_path=filePath.substring(0,filePath.length()-4)+"zip";
//		    FileUtil.zipSingleFile(filePath, zip_path);		    
//		    
//		    //File zipFile=new File(zip_path);
//		    //AliyunOSSUtils.put("fingerInfo.zip", zipFile);//传到阿里云
//		}catch(Exception e){
//	
//		}
		
		/**拿所有今天有课的学生指纹*/
		List<Object[]> list=studentService.getTodayStudentFingerInfo();
		String filePath=new File(getClass().getResource("/").getFile().toString()).getParentFile().getParent()+"/uploadfile/fingerInfo.text";
		try{
			filePath=URLDecoder.decode(filePath,"UTF-8");
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
			for(Object[] obj :list){
				writer.write(obj[0]+","+obj[1]+","+obj[2]+"\r\n");
			}
		    writer.close();
		    String zip_path=filePath.substring(0,filePath.length()-4)+"zip";
		    FileUtil.zipSingleFile(filePath, zip_path);		    
		    
		    //判断是否为debug模式还是生产模式，如果是生产环境就上传到阿里云
		    if(!Constants.DEBUG){
	    	   File zipFile=new File(zip_path);
	           AliyunOSSUtils.put("fingerInfo.zip", zipFile);//传到阿里云
		    }
		  
		}catch(Exception e){
	
		}
				 
	}

    /**
     * 获取班组学生考勤列表
     * @param token
     * @param dp
     * @param miniClassStudentVo
     * @return
     * @throws Exception
     */
	@RequestMapping(value ="/getMiniClassStudentAttendentList")
	@ResponseBody
	public List<MiniClassStudentVo> getMiniClassStudentAttendentList(@RequestParam String token,@ModelAttribute MiniClassStudentVo miniClassStudentVo) throws Exception {
		TokenUtil.checkTokenWithException(token);
		DataPackage dp = new DataPackage(0, 1000);		
		List<MiniClassStudentVo> miniClassStudentVoList = (List<MiniClassStudentVo>) smallClassService.getMiniClassStudentAttendentList(miniClassStudentVo, dp).getDatas();
		return miniClassStudentVoList;
	}






	/**
     * 班组课程列表
     * @param token
     * @param dp
     * @param miniClassCourseVo
     * @return
     */
	@RequestMapping(value ="/getMiniClassCourseList")
	@ResponseBody
	public DataPackageForJqGrid getMiniClassCourseList(@RequestParam String token, @ModelAttribute DataPackage dp, @ModelAttribute MiniClassCourseVo miniClassCourseVo) {
		TokenUtil.checkTokenWithException(token);
		dp = smallClassService.getMiniClassCourseList(miniClassCourseVo, dp);	
		return new DataPackageForJqGrid(dp);
	}
	
    /**
     * 班组课程列表
     * @param token
     * @param dp
     * @param miniClassCourseVo
     * @return
     */
	@RequestMapping(value ="/getMiniClassCourseListNew")
	@ResponseBody
	public DataPackage getMiniClassCourseListNew(@RequestParam String token, @ModelAttribute DataPackage dp, @ModelAttribute MiniClassCourseVo miniClassCourseVo) {
		TokenUtil.checkTokenWithException(token);
		dp = smallClassService.getMiniClassCourseListForMobile(miniClassCourseVo, dp);
		return dp;
	}






	/**
     * 班组课程列表  #  学生的接口  #
     * @param token
     * @param dp
     * @param miniClassCourseVo
     * @return
     */
	@RequestMapping(value ="/getMiniClassCourseListForStudent")
	@ResponseBody
	public List<MiniClassCourseVo> getMiniClassCourseListForStudent(@RequestParam String token, @ModelAttribute DataPackage dp, @ModelAttribute MiniClassCourseVo miniClassCourseVo) {
		TokenUtil.checkTokenWithException(token);
		dp = smallClassService.getMiniClassCourseList(miniClassCourseVo, dp);
		List<MiniClassCourseVo> miniClassCourseVoList = (List<MiniClassCourseVo>) smallClassService.getMiniClassCourseList(miniClassCourseVo, dp).getDatas();
		return miniClassCourseVoList;
	}

    /**
     * 更新班组学生考勤信息
     * @param token
     * @param miniClassCourseId
     * @param attendanceData
     * @param oprationCode
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/modifyMiniClassCourseStudentAttendance")
	@ResponseBody
	public Response modifyMiniClassCourseStudentAttendance(@RequestParam String token, @RequestParam String miniClassCourseId, @RequestParam String attendanceData, @RequestParam String oprationCode) throws Exception{
		TokenUtil.checkTokenWithException(token);
		oprationCode = "charge";
		return smallClassService.modifyMiniClassCourseStudentAttendance(miniClassCourseId, attendanceData, oprationCode);
	}



	/**
	 * 获取最新版本
	 * @param appId
	 * @param mobileType
	 * @return
	 */
	@RequestMapping(value="/getAppLastVersion")
	@ResponseBody
	public AppVersion getAppLastVersion(@RequestParam String appId, @RequestParam MobileType mobileType) {
		return appFunction.getLastAppVersion(appId, mobileType);
	}
	
	@RequestMapping(value = "/getReceptionistFollowUpTargetForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getReceptionistFollowUpTargetForSelection() {
		return commonService.getReceptionistFollowUpTargetForSelection();
	}
	
	/**
	 * 保存头像图片
	 * @throws Exception 
	 * */
	@RequestMapping(value = "/saveHeaderImgFile", method =  RequestMethod.POST)
	public void saveHeaderImgFile( @RequestParam("image1") MultipartFile myfile1, @RequestParam String token, 
			@RequestParam String mobileUserId ,HttpServletRequest request) {
		TokenUtil.checkTokenWithException(token);
		String servicePath = request.getSession().getServletContext().getRealPath("/");
		
		if(mobileUserId!=null){
			//MobileUser muser = this.findMobileUserByStaffId(user.getUserId());
//				String location = System.getProperty("user.dir");
				String folder=servicePath+PropertiesUtils.getStringValue("save_file_path");//系统路径
				isNewFolder(folder);
				//String fileName=myfile1.getOriginalFilename().substring(0,myfile1.getOriginalFilename().lastIndexOf("."));//上传的文件名
				//String puff=myfile1.getOriginalFilename().replace(fileName, "");//后缀
				String bigFileName = "MOBILE_HEADER_BIG_"+mobileUserId+".jpg";//阿里云上面的文件名 大   默认是JPG 
				String midFileName = "MOBILE_HEADER_MID_"+mobileUserId+".jpg";//阿里云上面的文件名 中
				String smallFileName = "MOBILE_HEADER_SMALL_"+mobileUserId+".jpg";//阿里云上面的文件名 小
				String puff = UUID.randomUUID().toString();
				String relFileName=folder+"/realFile"+puff+".jpg";
				File realFile=new File(relFileName);
				File bigFile=new File(folder+"/"+bigFileName);
				File midFile=new File(folder+"/"+midFileName);
				File smallFile=new File(folder+"/"+smallFileName);
				try {
					myfile1.transferTo(realFile);
					ImageSizer.compressImage(relFileName, smallFile.getAbsolutePath(), 60);//转换图片大小
					ImageSizer.compressImage(relFileName, midFile.getAbsolutePath(), 200);//转换图片大小
					ImageSizer.compressImage(relFileName, bigFile.getAbsolutePath(), 600);//转换图片大小
					AliyunOSSUtils.put(bigFileName, bigFile);//传到阿里云
					AliyunOSSUtils.put(midFileName, midFile);//传到阿里云
					AliyunOSSUtils.put(smallFileName, smallFile);//传到阿里云
					
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					realFile.delete();
					bigFile.delete();
					midFile.delete();
					smallFile.delete();
				}
		}else{
				throw new ApplicationException("找不到手机用户。");
		}
	
		//将上传代码从service里面迁移出来到contorller service里面不删 xiaojinwang 20171029
		
		//mobileUserService.saveHeaderImgFile(myfile1,mobileUserId,servicePath);
		//systemConfigService.saveHeaderImgFile(myfile1);
		//return new Response();
	}
	
	/**
	 * 获取班组详情
	 * @param token
	 * @param miniClassCourseId
	 * @return
	 */
	@RequestMapping(value="/findMiniClassCourseById")
	@ResponseBody
	public MiniClassCourseVo findMiniClassCourseById(@RequestParam String token, @RequestParam String miniClassCourseId) {
		TokenUtil.checkTokenWithException(token);
		return smallClassService.findMiniClassCourseById(miniClassCourseId);
	}
	
	
	/**
	 * 根据 不同的  mobileUserId 获取系统公告消息 
	 * @param token
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/getNoticesByMobileUserId")
	@ResponseBody
	public List<MobilePushMsgRecordVo> getNoticesByMobileUserId(@RequestParam String token, @RequestParam String mobileUserId,@ModelAttribute DataPackage dp) {
		TokenUtil.checkTokenWithException(token);
		return mobilePushMsgService.getNoticesByMobileUserId(mobileUserId,dp.getPageNo(),dp.getPageSize());
	}
	
	/**
	 * 根据 不同的  mobileUserId 获取 提醒 消息 
	 * @param token
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/getRemindsByMobileUserId")
	@ResponseBody
	public List<MobilePushMsgRecordVo> getRemindsByMobileUserId(@RequestParam String token, @RequestParam String mobileUserId,@ModelAttribute DataPackage dp) {
		TokenUtil.checkTokenWithException(token);
		return mobilePushMsgService.getRemindsByMobileUserId(mobileUserId,dp.getPageNo(),dp.getPageSize());
	}


     /**
     * 公用的 获取 消息的一个 方法， 有page 的方法 定义
      * 根据 不同的  mobileUserId, sessionType 还有 是 pageNo 和 pageSize 所有的信息
     * @param token
     * @param mobileUserId
     * @param sessionType 对话类型
     * @param sessionId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value="/getMsgByMobileUserIdAndSessionType")
    @ResponseBody
    public List<MobilePushMsgRecordVo> getMsgByMobileUserIdAndSessionType(@RequestParam String token, @RequestParam String mobileUserId,
                                                                          @RequestParam SessionType sessionType, @RequestParam(required = false) String sessionId,
                                                                          @RequestParam int pageNo, @RequestParam int pageSize, @RequestParam(required = false, defaultValue = "desc") String order,String lastFetchTime) {
        TokenUtil.checkTokenWithException(token);
        // 如果设空 就是 倒序， 比如提醒 公告等信息 就是需要倒序
//        if(order == null || "desc".equals(anObject)) {
//        	order = "desc";
//        } else {
//        	order = "asc";
//        }
        return mobilePushMsgService.getMsgByMobileUserIdAndSessionType(mobileUserId, sessionType, sessionId,  pageNo, pageSize, order,lastFetchTime);
    }

    
    
    /**
     * 获取课程评论， 有page 的方法 定义
      * 根据 不同的  mobileUserId 还有 是 pageNo 和 pageSize 所有的信息
     * @param token
     * @param mobileUserId
     * @param courseId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value="/getCOURSEMsgByMobileUserIdAndCourseId")
    @ResponseBody
    public List<MobilePushMsgRecordVo> getCOURSEMsgByMobileUserIdAndCourseId(@RequestParam String token, @RequestParam String mobileUserId,
                                                                          @RequestParam(required = false) String courseId,
                                                                          @RequestParam int pageNo, @RequestParam int pageSize, @RequestParam(required = false, defaultValue = "desc") String order ,String lastFetchTime) {
        TokenUtil.checkTokenWithException(token); 
        String sessionId="";
        if(StringUtils.isNotBlank(courseId)){
        	MobilePushMsgSessionVo sessionVo = mobilePushMsgService.getSessionByCourseId(courseId, "COURSE", mobileUserId);
        	if(sessionVo!=null){
        		sessionId=sessionVo.getId();
        	}
        	 
		}else{
			 throw new ApplicationException("传入的课程ID不允许为空");
		}
        //如果没有sessionId
        if(StringUtils.isBlank(sessionId)){
        	return new ArrayList<MobilePushMsgRecordVo>();
        }
        return mobilePushMsgService.getMsgByMobileUserIdAndSessionType(mobileUserId, SessionType.COURSE, sessionId,  pageNo, pageSize, order,lastFetchTime);
    }
    
    
    /**
     * 公用的 获取 最新消息纪录的一个 方法
     * @param token
     * @param mobileUserId
     * @param sessionType 对话类型
     * @param sessionId
     * @param lastFetchTime
     * @return
     */
    @RequestMapping(value="/getLastestMsgBySessionType")
    @ResponseBody
    public List<MobilePushMsgRecordVo> getLastestMsgBySessionType(@RequestParam String token, @RequestParam String mobileUserId,
                                                                          @RequestParam SessionType sessionType, @RequestParam String sessionId,
                                                                          @RequestParam String lastFetchTime ) {
        TokenUtil.checkTokenWithException(token);
        return mobilePushMsgService.getLastestMsgByMobileUserIdAndSessionType(mobileUserId, sessionType, sessionId, lastFetchTime);
    }



	/**
	 * 向 User 推送公告消息
	 * @param token
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/pushNoticeToMobileUserId")
	@ResponseBody
	public void pushNoticeToMobileUserId(@RequestParam String token, @RequestParam String mobileUserId , @RequestParam String msgContent) {
		TokenUtil.checkTokenWithException(token);
		mobilePushMsgService.pushNoticeToMobileUserId(mobileUserId, msgContent, null);
	}
	

    /**
     * 向 MobileUser 推送公告消息
     * @param token
     * @param arrayOfmobileUserId
     * @param createMobileUserId
     * @param msgContent
     */
	@RequestMapping(value="/pushNoticeToMobileUserIds")
	@ResponseBody
	public void pushNoticeToMobileUserIds(@RequestParam String token, @RequestParam List <String> arrayOfmobileUserId,@RequestParam String  createMobileUserId  , @RequestParam String msgContent) {
		TokenUtil.checkTokenWithException(token);
		mobilePushMsgService.pushNoticeToMobileUserIds(arrayOfmobileUserId, createMobileUserId, msgContent);
	}
	
	/**
	 * 向  User 推送公告消息
	 * @param token
	 * @param stringOfUserIds
	 * @param createUserId
	 * @param msgContent
	 */
	@RequestMapping(value="/pushNoticeToUserAndStudentIds")
	@ResponseBody
	public void pushNoticeToUserAndStudentIds(@RequestParam String token, @RequestParam String stringOfUserIds, @RequestParam String stringOfStuIds ,  @RequestParam String  createUserId  , @RequestParam String msgContent) {
		TokenUtil.checkTokenWithException(token);
//		List<String> arrayOfUserId =  new ArrayList<String> ();
//		Collections.addAll(arrayOfUserId, stringOfUserIds.split(","));
//		mobilePushMsgService.pushNoticeToUserIds(arrayOfUserId, createUserId, msgContent);

        TokenUtil.checkTokenWithException(token);
        List<String> arrayOfUserId =  new ArrayList<String> ();
        Collections.addAll(arrayOfUserId, stringOfUserIds.split(","));

        List<String> arrayOfStuId =  new ArrayList<String> ();
        Collections.addAll(arrayOfStuId, stringOfStuIds.split(","));

        mobilePushMsgService.pushNoticeToUserAndStudentIds(arrayOfUserId, arrayOfStuId, createUserId, msgContent);

        // 开启了 单例模式， 用于循环读取用户的信息并且发送出去
        MessagePushingUtil.startRun();
	}
	
	
	/**
	 * 向 User 推送通知消息
	 * @param token
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/pushRemindToMobileUserId")
	@ResponseBody
	public void pushRemindToMobileUserId(@RequestParam String token, @RequestParam String mobileUserId , @RequestParam String msgContent) {
		TokenUtil.checkTokenWithException(token);
		mobilePushMsgService.pushRemindToMobileUserId(mobileUserId, msgContent);
	}
	
	/**
	 * 向  MobileUser 推送提醒消息
	 * @param token
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/pushRemindToMobileUserIds")
	@ResponseBody
	public void pushRemindToMobileUserIds(@RequestParam String token, @RequestParam List <String> arrayOfmobileUserId,@RequestParam String  createMobileUserId  , @RequestParam String msgContent) {
		TokenUtil.checkTokenWithException(token);
		mobilePushMsgService.pushRemindToMobileUserIds(arrayOfmobileUserId, createMobileUserId, msgContent);
	}
	

	/**
	 * 向  User 推送提醒消息 
	 * @param token
	 * @param stringOfUserIds
	 * @param stringOfStuIds
	 * @param createUserId
	 * @param msgContent
	 */
	@RequestMapping(value="/pushRemindToUserAndStudentIds")
	@ResponseBody
	public void pushRemindToUserAndStudentIds(@RequestParam String token, @RequestParam String stringOfUserIds, @RequestParam String stringOfStuIds , @RequestParam String  createUserId  , @RequestParam String msgContent) {
		TokenUtil.checkTokenWithException(token);
		List<String> arrayOfUserId =  new ArrayList<String> (); 
		Collections.addAll(arrayOfUserId, stringOfUserIds.split(","));
		
		List<String> arrayOfStuId =  new ArrayList<String> (); 
		Collections.addAll(arrayOfStuId, stringOfStuIds.split(","));
		
		mobilePushMsgService.pushRemindToUserAndStudentIds(arrayOfUserId, arrayOfStuId, createUserId, msgContent);
		// 开启了 单例模式， 用于循环读取用户的信息并且发送出去
		MessagePushingUtil.startRun();
		
	}
	
	
	/**
	 * 根据 不同的  sessionId 获取 提醒 消息
	 * @param token
	 * @param sessionId
	 * @return
	 */
	@RequestMapping(value="/getMsgRecordsBySessionId")
	@ResponseBody
	public List<MobilePushMsgRecordVo> getMsgRecordsBySessionId(@RequestParam String token, @RequestParam String sessionId,@ModelAttribute DataPackage dp) {
        TokenUtil.checkTokenWithException(token);
        return mobilePushMsgService.getMsgRecordsBySessionId(sessionId,dp.getPageNo(),dp.getPageSize());
    }
	
	/**
	 * 获取到学生的联系人信息
	 * @param token
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/getContactsForStudent")
	@ResponseBody
	public List<UserForMobileVo> getContactsForStudent(@RequestParam String token, @RequestParam String studentId) {
		TokenUtil.checkTokenWithException(token);
		List<UserForMobileVo> usersList = userService.getMobileContactsForStudent(studentId);
		
		List<UserForMobileVo> returnUserForMobileVoList=new ArrayList<UserForMobileVo>();		 
		Map<String, String> map = new HashMap<String, String>();
	    for(UserForMobileVo userForMobileVo:usersList){
	    	if (map.get(userForMobileVo.getUserId())==null){
	    		map.put(userForMobileVo.getUserId(), userForMobileVo.getUserId());
	    		returnUserForMobileVoList.add(userForMobileVo);  
	    	} 
	    }
		return returnUserForMobileVoList;
	}
	
	/**
	 * 通过不同的 courseId 得到 相应的session ID
	 * @param token
	 * @param courseId
	 * @param courseType
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/getSessionByCourseId")
	@ResponseBody
	public MobilePushMsgSessionVo getSessionByCourseId(@RequestParam String token, @RequestParam String courseId, @RequestParam String courseType, @RequestParam String mobileUserId) {
		TokenUtil.checkTokenWithException(token);
		//List<UserForMobileVo> users = userService.getMobileContactsForStudent(studentId);
		
		MobilePushMsgSessionVo sessionVo = mobilePushMsgService.getSessionByCourseId(courseId, courseType, mobileUserId); 
		return sessionVo;
	}
	
	/**
	 * 向 session 生成 text Message 数据
	 * @param token
	 * @param mobileUserId
	 * @param sessionId
	 * @param msgContent
	 */
	@RequestMapping(value="/sendTextMsgFromUserIdForSessionId")
	@ResponseBody
	public void sendTextMsgFromUserIdForSessionId(@RequestParam String token, @RequestParam String mobileUserId , @RequestParam String sessionId,  @RequestParam String msgContent) {
		TokenUtil.checkTokenWithException(token);
		mobilePushMsgService.sendTextMsgFromUserIdForCourseId(mobileUserId, sessionId ,  msgContent);
	}
	
	/**
	 * 发送课程评论 生成 text Message 数据
	 * @param token
	 * @param mobileUserId
	 * @param courseId
	 * @param msgContent
	 */
	@RequestMapping(value="/sendTextMsgFromUserIdForCourseId")
	@ResponseBody
	public Response sendTextMsgFromUserIdForCourseId(@RequestParam String token, @RequestParam String mobileUserId , @RequestParam String courseId,  @RequestParam String msgContent) {
		TokenUtil.checkTokenWithException(token);		
	    String sessionId="";
        if(StringUtils.isNotBlank(courseId)){
        	MobilePushMsgSessionVo sessionVo = mobilePushMsgService.getSessionByCourseId(courseId, "COURSE", mobileUserId);
        	if(sessionVo!=null){
        		sessionId=sessionVo.getId();
        	}
        	 
		}else{
			 throw new ApplicationException("传入的课程ID不允许为空");
		}        
		mobilePushMsgService.sendTextMsgFromUserIdForCourseId(mobileUserId, sessionId ,  msgContent);
		return new Response();
	}
	
	/**
	 * 向 session 生成 data Message 数据
	 * @param token
	 * @param mobileUserId
	 * @param sessionId
	 * @param dataFile
	 * @param datatype
	 */
	@RequestMapping(value="/sendDataMsgFromUserIdForSessionId",method =  RequestMethod.POST)
	@ResponseBody
	public void sendDataMsgFromUserIdForSessionId(@RequestParam String token, @RequestParam String mobileUserId , @RequestParam String sessionId,  @RequestParam("data") MultipartFile dataFile, @RequestParam StoredDataType datatype) {
		TokenUtil.checkTokenWithException(token);
		mobilePushMsgService.sendDataMsgFromUserIdForCourseId(mobileUserId, sessionId,  dataFile, datatype);
	}
	
	/**
	 * 通过两个不同的 mobile user id  得到 相应的session ID
	 * @param token
	 * @param courseId
	 * @param courseType
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/getSessionByMobileUserIds")
	@ResponseBody
	public MobilePushMsgSessionVo getSessionByMobileUserIds(@RequestParam String token, @RequestParam String selectedMobileUserId, 
			@RequestParam String fromMobileUserId) {
		TokenUtil.checkTokenWithException(token);
		MobilePushMsgSessionVo sessionVo = mobilePushMsgService.getSessionByMobileUserIds(selectedMobileUserId, fromMobileUserId); 
		return sessionVo;
	}
	
	/**
	 * 获取到老师的联系人信息
	 * @param token
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/getContactsForStaff")
	@ResponseBody
	public List<MobileUserVo> getContactsForStaff(@RequestParam String token, @RequestParam String staffId) {
		TokenUtil.checkTokenWithException(token);
		List<MobileUserVo> stuMobileUserVos = studentService.getMobileContactsForStaff(staffId);		
	 
		List<MobileUserVo> returnMobileUserVoList=new ArrayList<MobileUserVo>();		 
		Map<String, String> map = new HashMap<String, String>();
	    for(MobileUserVo mobileUserVo:stuMobileUserVos){
	    	if (map.get(mobileUserVo.getId())==null){
	    		map.put(mobileUserVo.getId(), mobileUserVo.getId());
	    		returnMobileUserVoList.add(mobileUserVo);  
	    	} 
	    }
		return returnMobileUserVoList;
	}
	
	/**
	 * 获取一对一课消
	 * @param token
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/getCourseConsumeByStaffId")
	@ResponseBody
	public List getCourseConsumeByStaffId(@RequestParam String productType, @RequestParam String token, 
			@RequestParam String teacherId) {
		TokenUtil.checkTokenWithException(token);
		List<AccountChargeRecordsVo> recordVos = chargeService.findChargeRecordsForTeacher(productType, teacherId);
		List<Map> returnRecords = new ArrayList<Map>();
		Map<String, List<AccountChargeRecordsVo>> returnRecordsMap = new HashMap<String, List<AccountChargeRecordsVo>>();
//		
		for(AccountChargeRecordsVo recordVo : recordVos) {
			// 判断是否存在
			if(returnRecordsMap.containsKey(recordVo.getWeekStr())) {
				// 如果存在 就添加进入
				returnRecordsMap.get(recordVo.getWeekStr()).add(recordVo);
			} else {
				// 如果不存在 就添加
				List<AccountChargeRecordsVo> recordList = new ArrayList<AccountChargeRecordsVo>();
				recordList.add(recordVo);
				returnRecordsMap.put(recordVo.getWeekStr(), recordList);
				HashMap dataMap = new HashMap();
				dataMap.put("weekstr", recordVo.getWeekStr());
				dataMap.put("recordList", recordList);
				returnRecords.add(dataMap);
			}
		}
		return returnRecords;
	}
	
	
	/**
	 * 获取一对一课消
	 * @param token
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/getCourseConsumeByStaffIdAndMonth")
	@ResponseBody
	public List getCourseConsumeByStaffIdAndMonth(@RequestParam String productType, @RequestParam String token, @RequestParam String teacherId,String searchDate) {
		TokenUtil.checkTokenWithException(token);
		List<AccountChargeRecordsVo> recordVos = chargeService.findChargeRecordsForTeacher(productType, teacherId,searchDate);				
		
		//List<List<Map>> returnRecords = new ArrayList<List<Map>>();
		List<Map> returnRecords = new ArrayList<Map>();
		Map<String, List<AccountChargeRecordsVo>> returnRecordsMap = new HashMap<String, List<AccountChargeRecordsVo>>();
		Map<String, List<AccountChargeRecordsVo>> monthRecordsMap = new HashMap<String, List<AccountChargeRecordsVo>>();		
		
		List<String> monthKeyList = new ArrayList<String>();
		for(AccountChargeRecordsVo recordVo : recordVos) {
			// 判断是否存在
			if(monthRecordsMap.containsKey(recordVo.getMonthStr())) {
				// 如果存在 就添加进入
				monthRecordsMap.get(recordVo.getMonthStr()).add(recordVo);
			} else {
				// 如果不存在 就添加
				monthKeyList.add(recordVo.getMonthStr());
				List<AccountChargeRecordsVo> recordList = new ArrayList<AccountChargeRecordsVo>();
				recordList.add(recordVo);
				monthRecordsMap.put(recordVo.getMonthStr(), recordList);			
			}
		}
			
	 
		 
		List<AccountChargeRecordsVo> recordsList=null;
		//List<Map> weekRecords = null;
		//HashMap monthMap =null;
        for (String key:monthKeyList) {
        	//key = (String) it.next();
        	recordsList=monthRecordsMap.get(key);
        	//weekRecords = new ArrayList<Map>();
//        	monthMap = new HashMap();
//        	monthMap.put("monthstr", key);
//        	weekRecords.add(monthMap);
        	for(AccountChargeRecordsVo recordVo :recordsList){
        		
        		 //判断是否存在
    			if(returnRecordsMap.containsKey(recordVo.getWeekStr())) {
    				// 如果存在 就添加进入
    				returnRecordsMap.get(recordVo.getWeekStr()).add(recordVo);
    			} else {
    				// 如果不存在 就添加
    				List<AccountChargeRecordsVo> recordList = new ArrayList<AccountChargeRecordsVo>();
    				recordList.add(recordVo);
    				returnRecordsMap.put(recordVo.getWeekStr(), recordList);
    				HashMap dataMap = new HashMap();
    				dataMap.put("weekstr", recordVo.getWeekStr());
    				dataMap.put("recordList", recordList);
    				//weekRecords.add(dataMap);
    				returnRecords.add(dataMap);
    			}        		
        	}       	
        	//returnRecords.add(weekRecords);
        	
        }	
		
		return returnRecords; 
	}
	
	/**
     * 班组课消列表
     * @param token
     * @param dp
     * @param miniClassCourseVo
     * @return
     */
	@RequestMapping(value ="/getMiniClassConsumeList")
	@ResponseBody
	public List getMiniClassConsumeList(@RequestParam String token, @ModelAttribute DataPackage dp, @ModelAttribute MiniClassCourseVo miniClassCourseVo) {
		TokenUtil.checkTokenWithException(token);
		dp.setPageSize(1000);
		dp.setSidx("courseDate");
		dp.setSord("desc");
		miniClassCourseVo.setCourseStatus(CourseStatus.CHARGED.getValue());
		dp = smallClassService.getMiniClassCourseConsumeList(miniClassCourseVo, dp);
		
		List<MiniClassCourseConsumeVo> recordVos =(List<MiniClassCourseConsumeVo>) dp.getDatas();
		
		
		List<Map> returnRecords = new ArrayList<Map>();
		//List<List<Map>> returnRecords = new ArrayList<List<Map>>();
		Map<String, List<MiniClassCourseConsumeVo>> returnRecordsMap = new HashMap<String, List<MiniClassCourseConsumeVo>>();
		Map<String, List<MiniClassCourseConsumeVo>> monthRecordsMap = new HashMap<String, List<MiniClassCourseConsumeVo>>();		
		
		List<String> monthKeyList = new ArrayList<String>();
		for(MiniClassCourseConsumeVo recordVo : recordVos) {
			// 判断是否存在
			if(monthRecordsMap.containsKey(recordVo.getMonthStr())) {
				// 如果存在 就添加进入
				monthRecordsMap.get(recordVo.getMonthStr()).add(recordVo);
			} else {
				// 如果不存在 就添加
				monthKeyList.add(recordVo.getMonthStr());
				List<MiniClassCourseConsumeVo> recordList = new ArrayList<MiniClassCourseConsumeVo>();
				recordList.add(recordVo);
				monthRecordsMap.put(recordVo.getMonthStr(), recordList);			
			}
		}
			
	 
		 
		List<MiniClassCourseConsumeVo> recordsList=null;
		//List<Map> weekRecords = null;
		//HashMap monthMap =null;
        for (String key:monthKeyList) {
        	//key = (String) it.next();
        	recordsList=monthRecordsMap.get(key);
        	//weekRecords = new ArrayList<Map>();
//        	monthMap = new HashMap();
//        	monthMap.put("monthstr", key);
//        	weekRecords.add(monthMap);
        	for(MiniClassCourseConsumeVo recordVo :recordsList){
        		
        		 //判断是否存在
    			if(returnRecordsMap.containsKey(recordVo.getWeekStr())) {
    				// 如果存在 就添加进入
    				returnRecordsMap.get(recordVo.getWeekStr()).add(recordVo);
    			} else {
    				// 如果不存在 就添加
    				List<MiniClassCourseConsumeVo> recordList = new ArrayList<MiniClassCourseConsumeVo>();
    				recordList.add(recordVo);
    				returnRecordsMap.put(recordVo.getWeekStr(), recordList);
    				HashMap dataMap = new HashMap();
    				dataMap.put("weekstr", recordVo.getWeekStr());
    				dataMap.put("recordList", recordList);
    				returnRecords.add(dataMap);
    				//weekRecords.add(dataMap);
    			}        		
        	}       	
        	//returnRecords.add(weekRecords);
        	
        }	
		
		return returnRecords; 
	}
	
	
	@RequestMapping(value = "/sendSms")
	@ResponseBody
	public Response sendSms(@ModelAttribute SmsRecord message) {
		smsService.sendSms(message);
		return new Response();
	}
	
	/**
	 * 跟新用户的 channel 信息
	 * @param mobileUserVo
	 * @return
	 */
	@RequestMapping(value = "/updateChannelInfoForMobileUser")
	@ResponseBody
	public Response updateChannelInfoForMobileUser(@RequestParam String token, @ModelAttribute MobileUserVo mobileUserVo) {
		TokenUtil.checkTokenWithException(token);
		mobileUserService.updateChannelInfo(mobileUserVo); 
		return new Response();
	}
	
	
	
	/**
	 * 根据Id更改用户信息的记录状态
	 * @param token
	 * @param mobileUserId
	 * @param sessionId
	 * @param recordId
	 * @param sessionType
	 * @return
	 */
	@RequestMapping(value = "/updateUserRecordStatus")
	@ResponseBody
	public Response updateUserRecordStatus(@RequestParam String token, @RequestParam String mobileUserId, @RequestParam String sessionId, @RequestParam String recordId,String sessionType) {
		TokenUtil.checkTokenWithException(token);
		mobilePushMsgUserRecordService.updateUserRecordStatus(mobileUserId, sessionId, recordId,sessionType);
		return new Response();
	}
	
	/**
	 * 根据Id得到未读消息条数和第一条的内容与时间
	 * @param token
	 * @param mobileUserId
	 * @param sessionId
	 * @param sessionType
	 * @return String [count,firstRecordContent,firstRecordTime]  没有就返回null
	 */
	@RequestMapping(value = "/getUserRecordCount")
	@ResponseBody
	public String [] getUserRecordCount(@RequestParam String token, @RequestParam String mobileUserId, @RequestParam String sessionId,  String sessionType) {
		TokenUtil.checkTokenWithException(token);
		return mobilePushMsgUserRecordService.getUserRecordCount(mobileUserId, sessionId,sessionType);
	}
	
	/**
	 * 根据Id得到未读消息条数和第一条的内容与时间, 包括 提醒， 通告， 和 不同的session
	 * @param token
	 * @param mobileUserId
     * @param lastFetchTime
	 * @return String [count,firstRecordContent,firstRecordTime]  没有就返回null
     */
	@RequestMapping(value = "/getTotalCountOfUserUnReadRecord")
	@ResponseBody
	public List<Map> getTotalCountOfUserUnReadRecord(@RequestParam String token, @RequestParam String mobileUserId, @RequestParam String lastFetchTime ) {
		TokenUtil.checkTokenWithException(token);
		return mobilePushMsgService.getTotalCountOfUserUnReadRecord(mobileUserId, lastFetchTime);
	}
	

    /**
     * 判断是否有最新的消息
     * @param token
     * @param mobileUserId
     * @param lastFetchTime
     * @return
     */
	@RequestMapping(value = "/hasMoreMessageToMobileUserId")
	@ResponseBody
	public Map<String, String> hasMoreMessageToMobileUserId(@RequestParam String token, @RequestParam String mobileUserId, @RequestParam String lastFetchTime ) {
		TokenUtil.checkTokenWithException(token);
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("hasMoreMessage", String.valueOf(mobilePushMsgService.hasMoreMessageToMobileUserId(mobileUserId, lastFetchTime)));
		returnMap.put("lastFetchTime", DateTools.getCurrentDateTime());
		return returnMap;
	}
	
	/**
	 * 根据recordId找到记录
	 * @param token
	 * @param recordId
	 * @return
	 */
	@RequestMapping(value = "/getMobilePushMsgRecordById")
	@ResponseBody
	public MobilePushMsgRecordVo getMobilePushMsgRecordById(@RequestParam String token, @RequestParam String recordId) {
		TokenUtil.checkTokenWithException(token);
		return mobilePushMsgService.getMobilePushMsgRecordById(recordId);
	}
	
	/**
	 * 根据 recordId找到  notice 记录
	 * @param token
	 * @param recordId
	 * @return
	 */
	@RequestMapping(value = "/getNoticeByRecordId")
	@ResponseBody
	public SystemNoticeVo getNoticeByRecordId(@RequestParam String token, @RequestParam String recordId) {
		TokenUtil.checkTokenWithException(token);
		return mobilePushMsgService.getNoticeByRecordId(recordId);
	}
	

    /**
     *  * 通过手机用户Id得到客户List
     * @param token
     * @param pageNo
     * @param pageSize
     * @return
     */
	@RequestMapping(value = "/getCustomerList")
	@ResponseBody
	public List<CustomerVo> getCustomerList(@RequestParam String token, int pageNo, int pageSize){
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage = new DataPackage(pageNo, pageSize);
		dataPackage.setSidx("c.createTime");
		dataPackage.setSord("desc");
		dataPackage = customerService.getCustomers(new CustomerVo(), dataPackage, false);
		return (List<CustomerVo>) dataPackage.getDatas();
	}
	
	 /**
     *  * 通过关键字得到客户List
     * @param token
     * @param keywork
     * @param pageNo
     * @param pageSize
     * @return
     */
	@RequestMapping(value = "/getCustomerListByKeywork")
	@ResponseBody
	public List<CustomerVo> getCustomerListByKeywork(@RequestParam String token,String keywork, int pageNo, int pageSize){
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage = new DataPackage(pageNo, pageSize);	
		dataPackage.setSidx("c.createTime");
		dataPackage.setSord("desc");
		CustomerVo customerVo=new CustomerVo();
		customerVo.setKeywork(keywork);		
		dataPackage = customerService.getCustomers(customerVo, dataPackage, false);
		return (List<CustomerVo>) dataPackage.getDatas();
	}
	
	
	/**
	 * 根据客户Id找到客户
	 * @param token
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/getCustomerById")
	@ResponseBody
	public CustomerVo getCustomerById(@RequestParam String token,String customerId){
		TokenUtil.checkTokenWithException(token);
		return customerService.findCustomerById(customerId);
	}
	
	
	
	/**
	 *修改客户
	 * @param token
	 * @param cus
	 * @throws Exception
	 */
	@RequestMapping(value = "/editCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response  editCustomer(@RequestParam String token, @ModelAttribute CustomerVo cusVo) throws Exception {
		TokenUtil.checkTokenWithException(token);
		customerService.updateCustomerForSimple(cusVo);
		return new Response();
	}
	
	/**
	 * 新增或修改客户
	 * @param token
	 * @param cus
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveNewCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response  saveNewCustomer(@RequestParam String token, @ModelAttribute CustomerVo cus) throws Exception {
		TokenUtil.checkTokenWithException(token);
		
		//新增客户		
	   if(StringUtil.isBlank(cus.getId())){
			//获取当前用户 所拥有的rolecode
			List<Role> currentUserRoles = userService.getCurrentLoginUser().getRole();
			List<RoleCode> currentUserRolesRoleCode = new ArrayList<RoleCode>(); //本人所拥有的rolecode
			for (Role role : currentUserRoles) {
				currentUserRolesRoleCode.add(role.getRoleCode());
			}
			//判断是否为咨询师				 
			if(currentUserRolesRoleCode.contains(RoleCode.CONSULTOR)){
				cus.setDealStatus(CustomerDealStatus.FOLLOWING);//跟进中
				cus.setDeliverTarget(userService.getCurrentLoginUser().getUserId());//跟进人
				cus.setDeliverType(CustomerDeliverType.PERSONAL_POOL);//个人				
			}else{
				cus.setDealStatus(CustomerDealStatus.NEW);//新增
			}
		}
			
		 customerService.saveOrUpdateCustomer(cus);
		 return new Response();
	}
	
	/**
	 * 根据客户Id得到跟进记录列表
	 * @param token
	 * @param customerId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFollowUpRecrodsList")
	@ResponseBody
	public List<CustomerFolowup> getFollowUpRecrodsList(@RequestParam String token, @RequestParam String customerId, int pageNo, int pageSize) throws Exception {
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage =  new DataPackage(pageNo, pageSize);		
		Customer cus=new Customer();
		cus.setId(customerId);
		dataPackage = customerService.gtCustomerFollowingRecords(cus, dataPackage); 
		return (List<CustomerFolowup>) dataPackage.getDatas() ;
	}
	
	/**
	 * 保存客户跟进记录
	 * @param token
	 * @param follow
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveFollowupRecord")
	@ResponseBody
	public Response saveFollowupRecord(@RequestParam String token,@ModelAttribute CustomerFolowupVo follow) throws Exception {
		TokenUtil.checkTokenWithException(token);
		if (StringUtils.isNotBlank(follow.getRemark())) {
			//follow.setRemark(URLDecoder.decode(follow.getRemark(), "utf-8"));
			log.info("app-saveFollowupRecord:"+follow.getRemark());
			//String remark = EmojiConvertUtil.emojiConvert(follow.getRemark());
			String remark = EmojiConvertUtil.removeEmoji(follow.getRemark());
			if(remark==null||remark.length()>1024) {
				return new Response(400,"跟进内容过长");
			}
			follow.setRemark(remark);
		}
		customerService.saveCustomerFollowRecord(follow); 
		return new Response();
	}
	
	
	
	/**
	 * 得到学生列表
	 * @param token
	 * @param mobileUserId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/getStudentList")
	@ResponseBody
	public List<StudentVo> getStudentList(@RequestParam String token, @RequestParam String mobileUserId, int pageNo, int pageSize,
			String rcourseHour ,String rcourseHourEnd,String brenchId,String stuType,String stuNameGrade) throws Exception {
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage =  new DataPackage(pageNo, pageSize);	
		dataPackage.setSidx("create_Time");dataPackage.setSord("desc");
		dataPackage = studentService.getStudentList(new Student(),true, dataPackage, new ModelVo(),rcourseHour,rcourseHourEnd,brenchId,stuType,stuNameGrade);
		return (List<StudentVo>) dataPackage.getDatas() ;
	}
        

	/**
	 * 得到学生详情
	 * @param token
	 * @param id
	 */
    @ResponseBody
	@RequestMapping(value = "/getStudentById")
	public StudentVo getStudentById(@RequestParam String token,@RequestParam String id) {
		TokenUtil.checkTokenWithException(token);
		return studentService.findStudentById(id);
	}
        
    /**
     * 学生回访跟进记录
     * @param token
     * @param studentId
     * @param pageNo
     * @param pageSize
     * @return
     */
     @RequestMapping(value = "/getStudentFollowUp")
    @ResponseBody
    public List<StudentFollowUp> getStudentFollowUp(@RequestParam String token,String studentId, int pageNo, int pageSize){
    	TokenUtil.checkTokenWithException(token);
    	DataPackage dataPackage =  new DataPackage(pageNo, pageSize);
    	StudentFollowUpVo studentFollowUpVo= new StudentFollowUpVo();
    	studentFollowUpVo.setStudentId(studentId);
    	dataPackage = studentService.getStudentFollowUp(studentFollowUpVo, dataPackage);
    	return(List<StudentFollowUp>) dataPackage.getDatas() ;
    }
    

    /**
     * 保存学生回访跟进记录
     * @param token
     * @param studentFollowUp
     * @param documentfile
     * @return
     */
     @RequestMapping(value = "/saveStudentFollowUp")
    @ResponseBody
    public Response savaStudentFollowUp(@RequestParam String token,StudentFollowUp studentFollowUp,@RequestParam(value="documentfile",required=false) MultipartFile documentfile){
    	TokenUtil.checkTokenWithException(token);
    	studentService.saveStudentFollowUp(studentFollowUp,documentfile);
    	return new Response();
    }
     
     /**
      * 保存学生回访跟进记录 ， 没有附件发送
      * @param token
      * @param studentFollowUp
      * @return
      */
     @RequestMapping(value = "/saveStudentFollowUpSimple")
     @ResponseBody
     public Response savaStudentFollowUp(@RequestParam String token,StudentFollowUp studentFollowUp ){
     	TokenUtil.checkTokenWithException(token);
     	studentService.saveStudentFollowUp(studentFollowUp,null);
     	return new Response();
     }

    /**
     * 得到一对一课程list
     * @param token
     * @param status
     * @param currentRolId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getOneOnOneCourseList")
	@ResponseBody
	public List<CourseVo> getOneOnOneCourseList(@RequestParam String token, @RequestParam String status,String currentRolId,String courseDate, int pageNo, int pageSize) throws Exception {
    	TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage =  new DataPackage(pageNo, pageSize);	
		CourseVo svo=new CourseVo();
		if(StringUtils.isNotEmpty(status))
		svo.setCourseStatus(CourseStatus.valueOf(status));
		if(StringUtils.isNotEmpty(courseDate))
			svo.setCourseDate(courseDate);
		svo.setCurrentRoleId(currentRolId);
		dataPackage = courseService.getOneOnOneBatchAttendanceList(svo, dataPackage);
		return (List<CourseVo>) dataPackage.getDatas() ;
	}
    

    /**
     * 批量重置考勤状态
     * @param token
     * @param courseIds
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/resetCourseAttendances")
    @ResponseBody
    public Response resetCourseAttendances(@RequestParam String token,@RequestParam String courseIds) throws Exception{
    	TokenUtil.checkTokenWithException(token);
    	Response response=new Response();
        courseService.deleteCourseAttendances(courseIds);
        return response;
    }
	
    /**
	 * 批量考勤提交
	 * @return
	 */
    /**
     *
     * @param token
     * @param status
     * @param courseIds
     * @return
     * @throws Exception
     */
	@RequestMapping("/mutilAttendaceSubmit")
	@ResponseBody
	public Response mutilAttendaceSubmit(@RequestParam String token,@RequestParam String status,@RequestParam String courseIds) throws Exception{
		TokenUtil.checkTokenWithException(token);
		 if(StringUtils.isNotBlank(courseIds)){	            
			  OneOnOneBatchAttendanceEditVo oneOnOneBatchAttendanceEditVo=new OneOnOneBatchAttendanceEditVo();	            
			  oneOnOneBatchAttendanceEditVo.setIds(courseIds);
           	  oneOnOneBatchAttendanceEditVo.setOperteType(status);
	          courseService.oneOnOneBatchAttendanceEdit(oneOnOneBatchAttendanceEditVo);
	        }else{
	            throw new ApplicationException("传入的课程ID不允许为空");
	        }
		
		return new Response();
	}
	
	/**
	 * 班组课程列表
	 * @param token
	 * @param currentRoleId  
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
//	@RequestMapping(value ="/getMiniClassCourseList")
//	@ResponseBody
//	public List<MiniClassCourseVo> getMiniClassCourseList(@RequestParam String token,@RequestParam String currentRoleId, int pageNo, int pageSize) {
//		TokenUtil.checkTokenWithException(token);
//		DataPackage dataPackage =  new DataPackage(pageNo, pageSize);
//		MiniClassCourseVo vo=new MiniClassCourseVo();
//		vo.setCurrentRoleId(currentRoleId);
//		dataPackage = smallClassService.getMiniClassCourseList(vo, dataPackage);
//		return (List<MiniClassCourseVo>)dataPackage.getDatas();
//	}
	
	 /**
     * 学生评价记录
     * */
    @RequestMapping(value = "/getStudentComment")
    @ResponseBody
    public List<StudentCommentVo> getStudentComment(@RequestParam String token,@RequestParam String studentId, int pageNo, int pageSize){
    	TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage =  new DataPackage(pageNo, pageSize);
    	dataPackage = studentService.getStudentComment(studentId, dataPackage);
    	return (List<StudentCommentVo>) dataPackage.getDatas();
    }
    
    
    /**
     * 保存学生评价
     * */
    @RequestMapping(value = "/saveStudentComment")
    @ResponseBody
    public Response savaStudentComment(StudentComment studentComment){
    	studentService.saveStudentComment(studentComment);
    	return new Response();
    }
    
    
    
    /**
     * 根据校区级别来得到级别以下校区的收入列表
     * */
    @RequestMapping(value = "/getCampusTotalIncome")
    @ResponseBody
    public  CampusIncomeVo  getCampusTotalIncome(@RequestParam String token,@RequestParam String orgLel,@RequestParam String date){
    	TokenUtil.checkTokenWithException(token);
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("orgLel", orgLel);
    	params.put("date", date);
    	return realTimeReportService.getCampusIncome(params);
    }
    
    /**
     * 根据校区Id来得到校区的收入列表
     * */
    @RequestMapping(value = "/getOneCampusTotalIncome")
    @ResponseBody
    public  OneCampusIncomeVo  getOneCampusTotalIncome(@RequestParam String token,@RequestParam String campusId, String startDate,String endDate){
    	if (StringUtil.isBlank(startDate)) {
    		startDate = DateTools.getCurrentDate();
    	}
    	TokenUtil.checkTokenWithException(token);
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("campusId", campusId);
    	params.put("startDate", startDate);
    	params.put("endDate", endDate);
    	return realTimeReportService.getOneCampusTotalIncome(params);
    }

   	/**
	 * 简单员工登陆账号接口
	 * @param account
	 * @param passwordMd5
	 * @return 一个 tokenId 字符串
	 */
	@RequestMapping(value ="/simpleLogin")
	@ResponseBody
	public Map<String,String> simpleLogin(String account, String passwordMd5, MobileUserType mobileUserType) {	
		
		LoginResponse loginRsp = commonService.login(account, passwordMd5, mobileUserType);	
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put("tokenId", TokenUtil.genToken(loginRsp.getUser()));
		return respMap;
	}
	
	/**
	 * 简单的 学生登录与注册
	 * @param studentId
	 * @param appPassowrd
	 * @return
	 */
	@RequestMapping(value = "/simpleStudentLogin")
	@ResponseBody
	public Map<String,String> simpleStudentLogin(String studentId, String appPassowrd) {
		AppStudentLoginResponse response = studentService.appLogin(studentId, appPassowrd);
		// 生成token时，绑定学生的班主任和学生自己
		// 后面可以要去除这个信息， 学生应该有自己的session 信息
		//response.setToken(TokenUtil.genStudentToken(studentService.getStudentStudyManager(studentId), response.getStudent()));
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put("tokenId", TokenUtil.genStudentToken(studentService.getStudentStudyManager(studentId), response.getStudent()));
		return respMap;
	}
	
	
    /**
     * 获取学生动态记录
     * @param token
     * @param studentId
     * @param pageNo
     * @param pageSize
     * @param studentEventType
     * @return
     */
    @RequestMapping(value = "/getStudentDynamicStatusByPage")
    @ResponseBody
    public List<StudentDynamicStatusVo> getStudentDynamicStatusByPage(@RequestParam String token,String studentId, int pageNo, int pageSize, StudentEventType studentEventType ){
    	TokenUtil.checkTokenWithException(token);
    	return studentDynamicStatusService.getStudentDynamicStatusByPage(studentId , pageNo, pageSize, studentEventType );
    }

    
    /**
     * 判断这个老师 某个日期是不是有一对一课程
     * @param token
     * @param teacherId
     * @param date
     * @return
     */
    @RequestMapping(value = "/checkHasOneOnOneCourseForDate")
    @ResponseBody
    public Map checkHasOneOnOneCourseForDate(@RequestParam String token,@RequestParam String teacherId, @RequestParam String date){
    	Map<String, String> returnMap =  new HashMap<String, String> ();
    	TokenUtil.checkTokenWithException(token);
    	boolean hasCourse = courseService.checkHasOneOnOneCourseForDate(teacherId, date, new ArrayList<CourseStatus>());
    	returnMap.put("hasCourse", String.valueOf(hasCourse));
    	return returnMap;
    }
    
    /**
     * 判断这个老师 某个日期是不是有小班课程
     * @param token
     * @param teacherId
     * @param date
     * @return
     */
    @RequestMapping(value = "/checkHasMiniClassForDate")
    @ResponseBody
    public Map checkHasMiniClassForDate(@RequestParam String token,@RequestParam String teacherId, @RequestParam String date){
    	Map<String, String> returnMap =  new HashMap<String, String> ();
    	TokenUtil.checkTokenWithException(token);
    	boolean hasCourse = smallClassService.checkHasMiniClassForDate(teacherId, date, new ArrayList<CourseStatus>());
    	returnMap.put("hasCourse", String.valueOf(hasCourse));
    	return returnMap;
    }
    
    @RequestMapping(value = "/checkHasOneOnOneCourseForPeriodDate")
    @ResponseBody
    public List<HasOneOnOneCourseVo> checkHasOneOnOneCourseForPeriodDate(@RequestParam String token,@RequestParam String teacherId, String startDate, String endDate){
    	if (StringUtil.isBlank(startDate)) {
    		startDate = DateTools.getCurrentDate();
    	}
    	if (StringUtil.isBlank(endDate)) {
    		endDate = DateTools.getCurrentDate();
    	}
    	TokenUtil.checkTokenWithException(token);
    	List<HasOneOnOneCourseVo> listHasOneOnOneCourse = courseService.checkHasOneOnOneCourseForPeriodDate(teacherId, startDate, endDate);
    	return listHasOneOnOneCourse;
    }
    
    @RequestMapping(value = "/checkHasOneOnOneCourseForPeriodDateByStatu")
    @ResponseBody
    public List<HasOneOnOneCourseVo> checkHasOneOnOneCourseForPeriodDateByStatu(@RequestParam String token, String startDate, String endDate, @RequestParam String status){
    	if (StringUtil.isBlank(startDate)) {
    		startDate = DateTools.getCurrentDate();
    	}
    	if (StringUtil.isBlank(endDate)) {
    		endDate = DateTools.getCurrentDate();
    	}
    	TokenUtil.checkTokenWithException(token);
    	List<HasOneOnOneCourseVo> listHasOneOnOneCourse = courseService.checkHasOneOnOneCourseForPeriodDateByStatu(status, startDate, endDate);
    	return listHasOneOnOneCourse;
    }
    
    
    @RequestMapping(value = "/checkHasMiniClassForPeriodDate")
    @ResponseBody
    public List<HasClassCourseVo> checkHasMiniClassForPeriodDate(@RequestParam String token,@RequestParam String teacherId, String startDate, String endDate){
    	if (StringUtil.isBlank(startDate)) {
    		startDate = DateTools.getCurrentDate();
    	}
    	if (StringUtil.isBlank(endDate)) {
    		endDate = DateTools.getCurrentDate();
    	}
    	TokenUtil.checkTokenWithException(token);
    	List<HasClassCourseVo> listHasMiniClass = smallClassService.checkHasMiniClassForPeriodDate(teacherId, startDate, endDate);
    	return listHasMiniClass;
    }



    
    /**
     * 营收统计接口
     * @param token
     * @param startDate
     * @param endDate
     * @param basicOperationQueryLevelType
     * @param blCampusId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getIncomingAnalyze")
    @ResponseBody
    public DataPackageForJqGrid getIncomingAnalyze(@RequestParam String token, @ModelAttribute GridRequest gridRequest,
            @ModelAttribute BasicOperationQueryVo basicOperationQueryVo){
    	TokenUtil.checkTokenWithException(token);
    	DataPackage dp = new DataPackage(gridRequest);
    	dp= reportService.getIncomingAnalyzeForMobile(basicOperationQueryVo, dp);
    	
    	return new DataPackageForJqGrid(dp);
    }
    
    @RequestMapping(value = "/runMsgPush")
	@ResponseBody
	public void runMsgPush() {
    	 // 开启了 单例模式， 用于循环读取用户的信息并且发送出去
        MessagePushingUtil.startRun();
	}
	
    @RequestMapping(value = "/stopMsgPush")
	@ResponseBody
	public void stopMsgPush() {
    	 // 开启了 单例模式， 用于循环读取用户的信息并且发送出去
        MessagePushingUtil.stopRun();
	}
    
    /**
	 * 手机端现金流统计接口
	 * @param gridRequest
	 * @param cus
	 * @param onlyShowUndelive
	 * @return
	 */
	@RequestMapping(value = "/getFinanceAnalyze", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getFinanceAnalyze(@RequestParam String token,@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo) {
		log.info("getFinanceAnalyze() start.");
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage=reportService.getFinanceAnalyzeForMobile(basicOperationQueryVo, dataPackage);
		log.info("getFinanceAnalyze() end.");
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 手机端现金流统计接口
	 * @return
	 */
	@RequestMapping(value = "/getFinanceAnalyzeForMobileLine", method =  RequestMethod.GET)
	@ResponseBody
	public Map getFinanceAnalyzeForMobileLine(@RequestParam String token,@ModelAttribute BasicOperationQueryVo basicOperationQueryVo,@RequestParam String type) {
		TokenUtil.checkTokenWithException(token);
		return reportService.getFinanceAnalyzeForMobileLine(basicOperationQueryVo,type);
	}
	
	
	
	 /**
		 * 手机端html5客户来源统计
		 * @param gridRequest
		 * @param cus
		 * @param onlyShowUndelive
		 * @return
		 */
		@RequestMapping(value = "/getCustomerTotalByCusType", method =  RequestMethod.GET)
		@ResponseBody
		public DataPackageForJqGrid getCustomerTotalByCusType(@RequestParam String token,@ModelAttribute GridRequest gridRequest,
				@ModelAttribute BasicOperationQueryVo basicOperationQueryVo) {
			log.info("getFinanceAnalyze() start.");
			TokenUtil.checkTokenWithException(token);
			DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
			dataPackage=reportService.getCustomerTotalByCusType(basicOperationQueryVo, dataPackage);
			log.info("getFinanceAnalyze() end.");
			return new DataPackageForJqGrid(dataPackage);
		}
	
		/**
		 * 手机端html5总览
		 * @param gridRequest
		 * @param cus
		 * @param onlyShowUndelive
		 * @return
		 */
		@RequestMapping(value = "/getAllTotalForMobile", method =  RequestMethod.GET)
		@ResponseBody
		public DataPackageForJqGrid getAllTotalForMobile(@RequestParam String token,@ModelAttribute GridRequest gridRequest,
				@ModelAttribute BasicOperationQueryVo basicOperationQueryVo) {
			log.info("getAllTotalForMobile() start.");
			TokenUtil.checkTokenWithException(token);
			DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
			dataPackage=reportService.getAllTotalForMobile(basicOperationQueryVo, dataPackage);
			log.info("getAllTotalForMobile() end.");
			return new DataPackageForJqGrid(dataPackage);
		}
	
	/**
     * 获取数据字典
     * @param token
     * @param category
     * @return
     */
     @RequestMapping(value = "/getDataDict")
    @ResponseBody
    public List<DataDict> getDataDict(@RequestParam String token,String category){
    	TokenUtil.checkTokenWithException(token);
    	DataPackage dataPackage =  new DataPackage(0, 100);
    	DataDict dataDict= new DataDict();
    	if(StringUtil.isNotBlank(category) && DataDictCategory.CUS_ORG.getValue().equals(category)){//市场来源
    		dataDict.setCategory(DataDictCategory.CUS_ORG);    		
    	}else if(StringUtil.isNotBlank(category) && DataDictCategory.RES_TYPE.getValue().equals(category)){//资源类型
    		dataDict.setCategory(DataDictCategory.RES_TYPE);    
    	}else if(StringUtil.isNotBlank(category) && DataDictCategory.STUDENT_GRADE.getValue().equals(category)){ //学生年级
    		dataDict.setCategory(DataDictCategory.STUDENT_GRADE);    
    	}else if(StringUtil.isNotBlank(category) && DataDictCategory.INTENTION_OF_THE_CUSTOMER.getValue().equals(category)){//客户意向度分级
    		dataDict.setCategory(DataDictCategory.INTENTION_OF_THE_CUSTOMER);    
    	}
//    	else if(StringUtils.isNotBlank(category) && DataDictCategory.USER_INFO_JOB.getValue().equals(category)){//用户职位
//    		dataDict.setCategory(DataDictCategory.USER_INFO_JOB);  
//    	}    	
    	dataPackage = commonService.getSelectOptions(dataDict, dataPackage);
    	return(List<DataDict>) dataPackage.getDatas() ;
    }
     
     
    /**
     * @param jobId
     * @return
     */
    @RequestMapping(value = "/getUserListByJob")
    public List<User> getUserListByJob(@RequestParam String token,@RequestParam String jobId, DataPackage dataPackage){
    	TokenUtil.checkTokenWithException(token);
    	
		List<User> vos =   (List<User>) userService.getUserListByJob(dataPackage, jobId).getDatas();
		return vos;
    }
    
    
     /**
 	 * 保存下一次跟进时间
 	 * @param appointment
 	 * @return
 	 * @throws Exception
 	 * 
 	 */
 	@RequestMapping(value = "/setCustomerNextFollowupTime")
 	@ResponseBody
 	public Response setCustomerNextFollowupTime(
 			@ModelAttribute CustomerAppointmentVo appointment, String token) throws Exception {
 		TokenUtil.checkTokenWithException(token);
 		customerService.setCustomerNextFollowupTime(appointment);
 		return new Response();
 	}
 	
 	/**
	 * 保存下一次预约时间
	 * @param appointment
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addCustomerAppointment")
	@ResponseBody
	public Response addCustomerAppointment(@ModelAttribute CustomerAppointmentVo appointment, String token) throws Exception {
		customerService.addCustomerAppointment(appointment);
		return new Response();
	}
	
	/**
	 * 分公司所有校区
	 * @param organizationId
	 * @return
	 * organizationId:ORG0000000124
	 */
	@RequestMapping(value = "/getBrenchCampusByParentOrgId")
	@ResponseBody
	public List<CampusVo> getBrenchCampusByParentOrgId(String organizationId, String token){
		TokenUtil.checkTokenWithException(token);	
		Organization brench = userService.getBelongBranch();
		if(brench!=null){
			organizationId=brench.getId();
		}
		List<List<Organization>> organizationLists= commonService.getBrenchCampusByLimit(organizationId);
		List<Organization> list = new ArrayList<Organization>();
		for(List<Organization> organizationList:organizationLists){
			list.addAll(organizationList);
		}
		//筛选出校区,不要分公司
		List<CampusVo> listCampus = new ArrayList<CampusVo>();
		for(Organization organization:list){
			if(organization.getOrgType()==OrganizationType.CAMPUS){				
				CampusVo campus=new CampusVo();
				campus.setId(organization.getId());
				campus.setName(organization.getName());
				listCampus.add(campus);
			}		
		}
		return listCampus;
	}


	/**
	 * 分公司所有校区
	 * @return
	 */
	@RequestMapping(value = "/getAllCampus")
	@ResponseBody
	public Map<String, Map> getAllCampus(){
		return commonService.getAllCampus();
	}
	
	/**********************************************************星火管理端APP接口 start**************************************************************************************************/
	
	
	
	
	/**
	 * 当前登录用户所属组织架构下的所有校区
	 * @return
	 */
	@RequestMapping(value = "/getCampusByLoginUserForMobile")
	@ResponseBody
	public Map<String, Map> getCampusByLoginUserForMobile(String token) {
		TokenUtil.checkTokenWithException(token);
		return commonService.getCampusByLoginUserForMobile();
	}
	
	/**
	 * 根据学生id查询学生帐户情况
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value="/getStudentAccoutInfo")
	@ResponseBody
	public StudnetAccMvVo getStudentAccoutInfo(String studentId) {
		return studentService.getStudentAccoutInfo(studentId);
	}
	
	/**
	 * 得到学生列表
	 * @param token
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/getOneOnOneStudentList")
	@ResponseBody
	public List<MobileStudentVo> getOneOnOneStudentList(@RequestParam String token, MobileStudentListTo to,Student stu) throws Exception {
		TokenUtil.checkTokenWithException(token);
		to.setStudent(stu);
		return  studentService.getMobileStudentList(to);
	}
    
    
    /**
	 * 返回 所有我的合同
	 * @return
	 */
	@RequestMapping(value = "/getContractListForMobile", method =  RequestMethod.GET)
	@ResponseBody
	public List<ContractMobileVo> getContractListForMobile(String token, String studentId, int pageNo, int pageSize) {
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage = new DataPackage(pageNo, pageSize);
		List<ContractMobileVo> contracts= contractService.findContractForMobile(studentId, dataPackage);
		
		return contracts;
	}
	
	
	/**
	 * 根据合同的ID获取合同信息
	 * @return
	 */
	@RequestMapping(value = "/getContractByIdForMobile", method =  RequestMethod.GET)
	@ResponseBody
	public ContractMobileVo getContractByIdForMobile(String token, String contractId) {
		TokenUtil.checkTokenWithException(token);

		ContractMobileVo contract= contractService.findContractByIdForMobile(contractId);
		
		return contract;
	}
    
    
	/**
	 * 获取当前校区下指定角色的用户
	 * @param roleCode 多个用，号隔开
	 * @return
	 */
	@RequestMapping(value = "/getUserByRoldCodesSelectionMobile", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getUserByRoldCodesSelectionMobile(String token, String roleCode,String organizationId) {
		//TokenUtil.checkTokenWithException(token);	
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
	 * 获取id及id下属 指定类型 的组织架构数据
	 * @param orgType 默认 分公司BRENCH
	 * @param orgId 默认当前登录用户所属组织架构分公司id
	 * @return
	 */
	@RequestMapping(value = "/selectOrgByIdAndTypeOptionMobile", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse selectOrgByIdAndTypeOptionMobile(String token,String orgType, String orgId) {
		TokenUtil.checkTokenWithException(token);	
		
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
	 * 所有部门列表
	 */
	@RequestMapping(value = "/activeUserAppStatus")
	@ResponseBody
	public Response activeUserAppStatus(String token){
		TokenUtil.checkTokenWithException(token);	
		userService.activeUserAppStatus();
		return new Response();
	}
	
	/**
	 * 所有部门列表
	 */
	@RequestMapping(value = "/getAllMobileDeptList")
	@ResponseBody
	public List<MobileOrganization> getAllMobileDeptList(String token){
//		TokenUtil.checkTokenWithException(token);	
		return userService.getAllMobileDeptList();
	}
	
	/**
	 * 所有部门列表
	 */
	@RequestMapping(value = "/getAllMobileDeptAndUserByParentId")
	@ResponseBody
	public Map<String,Object> getAllMobileDeptAndUserByParentId(String deptId,String deptLevel, int pageNo, int pageSize,int level){
		DataPackage dp =  new DataPackage(pageNo, pageSize);	
		return userService.getAllMobileDeptAndUserByParentId(dp,deptId,deptLevel,level);
	}
	
	/**
	 * 所有APP部门列表 Map<String, List<UserOrg>>
	 */
	@RequestMapping(value = "/getAllMobileOrganizationList")
	@ResponseBody
	public Map<String, List<MobileOrganizationVo>> getAllMobileOrganizationList(String token){
//		TokenUtil.checkTokenWithException(token);
		Map<String, List<MobileOrganizationVo>> map = new HashMap<String, List<MobileOrganizationVo>>();
		List<MobileOrganizationVo> mobileOrganizationVos= userService.getAllMobileOrganizationList();
		map.put("mobileOrganizationVos", mobileOrganizationVos);
		return map;
	}
	
	/**
	 * 所有部门列表
	 */
	@RequestMapping(value = "/getAllMobileDeptAndUserList")
	@ResponseBody
	public List<MobileOrganization> getAllMobileDeptAndUserList(String token,HttpServletRequest request){
		String servicePath = request.getSession().getServletContext().getRealPath("/");
//		TokenUtil.checkTokenWithException(token);	
		return userService.getAllMobileDeptAndUserList("1",servicePath);
	}
	
	/**
	 * 所有部门列表
	 */
	@RequestMapping(value = "/getAllMobileDeptUserMaybeFile")
	@ResponseBody
	public List<MobileOrganization> getAllMobileDeptAndUserList(String token,String type ,HttpServletRequest request) {
		String servicePath = request.getSession().getServletContext().getRealPath("/");
		return userService.getAllMobileDeptAndUserList(type,servicePath);
	}
	
	
	/**
	 * 所有职位列表和用户
	 */
	@RequestMapping(value = "/getAllJobUserMaybeFile")
	@ResponseBody
	public List<UserJob> getAllJobAndUserList(String token,String type ,HttpServletRequest request){
		String servicePath = request.getSession().getServletContext().getRealPath("/");
		return userService.getAllJobAndUserList(type,servicePath);
	}
	
	
	
	/**
	 * 所有职位立列表
	 */
	@RequestMapping(value = "/findUsersByDept")
	@ResponseBody
	public List<UserMobileVo> findUsersByDept(String token,String deptId,String deptLevel, int pageNo, int pageSize,String deptType,int level) throws Exception {
//		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage =  new DataPackage(pageNo, pageSize);	
		List<UserMobileVo> userList=userService.findUsersByDept(dataPackage,deptId,deptLevel,deptType,level);
		return userList;
	}
	
	/**
	 * 所有职位立列表
	 */
	@RequestMapping(value = "/findUsersByDeptPC")
	@ResponseBody
	public List<UserMobileVo> findUsersByDeptPC(String token,String deptId,String deptLevel, int pageNo, int pageSize,String deptType,int level) throws Exception {
//		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage =  new DataPackage(pageNo, pageSize);	
		List<UserMobileVo> userList=userService.findUsersByDeptPC(dataPackage,deptId,deptLevel,deptType,level);
		return userList;
	}

	@RequestMapping(value = "/findUsersInPC")
	@ResponseBody
	public Map<String,List<UserMobileVo>> findUsersInPC() throws Exception {
		DataPackage dataPackage =  new DataPackage(0, 999999);	
		Map<String, List<UserMobileVo>> map = new HashMap<String, List<UserMobileVo>>();
		List<UserMobileVo> userMobileVos=userService.findUsersInPC1(dataPackage);
		map.put("userMobileVos", userMobileVos);
		return map;
	}
	
	/**
	 * 所有职位列表
	 */
	@RequestMapping(value = "/getAllUserJobList")
	@ResponseBody
	public List<UserJob> getAllUserJobList(String token){
//		TokenUtil.checkTokenWithException(token);	
		return userJobService.findAllUserJob();
	}
	
	/**
	 * 所有职位列表和用户
	 */
	@RequestMapping(value = "/getAllJobAndUserList")
	@ResponseBody
	public List<UserJob> getAllJobAndUserList(String token,HttpServletRequest request){
		String servicePath = request.getSession().getServletContext().getRealPath("/");
		return userService.getAllJobAndUserList("1",servicePath);
	}
	
	/**
	 *按职位查询用户列表
	 */
	@RequestMapping(value = "/findUsersByJobId")
	@ResponseBody
	public List<UserMobileVo> findUsersByJobId(String token,String jobId, int pageNo, int pageSize) throws Exception {
//		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage =  new DataPackage(pageNo, pageSize);	
		List<UserMobileVo> userList=userService.findUsersByJobId(dataPackage,jobId);
		return userList;
	}
	
	
	/**
	 *按职位查询用户列表
	 */
	@RequestMapping(value = "/getTeacherCourseGrade")
	@ResponseBody
	public List getTeacherCourseGrade(String token,String teacherId, String startDate, String endDate) throws Exception {
//		TokenUtil.checkTokenWithException(token);
		return reportService.getTeacherCourseGrade(teacherId, startDate, endDate);
	}
	
	/**
	 *按用户名字查询用户列表
	 */
	@RequestMapping(value = "/findUsersByUserName")
	@ResponseBody
	public List<UserMobileVo> findUsersByUserName(String token,String userName) throws Exception {
//		TokenUtil.checkTokenWithException(token);
		List<UserMobileVo> userList=userService.findUsersByUserName(userName);
		return userList;
	}
	
	/**
	 * 通过用户名查询用户所在的组织架构及用户信息
	 * @param token
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/findOrgAndUserInfoByUserName")
	@ResponseBody
	public Map<String, Object> findOrgAndUserInfoByUserName(String token,String userName)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder buffer = new StringBuilder();
		//通过用户名获取用户列表
		List<UserMobileVo> userList=userService.findUsersByUserNameWithJiaXue(userName);
		//如果返回的用户列表为空则直接返回
		if(userList == null || userList.size() == 0)
			return null;
		//获取用户的id，用逗号隔开
		for(UserMobileVo user:userList)
		{
			buffer.append("'").append(user.getUserId()).append("',");
		}
		buffer = buffer.deleteCharAt(buffer.length()-1);
		String userIds = buffer.toString();
		//获取userIds对应的组织架构
		List<MobileOrganization> orgs = userService.getAllMobileDeptListByUserIds(userIds);
		List<MobileOrganization> res=new ArrayList<MobileOrganization>();
		//result.put("users", userList);
		for(MobileOrganization mo:orgs){
			if(getAllUserCount(mo)>0){
				res.add(mo);
			}
		}
		result.put("orgs", res);
		return result;
	}
	
	
	private int getAllUserCount(MobileOrganization mo){
		int result=0;
		if(mo.getUserList()!=null) result=mo.getUserList().size();
		List<MobileOrganization> lmo= mo.getSubMobileOrganizations();
		if(lmo!=null && lmo.size()>0){
			for(MobileOrganization mo1:lmo){
				if(mo1.getUserList()!=null) result+=mo1.getUserList().size();
				List<MobileOrganization> lmo1= mo1.getSubMobileOrganizations();
				if(lmo1!=null && lmo1.size()>0){
					for(MobileOrganization mo2:lmo1){
						if(mo2.getUserList()!=null) result+=mo2.getUserList().size();
						List<MobileOrganization> lmo2= mo2.getSubMobileOrganizations();
						if(lmo2!=null && lmo2.size()>0){
							for(MobileOrganization mo3:lmo2){
								if(mo3.getUserList()!=null)  result+=mo3.getUserList().size();
								List<MobileOrganization> lmo3= mo3.getSubMobileOrganizations();
								if(lmo3!=null && lmo3.size()>0){
									for(MobileOrganization mo4:lmo3){
										if(mo4.getUserList()!=null)  result+=mo4.getUserList().size();
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 通过组织架构ID获取下级的组织架构信息
	 * @param token
	 * @param organizationId
	 * @return
	 */
	@RequestMapping(value = "/findSubOrganizationByOrgId")
	@ResponseBody
	public Map<String, Object> findSubOrganizationByOrgId(String token,String organizationId)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", userService.getSubMobileDeptListByOrgId(organizationId));
		return result;
	}
	
	
	/**
	 *按荣联帐号查询用户
	 */
	@RequestMapping(value = "/findUsersByCcpAccount")
	@ResponseBody
	public UserMobileVo findUsersByCcpAccount(String token,String ccpAccount) throws Exception {
		UserMobileVo user=userService.findUsersByCcpAccount(ccpAccount);
		return user;
	}
	
	/**
	 * 
	 * 小班学生列表
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/getSmallStudentList")
	@ResponseBody
	public List getSmallStudentList(@RequestParam String token, MobileStudentListTo to){
		TokenUtil.checkTokenWithException(token);
		return studentService.getMobileMiniClassStudents(to);
	}
	
	/**
	 * 
	 * 小班学生列表
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/getOtmStudentList")
	@ResponseBody
	public List getOtmStudentList(@RequestParam String token, MobileStudentListTo to,  StudentVo studentVo){
		TokenUtil.checkTokenWithException(token);
		to.setStudentVo(studentVo);
		return studentService.getMobileOtmClassStudents(to);
	}

	/*
	@ResponseBody
	public List getOtmStudentList(@RequestParam String token,StudentVo studentVo,MobileStudentListTo to) throws Exception {
		TokenUtil.checkTokenWithException(token);

		Student stu=HibernateUtils.voObjectMapping(studentVo,Student.class);
        to.setStudent(stu);
        return  studentService.getMobileStudentList(to);
	}

	 */
	
	/**
	 * 目标班学生列表
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/getPromiseStudentList")
	@ResponseBody
	public List getPromiseStudentList(@RequestParam String token,MobileStudentListTo to){
		TokenUtil.checkTokenWithException(token);
		return studentService.getMobilePromiseStudents(to);
	}
	
	
	/**********************************************************星火管理端APP接口 end**************************************************************************************************/
	
	/**
	 * 查找学生的消费记录
	 * @param 
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getStudentConsumes")
	@ResponseBody
	public List<AccountChargeRecordsVo> getStudentConsumes(String studentId,String productType,String token, DataPackage dataPackage,TimeVo timeVo){
		TokenUtil.checkTokenWithException(token);
		dataPackage.setSidx("payTime");dataPackage.setSord("desc");
		AccountChargeRecordsVo accountChargeRecordsVo = new AccountChargeRecordsVo();
		accountChargeRecordsVo.setStudentId(studentId);
		accountChargeRecordsVo.setProductTypeName(productType);		
		List<AccountChargeRecordsVo> vos =   (List<AccountChargeRecordsVo>) chargeService.findPageMyCharge(dataPackage, accountChargeRecordsVo,timeVo).getDatas();
		return vos;
	} 
	
	/**
	 * 查找当前用户当天的全部课程
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/getCourseBySign")
	@ResponseBody
	public List<CourseVo> getCourseBySign(String token,String courseDate,String userSign){
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage = new DataPackage();		
		dataPackage.setPageSize(1000);
		if(StringUtil.isNotBlank(userSign)){
			if(userSign.equals("allCourseDay")){
				dataPackage = courseService.findCourseDay(courseDate,dataPackage);
			}else{
				CourseVo vo = new CourseVo();
				vo.setCurrentRoleId(userSign);
				if(userSign.equals("studyManegerVerify")){//学管待确认课程(即老师已考勤)
					vo.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
				}else if(userSign.equals("classTeacherDeduction")){//教务待扣费课程 (即学管已确认)
					vo.setCourseStatus(CourseStatus.STUDY_MANAGER_AUDITED);
				}
				vo.setCourseDate(courseDate);
				dataPackage = courseService.getOneOnOneBatchAttendanceList(vo, dataPackage);
			}
		}else{			
			dataPackage = courseService.findCourseDay(courseDate,dataPackage);
		}
		return (List<CourseVo>) dataPackage.getDatas();
	}
	
	/**
	 * 批量考勤提交
	 * @return
	 */
    /**
     *
     * @param token
     * @param status
     * @param courseIds
     * @return
     * @throws Exception
     */
	@RequestMapping("/mutilAttendaceSubmitWithDuartion")
	@ResponseBody
	public Response mutilAttendaceSubmitWithDuartion(@RequestParam String token,@RequestParam String status,@RequestParam String targetParams) throws Exception{
		TokenUtil.checkTokenWithException(token);
		if(StringUtils.isNotBlank(targetParams)){	            
			  OneOnOneBatchAttendanceEditVo oneOnOneBatchAttendanceEditVo=new OneOnOneBatchAttendanceEditVo();	            
			  oneOnOneBatchAttendanceEditVo.setTargetParams(targetParams);
         	  oneOnOneBatchAttendanceEditVo.setOperteType(status);
	          courseService.oneOnOneBatchAttendanceEdit(oneOnOneBatchAttendanceEditVo);
	        }else{
	            throw new ApplicationException("传入的课程ID不允许为空");
	        }
		
		return new Response();
	}
	
	/**
	 * 保存学生档案图片
	 * @throws Exception 
	 * */
	@RequestMapping(value = "/saveStudentFile", method =  RequestMethod.POST)
	public void saveStudentFile( @RequestParam("myfile") MultipartFile studentFile, @RequestParam String token, @RequestParam String studentId ,@RequestParam String studentFileType,String docDescription ,HttpServletRequest request) {
		TokenUtil.checkTokenWithException(token);
		StudentFileVo studentFileVo=new StudentFileVo();
		if(StringUtils.isNotBlank(studentId)){
			studentFileVo.setStudentId(studentId);
		}else{
			 throw new ApplicationException("传入的学生ID不允许为空");
		}
		if(StringUtils.isNotBlank(studentFileType)){			
			if(StudentFileType.TEACHINGPLAN.getValue().equals(studentFileType)){//教案
				studentFileVo.setStudentFileType(StudentFileType.TEACHINGPLAN);
			}else if(StudentFileType.EXAMPAPER.getValue().equals(studentFileType)){//试卷
				studentFileVo.setStudentFileType(StudentFileType.EXAMPAPER);
			}else if(StudentFileType.COACHING.getValue().equals(studentFileType)){//辅导
				studentFileVo.setStudentFileType(StudentFileType.COACHING);
			}else{
				throw new ApplicationException("传入的图片类型错误");
			}
			
		}else{
			 throw new ApplicationException("传入的图片类型不允许为空");
		}	
		
		if(studentFile==null || studentFile.getSize()<=0){
			 throw new ApplicationException("传入的图片不能为空");
		}		
		
		if(StringUtils.isNotBlank(docDescription)){
			studentFileVo.setDocDescription(docDescription);
		}else{
			studentFileVo.setDocDescription("");
		}		 
		
		String servicePath = request.getSession().getServletContext().getRealPath("/");
		studentService.saveStudentFile(studentFile, studentFileVo,servicePath);	
	}
	
	
	
	
	/**
	 * 获取学生档案图片
	 * @throws Exception 
	 * */
	@RequestMapping(value = "/getStudentFile")
	@ResponseBody
	public List<StudentFileVo> getStudentFile(@RequestParam String token, String studentId , String studentFileType, DataPackage dataPackage){
		TokenUtil.checkTokenWithException(token);
		 
		StudentFileVo studentFileVo = new StudentFileVo();
		studentFileVo.setStudentId(studentId);
		if(StringUtils.isNotBlank(studentFileType)){			
			if(StudentFileType.TEACHINGPLAN.getValue().equals(studentFileType)){//教案
				studentFileVo.setStudentFileType(StudentFileType.TEACHINGPLAN);
			}else if(StudentFileType.EXAMPAPER.getValue().equals(studentFileType)){//试卷
				studentFileVo.setStudentFileType(StudentFileType.EXAMPAPER);
			}else if(StudentFileType.COACHING.getValue().equals(studentFileType)){//辅导
				studentFileVo.setStudentFileType(StudentFileType.COACHING);
			}			
		}		
		dataPackage = studentService.getStudentFile(studentFileVo,dataPackage);
		
		return (List<StudentFileVo>) dataPackage.getDatas();
	} 
	
	
	
	
	/**
	 * 学生档案类型
	 * @return
	 */
	@RequestMapping(value = "/getStudentFileType")
	@ResponseBody
	public List<StudentFileTypeVo> getStudentFileType(){		
		List<StudentFileTypeVo> list = new ArrayList<StudentFileTypeVo>();		
		for (StudentFileType studentFileType : StudentFileType.values()){
			StudentFileTypeVo studentFileTypeVo=new StudentFileTypeVo();
			studentFileTypeVo.setName(studentFileType.getName());
			studentFileTypeVo.setValue(studentFileType.getValue());
			list.add(studentFileTypeVo);
		}
		return   list; 
	}
	
	/**
	 * 修改学生信息
	 * @param token
	 * @param cus
	 * @throws Exception
	 */
	@RequestMapping(value = "/editStudent", method =  RequestMethod.POST)
	@ResponseBody
	public Response  editStudent(@RequestParam String token, @ModelAttribute StudentVo studentVo) throws Exception {
		TokenUtil.checkTokenWithException(token);
		studentService.updateStudentForSimple(studentVo);
		return new Response();
	}
	
	
	/**
	 * 根据客户Id得到跟进记录列表
	 * @param token
	 * @param customerId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCustomerRecrodsList")
	@ResponseBody
	public List<CustomerFollowUpRecrodsVo> getCustomerRecrodsList(@RequestParam String token, @RequestParam String customerId, int pageNo, int pageSize) throws Exception {
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage =  new DataPackage(pageNo, pageSize);		
		Customer cus=new Customer();
		cus.setId(customerId);		
		List<CustomerFollowUpRecrodsVo> list = new ArrayList<CustomerFollowUpRecrodsVo>();
		//预约
		CustomerFollowUpRecrodsVo appointmentVo=customerService.getAppointment(customerId, AppointmentType.APPOINTMENT);	
		if(appointmentVo!=null){
			list.add(appointmentVo);
		}
		//下次跟进
		CustomerFollowUpRecrodsVo followupVo=customerService.getAppointment(customerId, AppointmentType.FOLLOW_UP);
		if(followupVo!=null){
			list.add(followupVo);
		}		
		
		//跟进记录
		dataPackage = customerService.gtCustomerFollowingRecords(cus, dataPackage); 
		List<CustomerFolowupVo> folowupList =(List<CustomerFolowupVo>) dataPackage.getDatas();
		for(CustomerFolowupVo folowupVo:folowupList){
			CustomerFollowUpRecrodsVo vo= new CustomerFollowUpRecrodsVo();
			vo.setCreateTime(folowupVo.getCreateTime());  //跟进时间
			vo.setPlanTime(folowupVo.getMeetingTime());  //预约上门或者预约跟进时间
			vo.setCreateUserName(folowupVo.getCreateUserName());  //跟进人
			vo.setRemark(folowupVo.getRemark());  //备注
			vo.setType(folowupVo.getAppointmentType()==null?null:folowupVo.getAppointmentType().getValue());     //类型
			vo.setSatisficingName(folowupVo.getSatisficingName());   //意向度
			list.add(vo);
		}
		
		return list;
	}

	/**
	 * 跟进客户id获取客户动态
	 * @param token
	 * @param customerId
     * @return
     */
	@RequestMapping(value = "/customerDynamicStatusByCustomerId")
	@ResponseBody
	public List<CustomerDynamicStatusVo> customerDynamicStatusByCustomerId(@RequestParam String token,@RequestParam String customerId) {
		TokenUtil.checkTokenWithException(token);
		return (List<CustomerDynamicStatusVo>) customerEventService.findCustomerDynamicStatusByCustomerId(customerId, new DataPackage(0, 999)).getDatas();
	}
	
	/**
	 * 获取到老师的联系人信息
	 * @param token
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value="/getContactsForMobileUserId")
	@ResponseBody
	public List<MobileUserVo> getContactsForMobileUserId(@RequestParam String token, @RequestParam String mobileUserId) {
		TokenUtil.checkTokenWithException(token);
		
		MobileUser mUser =mobileUserService.findMobileUserById(mobileUserId);
		List<MobileUserVo> mobileUserVoList =null;
		List<MobileUserVo> returnMobileUserVoList=new ArrayList<MobileUserVo>();
		if(mUser==null){
			return returnMobileUserVoList;
		}else if(mUser.getUserType()==MobileUserType.STAFF_USER || mUser.getUserType()==MobileUserType.MANAGE){//员工
			mobileUserVoList = studentService.getMobileContactsForStaff(mUser.getUserId());		   
		}else if(mUser.getUserType()==MobileUserType.STUDENT_USER){//学生
			mobileUserVoList = userService.getMobileContactsByStudent(mUser.getUserId());		   
		}
		
		
		if(mobileUserVoList!=null){
			Map<String, String> map = new HashMap<String, String>();
		    for(MobileUserVo mobileUserVo:mobileUserVoList){
		    	if (map.get(mobileUserVo.getId())==null){
		    		map.put(mobileUserVo.getId(), mobileUserVo.getId());
		    		returnMobileUserVoList.add(mobileUserVo);  
		    	} 
		    } 
		} 
		return returnMobileUserVoList;
	     
	}
	
	/**
	 * 上传 小班的 考勤 签名图片
	 * @param token
	 * @param miniClassCourseId
	 * @param attendancePicFile
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/submitMiniClassAttendancePic", method = RequestMethod.POST)
	@ResponseBody
	public Response submitMiniClassAttendancePic(@RequestParam String token, @RequestParam String miniClassCourseId, @RequestParam("attendancePic") MultipartFile attendancePicFile,HttpServletRequest request) throws Exception {
		TokenUtil.checkTokenWithException(token);
		String servicePath = request.getSession().getServletContext().getRealPath("/");
		
		
		MiniClassCourse miniClassCourse = smallClassService.getMiniClassCourseById(miniClassCourseId);
		String fileName ="";
		if(miniClassCourse !=null) {
			// 统一使用 文件名 来 标明签名文件
			//String fileName = UUID.randomUUID().toString();
			fileName =  String.format("MINI_CLASS_ATTEND_PIC_%s.jpg", miniClassCourseId);
			String folder=servicePath+PropertiesUtils.getStringValue("save_file_path");//系统路径
			FileUtil.isNewFolder(folder);			 
			
			String bigFileName = "BIG_"+fileName;//阿里云上面的文件名 大   默认是JPG 
			String midFileName = "MID_"+fileName;//阿里云上面的文件名 中
			String smallFileName = "SMALL_"+fileName;//阿里云上面的文件名 小					 
			
			String relFileName=folder+"/realFile_"+miniClassCourseId+UUID.randomUUID().toString()+".jpg";
			File realFile=new File(relFileName);
			File bigFile=new File(folder+"/"+bigFileName);
			File midFile=new File(folder+"/"+midFileName);
			File smallFile=new File(folder+"/"+smallFileName);			
			
			try {				
				attendancePicFile.transferTo(realFile);
				ImageSizer.compressImage(relFileName, smallFile.getAbsolutePath(), 60);//转换图片大小
				ImageSizer.compressImage(relFileName, midFile.getAbsolutePath(), 200);//转换图片大小
				ImageSizer.compressImage(relFileName, bigFile.getAbsolutePath(), 600);//转换图片大小
				AliyunOSSUtils.put(bigFileName, bigFile);//传到阿里云
				AliyunOSSUtils.put(midFileName, midFile);//传到阿里云
				AliyunOSSUtils.put(smallFileName, smallFile);//传到阿里云				
				AliyunOSSUtils.put(fileName, realFile);//传原图到阿里云				
			} catch (IllegalStateException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}finally{				
				bigFile.delete();
				midFile.delete();
				smallFile.delete();
				realFile.delete();
			}

			if(StringUtils.isNotBlank(fileName)){
				miniClassCourse = smallClassService.getMiniClassCourseById(miniClassCourseId);
				miniClassCourse.setAttendacePicName(fileName);
				smallClassService.updateMiniClassCourse(miniClassCourse);
			}


		}
		
		
		
		//从service迁移出来 xiaojinwang 20171029
//		smallClassService.saveMiniClassAttendancePic(miniClassCourseId, attendancePicFile,servicePath);
		return new Response();
	}



	/**
	 * 获取系统时间
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/getSystemTime")
	@ResponseBody
	public SystemTimeVo getSystemTime(){	
		SystemTimeVo systemTimeVo=new SystemTimeVo();
		systemTimeVo.setLastFetchTime(DateTools.getCurrentDateTime());
		return systemTimeVo;
	}
	

	/**
	 * 按课程状态查找用户的课程
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/getCourseByCourseStatus")
	@ResponseBody
	public List<CourseVo> getCourseByCourseStatus(@RequestParam String token,String courseDate,String courseStatus,String searchParam, int pageNo, int pageSize,CourseStatus searchStatus){
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage = new DataPackage(pageNo, pageSize);	
		if(StringUtil.isNotBlank(courseStatus)){
				CourseVo vo = new CourseVo();
				vo.setCurrentRoleId(courseStatus);
				vo.setSearchParam(searchParam);
				vo.setCourseStatus(searchStatus);
//				if(courseStatus.equals("studyManegerVerify")){//学管待确认课程(即老师已考勤)
//					vo.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
//				}else if(courseStatus.equals("classTeacherDeduction")){//教务待扣费课程 (即学管已确认)
//					vo.setCourseStatus(CourseStatus.STUDY_MANAGER_AUDITED);
//				}
				vo.setCourseDate(courseDate);
				dataPackage = courseService.getOneOnOneBatchAttendanceListForMobile(vo, dataPackage);
		}
		return (List<CourseVo>) dataPackage.getDatas();
	}
	
	@RequestMapping(value = "/getPayInfo")
	@ResponseBody
	public Map findMoneyReadyToPayById(String signMsg,String encryptedMsg){
		Map map=new HashMap();
		Map resultMap=new HashMap();
		try {
			resultMap=moneyReadyToPayService.findByIdAndSignMsg(signMsg,encryptedMsg);
		}catch(ApplicationException e){
			e.printStackTrace();
			map.put("resultCode", "1");
			map.put("resultMessage", e.getErrorMsg());
			String returnMsg=CertUtil.mapToString(map);
			return CertUtil.EncryptMsg(returnMsg);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resultCode", "1");
			map.put("resultMessage", "系统异常");
			String returnMsg=CertUtil.mapToString(map);
			return CertUtil.EncryptMsg(returnMsg);
		}
		return resultMap;
	}
	
	/**
	 * @param payNo  星火支付单号
	 * @param payResultCode 支付结果
	 * @param payResultMessage 支付信息
	 * @param SignMsg 加密信息
	 * @param posCode 终端号
	 * @param payCode 银联支付流水号
	 * @param busNo 商户编号
	 * @param cardNo 支付卡号
	 * @param amount 金额
	 * @param transactionTime  付款时间
	 * @param encryptedMsg 加密信息
	 * @return
	 */
	@RequestMapping(value = "/payResultNotify")
	@ResponseBody
	public Map payResultNotify(String signMsg,String encryptedMsg) {
		Map map=new HashMap();
		Map resultMap=new HashMap();
		try {
			resultMap=moneyReadyToPayService.payResultNotify(signMsg,encryptedMsg);
		}catch(ApplicationException e){
			e.printStackTrace();
			map.put("resultCode", "1");
			map.put("resultMessage", e.getErrorMsg());
			String returnMsg=CertUtil.mapToString(map);
			return CertUtil.EncryptMsg(returnMsg);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resultCode", "1");
			map.put("resultMessage", "系统异常");
			String returnMsg=CertUtil.mapToString(map);
			return CertUtil.EncryptMsg(returnMsg);
		}
		return resultMap;
	}
	
	/**
	 * 
	 * @param token
	 * @param loginDeviceType  登录设备类型（1：适配iphone/android系统的设备，2：ipad）
	 * @param request
	 * @param response
	 * @return
	 * @description 描述 自动登录邮件系统
	 * @author wmy
	 */
	@RequestMapping(value = "/mailAutoLogin")
	@ResponseBody
	public void autoLogin(@RequestParam String token, @RequestParam Integer loginDeviceType, HttpServletRequest request, HttpServletResponse response){
		 if(true) {  //开启邮件系统
			 TokenUtil.checkTokenWithException(token);
			 User user = userService.getCurrentLoginUser();
			 if(user != null) {
				 try {
				     mailService.mailAutoLogin(request, response, user, loginDeviceType);
				 } catch (Exception e) {
				     e.printStackTrace();
				     throw new ApplicationException("该用户还没开启企业邮箱");
				 }
			 } 
		 } 
		 /*else {
			 throw new ApplicationException(ErrorCode.MAIL_SYS_DISABLED);
		 }*/
	  }
	
	/**
	 * 获取用户信件信息
	 */
	@RequestMapping(value = "/getMailCountInfo", method =  RequestMethod.GET)
	@ResponseBody
	public MailBoxMsg getMailCountInfo(@RequestParam String token) throws Exception {
		MailBoxMsg mailBoxMsg = new MailBoxMsg();
		if(mailService.isMailSysInUse() == true) {
			TokenUtil.checkTokenWithException(token);
			User user = userService.getCurrentLoginUser();
			mailBoxMsg =  mailService.getMailCountInfo(user);
		} else {
			log.error("邮箱功能已屏蔽");
		}
		return mailBoxMsg;
	}
	
	/**
	 * 手机端课程统计接口
	 * @param token
	 * @param gridRequest
	 * @param basicOperationQueryVo
	 * @return
	 */
	@RequestMapping(value = "/getCoursesAnalyze", method =  RequestMethod.GET)
	@ResponseBody
	public List getCoursesAnalyze(@RequestParam String token,@ModelAttribute GridRequest gridRequest,
			@ModelAttribute BasicOperationQueryVo basicOperationQueryVo) {
		log.info("getCoursesAnalyze() start.");
		TokenUtil.checkTokenWithException(token);
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage=reportService.getCoursesAnalyzeForMobile(basicOperationQueryVo, dataPackage);
		log.info("getCoursesAnalyze() end.");
		return (List) dataPackage.getDatas();
	}
	
	/**
	 * 手机端学生课程列表
	 */
	@RequestMapping(value = "/getStudentCourseScheduleList", method =  RequestMethod.GET)
	@ResponseBody
	public List<CommonClassCourseVo> getStudentCourseScheduleList(@RequestParam String token, @RequestParam String studentId, String startDate, String endDate) throws Exception {
		if (StringUtil.isBlank(startDate)) {
    		startDate = DateTools.getCurrentDate();
    	}
		if (StringUtil.isBlank(endDate)) {
    		endDate = DateTools.getCurrentDate();
    	}
		TokenUtil.checkTokenWithException(token);
		return courseService.getDifferentClassCourseList(studentId, startDate, endDate);
	}

	/**
	 * 手机端学生课程详情,list大小大于1，则表示课程有冲突
	 */
	@RequestMapping(value = "/getCourseDetails", method =  RequestMethod.GET)
	@ResponseBody
	public List<CommonClassCourseVo> getCourseDetails(@RequestParam String token,  @RequestParam String courseId, @RequestParam String productType) throws Exception {
//		TokenUtil.checkTokenWithException(token);
		return courseService.getCourseDetailsThatMaybeConfig(courseId, productType);
	}
	
	/**
	 * 根据类型获取新闻
	 * @param token
	 * @param newsType
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value ="/getAppNewsManage")
	@ResponseBody
	public List<AppNewsManageVo> getAppNewsManage(@RequestParam String token,@RequestParam String newsType,int pageNo,int pageSize){
		TokenUtil.checkTokenWithException(token);
		DataPackage dp=new DataPackage(pageNo,pageSize);
		AppNewsManageVo vo=new AppNewsManageVo();
		vo.setType((NewsType.valueOf(newsType)));
		vo.setTopButn("app");
		dp=appNewsManageService.getNews(dp, vo);
		List<AppNewsManageVo> list=(List<AppNewsManageVo>)dp.getDatas();
		return list;
	}
	
	
	/**
	 * 获取指定公告类型（noticeType）的前num条记录
	 * @param token
	 * @param noticeType 公告类型
	 * @param num 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getSystemNoticeByTypeTopNum", method =  RequestMethod.GET)
	@ResponseBody
	public List<SystemNoticeMobileVo> getSystemNoticeByTypeTopNum(@RequestParam String token, String recordId,  @RequestParam String noticeType) throws Exception{
//		公告() 不要了
//		String type=noticeType;
//		if(!StringUtils.isNotBlank(noticeType)) type="DAT0000000344";
//		List<SystemNoticeVo> lsn=systemNoticeService.getSystemNoticeByTypeTopNum(recordId, type, num1);

		//稍后改成动态设置		
		String num1=appNewsManageService.getNewsbannerNum();
		int num2=Integer.valueOf(num1)-1;
		List<AppNewsManageVo> appLsn=new ArrayList<AppNewsManageVo>();
		List<SystemNoticeMobileVo> lsnms=new ArrayList<SystemNoticeMobileVo>();	
		if(recordId!=null && StringUtils.isNotBlank(recordId) && noticeType!=null && StringUtils.isNotBlank(noticeType)){
			String array[] = recordId.split("&");
	         for(int i=0; i<array.length; i++) {
	 			String v[] = array[i].split("=");
	 			if(v[0].equals("id")) {
	 				recordId=v[1];				
	 			}
	 		}
			appLsn=appNewsManageService.getNewsByType(recordId, noticeType);
		}else{
			TokenUtil.checkTokenWithException(token);
			appLsn=appNewsManageService.getImageUrl();	
			List<AppNewsManageVo> appNewsBanner=appNewsManageService.getNewsBanner(num2);				
			//获取新闻各类的最新数据 
			if(appNewsBanner!=null && appNewsBanner.size()>0){
				for(AppNewsManageVo app:appNewsBanner){
					appLsn.add(app);
				}
			}
		}
		
		if(appLsn!=null && appLsn.size()>0){
			for(AppNewsManageVo app:appLsn){
				SystemNoticeMobileVo vo=new SystemNoticeMobileVo();
				Organization createOrg=new Organization();
				Organization modifyOrg=new Organization();				
				vo.setType("NEWS_MANAGE");
				vo.setAssistantTitle(app.getAssistantTitle());
				vo.setCreateTime(app.getCreateTime());
				vo.setCreateUserId(app.getCreateUserId());
				vo.setCreateUserName(app.getCreateUserName());
				vo.setId(app.getId());
				vo.setModyfyTime(app.getModifyTime());								
				vo.setTitle(app.getTitle());
				vo.setRealFileName(app.getCoverImageName());//图片名字
				vo.setAliPath(app.getAliPath());
				vo.setContentStatus(app.getContentStatus());
				if(app.getContentUrl()!=null){
					vo.setContentUrl(app.getContentUrl());					
				}
				if(app.getType()!=null){
					vo.setNoticeType(app.getType().getValue()); //app新闻类型
					vo.setNoticeTypeName(app.getType().getName());
				}				
				if(app.getCoverImageScreenshot()!=null && StringUtils.isNotBlank(app.getCoverImageScreenshot())){
					vo.setFilePath(app.getCoverImageScreenshot());
				}else{
					vo.setFilePath(app.getCoverImagePath());
				}
			
				if(vo.getCreateUserId()!=null && StringUtils.isNotBlank(vo.getCreateUserId())){
					User createUser=userService.findUserById(vo.getCreateUserId());
					createOrg=organizationDao.findById(createUser.getOrganizationId());
					vo.setCreateUserOrgId(createOrg.getId());
					vo.setCreateUserOrg(createOrg.getName());
				}
				if(vo.getModifyUserId()!=null && StringUtils.isNotBlank(vo.getModifyUserId())){
					User modifyUser=userService.findUserById(vo.getModifyUserId());
					modifyOrg=organizationDao.findById(modifyUser.getOrganizationId());
					vo.setModifyUserOrgId(modifyOrg.getId());
					vo.setModifyUserOrg(modifyOrg.getName());
				}
				
				lsnms.add(vo);
			}
		}	
		return lsnms;
	}
	
	/**
	 * 获取指定公告类型（noticeType）的前num条记录
	 * @param token
	 * @param noticeType 公告类型
	 * @param num 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAppNews3", method =  RequestMethod.GET)
	@ResponseBody
	public List<SystemNoticeMobileVo> getAppNews3(@RequestParam String token) throws Exception{
//		TokenUtil.checkTokenWithException(token);
		List<SystemNoticeVo> lsn=systemNoticeService.getSystemNoticeByTypeTopNum(null, "DAT0000000344", "3");
		List<SystemNoticeMobileVo> lsnms=HibernateUtils.voListMapping(lsn, SystemNoticeMobileVo.class);
		return lsnms;
	}
	
//	@RequestMapping(value = "/getSystemNoticeOneAndMore", method =  RequestMethod.GET)
//	@ResponseBody
//	public List<SystemNoticeVo> getSystemNoticeOneAndMore(@RequestParam String token,@RequestParam String recordId,@RequestParam String noticeType,  @RequestParam Integer num) throws Exception{
////		TokenUtil.checkTokenWithException(token);
//		List<SystemNotice> lsn=systemNoticeService.getSystemNoticeOneAndMore(recordId,noticeType, num);
//		return HibernateUtils.voListMapping(lsn, SystemNoticeVo.class);
//	}
	
	@RequestMapping(value = "/getSystemNoticeVoById", method =  RequestMethod.GET)
	@ResponseBody
	public SystemNoticeMobileDetailVo getSystemNoticeVoById(@RequestParam String token,@RequestParam String recordId){
		//现在是获取新闻的信息
//		SystemNoticeVo sn=systemNoticeService.findSystemNoticeVoById(recordId);
//		SystemNoticeMobileDetailVo sno=HibernateUtils.voObjectMapping(sn, SystemNoticeMobileDetailVo.class);
		String array[] = recordId.split("&");
         for(int i=0; i<array.length; i++) {
 			String v[] = array[i].split("=");
 			if(v[0].equals("id")) {
 				recordId=v[1];				
 			}
 		}
		AppNewsManageVo app=appNewsManageService.findAppNewsManageById(recordId);
		SystemNoticeMobileDetailVo vo=new SystemNoticeMobileDetailVo();
		if(app!=null){
			Organization createOrg=new Organization();
			Organization modifyOrg=new Organization();				
			vo.setCreateTime(app.getCreateTime());
			vo.setCreateUserId(app.getCreateUserId());
			vo.setCreateUserName(app.getCreateUserName());
			vo.setId(app.getId());
			vo.setModyfyTime(app.getModifyTime());								
			vo.setTitle(app.getTitle());
			vo.setRealFileName(app.getCoverImageName());//图片名字
			vo.setAliPath(app.getAliPath());
			vo.setContentUrl(app.getContentUrl());
			vo.setContent(app.getContent());
			vo.setContentStatus(app.getContentStatus());
			if(app.getType()!=null){
				vo.setNoticeType(app.getType().getValue()); //app新闻类型
				vo.setNoticeTypeName(app.getType().getName());
			}				
			if(app.getCoverImageScreenshot()!=null && StringUtils.isNotBlank(app.getCoverImageScreenshot())){
				vo.setFilePath(app.getCoverImageScreenshot());
			}else{
				vo.setFilePath(app.getCoverImagePath());
			}
		
			if(vo.getCreateUserId()!=null && StringUtils.isNotBlank(vo.getCreateUserId())){
				User createUser=userService.findUserById(vo.getCreateUserId());
				createOrg=organizationDao.findById(createUser.getOrganizationId());
				vo.setCreateUserOrgId(createOrg.getId());
				vo.setCreateUserOrg(createOrg.getName());
			}
			if(vo.getModifyUserId()!=null && StringUtils.isNotBlank(vo.getModifyUserId())){
				User modifyUser=userService.findUserById(vo.getModifyUserId());
				modifyOrg=organizationDao.findById(modifyUser.getOrganizationId());
				vo.setModifyUserOrgId(modifyOrg.getId());
				vo.setModifyUserOrg(modifyOrg.getName());
			}
		}
		return vo;
	}
	
	/**
	 * 根据用户编号与部门编号获取用户在该部门的主要职位
	 * @param token 标识
	 * @param userId 用户Id
	 * @param orgId 部门ID
	 * @return
	 */
	@RequestMapping(value = "/getUserJobByDepartment", method =  RequestMethod.GET)
	@ResponseBody
	public List<UserJobAndOrganizationVo> getUserJobByDepartment(@RequestParam String token,@RequestParam String userId,@RequestParam String deptId){
		//TokenUtil.checkTokenWithException(token);
		List<UserDeptJob> udjvList= userDeptJobService.findDeptJobByUserIdWithDeptId(userId, deptId);
		List<UserJobAndOrganizationVo> ujList=new ArrayList<UserJobAndOrganizationVo>();
		for(UserDeptJob udjv:udjvList){
			UserJob uj=userJobService.findUserJobById(udjv.getJobId());
			Organization org=organizationDao.findById(udjv.getDeptId());
			UserJobAndOrganizationVo uov=new UserJobAndOrganizationVo();
			uov.setIsMajorRole(udjv.getIsMajorRole());
			uov.setId(uj.getId());
			uov.setJobName(uj.getJobName());
			uov.setFlag(uj.getFlag());
			uov.setRemark(uj.getRemark());
			uov.setUserCount(uj.getUserCount());
			uov.setRealCount(uj.getRealCount());
			uov.setJobSign(uj.getJobSign());
			uov.setIsCustomerFollow(uj.getIsCustomerFollow());
			uov.setResourceNum(uj.getResourceNum());
			uov.setReturnCycle(uj.getReturnCycle());
			uov.setCycleType(uj.getCycleType());
			uov.setReturnNode(uj.getReturnNode());
			uov.setOrgId(org.getId());
			uov.setName(org.getName());
			uov.setParentId(org.getParentId());
			uov.setRegionId(org.getRegionId());
			uov.setOrgRemark(org.getRemark());
			uov.setOrgLevel(org.getOrgLevel());
			uov.setOrgOrder(org.getOrgOrder());
			uov.setOrgType(org.getOrgType());
			uov.setProvinceId(org.getProvinceId());
			uov.setAddress(org.getAddress());
			uov.setContact(org.getContact());
			uov.setCustomerPoolName(org.getCustomerPoolName());
			uov.setIsPublicPool(org.getIsPublicPool());
			uov.setAccessRoles(org.getAccessRoles());
			uov.setOrgSign(org.getOrgSign());
			ujList.add(uov);
		}
		return ujList;
	}
	

//	/**
//	 * 根据用户编号与部门编号获取用户在该部门的主要职位
//	 * @param token 标识
//	 * @param userId 用户Id
//	 * @param orgId 部门ID
//	 * @return
//	 */
//	@RequestMapping(value = "/getUserJobByDepartment", method =  RequestMethod.GET)
//	@ResponseBody
//	public List<UserJob> getUserJobByDepartment(@RequestParam String token,@RequestParam String userId,@RequestParam String deptId){
//		//TokenUtil.checkTokenWithException(token);
//		List<UserDeptJobVo> udjvList= userDeptJobService.findDeptJobByUserIdWithDeptId(userId, deptId);
//		List<UserJob> ujList=new ArrayList<UserJob>();
//		for(UserDeptJobVo udjv:udjvList){
//			ujList.add(userJobService.findUserJobById(udjv.getUserDeptJob().getId().getJobId()));
//		}
//		return ujList;
//	}
	
	/**
	 * 废弃 2017-01-11
     * 获取指定时间内的个人工作日程
     * @param startDate
     * @param endDate
     * @param token
     * @return
     */
    @RequestMapping(value ="/getPersonWorkScheduleRecords")
    @ResponseBody
    public Response getPersonWorkScheduleRecordsAndDates(@RequestParam String token, String startDate, String endDate){
//    	if (StringUtil.isBlank(startDate)) {
//    		startDate = DateTools.getCurrentDate();
//    	}
//    	if (StringUtil.isBlank(endDate)) {
//    		endDate = DateTools.getCurrentDate();
//    	}
//
//    	List<PersonWorkScheduleRecordVo> list =  personWorkService.getPersonWorkScheduleRecordsAndDates(startDate, endDate);
//    	return list;
		return new Response(-1, "请更新最新版app再使用");
    }

    /**
	 * 获取当前校区下指定角色的用户
	 * @param roleCode 多个用，号隔开
	 * @return
	 */
	@RequestMapping(value = "/getUserByRoldCodesAndCampusId", method =  RequestMethod.GET)
	@ResponseBody
	public List<UserSimpleMobileVo> getUserByRoldCodesAndCampusId(String token, String roleCode,String organizationId) {
		//TokenUtil.checkTokenWithException(token);	
        List<User> users = null;
        List<UserSimpleMobileVo> lum=new ArrayList<UserSimpleMobileVo>();
		try {
			if (roleCode == null) {
				roleCode=RoleCode.STUDY_MANAGER.toString();
			}
            if(StringUtils.isNotBlank(organizationId)){
                users =userService.getUserByRoldCodesAndOrgId(roleCode,organizationId);
            }else{
                users =userService.getUserByRoldCodes(roleCode);
            }
            lum=HibernateUtils.voListMapping(users, UserSimpleMobileVo.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return lum;
	}
	
	@RequestMapping(value = "/getUserRanking", method =  RequestMethod.GET)
	@ResponseBody
	public UserRankVo getUserRanking(String token, String userId,String startDate,String endDate,String role,String orgId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		UserRankVo urv=new UserRankVo();
		RoleCode roleCode=null;
		if(role.equals("1")) roleCode=null;
		else if(role.equals("2")) roleCode=RoleCode.CONSULTOR;
		else if(role.equals("3")) roleCode=RoleCode.STUDY_MANAGER;
		else return null;
		Organization blBranch=null;
		if(StringUtils.isNotEmpty(orgId)) blBranch=organizationDao.findById(orgId);
		else return null;
		List<Map<String, Object>> lmss2= reportFormsService.getRankWithOrgAndTimeSpan(startDate, endDate, blBranch, roleCode);
		for(int i=0;i<lmss2.size();i++){
			Map<String,Object> mss=lmss2.get(i);
			if(mss.get("user_id").equals(userId)){  
				urv.setRank(i+1);
				if(i==0) urv.setMoney((double)0);
				else urv.setMoney(Double.parseDouble(lmss2.get(i-1).get("amount").toString())-Double.parseDouble(mss.get("amount").toString()));
			}
		}
		if(urv.getMoney()==null) urv.setMoney((double)0);
		String date="";
		int days=DateTools.daysOfTwo(endDate, startDate);
		try{
			if(days>27 && days<32) date=sdf.format(DateTools.getLastMonthStart(startDate))+"~"+sdf.format(DateTools.getLastMonthEnd(startDate));
			else if(7==days) date=sdf.format(DateTools.getLastWeekStart(startDate))+"~"+sdf.format(DateTools.getLastWeekEnd(startDate));
			else if(0==days || 1==days) date=sdf.format(DateTools.getPre(startDate))+"~"+sdf.format(DateTools.getPre(startDate));
		}catch(Exception ex){
			return null;
		}
		if(!date.equals("")){
			List<Map<String, Object>> lmss1= reportFormsService.getRankWithOrgAndTimeSpan(date.split("~")[0], date.split("~")[1], blBranch, roleCode);
			boolean istop10=false;
			for(int i=0;i<lmss1.size();i++){
				Map<String,Object> mss=lmss1.get(i);
				if(mss.get("user_id").equals(userId)){
					istop10=true;
					urv.setUpOrDown(i+1-urv.getRank());  //正整数表示上升，负整数表示下降，0较上期没有变化
				}
			}
			if(!istop10) urv.setUpOrDown(1); //1是虚数，表示上升，由于前一期排名不在前10名
		}else{
			urv.setUpOrDown(0);
		}
		return urv;
	}

	@RequestMapping(value = "/getLastRank", method =  RequestMethod.GET)
	@ResponseBody
	public int getLastRank(String token, String userId,String startDate,String endDate,String role,String[] orgIds) {
		List<Organization> lo=new ArrayList<Organization>();
		if(orgIds!=null && orgIds.length>0){
			for(String orgId : orgIds)
				lo.add(organizationDao.findById(orgId));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int result=0;
		RoleCode roleCode=null;
		if(role.equals("1")) roleCode=null;
		else if(role.equals("2")) roleCode=RoleCode.CONSULTOR;
		else if(role.equals("3")) roleCode=RoleCode.STUDY_MANAGER;
		else return -1;
		String date="";
		int days=DateTools.daysOfTwo(startDate,endDate);
		try{
			if(days>26 && days<31) date=sdf.format(DateTools.getLastMonthStart(startDate))+"~"+sdf.format(DateTools.getLastMonthEnd(startDate));
			else if(7==days) date=sdf.format(DateTools.getLastWeekStart(startDate))+"~"+sdf.format(DateTools.getLastWeekEnd(startDate));
			else if(0==days || 1==days) date=sdf.format(DateTools.getPre(startDate))+"~"+sdf.format(DateTools.getPre(startDate));
		}catch(Exception ex){
			result=-1;
		}
		if(!date.equals("")){
			List<Map<String, Object>> lmss1= reportFormsService.getRankWithOrgAndTimeSpanOrgs(date.split("~")[0], date.split("~")[1], lo, roleCode);
			Boolean isNotZero=false;
			for(int i=0;i<lmss1.size();i++){
				Map<String,Object> mss=lmss1.get(i);
				if(mss.get("user_id").equals(userId)){  
					isNotZero=true;
					result=i+1;
				}
			}
			if(!isNotZero) result=lmss1.size()+1;
		}else{
			result=-1;
		}
		return result; //-1表示参数错误，正数表示上期的排名。
	}
	
	public int getLastRankOf(String userId,String startDate,String endDate,String role,List<Organization> lo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int result=0;
		String date="";
		int days=DateTools.daysOfTwo(startDate,endDate);
		try{
			if(days>26 && days<31) date=sdf.format(DateTools.getLastMonthStart(startDate))+"~"+sdf.format(DateTools.getLastMonthEnd(startDate));
			else if(7==days) date=sdf.format(DateTools.getLastWeekStart(startDate))+"~"+sdf.format(DateTools.getLastWeekEnd(startDate));
			else if(0==days || 1==days) date=sdf.format(DateTools.getPre(startDate))+"~"+sdf.format(DateTools.getPre(startDate));
		}catch(Exception ex){
			result=-1;
		}
		if(!date.equals("")){
			List<Map<String, Object>> lmss1=null;
			if(role.equals("1")){
				lmss1= reportFormsService.getStudyManagerHoursOrgs(date.split("~")[0], date.split("~")[1], lo);
			}else if(role.equals("2")){
				lmss1= reportFormsService.getTeacherHoursOrgs(date.split("~")[0], date.split("~")[1], lo);
			}
			Boolean isNotZero=false;
			for(int i=0;i<lmss1.size();i++){
				Map<String,Object> mss=lmss1.get(i);
				if(mss.get("USER_ID").equals(userId)){  
					isNotZero=true;
					result=i+1;
				}
			}
			if(!isNotZero) result=lmss1.size()+1;
		}else{
			result=-1;
		}
		return result; //-1表示参数错误，正数表示上期的排名。
	}
	
	
	public int getLastRankOfByProductType(String userId,String productType,String startDate,String endDate,String role,List<Organization> lo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int result=0;
		String date="";
		int days=DateTools.daysOfTwo(startDate,endDate);
		try{
			if(days>26 && days<31) date=sdf.format(DateTools.getLastMonthStart(startDate))+"~"+sdf.format(DateTools.getLastMonthEnd(startDate));
			else if(7==days) date=sdf.format(DateTools.getLastWeekStart(startDate))+"~"+sdf.format(DateTools.getLastWeekEnd(startDate));
			else if(0==days || 1==days) date=sdf.format(DateTools.getPre(startDate))+"~"+sdf.format(DateTools.getPre(startDate));
		}catch(Exception ex){
			result=-1;
		}
		if(!date.equals("")){
			List<Map<String, Object>> lmss1=null;
			if(role.equals("1")){
				
				//区分产品类型
				if(productType.equals(ProductType.ONE_ON_ONE_COURSE.getValue())){
					lmss1= reportFormsService.getOneStudyManagerHoursOrgs(date.split("~")[0], date.split("~")[1], lo);
				}else if(productType.equals(ProductType.SMALL_CLASS.getValue())){
					lmss1= reportFormsService.getSmallClassStudyManagerHoursOrgs(date.split("~")[0], date.split("~")[1], lo);
				}else if(productType.equals(ProductType.OTHERS.getValue())) {
					lmss1 = reportFormsService.getOtherStudyManagerHoursOrgs(date.split("~")[0], date.split("~")[1], lo);
				}else if(productType.equals("TOTAL")){
					lmss1 = reportFormsService.getTotalStudyManagerHoursOrgs(date.split("~")[0], date.split("~")[1], lo);
				}
			}else if(role.equals("2")){
				//区分产品类型				
				if(productType.equals(ProductType.ONE_ON_ONE_COURSE.getValue())){
					lmss1 =reportFormsService.getOneTeacherHoursOrgs(date.split("~")[0], date.split("~")[1], lo);
				}else if(productType.equals(ProductType.SMALL_CLASS.getValue())){
					lmss1 = reportFormsService.getSmallClassTeacherHoursOrgs(date.split("~")[0], date.split("~")[1], lo);
				}else if(productType.equals(ProductType.OTHERS.getValue())) {
					lmss1 = reportFormsService.getOtherTeacherHoursOrgs(date.split("~")[0], date.split("~")[1], lo);
				}else if(productType.equals("TOTAL")){
					lmss1 = reportFormsService.getTotalTeacherHoursOrgs(date.split("~")[0], date.split("~")[1], lo);
				}
			}
			Boolean isNotZero=false;
			for(int i=0;i<lmss1.size();i++){
				Map<String,Object> mss=lmss1.get(i);
				if(mss.get("userId").equals(userId)){  
					isNotZero=true;
					result=i+1;
				}
			}
			if(!isNotZero) result=lmss1.size()+1;
		}else{
			result=-1;
		}
		return result; //-1表示参数错误，正数表示上期的排名。
	}
	
	
	@RequestMapping(value = "/getUserRankList", method =  RequestMethod.GET)
	@ResponseBody
	public List<UserAmountRankInfo> getUserRankList(String token,String startDate,String endDate,String role,String orgIds){
		TokenUtil.checkTokenWithException(token);
//		User user=TokenUtil.getTokenUser(token);
		List<UserAmountRankInfo> luri= staffBonusDayService.getRankWithOrgAndTimeSpanOrgs(startDate, endDate, orgIds, role);
		return luri;
	}
	
	@RequestMapping(value = "/getUserRankListOf", method =  RequestMethod.GET)
	@ResponseBody
	//public List<UserRankInfo> getUserRankListOf(String userId,String startDate,String endDate,String role,String[] orgIds){
	public List<UserRankInfo> getUserRankListOf(String token,String startDate,String endDate,String role,String[] orgIds){
		TokenUtil.checkTokenWithException(token);
		User user=TokenUtil.getTokenUser(token);
//		User user=userService.getCurrentLoginUser();
		//User user=userService.getUserById(userId);
		List<Organization> orgs=new ArrayList<Organization>();
		if(orgIds!=null){
			for(String orgId:orgIds)
			orgs.add(organizationDao.findById(orgId));
		}
		List<Map<String, Object>> lmss=null;
		if(role.equals("1")){
			lmss= reportFormsService.getStudyManagerHoursOrgs(startDate, endDate, orgs);
		} 
		else if(role.equals("2")){
			lmss=reportFormsService.getTeacherHoursOrgs(startDate, endDate, orgs);
		} else return null;
		List<UserRankInfo> luri=new ArrayList<UserRankInfo>();
		if(lmss.size()>=10){
			boolean isTopTen=false;
			for(int i=0;i<10;i++){
				Map<String,Object> mss=lmss.get(i);
				Object obj=mss.get("amount");
				String orgName=organizationDao.getOrgNameById(userService.findUserById((String)mss.get("USER_ID")).getOrganizationId());
				if(user.getUserId().equals(mss.get("USER_ID"))){
					isTopTen=true;
					BigDecimal amount=BigDecimal.ZERO;
					if(0!=i) {
						Object obj1=lmss.get(i-1).get("amount");
						Object obj2=lmss.get(i).get("amount");
						amount= new BigDecimal(obj1.toString()).subtract(new BigDecimal(obj2.toString()));	
					}
					//求上期的名次
					int lastRank=this.getLastRankOf(user.getUserId(), startDate, endDate, role, orgs);
					int upDown=0;
					if(lastRank!=-1) upDown=lastRank-(i+1);
					UserRankInfo uri=new UserRankInfo((String)mss.get("USER_ID"),orgName,(String)mss.get("orgid"),obj.toString(),(String)mss.get("username"),"1",(i+1)+"",amount.toString(),upDown+"");
					luri.add(uri);
				} else
					luri.add(new UserRankInfo((String)mss.get("USER_ID"),orgName,(String)mss.get("orgid"),obj.toString(),(String)mss.get("username"),(i+1)+""));
			}
			if(!isTopTen){ //不在前10名
				boolean isAmountNOZero=false;
				for(int i=10;i<lmss.size();i++){
					Map<String,Object> mss=lmss.get(i);
					Object obj=mss.get("amount");
					if(user.getUserId().equals(mss.get("USER_ID"))){
						isAmountNOZero=true;
						Object obj1=lmss.get(i-1).get("amount");
						Object obj2=lmss.get(i).get("amount");
						BigDecimal amount= new BigDecimal(obj1.toString()).subtract(new BigDecimal(obj2.toString()));	
						//求上期的名次
						int lastRank=this.getLastRankOf(user.getUserId(), startDate, endDate, role, orgs); 
						int upDown=lastRank-(i+1);
						String orgName=organizationDao.getOrgNameById(userService.findUserById((String)mss.get("USER_ID")).getOrganizationId());
						luri.add(new UserRankInfo((String)mss.get("USER_ID"),orgName,(String)mss.get("orgid"),obj.toString(),(String)mss.get("username"),"1",(i+1)+"",amount.toString(),upDown+""));
					}
				}
				if(!isAmountNOZero && lmss.size()!=0){   //没有业绩，与最后一名的业绩差就可以了
					int lastRank=this.getLastRankOf(user.getUserId(), startDate, endDate, role, orgs);
					int upDown=lastRank-lmss.size();
					UserRankInfo uri=new UserRankInfo();
					//user.getUserId(),organizationDao.getOrgNameById(user.getOrganizationId()),user.getOrganizationId(),"0",user.getName(),"1",(lmss.size()+1)+"",luri.get(lmss.size()-1).getAmount(),upDown+""
					//这里的orgId与 luri.add(new UserRankInfo(user.getUserId(),mss.get("orgName"),mss.get("orgid"),"0",user.getName(),"1",(lmss.size()+1)+"",luri.get(lmss.size()-1).getAmount(),upDown+""));
					uri.setAmount("0");
					uri.setUserId(user.getUserId());
					uri.setOrgName(organizationDao.getOrgNameById(user.getOrganizationId()));
					uri.setOrgId(user.getOrganizationId());
					uri.setUserName(user.getName());
					uri.setUpDown(upDown+"");
					uri.setRandId(lmss.size()+1+"");
					Object objTyy=lmss.get(lmss.size()-1).get("amount");
					uri.setBalance(objTyy.toString());
					uri.setIsMe("1");
					luri.add(uri);
				}
			}
		}else{
			boolean isTop=false;
			for(int i=0;i<lmss.size();i++){
				Map<String,Object> mss=lmss.get(i);
				Object obj=mss.get("amount");
				String orgName=organizationDao.getOrgNameById(userService.findUserById((String)mss.get("USER_ID")).getOrganizationId());
				if(user.getUserId().equals(mss.get("USER_ID"))){
					isTop=true;
					BigDecimal amount=BigDecimal.ZERO;
					if(0!=i) {
						Object obj1=lmss.get(i-1).get("amount");
						Object obj2=lmss.get(i).get("amount");
						amount= new BigDecimal(obj1.toString()).subtract(new BigDecimal(obj2.toString()));	
					}
					//求上期的名次
					int lastRank=this.getLastRankOf(user.getUserId(), startDate, endDate, role, orgs);
					int upDown=lastRank-(i+1);
					luri.add(new UserRankInfo((String)mss.get("USER_ID"),orgName,(String)mss.get("orgid"),obj.toString(),(String)mss.get("username"),"1",(i+1)+"",amount.toString(),upDown+""));
				}else 
					luri.add(new UserRankInfo((String)mss.get("USER_ID"),orgName,(String)mss.get("orgid"),obj.toString(),(String)mss.get("username"),(i+1)+""));
			}
			if(!isTop && lmss.size()!=0){
				//与最后一名的差距就可以了
				int lastRank=this.getLastRankOf(user.getUserId(), startDate, endDate, role, orgs);
				int upDown=lastRank-lmss.size();
				//这里的orgId与 luri.add(new UserRankInfo(user.getUserId(),mss.get("orgName"),mss.get("orgid"),"0",user.getName(),"1",(lmss.size()+1)+"",luri.get(lmss.size()-1).getAmount(),upDown+""));
				UserRankInfo uri=new UserRankInfo();
				//user.getUserId(),organizationDao.getOrgNameById(user.getOrganizationId()),user.getOrganizationId(),"0",user.getName(),"1",(lmss.size()+1)+"",luri.get(lmss.size()-1).getAmount(),upDown+""
				//这里的orgId与 luri.add(new UserRankInfo(user.getUserId(),mss.get("orgName"),mss.get("orgid"),"0",user.getName(),"1",(lmss.size()+1)+"",luri.get(lmss.size()-1).getAmount(),upDown+""));
				uri.setAmount("0");
				uri.setUserId(user.getUserId());
				uri.setOrgName(organizationDao.getOrgNameById(user.getOrganizationId()));
				uri.setOrgId(user.getOrganizationId());
				uri.setUserName(user.getName());
				uri.setUpDown(upDown+"");
				uri.setRandId(lmss.size()+1+"");
				Object objTcc=lmss.get(lmss.size()-1).get("amount");
				uri.setBalance(objTcc.toString());
				uri.setIsMe("1");
				luri.add(uri);
			}
		}
		if(luri==null || luri.size()==0){
			UserRankInfo uri=new UserRankInfo();
			uri.setAmount("0");
			uri.setUserId(user.getUserId());
			uri.setOrgName(organizationDao.getOrgNameById(user.getOrganizationId()));
			uri.setOrgId(user.getOrganizationId());
			uri.setUserName(user.getName());
			uri.setUpDown(0+"");
			uri.setRandId(lmss.size()+1+"");
			uri.setBalance("0");
			uri.setIsMe("1");
			luri.add(uri);
		}
		return luri;
	}
	
	@RequestMapping(value = "/getUserRankListOfByType", method =  RequestMethod.GET)
	@ResponseBody
	public List<UserRankInfo> getUserRankListOfByProductType(String token,String productType,String startDate,String endDate,String role,String[] orgIds){
		TokenUtil.checkTokenWithException(token);
		User user=TokenUtil.getTokenUser(token);
//		User user=userService.getCurrentLoginUser();
		//User user=userService.getUserById(userId);
		List<Organization> orgs=new ArrayList<Organization>();
		if(orgIds!=null){
			for(String orgId:orgIds)
			orgs.add(organizationDao.findById(orgId));
		}
		List<Map<String, Object>> lmss=null;
		if(StringUtil.isBlank(productType)){
			return null;
		}
		if(role.equals("1")){
			//区分产品类型
			if(productType.equals(ProductType.ONE_ON_ONE_COURSE.getValue())){
				lmss= reportFormsService.getOneStudyManagerHoursOrgs(startDate, endDate, orgs);
			}else if(productType.equals(ProductType.SMALL_CLASS.getValue())){
				lmss= reportFormsService.getSmallClassStudyManagerHoursOrgs(startDate, endDate, orgs);
			}else if(productType.equals(ProductType.OTHERS.getValue())) {
				lmss = reportFormsService.getOtherStudyManagerHoursOrgs(startDate, endDate, orgs);
			}else if(productType.equals("TOTAL")){
				lmss = reportFormsService.getTotalStudyManagerHoursOrgs(startDate, endDate, orgs);
			}	
		} 
		else if(role.equals("2")){
			//区分产品类型
			if(productType.equals(ProductType.ONE_ON_ONE_COURSE.getValue())){
				lmss=reportFormsService.getOneTeacherHoursOrgs(startDate, endDate, orgs);
			}else if(productType.equals(ProductType.SMALL_CLASS.getValue())){
				lmss= reportFormsService.getSmallClassTeacherHoursOrgs(startDate, endDate, orgs);
			}else if(productType.equals(ProductType.OTHERS.getValue())) {
				lmss = reportFormsService.getOtherTeacherHoursOrgs(startDate, endDate, orgs);
			}else if(productType.equals("TOTAL")){
				lmss = reportFormsService.getTotalTeacherHoursOrgs(startDate, endDate, orgs);
			}
			
		} else return null;
		List<UserRankInfo> luri=new ArrayList<UserRankInfo>();
		if(lmss.size()>=10){
			boolean isTopTen=false;
			for(int i=0;i<10;i++){
				Map<String,Object> mss=lmss.get(i);
				Object obj=mss.get("amount");
				String orgName=organizationDao.getOrgNameById(userService.findUserById((String)mss.get("userId")).getOrganizationId());
				if(user.getUserId().equals(mss.get("userId"))){
					isTopTen=true;
					BigDecimal amount=BigDecimal.ZERO;
					if(0!=i) {
						Object obj1=lmss.get(i-1).get("amount");
						Object obj2=lmss.get(i).get("amount");
						amount= new BigDecimal(obj1.toString()).subtract(new BigDecimal(obj2.toString()));	
					}
					//求上期的名次
					int lastRank=this.getLastRankOfByProductType(user.getUserId(),productType,startDate, endDate, role, orgs);
					int upDown=0;
					if(lastRank!=-1) upDown=lastRank-(i+1);
					UserRankInfo uri=new UserRankInfo((String)mss.get("userId"),orgName,(String)mss.get("orgid"),obj.toString(),(String)mss.get("userName"),"1",(i+1)+"",amount.toString(),upDown+"");
					luri.add(uri);
				} else
					luri.add(new UserRankInfo((String)mss.get("userId"),orgName,(String)mss.get("orgid"),obj.toString(),(String)mss.get("userName"),(i+1)+""));
			}
			if(!isTopTen){ //不在前10名
				boolean isAmountNOZero=false;
				for(int i=10;i<lmss.size();i++){
					Map<String,Object> mss=lmss.get(i);
					Object obj=mss.get("amount");
					if(user.getUserId().equals(mss.get("userId"))){
						isAmountNOZero=true;
						Object obj1=lmss.get(i-1).get("amount");
						Object obj2=lmss.get(i).get("amount");
						BigDecimal amount= new BigDecimal(obj1.toString()).subtract(new BigDecimal(obj2.toString()));	
						//求上期的名次
						int lastRank=this.getLastRankOfByProductType(user.getUserId(),productType,startDate, endDate, role, orgs);
						int upDown=lastRank-(i+1);
						String orgName=organizationDao.getOrgNameById(userService.findUserById((String)mss.get("userId")).getOrganizationId());
						luri.add(new UserRankInfo((String)mss.get("userId"),orgName,(String)mss.get("orgid"),obj.toString(),(String)mss.get("userName"),"1",(i+1)+"",amount.toString(),upDown+""));
					}
				}
				if(!isAmountNOZero && lmss.size()!=0){   //没有业绩，与最后一名的业绩差就可以了
					int lastRank=this.getLastRankOfByProductType(user.getUserId(),productType,startDate, endDate, role, orgs);
					int upDown=lastRank-lmss.size();
					UserRankInfo uri=new UserRankInfo();
					//user.getUserId(),organizationDao.getOrgNameById(user.getOrganizationId()),user.getOrganizationId(),"0",user.getName(),"1",(lmss.size()+1)+"",luri.get(lmss.size()-1).getAmount(),upDown+""
					//这里的orgId与 luri.add(new UserRankInfo(user.getUserId(),mss.get("orgName"),mss.get("orgid"),"0",user.getName(),"1",(lmss.size()+1)+"",luri.get(lmss.size()-1).getAmount(),upDown+""));
					uri.setAmount("0");
					uri.setUserId(user.getUserId());
					uri.setOrgName(organizationDao.getOrgNameById(user.getOrganizationId()));
					uri.setOrgId(user.getOrganizationId());
					uri.setUserName(user.getName());
					uri.setUpDown(upDown+"");
					uri.setRandId(lmss.size()+1+"");
					Object objTyy=lmss.get(lmss.size()-1).get("amount");
					uri.setBalance(objTyy.toString());
					uri.setIsMe("1");
					luri.add(uri);
				}
			}
		}else{
			boolean isTop=false;
			for(int i=0;i<lmss.size();i++){
				Map<String,Object> mss=lmss.get(i);
				Object obj=mss.get("amount");
				String orgName=organizationDao.getOrgNameById(userService.findUserById((String)mss.get("userId")).getOrganizationId());
				if(user.getUserId().equals(mss.get("userId"))){
					isTop=true;
					BigDecimal amount=BigDecimal.ZERO;
					if(0!=i) {
						Object obj1=lmss.get(i-1).get("amount");
						Object obj2=lmss.get(i).get("amount");
						amount= new BigDecimal(obj1.toString()).subtract(new BigDecimal(obj2.toString()));	
					}
					//求上期的名次
					int lastRank=this.getLastRankOfByProductType(user.getUserId(),productType,startDate, endDate, role, orgs);
					int upDown=lastRank-(i+1);
					luri.add(new UserRankInfo((String)mss.get("userId"),orgName,(String)mss.get("orgid"),obj.toString(),(String)mss.get("userName"),"1",(i+1)+"",amount.toString(),upDown+""));
				}else 
					luri.add(new UserRankInfo((String)mss.get("userId"),orgName,(String)mss.get("orgid"),obj.toString(),(String)mss.get("userName"),(i+1)+""));
			}
			if(!isTop && lmss.size()!=0){
				//与最后一名的差距就可以了
				int lastRank=this.getLastRankOfByProductType(user.getUserId(),productType,startDate, endDate, role, orgs);
				int upDown=lastRank-lmss.size();
				//这里的orgId与 luri.add(new UserRankInfo(user.getUserId(),mss.get("orgName"),mss.get("orgid"),"0",user.getName(),"1",(lmss.size()+1)+"",luri.get(lmss.size()-1).getAmount(),upDown+""));
				UserRankInfo uri=new UserRankInfo();
				//user.getUserId(),organizationDao.getOrgNameById(user.getOrganizationId()),user.getOrganizationId(),"0",user.getName(),"1",(lmss.size()+1)+"",luri.get(lmss.size()-1).getAmount(),upDown+""
				//这里的orgId与 luri.add(new UserRankInfo(user.getUserId(),mss.get("orgName"),mss.get("orgid"),"0",user.getName(),"1",(lmss.size()+1)+"",luri.get(lmss.size()-1).getAmount(),upDown+""));
				uri.setAmount("0");
				uri.setUserId(user.getUserId());
				uri.setOrgName(organizationDao.getOrgNameById(user.getOrganizationId()));
				uri.setOrgId(user.getOrganizationId());
				uri.setUserName(user.getName());
				uri.setUpDown(upDown+"");
				uri.setRandId(lmss.size()+1+"");
				Object objTcc=lmss.get(lmss.size()-1).get("amount");
				uri.setBalance(objTcc.toString());
				uri.setIsMe("1");
				luri.add(uri);
			}
		}
		if(luri==null || luri.size()==0){
			UserRankInfo uri=new UserRankInfo();
			uri.setAmount("0");
			uri.setUserId(user.getUserId());
			uri.setOrgName(organizationDao.getOrgNameById(user.getOrganizationId()));
			uri.setOrgId(user.getOrganizationId());
			uri.setUserName(user.getName());
			uri.setUpDown(0+"");
			uri.setRandId(lmss.size()+1+"");
			uri.setBalance("0");
			uri.setIsMe("1");
			luri.add(uri);
		}
		return luri;
	}
	
	
	
	
	
	@RequestMapping(value = "/getUserOrganizationList", method =  RequestMethod.GET)
	@ResponseBody
	public List<OrganizationMobileSimpleVo> getUserOrganizationList(String token){
	//public List<OrganizationMobileSimpleVo> getUserOrganizationList(String userId){
		TokenUtil.checkTokenWithException(token);
		User user=userService.getCurrentLoginUser();
		if(user!=null){
			List<OrganizationMobileSimpleVo> omsl=new ArrayList<OrganizationMobileSimpleVo>();
			List<Organization> orgList=user.getOrganization();
			List<Organization> OList=new ArrayList<Organization>();
			List<OrganizationType> lot=new ArrayList<OrganizationType>();
			//lot.add(OrganizationType.GROUNP);
			lot.add(OrganizationType.BRENCH);
			for(Organization org:orgList){
				if(org.getOrgType().equals(OrganizationType.BRENCH)){
					if(!OList.contains(org)) OList.add(org);
				}else if(org.getOrgType().equals(OrganizationType.GROUNP )){
					//检索出所有分公司
					OList=commonService.getOrganizatonByOrgIdAndType(lot,org.getId());
					break;
				}else{
					//上级中可能有分公司，也可能上级中没有分公司而是集团
//					while((!org.getOrgType().equals(OrganizationType.BRENCH)) || (!org.getOrgType().equals(OrganizationType.GROUNP))){
//						org=organizationDao.findById(org.getParentId());
//					}
					if(organizationDao.findById(org.getParentId()).getOrgType().equals(OrganizationType.GROUNP)){
						OList= commonService.getOrganizatonByOrgIdAndType(lot,org.getId());
						break;
					}else{
						while(!org.getOrgType().equals(OrganizationType.BRENCH)){
							org=organizationDao.findById(org.getParentId());
						}
						if(!OList.contains(org)) OList.add(org);
					}
				}
			}
			for(Organization org : OList){
				OrganizationMobileSimpleVo omsv=new OrganizationMobileSimpleVo(org.getId(),org.getName(),org.getOrgType().getName());
				boolean contains=false;
				for(OrganizationMobileSimpleVo oo : omsl){
					if(oo.getOrgId().equals(omsv.getOrgId())){
						contains=true;
						break;
					}
				}
				if(!contains) 
					omsl.add(new OrganizationMobileSimpleVo(org.getId(),org.getName(),org.getOrgType().getName()));
			}
			return omsl;
		}
		else return null;
	}

	
	/**
	 * 废弃 2017-01-11
	 * 保存或修改个人工作日程
	 */
	@Deprecated
	@RequestMapping(value ="/editPersonWorkScheduleRecord")
    @ResponseBody
    public Response editPersonWorkScheduleRecord(@RequestParam String token,@ModelAttribute GridRequest gridRequest, @ModelAttribute PersonWorkScheduleRecord record) throws IOException, IllegalAccessException {
		TokenUtil.checkTokenWithException(token);
//		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
//    		personWorkService.deletePersonWorkScheduleRecord(record);
//    	} else {
//    		personWorkService.saveEditPersonWorkScheduleRecord(record);
//    	}
    	return new Response(-1, "请更新最新版app再使用");
    }
	
	 /**
	  * 废弃 2017-01-11
	 * 根据id查询个人工作日程
	 * @param id
	 * @return
	 */
	 @Deprecated
	   @RequestMapping(value ="/findPersonWorkScheduleRecordById")
	   @ResponseBody
	   public PersonWorkScheduleRecord findPersonWorkScheduleRecordById(@RequestParam String token,@RequestParam String id) throws IOException {
		   TokenUtil.checkTokenWithException(token);
//		   return personWorkService.findPersonWorkScheduleRecordById(id);
		   return null;
	   }
	
   /**
	 * 获取登录人当天的工作日程个数,
	 * 系统消息未读数
	 */
   @RequestMapping(value ="/getTodayWorkNumberByUserId")
   @ResponseBody   
	public CountVo getTodayWorkNumberByUserId(@RequestParam String token,String userId,@RequestParam String workDate){
	   TokenUtil.checkTokenWithException(token);
	   CountVo countvo=new CountVo();
	   if(StringUtils.isEmpty(userId)){
	   		return countvo;
	   }
	   int systemNotRead=sentRecordService.getNotReadCountByUserId(userId);
	   countvo.setSystemNotReadNum(systemNotRead);
	   CourseAttendaceStatusCountVo vo = courseService.getCourseAttendaceStatusCount();
	   int courseUntreatedCount = vo.getNewCount() + vo.getStudyManagerAuditedCount() + vo.getTeacherAttendanceCount();
	   countvo.setCourseUntreatedCount(courseUntreatedCount);
	   Response response = customerService.getUserCustomerCount();
	   int stayFollowCount = 0;
	   int tobeassigned = 0;
	   
	   if(response.getResultCode() == 400) {
		   countvo.setCustomerUntreatedCount(0);
	   }else {
		   Map<String, Object> result = (Map<String, Object>) response.getData();
		   stayFollowCount = (int) result.get("STAY_FOLLOW");
		   tobeassigned = (int) result.get("TOBEASSIGNED");
	   }
	   countvo.setCustomerUntreatedCount(stayFollowCount+tobeassigned);
	   return countvo;
	}
   
    @RequestMapping(value = "/checkHasOtmClassForPeriodDate")
    @ResponseBody
    public List<HasClassCourseVo> checkHasOtmClassForPeriodDate(@RequestParam String token,@RequestParam String teacherId, String startDate,String endDate){
    	if (StringUtil.isBlank(startDate)) {
    		startDate = DateTools.getCurrentDate();
    	}
    	if (StringUtil.isBlank(endDate)) {
    		endDate = DateTools.getCurrentDate();
    	}
    	TokenUtil.checkTokenWithException(token);
	   	List<HasClassCourseVo> listHasOtmClass = otmClassService.checkHasOtmClassForPeriodDate(teacherId, startDate, endDate);
	   	return listHasOtmClass;
    }
    
    /**
     * 一对多课程列表
     * @param token
     * @param dp
     * @param miniClassCourseVo
     * @return
     */
	@RequestMapping(value ="/getOtmClassCourseListNew")
	@ResponseBody
	public DataPackage getOtmClassCourseListNew(@RequestParam String token, @ModelAttribute DataPackage dp, @ModelAttribute OtmClassCourseVo otmClassCourseVo) {
		TokenUtil.checkTokenWithException(token);
		return dp = otmClassService.getOtmClassCourseListForMobile(otmClassCourseVo, dp);		
	}
	
	/**
	 * 上传 一对多的 考勤 签名图片
	 * @param token
	 * @param otmClassCourseId
	 * @param attendancePicFile
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/submitOtmClassAttendancePic", method = RequestMethod.POST)
	@ResponseBody
	public Response submitOtmClassAttendancePic(@RequestParam String token, @RequestParam String otmClassCourseId, @RequestParam("attendancePic") MultipartFile attendancePicFile,HttpServletRequest request) throws Exception {
		TokenUtil.checkTokenWithException(token);
		String servicePath = request.getSession().getServletContext().getRealPath("/");
		
		
		OtmClassCourse otmClassCourse = otmClassService.getOtmClassCourseById(otmClassCourseId);
		if(otmClassCourse !=null) {
			// 统一使用 文件名 来 标明签名文件
			//String fileName = UUID.randomUUID().toString();
			String fileName =  String.format("OTM_CLASS_ATTEND_PIC_%s.jpg", otmClassCourseId);
			otmClassCourse.setAttendacePicName(fileName);
			otmClassService.updateOtmClassCourse(otmClassCourse);
			
			
			String folder=servicePath+PropertiesUtils.getStringValue("save_file_path");//系统路径
			FileUtil.isNewFolder(folder);			 
			
			String bigFileName = "BIG_"+fileName;//阿里云上面的文件名 大   默认是JPG 
			String midFileName = "MID_"+fileName;//阿里云上面的文件名 中
			String smallFileName = "SMALL_"+fileName;//阿里云上面的文件名 小					 
			
			String relFileName=folder+"/realFile_"+otmClassCourseId+UUID.randomUUID().toString()+".jpg";
			File realFile=new File(relFileName);
			File bigFile=new File(folder+"/"+bigFileName);
			File midFile=new File(folder+"/"+midFileName);
			File smallFile=new File(folder+"/"+smallFileName);			
			
			try {				
				attendancePicFile.transferTo(realFile);
				ImageSizer.compressImage(relFileName, smallFile.getAbsolutePath(), 60);//转换图片大小
				ImageSizer.compressImage(relFileName, midFile.getAbsolutePath(), 200);//转换图片大小
				ImageSizer.compressImage(relFileName, bigFile.getAbsolutePath(), 600);//转换图片大小
				AliyunOSSUtils.put(bigFileName, bigFile);//传到阿里云
				AliyunOSSUtils.put(midFileName, midFile);//传到阿里云
				AliyunOSSUtils.put(smallFileName, smallFile);//传到阿里云				
				AliyunOSSUtils.put(fileName, realFile);//传原图到阿里云				
			} catch (IllegalStateException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}finally{				
				bigFile.delete();
				midFile.delete();
				smallFile.delete();
				realFile.delete();
			}			
		}
		
		//otmClassService.saveOtmClassAttendancePic(otmClassCourseId, attendancePicFile,servicePath);
		return new Response();
	}



	/**
     * 更新班组学生考勤信息
     * @param token
     * @param otmClassCourseId
     * @param attendanceData
     * @param oprationCode
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/modifyOtmClassCourseStudentAttendance")
	@ResponseBody
	public Response modifyOtmClassCourseStudentAttendance(@RequestParam String token, @RequestParam String otmClassCourseId, @RequestParam String attendanceData, @RequestParam String oprationCode) throws Exception{
		TokenUtil.checkTokenWithException(token);
		return otmClassService.modifyOtmClassCourseStudentAttendance(otmClassCourseId, attendanceData, oprationCode);
	}
	
	/**
     * 获取班组学生考勤列表
     * @param token
     * @param dp
     * @param otmClassStudentAttendentVo
     * @return
     * @throws Exception
     */
	@RequestMapping(value ="/getOtmClassStudentAttendentList")
	@ResponseBody
	public List<OtmClassStudentVo> getOtmClassStudentAttendentList(@RequestParam String token,@ModelAttribute OtmClassStudentAttendentVo otmClassStudentAttendentVo, @RequestParam String oprationCode) throws Exception {
		TokenUtil.checkTokenWithException(token);
		DataPackage dp = new DataPackage(0, 1000);		
		List<OtmClassStudentVo> otmClassStudentVoList = (List<OtmClassStudentVo>) otmClassService.getOtmClassStudentAttendentList(otmClassStudentAttendentVo, oprationCode, dp).getDatas();
		return otmClassStudentVoList;
	}
	/**
     * 一对多 获取的课表详情
     * @param token
     * @param dp
     * @param otmClassStudentAttendentVo
     * @return
     * @throws Exception
     */
	@RequestMapping(value ="/findOtmClassCourseById")
	@ResponseBody
	public OtmClassCourseVo findOtmClassCourseById(@RequestParam String token, @ModelAttribute OtmClassCourseVo otmClassCourseVo) throws Exception {
		TokenUtil.checkTokenWithException(token);
		return otmClassService.findOtmClassCourseById(otmClassCourseVo);
	}

	/**
	 * 向传入的用户id 在mobileUserIds内的mobileUser推送指定消息
	 * @param ids
	 * @param type
	 * @param newsId
	 * @return
	 */
	@RequestMapping(value ="/pushMsgToMobileByUserIds")
	@ResponseBody
	public Response pushMsgToMobileByUserIds(@RequestParam String token,@RequestParam String userIds,@RequestParam String type,
			@RequestParam String newsId,@RequestParam String msgContent,@RequestParam String time){
		TokenUtil.checkTokenWithException(token);
		mobilePushMsgService.pushMsgToMobileByUserIds(userIds, type, newsId, msgContent,time);
		return new Response();
	} 

	
	/**
	 * 用于PC首页跳转佳学页面使用
	 * @param token
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/putSessionAndUserId")
	@ResponseBody		
	public String putSessionAndUserId(@RequestParam String token,@RequestParam String userId){
		return TokenUtil.putSessionAndUserId(token,userId);
	}

	/**
	 * app我签单的学生
	 */
	@RequestMapping(value ="/getContractStudents")
	@ResponseBody
	public List<Map<Object,Object>> getContractStudents(@RequestParam String token,StudentVo studentVo,int pageNo,int pageSize){
		TokenUtil.checkTokenWithException(token);
		DataPackage dp=new DataPackage(pageNo,pageSize);
		dp.setSidx("con.CREATE_TIME");
		dp.setSord("desc");
		return customerService.getAppAllContractCustomer(studentVo, dp);
		
	}

	

	/**
	 * app今日跟进，今日预约
	 */
	@RequestMapping(value ="/getFollowingTodayThings")
	@ResponseBody
	public List<CustomerFolowupVo> getFollowingTodayThings(@RequestParam String token,CustomerFolowupVo vo,int pageNo,int pageSize){
		TokenUtil.checkTokenWithException(token);
		DataPackage dp=new DataPackage(pageNo,pageSize);
		dp.setSidx("meetingTime ");
		dp.setSord("desc");
		String createUserId=userService.getCurrentLoginUser().getUserId();
		vo.setCreateUserId(createUserId);
		dp = customerService.gtCustomerFollowingRecords(vo,dp);
		List<CustomerFolowupVo> volist =(List<CustomerFolowupVo>) dp.getDatas();
		return volist;
		
	}		
	

	/***************************************************APP客户管理接口区域Start************************************************************************/
	/**
	 * APP用来无效或者释放客户的接口
	 * @param cusId
	 * @param dealStatus   
	 * @return
	 */
	@RequestMapping(value = "/deliverCustomer", method =  RequestMethod.GET)
	@ResponseBody
	public Response deliverCustomer(@RequestParam String token,@RequestParam String cusId, @RequestParam CustomerDealStatus dealStatus) {
		TokenUtil.checkTokenWithException(token);
		
		String userId=userService.getCurrentLoginUser().getUserId();
		Customer customer = customerService.findById(cusId);
		
		customer.setDealStatus(dealStatus);
		customer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
		
		//当前组织架构                  
		Organization rrrpool = userService.getCurrentLoginUserOrganization();
	   if(rrrpool!=null){
			//当前用户的资源池
			ResourcePool repool2 = resourcePoolService.findResourcePoolById(rrrpool.getId());
			if(repool2!=null && repool2.getStatus().equals(ValidStatus.VALID)){//有资源池就设置为当前这个资源池
				customer.setDeliverTarget(rrrpool.getId());
			}else{//没有就先查所属再查父类，如果父类都找不到就gameOver了
				getBelongResourcePool(customer,rrrpool);
			}
		}
	   customer.setModifyTime(DateTools.getCurrentDateTime());
	   
		customerService.saveOrUpdateCustomer(customer);
		
		String description="释放客户-操作人:"+userService.getCurrentLoginUser().getName();
		
		if(CustomerDealStatus.INVALID == dealStatus ){
			description ="无效客户-操作人:"+userService.getCurrentLoginUser().getName();
			customerEventService.saveCustomerDynamicStatus(cusId, CustomerEventType.INVALID, description);
		}else{
			customerEventService.saveCustomerDynamicStatus(cusId, CustomerEventType.DELIVER, description);
		}
		
		return new Response();
		
	}
		
	/**
	 * 处理当前用户最接近的资源池
	 * @param customer
	 * @param rrrpool
	 */
	public void getBelongResourcePool(Customer customer,Organization rrrpool){
		ResourcePool repool2 = resourcePoolService.findResourcePoolById(rrrpool.getBelong());
		if(repool2==null){//所属没有资源池的话就找父类。
			ResourcePool repool3 = resourcePoolService.findResourcePoolById(rrrpool.getParentId());
			if(repool3!=null  && repool3.getStatus().equals(ValidStatus.VALID)){
				customer.setDeliverTarget(rrrpool.getParentId());
			}
		}else if(repool2!=null  && repool2.getStatus().equals(ValidStatus.VALID)){
			customer.setDeliverTarget(rrrpool.getBelong());
		}
	}
	
	
	
	/**
	 * 获取当前用户组织架构下的校区主任
	 */
	@RequestMapping(value = "/getCurrentSchoolManager", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getCurrentSchoolManager(@RequestParameters String token) {
		TokenUtil.checkTokenWithException(token);
		User user=TokenUtil.getTokenUser(token);
		
		SelectOptionResponse selectOptionResponse = null;
		if(user!=null && user.getOrganizationId()!=null){
	        List<User> users = null;
			try {
	
				users =userService.getUserByRoldCodesAndOrgId2(user.getOrganizationId());
	
				List<NameValue> nvs = new ArrayList<NameValue>();
				if(users!=null){
					nvs.addAll(users);
				}
				selectOptionResponse = new SelectOptionResponse(nvs);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return selectOptionResponse;
	}
	
	
	@RequestMapping(value = "/saveOrUpdateCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveOrUpdateCustomer(@RequestParameters String token, @ModelAttribute CustomerVo cus,String  studentJsonInfo) throws Exception {
		TokenUtil.checkTokenWithException(token);
		List<CusFollowStuVo> rtn=new ArrayList<CusFollowStuVo>();
		try{
			 rtn= new Gson().fromJson(studentJsonInfo,new TypeToken<List<CusFollowStuVo>>(){}.getType());
		}catch(Exception e){
			return new Response(1,"学生信息解析失败！");
		}
//        for(CusFollowStuVo person : rtn){  
//            System.out.println(person.getId()+","+person.getName());  
//        } 
        
		if(rtn!=null)
        cus.setCusfollowStudent(rtn);
        
		if (cus == null) {
			throw new ApplicationException(ErrorCode.RES_NOT_FOUND);
		}
		
		if(StringUtils.isNotBlank(cus.getId())){//APP 修改客户信息，只有少部分字段会修改，不调用PC端的方法
				return customerService.updateNormalCustomerForApp(cus); 
		}else{
			 customerService.saveOrUpdateCustomer(cus); 
		}
			 
		return new Response();
	}
	
	
	
	@RequestMapping(value = "/getSelectOptionByParentId", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getSelectOptionByParentId(@RequestParam String parentId){
		SelectOptionResponse selectOptionResponse = null;
		List<NameValue> nvs = new ArrayList<NameValue>();
			List<DataDict> datas =commonService.getDataDictByParentId(parentId);
			if(datas!=null)
				for (DataDict data : datas) {
					nvs.add(SelectOptionResponse.buildNameValue(data.getName(), data.getId()));
				}
			selectOptionResponse = new SelectOptionResponse(nvs);
		
		return selectOptionResponse;
	}
	
	
	/***************************************************APP客户管理接口区域End************************************************************************/


	
   /**
     * app我跟进的客户资源
     */
	@RequestMapping(value ="/getFollowCustomer")
	@ResponseBody
	public List<AppCustomerVo> getFollowCustomer(@RequestParam String token,CustomerVo customerVo,int pageNo,int pageSize){
		TokenUtil.checkTokenWithException(token);
		DataPackage dp=new DataPackage(pageNo,pageSize);
		dp.setSidx("modifyTime");
		dp.setSord("desc");
		String userId=userService.getCurrentLoginUser().getUserId();
		customerVo.setDeliverTarget(userId);
		customerVo.setDealStatus(CustomerDealStatus.FOLLOWING);
		customerVo.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
		dp = customerService.getCustomers(customerVo, dp, false);		
		List<AppCustomerVo> volist = new ArrayList<AppCustomerVo>();
		List<CustomerVo> customerList=(List<CustomerVo>) dp.getDatas();
		if(customerList==null)return volist;
		for(CustomerVo vo:customerList){
			AppCustomerVo appVo=new AppCustomerVo();
			appVo.setContact(vo.getContact());
			appVo.setCusOrg(vo.getCusOrg());
			appVo.setCusOrgName(vo.getCusOrgName());
			appVo.setCusType(vo.getCusType());
			appVo.setCusTypeName(vo.getCusTypeName());
			appVo.setId(vo.getCusId());
			appVo.setName(vo.getName());
			appVo.setIntentionOfTheCustomerId(vo.getIntentionOfTheCustomerId());
			appVo.setIntentionOfTheCustomerName(vo.getIntentionOfTheCustomerName());
			appVo.setRemark(vo.getRemark());
			appVo.setResEntranceId(vo.getResEntranceId());
			appVo.setResEntranceName(vo.getResEntranceName());
			volist.add(appVo);
		}
		return volist;
		
	}
	
	
	
	/**
	 * 阅读发送的系统信息
	 * @param token
	 * @param recordId
	 * @return
	 */
	@RequestMapping(value = "/readSentRecord")
	@ResponseBody
	public Response readSentRecord(@RequestParam String token, @RequestParam String recordIds) {
		TokenUtil.checkTokenWithException(token);
		return sentRecordService.readSentRecord(recordIds);
	}
	
	/**
	 * 发送系统信息
	 * @param token
	 * @param sentRecordVo
	 * @return
	 */
	@RequestMapping(value = "/sendRecord")
	@ResponseBody
	public Response sendRecord(@ModelAttribute SentRecordVo sentRecordVo) {
//		TokenUtil.checkTokenWithException(token);
	    if (StringUtil.isNotBlank(sentRecordVo.getEmployeeNo())) {
	        User user = userService.findUserByEmployeeNo(sentRecordVo.getEmployeeNo());
	        if (user != null) {
	            sentRecordVo.setMsgRecipientId(user.getUserId());
	        } else {
	            return new Response();
	        }
	    }
	    String msgContent;
        try {
            msgContent = new String(URLDecoder.decode(sentRecordVo.getMsgContent(), "utf-8"));
            sentRecordVo.setMsgContent(msgContent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }       
        return sentRecordService.sendRecord(sentRecordVo);
	}
	
	/**
	 * 根据用户id查询查询未读信息
	 */
	@RequestMapping(value="/getSystemNotRead")
	@ResponseBody
	public List<SentRecordVo> getSystemNotRead(@RequestParam String token, int pageNo, int pageSize,
			@RequestParam String userId){
		TokenUtil.checkTokenWithException(token);
		DataPackage dp=new DataPackage(pageNo,pageSize);
		dp=sentRecordService.getNotReadByUserId(userId, dp);
		List<SentRecordVo> volist=(List<SentRecordVo>)dp.getDatas();
		return volist;
	}
	
	/**
	 * 获取登录用户权限相关的信息 培训系统用
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value ="/getLoginUserProfileByParam", method = RequestMethod.GET)
	@ResponseBody
	public Profile getLoginUserProfileByParam(@RequestParam String userId, @RequestParam String token) {
		if(!TokenUtil.checkTokenUserIdWithException(token, userId))
		{
			SpringMvcUtils.getSession().invalidate();
			throw new ApplicationException("请先登录！");
		}
		User user = TokenUtil.getTokenUser(token);
		
		Profile response = new Profile();
		
		response.setUserId(user.getUserId());
		response.setUsername(user.getName());
		
		String imagePath = PropertiesUtils.getStringValue("oss.access.url.prefix")+"MOBILE_HEADER_BIG_"+user.getUserId()+".jpg";
        response.setAvatarUrl(imagePath);//头像路径
        
        //获取当前用户 所拥有的rolecode
		List<Role> currentUserRoles = user.getRole();
		List<String> lur=new ArrayList<String>();
		//本人所拥有的rolecode
		List<RoleCode> currentRoleCode = new ArrayList<RoleCode>(); //本人所拥有的rolecode
		if(currentUserRoles==null || currentUserRoles.size()==0){
			currentUserRoles=userService.getRoleByUserId(user.getUserId());	
		}
		if(currentUserRoles!=null && currentUserRoles.size()>0){
			for (Role role : currentUserRoles) {
				currentRoleCode.add(role.getRoleCode());
				if(role.getRoleCode()==null){
					log.info("RoleCode为空"+role.getRoleId()+" -- "+role.getName());
				}else{
					lur.add(StringUtils.isNotBlank(role.getRoleCode().getValue())? role.getRoleCode().getValue().toString():"");	
				}
			}	
		}
		response.setUserRole(lur);
		return response;
	}

	@RequestMapping(value ="/updateTop10Rank", method = RequestMethod.GET)
	@ResponseBody
	public void updateTop10Rank(String type) throws SQLException{
		if(StringUtils.isBlank(type) || (!type.equals("week") && !type.equals("month"))){
			throw new ApplicationException("你敢操作，打断腿！");
		}
		staffBonusDayService.updateTop10Rank(type);
	}
	
	/**
	 * 获取用户没读邮件信息
	 */
	@RequestMapping(value = "/getMailInfos", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackage getMailInfos(@RequestParam String userId, int pageSize) throws Exception {
		DataPackage dataPackage = new DataPackage(1, pageSize);
		if(mailService.isMailSysInUse() == true) {
			User user = userService.findUserById(userId);
			if (user == null) {
				throw new ApplicationException("不存在该用户");
			}
			MailBoxMsg mailBoxMsg = mailService.getMailCountInfo(user);
			int mboxMsgCnt = 0;
			int limit = pageSize;
			List<MailInfoView> newMailList = new ArrayList<MailInfoView>();
			if (null != mailBoxMsg) {
				mboxMsgCnt = mailBoxMsg.getMboxMsgCnt();
				if (mboxMsgCnt < limit) {
					limit = mboxMsgCnt;
				}
				newMailList = mailService.listMailInfos(mailBoxMsg.getMailAddr().toLowerCase(), 0, limit);
			}
			dataPackage.setPageSize(mboxMsgCnt);
			dataPackage.setDatas(newMailList);
			return dataPackage;
		} else {
			throw new ApplicationException("该用户没开通邮箱");
		}
	}
	
	/**
	 * 获取用户没读邮件信息
	 */
	@RequestMapping(value = "/getMailInfosForOA", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackage getMailInfosForOA(@RequestParam String account, int pageSize) throws Exception {
		DataPackage dataPackage = new DataPackage(1, pageSize);
		User user = userService.findUserByAccount(account);
		if (user == null) {
			throw new ApplicationException("不存在该用户");
		}
		MailBoxMsg mailBoxMsg = mailService.getMailCountInfo(user);
		int mboxMsgCnt = 0;
		int limit = pageSize;
		List<MailInfoView> newMailList = new ArrayList<MailInfoView>();
		if (null != mailBoxMsg) {
			mboxMsgCnt = mailBoxMsg.getMboxMsgCnt();
			if (mboxMsgCnt < limit) {
				limit = mboxMsgCnt;
			}
			newMailList = mailService.listMailInfos(mailBoxMsg.getMailAddr().toLowerCase(), 0, limit);
		}
		dataPackage.setPageSize(mboxMsgCnt);
		dataPackage.setDatas(newMailList);
		return dataPackage;
	}
	
	
	/**
	 * 获取邮箱自动登录信息 for OA
	 */
	@RequestMapping(value = "/getMailAutoLoginInfo")
	@ResponseBody
	public String getMailAutoLoginInfo(HttpServletRequest request, HttpServletResponse response){
		String account = request.getParameter("account");
		User user = userService.findUserByAccount(account);
		if(user != null) {
			String mailAddr = user.getMailAddr();
			if(StringUtils.isBlank(mailAddr)) {
                SystemConfig findSc = new SystemConfig();
                findSc.setTag("coreMailDomain");
                SystemConfig domainSc = systemConfigService.getSystemPath(findSc);
                mailAddr = user.getAccount() + "@" + domainSc.getValue();
            }
    		return mailService.getMailAutoLoginInfo(request, response, mailAddr, 0);
		} else {
			 throw new ApplicationException("没找到当前用户");
		}
	}
	
	
	
	/*@RequestMapping(value = "/getAllUserJobListOA",method = RequestMethod.GET,produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String getAllUserJobListOA(HttpServletRequest request,HttpServletResponse response){
//		TokenUtil.checkTokenWithException(token);
		response.setContentType("text/html;charset=utf-8");
		String callname=request.getParameter("callback");
		List<UserJob> uoList=userJobService.findAllUserJob();
		JSONArray jsonArray = JSONArray.fromObject(uoList);
		if(StringUtils.isNotBlank(callname)){
			return callname+" (" +jsonArray.toString()+ ")" ;
		}
		return jsonArray.toString();
		//return userJobService.findAllUserJob();
	}
	
	*//**
	 * 所有部门列表OA
	 *//*
	@RequestMapping(value = "/getAllMobileDeptListOA",method = RequestMethod.GET,produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String getAllMobileDeptListOA(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		String callname=request.getParameter("callback");
		List<Map<String, Object>> uoList=userService.getAllMobileDeptList();
		JSONArray jsonArray = JSONArray.fromObject(uoList);
		if(StringUtils.isNotBlank(callname)){
			return callname+" (" +jsonArray.toString()+ ")" ;
		}
		return jsonArray.toString();
	}*/
	
	
	/**
	 *删除部分支付为0的合同根据temp表
	* @return
	* @author  author :Yao 
	* @date  2016年8月12日 下午6:40:02 
	* @version 1.0 
	*/
	@RequestMapping(value = "/deleteSomeContract",method = RequestMethod.GET)
	@ResponseBody
	public Response deleteSomeContract(){
		initDataDeleteService.deleteSomeConract();
		return new Response();
	}
	
	
	/**
	 * 用来测试某些需要跑批执行的功能
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/updateSomethings",method = RequestMethod.GET)
	@ResponseBody
	public Response updateSomethings(String countDate) throws SQLException{
		schedulerCountService.updatePaymentReceiptUntilLastAudit();
		return new Response();
	}
	
	@RequestMapping(value = "/loginElearning",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> loginElearning(@RequestParam String code) throws IOException {

		Map<String,String> returnMap=new HashMap<String,String>();

		String url = PropertiesUtils.getStringValue("OAUTH_HOST")+"/oauth2/"+PropertiesUtils.getStringValue("ELE_PROJECT_NAME")+"/user_info/"+code;
		HttpClient client = wrapHttpClient();
		HttpGet getrequest = new HttpGet(url);
		String auth = "oauth2:" +  PropertiesUtils.getStringValue("ELE_KEY");
		byte[] encodedAuth = Base64.encode(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		getrequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		getrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		try {
			HttpResponse getResponse = client.execute(getrequest);
			if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = "";
				/**读取服务器返回过来的json字符串数据**/
				str = EntityUtils.toString(getResponse.getEntity());
				/**把json字符串转换成json对象**/
				JSONObject result = new JSONObject(str);
				boolean flag = result.getBoolean("success");
				if(flag)
				{
					JSONObject obj = result.getJSONObject("user_info");
					//从spring容器中获取UserDetailsService(这个从数据库根据用户名查询用户信息,及加载权限的service)
					UserDetailsService userDetailsService =
							(UserDetailsService) ApplicationContextUtil.getContext().getBean("userDetailsService");
					//根据用户名username加载userDetails
					UserDetailsImpl userDetails = (UserDetailsImpl)userDetailsService.loadUserByUsername(obj.getString("account"));
					userDetails.getUser().getUserId();
					returnMap.put("userId",userDetails.getUser().getUserId());
					returnMap.put("token",TokenUtil.genToken(userDetails.getUser()));
					return returnMap;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return returnMap;
	}

	@RequestMapping(value = "/saveTransferCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveTransferCustomer(@RequestParam String token,@ModelAttribute CustomerVo customerVo) throws Exception {
 
		TokenUtil.checkTokenWithException(token);
		
		
		
		if(StringUtils.isBlank(customerVo.getContact()))throw new ApplicationException("客户手机号码不能为空");
		//根据客户的电话号码查询该客户是否已经在待审核列表中
		if(transferCustomerService.getTransferRecordByContactAndStuName(customerVo.getContact(),customerVo.getPointialStuName()).size()>0){
			return new Response(-1,"该客户和学生已经存在待审核列表");
		}
		//老客户审核通过后要 添加被抢客户标记
		User user=userService.getCurrentLoginUser();
		
		
		Customer customer =HibernateUtils.voObjectMapping(customerVo, Customer.class);
		//CustomerVo customer_temp = customerService.findCustomerByPhone(customer.getContact());
		Customer customer_temp = customerService.loadCustomerByContact(customer.getContact());

		
		//传入的意向校区 必填项
		Organization intentionCampus = organizationService.findById(customerVo.getIntentionCampusId());
		
		//客户id
		String customerId ="";
		//学生id
		String studentId = "";
		//生成一条审核记录:
		TransferCustomerRecordVo tCustomerRecordVo =new TransferCustomerRecordVo();
		
		//主组织架构
		if(user.getOrganizationId()!=null){
			Organization organization = organizationService.findById(user.getOrganizationId());
			tCustomerRecordVo.setOrgLevel(organization.getOrgLevel());
		}else{
			return new Response(-1,"当前登录者所在组织架构有误");
		}
		
		//不再对新旧客户进行校验 20170713 执行新的规则    也不用判断新的6个月是否有跟进
		//如果是老客户
		//判断这个孩子是不是这个客户的老孩子，如果是 判断是否已经签单，如果已经签单，则不能登记，如果没签单，是否已经在审核记录里，如果在，则
        //则不能登记，如果不在 则可以登记  
		//如果不是老孩子，可以登记并且增加孩子 并且增加关联关联 
		//如果是新客户 可以登记  新增孩子 并且增加关联 新增审核记录
	
		//判断是否是老客户
		if(customer_temp!=null){
			//老客户
			try {
				//isOldChild isSign	isTransfer		
			   Map<String, Object> map = transferCustomerService.checkTransferCustomer(customer_temp, customerVo.getPointialStuName());
			   Boolean isOldChild = (Boolean)map.get("isOldChild");
			   Boolean isSign = (Boolean)map.get("isSign");
			   Boolean isFollowup = (Boolean)map.get("isFollowup");
			   Boolean isTransfer = (Boolean)map.get("isTransfer");
			   
			   if(isOldChild){
				   //如果是旧孩子 判断是否已经签单
				   if(isSign){
					   return new Response(-1,"该学生已经签合同，不能进行转介绍登记");
				   }else{
					   if(isFollowup){
						   return new Response(-1,"该学生已经有跟进过，不能进行转介绍登记");
					   }else{
						   if(isTransfer){
							   return new Response(-1,"该学生已经进行过转介绍登记，不能重复登记");
						   }						   
					   }
					 //可以登记
					 //获取这个旧孩子的id
					 studentId = (String)map.get("studentId");
					 if(StringUtil.isNotBlank(studentId) && StringUtil.isNotBlank(customerVo.getPointialStuGrade()) ){
						 Student student = studentService.getStduentById(studentId);
						 student.setGradeDict(new DataDict(customerVo.getPointialStuGrade()));
					 }
					
					 
					 //如果传入的年级不为空，则修改这个学生的年级
				   }
			   }else{			
				   //新孩子  增加孩子   绑定关系
				   StudentImportVo vo = new StudentImportVo();
				   vo.setName(customerVo.getPointialStuName());
				   if(StringUtil.isNotBlank(customerVo.getPointialStuGrade())){
					   vo.setGradeId(customerVo.getPointialStuGrade());
				   }				   
				   studentId = studentService.savePotentialStudent(vo, customer_temp.getId());
				   
			   }				
			} catch (ApplicationException e) {
				return new Response(-1,e.getMessage());
			}

			//标记为老客户
			tCustomerRecordVo.setCustomerStatus(0);
       			
					
			customerId = customer_temp.getId();
						
			//说明是老客户
			//修改介绍人 介绍人类型  还有cusType固定值为INTRODUCE 修改姓名  
			customer_temp.setIntroducer(customer.getIntroducer());
//			customer_temp.setCusOrg(customer.getCusOrg());
//			customer_temp.setCusType(new DataDict("INTRODUCE"));//customer.getCusType().getId()
			customer_temp.setName(customer.getName());
			customer_temp.setModifyTime(DateTools.getCurrentDateTime());
            customer_temp.setModifyUserId(user.getUserId());
            customer_temp.setIntentionCampus(intentionCampus);
            customerService.updateCustomer(customer_temp);
            
			
		}else{
			//新客户 新增客户 然后新增孩子 绑定关系 
			//新客户
			//所在校区
			//资源入口 转介绍
			log.info("pc_transfer_save_customer:"+customer.getContact());			
			customer.setPreEntrance(new DataDict(ResEntranceType.TRANSFER.getValue()));
			customer.setIntentionCampus(intentionCampus);
			customerId = customerService.saveTransferCustomer(customer,customerVo);
			
			StudentImportVo vo = new StudentImportVo();
			vo.setName(customerVo.getPointialStuName());
			if(StringUtil.isNotBlank(customerVo.getPointialStuGrade())){
				   vo.setGradeId(customerVo.getPointialStuGrade());
			}
			studentId = studentService.savePotentialStudent(vo, customerId);
			
			tCustomerRecordVo.setCustomerStatus(1);
			
		}
 		
//		if(customer_temp!=null){
//			//增加校验
//			//有人跟进
//    		String modifyTime = customer_temp.getModifyTime();
//    		int days = 0;
//    		try {
//				days = DateTools.getDateSpace(DateTools.getCurrentDateTime(), modifyTime);
//			} catch (ParseException e) {
//				days = 0;
//				e.printStackTrace();
//			}
//    		if(days<180){
//    			Response response = new Response();
//    			response.setResultCode(-1);
//    			response.setResultMessage("客户为老客户，上次跟进未超过6个月，校验失败");
//        		return response; 
//    		}
//			
//					
//			customerId = customer_temp.getId();
//			//说明是老客户
//			//修改介绍人 介绍人类型  还有cusType固定值为INTRODUCE 修改姓名  
//			customer_temp.setIntroducer(customer.getIntroducer());
//			customer_temp.setCusOrg(customer.getCusOrg());
//			customer_temp.setCusType(new DataDict("INTRODUCE"));//customer.getCusType().getId()
//			customer_temp.setName(customer.getName());
//			customer_temp.setModifyTime(DateTools.getCurrentDateTime());
//            customer_temp.setModifyUserId(user.getUserId());
//            customerService.updateCustomer(customer_temp);
//            
//			//标记为老客户
//			tCustomerRecordVo.setCustomerStatus(0);
//			//设置资源入口
//			//注意老客户的资源入口不一定生效 要加入preEntrance 
//			DataDict resEntrance = customer.getResEntrance();
//			if(resEntrance==null){
//				resEntrance = customer.getPreEntrance();
//			}
//			if(resEntrance!=null){
//				tCustomerRecordVo.setResEntranceId(resEntrance.getId());
//				tCustomerRecordVo.setResEntranceName(resEntrance.getName());
//				tCustomerRecordVo.setPreEntranceId(resEntrance.getId());
//				tCustomerRecordVo.setPreEntranceName(resEntrance.getName());
//			}else{
//				//应该抛出异常了
//				return new Response(-1,"该客户系统已经存在，但是客户的入口资源不能为空");
//			}
//
//			
//		}else{
//			//新客户
//			//所在校区
//			//资源入口 转介绍
//			log.info("pc_transfer_save_customer:"+customer.getContact());			
//			customer.setPreEntrance(new DataDict(ResEntranceType.TRANSFER.getValue()));
//			//customer.setResEntrance(new DataDict(ResEntranceType.TRANSFER.getValue()));
//			customerId = customerService.saveTransferCustomer(customer,customerVo);
//			tCustomerRecordVo.setCustomerStatus(1);
//			//新客户的资源入口为空   新客户的资源入口pre_Entrance res_Entrance都为空            
//		}		
		
		tCustomerRecordVo.setIntentionCampusId(intentionCampus.getId());
		tCustomerRecordVo.setIntentionCampusName(intentionCampus.getName());
		tCustomerRecordVo.setCampusOrgLevel(intentionCampus.getOrgLevel());
		tCustomerRecordVo.setStudentId(studentId);
		tCustomerRecordVo.setStudentName(customerVo.getPointialStuName());
		tCustomerRecordVo.setCustomerId(customerId);
		tCustomerRecordVo.setCreateTime(DateTools.getCurrentDateTime());
		tCustomerRecordVo.setCreateUserId(user.getUserId());
		tCustomerRecordVo.setModifyTime(DateTools.getCurrentDateTime());
		tCustomerRecordVo.setModifyUserId(user.getUserId());
		tCustomerRecordVo.setContact(customer.getContact());
		tCustomerRecordVo.setCustomerName(customer.getName());
		tCustomerRecordVo.setIntroducer(customer.getIntroducer());//介绍人
		tCustomerRecordVo.setIntroducerContact(customerVo.getIntroducerContact());
        tCustomerRecordVo.setCusOrg(customer.getCusOrg().getId());//介绍人类型
        //tCustomerRecordVo.setCusType(customer.getCusType().getId());//固定值 INTRODUCE
        tCustomerRecordVo.setRemark(customer.getRemark());
        tCustomerRecordVo.setAuditStatus(CustomerAuditStatus.TOBE_AUDIT);
          
        

		Response response = transferCustomerService.saveTransferCustomerAuditRecord(tCustomerRecordVo);
		
		CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
		dynamicStatus.setDynamicStatusType(CustomerEventType.TRANSFER_SIGN);
		dynamicStatus.setDescription(user.getName()+"进行转介绍登记");
		if(customer.getResEntrance()!=null){
		      dynamicStatus.setResEntrance(customer.getResEntrance());
		}
		dynamicStatus.setStatusNum(1);
		dynamicStatus.setTableName("transfer_introduce_customer");
		dynamicStatus.setTableId(response.getResultMessage());
		dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
		customerEventService.addCustomerDynameicStatus(customer_temp!=null?customer_temp:customer, dynamicStatus, user);
		
		return new Response(0, customerId);

	}
	
	/**
	 * 获取课程考勤状态统计数据
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/getCourseAttendaceStatusCount")
	@ResponseBody
	public CourseAttendaceStatusCountVo getCourseAttendaceStatusCount(@RequestParam String token) {
		TokenUtil.checkTokenWithException(token);
		return courseService.getCourseAttendaceStatusCount();
	}
	
	/**
     * 下载并导入银联支付数据
     * @param targetDate
     * @return
     */
    @RequestMapping(value = "/downLoadAndImportPosData")
    @ResponseBody
    public Response downLoadAndImportPosData(@RequestParam String targetDate) {
        operationCountService.downLoadAndImportPosData(targetDate);
        return new Response();
    }

	/**
     * 下载并导入快钱支付数据
     * @param targetDate
     * @return
     */
    @RequestMapping(value = "/downLoadAndImportPosDataKuaiQian")
    @ResponseBody
    public Response downLoadAndImportPosDataKuaiQian(@RequestParam String targetDate) {
        operationCountService.downLoadAndImportPosDataKuaiQian(targetDate);
        return new Response();
    }

	/**
	 * 创建新的Session，复制旧Session的属性
	 *
	 * @see org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy
	 *       .onAuthentication(Authentication, HttpServletRequest, HttpServletResponse)
	 */
	private HttpSession migrateSession(HttpServletRequest request) {
		HttpSession session = request.getSession();

		HashMap<String, Object> attributesToMigrate = new HashMap<String, Object>();
		Enumeration<?> enumer = session.getAttributeNames();
		while (enumer.hasMoreElements()) {
			String key = (String) enumer.nextElement();
			attributesToMigrate.put(key, session.getAttribute(key));
		}

		session.invalidate();
		session = request.getSession(true);
		//Copy attributes to new session
		if (!attributesToMigrate.isEmpty()) {
			for (Map.Entry<String, Object> entry : attributesToMigrate.entrySet()) {
				session.setAttribute(entry.getKey(), entry.getValue());
			}
		}

		return session;
	}


	/**
	 * 装饰HttpClient，允许访问https协议
	 * @param url
	 * @return
	 */
	private HttpClient wrapHttpClient()
	{
		HttpClient client = HttpClientUtils.getHttpClient();
		client = WebClientDevWrapper.wrapClient(client);
		return client;
	}



	/**
	 * 得到秘钥
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getKey", method = RequestMethod.GET)
	public void getKey(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AliyunOSSUtils.getKey(request,response);
	}
	
	/**
     * 得到秘钥
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getSseKey", method = RequestMethod.GET)
    public void getSseKey(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AliyunOSSUtils.getSseKey(request,response);
    }

	/**
	 * oss回调此方法，然后回应客户端
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/callBack", method = RequestMethod.POST)
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String c_length=request.getHeader("content-length");
		int c_i=Integer.parseInt(c_length);
		//解析阿里回调服务器的接口数据
		String ossCallbackBody = AliyunOSSUtils.getPostBody(request.getInputStream(), c_i);

		System.out.println("-----------ossCallbackBody-----------------"+ossCallbackBody);
		//验证是否是阿里发起的回调
		boolean ret = AliyunOSSUtils.verifyOSSCallBackRequest(request, ossCallbackBody);
		if (ret)
		{
			//保存oss返回来的文件信息
			uploadFileService.saveOSSCallbackFile(ossCallbackBody);
//			userService.processOssCallbackWithRedis(ossCallbackBody);
			//返回给OSS
			AliyunOSSUtils.response(request, response, ossCallbackBody, HttpServletResponse.SC_OK);
		}
		else
		{
			AliyunOSSUtils.response(request, response, "{\"Status\":\"verdify not ok\"}", HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	/**
     * 删除单个文件权限过滤
     * @param filePath
     * @param type
     */
    @RequestMapping(value="/deleteOSSFile", method = RequestMethod.DELETE)
    @ResponseStatus(value = org.springframework.http.HttpStatus.OK)
    public void deleteOSSFile(String filePath, String uploadType, String uploadId) {
        itemInstanceFileService.deleteFile(filePath, uploadType, uploadId);
    }


	/**
	 * 双师接口
	 * 双师 辅班课程列表数据查询
	 * @param token
	 * @param dp
	 * @param twoTeacherClassCourseVo
	 * @return
	 */
	@RequestMapping(value ="/getTwoTeacherClassCourseList")
	@ResponseBody
	public DataPackage getTwoTeacherClassCourseList(@RequestParam String token, @ModelAttribute DataPackage dp, @ModelAttribute TwoTeacherClassCourseVo twoTeacherClassCourseVo){
		TokenUtil.checkTokenWithException(token);
		dp = twoTeacherClassService.getTwoTeacherClassCourseList(dp, twoTeacherClassCourseVo);
		return dp;
	}

	/**
	 * 双师接口
	 * 双师 根据 课程id 辅班id 获取对应的考勤学生列表
	 * @param token
	 * @param twoTeacherClassCourseVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getTwoTeacherTwoStudentAttendentList")
	@ResponseBody
	public List<TwoTeacherClassStudentAttendentVo> getTwoTeacherTwoStudentAttendentList(@RequestParam String token, @ModelAttribute TwoTeacherClassCourseVo twoTeacherClassCourseVo) throws Exception {
		TokenUtil.checkTokenWithException(token);
		DataPackage dp = new DataPackage(0, 1000);
		List<TwoTeacherClassStudentAttendentVo> list = (List<TwoTeacherClassStudentAttendentVo>)twoTeacherClassService.getTwoTeacherClassStudentAttendentList(dp, twoTeacherClassCourseVo).getDatas();
		return list;
	}

	/**
	 * 双师接口
	 * 双师 双师学生提交考勤数据
	 * @param token
	 * @param courseId
	 * @param twoTeacherClassTwoId
	 * @param attendanceData
	 * @param oprationCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/modifyTwoTeacherStudentAttendance")
	@ResponseBody
	public Response modifyTwoTeacherStudentAttendance(@RequestParam String token, @RequestParam int courseId, @RequestParam int twoTeacherClassTwoId, @RequestParam String attendanceData,  String oprationCode) throws Exception{
		TokenUtil.checkTokenWithException(token);
		return twoTeacherClassService.modifyTwoTeacherClassTwoCourseStudentAttendance(courseId, twoTeacherClassTwoId, attendanceData, oprationCode);
	}

	/**
	 * 双师接口
	 * 双师 小红点查询
	 * @param token
	 * @param teacherId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value = "/checkHasTwoTeacherClassForPeriodDate")
	@ResponseBody
	public List<HasClassCourseVo> checkHasTwoTeacherClassForPeriodDate(@RequestParam String token, @RequestParam String teacherId, String startDate, String endDate){
		if (StringUtil.isBlank(startDate)) {
			startDate = DateTools.getCurrentDate();
		}
		if (StringUtil.isBlank(endDate)) {
			endDate = DateTools.getCurrentDate();
		}
		TokenUtil.checkTokenWithException(token);
		List<HasClassCourseVo> list = twoTeacherClassService.checkHasTwoTeacherClassForPeriodDate(teacherId, startDate, endDate);
		return list;
	}

	/**
	 * 双师接口
	 * 上传 小班的 考勤 签名图片
	 * @param token
	 * @param twoTeacherClassTwoId
	 * @param twoTeacherCourseId
	 * @param attendancePicFile
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/submitTwoTeacherPic", method = RequestMethod.POST)
	@ResponseBody
	public Response submitTwoTeacherPic(@RequestParam String token, @RequestParam int twoTeacherCourseId,@RequestParam int twoTeacherClassTwoId, @RequestParam("attendancePic") MultipartFile attendancePicFile,HttpServletRequest request) throws Exception {
		TokenUtil.checkTokenWithException(token);
		String servicePath = request.getSession().getServletContext().getRealPath("/");
		
		
        TwoTeacherClassCourse course = twoTeacherClassService.getTwoTeacherCourseById(twoTeacherCourseId);
        if(course !=null) {
            // 统一使用 文件名 来 标明签名文件
            //String fileName = UUID.randomUUID().toString();
            String fileName =  String.format("TWO_CLASS_ATTEND_PIC_%s.jpg", twoTeacherCourseId+"_"+twoTeacherClassTwoId);
            course.setAttendacePicName(fileName);
            twoTeacherClassService.updatePicByCourseAndClassId(twoTeacherCourseId,twoTeacherClassTwoId,fileName);
            String folder=servicePath+ PropertiesUtils.getStringValue("save_file_path");//系统路径
            FileUtil.isNewFolder(folder);

            String bigFileName = "BIG_"+fileName;//阿里云上面的文件名 大   默认是JPG
            String midFileName = "MID_"+fileName;//阿里云上面的文件名 中
            String smallFileName = "SMALL_"+fileName;//阿里云上面的文件名 小

            String relFileName=folder+"/realFile_"+twoTeacherCourseId+"_"+twoTeacherClassTwoId+UUID.randomUUID().toString()+".jpg";
            File realFile=new File(relFileName);
            File bigFile=new File(folder+"/"+bigFileName);
            File midFile=new File(folder+"/"+midFileName);
            File smallFile=new File(folder+"/"+smallFileName);

            try {
                attendancePicFile.transferTo(realFile);
                ImageSizer.compressImage(relFileName, smallFile.getAbsolutePath(), 60);//转换图片大小
                ImageSizer.compressImage(relFileName, midFile.getAbsolutePath(), 200);//转换图片大小
                ImageSizer.compressImage(relFileName, bigFile.getAbsolutePath(), 600);//转换图片大小
                AliyunOSSUtils.put(bigFileName, bigFile);//传到阿里云
                AliyunOSSUtils.put(midFileName, midFile);//传到阿里云
                AliyunOSSUtils.put(smallFileName, smallFile);//传到阿里云
                AliyunOSSUtils.put(fileName, realFile);//传原图到阿里云
            } catch (IllegalStateException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }finally{
                bigFile.delete();
                midFile.delete();
                smallFile.delete();
                realFile.delete();
            }
        }
				
		
		//将上传从service迁移到controller xiaojinwang 20171029		
		//twoTeacherClassService.submitTwoTeacherPic(twoTeacherCourseId,twoTeacherClassTwoId, attendancePicFile,servicePath);		
		return new Response();
	}


	/**
	 * 双师接口
	 * 获取双师具体考勤课程
	 * @param token
	 * @param twoTeacherCourseId
	 * @param twoTeacherClassTwoId
	 * @return
	 */
	@RequestMapping(value="/findTwoTeacherClassCourseById")
	@ResponseBody
	public TwoTeacherClassCourseVo findTwoTeacherClassCourseById(@RequestParam String token, @RequestParam int twoTeacherCourseId, @RequestParam int twoTeacherClassTwoId) {
		TokenUtil.checkTokenWithException(token);
		return twoTeacherClassService.findTwoTeacherClassCourseByCourseIdAndClassId(twoTeacherCourseId, twoTeacherClassTwoId);
	}

	/**
	 * 给本地测试接口登录用
	 * @param account
	 * @param request
	 */
	@RequestMapping(value = "/testLogin",method = RequestMethod.GET)
	@ResponseBody
	public void testLogin(String account,HttpServletRequest request){
		//从spring容器中获取UserDetailsService(这个从数据库根据用户名查询用户信息,及加载权限的service)
		UserDetailsService userDetailsService =
				(UserDetailsService)ApplicationContextUtil.getContext().getBean("userDetailsService");

		//根据用户名username加载userDetails
		UserDetails userDetails = userDetailsService.loadUserByUsername(account);

		//根据userDetails构建新的Authentication,这里使用了
		//PreAuthenticatedAuthenticationToken当然可以用其他token,如UsernamePasswordAuthenticationToken
		PreAuthenticatedAuthenticationToken authentication =
				new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());

		//设置authentication中details
		authentication.setDetails(new WebAuthenticationDetails(request));

		//存放authentication到SecurityContextHolder
		SecurityContextHolder.getContext().setAuthentication(authentication);
//                        HttpSession session = request.getSession(true);
		HttpSession session = migrateSession(request);
		//在session中存放security context,方便同一个session中控制用户的其他操作
		session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
	}


	@RequestMapping(value = "/getInternetPayInfo",method = RequestMethod.GET)
	@ResponseBody
	public String getPayInfo(String reqsn,String trxid,String cusId,String appId,String appKey){
		SybPayService service = new SybPayService();
		String ssss="";
		try {
			Map map=service.query(reqsn, trxid,cusId,appId,appKey);//查询支付宝或者微信订单的支付状态
			for(Object s : map.keySet()){
				log.info(map.get(s));
				ssss+=s+":"+map.get(s)+";";
			}
		} catch (Exception e) {
			throw new ApplicationException("查询通联支付订单的时候报错，请联系管理员");
		}
		return ssss;
	}
	
	/**
     * 客户信息录入   保存或者更新客户信息  
     */
    @RequestMapping(value = "/saveOrUpdatePhoneCusomer", method = RequestMethod.POST)
    @ResponseBody
    public Response saveOrUpdatePhoneCusomer(@RequestParam String token, @ModelAttribute PhoneCustomerVo cus) throws Exception {
        TokenUtil.checkTokenWithException(token);
        customerService.saveOrUpdatePhoneCusomer(cus);
        return new Response();
    }
	
	/**
	 * 
	 * @author: duanmenrun
	 * @Title: getUserStayCustomerListByStatus 
	 * @Description: 查询用户待获取客户（营主待分配）
	 * @throws Exception      
	 * @param token
	 * @param status TOBEASSIGNED待分配,STAY_FOLLOW待获取
	 * @param gridRequest
	 * @return
	 */
    @RequestMapping(value = "/getUserStayCustomerList",method = RequestMethod.POST)
	@ResponseBody
	public DataPackage getUserStayCustomerListByStatus(@RequestParam String token, @RequestParam String status, @ModelAttribute GridRequest gridRequest) throws Exception {
    	TokenUtil.checkTokenWithException(token);
    	DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
    	dataPackage = customerService.getUserStayCustomerListByStatus(dataPackage, status);
		return dataPackage;
	}
    
    /**
	 * 
	 * @author: duanmenrun
	 * @Title: getUserFollowCustomerList
	 * @Description: 查询用户跟进客户客户
	 * @throws Exception      
	 * @param token
	 * @param status SIGNEUP签单客户     FOLLOWING跟进客户
	 * @param gridRequest
	 * @return
	 */
    @RequestMapping(value = "/getUserFollowCustomerList",method = RequestMethod.POST)
	@ResponseBody
	public DataPackage getUserFollowCustomerList(@RequestParam String token, @RequestParam String status, @ModelAttribute GridRequest gridRequest) throws Exception {
    	TokenUtil.checkTokenWithException(token);
    	DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
    	dataPackage = customerService.getUserFollowCustomerList(dataPackage, status);
		return dataPackage;
	}
    
    
    /**
     * 
     * @author: duanmenrun
     * @Title: getUsersByRoleSign  查找职位
     * @Description: TODO 
     * @throws 
     * @param token
     * @param showBelong GROUNP  BRENCH  CAMPUS
     * @param job   user_job.JOB_SIGN
     * @return
     */
	@RequestMapping(value ="/getUsersByRoleSign",method = RequestMethod.POST)
	@ResponseBody
	public List<Map<Object, Object>> getUsersByRoleSign(@RequestParam String token,String showBelong,@RequestParam(value="job[]",required=false)String[] job) {
		TokenUtil.checkTokenWithException(token);
		return commonService.getUsersByRoleSign(showBelong, job);
	}
	/**
	 * 
	 * @author: duanmenrun
	 * @Title: updateCustomerDeleverTarget 
	 * @Description: TODO 营主分配客户给咨询师
	 * @throws 
	 * @param token
	 * @param customerVo
	 * @return
	 */
	@RequestMapping(value ="/updateCustomerDeleverTarget",method = RequestMethod.POST)
	@ResponseBody
	public Response updateCustomerDeleverTarget(@RequestParam String token,@ModelAttribute CustomerVo customerVo) {
		TokenUtil.checkTokenWithException(token);
		Response response = customerService.updateCustomerDeleverTarget(customerVo);
		return response;
	}
	
	@RequestMapping(value ="/judgeUserIdentity",method = RequestMethod.POST)
	@ResponseBody
	public Response judgeUserIdentity(@RequestParam String token) {
		TokenUtil.checkTokenWithException(token);
		User currentUser = userService.getCurrentLoginUser();
		Response response = commonService.judgeUserIdentity(currentUser.getUserId());
		return response;
	}
	
	/**
	 * 咨询师获取客户
	 * @author: duanmenrun
	 * @Title: distributeCustomer 
	 * @Description: TODO 
	 * @throws 
	 * @param token
	 * @param cusId
	 * @return
	 */
	@RequestMapping(value ="/distributeCustomer",method = RequestMethod.POST)
	@ResponseBody
	public Response distributeCustomer(@RequestParam String token,@RequestParam String cusId,@RequestParam String status) {
		TokenUtil.checkTokenWithException(token);
		Response response = customerService.mobileDistributeCustomer(cusId,status);
		return response;
	}
    
    
    /**
     * app:ios android 直传图片文件获取密钥 token
     */
	@RequestMapping(value = "/getAppSTSKey", method = RequestMethod.GET)
	public void getAppSTSKey(HttpServletRequest request, HttpServletResponse response,@RequestParam String token) throws Exception {
		TokenUtil.checkTokenWithException(token);
		AliyunAppOssUtil.getAppSTSKey(request, response);
	}
	
	/**
	 * app 直传文件回调接口
	 */
	@RequestMapping(value = "/stsCallBack", method = RequestMethod.POST)
	public void stsCallBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ossCallbackBody = AliyunAppOssUtil.GetPostBody(request.getInputStream(), Integer.parseInt(request.getHeader("content-length")));
		boolean ret = AliyunAppOssUtil.VerifyOSSCallbackRequest(request, ossCallbackBody);
		if(ret){			
			//回调参数
			log.info("appStsCallBack:"+ossCallbackBody);
			appUploadCallBackService.uploadCallBackService(ossCallbackBody);
			
			
			AliyunAppOssUtil.response(request, response, "{\"Status\":\"OK\"}", HttpServletResponse.SC_OK);
		}else{
			AliyunAppOssUtil.response(request, response, "{\"Status\":\"verdify not ok\"}", HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/**
	 * 查询营主可分配的咨询师
	 * @author: duanmenrun
	 * @Title: getAllDistributableZXS 
	 * @Description: TODO 
	 * @param token
	 * @return
	 */
	@RequestMapping(value ="/getAllDistributableZXS",method = RequestMethod.POST)
	@ResponseBody
	public List<UserDetailForMobileVo> getAllDistributableZXS(@RequestParam String token) {
		TokenUtil.checkTokenWithException(token);
		return customerService.getAllDistributableZXS();
	}
	
	/**
	 * 获取可分配咨询师分公司
	 * @author: duanmenrun
	 * @Title: getDistributableZXSBrenchs 
	 * @Description: TODO 
	 * @param token
	 * @return
	 */
	@RequestMapping(value ="/getDistributableZXSBrenchs",method = RequestMethod.POST)
	@ResponseBody
	public List<OrganizationMobileSimpleVo> getDistributableZXSBrenchs(@RequestParam String token) {
		TokenUtil.checkTokenWithException(token);
		User currentUser = userService.getCurrentLoginUser();
		if(currentUser==null) throw new ApplicationException("系统出错");
		List<Organization> organizations = userService.getDeptExistsDistributableZXS(currentUser.getUserId());
		//List<Organization> organizations = userService.getDeptExistsDistributableZXS("USE0000018100");
		List<OrganizationMobileSimpleVo> vos = new LinkedList<>();		
		if(organizations!=null&&organizations.size()>0) {
			for( Organization org :  organizations) {
				OrganizationMobileSimpleVo vo = new OrganizationMobileSimpleVo();
				vo.setOrgT(OrganizationType.BRENCH.getValue());
				if(OrganizationType.BRENCH == org.getOrgType()) {
					vo.setName(org.getName());
					vo.setOrgId(org.getId());
				}else {
					Organization o =  organizationService.findBrenchById(org.getId());
					if(o!=null) {
						vo.setName(o.getName());
						vo.setOrgId(o.getId());
						
					}
				}
				vos.add(vo);
			}
			vos = removeDuplicateOrganizationMobileSimpleVo(vos);
		}
		
		return vos;
	}
	
	private List<OrganizationMobileSimpleVo> removeDuplicateOrganizationMobileSimpleVo(List<OrganizationMobileSimpleVo> list) {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getOrgId().equals(list.get(i).getOrgId())) {
					list.remove(j);
				}
			}
		}
		return list;
	}
	/**
	 * 根据organizationId查询咨询师（分公司查询校区）
	 * @author: duanmenrun
	 * @Title: getDistributableZXSByParentId 
	 * @Description: TODO 
	 * @param token
	 * @return
	 */
	@RequestMapping(value ="/getDistributableZXSByParentId",method = RequestMethod.POST)
	@ResponseBody
	public Response getDistributableZXSByParentId(@RequestParam String token, @RequestParam String organizationId) {
		TokenUtil.checkTokenWithException(token);
		Long time1 = System.currentTimeMillis();
		Response response = customerService.getDistributableZXSByParentId(organizationId);
		Long time2 = System.currentTimeMillis();
		System.out.println("time2-time1: "+(time2-time1));
		return response;
	}
	/**
	 * 根据名字查询可分配的用户
	 * @author: duanmenrun
	 * @Title: getDistributableUserByName 
	 * @Description: TODO 
	 * @param token
	 * @param userName
	 * @return
	 */
	@RequestMapping(value ="/getDistributableUserByName",method = RequestMethod.POST)
	@ResponseBody
	public Response getDistributableUserByName(@RequestParam String token, @RequestParam String userName) {
		TokenUtil.checkTokenWithException(token);
		Long time1 = System.currentTimeMillis();
		Response response = customerService.getDistributableUserByName(userName);
		Long time2 = System.currentTimeMillis();
		System.out.println("time2-time1: "+(time2-time1));
		return response;
	}
	
	/**
	 * 根据客户Id找到客户
	 * @param token
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/getCustomerDetailById")
	@ResponseBody
	public Response getCustomerDetailById(@RequestParam String token,String customerId){
		TokenUtil.checkTokenWithException(token);
		Long time1 = System.currentTimeMillis();
		Response response = customerService.getCustomerDetailById(customerId);
		Long time2 = System.currentTimeMillis();
		System.out.println("time2-time1: "+(time2-time1));
		return response;
	}
	
	/**
	 * 查询客户跟进记录
	 * @author: duanmenrun
	 * @Title: getCustomerFollowUpRecrodsList 
	 * @Description: TODO 
	 * @param token
	 * @param customerId
	 * @param gridRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCustomerFollowUpRecrodsList")
	@ResponseBody
	public DataPackage getCustomerFollowUpRecrodsList(@RequestParam String token, @RequestParam String customerId, @ModelAttribute GridRequest gridRequest) throws Exception {
		TokenUtil.checkTokenWithException(token);
    	DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.getCustomerFollowUpRecrodsList(customerId, dataPackage); 
		return dataPackage ;
	}
	
	/**
	 * 查询用户待获取客户（营主待分配）、跟进中客户数量
	 * @author: duanmenrun
	 * @Title: getUserCustomerCount 
	 * @Description: TODO 
	 * @param token
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/getUserCustomerCount")
	@ResponseBody
	public Response getUserCustomerCount(@RequestParam String token) throws Exception {
    	TokenUtil.checkTokenWithException(token);
    	Response response=customerService.getUserCustomerCount();
		return response;
	}
    /**
     * 删除学生
     * @author: duanmenrun
     * @Title: setDeleteStudent 
     * @Description: TODO 
     * @param token
     * @param studentId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setDeleteStudent")
	@ResponseBody
	public Response setDeleteStudent(@RequestParam String token,String studentId) throws Exception {
    	TokenUtil.checkTokenWithException(token);
    	Response response = new Response();
    	if(StringUtil.isBlank(studentId)){
			response.setResultCode(-1);
			response.setResultMessage("学生ID不能为空，删除学生失败");
			return response;
		}
		return customerService.setDeleteStudent(studentId);
	}
    /**
     * 获取省份
     * @author: duanmenrun
     * @Title: getRegionList 
     * @Description: TODO 
     * @param token
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getRegionList")
	@ResponseBody
	public Response getRegionList(@RequestParam String token) throws Exception {
    	TokenUtil.checkTokenWithException(token);
    	Response response = new Response();
    	List<RegionVo> vos = regionService.getAllProvinces();
    	response.setData(vos);
		return response;
	}
    /**
     * 获取城市
     * @author: duanmenrun
     * @Title: getCitys 
     * @Description: TODO 
     * @param token
     * @param provinceID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getCitys")
	@ResponseBody
	public Response getCitys(@RequestParam String token,String provinceID) throws Exception {
    	TokenUtil.checkTokenWithException(token);
    	Response response = new Response();
    	if(StringUtil.isBlank(provinceID)){
			response.setResultCode(-1);
			response.setResultMessage("省份ID不能为空");
			return response;
		}
    	List<RegionVo> list=regionService.getCitys(provinceID);
    	response.setData(list);
		return response;
	}
    /**
     * 获取学生学校
     * @author: duanmenrun
     * @Title: getSchoolByCity 
     * @Description: TODO 
     * @param token
     * @param cityId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getSchoolByCity")
	@ResponseBody
	public Response getSchoolByCity(@RequestParam String token,String cityId) throws Exception {
    	TokenUtil.checkTokenWithException(token);
    	Response response = new Response();
    	if(StringUtil.isBlank(cityId)){
			response.setResultCode(-1);
			response.setResultMessage("城市ID不能为空");
			return response;
		}
    	List<StudentSchoolVo> list=commonService.getStuSchools(cityId);
		List<NameValue> nvs = new ArrayList<NameValue>();
		nvs.add(SelectOptionResponse.buildNameValue("请选择学校",""));
		for(StudentSchoolVo school : list){
				nvs.add(SelectOptionResponse.buildNameValue(school.getName()+"（"+school.getSchoolTypeName()+"）", school.getId()));
		}
		response.setData(nvs);
		return response;
	}
    
    @RequestMapping(value = "/saveCustomerStudent")
	@ResponseBody
	public Response saveCustomerStudent(@RequestParam String token,String customerId,StudentImportVo vo) throws Exception {
    	TokenUtil.checkTokenWithException(token);
    	Response response = new Response();
    	if(StringUtil.isBlank(customerId)){
			response.setResultCode(-1);
			response.setResultMessage("客户id不能为空");
			return response;
		}
    	if(StringUtil.isBlank(vo.getName())){
			response.setResultCode(-1);
			response.setResultMessage("学生姓名不能为空");
			return response;
		}
    	studentService.savePotentialStudent(vo,customerId);	
		return response;
	}
    
}


