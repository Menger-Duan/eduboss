package com.eduboss.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.CustomerAuditStatus;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.CustomerEventType;
import com.eduboss.common.MsgNo;
import com.eduboss.common.PushMsgType;
import com.eduboss.common.ResEntranceType;
import com.eduboss.common.RoleCode;
import com.eduboss.common.SysMsgType;
import com.eduboss.common.ValidStatus;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerAppointment;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.CustomerImportTransform;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.GainCustomer;
import com.eduboss.domain.Organization;
import com.eduboss.domain.ResourcePool;
import com.eduboss.domain.TransferCustomerCampus;
import com.eduboss.domain.Role;
import com.eduboss.domain.SentRecord;
import com.eduboss.domain.Student;
import com.eduboss.domain.User;
import com.eduboss.domain.UserJob;
import com.eduboss.domainVo.AuditRecordVo;
import com.eduboss.domainVo.AutoCompleteOptionVo;
import com.eduboss.domainVo.ChangeCampusApplyVo;
import com.eduboss.domainVo.ChangeUserRoleRecordVo;
import com.eduboss.domainVo.CustomerAppointmentVo;
import com.eduboss.domainVo.CustomerCallsLogVo;
import com.eduboss.domainVo.CustomerDataImport;
import com.eduboss.domainVo.CustomerDeliverTarget;
import com.eduboss.domainVo.CustomerDynamicStatusVo;
import com.eduboss.domainVo.CustomerFolowupVo;
import com.eduboss.domainVo.CustomerPoolDataVo;
import com.eduboss.domainVo.CustomerPoolVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.DataImportResult;
import com.eduboss.domainVo.DistributeCustomerVo;
import com.eduboss.domainVo.FollowupCustomerVo;
import com.eduboss.domainVo.InvalidCustomerRecordVo;
import com.eduboss.domainVo.ReceptionistCustomerVo;
import com.eduboss.domainVo.SignCustomerVo;
import com.eduboss.domainVo.SignupCustomerVo;
import com.eduboss.domainVo.StudentImportVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.TransferCustomerRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.dto.TimeVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.ChangeCampusApplyService;
import com.eduboss.service.CustomerEventService;
import com.eduboss.service.CustomerFolowupService;
import com.eduboss.service.CustomerService;
import com.eduboss.service.DisabledCustomerService;
import com.eduboss.service.MobileUserService;
import com.eduboss.service.InvalidCustomerAuditService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.ResourcePoolService;
import com.eduboss.service.StudentService;
import com.eduboss.service.TransferCustomerService;
import com.eduboss.service.UserJobService;
import com.eduboss.service.UserService;
import com.eduboss.task.SendSysMsgCostomerDistributionThread;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.FileUtil;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.ReadExcel1;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;


/**
 * @classname	CommonAction.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2013-3-16 01:09:47
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */

@Controller
@RequestMapping(value = "/CustomerAction")
public class CustomerController{
	
	/**
	 * 日志
	 */
	private final static Logger log = Logger.getLogger(CustomerController.class);
	
	//将转介绍的天数限制 提示语提取出来
	private final static Integer TRANSFER_DAY = 60;
	private final static String TRANSFER_TIP ="客户为老客户，上次跟进未超过2个月，校验失败";
	
	
	/**
	 * common service
	 */
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerEventService customerEventService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ResourcePoolService resourcePoolService;
	
	@Autowired
	private MobileUserService mobileUserService;
	
	@Autowired
	private TransferCustomerService transferCustomerService;
	
	@Autowired
	private InvalidCustomerAuditService invalidCustomerAuditService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private CustomerFolowupService customerFolowupService;
	
	@Autowired
	private ChangeCampusApplyService changeCampusApplyService;
	
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private UserJobService userJobService;
    
    @Autowired
    private DisabledCustomerService disabledCustomerService;

	@RequestMapping(value = "/getCustomersForJqGrid", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getCustomersForJqGrid(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerVo cus, String onlyShowUndelive) {
		log.info("getCustomersForJqGrid() start.");
		
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		//cus.setDeliverTarget(userService.getUserByName(cus.getDeliverTargetName()).getUserId());
		dataPackage = customerService.getCustomers(cus, dataPackage, "true".equalsIgnoreCase(onlyShowUndelive)?true:false);
		
		log.info("getCustomersForJqGrid() end.");
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value ="/getPhoneRecords", method= RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getPhoneRecords(@ModelAttribute GridRequest gridRequest,@ModelAttribute CustomerCallsLogVo customerCallsLogVo){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage=customerService.getPhoneRecords(dataPackage,customerCallsLogVo);
		return new DataPackageForJqGrid(dataPackage);
	}

	@RequestMapping(value = "/editCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response editCustomer(@ModelAttribute GridRequest gridRequest, @RequestBody CustomerVo cus) throws Exception {
		if (cus == null) {
			throw new ApplicationException(ErrorCode.RES_NOT_FOUND);
		}
		String customerId = "";
		User user=userService.getCurrentLoginUser();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			customerService.deleteCustomer(cus.getId());
		} else {
			if(cus.getDeliverTarget()!=null && StringUtils.isNotBlank(cus.getDeliverTarget()) && !cus.getDeliverTarget().equals(user.getUserId())
					&& cus.getWorkBench() != null && !cus.getWorkBench().equals("receptionistNew")){
				Response res=resourcePoolService.getResourcePoolVolume(1, cus.getDeliverTarget());
				if(res.getResultCode()!=0){
					return res;
				}
			}			
			customerId = customerService.saveOrUpdateCustomer(cus); 
		}
		return new Response(0, customerId);
	}
	
	//获取客户详细信息 
	@RequestMapping(value = "/findCustomerById", method =  RequestMethod.GET)
	@ResponseBody
	public CustomerVo findCustomerById(@ModelAttribute GridRequest gridRequest, @ModelAttribute Customer cus) throws Exception {
		
		if(cus.getRemark() != null){
			return customerService.findCustomerById(cus); //查询客户合同信息
		}else{
			return customerService.findCustomerById(cus.getId()); 
		}
		
	}
	
	@RequestMapping(value = "/getAutoCompeleteCustomer", method =  RequestMethod.GET)
	@ResponseBody
	public String getAutoCompeleteCustomer(@RequestParam String term) throws Exception {
		String autoCompeleteData = "";
		
		List<Customer> resList = customerService.getCustomersForAutoCompelate(term);
		
		for (Customer customer : resList) {
			if(customer.getRemark() == null)
				customer.setRemark("");
			autoCompeleteData += "\""+customer.getName()+"-"+customer.getContact()+","+customer.getRecordDate()+","+customer.getDealStatus()+","+customer.getRemark()+","+customer.getId()+"\","; 
		}
		if (autoCompeleteData.length() > 0) {
			autoCompeleteData = autoCompeleteData.substring(0, autoCompeleteData.length() - 1);
			autoCompeleteData = autoCompeleteData.replaceAll("\n", "").replaceAll("\t", "");
		}
		autoCompeleteData = "["+autoCompeleteData+"]";
		
		return autoCompeleteData;
	}
	
	@RequestMapping(value = "/changeCustomerStatus", method =  RequestMethod.GET)
	@ResponseBody
	public Response changeCustomerStatus(@RequestParam String cusId, @RequestParam String dealStatus) throws Exception {
		
		Customer customerWidthRequestStatusCustomer = customerService.findById(cusId);
		customerWidthRequestStatusCustomer.setDealStatus(CustomerDealStatus.valueOf(dealStatus));
		
		Response res=new Response();
		res=customerService.changeCustomerStatus(customerWidthRequestStatusCustomer); 
		if(res.getResultCode()==0){
			customerEventService.saveCustomerDynamicStatus(cusId, CustomerEventType.FOLLOWING, "更改客户处理状态：" + CustomerDealStatus.valueOf(dealStatus).getName());			
		}
		
		return res;
	}

	/**
	 * 批量分配客户资源，资源ID用英文逗号隔开
	 * 包括置无效特例：deliverType deliverTarget 均为null，且dealStatus 为 "INVALID"
	 * @param cusId
	 * @param deliverType
	 * @param deliverTarget
	 * @param dealStatus
	 * @return
	 */
	@RequestMapping(value = "/deliverCustomer", method =  RequestMethod.GET)
	@ResponseBody
	public Response deliverCustomer(@RequestParam String cusId, @RequestParam CustomerDeliverType deliverType, @RequestParam String deliverTarget, @RequestParam(required=false) CustomerDealStatus dealStatus
			,String beforeDeliverTarget,String mycustomer,String invalidReason) {
		
		String [] cusIdArray = cusId.split(",");
		Response re=new Response();
		boolean flag=false;
		String resultMSg="";
		String userId=userService.getCurrentLoginUser().getUserId();
		if(CustomerDeliverType.PERSONAL_POOL.equals(deliverType) && (mycustomer==null ||(StringUtil.isNotBlank(mycustomer) && "0".equals(mycustomer)))){
			re = resourcePoolService.getResourcePoolVolume(cusIdArray.length, deliverTarget);
		}
		if(re.getResultCode()==0){
			for (String id : cusIdArray) {
				Customer customerPaddingExtract = customerService.findById(id);
				// 自己的资源是可以往外分配的，
				if(!userId.endsWith(customerPaddingExtract.getDeliverTarget()) 
						&& CustomerDeliverType.PERSONAL_POOL.equals(deliverType) //分配给个人
						&& customerPaddingExtract.getDeliverType() != null //旧数据类型不为空 并且也在个人资源池
						&& customerPaddingExtract.getDeliverType().equals(CustomerDeliverType.PERSONAL_POOL) 
					    && ((StringUtils.isNotBlank(deliverTarget) && !deliverTarget.equals(customerPaddingExtract.getDeliverTarget())))// 跟进人不为空并且不等于当前跟进人
						&& customerPaddingExtract.getDealStatus().equals(CustomerDealStatus.FOLLOWING)// 旧数据状态不是新增跟待跟进  (旧数据是跟进中的，只要不是跟进中的都可以获取的)
						){
					flag=true;
					resultMSg+=customerPaddingExtract.getName()+",";
				}else{
					customerPaddingExtract.setDeliverType(deliverType);
					customerPaddingExtract.setTransferFrom(userService.getBelongCampus().getId());
					if(StringUtils.isNotBlank(beforeDeliverTarget)){
						customerPaddingExtract.setBeforeDeliverTarget(beforeDeliverTarget);
						customerPaddingExtract.setBeforeDeliverTime(DateTools.getCurrentDateTime());
					}
						customerPaddingExtract.setDeliverTime(DateTools.getCurrentDateTime());
						customerPaddingExtract.setDeliverTarget(deliverTarget);
					if (dealStatus == null) {
						if(deliverType != null )
							dealStatus=CustomerDealStatus.STAY_FOLLOW;
						else
							dealStatus=CustomerDealStatus.NEW;
					}
					
					if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.equals(deliverType)){
						//修改了资源池
						if(StringUtil.isNotBlank(deliverTarget) && !deliverType.equals(customerPaddingExtract.getDeliverTarget())){
							//传入进来的资源池
							ResourcePool repool = resourcePoolService.findResourcePoolById(deliverTarget);
							if(repool==null  || (repool!=null && repool.getStatus().equals(ValidStatus.INVALID))){
								//当前组织架构
								Organization rrrpool = userService.getCurrentLoginUserOrganization();
								//如果跟传进来的一样就查找他的父类
								if(rrrpool!=null && deliverTarget.equals(rrrpool.getId())){
										//没有就先查所属再查父类，如果父类都找不到就gameOver了
										getBelongResourcePool(customerPaddingExtract,rrrpool);
								}else if(rrrpool!=null){
									//当前用户的资源池
									ResourcePool repool2 = resourcePoolService.findResourcePoolById(rrrpool.getId());
									if(repool2!=null && repool2.getStatus().equals(ValidStatus.VALID)){//有资源池就设置为当前这个资源池
										customerPaddingExtract.setDeliverTarget(rrrpool.getId());
									}else{//没有就先查所属再查父类，如果父类都找不到就gameOver了
										getBelongResourcePool(customerPaddingExtract,rrrpool);
									}
								}
								
							}
						}
					}				    
					if(StringUtils.isNotBlank(invalidReason) && invalidReason!=null){
						customerPaddingExtract.setInvalidReason(invalidReason);
					}
					customerPaddingExtract.setDealStatus(dealStatus);
					re=customerService.changeCustomerStatus(customerPaddingExtract);
					String deliverTargetName="";
					if(CustomerDeliverType.PERSONAL_POOL.equals(deliverType)){
						User user=userService.getUserById(deliverTarget);
						if(user!=null)
							deliverTargetName=user.getName();
					}else if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.equals(deliverType)){
						Organization org=userService.getOrganizationById(deliverTarget);
						if(org!=null)
							deliverTargetName=org.getName();
					}
					String description="分配客户：" + (deliverType==null?"":deliverType.getName() )+ " - " + deliverTargetName;
					if(re.getResultCode()==0){
						if(CustomerDealStatus.INVALID == dealStatus ){
							description ="无效客户-操作人:"+userService.getCurrentLoginUser().getName()+"  无效理由："+invalidReason;
							customerEventService.saveCustomerDynamicStatus(id, CustomerEventType.INVALID, description);
						}else if(CustomerDealStatus.FOLLOWING == dealStatus ){
							description ="获取跟进客户-操作人:"+userService.getCurrentLoginUser().getName();;
							customerEventService.saveCustomerDynamicStatus(id, CustomerEventType.FOLLOWING, description);
						}else{
							customerEventService.saveCustomerDynamicStatus(id, CustomerEventType.DELIVER, description);
						}
					}
					
				}
			}
		}else if(re.getResultCode()==-1){
			re.setResultMessage("咨询师没有职位勾选过职位可跟进客户资源");
		}else if(re.getResultCode()==-2){
			re.setResultMessage("容量已超出");
		}
		
		if(flag){
			re.setResultCode(-1);
			re.setResultMessage("客户"+resultMSg.substring(0,resultMSg.length()-1)+"已被人获取跟进。");
		} else {
			if(CustomerDealStatus.INVALID != dealStatus && CustomerDeliverType.PERSONAL_POOL.equals(deliverType)){
				MobileUser mobileUser = mobileUserService.findMobileUserByStaffId(deliverTarget);
				if (mobileUser != null) { // 存在手机用户才发送
					User currentUser = userService.getCurrentLoginUser();
					SentRecord record = new SentRecord();
					record.setMsgNo(MsgNo.M13);
					record.setMsgName("客户资源调配");
					record.setMsgType(new DataDict("CUSTOMER_MSG"));
					List<Role> roles = userService.getCurrentLoginUserRoles();
					String mainRoleName = "";
					for (Role role : roles) {
						if (role.getRoleId().equals(currentUser.getRoleId())) {
							mainRoleName = role.getName();
							break;
						}
					}
					record.setMsgContent(mainRoleName + "-" + currentUser.getName() + "已分配" + cusIdArray.length + "位新的客户至您的客户列表，请及时对该客户进行跟进，或在pc端进行再次分配");
					record.setMsgRecipient(new User(deliverTarget));
					String currentUserId = currentUser.getUserId();
					record.setCreateUserId(currentUserId);
					record.setCreateTime(DateTools.getCurrentDateTime());
					record.setModifyUserId(currentUserId);
					record.setModifyTime(DateTools.getCurrentDateTime());
					record.setPushMsgType(PushMsgType.SYSTEM_NOTICE);
					record.setSysMsgType(SysMsgType.CUSTOMER);
					record.setSentTime(DateTools.getCurrentDateTime());
					this.sendCostomerDistributionSysMsg(record, mobileUser);
				}
			}
		}
		
		return re;
	}
	
	private void sendCostomerDistributionSysMsg(SentRecord record, MobileUser mobileUser) {
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendSysMsgCostomerDistributionThread thread = new SendSysMsgCostomerDistributionThread(record, mobileUser);
		taskExecutor.execute(thread);
	}
	
	public void getBelongResourcePool(Customer customerPaddingExtract,Organization rrrpool){
		ResourcePool repool2 = resourcePoolService.findResourcePoolById(rrrpool.getBelong());
		if(repool2==null){//所属没有资源池的话就找父类。
			ResourcePool repool3 = resourcePoolService.findResourcePoolById(rrrpool.getParentId());
			if(repool3!=null  && repool3.getStatus().equals(ValidStatus.VALID)){
				customerPaddingExtract.setDeliverTarget(rrrpool.getParentId());
			}
		}else if(repool2!=null  && repool2.getStatus().equals(ValidStatus.VALID)){
			customerPaddingExtract.setDeliverTarget(rrrpool.getBelong());
		}
	}

	
	/**
	 * 前台接收确定客户转校客户
	 */
	@RequestMapping(value = "/confirmTurnCampusCustomer", method =  RequestMethod.GET)
	@ResponseBody
	public Response confirmTurnCampusCustomer(@RequestParam String cusId) {
		Customer customerPaddingExtract = customerService.findById(cusId);
		customerPaddingExtract.setRecordUserId(userService.getCurrentLoginUser());
		customerPaddingExtract.setTransferStatus("1");
		customerPaddingExtract.setRecordDate(DateTools.getCurrentDateTime());
		//转校接收时 跟进人为当前校区，状态新增，
		customerPaddingExtract.setDeliverTarget(userService.getBelongCampus().getId());
		customerPaddingExtract.setDealStatus(CustomerDealStatus.NEW);
		customerPaddingExtract.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
		customerPaddingExtract.setTransferFrom(userService.getBelongCampus().getId());
		customerService.saveOrUpdateCustomer(customerPaddingExtract);
		String description="前台接收确定客户转校客户-操作人：" + customerPaddingExtract.getRecordUserId().getName();
		customerEventService.saveCustomerDynamicStatus(cusId, CustomerEventType.DELIVER, description);
		return new Response();
	}
	
	@RequestMapping(value = "/releaseCustomerToUndeliver", method =  RequestMethod.GET)
	@ResponseBody
	public Response releaseCustomerToUndeliver(@RequestParam String cusId) {
		
		String [] cusIdArray = cusId.split(",");
		for (String id : cusIdArray) {
			customerService.releaseCustomer(new Customer(id));
		}
		
		customerEventService.saveCustomerDynamicStatus(cusId, CustomerEventType.FOLLOWING, "释放客户");
		
		return new Response();
	}
	
	@RequestMapping(value = "/savePenddingStudents", method =  RequestMethod.POST)
	@ResponseBody
	public Response savePenddingStudents(Set<StudentImportVo> studentImportVos,String customerId) {
		customerService.savePaddingStudentJsonInfo(studentImportVos,customerId); 
		return new Response();
	}
	
	/**
	 * 删除潜在学生
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value = "/delPotentialStudent", method =  RequestMethod.POST)
	@ResponseBody
	public Response delPotentialStudent(@RequestParam String stuId){
		customerService.delPotentialStudent(stuId); 
		return new Response();
	}

	
	@RequestMapping(value = "/getFollowingRecord", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getFollowingRecord(@ModelAttribute GridRequest gridRequest, @ModelAttribute Customer cus) throws Exception {
		DataPackageForJqGrid dataPackageForJqGrid = null;
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.gtCustomerFollowingRecords(cus, dataPackage); 
		dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}

	
	/**
	 * 保存通话日志
	 * @param follow
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveFollowRecord", method =  RequestMethod.GET)
	@ResponseBody
	public Response saveFollowRecord(@ModelAttribute CustomerFolowupVo follow,HttpServletRequest request) throws Exception {
		if (!StringUtils.isBlank(follow.getRemark())) {
			follow.setRemark(URLDecoder.decode(follow.getRemark(), "utf-8"));
		}
		customerService.saveCustomerFollowRecord(follow); 
		return new Response();
	}
	
	@RequestMapping(value = "/addCustomerAppointment", method =  RequestMethod.GET)
	@ResponseBody
	public Response addCustomerAppointment(@ModelAttribute CustomerAppointmentVo appointment) throws Exception {
		customerService.addCustomerAppointment(appointment);
		return new Response();
	}
	
	@RequestMapping(value = "/setCustomerNextFollowupTime", method =  RequestMethod.GET)
	@ResponseBody
	public Response setCustomerNextFollowupTime(@ModelAttribute CustomerAppointmentVo appointment) throws Exception {
		customerService.setCustomerNextFollowupTime(appointment);
		return new Response();
	}
	
	@RequestMapping(value = "/getTodayAppointment", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getTodayAppointment(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerAppointment appointment) throws Exception {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = customerService.getTodayAppointment(appointment, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value = "/getTodayFollowup", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getTodayFollowup(@ModelAttribute GridRequest gridRequest) throws Exception {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = customerService.getTodayFollowup( dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value = "/getTodayAppointmentQuantity", method =  RequestMethod.GET)
	@ResponseBody
	public Integer getTodayAppointmentQuantity(@ModelAttribute CustomerAppointment appointment) throws Exception {
		DataPackage dataPackage = new DataPackage();
		dataPackage.setPageSize(999);//获取所有的记录
		dataPackage = customerService.getTodayAppointment(appointment, dataPackage); 
		return dataPackage.getDatas().size();
	}
	
	@RequestMapping(value = "/confirmAppointment", method =  RequestMethod.GET)
	@ResponseBody
	public Response confirmAppointment(@RequestParam String appointId,@RequestParam String visitType) throws Exception {
		customerService.appointmentConfirm(appointId,visitType); 
		return new Response();
	}
	
	@RequestMapping(value = "/getCustomerDeliveTargets", method =  RequestMethod.GET)
	@ResponseBody
	public List<CustomerDeliverTarget> getCustomerDeliveTargets(CustomerDeliverType deliverType, String campusId, String cusRecordDate, String cusOrg) {
		return customerService.getCustomerDeliveTargets(deliverType, campusId, cusRecordDate, cusOrg);
	}
	
	@RequestMapping(value = "/findCustomerDynamicStatusByCustomerId", method =  RequestMethod.GET)
	@ResponseBody
	public List<CustomerDynamicStatusVo> findCustomerDynamicStatusByCustomerId(@RequestParam String customerId) {
		return (List<CustomerDynamicStatusVo>) customerEventService.findCustomerDynamicStatusByCustomerId(customerId, new DataPackage(0, 999)).getDatas();
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
	 * 页面查询条件- 根据跟进对象类型查询具体的跟进对象名称
	 * @param deliverType
	 * @return
	 */
	@RequestMapping(value = "/getCustomerTargetByDeliveTypeForSelection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getCustomerTargetByDeliveTypeForSelection(@RequestParam CustomerDeliverType deliverType) {
		List<CustomerDeliverTarget> roles=customerService.getCustomerTargetByDeliveType(deliverType);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for(CustomerDeliverTarget target : roles){
				nvs.add(SelectOptionResponse.buildNameValue(target.getTargetName(),target.getTargetId()));
		}
		return new SelectOptionResponse(nvs);
	}
	
	/**
	 * 获取资源的id name
	 * @param cusVo
	 * @return
	 */
	@RequestMapping(value = "/getCustomerNames")
	@ResponseBody
	public List<CustomerVo> getCustomerNames(CustomerVo cusVo){
		return customerService.getCustomerNames(cusVo);
	}
	
	/**
	 * 获取客户资源autoComplete
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "/getAutoCompleteByCustomer", method =  RequestMethod.GET)
	@ResponseBody
	public List<AutoCompleteOptionVo> getAutoCompleteByCustomer(@RequestParam String term) {
		List<Customer> resList = customerService.getCustomersForAutoCompelate(term);
		List<AutoCompleteOptionVo> optionVos =  new ArrayList<AutoCompleteOptionVo>();
		for(Customer cus: resList){
			AutoCompleteOptionVo auto=new AutoCompleteOptionVo();
			if(StringUtils.isNotEmpty(cus.getContact()))
				auto.setLabel(cus.getName()+"（"+cus.getContact()+"）");
			else
				auto.setLabel(cus.getName());
			auto.setValue(cus.getId());
			optionVos.add(auto);
		}
		return optionVos;
	}
	
	/**
	 * 前台确认客户
	 * @param customerId
	 */
	@RequestMapping(value = "/recepitionistConfirmCustomer", method =  RequestMethod.GET)
	@ResponseBody
	public Response recepitionistConfirmCustomer(String customerId) {
		customerService.recepitionistConfirmCustomer(customerId);
		return new Response(); 
	}
	
	/**
	 * 根据orgLevel 获取所有上级客户资源池 (为空  默认当前登陆用户的)
	 * @param orgLevel
	 * @return
	 */
	@RequestMapping(value = "/getCustomerPool", method =  RequestMethod.GET)
	@ResponseBody
	public List<CustomerPoolVo> getCustomerPool(String orgLevel){
		return customerService.getCustomerPool(orgLevel);
	}
	
	/**
	 * 根据orgLevel 获取所有上级客户资源池 和可分配的人(为空  默认当前登陆用户的)
	 * @param orgLevel
	 * @return
	 */
	@RequestMapping(value = "/getCustomerPoolAndUser", method =  RequestMethod.GET)
	@ResponseBody
	public List<CustomerPoolVo> getCustomerPoolAndUser(String orgLevel){

		return customerService.getCustomerPoolAndUser(orgLevel);
	}
	
	/**
	 * 根据orgLevel 获取所有上级客户资源池 和可分配的人(为空  默认当前登陆用户的)
	 * @param orgLevel
	 * @return
	 */
	@RequestMapping(value = "/getCustomerPoolAndUserWithDI", method =  RequestMethod.GET)
	@ResponseBody
	public List<CustomerPoolVo> getCustomerPoolAndUserWithDI(String orgLevel){
		return customerService.getCustomerPoolAndUserWithDI(orgLevel);
	}
	
	
	
	/**
	 * 判断用户是否是转校预约上班
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkCustomerTurnCampus", method =  RequestMethod.GET)
	@ResponseBody
	public Response checkCustomerTurnCampus(@RequestParam String customerId) throws Exception {
		Response response= new Response();
		boolean bool = customerService.checkCustomerTurnCampus(customerId); 
		response.setResultMessage(""+bool);
		return response;
	}
	
	
	/**
	 * 根据电话查询客户
	 * @param contact 电话
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findCustomerByContact")
	@ResponseBody
	public CustomerVo findCustomerByContact(@RequestParam String contact) throws Exception {
		return customerService.findCustomerByPhone(contact); 
	}
	
	/**
	 * 前台预约信息
	 */
	@RequestMapping(value="/getCustomerAppointment")
	@ResponseBody
	public DataPackageForJqGrid getCustomerAppointment(@ModelAttribute GridRequest gridRequest,@ModelAttribute CustomerAppointmentVo customerAppointmentVo){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.getCustomerAppointment(customerAppointmentVo,dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	
	
	/**
	 * 得到当前用户签单的客户
	 */
	@RequestMapping(value="/getAllContractCustomer")
	@ResponseBody
	public DataPackageForJqGrid getAllContractCustomer(@ModelAttribute GridRequest gridRequest,StudentVo studentVo,String type){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.getAllContractCustomer(studentVo,dataPackage,type);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 预约信息登记
	 */
	@RequestMapping(value="/findCustomeraAppointmentById")
	@ResponseBody
	public CustomerAppointmentVo findCustomeraAppointmentById(@RequestParam String id){		
		return customerService.findCustomeraAppointmentById(id);
	}
	
	/**
	 * 预约信息登记
	 */
	@RequestMapping(value="/findCustomeraFollowUpById")
	@ResponseBody
	public CustomerFolowupVo findCustomeraFollowUpById(@RequestParam String id){		
		return customerService.findCustomeraFollowUpById(id);
	}
	
	/**
	 * 保存预约信息登记
	 */
	@RequestMapping(value="/customerAppointmentVisit")
	@ResponseBody
	public Response customerAppointmentVisit(@RequestParam String id,@RequestParam String customerId,
			String visitId,
			String deliverTargetId){
		customerService.customerAppointmentVisit(id, customerId, visitId, deliverTargetId);
		return new Response();
		
	}
	
	/**
	 * 保存预约信息登记
	 */
	@RequestMapping(value="/signCustomerComeon")
	@ResponseBody
	public Response signCustomerComeon(@RequestParam String id,@RequestParam String customerId,
			String visitId,
			String deliverTargetId){
		customerService.signCustomerComeon(id, customerId, visitId, deliverTargetId);
		return new Response();
		
	}
	
	/*@RequestMapping(value="/uploadCustomerResourceModel")
	@ResponseBody
	public CustomerDataImport uploadCustomerResourceModel(@RequestParam(value = "path") MultipartFile path,@RequestParam String deliverTargetName,
			@RequestParam String dealStatus,@RequestParam String deliverType,@RequestParam String deliverTarget,@RequestParam String assign,
			HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception{
		String result1="";
		String fileUrl1="";
		Integer code=0;
		Integer failNum=0;
		if(".xlsx".equals(path.getOriginalFilename().substring(path.getOriginalFilename().lastIndexOf(".")).toLowerCase())
				|| ".xls".equals(path.getOriginalFilename().substring(path.getOriginalFilename().lastIndexOf(".")).toLowerCase())){
			String rootPath=getClass().getResource("/").getFile().toString();
			String fileName=path.getOriginalFilename().substring(0,path.getOriginalFilename().lastIndexOf("."))
					+"_"+DateTools.getCurrentExactDateTime()+path.getOriginalFilename().substring(path.getOriginalFilename().lastIndexOf("."));
			String uploadFile=rootPath.substring(0,rootPath.lastIndexOf("WEB-INF"))+"uploadfile/"+fileName;
			uploadFile=new File(uploadFile).getPath().replaceAll("%20", " ");
			boolean  isUploadFinish = FileUtil.readInputStreamToFile(path.getInputStream(),uploadFile);
			if(isUploadFinish){
				//要取值assign_where
				List<List<Object>> list=ReadExcel1.readExcel(new File(uploadFile));
				//条件判断，是CustomerDeliverType.CUSTOMER_RESOURCE_POOL 还是 CustomerDeliverType.PERSONAL_POOL 暂时未加
				String[] strs=new String[]{dealStatus,deliverTarget,deliverTargetName};
				DataImportResult dir=customerService.customerDataImportByList(list,CustomerDeliverType.valueOf(CustomerDeliverType.class, deliverType),strs); //不能填空
				//将导入数据的结果添加到文件的最后一行
				ReadExcel1.writeExcel1(new File(uploadFile), dir, uploadFile, 0);
				//ReadExcel.writeExcel(resList, uploadFile, sheetAt, writeRow);
				session.setAttribute("importResult", dir.getResult());
				result1=dir.getResult();
				String fileUrl=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/uploadfile/"+fileName;
				session.setAttribute("fileUrl", fileUrl);
				fileUrl1=fileUrl;
				code=Integer.valueOf(dir.getResultCode());
				failNum=dir.getFailNum();
			}
		}
		return new CustomerDataImport(result1,fileUrl1,true,code,failNum);
	}*/

	@RequestMapping(value="/uploadCustomerResourceNoAssignModel",method =  RequestMethod.POST)
	@ResponseBody
	public CustomerDataImport uploadCustomerResourceNoAssignModel(CustomerPoolDataVo customerPoolDataVo,@RequestParam("path") MultipartFile path,
			HttpServletRequest request) throws Exception{
		CustomerDataImport cdi=null;
		if(".xlsx".equals(path.getOriginalFilename().substring(path.getOriginalFilename().lastIndexOf(".")).toLowerCase())
				|| ".xls".equals(path.getOriginalFilename().substring(path.getOriginalFilename().lastIndexOf(".")).toLowerCase())){
			String rootPath=getClass().getResource("/").getFile().toString();  
			String fileName=path.getOriginalFilename().substring(0,path.getOriginalFilename().lastIndexOf("."))
					+"_"+DateTools.getCurrentExactDateTime()+path.getOriginalFilename().substring(path.getOriginalFilename().lastIndexOf("."));
			String uploadFile=rootPath.substring(0,rootPath.lastIndexOf("WEB-INF"))+"uploadfile/"+fileName;
//			String uploadFile=rootPath.substring(0,rootPath.lastIndexOf("target"))+"/target/eduboss/uploadfile/"+fileName;
			uploadFile=new File(uploadFile).getPath().replaceAll("%20", " ");
			boolean  isUploadFinish = FileUtil.readInputStreamToFile(path.getInputStream(),uploadFile);
			if(isUploadFinish){
				cdi = customerService.customerDataImportByFile(new File(uploadFile),customerPoolDataVo);
			}else {
				cdi = new CustomerDataImport("-","-",false,0,0,"上传文件出现异常，请重试！");
			}
		}else {
			cdi = new CustomerDataImport("-","-",false,0,0,"上传失败，不支持该文件类型！");
		}
		return cdi;
	}
	
	/**
	 * 前台客户资源
	 */
	@RequestMapping(value = "/ReCeptionistGetCustomers", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid ReCeptionistGetCustomers(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerVo cus, String onlyShowUndelive,
			String startDate,String endDate) {
		log.info("getCustomersForJqGrid() start.");
		
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.ReCeptionistGetCustomers(startDate,endDate,cus, dataPackage, "true".equalsIgnoreCase(onlyShowUndelive)?true:false);
		
		log.info("getCustomersForJqGrid() end.");
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 前台客户资源
	 */
	@RequestMapping(value = "/getCustomerListForPlat", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getCustomerListForPlat(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerVo cus) {
		log.info("getCustomerListForPlat() start.");
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.getCustomerListForPlat(cus, dataPackage);
		log.info("getCustomerListForPlat() end.");
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 客户资源导入明细
	 */
	
	@RequestMapping(value = "/customerImportInfo", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid customerImportInfo(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerImportTransform cif,
			String startDate,String endDate,String gradeName) {
		DataPackage dataPackage=new DataPackage(gridRequest);
		dataPackage = customerService.getCustomerImportInfo(cif,startDate,endDate,gradeName, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
		
		
	}
	
	/**
	 * 修改客户导入信息
	 */
	
	@RequestMapping(value = "/editImportCustomer", method =  RequestMethod.GET)
	@ResponseBody
	public Response editImportCustomer(@ModelAttribute GridRequest gridRequest ,@ModelAttribute CustomerImportTransform cif,
			@ModelAttribute StudentImportVo stu, String ws
			){
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			if (StringUtil.isNotBlank(ws)){
				customerService.delImCus(ws);
			}else if (StringUtils.isNotBlank(cif.getId())){
				customerService.delImportCustomer(cif);
			}
		} else {
			customerService.editImportCustomer(cif,stu,false);
		}
		return new Response();
	}
	
	/**
	 * 查询客户导入信息
	 */
	@RequestMapping(value = "/getImportCustomerById", method =  RequestMethod.GET)
	@ResponseBody
	public CustomerImportTransform getImportCustomerById(@RequestParam String id){		
		return customerService.getImportCustomerById(id);
	}
	
	/**
	 * 批量导入客户
	 */
	@RequestMapping(value = "/batchImportCustomer", method =  RequestMethod.GET)
	@ResponseBody
	public Response batchImportCustomer(@RequestParam String ids){
		customerService.batchImportCustomer(ids);
		return new Response();
	}
	
//-----------------------------merge uat分割线 2016-12-06-------------------------------------------------------------------
	
	/**
	 * 根据主审核记录的id 查询 客户的无效客户 子审核记录  以及转介绍审核记录
	 */
	@RequestMapping(value = "/getAuditRecordById", method =  RequestMethod.GET)
	@ResponseBody
	public List<AuditRecordVo> getAuditRecordById(@RequestParam String parentId,@RequestParam String type){
		
		if("invalid".equals(type)){
			return invalidCustomerAuditService.getAuditRecordById(parentId);
		}
		if("transfer".equals(type)){
			return transferCustomerService.getAuditRecordById(parentId);
		}
		return null;
	}
	
	
	
	
	
	/**
	 * 跟进客户管理--将客户置为无效 (无效标记)
	 * 根据customerId 修改客户的状态 同时插入一条审核记录
	 * 重构新增
	 * @param customerId
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/setCustomerInvalid", method =  RequestMethod.POST)
	@ResponseBody
	public Response setCustomerInvalid(@RequestParam String customerId,@RequestParam String remark) {
		//根据customerId获取客户的信息
		if(customerId==null)throw new ApplicationException("客户的编号不能为空");
//		CustomerVo customerVo= customerService.findCustomerById(customerId);
		Customer customer= customerService.findById(customerId);
		if(customer==null)throw new ApplicationException("该客户不存在");
		User refUser = userService.getCurrentLoginUser();
		String userId = refUser.getUserId();
		//防止上工序标记他人正在跟进中的客户为无效
		if(!userId.equals(customer.getDeliverTarget())){
			return new Response(-1, "该客户已有其他人正在跟进，将客户置为无效失败");
		}
		
	    Long result = invalidCustomerAuditService.setCustomerInvalid(customer,remark);	
		if(result>0){
			
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.INVALID);
			dynamicStatus.setDescription("客户被置为无效");
			if(customer.getResEntrance()!=null){
			   dynamicStatus.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus,refUser);
			
			
			
			
			//插入记录成功，只有审核通过后才将客户的状态置为无效
			//判断标记为为无效的人是否是网络专员 网络主管 外呼主管外呼专员 把前工序也去掉
			
			Boolean isWLZY = userService.isUserRoleCode(userId, RoleCode.NETWORK_SPEC);
			Boolean isWLZG = userService.isUserRoleCode(userId, RoleCode.NETWORK_MANAGER);
			Boolean isWHZY = userService.isUserRoleCode(userId, RoleCode.OUTCALL_SPEC);
			Boolean isWHZG = userService.isUserRoleCode(userId, RoleCode.OUTCALL_MANAGER);			
			if(isWLZY||isWLZG||isWHZY||isWHZG){
				customer.setBeforeDeliverTarget(null);
			}
			customer.setDealStatus(CustomerDealStatus.TOBE_AUDIT);
			customerService.updateCustomer(customer);		
			return new Response(0, customerId);
		}else{
			return new Response(-1, "将客户置为无效失败");
		}
		
	}
	
	/**
	 * 客户管理工作台--无效客户审核    审核 无效客户审核的记录
	 * 重构新增
	 * @param recordVo
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/auditInvalidCustomer", method =  RequestMethod.POST)
	@ResponseBody
    public Response auditInvalidCustomer(InvalidCustomerRecordVo recordVo,String deliverType, String deliverTarget,String dealStatus) {
    	//考虑可以再次审核的情况
		//无效审核没有 流失抢客的概念
		//recordVo要有recordId 根据id进行审核
    	//使用CustomerVo封装分配客户条件  此处的资源入口不会变
    	CustomerVo customerVo = new CustomerVo();
    	if(StringUtils.isNotBlank(deliverTarget)){
    		customerVo.setDeliverTarget(deliverTarget);
    	}
    	if(StringUtils.isNotBlank(deliverType)){
    		customerVo.setDeliverType(CustomerDeliverType.valueOf(deliverType));
    	}else{
    		//无效客户审核一旦曾经通过后就会释放到资源池中，所以deliverType参数可能为空 但是后面需要用到该参数 
    		if(StringUtils.isNotBlank(deliverTarget)){
    			User userDeliverTarget = userService.loadUserById(deliverTarget);
    			if(userDeliverTarget!=null){
    				customerVo.setDeliverType(CustomerDeliverType.PERSONAL_POOL);    	    		
    			}else{
    				customerVo.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);    	
    			}
    		}else{
    			customerVo.setDeliverType(CustomerDeliverType.PERSONAL_POOL);  
    		}
    		
    	}
    	if(StringUtils.isNotBlank(dealStatus)){
    		customerVo.setDealStatus(CustomerDealStatus.valueOf(dealStatus));
    	}
	    User user=userService.getCurrentLoginUser();
	    String currentTime=DateTools.getCurrentDateTime();
	    recordVo.setModifyTime(currentTime);
	    recordVo.setModifyUserId(user.getUserId());
	    //无效审核的流失客户的统计
	    return invalidCustomerAuditService.auditInvalidCustomer(recordVo,customerVo);
    	
    }
	
	/**
	 * 客户管理工作台--无效客户审核  获取 需要进行审核的无效客户的审核列表
	 * 重构新增
	 * @param gridRequest
	 * @param recordVo
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/getInvalidCustomerAuditRecords", method =  RequestMethod.GET)
	@ResponseBody
    public DataPackageForJqGrid getInvalidCustomerAuditRecords(@ModelAttribute GridRequest gridRequest,@ModelAttribute InvalidCustomerRecordVo recordVo) throws Exception{
		//外呼主管只能查看外呼专员提交的审核  网络主管只能查看网络专员提交的审核  校区主任只能看见咨询师提交的审核   
		DataPackageForJqGrid dataPackageForJqGrid = null;
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = invalidCustomerAuditService.getInvalidCustomerRecords(recordVo, dataPackage);
		dataPackageForJqGrid = new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;				
    }
    
	/**
	 * 客户管理--无效审核 --查看审核结果
	 * @param customerId
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/loadInvalidCustomerResult", method =  RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loadInvalidCustomerResult(@RequestParam String id) throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(id))throw new ApplicationException("审核记录ID不能为空");		
	    InvalidCustomerRecordVo vo=invalidCustomerAuditService.loadInvalidCustomerResult(id);
	    if(vo!=null){
            map.put("result", vo);
            map.put("resultCode", 0);
            map.put("resultMessage", "无效审核结果");
		}else{
            map.put("result", null);
            map.put("resultCode", -1);
            map.put("resultMessage", "查询返回空");			
		}
	    
	    return map;
	    
	}
	
	
	/**
	 * 客户管理     转介绍登记 保存转介绍的客户信息
	 * 重构新增
	 * @param customerVo
	 * @return
	 * @throws Exception
	 * 确认标记
     */
	@RequestMapping(value = "/saveTransferCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveTransferCustomer(@RequestBody CustomerVo customerVo) throws Exception {
         
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
	 * 客户管理-转介绍审核  ---查询转介绍客户的审核记录
	 * 重构新增
	 * xiaojinwang
	 * @param gridRequest
	 * @param tRecordVo
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/getTransferCustomers", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getTransferCustomers(@ModelAttribute GridRequest gridRequest, @ModelAttribute TransferCustomerRecordVo tRecordVo) throws Exception {
		log.debug("查询转介绍客户的审核记录");
		DataPackageForJqGrid dataPackageForJqGrid = null;
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = transferCustomerService.getTransferCustomers(tRecordVo, dataPackage);
		dataPackageForJqGrid = new DataPackageForJqGrid(dataPackage);
		log.debug("查询转介绍客户的审核记录数量:"+dataPackageForJqGrid.getTotal());
		return dataPackageForJqGrid;
	}
	
	
	/**
	 * 客户管理-转介绍审核记录  ---查询转介绍登记人自己的提交的审核记录
	 * 重构新增
	 * xiaojinwang
	 * @param gridRequest
	 * @param tRecordVo
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/getTransferAuditRecords", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getTransferAuditRecords(@ModelAttribute GridRequest gridRequest, @ModelAttribute TransferCustomerRecordVo tRecordVo) throws Exception {
		System.out.println("查询转介绍登记人的提交的审核记录");
		DataPackageForJqGrid dataPackageForJqGrid = null;
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = transferCustomerService.getTransferAuditRecords(tRecordVo, dataPackage);
		dataPackageForJqGrid = new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}

	/**
	 * 客户管理-转介绍审核      审核  转介绍客户
	 * 重构新增
	 * 修改转介绍资源来源为转介绍 cusTYPE INTRODUCE  资源入口为RES_ENTRANCE  TRANSFER
     * @param tRecordVo
     * @param deliverType
     * @param deliverTarget
     * @param dealStatus  如果要重新分配客户的话 状态应该是STAY_FOLLOW 
     * @return
     * @throws Exception
     * 
     */
	@RequestMapping(value = "/auditTransferCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response auditTransferCustomer(@ModelAttribute TransferCustomerRecordVo tRecordVo,@RequestParam(required=false) String deliverType,@RequestParam(required=false) String deliverTarget,@RequestParam(required=false) String dealStatus) throws Exception {
		//考虑可以再次审核的情况
		log.debug("审核 转介绍客户");
	    if(tRecordVo==null)throw new ApplicationException("审核记录不存在");
    	//使用CustomerVo封装分配客户条件   一般是分配咨询师 然后跟进状态是待跟进stay_follow  deliverType是个人池
    	CustomerVo customerVo = new CustomerVo();
    	if(StringUtils.isNotBlank(deliverTarget)){
    		customerVo.setDeliverTarget(deliverTarget);
    	}
    	if(StringUtils.isNotBlank(deliverType)){
    		customerVo.setDeliverType(CustomerDeliverType.valueOf(deliverType));
    	}
    	if(StringUtils.isNotBlank(dealStatus)){
    		customerVo.setDealStatus(CustomerDealStatus.valueOf(dealStatus));
    	}    	  	
	    User user=userService.getCurrentLoginUser();
	    String currentTime=DateTools.getCurrentDateTime();
	    tRecordVo.setModifyTime(currentTime);
	    tRecordVo.setModifyUserId(user.getUserId());
	    tRecordVo.setAuditTime(currentTime);
	    //审核通过要进行客户分配  添加抢客记录 添加 数据统计事件  审核不通过 则发送系统通知 
	    return transferCustomerService.auditTransferCustomer(tRecordVo,customerVo);
	}
	
	/**
	 * 客户管理--转介绍审核列表--查看审核结果
	 * @param id
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/loadTransferCustomerResult", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loadTransferCustomerResult(@RequestParam String id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		TransferCustomerRecordVo transferCustomerRecordVo = transferCustomerService.loadTransferCustomerResult(id);
		if (transferCustomerRecordVo != null) {
            map.put("result", transferCustomerRecordVo);
            map.put("resultCode", 0);
            map.put("resultMessage", "转介绍审核结果");
		}else{
            map.put("result", null);
            map.put("resultCode", -1);
            map.put("resultMessage", "查询返回空");			
		}
		return map;
	}
    /**
	 * 分配客户管理--根据条件查询符合条件的客户列表 (在分配客户的时候需要在资源的入口字段设置   RES_ENTRANCE  )
	 * getDistributeCustomers（替代接口getCustomerListForPlat）
     * @param gridRequest  RES_ENTRANCE
     * @param vo 条件查询VO
     * @param workbrenchType 不同的平台标识 外呼: 外呼 咨询师：CALL_OUT 网络：ON_LINE
     * @author xiaojinwang
     * 
     * "dealStatus":"STAY_FOLLOW", //待分配未确认的
     * "deliverType":"CUSTOMER_RESOURCE_POOL", //池  PERSONAL_POOL
     * "deliverTarget": organizationId 
     *  确认标记
     */
	@RequestMapping(value = "/getDistributeCustomersForPlat", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getDistributeCustomersForPlat(@ModelAttribute GridRequest gridRequest, @ModelAttribute DistributeCustomerVo vo,@RequestParam String workbrenchType)throws Exception{
		log.debug("获取分配管理客户列表");		
		if(workbrenchType==null)throw new ApplicationException("平台类型不能为空");
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
        //调用service层获取代码	传入dataPackage vo  workbrenchType
		//根据不同的分配类型来进行区分查询
		if(vo.getDeliverType().equals(CustomerDeliverType.PERSONAL_POOL.getValue())){
			//个人池
			dataPackage = customerService.getDistributeCustomersPersonPool(vo, dataPackage, workbrenchType);
		}else{
			//公共资源池
			//前端通过其他方法遍历出所有的公共资源池   这里要传入资源池的ID作为deliverTarget
			dataPackage = customerService.getDistributeCustomersResourcePool(vo, dataPackage, workbrenchType);
		}
		
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 分配客户管理--获取多个客户(从个人或者其他资源池获取)
	 * @param cusId
	 * @param poolType  如果poolType==person 则从个人资源池获得客户资源  common表示其他资源池
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/distributeCustomer",method = RequestMethod.POST)
	@ResponseBody
	public Response distributeCustomer(@RequestParam String cusId,@RequestParam String poolType,@RequestParam String workbrenchType) throws Exception {	
		//添加资源量
		//校验参数
		if(cusId==null)throw new ApplicationException("没有要获取的客户资源");						
		return customerService.distributeCustomer(cusId, poolType,workbrenchType);
}	
	/**
	 * 跟进客户管理--跟进客户       条件查询获取跟进客户列表
	 * @param workbrenchType 不同的平台标识 外呼: 外呼 咨询师：CALL_OUT 网络：ON_LINE
	 * @param gridRequest
	 * @param vo
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/getFollowupCustomersForPlat", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getFollowupCustomers(@ModelAttribute GridRequest gridRequest, @ModelAttribute FollowupCustomerVo vo,@RequestParam String workbrenchType)throws Exception{
		log.debug("获取跟进管理客户列表");
		if(workbrenchType==null)throw new ApplicationException("平台类型不能为空");
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);		
		//外呼 网络的跟进列表 查询  beforeDeliverTarget或者是deleverTarget是当前userId 咨询师是deliverTarget 
        //调用service层获取代码	传入dataPackage vo  workbrenchType
		dataPackage = customerService.getFollowupCustomers(vo, dataPackage, workbrenchType);
		return new DataPackageForJqGrid(dataPackage);
	}
    	
	/**
	 * 客户管理--客户列表    
	 * 替换原来的接口  getCustomersForJqGrid
	 * @param customerVo 
	 * 确认标记   
	 */
	@RequestMapping(value = "/getAllCustomers", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getAllCustomers(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerVo vo)throws Exception{
		//log.debug("客户管理获取客户列表");
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.getAllCustomers(vo, dataPackage);	
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
     * 客户管理--无效电话列表
     */
    @RequestMapping(value = "/getDisCusForJqGrid", method =  RequestMethod.GET)
    @ResponseBody
    public DataPackageForJqGrid getDisCusForJqGrid(@ModelAttribute GridRequest gridRequest, String contact)throws Exception{
        //log.debug("客户管理获取客户列表");
        DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
        dataPackage = disabledCustomerService.getDisCusForJqGrid(dataPackage, contact);
        return new DataPackageForJqGrid(dataPackage);
    }
	
	/**
	 * 客户管理--客户列表  (网络专用)  
	 * 替换原来的接口  getCustomersForJqGrid
	 * @param customerVo 
	 * 确认标记   
	 */
	@RequestMapping(value = "/getCustomersForInternet", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getCustomersForInternet(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerVo vo)throws Exception{
		//log.debug("客户管理获取客户列表");
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.getCustomersForInternet(vo, dataPackage);	
		return new DataPackageForJqGrid(dataPackage);
	}
	
	

	/**
	 * 跟进客户管理--释放多个客户到资源池
	 * @param cusId  待释放的客户资源Id
	 * @param deliverTarget 目标资源池
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/releaseCustomer",method = RequestMethod.POST)
	@ResponseBody
	public Response releaseCustomer(@RequestParam String cusId,@RequestParam String deliverTarget) throws Exception {	
		 //添加数据埋点   customer_dynamic_status 释放客户
		Response response = new Response();	
		//校验参数
		if(cusId==null)throw new ApplicationException("没有要释放的客户资源");	
		
		if(deliverTarget==null)throw new ApplicationException("无法释放资源");
		//获取多个客户
		String [] cusIdArray = cusId.split(",");
		User user=userService.getCurrentLoginUser();
        if(user==null) throw new ApplicationException("系统出错");      
        //调用service层
        response= customerService.releaseCustomer(deliverTarget, cusIdArray, user);	
		return response;	
	}
	
	/**
	 * 跟进管理--添加预约上门信息
	 * 跟进管理--添加跟进记录(很多地方都会调用该接口)    网络工作台 改名分配校区
	 * @param follow
	 * @param request(去掉该参数--xiaojinwang)
	 * @return 
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/saveFollowRecord", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveFollowRecord(@ModelAttribute CustomerFolowupVo follow) {
		/*if (!StringUtils.isBlank(follow.getRemark())) {
			try {
				follow.setRemark(URLDecoder.decode(follow.getRemark(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}*/
		//saveCustomerFollowRecord方法 保存客户的跟进记录和预约的信息
		//"appointmentType" :"FOLLOW_UP
		//"appointmentType" :"APPOINTMENT" 预约上门要设置 customerFollowup的visitType为not_come
		//TODO 增加资源量
		Response response = new Response();		
		try {
			customerService.saveCustomerFollowRecord(follow); 
		} catch (ApplicationException e) {
			response.setResultCode(-1);
			response.setResultMessage(e.getErrorMsg());
		}
		
		return response;
	}
	
    //根据客户id返回学生的名字和id 
	@RequestMapping(value = "/getRelatedStudentsByCusId", method =  RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getStudentsByCusId(String customerId){
		//TODO 
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(customerId)){
			map.put("resultCode", -1);
            map.put("result", null);
            map.put("resultMessage", "客户id不能为空");
            return map;
		}	
		return customerService.getStudentsByCusId(customerId);
	}
	
			
//***************暂不处理****************	
	/**
	 * 跟进客户管理 --签订合同 跳转到页面
	 * loadURL("function/contract/editContractNew.html?customerId
	 */
	
	/**
	 * 跟进客户管理--设置跟进提醒（以后做成模版，目前暂时不做）
	 */
//***************暂不处理****************
	/**
	 * @param
	 * 跟进客户管理--流失客户  列表
	 * 确认标记
	 */
	@RequestMapping(value = "/getLostCustomersForPlat", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getLostCustomers(@ModelAttribute GridRequest gridRequest, @ModelAttribute FollowupCustomerVo vo)throws Exception{    
		log.debug("获取跟进客户管理-流失客户列表");
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);	
		//(原型上的录入者  录入者部门去掉 )
		//查询抢客表
        //调用service层获取代码	传入dataPackage vo  workbrenchType
		dataPackage = customerService.getLostCustomers(vo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);	
		
	}
	/**
	 * 跟进客户管理--无效客户客户  列表
	 * 确认标记
	 */
	@RequestMapping(value = "/getInvalidCustomersForPlat", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getInvalidCustomers(@ModelAttribute GridRequest gridRequest, @ModelAttribute FollowupCustomerVo vo)throws Exception{    
		log.debug("获取跟进客户管理-无效客户列表");
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);		
		// 查询无效记录表
		//(原型上的 录入者  录入者部门 要去掉)
        //调用service层获取代码	传入dataPackage vo  
		dataPackage = customerService.getInvalidCustomers(vo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);	
		
	}	
	/**
	 * @param workbrenchType 不同的平台标识 外呼: 外呼 咨询师：CALL_OUT 网络：ON_LINE （跟标识无关 20161027）
	 * 跟进客户管理-已签单的客户列表  当前跟进人
	 * 确认标记
	 */
	@RequestMapping(value = "/getSignupCustomers", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getSignupCustomers(@ModelAttribute GridRequest gridRequest, @ModelAttribute SignupCustomerVo vo)throws Exception{
		log.debug("跟进客户管理 获取已签单客户列表");
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.getSignupCustomers(vo, dataPackage);	
		return new DataPackageForJqGrid(dataPackage);
	}	
	
	
	/**
	 * 跟进客户管理   客户概览(需要 区分平台+当前登录人为跟进人)
	 * @param startDate
	 * @param endDate
	 * @return
	 * 当前接口已经被替代
	 */
	@RequestMapping(value = "/findCustomerDynamicCount", method =  RequestMethod.GET)
	@ResponseBody
	public Map findCustomerDynamicCount(String startDate,String endDate) {
		return customerService.findCustomerDynamicCount(startDate,endDate);
	}
	/**
	 * 新增接口 取代原来的客户概览
	 * 跟进客户管理   客户概览                    
	 * @param workbrenchType 前台 RECEPTION 不同的资源入口--外呼: 外呼 咨询师  呼出：CALL_OUT 网络：ON_LINE 呼入: CALL_IN 直访:DIRECT_VISIT 拉访:OUTSTANDING_VISIT 转介绍：TRANSFER
	 * @param startDate
	 * @param endDate
	 * @return
	 * 确认标记
	 */	
	@RequestMapping(value = "/getCustomerOverviewForPlat", method =  RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getCustomerOverviewForPlat(String startDate,String endDate,String workbrenchType) {
		//外呼、网络、咨询师工作台    新增 待跟进 跟进中 已上门 已签单 释放 无效  查询七种数据
		//通过查询 customer_dynamic_status表的数据来统计
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date begin = format.parse(startDate);
			Date end = format.parse(endDate);
			if (begin.getTime() > end.getTime()) {
				throw new ApplicationException("开始日期必须小于或者等于结束日期");
			}
		} catch (ParseException e) {
			if (log.isDebugEnabled()) {
				log.debug("日期不符合yyyy-MM-dd格式");
			}
			throw new ApplicationException("日期不符合yyyy-MM-dd格式");
		}
		if("RECEPTION".equalsIgnoreCase(workbrenchType)){
			//前台首页的客户概览
			return customerService.getCustomerOverviewForReception(startDate, endDate);
		}else{
			return customerService.getCustomerOverviewForPlat(startDate, endDate, workbrenchType);
		}
		
	}	
	
	
	/**
	 * 前台工作台 和客户管理   ----分配客户管理--分配客户    分配的对象都是网络预约的客户
	 * @param gridRequest
	 * @param vo
	 * @param isDeliver true 表示已分配 false 表示待分配
	 * @return
	 * 确认标记
	 */
	@RequestMapping(value = "/getDistributeCustomers", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getDistributeCustomers(@ModelAttribute GridRequest gridRequest, @ModelAttribute DistributeCustomerVo vo)throws Exception{
		log.debug("获取分配管理客户列表");	
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);		
		//这里的分配管理  是网络预约校区的客户
        //前台工作台的分配客户和客户管理的分配客户是一样的
		dataPackage = customerService.getDistributeCustomers(vo, dataPackage);		
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 客户管理    ----网络资源管理--网络分配给营运经理的客户    分配的对象都是网络分配过来的客户
	 * @param gridRequest
	 * @param vo
	 * @param isDeliver true 表示已分配 false 表示待分配
	 * @return
	 * 确认标记
	 */
	@RequestMapping(value = "/getOperateManagerCustomers", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getOperateManagerCustomers(@ModelAttribute GridRequest gridRequest, @ModelAttribute DistributeCustomerVo vo)throws Exception{
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);		
		dataPackage = customerService.getOperateManagerCustomers(vo, dataPackage);	
		return new DataPackageForJqGrid(dataPackage);
	}
	
	
	
	
	/**
	 * 前台工作台--转校客户管理(包括 已转出  已接收)
	 * @author tangyuping  xiaojinwang 
	 * @param gridRequest
	 * @param vo   vo 的getReceiveOrTransfer 是否等于transfer来确认是否转出转入
	 * @param workbrenchType  前台工作台  RECEPTION  和客户管理工作台  CUSTOMERMANAGER
	 * @return
	 * 确认标记
	 */
	@RequestMapping(value = "/transferCustomerList", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid transferCustomerList(@ModelAttribute GridRequest gridRequest, 
			@ModelAttribute ReceptionistCustomerVo vo,@RequestParam String workbrenchType){		
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		//要将原来的转校客户管理的 转出 已接收客户  放入分配客户管理   
		dataPackage = customerService.transferCustomerList(vo, dataPackage,workbrenchType);
		return new DataPackageForJqGrid(dataPackage);	
	}
	
	

     /**
      * 前台工作台  -来访客户管理  -点击登记加载来访客户信息
      * @param visitId 来访记录Id
      * @author xiaojinwang
      * 
      */
	@RequestMapping(value = "/loadFollowupCustomerById", method = RequestMethod.GET)
	@ResponseBody
	public SignCustomerVo loadFollowupCustomerById(@RequestParam(required = false ) String visitId,@RequestParam String customerId)
			throws Exception {
		//显示分配人的名字如果分配类型是个人   deliverTargetName
		// 判读当前登记时间是最近一次的登记时间相差是否超过30天来修改客户的资源入口
		if (StringUtils.isBlank(customerId))
			throw new ApplicationException("无法查询来访客户");
		SignCustomerVo signCustomerVo = null;
		CustomerVo customerVo = null;
		if(StringUtils.isBlank(visitId)){
			customerVo = new CustomerVo();
			customerVo.setId(customerId);
			signCustomerVo = customerService.loadFollowupCustomerById(visitId, customerVo);	
		}else{
			customerVo = customerService.findCustomerById(customerId);
			// 根据判断条件来确定是否修改客户的资源入口
			signCustomerVo = customerService.loadFollowupCustomerById(visitId, customerVo);	
			
		}
		return signCustomerVo;
		
		
	}
		
	/**
	 * 前台工作台-来访客户管理-预约信息
	 * 客户跟进记录新方法
	 * @param gridRequest
	 * @param cus
	 * @return
	 * @throws Exception
	 * (挪动位置 而已 by xiaojinwang)
	 * 确认标记
	 */
	@RequestMapping(value = "/getFollowingRecordNew", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getFollowingRecord(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerFolowupVo cus) throws Exception {
		DataPackageForJqGrid dataPackageForJqGrid = null;
		//接口功能：前台查询预约信息 ,客户详情查询历史跟进记录
		//appointmentType  预约：APPOINTMENT,跟进  FOLLOW_UP
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);	
		dataPackage = customerService.getCustomerFollowingRecords(cus, dataPackage); 
		dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}
	
	
	
	/**
	 * 前台工作台-来访客户管理 (登记-保存信息)
	 * 保存预约信息登记
	 * @author tangyuping
	 * ( 挪动位置 by xiaojinwang)
	 * 
	 */
	@RequestMapping(value="/signCustomerComeon",method = RequestMethod.POST)
	@ResponseBody
	public Response signCustomerComeon(@RequestBody SignCustomerVo signCustomerVo){
		//增加资源量  增加上门数  通过customerEventService来写
		return customerService.signCustomerComeon(signCustomerVo);
				
	}
	
	/**
	 * 前台工作台-来访客户管理
	 * @author tangyuping 2016/07/30
	 * ( 挪动位置 by xiaojinwang)
	 * 前台客户登记记录列表
	 * 确认标记
	 * 因为要改访问路径所以要废弃
	 */
	@RequestMapping(value = "/findPageCustomerForReceptionistWb", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid findPageCustomerForReceptionistWb(@ModelAttribute GridRequest gridRequest, 
			@ModelAttribute ReceptionistCustomerVo vo){
		DataPackage dataPackage=new DataPackage(gridRequest);
		dataPackage = customerService.findPageCustomerForReceptionistWb(vo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}	

	/**
	 * 前台工作台-来访客户管理
	 * 前台客户登记记录列表
	 * @param gridRequest
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/findCustomeRegistRecord", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid findCustomeRegistRecord(@ModelAttribute GridRequest gridRequest, 
			@ModelAttribute ReceptionistCustomerVo vo){
		DataPackage dataPackage=new DataPackage(gridRequest);
		
		dataPackage = customerService.findPageCustomerForReceptionistWb(vo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 前台工作台  客户管理    分配客户管理--转校操作 转出
	 * @author tangyuping
	 * 客户转校区
	 * ( 挪动位置 by xiaojinwang 20160909)
	 * 确认标记  
	 */
	@RequestMapping(value = "/turnCampusCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response turnCampusCustomer(@RequestParam String cusId, @RequestParam CustomerDeliverType deliverType, @RequestParam String deliverTarget, @RequestParam(required=false) CustomerDealStatus dealStatus) {
		//这里的dealStatus 不是必须的
		System.out.println(cusId+"转校到:"+deliverTarget+"----------");
		Response response = new Response();
		String userCampusId = userService.getBelongCampus().getId();
		//当前的登录者就是转校记录的创建者
		User referUser = userService.getCurrentLoginUser();
		String userId = referUser.getUserId();
		String [] cusIdArray = cusId.split(",");
		for (String id : cusIdArray) {
			Customer customerPaddingExtract = customerService.findById(id);
			if(customerPaddingExtract.getDealStatus()!=null && customerPaddingExtract.getDealStatus()==CustomerDealStatus.FOLLOWING){
		    	response.setResultCode(-1);
		    	response.setResultMessage("该客户已经被跟进中，操作失败");
			}
			String deliverTarget_before = customerPaddingExtract.getDeliverTarget();
			String oldOrganizatonName="";
			if(StringUtils.isNotBlank(customerPaddingExtract.getBlSchool())){
				Organization oldOrganizaton = userService.getOrganizationById(customerPaddingExtract.getBlSchool());
			    if(oldOrganizaton!=null){
			    	oldOrganizatonName=oldOrganizaton.getName();
			    }else{
			    	Organization blCampus = customerPaddingExtract.getBlCampusId();
			    	if(blCampus!=null){
			    		oldOrganizatonName= blCampus.getName();
			    	}
			    }
							
			}
			//customerPaddingExtract.setDeliverType(deliverType);
			customerPaddingExtract.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
			customerPaddingExtract.setDeliverTarget(deliverTarget);
			customerPaddingExtract.setDealStatus(CustomerDealStatus.NEW);
			customerPaddingExtract.setBlSchool(deliverTarget);
			//设置校区 同时用bl_campus blschool
			customerPaddingExtract.setBlCampusId(userService.getOrganizationById(deliverTarget));
			customerPaddingExtract.setTransferFrom(userCampusId);
			customerPaddingExtract.setTransferStatus("0");
			customerPaddingExtract.setTransferTime(DateTools.getCurrentDateTime());
			customerService.saveOrUpdateCustomer(customerPaddingExtract);
			
			TransferCustomerCampus info = null;
		    Customer customer = customerService.findById(id);
		    String transferStatus = customer.getTransferStatus();
		    
		    String recordId = null;
		    
		    if(transferStatus!=null && transferStatus.equals("0")){
		    	List<TransferCustomerCampus> list = customerService.getTransferCustomerRecord(id);
		    	if(list!=null&& list.size()>0){
		    		info = list.get(0);
		    		info.setTransferCampus(userCampusId);
		    		info.setReceiveCampus(deliverTarget);
					info.setDelivetTargetTo(deliverTarget);
					info.setTransferTime(DateTools.getCurrentDateTime());
					info.setCreateTime(DateTools.getCurrentDateTime());
					info.setCreateuser(userId);
					customerService.updateTransferCustomer(info);
					recordId = info.getId();
		    	}else{
		    		//添加客户转校信息
				    info = new TransferCustomerCampus();
					info.setCusId(id);
					info.setTransferCampus(userCampusId);
					info.setReceiveCampus(deliverTarget);
					//设置客户原来的分配目标
					info.setDeliverTargetFrom(deliverTarget_before);
					//设置将要的分配目标
					info.setDelivetTargetTo(deliverTarget);
					info.setTransferTime(DateTools.getCurrentDateTime());
					info.setCreateTime(DateTools.getCurrentDateTime());
					info.setCreateuser(userId);
					recordId = customerService.addTransferCustomer(info);
		    	}
		    }else if(transferStatus!=null && transferStatus.equals("1")){
		    	response.setResultCode(-1);
		    	response.setResultMessage("该客户已经被转校，操作失败");
		    }
			Organization org=userService.getOrganizationById(deliverTarget);
			String description="客户转校区：" + oldOrganizatonName+ " - " + org.getName();
			
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.TRANSFERCUSTOMEROUT);
			dynamicStatus.setDescription(description);
			if(customer.getResEntrance()!=null){
			   dynamicStatus.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setTableName("transfer_customer");
			dynamicStatus.setTableId(recordId);
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);

		}
		
		return response;
	}
	
	/**
	 * 前台工作台  客户管理    分配客户管理--接收校区    接收以后将 客户的状态转为待跟进
	 * @author xiaojinwang
	 * 客户转校区
	 * 确认标记
	 */
	@RequestMapping(value = "/receiveCampusCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response receiveTransferCampusCustomer(@RequestParam String transferCustomerId, @ModelAttribute CustomerVo customerVo) {
	    //customer 封装 id(customerId)deliverType  delivetTarget dealStatus
		//客户的资源入口不变
		return  customerService.receiveTransferCampusCustomer(transferCustomerId, customerVo);
		
		
	
	}
	/**
	 *  外呼工作台、网络工作台、咨询师工作台
	 *  预约客户管理
	 *  @param workbrenchType 不同的平台标识 外呼: 外呼 咨询师：CALL_OUT  (没必要区分不同的平台)
	 *  @author xiaojinwang  
	 *  确认标记 
	 */
	@RequestMapping(value = "/getAppointmentRecordsForPlat", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getAppointmentRecords(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerFolowupVo cus) throws Exception {
		DataPackageForJqGrid dataPackageForJqGrid = null;
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);			
		dataPackage = customerService.getAppointmentRecords(cus, dataPackage); 
		dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}
	
	
	/**
	 * 前台工作台-公共客户查询    前台抢客户  调配
	 * @author tangyuping xiaojinwang
	 * @param gainCustomer 封装 抢客原因  新的分配目标对象id
	 * @param res_Entrance  此参数不是必须的，如果不为空说明资源入口有变  
	 * @return 
	 * (挪动位置 by xiaojinwang 20160912)
	 * 确认标记
	 */
	@RequestMapping(value="/gainCustomer",method = RequestMethod.POST)
	@ResponseBody
	public Response gainCustomer(@ModelAttribute GainCustomer gainCustomer,@RequestParam(required=false) String res_Entrance){
		 //使用重构剥离功能后的方法
		 //Response res = this.deliverCustomer(gainCustomer.getCusId().getId(), CustomerDeliverType.PERSONAL_POOL, gainCustomer.getDeliverTarget(), 
		 //		 CustomerDealStatus.STAY_FOLLOW, null, "0", null);
		 //将复杂的业务逻辑放进gainCustomer里
		 return customerService.gainCustomer(gainCustomer,res_Entrance);
		
	}
		
	/**
	 * 外呼/网络/咨询师工作台 客户管理  前台工作台 ----客户查询  只有前台工作台的查询有操作一栏
	 * 根据电话查询客户，返回列表
	 * @param contact
	 * @return
	 * @throws Exception
	 * 确认标记
	 */
	@RequestMapping(value = "/findCustomerByContactList")
	@ResponseBody
	public DataPackageForJqGrid findCustomerByContactList(@ModelAttribute GridRequest gridRequest, 
			@ModelAttribute CustomerVo vo) throws Exception {
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.findCustomerByContactList(vo,dataPackage);
		DataPackageForJqGrid  d = new DataPackageForJqGrid(dataPackage);
		d.setData(dataPackage.getData());
		return d;
		
	}
    
	/**
	 * 客户录入 --检验手机号码是否存在   在客户列表和学生列表中 父母手机联系方式进行
	 * 接口共用 20170328 转介绍客户也要校验
	 * @param contact
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkCustomerContact")
	@ResponseBody
	public Response checkCustomerContact(@RequestParam String contact, @RequestParam String workbrenchType) throws Exception{
		return customerService.checkCustomerContact(contact, workbrenchType);
	}
	
	/**
	 * 保存客户的时候 判断如果是老客户 学生是否重名
	 * @param contact
	 * @param workbrenchType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkCustomerStudent",method = RequestMethod.POST)
	@ResponseBody
	public Response checkCustomerStudent(@RequestBody CustomerVo customerVo ) throws Exception{
		return customerService.checkCustomerStudent(customerVo);
	}	
	
	
	

	/**
	 * 客户查询   客户在资源池中时候  客户分配
	 * 单独的客户分配接口  选择 分配目标  资源入口是否变更
	 * 客户录入的时候也要选择 分配目标  不是调用此接口 
	 * @param customerVo  需要封装 deliverTarget 和resEntrance 其中resEntrance不是必须的
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/allocateCustomer")
	@ResponseBody
	public Response allocateCustomer(@ModelAttribute CustomerVo customerVo) throws Exception{
		//借用CutomerVo 的lastFollowId 来处理 前台工作台 分配客户列表的 分配咨询师和更换咨询师
		//TODO 
		// 分配客户是分配咨询师 分配类型为个人 且跟进状态为待跟进 等待被分配的咨询师获取客户
		customerVo.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
		customerVo.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
        User currentLoginUser = userService.getCurrentLoginUser();
		String customerId=customerVo.getId();
		if(StringUtils.isBlank(customerVo.getRemark())){
			customerVo.setRemark("前台重新分配咨询师");
		}
		Response res = customerService.allocateCustomer(customerVo, customerId, currentLoginUser.getUserId(),customerVo.getRemark(),CustomerEventType.CHANGE_COUNSELOR );
		
		if(res.getResultCode()==0){			
			if(customerVo.getLastFollowId()!=null){
				//前台工作台  分配客户列表   分配或者更换咨询师  //这里是 前台工作台 分配客户列表 处理网络分配客户  分配营运经理 营运经理分配校区
		         customerFolowupService.updateFollowupDealStatus(customerVo.getLastFollowId());		
			}			
		}

		return res;
	}
	
	/**
	 * 增加资源量 和上门数
	 * CustomerEventService.saveCustomerDynamicStatus
	 * 关键词
	 */
    
	
	/**
	 * 查询前台和客户管理录入新客户的记录
	 */
	@RequestMapping(value = "/loadCustomerRecord", method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid loadCustomerRecord(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerVo customerVo) throws Exception {
		DataPackageForJqGrid dataPackageForJqGrid = null;
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);			
		dataPackage = customerService.loadCustomerRecord(dataPackage, customerVo);
		dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}
	
	
	
	/**
	 * 客户信息录入   保存或者更新客户信息  
	 */
	@RequestMapping(value = "/saveCustomer", method = RequestMethod.POST)
	@ResponseBody
	public Response saveCustomer(@RequestBody CustomerVo cus) throws Exception {
		//资源入口的确定	
		if (cus == null) {
			throw new ApplicationException("客户信息不存在");
		}
		String customerId = "";				
		customerId = customerService.saveOrUpdateCustomer(cus);
		Response response  = new Response(0, customerId);
		if(customerId==null){
			response.setResultCode(-1);
			response.setResultMessage("手机号码不能为空");
		}
		if(customerId!=null && customerId.equals("fail")){
			response.setResultCode(-1);
			response.setResultMessage("该手机号码已经存在");			
		}
		if(customerId!=null && customerId.equals("active")){
			response.setResultCode(-1);
			response.setResultMessage("该客户处于活跃状态，不允许激活");			
		}
		return response;
	}
	
	/**
	 * 资源入口解锁 问题
	 * 这个接口是在 前台查询重新调配客户的时候判断是否出现资源入口的，现在要屏蔽这个入口，这个接口暂时没用了 20170823
	 */
	@RequestMapping(value = "/isExceedPeriod", method = RequestMethod.GET)
	@ResponseBody	
	public Boolean isExceedPeriod(@RequestParam String customerId){
		return customerService.isExceedPeriod(customerId);
	}
	
	
	/**
	 * 客户管理 ---客户资源调配 
	 * @author xiaojinwang
	 * @date 20160914
     *
	 */
	@RequestMapping(value = "/allocateCustomerResource", method =  RequestMethod.POST)
	@ResponseBody
	public Response allocateCustomerResource(@RequestParam String cusId, @RequestParam CustomerDeliverType deliverType,
			@RequestParam String deliverTarget) {
	
		//TODO 客户资源调配  需要重新考虑
		CustomerVo customerVo = new CustomerVo();
		customerVo.setDeliverType(deliverType);
		customerVo.setDeliverTarget(deliverTarget);
		
		return customerService.allocateCustomerResource(cusId, customerVo);
		
	}
	
	/**
	 * 资源池里面的全部客户
	 * @param gridRequest
	 * @param vo 封装
	 * @param deliverTargetId:resourcePoolId 资源池的id  或者是某个user的Id 
	 * @return
	 */
	@RequestMapping(value = "/getResourcePoolCustomer",method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getResourcePoolCustomer(@ModelAttribute GridRequest gridRequest, 
			@ModelAttribute DistributeCustomerVo vo,String deliverTargetId) throws Exception {
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
        dataPackage = customerService.getResourcePoolCustomer(vo, dataPackage,deliverTargetId);
		return new DataPackageForJqGrid(dataPackage);
		
	}
	
	/**
	 * 手工设置客户上门
	 */
	@RequestMapping(value = "/setCustomerVisitCome",method = RequestMethod.POST)
	@ResponseBody
	public Response setCustomerVisitCome(String customerId){
		return customerService.setCustomerVisitCome(customerId);
	}
	
	/**
	 * 查询切换前台模式记录
	 */
	@RequestMapping(value = "/getChangeUserRoleRecords", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getChangeUserRoleRecords(@ModelAttribute GridRequest gridRequest,@ModelAttribute ChangeUserRoleRecordVo recordVo){	
		DataPackageForJqGrid dataPackageForJqGrid = null;
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.getChangeUserRoleRecords(dataPackage,recordVo);
		dataPackageForJqGrid = new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;		
	}
	
	/**
	 * 保存咨询师转校申请
	 */
	@RequestMapping(value = "/saveChangeCampusApply",method = RequestMethod.POST)
	@ResponseBody
	public Response saveChangeCampusApply(@ModelAttribute ChangeCampusApplyVo applyVo){
		if(applyVo==null || StringUtil.isBlank(applyVo.getCustomerId())){
			return new Response(-1, "转校参数出错");
		}			
		return changeCampusApplyService.saveChangeCampusApply(applyVo);	
	}
	
	/**
	 * 咨询师查询自己提交的转校申请 转校申请客户表
	 */
	@RequestMapping(value = "/getChangeCampusCustomers", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getChangeCampusCustomers(@ModelAttribute GridRequest gridRequest,@ModelAttribute ChangeCampusApplyVo applyVo){	
		DataPackageForJqGrid dataPackageForJqGrid = null;
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = changeCampusApplyService.getChangeCampusCustomers(dataPackage, applyVo);
		dataPackageForJqGrid = new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;		
	}
	/**
	 * 营运主任查看 转校申请客户列表 
	 */	
	@RequestMapping(value = "/getChangeCampusApplys", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getChangeCampusApplys(@ModelAttribute GridRequest gridRequest,@ModelAttribute ChangeCampusApplyVo applyVo){	
		DataPackageForJqGrid dataPackageForJqGrid = null;
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = changeCampusApplyService.getChangeCampusApplys(dataPackage, applyVo);
		dataPackageForJqGrid = new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;		
	}
	
	/**
	 * 营运主任查看  审核结果
	 */	
	@RequestMapping(value = "/loadChangeCampusResult", method =  RequestMethod.GET)
	@ResponseBody
	public ChangeCampusApplyVo loadChangeCampusResult(@RequestParam String id){	
		return changeCampusApplyService.loadChangeCampusResult(id);	
	}
	
	
	/**
	 * 营运主任 审核 转校申请
	 */	
	@RequestMapping(value = "/updateChangeCampusResult", method =  RequestMethod.POST)
	@ResponseBody
	public Response updateChangeCampusResult(@ModelAttribute ChangeCampusApplyVo applyVo){	
		return changeCampusApplyService.updateChangeCampusResult(applyVo);	
	}
	
	
	/**
	 * 转介绍审核 查询分配对象
	 */
	@RequestMapping(value = "/getTransferTargetByCampus", method =  RequestMethod.GET)
	@ResponseBody
	public List getTransferTargetByCampus(String showBelong,@RequestParam(value="job[]",required=false)String[] job,String transferId) {
		return transferCustomerService.getTransferTargetByCampus(showBelong,job,transferId);
	}
	
	/**
	 * 客户管理--客户列表 （市场专用）
	 * 替换原来的接口  getCustomersForJqGrid
	 * @param customerVo
	 * 确认标记
	 */
	@RequestMapping(value = "/getAllCustomersForMarket", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getAllCustomersForMarket(@ModelAttribute GridRequest gridRequest, @ModelAttribute CustomerVo vo)throws Exception{
		//log.debug("客户管理获取客户列表");
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = customerService.getAllCustomersForMarket(vo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	//客户详情删除学生
	@RequestMapping(value = "/setDeleteStudent", method =  RequestMethod.POST)
	@ResponseBody
	public Response setDeleteStudent(String studentId){				
		if(StringUtil.isBlank(studentId)){
			Response response = new Response();
			response.setResultCode(-1);
			response.setResultMessage("学生ID不能为空，删除学生失败");
			return response;
		}
		return customerService.setDeleteStudent(studentId);
	}

	//查询客户是否已经预约到访登记
	@RequestMapping(value = "/getCustomerMeetingConfirm", method =  RequestMethod.GET)
	@ResponseBody
	public Response getCustomerMeetingConfirm(String customerId){				
		if(StringUtil.isBlank(customerId)){
			Response response = new Response();
			response.setResultCode(-1);
			response.setResultMessage("客户ID不能为空");
			return response;
		}
		return customerFolowupService.getCustomerMeetingConfirm(customerId);
	}
	
	//根据customerId获取最新的预约客户记录
	@RequestMapping(value = "/getLastAppointmentRecord", method =  RequestMethod.GET)
	@ResponseBody
	public CustomerFolowupVo getLastAppointmentRecord(String customerId){				
		if(StringUtil.isBlank(customerId)){
			throw
			new ApplicationException("客户ID不能为空");
		}
		return customerFolowupService.getLastAppointmentRecord(customerId);
	}
	
	@RequestMapping(value = "/updateAppointCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response updateAppointCustomer(@ModelAttribute CustomerFolowupVo follow) throws Exception {
        if(StringUtil.isBlank(follow.getId())){
        	Response response = new Response();
        	response.setResultCode(-1);
        	response.setResultMessage("数据出错");
        }
		customerFolowupService.updateAppointCustomer(follow); 
		return new Response();
	}
	
	@RequestMapping(value = "/allocateTransferCustomer", method =  RequestMethod.POST)
	@ResponseBody
	public Response allocateTransferCustomer(@ModelAttribute TransferCustomerRecordVo tRecordVo) throws Exception {
		if(tRecordVo==null)throw new ApplicationException("审核记录不存在");	  	
	    User user=userService.getCurrentLoginUser();
	    String currentTime=DateTools.getCurrentDateTime();
	    tRecordVo.setModifyTime(currentTime);
	    tRecordVo.setModifyUserId(user.getUserId());
	    //审核通过要进行客户分配  添加抢客记录 添加 数据统计事件  审核不通过 则发送系统通知 
	    return transferCustomerService.allocateTransferCustomer(tRecordVo);
	}
	
	
	//或者咨询师职位的的回收周期
	@RequestMapping(value = "/getZXSReturnCycle", method =  RequestMethod.GET)
	@ResponseBody
	public Integer getZXSReturnCycle() {
		String sql ="select * from user_job where JOB_SIGN = :jobSign ";
		Map<String, Object> params = Maps.newHashMap();
		params.put("jobSign", "zxs");
		List<UserJob> list = userJobService.findAllUserJobBySql(sql,params);
		if(list!=null && !list.isEmpty()){
			return list.get(0).getReturnCycle();
		}
		return 0;
	}
	
	@RequestMapping(value = "/customerImportHistory", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid customerImportHistory(@ModelAttribute GridRequest gridRequest, @ModelAttribute TimeVo timeVo) throws Exception {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage= customerService.customerImportHistoryList( dataPackage, timeVo  );
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value = "/getImportHistoryExcel", method =  RequestMethod.GET)
	@ResponseBody
	public void getImportHistoryExcel(@RequestParam String nameKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(StringUtils.isBlank(nameKey)) {
			throw new ApplicationException("导出excel参数为空");
		}
		InputStream in = null;
		OutputStream out = null;
		byte[] buff = new byte[4096];
		try {
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String filename = "客户导入历史Excel"+timeFormat.format(new Date())+".xls";
			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
			
			out = response.getOutputStream();
			in = AliyunOSSUtils.get(nameKey);
			int len = 0;
			while ((len = in.read(buff)) > 0) {
				out.write(buff, 0, len);
			}
		} catch (FileNotFoundException e) {
			log.info("getImportHistoryExcel导出失败！");
			e.printStackTrace();
		} catch (IOException e) {
			log.info("getImportHistoryExcel导出失败！");
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
	}
	/**
	 * 校验客户学生姓名
	 * @author: duanmenrun
	 * @Title: checkCustomerStudengName 
	 * @Description: TODO 
	 * @param cusId
	 * @param stuId
	 * @param stuName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkCustomerStudengName")
	@ResponseBody
	public Response checkCustomerStudengName(@RequestParam String cusId, @RequestParam String stuId,@RequestParam String stuName) throws Exception{
		return customerService.checkCustomerStudengName(cusId, stuId, stuName);
	}
	
	
	
	
	
	
	
	
	
	
}
