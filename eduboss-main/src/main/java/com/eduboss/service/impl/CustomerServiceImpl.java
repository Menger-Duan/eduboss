package com.eduboss.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduboss.common.*;
import com.eduboss.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.eduboss.dao.AccountChargeRecordsDao;
import com.eduboss.dao.ChangeUserRoleRecordDao;
import com.eduboss.dao.ContractDao;
import com.eduboss.dao.CustomerAppointmentDao;
import com.eduboss.dao.CustomerCallsLogDao;
import com.eduboss.dao.CustomerDao;
import com.eduboss.dao.CustomerFolowupDao;
import com.eduboss.dao.CustomerImportHistoryDao;
import com.eduboss.dao.CustomerImportTransformDao;
import com.eduboss.dao.CustomerStudentDao;
import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.GainCustomerDao;
import com.eduboss.dao.MobileUserDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.ProcedureDao;
import com.eduboss.dao.RegionDao;
import com.eduboss.dao.ResourcePoolDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.dao.StudentSchoolDao;
import com.eduboss.dao.StudnetAccMvDao;
import com.eduboss.dao.TransactionRecordDao;
import com.eduboss.dao.TransferCustomerCampusDao;
import com.eduboss.dao.TransferCustomerDao;
import com.eduboss.dao.UserDao;
import com.eduboss.dao.UserDeptJobDao;
import com.eduboss.dao.UserJobDao;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerAllocateObtain;
import com.eduboss.domain.CustomerAppointment;
import com.eduboss.domain.CustomerCallsLog;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.CustomerFolowup;
import com.eduboss.domain.CustomerImportHistory;
import com.eduboss.domain.CustomerImportTransform;
import com.eduboss.domain.CustomerStudent;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.DeliverTargetChangeRecord;
import com.eduboss.domain.DisabledCustomer;
import com.eduboss.domain.GainCustomer;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Region;
import com.eduboss.domain.ResourcePool;
import com.eduboss.domain.Role;
import com.eduboss.domain.SentRecord;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudentSchool;
import com.eduboss.domain.TransferCustomerCampus;
import com.eduboss.domain.TransferCustomerRecord;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domain.UserJob;
import com.eduboss.domainVo.AppCustomerVo;
import com.eduboss.domainVo.ChangeUserRoleRecordVo;
import com.eduboss.domainVo.CustomerAppointmentVo;
import com.eduboss.domainVo.CustomerCallsLogVo;
import com.eduboss.domainVo.CustomerDataImport;
import com.eduboss.domainVo.CustomerDeliverTarget;
import com.eduboss.domainVo.CustomerFollowUpRecrodsVo;
import com.eduboss.domainVo.CustomerFolowupVo;
import com.eduboss.domainVo.CustomerLessVo;
import com.eduboss.domainVo.CustomerPoolDataVo;
import com.eduboss.domainVo.CustomerPoolVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.DataImportResult;
import com.eduboss.domainVo.DistributeCustomerVo;
import com.eduboss.domainVo.FollowupCustomerVo;
import com.eduboss.domainVo.OrganizationMobileSimpleVo;
import com.eduboss.domainVo.PhoneCustomerVo;
import com.eduboss.domainVo.ReceptionistCustomerVo;
import com.eduboss.domainVo.SignCustomerVo;
import com.eduboss.domainVo.SignupCustomerVo;
import com.eduboss.domainVo.StudentImportVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.UserDetailForMobileVo;
import com.eduboss.domainVo.UserInfoForMobileVo;
import com.eduboss.domainVo.UserSimpleMobileVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.TimeVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.CommonService;
import com.eduboss.service.ContractService;
import com.eduboss.service.CustomerAllocateObtainService;
import com.eduboss.service.CustomerEventService;
import com.eduboss.service.CustomerFolowupService;
import com.eduboss.service.CustomerService;
import com.eduboss.service.DeliverTargetChangeService;
import com.eduboss.service.DisabledCustomerService;
import com.eduboss.service.MobilePushMsgService;
import com.eduboss.service.MobileUserService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.ResourcePoolService;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.SentRecordService;
import com.eduboss.service.StudentService;
import com.eduboss.service.UserService;
import com.eduboss.task.SendSysMsgCostomerDistributionThread;
import com.google.common.collect.Maps;


@Service("com.eduboss.service.CustomerService")
public class CustomerServiceImpl implements CustomerService {
	
	//将转介绍的天数限制 提示语提取出来
	private final static Integer TRANSFER_DAY = 60;
	
	//学生结课时长
	private final static int FINISH_CLASS_DAY = 15;
	
	//学生停课时长
	private final static int STOP_CLASS_DAY = 90;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerEventService customerEventService;
	
	@Autowired
	private MobileUserService mobileUserService;
	
	@Autowired
	private CustomerDao customerDao; 
	
	@Autowired
	private UserDao userDao; 

	@Autowired
	private CustomerFolowupDao customerFolowupDao;
	
	@Autowired
	private CustomerAppointmentDao customerAppointmentDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private CustomerCallsLogDao customerCallsLogDao;

    @Autowired
    private RoleQLConfigService roleQLConfigService;
    
    @Autowired
    private ContractDao contractDao;
    
    @Autowired ContractService contractService;
    
    @Autowired
    private DataDictDao dataDictDao;
    
    @Autowired
    private UserJobDao userJobDao;
    
    @Autowired
    private UserDeptJobDao userDeptJobDao;

    @Autowired
    private ResourcePoolService resourcePoolService;

    @Autowired
    private ResourcePoolDao resourcePoolDao;
    
    @Autowired 
    private TransactionRecordDao transactionRecordDao;
    
    @Autowired
    private CustomerImportTransformDao customerImportTransformDao;
    
    @Autowired
	private HibernateTemplate hibernateTemplate;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CustomerStudentDao customerStudentDao;
    
    @Autowired
    private StudentDao studentDao;
    
    @Autowired
    private StudentSchoolDao studentSchoolDao;
    
    @Autowired
    private DeliverTargetChangeService deliverTargetChangeService;
    
    @Autowired
    private TransferCustomerCampusDao transferCustomerCampusDao;
    
    @Autowired
    private GainCustomerDao gainCustomerDao;
    

    @Autowired
    private CustomerFolowupService customerFolowupService;
    
    @Autowired
    private ChangeUserRoleRecordDao changeUserRoleRecordDao;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private  AccountChargeRecordsDao accountChargeRecordsDao;
    
    @Autowired
    private StudnetAccMvDao studnetAccMvDao;
    
    @Autowired
    private TransferCustomerDao transferCustomerDao;
    
    @Autowired
    private DisabledCustomerService disabledCustomerService;
    
	@Autowired
	private MobileUserDao mobileUserDao;
	
	@Autowired
	private MobilePushMsgService mobilePushMsgService;
	
    @Autowired
    private CustomerAllocateObtainService customerAllocateObtainService;
    
    @Autowired
	private CommonService commonService;
    
    @Autowired
	private SentRecordService sentRecordService;
    
    @Autowired
	private RegionDao regionDao;
    
    @Autowired
  	private CustomerImportHistoryDao customerImportHistoryDao;
    
	private final static Logger log = Logger.getLogger(ResourcePoolServiceImpl.class);
    
    
    //作废 xiaojinwang 20170812 
	public String saveOrUpdateCustomer_zuofei(CustomerVo customerVo) {    	
    	Customer customer =HibernateUtils.voObjectMapping(customerVo, Customer.class);
    	if(customer == null)throw new ApplicationException("新客户信息不全不能修改或保存");
    	String currentTime=DateTools.getCurrentDateTime();
    	Boolean isNewCustomer = false;
    	User referUser = userService.getCurrentLoginUser();  
    	
    	String preEntrance =null;
    	String customerId = customerVo.getId();
    	CustomerActive customerActive = null;

		if(StringUtils.isNotBlank(customerVo.getContact()) && StringUtil.isBlank(customerId)){
			//如果是僵尸客户 客户录入
			Customer cus = this.loadCustomerByContact(customerVo.getContact());

			//如果是不活跃的说明是(重新录入)激活的僵尸客户  如果cusId已经存在说明是修改

			if(cus!=null && cus.getCustomerActive() == CustomerActive.INACTIVE){
				customerVo.setId(cus.getId());
				customer.setId(cus.getId());
				customerActive = cus.getCustomerActive();
			}else if(cus!=null){
				return "active";
			}

		}
    	
    	
    	if(StringUtils.isNotEmpty(customerVo.getId())){
    		//不为空则是修改旧的客户信息
    		
    		Customer customerOld=customerDao.findById(customerVo.getId());
    		
    		CustomerDealStatus customerDealStatus_old = customerOld.getDealStatus();
    		String deliverTarget_old = customerOld.getDeliverTarget();
    		String beforedeliverTarget_old = customerOld.getBeforeDeliverTarget();
    		
    		
    		
    		
    		Customer temp = customer;
    		customer = customerOld;
    		customer.setName(temp.getName());
    		customer.setContact(temp.getContact());
    		customer.setCusOrg(temp.getCusOrg());
    		customer.setCusType(temp.getCusType());
    		customer.setDealStatus(temp.getDealStatus());
    		customer.setDeliverTarget(temp.getDeliverTarget());
    		customer.setId(temp.getId());
    		customer.setLastDeliverName(temp.getLastDeliverName());
    		customer.setResEntrance(customerOld.getResEntrance());
    		
    		
    		
    		
    		
    		
    		if(customerOld.getCustomerActive() == CustomerActive.INACTIVE && StringUtil.isBlank(customerId)){
    			//重新激活僵尸客户
    			customer.setCustomerActive(CustomerActive.NEW_CUSTOMER);
    			if(StringUtil.isNotBlank(customerVo.getPreEntranceId())) {
					if (customerVo.getPreEntranceId().equals(ResEntranceType.ON_LINE.getValue())) {
						customer.setResEntrance(new DataDict(ResEntranceType.ON_LINE.getValue()));
					}
					customer.setPreEntrance(new DataDict(customerVo.getPreEntranceId()));
				}
    		}else{
    			customer.setPreEntrance(customerOld.getPreEntrance());
    		}
    		
    		
    		customer.setBlSchool(temp.getBlSchool());
    		if(StringUtil.isNotBlank(temp.getRemark())){
    			customer.setRemark(temp.getRemark());
    		}else{
    			customer.setRemark(customerOld.getRemark());
    		}
    		
    		//新增需求 20170319
    		customer.setIntentionCampus(customerOld.getIntentionCampus());
    		
    		// 归属校区的处理   所属分公司bl_branch字段废弃了
    		if(customer.getBlSchool()!=null){
    			Organization organization = userService.getBelongCampusByOrgId(customer.getBlSchool());
				customer.setBlCampusId(organization);
    		}else{
    			if(CustomerDeliverType.PERSONAL_POOL.equals(customer.getDeliverType())
    					&& StringUtils.isNotBlank(customer.getDeliverTarget())){
    				String organizationId=userService.loadUserById(customer.getDeliverTarget()).getOrganizationId();
    				Organization organization = userService.getBelongCampusByOrgId(organizationId);
    				if(organization!=null){
    				  customer.setBlCampusId(organization);
    				  customer.setBlSchool(organization.getId());
    				}
    			}else{
    				Organization organization = userService.getBelongCampus();
    				if(organization!=null){
    				  customer.setBlCampusId(organization);
    				  customer.setBlSchool(organization.getId());
    				}
    			}
    		}
    		
    		if(customerDealStatus_old!=null && customerDealStatus_old==CustomerDealStatus.INVALID){
    			//这种情况是重新激活旧的无效客户
    			
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.INVALID);
				dynamicStatus.setDescription("重新激活无效客户"+customerVo.getId());
				if(customer.getResEntrance()!=null){
					dynamicStatus.setResEntrance(customer.getResEntrance());
				}
				dynamicStatus.setStatusNum(-1);
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	
    		    			
    		}
    		
    		customer.setModifyTime(currentTime);
    		customer.setModifyUserId(referUser.getUserId());
    		

    		customer.setLastFollowUpTime(DateTools.getCurrentDateTime());
    		customer.setLastFollowUpUser(referUser);
    		
    		if(StringUtils.isNotBlank(customerVo.getDeliverTarget())){
    			//TODO 先做处理，20170427 
    			Organization org  = organizationDao.findById(customerVo.getDeliverTarget());
    			Organization belongCampus = null;
    			if(org != null){
    				belongCampus =userService.getBelongCampusByOrgId(customerVo.getDeliverTarget());
    			}else{
    				User user = userService.loadUserById(customerVo.getDeliverTarget());
    				if(user!=null){
    					belongCampus = userService.getBelongCampusByUserId(customerVo.getDeliverTarget());
    				}	    				
    			}
    			customer.setTransferFrom(belongCampus!=null?belongCampus.getId():"");
    			// 签完合同再来，详情中重新分配咨询师，修改客户状态  
    			if(customerDealStatus_old==CustomerDealStatus.SIGNEUP && !customerVo.getDeliverTarget().equals(deliverTarget_old)){    				
    				customer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);   				   				    				
    			}
    		}else{
    			customer.setTransferFrom(customerOld.getTransferFrom());
    		}   
    		if(StringUtils.isNotBlank(customerVo.getDeliverTarget())){
    			//区分 vo的deliverTarget与old的deliverTarget是否相同
    			String deliverTarget = customerVo.getDeliverTarget();
    			
    			//增加资源量的校验
//				Response res = resourcePoolService.getResourcePoolVolume(1, deliverTarget);
//				if(res.getResultCode() != 0) {
//					return res;
//				}
    			
    			
    			if(!deliverTarget.equals(deliverTarget_old)){
        			String deliverTargetName = ""; 				
    		    	User deliverUser = userDao.findById(deliverTarget); 
    		    	if(deliverUser!=null){
    		    		deliverTargetName = deliverUser.getName();
    		    		customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
    		    	}else{
    		    		Organization deliverOrg = organizationDao.findById(deliverTarget);
    		    		if(deliverOrg!=null){
    		    			deliverTargetName = deliverOrg.getName();
    		    			customer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
    		    		}
    		    	}
        			customer.setDeliverTarget(deliverTarget);
        			//customer.setBeforeDeliverTarget(customerOld.getDeliverTarget());
        			customer.setDeliverTargetName(deliverTargetName);
        			customer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);  
        			customer.setLastDeliverName(referUser.getName());
        			customer.setDeliverTime(currentTime);
        			
        		
    			}
    			
    			
    		}else{
    			//不是待审核的客户才处理
    			if (customerDealStatus_old != CustomerDealStatus.TOBE_AUDIT){
					//分配给当前录入者
					//这种情况是  外呼咨询师网络工作台中客户录入覆盖在资源池中的客户的信息
					Organization organization = userService.getBelongCampus();
					if(organization!=null){
						customer.setBlCampusId(organization);
						customer.setBlSchool(organization.getId());
					}
					customer.setDeliverTarget(referUser.getUserId());
					//customer.setBeforeDeliverTarget(customerOld.getDeliverTarget());
					customer.setDeliverTargetName(referUser.getName());
					customer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
					customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
					customer.setLastDeliverName(referUser.getName());
					customer.setDeliverTime(currentTime);
					
	
				
					CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
					dynamicStatus.setDynamicStatusType(CustomerEventType.PHONECALL);
					dynamicStatus.setDescription("覆盖资源池客户重新录入增加留电量");
					if(customer.getResEntrance()!=null){
						dynamicStatus.setResEntrance(customer.getResEntrance());
					}
					dynamicStatus.setStatusNum(1);
					dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                    customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	
					
					
                    //覆盖录入旧客户的时候覆盖资源入口的问题
                    
                    //TODO 
					
					
					
				}

    			
    		}
    		
    		
    		if (StringUtils.isNotBlank(customerVo.getDeliverTarget())&&!customerVo.getDeliverTarget().equals(deliverTarget_old)) {
    			customer.setLastDeliverName(referUser.getName());
    			customer.setDeliverTime(currentTime);   			
    		}else if (StringUtils.isNotBlank(customerVo.getBeforeDeliverTarget()) && !customerVo.getBeforeDeliverTarget().equals(beforedeliverTarget_old)) {
    			customer.setLastDeliverName(referUser.getName());
    			customer.setDeliverTime(currentTime);
    		}   		
    		//如果跟进人和前工序跟进人为空，但是旧的不为空那么要把原来的跟进人填回来
    		if (StringUtils.isBlank(customer.getDeliverTarget()) && StringUtils.isNotBlank(deliverTarget_old) ) {
    			customer.setDeliverTarget(deliverTarget_old);
    		}else if(StringUtils.isBlank(customer.getBeforeDeliverTarget()) && StringUtils.isNotBlank(beforedeliverTarget_old) ){
    			customer.setBeforeDeliverTarget(beforedeliverTarget_old);
    		}
    		//如果是重新激活 则去掉上工序 20170509
    		if(customerActive == CustomerActive.INACTIVE && StringUtil.isBlank(customerId)){
    			customer.setBeforeDeliverTarget(null);
    			
    			CustomerDynamicStatus dynamicStatus_active = new CustomerDynamicStatus();
    			dynamicStatus_active.setDynamicStatusType(CustomerEventType.ACTIVECUSTOMER);
    			dynamicStatus_active.setDescription("非活跃客户重新激活");
				if(customer.getPreEntrance()!=null){
					dynamicStatus_active.setResEntrance(customer.getPreEntrance());
				}
				dynamicStatus_active.setStatusNum(1);
				dynamicStatus_active.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
                customerEventService.addCustomerDynameicStatus(customer, dynamicStatus_active, referUser);	
    		}
    		
    		customer.setAppointmentDate(customerOld.getAppointmentDate());
    		customer.setNextFollowupTime(customerOld.getNextFollowupTime());
    		
    		customerDao.clear(); 

    	}else{
    		//增加手机号码校验 重复不能录入
    		String  phone = customerVo.getContact();
    		if(StringUtils.isNotBlank(phone)){
    			//
    			Customer temp = this.loadCustomerByContact(phone);
    			if(temp!=null){
    				return "fail";
    			}
    		}else{
    			return null;
    		}
    		isNewCustomer = true;
    		//客户录入的资源入口核心字段
    		//外呼 网络  咨询师的录入客户的资源入口  pre_Entrance res_Entrance
    		//网络的录入的时候资源入口就生效
    		
    		if(customer.getPreEntrance()!=null){
    			preEntrance = customer.getPreEntrance().getValue();
    		}	
    		if(preEntrance!=null && preEntrance.equals(ResEntranceType.ON_LINE.getValue())){
    			customer.setResEntrance(new DataDict(ResEntranceType.ON_LINE.getValue()));
    		}
    		
    	    //20170524调整 将直访拉访的设置上门 放到获取客户的时候进行  
    		
    		if(customer.getResEntrance()!=null){
    			String resEntrance = customer.getResEntrance().getValue();
    			if(resEntrance.equals(ResEntranceType.DIRECT_VISIT.getValue())||resEntrance.equals(ResEntranceType.OUTSTANDING_VISIT.getValue())){
    				//customer.setVisitType(VisitType.PARENT_COME);
    				customer.setBlConsultor(customerVo.getDeliverTarget());
    			}
   		    }
    		
    		//新增需求 意向校区
    		if(StringUtil.isNotBlank(customerVo.getIntentionCampusId())){
    			customer.setIntentionCampus(organizationService.findById(customerVo.getIntentionCampusId()));
    		}
    		
    		
    		//新客户的信息      	
			customer.setRecordDate(currentTime);
			customer.setRecordUserId(referUser);
			customer.setCreateTime(currentTime);
			customer.setCreateUser(referUser);
			customer.setModifyTime(currentTime);
			customer.setModifyUserId(referUser.getUserId());
			//transferFrom和归属校区处理  
			if(StringUtils.isNotBlank(customer.getDeliverTarget())){
				Organization deOrganization = organizationDao.findById(customer.getDeliverTarget());
		    	User deUser = userDao.findById(customer.getDeliverTarget()); 
				if(deOrganization!=null){
					customer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
				}else if(deUser!=null){
					customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
				}					
				if (deUser != null) {					
					Organization belongCampus = userService.getBelongCampusByUserId(deUser.getUserId());
					if (belongCampus != null)
					customer.setTransferFrom(belongCampus.getId());					
				} else {
					customer.setTransferFrom(customer.getDeliverTarget());
				}	
				//如果分配人不为空 设置最新分配人
				String lastDeliverName = referUser.getName();
				customer.setLastDeliverName(lastDeliverName);
				customer.setDeliverTime(currentTime);
			}else{
				customer.setTransferFrom(userService.getBelongCampus().getId());
			}
			if (StringUtil.isBlank(customer.getDeliverTarget()) && StringUtil.isBlank(customer.getBeforeDeliverTarget())){
				customer.setDeliverTarget(referUser.getUserId());
				customer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
				customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
				//如果分配人为空 设置最新分配人 为当前登录者
				String lastDeliverName = referUser.getName();
				customer.setLastDeliverName(lastDeliverName);
				customer.setDeliverTime(currentTime);
			}
			if(StringUtils.isNotEmpty(customer.getRemark())){
				customer.setLastFollowUpTime(DateTools.getCurrentDateTime());
				customer.setLastFollowUpUser(referUser);
			}
			//归属校区处理  save
			if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.equals(customer.getDeliverType())
					&& StringUtils.isNotBlank(customer.getDeliverTarget())){
				//Organization deOrganization = organizationDao.findById(customer.getDeliverTarget());		
			}else if(CustomerDeliverType.PERSONAL_POOL.equals(customer.getDeliverType())
					&& StringUtils.isNotBlank(customer.getDeliverTarget())){
				String organizationId=userService.getUserById(customer.getDeliverTarget()).getOrganizationId();
				Organization organization = userService.getBelongCampusByOrgId(organizationId);
				customer.setBlSchool(organization!=null?organization.getId():"");
				customer.setBlCampusId(organization);
			}else if(CustomerDeliverType.PERSONAL_POOL.equals(customer.getDeliverType())
					&& StringUtils.isNotBlank(customer.getBeforeDeliverTarget())){
				String organizationId=userService.getUserById(customer.getBeforeDeliverTarget()).getOrganizationId();
				Organization organization = userService.getBelongCampusByOrgId(organizationId);
				customer.setBlSchool(organization!=null?organization.getId():"");
				customer.setBlCampusId(organization);
			}else if(StringUtils.isEmpty(customer.getBlSchool())){
				Organization organization = userService.getBelongCampus();
				customer.setBlSchool(organization!=null?organization.getId():"");
				customer.setBlCampusId(organization);
			}  		   		
    	}
    	
    	
    	
    	   	 
		
		//先保留下面的逻辑 2016-10-26
		if(customer.getCusOrg()!=null && ( customer.getCusOrg().getId()==null 
				|| (customer.getCusOrg()!=null && StringUtils.isBlank(customer.getCusOrg().getId())) )
				&& (customer.getCusOrg()!=null && StringUtils.isNotBlank(customer.getCusOrg().getName())) ){
			//防止旧数据报错
			customer.setCusOrg(null);
		}
		customerDao.save(customer);
		
		//保存潜在学生
		Set<StudentImportVo> stuVos=customerVo.getStudentImportVos();
		this.savePaddingStudentJsonInfo(stuVos,customer.getId()); 
		
		
		//如果电话号码不存在的情况下保存事务记录和保存潜在学生
    	if(StringUtils.isNotBlank(customer.getContact())){
    		CustomerVo vo = new CustomerVo();
    		vo.setContact(customer.getContact());
    		if(customerDao.getCustomerCountByPhone(vo)<=0){
        		if (StringUtil.isNotBlank(customerVo.getTransactionUuid())) {
        			transactionRecordDao.saveTransactionRecord(customerVo.getTransactionUuid());
        		}   		
    		} 
    	}
		
		
		if (isNewCustomer) {
    		//资源量和上门数的统计
			//呼入  拉访 直访 的客户信息首次录入 资源量增加1
			if(preEntrance!=null){
	    		if(preEntrance.equals(ResEntranceType.CALL_IN.getValue())||preEntrance.equals(ResEntranceType.OUTSTANDING_VISIT.getValue())|| preEntrance.equals(ResEntranceType.DIRECT_VISIT.getValue())){
	    			//增加资源量
					CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
					dynamicStatus.setDynamicStatusType(CustomerEventType.RESOURCES);
					dynamicStatus.setDescription("新增资源量");
					dynamicStatus.setResEntrance(new DataDict(preEntrance));
					dynamicStatus.setStatusNum(1);
					dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                    customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	
			    			
	    			//customerEventService.saveCustomerDynamicStatus(customer.getId(), CustomerEventType.RESOURCES, "新增资源量", referUser, preEntrance, 1);
	    		}
	    		if(preEntrance.equals(ResEntranceType.OUTSTANDING_VISIT.getValue())||preEntrance.equals(ResEntranceType.DIRECT_VISIT.getValue())){
	    			//增加上门数
					CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
					dynamicStatus.setDynamicStatusType(CustomerEventType.VISITCOME);
					dynamicStatus.setDescription("新增上门数");
					dynamicStatus.setResEntrance(new DataDict(preEntrance));
					dynamicStatus.setStatusNum(1);
					dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                    customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	
	    		}
			}
						
			if(StringUtil.isNotBlank(customerVo.getSource()) && "import".equals(customerVo.getSource())){
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.IMPORTCUSTOMER);
				dynamicStatus.setDescription("导入客户");
				if(customer.getResEntrance()!=null){
					dynamicStatus.setResEntrance(customer.getResEntrance());
				}else if(customer.getPreEntrance()!=null){
					dynamicStatus.setResEntrance(customer.getPreEntrance());
				}
				dynamicStatus.setStatusNum(1);
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
                customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	
			}else{
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.PHONECALL);
				dynamicStatus.setDescription("新增客户录入增加留电量");
				if(customer.getResEntrance()!=null){
					dynamicStatus.setResEntrance(customer.getResEntrance());
				}else if(customer.getPreEntrance()!=null){
					dynamicStatus.setResEntrance(customer.getPreEntrance());
				}
				dynamicStatus.setStatusNum(1);
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	


				CustomerDynamicStatus nDynamicStatus = new CustomerDynamicStatus();
				nDynamicStatus.setDynamicStatusType(CustomerEventType.NEW);
				nDynamicStatus.setDescription("新增客户");
				if(customer.getResEntrance()!=null){
					nDynamicStatus.setResEntrance(customer.getResEntrance());
				}else if(customer.getPreEntrance()!=null){
					nDynamicStatus.setResEntrance(customer.getPreEntrance());
				}
				nDynamicStatus.setStatusNum(1);
				nDynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
                customerEventService.addCustomerDynameicStatus(customer, nDynamicStatus, referUser);	
			}
			
		}
		return customer.getId();
    	
    }
    
    @Override
    public String saveOrUpdateCustomer(CustomerVo customerVo) {    	
    	Customer customer =HibernateUtils.voObjectMapping(customerVo, Customer.class);   	
    	//获取当前时间
    	String currentTime=DateTools.getCurrentDateTime();
    	//当前登陆者
    	User referUser = userService.getCurrentLoginUser();  
    	
    	//资源 入口 pre标记 客户id判断是否录入老客户
    	String preEntrance =null;
    	String customerId = customerVo.getId();
    	CustomerActive customerActive = null;
    	
    	//客户录入  老客户 也是会传customerId的
    	//下面这段代码是为了兼容之前的代码
    	Customer cus = null;
    	if(StringUtils.isNotBlank(customerVo.getContact())){
    		 cus = this.loadCustomerByContact(customerVo.getContact());
    		 if(cus!=null && cus.getCustomerActive() == CustomerActive.INACTIVE){
    	         customerId = null; 
    		 }
    	}
    	

		if(StringUtils.isNotBlank(customerVo.getContact()) && StringUtil.isBlank(customerId)){
			//如果是僵尸客户 客户录入			
			//如果是不活跃的说明是(重新录入)激活的僵尸客户  如果cusId已经存在说明是修改
			if(cus!=null && cus.getCustomerActive() == CustomerActive.INACTIVE){
				//用户后面判断是否修改老客户信息  录入老客户也算是修改
				customerVo.setId(cus.getId());
				customer.setId(cus.getId());
				customerActive = cus.getCustomerActive();
			}else if(cus!=null){
				return "active";
			}

		}    	
    	if(StringUtils.isNotEmpty(customerVo.getId())){    		
    		//录入客户界面的携带信息：客户姓名、 联系方式 、资源来源 来源细分 备注  如果是网络则有意向校区  前台和客户管理的录入则有 分配对象    		
    		//其中手机号码为不能修改
    		//customerVo.getId()不为空则是修改旧的客户信息  		
    		Customer customerOld=customerDao.findById(customerVo.getId());
    		
    		//记录原来的资源 入口  如果有变 原来的资源入口的资源量少1 将原来在保存预约登记的逻辑迁移至此  20170810  xiaojinwang
    		DataDict resEntrancOld = customerOld.getResEntrance();
    		
    		
    		//旧的客户 处理状态、原来的跟进人  原来的前工序 
    		CustomerDealStatus customerDealStatus_old = customerOld.getDealStatus();
    		String deliverTarget_old = customerOld.getDeliverTarget();
   		
    		if(StringUtil.isNotBlank(customer.getContact())){
    			customerOld.setContact(customer.getContact());
    		}
    		if(StringUtil.isNotBlank(customer.getName())){
    			customerOld.setName(customer.getName());
    		}
    		if(customer.getCusOrg()!=null){
    			customerOld.setCusOrg(customer.getCusOrg());
    		}
    		if(customer.getCusType()!=null){
    			customerOld.setCusType(customer.getCusType());
    		}
    		if(customer.getDeliverType()!=null){
    			customerOld.setDeliverType(customer.getDeliverType());
    		}
    		if(StringUtil.isNotBlank(customer.getDeliverTarget())){
    			customerOld.setDeliverTarget(customer.getDeliverTarget());
    		}
    		if(StringUtil.isNotBlank(customer.getRemark())){
    			customerOld.setRemark(customer.getRemark());
    		}
    		if(customer.getIntentionCampus()!=null){
    			customerOld.setIntentionCampus(customer.getIntentionCampus());
    		}
    		
    		//#702956  网络是分配校区这里不处理，其他是deliverTarget的校区
			boolean firstCampusFlag = false;
			boolean clearFirstCampus = false;
			
			if (!ResEntranceType.ON_LINE.getValue().equals(customerVo.getPreEntranceId())) {
				firstCampusFlag = true;
			}else if (ResEntranceType.ON_LINE.getValue().equals(customerVo.getPreEntranceId())
					&& customerOld.getResEntrance()!=null
					&& !customerOld.getResEntrance().getValue().equals(ResEntranceType.ON_LINE.getValue())){
				clearFirstCampus = true;
			}
    		String firstCampus = "";
    		
    	    //TODO 
    		//资源入口  资源入口标记   只有是在不活跃客户的时候才会修改     
    		//重新激活僵尸客户  清空前工序
    		if(customerOld.getCustomerActive() == CustomerActive.INACTIVE && StringUtil.isBlank(customerId)){
    			customerOld.setCustomerActive(CustomerActive.NEW_CUSTOMER);
    			customerOld.setBeforeDeliverTarget(null);
    			
    			//防止无效客户重新激活后，当晚被定时任务重新设置为无效duanmenrun  20171102
				CustomerFolowup followupRecord = new CustomerFolowup();
				followupRecord.setCustomer(customer);
				followupRecord.setRemark("无效客户重新激活，添加系统跟进记录");
				followupRecord.setCreateTime(DateTools.getCurrentDateTime());
				followupRecord.setCreateUser(referUser);
				followupRecord.setAppointmentType(AppointmentType.APPOINTMENT_REACTIVATE);
				customerFolowupDao.save(followupRecord);
    			
				
    			//客户管理的客户录入
    			//资源入口  资源入口标记   只有是在不活跃客户的时候才会修改     
    			//当资源来源是市场推广、来源细分是地推数据，保存这个客户时，入口更新为地推数据
				if (StringUtil.isBlank(customerVo.getPreEntranceId())) {
					if(customerOld.getCusOrg()!=null && customerOld.getCusType()!=null){
						DataDict cusTypeD = dataDictDao.findById(customerOld.getCusType().getId());
						DataDict cusOrgD = dataDictDao.findById(customerOld.getCusOrg().getId());
						if (cusTypeD != null && cusOrgD != null && cusTypeD.getName().equals("市场推广")
								&& cusOrgD.getName().equals("地推数据")) {
							customerOld.setPreEntrance(new DataDict(ResEntranceType.GROUNDPROMOTION.getValue()));
							customerOld.setResEntrance(new DataDict(ResEntranceType.GROUNDPROMOTION.getValue()));
						}else if(cusTypeD != null && cusOrgD != null){
							customerOld.setPreEntrance(null);
							customerOld.setResEntrance(null);							
						}
						// 资源来源是数据库数据、来源细分是数据库数据，保存成功后，入口为空。(修改为除了市场推广地推数据外的都要为空)
						// 当校区营运主任通过客户资源调配给咨询师，咨询师获取后，入口标记为咨询师陌拜；
						// 也可以调配给TMK主管， TMK主管调配给TMK专员，专员获取后，入口标记为TMK
//						if (cusTypeD != null && cusOrgD != null && cusTypeD.getName().equals("数据库数据")
//								&& cusOrgD.getName().equals("数据库数据")) {
//							customerOld.setPreEntrance(null);
//							customerOld.setResEntrance(null);
//						}						
					}
				}else{
		            //利用工作台来区分处理 customerVo.getPreEntranceId()
					if (customerVo.getPreEntranceId().equals(ResEntranceType.CALL_OUT.getValue())) {
						customerOld.setPreEntrance(new DataDict(ResEntranceType.CALL_OUT.getValue()));
						customerOld.setResEntrance(new DataDict(ResEntranceType.CALL_OUT.getValue()));
					}	
					if (customerVo.getPreEntranceId().equals(ResEntranceType.OUTCALL.getValue())) {
						customerOld.setPreEntrance(new DataDict(ResEntranceType.OUTCALL.getValue()));
						customerOld.setResEntrance(new DataDict(ResEntranceType.OUTCALL.getValue()));
					}
					if (customerVo.getPreEntranceId().equals(ResEntranceType.ON_LINE.getValue())) {
						customerOld.setPreEntrance(new DataDict(ResEntranceType.ON_LINE.getValue()));
						customerOld.setResEntrance(new DataDict(ResEntranceType.ON_LINE.getValue()));
					}
					//前台录入
					if(customerVo.getPreEntranceId()!=null && customerVo.getResEntranceId()!=null){
						customerOld.setPreEntrance(new DataDict(customerVo.getPreEntranceId()));
						customerOld.setResEntrance(new DataDict(customerVo.getResEntranceId()));
					}
					
				}

    			
    			
    			
    			
    		}
    		
    		
    		//归属校区的更新      所属分公司bl_branch字段废弃了
			if(CustomerDeliverType.PERSONAL_POOL == customer.getDeliverType()
					&& StringUtils.isNotBlank(customer.getDeliverTarget())){
				String organizationId=userService.loadUserById(customer.getDeliverTarget()).getOrganizationId();
				Organization organization = userService.getBelongCampusByOrgId(organizationId);
				if(organization!=null){
				  customerOld.setBlCampusId(organization);
				  customerOld.setBlSchool(organization.getId());
				  firstCampus = organization.getId();
				}
			}else{
				Organization organization = userService.getBelongCampus();
				if(organization!=null){
				  customerOld.setBlCampusId(organization);
				  customerOld.setBlSchool(organization.getId());
				  firstCampus = organization.getId();
				}
			}
			
    		customerOld.setModifyTime(currentTime);
    		customerOld.setModifyUserId(referUser.getUserId());
    		

    		customerOld.setLastFollowUpTime(DateTools.getCurrentDateTime());
    		customerOld.setLastFollowUpUser(referUser);
			
    		
    		if(StringUtils.isNotBlank(customerVo.getDeliverTarget())){     			
    			String deliverTarget = customerVo.getDeliverTarget();
    			Organization org  = organizationDao.findById(deliverTarget);
    			Organization belongCampus = null;
    			if(org != null){
    				belongCampus =userService.getBelongCampusByOrgId(deliverTarget);
    			}else{
    				User user = userService.loadUserById(deliverTarget);
    				if(user!=null){
    					belongCampus = userService.getBelongCampusByUserId(deliverTarget);
    				}	    				
    			}
    			customerOld.setTransferFrom(belongCampus!=null?belongCampus.getId():"");
    			//customerOld.setDealStatus(CustomerDealStatus.STAY_FOLLOW);   				   				    				
    			
    			if(!deliverTarget.equals(deliverTarget_old)){
    				User deliverUser = userDao.findById(deliverTarget);
    				if(deliverUser!=null){
    					customerOld.setDeliverTargetName(deliverUser.getName());
    				}
    				
        			customerOld.setLastDeliverName(referUser.getName());
        			customerOld.setDeliverTime(currentTime);
    				
    			}			
    		}else{
    			//分配给当前录入者
    			//不是待审核的客户才处理
				//这种情况是  外呼咨询师网络工作台中客户录入覆盖在资源池中的客户的信息
				Organization organization = userService.getBelongCampus();
				if(organization!=null){
					customerOld.setBlCampusId(organization);
					customerOld.setBlSchool(organization.getId());
					firstCampus = organization.getId();
				}
				customerOld.setDeliverTarget(referUser.getUserId());
				customerOld.setDeliverTargetName(referUser.getName());
				//customerOld.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
				customerOld.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
				customerOld.setLastDeliverName(referUser.getName());
				customerOld.setDeliverTime(currentTime);			
                //覆盖录入旧客户的时候覆盖资源入口的问题 				
    		}
    		
    		if(firstCampusFlag && StringUtils.isBlank(customerOld.getFirstCampus())) {
    			customerOld.setFirstCampus(firstCampus);
    		}else if(clearFirstCampus) {
    			customerOld.setFirstCampus(null);
    		}
    		
    		//#699233  录入学生结课停课超时的客户
    		if(StringUtils.isNotBlank(customerVo.getWorkBench())&& customerVo.getStudentStatusFlag()) {
    			customerOld.setCustomerActive(CustomerActive.NEW_CUSTOMER);
    			customerOld.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
    		}
    		
    		customerDao.flush();     	
    		
    		Customer cusObj=customerDao.findById(customerVo.getId());
    		
    		//#699233录入学生结课停课超时的客户
    		if(StringUtils.isNotBlank(customerVo.getWorkBench())&& customerVo.getStudentStatusFlag()) {
    				DeliverTargetChangeRecord record = new DeliverTargetChangeRecord();
        			record.setCustomerId(cusObj.getId());
        			record.setPreviousTarget(deliverTarget_old);
        			record.setCurrentTarget(cusObj.getDeliverTarget());
        			record.setRemark("客户学生停课/结课超过时间限制，重新录入分配客户");
        			record.setCreateUserId(referUser.getUserId());
        			record.setCreateTime(currentTime);
        			String recordId = deliverTargetChangeService.saveDeliverTargetChangeRecord(record);
        			
        			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
        			dynamicStatus.setDynamicStatusType(CustomerEventType.NEW);
        			dynamicStatus.setDescription("客户学生停课/结课超过时间限制，重新录入分配客户");
        			if(cusObj.getResEntrance()!=null){
        			   dynamicStatus.setResEntrance(cusObj.getResEntrance());
        			}
        			dynamicStatus.setStatusNum(1);
        			dynamicStatus.setTableName("delivertarget_change_record");
        			dynamicStatus.setTableId(recordId);
        			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
        			customerEventService.addCustomerDynameicStatus(cusObj, dynamicStatus, referUser);
    		}
    		
    		//保存潜在学生   		
    		Set<StudentImportVo> stuVos=customerVo.getStudentImportVos();
    		/*if(stuVos == null || stuVos.isEmpty()) {
				StudentImportVo s = new StudentImportVo();
				s.setName(customerVo.getName()+"孩子");
				stuVos.add(s);
			}*/
    		
    		if(stuVos!=null && !stuVos.isEmpty()){
    			this.savePaddingStudentJsonInfo(stuVos,cusObj.getId()); 
    		}
    		
    		
    		
    		//如果是重新激活 则去掉上工序 20170509
    		if(customerActive == CustomerActive.INACTIVE && StringUtil.isBlank(customerId)){  			
    			CustomerDynamicStatus dynamicStatus_active = new CustomerDynamicStatus();
    			dynamicStatus_active.setDynamicStatusType(CustomerEventType.ACTIVECUSTOMER);
    			dynamicStatus_active.setDescription("非活跃客户重新激活");
				if(cusObj.getPreEntrance()!=null){
					dynamicStatus_active.setResEntrance(cusObj.getPreEntrance());
				}
				dynamicStatus_active.setStatusNum(1);
				dynamicStatus_active.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
                customerEventService.addCustomerDynameicStatus(cusObj, dynamicStatus_active, referUser);	
    		}
    		
    		
			//记录激活无效客户的动态事件
    		if(customerDealStatus_old!=null && customerDealStatus_old==CustomerDealStatus.INVALID){
    			//这种情况是重新激活旧的无效客户   			
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.INVALID);
				dynamicStatus.setDescription("重新激活无效客户"+customerVo.getId());
				if(cusObj.getResEntrance()!=null){
					dynamicStatus.setResEntrance(cusObj.getResEntrance());
				}
				dynamicStatus.setStatusNum(-1);
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                customerEventService.addCustomerDynameicStatus(cusObj, dynamicStatus, referUser);	    		    			
    		}  
    		
    		

    		if(StringUtil.isBlank(customerVo.getDeliverTarget())&& customerDealStatus_old != CustomerDealStatus.TOBE_AUDIT ){
    			//分配给自己的 覆盖老客户    			
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.PHONECALL);
				dynamicStatus.setDescription("覆盖资源池客户重新录入增加留电量");
				if(cusObj.getResEntrance()!=null){
					dynamicStatus.setResEntrance(cusObj.getResEntrance());
				}
				dynamicStatus.setStatusNum(1);
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                customerEventService.addCustomerDynameicStatus(cusObj, dynamicStatus, referUser);	
    		}
    		
    		//现在的资源入口 
    		DataDict resEntranceNow = cusObj.getResEntrance();   		
			if(resEntrancOld!=null && resEntranceNow!=null&& !resEntrancOld.getId().equals(resEntranceNow.getId())){
				//原来的资源量resEntrancOld减1			
				CustomerDynamicStatus dynamicStatus2 = new CustomerDynamicStatus();
				dynamicStatus2.setDynamicStatusType(CustomerEventType.RESOURCES);
				dynamicStatus2.setDescription("原来的资源量减少一");
				dynamicStatus2.setResEntrance(new DataDict(resEntrancOld.getId()));
				dynamicStatus2.setStatusNum(-1);
				dynamicStatus2.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
				customerEventService.addCustomerDynameicStatus(cusObj, dynamicStatus2, referUser);
			
			}
    		
    	
    	}else{
    		
    		//新增客户的情况   		
    		//增加手机号码校验 重复不能录入
    		String  phone = customerVo.getContact();
    		if(StringUtils.isNotBlank(phone)){
    			//
    			Customer temp = this.loadCustomerByContact(phone);
    			if(temp!=null){
    				return "fail";
    			}
    		}else{
    			return null;
    		}
    		
    		//客户录入的资源入口核心字段
    		//外呼 网络  咨询师的录入客户的资源入口  pre_Entrance res_Entrance
    		//三个工作台录入的时候资源入口就生效   	    		
    		if(customer.getPreEntrance()!=null){
    			preEntrance = customer.getPreEntrance().getValue();
    			if(preEntrance.equals(ResEntranceType.CALL_OUT.getValue()) || preEntrance.equals(ResEntranceType.ON_LINE.getValue())
    				|| preEntrance.equals(ResEntranceType.OUTCALL.getValue())){
    				customer.setResEntrance(new DataDict(preEntrance));
    			}		
    		}else{
    			//如果为空 则是客户管理的客户录入
    			//资源来源CusType是市场推广、来源细分CusOrg是地推数据时，保存成功后，入口为地推数据
    			if(customer.getCusType()!=null && customer.getCusOrg()!=null){
    				String cusType = customer.getCusType().getId();
    				String cusOrg = customer.getCusOrg().getId();
    				DataDict cusTypeD = dataDictDao.findById(cusType);
    				DataDict cusOrgD = dataDictDao.findById(cusOrg);
    				if(cusTypeD!=null && cusOrgD!=null && cusTypeD.getName().equals("市场推广")&& cusOrgD.getName().equals("地推数据")){
    					customer.setPreEntrance(new DataDict(ResEntranceType.GROUNDPROMOTION.getValue()));
    					customer.setResEntrance(new DataDict(ResEntranceType.GROUNDPROMOTION.getValue()));
    				}else if(cusTypeD!=null && cusOrgD!=null){
    					customer.setPreEntrance(null);
    					customer.setResEntrance(null);    					
    				}
        			//资源来源是数据库数据、来源细分是数据库数据，保存成功后，入口为空。(修改为除了市场推广 地推数据 外的都为空)
        			//当校区营运主任通过客户资源调配给咨询师，咨询师获取后，入口标记为咨询师陌拜；
        			//也可以调配给TMK主管， TMK主管调配给TMK专员，专员获取后，入口标记为TMK
//    				if(cusTypeD!=null && cusOrgD!=null && cusTypeD.getName().equals("数据库数据")&& cusOrgD.getName().equals("数据库数据")){
//    					customer.setPreEntrance(null);
//    					customer.setResEntrance(null);
//    				}
    			}   			
    		}

    		
    	    //20170524调整 将直访拉访的设置上门 放到获取客户的时候进行  
    		//下面代码的补充说明：新客户录入的时候  直访拉访 原来设置是已经上门 取消已经上门 
    		// 原来的上门数不变，更新customer的blConsultor字段
    		// 等客户获取的时候进行设置 如果获取的人userid和该字段一致 则设置上门和添加新的事件
    		if(customer.getResEntrance()!=null){
    			String resEntrance = customer.getResEntrance().getValue();
    			if(resEntrance.equals(ResEntranceType.DIRECT_VISIT.getValue())||resEntrance.equals(ResEntranceType.OUTSTANDING_VISIT.getValue())){
    				//customer.setVisitType(VisitType.PARENT_COME);
    				customer.setBlConsultor(customerVo.getDeliverTarget());
    			}
   		    }
    		
    		//新增需求 意向校区
    		if(StringUtil.isNotBlank(customerVo.getIntentionCampusId())){
    			customer.setIntentionCampus(organizationService.findById(customerVo.getIntentionCampusId()));
    		}
    		
    		
    		//新客户的信息      	
			customer.setRecordDate(currentTime);
			customer.setRecordUserId(referUser);
			customer.setCreateTime(currentTime);
			customer.setCreateUser(referUser);
			customer.setModifyTime(currentTime);
			customer.setModifyUserId(referUser.getUserId());
			
			//#702956  网络是分配校区这里不处理，其他是deliverTarget的校区
			boolean firstCampusFlag = false;
			if (!ResEntranceType.ON_LINE.getValue().equals(customerVo.getPreEntranceId())) {
				firstCampusFlag = true;
			}
    		String firstCampus = "";
			
			//transferFrom和归属校区处理  
			if(StringUtils.isNotBlank(customer.getDeliverTarget())){
				Organization deOrganization = organizationDao.findById(customer.getDeliverTarget());
		    	User deUser = userDao.findById(customer.getDeliverTarget()); 
				if(deOrganization!=null){
					customer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
				}else if(deUser!=null){
					customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
				}					
				if (deUser != null) {					
					Organization belongCampus = userService.getBelongCampusByUserId(deUser.getUserId());
					if (belongCampus != null)
					customer.setTransferFrom(belongCampus.getId());					
				} else {
					customer.setTransferFrom(customer.getDeliverTarget());
				}	
				//如果分配人不为空 设置最新分配人
				String lastDeliverName = referUser.getName();
				customer.setLastDeliverName(lastDeliverName);
				customer.setDeliverTime(currentTime);
			}else{
				customer.setTransferFrom(userService.getBelongCampus().getId());
			}
			if (StringUtil.isBlank(customer.getDeliverTarget()) && StringUtil.isBlank(customer.getBeforeDeliverTarget())){
				customer.setDeliverTarget(referUser.getUserId());
				customer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
				customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
				//如果分配人为空 设置最新分配人 为当前登录者
				String lastDeliverName = referUser.getName();
				customer.setLastDeliverName(lastDeliverName);
				customer.setDeliverTime(currentTime);
			}
			if(StringUtils.isNotEmpty(customer.getRemark())){
				customer.setLastFollowUpTime(DateTools.getCurrentDateTime());
				customer.setLastFollowUpUser(referUser);
			}
			//归属校区处理  save
			if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL==(customer.getDeliverType())
					&& StringUtils.isNotBlank(customer.getDeliverTarget())){
				//Organization deOrganization = organizationDao.findById(customer.getDeliverTarget());		
			}else if(CustomerDeliverType.PERSONAL_POOL==(customer.getDeliverType())
					&& StringUtils.isNotBlank(customer.getDeliverTarget())){
				String organizationId=userService.getUserById(customer.getDeliverTarget()).getOrganizationId();
				Organization organization = userService.getBelongCampusByOrgId(organizationId);
				customer.setBlSchool(organization!=null?organization.getId():"");
				customer.setBlCampusId(organization);
				firstCampus = organization.getId();
			}else if(CustomerDeliverType.PERSONAL_POOL == customer.getDeliverType()
					&& StringUtils.isNotBlank(customer.getBeforeDeliverTarget())){
				String organizationId=userService.getUserById(customer.getBeforeDeliverTarget()).getOrganizationId();
				Organization organization = userService.getBelongCampusByOrgId(organizationId);
				customer.setBlSchool(organization!=null?organization.getId():"");
				customer.setBlCampusId(organization);
				firstCampus = organization.getId();
			}else if(StringUtils.isEmpty(customer.getBlSchool())){
				Organization organization = userService.getBelongCampus();
				customer.setBlSchool(organization!=null?organization.getId():"");
				customer.setBlCampusId(organization);
				firstCampus = organization.getId();
			}  
			
			if(firstCampusFlag && StringUtils.isBlank(customer.getFirstCampus())) {
				customer.setFirstCampus(firstCampus);
    		}
			if(StringUtil.isNotBlank(customerVo.getSource()) && "import".equals(customerVo.getSource())){
				customer.setIsImport("1");
			}
			customerDao.save(customer);
			
			//保存潜在学生
			Set<StudentImportVo> stuVos=customerVo.getStudentImportVos();
			if((stuVos == null || stuVos.isEmpty()) && 
					((!"1".equals(customerVo.getHaveStudent()) && "import".equals(customerVo.getSource())) || !"import".equals(customerVo.getSource()))) {
				StudentImportVo s = new StudentImportVo();
				s.setName(customerVo.getName()+"孩子");
				stuVos.add(s);
			}
			
			if(stuVos!=null && !stuVos.isEmpty()){
				this.savePaddingStudentJsonInfo(stuVos,customer.getId()); 
			}
			//如果电话号码不存在的情况下保存事务记录和保存潜在学生
	    	if(StringUtils.isNotBlank(customer.getContact())){
	    		CustomerVo vo = new CustomerVo();
	    		vo.setContact(customer.getContact());
	    		if(customerDao.getCustomerCountByPhone(vo)<=0){
	        		if (StringUtil.isNotBlank(customerVo.getTransactionUuid())) {
	        			transactionRecordDao.saveTransactionRecord(customerVo.getTransactionUuid());
	        		}   		
	    		} 
	    	}			
			//资源量和上门数的统计
			//呼入  拉访 直访 的客户信息首次录入 资源量增加1
			if(preEntrance!=null){
	    		if(preEntrance.equals(ResEntranceType.CALL_IN.getValue())||preEntrance.equals(ResEntranceType.OUTSTANDING_VISIT.getValue())|| preEntrance.equals(ResEntranceType.DIRECT_VISIT.getValue())){
	    			//增加资源量
					CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
					dynamicStatus.setDynamicStatusType(CustomerEventType.RESOURCES);
					dynamicStatus.setDescription("新增资源量");
					dynamicStatus.setResEntrance(new DataDict(preEntrance));
					dynamicStatus.setStatusNum(1);
					dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                    customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	
			    			
	    			//customerEventService.saveCustomerDynamicStatus(customer.getId(), CustomerEventType.RESOURCES, "新增资源量", referUser, preEntrance, 1);
	    		}
	    		if(preEntrance.equals(ResEntranceType.OUTSTANDING_VISIT.getValue())||preEntrance.equals(ResEntranceType.DIRECT_VISIT.getValue())){
	    			//增加上门数
					CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
					dynamicStatus.setDynamicStatusType(CustomerEventType.VISITCOME);
					dynamicStatus.setDescription("新增上门数");
					dynamicStatus.setResEntrance(new DataDict(preEntrance));
					dynamicStatus.setStatusNum(1);
					dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                    customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	
	    		}
			}
						
			if(StringUtil.isNotBlank(customerVo.getSource()) && "import".equals(customerVo.getSource())){
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.IMPORTCUSTOMER);
				dynamicStatus.setDescription("导入客户");
				if(customer.getResEntrance()!=null){
					dynamicStatus.setResEntrance(customer.getResEntrance());
				}else if(customer.getPreEntrance()!=null){
					dynamicStatus.setResEntrance(customer.getPreEntrance());
				}
				dynamicStatus.setStatusNum(1);
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
                customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	
			}else{
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.PHONECALL);
				dynamicStatus.setDescription("新增客户录入增加留电量");
				if(customer.getResEntrance()!=null){
					dynamicStatus.setResEntrance(customer.getResEntrance());
				}else if(customer.getPreEntrance()!=null){
					dynamicStatus.setResEntrance(customer.getPreEntrance());
				}
				dynamicStatus.setStatusNum(1);
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	


				CustomerDynamicStatus nDynamicStatus = new CustomerDynamicStatus();
				nDynamicStatus.setDynamicStatusType(CustomerEventType.NEW);
				nDynamicStatus.setDescription("新增客户");
				if(customer.getResEntrance()!=null){
					nDynamicStatus.setResEntrance(customer.getResEntrance());
				}else if(customer.getPreEntrance()!=null){
					nDynamicStatus.setResEntrance(customer.getPreEntrance());
				}
				nDynamicStatus.setStatusNum(1);
				nDynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
                customerEventService.addCustomerDynameicStatus(customer, nDynamicStatus, referUser);	
			}
    	}
    	//判断负责人是否是营主，推送消息
    	if(StringUtil.isNotBlank(customer.getDeliverTarget()) && StringUtil.isNotBlank(customer.getId())) {
    		this.judgeUserSendMsg(customer.getDeliverTarget(),referUser.getUserId());
    	}
		return customer.getId(); 	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void sendCustomerSysMsg(CustomerVo customerVo) {
    	ResourcePool resourcePool = resourcePoolService.findResourcePoolById(customerVo.getDeliverTarget());
		if (resourcePool == null) {
			MobileUser mobileUser = mobileUserService.findMobileUserByStaffId(customerVo.getDeliverTarget());
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
				record.setMsgContent(mainRoleName + "-" + currentUser.getName() + "已分配1位新的客户至您的客户列表，请及时对该客户进行跟进，或在pc端进行再次分配");
				record.setMsgRecipient(new User(customerVo.getDeliverTarget()));
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
    
    private void sendCostomerDistributionSysMsg(SentRecord record, MobileUser mobileUser) {
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendSysMsgCostomerDistributionThread thread = new SendSysMsgCostomerDistributionThread(record, mobileUser);
		taskExecutor.execute(thread);
	}
    
    @Override
    public Response updateNormalCustomerForApp(CustomerVo vo) {
    	Response res=new Response();
    	Customer cus =HibernateUtils.voObjectMapping(vo, Customer.class);   
    	if(StringUtils.isNotEmpty(vo.getId())){
    		Customer customerOld=customerDao.findById(vo.getId());
    		
    		if(StringUtils.isNotBlank(vo.getCusOrg())){
    			customerOld.setCusOrg(cus.getCusOrg());
    		}
    		
    		if(StringUtils.isNotBlank(vo.getName())){
    			customerOld.setName(cus.getName());
    		}
    		
    		if(StringUtils.isNotBlank(vo.getResEntranceId())){
    			customerOld.setResEntrance(cus.getResEntrance());
    		}
    		
    		if(StringUtils.isNotBlank(vo.getCusType())){
    			customerOld.setCusType(cus.getCusType());
    		}
    		
    		if(StringUtils.isNotBlank(vo.getContact())){
    			customerOld.setContact(cus.getContact());
    		}
    		
    		customerOld.setModifyUserId(userService.getCurrentLoginUser().getUserId());
    		customerOld.setModifyTime(DateTools.getCurrentDateTime());
    		
    		customerDao.save(customerOld);
    		
    		//处理APP修改的客户信息，set到查出来的domain里面
    	}else{
    		res.setResultCode(8801);
    		res.setResultMessage("id不能为空");
    	}
    	return res;
    }
    
	@Override
	public String saveOrUpdateCustomer(Customer cus) {
		//测试号码是否已存在
		CustomerVo vo = new CustomerVo();
		vo.setContact(cus.getContact());
		vo.setId(cus.getId());
		if (customerDao.getCustomerCountByPhone(vo) > 0 && StringUtils.isNotBlank(cus.getContact())) {
//			throw new ApplicationException(ErrorCode.CUSTOMER_CONTACT_FOUND);
			return "fail";
			
		}
		
		String currentTime=DateTools.getCurrentDateTime();
		User user=userService.getCurrentLoginUser();
		boolean isNewCustomer = false;
		if(StringUtils.isEmpty(cus.getId())){
			if(StringUtils.isEmpty(cus.getRecordDate()))
				cus.setRecordDate(currentTime);
			if(cus.getRecordUserId() == null)
				cus.setRecordUserId(user);
			cus.setCreateTime(currentTime);
			cus.setCreateUser(user);
			if(StringUtils.isNotEmpty(user.getOrganizationId())) {
				cus.setBlCampusId(new Organization(user.getOrganizationId()));
				cus.setBlSchool(user.getOrganizationId());
			}else {
				Organization o = userService.getBelongCampus();
				cus.setBlCampusId(o);
				cus.setBlSchool(o!=null?o.getId():null);
			}
			isNewCustomer =true;
			if(cus.getDeliverTarget()!=null && StringUtils.isNotBlank(cus.getDeliverTarget())){
				User u=userDao.findById(cus.getDeliverTarget());
				if(u!=null){
					cus.setTransferFrom(userService.getBelongCampusByUserId(cus.getDeliverTarget()).getId());
				}else{
					cus.setTransferFrom(cus.getDeliverTarget());
					
				}
				
				
			}else{
				cus.setTransferFrom(userService.getBelongCampus().getId());
			}
		}
		cus.setModifyTime(currentTime);
		cus.setModifyUserId(user.getUserId());
		if (StringUtil.isBlank(cus.getDeliverTarget()) && StringUtil.isBlank(cus.getBeforeDeliverTarget())) {
			cus.setDeliverTarget(userService.getCurrentLoginUser().getUserId());
			cus.setDealStatus(CustomerDealStatus.FOLLOWING);
			cus.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
		}
		if (cus.getDeliverTarget() != null) {
			String lastDeliverNmae = userService.getCurrentLoginUser().getName();
			cus.setLastDeliverName(lastDeliverNmae);
		}else if(cus.getBeforeDeliverTarget() != null){
			String lastDeliverNmae = userService.getCurrentLoginUser().getName();
			cus.setLastDeliverName(lastDeliverNmae);
		}
		cus.setDeliverTime(DateTools.getCurrentDateTime());
		
		
		//归属校区处理
		if(StringUtils.isNotBlank(cus.getDeliverTarget())){
			Organization deOrganization = organizationDao.findById(cus.getDeliverTarget());
			User deUser = userDao.findById(cus.getDeliverTarget());
			if(deOrganization!=null){
				cus.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
			}else if(deUser!=null){
				cus.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
			}
		}
			
		if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.equals(cus.getDeliverType())
				&& StringUtils.isNotBlank(cus.getDeliverTarget())){
			//Organization deOrganization = organizationDao.findById(cus.getDeliverTarget());
		}else if(CustomerDeliverType.PERSONAL_POOL.equals(cus.getDeliverType())
				&& StringUtils.isNotBlank(cus.getDeliverTarget())){
			String organizationId=userService.getUserById(cus.getDeliverTarget()).getOrganizationId();
			Organization o = userService.getBelongCampusByOrgId(organizationId);
			cus.setBlSchool(o!=null?o.getId():null);
			cus.setBlCampusId(o);
		}else if(CustomerDeliverType.PERSONAL_POOL.equals(cus.getDeliverType())
				&& StringUtils.isNotBlank(cus.getBeforeDeliverTarget())){
			String organizationId=userService.getUserById(cus.getBeforeDeliverTarget()).getOrganizationId();
			Organization o = userService.getBelongCampusByOrgId(organizationId);
			cus.setBlSchool(o!=null?o.getId():null);
			cus.setBlCampusId(o);
		}else if(StringUtils.isEmpty(cus.getBlSchool())){
			Organization o = userService.getBelongCampus();
			cus.setBlSchool(o!=null?o.getId():null);
			cus.setBlCampusId(o);
		}
		
//		if(isNewCustomer && StringUtils.isNotEmpty(cus.getRemark())){
//			cus.setLastFollowUpTime(DateTools.getCurrentDateTime());
//			cus.setLastFollowUpUser(user);
//		}
		cus.setLastFollowUpTime(DateTools.getCurrentDateTime());
		cus.setLastFollowUpUser(user);
		if(cus.getCusOrg()!=null && ( cus.getCusOrg().getId()==null 
				|| (cus.getCusOrg()!=null && StringUtils.isBlank(cus.getCusOrg().getId())) )
				&& (cus.getCusOrg()!=null && StringUtils.isNotBlank(cus.getCusOrg().getName())) ){
			//防止旧数据报错
			cus.setCusOrg(null);
		}
		customerDao.save(cus);
		
//		if(isNewCustomer && StringUtils.isNotEmpty(cus.getRemark())){
//			//保存跟进记录
//			CustomerFolowup customerFolowup = new CustomerFolowup();
//			customerFolowup.setCustomer(cus);
//			customerFolowup.setRemark(cus.getRemark());
//			customerFolowup.setCreateTime(DateTools.getCurrentDateTime());
//			customerFolowup.setCreateUser(userService.getCurrentLoginUser());
//			customerFolowupDao.save(customerFolowup);
//		}
		if(isNewCustomer){
			
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.PHONECALL);
			dynamicStatus.setDescription("新增客户录入增加留电量");
			if(cus.getResEntrance()!=null){
				dynamicStatus.setResEntrance(cus.getResEntrance());
			}else if(cus.getPreEntrance()!=null){
				dynamicStatus.setResEntrance(cus.getPreEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
            customerEventService.addCustomerDynameicStatus(cus, dynamicStatus, user);	


			CustomerDynamicStatus nDynamicStatus = new CustomerDynamicStatus();
			nDynamicStatus.setDynamicStatusType(CustomerEventType.NEW);
			nDynamicStatus.setDescription("新增客户");
			if(cus.getResEntrance()!=null){
				nDynamicStatus.setResEntrance(cus.getResEntrance());
			}else if(cus.getPreEntrance()!=null){
				nDynamicStatus.setResEntrance(cus.getPreEntrance());
			}
			nDynamicStatus.setStatusNum(1);
			nDynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
            customerEventService.addCustomerDynameicStatus(cus, nDynamicStatus, user);
			
			
			
//			customerEventService.saveCustomerDynamicStatus(cus.getId(), CustomerEventType.NEW, "新增客户", "");	
//			if(cus.getRemark() != null && StringUtils.isNotBlank(cus.getRemark())){
//				customerEventService.saveCustomerDynamicStatus(cus.getId(), CustomerEventType.FOLLOWING, "保存跟进记录："+cus.getRemark(), "");
//			}
		}	
		return cus.getId();
//		//TODO:获取当前用户的角色，如果是前台提交的客户信息是直接从列表中编辑跟进对象，跟进类型没有提交，需要根据跟进对象ID来决定跟进类型 (前台只能分配给咨询师或其他校区，对应的类型是3-其他校区，5-咨询师)
//		
//		//如果只设置了分配对象，没有分配类型，匹配
//		String deliveTaget = cus.getDeliverTarget();
//		if (cus.getDeliverType() == null && StringUtils.isNotBlank(cus.getDeliverTarget())){
//			if (userService.getUserById(deliveTaget) != null) {
//				cus.setDeliverType(CustomerDeliverType.CONSULTOR);
//			} else  {
//				cus.setDeliverType(CustomerDeliverType.CAMPUS);
//			}
//		} 
//		
//		//根据分配途径设置归属校区及分公司
//		if (StringUtils.isNotEmpty(deliveTaget)) {
//			if (CustomerDeliverType.BRENCH.equals(cus.getDeliverType()) || CustomerDeliverType.BRENCH_PUBLIC_POOL.equals(cus.getDeliverType())) {
//				cus.setBlBranch(organizationDao.findById(deliveTaget).getId());//当前分公司
//			} else if (CustomerDeliverType.CAMPUS.equals(cus.getDeliverType()) || CustomerDeliverType.CAMPUS_PUBLIC_POOL.equals(cus.getDeliverType())) {
//				cus.setBlSchool(deliveTaget);//当前校区
//				cus.setBlBranch(userService.getBelongBrenchByCampusId(deliveTaget).getId());//当前分公司
//			} else if (CustomerDeliverType.CONSULTOR.equals(cus.getDeliverType())) {
//				Organization campus = userService.getBelongCampusByUserId(deliveTaget);
//				cus.setBlSchool(campus.getId());//当前校区
//				Organization org=userService.getBelongBrenchByCampusId(campus.getId());
//				if(org==null){//当前分公司
//					cus.setBlBranch(campus.getId());
//				}else{
//					cus.setBlBranch(org.getId());
//				}
//				
//			}
//		}
//		
//		
//		//避免对空值拼查询语句
//		if (StringUtils.isBlank(deliveTaget)) {
//			cus.setDeliverTarget(null);
//		}
//		
//		Customer cusInDb = null;
//		if (StringUtils.isNotEmpty(cus.getId())) {
//			cusInDb = customerDao.findById(cus.getId());
//		}
//		//测试号码是否已存在
//		CustomerVo vo = new CustomerVo();
//		vo.setContact(cus.getContact());
//		vo.setId(cus.getId());
//		if (customerDao.getCustomerCountByPhone(vo) > 0 && StringUtils.isNotBlank(cus.getContact())) {
//			throw new ApplicationException(ErrorCode.CUSTOMER_CONTACT_FOUND);
//		}
//		
//		if (cusInDb != null) {
//			//如果修改了跟进信息，记录跟进时间
//			if ((cus.getDeliverType() != null && !cus.getDeliverType().equals(cusInDb.getDeliverType()))
//					|| (StringUtils.isNotEmpty(cus.getDeliverTarget()) && !cus.getDeliverTarget().equals(cusInDb.getDeliverTarget()))) {
//				cusInDb.setDeliverTime(DateTools.getCurrentDateTime());
//			}
//			HibernateUtils.copyPropertysWithoutNull(cusInDb, cus);
//			if("receptionistToEdit".equals(cus.getDeliverTargetName())){//前台修改客户信息
//				cusInDb.setDeliverTarget(cus.getDeliverTarget());
//				cusInDb.setRemark(cus.getRemark());
//			}
//			
//			cusInDb.setModifyTime(DateTools.getCurrentDateTime());
//			cusInDb.setModifyUserId(userService.getCurrentLoginUser().getUserId());
//		} else {
//			
//			//如果是前台，确认人是自己
//			if (userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST)) {
//				cus.setConfirmReceptionist(userService.getCurrentLoginUser());
//			}
//			
//			//咨询师通过合同或其他方式自动新建客户时，如果没指定跟进情况，则默认自己跟进
//			if (userService.isCurrentUserRoleCode(RoleCode.CONSULTOR) 
//					&& cus.getDeliverType() == null 
//					&& StringUtils.isBlank(cus.getDeliverTarget())) {
//				cus.setDeliverType(CustomerDeliverType.CONSULTOR);
//				cus.setDeliverTarget(userService.getCurrentLoginUser().getUserId());
//				cus.setDeliverTime(DateTools.getCurrentDateTime());
//			}
//			
//			//保存到数据库
//			cus.setId(null);
//			//默认状态
//			if (cus.getDealStatus() == null) {
//				cus.setDealStatus(CustomerDealStatus.NEW);
//			}
//			//默认归属校区和分公司
//			if (StringUtils.isEmpty(cus.getBlBranch())) {
//				cus.setBlBranch(userService.getBelongBrench().getId());//当前分公司ID
//				cus.setBlSchool(userService.getBelongCampus().getId());//当前校区
//			}
//			if(StringUtils.isEmpty(cus.getDeliverTarget())){
//				if(CustomerDeliverType.BRENCH.equals(cus.getDeliverType()) || CustomerDeliverType.BRENCH_PUBLIC_POOL.equals(cus.getDeliverType())){
//					cus.setDeliverTarget(cus.getBlBranch());
//				}else if(CustomerDeliverType.CAMPUS.equals(cus.getDeliverType()) || CustomerDeliverType.CAMPUS_PUBLIC_POOL.equals(cus.getDeliverType())){
//					cus.setDeliverTarget(cus.getBlSchool());
//				}else if(CustomerDeliverType.CONSULTOR.equals(cus.getDeliverType())){
//					cus.setDeliverTarget(cus.getBlConsultor());
//				}
//			}
//			
//			cus.setRecordDate(DateTools.getCurrentDateTime());
//			cus.setModifyTime(DateTools.getCurrentDateTime());
//			cus.setModifyUserId(userService.getCurrentLoginUser().getUserId());
//			cus.setCreateTime(DateTools.getCurrentDateTime());
//			cus.setCreateUser(userService.getCurrentLoginUser());
//			customerDao.save(cus);
//			
//			customerEventService.saveCustomerDynamicStatus(cus.getId(), CustomerEventType.NEW, "新增客户", "");
//		}
//		return cus.getId();
	}
	
	@Override
	public void releaseCustomer(Customer cus) {
		Customer cusInDb = customerDao.findById(cus.getId());
		if (cusInDb != null) {
			cusInDb.setDealStatus(CustomerDealStatus.NEW);
			cusInDb.setDeliverTarget(null);
			cusInDb.setDeliverType(null);
			cusInDb.setModifyTime(DateTools.getCurrentDateTime());
			cusInDb.setModifyUserId(userService.getCurrentLoginUser().getUserId());
			
			customerDao.save(cusInDb);
		}
	}

	@Override
	public void deleteCustomer(String resIds) throws ApplicationException {
		String[] idArray = resIds.split(",");
		for (String id : idArray) {
			customerDao.delete(new Customer(id));
		}
	}
	
	public void recepitionistConfirmCustomer(String customerId) {
		if (userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST)) {
			Customer customer = customerDao.findById(customerId);
			 if (customer == null) {
				 throw new ApplicationException(ErrorCode.CUSTOMER_NOT_FOUND);
			 }
			if (customer.getConfirmReceptionist() != null) {
				throw new ApplicationException(ErrorCode.CUSTOMER_HAD_BEED_CONFIRM);
			} 
			customer.setConfirmReceptionist(userService.getCurrentLoginUser());
		} else {
			throw new ApplicationException(ErrorCode.PERMISSION_DENY);
		}
	}

	@Deprecated
	@Override
	public DataPackage getCustomers(CustomerVo res, DataPackage dp) {
		return getCustomers(res, dp, false);
	}
	
	@Deprecated
	@Override
	public DataPackage getCustomers(CustomerVo customerVo, DataPackage dp, boolean onlyShowUndeliver) {
//		
//		//当前登录者
//		User currentUser = userService.getCurrentLoginUser();
//		
//		StringBuilder cusSql=new StringBuilder(4096);	
//		cusSql.append(" select c.id,c.name,c.contact,c.CUS_TYPE as cusType,c.CUS_ORG as cusOrg,c.RECORD_DATE as recordDate,");
//		cusSql.append(" c.RECORD_USER_ID as recordUserId,c.BL_SCHOOL as blSchool,c.BL_CONSULTOR as blConsultor,c.IS_PUBLIC as isPublic, ");
//		cusSql.append(" c.DEAL_STATUS as dealStatus,c.REMARK as remark,c.CREATE_TIME as createTime,c.CREATE_USER_ID as createUser,");
//		cusSql.append(" c.MODIFY_TIME as modifyTime,c.MODIFY_USER_ID as modifyUserId,c.DELIVER_TYPE as deliverType,c.DELEVER_TARGET as deliverTarget,");
//		cusSql.append(" c.INTRODUCER as introducer,c.APPOINTMENT_DATE as appointmentDate,c.CONFIRM_RECEPTIONIST_ID as confirmReceptionist,");
//		cusSql.append(" c.DELIVER_TIME as deliverTime,c.LAST_DELIVER_NAME as lastDeliverName, c.LAST_FOLLOW_UP_TIME as lastFollowUpTime,");
//		cusSql.append(" c.LAST_FOLLOW_UP_USER_ID as lastFollowUpUser,c.BL_CAMPUS_ID as blCampusId, c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer, ");
//		cusSql.append(" c.PURCHASING_POWER as purchasingPower, c.VISIT_TYPE as visitType,c.RES_ENTRANCE as resEntrance,c.CONFIRM_RECEPTIONIST_ID as confirmReceptionistId,c.NEXT_FOLLOWUP_TIME as nextFollowupTime, ");
//		cusSql.append(" c.SIGN_NEXT_FOLLOWUP_TIME as signNextFollowupTime,c.MEETING_CONFIRM_TIME as meetingConfirmTime,c.BEFOR_DELIVER_TARGET as beforeDeliverTarget, c.BEFOR_DELIVER_TIME as beforeDeliverTime,");
//		cusSql.append(" c.LAST_APPOINT_ID as lastAppointId, c.LAST_FOLLOW_ID as lastFollowId, c.TRANSFER_FROM as transferFrom,c.TRANSFER_STATUS as transferStatus,");
//		cusSql.append(" c.invalid_reason as invalidReason ");
//		
//		
//		cusSql.append(" ,d.`NAME` as resEntranceName ,dd.`NAME` as cusTypeName,ddd.`NAME` as cusOrgName, o.`name` as blSchoolName,oo.`name` as branchName ");
//		cusSql.append(" ,u.`NAME` as createUserName,uu.`NAME` as recordUserName,uuu.`NAME` as deliverTargetName,oga.`name` as blCampusName "); 
//		cusSql.append(" ,og.`NAME` as deptName,ogg.`NAME` as deliverTargetdeptName,da.`NAME` as purchasingPowerName,dda.`NAME` as intentionOfTheCustomerName,ddt.`NAME` as addressName,ddda.`NAME` as pointialStuGradeName ");   
//		cusSql.append(" ,ss.`NAME` as pointialStuSchoolName,");
//		cusSql.append("  CONCAT('',s.SCHOOL) as pointialStuSchool ,s.GRADE_ID as pointialStuGrade, s.name as pointialStuName from customer c ");
//		cusSql.append(" LEFT JOIN customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
//		cusSql.append(" LEFT JOIN student s on s.id=csr.STUDENT_ID ");
//		
//		cusSql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID "); //资源入口
//		cusSql.append(" LEFT JOIN data_dict dd on c.CUS_TYPE = dd.ID "); //资源分类
//		cusSql.append(" LEFT JOIN data_dict ddd on c.CUS_ORG = ddd.ID ");//资源细分
//
//		cusSql.append(" LEFT JOIN data_dict da on c.PURCHASING_POWER = da.ID "); 
//		cusSql.append(" LEFT JOIN data_dict dda on c.INTENTION_OF_THE_CUSTOMER = dda.ID ");
//		cusSql.append(" LEFT JOIN data_dict ddda on s.GRADE_ID = ddda.ID ");
//		cusSql.append(" LEFT JOIN student_school ss on ss.ID = s.SCHOOL ");
//		
//		cusSql.append(" LEFT JOIN organization oga on c.BL_CAMPUS_ID = oga.id ");
//		cusSql.append(" LEFT JOIN organization o on c.BL_SCHOOL = o.id ");//所属学校
//		cusSql.append(" LEFT JOIN data_dict ddt on o.REGION_ID = ddt.ID ");
//		cusSql.append(" LEFT JOIN organization oo on o.parentID = oo.id ");//分公司名字
//		cusSql.append(" LEFT JOIN `user` u on c.CREATE_USER_ID = u.USER_ID ");//录入者 
//		cusSql.append(" LEFT JOIN `user` uu on c.RECORD_USER_ID = uu.USER_ID ");
//		cusSql.append(" LEFT JOIN `user` uuu on c.DELEVER_TARGET = uuu.USER_ID "); 
//		
//		cusSql.append(" LEFT JOIN user_dept_job udj on ( c.CREATE_USER_ID = udj.USER_ID and udj.isMajorRole=0 ) ");
//		cusSql.append(" LEFT JOIN organization og on og.id=udj.DEPT_ID "); //部门职位
//		
//		cusSql.append(" LEFT JOIN user_dept_job udjj on ( c.DELEVER_TARGET = udjj.USER_ID and udjj.isMajorRole=0 ) ");
//		cusSql.append(" LEFT JOIN organization ogg on ogg.id=udjj.DEPT_ID "); //部门职位
//		cusSql.append(" where 1=1 ");
//		
//		
//		Map<String, Object> params = Maps.newHashMap();
//		if (onlyShowUndeliver){
//			cusSql.append(" and c.DELIVER_TYPE is null");
//		}
//		if(StringUtils.isNotEmpty(customerVo.getName())){
//			cusSql.append(" and c.name like :name ");
//			params.put("name", "%"+customerVo.getName()+"%");
//		}
//		if(StringUtils.isNotEmpty(customerVo.getRecordDate())){
//			cusSql.append(" and c.RECORD_DATE like :recordDate ");
//			params.put("recordDate", customerVo.getRecordDate()+"%");
//		}
//		if (StringUtils.isNotBlank(customerVo.getLastDeliverName())) {
//			cusSql.append(" and c.LAST_DELIVER_NAME like :lastDeliverName ");
//			params.put("lastDeliverName", "%"+customerVo.getLastDeliverName()+"%");
//		}
//		if(StringUtils.isNotEmpty(customerVo.getCreateUserName())){
//			cusSql.append(" and c.CREATE_USER_ID in (select user_id from `user` where name like :createUserName ) ");
//			params.put("createUserName", "%"+customerVo.getCreateUserName()+"%");
//		}
//		if(StringUtils.isNotEmpty(customerVo.getCusOrg())){
//			cusSql.append(" and c.CUS_ORG= :cusOrg ");
//			params.put("cusOrg", customerVo.getCusOrg());
//		}
//		if(StringUtils.isNotEmpty(customerVo.getCusType())){
//			cusSql.append(" and c.CUS_TYPE = :cusType ");
//			params.put("cusType", customerVo.getCusType());
//		}
//		if(StringUtils.isNotEmpty(customerVo.getContact())){
//			cusSql.append(" and c.CONTACT like :contact ");
//			params.put("contact", "%"+customerVo.getContact()+"%");
//		}
//		if(StringUtils.isNotEmpty(customerVo.getRemark())){
//			cusSql.append("  and c.REMARK like :remark ");
//			params.put("remark", "%"+customerVo.getRemark()+"%");
//		}
//		if(StringUtils.isNotEmpty(customerVo.getBlCampusName())){
//			cusSql.append(" and c.BL_SCHOOL in (select id from organization where name LIKE :blCampusName ) ");
//			params.put("blCampusName", "%"+customerVo.getBlCampusName()+"%");
//		}
//		if(StringUtils.isNotEmpty(customerVo.getResEntranceId())){
//			cusSql.append(" and c.RES_ENTRANCE= :resEntranceId ");
//			params.put("resEntranceId", customerVo.getResEntranceId());
//		}
//		if(StringUtils.isNotEmpty(customerVo.getBlSchool())){
//			cusSql.append(" and c.BL_SCHOOL = :blSchool ");
//			params.put("blSchool", customerVo.getBlSchool());
//		}
//		if(StringUtils.isEmpty(customerVo.getBlSchool()) && StringUtils.isNotBlank(customerVo.getBrenchId())){
//			//查询分公司下所有客户
//			params.put("brenchId", customerVo.getBrenchId());
//			cusSql.append(" and c.BL_SCHOOL in ( select id from organization where parentID= :brenchId ");
//			cusSql.append(" or c.BL_SCHOOL = :brenchId ) ");
//		}		
//		if(customerVo.getDealStatus()!=null){
//			cusSql.append(" and c.DEAL_STATUS = :dealStatus ");
//			params.put("dealStatus", customerVo.getDealStatus());
//		}
//		if(customerVo.getDeliverType() != null){
//			cusSql.append(" and c.DELIVER_TYPE = :deliverType ");
//			params.put("deliverType", customerVo.getDeliverType());
//		}
//		
//		if(StringUtils.isNotBlank(customerVo.getPointialStuGrade())){
//			cusSql.append("and s.GRADE_ID = :pointialStuGrade ");
//			params.put("pointialStuGrade", customerVo.getPointialStuGrade());
//		}
//		
//		if(StringUtils.isNotBlank(customerVo.getPointialStuSchool())){
//			cusSql.append(" and s.SCHOOL in (select id from student_school where name like :pointialStuSchool ) ");
//			params.put("pointialStuSchool", customerVo.getPointialStuSchool());
//		}
//		
//		if(StringUtils.isNotEmpty(customerVo.getDeliverTarget())){
//			cusSql.append("and c.DELEVER_TARGET = :deliverTarget ");
//			params.put("deliverTarget", customerVo.getDeliverTarget());
//		}else if(StringUtils.isNotEmpty(customerVo.getDeliverTargetName())){
//			//通过getDeliverTargetName 获取 DeliverTarget						
//			cusSql.append(" and (c.DELEVER_TARGET in (select user_id from user where name like :deliverTargetName ) or c.DELEVER_TARGET in (select id from organization  where name like :deliverTargetName ) ) ");					
//			params.put("deliverTargetName", "%"+customerVo.getDeliverTargetName()+"%");
//		}
//		
//		if(StringUtils.isNotBlank(customerVo.getCustomerList()) && customerVo.getCustomerList().equals("customer")){
//			if(StringUtils.isNotEmpty(customerVo.getStartDate())){
//				cusSql.append(" and c.CREATE_TIME >= :startDate ");
//				params.put("startDate", customerVo.getStartDate());
//			}
//			if(StringUtils.isNotEmpty(customerVo.getEndDate())){
//				cusSql.append(" and c.CREATE_TIME < :endDate ");
//				params.put("endDate", DateTools.addDateToString(customerVo.getEndDate(), 1));
//			}
//		}else{
//			if(StringUtils.isNotEmpty(customerVo.getStartDate())){
//				cusSql.append(" and c.DELIVER_TIME >= :startDate ");
//				params.put("startDate", customerVo.getStartDate());
//			}
//			if(StringUtils.isNotEmpty(customerVo.getEndDate())){
//				cusSql.append(" and c.DELIVER_TIME < :endDate ");
//				params.put("endDate", DateTools.addDateToString(customerVo.getEndDate(), 1));
//			}
//			cusSql.append(" and c.DEAL_STATUS <> '" + CustomerDealStatus.SIGNEUP + "' ");
//			cusSql.append(" and c.DEAL_STATUS <> '" + CustomerDealStatus.INVALID + "' "); 
//		}		
//		if(StringUtils.isNotEmpty(customerVo.getSchoolAddress())){
//			cusSql.append(" and c.BL_SCHOOL in (select id from organization where REGION_ID in (select id from data_dict where name like :schoolAddress )) ");
//			params.put("schoolAddress", "%"+customerVo.getSchoolAddress()+"%");
//		}
//		
//		if(StringUtils.isNotEmpty(customerVo.getBeforeDeliverTarget())){
//			cusSql.append(" and c.BEFOR_DELIVER_TARGET = :beforeDeliverTarget ");
//			params.put("beforeDeliverTarget", customerVo.getBeforeDeliverTarget());
//		}
//		if(StringUtils.isNotEmpty(customerVo.getResEntranceId())){
//			cusSql.append(" and c.RES_ENTRANCE = :resEntranceId ");
//			params.put("resEntranceId", customerVo.getResEntranceId());
//		}
//		if(StringUtils.isNotEmpty(customerVo.getIntentionOfTheCustomerId())){
//			cusSql.append(" and c.INTENTION_OF_THE_CUSTOMER = :intentionOfTheCustomerId ");
//			params.put("intentionOfTheCustomerId", customerVo.getIntentionOfTheCustomerId());
//		}
//		
//		if(StringUtils.isNotEmpty(customerVo.getKeywork())){        	
//			cusSql.append(" and (");        	
//			cusSql.append(" c.name like :keyWork ");        	
//			cusSql.append(" or c.contact like :keyWork ");     			
//			params.put("keyWork", "%"+customerVo.getKeywork()+"%");		
//        	//资源类型
//        	String cusTypeId=dataDictDao.getDataDictIdByLikeName(customerVo.getKeywork(),DataDictCategory.RES_TYPE);
//        	if(StringUtils.isNotEmpty(cusTypeId)){ 
//        		cusSql.append(" or c.CUS_TYPE in ( :cusTypeId ) ");
//        		params.put("cusTypeId", cusTypeId);
//        	}
//        	//客户意向度分级
//        	String intentionId=dataDictDao.getDataDictIdByLikeName(customerVo.getKeywork(),DataDictCategory.INTENTION_OF_THE_CUSTOMER);
//        	if(StringUtils.isNotEmpty(intentionId)){ 
//        		cusSql.append(" or c.INTENTION_OF_THE_CUSTOMER in ( :intentionId ) ");
//        		params.put("intentionId", intentionId);
//        	}
//        	
//        	cusSql.append(" )");
//        }
//		
//		if((customerVo.getDealStatus() == null || CustomerDealStatus.STAY_FOLLOW==customerVo.getDealStatus())
//				&& customerVo.getDeliverType() != null
//				&&  StringUtils.isNotEmpty(customerVo.getDeliverTarget())){//咨询师工作台 数据查新
//			
//		}else if(StringUtils.isNotBlank(customerVo.getShowDeliver()) && "1".equals(customerVo.getShowDeliver())){//已分配的客户不现实在前工序跟进管理列表里面。
//			cusSql.append(" and c.BEFOR_DELIVER_TARGET = :currentUserId ");//前工序等于自己
//			cusSql.append(" and c.DELEVER_TARGET <> :currentUserId ");//跟进不等于自己
//			params.put("currentUserId", currentUser.getUserId());
//		}else if(StringUtils.isNotBlank(customerVo.getShowDeliver()) && "2".equals(customerVo.getShowDeliver())){
//			
//		} else{ // 客户列表数据查询
//			if(StringUtils.isNotBlank(customerVo.getCustomerList()) && customerVo.getCustomerList().equals("resource")){
//				//客户资源调配不用权限查询
//			}else{
//				cusSql.append(roleQLConfigService.getValueResult("客户列表","sql"));
//			}
//			
//		}
//		
//		if (StringUtils.isNotBlank(dp.getSord())
//				&& StringUtils.isNotBlank(dp.getSidx())) {
//			cusSql.append(" order by :orderBy ");
//			params.put("orderBy", dp.getSidx()+" "+dp.getSord());
//		} 
//		
//		List<Map<Object, Object>> list = customerDao.findMapOfPageBySql(cusSql.toString(),dp,params);
////		List<Map<String,String>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dp);
//		dp.setDatas(list);
////		dp.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall "));
//		dp.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));
//			
//		
//		List<CustomerVo> voList=new ArrayList<CustomerVo>();
//		int num=0;
//		for(Map<Object,Object> tmaps : list){
//			Map<String,Object> maps = (Map)tmaps;
//			CustomerVo vo =new CustomerVo();
//			num+=1;;
//			vo=forMapToCustomer2(maps,vo,num);	
//			
////			String deliverTarget=(String) maps.get("deliverTarget");
////			if(deliverTarget != null && StringUtils.isNotBlank(deliverTarget)){
////				User user=userService.findUserById(deliverTarget);
////				if(user != null){
////					UserDeptJob dept=userDeptJobDao.findDeptJobByParam(user.getUserId(), 0);
////					UserJob job=userJobDao.findById(dept.getId().getJobId());
////					vo.setDeliverTargetJob(job == null ? "" : job.getJobName());
////				}
////			}
//		    vo.setDeliverTargetJob(maps.get("deliverTargetdeptName")!=null?maps.get("deliverTargetdeptName").toString():"");
//
//			
//			
//			//30天内的缴费金额
//			StringBuffer sql=new StringBuffer();
//			sql.append(" select c.ID,sum(fch.TRANSACTION_AMOUNT) fortyPaidAmount from customer c");
//			sql.append(" inner join contract con on c.id=con.CUSTOMER_ID");
//			sql.append(" inner join funds_change_history fch on con.id=fch.CONTRACT_ID");
//			sql.append(" where fch.TRANSACTION_TIME>=c.CREATE_TIME and fch.TRANSACTION_TIME<=DATE_SUB(c.CREATE_TIME,INTERVAL -30 day) ");
//			sql.append(" and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
//			sql.append(" and c.id= :cId ");
//			sql.append(" group by c.ID");
//			Map<String, Object> param = Maps.newHashMap();
//			param.put("cId", vo.getCusId());
////			List<Map> amountList = customerDao.findMapBySql(sql.toString());
//			List<Map<Object, Object>> amountList = customerDao.findMapBySql(sql.toString(), param);
//			if(amountList.size()>0){
//				Map<Object, Object> map=amountList.get(0);
//				vo.setFortyPaidAmount(Float.valueOf(map.get("fortyPaidAmount").toString()));
//			}else{
//				vo.setFortyPaidAmount(Float.valueOf("0.00"));
//			}
//			
//			
//			List<Contract> cList= contractDao.findContractByCustomer(vo.getCusId(), null, null);
//			//如果是已签合同，则查询第一份金额
//			if(cList.size()>0){
//					Contract contract = cList.get(0);
//					List<ContractProduct> conProds = contractProductDao.getContractProductByContractId(contract.getId());
////					Set<ContractProduct> conProds = contract.getContractProducts();
//					BigDecimal productTotalAmount = BigDecimal.ZERO;
//					for(ContractProduct conProdInLoop : conProds) {//计算合同总额
//						productTotalAmount = productTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
//					}
//					vo.setFirstContractMoney(productTotalAmount);
//					vo.setFirstContractTime(contract.getCreateTime());//add by Yao  2015-04-13  第一份合同时间
//					vo.setIsSignContract("1");//签过合同    Yao  06-3-11
//
//			}
//			
//			if(StringUtils.isNotEmpty(vo.getNextFollowupTime())){
////				List<CustomerFolowup> fvos = customerFolowupDao.findByCriteria(HibernateUtils.prepareOrder(null, "createTime", "desc"), Expression.eq("customer.id", vo.getCusId()), Expression.eq("createUser.userId", vo.getCusId()));
//				List<CustomerFolowup> fvos = customerFolowupDao.getCustomerFolowupByCusId(vo.getCusId(), vo.getCusId());
//				int size = fvos.size();
//				if(size > 0){
//					CustomerFolowup followup = fvos.get(0);
//					vo.setLastFollowUpTime(followup.getCreateTime());
//				}
//			}
//		
//			vo.setCreateUserDept(maps.get("deptName")==null?"":maps.get("deptName").toString());
////			if (vo.getCreateUserId() != null && StringUtils.isNotBlank(vo.getCreateUserId())) {
////				Map map=userService.getMainDeptAndJob(vo.getCreateUserId());
////				if(map!=null){
////					vo.setCreateUserDept(map.get("deptName")==null?"":map.get("deptName").toString());
////				}
////			}
//			
////		    List<CustomerFolowup> customerFolowups = customerFolowupDao.findByCriteria(HibernateUtils.prepareOrder(new DataPackage(), "createTime", "desc"), Expression.eq("customer.id", vo.getCusId()),Expression.eq("createUser.userId", userService.getCurrentLoginUser().getUserId()));
//			List<CustomerFolowup> customerFolowups = customerFolowupDao.getCustomerFolowupByCusId(vo.getCusId(), currentUser.getUserId());
//			if(customerFolowups.size()>0){
//				CustomerFolowup cfu=customerFolowups.get(0);
//					if(cfu.getVisitType()==null ||(cfu.getVisitType()!=null && cfu.getVisitType().equals(VisitType.NOT_COME))){
//						vo.setIsAppointCome("否");
//					}else{
//						vo.setIsAppointCome("是");
//					}
//			}
//			
//			voList.add(vo);
//			
//		}
//		dp.setDatas(voList);
		return dp;
	}

	public CustomerVo forMapToCustomer2(Map<String, Object> map,CustomerVo vo, int num){

		vo.setId(String.valueOf(num));
		vo.setCusId(StringUtil.toString(map.get("id")));
		if(map.get("name") != null){
			vo.setName(StringUtil.toString(map.get("name")));				
		}
		if(map.get("recordDate") != null){
			vo.setRecordDate(StringUtil.toString(map.get("recordDate")));
		}
		if(map.get("contact") != null){
			vo.setContact(StringUtil.toString(map.get("contact")));
		}
		
		if(map.get("cusType") != null){
			vo.setCusType(StringUtil.toString(map.get("cusType")));
			vo.setCusTypeName(StringUtil.toString(map.get("cusTypeName")));
		}
		
		if(map.get("cusOrg") != null){
			vo.setCusOrg(StringUtil.toString(map.get("cusOrg")));
			vo.setCusOrgName(StringUtil.toString(map.get("cusOrgName")));
		}
		
		if(map.get("recordUserId") != null){		
			vo.setRecordUserId(StringUtil.toString(map.get("recordUserId")));
			vo.setRecordUserName(map.get("recordUserName")!=null?map.get("recordUserName").toString():"");
		}
		if(map.get("blSchool") != null){
			vo.setBlSchool(StringUtil.toString(map.get("blSchool")));
			vo.setBlSchoolName(StringUtil.toString(map.get("blSchoolName")));
		}
		
		if(map.get("dealStatus") != null){
			String id=StringUtil.toString(map.get("dealStatus"));
			vo.setDealStatus(CustomerDealStatus.valueOf(id));
			vo.setDealStatusName(CustomerDealStatus.valueOf(id) == null ? "" : CustomerDealStatus.valueOf(id).getName());
		}
		if(map.get("deliverType") != null){
			String id=StringUtil.toString(map.get("deliverType"));
			vo.setDeliverType(CustomerDeliverType.valueOf(id));
			vo.setDeliverTypeName(CustomerDeliverType.valueOf(id) == null ? "" : CustomerDeliverType.valueOf(id).getName());
		}
		if(map.get("createUser") != null){
			vo.setCreateUserId(StringUtil.toString(map.get("createUser")));
			vo.setCreateUserName(StringUtil.toString(map.get("createUserName")));
		}
		if(map.get("deliverTarget") != null){
			vo.setDeliverTarget(StringUtil.toString(map.get("deliverTarget")));
			vo.setDeliverTargetName(StringUtil.toString(map.get("deliverTargetName")));
		}
		if(map.get("blCampusId") != null){
			vo.setBlCampusId(StringUtil.toString(map.get("blCampusId")));
			vo.setBlCampusName(StringUtil.toString(map.get("blCampusName")));
		}
		if(map.get("branchName")!=null){
			vo.setBrenchName(StringUtil.toString(map.get("branchName")));
		}
		
		if(map.get("purchasingPower") != null){
			vo.setPurchasingPowerId(StringUtil.toString(map.get("purchasingPower")));
			vo.setPurchasingPowerName(StringUtil.toString(map.get("purchasingPowerName")));
		}
		
		if(map.get("intentionOfTheCustomer") != null){
			vo.setIntentionOfTheCustomerId(StringUtil.toString(map.get("intentionOfTheCustomer")));
			vo.setIntentionOfTheCustomerName(StringUtil.toString(map.get("intentionOfTheCustomerName")));
		}
		
		if(map.get("resEntrance") != null){
			vo.setResEntranceId(StringUtil.toString(map.get("resEntrance")));
			vo.setResEntranceName(StringUtil.toString(map.get("resEntranceName")));
		}
		
		if(map.get("addressName")!=null){
			vo.setSchoolAddress(map.get("addressName").toString());
		}else{
			vo.setSchoolAddress("");
		}
		
		if(map.get("pointialStuGradeName") != null ){			
			vo.setPointialStuGrade(map.get("pointialStuGradeName").toString());
		}else{
			vo.setPointialStuGrade("");
		}
		if(map.get("pointialStuSchool") != null){
			vo.setPointialStuSchoolId(map.get("pointialStuSchool").toString());
			vo.setPointialStuSchool(map.get("pointialStuSchoolName")!=null?map.get("pointialStuSchoolName").toString():"");
		}
		vo.setPointialStuName(map.get("pointialStuName") == null ? "" : map.get("pointialStuName").toString());		
		vo.setRemark(map.get("remark") == null ? "" : map.get("remark").toString());	
		vo.setCreateTime(map.get("createTime") == null ? "" : map.get("createTime").toString());	
		vo.setLastDeliverName(map.get("lastDeliverName")== null ? "" : map.get("lastDeliverName").toString());
		vo.setLastFollowUpTime(map.get("lastFollowUpTime") == null ? "" : map.get("lastFollowUpTime").toString());
		vo.setDeliverTime(map.get("deliverTime") == null ? "" : map.get("deliverTime").toString());	
		vo.setRecordUserId(map.get("recordUserId") == null ? null : map.get("recordUserId").toString());
		vo.setNextFollowupTime(map.get("nextFollowupTime") == null ? "" : map.get("nextFollowupTime").toString());
		vo.setAppointmentDate(map.get("appointmentDate") == null ? "" : map.get("appointmentDate").toString());
		vo.setSignNextFollowupTime(map.get("signNextFollowupTime") == null ? "" : map.get("signNextFollowupTime").toString());			
		vo.setModifyTime(map.get("modifyTime") == null ? "" : map.get("modifyTime").toString());
		vo.setModifyUserId(map.get("modifyUserId") == null ? "" : map.get("modifyUserId").toString());					
		vo.setLastFollowId(map.get("lastFollowId") == null ? "" : map.get("lastFollowId").toString());
		vo.setTransferFrom(map.get("transferFrom") == null ? "" : map.get("transferFrom").toString());
		
//		vo.setIntroducer(map.get("introducer").toString());		
//		vo.setConfirmReceptionistId(map.get("confirmReceptionistId").toString());			
//		vo.setBlConsultor(map.get("blConsultor").toString());
//		vo.setIsPublic(map.get("isPublic").toString());	
//		vo.setTransferStatus(map.get("transferStatus").toString());
//		vo.setInvalidReason(map.get("invalidReason").toString());
			
		return vo;
	}
	
	
	
	
	public CustomerVo forMapToCustomer(Map<String, Object> map,CustomerVo vo, int num){
		vo.setId(String.valueOf(num));
		vo.setCusId(map.get("id").toString());
		if(map.get("name") != null){
			vo.setName(map.get("name").toString());				
		}
		if(map.get("recordDate") != null){
			vo.setRecordDate(map.get("recordDate").toString());
		}
		if(map.get("contact") != null){
			vo.setContact(map.get("contact").toString());
		}
		
		if(map.get("cusType") != null){
			String cusTypeId=map.get("cusType").toString();
			vo.setCusType(cusTypeId);
			vo.setCusTypeName(dataDictDao.findById(cusTypeId) == null ? "" : dataDictDao.findById(cusTypeId).getName());
		}
		
		if(map.get("cusOrg") != null){
			String cusOrgId=map.get("cusOrg").toString();
			vo.setCusOrg(cusOrgId);
			vo.setCusOrgName(dataDictDao.findById(cusOrgId) == null ? "" : dataDictDao.findById(cusOrgId).getName());
		}
		
		if(map.get("recordUserId") != null){
			String id=map.get("recordUserId").toString();			
			vo.setRecordUserId(id);
			vo.setRecordUserName(userService.findUserById(id)==null ? "" : userService.findUserById(id).getName());
		}
		if(map.get("blSchool") != null){
			String id=map.get("blSchool").toString();
			vo.setBlSchool(id);
			vo.setBlSchoolName(organizationDao.findById(id) == null ? "" : organizationDao.findById(id).getName());
		}
		
		if(map.get("dealStatus") != null){
			String id=map.get("dealStatus").toString();
			vo.setDealStatus(CustomerDealStatus.valueOf(id));
			vo.setDealStatusName(CustomerDealStatus.valueOf(id) == null ? "" : CustomerDealStatus.valueOf(id).getName());
		}
		if(map.get("deliverType") != null){
			String id=map.get("deliverType").toString();
			vo.setDeliverType(CustomerDeliverType.valueOf(id));
			vo.setDeliverTypeName(CustomerDeliverType.valueOf(id) == null ? "" : CustomerDeliverType.valueOf(id).getName());
		}
		if(map.get("createUser") != null){
			String id=map.get("createUser").toString();
			vo.setCreateUserId(id);
			vo.setCreateUserName(userService.findUserById(id)==null ? "" : userService.findUserById(id).getName());
		}
		if(map.get("deliverTarget") != null){
			String id=map.get("deliverTarget").toString();
			vo.setDeliverTarget(id);
			vo.setDeliverTargetName(userService.findUserById(id)==null ? "" : userService.findUserById(id).getName());
		}
		if(map.get("blCampusId") != null){
			String id=map.get("blCampusId").toString();
			vo.setBlCampusId(id);
			vo.setBlCampusName(organizationDao.findById(id) == null ? "" : organizationDao.findById(id).getName());
		}
		
		if(map.get("purchasingPower") != null){
			String id=map.get("purchasingPower").toString();
			vo.setPurchasingPowerId(id);
			vo.setPurchasingPowerName(dataDictDao.findById(id) == null ? "" : dataDictDao.findById(id).getName());
		}
		
		if(map.get("intentionOfTheCustomer") != null){
			String id=map.get("intentionOfTheCustomer").toString();
			vo.setIntentionOfTheCustomerId(id);
			vo.setIntentionOfTheCustomerName(dataDictDao.findById(id) == null ? "" : dataDictDao.findById(id).getName());
		}
		
		if(map.get("resEntrance") != null){
			String id=map.get("resEntrance").toString();
			vo.setResEntranceId(id);
			vo.setResEntranceName(dataDictDao.findById(id) == null ? "" : dataDictDao.findById(id).getName());
		}
		
		if(map.get("pointialStuGrade") != null ){			
			DataDict stuGrade=dataDictDao.findById(map.get("pointialStuGrade").toString());
			vo.setPointialStuGrade(stuGrade == null ? "" : stuGrade.getName());
		}
		if(map.get("pointialStuSchool") != null){
			String schoolId = map.get("pointialStuSchool").toString();
			vo.setPointialStuSchoolId(schoolId);
			StudentSchool stuSchool=studentSchoolDao.findById(schoolId);
			vo.setPointialStuSchool(stuSchool == null ? "" : stuSchool.getName());
		}
		vo.setPointialStuName(map.get("pointialStuName") == null ? "" : map.get("pointialStuName").toString());		
		vo.setRemark(map.get("remark") == null ? "" : map.get("remark").toString());	
		vo.setCreateTime(map.get("createTime") == null ? "" : map.get("createTime").toString());	
		vo.setLastDeliverName(map.get("lastDeliverName")== null ? "" : map.get("lastDeliverName").toString());
		vo.setLastFollowUpTime(map.get("lastFollowUpTime") == null ? "" : map.get("lastFollowUpTime").toString());
		vo.setDeliverTime(map.get("deliverTime") == null ? "" : map.get("deliverTime").toString());	
		vo.setRecordUserId(map.get("recordUserId") == null ? null : map.get("recordUserId").toString());
		vo.setNextFollowupTime(map.get("nextFollowupTime") == null ? "" : map.get("nextFollowupTime").toString());
		vo.setAppointmentDate(map.get("appointmentDate") == null ? "" : map.get("appointmentDate").toString());
		vo.setSignNextFollowupTime(map.get("signNextFollowupTime") == null ? "" : map.get("signNextFollowupTime").toString());			
		vo.setModifyTime(map.get("modifyTime") == null ? "" : map.get("modifyTime").toString());
		vo.setModifyUserId(map.get("modifyUserId") == null ? "" : map.get("modifyUserId").toString());					
		vo.setLastFollowId(map.get("lastFollowId") == null ? "" : map.get("lastFollowId").toString());
		vo.setTransferFrom(map.get("transferFrom") == null ? "" : map.get("transferFrom").toString());
		
//		vo.setIntroducer(map.get("introducer").toString());		
//		vo.setConfirmReceptionistId(map.get("confirmReceptionistId").toString());			
//		vo.setBlConsultor(map.get("blConsultor").toString());
//		vo.setIsPublic(map.get("isPublic").toString());	
//		vo.setTransferStatus(map.get("transferStatus").toString());
//		vo.setInvalidReason(map.get("invalidReason").toString());
			
		return vo;
	}
	
	public List<Customer> getCustomersForAutoCompelate(String term) {
		SimpleExpression contactLike = Expression.like("contact", term, MatchMode.START);
		SimpleExpression nameLike = Expression.like("name", term, MatchMode.START);
		return (List<Customer>) customerDao.findPageByCriteria(new DataPackage(0, 20), new ArrayList<Order>(), Expression.or(contactLike, nameLike)).getDatas();
	}

	@Override
	public CustomerVo findCustomerById(String id) throws ApplicationException {
        Customer customer = customerDao.findById(id);
    	if(customer==null)return null;
		CustomerVo customervo= HibernateUtils.voObjectMapping(customer, CustomerVo.class);
		Organization org=new Organization();
		if(StringUtils.isNotBlank(customervo.getDeliverTarget()) && customervo.getDeliverTarget()!=null){
			Organization testOrg=organizationDao.findById(customervo.getDeliverTarget());			
			if(testOrg!=null){
				Organization orgs=organizationDao.findById(customervo.getDeliverTarget());
				org=organizationDao.getBelongOrgazitionByOrgType(orgs.getOrgLevel(),orgs.getOrgType(), true);
				customervo.setDeliverTargetName(org.getCustomerPoolName());
			}else{
				 org=userService.getBelongCampusByUserId(customervo.getDeliverTarget());
				 customervo.setDeliverTargetName(userService.getUserById(customervo.getDeliverTarget()).getName());
			}			
			customervo.setDeliverTargetCampus(org.getName());
			
		}
		//客户详情里面 客户对应的学生关系
		
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		Map<String, Object> params = Maps.newHashMap();
		if (customervo != null) {
			//查询没有被删除的学生 逻辑删除
			String sql_student = "select * from student s WHERE (s.STU_STATUS = 1 OR s.STU_STATUS IS NULL) and  s.ID in (select csr.STUDENT_ID from customer_student_relation csr where csr.CUSTOMER_ID= :customerId and csr.IS_DELETED =0 )"; //#179 签续费合同时还可以查询无效的学生进行签合同
			params.put("customerId", customervo.getId());
			List<Student> list=studentDao.findBySql(sql_student,params);
			Set<StudentVo> studentVos = new HashSet<StudentVo>();
			for(Student student :list){
				StudentVo studentVo = HibernateUtils.voObjectMapping(student, StudentVo.class);
				studentVo.setDaysFromLastSignUp(findDaysFromLastSignUpByStuId(studentVo.getId()));
				//studentVo.setTransferCanSignUp(transferCanBeSignUp(customervo.getId(), currentUserId, studentVo.getId()));
				studentVo.setTransferCanSignUp(true);//解除转介绍限制
				StringBuffer  query = new StringBuffer();
				Map<String, Object> param = Maps.newHashMap();
				
				if(StudentStatus.FINISH_CLASS.getValue().equals(studentVo.getStatus())
						&&StudentType.ENROLLED.getValue().equals(studentVo.getStudentType()) ) {
					//判断是否停课15天以上
					query.delete(0, query.length());
					query.append(" SELECT IFNULL(TIMESTAMPDIFF(DAY,DATE_FORMAT(FINISH_TIME,'%Y-%m-%d'),CURDATE()),0) FROM student WHERE id ='"+studentVo.getId()+"' ");
					int finish_day = studentDao.findCountSql(query.toString(), param);
					if(finish_day > FINISH_CLASS_DAY) {
						studentVo.setOvertime(true);
					}else {
						studentVo.setOvertime(false);
					}
					studentVo.setDayNumbers(FINISH_CLASS_DAY+"");
				}
				if(StudentStatus.STOP_CLASS.getValue().equals(studentVo.getStatus())
						&&StudentType.ENROLLED.getValue().equals(studentVo.getStudentType())){
					//判断是否停课90天以上
					query.delete(0, query.length());
					query.append(" SELECT IFNULL(TIMESTAMPDIFF(DAY,DATE_FORMAT(FINISH_TIME,'%Y-%m-%d'),CURDATE()),0) FROM student WHERE id ='"+studentVo.getId()+"'  ");
					int stop_day = studentDao.findCountSql(query.toString(), param);
					if(stop_day > STOP_CLASS_DAY) {
						studentVo.setOvertime(true);
					}else {
						studentVo.setOvertime(false);
					}
					studentVo.setDayNumbers(STOP_CLASS_DAY+"");
				}
				
				studentVos.add(studentVo);
			}
			customervo.setStudentVos(studentVos);
//			Criterion studentCriterion = Expression.eq("customerId", customervo.getId());
//			List<PointialStudent> pstudentListsList = pointialStudentDao.findByCriteria(studentCriterion);
//			if (pstudentListsList.size() > 0) {
//				customervo.setStudents(pstudentListsList);
//			}
		}
		
		
		
		
		if(StringUtils.isNotEmpty(customervo.getNextFollowupTime())){
			List<CustomerFolowup> fvos = customerFolowupDao.findByCriteria(HibernateUtils.prepareOrder(null, "createTime", "desc"), Expression.eq("customer.id", customervo.getId()));
			int size = fvos.size();
			if(size > 0){
				CustomerFolowup followup = fvos.get(0);
				customervo.setLastFollowUpTime(followup.getCreateTime());
			}
		}
		//2016-08-13 增加查询客户的信息：分配人  跟进人  跟进状态 所属校区  所属分公司  第一份合同时间 第一份合同金额   xiaojinwang
		List<Contract> cList= contractDao.findContractByCustomer(id, null, null);
		//如果是已签合同，则查询第一份金额
		if(cList.size()>0){
				Contract contract = cList.get(0);
				Set<ContractProduct> conProds = contract.getContractProducts();
				BigDecimal productTotalAmount = BigDecimal.ZERO;
				for(ContractProduct conProdInLoop : conProds) {//计算合同总额
					productTotalAmount = productTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				}
				customervo.setFirstContractMoney(productTotalAmount);
				customervo.setFirstContractTime(contract.getCreateTime()!=null?contract.getCreateTime().substring(0, 10):"");//add by Yao  2015-04-13  第一份合同时间
				customervo.setIsSignContract("1");//签过合同    Yao  06-3-11
		}
		//跟进人
		if(StringUtils.isNotEmpty(customervo.getDeliverTarget())){
			if(customervo.getDeliverType()!=null && customervo.getDeliverType()==CustomerDeliverType.PERSONAL_POOL){
				User user = userService.loadUserById(customervo.getDeliverTarget());
				customervo.setDeliverTargetName(user!=null?user.getName():"");
				customervo.setDeliverTypeName(customervo.getDeliverType().getValue());
			}else if(customervo.getDeliverType()!=null && customervo.getDeliverType()==CustomerDeliverType.CUSTOMER_RESOURCE_POOL ){
				Organization organization = organizationDao.findById(customervo.getDeliverTarget());
				customervo.setDeliverTargetName(organization!=null?organization.getCustomerPoolName():"");
				customervo.setDeliverTypeName(customervo.getDeliverType().getValue());
			}
		}
		//最新分配人lastDeliverName

		
		//校区  分公司
		String brenchName ="-";
		String blSchoolName="";
		if(StringUtils.isNotBlank(customervo.getBlSchool())){				
			Organization organization = organizationDao.findById(customervo.getBlSchool());
			if(organization!=null){
				blSchoolName =organization.getName();	
			}
			String orgid="";				
			if(organization.getOrgType().getValue().equals(OrganizationType.CAMPUS.getValue())){
				orgid=organization.getParentId();
			}else{
				orgid=organization.getId();
			}			
			Organization orgParent=organizationDao.findById(orgid);
			if(orgParent!=null){
				brenchName=orgParent.getName();
			}
		}
		customervo.setBlSchoolName(blSchoolName);
		customervo.setBrenchName(brenchName);
		
		return customervo;
	}

	/**
	 * 查询学生距离现在最近一次合同时间
	 * @param stuId
	 * @return
	 */
	private int findDaysFromLastSignUpByStuId(String stuId) {
		Map<String, Object> params = Maps.newHashMap();
		String sql = "SELECT * FROM contract WHERE STUDENT_ID = :stuId ORDER BY CREATE_TIME DESC LIMIT 1 ";
		params.put("stuId", stuId);
		List<Contract> contracts = contractDao.findBySql(sql, params);
		if (contracts.size()==0){
			return -1;//没有合同
		}else {
			Contract contract = contracts.get(0);
			String createTime = contract.getCreateTime();
			int	days = -1;
			try {
				days = DateTools.getDateSpace(createTime, DateTools.getCurrentDate());
			} catch (ParseException e) {
				e.printStackTrace();
				days= -1;
			}finally {
				return days;
			}
		}
	}
	
	/**
	 * 判断学生是否目前登陆者的转介绍学生
	 * @param studentId
	 * @return
	 */
	private Boolean transferCanBeSignUp(String customerId,String currentUserId,String studentId){
		//首先判断此学生是不是转介绍学生  不是 则返回true
		//如果是 则判断是否是当前登陆者的转介绍学生 是 返回true 不是则返回false
		String transferSql = "select * from transfer_introduce_customer where customer_id = :cusId and student_id = :stuId ";
		Map<String, Object> params = Maps.newHashMap();
		params.put("stuId", studentId);
		params.put("cusId", customerId);
		List<TransferCustomerRecord> tRecords = transferCustomerDao.findBySql(transferSql, params);
		if(tRecords!=null && tRecords.size()>0){
			TransferCustomerRecord transferCustomerRecord = tRecords.get(0);
			if(currentUserId.equals(transferCustomerRecord.getCreateUserId())){
				return true;
			}else {					
	    		String createUserId = transferCustomerRecord.getCreateUserId();
	    		User user = userService.loadUserById(createUserId);
	    		if(user!=null && user.getEnableFlg()==1){
	    			//如果原来的转介绍登记人被禁用掉，比如离职了 则可以签单
	    			return true;
	    		}else{
	    			//没用禁用 判断是不是老师这种角色的人登记的
		    		List<Role> roles = userService.getRoleByUserId(createUserId);
	                Boolean flag = false;
		    		for(Role role: roles){
		    			if(role.getRoleCode()==RoleCode.TEATCHER){
	                        flag = true;
	                        break;
		    			}
		    		}				
					return flag;
	    		}
			}
		}else{
			return true;
		}
	}
	
	
	

	@Override
	public CustomerVo findCustomerById(Customer cus) throws ApplicationException {
		Customer customer = customerDao.findById(cus.getId());
		CustomerVo customervo= HibernateUtils.voObjectMapping(customer, CustomerVo.class);
		Organization org=new Organization();
		if(StringUtils.isNotBlank(customervo.getDeliverTarget()) && customervo.getDeliverTarget()!=null){
			Organization testOrg=organizationDao.findById(customervo.getDeliverTarget());			
			if(testOrg!=null){
				Organization orgs=organizationDao.findById(customervo.getDeliverTarget());
				org=organizationDao.getBelongOrgazitionByOrgType(orgs.getOrgLevel(),orgs.getOrgType(), true);
				customervo.setDeliverTargetName(org.getCustomerPoolName());
			}else{
				 org=userService.getBelongCampusByUserId(customervo.getDeliverTarget());
				 customervo.setDeliverTargetName(userService.getUserById(customervo.getDeliverTarget()).getName());
			}			
			customervo.setDeliverTargetCampus(org.getName());
			
		}	
//		if (customervo != null) {
//			Criterion studentCriterion = Expression.eq("customerId", customervo.getId());
//			List<PointialStudent> pstudentListsList = pointialStudentDao.findByCriteria(studentCriterion);
//			if (pstudentListsList.size() > 0) {
//				customervo.setStudents(pstudentListsList);
//			}
//		}
		if(StringUtils.isNotEmpty(customervo.getNextFollowupTime())){
			List<CustomerFolowup> fvos = customerFolowupDao.findByCriteria(HibernateUtils.prepareOrder(null, "createTime", "desc"), Expression.eq("customer.id", customervo.getId()));
			int size = fvos.size();
			if(size > 0){
				CustomerFolowup followup = fvos.get(0);
				customervo.setLastFollowUpTime(followup.getCreateTime());
			}
		}
		
		if(customervo.getBlSchool() != null){
			Organization campus=organizationDao.findById(customervo.getBlSchool());
			customervo.setBlSchoolName(campus.getName());
			customervo.setBrenchName(organizationDao.findById(campus.getParentId()).getName());
		}
		
		//查询第一份合同时间和金额
		if(customervo.getId() != null && cus.getRemark().equals("contact")){
			List<Contract> cList= contractDao.findContractByCustomer(customervo.getId(), null, null);
			if(cList.size()>0){
				Contract contract = cList.get(0);
				Set<ContractProduct> conProds = contract.getContractProducts();
				BigDecimal productTotalAmount = BigDecimal.ZERO;
				for(ContractProduct conProdInLoop : conProds) {//计算合同总额
					productTotalAmount = productTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				}
				customervo.setFirstContractMoney(productTotalAmount); 
				customervo.setFirstContractTime(contract.getCreateTime());
				customervo.setIsSignContract("1");	
			}
		}
		
		return customervo;
	}
	/**
	 * 手机接口
	 *
	 * @param id 客户id
	 * @return
	 */
	@Override
	public CustomerLessVo findCustomerLessVoById(String id) {
		CustomerVo customerVo = findCustomerById(id);
		return HibernateUtils.voObjectMapping(customerVo, CustomerLessVo.class);
	}

	public Customer findById(String id){
		return customerDao.findById(id);
	}

	@Override
	public void savePaddingStudentJsonInfo(Set<StudentImportVo> vos,String customerId) throws ApplicationException {
		try {				
			for (StudentImportVo vo : vos) {
				//保存潜在学生到student表
				studentService.savePotentialStudent(vo,customerId);					
				

				//customerEventService.saveCustomerDynamicStatus(customerId, CustomerEventType.FOLLOWING, "修改潜在学生信息", "");
								
			}
		    User currentUser = userService.getCurrentLoginUser();
		    Customer customer = customerDao.findById(customerId);
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.UPDATE_STUDENTINFO);
			dynamicStatus.setDescription("修改潜在学生信息");
			if(customer.getResEntrance()!=null){
				dynamicStatus.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
            customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, currentUser);	
			
		} catch(Exception e) {
		}
		
		
	}
	
	/**
	 * 删除潜在学生和关系表
	 */
	public void delPotentialStudent(String stuId){
		Map<String, Object> params = Maps.newHashMap();
		params.put("stuId", stuId);
		String sql="DELETE FROM customer_student_relation where STUDENT_ID= :stuId ";
		customerStudentDao.excuteSql(sql,params);
		customerStudentDao.flush();
		String stuSql="DELETE FROM student where id= :stuId and STUDENT_TYPE='POTENTIAL'";
		studentDao.excuteSql(stuSql,params);
		studentDao.flush();
	}
	
	@Override
	public DataPackage gtCustomerFollowingRecords(Customer res, DataPackage dp) throws ApplicationException {
		Order descOrder = Order.desc("createTime");
		List<Order> descOrderList = new ArrayList<Order>();
		descOrderList.add(descOrder);
		 List<CustomerFolowup> customerFolowups = customerFolowupDao.findByCriteria(HibernateUtils.prepareOrder(dp, "createTime", "desc"), Expression.eq("customer.id", res.getId()));
		 dp.setDatas(HibernateUtils.voListMapping(customerFolowups, CustomerFolowupVo.class));
		return dp;
	}

	@Override
	public void saveCustomerFollowRecord(CustomerFolowupVo follow) {
		//预约上门如果是查询是否有not_come的预约如果有并且预约的是同一天，则更新meetingTime
		
		User referUser = userService.getCurrentLoginUser();		
		CustomerFolowup customerFolowup = HibernateUtils.voObjectMapping(follow, CustomerFolowup.class);
		AppointmentType appointmentType = customerFolowup.getAppointmentType();
		Customer customer = customerDao.findById(follow.getCustomerId());
			
		if(AppointmentType.APPOINTMENT_ONLINE==appointmentType){			
			//如果是网络专员 分配营运经理  //改为 分配校区营运主任
			String currentDeliverTarget = customer.getDeliverTarget();
			if(currentDeliverTarget==null){
				throw new ApplicationException("当前客户没有跟进人不能分配校区营运主任");//分配校区营运主任
			}
			if(!currentDeliverTarget.equals(referUser.getUserId())){
				throw new ApplicationException("当前客户已经其他跟进人跟进，不能分配校区营运主任");
			}
			//记录的类型是 APPOINTMENT_ONLINE dealStatus=NEW //此行作废 需求变更了  客户deslStatus 是待跟进
			//需求变更后 新增代码 begin
			CustomerVo customerVo = new CustomerVo();
			customerVo.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
			customerVo.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
			customerVo.setRemark("网络分配校区营运主任");
			customerVo.setId(follow.getCustomerId());
			customerVo.setDeliverTarget(follow.getAgencyUserId());//被分配的校区营运主任userId
			Response res = this.allocateCustomer(customerVo, follow.getCustomerId(), referUser.getUserId(),"网络分配校区营运主任更换跟进对象",CustomerEventType.APPOINTMENT_ONLINEDELIVER);
            if(res.getResultCode()!=0){
            	throw new ApplicationException(res.getResultMessage());
            }
          //需求变更后 新增代码 end
			
			//考虑重复 资源分配的问题
			List<CustomerFolowup>  list = customerFolowupService.findRecordbyUserIdAndCustomerId(referUser.getUserId(), follow.getCustomerId(), null);
			if(list!=null && list.size()>0){
				CustomerFolowup followupRecord = list.get(0);
				if(AppointmentType.APPOINTMENT_ONLINE_OPERATION==followupRecord.getAppointmentType()){
					throw new ApplicationException("当前客户已经分配其他跟进人跟进，请勿重复分校区营运主任");//改变需求后 这一步不会执行
				}else if(AppointmentType.APPOINTMENT_ONLINE==followupRecord.getAppointmentType()){
					if(follow.getAgencyUserId()!=null){
						Organization organization =userService.getBelongBranchByUserId(follow.getAgencyUserId());
						if(organization!=null){
							followupRecord.setAppointBranch(organization.getOrgLevel());
						}
					}
					User agencyUser = userService.loadUserById(follow.getAgencyUserId());
					followupRecord.setRemark(follow.getRemark());
					followupRecord.setAgencyUser(agencyUser);
					followupRecord.setCreateTime(DateTools.getCurrentDateTime());
					followupRecord.setCreateUser(referUser);
					customerFolowupDao.save(followupRecord);
				}
			}else{
				if(follow.getAgencyUserId()!=null){
					Organization organization =userService.getBelongBranchByUserId(follow.getAgencyUserId());
					if(organization!=null){
						customerFolowup.setAppointBranch(organization.getOrgLevel());
					}
				}
				customerFolowup.setCreateTime(DateTools.getCurrentDateTime());
				customerFolowup.setCreateUser(referUser);
				customerFolowupDao.save(customerFolowup);				
			}
//			UserEventRecord userEventRecord = new UserEventRecord();
//			userEventRecord.setCustomerId(customerVo.getId());
//			userEventRecord.setUserId(referUser.getUserId());
//			userEventRecord.setEventType(EventType.APPOINTMENT_ONLINE);
//			userEventRecord.setUserName(referUser.getName());
//			userEventRecord.setCreateTime(DateTools.getCurrentDateTime());
//			userEventRecord.setStatusNum(1);
//			userEventRecordService.saveUserEventRecord(userEventRecord);
			
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.APPOINTMENT_ONLINE);
			dynamicStatus.setDescription("网络分配校区营运主任");
			if(customer.getResEntrance()!=null){
			   dynamicStatus.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setTableName("customer_folowup");
			dynamicStatus.setTableId(customerFolowup.getId());
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);
			
			//#1728 计算网络分配时间 20171110 duanmenrun
			CustomerAllocateObtain  customerAllocateObtain = customerAllocateObtainService.findAllocateObtainByCustomerId(follow.getCustomerId());
			if(customerAllocateObtain == null ) {
				customerAllocateObtain = new CustomerAllocateObtain();
				customerAllocateObtain.setCustomerId(follow.getCustomerId());
			}
			customerAllocateObtain.setAllocateTime(DateTools.getCurrentDateTime());
			customerAllocateObtain.setObtainTime(DateTools.getCurrentDateTime());
			customerAllocateObtain.setStatus(0);
			customerAllocateObtainService.saveOrUpdateCustomerAllocateObtain(customerAllocateObtain);
			
		}else if(AppointmentType.APPOINTMENT==(appointmentType)){
			//根据custoemrId获取记录
			
			Boolean isZXS = userService.isUserRoleCode(referUser.getUserId(), RoleCode.CONSULTOR);
			Boolean isZXZG = userService.isUserRoleCode(referUser.getUserId(), RoleCode.CONSULTOR_DIRECTOR);
			Boolean isWHZG = userService.isUserRoleCode(referUser.getUserId(), RoleCode.OUTCALL_MANAGER);
			Boolean isBWHZG = userService.isUserRoleCode(referUser.getUserId(), RoleCode.BRANCH_OUTCALL_MANAGER);
			Boolean isWHZY = userService.isUserRoleCode(referUser.getUserId(), RoleCode.OUTCALL_SPEC);
			if(isZXS||isWHZG||isWHZY||isBWHZG||isZXZG){				
			}else{
				throw new ApplicationException("该角色不能进行预约上门");
			}
			
	        Map<String, Object> params = Maps.newHashMap();
			params.put("customerId", follow.getCustomerId());
			params.put("visitType", VisitType.NOT_COME.getValue());
			params.put("appointmentType", AppointmentType.APPOINTMENT.getValue());
			
			
			StringBuilder query =new StringBuilder(128);
			query.append(" select * from customer_folowup ");
			query.append(" where CUSTOMER_ID = :customerId ");
			query.append(" and VISIT_TYPE = :visitType ");
			query.append(" and APPOINTMENT_TYPE = :appointmentType ");
			List<CustomerFolowup> list=customerFolowupDao.findBySql(query.toString(),params);
			if(list!=null && list.size()>0){
				CustomerFolowup followup = list.get(0);
				String meetingTime_old = followup.getMeetingTime();
				String meetingTime_new = follow.getMeetingTime();
				//如果两个日期是同一天，则更新meeting_Time
				if(DateTools.isSameDate(meetingTime_old, meetingTime_new, "yyyy-MM-dd HH:mm")){
					followup.setMeetingTime(follow.getMeetingTime());
					followup.setAppointCampus(customerFolowup.getAppointCampus());
					followup.setSatisficing(customerFolowup.getSatisficing());
					followup.setRemark(customerFolowup.getRemark());
					customerFolowupDao.save(followup);
				}else{
					customerFolowup.setCreateTime(DateTools.getCurrentDateTime());
					customerFolowup.setCreateUser(referUser);
					customerFolowup.setVisitType(VisitType.NOT_COME);
					customerFolowupDao.save(customerFolowup);
				}			
			}else{
				//如果是预约上门，设置visit_Type为没上门    2016-09-26  
				customerFolowup.setCreateTime(DateTools.getCurrentDateTime());
				customerFolowup.setCreateUser(referUser);
				customerFolowup.setVisitType(VisitType.NOT_COME);
				customerFolowupDao.save(customerFolowup);
			}	
			
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.APPOINTMENT);
			dynamicStatus.setDescription(follow.getRemark()!=null?("保存预约上门记录：" + follow.getRemark()):"保存预约上门记录");
			if(customer.getResEntrance()!=null){
			   dynamicStatus.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setTableName("customer_folowup");
			dynamicStatus.setTableId(customerFolowup.getId());
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);
			
		}else if(AppointmentType.APPOINTMENT_ONLINE_OPERATION == appointmentType){
			//分配校区   更换校区    
			//获取记录  分配给营运经理的记录
			CustomerFolowup folowup = customerFolowupDao.findById(customerFolowup.getId());
			
			if(StringUtils.isBlank(customerFolowup.getRemark())){
				throw new ApplicationException("请求出错");
			}
			if(customerFolowup.getRemark().equals("distribute")){
				//分配校区
	            folowup.setAppointmentType(appointmentType);
	            folowup.setAppointCampus(customerFolowup.getAppointCampus());
				//更改状态
				folowup.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
				customerFolowupDao.save(folowup);	
			}else if(customerFolowup.getRemark().equals("change")){
			    //更换校区
				//更换校区的逻辑:客户是待跟进才可以更换
				String current_delivertarget = customer.getDeliverTarget();
				String folowupCreateUser = folowup.getCreateUser().getUserId();
				if(!current_delivertarget.equals(folowupCreateUser) && customer.getDealStatus()!=CustomerDealStatus.STAY_FOLLOW){
					throw new ApplicationException("客户不是待跟进中，更换校区失败");
				}
				//可以更换
				//修改校区
				folowup.setAppointCampus(customerFolowup.getAppointCampus());
				folowup.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
				customerFolowupDao.save(folowup);
				
				if(!current_delivertarget.equals(folowupCreateUser)){
					customer.setDeliverTarget(folowupCreateUser);
					customer.setDealStatus(CustomerDealStatus.FOLLOWING);
					customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
				}
						
//				DeliverTargetChangeRecord record =deliverTargetChangeService.getLastChangeRecord(follow.getCustomerId());
//				if(record!=null){
//					//根据最新的记录来还原  到某个网络专员来跟进中
//					String target = record.getPreviousTarget();
//					if(userService.loadUserById(target)!=null){
//						customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
//					}else{
//						customer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
//					}
//					customer.setDeliverTarget(record.getPreviousTarget());	
//				}
				
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.APPOINTMENT_ONLINE_OPERATION);
				dynamicStatus.setDescription("保存营运经理分配校区记录");
				if(customer.getResEntrance()!=null){
				   dynamicStatus.setResEntrance(customer.getResEntrance());
				}
				dynamicStatus.setStatusNum(1);
				dynamicStatus.setTableName("customer_folowup");
				dynamicStatus.setTableId(customerFolowup.getId());
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
				customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);

			
			}			
		}else{
			customerFolowup.setCreateTime(DateTools.getCurrentDateTime());
			customerFolowup.setCreateUser(referUser);
			customerFolowupDao.save(customerFolowup);
			
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.FOLLOWING);
			dynamicStatus.setDescription(follow.getRemark()!=null?("保存跟进记录：" + follow.getRemark()):"保存跟进记录");
			if(customer.getResEntrance()!=null){
			   dynamicStatus.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setTableName("customer_folowup");
			dynamicStatus.setTableId(customerFolowup.getId());
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);
		}
		
		
		//修改客户表备注信息
		if(customer!=null){
			//customer.setModifyTime(DateTools.getCurrentDateTime());
			//customer.setModifyUserId(userService.getCurrentLoginUser().getUserId());
			
			//这里的满意度是处理添加跟进记录的满意度
			if(AppointmentType.FOLLOW_UP==(appointmentType)){
				//customer.setRemark(follow.getRemark()); 20170622 修改不更新备注
				if(customerFolowup.getSatisficing()!=null){
			       customer.setIntentionOfTheCustomer(customerFolowup.getSatisficing());
				}
			}
			customer.setLastFollowUpTime(DateTools.getCurrentDateTime());
			customer.setLastFollowUpUser(referUser);
			//如果是网络平台分配校区则需要保存跟进状态
//			if(StringUtils.isNotBlank(follow.getDealStatus())){
//				customer.setDealStatus(CustomerDealStatus.valueOf(follow.getDealStatus()));
//			}
			//不修改customer的状态 2017-02-22
			
//			if(follow.getDealStatus()!=null){
//		     	customer.setDealStatus(follow.getDealStatus());
//		    }
						
			if(AppointmentType.APPOINTMENT==(appointmentType)){
				Organization blcampus = organizationDao.findById(follow.getAppointCampusId());
				customer.setAppointmentDate(customerFolowup.getMeetingTime());
				customer.setBlSchool(follow.getAppointCampusId());
				customer.setBlCampusId(blcampus);	
			}else if(AppointmentType.FOLLOW_UP==(appointmentType)){
				if(StringUtil.isNotBlank(customerFolowup.getMeetingTime())){
					customer.setNextFollowupTime(customerFolowup.getMeetingTime());
				}
				CustomerActive customerActive = getCustomerActiveByCustomer(customer);
				customer.setCustomerActive(customerActive);//添加根据记录的时候要把客户变为活跃
			}else if(AppointmentType.APPOINTMENT_ONLINE_OPERATION==(appointmentType)){
				Organization blcampus = organizationDao.findById(follow.getAppointCampusId());
				customer.setBlSchool(follow.getAppointCampusId());
				customer.setBlCampusId(blcampus);
			}else if(AppointmentType.APPOINTMENT_ONLINE ==appointmentType){
				//网络分配给营运经理  记录最新分配时间最新分配人 为某某网络专员
				//网络分配校区营运主任 要同时修改客户所属的校区
				Organization blcampus = userService.getBelongCampusByUserId(follow.getAgencyUserId());
				customer.setBlSchool(blcampus.getId());
				customer.setBlCampusId(blcampus);					
				customer.setDeliverTime(DateTools.getCurrentDateTime());
				customer.setLastDeliverName(referUser.getName());
				
			}
			//修改customer的modifyTime 系统回收的时间依据
			customer.setModifyTime(DateTools.getCurrentDateTime());
			customer.setModifyUserId(referUser.getUserId());
			
			//  add by Yao 2015-04-22
			customer.setPurchasingPower(customerFolowup.getPurchasingPower());
		}
				
				
		//添加跟进周期内有效备注量
		//多次添加备注 仅计算一次
		if(AppointmentType.FOLLOW_UP.equals(appointmentType)){
			//如果是跟进中记录
			//查询是否有多条记录
			Map<String, Object> param = Maps.newHashMap();
			param.put("appointmentType", AppointmentType.FOLLOW_UP.getValue());
			param.put("customerId", follow.getCustomerId());
			StringBuilder query =new StringBuilder(128);
			query.append(" select * from customer_folowup cf where appointment_type = :appointmentType ");
			query.append(" and cf.customer_id = :customerId ORDER BY cf.create_time desc ");			

			//String hql = "from CustomerFolowup cf where cf.appointmentType ='FOLLOW_UP' and cf.customer.id = '"+follow.getCustomerId()+"' order by cf.createTime desc";
			//customerFolowupService.findByHql(hql);
			List<CustomerFolowup> list = customerFolowupDao.findBySql(query.toString(),param);
			if(list!=null && list.size()==0){
				//添加跟进周期内有效备注量
				
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.RESOURCES);
				dynamicStatus.setDescription("跟进周期内有效备注量加一");
				dynamicStatus.setResEntrance(customer.getPreEntrance());
				dynamicStatus.setStatusNum(1);
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
				customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);
				
			}
		}
		
		
	}

	/**
	 * 判断客户 是否6个月未跟进
	 * 有学生就以学生为准，否则返回活跃
	 * @param customer
	 * @return
	 */
	private CustomerActive getCustomerActiveByCustomer(Customer customer) {
		return CustomerActive.ACTIVE;
//		if (customer!=null){
//			Map<String, Object> params = new HashMap<>();
//			String sql = "select * from customer_student_relation where CUSTOMER_ID= :customerId ";
//			params.put("customerId", customer.getId());
//			List<CustomerStudent> list = customerStudentDao.findBySql(sql, params);
//			if (list.size()==0){
//				return CustomerActive.ACTIVE;
//			}else {
//				StringBuffer sb = new StringBuffer();
//				boolean flag = true;
//				Map<String, Object> params1 = new HashMap<>();
//				for (CustomerStudent customerStudent : list){
//					String studentId = customerStudent.getStudent().getId();
//					sb.append("SELECT COUNT(1) FROM ACCOUNT_CHARGE_RECORDS acr WHERE acr.CHARGE_TYPE = 'NORMAL' AND acr.PRODUCT_TYPE <>'OTHERS' AND acr.STUDENT_ID = :studentId AND acr.TRANSACTION_TIME > DATE_SUB(curdate(), INTERVAL 180 DAY)");
//					params1.put("studentId", studentId);
//					int count = accountChargeRecordsDao.findCountSql(sb.toString(), params1);
//					params1.remove("studentId");
//					if (count>0){
//						flag = false;
//						break;
//					}
//					sb.delete(0,sb.length());
//				}
//				if (flag){
//					return CustomerActive.ACTIVE;
//				}else {
//					return CustomerActive.ACTIVE;
//				}
//			}
//		}else {
//			throw new ApplicationException("找不到客户");
//		}
	}

	/* (non-Javadoc)
	 * @see com.eduboss.service.CustomerService#findCustomerDynamicCount(java.lang.String, java.lang.String)
	 */
	@Override
	public Map findCustomerDynamicCount(String StartDate, String endDate) {
		String sql="select sum(case when DYNAMIC_STATUS_TYPE='NEW' then 1 else 0 end) as news,"
				+ "sum(case when DYNAMIC_STATUS_TYPE='DELIVER' then 1 else 0 end) as deliver,"
				+ "sum(case when DYNAMIC_STATUS_TYPE='FOLLOWING' then 1 else 0 end) as follow,"
				+ "sum(case when DYNAMIC_STATUS_TYPE='APPOINTMENT' then 1 else 0 end) as appoin,"
				+ "sum(case when DYNAMIC_STATUS_TYPE='CONTRACT_SIGN' then 1 else 0 end) as sign,"
				+ "sum(case when DYNAMIC_STATUS_TYPE='BACK' then 1 else 0 end) as back,"
				+ "sum(case when DYNAMIC_STATUS_TYPE='INVALID' then 1 else 0 end) as invalid"
				+ " from customer_dynamic_status where 1=1 ";
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(StartDate)){
			sql+=" and OCCOUR_TIME>= :startDate ";
			params.put("startDate", StartDate+" 00:00:00 ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sql+=" and OCCOUR_TIME<= :endDate ";
			params.put("endDate", endDate+" 23:59:59 ");
		}
		sql+=" and referuser_id='"+userService.getCurrentLoginUser().getUserId()+"'";
		List<Map<Object,Object>> list = customerDao.findMapBySql(sql,params);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取公共资源池资源
	 * 根据poolType获取用户所在的分公司公共资源和校区公共资源
	 * 客户字段DELIVER_TYPE取值：1-分公司资源，2-分公司公共资源，3-校区资源，4-校区公共资源，5-分配给咨询师
	 * 根据用户ID获取归属分公司ID及校区ID，用来作为查询的条件
	 * 分页支持
	 * @throws ApplicationException 
	 */
	@Override
	public DataPackage getCustomerInPublicPool(String userId, String poolType,
			DataPackage dp) throws ApplicationException {
		//顾客表里面查1）DELIVER_TYPE为3，blSchool和user同一个校区的；2）DELIVER_TYPE为1，blBranch与user同一个分公司的
		User user = userService.getUserById(userId);
		String orgLevelId = user.getOrgLevelId();
/*		int bpos = orgLevelId.indexOf("-");
		int lpos = orgLevelId.lastIndexOf("-");
		String branchOfUser = orgLevelId.substring(bpos+1, lpos);
		String schoolOfUser = orgLevelId.substring(lpos+1);*/
		String[] orgLevel=orgLevelId.split("-");
		String branchOfUser = orgLevel[1];
		String schoolOfUser = orgLevel[2];
		Order descOrder = Order.desc("createTime");
		List<Order> descOrderList = new ArrayList<Order>();
		descOrderList.add(descOrder);
		List<Criterion> cusCriterion = new ArrayList<Criterion>();
		Criterion schoolCriterion = null;
		Criterion branchCriterion = null;
		Criterion myCriterion = null;
		schoolCriterion = Expression.isNotEmpty("blSchool");
		schoolCriterion = Expression.and(schoolCriterion, Expression.eq("blSchool",schoolOfUser));
		schoolCriterion = Expression.and(schoolCriterion, Expression.eq("deliverType","3"));
		branchCriterion = Expression.isNotEmpty("blBranch");
		branchCriterion = Expression.and(branchCriterion, Expression.eq("blBranch",branchOfUser));
		branchCriterion = Expression.and(branchCriterion, Expression.eq("deliverType","1"));
		myCriterion = Expression.or(schoolCriterion, branchCriterion);
		cusCriterion.add(myCriterion);
		return customerDao.findPageByCriteria(dp, descOrderList,myCriterion);
	}

	@Override
	public DataPackage getAutoCompeleteCustomer(String inputStr, DataPackage dp) {
		// TODO Auto-generated method stub
		List<Criterion> criterionList = null;
		if(inputStr!=null && !(inputStr.trim().equals("")))
		{
			//把输入数据与姓名或电话作比较
			Criterion myCriterion = null;
			myCriterion = Expression.or(Expression.like("name", inputStr), Expression.like("contact", inputStr));
			criterionList.add(myCriterion);
		}
		return customerDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "createTime", "desc"), criterionList);
	}

	@Override
	public void addCustomerAppointment(CustomerAppointmentVo appointment)
			throws ApplicationException {
		//修改客户中的预约时间
		Customer customer = customerDao.findById(appointment.getCustomerId());
		if (customer == null) {
			throw new ApplicationException(ErrorCode.CUSTOMER_NOT_FOUND);
		} else {
			if(StringUtils.isNotEmpty(appointment.getBlSchool())){
				customer.setBlSchool(appointment.getBlSchool());
				customer.setBlCampusId(organizationDao.findById(appointment.getBlSchool()));
			}
			
			customer.setAppointmentDate(appointment.getMeetingTime());			
			customer.setVisitType(null);			
			customerDao.save(customer);
		}
		
		//如果已存在一条相同的客户与咨询师但未确认上门的记录，直接把该记录改成最新的预约时间
		List<CustomerAppointment> oldappointments = customerAppointmentDao.findByCriteria(Expression.eq("customer.id", appointment.getCustomerId()),Expression.eq("appointmentType", AppointmentType.APPOINTMENT), Expression.isNull("meetingConfirmTime"));
		if (oldappointments.size() > 0) {
			oldappointments.get(0).setMeetingTime(appointment.getMeetingTime());
			oldappointments.get(0).setAppointmentTime(DateTools.getCurrentDateTime());//  以前没加这个时间   ？  add  by Yao 2015-04-21
			appointment.setAppointmentTime(DateTools.getCurrentDateTime());
		} else {
			appointment.setAppointmentTime(DateTools.getCurrentDateTime());
			CustomerAppointment customerAppointment = new CustomerAppointment();//HibernateUtils.voObjectMapping(appointment, CustomerAppointment.class);
			customerAppointment.setAppointmentUser(userService.getCurrentLoginUser());
			customerAppointment.setMeetingTime(appointment.getMeetingTime());
			customerAppointment.setCustomer(customer);
			customerAppointment.setAppointmentTime(DateTools.getCurrentDateTime());//  以前没加这个时间   ？  add  by Yao 2015-04-21
			customerAppointment.setAppointmentType(AppointmentType.APPOINTMENT);
			customerAppointmentDao.save(customerAppointment);
		}
		
		customerEventService.saveCustomerDynamicStatus(appointment.getCustomerId(), CustomerEventType.APPOINTMENT, "预约客户："+appointment.getMeetingTime(), "");
		
		//TODO: 推送一个通知给前台
	}

	@Override
	public DataPackage getTodayAppointment(CustomerAppointment customerAppointment, DataPackage dp) {
		// 要添加查询条件: 当前用户ID, 今天的日期, 咨询师只能看自己的，前台能看本校区的
		List<Criterion> criterionList = new ArrayList<Criterion>();
		
		// 今天日期的
		if(StringUtils.isNotEmpty(customerAppointment.getMeetingTime())){
			criterionList.add(Expression.like("meetingTime",customerAppointment.getMeetingTime(), MatchMode.START));
		}else{
			criterionList.add(Expression.like("meetingTime",DateTools.getCurrentDate(), MatchMode.START));
		}
		criterionList.add(Expression.isNull("meetingConfirmTime"));
		criterionList.add(Expression.eq("appointmentType", AppointmentType.APPOINTMENT));
		// 如果是前台或校区主任，只显示本校区的
		if (userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST)
				|| userService.isCurrentUserRoleCode(RoleCode.CAMPUS_DIRECTOR)) {
			criterionList.add(Expression.eq("customer.blSchool", userService.getBelongCampus().getId()));
		}else{
			criterionList.add(Expression.eq("customer.deliverTarget", userService.getCurrentLoginUser().getUserId()));
		}
		
		dp = customerAppointmentDao.findPageByCriteria(dp,
				HibernateUtils.prepareOrder(dp, "meetingTime", "desc"),
				criterionList);
		dp.setDatas(HibernateUtils.voListMapping((List<CustomerAppointment>)dp.getDatas(), CustomerAppointmentVo.class));
		return dp;
	}
	
	@Override
	public DataPackage getTodayFollowup(DataPackage dp) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		// 今天日期的
		criterionList.add(Expression.like("nextFollowupTime", DateTools.getCurrentDate(), MatchMode.START));
		criterionList.add(Expression.eq("deliverTarget", userService.getCurrentLoginUser().getUserId()));
		dp = customerDao.findPageByCriteria(dp, new ArrayList<Order>(), criterionList);
		dp.setDatas(HibernateUtils.voListMapping((List<Customer>)dp.getDatas(), CustomerVo.class));
		return dp;
	}

	@Override
	public void appointmentConfirm(String appointmentId,String visitType) throws ApplicationException {
		CustomerAppointment ca = customerAppointmentDao.findById(appointmentId);
		Customer customer = customerDao.findById(ca.getCustomer().getId());
		ca.setMeetingConfirmTime(DateTools.getCurrentDateTime());
		ca.setMeetingConfirmUser(userService.getCurrentLoginUser());
		ca.setVisitType(VisitType.valueOf(visitType));//add by Yao 2015-04-20
		customer.setVisitType(VisitType.valueOf(visitType));
		customerDao.save(customer);
		customerEventService.saveCustomerDynamicStatus(ca.getCustomer().getId(), CustomerEventType.APPOINTMENT, "预约客户到访："+ca.getMeetingConfirmTime(), "");
	}
	
	public List<CustomerDeliverTarget> getCustomerDeliveTargets(CustomerDeliverType deliverType, String campusId, String cusRecordDate, String cusOrg) {
//		List<CustomerDeliverTarget> targets = new ArrayList<CustomerDeliverTarget>();
//		
//		if (deliverType.equals(CustomerDeliverType.CONSULTOR)) {//获取咨询师列表，并返回指定客户资源搜索条件下所跟进的客户
//			//如果没有传校区id，获取本分公司id
//			String orgId = campusId;
//			if (StringUtils.isBlank(orgId)) {
//				Organization org = userService.getBelongBrench();
//				if (org != null) {
//					orgId  = org.getId();
//				}
//			}
//			List<User> userList = userService.getStaffByRoldCodeAndOrgId(RoleCode.CONSULTOR.toString(), orgId, true, new DataPackage(0, 999));
//			CustomerDeliverTarget customerDeliverTarget = null;
//			for (User u : userList) {
//				customerDeliverTarget = new CustomerDeliverTarget();
//				customerDeliverTarget.setDeliveType(deliverType);
//				customerDeliverTarget.setTargetId(u.getUserId());
//				customerDeliverTarget.setTargetName(u.getName());
//				
//				CustomerVo cus = new CustomerVo();
//				cus.setCusOrg(cusOrg);
//				cus.setRecordDate(cusRecordDate);
//				cus.setDeliverTarget(u.getUserId());
//				cus.setDeliverType(deliverType);
//				customerDeliverTarget.setFollowingCount(customerDao.getCustomerCount(cus));
//				//customerDeliverTarget.setFollowingCustomers((List<Customer>)this.getCustomers(cus, new DataPackage(0, 999)).getDatas());
//				targets.add(customerDeliverTarget);
//			}
//		} else if (deliverType.equals(CustomerDeliverType.CAMPUS) || deliverType.equals(CustomerDeliverType.CAMPUS_PUBLIC_POOL)){
//			//获取校区列表，并返回在指定客户资源搜索条件下所跟进的客户
//			Organization org = userService.getBelongBrench();
//			if (org == null) {
//				throw new ApplicationException(ErrorCode.USRR_ORG_NOT_FOUND);
//			}
//			List<Organization> orgs = commonService.getCapumsByOrgLevel(userService.getCurrentLoginUser().getName(), org.getOrgLevel());
//			CustomerDeliverTarget customerDeliverTarget = null;
//			for (Organization o : orgs) {
//				customerDeliverTarget = new CustomerDeliverTarget();
//				customerDeliverTarget.setDeliveType(deliverType);
//				customerDeliverTarget.setTargetId(o.getId());
//				customerDeliverTarget.setTargetName(o.getName());
//				
//				CustomerVo cus = new CustomerVo();
//				cus.setCusOrg(cusOrg);
//				cus.setRecordDate(cusRecordDate);
//				cus.setDeliverTarget(o.getId());
//				cus.setDeliverType(deliverType);
//				customerDeliverTarget.setFollowingCount(customerDao.getCustomerCount(cus));
//				//customerDeliverTarget.setFollowingCustomers((List<Customer>)this.getCustomers(cus, new DataPackage(0, 999)).getDatas());
//				targets.add(customerDeliverTarget);
//			}
//		} else {
//			//获取当前分公司，并返回指定客户资源搜索条件下所跟进的客户
//			Organization org = userService.getBelongBrench();
//			CustomerDeliverTarget customerDeliverTarget = new CustomerDeliverTarget();
//			customerDeliverTarget = new CustomerDeliverTarget();
//			customerDeliverTarget.setDeliveType(deliverType);
//			customerDeliverTarget.setTargetId(org.getId());
//			customerDeliverTarget.setTargetName(org.getName());
//			
//			CustomerVo cus = new CustomerVo();
//			cus.setCusOrg(cusOrg);
//			cus.setRecordDate(cusRecordDate);
//			cus.setDeliverTarget(org.getId());
//			cus.setDeliverType(deliverType);
//			customerDeliverTarget.setFollowingCount(customerDao.getCustomerCount(cus));
//			//customerDeliverTarget.setFollowingCustomers((List<Customer>)this.getCustomers(cus, new DataPackage(0, 999)).getDatas());
//			targets.add(customerDeliverTarget);
//		}
//		
//		return targets;
		return null;
	}
	
	/**
	 * 页面查询条件- 根据跟进对象类型查询具体的跟进对象名称
	 * @param deliverType
	 * @return
	 */
	public List<CustomerDeliverTarget> getCustomerTargetByDeliveType(CustomerDeliverType deliverType) {
//		List<CustomerDeliverTarget> targets = new ArrayList<CustomerDeliverTarget>();
//		if (deliverType.equals(CustomerDeliverType.CONSULTOR)) {//获取咨询师列表，并返回指定客户资源搜索条件下所跟进的客户
//			//如果没有传校区id，获取本分公司id
//			String orgId = null;
//			if (StringUtils.isBlank(orgId)) {
//				Organization org = userService.getBelongBrench();
//				if (org != null) {
//					orgId  = org.getId();
//				}
//			}
//			List<User> userList = userService.getStaffByRoldCodeAndOrgId(RoleCode.CONSULTOR.toString(), orgId, true, new DataPackage(0, 999));
//			CustomerDeliverTarget customerDeliverTarget = null;
//			for (User u : userList) {
//				customerDeliverTarget = new CustomerDeliverTarget();
//				customerDeliverTarget.setDeliveType(deliverType);
//				customerDeliverTarget.setTargetId(u.getUserId());
//				customerDeliverTarget.setTargetName(u.getName());
//				targets.add(customerDeliverTarget);
//			}
//		} else if (deliverType.equals(CustomerDeliverType.CAMPUS) || deliverType.equals(CustomerDeliverType.CAMPUS_PUBLIC_POOL)){
//			//获取校区列表，并返回在指定客户资源搜索条件下所跟进的客户
//			Organization org = userService.getBelongBrench();
//			
//			if (org == null) {
//				throw new ApplicationException(ErrorCode.USRR_ORG_NOT_FOUND);
//			}
//			List<Organization> orgs = commonService.getCapumsByOrgLevel(userService.getCurrentLoginUser().getName(), org.getOrgLevel());
//			CustomerDeliverTarget customerDeliverTarget = null;
//			for (Organization o : orgs) {
//				customerDeliverTarget = new CustomerDeliverTarget();
//				customerDeliverTarget.setDeliveType(deliverType);
//				customerDeliverTarget.setTargetId(o.getId());
//				customerDeliverTarget.setTargetName(o.getName());
//				targets.add(customerDeliverTarget);
//			}
//		} else {
//			Organization org= userService.getBelongBrench();
//			if (org == null) {
//				throw new ApplicationException(ErrorCode.USRR_ORG_NOT_FOUND);
//			}
//			List<Organization> orgs = organizationDao.getOrganizationsByOrgLevelAndType(org.getOrgLevel(), OrganizationType.BRENCH);
//			CustomerDeliverTarget customerDeliverTarget = null;
//			for (Organization o : orgs) {
//				customerDeliverTarget = new CustomerDeliverTarget();
//				customerDeliverTarget.setDeliveType(deliverType);
//				customerDeliverTarget.setTargetId(o.getId());
//				customerDeliverTarget.setTargetName(o.getName());
//				targets.add(customerDeliverTarget);
//			}
//		}
//		
//		return targets;
		return null;
	}
	
	@Override
	public CustomerVo getVoById(String customeId) {
		Customer  cus =  customerDao.findById(customeId);
		CustomerVo cusVo = (CustomerVo) HibernateUtils.voObjectMapping(cus, CustomerVo.class);
		return cusVo;
	}

	public List<CustomerVo> getCustomerNames(CustomerVo vo){
		return customerDao.getCustomerNames(vo);
	}
	
	@Override
	public CustomerVo findCustomerByPhone(String phone) throws ApplicationException {
		List<Customer> list = customerDao.findByCriteria(Expression.eq("contact", phone));
		if(list.size()>0){
			CustomerVo customervo= HibernateUtils.voObjectMapping(list.get(0), CustomerVo.class);
			if(StringUtils.isNotBlank(customervo.getDeliverTarget()) && customervo.getDeliverTarget()!=null){
				Organization org=new Organization();
				Organization testOrg=organizationDao.findById(customervo.getDeliverTarget());			
				if(testOrg!=null){
					//公共池
					Organization orgs=organizationDao.findById(customervo.getDeliverTarget());
					org=organizationDao.getBelongOrgazitionByOrgType(orgs.getOrgLevel(),orgs.getOrgType(), true);
					customervo.setDeliverTargetName(org.getCustomerPoolName());
				}else{
					//个人
					org=userService.getBelongCampusByUserId(customervo.getDeliverTarget());
					customervo.setDeliverTargetName(userService.getUserById(customervo.getDeliverTarget()).getName());
				}				
				customervo.setDeliverTargetCampus(org.getName());
				
			}	
			return customervo;
		}
			
//		if (customer != null) {
//			Criterion studentCriterion = Expression.eq("customerId", customer.getId());
//			List<PointialStudent> pstudentListsList = pointialStudentDao.findByCriteria(studentCriterion);
//			if (pstudentListsList.size() > 0) {
//				customer.setStudent(pstudentListsList.get(0));
//			}
//		}
		return null;
	}

	@Override
	public void setCustomerNextFollowupTime(CustomerAppointmentVo appointment) {
		//修改客户中的预约时间
		Customer customer = customerDao.findById(appointment.getCustomerId());
		if (customer == null) {
			throw new ApplicationException(ErrorCode.CUSTOMER_NOT_FOUND);
		} else {
			customer.setNextFollowupTime(appointment.getMeetingTime());
			customer.setSignNextFollowupTime(DateTools.getCurrentDateTime());
			customerDao.save(customer);
		}
		
		//如果已存在一条相同的客户与咨询师但未确认上门的记录，直接把该记录改成最新的预约时间
		List<CustomerAppointment> oldappointments = customerAppointmentDao.findByCriteria(Expression.eq("customer.id", appointment.getCustomerId()),Expression.eq("appointmentType", AppointmentType.FOLLOW_UP), Expression.isNull("meetingConfirmTime"));
		if (oldappointments.size() > 0) {
			oldappointments.get(0).setMeetingTime(appointment.getMeetingTime());
			oldappointments.get(0).setAppointmentTime(DateTools.getCurrentDateTime());//  以前没加这个时间   ？  add  by Yao 2015-04-21
			appointment.setAppointmentTime(DateTools.getCurrentDateTime());
		} else {
			appointment.setAppointmentTime(DateTools.getCurrentDateTime());
			CustomerAppointment customerAppointment = new CustomerAppointment();//HibernateUtils.voObjectMapping(appointment, CustomerAppointment.class);
			customerAppointment.setAppointmentUser(userService.getCurrentLoginUser());
			customerAppointment.setMeetingTime(appointment.getMeetingTime());
			customerAppointment.setCustomer(customer);
			customerAppointment.setAppointmentTime(DateTools.getCurrentDateTime());//  以前没加这个时间   ？  add  by Yao 2015-04-21
			customerAppointment.setAppointmentType(AppointmentType.FOLLOW_UP);
			customerAppointmentDao.save(customerAppointment);
		}
		
				
 				//TODO: 推送一个通知给前台
//		customerDao.setCustomerNextFollowupTime(appointment);
	}

	@Override
	public void saveCustomerCallsLog(String phone,String callsTime,PhoneType phoneType, PhoneEvent phoneEvent) {
		CustomerCallsLog log=new CustomerCallsLog();
		log.setPhone(phone);
		User user=userService.getCurrentLoginUser();
		log.setCallsTime(URLDecoder.decode(callsTime));
		log.setPhoneEvent(phoneEvent);
		log.setPhoneType(phoneType);
		if(user!=null)
			log.setUser(user);
		log.setCreateTime(DateTools.getCurrentDateTime());
		
		//本次事件是去电且是挂机事件 -处理上次摘机事件没有给电话赋值的电话赋值
		if(PhoneEvent.ON_HOOK.equals(phoneEvent)){
			List<Criterion> criterionList =new ArrayList<Criterion>();
			List<Order> orderList =new ArrayList<Order>();
			orderList.add(Order.desc("createTime"));
			criterionList.add(Expression.eq("user.userId", user.getUserId()));
			List<CustomerCallsLog> list=customerCallsLogDao.findAllByCriteria(criterionList, orderList);
			if(list!=null && list.size()>0){
				CustomerCallsLog customerCallsLog=list.get(0);
				if(PhoneEvent.OFF_HOOK.equals(customerCallsLog.getPhoneEvent()) 
						&& StringUtils.isEmpty(customerCallsLog.getPhone())){
					customerCallsLog.setPhone(phone);
					customerCallsLog.setPhoneType(phoneType);
					customerCallsLogDao.save(customerCallsLog);
					//保存一条跟进记录
					CustomerVo customerVo= findCustomerByPhone(phone);
					if(customerVo!=null){
						CustomerFolowupVo follow=new CustomerFolowupVo();
						if(PhoneType.INCOMING_TEL.equals(phoneType)){
							follow.setRemark("来电");
						}else{
							follow.setRemark("去电");
						}
						follow.setCustomerId(customerVo.getId());
						saveCustomerFollowRecord(follow);
					}
						
				}
			}
		}
		customerCallsLogDao.save(log);
	}

	@Override
	public DataPackage getPhoneRecords(DataPackage dataPackage,
			CustomerCallsLogVo customerCallsLog) {
		//如果有字段有值，用like进行条件查询
		StringBuffer hql=new StringBuffer();
		
		hql.append(" from CustomerCallsLog  where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotEmpty(customerCallsLog.getCustomerName())){
//			List<Customer> customerList =getCustomerByName(customerCallsLog.getCustomerName());
//			String[] phones=new String[customerList.size()];
//			for(int i=0;i<customerList.size();i++){
//				phones[i]=customerList.get(i).getContact();
//			}
//			if(phones!=null && phones.length>0) {
//				hql.append(" and phone in ( " +phones+ ") ");
//			} else {
//				hql.append(" and 1=2 ");
//			}
			hql.append(" and phone in( select contact from Customer where name like :customerName )");
		    params.put("customerName", customerCallsLog.getCustomerName()+"%");
		}
		if(StringUtils.isNotEmpty(customerCallsLog.getUserName())){
			hql.append(" and user.name like :userName ");
			params.put("userName", customerCallsLog.getUserName()+"%");
		}
		if(StringUtils.isNotEmpty(customerCallsLog.getBlCampusId())){
			hql.append(" and user.organizationId = :blCampusId ");
			params.put("blCampusId", customerCallsLog.getBlCampusId());
		}
		if(StringUtils.isNotEmpty(customerCallsLog.getPhone())){
			hql.append(" and phone like :phone ");
			params.put("phone", customerCallsLog.getPhone()+"%");
		}
		if(StringUtils.isNotEmpty(customerCallsLog.getStartDate())){
			hql.append(" and substring(callsTime,1,10) >= :startDate ");
			params.put("startDate", customerCallsLog.getStartDate());
		}
		if(StringUtils.isNotEmpty(customerCallsLog.getEndDate())){
			hql.append(" and substring(callsTime,1,10) <= :endDate ");
			params.put("endDate", DateTools.addDateToString( customerCallsLog.getEndDate(), 1));
		}
		
//		Organization organization = userService.getBelongCampus();
//		String hqlOrg=RoleCodeAuthoritySearchUtil.getOrganizationHql(organization, "user.organizationId");
//		if(StringUtils.isNotEmpty(hqlOrg)) {
//			hql.append(" and "+hqlOrg);
//		}
		hql.append(roleQLConfigService.getAppendSqlByAllOrg("通话记录","hql","user.organizationId"));
		
		hql.append(" order by createTime desc ");
		
		dataPackage = customerCallsLogDao.findPageByHQL(hql.toString(), dataPackage,true,params);
		List<CustomerCallsLog> list=(List<CustomerCallsLog>) dataPackage.getDatas();
		List<CustomerCallsLogVo> listVo=HibernateUtils.voListMapping(list, CustomerCallsLogVo.class);
		for(CustomerCallsLogVo vo:listVo){
			CustomerVo customerVo= findCustomerByPhone(vo.getPhone());
			if(customerVo!=null)
				vo.setCustomerName(customerVo.getName());
		}
		dataPackage.setDatas(listVo);
		return dataPackage;
	}
	
	public List<Customer> getCustomerByName(String name){
		return customerDao.findByCriteria(Expression.like("name", name,MatchMode.START));
	}
	
	/**
	 * 根据orgLevel 获取所有上级客户资源池 (为空  默认当前登陆用户的)
	 * @param orgLevel
	 * @return
	 */
	public List<CustomerPoolVo> getCustomerPool(String orgLevel){
		if(StringUtils.isBlank(orgLevel)){
			Organization organization = userService.getCurrentLoginUserOrganization();
			if(organization!=null && StringUtils.isNotBlank(organization.getOrgLevel()))
				orgLevel = organization.getOrgLevel();
		}
		if(StringUtils.isNotBlank(orgLevel)){
			List<Organization> organizationList = organizationDao.getOrganizationByCustomerPool(orgLevel);
			if(organizationList != null && organizationList.size()>0){
				return HibernateUtils.voListMapping(organizationList, CustomerPoolVo.class);
			}
		}
		return null;
	}
	
	/**
	 * 根据orgLevel 获取所有下级客户资源池 (为空  默认当前登陆用户的) 不包括自己
	 * @param orgLevel
	 * @return
	 */
	public List<CustomerPoolVo> getCustomerPoolLower(String orgLevel){
		if(StringUtils.isBlank(orgLevel)){
			Organization organization = userService.getCurrentLoginUserOrganization();
			if(organization.getOrgType()!=OrganizationType.BRENCH
					&&organization.getOrgType()!=OrganizationType.CAMPUS
					&&organization.getOrgType()!=OrganizationType.GROUNP){
				
				organization=userService.getOrganizationById(organization.getParentId());
			}
			if(organization!=null && StringUtils.isNotBlank(organization.getOrgLevel()))
				orgLevel = organization.getOrgLevel();
		}
		if(StringUtils.isNotBlank(orgLevel)){
			List<Organization> organizationList = organizationDao.getOrganizationByCustomerPoolLower(orgLevel);
			if(organizationList != null && organizationList.size()>0){
				return HibernateUtils.voListMapping(organizationList, CustomerPoolVo.class);
			}
		}
		return null;
	}
	
	private List<CustomerPoolVo> removeDuplicate(List<CustomerPoolVo> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getId().equals(list.get(i).getId())) {
					list.remove(j);
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据orgLevel 获取所有上级客户资源池 和可分配的人(为空  默认当前登陆用户的)
	 * @param orgLevel
	 * @return
	 */
	public List<CustomerPoolVo> getCustomerPoolAndUser(String orgLevel){
		List<CustomerPoolVo> customerPoolVoList = getCustomerPool(orgLevel);
		List<CustomerPoolVo> customerPoolLowerVoList = getCustomerPoolLower(orgLevel);
		if(customerPoolLowerVoList!=null && customerPoolLowerVoList.size()>0){
			customerPoolVoList.addAll(customerPoolLowerVoList);
		}
		if (customerPoolVoList != null) {
			customerPoolVoList=removeDuplicate(customerPoolVoList);
		}
		List<User> userList = null;
		String userName = null;
		List<Role> roles = null;
		List<CustomerPoolVo> customerPoolUserVoList =new  ArrayList<CustomerPoolVo>();
		String blorgLevel = userService.getBelongCampus().getOrgLevel();
		if (customerPoolVoList != null) {
			for(CustomerPoolVo customerPoolVo : customerPoolVoList){
	//			if(customerPoolVo.getOrgLevel().startsWith(blorgLevel)){
					userList = userService.getUserByBlcampusAndRole(customerPoolVo.getId(), customerPoolVo.getAccessRoles());
					for(User user : userList){
						CustomerPoolVo userVo=new CustomerPoolVo();
						userName = user.getName()+"(";
						roles = userService.getRoleByUserId(user.getUserId());
						for(int i=0; i<roles.size(); i++){
							if(i==0){
								userName += roles.get(i).getName();
							}else{
								userName += ","+roles.get(i).getName();
							}
						}
						userName+=")";
						userVo.setId(user.getUserId());
						userVo.setCustomerPoolName(userName);
						userVo.setParentId(user.getOrganizationId());
						customerPoolUserVoList.add(userVo);
					}
	//			}
			}
			customerPoolVoList.addAll(customerPoolUserVoList);
		}
		return customerPoolVoList; 
	}

	/**
	 * 根据orgLevel 获取所有上级客户资源池 和可分配的人(为空  默认当前登陆用户的)
	 * @param orgLevel
	 * @return
	 */
	public List<CustomerPoolVo> getCustomerPoolAndUserWithDI(String orgLevel){
		List<CustomerPoolVo> customerPoolVoList = getCustomerPool(orgLevel);
		List<CustomerPoolVo> customerPoolLowerVoList = getCustomerPoolLower(orgLevel);
		if(customerPoolLowerVoList!=null && customerPoolLowerVoList.size()>0){
			customerPoolVoList.addAll(customerPoolLowerVoList);
		}
		if (customerPoolVoList != null) {
			customerPoolVoList=removeDuplicate(customerPoolVoList);
		}
		List<User> userList = null;
		String userName = null;
		List<Role> roles = null;
		List<CustomerPoolVo> customerPoolUserVoList =new  ArrayList<CustomerPoolVo>();
		String blorgLevel = userService.getBelongCampus().getOrgLevel();
		if (customerPoolVoList != null) {
			for(CustomerPoolVo customerPoolVo : customerPoolVoList){
	//			if(customerPoolVo.getOrgLevel().startsWith(blorgLevel)){
					userList = userService.getUserByBlcampusAndRole(customerPoolVo.getId(), customerPoolVo.getAccessRoles());
					for(User user : userList){
						CustomerPoolVo userVo=new CustomerPoolVo();
						userName = user.getName()+"(";
						roles = userService.getRoleByUserId(user.getUserId());
						for(int i=0; i<roles.size(); i++){
							if(i==0){
								userName += roles.get(i).getName();
							}else{
								userName += ","+roles.get(i).getName();
							}
						}
						userName+=")";
						userVo.setId(user.getUserId());
						userVo.setCustomerPoolName(userName);
						userVo.setParentId(user.getOrganizationId());
						customerPoolUserVoList.add(userVo);
					}
	//			}
			}
			customerPoolVoList.addAll(customerPoolUserVoList);
		}
		//对没有资源池的删掉
		List<CustomerPoolVo> customerPoolVoList1=new ArrayList<CustomerPoolVo>();
		for(CustomerPoolVo cpv:customerPoolVoList){
			int code=resourcePoolService.getResourcePoolVolume(1, cpv.getId()).getResultCode();
			if(0==code) customerPoolVoList1.add(cpv);
		}
		return customerPoolVoList1; 
	}

	
	@Override
	public boolean checkCustomerTurnCampus(String customerId) {
		Customer customer =customerDao.findById(customerId);
		String organizationId=userService.getUserById(customer.getDeliverTarget()).getOrganizationId();
		if(StringUtils.isNotBlank(organizationId)){
			organizationId = userService.getBelongCampusByOrgId(organizationId).getId();
		}
		if(!organizationId.equals(customer.getBlSchool())){
			return true;
		}
		return false;
	}
	

	@Override
	public void updateCustomerForSimple(CustomerVo cusVo) {
		if(cusVo.getId()!=null) {
			Customer cus = this.customerDao.findById(cusVo.getId());
			if(cusVo.getName()!=null) {			// 客户姓名
				cus.setName(cusVo.getName());
			}
			if(cusVo.getContact()!=null) {  // 客户电话
				cus.setContact(cusVo.getContact());
			}
			if(cusVo.getCusType()!=null) {  // 客户类型
				cus.setCusType(this.dataDictDao.findById(cusVo.getCusType()));
			}
			if(cusVo.getCusOrg()!=null) {  // 客户来源
				cus.setCusOrg(this.dataDictDao.findById(cusVo.getCusOrg()));
			}
			
			if(cusVo.getIntentionOfTheCustomerId()!=null){//客户分级
				cus.setIntentionOfTheCustomer(this.dataDictDao.findById(cusVo.getIntentionOfTheCustomerId()));
			}
//			if(cusVo.getStudentName()!=null) {  // 学生姓名
//				cus.setStudentName(cusVo.getStudentName());
//			}
//			if(cusVo.getStudentContact()!=null) {  // 学生电话
//				cus.setStudentContact(cusVo.getStudentContact());
//			}
			if(cusVo.getRemark()!=null) {  // 备注
				cus.setRemark(cusVo.getRemark());
			}
//			if(cusVo.getStudentSex()!=null) {  // 性别
//				cus.setStudentSex(cusVo.getStudentSex());
//			}
//			if(cusVo.getStudentSchoolNameTable()!=null) {  // 学生学校
//				cus.setStudentSchoolNameTable(cusVo.getStudentSchoolNameTable());
//			}
			
			 
		}
	}
	
	public CustomerFollowUpRecrodsVo getAppointment(String customerId,AppointmentType appointmentType){		
		List<CustomerAppointment> appointments = customerAppointmentDao.findByCriteria(Expression.eq("customer.id", customerId),Expression.eq("appointmentType", appointmentType), Expression.isNull("meetingConfirmTime"));		
		if (appointments.size() > 0) {
			CustomerAppointment customerAppointment=appointments.get(0); 
			CustomerAppointmentVo appointmentVo = HibernateUtils.voObjectMapping(customerAppointment, CustomerAppointmentVo.class);
			CustomerFollowUpRecrodsVo vo= new CustomerFollowUpRecrodsVo();
			vo.setCreateTime(appointmentVo.getAppointmentTime());//创建时间
			vo.setPlanTime(appointmentVo.getMeetingTime());//下次跟进时间 或预约时间
			vo.setCreateUserName(appointmentVo.getAppointmentUserName());//跟进人名字或预约人名字
			vo.setRemark("");			
			if(appointmentType.getValue().equals("APPOINTMENT")){//预约			 
				vo.setType(AppointmentType.APPOINTMENT.getValue());				
				//查询客户预约校区
				customerDao.clear();
				Customer cus = this.customerDao.findById(customerId);
				if(cus!=null){					
					String schoolId=cus.getBlSchool();
					if(StringUtil.isNotBlank(schoolId)){
						Organization organization=organizationDao.findById(schoolId);
						if(organization!=null){
							 vo.setSchoolName(organization.getName());//预约校区
						}else{
							 vo.setSchoolName("");//预约校区
						}						
					}else{
						vo.setSchoolName("");
					}				   
				}else{
					vo.setSchoolName("");
				}
				
			}else{//FOLLOW_UP 
				vo.setType(AppointmentType.FOLLOW_UP.getValue());
				vo.setSchoolName("");
			}
			return vo;			
		}else{
			return null;
		}
	}

	/**
	 * 预约，跟进信息列表
	 */
	@Override
	public DataPackage getCustomerAppointment( CustomerAppointmentVo customerAppointmentVo , DataPackage dp) {
		StringBuffer hql=new StringBuffer();
		hql.append(" from CustomerAppointment ca where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(customerAppointmentVo.getMeetingTimeStart())){
			hql.append(" and ca.meetingTime >= :meetingTimeStart ");
			params.put("meetingTimeStart", customerAppointmentVo.getMeetingTimeStart()+" 00:00:00.000 ");
		}
		
		if(StringUtils.isNotBlank(customerAppointmentVo.getMeetingTimeEnd())){
			hql.append(" and ca.meetingTime <= :meetingTimeEnd ");
			params.put("meetingTimeEnd", customerAppointmentVo.getMeetingTimeStart()+" 23:59:59.999 ");
		}
		// 如果是前台或校区主任，只显示本校区的
		if (userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST)
				|| userService.isCurrentUserRoleCode(RoleCode.CAMPUS_DIRECTOR)) {
			hql.append(" and ca.customer.blSchool = :blSchool ");
			params.put("blSchool", userService.getBelongCampus().getId());
		}else{
			hql.append(" and ca.customer.deliverTarget = :deliverTarget ");
			params.put("deliverTarget", userService.getCurrentLoginUser().getUserId());
		}
		
		if (StringUtils.isNotBlank(customerAppointmentVo.getAppointmentType())) {
			hql.append(" and ca.appointmentType= :appointmentType ");
			params.put("appointmentType", customerAppointmentVo.getAppointmentType());
		} 	
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql.append(" order by "+dp.getSidx()+" "+dp.getSord());
		} 
		
		
		dp=customerAppointmentDao.findPageByHQL(hql.toString(), dp,true,params);
		List<CustomerAppointment> list=(List<CustomerAppointment>)dp.getDatas();		
		List<CustomerAppointmentVo> volist=HibernateUtils.voListMapping(list,CustomerAppointmentVo.class);
				
		for(CustomerAppointmentVo ca:volist){
			List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(ca.getAppointmentUserId());
			for (Iterator iterator = userDeptJob.iterator(); iterator.hasNext();) {
				UserDeptJob userDeptJob2 = (UserDeptJob) iterator.next();
				
				UserJob userJob=userJobDao.findById(userDeptJob2.getJobId());
//				Organization dept=organizationDao.findById(userDeptJob2.getId().getDeptId());
				
				if(userDeptJob2.getIsMajorRole()==0){					
					String jobName=userJob.getJobName();
					String jobId=userJob.getId();
					ca.setAppointmentUserJobId(jobId);
					ca.setAppointmentUserJobName(jobName);
				}
			}
			
		}
		
		dp.setDatas(volist);
		return dp;
	}
	
	@Override
	public DataPackage getAllContractCustomer(StudentVo studentVo,
			DataPackage dataPackage,String type) {
		User currentUser = userService.getCurrentLoginUser();
		StringBuilder sb= new StringBuilder(1024);
		sb.append(" select distinct c.id customerId, c.name customerName,c.contact,s.id studentId,s.name studentName,dd.NAME gradeName,u.name studyManagerName,s.`status`,ss.`name` schoolName from customer c ");
		sb.append(" left join customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
		sb.append(" left join student s on csr.student_id=s.ID ");
		sb.append(" left join user u on u.user_id=s.STUDY_MANEGER_ID ");
		sb.append(" left join data_dict dd on dd.id=s.GRADE_id ");
//		sb.append(" left join customer_dynamic_status cds on cds.CUSTOMER_ID=c.id ");
		sb.append(" left join contract con on con.student_id=s.ID ");
		sb.append(" left join student_school ss on ss.ID = s.SCHOOL ");
		sb.append(" where 1=1 and s.student_type='ENROLLED' ");
		
		Map<String, Object> params = Maps.newHashMap();
		
		if(StringUtils.isNotBlank(type) && "1".equals(type)){
			sb.append("  and con.create_user_id= :createUserId ");
			params.put("createUserId", currentUser.getUserId());
		}else{
			sb.append(" and con.id is not null and c.befor_Deliver_Target= :befor_Deliver_Target ");
			params.put("befor_Deliver_Target", currentUser.getUserId());
		}
		
		if(StringUtils.isNotBlank(studentVo.getName())){
			sb.append(" and s.name like :studentName ");
			params.put("studentName", "%"+studentVo.getName()+"%");
		}
		
		if(StringUtils.isNotBlank(studentVo.getLatestCustomerName())){
			sb.append(" and c.name like :latestCustomerName ");
			params.put("latestCustomerName", "%"+studentVo.getLatestCustomerName()+"%");
		}
		if(StringUtils.isNotBlank(studentVo.getContact())){
			sb.append(" and c.contact like :studentContact ");
			params.put("studentContact", "%"+studentVo.getContact()+"%");
		}
		
		if(StringUtils.isNotBlank(studentVo.getGradeId())){
			sb.append(" and s.Grade_id = :gradeId ");
			params.put("gradeId", studentVo.getGradeId());
		}
		
		if(StringUtils.isNotBlank(studentVo.getStudentStatus())){
			sb.append(" and s.STATUS = :studentStatus ");
			params.put("studentStatus", studentVo.getStudentStatus());
		}
		
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sb.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
		}
		List<Map<Object,Object>> maplist=customerDao.findMapOfPageBySql(sb.toString(), dataPackage, params);	
		
		
		for (Map map : maplist) {
			if(map.get("status")!=null)
			map.put("statusName", StudentStatus.valueOf(map.get("status").toString()).getName());
//			String stuId=map.get("studentId")==null ? null : map.get("studentId").toString();
//			if(stuId != null){
//				Student stu=studentDao.findById(stuId);
//				map.put("schoolName", stu.getSchool() == null ? "" : stu.getSchool().getName());
//			}
			if(map.get("schoolName")==null)
				map.put("schoolName", "");
			
		}
		dataPackage.setDatas(maplist);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + sb.toString() + " ) countall ",params));
		return dataPackage;
	}
	
	/**
	 * 预约信息登记
	 */
	@Override
	public CustomerAppointmentVo findCustomeraAppointmentById( String id){
		if(StringUtils.isNotBlank(id)){
			CustomerAppointment ca= customerAppointmentDao.findById(id);
			return HibernateUtils.voObjectMapping(ca, CustomerAppointmentVo.class);
		}else{
			return null;
		}
	}
	
	@Override
	public CustomerFolowupVo findCustomeraFollowUpById(String id) {
		if(StringUtils.isNotBlank(id)){
			CustomerFolowup ca= customerFolowupDao.findById(id);
			return HibernateUtils.voObjectMapping(ca, CustomerFolowupVo.class);
		}else{
			return null;
		}
	}
	
	
	/**
	 * 预约信息登记保存，分配咨询师
	 */
	public void customerAppointmentVisit(String id, String customerId,String visitId,String deliverTargetId){
		if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(customerId)){
			CustomerAppointment ca=customerAppointmentDao.findById(id);
			Customer c=customerDao.findById(customerId);
			if(StringUtils.isNotBlank(visitId)){
				c.setVisitType(VisitType.valueOf(visitId));
			}
			//已到访
			if(ca.getMeetingConfirmTime()!=null){				
				if(StringUtils.isNotBlank(deliverTargetId)){
					c.setDeliverTarget(deliverTargetId);	
					c.setDealStatus(CustomerDealStatus.valueOf("STAY_FOLLOW"));
					customerDao.save(c);
				}				
			}else{
				if(StringUtils.isNotBlank(deliverTargetId)){
					c.setDeliverTarget(deliverTargetId);
					c.setDealStatus(CustomerDealStatus.valueOf("STAY_FOLLOW"));
					customerDao.save(c);
				}
				if(StringUtils.isNotBlank(visitId)){
					ca.setVisitType(VisitType.valueOf(visitId));
					ca.setMeetingConfirmUser(userService.getCurrentLoginUser());
					ca.setMeetingConfirmTime(DateTools.getCurrentDateTime());
					customerAppointmentDao.save(ca);
				}
				
			}
		}
		
	}
	
	@Override
	public void signCustomerComeon(String id, String customerId,
			String visitId, String deliverTargetId) {
		
		if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(customerId)){
			CustomerFolowup ca=customerFolowupDao.findById(id);
			Customer c=customerDao.findById(customerId);
			c.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
			if(StringUtils.isNotBlank(visitId)){
				c.setVisitType(VisitType.valueOf(visitId));
			}
			//已到访
			if(ca.getMeetingConfirmTime()!=null){				
				if(StringUtils.isNotBlank(deliverTargetId)){					
					if(!c.getDeliverTarget().equals(deliverTargetId)){
						c.setDealStatus(CustomerDealStatus.valueOf("STAY_FOLLOW"));
					}					
					c.setDeliverTarget(deliverTargetId);
					customerDao.save(c);
				}				
			}else{
				if(StringUtils.isNotBlank(deliverTargetId)){					
					if(!c.getDeliverTarget().equals(deliverTargetId)){
						c.setDealStatus(CustomerDealStatus.valueOf("STAY_FOLLOW"));
					}
					c.setDeliverTarget(deliverTargetId);
					customerDao.save(c);
				}
				if(StringUtils.isNotBlank(visitId)){
					ca.setVisitType(VisitType.valueOf(visitId));
					ca.setMeetingConfirmUser(userService.getCurrentLoginUser());
					ca.setMeetingConfirmTime(DateTools.getCurrentDateTime());					
					customerFolowupDao.save(ca);
					//添加客户动态信息
					customerEventService.saveCustomerDynamicStatus(customerId, CustomerEventType.APPOINTMENT_CONFORM,"前台登记", "");

				}
				
			}
		}
	}

	@Override
	public DataImportResult customerDataImportByList(List<List<Object>> list,CustomerDeliverType cdt,String[] strs) {
		String result="";
		DataImportResult dir=new DataImportResult();
		List<Integer> li=new ArrayList<Integer>();
		User user=userService.getCurrentLoginUser();
		int index=1;
		int contactFail=0;
			for(index=0;index<list.size();index++){
				try{
					DataImportResult dir1=dealRowData(list.get(index),index+1,strs,cdt,user);
					if(dir1.getResult()!=null){
						result+=dir1.getResult();
						contactFail++;
					}
				
				}catch(Exception e){
					log.error(e.getMessage());
					e.printStackTrace();
					dir.setLi(li);
					dir.setResultCode(String.valueOf(list.size()));
					dir.setFailNum((list.size()-index));
					return dir;
				}
				
			}
					
		dir.setResult(""); //#760 删除客户资源导入后，错误原因提示语
		dir.setLi(li);
		dir.setResultCode(String.valueOf(list.size()));
		dir.setFailNum(contactFail);//(list.size()-index)+
		return dir;
					
	}
	
	@Override
	public DataImportResult customerDataImportByList(List<List<Object>> list,CustomerDeliverType cdt,String[] strs,DataImportResult dir) {
		int yetImport = dir.getYetImport();
		int maxNumber = dir.getMaxNumber();
		int contactFail = 0;
		boolean flag = yetImport >= maxNumber;
		User user=userService.getCurrentLoginUser();
		for(int index=0;index<list.size();index++){
			LinkedList<Object> obj = (LinkedList<Object>) list.get(index);
			try{
				if(flag) {
					obj.add("资源池已满");
					contactFail++;
					continue;
				}
				DataImportResult dir1=dealRowData(obj,index+1,strs,cdt,user);
				if(dir1.getResult()!=null){
					obj.add(dir1.getResult());
					contactFail++;
				}else {
					obj.add("SUCCESS");
					yetImport++;
					if(yetImport >= maxNumber) {
						flag = true;
					}
				}
			
			}catch(Exception e){
				log.error(e.getMessage());
				e.printStackTrace();
				obj.add("插入数据失败");
				contactFail ++;
			}
			
		}
					
		dir.setYetImport(yetImport);
		dir.setMaxNumber(maxNumber);
		dir.setFailNum(contactFail);
		return dir;
					
	}
	
	private DataImportResult dealRowData(List<Object> lo,int index,String[] strs,CustomerDeliverType cdt,User user){
		CustomerImportTransform cif=new CustomerImportTransform();
		DataImportResult dir=new DataImportResult();
		String failReason="";
		String grade="";
		String result="";
		// 查询电话号码是否存在临时表
		Map<String, Object> params = Maps.newHashMap();
		if(lo.get(2).toString()!=null && StringUtils.isNotBlank(lo.get(2).toString())){
			params.put("contact", lo.get(2).toString());
			String sql=" select COUNT(1) from customer_import_transform where CONTACT= :contact and CUS_STATUS!='-1' ";
			int count=customerDao.findCountSql(sql,params);
			if(count>0){
				dir.setResult("系统已存在该客户联系方式");
				return dir;
			}
		}
		
		//数据检验
		if(lo.get(1).toString().equals("")){
			dir.setResult("客户姓名为空");
			return dir;
		}
		if( lo.get(2).toString().equals("")){
			dir.setResult("联系方式为空");
			return dir;
		}
		else{
			if(!CheckPhoneNumber.checkPhoneNum(lo.get(2).toString())){
				dir.setResult("联系方式格式有误");
				return dir;
			}
		}

		DataDict resType = null;
		String resourceName = lo.get(3).toString();

		if(resourceName.equals("")){//
			dir.setResult("资源来源为空");
			return dir;
		}else {
			Map<String, Object> params1 = new HashMap<>();
			params1.put("resourceName", resourceName);
			resType = dataDictDao.findOneByHQL("from DataDict where name = :resourceName and  category = 'RES_TYPE' ", params1);
			if (resType == null){
				dir.setResult("资源来源查找不到");
				return dir;
			}
			cif.setCusTypeName(resType.getName());
			cif.setCusType(resType.getId());
		}
		
		String cusOrgName = lo.get(4).toString();
		DataDict resOrg = null;
		if(cusOrgName.equals("")){//
			dir.setResult("来源细分为空");
			return dir;
		}else {
			if (resType!=null){
				Map<String, Object> params2 = new HashMap<>();
				params2.put("cusOrgName", cusOrgName);
				params2.put("resTypeId", resType.getId());
				resOrg = dataDictDao.findOneByHQL(" from DataDict where name = :cusOrgName and  category = 'CUS_ORG' and parentDataDict.id= :resTypeId ", params2);
			}
			if (resOrg==null){
				dir.setResult("来源细分查找不到");
				return dir;
			}
			cif.setCusOrgName(resOrg.getName());
			cif.setCusOrg(resOrg.getId());
		}
		
		String currentTime=DateTools.getCurrentDateTime();	
		
		cif.setCreateTime(currentTime);
		cif.setCreateUser(user.getUserId());
		cif.setLastDeliverName(user.getName());
		
		
		if(lo.get(1)!=null && !lo.get(1).toString().equals("")){
			cif.setCusName(lo.get(1).toString());
		}
		if(lo.get(2)!=null && !lo.get(2).toString().equals("")){
			cif.setCusContact(lo.get(2).toString());
		}		
		
		if(lo.get(3+3)!=null && !lo.get(3+3).toString().equals("")){//+3 old
			cif.setStudentName(lo.get(3+3).toString());
		}
		if(lo.get(4+3)!=null && !lo.get(4+3).toString().equals("")){//+3
			cif.setStudentContact(lo.get(4+3).toString());
		}
		//学校所在市
		String schoolRegionName = lo.get(5+3)!=null?lo.get(5+3).toString():"";
		if(StringUtils.isNotBlank(schoolRegionName)){//学生所在市 +3
			Map<String, Object> params3 = new HashMap<>();
			params3.put("regionName", schoolRegionName);
			String sql3 = " select * from region where name= :regionName ";
			List<Region> list3=regionDao.findBySql(sql3,params3);
			if(list3!=null && list3.size()>0){
				Region region=list3.get(0);
				cif.setSchoolRegionName(region.getName());
				cif.setSchoolRegionId(region.getId());
			}else {
				dir.setResult("学校所在市查找不到");
				return dir;
			}
		}
		
		if(lo.get(6+3).toString()!=null && !lo.get(6+3).toString().equals("")){//学校级别 +3
			cif.setSchoolLevel(lo.get(6+3).toString());
		}
		//学校名
		String schoolName = lo.get(7+3)!=null?lo.get(7+3).toString():"";
		if(StringUtils.isNotBlank(schoolName)){//学校 +3
			Map<String, Object> params3 = new HashMap<>();
			StringBuffer sql3= new StringBuffer(512);
			sql3.append(" select * from student_school where name = :studentSchoolName ");
			params3.put("studentSchoolName", schoolName);
			if(cif.getSchoolLevel() != null ){
				sql3.append(" and CATEGORY in (SELECT id from data_dict where name= :schoolLevel and CATEGORY='SCHOOL_LEVEL' ) ");
				params3.put("schoolLevel", cif.getSchoolLevel());
			}
			if(cif.getSchoolRegionId() != null){
				sql3.append(" and city_id = :regionId ");
				params3.put("regionId", cif.getSchoolRegionId());
			}
			List<StudentSchool> list3=studentSchoolDao.findBySql(sql3.toString(),params3);
			if(list3 != null && list3.size()>0){
				StudentSchool school=list3.get(0);
				cif.setBlSchoolName(schoolName);
				cif.setSchoolId(school.getId());
			}else {
				dir.setResult("学校查找不到");
				return dir;
			}
		}
		
		if(lo.get(8+3)!=null && !lo.get(8+3).toString().equals("")){// +3
			cif.setGradeName(lo.get(8+3).toString());
			Map<String, Object> query = new HashMap<>();
			query.put("name", lo.get(8+3).toString());
			List<DataDict> stuGrade=dataDictDao.findBySql(" select * from data_dict where name= :name and CATEGORY = 'STUDENT_GRADE' ",query);
			if(stuGrade!=null && stuGrade.size()>0){				
				grade=stuGrade.get(0).getId();
			}else{
				dir.setResult("年级不存在");
				return dir;
			}
			cif.setGradeId(grade);
		}
		if(lo.get(9+3)!=null && !lo.get(9+3).toString().equals("")){ //+3
			cif.setClasses(lo.get(9+3).toString());
		}
		if(lo.get(10+3)!=null && !lo.get(10+3).toString().equals("")){ //+3
			cif.setFatherName(lo.get(10+3).toString());
		}
		if(lo.get(11+3)!=null && !lo.get(11+3).toString().equals("")){  //+3
			cif.setFatherPhone(lo.get(11+3).toString());
		}
		if(lo.get(12+3)!=null && !lo.get(12+3).toString().equals("")){  //+3
			cif.setMotherName(lo.get(12+3).toString());
		}
		if(lo.get(13+3)!=null && !lo.get(13+3).toString().equals("")){  //+3
			cif.setNotherPhone(lo.get(13+3).toString());
		}
		
		//增加资源入口写入 20170810 xiaojinwang
		if(cif.getCusTypeName()!=null && cif.getCusOrgName()!=null && cif.getCusTypeName().equals("市场推广")
				&& cif.getCusOrgName().equals("地推数据")){
			cif.setResEntrance(ResEntranceType.GROUNDPROMOTION.getValue());
			cif.setResEntranceName(ResEntranceType.GROUNDPROMOTION.getName());
		}
		
		
		if(lo.get(16-11)!=null && !lo.get(16-11).toString().equals("")){ //-11
			cif.setRemark(lo.get(16-11).toString());
		}
			
		if(strs[1]!=null && StringUtils.isNotBlank(strs[1])){
			cif.setDeliverTarget(strs[1]);
			User deliverName=userService.findUserById(strs[1]);
			if(deliverName!=null){
				cif.setDeliverTargetName(deliverName.getName());
			}else{
				Organization org=organizationDao.findById(strs[1]);
				if(org!=null){
					cif.setDeliverTargetName(org.getName());
				}
			}
		}
		
		if(strs[0]!=null && !strs[0].equals("")){
			cif.setDeliverStatus(CustomerDealStatus.valueOf(CustomerDealStatus.class, strs[0]).toString());
		}  		
		if(cdt!=null){
			cif.setDeliverType(cdt.toString());
		}else{
			cif.setDeliverType(CustomerDeliverType.PERSONAL_POOL.toString());
		}

		//资源导入，
		String importBlSchool="";
		String importCustomerSchool="";
		String importPersonalSchool="";

		if(StringUtils.isBlank(importCustomerSchool) && CustomerDeliverType.CUSTOMER_RESOURCE_POOL.toString().equals(cif.getDeliverType())){
			importCustomerSchool=userService.getBelongCampusByOrgId(strs[1]).getId();
		}
		if(StringUtils.isBlank(importPersonalSchool) && CustomerDeliverType.PERSONAL_POOL.toString().equals(cif.getDeliverType())){
			String organizationId=userService.getUserById(strs[1]).getOrganizationId();		
			importPersonalSchool=userService.getBelongCampusByOrgId(organizationId).getId();
		}
		if(StringUtils.isBlank(importBlSchool)){
			importBlSchool=userService.getBelongCampus().getId();
		}		

		if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.toString().equals(cif.getDeliverType())
				&& StringUtils.isNotBlank(cif.getDeliverTarget())){
			cif.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.toString());
			cif.setBlSchool(importCustomerSchool);			
			cif.setDeliverStatus(CustomerDealStatus.STAY_FOLLOW.toString());

		}else if(CustomerDeliverType.PERSONAL_POOL.toString().equals(cif.getDeliverType())
				&& StringUtils.isNotBlank(cif.getDeliverTarget())){				
			cif.setDeliverType(CustomerDeliverType.PERSONAL_POOL.toString());			
			cif.setDeliverStatus(CustomerDealStatus.STAY_FOLLOW.toString());
			cif.setBlSchool(importPersonalSchool);
		}else if(StringUtils.isEmpty(cif.getBlSchool())){
			cif.setDeliverType(CustomerDeliverType.PERSONAL_POOL.toString());
			cif.setDeliverStatus(CustomerDealStatus.FOLLOWING.toString());
			cif.setBlSchool(importBlSchool);
		}
		
		cif.setFailReason(failReason);
		if(cif.getCusStatus()==null){
			cif.setCusStatus("0");
		}
		customerImportTransformDao.save(cif);
		try {
			customerImportTransformDao.commit();
			
		} catch (Exception e) {
			throw new ApplicationException("导入时，插入数据失败 "+StringUtil.toString(lo.get(2)));  //抛出失败数据电话
		}
		DataImportResult dataImportResult = new DataImportResult();
		if (StringUtils.isNotBlank(result)){
			dataImportResult.setResult(result);
		}

		return dataImportResult;
		
	}
	
	/**
	 * 前台客户资源
	 */
	@Override
	public DataPackage ReCeptionistGetCustomers(String startDate,String endDate,CustomerVo customerVo, DataPackage dp, boolean onlyShowUndeliver) {		
		StringBuffer hql=new StringBuffer();
		hql.append("from Customer c where 1=1 ");
		
		Map<String, Object> params = Maps.newHashMap();
		
		if (onlyShowUndeliver){
			hql.append(" and c.deliverType is null ");
			//hql.append(" and c.dealStatus = 'NEW' "); //or
		}
		if(StringUtils.isNotEmpty(customerVo.getName())){
			hql.append(" and c.name like :name ");
			params.put("name", "%"+customerVo.getName()+"%");
		}
		if(StringUtils.isNotEmpty(customerVo.getRecordDate())){
			hql.append(" and c.recordDate like :recordDate ");
			params.put("recordDate", customerVo.getRecordDate()+"%");
		}
		if(StringUtils.isNotEmpty(startDate)){
			hql.append(" and c.recordDate >= :startDate ");
			params.put("startDate", startDate+" 00:00:00");
		}
		if(StringUtils.isNotEmpty(endDate)){
			hql.append(" and c.recordDate <= :endDate ");
			params.put("endDate", endDate+" 23:59:59");
		}
		if(customerVo.getVisitTypeName()!=null && StringUtils.isNotBlank(customerVo.getVisitTypeName())){
			if(customerVo.getVisitTypeName().equals("1")){
				//上门
				hql.append(" and c.visitType is not null ");
			}else{
				hql.append(" and c.visitType is null ");
			}
			
		}
		if(StringUtils.isNotEmpty(customerVo.getResEntranceId())){
			hql.append(" and c.resEntrance = :resEntranceId ");
			params.put("resEntranceId", customerVo.getResEntranceId());
		}
		if(StringUtils.isNotEmpty(customerVo.getCreateUserName())){
			hql.append(" and c.createUser.name like :createUserName ");
			params.put("createUserName", "%"+customerVo.getCreateUserName()+"%");
		}
		if(StringUtils.isNotEmpty(customerVo.getCusOrg())){
			hql.append(" and c.cusOrg.id = :cusOrg ");
			params.put("cusOrg", customerVo.getCusOrg());
		}
		if(StringUtils.isNotEmpty(customerVo.getCusType())){
			hql.append(" and c.cusType.id = :cusType ");
			params.put("cusType", customerVo.getCusType());
		}
		if(StringUtils.isNotEmpty(customerVo.getContact())){
			hql.append(" and c.contact like :contact ");
			params.put("contact", "%"+customerVo.getContact()+"%");
		}
		if(StringUtils.isNotEmpty(customerVo.getRemark())){
			hql.append(" and c.remark like :remark ");
			params.put("remark", "%"+customerVo.getRemark()+"%");
		}
		if(StringUtils.isNotEmpty(customerVo.getBlCampusName())){
			hql.append(" and c.blCampusId.name like :blCampusName ");
			params.put("blCampusName", "%"+customerVo.getBlCampusName()+"%");
		}
		if(StringUtils.isNotEmpty(customerVo.getBlSchoolName())){
			hql.append(" and c.blSchool in (select id from Organization where name like :blCampusName ) ");
			params.put("blCampusName", "%"+customerVo.getBlSchoolName()+"%");
		}
		if(customerVo.getDealStatus()!=null){
			hql.append(" and c.dealStatus = :dealStatus ");
			params.put("dealStatus", customerVo.getDealStatus().getValue());
		}
		if(customerVo.getDeliverType() != null){
			hql.append(" and c.deliverType = :deliverType ");
			params.put("deliverType", customerVo.getDeliverType().getValue());
		}
		if(StringUtils.isNotEmpty(customerVo.getDeliverTarget())){
			hql.append(" and c.deliverTarget = :deliverTarget ");
			params.put("deliverTarget", customerVo.getDeliverTarget());
		}
		if(StringUtils.isNotEmpty(customerVo.getStartDate())){
			hql.append(" and c.createTime >= :startDate ");
			params.put("startDate", customerVo.getStartDate());
		}
		if(StringUtils.isNotEmpty(customerVo.getEndDate())){
			hql.append(" and c.createTime < :endDate ");
			params.put("endDate", DateTools.addDateToString(customerVo.getEndDate(), 1));
		}
		if(StringUtils.isNotEmpty(customerVo.getSchoolAddress())){
			hql.append(" and c.blSchool in (select id from Organization where regionId in(select id from DataDict where name like :schoolAddress )) ");
		    params.put("schoolAddress", "%"+customerVo.getSchoolAddress()+"%");
		}
		if(StringUtils.isNotEmpty(customerVo.getKeywork())){     
			params.put("keyWork", "%"+customerVo.getKeywork()+"%");
        	hql.append(" and (");        	
        	hql.append(" c.name like :keyWork ");        	
        	hql.append(" or c.contact like :keyWork ");     			
        			
        	//资源类型
        	String cusTypeId=dataDictDao.getDataDictIdByLikeName(customerVo.getKeywork(),DataDictCategory.RES_TYPE);
        	if(StringUtils.isNotEmpty(cusTypeId)){ 
        		hql.append(" or c.cusType.id in ( :cusTypeId ) ");
        		params.put("cusTypeId", cusTypeId);
        	}
        	//客户意向度分级
        	String intentionId=dataDictDao.getDataDictIdByLikeName(customerVo.getKeywork(),DataDictCategory.INTENTION_OF_THE_CUSTOMER);
        	if(StringUtils.isNotEmpty(intentionId)){ 
        		hql.append(" or c.intentionOfTheCustomer.id in ( :intentionId ) ");
        		params.put("intentionId", intentionId);
        	}
        	
            hql.append(" )");
        }
		//得到当前日期减30
		if((startDate==null || StringUtils.isBlank(startDate) ) && (endDate==null || StringUtils.isBlank(endDate))){
			String end=DateTools.getCurrentDateTime();
			String start=DateTools.addDateToString(end,-30);
			params.put("end", end);
			params.put("start", start+" 0000");
			hql.append(" and c.recordDate >= :start and c.recordDate<= :end ");
		}			
		if(CustomerDealStatus.STAY_FOLLOW==customerVo.getDealStatus()
				&& customerVo.getDeliverType() != null
				&& StringUtils.isNotEmpty(customerVo.getDeliverTarget())){//咨询师工作台 数据查新
			
		}else{ // 客户列表数据查询
//			hql.append(roleQLConfigService.getValueResult("客户列表","hql"));
//			 如果是前台或校区主任，只显示本校区的
			if (userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST)
					|| userService.isCurrentUserRoleCode(RoleCode.CAMPUS_DIRECTOR)) {
				hql.append(" and c.blSchool = :blSchool ");
				params.put("blSchool", userService.getBelongCampus().getId());
			}else{
				hql.append(" and c.deliverTarget = :deliverTarget ");
				params.put("deliverTarget", userService.getCurrentLoginUser().getUserId());
			}
		}
		
		//从本校区转出，但转进的那个校区还没有接受
		hql.append(" or (  transferFrom ='"+userService.getBelongCampus().getId()+"' and transferStatus='0' )");
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			if(dp.getSidx().equals("visitTypeName")){
				hql.append(" order by visit_type "+dp.getSord());				
			}else{
				hql.append(" order by "+dp.getSidx()+" "+dp.getSord());
			}
			
		} 
		
		dp= customerDao.findPageByHQL(hql.toString(), dp,true,params);
		List<Customer> customerList=(List<Customer>) dp.getDatas();
		List<CustomerVo> voList=new ArrayList<CustomerVo>();	
		boolean recep=userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST);
		for(Customer c : customerList){					
			CustomerVo vo =HibernateUtils.voObjectMapping(c, CustomerVo.class);
		if(recep){
			if(c.getRecordUserId()!=null){
				//登记人校区
				Organization blcampus=userService.getBelongCampusByUserId(c.getRecordUserId().getUserId());
				vo.setRecordCampusId(blcampus.getId());
			}	
		}	
			
			if(vo.getDeliverTarget()!=null && StringUtils.isNotEmpty(vo.getDeliverTarget())){
				if(CustomerDeliverType.PERSONAL_POOL.equals(vo.getDeliverType())){
					vo.setDeliverTargetName(userService.getUserById(vo.getDeliverTarget()).getName());
				}else if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.equals(vo.getDeliverType()) ){
					vo.setDeliverTargetName(organizationDao.findById(vo.getDeliverTarget()).getCustomerPoolName());
				}
			}
			//如果是已签合同，则查询第一份金额
			if(CustomerDealStatus.SIGNEUP.equals(c.getDealStatus())){
				List<Contract> cList = contractDao.findContractByCustomer(c.getId(), null, null);
				int size = cList.size();
				if(size > 0){
					Contract contract = cList.get(0);
					contractService.calculateContractDomain(contract);
					vo.setFirstContractMoney(contract.getTotalAmount());
					vo.setFirstContractTime(contract.getCreateTime());//add by Yao  2015-04-13  第一份合同时间
				}
			}
			if(c!=null && c.getBlCampusId()!=null){
				 c.getBlCampusId().getRegionId();
			}			
			if(StringUtils.isNotEmpty(c.getNextFollowupTime())){
				List<CustomerFolowup> fvos = customerFolowupDao.findByCriteria(HibernateUtils.prepareOrder(null, "createTime", "desc"), Expression.eq("customer.id", c.getId()));
				int size = fvos.size();
				if(size > 0){
					CustomerFolowup followup = fvos.get(0);
					vo.setLastFollowUpTime(followup.getCreateTime());
				}
			}
//			if(CustomerDealStatus.STAY_FOLLOW.equals(vo.getDealStatus()) || CustomerDealStatus.FOLLOWING.equals(vo.getDealStatus())){// 待跟进与跟进中的客户
//				DataPackage cdp = customerEventService.findCustomerDynamicStatusByCustomerId(c.getId(), new DataPackage());
//				List<CustomerDynamicStatus> cds=(List<CustomerDynamicStatus>) cdp.getDatas();
//				int size=cds.size();
//				if(size > 0){
//					CustomerDynamicStatus cdst=cds.get(0);
//					
//				}
//			}
			if(StringUtils.isNotEmpty(c.getBlSchool())){
				
				Organization organization=organizationDao.findById(c.getBlSchool());
				if(organization!=null){
					vo.setBlSchoolName(organization.getName());
				}else{
					vo.setBlSchoolName("");
				}			
				
				DataDict dddt = dataDictDao.findById(organization.getRegionId());
				if(dddt!=null){//跟进校区归属地区
					vo.setSchoolAddress(dddt.getName());
				}else{
					vo.setSchoolAddress("");
				}
			}
			voList.add(vo);
		}
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public DataPackage gtCustomerFollowingRecords(CustomerFolowupVo res, DataPackage dp) throws ApplicationException {
		 StringBuffer hql=new StringBuffer();
		 Map<String, Object> params = Maps.newHashMap();
		 hql.append(" from CustomerFolowup  where 1=1 ");
		 
		 if(StringUtils.isNotBlank(res.getCustomerId())){
			 hql.append(" and customer.id = :customerId ");
			 params.put("customerId", res.getCustomerId());
		 }
		 if(res.getAppointmentType()!=null){
			 hql.append(" and appointmentType = :appointmentType ");
			 params.put("appointmentType", res.getAppointmentType());
		 }
		 if(StringUtils.isNotBlank(res.getMeetingTimeStart())){
			hql.append(" and meetingTime >= :meetingTimeStart ");
			params.put("meetingTimeStart", res.getMeetingTimeStart()+" 00:00:00");
		 }
			
		if(StringUtils.isNotBlank(res.getMeetingTimeEnd())){
			hql.append(" and meetingTime <= :meetingTimeEnd ");
			params.put("meetingTimeEnd",res.getMeetingTimeEnd()+" 23:59:59");
		}
		
		if(StringUtils.isNotBlank(res.getCreateUserId())){
			hql.append(" and createUser.userId= :createUserId ");
			params.put("createUserId",res.getCreateUserId());
		}
		
		//app客户名字查询
		if(StringUtils.isNotBlank(res.getCustomerName())){
			 hql.append(" and customer.name like :customerName ");
			 params.put("customerName","%"+res.getCustomerName()+"%");
		}
		
		// 如果是前台或校区主任，只显示本校区的
		if(res.getAppointmentType()!=null && res.getAppointmentType().equals(AppointmentType.APPOINTMENT)){
			if (userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST)
					|| userService.isCurrentUserRoleCode(RoleCode.CAMPUS_DIRECTOR)) {
				hql.append(" and appointCampus.id = :appointCampusId ");
				params.put("appointCampusId",userService.getBelongCampus().getId());
			}else{
				hql.append(" and customer.deliverTarget = :customerDeliverTarget ");
				params.put("customerDeliverTarget",userService.getCurrentLoginUser().getUserId());
			}
		}
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql.append(" order by "+dp.getSidx()+" "+dp.getSord());		
		}
//		 hql.append(" order by createTime desc");
		 dp=customerFolowupDao.findPageByHQL(hql.toString(), dp,true,params);
		 
		List<CustomerFolowupVo> list = HibernateUtils.voListMapping((List<CustomerFolowup>)dp.getDatas(), CustomerFolowupVo.class);
		for (CustomerFolowupVo cf : list) {
			Map map=userService.getMainDeptAndJob(cf.getCreateUserId());
			if(map!=null){
				cf.setAppointmentUserJobName(map.get("jobName")==null?"":map.get("jobName").toString());
			}
		}
		 
		 dp.setDatas(list);
		return dp;
	}
	
	
	@Override
	public DataPackage getCustomerListForPlat(CustomerVo cus,
			DataPackage dp) {
		
		StringBuffer cusSql=new StringBuffer();	
		cusSql.append(" select c.id,c.name,c.contact,c.CUS_TYPE as cusType,c.CUS_ORG as cusOrg,c.RECORD_DATE as recordDate,");
		cusSql.append(" c.RECORD_USER_ID as recordUserId,c.BL_SCHOOL as blSchool,c.BL_CONSULTOR as blConsultor,c.IS_PUBLIC as isPublic, ");
		cusSql.append(" c.DEAL_STATUS as dealStatus,c.REMARK as remark,c.CREATE_TIME as createTime,c.CREATE_USER_ID as createUser,");
		cusSql.append(" c.MODIFY_TIME as modifyTime,c.MODIFY_USER_ID as modifyUserId,c.DELIVER_TYPE as deliverType,c.DELEVER_TARGET as deliverTarget,");
		cusSql.append(" c.INTRODUCER as introducer,c.APPOINTMENT_DATE as appointmentDate,c.CONFIRM_RECEPTIONIST_ID as confirmReceptionist,");
		cusSql.append(" c.DELIVER_TIME as deliverTime,c.LAST_DELIVER_NAME as lastDeliverName, c.LAST_FOLLOW_UP_TIME as lastFollowUpTime,");
		cusSql.append(" c.LAST_FOLLOW_UP_USER_ID as lastFollowUpUser,c.BL_CAMPUS_ID as blCampusId, c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer, ");
		cusSql.append(" c.PURCHASING_POWER as purchasingPower, c.VISIT_TYPE as visitType,c.RES_ENTRANCE as resEntrance,c.CONFIRM_RECEPTIONIST_ID as confirmReceptionistId,c.NEXT_FOLLOWUP_TIME as nextFollowupTime, ");
		cusSql.append(" c.SIGN_NEXT_FOLLOWUP_TIME as signNextFollowupTime,c.MEETING_CONFIRM_TIME as meetingConfirmTime,c.BEFOR_DELIVER_TARGET as beforeDeliverTarget, c.BEFOR_DELIVER_TIME as beforeDeliverTime,");
		cusSql.append(" c.LAST_APPOINT_ID as lastAppointId, c.LAST_FOLLOW_ID as lastFollowId, c.TRANSFER_FROM as transferFrom,c.TRANSFER_STATUS as transferStatus,");
		cusSql.append(" c.invalid_reason as invalidReason,");
		cusSql.append("  CONCAT('',s.SCHOOL) as pointialStuSchool ,s.GRADE_ID as pointialStuGrade, s.name as pointialStuName from customer c ");
		cusSql.append(" LEFT JOIN customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
		cusSql.append(" LEFT JOIN student s on s.id=csr.STUDENT_ID where 1=1 ");
		

        Map<String, Object> params = Maps.newHashMap();
		
		if(StringUtils.isNotEmpty(cus.getName())){
			cusSql.append(" and c.name like :name ");
			params.put("name", "%"+cus.getName()+"%");
		}
		if(StringUtils.isNotEmpty(cus.getRecordDate())){
			cusSql.append(" and c.RECORD_DATE like :recordDate" );
			params.put("recordDate", cus.getRecordDate()+"%");
		}
		if (StringUtils.isNotBlank(cus.getLastDeliverName())) {
			cusSql.append(" and c.LAST_DELIVER_NAME like :lastDeliverName ");
			params.put("lastDeliverName", "%"+cus.getLastDeliverName()+"%");
		}
		if(StringUtils.isNotEmpty(cus.getCreateUserName())){
			cusSql.append(" and c.CREATE_USER_ID in (select user_id from `user` where name like :createUserName ) ");
			params.put("createUserName", "%"+cus.getCreateUserName()+"%");
		}
		if(StringUtils.isNotEmpty(cus.getCusOrg())){
			cusSql.append(" and c.CUS_ORG= :cusOrg ");
			params.put("cusOrg", cus.getCusOrg());
		}
		if(StringUtils.isNotEmpty(cus.getCusType())){
			cusSql.append(" and c.CUS_TYPE = :cusType ");
			params.put("cusType", cus.getCusType());
		}
		if(StringUtils.isNotEmpty(cus.getContact())){
			cusSql.append(" and c.CONTACT like :contact ");
			params.put("contact", "%"+cus.getContact()+"%");
		}
		if(StringUtils.isNotEmpty(cus.getRemark())){
			cusSql.append("  and c.REMARK like :remark ");
			params.put("remark", "%"+cus.getRemark()+"%");
		}
		if(StringUtils.isNotEmpty(cus.getBlCampusName())){
			cusSql.append(" and c.BL_CAMPUS_ID in (select id from organization where name LIKE :blCampusName ) ");
			params.put("blCampusName", "%"+cus.getBlCampusName()+"%");
		}
		if(StringUtils.isNotEmpty(cus.getResEntranceId())){
			cusSql.append(" and c.RES_ENTRANCE= :resEntranceId ");
			params.put("resEntranceId", cus.getResEntranceId());
		}
		if(StringUtils.isNotEmpty(cus.getBlSchool())){
			cusSql.append(" and c.BL_SCHOOL = :blSchool ");
			params.put("blSchool", cus.getBlSchool());
		}
		if(StringUtils.isEmpty(cus.getBlSchool()) && StringUtils.isNotBlank(cus.getBrenchId())){
			//查询分公司下所有客户
			cusSql.append(" and c.BL_SCHOOL in ( select id from organization where parentID= :brenchId ) ");
			params.put("brenchId", cus.getBrenchId());
		}
		
		if(cus.getDealStatus()!=null){
			cusSql.append(" and c.DEAL_STATUS = :dealStatus ");
			params.put("dealStatus", cus.getDealStatus().getValue());
		}
		if(cus.getDeliverType() != null){
			cusSql.append(" and c.DELIVER_TYPE = :deliverType ");
			params.put("deliverType", cus.getDeliverType().getValue());
		}
		if(StringUtils.isNotEmpty(cus.getDeliverTarget())){
			cusSql.append("and c.DELEVER_TARGET = :deliverTarget ");
			params.put("deliverTarget", cus.getDeliverTarget());
		}else if(StringUtils.isNotEmpty(cus.getDeliverTargetName())){
			//通过getDeliverTargetName 获取 DeliverTarget						
			cusSql.append(" and (c.DELEVER_TARGET in (select user_id from user where name like :deliverTargetName ) or c.DELEVER_TARGET in (select id from organization  where name like :deliverTargetName ) ) ");					
			params.put("deliverTargetName", "%"+cus.getDeliverTargetName()+"%");
		}
		
		
		if(StringUtils.isNotBlank(cus.getCustomerList()) && cus.getCustomerList().equals("customer")){
			if(StringUtils.isNotEmpty(cus.getStartDate())){
				cusSql.append(" and c.CREATE_TIME >= :startDate ");
				params.put("startDate", cus.getStartDate());
			}
			if(StringUtils.isNotEmpty(cus.getEndDate())){
				cusSql.append(" and c.CREATE_TIME < :endDate ");
				params.put("endDate", DateTools.addDateToString(cus.getEndDate(), 1));
			}
		}else{
			if(StringUtils.isNotEmpty(cus.getStartDate())){
				cusSql.append(" and c.DELIVER_TIME >= :startDate ");
				params.put("startDate", cus.getStartDate());
			}
			if(StringUtils.isNotEmpty(cus.getEndDate())){
				cusSql.append(" and c.DELIVER_TIME < :endDate ");
				params.put("endDate", DateTools.addDateToString(cus.getEndDate(), 1));
			}
			cusSql.append(" and c.DEAL_STATUS <> '" + CustomerDealStatus.SIGNEUP + "' ");
			cusSql.append(" and c.DEAL_STATUS <> '" + CustomerDealStatus.INVALID + "' "); 
		}		
		
		if(StringUtils.isNotEmpty(cus.getSchoolAddress())){
			cusSql.append(" and c.BL_SCHOOL in (select id from organization where REGION_ID in (select id from data_dict where name like :schoolAddress )) ");
			params.put("schoolAddress",cus.getSchoolAddress());
		}
		
		if(StringUtils.isNotEmpty(cus.getBeforeDeliverTarget())){
			cusSql.append(" and c.BEFOR_DELIVER_TARGET = :beforeDeliverTarget ");
			params.put("beforeDeliverTarget",cus.getBeforeDeliverTarget());
		}
		if(StringUtils.isNotEmpty(cus.getResEntranceId())){
			cusSql.append(" and c.RES_ENTRANCE = :resEntranceId ");
			params.put("resEntranceId",cus.getResEntranceId());
		}
		if(StringUtils.isNotEmpty(cus.getIntentionOfTheCustomerId())){
			cusSql.append(" and c.INTENTION_OF_THE_CUSTOMER = :intentionOfTheCustomerId ");
			params.put("intentionOfTheCustomerId",cus.getIntentionOfTheCustomerId());
		}

		
		if(StringUtils.isNotEmpty(cus.getKeywork())){    
			params.put("keywork","%"+cus.getKeywork()+"%");
			cusSql.append(" and (");        	
			cusSql.append(" c.name like :keywork ");        	
			cusSql.append(" or c.contact like :keywork ");     			
        			
        	//资源类型
        	String cusTypeId=dataDictDao.getDataDictIdByLikeName(cus.getKeywork(),DataDictCategory.RES_TYPE);
        	if(StringUtils.isNotEmpty(cusTypeId)){ 
        		cusSql.append(" or c.CUS_TYPE in ( :cusTypeId ) ");
        		params.put("cusTypeId",cusTypeId);
        	}
        	//客户意向度分级
        	String intentionId=dataDictDao.getDataDictIdByLikeName(cus.getKeywork(),DataDictCategory.INTENTION_OF_THE_CUSTOMER);
        	if(StringUtils.isNotEmpty(intentionId)){ 
        		cusSql.append(" or c.INTENTION_OF_THE_CUSTOMER in (:intentionId) ");
        		params.put("intentionId",intentionId);
        	}
        	
        	cusSql.append(" )");
        }
		
		if((cus.getDealStatus() == null || CustomerDealStatus.STAY_FOLLOW==cus.getDealStatus())
				&& cus.getDeliverType() != null
				&&  StringUtils.isNotEmpty(cus.getDeliverTarget())){//咨询师工作台 数据查新
			
		}else if(StringUtils.isNotBlank(cus.getShowDeliver()) && "1".equals(cus.getShowDeliver())){//已分配的客户不现实在前工序跟进管理列表里面。
			cusSql.append(" and c.BEFOR_DELIVER_TARGET = '"+userService.getCurrentLoginUser().getUserId()+"' ");//前工序等于自己
			cusSql.append(" and c.DELEVER_TARGET <> '"+userService.getCurrentLoginUser().getUserId()+"' ");//跟进不等于自己
		}else if(StringUtils.isNotBlank(cus.getShowDeliver()) && "2".equals(cus.getShowDeliver())){
			
		} else{ // 客户列表数据查询
			if(StringUtils.isNotBlank(cus.getCustomerList()) && cus.getCustomerList().equals("resource")){
				//客户资源调配不用权限查询
			}else{
				//我跟进的资源				
//				cusSql.append(roleQLConfigService.getValueResult("客户列表","sql"));
			}
			
		}		
		
		if(StringUtils.isNotBlank(cus.getPointialStuGrade())){
			cusSql.append("and s.GRADE_ID = :pointialStuGrade ");
			params.put("pointialStuGrade",cus.getPointialStuGrade());
		}
		
		if(StringUtils.isNotBlank(cus.getPointialStuSchool())){
			cusSql.append(" and s.SCHOOL in (select id from student_school where name like :pointialStuSchool ) ");
			params.put("pointialStuSchool","%"+cus.getPointialStuSchool()+"%");
		}		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {			
			cusSql.append(" order by "+dp.getSidx()+" "+dp.getSord());
		} 

		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dp,params);
		dp.setDatas(list);
		dp.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));			
			List<CustomerVo> voList=new ArrayList<CustomerVo>();
			int num=0;
			for(Map<Object,Object> tmaps : list){
				Map<String,Object> maps =(Map)tmaps;
				CustomerVo vo =new CustomerVo();
				num+=1;
				vo=this.forMapToCustomer(maps, vo,num);
				
				if(StringUtils.isNotEmpty(vo.getDeliverTarget())){
					if(CustomerDeliverType.PERSONAL_POOL.equals(vo.getDeliverType())){
						vo.setDeliverTargetName(userService.getUserById(vo.getDeliverTarget()).getName());
					}else if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.equals(vo.getDeliverType()) ){
						vo.setDeliverTargetName(organizationDao.findById(vo.getDeliverTarget()).getCustomerPoolName());
					}
				}
				if(StringUtils.isNotEmpty(vo.getNextFollowupTime())){
					List<CustomerFolowup> fvos = customerFolowupDao.findByCriteria(HibernateUtils.prepareOrder(null, "createTime", "desc"), Expression.eq("customer.id", vo.getCusId()), Expression.eq("createUser.userId", vo.getCusId()));
					int size = fvos.size();
					if(size > 0){
						CustomerFolowup followup = fvos.get(0);
						vo.setLastFollowUpTime(followup.getCreateTime());
					}
				}
				if (vo.getCreateUserId() != null && StringUtils.isNotBlank(vo.getCreateUserId())) {
					Map map=userService.getMainDeptAndJob(vo.getCreateUserId());
					
					if(map!=null){
						vo.setCreateUserDept(map.get("deptName")==null?"":map.get("deptName").toString());
					}
				}
				
			    List<CustomerFolowup> customerFolowups = customerFolowupDao.findByCriteria(HibernateUtils.prepareOrder(new DataPackage(), "createTime", "desc"), Expression.eq("customer.id", vo.getCusId()),Expression.eq("createUser.userId", userService.getCurrentLoginUser().getUserId()));
				if(customerFolowups.size()>0){
					CustomerFolowup cfu=customerFolowups.get(0);
						if(cfu.getVisitType()==null ||(cfu.getVisitType()!=null && cfu.getVisitType().equals(VisitType.NOT_COME))){
							vo.setIsAppointCome("否");
						}else{
							vo.setIsAppointCome("是");
						}
				}
				voList.add(vo);
			}
			dp.setDatas(voList);
			return dp;
	}	

	/**
	 * 客户资源导入明细
	 */
	@Override
	public DataPackage getCustomerImportInfo(CustomerImportTransform cif,String startDate,String endDate,String gradeName,
			DataPackage dp) {
		return customerImportTransformDao.getCustomerImportInfo(cif,startDate,endDate,gradeName,dp);
	}

	/**
	 * 获取的导入的客户详情
	 */
	@Override
	public CustomerImportTransform getImportCustomerById(String id) {
		CustomerImportTransform cif=customerImportTransformDao.findById(id);
		getDataByCif(cif);
		return cif;
	}
	
	/**
	 * 修改导入客户信息
	 */

	@Override
	public void editImportCustomer(CustomerImportTransform cif,
			StudentImportVo stu,Boolean listImp) {
		CustomerImportTransform old=new CustomerImportTransform();
		if(StringUtils.isNotBlank(cif.getId())){
			old=customerImportTransformDao.findById(cif.getId());
			Customer cus = null;
			if(StringUtils.isNotBlank(cif.getCusContact())) {
				cus = loadCustomerByContact(cif.getCusContact());
			}
			if(cus !=null) {
				if(listImp){
			    	//批量导入错误提示
					throw new ApplicationException("导入失败。客户："+cif.getCusName()+"  电话号码"+cif.getCusContact()+"重复");
				}else {
					throw new ApplicationException("导入失败。客户："+cif.getCusName()+"  电话号码"+cif.getCusContact()+"重复，如需修改客户其他信息请到客户列表中修改！");
				}
			}
			old.setCusName(cif.getCusName());
			old.setCusContact(cif.getCusContact());			
//			old.setResEntrance(cif.getResEntrance());
//			old.setResEntranceName(cif.getResEntranceName());
			old.setCusOrg(cif.getCusOrg());
			old.setCusOrgName(cif.getCusOrgName());
			old.setCusType(cif.getCusType());
			old.setCusTypeName(cif.getCusTypeName());
			old.setGradeName(cif.getGradeName());
			old.setRemark(cif.getRemark());
			if(StringUtils.isNotBlank(cif.getDeliverStatus())){
				old.setDeliverStatus(cif.getDeliverStatus());
			}
			if(StringUtils.isNotBlank(cif.getDeliverTarget())){
				old.setDeliverTarget(cif.getDeliverTarget());		
			}
			if(StringUtils.isNotBlank(cif.getDeliverType())){
				old.setDeliverType(cif.getDeliverType());
			}
		
			old.setDeliverTargetName(cif.getDeliverTargetName());	
			old.setStudentName(stu.getName());
			old.setStudentContact(stu.getContact());			
			old.setGradeId(stu.getGradeId());
//			if(StringUtils.isNotBlank(cif.getSchoolId())){
//				old.setBlSchoolName(cif.getBlSchoolName());
//				old.setSchoolId(stu.getSchoolId());
//			}else{
//				old.setBlSchoolName(null);
//				old.setSchoolId(null);
//			}
			
			if(StringUtils.isNotBlank(stu.getGradeId())){
				DataDict grade=dataDictDao.findById(stu.getGradeId());
				old.setGradeName(grade == null ? "" : grade.getName());
			}
			old.setClasses(stu.getClasses());
			old.setMotherName(stu.getMotherName());
			old.setFatherName(stu.getFatherName());
			old.setNotherPhone(stu.getNotherPhone());
			old.setFatherPhone(stu.getFatherPhone());
									
			getDataByCif(old);				
			
			CustomerVo vo=new CustomerVo();
			vo.setName(old.getCusName());
			vo.setContact(old.getCusContact());
			vo.setCusType(old.getCusType());
			vo.setCusOrg(old.getCusOrg());
			vo.setBlSchool(old.getBlSchool());
			vo.setDeliverTarget(old.getDeliverTarget());
			String deliverType=old.getDeliverType();
			if(old.getDeliverType()!=null && StringUtils.isNotBlank(old.getDeliverType())){
				vo.setDeliverType(CustomerDeliverType.valueOf(deliverType));
			}
			if(old.getDeliverStatus()!=null && StringUtils.isNotBlank(old.getDeliverStatus())){
				vo.setDealStatus(CustomerDealStatus.valueOf(old.getDeliverStatus()));
			}			
			vo.setBlCampusId(old.getBlSchool());
			vo.setTransferFrom(old.getCreateUser());
			vo.setRemark(old.getRemark());
			vo.setResEntranceId(old.getResEntrance());	
			
			//标记为导入
			vo.setSource("import");
			vo.setHaveStudent("1");
			
			String statu=this.saveOrUpdateCustomer(vo);
			
			if(statu.equals("fail") && listImp){
		    	//批量导入错误提示
				throw new ApplicationException("导入失败。客户："+cif.getCusName()+"  电话号码"+cif.getCusContact()+"重复");
			}else if(statu.equals("fail") && !listImp){
				throw new ApplicationException("导入失败。客户："+cif.getCusName()+"  电话号码"+cif.getCusContact()+"重复，如需修改客户其他信息请到客户列表中修改！");
			}else{
				old.setCusStatus("1");
				old.setCusId(statu);
				old.setFailReason("");
				customerImportTransformDao.commit();
				customerDao.commit();
			}		
			
			if(StringUtils.isBlank(stu.getName())) {
				stu.setName(cif.getCusName()+"孩子");
			}
			
			if (StringUtils.isNotBlank(stu.getName())){
				//保存学生信息
				Student student=new Student();
				student.setName(stu.getName());
				if (StringUtil.isNotBlank(stu.getContact())) {
				    student.setContact(stu.getContact());
				} else {
				    student.setContact(old.getCusContact()); 
				}
				if(StringUtils.isNotBlank(old.getSchoolId())){
					StudentSchool school=studentSchoolDao.findById(old.getSchoolId());
					student.setSchool( school == null ? null : school);
				}else if(StringUtils.isNotBlank(old.getBlSchoolName()) && StringUtils.isNotBlank(old.getSchoolRegionName())&&StringUtils.isNotBlank(old.getSchoolLevel())){
					Map<String, Object> params = Maps.newHashMap();
					params.put("name", old.getBlSchoolName());
					params.put("regionName", old.getSchoolRegionName());
					params.put("schoolLevel", old.getSchoolLevel());
					String sql = "select * from student_school where name= :name and city_id in (SELECT id from region where name= :regionName ) and CATEGORY in (SELECT id from data_dict where name= :schoolLevel and CATEGORY='SCHOOL_LEVEL')  ";
//				String sql="select * from student_school where name='"+stu.getSchoolName()+"' and PARENT_ID in (SELECT id from data_dict where name='"+stu.getRegionName()+"' )";
					List<StudentSchool> list=studentSchoolDao.findBySql(sql,params);
					if(list!=null && list.size()>0){
						StudentSchool school=list.get(0);
						if(school != null ){
							student.setSchool(school);
							old.setBlSchoolName(school.getName());
							old.setSchoolId(school.getId());
						}else{
							old.setBlSchoolName(null);
							old.setSchoolId(null);
						}
					}

				}
				if(StringUtils.isNotBlank(stu.getGradeId())){
					DataDict grade=dataDictDao.findById(stu.getGradeId());
					student.setGradeDict(grade == null ? null : grade);
				}
				student.setFatherName(stu.getFatherName());
				student.setMotherName(stu.getMotherName());
				student.setNotherPhone(stu.getNotherPhone());
				student.setFatherPhone(stu.getFatherPhone());
				student.setClasses(stu.getClasses());
				student.setCreateUserId(old.getCreateUser());
				student.setCreateTime((new SimpleDateFormat("yyyy-MM-dd")).format(new Date()));
				student.setStudentType(StudentType.POTENTIAL);
				student.setSchoolOrTemp("school");
				studentDao.save(student);
				studentDao.commit();

				old.setStudentId(student.getId());

				//保存客户学生关系
				CustomerStudent cusStu=new CustomerStudent();
//			CustomerStudentId id=new CustomerStudentId();
//			if(StringUtils.isNotBlank(old.getCusId())){
//				id.setCustomerId(old.getCusId());
//			}
//			if(StringUtils.isNotBlank(student.getId())){
//				id.setStudentId(student.getId());
//			}
				cusStu.setCustomer(new Customer(old.getCusId()));
				cusStu.setStudent(student);
				studentService.addCustomerStudent(cusStu);
			}

		}
		
	}

	/**
	 * 删除导入的客户信息
	 */
	@Override
	public void delImportCustomer(CustomerImportTransform cif) {
		if(cif.getId()!=null && StringUtils.isNotBlank(cif.getId()) ){
			Map<String, Object> params = Maps.newHashMap();
			params.put("id", cif.getId());
			String sql="DELETE from customer_import_transform where ID= :id ";
			customerImportTransformDao.excuteSql(sql,params);
		}else{
			throw new ApplicationException("没有找到删除对象的ID");
		}
		
	}

	/**
	 * 批量删除
	 *
	 * @param ws
	 */
	@Override
	public void delImCus(String ws) {
		if (StringUtil.isNotBlank(ws)){
			StringBuffer array = new StringBuffer();
			String[] split = ws.split(",");
			if (split!=null){
				for (int i=0; i<split.length ; i++){
					array.append("'").append(split[i]).append("'");
					if (i <split.length-1){
						array.append(",");
					}
				}
			}

			String sql="DELETE from customer_import_transform where ID in ("+array.toString()+") ";
			Map<String,Object> params = Maps.newHashMap();
			customerImportTransformDao.excuteSql(sql,params);
		}
	}

	/**
	 * 批量导入客户资源
	 */
	@Override
	public void batchImportCustomer(String ids) {
		if(ids.length()>0){
			String idArray[] = ids.split(",");
			for(String id:idArray){
				CustomerImportTransform cus=customerImportTransformDao.findById(id);
				StudentImportVo stu=new StudentImportVo();
				stu.setName(cus.getStudentName());
				stu.setContact(cus.getStudentContact());
				stu.setGradeId(cus.getGradeId());
				stu.setSchoolName(cus.getBlSchoolName());
				stu.setRegionName(cus.getSchoolRegionName());
				stu.setFatherName(cus.getFatherName());
				stu.setFatherPhone(cus.getFatherPhone());
				stu.setMotherName(cus.getMotherName());
				stu.setNotherPhone(cus.getNotherPhone());
				stu.setClasses(cus.getClasses());
				this.editImportCustomer(cus, stu,true);
			}
		}
		
	}

	/**
	 * 客户资源导入从临时表到customer
	 */
	@Override
	public void importToCustomer() throws SQLException {
		log.info("########## proc_update_customerImport   proc_insertCustomer_fromImport ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{CALL proc_update_customerImport()}";
		procedureDao.executeProc(sql);
		
		String sql2 = "{CALL proc_insertCustomer_fromImport()}";
		procedureDao.executeProc(sql2);
		log.info("########## proc_update_customerImport   proc_insertCustomer_fromImport ########## " + "end" );
		
	}
	
	/**
	 * 获取数据字典
	 */
	
	public CustomerImportTransform getDataByCif(CustomerImportTransform cif){
		Map<String, Object> params = Maps.newHashMap();
		Map<String, Object> params2 = Maps.newHashMap();
		Map<String, Object> params3 = Maps.newHashMap();
		if((cif.getCusType()==null || StringUtils.isBlank(cif.getCusType())) && (cif.getCusTypeName()!=null ||StringUtils.isNotBlank(cif.getCusTypeName())) ){
			String sql=" SELECT * from data_dict where `NAME`= :cusTypeName and CATEGORY='RES_TYPE' ";
			params3.put("cusTypeName", cif.getCusTypeName());
			if((cif.getCusOrg()==null || StringUtils.isBlank(cif.getCusOrg())) && (cif.getCusOrgName()!=null|| StringUtils.isNotBlank(cif.getCusOrgName()))){
				String sql2=" SELECT * from data_dict where name= :cusOrgName and PARENT_ID=(SELECT ID from data_dict where `NAME`= :cusTypeName and CATEGORY='RES_TYPE' ) and CATEGORY='CUS_ORG' ";				
				params2.put("cusOrgName", cif.getCusOrgName());
				params2.put("cusTypeName", cif.getCusTypeName());
				List<DataDict> listCusOrg=dataDictDao.findBySql(sql2,params2);
				if(listCusOrg!=null && listCusOrg.size()>0){
					cif.setCusOrg(listCusOrg.get(0).getId());
				}
				
			}
			List<DataDict> cusTypes=dataDictDao.findBySql(sql,params3);
			if(cusTypes!=null && cusTypes.size()>0){
				cif.setCusType(cusTypes.get(0).getId());
			}

			
			
		}
		if(cif.getBlSchoolName() != null){
			String sql="";
			if(cif.getSchoolLevel() != null && cif.getSchoolRegionName() != null ){
				sql+=" select * from student_school where name = :studentSchoolName and PARENT_ID in (SELECT id from data_dict where name= :regionName ) and CATEGORY in (SELECT id from data_dict where name= :schoolLevel and CATEGORY='SCHOOL_LEVEL' )";
			    params.put("studentSchoolName", cif.getBlSchoolName());
			    params.put("regionName", cif.getSchoolRegionName());
			    params.put("schoolLevel", cif.getSchoolLevel());
			}else if(cif.getSchoolRegionName() != null){
				sql+=" select * from student_school where name = :studentSchoolName and PARENT_ID in (SELECT id from data_dict where name= :regionName  )";
			    params.put("studentSchoolName", cif.getBlSchoolName());
			    params.put("regionName", cif.getSchoolRegionName());
			}
			if(sql.length() > 0 ){
				List<StudentSchool> list=studentSchoolDao.findBySql(sql,params);
				if(list != null && list.size()>0){
					StudentSchool school=list.get(0);
					cif.setBlSchoolName(cif.getBlSchoolName());
					cif.setSchoolId(school.getId());
				}
			}
			
		}
		
		return cif;
	}
	
	/**
	 * 获取客户资源，改变跟进状态
	 */
	@Override
	public Response changeCustomerStatus(Customer cus){
		//测试号码是否已存在
		Response res=new Response();
		CustomerVo vo = new CustomerVo();
		vo.setContact(cus.getContact());
		vo.setId(cus.getId());
		if (customerDao.getCustomerCountByPhone(vo) > 0 && StringUtils.isNotBlank(cus.getContact())) {
			res.setResultCode(-1);
			res.setResultMessage("获取失败，客户电话号码重复");
			return res;
		}
		String currentTime=DateTools.getCurrentDateTime();
		User user=userService.getCurrentLoginUser();
		cus.setModifyTime(currentTime);
		cus.setModifyUserId(user.getUserId());
		if (StringUtil.isBlank(cus.getDeliverTarget()) && StringUtil.isBlank(cus.getBeforeDeliverTarget())) {
			cus.setDeliverTarget(userService.getCurrentLoginUser().getUserId());
			cus.setDealStatus(CustomerDealStatus.FOLLOWING);
			cus.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
		}
		if (cus.getDeliverTarget() != null) {
			String lastDeliverNmae = userService.getCurrentLoginUser().getName();
			cus.setLastDeliverName(lastDeliverNmae);
		}else if(cus.getBeforeDeliverTarget() != null){
			String lastDeliverNmae = userService.getCurrentLoginUser().getName();
			cus.setLastDeliverName(lastDeliverNmae);
		}
		cus.setDeliverTime(DateTools.getCurrentDateTime());
		//归属校区处理
		if(StringUtils.isNotBlank(cus.getDeliverTarget())){
			Organization deOrganization = organizationDao.findById(cus.getDeliverTarget());
			User deUser = userDao.findById(cus.getDeliverTarget());
			if(deOrganization!=null){
				cus.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
			}else if(deUser!=null){
				cus.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
			}
		}
			
		if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.equals(cus.getDeliverType())
				&& StringUtils.isNotBlank(cus.getDeliverTarget())){
			Organization deOrganization = organizationDao.findById(cus.getDeliverTarget());
			if(deOrganization!=null){
				Organization o = userService.getBelongCampusByOrgId(deOrganization.getId());
				cus.setBlSchool(o!=null?o.getId():null);
				cus.setBlCampusId(o);
			}			
		}else if(CustomerDeliverType.PERSONAL_POOL.equals(cus.getDeliverType())
				&& StringUtils.isNotBlank(cus.getDeliverTarget())){
			String organizationId=userService.getUserById(cus.getDeliverTarget()).getOrganizationId();
			Organization o = userService.getBelongCampusByOrgId(organizationId);
			cus.setBlSchool(o!=null?o.getId():null);
			cus.setBlCampusId(o);
		}else if(CustomerDeliverType.PERSONAL_POOL.equals(cus.getDeliverType())
				&& StringUtils.isNotBlank(cus.getBeforeDeliverTarget())){
			String organizationId=userService.getUserById(cus.getBeforeDeliverTarget()).getOrganizationId();
			Organization o = userService.getBelongCampusByOrgId(organizationId);
			cus.setBlSchool(o!=null?o.getId():null);
			cus.setBlCampusId(o);
		}else if(StringUtils.isEmpty(cus.getBlSchool())){
			Organization o = userService.getBelongCampus();
			cus.setBlSchool(o!=null?o.getId():null);
			cus.setBlCampusId(o);
		}
		customerDao.save(cus);
		return res;
	}
	
	@Override
	public String updateCustomer(Customer customer) {
		customerDao.save(customer);
		return customer.getId();
	}
	@Override
	public Customer loadCustomerByContact(String contact) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("contact", contact);
		String sql_customer = " select * from customer c where c.CONTACT  = :contact ";
		List<Customer> list = customerDao.findBySql(sql_customer,params);
		if(list!=null && list.size()>0){
			Customer customer = list.get(0);
			return customer;
		}else{
			return null;
		}
		
	}
	   /**
     * 保存转介绍的客户
     */
	@Override
	public String saveTransferCustomer(Customer customer,CustomerVo customerVo) {
		if (StringUtil.isNotBlank(customerVo.getTransactionUuid())) {
			transactionRecordDao.saveTransactionRecord(customerVo.getTransactionUuid());
		}   
		String currentTime=DateTools.getCurrentDateTime();
		User user=userService.getCurrentLoginUser();
        customer.setRecordDate(currentTime);
        if(null==customer.getRecordUserId()){
        	customer.setRecordUserId(user);
        }
        customer.setCreateTime(currentTime);
        customer.setCreateUser(user);
		customer.setModifyTime(currentTime);
		customer.setModifyUserId(user.getUserId());
		//客户归属校区
		if(StringUtils.isNotEmpty(user.getOrganizationId())){
			customer.setBlCampusId(new Organization(user.getOrganizationId()));
			customer.setBlSchool(user.getOrganizationId());
		}else{
			Organization blCampus = userService.getBelongCampus();
			if(blCampus!=null){
				customer.setBlCampusId(blCampus);
				customer.setBlSchool(blCampus.getId());
			}		
		}
		
		//下面这个if在保存转介绍客户的时候是否需要 待定
		//保存转介绍客户信息的时候不需要 下面的操作 2016-09-23 xiaojinwang
//		if(StringUtils.isNotBlank(customer.getDeliverTarget())){
//			User u=userDao.findById(customer.getDeliverTarget());
//			if(u!=null){
//				customer.setTransferFrom(userService.getBelongCampusByUserId(customer.getDeliverTarget()).getId());
//			}else{
//				customer.setTransferFrom(customer.getDeliverTarget());				
//			}		
//		}else{
//			customer.setTransferFrom(userService.getBelongCampus().getId());
//		}
		
		//归属校区处理  暂时不处理  页面已经不显示
		//审批通过或者不通过都是 新增状态
		customer.setDealStatus(CustomerDealStatus.TOBE_AUDIT);
//		customer.setDeliverTarget(null);
//		customer.setDeliverTime(null);

		customerDao.save(customer);
		//发送消息给相关客户 暂时不处理
		return customer.getId();
	}
	
	@Override
	public DataPackage getDistributeCustomersPersonPool(DistributeCustomerVo vo, DataPackage dataPackage, String workbrenchType) {
		//分配客户管理--多条件获取客户列表
		//当前登录者
		User user = userService.getCurrentLoginUser();
		
		//分配客户管理  这些客户的资源入口RES_Entrance 不一定生效 要上门登记的时候资源入口才生效
		//但是客户录入或者被分配的时候 预资源入口PRE_Entrance生效 但是不显示在前端列表里面
		//对于外呼主管网络主管 分配给他们 或者他们自己录入客户并且分配给他们自己  客户的状态是待跟进  
		StringBuilder cusSql=new StringBuilder(1024);	
		cusSql.append(" select c.id as cusId,c.name as cusName,c.CONTACT as contact,");
		cusSql.append(" c.REMARK as remark,c.LAST_DELIVER_NAME as lastDeliverName,c.DELIVER_TIME as deliverTime,");
		cusSql.append(" c.RES_ENTRANCE as resEntrance,c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer ");	
		//两个资源入口进行查询
		cusSql.append(" ,d.`NAME` as intentionOfTheCustomerName,dd.`NAME` as resEntranceName,c.DEAL_STATUS as dealStatus ");
		cusSql.append(" from customer c ");
		cusSql.append(" LEFT JOIN data_dict d on c.INTENTION_OF_THE_CUSTOMER = d.ID ");
		cusSql.append(" LEFT JOIN data_dict dd on c.RES_ENTRANCE = dd.ID ");
		//cusSql.append(" from customer c LEFT JOIN customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
		//cusSql.append(" LEFT JOIN student s on s.id=csr.STUDENT_ID  ");
		//cusSql.append(" where (c.RES_ENTRANCE ='"+workbrenchType+"' or c.PRE_ENTRANCE ='"+workbrenchType+"' ) ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", user.getUserId());
		cusSql.append(" where c.DELEVER_TARGET = :userId ");
		cusSql.append(" and c.DEAL_STATUS ='"+CustomerDealStatus.STAY_FOLLOW.getValue()+"'");
		
		if(StringUtils.isNotBlank(vo.getResEntranceId())){
			params.put("resEntranceId", vo.getResEntranceId());
			cusSql.append(" and (c.RES_ENTRANCE = :resEntranceId or c.PRE_ENTRANCE = :resEntranceId )  ");
		}
		
		if(StringUtils.isNotBlank(vo.getCusName())){
			cusSql.append(" and c.name like :cusName ");
			params.put("cusName", "%"+vo.getCusName()+"%");
		}
		if(StringUtils.isNotBlank(vo.getContact())){
			cusSql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}

		if(StringUtils.isNotBlank(vo.getLastDeliverName())){
			cusSql.append(" and c.LAST_DELIVER_NAME like :lastDeliverName ");
			params.put("lastDeliverName", "%"+vo.getLastDeliverName()+"%");
		}
		if(StringUtils.isNotBlank(vo.getDeliverBeginDate())){
			cusSql.append(" and c.DELIVER_TIME >= :deliverBeginDate ");
			params.put("deliverBeginDate", vo.getDeliverBeginDate()+" 00:00:00");
		}			
		if(StringUtils.isNotBlank(vo.getDeliverEndDate())){
			cusSql.append(" and c.DELIVER_TIME <= :deliverEndDate ");
			params.put("deliverEndDate", vo.getDeliverEndDate()+" 23:59:59");
		}
		
		if(StringUtils.isNotBlank(vo.getDealStatus())){
			cusSql.append(" and c.DEAL_STATUS = :dealStatus ");
			params.put("dealStatus", vo.getDealStatus());
		}
		if(StringUtils.isNotBlank(vo.getDeliverType())){
			cusSql.append(" and c.DELIVER_TYPE = :deliverType ");
			params.put("deliverType", vo.getDeliverType());
		}
		//注释掉分配人 因为分配人就是当前登录者 
//		if(StringUtils.isNotEmpty(vo.getDeliverTarget())){
//			cusSql.append("and c.DELEVER_TARGET = '"+vo.getDeliverTarget()+"' ");
//		}
		//意向度
		if (StringUtils.isNotBlank(vo.getIntentionOfTheCustomerId())) {
			cusSql.append(" and c.INTENTION_OF_THE_CUSTOMER = :IntentionOfTheCustomerId ");
			params.put("IntentionOfTheCustomerId", vo.getIntentionOfTheCustomerId());
		}
		
		//如果这三个条件有一个不为空则关联学生表
		if(StringUtils.isNotBlank(vo.getStudentName())||StringUtils.isNotBlank(vo.getSchoolId())||StringUtils.isNotBlank(vo.getGradeId())){
			cusSql.append(" and c.id in ( select cs.CUSTOMER_ID from customer_student_relation cs left join ");
			cusSql.append(" student s on cs.STUDENT_ID = s.ID where 1=1 ");
			//学生姓名
			if(StringUtils.isNotBlank(vo.getStudentName())){
               cusSql.append(" and s.NAME like :studentName "); 
               params.put("studentName", "%"+vo.getStudentName()+"%");
			}
			//选择就读学校  这里传进来的是学校的Id 不是名字
			if(StringUtils.isNotBlank(vo.getSchoolId())){
			   cusSql.append(" and s.SCHOOL = :schoolId ");
			   params.put("schoolId", vo.getSchoolId());
			}
			//学生的年级   传入 学生年级的Id
			if(StringUtils.isNotBlank(vo.getGradeId())){
				cusSql.append(" and s.GRADE_ID = :gradeId ");
				 params.put("gradeId", vo.getGradeId());
			}
			
			cusSql.append(" ) ");
		}		
		
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());			
		} 
		//不能直接关联学生表  有多个学生
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
		//dataPackage.setDatas(list);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));	
		
		List<DistributeCustomerVo> voList=new ArrayList<DistributeCustomerVo>();
		StringBuilder studentBuffer= new StringBuilder(128);
		StringBuilder schoolBuffer = new StringBuilder(128);
		DistributeCustomerVo dCVo = null;
		for(Map<Object,Object> tmaps : list){
			Map<String,String> maps =(Map)tmaps;
		    dCVo = new DistributeCustomerVo();
			String cusId =maps.get("cusId");
			dCVo.setCusId(cusId!=null ? cusId:"");
			dCVo.setCusName(maps.get("cusName")!=null? maps.get("cusName"):"");
			dCVo.setContact(maps.get("contact")!=null? maps.get("contact"):"");
			//设置资源入口
			dCVo.setResEntranceId(maps.get("resEntrance"));
			dCVo.setResEntranceName(maps.get("resEntranceName"));
			//设置意向度
			dCVo.setIntentionOfTheCustomerId(maps.get("intentionOfTheCustomer"));
			dCVo.setIntentionOfTheCustomerName(maps.get("intentionOfTheCustomerName"));		
			//设置最后分配人名称
			dCVo.setLastDeliverName(maps.get("lastDeliverName")!=null ? maps.get("lastDeliverName"):"");
			//设置最后分配时间
			dCVo.setDeliverTime(maps.get("deliverTime")!=null ? maps.get("deliverTime"):"");
			//设置备注
			dCVo.setRemark(maps.get("remark")!=null ? maps.get("remark"):"");
			
			//设置客户状态
			if(maps.get("dealStatus")!=null){
			   dCVo.setDealStatusName(CustomerDealStatus.valueOf(maps.get("dealStatus")).getName());
			}
			
			// 个人池才显示学生姓名和就读学校
			//全部资源池都显示学生姓名和就读学校
			//if (vo.getDeliverType().equals(CustomerDeliverType.PERSONAL_POOL.getValue())) {
				// 设置学生姓名列表(多个学生用逗号,分割开来) 根据cusId来查询
				Map<String, Object> param = Maps.newHashMap();
				param.put("cusId", cusId);
				String sql = "SELECT s.`NAME` as studentName,ss.`NAME` as schoolName from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID LEFT JOIN student_school ss on s.SCHOOL = ss.ID"
						+ " where csr.CUSTOMER_ID = :cusId ";
				List<Map<Object, Object>> resultList = customerDao.findMapBySql(sql,param);
				if(resultList!=null && resultList.size()>1){				
					for(Map<Object, Object> map:resultList){
						if(map.get("studentName")!=null){
							studentBuffer.append(map.get("studentName")+",");
						}
						if(map.get("schoolName")!=null){
							schoolBuffer.append(map.get("schoolName")+",");
						}
						
					}
			        String studentList = studentBuffer.toString();
			        String schoolList = schoolBuffer.toString();
			        if(StringUtils.isNotBlank(studentList)){
			        	dCVo.setStudentNameList(new String(studentList.substring(0, studentList.length()-1)));
			        }else{
			        	dCVo.setStudentNameList("");
			        }
			        if(StringUtils.isNotBlank(schoolList)){
			        	dCVo.setSchoolNameList(new String(schoolList.substring(0, schoolList.length()-1)));
			        }else{
			        	dCVo.setSchoolNameList("");
			        }
			        //清空Buffer
			        studentBuffer.delete(0, studentBuffer.length());
			        schoolBuffer.delete(0, schoolBuffer.length());
				}else if(resultList.size()==1){
					Map<Object, Object> map=resultList.get(0);
					if(map.get("studentName")!=null){
						dCVo.setStudentNameList(map.get("studentName").toString());
					}else{
						dCVo.setStudentNameList("");
					}
					if(map.get("schoolName")!=null){
						dCVo.setSchoolNameList(map.get("schoolName").toString());
					}else{
						dCVo.setSchoolNameList("");
					}
				}				
			//}
	        //设置最新跟进记录	 
			StringBuilder query =new StringBuilder(128);
			Map<String, Object> param2 = Maps.newHashMap();
			param2.put("cusId", cusId);
			param2.put("appointmentType", AppointmentType.FOLLOW_UP.getValue());
			query.append(" select cf.remark as remark from customer_folowup cf where appointment_type = :appointmentType ");
			query.append(" and cf.customer_id = :cusId ORDER BY cf.create_time desc LIMIT 1 ");			
			List<Map<Object, Object>> customerFolowups=customerFolowupDao.findMapBySql(query.toString(),param2);
			if (customerFolowups.size() > 0) {
				dCVo.setFollowupRemark(customerFolowups.get(0).get("remark")!=null?customerFolowups.get(0).get("remark").toString():"");
			}
	        
	        voList.add(dCVo);
			
		}
		dataPackage.setDatas(voList);
        return dataPackage;
	}
	
	//从其他资源获取客户
	@Override
	public DataPackage getDistributeCustomersResourcePool(DistributeCustomerVo vo, DataPackage dataPackage, String workbrenchType) {
		//分配客户管理--多条件获取客户列表
		//传入的deliverTarget是资源池的organzationId		
		StringBuilder cusSql=new StringBuilder(512);	
		cusSql.append(" select c.id as cusId,c.name as cusName,c.CONTACT as contact,");
		cusSql.append(" c.REMARK as remark,c.LAST_DELIVER_NAME as lastDeliverName,c.DELIVER_TIME as deliverTime,");
		cusSql.append(" c.RES_ENTRANCE as resEntrance,c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer ");			
		cusSql.append(" ,d.`NAME` as resEntranceName,dd.`NAME` as intentionName,c.DEAL_STATUS as dealStatus  ");
		
		cusSql.append(" from customer c");
		cusSql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID ");
		cusSql.append(" LEFT JOIN data_dict dd on c.INTENTION_OF_THE_CUSTOMER = dd.ID  ");
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("deliverTarget", vo.getDeliverTarget());
		//cusSql.append(" where (c.RES_ENTRANCE ='"+workbrenchType+"' or c.PRE_ENTRANCE ='"+workbrenchType+"' ) ");
		cusSql.append(" where c.DELEVER_TARGET = :deliverTarget ");
		
		//无效客户也要筛出来
		cusSql.append(" and (c.DEAL_STATUS ='"+CustomerDealStatus.STAY_FOLLOW.getValue()+"' or c.DEAL_STATUS ='"+CustomerDealStatus.INVALID.getValue()+"' )");
		
		if(StringUtils.isNotBlank(vo.getResEntranceId())){
			params.put("resEntranceId", vo.getResEntranceId());
			cusSql.append(" and (c.RES_ENTRANCE = :resEntranceId or c.PRE_ENTRANCE = :resEntranceId )  ");
		}
		
		if(StringUtils.isNotBlank(vo.getCusName())){
			cusSql.append(" and c.name like :name ");
			params.put("name", "%"+vo.getCusName()+"%");
		}
		if(StringUtils.isNotBlank(vo.getContact())){
			cusSql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
		if(StringUtils.isNotBlank(vo.getLastDeliverName())){
			cusSql.append(" and c.LAST_DELIVER_NAME like :lastDeliverName ");
			params.put("lastDeliverName", "%"+vo.getLastDeliverName()+"%");
		}
		if(StringUtils.isNotBlank(vo.getDeliverBeginDate())){
			cusSql.append(" and c.DELIVER_TIME >= :deliverBeginDate ");
			params.put("deliverBeginDate", vo.getDeliverBeginDate()+" 00:00:00");
		}			
		if(StringUtils.isNotBlank(vo.getDeliverEndDate())){
			cusSql.append(" and c.DELIVER_TIME <= :deliverEndDate ");
			params.put("deliverEndDate", vo.getDeliverEndDate()+" 23:59:59");
		}
		if(StringUtils.isNotBlank(vo.getDeliverType())){
			cusSql.append(" and c.DELIVER_TYPE = :deliverType ");
			params.put("deliverType", vo.getDeliverType());
		}
		
		if(StringUtils.isNotBlank(vo.getDealStatus())){
			cusSql.append(" and c.DEAL_STATUS = :dealStatus ");
			params.put("dealStatus", vo.getDealStatus());
		}
		//如果这三个条件有一个不为空则关联学生表
		if(StringUtils.isNotBlank(vo.getStudentName())||StringUtils.isNotBlank(vo.getSchoolId())||StringUtils.isNotBlank(vo.getGradeId())){
			cusSql.append(" and c.id in ( select cs.CUSTOMER_ID from customer_student_relation cs left join ");
			cusSql.append(" student s on cs.STUDENT_ID = s.ID where 1=1 ");
			//学生姓名
			if(StringUtils.isNotBlank(vo.getStudentName())){
               cusSql.append(" and s.NAME like :studentName "); 
               params.put("studentName", "%"+vo.getStudentName()+"%");
			}
			//选择就读学校  这里传进来的是学校的Id 不是名字
			if(StringUtils.isNotBlank(vo.getSchoolId())){
			   cusSql.append(" and s.SCHOOL = :schoolId ");
			   params.put("schoolId", vo.getSchoolId());
			}
			//学生的年级   传入 学生年级的Id
			if(StringUtils.isNotBlank(vo.getGradeId())){
				cusSql.append(" and s.GRADE_ID = :gradeId ");
				 params.put("gradeId", vo.getGradeId());
			}
			
			cusSql.append(" ) ");
		}
		
		
		//意向度
		if (StringUtils.isNotBlank(vo.getIntentionOfTheCustomerId())) {
			cusSql.append(" and c.INTENTION_OF_THE_CUSTOMER = '" + vo.getIntentionOfTheCustomerId() + "' ");
		}
		if(StringUtils.isNotBlank(vo.getDealStatus())){
			cusSql.append(" and c.DEAL_STATUS = '"+vo.getDealStatus()+"' ");
		}
		//如果这三个条件有一个不为空则关联学生表
		if(StringUtils.isNotBlank(vo.getStudentName())||StringUtils.isNotBlank(vo.getSchoolId())||StringUtils.isNotBlank(vo.getGradeId())){
			cusSql.append(" and c.id in ( select cs.CUSTOMER_ID from customer_student_relation cs left join ");
			cusSql.append(" student s on cs.STUDENT_ID = s.ID where 1=1 ");
			//学生姓名
			if(StringUtils.isNotBlank(vo.getStudentName())){
               cusSql.append(" and s.NAME like '%"+vo.getStudentName()+"%' "); 						
			}
			//选择就读学校  这里传进来的是学校的Id 不是名字
			if(StringUtils.isNotBlank(vo.getSchoolId())){
			   cusSql.append(" and s.SCHOOL ='"+vo.getSchoolId()+"' ");
			}
			//学生的年级   传入 学生年级的Id
			if(StringUtils.isNotBlank(vo.getGradeId())){
				cusSql.append(" and s.GRADE_ID ='"+vo.getGradeId()+"'");
			}
			
			cusSql.append(" ) ");
		}		
		
		
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
		} 
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));	
		
		List<DistributeCustomerVo> voList=new ArrayList<DistributeCustomerVo>();
		DistributeCustomerVo dCVo = null;
		StringBuilder studentBuffer= new StringBuilder(128);
		StringBuilder schoolBuffer = new StringBuilder(128);
		for(Map<Object,Object> tmaps : list){
			Map<String,String> maps = (Map)tmaps;
		    dCVo = new DistributeCustomerVo();
			String cusId =maps.get("cusId");
			dCVo.setCusId(cusId!=null ? cusId:"");
			dCVo.setCusName(maps.get("cusName")!=null? maps.get("cusName"):"");
			cusSql.append(" ,d.`NAME` as resEntranceName,dd.`NAME` as intentionName ");
			dCVo.setResEntranceId(maps.get("resEntrance"));
			dCVo.setResEntranceName(maps.get("resEntranceName"));
			dCVo.setIntentionOfTheCustomerId(maps.get("intentionOfTheCustomer"));
			dCVo.setIntentionOfTheCustomerName(maps.get("intentionName"));
			dCVo.setContact(maps.get("contact")!=null? maps.get("contact"):"");
			
			//设置最后分配人名称
			dCVo.setLastDeliverName(maps.get("lastDeliverName")!=null ? maps.get("lastDeliverName"):"");
			//设置最后分配时间
			dCVo.setDeliverTime(maps.get("deliverTime")!=null ? maps.get("deliverTime"):"");
			//设置备注
			dCVo.setRemark(maps.get("remark")!=null ? maps.get("remark"):"");
			//设置客户状态
			if(maps.get("dealStatus")!=null){
			   dCVo.setDealStatusName(CustomerDealStatus.valueOf(maps.get("dealStatus")).getName());
			}
	        //设置最新跟进记录	
			Map<String, Object> param = Maps.newHashMap();
			param.put("cusId", cusId);
			StringBuilder query =new StringBuilder(" select cf.remark as remark from customer_folowup cf where appointment_type ='"+AppointmentType.FOLLOW_UP.getValue()+"' ");
			query.append(" and cf.customer_id = :cusId ORDER BY cf.create_time desc LIMIT 1 ");			
			List<Map<Object, Object>> customerFolowups=customerFolowupDao.findMapBySql(query.toString(),param);
			if (customerFolowups.size() > 0) {
				dCVo.setFollowupRemark(customerFolowups.get(0).get("remark")!=null?customerFolowups.get(0).get("remark").toString():"");
			}
			
			
			Map<String, Object> param2 = Maps.newHashMap();
			param2.put("cusId", cusId);
			String sql = "SELECT s.`NAME` as studentName,ss.`NAME` as schoolName from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID LEFT JOIN student_school ss on s.SCHOOL = ss.ID"
					+ " where csr.CUSTOMER_ID = :cusId ";
			List<Map<Object, Object>> resultList = customerDao.findMapBySql(sql,param2);
			if(resultList!=null && resultList.size()>1){				
				for(Map<Object, Object> map:resultList){
					if(map.get("studentName")!=null){
						studentBuffer.append(map.get("studentName")+",");
					}
					if(map.get("schoolName")!=null){
						schoolBuffer.append(map.get("schoolName")+",");
					}
					
				}
		        String studentList = studentBuffer.toString();
		        String schoolList = schoolBuffer.toString();
		        if(StringUtils.isNotBlank(studentList)){
		        	dCVo.setStudentNameList(new String(studentList.substring(0, studentList.length()-1)));
		        }else{
		        	dCVo.setStudentNameList("");
		        }
		        if(StringUtils.isNotBlank(schoolList)){
		        	dCVo.setSchoolNameList(new String(schoolList.substring(0, schoolList.length()-1)));
		        }else{
		        	dCVo.setSchoolNameList("");
		        }
		        //清空Buffer
		        studentBuffer.delete(0, studentBuffer.length());
		        schoolBuffer.delete(0, schoolBuffer.length());
			}else if(resultList.size()==1){
				Map<Object, Object> map=resultList.get(0);
				if(map.get("studentName")!=null){
					dCVo.setStudentNameList(map.get("studentName").toString());
				}else{
					dCVo.setStudentNameList("");
				}
				if(map.get("schoolName")!=null){
					dCVo.setSchoolNameList(map.get("schoolName").toString());
				}else{
					dCVo.setSchoolNameList("");
				}
			}				
			
	        
	        voList.add(dCVo);
			
		}
		dataPackage.setDatas(voList);
        return dataPackage;
	}
	
	@Override
	public Response distributeCustomer(String cusId, String poolType,String workbrenchType) {
		Response response = new Response();
		if(StringUtils.isBlank(cusId)){
		    response.setResultCode(-1);
		    response.setResultMessage("未选中客户ID，请选择客户ID再提交！");
		    return response;
        }

		String [] cusIdArray = cusId.split(",");
		User user=userService.getCurrentLoginUser();
		if(user==null) throw new ApplicationException("系统出错");
		Boolean isZXS = userService.isUserRoleCode(user.getUserId(), RoleCode.CONSULTOR);
		Boolean isZXZG = userService.isUserRoleCode(user.getUserId(), RoleCode.CONSULTOR_DIRECTOR);
		isZXS = isZXS||isZXZG;      
		if("person".equals(poolType)){
			//从个人池获得 StringBuilder
			StringBuilder msgBuffer = new StringBuilder();
			for(String id:cusIdArray){				
				//根据customerId 获取客户customer
				Customer distributeCustomer = this.findById(id);				
				String resEntrance =null;
				if(distributeCustomer.getResEntrance()!=null){
					resEntrance = distributeCustomer.getResEntrance().getValue();
				}
				String before_deliverTarget = distributeCustomer.getBeforeDeliverTarget();
				//设置客户的跟进状态为FOLLOWING 跟进中				
                response=this.distributeCustomer(distributeCustomer, user,poolType,workbrenchType); 
        		if(response.getResultCode()==0){
        			//#1728 计算客户分配时间 20171110 duanmenrun
        			CustomerAllocateObtain  customerAllocateObtain = customerAllocateObtainService.findAllocateObtainByCustomerId(id);
        			if(customerAllocateObtain != null && customerAllocateObtain.getStatus()==0) {
            			customerAllocateObtain.setObtainTime(DateTools.getCurrentDateTime());
            			customerAllocateObtain.setStatus(1);
            			customerAllocateObtainService.saveOrUpdateCustomerAllocateObtain(customerAllocateObtain);
        			}
        			
        			//更改成功则 增加更改状态记录以及发出msg
        			
        			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
        			dynamicStatus.setDynamicStatusType(CustomerEventType.GET);
        			dynamicStatus.setDescription("从个人分配客户列表获取客户");
        			if(distributeCustomer.getResEntrance()!=null){
        		      	dynamicStatus.setResEntrance(distributeCustomer.getResEntrance());
        			}
        			dynamicStatus.setStatusNum(1);
        			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
        			customerEventService.addCustomerDynameicStatus(distributeCustomer, dynamicStatus, user);
        			
        			//customerEventService.saveCustomerDynamicStatus(id, CustomerEventType.FOLLOWING, "更改客户处理状态");	
        			//咨询师获取网络 和转介绍的客户  增加获取量       			
        			if(resEntrance!=null && (resEntrance.equals(ResEntranceType.ON_LINE)|| resEntrance.equals(ResEntranceType.TRANSFER))){
        				
            			CustomerDynamicStatus dynamicStatus2 = new CustomerDynamicStatus();
            			dynamicStatus2.setDynamicStatusType(CustomerEventType.RESOURCES);
            			dynamicStatus2.setDescription("获取量增加");
            			if(distributeCustomer.getResEntrance()!=null){
            		      	dynamicStatus2.setResEntrance(distributeCustomer.getResEntrance());
            			}
            			dynamicStatus2.setStatusNum(1);
            			dynamicStatus2.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
            			customerEventService.addCustomerDynameicStatus(distributeCustomer, dynamicStatus2, user);
        				
        			}
        			        			
    				
    				if(before_deliverTarget!=null && isZXS){
    					User before = userService.loadUserById(before_deliverTarget);
    					if(before!=null){
        					Boolean isWL = userService.isUserRoleCode(before_deliverTarget, RoleCode.NETWORK_SPEC);
        					Boolean isWH = userService.isUserRoleCode(before_deliverTarget, RoleCode.OUTCALL_SPEC);
        					
        			
            				
                			CustomerDynamicStatus dynamicStatus3 = new CustomerDynamicStatus();
            				if(isWL){
            					dynamicStatus3.setDynamicStatusType(CustomerEventType.COUNSELOR_NETWORK);
            				}else if(isWH){
            					dynamicStatus3.setDynamicStatusType(CustomerEventType.COUNSELOR_OUTCALL);
            				}else{
            					dynamicStatus3.setDynamicStatusType(CustomerEventType.COUNSELOR_OTHER);
            				}
                			
                			dynamicStatus3.setDescription("咨询师获取客户");
                			if(distributeCustomer.getResEntrance()!=null){
                		      	dynamicStatus3.setResEntrance(distributeCustomer.getResEntrance());
                			}
                			dynamicStatus3.setStatusNum(1);
                			dynamicStatus3.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                			customerEventService.addCustomerDynameicStatus(distributeCustomer, dynamicStatus3, before);
    					}
    				}
    				
    				// 获取客户的时候设置 客户已经上门  20170524
    				if(user.getUserId().equals(distributeCustomer.getBlConsultor())){   					
    					CustomerDynamicStatus dynamicStatus_come = new CustomerDynamicStatus();
            			dynamicStatus_come.setDynamicStatusType(CustomerEventType.VISITCOME_CUSTOMER);
            			dynamicStatus_come.setDescription("客户已上门");
            			if(distributeCustomer.getResEntrance()!=null){
            		      	dynamicStatus_come.setResEntrance(distributeCustomer.getResEntrance());
            			}
            			dynamicStatus_come.setStatusNum(1);
            			dynamicStatus_come.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
            			customerEventService.addCustomerDynameicStatus(distributeCustomer, dynamicStatus_come, user);
            			
            			
            			distributeCustomer.setVisitType(VisitType.PARENT_COME);
            			distributeCustomer.setVisitComeDate(new Date());
            			distributeCustomer.setBlConsultor(null);//获取完以后清空
    				}
    				
    				
    				
    				
    				
        			        			
        		}else{
        			msgBuffer.append(distributeCustomer.getName()+",");
        		}
			}
			String msg = msgBuffer.toString();
			if(StringUtils.isNotBlank(msg)){
				response.setResultCode(-1);
				response.setResultMessage("无法获取"+msg+"客户的资源");
			}
		}else{
			//从其他的资源池获得
			//计算资源池的容量
			StringBuilder msgBuffer = new StringBuilder();
			//deliverTarget为当前登录者的id
			String deliverTarget = user.getUserId();
			response = resourcePoolService.getResourcePoolVolume(cusIdArray.length, deliverTarget);
			if (response.getResultCode() == 0) {
				// 符合要求
				for (String id : cusIdArray) {
					Customer customerOtherPool = this.findById(id);
					//先进行判断是否能够满足获取的条件
					if (customerOtherPool.getDeliverType() != null // 旧数据类型不为空																	
							&& customerOtherPool.getDeliverType().equals(CustomerDeliverType.PERSONAL_POOL)// 并且也在个人资源池
							&& (!deliverTarget.equals(customerOtherPool.getDeliverTarget()))// 跟进人不为空并且不等于当前跟进人
							&& customerOtherPool.getDealStatus().equals(CustomerDealStatus.FOLLOWING)// 旧数据状态不是新增跟待跟进	// (旧数据是跟进中的，只要不是跟进中的都可以获取的)																									
					) {
						//记录无法获取的客户名称
						msgBuffer.append(customerOtherPool.getName() + ",");
					} else {
						//可以进行获取资源
						String before_deliverTarget = customerOtherPool.getDeliverTarget();
						
						//#1728 计算客户分配时间 20171110 duanmenrun
	        			CustomerAllocateObtain  customerAllocateObtain = customerAllocateObtainService.findAllocateObtainByCustomerId(id);
	        			if(customerAllocateObtain != null && customerAllocateObtain.getStatus()==0) {
	            			customerAllocateObtain.setObtainTime(DateTools.getCurrentDateTime());
	            			customerAllocateObtain.setStatus(1);
	            			customerAllocateObtainService.saveOrUpdateCustomerAllocateObtain(customerAllocateObtain);
	        			}
						
						response = this.distributeCustomer(customerOtherPool, user,poolType,workbrenchType);
						if(response.getResultCode() == 0){							
		        			CustomerDynamicStatus dynamicStatus2 = new CustomerDynamicStatus();
		        			dynamicStatus2.setDynamicStatusType(CustomerEventType.GET);
		        			dynamicStatus2.setDescription("从公共资源池获取客户");
		        			if(customerOtherPool.getResEntrance()!=null){
		        		      	dynamicStatus2.setResEntrance(customerOtherPool.getResEntrance());
		        			}
		        			dynamicStatus2.setStatusNum(1);
		        			dynamicStatus2.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
		        			customerEventService.addCustomerDynameicStatus(customerOtherPool, dynamicStatus2, user);
							
						}	
						
						if(StringUtils.isNotBlank(deliverTarget) && deliverTarget.equals(user.getUserId()) ){
							DeliverTargetChangeRecord record = new DeliverTargetChangeRecord();
							record.setCustomerId(id);
							record.setPreviousTarget(before_deliverTarget);
							record.setCurrentTarget(user.getUserId());
							record.setRemark("从公共资源池获取客户变更跟进人记录");
							record.setCreateUserId(user.getUserId());
							record.setCreateTime(DateTools.getCurrentDateTime());
							String recordId = deliverTargetChangeService.saveDeliverTargetChangeRecord(record);

		        			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
		        			dynamicStatus.setDynamicStatusType(CustomerEventType.GET_RESOURCEPOOL);
		        			dynamicStatus.setDescription("从公共资源池获取客户变更跟进人记录");
		        			if(customerOtherPool.getResEntrance()!=null){
		        		      	dynamicStatus.setResEntrance(customerOtherPool.getResEntrance());
		        			}
		        			dynamicStatus.setStatusNum(1);
		        			dynamicStatus.setTableName("delivertarget_change_record");
		        			dynamicStatus.setTableId(recordId);
		        			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
		        			customerEventService.addCustomerDynameicStatus(customerOtherPool, dynamicStatus, user);
							
							
						}
					}
				}
				//可能部分人能获取成功，如果msg不为空则有部分客户无法获取
				String msg = msgBuffer.toString();
				if(StringUtils.isNotBlank(msg)){
					response.setResultCode(-1);
					response.setResultMessage("客户"+msg.substring(0,msg.length()-1)+"已被人获取跟进。");
				}
			}//else 报错
			
		}
		
		return response;
	}
	
	/**
	 * 改进客户状态，改变跟进状态
	 */
	@Override
	public Response distributeCustomer(Customer customer,User user,String poolType,String workbrenchType){
		Response response = new Response();
		String currentTime=DateTools.getCurrentDateTime();
		String roleSign = userService.getUserRoleSign(user.getUserId());
		List<String> roleSignList = userService.getUserRoleSignFromRole(user.getUserId());
		if(roleSignList==null) roleSignList = new ArrayList<>();
		
		Boolean isWHZY = userService.isUserRoleCode(user.getUserId(), RoleCode.OUTCALL_SPEC);
		Boolean isWLZY = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_SPEC);
		Boolean isZXS = userService.isUserRoleCode(user.getUserId(), RoleCode.CONSULTOR);
		Boolean isWLZG = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_MANAGER);
		Boolean isWLJL = userService.isUserRoleCode(user.getUserId(), RoleCode.INTERNET_MANAGER);
		Boolean isZXZG = userService.isUserRoleCode(user.getUserId(), RoleCode.CONSULTOR_DIRECTOR);
		isZXS = isZXS||isZXZG; 
		
		//获取客户的时候判读 当前操作人的职位 如果是外呼或者网络专员 则记录上工序		
		if(isWLZG||isWLJL||isWHZY||isWLZY||RoleSign.WHZY.getValue().equalsIgnoreCase(roleSign)||RoleSign.WLZY.getValue().equalsIgnoreCase(roleSign)
				||(roleSignList!=null &&roleSignList.contains(RoleSign.WHZY.getValue().toLowerCase()))
				||(roleSignList!=null &&roleSignList.contains(RoleSign.WLZY.getValue().toLowerCase()))){
		    customer.setBeforeDeliverTarget(user.getUserId());
		    customer.setBeforeDeliverTime(currentTime);
		}	
		if(isZXS){
			Organization belongCampus = userService.getBelongCampusByUserId(user.getUserId());
			if(belongCampus!=null){
				customer.setBlCampusId(belongCampus);
				customer.setBlSchool(belongCampus.getId());				
			}
		}
		
		customer.setModifyTime(currentTime);
		customer.setModifyUserId(user.getUserId());
		customer.setDealStatus(CustomerDealStatus.FOLLOWING);
		customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
		customer.setDeliverTarget(user.getUserId());
		customer.setDeliverTargetName(user.getName());
//		customer.setLastDeliverName(user.getName());
		customer.setGetCustomerTime(currentTime);
		
		
		//新需求 处理资源入口的变更问题   20170810xiaojinwang
		if("person".equals(poolType)){
			//是 从个人池获取
			
			
			
//			DataDict cusTypeD = customer.getCusType();
//			DataDict cusOrgD = customer.getCusOrg();
//			if (cusTypeD != null && cusOrgD != null && cusTypeD.getName().equals("数据库数据")
//					&& cusOrgD.getName().equals("数据库数据")) {
			if(customer.getResEntrance()==null || customer.getCustomerActive() ==CustomerActive.INACTIVE){				
				//资源入口条件更改：满足解锁条件才能修改   客户的资源入口为空或者客户是不活跃的 
				//如果分配给校区营运主任，校区营运主任再调配给咨询师，咨询师获取资源后，才标记入口为咨询师陌拜；
				//如果分配给TMK主管，TMK主管调配给外呼专员，外呼专员获取资源后入口标记为TMK
				String resEntrance = null;
				DataDict resEntrancOld = customer.getResEntrance();
				if(workbrenchType.equals(WorkbrenchType.ON_LINE.getValue())){
					resEntrance = ResEntranceType.ON_LINE.getValue();
				}
				if(workbrenchType.equals(WorkbrenchType.CALL_OUT.getValue())){
					resEntrance = ResEntranceType.CALL_OUT.getValue();
				}
				if(workbrenchType.equals(WorkbrenchType.OUTCALL.getValue())){
					resEntrance = ResEntranceType.OUTCALL.getValue();
				}
				if(resEntrance!=null){
					customer.setPreEntrance(new DataDict(resEntrance));
					customer.setResEntrance(new DataDict(resEntrance));	
				}
				if(resEntrancOld!=null && resEntrance!=null&& !resEntrancOld.getId().equals(resEntrance)){
					//原来的资源量resEntrancOld减1			
					CustomerDynamicStatus dynamicStatus2 = new CustomerDynamicStatus();
					dynamicStatus2.setDynamicStatusType(CustomerEventType.RESOURCES);
					dynamicStatus2.setDescription("原来的资源量减少一");
					dynamicStatus2.setResEntrance(new DataDict(resEntrancOld.getId()));
					dynamicStatus2.setStatusNum(-1);
					dynamicStatus2.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
					customerEventService.addCustomerDynameicStatus(customer, dynamicStatus2, user);
				}

			}			
		}
		//现在的资源入口
		customerDao.save(customer);  		
	

		if(customer.getId()!=null){
			response.setResultCode(0);
		}else{
			response.setResultCode(-1);
		}		
		return response;
	}
	
	//customerVo要封装四种参数  deliverTarget dealStatus  deliverType 资源入口 res_Entrance  
	@Override
	public Response allocateCustomer(CustomerVo customerVo,String customerId,String allocateUserId,String allocateReason,CustomerEventType eventType) {
		
		Response response = new Response();
		
		//分配客户   设置beforeDeliverTarget deliverTarget
		//根据customerId获取customer
		String deliverTarget = customerVo.getDeliverTarget();
		
		Response res = resourcePoolService.getResourcePoolVolume(1, deliverTarget);
		if(res.getResultCode() != 0) {
			return res;
		}
		
		//客户处理状态
		CustomerDealStatus dealStatus = customerVo.getDealStatus();
		Customer customer = customerDao.findById(customerId);
		
		CustomerActive active = customer.getCustomerActive();
		
		//分配目标要变动才行
		String currentTime = DateTools.getCurrentDateTime();
		if(StringUtils.isNotBlank(deliverTarget)&& (!deliverTarget.equals(customer.getDeliverTarget())
				||customerVo.getFromTransfer())){//只有转介绍的情况下可以设置以前的负责人 #1673
			//beforeDeliverTarget 上工序是用来专门存放  外呼 网络  跟进过的客户  不做  当前跟进人 使用
//			customer.setBeforeDeliverTarget(customer.getDeliverTarget());
//			customer.setBeforeDeliverTime(customer.getDeliverTime());
			customer.setDeliverTarget(deliverTarget);
			customer.setDealStatus(dealStatus);
			customer.setDeliverTime(currentTime);
			customer.setDeliverType(customerVo.getDeliverType());
			if(customerVo.getResEntranceId()!=null){
				//说明参数中封装了新的资源入口，则需要设置新的资源入口
				customer.setResEntrance(new DataDict(customerVo.getResEntranceId()));
			}
			//customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
			//根据allocateUserId 获取user  最新分配人 
			User allocateUser  = userDao.findById(allocateUserId);
			customer.setLastDeliverName(allocateUser.getName());
			if(active==CustomerActive.INACTIVE){
				customer.setCustomerActive(CustomerActive.NEW_CUSTOMER);
				
				//防止无效客户重新激活后，当晚被定时任务重新设置为无效duanmenrun  20171102
				CustomerFolowup followupRecord = new CustomerFolowup();
				followupRecord.setCustomer(customer);
				followupRecord.setRemark("无效客户重新激活，添加系统跟进记录");
				followupRecord.setCreateTime(currentTime);
				followupRecord.setCreateUser(allocateUser);
				followupRecord.setAppointmentType(AppointmentType.APPOINTMENT_REACTIVATE);
				customerFolowupDao.save(followupRecord);
				
				//如果客户是不活跃的情况下，将客户的资源入口标记为直访 20170703 #1099需求
				customer.setResEntrance(new DataDict(ResEntranceType.DIRECT_VISIT.getValue()));
			}
			
			// xiaojinwang 20170524 客户分配　客户标记上门
			if(customerVo.getVisitCome()!=null && customerVo.getVisitCome()==true){
				customer.setBlConsultor(deliverTarget);
			}
			
			if(CustomerEventType.APPOINTMENT_ONLINEDELIVER == eventType
					&& StringUtils.isBlank(customer.getFirstCampus())
					&& CustomerDeliverType.PERSONAL_POOL == customerVo.getDeliverType()
					&& StringUtils.isNotBlank(deliverTarget)){
				String organizationId=userService.loadUserById(customer.getDeliverTarget()).getOrganizationId();
				Organization organization = userService.getBelongCampusByOrgId(organizationId);
				if(organization!=null){
					customer.setFirstCampus(organization.getId());
				}
			}
			
			
			customerDao.save(customer);
			
			//判断负责人是否是营主，推送消息
	    	if(StringUtil.isNotBlank(customer.getDeliverTarget()) && StringUtil.isNotBlank(customer.getId())) {
	    		this.judgeUserSendMsg(customer.getDeliverTarget(),userService.getCurrentLoginUserId());
	    	}
			//customerEventService.saveCustomerDynamicStatus(customerId, CustomerEventType.DELIVER, description);
			//添加分配人变动记录
			DeliverTargetChangeRecord record = new DeliverTargetChangeRecord();
			record.setCustomerId(customerId);
			record.setPreviousTarget(customer.getDeliverTarget());
			record.setCurrentTarget(customerVo.getDeliverTarget());
			record.setRemark(allocateReason);
			record.setCreateUserId(allocateUserId);
			record.setCreateTime(currentTime);
			String recordId = deliverTargetChangeService.saveDeliverTargetChangeRecord(record);
			
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(eventType);
			dynamicStatus.setDescription(allocateReason);
			if(customer.getResEntrance()!=null){
			   dynamicStatus.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setTableName("delivertarget_change_record");
			dynamicStatus.setTableId(recordId);
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, allocateUser);	
			
			if(active==CustomerActive.INACTIVE){
				CustomerDynamicStatus dynamicStatus_active = new CustomerDynamicStatus();
				dynamicStatus_active.setDynamicStatusType(CustomerEventType.ACTIVECUSTOMER);
				dynamicStatus_active.setDescription("非活跃客户重新分配激活");
				if(customer.getPreEntrance()!=null){
					dynamicStatus_active.setResEntrance(customer.getPreEntrance());
				}
				dynamicStatus_active.setStatusNum(1);
				dynamicStatus_active.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
	            customerEventService.addCustomerDynameicStatus(customer, dynamicStatus_active, allocateUser);
			}
				
			
			
		}else{
			response.setResultCode(-1);
			response.setResultMessage("分配客户对象不能为空或者不能跟原来对象一样");
		}
				
		return response;
	}
	
	
	@Override
	public DataPackage getFollowupCustomers(FollowupCustomerVo vo, DataPackage dataPackage, String workbrenchType) {
		//跟进客户管理--多条件获取 跟进客户列表
		StringBuilder cusSql=new StringBuilder(1024);	
		User currentUser = userService.getCurrentLoginUser();
		cusSql.append(" select c.id as cusId,c.name as cusName,c.CONTACT as contact,c.RES_ENTRANCE as resEntrance,c.GET_CUSTOMER_TIME as getCustomerTime,");
		cusSql.append(" c.CUS_TYPE as cusType,c.CUS_ORG as cusOrg,c.LAST_DELIVER_NAME as lastDeliverName, ");
		cusSql.append(" c.CREATE_USER_ID as userId,c.REMARK as lastRemark,c.DELIVER_TIME as deliverTime,c.DEAL_STATUS as dealStatus,");
		cusSql.append(" c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer,c.NEXT_FOLLOWUP_TIME as nextFollowupDate, ");
		cusSql.append(" c.LAST_FOLLOW_UP_TIME as lastFollowUpTime,c.APPOINTMENT_DATE as appointmentDate ,");
		cusSql.append(" c.CREATE_TIME as createTime ,c.VISIT_TYPE as visitType,c.INTENTION_CAMPUS_ID as intentionCampusId ");
		
		cusSql.append(" , d.`NAME` as resEntranceName ,dd.`NAME` as cusTypeName,ddd.`NAME` as cusOrgName,dddd.`NAME` as intentionName ");
		cusSql.append(" from customer c ");
		
		cusSql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID "); 
		cusSql.append(" LEFT JOIN data_dict dd on c.CUS_TYPE = dd.ID "); 
		cusSql.append(" LEFT JOIN data_dict ddd on c.CUS_ORG = ddd.ID ");
		cusSql.append(" LEFT JOIN data_dict dddd on c.INTENTION_OF_THE_CUSTOMER = dddd.ID ");
		

        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", currentUser.getUserId());
        
		
		cusSql.append(" where 1=1 ");	
		//区分 不同的资源入口
		
		Organization organization = userService.getUserMainDeptByUserId(currentUser.getUserId());
		String orgId=organization.getId();
		
		//cusSql.append(" and ( c.RES_ENTRANCE ='"+workbrenchType+"' or c.PRE_ENTRANCE ='"+workbrenchType+"' ) ");
		if(workbrenchType.equals(ResEntranceType.OUTCALL.getValue())|| workbrenchType.equals(ResEntranceType.ON_LINE.getValue())){
			//客户的资源入口或者预资源入口是外呼或者网络
			//如果是外呼或者网络  跟进列表的客户 
			//可以分配给他们并且客户的状态是跟进中 
			//可以是 前任分配对象是他们  现在分配对象是咨询师在跟进中	
//			cusSql.append(" and ( c.DELEVER_TARGET ='"+currentUser.getUserId()+"' or c.BEFOR_DELIVER_TARGET='"+currentUser.getUserId()+"')");
//			cusSql.append(" and ( c.DEAL_STATUS = 'FOLLOWING' or c.DEAL_STATUS = 'NEW' or c.DEAL_STATUS = 'STAY_FOLLOW' ) ");
			
//			cusSql.append(" and (( c.DELEVER_TARGET = :userId and c.DEAL_STATUS ='"+CustomerDealStatus.FOLLOWING.getValue()+"' )");
//			cusSql.append(" or ( c.BEFOR_DELIVER_TARGET= :userId and ( c.DEAL_STATUS = 'FOLLOWING' or c.DEAL_STATUS = 'NEW' or c.DEAL_STATUS = 'STAY_FOLLOW' ) ) )");
			
			Map<String, String> map_id = getNetWorkGroupOUTCALLOrgId();

			if (map_id.get("network") != null && orgId.equals(map_id.get("network"))) {
				//网络不做处理
				cusSql.append(" and (( c.DELEVER_TARGET = :userId and c.DEAL_STATUS ='"+CustomerDealStatus.FOLLOWING.getValue()+"' )");
				cusSql.append(" or ( c.BEFOR_DELIVER_TARGET= :userId and ( c.DEAL_STATUS = 'FOLLOWING' or c.DEAL_STATUS = 'NEW' or c.DEAL_STATUS = 'STAY_FOLLOW' ) ) )");
			} else if (map_id.get("outcall") != null && orgId.equals(map_id.get("outcall"))) {
				//外呼限制在所属分公司
				organization = userService.getBelongBranchByUserId(currentUser.getUserId());
				String orgLevel = organization.getOrgLevel();
				params.put("orgLevel",  orgLevel+"%");
				cusSql.append(
						" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
				
				cusSql.append(" and (( c.DELEVER_TARGET = :userId and c.DEAL_STATUS ='"+CustomerDealStatus.FOLLOWING.getValue()+"' )");
				cusSql.append(" or ( c.BEFOR_DELIVER_TARGET= :userId and ( c.DEAL_STATUS = 'FOLLOWING' or c.DEAL_STATUS = 'NEW' or c.DEAL_STATUS = 'STAY_FOLLOW' ) ) )");
			} else {
				
				organization = userService.getBelongCampusByUserId(currentUser.getUserId());
				String orgLevel = organization.getOrgLevel();
				params.put("orgLevel",  orgLevel+"%");
				
				
//				params.put("orgId", orgId);
				cusSql.append(" and c.DELEVER_TARGET = :userId ");
				cusSql.append(" and (c.DEAL_STATUS = 'FOLLOWING' or c.DEAL_STATUS = 'NEW') ");
				cusSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
			}
			
			
		}
		if(workbrenchType.equals(ResEntranceType.CALL_OUT.getValue())){
			//如果是咨询师
			
			Organization blCampus = userService.getBelongCampusByOrgId(orgId);
			String orgLevel = blCampus.getOrgLevel();
			params.put("orgLevel",  orgLevel+"%");
			cusSql.append(" and c.DELEVER_TARGET = :userId ");
			cusSql.append(" and (c.DEAL_STATUS = 'FOLLOWING' or c.DEAL_STATUS = 'NEW') ");
			cusSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
		}
		// 查询姓名
		if (StringUtils.isNotBlank(vo.getCusName())) {
			cusSql.append(" and c.NAME like :cusName ");
			params.put("cusName", "%" + vo.getCusName() + "%");
		}
		// 联系方式
		if (StringUtils.isNotBlank(vo.getContact())) {
			cusSql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(vo.getResEntranceId())) {
			cusSql.append(" and c.RES_ENTRANCE= :resEntranceId ");
			params.put("resEntranceId", vo.getResEntranceId());
		}
		// 资源大类
		if (StringUtils.isNotBlank(vo.getCusOrg())) {
			cusSql.append(" and c.CUS_ORG= :cusOrg ");
			params.put("cusOrg", vo.getCusOrg());
		}
		// 资源细分
		if (StringUtils.isNotBlank(vo.getCusType())) {
			cusSql.append(" and c.CUS_TYPE = :cusType ");
			params.put("cusType", vo.getCusType());
		}
		
		//去掉 2016-11-17
//		// 处理状态
//		if (vo.getDealStatus() != null) {
//			if (StringUtils.isNotBlank(vo.getDealStatus().getValue())) {
//				cusSql.append(" and c.DEAL_STATUS = '" + vo.getDealStatus().getValue() + "' ");
//			}
//		}
		// 是否上门
		if (StringUtils.isNotBlank(vo.getIsAppointCome())) {
			// 如果是预约上门
			if ("1".equals(vo.getIsAppointCome())) {
				cusSql.append("and (c.VISIT_TYPE is not NULL && c.VISIT_TYPE <>'NOT_COME' )");
			}
			// 非预约上门
			if ("0".equals(vo.getIsAppointCome())) {
				cusSql.append("and (c.VISIT_TYPE is NULL || c.VISIT_TYPE = 'NOT_COME' )");
			}
		}
		// 意向度
		if (StringUtils.isNotEmpty(vo.getIntentionOfTheCustomerId())) {
			cusSql.append(" and c.INTENTION_OF_THE_CUSTOMER = :intentionOfTheCustomerId ");
			params.put("intentionOfTheCustomerId", vo.getIntentionOfTheCustomerId());
		}
		// 获取时间    修改需求 20170319 获取时间修改为最新跟进时间
		if(StringUtils.isNotBlank(vo.getFollowupBeginDate())){
			//cusSql.append(" and c.GET_CUSTOMER_TIME >= '"+vo.getFollowupBeginDate()+" 00:00:00.000' ");
			cusSql.append(" and c.LAST_FOLLOW_UP_TIME >= :followupBeginDate ");
			params.put("followupBeginDate", vo.getFollowupBeginDate()+" 00:00:00");
		}			
		if(StringUtils.isNotBlank(vo.getFollowupEndDate())){
			//cusSql.append(" and c.GET_CUSTOMER_TIME <= '"+vo.getFollowupEndDate()+" 23:59:59.999' ");
			cusSql.append(" and c.LAST_FOLLOW_UP_TIME <= :followupEndDate ");
            params.put("followupEndDate", vo.getFollowupEndDate()+" 23:59:59");
		}
		
		//意向校区
		if(StringUtil.isNotBlank(vo.getIntentionCampusId())){
			cusSql.append(" and c.INTENTION_CAMPUS_ID = '"+vo.getIntentionCampusId()+"' ");
		}
		
		
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
		} 
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
		//dataPackage.setDatas(list);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));	
        
		//将查询结果转换为vo		
		List<FollowupCustomerVo> voList =new ArrayList<FollowupCustomerVo>();
		FollowupCustomerVo fVo = null;
		for(Map<Object,Object> tmaps : list){
			Map<String,String> maps = (Map)tmaps;
		    fVo = new FollowupCustomerVo();
			String cusId =maps.get("cusId");
			fVo.setCusId(cusId!=null ? cusId:"");
			fVo.setCusName(maps.get("cusName")!=null? maps.get("cusName"):"");
			//联系方式
			fVo.setContact(maps.get("contact")!=null? maps.get("contact"):"");
			//客户创建时间
			if(maps.get("createTime")!=null){
				Object createTime = maps.get("createTime");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date=format.parse(createTime.toString());					
					fVo.setCreateTime(formatDate.format(date));
				} catch (ParseException e) {
					fVo.setCreateTime("");
				}
			}else{
				fVo.setCreateTime("");
			}		
			fVo.setGetCustomerTime(maps.get("getCustomerTime"));
						
			//设置资源入口
			fVo.setResEntranceId(maps.get("resEntrance"));
			fVo.setResEntranceName(maps.get("resEntranceName"));
			
			//资源来源 
			fVo.setCusType(maps.get("cusTypeName"));		
			//资源细分
			fVo.setCusOrg(maps.get("cusOrgName"));
			
			//设置意向度
			fVo.setIntentionOfTheCustomerId(maps.get("intentionOfTheCustomer"));
			fVo.setIntentionOfTheCustomerName(maps.get("intentionName"));
			//设置意向校区
			if(StringUtils.isNotBlank(maps.get("intentionCampusId"))){
				fVo.setIntentionCampusName(organizationService.findById(maps.get("intentionCampusId")).getName());
			}
			
			//设置跟进状态
			if(maps.get("dealStatus")!=null){
				fVo.setDealStatus(CustomerDealStatus.valueOf(maps.get("dealStatus")));
			}
			
			
			//去掉 2016-11-17
//			//处理状态			
//			if (maps.get("dealStatus") != null) {
//				String dealStatus = maps.get("dealStatus");
//				fVo.setDealStatus(CustomerDealStatus.valueOf(dealStatus));
//				fVo.setDealStatusName(CustomerDealStatus.valueOf(dealStatus) == null ? ""
//						: CustomerDealStatus.valueOf(dealStatus).getName());
//			}
			
			//设置最后分配人名称
			fVo.setLastDeliverName(maps.get("lastDeliverName")!=null ? maps.get("lastDeliverName"):"");
			//设置最后分配时间  用于计算总跟进天数
			fVo.setDeliverTime(maps.get("deliverTime")!=null ? maps.get("deliverTime"):"");
            //用于计算距上次跟进天数
			fVo.setLastFollowUpTime(maps.get("lastFollowUpTime")!=null ? maps.get("lastFollowUpTime"):"");
	

            Map<String, Object> param = Maps.newHashMap();
            param.put("cusId", cusId);
            param.put("appointmentType", AppointmentType.FOLLOW_UP.getValue());
			//最新跟进记录
			StringBuilder query =new StringBuilder(" select cf.remark as remark from customer_folowup cf where appointment_type = :appointmentType ");
			query.append(" and cf.customer_id = :cusId ORDER BY cf.create_time desc LIMIT 1 ");			
			List<Map<Object, Object>> customerFolowups=customerFolowupDao.findMapBySql(query.toString(),param);
			if (customerFolowups.size() > 0) {
				fVo.setLastRemark(customerFolowups.get(0).get("remark")!=null?customerFolowups.get(0).get("remark").toString():"");
			}
			//是否上门和最新跟进是否预约上门没有关系 是只要客户上门登记以后都是上门 
			//根据cusId获取Customer
//			Customer customer = customerDao.findById(cusId);
//			if(customer.getVisitType()==null||(customer.getVisitType() != null && customer.getVisitType().equals(VisitType.NOT_COME))){
//				fVo.setIsAppointCome("否");
//			}else{
//				fVo.setIsAppointCome("是");
//			}
			String visitType = maps.get("visitType");
			if(visitType==null ||(visitType!=null && visitType.equals(VisitType.NOT_COME.getValue()))){
				fVo.setIsAppointCome("否");
			}else{
				fVo.setIsAppointCome("是");
			}
			
			//下次跟进时间
			fVo.setNextFollowupDate(maps.get("nextFollowupDate")!=null?maps.get("nextFollowupDate"):"");
			//预约上门时间
			fVo.setAppointmentDate(maps.get("appointmentDate")!=null?maps.get("appointmentDate"):"");
		    fVo.setRemark(maps.get("lastRemark")!=null?maps.get("lastRemark"):"");
	        voList.add(fVo);
			
		}
		//如果是网络跟进客户 xiaojinwang 20170301
		if(workbrenchType.equals(ResEntranceType.ON_LINE.getValue())){
			for(FollowupCustomerVo foCustomerVo:voList){			
				List<CustomerFolowup> fList= customerFolowupService.findRecordbyUserIdAndCustomerId(currentUser.getUserId(), foCustomerVo.getCusId(),null);
				if(fList!=null && fList.size()>0){
					CustomerFolowup customerFolowup = fList.get(0);					
					foCustomerVo.setIsAllocate("是");				
					foCustomerVo.setAllocateTargetName(customerFolowup.getAgencyUser()!=null?customerFolowup.getAgencyUser().getName():"-");
					foCustomerVo.setAllocateTime(customerFolowup.getCreateTime());
				}else{
					foCustomerVo.setIsAllocate("否");
					foCustomerVo.setAllocateTargetName("-");
					foCustomerVo.setAllocateTime("-");
				}
			}
		}
		
		
		dataPackage.setDatas(voList);
        return dataPackage;
	}
	
	
	@Override
	public DataPackage getAllCustomers(CustomerVo vo, DataPackage dataPackage) {
		//客户管理 --客户列表
		
		//TODO 
		long startTime=System.currentTimeMillis();   //获取开始时间
		StringBuilder cusSql=new StringBuilder(512);	
		cusSql.append(" select c.id as cusId,c.name as cusName,c.CONTACT as contact,c.RES_ENTRANCE as resEntrance,c.CREATE_TIME as createTime,");
		cusSql.append(" c.CUS_TYPE as cusType,c.CUS_ORG as cusOrg,c.CREATE_USER_ID as userId,c.BL_SCHOOL as blCampusId,c.LAST_FOLLOW_UP_TIME as lastFollowUpTime,");
		cusSql.append(" c.DELIVER_TYPE as deliverType,c.DELEVER_TARGET as deliverTarget, c.BL_BRANCH as branchId,c.DEAL_STATUS as dealStatus ");
		cusSql.append(" , d.`NAME` as resEntranceName ,dd.`NAME` as cusTypeName,ddd.`NAME` as cusOrgName, o.`name` as blCampusName,oo.`name` as branchName ");
		cusSql.append(" ,u.`NAME` as userName "); 
		cusSql.append(" ,og.`NAME` as deptName ");
		cusSql.append(" ,(select name from organization where id = c.FIRST_CAMPUS ) as firstCampusName");
		cusSql.append(" from customer c ");
		
		cusSql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID "); 
		cusSql.append(" LEFT JOIN data_dict dd on c.CUS_TYPE = dd.ID "); 
		cusSql.append(" LEFT JOIN data_dict ddd on c.CUS_ORG = ddd.ID ");

		cusSql.append(" LEFT JOIN organization o on c.BL_SCHOOL = o.id ");
		cusSql.append(" LEFT JOIN organization oo on o.parentID = oo.id ");
		cusSql.append(" LEFT JOIN `user` u on c.CREATE_USER_ID = u.USER_ID ");
		
		cusSql.append(" LEFT JOIN user_dept_job udj on ( c.CREATE_USER_ID = udj.USER_ID and udj.isMajorRole=0 ) ");
		cusSql.append(" LEFT JOIN organization og on og.id=udj.DEPT_ID ");
		
		//cusSql.append(" LEFT JOIN customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
		//cusSql.append(" LEFT JOIN student s on s.id=csr.STUDENT_ID where 1=1 ");
		cusSql.append(" where 1=1 ");
		//cusSql.append(" and c.id in ('CUS0000262965','CUS0000262964','CUS0000031282','CUS0000262885','CUS0000262887','CUS0000029895') ");
		//12个查询条件
		// 查询姓名
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(vo.getName())) {
			cusSql.append(" and c.NAME like :name ");
			params.put("name", "%"+vo.getName()+"%");
		}
		// 联系方式
		if (StringUtils.isNotBlank(vo.getContact())) {
			cusSql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(vo.getResEntranceId())) {
			cusSql.append(" and c.RES_ENTRANCE= :resEntranceId ");
			params.put("resEntranceId", vo.getResEntranceId());
		}
		// 资源大类
		if (StringUtils.isNotBlank(vo.getCusOrg())) {
			if(vo.getCusOrg().equals("NULL")){
				cusSql.append(" and c.CUS_ORG is NULL ");
			}else{
				cusSql.append(" and c.CUS_ORG= :cusOrg ");
				params.put("cusOrg", vo.getCusOrg());
			}
			
		}
		// 资源细分
		if (StringUtils.isNotBlank(vo.getCusType())) {
			if(vo.getCusType().equals("NULL")){
				cusSql.append(" and c.CUS_TYPE is NULL ");
			}else{
				cusSql.append(" and c.CUS_TYPE = :cusType ");
				params.put("cusType", vo.getCusType());
			}
			
		}
		//录入者 
        if(StringUtil.isNotBlank(vo.getCreateUserName())){
        	cusSql.append(" and (c.CREATE_USER_ID in (select user_id from user where name like :createUserName ) )");
        	params.put("createUserName", "%"+vo.getCreateUserName()+"%");
        }
		//录入者部门
		if(StringUtil.isNotBlank(vo.getCreateUserDept())){
			cusSql.append(" and (c.CREATE_USER_ID in (select udj.USER_ID from user_dept_job udj LEFT JOIN organization o on o.id =udj.DEPT_ID where o.name LIKE :createUserDept ) )");
			params.put("createUserDept", "%"+vo.getCreateUserDept()+"%");
		}		
		//跟进人
		if(StringUtils.isNotBlank(vo.getDeliverTarget())){
			cusSql.append("and c.DELEVER_TARGET = :deliverTarget ");
			params.put("deliverTarget", vo.getDeliverTarget());
		}
		if(StringUtils.isNotBlank(vo.getDeliverTargetName())){
			//通过getDeliverTargetName 获取 DeliverTarget						
			cusSql.append(" and (c.DELEVER_TARGET in (select user_id from user where name like :deliverTargetName ) or c.DELEVER_TARGET in (select id from organization  where name like :deliverTargetName ) ) ");					
			params.put("deliverTargetName", "%"+vo.getDeliverTargetName()+"%");
		}		
		//选择分公司
        if(StringUtil.isNotBlank(vo.getBrenchId())){
        	cusSql.append(" and (c.BL_SCHOOL in( select id from organization where organization.parentID = :brenchId ) or c.BL_SCHOOL= :brenchId )");
        	params.put("brenchId", vo.getBrenchId());
        }
        
        //选择首次分配校区
  		if(StringUtil.isNotBlank(vo.getFirstCampus())){
  			cusSql.append(" and c.FIRST_CAMPUS = :firstCampus ");
              params.put("firstCampus", vo.getFirstCampus());
  		}
        
		//选择校区
		if(StringUtil.isNotBlank(vo.getBlCampusId())){
			cusSql.append(" and (c.BL_SCHOOL = :blCampusId or c.DELEVER_TARGET= :blCampusId ) ");
            params.put("blCampusId", vo.getBlCampusId());
		}
		//最新跟进时间
		if(StringUtils.isNotBlank(vo.getFollowupBeginDate())){		
			cusSql.append(" and c.LAST_FOLLOW_UP_TIME >= '"+vo.getFollowupBeginDate()+" 00:00:00.000' ");
		}			
		if(StringUtils.isNotBlank(vo.getFollowupEndDate())){
			cusSql.append(" and c.LAST_FOLLOW_UP_TIME <= '"+vo.getFollowupEndDate()+" 23:59:59.999' ");
		}
		
		//toLastFollowupDays距离上次跟进天数 //
		//例如要查5天没有跟进过的，那么就只查距今天至上一次跟进超过5天的，不包含4天以下的   xiaojinwang 20170810
		//1、未获取的情况下，以分配时间为准DELIVER_TIME
		//2、获取了但是没有跟进时，以获取时间为准 GET_CUSTOMER_TIME
		if(vo.getToLastFollowupDays()!=null && vo.getToLastFollowupDays()!=0){
			cusSql.append(" and (( c.LAST_FOLLOW_UP_TIME is not NULL and DATE_FORMAT(c.LAST_FOLLOW_UP_TIME,'%Y-%m-%d') = :toLastFollowupDays ) or  ");
			cusSql.append(" (c.LAST_FOLLOW_UP_TIME is NULL and c.DEAL_STATUS = 'STAY_FOLLOW' and DATE_FORMAT(c.DELIVER_TIME,'%Y-%m-%d') = :toLastFollowupDays ) or  ");	
			cusSql.append(" (c.LAST_FOLLOW_UP_TIME is NULL and c.DEAL_STATUS = 'FOLLOWING' and DATE_FORMAT(c.GET_CUSTOMER_TIME,'%Y-%m-%d') = :toLastFollowupDays ))  ");	
			params.put("toLastFollowupDays", DateTools.addDateToString(DateTools.getCurrentDate(), -vo.getToLastFollowupDays()));
		}
		
		//录入时间
		if(StringUtils.isNotBlank(vo.getCreateBeginDate())){		
			cusSql.append(" and c.CREATE_TIME >= '"+vo.getCreateBeginDate()+" 00:00:00.000' ");
		}			
		if(StringUtils.isNotBlank(vo.getCreateEndDate())){
			cusSql.append(" and c.CREATE_TIME <= '"+vo.getCreateEndDate()+" 23:59:59.999' ");
		}
		
		//第一份签订合同时间
		if(StringUtil.isNotBlank(vo.getFirstContractBeginTime())||StringUtil.isNotBlank(vo.getFirstContractEndTime())){
			cusSql.append(" and c.id in (select CUSTOMER_ID from contract where 1=1 ");
			if(StringUtil.isNotBlank(vo.getFirstContractBeginTime())){
				cusSql.append(" and CREATE_TIME >= :firstContractBeginTime ");
				params.put("firstContractBeginTime", vo.getFirstContractBeginTime()+" 00:00:00 ");
			}
			if(StringUtil.isNotBlank(vo.getFirstContractEndTime())){
				cusSql.append(" and CREATE_TIME <= :firstContractEndTime ");
				params.put("firstContractEndTime", vo.getFirstContractEndTime()+" 23:59:59 ");
			}
			cusSql.append(" ) ");
		}
		
		//如果这三个条件有一个不为空则关联学生表
		if(StringUtils.isNotBlank(vo.getPointialStuName())||StringUtils.isNotBlank(vo.getPointialStuSchoolId())){
			cusSql.append(" and c.id in ( select cs.CUSTOMER_ID from customer_student_relation cs left join ");
			cusSql.append(" student s on cs.STUDENT_ID = s.ID where 1=1 ");
			//学生姓名
			if(StringUtils.isNotBlank(vo.getPointialStuName())){
				cusSql.append(" and s.NAME like :pointialStuName ");
				params.put("pointialStuName", "%"+vo.getPointialStuName()+"%");
			}
			//选择就读学校  这里传进来的是学校的Id 不是名字
			if(StringUtils.isNotBlank(vo.getPointialStuSchoolId())){
				cusSql.append(" and s.SCHOOL = :pointialStuSchoolId ");
				params.put("pointialStuSchoolId", vo.getPointialStuSchoolId());
			}	
			cusSql.append(" ) ");
		}

		
		
		//客户类型  就是客户的处理状态 dealStatus 	
		if(vo.getDealStatus()!=null){
			cusSql.append(" and c.DEAL_STATUS = :dealStatus ");
			params.put("dealStatus", vo.getDealStatus().getValue());
		}
		
		//客户列表配置权限   xiaojinwang 20160928
		//cusSql.append(roleQLConfigService.getValueResult("客户列表","sql"));
		User user = userService.getCurrentLoginUser();	
		
		Organization organization = userService.getUserMainDeptByUserId(user.getUserId());
		
		String orgId=organization.getId();
        
		//获取集团网络部以及当前校区的外呼部门的id
		Map<String, String> map_id = getNetWorkGroupOUTCALLOrgId();
		if(map_id.get("network")!=null && orgId.equals(map_id.get("network"))){			
			//所有集团网络部的客户列表			
			//Boolean flag_NETWORK_SPEC = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_SPEC);
			Boolean flag_NETWORK_MANAGER = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_MANAGER);
			Boolean flag_INTERNET_MANAGER = userService.isUserRoleCode(user.getUserId(), RoleCode.INTERNET_MANAGER);
			String userIds = null;
			if(flag_NETWORK_MANAGER || flag_INTERNET_MANAGER){
				userIds = getUserIdsByOrgId(orgId);
			}else{
			    userIds = "'"+user.getUserId()+"'";
			}		
			//集团网络主管、网络经理只能看到网络部的所有人录入的客户资源，专员只能看到自己录入的客户资源 2017-04-11
			cusSql.append(" and ( c.CREATE_USER_ID in ("+userIds+") ) ");		
		}else if(map_id.get("outcall")!=null && orgId.equals(map_id.get("outcall"))){
			//该外呼部门的客户列表
			//外呼主管
			Boolean flag_OUTCALL_MANAGER = userService.isUserRoleCode(user.getUserId(), RoleCode.OUTCALL_MANAGER);
			//分公司外呼主管
			Boolean isBWHZG = userService.isUserRoleCode(user.getUserId(), RoleCode.BRANCH_OUTCALL_MANAGER);
			String userIds = null;
			if(flag_OUTCALL_MANAGER||isBWHZG){
				userIds = getUserIdsByOrgId(orgId);
			}else{
				userIds ="'"+user.getUserId()+"'";
			}	
			
			//增加限制
			Organization blBranch = userService.getBelongBranchByUserId(user.getUserId());
			String orgLevel = blBranch.getOrgLevel();
			cusSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE '"+orgLevel+"%' ))");
			
			
			if(StringUtils.isNotBlank(userIds)){
				
				cusSql.append(" and ( c.BEFOR_DELIVER_TARGET in ("+userIds+") or c.DELEVER_TARGET in ("+userIds+") or c.CREATE_USER_ID in ("+userIds+") )");
			}else{
				params.put("orgId", orgId);
				params.put("userId", user.getUserId());
				cusSql.append(" and ( c.BL_SCHOOL in ( select id FROM organization where parentID in (select id from organization where id= :orgId )) or c.BL_SCHOOL in (select id from organization where id= :orgId ) or c.CREATE_USER_ID = :userId ) ");
			}
		}else {
			//网络专员
			Boolean flag_NETWORK_SPEC = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_SPEC);
			//外呼专员
			Boolean flag_OUTCALL_SPEC = userService.isUserRoleCode(user.getUserId(), RoleCode.OUTCALL_SPEC);
			//网络主管 
			Boolean flag_NETWORK_MANAGER = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_MANAGER);
			//网络经理
			Boolean flag_INTERNET_MANAGER = userService.isUserRoleCode(user.getUserId(), RoleCode.INTERNET_MANAGER);
			//外呼主管
			Boolean flag_OUTCALL_MANAGER = userService.isUserRoleCode(user.getUserId(), RoleCode.OUTCALL_MANAGER);
			//分公司外呼主管
			Boolean isBWHZG = userService.isUserRoleCode(user.getUserId(), RoleCode.BRANCH_OUTCALL_MANAGER);
			String userIds = null;
			Boolean flag = false;
			//如果是网络主管 外呼主管 网络经理 分公司外呼主管 获取整个部门的人员id
			if(flag_NETWORK_MANAGER||flag_OUTCALL_MANAGER||flag_INTERNET_MANAGER||isBWHZG){
				userIds = getUserIdsByOrgId(orgId);
				flag = true;
				//如果是外呼专员和网络专员则获取自己
			}else if(flag_NETWORK_SPEC||flag_OUTCALL_SPEC){
				userIds ="'"+user.getUserId()+"'";
				flag = true;
			}
			if(StringUtils.isBlank(userIds)){
			    userIds ="'"+user.getUserId()+"'";
			}
			if(flag_NETWORK_MANAGER||flag_INTERNET_MANAGER||flag_NETWORK_SPEC){
				cusSql.append(" and ( c.CREATE_USER_ID in ("+userIds+") ) ");
			}else{
				//下面情况适用于外呼 以及 非网络主管网络经理 网络专员  然后根据主组织部门的权限来控制的，20170414
				//是否是 以上写死的特定的角色
				if(flag){					
					if(flag_OUTCALL_SPEC||flag_OUTCALL_MANAGER||isBWHZG){
						Organization blBranch = userService.getBelongBranchByUserId(user.getUserId());
						String orgLevel = blBranch.getOrgLevel();
						cusSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE '"+orgLevel+"%' ))");						
					}					
					cusSql.append(" and ( c.BEFOR_DELIVER_TARGET in ("+userIds+") or c.DELEVER_TARGET in ("+userIds+") or c.CREATE_USER_ID in ("+userIds+") )");
				}else{					
					Organization blCampus = userService.getBelongCampusByUserId(user.getUserId());		
				    params.put("orgId", blCampus.getId());
				    params.put("userId", user.getUserId());
					cusSql.append(" and ( c.BL_SCHOOL in ( select id FROM organization where parentID in (:orgId )) or c.BL_SCHOOL in (:orgId ) "
							+ "or ((c.DELEVER_TARGET = :userId or c.CREATE_USER_ID in ("+userIds+")) and (c.BL_SCHOOL in ( select id FROM organization where parentID in (:orgId )) or c.BL_SCHOOL in (:orgId )) ) )");
				}				
			}
			

					
			
		}
		
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" order by c.is_import asc,"+dataPackage.getSidx()+" "+dataPackage.getSord());
			//params.put("orderBy", );
		} 
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
		//dataPackage.setDatas(list);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params, 300));	
        
		//将查询结果转换为vo		
		//vo原来是签单客户列表 后来改成客户列表  仍使用原来的签单vo
		List<SignupCustomerVo> voList =new ArrayList<SignupCustomerVo>();
		StringBuilder studentBuffer = new StringBuilder(" ");
		StringBuilder schoolBuffer = new StringBuilder(" ");
		SignupCustomerVo customerVo = null;
		for(Map<Object,Object> tmaps : list){
			Map<String,String> maps = (Map)tmaps;
			customerVo = new SignupCustomerVo();
			String cusId =maps.get("cusId");
			customerVo.setCusId(cusId!=null ? cusId:null);
			customerVo.setCusName(maps.get("cusName")!=null? maps.get("cusName"):null);
			//联系方式
			customerVo.setContact(maps.get("contact")!=null? maps.get("contact"):null);
			//登记人姓名以及登记人部门
			customerVo.setCreateCustomerUserId(maps.get("userId"));
			customerVo.setCreateUserName(maps.get("userName"));
			customerVo.setCreateUserDept(maps.get("deptName"));			
			customerVo.setResEntranceId(maps.get("resEntrance"));
			customerVo.setResEntranceName(maps.get("resEntranceName"));
			customerVo.setCusType(maps.get("cusType"));
			customerVo.setCusTypeName(maps.get("cusTypeName"));
			customerVo.setCusOrg(maps.get("cusOrg"));
			customerVo.setCusOrgName(maps.get("cusOrgName"));
			Object createTime = maps.get("createTime");
			customerVo.setCreateTime(createTime!=null?createTime.toString():"");
				
			//最新跟进时间
			customerVo.setLastFollowUpTime(maps.get("lastFollowUpTime"));
			//所属校区
			//所属分公司
			customerVo.setBranchName(maps.get("branchName"));			
			customerVo.setBlCampusName(maps.get("blCampusName"));
			//首次分配校区
			customerVo.setFirstCampusName(maps.get("firstCampusName"));
			//跟进人
			if(StringUtil.isNotBlank(maps.get("deliverTarget"))){
				customerVo.setDeliverTarget(maps.get("deliverTarget"));
				if (maps.get("deliverType") != null) {
					if (CustomerDeliverType.PERSONAL_POOL.getValue().equals(maps.get("deliverType"))) {
						User deliverTarget = userService.loadUserById(maps.get("deliverTarget"));
						if(deliverTarget!=null){														
							customerVo.setDeliverTargetName(deliverTarget.getName());
							Organization belongCampus = userService.getBelongCampusByUserId(maps.get("deliverTarget"));
							Organization belongBranch = userService.getBelongBranchByUserId(maps.get("deliverTarget"));
							if(belongCampus!=null && belongCampus.getOrgType()!= OrganizationType.CAMPUS){
								customerVo.setBlCampusName("");
							}
							if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
								customerVo.setBranchName(belongBranch.getName());
							}else{
								customerVo.setBranchName("");
							}
						}else{
							Organization deliverTargetOrg = organizationDao.findById(maps.get("deliverTarget"));
							if(deliverTargetOrg!=null){
								if(deliverTargetOrg.getOrgType()==OrganizationType.CAMPUS){
									Organization belongCampus =userService.getBelongCampusByOrgId(deliverTargetOrg.getId());
									if(belongCampus!=null){
										customerVo.setBlCampusName(belongCampus.getName());
									}else{
										customerVo.setBlCampusName("");
									}
								}else{
									customerVo.setBlCampusName("");
								}
								Organization belongBranch =userService.getBelongBranchByOrgId(deliverTargetOrg.getId());
								if(belongBranch!=null){
									customerVo.setBranchName(belongBranch.getName());
									if(deliverTargetOrg.getName().indexOf("网络")!=-1){
										customerVo.setBranchName("");
									}
								}else{
									customerVo.setBranchName("");
								}
								customerVo.setDeliverTargetName(deliverTargetOrg.getResourcePoolName());
							}
						}
					} else if (CustomerDeliverType.CUSTOMER_RESOURCE_POOL.getValue().equals(maps.get("deliverType"))) {
						Organization deliverTargetOrg = organizationDao.findById(maps.get("deliverTarget"));
						if(deliverTargetOrg!=null){
							if(deliverTargetOrg.getOrgType()==OrganizationType.CAMPUS){
								Organization belongCampus =userService.getBelongCampusByOrgId(deliverTargetOrg.getId());
								if(belongCampus!=null){
									customerVo.setBlCampusName(belongCampus.getName());
								}else{
									customerVo.setBlCampusName("");
								}
							}else{
								customerVo.setBlCampusName("");
							}
							
							Organization belongBranch =userService.getBelongBranchByOrgId(deliverTargetOrg.getId());
							if(belongBranch!=null){
								customerVo.setBranchName(belongBranch.getName());	
								if(deliverTargetOrg.getName().indexOf("网络")!=-1){
									customerVo.setBranchName("");
								}
							}else{
								customerVo.setBranchName("");
							}
							customerVo.setDeliverTargetName(deliverTargetOrg.getResourcePoolName());
						}else{
							User deliverTarget = userService.loadUserById(maps.get("deliverTarget"));
							if(deliverTarget!=null){
								customerVo.setDeliverTargetName(deliverTarget.getName());
								Organization belongCampus = userService.getBelongCampusByUserId(maps.get("deliverTarget"));
								Organization belongBranch = userService.getBelongBranchByUserId(maps.get("deliverTarget"));
								if(belongCampus!=null && belongCampus.getOrgType()!= OrganizationType.CAMPUS){
									customerVo.setBlCampusName("");
								}
								if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
									customerVo.setBranchName(belongBranch.getName());
								}else{
									customerVo.setBranchName("");
								}
							}
						}
					}
				}else{
					User deliverTarget =userService.loadUserById(maps.get("deliverTarget"));
					if(deliverTarget!=null){
						customerVo.setDeliverTargetName(deliverTarget.getName());
						Organization belongCampus = userService.getBelongCampusByUserId(maps.get("deliverTarget"));
						Organization belongBranch = userService.getBelongBranchByUserId(maps.get("deliverTarget"));
						if(belongCampus!=null && belongCampus.getOrgType()!= OrganizationType.CAMPUS){
							customerVo.setBlCampusName("");
						}
						if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
							customerVo.setBranchName(belongBranch.getName());
						}else{
							customerVo.setBranchName("");
						}
					}else{
						Organization deliverTargetOrg = organizationDao.findById(maps.get("deliverTarget"));
						if(deliverTargetOrg!=null){
							if(deliverTargetOrg.getOrgType()==OrganizationType.CAMPUS){
								Organization belongCampus =userService.getBelongCampusByOrgId(deliverTargetOrg.getId());
								if(belongCampus!=null){
									customerVo.setBlCampusName(belongCampus.getName());
								}else{
									customerVo.setBlCampusName("");
								}
							}else{
								customerVo.setBlCampusName("");
							}
							Organization belongBranch =userService.getBelongBranchByOrgId(deliverTargetOrg.getId());
							if(belongBranch!=null){
								customerVo.setBranchName(belongBranch.getName());
								if(deliverTargetOrg.getName().indexOf("网络")!=-1){
									customerVo.setBranchName("");
								}
							}else{
								customerVo.setBranchName("");
							}
							customerVo.setDeliverTargetName(deliverTargetOrg.getResourcePoolName());
						}
					}
				}
			}else{
				//转介绍 根据录入人来判断 INTRODUCE
				if(maps.get("cusType")!=null && "INTRODUCE".equals(maps.get("cusType"))){
					Organization belongCampus = userService.getBelongCampusByUserId(maps.get("userId"));
					Organization belongBranch = userService.getBelongBranchByUserId(maps.get("userId"));
					if(belongCampus!=null && belongCampus.getOrgType()== OrganizationType.CAMPUS){
						customerVo.setBlCampusName(belongCampus.getName());
					}else{
						customerVo.setBlCampusName("");
					}
					if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
						customerVo.setBranchName(belongBranch.getName());
					}else{
						customerVo.setBranchName("");
					}
				}				
			}
			
			
			//客户跟进状态  客户类型
			String dealStatus = maps.get("dealStatus");
			if(dealStatus!=null){
				customerVo.setDealStatusName(CustomerDealStatus.valueOf(dealStatus).getName());
			}
			
			
			//最新跟进记录
			StringBuilder query =new StringBuilder(" select cf.remark as remark from customer_folowup cf where appointment_type ='"+AppointmentType.FOLLOW_UP.getValue()+"' ");
			query.append(" and cf.customer_id = :cusId ORDER BY cf.create_time desc LIMIT 1 ");
			Map<String, Object> param = Maps.newHashMap();
			param.put("cusId", cusId);
			List<Map<Object, Object>> customerFolowups=customerFolowupDao.findMapBySql(query.toString(),param);
			if (customerFolowups.size() > 0) {
				customerVo.setFollowupRemark(customerFolowups.get(0).get("remark")!=null?customerFolowups.get(0).get("remark").toString():"");
			}
			
			
			
			//学生姓名  多个 用逗号,分割开//就读学校 多个 用逗号,分割开 
			String sql ="SELECT s.`NAME` as studentName,ss.`NAME` as schoolName from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID LEFT JOIN student_school ss on s.SCHOOL = ss.ID"+ 
			        " where csr.CUSTOMER_ID = :cusId and csr.IS_DELETED = 0 ";  
			List<Map<Object, Object>> resultList=customerDao.findMapBySql(sql,param);
			if(resultList!=null && resultList.size()>1){				
				for(Map<Object, Object> map:resultList){
					if(map.get("studentName")!=null){
						studentBuffer.append(map.get("studentName")+",");
					}
					if(map.get("schoolName")!=null){
						schoolBuffer.append(map.get("schoolName")+",");
					}
					
				}
		        String studentList = studentBuffer.toString();
		        String schoolList = schoolBuffer.toString();
		        if(StringUtils.isNotBlank(studentList)){
		        	customerVo.setStudentNameList(new String(studentList.substring(0, studentList.length()-1)));
		        }else{
		        	customerVo.setStudentNameList("");
		        }
		        if(StringUtils.isNotBlank(schoolList)){
		        	customerVo.setSchoolNameList(new String(schoolList.substring(0, schoolList.length()-1)));
		        }else{
		        	customerVo.setSchoolNameList("");
		        }
		        
		        //清空Buffer
		        studentBuffer.delete(0, studentBuffer.length());
		        schoolBuffer.delete(0, schoolBuffer.length());
			}else if(resultList.size()==1){
				Map<Object, Object> map=resultList.get(0);
				if(map.get("studentName")!=null){
					customerVo.setStudentNameList(map.get("studentName").toString());
				}else{
					customerVo.setStudentNameList("");
				}
				if(map.get("schoolName")!=null){
					customerVo.setSchoolNameList(map.get("schoolName").toString());
				}else{
					customerVo.setSchoolNameList("");
				}
			}
					
			//客户是否签单
			List<Contract> cLists = contractDao.findContractByCustomer(cusId, null, null);
			if(cLists!=null && cLists.size()>0){
				Contract contract = cLists.get(0);
				customerVo.setFirstContractTime(contract.getCreateTime());
			}else{
				customerVo.setFirstContractTime("");
			}
			int cList = contractDao.countContractByCustomer(cusId);
			if(cList>0){
				customerVo.setIsSignContract("true");
			}else{
				customerVo.setIsSignContract("false");
			}			
			voList.add(customerVo);
		}
		dataPackage.setDatas(voList);
		long endTime=System.currentTimeMillis(); //获取结束时间 
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms"); 
        return dataPackage;
	}
	
	@Override
	public DataPackage getCustomersForInternet(CustomerVo vo, DataPackage dataPackage) {
		//客户管理 --客户列表（网络专用）
		long startTime=System.currentTimeMillis();   //获取开始时间
		StringBuilder cusSql=new StringBuilder(512);	
		cusSql.append(" select c.id as cusId,c.name as cusName,c.CONTACT as contact,c.RES_ENTRANCE as resEntrance,c.CREATE_TIME as createTime,");
		cusSql.append(" c.CUS_TYPE as cusType,c.CUS_ORG as cusOrg,c.CREATE_USER_ID as userId,c.BL_SCHOOL as blCampusId,c.LAST_FOLLOW_UP_TIME as lastFollowUpTime,");
		cusSql.append(" c.DELIVER_TYPE as deliverType,c.DELEVER_TARGET as deliverTarget, c.BL_BRANCH as branchId,c.DEAL_STATUS as dealStatus ");
		cusSql.append(" , d.`NAME` as resEntranceName ,dd.`NAME` as cusTypeName,ddd.`NAME` as cusOrgName, o.`name` as blCampusName,oo.`name` as branchName ");
		cusSql.append(" ,u.`NAME` as userName "); 
		cusSql.append(" ,og.`NAME` as deptName ");
		//#1728 计算网络分配时间  duanmenrun 20171110
		cusSql.append(" ,cusao.allocate_time as allocateTime ");
		cusSql.append(" ,cusao.obtain_time as obtainTime ");
		cusSql.append(" ,cusao.status as status ");
		cusSql.append(" ,(select name from organization where id = c.FIRST_CAMPUS ) as firstCampusName ");
		
		cusSql.append(" from customer c ");
		
		cusSql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID "); 
		cusSql.append(" LEFT JOIN data_dict dd on c.CUS_TYPE = dd.ID "); 
		cusSql.append(" LEFT JOIN data_dict ddd on c.CUS_ORG = ddd.ID ");

		cusSql.append(" LEFT JOIN organization o on c.BL_SCHOOL = o.id ");
		cusSql.append(" LEFT JOIN organization oo on o.parentID = oo.id ");
		cusSql.append(" LEFT JOIN `user` u on c.CREATE_USER_ID = u.USER_ID ");
		
		cusSql.append(" LEFT JOIN user_dept_job udj on ( c.CREATE_USER_ID = udj.USER_ID and udj.isMajorRole=0 ) ");
		cusSql.append(" LEFT JOIN organization og on og.id=udj.DEPT_ID ");
		
		//#1728 计算网络分配时间  duanmenrun 20171110
 		cusSql.append(" LEFT JOIN customer_allocate_obtain cusao on c.id=cusao.customer_id ");
		
		cusSql.append(" where 1=1 ");
	
		//12个查询条件 
		// 查询姓名
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(vo.getName())) {
			cusSql.append(" and c.NAME like :name ");
			params.put("name", "%"+vo.getName()+"%");
		}
		// 联系方式
		if (StringUtils.isNotBlank(vo.getContact())) {
			cusSql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(vo.getResEntranceId())) {
			cusSql.append(" and c.RES_ENTRANCE= :resEntranceId ");
			params.put("resEntranceId", vo.getResEntranceId());
		}
		// 资源大类
		if (StringUtils.isNotBlank(vo.getCusOrg())) {
			if(vo.getCusOrg().equals("NULL")){
				cusSql.append(" and c.CUS_ORG is NULL ");
			}else{
				cusSql.append(" and c.CUS_ORG= :cusOrg ");
				params.put("cusOrg", vo.getCusOrg());
			}
			
		}
		// 资源细分
		if (StringUtils.isNotBlank(vo.getCusType())) {
			if(vo.getCusType().equals("NULL")){
				cusSql.append(" and c.CUS_TYPE is NULL ");
			}else{
				cusSql.append(" and c.CUS_TYPE = :cusType ");
				params.put("cusType", vo.getCusType());
			}
			
		}
		//录入者 
        if(StringUtil.isNotBlank(vo.getCreateUserName())){
        	cusSql.append(" and (c.CREATE_USER_ID in (select user_id from user where name like :createUserName ) )");
        	params.put("createUserName", "%"+vo.getCreateUserName()+"%");
        }
		//录入者部门
		if(StringUtil.isNotBlank(vo.getCreateUserDept())){
			cusSql.append(" and (c.CREATE_USER_ID in (select udj.USER_ID from user_dept_job udj LEFT JOIN organization o on o.id =udj.DEPT_ID where o.name LIKE :createUserDept ) )");
			params.put("createUserDept", "%"+vo.getCreateUserDept()+"%");
		}		
		//跟进人
		if(StringUtils.isNotBlank(vo.getDeliverTarget())){
			cusSql.append("and c.DELEVER_TARGET = :deliverTarget ");
			params.put("deliverTarget", vo.getDeliverTarget());
		}
		if(StringUtils.isNotBlank(vo.getDeliverTargetName())){
			//通过getDeliverTargetName 获取 DeliverTarget						
			cusSql.append(" and (c.DELEVER_TARGET in (select user_id from user where name like :deliverTargetName ) or c.DELEVER_TARGET in (select id from organization  where name like :deliverTargetName ) ) ");					
			params.put("deliverTargetName", "%"+vo.getDeliverTargetName()+"%");
		}		
		//选择分公司
        if(StringUtil.isNotBlank(vo.getBrenchId())){
        	cusSql.append(" and (c.BL_SCHOOL in( select id from organization where organization.parentID = :brenchId ) or c.BL_SCHOOL= :brenchId )");
        	params.put("brenchId", vo.getBrenchId());
        }
        
        //选择首次分配校区
  		if(StringUtil.isNotBlank(vo.getFirstCampus())){
  			cusSql.append(" and c.FIRST_CAMPUS = :firstCampus ");
              params.put("firstCampus", vo.getFirstCampus());
  		}
        
		//选择校区
		if(StringUtil.isNotBlank(vo.getBlCampusId())){
			cusSql.append(" and (c.BL_SCHOOL = :blCampusId or c.DELEVER_TARGET= :blCampusId ) ");
            params.put("blCampusId", vo.getBlCampusId());
		}
		//最新跟进时间
		if(StringUtils.isNotBlank(vo.getFollowupBeginDate())){		
			cusSql.append(" and c.LAST_FOLLOW_UP_TIME >= '"+vo.getFollowupBeginDate()+" 00:00:00.000' ");
		}			
		if(StringUtils.isNotBlank(vo.getFollowupEndDate())){
			cusSql.append(" and c.LAST_FOLLOW_UP_TIME <= '"+vo.getFollowupEndDate()+" 23:59:59.999' ");
		}
		
		//toLastFollowupDays距离上次跟进天数 //
		//例如要查5天没有跟进过的，那么就只查距今天至上一次跟进超过5天的，不包含4天以下的   xiaojinwang 20170810
		//1、未获取的情况下，以分配时间为准DELIVER_TIME
		//2、获取了但是没有跟进时，以获取时间为准 GET_CUSTOMER_TIME
		if(vo.getToLastFollowupDays()!=null && vo.getToLastFollowupDays()!=0){
			cusSql.append(" and (( c.LAST_FOLLOW_UP_TIME is not NULL and DATE_FORMAT(c.LAST_FOLLOW_UP_TIME,'%Y-%m-%d') = :toLastFollowupDays ) or  ");
			cusSql.append(" (c.LAST_FOLLOW_UP_TIME is NULL and c.DEAL_STATUS = 'STAY_FOLLOW' and DATE_FORMAT(c.DELIVER_TIME,'%Y-%m-%d') = :toLastFollowupDays ) or  ");	
			cusSql.append(" (c.LAST_FOLLOW_UP_TIME is NULL and c.DEAL_STATUS = 'FOLLOWING' and DATE_FORMAT(c.GET_CUSTOMER_TIME,'%Y-%m-%d') = :toLastFollowupDays ))  ");	
			params.put("toLastFollowupDays", DateTools.addDateToString(DateTools.getCurrentDate(), -vo.getToLastFollowupDays()));
		}
		
		//录入时间
		if(StringUtils.isNotBlank(vo.getCreateBeginDate())){		
			cusSql.append(" and c.CREATE_TIME >= '"+vo.getCreateBeginDate()+" 00:00:00.000' ");
		}			
		if(StringUtils.isNotBlank(vo.getCreateEndDate())){
			cusSql.append(" and c.CREATE_TIME <= '"+vo.getCreateEndDate()+" 23:59:59.999' ");
		}
		
		//第一份签订合同时间
		if(StringUtil.isNotBlank(vo.getFirstContractBeginTime())||StringUtil.isNotBlank(vo.getFirstContractEndTime())){
			cusSql.append(" and c.id in (select CUSTOMER_ID from contract where 1=1 ");
			if(StringUtil.isNotBlank(vo.getFirstContractBeginTime())){
				cusSql.append(" and CREATE_TIME >= :firstContractBeginTime ");
				params.put("firstContractBeginTime", vo.getFirstContractBeginTime()+" 00:00:00 ");
			}
			if(StringUtil.isNotBlank(vo.getFirstContractEndTime())){
				cusSql.append(" and CREATE_TIME <= :firstContractEndTime ");
				params.put("firstContractEndTime", vo.getFirstContractEndTime()+" 23:59:59 ");
			}
			cusSql.append(" ) ");
		}
		
		//如果这三个条件有一个不为空则关联学生表
		if(StringUtils.isNotBlank(vo.getPointialStuName())||StringUtils.isNotBlank(vo.getPointialStuSchoolId())){
			cusSql.append(" and c.id in ( select cs.CUSTOMER_ID from customer_student_relation cs left join ");
			cusSql.append(" student s on cs.STUDENT_ID = s.ID where 1=1 ");
			//学生姓名
			if(StringUtils.isNotBlank(vo.getPointialStuName())){
				cusSql.append(" and s.NAME like :pointialStuName ");
				params.put("pointialStuName", "%"+vo.getPointialStuName()+"%");
			}
			//选择就读学校  这里传进来的是学校的Id 不是名字
			if(StringUtils.isNotBlank(vo.getPointialStuSchoolId())){
				cusSql.append(" and s.SCHOOL = :pointialStuSchoolId ");
				params.put("pointialStuSchoolId", vo.getPointialStuSchoolId());
			}	
			cusSql.append(" ) ");
		}

		
		
		//客户类型  就是客户的处理状态 dealStatus 	
		if(vo.getDealStatus()!=null){
			cusSql.append(" and c.DEAL_STATUS = :dealStatus ");
			params.put("dealStatus", vo.getDealStatus().getValue());
		}
		
		//客户列表配置权限   xiaojinwang 20160928
		User user = userService.getCurrentLoginUser();	
		//当前登陆者所属分公司
		Organization blBranch = userService.getBelongBranchByUserId(user.getUserId());
		Organization organization = organizationService.findById(user.getOrganizationId());
		if(blBranch!=null){
			cusSql.append(" and o.orgLevel like '"+blBranch.getOrgLevel()+"%' ");
		}else{
			cusSql.append(" and o.orgLevel like '"+organization.getOrgLevel()+"%' ");
		}
		
		//整个分公司的网络资源入口为网络的客户
		cusSql.append(" and c.RES_ENTRANCE ='"+ResEntranceType.ON_LINE.getValue()+"'");
		
		//#1728 计算网络分配时间  duanmenrun 20171110
		if(vo.getJudgeTimeOut()!=null) {
			if(vo.getJudgeTimeOut().equals(0)) {//未超过5分钟
				cusSql.append(" and  cusao.allocate_time >= DATE_SUB(IF(cusao.obtain_time>cusao.allocate_time,cusao.obtain_time,NOW()),INTERVAL 5 MINUTE )");
			}else if(vo.getJudgeTimeOut().equals(1)){//超过五分钟
				cusSql.append(" and  cusao.allocate_time < DATE_SUB(IF(cusao.obtain_time>cusao.allocate_time,cusao.obtain_time,NOW()),INTERVAL 5 MINUTE )");
			}
		}
		
//		Organization organization = userService.getUserMainDeptByUserId(user.getUserId());
//		
//		String orgId=organization.getId();
//        
//		//获取集团网络部以及当前校区的外呼部门的id
//		Map<String, String> map_id = getNetWorkGroupOUTCALLOrgId();
//		String userIds = null;
//		if(map_id.get("network")!=null && orgId.equals(map_id.get("network"))){			
//			//所有集团网络部的客户列表			
//			//Boolean flag_NETWORK_SPEC = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_SPEC);
////			Boolean flag_NETWORK_MANAGER = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_MANAGER);
////			Boolean flag_INTERNET_MANAGER = userService.isUserRoleCode(user.getUserId(), RoleCode.INTERNET_MANAGER);
//			userIds = getUserIdsByOrgId(orgId);
//			if(userIds.length()==0){
//				userIds = "'"+user.getUserId()+"'";
//			}					
//		}
//		
//		if(userIds!=null){
//			cusSql.append(" and ( c.CREATE_USER_ID in ("+userIds+") or c.DELEVER_TARGET in ("+userIds+")  ) ");					
//		}
	
		
		
		
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
			//params.put("orderBy", );
		} 
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
		//dataPackage.setDatas(list);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params, 300));	
        
		//将查询结果转换为vo		
		//vo原来是签单客户列表 后来改成客户列表  仍使用原来的签单vo
		List<SignupCustomerVo> voList =new ArrayList<SignupCustomerVo>();
		StringBuilder studentBuffer = new StringBuilder(" ");
		StringBuilder schoolBuffer = new StringBuilder(" ");
		SignupCustomerVo customerVo = null;
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate = new Date();
		for(Map<Object,Object> tmaps : list){
			String status = tmaps.get("status")!=null?tmaps.get("status").toString():null;
			Map<String,String> maps = (Map)tmaps;
			customerVo = new SignupCustomerVo();
			String cusId =maps.get("cusId");
			customerVo.setCusId(cusId!=null ? cusId:null);
			customerVo.setCusName(maps.get("cusName")!=null? maps.get("cusName"):null);
			
			//#1728 计算网络分配时间  duanmenrun 20171110
			String allocateTime = maps.get("allocateTime")!=null?maps.get("allocateTime"):null;
			if(StringUtils.isNotBlank(status)) {
				Date obtainDate = null;
				try {
					if(status.equals("0")) {//未获取
						customerVo.setAllocateTime(allocateTime);
						obtainDate = nowDate;
					}else if(status.equals("1")) {//已获取
						String obtainTime = maps.get("obtainTime")!=null?maps.get("obtainTime"):null;
						customerVo.setAllocateTime(allocateTime);
						customerVo.setObtainTime(obtainTime);
						obtainDate = sdf.parse(obtainTime);
					}
					Date allocateDate = sdf.parse(allocateTime);
					long diffSecond = (obtainDate.getTime() - allocateDate.getTime())/1000 - 300;//五分钟算超时	
					if(diffSecond > 0) {
						String waitingTime = (diffSecond/60/60/24 > 0?diffSecond/60/60/24+"天" : "") +
								 (diffSecond/60/60%24 > 0?diffSecond/60/60%24+"小时" : "") +
								 (diffSecond/60%60 > 0?diffSecond/60%60+"分" : "") +
								 (diffSecond%60 > 0?diffSecond%60+"秒" : "");
						customerVo.setOutTime(waitingTime);
					}else {
						customerVo.setOutTime("");
					}
				
				} catch (ParseException e) {
					log.error("计算分配时间异常！");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
//			//联系方式
//			customerVo.setContact(maps.get("contact")!=null? maps.get("contact"):null);
			//登记人姓名以及登记人部门
			customerVo.setCreateCustomerUserId(maps.get("userId"));
			customerVo.setCreateUserName(maps.get("userName"));
			customerVo.setCreateUserDept(maps.get("deptName"));			
			customerVo.setResEntranceId(maps.get("resEntrance"));
			customerVo.setResEntranceName(maps.get("resEntranceName"));
			customerVo.setCusType(maps.get("cusType"));
			customerVo.setCusTypeName(maps.get("cusTypeName"));
			customerVo.setCusOrg(maps.get("cusOrg"));
			customerVo.setCusOrgName(maps.get("cusOrgName"));
			Object createTime = maps.get("createTime");
			customerVo.setCreateTime(createTime!=null?createTime.toString():"");
				
			//最新跟进时间
			customerVo.setLastFollowUpTime(maps.get("lastFollowUpTime"));
			//所属校区
			//所属分公司
			customerVo.setBranchName(maps.get("branchName"));			
			customerVo.setBlCampusName(maps.get("blCampusName"));
			customerVo.setFirstCampusName(maps.get("firstCampusName"));
			//跟进人
			if(StringUtil.isNotBlank(maps.get("deliverTarget"))){
				customerVo.setDeliverTarget(maps.get("deliverTarget"));
				if (maps.get("deliverType") != null) {
					if (CustomerDeliverType.PERSONAL_POOL.getValue().equals(maps.get("deliverType"))) {
						User deliverTarget = userService.loadUserById(maps.get("deliverTarget"));
						if(deliverTarget!=null){														
							customerVo.setDeliverTargetName(deliverTarget.getName());
							Organization belongCampus = userService.getBelongCampusByUserId(maps.get("deliverTarget"));
							Organization belongBranch = userService.getBelongBranchByUserId(maps.get("deliverTarget"));
							if(belongCampus!=null && belongCampus.getOrgType()!= OrganizationType.CAMPUS){
								customerVo.setBlCampusName("");
							}
							if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
								customerVo.setBranchName(belongBranch.getName());
							}else{
								customerVo.setBranchName("");
							}
						}else{
							Organization deliverTargetOrg = organizationDao.findById(maps.get("deliverTarget"));
							if(deliverTargetOrg!=null){
								if(deliverTargetOrg.getOrgType()==OrganizationType.CAMPUS){
									Organization belongCampus =userService.getBelongCampusByOrgId(deliverTargetOrg.getId());
									if(belongCampus!=null){
										customerVo.setBlCampusName(belongCampus.getName());
									}else{
										customerVo.setBlCampusName("");
									}
								}else{
									customerVo.setBlCampusName("");
								}
								Organization belongBranch =userService.getBelongBranchByOrgId(deliverTargetOrg.getId());
								if(belongBranch!=null){
									customerVo.setBranchName(belongBranch.getName());
									if(deliverTargetOrg.getName().indexOf("网络")!=-1){
										customerVo.setBranchName("");
									}
								}else{
									customerVo.setBranchName("");
								}
								customerVo.setDeliverTargetName(deliverTargetOrg.getResourcePoolName());
							}
						}
					} else if (CustomerDeliverType.CUSTOMER_RESOURCE_POOL.getValue().equals(maps.get("deliverType"))) {
						Organization deliverTargetOrg = organizationDao.findById(maps.get("deliverTarget"));
						if(deliverTargetOrg!=null){
							if(deliverTargetOrg.getOrgType()==OrganizationType.CAMPUS){
								Organization belongCampus =userService.getBelongCampusByOrgId(deliverTargetOrg.getId());
								if(belongCampus!=null){
									customerVo.setBlCampusName(belongCampus.getName());
								}else{
									customerVo.setBlCampusName("");
								}
							}else{
								customerVo.setBlCampusName("");
							}
							
							Organization belongBranch =userService.getBelongBranchByOrgId(deliverTargetOrg.getId());
							if(belongBranch!=null){
								customerVo.setBranchName(belongBranch.getName());	
								if(deliverTargetOrg.getName().indexOf("网络")!=-1){
									customerVo.setBranchName("");
								}
							}else{
								customerVo.setBranchName("");
							}
							customerVo.setDeliverTargetName(deliverTargetOrg.getResourcePoolName());
						}else{
							User deliverTarget = userService.loadUserById(maps.get("deliverTarget"));
							if(deliverTarget!=null){
								customerVo.setDeliverTargetName(deliverTarget.getName());
								Organization belongCampus = userService.getBelongCampusByUserId(maps.get("deliverTarget"));
								Organization belongBranch = userService.getBelongBranchByUserId(maps.get("deliverTarget"));
								if(belongCampus!=null && belongCampus.getOrgType()!= OrganizationType.CAMPUS){
									customerVo.setBlCampusName("");
								}
								if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
									customerVo.setBranchName(belongBranch.getName());
								}else{
									customerVo.setBranchName("");
								}
							}
						}
					}
				}else{
					User deliverTarget =userService.loadUserById(maps.get("deliverTarget"));
					if(deliverTarget!=null){
						customerVo.setDeliverTargetName(deliverTarget.getName());
						Organization belongCampus = userService.getBelongCampusByUserId(maps.get("deliverTarget"));
						Organization belongBranch = userService.getBelongBranchByUserId(maps.get("deliverTarget"));
						if(belongCampus!=null && belongCampus.getOrgType()!= OrganizationType.CAMPUS){
							customerVo.setBlCampusName("");
						}
						if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
							customerVo.setBranchName(belongBranch.getName());
						}else{
							customerVo.setBranchName("");
						}
					}else{
						Organization deliverTargetOrg = organizationDao.findById(maps.get("deliverTarget"));
						if(deliverTargetOrg!=null){
							if(deliverTargetOrg.getOrgType()==OrganizationType.CAMPUS){
								Organization belongCampus =userService.getBelongCampusByOrgId(deliverTargetOrg.getId());
								if(belongCampus!=null){
									customerVo.setBlCampusName(belongCampus.getName());
								}else{
									customerVo.setBlCampusName("");
								}
							}else{
								customerVo.setBlCampusName("");
							}
							Organization belongBranch =userService.getBelongBranchByOrgId(deliverTargetOrg.getId());
							if(belongBranch!=null){
								customerVo.setBranchName(belongBranch.getName());
								if(deliverTargetOrg.getName().indexOf("网络")!=-1){
									customerVo.setBranchName("");
								}
							}else{
								customerVo.setBranchName("");
							}
							customerVo.setDeliverTargetName(deliverTargetOrg.getResourcePoolName());
						}
					}
				}
			}else{
				//转介绍 根据录入人来判断 INTRODUCE
				if(maps.get("cusType")!=null && "INTRODUCE".equals(maps.get("cusType"))){
					Organization belongCampus = userService.getBelongCampusByUserId(maps.get("userId"));
					Organization belongBranch = userService.getBelongBranchByUserId(maps.get("userId"));
					if(belongCampus!=null && belongCampus.getOrgType()== OrganizationType.CAMPUS){
						customerVo.setBlCampusName(belongCampus.getName());
					}else{
						customerVo.setBlCampusName("");
					}
					if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
						customerVo.setBranchName(belongBranch.getName());
					}else{
						customerVo.setBranchName("");
					}
				}				
			}
			
			
			//客户跟进状态  客户类型
			String dealStatus = maps.get("dealStatus");
			if(dealStatus!=null){
				customerVo.setDealStatusName(CustomerDealStatus.valueOf(dealStatus).getName());
			}
			
			
			//最新跟进记录
			StringBuilder query =new StringBuilder(" select cf.remark as remark from customer_folowup cf where appointment_type ='"+AppointmentType.FOLLOW_UP.getValue()+"' ");
			query.append(" and cf.customer_id = :cusId ORDER BY cf.create_time desc LIMIT 1 ");
			Map<String, Object> param = Maps.newHashMap();
			param.put("cusId", cusId);
			List<Map<Object, Object>> customerFolowups=customerFolowupDao.findMapBySql(query.toString(),param);
			if (customerFolowups.size() > 0) {
				customerVo.setFollowupRemark(customerFolowups.get(0).get("remark")!=null?customerFolowups.get(0).get("remark").toString():"");
			}
			
			
			
			//学生姓名  多个 用逗号,分割开//就读学校 多个 用逗号,分割开 
			String sql ="SELECT s.`NAME` as studentName,ss.`NAME` as schoolName from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID LEFT JOIN student_school ss on s.SCHOOL = ss.ID"+ 
			        " where csr.CUSTOMER_ID = :cusId and csr.IS_DELETED = 0 ";  
			List<Map<Object, Object>> resultList=customerDao.findMapBySql(sql,param);
			if(resultList!=null && resultList.size()>1){				
				for(Map<Object, Object> map:resultList){
					if(map.get("studentName")!=null){
						studentBuffer.append(map.get("studentName")+",");
					}
					if(map.get("schoolName")!=null){
						schoolBuffer.append(map.get("schoolName")+",");
					}
					
				}
		        String studentList = studentBuffer.toString();
		        String schoolList = schoolBuffer.toString();
		        if(StringUtils.isNotBlank(studentList)){
		        	customerVo.setStudentNameList(new String(studentList.substring(0, studentList.length()-1)));
		        }else{
		        	customerVo.setStudentNameList("");
		        }
		        if(StringUtils.isNotBlank(schoolList)){
		        	customerVo.setSchoolNameList(new String(schoolList.substring(0, schoolList.length()-1)));
		        }else{
		        	customerVo.setSchoolNameList("");
		        }
		        
		        //清空Buffer
		        studentBuffer.delete(0, studentBuffer.length());
		        schoolBuffer.delete(0, schoolBuffer.length());
			}else if(resultList.size()==1){
				Map<Object, Object> map=resultList.get(0);
				if(map.get("studentName")!=null){
					customerVo.setStudentNameList(map.get("studentName").toString());
				}else{
					customerVo.setStudentNameList("");
				}
				if(map.get("schoolName")!=null){
					customerVo.setSchoolNameList(map.get("schoolName").toString());
				}else{
					customerVo.setSchoolNameList("");
				}
			}
					
			//客户是否签单
			List<Contract> cLists = contractDao.findContractByCustomer(cusId, null, null);
			if(cLists!=null && cLists.size()>0){
				Contract contract = cLists.get(0);
				customerVo.setFirstContractTime(contract.getCreateTime());
			}else{
				customerVo.setFirstContractTime("");
			}
			int cList = contractDao.countContractByCustomer(cusId);
			if(cList>0){
				customerVo.setIsSignContract("true");
			}else{
				customerVo.setIsSignContract("false");
			}			
			voList.add(customerVo);
		}
		dataPackage.setDatas(voList);
		long endTime=System.currentTimeMillis(); //获取结束时间 
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms"); 
        return dataPackage;
	}
	
	
	private Map<String, String> getNetWorkGroupOUTCALLOrgId(){
		Map<String, String> map = new HashMap<>();
		Map<String, Object> params = Maps.newHashMap();
		//获取网络集团部id 
		List<Organization> network = organizationDao.findBySql("select * from organization where ORG_SIGN ='jtwlyxb' ", params);
		if(network!=null && network.size()>0){
			map.put("network", network.get(0).getId());
		}else{
			map.put("network", null);
		}
		//获取当前登录者所在组织架构的外呼部门orgId
		Organization organization = userService.getCurrentLoginUserOrganization();
		params.put("orgLevel", organization.getOrgLevel()+"%");
		List<Organization> outcall = organizationDao.findBySql("select * from organization where orgLevel LIKE :orgLevel and `name` LIKE '%外呼%' ",params);
		if(outcall!=null && outcall.size()>0 ){
			map.put("outcall", outcall.get(0).getId());
		}else{
			map.put("outcall", null);
		}		
		return map ;
	}
	
	private String getUserIdsByOrgId(String orgId){
		StringBuilder userIds =new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		params.put("orgId", orgId);
		List<Map<Object, Object>> result  = userDao.findMapBySql("select u.USER_ID as userId from user_dept_job udj,`user` u where udj.USER_ID = u.USER_ID and udj.DEPT_ID = :orgId  ",params);//and u.ENABLE_FLAG = 0
		for(Map<Object, Object> map : result){
			userIds.append("'"+map.get("userId")+"',");
		}
		if(userIds.length()>0){
			return userIds.substring(0, userIds.length()-1);
		}else{
			return userIds.toString();
		}	
	}

	/**
	 * 跟进客户管理--释放客户资源
	 * user是当前跟进人
	 * deliverTarget传入的参数目前没用
	 */
	@Override
	public Response releaseCustomer(String deliverTarget,String[] cusIds,User user){
		Response response = new Response();
		StringBuilder msgbuffer = new StringBuilder(128);  
		String currentTime=DateTools.getCurrentDateTime();	
		
		String releaseTargetName = null;
		
		for(String id :cusIds){		
			String result = null;
			//根据customerId 获取客户customer
			Customer releaseCustomer = this.findById(id);
			Customer customer = this.findById(id); 
			String before_deliverTarget = releaseCustomer.getDeliverTarget();//被释放的客户的当前跟进人
			String beforeDeliverTarget = releaseCustomer.getBeforeDeliverTarget();//客户的前工序
			String deliverTargetName = releaseCustomer.getDeliverTargetName();
			CustomerDealStatus customerDealStatus = releaseCustomer.getDealStatus();
			
			//释放客户资源的逻辑  逐级释放  获取被释放客户
			if(StringUtils.isBlank(before_deliverTarget)){
				response.setResultCode(-1);
				response.setResultMessage("该客户没有被跟进不能释放");
				return response;
			}	
			if(releaseCustomer.getDealStatus()!=null && releaseCustomer.getDealStatus()==CustomerDealStatus.TOBE_AUDIT){
				response.setResultCode(-1);
				response.setResultMessage("该客户处于待审核状态，不能释放");
				return response;
			}
			
			User deliver_user = userService.loadUserById(before_deliverTarget);
			//如果当前登录者不是被释放的客户的当前跟进人不能释放客户
			if(!user.getUserId().equals(before_deliverTarget)){
				
				if(releaseCustomer.getDealStatus()!=null && (releaseCustomer.getDealStatus()!=CustomerDealStatus.STAY_FOLLOW &&
						releaseCustomer.getDealStatus()!=CustomerDealStatus.NEW)){
					response.setResultCode(-1);
					response.setResultMessage("该客户"+releaseCustomer.getDealStatus().getName()+",不能释放");
					return response;
				}
							
			}
			
			
			
			
			
			
			
			
			
			if(deliver_user!=null){
				//优化  跟进人是咨询师  咨询主管 校区营运主任 释放客户资源时，资源回收到跟进人所属校区资源池  ----xiaojinwang 2017-02-17
				
				
				Boolean isZXS = userService.isUserRoleCode(deliver_user.getUserId(), RoleCode.CONSULTOR);//是否咨询师
				Boolean isCOD = userService.isUserRoleCode(deliver_user.getUserId(), RoleCode.CAMPUS_OPERATION_DIRECTOR);//是否校区营运主任
				Boolean isCD = userService.isUserRoleCode(deliver_user.getUserId(), RoleCode.CONSULTOR_DIRECTOR);//是否咨询主管
				Boolean isXQZR = userService.isUserRoleCode(deliver_user.getUserId(), RoleCode.CAMPUS_DIRECTOR);//是否校区主任
				
				String roleSign = userService.getUserRoleSign(deliver_user.getUserId());
				List<String> roleSignList = userService.getUserRoleSignFromRole(deliver_user.getUserId());
				if(roleSignList==null) roleSignList = new ArrayList<>();
				
				if(isZXS||isCOD||isCD||isXQZR||RoleSign.ZXS.getValue().equalsIgnoreCase(roleSign)
						||(roleSignList!=null && roleSignList.contains(RoleSign.ZXS.getValue().toLowerCase()))){
					ResourcePool resourcePool =resourcePoolService.getBelongBranchResourcePool(deliver_user.getUserId(), OrganizationType.CAMPUS.getValue());
					if(resourcePool!=null){
						releaseCustomer.setDeliverTarget(resourcePool.getOrganizationId());
						releaseCustomer.setDeliverTargetName(resourcePool.getName());
					}else{
						msgbuffer.append(releaseCustomer.getName() + ",");
						result ="";
					}
				//下面是其他角色的人的释放客户	
				}else{	
					
					
					
					deliverTarget = deliver_user.getOrganizationId();
					ResourcePool targetPool = resourcePoolService.findResourcePoolById(deliverTarget);
					if(targetPool!=null && targetPool.getStatus().equals(ValidStatus.VALID)){
						releaseCustomer.setDeliverTarget(deliverTarget);
						releaseCustomer.setDeliverTargetName(targetPool.getName());
					}else{
						Organization deliver_Org = organizationDao.findById(deliverTarget);
						result = getBelongResourcePool(releaseCustomer, deliver_Org);
						//result不为空则无法设置
						if(result!=null){
							// 继续查找
							if (StringUtil.isNotBlank(deliver_Org.getParentId())) {
								Organization deliver_Org_parent = organizationDao.findById(deliver_Org.getParentId());
								result = getBelongResourcePool(releaseCustomer, deliver_Org_parent);
								if (result != null) {
									msgbuffer.append(result + ",");
								}
							}else{
								msgbuffer.append(result + ",");
							}
						}					
					}					
				}				
				
			}else{
				//当前是分配给某一个organizaiton
				deliverTarget = before_deliverTarget;
				Organization deliver_Org = organizationDao.findById(deliverTarget);				
//				result = getBelongResourcePool(releaseCustomer, deliver_Org);
//				//result不为空则无法设置
				
				// 查找 上一级
				if (StringUtil.isNotBlank(deliver_Org.getParentId())) {
					Organization deliver_Org_parent = organizationDao.findById(deliver_Org.getParentId());
					result = getBelongResourcePool(releaseCustomer, deliver_Org_parent);
					if (result != null) {
						msgbuffer.append(result + ",");
					}
				} else {
					msgbuffer.append(result + ",");
					result = "";
				}
									
			}			
//			// 修改资源池
//			if(!deliverTarget.equals(releaseCustomer.getDeliverTarget())){
//				//传入进来的资源池
//				ResourcePool targetPool = resourcePoolService.findResourcePoolById(deliverTarget);
//				if(targetPool!=null && targetPool.getStatus().equals(ValidStatus.VALID)){
//					releaseCustomer.setDeliverTarget(deliverTarget);
//					releaseCustomer.setDeliverTargetName(targetPool.getName());
//				}else{
//					//查找当前组织架构
//					Organization currentOrg = userService.getCurrentLoginUserOrganization();
//					if(currentOrg!=null && deliverTarget.equals(currentOrg.getId())){
//						//如果当前组织架构和deliverTarget相等则找父类资源池
//						//result不为空则无法设置
//						result =getBelongResourcePool(releaseCustomer, currentOrg);
//						if(result!=null){
//							msgbuffer.append(result+",");
//						}
//					}else if(currentOrg!=null){
//						//当前组织架构和deliverTarget不相等 如果有效则设置
// 						ResourcePool currentPool = resourcePoolService.findResourcePoolById(currentOrg.getId());
//						if(currentPool!=null && currentPool.getStatus().equals(ValidStatus.VALID)){
//							releaseCustomer.setDeliverTarget(currentOrg.getId());
//							releaseCustomer.setDeliverTargetName(currentPool.getName());
//						}else{
//							//如果无效 则继续找父类  result不为空则无法设置
//    					    result =getBelongResourcePool(releaseCustomer, currentOrg);
//    						if(result!=null){
//    							msgbuffer.append(result+",");
//    						}
//						}
//					}
//				}				
//			}else{
//				msgbuffer.append(releaseCustomer.getName()+",");
//				result="";
//			}	
			//上面代码是设置deliverTarget
			
			//释放分为两种情况： 主动释放 被动释放 判断当前登录人是否等于当前跟进人和前工序
			//主动释放：当前登录者是咨询师  网络专员 外呼专员  如果是网络和外呼 则继续判断 当前跟进人是不是等于前工序 等于则把前工序干掉 不等也要干掉前工序 然后把原来的跟进人设回来
			//被动释放：其他管理者进行释放操作 只能释放 deliverTarget的人  如果是外呼和网络 则把前工序干掉 
					
			//释放资源 设置为待跟进  和公共资源池
			if (result == null) {
				
				//before_deliverTarget 被释放的客户的当前跟进人
				//beforeDeliverTarget 客户的前工序
				//user 当前登录者
				Boolean isWHZY = userService.isUserRoleCode(user.getUserId(), RoleCode.OUTCALL_SPEC);
				Boolean isWLZY = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_SPEC);
				if(user.getUserId().equals(before_deliverTarget)||user.getUserId().equals(beforeDeliverTarget)){
					if(isWHZY||isWLZY){
						releaseCustomer.setBeforeDeliverTarget(null);
						if(before_deliverTarget!=null&& !before_deliverTarget.equals(beforeDeliverTarget)){
							releaseCustomer.setDeliverTarget(before_deliverTarget);
							releaseCustomer.setDeliverTargetName(deliverTargetName);
							releaseCustomer.setDealStatus(customerDealStatus);
							releaseCustomer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
						}else{
							releaseCustomer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
							releaseCustomer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
						}										
					}else{
						releaseCustomer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
						releaseCustomer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
					}
				}else{
				    if(isWHZY||isWLZY){
						releaseCustomer.setBeforeDeliverTarget(null);
					}
					releaseCustomer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
					releaseCustomer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
				}
				
				
				
				releaseTargetName = releaseCustomer.getDeliverTargetName();			
				releaseCustomer.setModifyTime(currentTime);
				releaseCustomer.setModifyUserId(user.getUserId());
				releaseCustomer.setLastDeliverName(user.getName());
				releaseCustomer.setDeliverTime(currentTime);
				
				//增加需求 主动释放客户需要更新客户的上门状态
                releaseCustomer.setVisitType(VisitType.NOT_COME);				
				
				customerDao.save(releaseCustomer);
				
				//添加分配人变动记录表
				DeliverTargetChangeRecord record = new DeliverTargetChangeRecord();
				record.setCustomerId(id);
				record.setPreviousTarget(before_deliverTarget);
				record.setCurrentTarget(releaseCustomer.getDeliverTarget());
				record.setRemark("主动释放客户资源到公共资源池变更分配人记录");
				record.setCreateUserId(user.getUserId());
				record.setCreateTime(currentTime);
				String recordId = deliverTargetChangeService.saveDeliverTargetChangeRecord(record);
				
//				//追加事件埋点数据
//				UserEventRecord userEventRecord = new UserEventRecord();
//				userEventRecord.setCustomerId(releaseCustomer.getId());
//				userEventRecord.setUserId(user.getUserId());
//				userEventRecord.setEventType(EventType.RELEASE);
//				userEventRecord.setUserName(user.getName());
//				userEventRecord.setCreateTime(currentTime);
//				userEventRecord.setStatusNum(1);
//				Long userEventRecordId = userEventRecordService.saveUserEventRecord(userEventRecord);
				
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.RELEASE);
				dynamicStatus.setDescription("释放客户资源");
				if(releaseCustomer.getResEntrance()!=null){
					dynamicStatus.setResEntrance(releaseCustomer.getResEntrance());
				}else if(releaseCustomer.getPreEntrance()!=null){
					dynamicStatus.setResEntrance(releaseCustomer.getPreEntrance());
				}
				dynamicStatus.setStatusNum(1);
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
                customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, user);
				

				
				if(releaseCustomer.getId()!=null){
					CustomerDynamicStatus dynamicStatus2 = new CustomerDynamicStatus();
					dynamicStatus2.setDynamicStatusType(CustomerEventType.RELEASECUSTOMER);
					dynamicStatus2.setDescription("释放客户资源");
					if(releaseCustomer.getResEntrance()!=null){
						dynamicStatus2.setResEntrance(releaseCustomer.getResEntrance());
					}else if(releaseCustomer.getPreEntrance()!=null){
						dynamicStatus2.setResEntrance(releaseCustomer.getPreEntrance());
					}
					dynamicStatus2.setStatusNum(1);
					dynamicStatus2.setTableName("delivertarget_change_record");
					dynamicStatus2.setTableId(recordId);
					dynamicStatus2.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
	                customerEventService.addCustomerDynameicStatus(customer, dynamicStatus2, user);	
					//customerEventService.saveCustomerDynamicStatus(id, CustomerEventType.RELEASECUSTOMER, "释放客户资源");
				}else{
					msgbuffer.append(releaseCustomer.getName()+",");
				}				
			}			
		}
		//循环遍历结束
		String msg = msgbuffer.toString();
		if(StringUtils.isNotBlank(msg)){
			response.setResultCode(-1);
			response.setResultMessage("资源池配置无效,"+msg.substring(0, msg.length()-1)+"客户释放失败");
		}else {
			if(releaseTargetName!=null){
				response.setResultMessage("释放客户资源成功，客户被释放到 "+releaseTargetName);
			}else{
				response.setResultMessage("释放客户资源成功");
			}
			response.setResultCode(0);
			
		}	
		return response;
	}
	
	public String getBelongResourcePool(Customer customer, Organization organization) {
		// 如果无法设置成功则返回customer的name
		// 先找belong
		String result = null;
		if (organization.getBelong() != null) {
			ResourcePool belongPool = resourcePoolService.findResourcePoolById(organization.getBelong());
			if (belongPool != null && belongPool.getStatus().equals(ValidStatus.VALID)) {//
				customer.setDeliverTarget(organization.getBelong());
				customer.setDeliverTargetName(belongPool.getName());
				customer.setLastDeliverName(belongPool.getName());
			} else if(organization.getParentId()!=null){
				// 再找parent
				ResourcePool parentPool = resourcePoolService.findResourcePoolById(organization.getParentId());
				if (parentPool != null && parentPool.getStatus().equals(ValidStatus.VALID)) {
					customer.setDeliverTarget(organization.getParentId());
					customer.setDeliverTargetName(parentPool.getName());
					customer.setLastDeliverName(parentPool.getName());
				} else {
					// 无法找到
					result = customer.getName();
				}
			}
		} else {
			// 再找parent
			if (organization.getParentId() != null) {
				ResourcePool parentPool = resourcePoolService.findResourcePoolById(organization.getParentId());
				if (parentPool != null && parentPool.getStatus().equals(ValidStatus.VALID)) {
					customer.setDeliverTarget(organization.getParentId());
					customer.setDeliverTargetName(parentPool.getName());
					customer.setLastDeliverName(parentPool.getName());
				} else {
					// 无法找到
					result = customer.getName();
				}
			} else {
				// 无法找到
				result = customer.getName();
			}
		}
		return result;
	}
	
	@Override
	public Map<String, Object> getStudentsByCusId(String customerId) {
		//
		Map<String, Object> map = new HashMap<String,Object>();
		Customer customer = customerDao.findById(customerId);
		if(customer==null){
			map.put("resultCode", -1);
            map.put("result", null);
            map.put("resultMessage", "客户不存在");
		}
		StringBuilder query = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId", customerId);
		query.append(" select s.ID as studentId,s.`NAME` as studentName from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID where csr.CUSTOMER_ID = :customerId and csr.IS_DELETED = 0 ");
		List<Map<Object, Object>> list = studentDao.findMapBySql(query.toString(),params);
		if(list==null||list.size()==0){
			map.put("resultCode", -1);
            map.put("result", null);
            map.put("resultMessage", "该客户不存在相关学生");
		}else{
			map.put("resultCode", 0);
            map.put("result", list);
            map.put("resultMessage", "客户相关学生列表");
		}
		return map;
	}
	
	@Override
	public DataPackage getLostCustomers(FollowupCustomerVo vo, DataPackage dataPackage) {
		//查询抢客表gain_customer  deliverFrom 就是客户被抢来源 也就是流失来源 
		User user = userService.getCurrentLoginUser();
		StringBuilder querySql = new StringBuilder(1024);
		querySql.append(" select c.id as cusId,c.name as cusName,c.CONTACT as contact,c.RES_ENTRANCE as resEntrance, ");
		querySql.append(" c.CUS_TYPE as cusType,c.CUS_ORG as cusOrg,c.LAST_DELIVER_NAME as lastDeliverName, ");
		querySql.append(" c.REMARK as lastRemark,c.DELIVER_TIME as deliverTime, ");
		querySql.append(" c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer,c.NEXT_FOLLOWUP_TIME as nextFollowupDate, ");
		querySql.append(" c.LAST_FOLLOW_UP_TIME as lastFollowUpTime,c.APPOINTMENT_DATE as appointmentDate,c.VISIT_TYPE as visitType, ");
		querySql.append(" gc.REASON as lostCustomerReason,gc.CREATE_TIME as lostCustomerTime ");
		querySql.append(" ,d.`NAME` as resEntranceName, dd.`NAME` as cusTypeName, ddd.`NAME` as cusOrgName,dddd.`NAME` as intentionName ");
		querySql.append(" from gain_customer gc LEFT JOIN customer c on gc.CUS_ID = c.ID ");
		querySql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID ");	 
		querySql.append(" LEFT JOIN data_dict dd on c.CUS_TYPE = dd.ID ");
		querySql.append(" LEFT JOIN data_dict ddd on c.CUS_ORG = ddd.ID ");
		querySql.append(" LEFT JOIN data_dict dddd on c.INTENTION_OF_THE_CUSTOMER = dddd.ID ");
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("deliverFrom", user.getUserId());
		querySql.append(" where gc.DELIVERFROM = :deliverFrom ");
			
		//查询条件
		// 查询姓名
		if (StringUtils.isNotBlank(vo.getCusName())) {
			querySql.append(" and c.NAME like :name ");
			params.put("name", "%"+vo.getCusName()+"%");
		}
		// 联系方式
		if (StringUtils.isNotBlank(vo.getContact())) {
			querySql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(vo.getResEntranceId())) {
			querySql.append(" and c.RES_ENTRANCE = :resEntranceId ");
			params.put("resEntranceId", vo.getResEntranceId());
		}
		// 资源大类
		if (StringUtils.isNotBlank(vo.getCusOrg())) {
			querySql.append(" and c.CUS_ORG= :cusOrg ");
			params.put("cusOrg", vo.getCusOrg());
		}
		// 资源细分
		if (StringUtils.isNotBlank(vo.getCusType())) {
			querySql.append(" and c.CUS_TYPE = :cusType ");
			params.put("cusType", vo.getCusType());
		}
		//去掉 2016-11-17
//		// 处理状态
//		if (vo.getDealStatus() != null) {
//			if (StringUtils.isNotBlank(vo.getDealStatus().getValue())) {
//				querySql.append(" and c.DEAL_STATUS = '" + vo.getDealStatus().getValue() + "' ");
//			}
//		}
		// 是否上门
		if (StringUtils.isNotBlank(vo.getIsAppointCome())) {
			// 如果是预约上门
			if ("1".equals(vo.getIsAppointCome())) {
				//querySql.append("and (c.VISIT_TYPE is not NULL && c.VISIT_TYPE <>'NOT_COME' )");
				querySql.append("and c.VISIT_TYPE = 'PARENT_COME' ");
			}
			// 非预约上门
			if ("0".equals(vo.getIsAppointCome())) {
				querySql.append("and (c.VISIT_TYPE is NULL || c.VISIT_TYPE = 'NOT_COME' )");
			}
		}
		
		//增加过滤条件
		Map<String, String> map_id = getNetWorkGroupOUTCALLOrgId();
		Organization organization = userService.getUserMainDeptByUserId(user.getUserId());
		String orgId=organization.getId();
		
		if (map_id.get("network") != null && orgId.equals(map_id.get("network"))) {
			//网络 不加设置限制
//			Boolean isWLZY = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_SPEC);			
//			if (isWLZY) {				
//			} else {
//			}
            
		} else if (map_id.get("outcall") != null && orgId.equals(map_id.get("outcall"))) {
			Boolean isWHZY = userService.isUserRoleCode(user.getUserId(), RoleCode.OUTCALL_SPEC);		
			if (isWHZY) {
				organization = userService.getBelongBranchByUserId(user.getUserId());
			} else {
				organization = userService.getBelongCampusByUserId(user.getUserId());
			}
			String orgLevel = organization.getOrgLevel();
			params.put("orgLevel",  orgLevel+"%");
			querySql.append(
					" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
		} else {
			organization = userService.getBelongCampusByUserId(user.getUserId());
			String orgLevel = organization.getOrgLevel();
			params.put("orgLevel",  orgLevel+"%");
			querySql.append(
					" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
		}
		
		
		
		
		
		// 分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord()) && StringUtils.isNotBlank(dataPackage.getSidx())) {			
			querySql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());						
		}
		List<Map<Object, Object>> list = customerDao.findMapOfPageBySql(querySql.toString(), dataPackage,params);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + querySql.toString() + " ) countall ",params));

		// 将查询结果转换为vo
		List<FollowupCustomerVo> voList = new ArrayList<FollowupCustomerVo>();
		FollowupCustomerVo fVo = null;
		for (Map<Object, Object> tmaps : list) {
			Map<String, String> maps = (Map)tmaps;
			fVo = new FollowupCustomerVo();
			String cusId =maps.get("cusId");
			fVo.setCusId(cusId!=null ? cusId:"");
			fVo.setCusName(maps.get("cusName")!=null? maps.get("cusName"):"");
			//联系方式
			fVo.setContact(maps.get("contact")!=null? maps.get("contact"):"");
			
			//设置资源入口
			fVo.setResEntranceId(maps.get("resEntrance"));
			fVo.setResEntranceName(maps.get("resEntranceName"));
			
			//资源细分
			fVo.setCusType(maps.get("cusTypeName"));
					
			//资源来源 
			fVo.setCusOrg(maps.get("cusOrgName"));

			
			//设置意向度
			fVo.setIntentionOfTheCustomerId(maps.get("intentionOfTheCustomer"));
			fVo.setIntentionOfTheCustomerName(maps.get("intentionName"));
			
			//去掉  2016-11-17
//			//处理状态			
//			if (maps.get("dealStatus") != null) {
//				String dealStatus = maps.get("dealStatus");
//				fVo.setDealStatus(CustomerDealStatus.valueOf(dealStatus));
//				fVo.setDealStatusName(CustomerDealStatus.valueOf(dealStatus) == null ? ""
//						: CustomerDealStatus.valueOf(dealStatus).getName());
//			}
			
			//设置最后分配人名称
			fVo.setLastDeliverName(maps.get("lastDeliverName")!=null ? maps.get("lastDeliverName"):"");
			//设置最后分配时间  用于计算总跟进天数
			fVo.setDeliverTime(maps.get("deliverTime")!=null ? maps.get("deliverTime"):"");

	
            //设置最新跟进记录	
			Map<String, Object> param = Maps.newHashMap();
			param.put("cusId", cusId);
			StringBuilder query =new StringBuilder(" select cf.remark as remark from customer_folowup cf where appointment_type ='"+AppointmentType.FOLLOW_UP.getValue()+"' ");
			query.append(" and cf.customer_id = :cusId ORDER BY cf.create_time desc LIMIT 1 ");			
			List<Map<Object, Object>> customerFolowups=customerFolowupDao.findMapBySql(query.toString(),param);
			if (customerFolowups.size() > 0) {
				fVo.setLastRemark(customerFolowups.get(0).get("remark")!=null?customerFolowups.get(0).get("remark").toString():"");
			}
			
			
			//是否上门和最新跟进是否预约上门没有关系 是只要客户上门登记以后都是上门 
            String visitType = maps.get("visitType");
            if(visitType==null ||(visitType!=null && visitType.equals(VisitType.NOT_COME.getValue()))){
            	fVo.setIsAppointCome("否");
            }else{
            	fVo.setIsAppointCome("是");
            }
            	
			
//			Customer customer = customerDao.findById(cusId);
//			if(customer.getVisitType()==null||(customer.getVisitType() != null && customer.getVisitType().equals(VisitType.NOT_COME))){
//				fVo.setIsAppointCome("否");
//			}else{
//				fVo.setIsAppointCome("是");
//			}
			
			
			
			
			//下次跟进时间
			fVo.setNextFollowupDate(maps.get("nextFollowupDate")!=null?maps.get("nextFollowupDate"):"");
			//预约上门时间
			fVo.setAppointmentDate(maps.get("appointmentDate")!=null?maps.get("appointmentDate"):"");		
			//流失时间
			fVo.setLostCustomerTime(maps.get("lostCustomerTime")!=null?maps.get("lostCustomerTime"):"");			
			//流失原因
			fVo.setLostCustomerReason(maps.get("lostCustomerReason")!=null?maps.get("lostCustomerReason"):"");
	        voList.add(fVo);			
		}
		dataPackage.setDatas(voList);

		return dataPackage;
	}

	@Override
	public DataPackage getInvalidCustomers(FollowupCustomerVo vo, DataPackage dataPackage) {
		// 查询无效审核列表
		User user = userService.getCurrentLoginUser();
		StringBuilder querySql = new StringBuilder(1024);
		querySql.append(" select c.id as cusId,c.name as cusName,c.CONTACT as contact,c.RES_ENTRANCE as resEntrance, ");
		querySql.append(" c.CUS_TYPE as cusType,c.CUS_ORG as cusOrg,c.LAST_DELIVER_NAME as lastDeliverName, ");
		querySql.append(" c.REMARK as lastRemark,c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer, ");
		querySql.append(" ica.remark as invalidAuditRemark,ica.create_time as invalidCreateTime,ica.audit_status as invalidAuditStatus ");
		
		querySql.append(" ,c.VISIT_TYPE as visitType ");
		querySql.append(" ,d.`NAME` as resEntranceName, dd.`NAME` as cusTypeName, ddd.`NAME` as cusOrgName,dddd.`NAME` as intentionName ");
		
		querySql.append(" from invalid_customer_audit ica LEFT JOIN customer c on ica.customer_id = c.ID ");
		
		querySql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID ");	 
		querySql.append(" LEFT JOIN data_dict dd on c.CUS_TYPE = dd.ID ");
		querySql.append(" LEFT JOIN data_dict ddd on c.CUS_ORG = ddd.ID ");
		querySql.append(" LEFT JOIN data_dict dddd on c.INTENTION_OF_THE_CUSTOMER = dddd.ID ");
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", user.getUserId());
		querySql.append(" where ica.create_user_id = :userId ");	
		//querySql.append(" and ica.audit_status ='"+CustomerAuditStatus.PASS+"'");
		querySql.append(" and (ica.parent_id ='0' or ica.parent_id ='-1' ) ");
		//查询条件
		// 查询姓名
		if (StringUtils.isNotBlank(vo.getCusName())) {
			querySql.append(" and c.NAME like :name ");
			params.put("name", "%"+vo.getCusName()+"%");
		}
		// 联系方式
		if (StringUtils.isNotBlank(vo.getContact())) {
			querySql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(vo.getResEntranceId())) {
			querySql.append(" and c.RES_ENTRANCE = :resEntranceId ");
			params.put("resEntranceId", vo.getResEntranceId());
		}
		// 资源大类
		if (StringUtils.isNotBlank(vo.getCusOrg())) {
			querySql.append(" and c.CUS_ORG = :cusOrg ");
			params.put("cusOrg", vo.getCusOrg());
		}
		// 资源细分
		if (StringUtils.isNotBlank(vo.getCusType())) {
			querySql.append(" and c.CUS_TYPE = :cusType ");
			params.put("cusType", vo.getCusType());
		}
		//去掉 2016-11-17
//		// 处理状态
//		if (vo.getDealStatus() != null) {
//			if (StringUtils.isNotBlank(vo.getDealStatus().getValue())) {
//				querySql.append(" and c.DEAL_STATUS = '" + vo.getDealStatus().getValue() + "' ");
//			}
//		}
		// 是否上门
		if (StringUtils.isNotBlank(vo.getIsAppointCome())) {
			// 如果是预约上门
			if ("1".equals(vo.getIsAppointCome())) {
				//querySql.append("and (c.VISIT_TYPE is not NULL && c.VISIT_TYPE <>'NOT_COME' )");
				querySql.append("and c.VISIT_TYPE = 'PARENT_COME' ");
			}
			// 非预约上门
			if ("0".equals(vo.getIsAppointCome())) {
				querySql.append("and (c.VISIT_TYPE is NULL || c.VISIT_TYPE = 'NOT_COME' )");
			}
		}
		
		//增加过滤条件
		Map<String, String> map_id = getNetWorkGroupOUTCALLOrgId();
		Organization organization = userService.getUserMainDeptByUserId(user.getUserId());
		String orgId=organization.getId();
		
		if (map_id.get("network") != null && orgId.equals(map_id.get("network"))) {
			//网络 不加设置限制
            
		} else if (map_id.get("outcall") != null && orgId.equals(map_id.get("outcall"))) {
			Boolean isWHZY = userService.isUserRoleCode(user.getUserId(), RoleCode.OUTCALL_SPEC);		
			if (isWHZY) {
				organization = userService.getBelongBranchByUserId(user.getUserId());
			} else {
				organization = userService.getBelongCampusByUserId(user.getUserId());
			}
			String orgLevel = organization.getOrgLevel();
			params.put("orgLevel",  orgLevel+"%");
			querySql.append(
					" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
		} else {
			organization = userService.getBelongCampusByUserId(user.getUserId());
			String orgLevel = organization.getOrgLevel();
			params.put("orgLevel",  orgLevel+"%");
			querySql.append(
					" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
		}
		
		
		
		// 分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord()) && StringUtils.isNotBlank(dataPackage.getSidx())) {
			querySql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());			
		}
		List<Map<Object, Object>> list = customerDao.findMapOfPageBySql(querySql.toString(), dataPackage,params);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + querySql.toString() + " ) countall ",params));

		// 将查询结果转换为vo
		List<FollowupCustomerVo> voList = new ArrayList<FollowupCustomerVo>();
		FollowupCustomerVo fVo = null ;
		int i = 0;
		Map<String, Object> pMap = Maps.newHashMap();
		for (Map<Object, Object> tmaps: list) {
			i = i+1;
			Map<String, String> maps =(Map)tmaps;
		    fVo = new FollowupCustomerVo();
			String cusId =maps.get("cusId");
			fVo.setCusId(cusId!=null ? cusId:"");
			fVo.setCusName(maps.get("cusName")!=null? maps.get("cusName"):"");
			//联系方式
			fVo.setContact(maps.get("contact")!=null? maps.get("contact"):"");
			//设置资源入口
			fVo.setResEntranceId(maps.get("resEntrance"));
			fVo.setResEntranceName(maps.get("resEntranceName"));
			//资源细分
			fVo.setCusType(maps.get("cusTypeName"));
			//资源来源 
			fVo.setCusOrg(maps.get("cusOrgName"));
			//设置意向度
			fVo.setIntentionOfTheCustomerId(maps.get("intentionOfTheCustomer"));
			fVo.setIntentionOfTheCustomerName(maps.get("intentionName"));

			//去掉 2016-11-17
//			//处理状态			
//			if (maps.get("dealStatus") != null) {
//				String dealStatus = maps.get("dealStatus");
//				fVo.setDealStatus(CustomerDealStatus.valueOf(dealStatus));
//				fVo.setDealStatusName(CustomerDealStatus.valueOf(dealStatus) == null ? ""
//						: CustomerDealStatus.valueOf(dealStatus).getName());
//			}
			
			//设置最后分配人名称
			fVo.setLastDeliverName(maps.get("lastDeliverName")!=null ? maps.get("lastDeliverName"):"");


	        
            //是否上门和设置最新跟进记录
			StringBuilder query =new StringBuilder(" select cf.remark as remark from customer_folowup cf where appointment_type ='"+AppointmentType.FOLLOW_UP.getValue()+"' ");
			query.append(" and cf.customer_id ='"+cusId+"'  ORDER BY cf.create_time desc LIMIT 1 ");			
			List<Map<Object,Object>> customerFolowups=customerFolowupDao.findMapBySql(query.toString(),pMap);
			if (customerFolowups.size() > 0) {
				fVo.setLastRemark(customerFolowups.get(0).get("remark")!=null?customerFolowups.get(0).get("remark").toString():"");
			}
			
			//是否上门和最新跟进是否预约上门没有关系 是只要客户上门登记以后都是上门 
			//根据cusId获取Customer
//			Customer customer = customerDao.findById(cusId);
//			if(customer.getVisitType()==null||(customer.getVisitType() != null && customer.getVisitType().equals(VisitType.NOT_COME))){
//				fVo.setIsAppointCome("否");
//			}else{
//				fVo.setIsAppointCome("是");
//			}
			String visitType = maps.get("visitType");
			if(visitType==null||visitType!=null && visitType.equals(VisitType.NOT_COME.getValue())){
				fVo.setIsAppointCome("否");
			}else{
				fVo.setIsAppointCome("是");
			}
            //无效标记时间
			fVo.setInvalidCreateTime(maps.get("invalidCreateTime")!=null?maps.get("invalidCreateTime"):"");
			//审核状态
			if(maps.get("invalidAuditStatus")!=null){
				fVo.setInvalidAuditStatus(CustomerAuditStatus.valueOf(maps.get("invalidAuditStatus")).getName());
			}else{
				fVo.setInvalidAuditStatus("");
			}
			
			//无效审核意见
			fVo.setInvalidAuditRemark(maps.get("invalidAuditRemark")!=null?maps.get("invalidAuditRemark"):"");
	        voList.add(fVo);			
		}
		dataPackage.setDatas(voList);

		return dataPackage;
	}
	
	@Override
	public Map<String, Object> getCustomerOverviewForPlat(String startDate, String endDate, String workbrenchType) {
		//外呼 网络 咨询师工作台  //根据不同的的资源入口  
		//新增 待跟进 跟进中 已上门 已签单 释放 无效  查询七种数据
		//待跟进  跟进中 查询其他表    其他五个查询表 customer_dynamic_status 
		String userId = userService.getCurrentLoginUser().getUserId();
		Map<String, Object> resultMap = new HashMap<String,Object>();
		StringBuilder sql = new StringBuilder(512);
		sql.append(" select sum(case when DYNAMIC_STATUS_TYPE='NEW' then 1 else 0 end) as news, ");//新增数
		sql.append(" sum(case when DYNAMIC_STATUS_TYPE='VISITCOME' then 1 else 0 end) as visitcome, ");//上门数
		sql.append(" sum(case when DYNAMIC_STATUS_TYPE='CONTRACT_SIGN' then 1 else 0 end) as sign,");//签单数
		sql.append(" sum(case when DYNAMIC_STATUS_TYPE='RELEASECUSTOMER' then 1 else 0 end) as `release` ");
		//sql.append(" sum(case when DYNAMIC_STATUS_TYPE='INVALID' then 1 else 0 end) as `invalid` ");
		sql.append(" from customer_dynamic_status where 1=1 ");
		
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(startDate)){
			sql.append(" and OCCOUR_TIME>=:startDate ");			
			params.put("startDate", startDate+" 00:00:00");
		}
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and OCCOUR_TIME<=:endDate ");			
			params.put("endDate", endDate+" 23:59:59");
		}
		sql.append(" and referuser_id= '"+userId+"' ");		
		List<Map<Object,Object>> list = customerDao.findMapBySql(sql.toString(),params);
		if(list.size()>0){
			Map<Object, Object> map=list.get(0);
			resultMap.put("news", map.get("news"));
			resultMap.put("visitcome", map.get("visitcome"));
			resultMap.put("sign", map.get("sign"));
			resultMap.put("release", map.get("release"));
			//resultMap.put("invalid", map.get("invalid"));
		}
		
		//无效的统计
		StringBuilder query_invalid = new StringBuilder(128);
		query_invalid.append(" select count(1)  as `invalid` from invalid_customer_audit ica where ica.parent_id ='0' ");
		if(StringUtils.isNotBlank(startDate)){
			query_invalid.append(" and ica.create_time>= :startDate ");
		}
		if(StringUtils.isNotBlank(endDate)){
			query_invalid.append(" and ica.create_time<= :endDate ");
		}
		query_invalid.append(" and ica.create_user_id = '"+userId+"' ");
		int invalidResult = customerDao.findCountSql(query_invalid.toString(),params);
		resultMap.put("invalid", invalidResult);
		
		//待跟进
		//外呼 网络  咨询师  都是分配给他们，并且customer dealstatus = stay_follow 
		StringBuilder query_stayfollow =new StringBuilder("select count(*) as stayfollow from customer c where c.DEAL_STATUS ='STAY_FOLLOW'");
		query_stayfollow.append(" and c.DELEVER_TARGET = '"+userId+"' ");
		if(StringUtils.isNotBlank(startDate)){
			query_stayfollow.append(" and c.DELIVER_TIME>= :startDate ");
		}
		if(StringUtils.isNotBlank(endDate)){
			query_stayfollow.append(" and c.DELIVER_TIME<= :endDate ");		
		}
		int stayfollowResult = customerDao.findCountSql(query_stayfollow.toString(),params);
		
		resultMap.put("stayfollow", stayfollowResult);
		
		//跟进中
		//外呼 网络 
		StringBuilder following = new StringBuilder("select count(*) as following from customer c where c.DEAL_STATUS ='FOLLOWING'");
		if(workbrenchType.equals(WorkbrenchType.OUTCALL.getValue())||workbrenchType.equals(WorkbrenchType.ON_LINE.getValue())){
			following.append(" and ( c.BEFOR_DELIVER_TARGET ='"+userId+"' or c.DELEVER_TARGET = '"+userId+"' ) ");
		}else if(workbrenchType.equals(WorkbrenchType.CALL_OUT.getValue())){
			following.append(" and c.DELEVER_TARGET = '"+userId+"' ");
		}
		if(StringUtils.isNotBlank(startDate)){
			following.append(" and c.DELIVER_TIME>= :startDate ");
		}
		if(StringUtils.isNotBlank(endDate)){
			following.append(" and c.DELIVER_TIME<= :endDate ");		
		}
		int followingResult = customerDao.findCountSql(following.toString(),params);		
		resultMap.put("following", followingResult);
		
		return resultMap;
	}
	@Override
	public Map<String, Object> getCustomerOverviewForReception(String startDate, String endDate) {
		//前台的查询   按校区为单位 不是按某个前台  所有前台的功能是共享的	
		String blCampusId = userService.getBelongCampus().getOrgLevel();
		
		//查询的数据种类
		//待分配  已分配  新增   转出  接收
		Map<String, Object> resultMap = new HashMap<String,Object>();
	    
		Map<String, Object> params = Maps.newHashMap();
		params.put("startDate", startDate+" 00:00:00");
		params.put("endDate", endDate+" 23:59:59");
		params.put("orgLevel", blCampusId+"%");
		//已分配
		//经前台分配咨询师的客户（重新分配也算分配数）
		//包括外呼 网络 预约登记分配咨询师  呼入直访拉访录入分配咨询师 呼出重新分配咨询师
		StringBuilder assigned = new StringBuilder(256);
		assigned.append(" select count(*) as assigned from delivertarget_change_record d left join customer c ");
		assigned.append(" on d.customer_id = c.id where c.DEAL_STATUS <> 'NEW' ");
		assigned.append(" and c.BL_CAMPUS_ID in (select id  from organization  where orgLevel like :orgLevel ) ");
		//assigned.append(" and (d.previous_target ='"+currentUserId+"' or d.create_user_id ='"+currentUserId+"' )");
		//这里的权限不止是currentUserId 而是这个校区的所有的前台都能看到
		
		
		if(StringUtils.isNotBlank(startDate)){
			assigned.append(" and c.DELIVER_TIME>= :startDate ");
		}
		if(StringUtils.isNotBlank(endDate)){
			assigned.append(" and c.DELIVER_TIME<= :endDate ");		
		}
		int assignedResult = customerDao.findCountSql(assigned.toString(),params);

		resultMap.put("assign", assignedResult);

		//待分配
		//前台所在的校区 网络预约校区的
		StringBuilder toassign = new StringBuilder(256);
		toassign.append(" select count(*) as toassign from customer_folowup cf left join customer c on  cf.CUSTOMER_ID = c.ID  ");
		toassign.append(" where cf.APPOINTMENT_TYPE ='"+AppointmentType.APPOINTMENT_ONLINE.getValue()+"' ");
		toassign.append(" and c.DEAL_STATUS = 'NEW'");
        toassign.append(" and c.BL_CAMPUS_ID in (select id  from organization  where orgLevel like :orgLevel ) ");
		if(StringUtils.isNotBlank(startDate)){
			assigned.append(" and c.DELIVER_TIME>= :startDate ");
		}
        if(StringUtils.isNotBlank(endDate)){
			toassign.append(" and c.DELIVER_TIME<= :endDate ");		
		}
		int toassignResult = customerDao.findCountSql(toassign.toString(),params);
		resultMap.put("toassign", toassignResult);
	
		//调配 就是前台经手的抢客记录     
		StringBuilder gainCustomer = new StringBuilder(256);
		gainCustomer.append(" select count(*) as gainCustomer from gain_customer gc left join customer c ");
		gainCustomer.append(" on gc.CUS_ID = c.ID where c.BL_CAMPUS_ID in (select id  from organization  where orgLevel like '"+ blCampusId +"%') ");
		if(StringUtils.isNotBlank(startDate)){
			gainCustomer.append(" and gc.CREATE_TIME >= :startDate ");
		}
		if(StringUtils.isNotBlank(endDate)){
			gainCustomer.append(" and gc.CREATE_TIME <= :endDate ");		
		}
		int gainCustomerResult = customerDao.findCountSql(gainCustomer.toString(),params);
		resultMap.put("gainCustomer", gainCustomerResult);
			

		//转出  不管对方是否接收
		//当前前台校区的Id
		String currentBlCampusId = userService.getBelongCampus().getId();
		params.put("currentBlCampusId", currentBlCampusId);
		StringBuilder transferOut = new StringBuilder(128);
		transferOut.append(" select count(*) as transferOut from transfer_customer tc ");	
		transferOut.append(" where tc.TRANSFER_CAMPUS= :currentBlCampusId ");
		if(StringUtils.isNotBlank(startDate)){
			transferOut.append(" and tc.CREATE_TIME >= :startDate ");
		}
		if(StringUtils.isNotBlank(endDate)){
			transferOut.append(" and tc.CREATE_TIME <= :endDate ");		
		}	
		int transferOutResult = customerDao.findCountSql(transferOut.toString(),params); 

		resultMap.put("transferOut", transferOutResult);
	
		
		//接收  转入并且接收
		StringBuilder transferIn = new StringBuilder(256);
		transferIn.append(" select count(*) as transferIn from transfer_customer tc left join customer c ");
        transferIn.append(" on tc.CUS_ID = c.ID where c.TRANSFER_STATUS ='1' ");       
        transferIn.append(" and tc.RECEIVE_CAMPUS= :currentBlCampusId ");
		if(StringUtils.isNotBlank(startDate)){
			transferIn.append(" and tc.RECEIVE_TIME >= :startDate ");
		}
		if(StringUtils.isNotBlank(endDate)){
			transferIn.append(" and tc.RECEIVE_TIME <= :endDate ");		
		}  
		int transferInResult  = customerDao.findCountSql(transferIn.toString(),params);
		resultMap.put("transferIn", transferInResult);
	
		//新增  = 转入总数+分配总数
		//转入总数	
		resultMap.put("new", transferInResult+assignedResult+toassignResult);
	
		return resultMap;
	}
	
	@Override
	public DataPackage getSignupCustomers(SignupCustomerVo vo, DataPackage dataPackage) {
		//签单客户列表
		//一个客户多个学生 几个学生就几条记录
		
		User currentUser = userService.getCurrentLoginUser();
		Boolean isWHZY = false;
		Boolean isWLZY = false;
		List<Role> roles = userService.getRoleByUserId(currentUser.getUserId());
		if(roles!=null && roles.size()>0){
			for(Role role : roles){
				if(RoleCode.OUTCALL_SPEC == role.getRoleCode()){
					isWHZY = true;
				}
				if(RoleCode.NETWORK_SPEC == role.getRoleCode()){
					isWLZY = true;
				}
			}
		}

		StringBuilder cusSql= new StringBuilder(512);
		StringBuffer countSql = new StringBuffer(512);
		
		countSql.append(" select count(1) from customer c ");
		countSql.append(" left join customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
		countSql.append(" left join student s on csr.student_id=s.ID ");
		if(isWHZY||isWLZY){
			countSql.append(" left join (select cono.STUDENT_ID,cono.CREATE_TIME,cono.CREATE_USER_ID from contract cono group by cono.STUDENT_ID )con on con.STUDENT_ID=s.ID ");
		}else{
			countSql.append(" left join (select cono.STUDENT_ID,cono.CREATE_TIME,cono.CREATE_USER_ID from contract cono where cono.create_user_id= :userId group by cono.STUDENT_ID )con on con.STUDENT_ID=s.ID ");
		}
		
		
		cusSql.append(" select c.id customerId, c.name customerName,c.contact,s.id studentId,s.name studentName,dd.NAME gradeName,u.name studyManagerName,s.`status` status ");
		cusSql.append(" ,con.CREATE_TIME create_time from customer c ");
		cusSql.append(" left join customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
		cusSql.append(" left join student s on csr.student_id=s.ID ");
		cusSql.append(" left join user u on u.user_id=s.STUDY_MANEGER_ID ");
		cusSql.append(" left join data_dict dd on dd.id=s.GRADE_id ");
		if(isWHZY||isWLZY){
		    cusSql.append(" left join (select cono.STUDENT_ID,cono.CREATE_TIME,cono.CREATE_USER_ID from contract cono group by cono.STUDENT_ID )con on con.STUDENT_ID=s.ID ");
		}else{
			cusSql.append(" left join (select cono.STUDENT_ID,cono.CREATE_TIME,cono.CREATE_USER_ID from contract cono where cono.create_user_id= :userId group by cono.STUDENT_ID )con on con.STUDENT_ID=s.ID ");
		}
		cusSql.append(" where 1=1 ");
		cusSql.append("  and s.student_type='ENROLLED' ");
		countSql.append(" where 1=1 ");
		countSql.append("  and s.student_type='ENROLLED' ");
		
		
		
//		Boolean isWHZY = userService.isUserRoleCode(currentUser.getUserId(), RoleCode.OUTCALL_SPEC);
//		Boolean isWLZY = userService.isUserRoleCode(currentUser.getUserId(), RoleCode.NETWORK_SPEC);
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", currentUser.getUserId());
		
		Map<String, String> map_id = getNetWorkGroupOUTCALLOrgId();
		Organization organization = userService.getUserMainDeptByUserId(currentUser.getUserId());
		String orgId=organization.getId();
		Organization blCampus = userService.getBelongCampusByUserId(currentUser.getUserId());
		if (map_id.get("network") != null && orgId.equals(map_id.get("network"))) {
			if (isWLZY) {
				cusSql.append(" and c.BEFOR_DELIVER_TARGET = :userId ");
				countSql.append(" and c.BEFOR_DELIVER_TARGET = :userId ");
			} else {				
				organization = userService.getBelongCampusByUserId(currentUser.getUserId());
				String orgLevel = organization.getOrgLevel();
				params.put("orgLevel",  orgLevel+"%");
				cusSql.append(" and con.create_user_id= :userId ");
				cusSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
				countSql.append("  and con.create_user_id= :userId ");
				countSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
			}

		} else if (map_id.get("outcall") != null && orgId.equals(map_id.get("outcall"))) {
			if (isWHZY) {				
				organization = userService.getBelongBranchByUserId(currentUser.getUserId());
				String orgLevel = organization.getOrgLevel();
				params.put("orgLevel",  orgLevel+"%");
				cusSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
				countSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");		
				cusSql.append(" and c.BEFOR_DELIVER_TARGET = :userId ");
				countSql.append(" and c.BEFOR_DELIVER_TARGET = :userId ");
			} else {				
				String orgLevel = blCampus.getOrgLevel();
				params.put("orgLevel",  orgLevel+"%");				
				cusSql.append(" and con.create_user_id= :userId ");
				cusSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
				countSql.append("  and con.create_user_id= :userId ");
				countSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
			}
		} else {
			String orgLevel = blCampus.getOrgLevel();
			params.put("orgLevel",  orgLevel+"%");				
			cusSql.append(" and con.create_user_id= :userId ");
			cusSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
			countSql.append("  and con.create_user_id= :userId ");
			countSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
		}
//		if(isWHZY||isWLZY){
//			cusSql.append(" and c.BEFOR_DELIVER_TARGET = :userId ");
//			countSql.append(" and c.BEFOR_DELIVER_TARGET = :userId ");
//		}else{
//			cusSql.append("  and con.create_user_id= :userId ");
//			countSql.append("  and con.create_user_id= :userId ");
//		}
		
        //客户姓名
		if(StringUtils.isNotBlank(vo.getCusName())){
			cusSql.append(" and c.name like :name ");
			countSql.append(" and c.name like :name ");
			params.put("name", "%"+vo.getCusName()+"%");
		}		
		//联系方式        
		if(StringUtils.isNotBlank(vo.getContact())){
			cusSql.append(" and c.contact like :contact ");
			countSql.append(" and c.contact like :contact ");
			params.put("contact", "%"+vo.getContact()+"%");
		}		         
		//学生姓名      
		if(StringUtils.isNotBlank(vo.getStudentName())){
			cusSql.append(" and s.name like :studentName ");
			countSql.append(" and s.name like :studentName ");
			params.put("studentName", "%"+vo.getStudentName()+"%");
		}		 
		//学生年级
		if(StringUtils.isNotBlank(vo.getGradeId())){
			cusSql.append(" and s.Grade_id = :gradeId ");
			countSql.append(" and s.Grade_id = :gradeId ");
			params.put("gradeId", vo.getGradeId());
		}		
		//学生状态 
		if(StringUtils.isNotBlank(vo.getStudentStatus())){
			cusSql.append(" and s.STATUS = :studentStatus ");
			countSql.append(" and s.STATUS = :studentStatus ");
			params.put("studentStatus", vo.getStudentStatus());
		}
		
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
		}else {
			cusSql.append(" order by con.CREATE_TIME  desc ");
		}
		long time1 = System.currentTimeMillis();
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
		long time2 = System.currentTimeMillis();
		System.out.println("time2-time1:"+(time2-time1)+"ms");
		dataPackage.setRowCount(customerDao.findCountSql(countSql.toString(),params));	
		long time3 = System.currentTimeMillis();
		System.out.println("time3-time2:"+(time3-time2)+"ms");
		//将查询结果转换为vo		
		List<SignupCustomerVo> voList =new ArrayList<SignupCustomerVo>();
		SignupCustomerVo customerVo = null;
		for(Map<Object,Object> tmaps : list){
			Map<String,String> maps =(Map)tmaps;
		    customerVo = new SignupCustomerVo();
			//客户姓名
			customerVo.setCusId(maps.get("customerId")!=null?maps.get("customerId"):"");
			customerVo.setCusName(maps.get("customerName")!=null?maps.get("customerName"):"");
			//联系方式
			customerVo.setContact(maps.get("contact")!=null?maps.get("contact"):"");
			//学生姓名  这里只有一个学生姓名所以不用studentNameList
			customerVo.setStudentId(maps.get("studentId")!=null?maps.get("studentId"):"");
			customerVo.setStudentName(maps.get("studentName")!=null?maps.get("studentName"):"");
			//学生年级
			customerVo.setGradeName(maps.get("gradeName")!=null?maps.get("gradeName"):"");
			//学管师
			customerVo.setStudyManegerName(maps.get("studyManagerName")!=null?maps.get("studyManagerName"):"");
			//学生状态
			if(maps.get("status")!=null){
				String status = maps.get("status");
				customerVo.setStudentStatus(status);
				customerVo.setStudentStatusName(StudentStatus.valueOf(status).getName());
			}else{
				customerVo.setStudentStatus("");
				customerVo.setStudentStatusName("");
			}
			if(maps.get("create_time")!=null){
				customerVo.setDays(DateTools.daysBetween(maps.get("create_time"), DateTools.getCurrentDateTime()));
			}
			

			voList.add(customerVo);
		}
		dataPackage.setDatas(voList);
        return dataPackage;
	}
	
	@Override
	public DataPackage getDistributeCustomers(DistributeCustomerVo vo, DataPackage dataPackage) {
		//前台是属于这个校区的前台都能看到    
		//查询条件语句
		StringBuilder conSql= new StringBuilder(256);
		//整个查询条件
		StringBuilder cusSql= new StringBuilder(1024);
		//查询 前台平台的分配客户列表  
		//当前登录者的所属的组织架构 前台所在的校区
		String blCampusId =userService.getBelongCampus().getOrgLevel();
		//查询条件
		//客户姓名
		Map<String, Object> params = Maps.newHashMap(); 
		if(StringUtils.isNotBlank(vo.getCusName())){
			conSql.append(" and c.name like :name ");
			params.put("name", "%"+vo.getCusName()+"%");
		}
		//联系方式
		if(StringUtil.isNotBlank(vo.getContact())){
			conSql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(vo.getResEntranceId())) {
			conSql.append(" and c.RES_ENTRANCE= :resEntranceId ");
            params.put("resEntranceId", vo.getResEntranceId());
		}
        //分配人(最新)
		if(StringUtils.isNotBlank(vo.getLastDeliverName())){
			conSql.append(" and c.LAST_DELIVER_NAME like :lastDeliverName ");
			params.put("lastDeliverName", "%"+vo.getLastDeliverName()+"%");
		}
		//意向度
		if (StringUtils.isNotBlank(vo.getIntentionOfTheCustomerId())) {
			conSql.append(" and c.INTENTION_OF_THE_CUSTOMER = :intentionOfTheCustomerId ");
			params.put("intentionOfTheCustomerId", vo.getIntentionOfTheCustomerId());
		}
		//跟进备注
		if(StringUtils.isNotBlank(vo.getRemark())){
			conSql.append(" and cf.REMARK like :remark ");
			params.put("remark", "%"+vo.getRemark()+"%");
		}
		//分配状态    不是new 也不是stay follow 
		if(StringUtils.isNotBlank(vo.getIsDistribute())){
			if(vo.getIsDistribute().equals("1")){
				//已经分配
				conSql.append(" and cf.DEAL_STATUS <> 'STAY_FOLLOW' ");
				//conSql.append(" and c.DEAL_STATUS <> 'STAY_FOLLOW' ");
			}
			if(vo.getIsDistribute().equals("0")){
				//conSql.append(" and (c.DEAL_STATUS ='NEW' or c.DEAL_STATUS ='STAY_FOLLOW' ) ");
				conSql.append(" and (cf.DEAL_STATUS ='STAY_FOLLOW' ) ");
			}
		}
		if(StringUtil.isNotBlank(vo.getDealStatus())){
			conSql.append(" and (c.DEAL_STATUS = :dealStatus ) ");
			params.put("dealStatus", vo.getDealStatus());
		}
		//分配开始时间
		if(StringUtils.isNotBlank(vo.getDeliverBeginDate())){
			 conSql.append(" and c.DELIVER_TIME >= :deliverBeginDate ");
			 params.put("deliverBeginDate", vo.getDeliverBeginDate()+" 00:00:00");
		}
		//分配结束时间
		if(StringUtils.isNotBlank(vo.getDeliverEndDate())){
			 conSql.append(" and c.DELIVER_TIME <= :deliverEndDate ");
			 params.put("deliverEndDate", vo.getDeliverEndDate()+" 23:59:59");
		}
		
		cusSql.append(" select cf.ID as customerFollowupId,c.id as cusId,c.name as cusName,c.DEAL_STATUS as dealStatus,c.CONTACT as contact,");
		cusSql.append(" cf.REMARK as remark,c.LAST_DELIVER_NAME as lastDeliverName,c.DELIVER_TIME as deliverTime,");
		cusSql.append(" c.RES_ENTRANCE as resEntrance,c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer ");
		cusSql.append(",cf.DEAL_STATUS as FollowupDealStatus ");
		cusSql.append(" , d.`NAME` as resEntranceName ,dd.`NAME` as intentionName ");
		// 待分配的状态是New 和 前台所在的校区
		cusSql.append(" from customer_folowup cf left join customer c on  cf.CUSTOMER_ID = c.ID ");
		
		cusSql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID "); 
		cusSql.append(" LEFT JOIN data_dict dd on c.INTENTION_OF_THE_CUSTOMER = dd.ID "); 
		
		cusSql.append(" where cf.APPOINTMENT_TYPE ='"+AppointmentType.APPOINTMENT_ONLINE_OPERATION.getValue()+"' ");
		cusSql.append(" and (cf.DEAL_STATUS ='"+CustomerDealStatus.STAY_FOLLOW.getValue()+"' or cf.DEAL_STATUS = '"+CustomerDealStatus.FOLLOWING.getValue()+"' )");
		cusSql.append(" and c.BL_CAMPUS_ID in (select id  from organization  where orgLevel like :blCampusId ) ");
		params.put("blCampusId",blCampusId + "%");
		if (StringUtils.isNotBlank(conSql.toString())) {
			cusSql.append(conSql.toString());
		}		
	
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());			
		}
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
	
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));	

		//将查询结果转换为vo		
		List<DistributeCustomerVo> voList = new ArrayList<DistributeCustomerVo>();
		DistributeCustomerVo customerVo = null;
		for (Map<Object,Object> tmaps : list) {
			Map<String, String> maps =(Map)tmaps;
		    customerVo = new DistributeCustomerVo();
		    
		    customerVo.setCustomerFollowupId(maps.get("customerFollowupId"));
		    
			String cusId = maps.get("cusId");
			customerVo.setCusId(cusId != null ? cusId : "");
			customerVo.setCusName(maps.get("cusName") != null ? maps.get("cusName") : "");
			customerVo.setContact(maps.get("contact"));

			// 设置资源入口
			customerVo.setResEntranceId(maps.get("resEntrance"));
			customerVo.setResEntranceName(maps.get("resEntranceName"));
			// 设置意向度
			customerVo.setIntentionOfTheCustomerId(maps.get("intentionOfTheCustomer"));
			customerVo.setIntentionOfTheCustomerName(maps.get("intentionName"));

			// 设置最后分配人名称
			customerVo.setLastDeliverName(maps.get("lastDeliverName") );
			// 设置最后分配时间
			customerVo.setDeliverTime(maps.get("deliverTime"));
			// 设置备注
			customerVo.setRemark(maps.get("remark"));
            //分配状态
			String dealStatus = maps.get("dealStatus");
			customerVo.setDealStatus(dealStatus);
			if(dealStatus!=null){
				customerVo.setDealStatusName(CustomerDealStatus.valueOf(dealStatus).getName());
			}else{
				customerVo.setDealStatusName("-");
			}
//			if("STAY_FOLLOW".equals(dealStatus)){
//				customerVo.setIsDistribute("已分配");
//			}
//			if("STAY_FOLLOW".equals(dealStatus)||"NEW".equals(dealStatus)){
			String followupDealStatus = maps.get("FollowupDealStatus");
			if("STAY_FOLLOW".equals(followupDealStatus)){
				customerVo.setIsDistribute("未分配");
			}else{
				customerVo.setIsDistribute("已分配");
			}
			voList.add(customerVo);
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}
	
	/**
	 * 转校客户管理
	 */
	@Override
	public DataPackage transferCustomerList(ReceptionistCustomerVo vo,DataPackage dp,String workbrenchType){
		StringBuilder sql  = new StringBuilder(1024);
		sql.append(" select tc.ID as transferId,c.ID as cusId,c.NAME as name,c.RES_ENTRANCE as resEntrance ,c.REMARK as remark ,c.CONTACT as contact, ");
		sql.append(" c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer, tc.TRANSFER_CAMPUS as transferCampus ,dddd.`NAME` as receiveCampus, ");
		sql.append(" c.TRANSFER_STATUS as transferStatus , tc.TRANSFER_TIME as transferTime,tc.RECEIVE_TIME as receiveTime,c.DEAL_STATUS as dealStatus ");
		
		sql.append(" ,d.`NAME` as resEntranceName,dd.`NAME` as intentionName,ddd.`NAME` as transferCampusName,oo.`name` as branchName ");
		sql.append(" from transfer_customer tc left join customer c on tc.CUS_ID = c.ID");
		
		sql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID ");
		sql.append(" LEFT JOIN data_dict dd on c.INTENTION_OF_THE_CUSTOMER = dd.ID  ");
		sql.append(" LEFT JOIN organization ddd on tc.TRANSFER_CAMPUS = ddd.ID  ");
		sql.append(" LEFT JOIN organization dddd on tc.RECEIVE_CAMPUS = dddd.ID  ");
		sql.append(" LEFT JOIN organization oo on ddd.parentID = oo.id ");
	
		sql.append(" where 1=1");
		
		Map<String, Object> params = Maps.newHashMap();
		//查询条件
		//客户姓名
		if(StringUtils.isNotBlank(vo.getName())){
			sql.append(" and c.NAME like :name ");
			params.put("name", "%"+vo.getName()+"%");
		}
		//联系方式
		if(StringUtil.isNotBlank(vo.getContact())){
			sql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
        //资源入口 
		if(StringUtils.isNotBlank(vo.getResEntranceId())){
			sql.append(" and c.RES_ENTRANCE = :resEntranceId ");
			params.put("resEntranceId", vo.getResEntranceId());
		}
		//备注
		if(StringUtils.isNotBlank(vo.getRemark())){
			sql.append(" and c.REMARK like :remark ");
			params.put("remark", "%"+vo.getRemark()+"%");
		}
		//意向度
		if(StringUtils.isNotBlank(vo.getScusSatisficing())){
			sql.append(" and c.INTENTION_OF_THE_CUSTOMER = :scusSatisficing ");
			params.put("scusSatisficing", vo.getScusSatisficing());
		}	
		//选择分公司
        if(StringUtil.isNotBlank(vo.getBrenchId())){
        	sql.append(" and (c.BL_CAMPUS_ID in( select id from organization where organization.parentID = :brenchId ) )");
        	params.put("brenchId", vo.getBrenchId());
        }
		
		//来源校区 
		if(StringUtils.isNotBlank(vo.getTransferFrom())){
			sql.append(" and tc.TRANSFER_CAMPUS = :transferFrom ");
			params.put("transferFrom", vo.getTransferFrom());
		}		
		// receiverOrTransfer = transfer  转出  receive  转入
		Boolean isTransfer = false;
		
		
		if(StringUtils.isNotBlank(vo.getReceiveOrTransfer()) && vo.getReceiveOrTransfer().equals("transfer")){
			isTransfer = true;
			//转出时间  transferTime   转出开始时间   转出结束时间   
			if(StringUtils.isNotBlank(vo.getStartDate())){
				sql.append(" and tc.TRANSFER_TIME >= :startDate ");
				params.put("startDate", vo.getStartDate()+" 00:00:00");
			}
			if(StringUtils.isNotBlank(vo.getEndDate())){
				sql.append(" and tc.TRANSFER_TIME <= :endDate ");	
				params.put("endDate", vo.getEndDate()+" 23:59:59");
			}

		}else{			
			//接收状态   转入才有接收状态的筛选条件
			if(StringUtils.isNotBlank(vo.getTransferStatus())){
				sql.append(" and c.TRANSFER_STATUS = :transferStatus ");
				params.put("transferStatus", vo.getTransferStatus());
			}
			//转入时间  receiveTime 转入开始时间   转入结束时间   
			if(StringUtils.isNotBlank(vo.getStartDate())){
				sql.append(" and tc.RECEIVE_TIME >= :startDate ");
				params.put("startDate", vo.getStartDate()+" 00:00:00");
			}
			if(StringUtils.isNotBlank(vo.getEndDate())){
				sql.append(" and tc.RECEIVE_TIME <= :endDate ");
				params.put("endDate", vo.getEndDate()+" 23:59:59");
			}			
		}
		//限制权限
		//前台
		if("RECEPTION".equalsIgnoreCase(workbrenchType)||"MANAGER".equalsIgnoreCase(workbrenchType)){
			//只能看到前台所在校区的 转校客户 
			Organization currentBlCampus = userService.getBelongCampus();
			params.put("currentBlCampus", currentBlCampus.getId());
	        if(isTransfer){
	        	sql.append(" and tc.TRANSFER_CAMPUS= :currentBlCampus ");
	        }else{
	        	sql.append(" and tc.RECEIVE_CAMPUS= :currentBlCampus ");
	        }
		}
//		//客户管理
//		if("CUSTOMERMANAGER".equalsIgnoreCase(workbrenchType)){
//		    //只能看到当前登录者所在组织架构下的所有校区的 转校客户 
//			List<Organization> organizations= userService.getCurrentLoginUser().getOrganization();
//			StringBuilder campusIds =new StringBuilder("");
//			for(Organization org:organizations){
//				Organization campus = userService.getBelongCampusByOrgId(org.getId());
//				campusIds.append(campus.getId()+",");
//			}
//			String  ids = campusIds.toString().substring(0, campusIds.length()-1);
//			campusIds.delete(0, campusIds.length());
//			campusIds.append(ids);
//	        if(isTransfer){
//	        	sql.append(" and tc.TRANSFER_CAMPUS in ("+campusIds.toString()+") ");
//	        }else{
//	        	sql.append(" and tc.RECEIVE_CAMPUS  in ("+campusIds.toString()+") ");
//	        }
//			
//		}
		//排序 按时间的进行降序排序
		if(isTransfer){
			sql.append(" and tc.TRANSFER_TIME IS NOT NULL ");
			sql.append(" ORDER BY tc.TRANSFER_TIME DESC ");			
		}else{
			//sql.append(" and tc.RECEIVE_TIME IS NOT NULL ");
			sql.append(" ORDER BY tc.RECEIVE_TIME DESC ");			
		}
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(sql.toString(), dp,params);
		dp.setRowCount(customerDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ",params));	

		//将查询结果转换为vo	
		List<ReceptionistCustomerVo> volist = new ArrayList<ReceptionistCustomerVo>();
		ReceptionistCustomerVo customerVo = null;
		for(Map<Object,Object> tmaps : list){
			Map<String,String> maps =(Map)tmaps;
		    customerVo = new ReceptionistCustomerVo();
			//转校记录Id
		    customerVo.setId(maps.get("transferId")!=null ? maps.get("transferId"):"");
			customerVo.setTransferRecordId(maps.get("transferId")!=null ? maps.get("transferId"):"");
			customerVo.setCustomerId(maps.get("cusId")!=null ? maps.get("cusId"):"");
			customerVo.setName(maps.get("name")!= null ? maps.get("name"):"");
			customerVo.setContact(maps.get("contact")!=null?maps.get("contact"):"");
			customerVo.setRemark(maps.get("remark")!=null? maps.get("remark"):"");
			
			customerVo.setResEntranceId(maps.get("resEntrance"));
			customerVo.setResEntranceName(maps.get("resEntranceName"));
			customerVo.setScusSatisficing(maps.get("intentionName"));
			customerVo.setBranchName(maps.get("branchName"));
//			if(maps.get("resEntrance")!=null){
//				String resEntrance = maps.get("resEntrance");
//				customerVo.setResEntranceId(resEntrance);
//				DataDict dataDict = dataDictDao.findById(resEntrance);
//				customerVo.setResEntranceName(dataDict.getName());
//			}
//			//意向度
//			String intentionId = maps.get("intentionOfTheCustomer");
//			if (intentionId != null) {
//				DataDict dataDict_intention = dataDictDao.findById(intentionId);
//				if (dataDict_intention != null) {
//					customerVo.setScusSatisficing(dataDict_intention.getName());
//				}
//			}else{
//				customerVo.setScusSatisficing("");
//			}
			customerVo.setDealStatus(maps.get("dealStatus")!=null?maps.get("dealStatus"):"");
			customerVo.setReceiveCampus(maps.get("receiveCampus")!=null?maps.get("receiveCampus"):"");
			customerVo.setTransferFrom(maps.get("transferCampusName")!=null?maps.get("transferCampusName"):"");
			String transferStatus = maps.get("transferStatus");
            if(transferStatus!=null && transferStatus.equals("1")){
            	customerVo.setTransferStatus("已接收");
            }
            if(transferStatus!=null && transferStatus.equals("0")&& maps.get("receiveTime")==null){
            	customerVo.setTransferStatus("未接收");
            }
            customerVo.setTransferTime(maps.get("transferTime")!=null?maps.get("transferTime"):"");
            customerVo.setReceiveTime(maps.get("receiveTime")!=null?maps.get("receiveTime"):"");
            
            volist.add(customerVo);		
		}
		dp.setDatas(volist);
		return dp;
		
		
	}
	
	/**
	 * 前台客户端 登记按钮加载来访客户新消息  需要根据来访时间来判断是否修改 来访客户的资源入口
	 */
	@Override
	public SignCustomerVo loadFollowupCustomerById(String visitId,CustomerVo customerVo) {
		if(StringUtils.isNotBlank(visitId)){
			//StringBuilder sql = new StringBuilder(256);	
			SignCustomerVo signCustomerVo = new SignCustomerVo();
			signCustomerVo.setVisitId(visitId);
			signCustomerVo.setCustomerId(customerVo.getId());
			signCustomerVo.setContact(customerVo.getContact());
			signCustomerVo.setName(customerVo.getName());
			signCustomerVo.setCusOrg(customerVo.getCusOrg());
			if(customerVo.getCusOrg()!=null){
				DataDict cusOrg =dataDictDao.findById(customerVo.getCusOrg());
				if(cusOrg!=null){
					signCustomerVo.setCusOrgName(cusOrg.getName());
				}else{
					signCustomerVo.setCusOrgName("");
				}
			}
			signCustomerVo.setCusType(customerVo.getCusType());
			if(customerVo.getCusType()!=null){
				DataDict cusType =dataDictDao.findById(customerVo.getCusType());
				if(cusType!=null){
					signCustomerVo.setCusTypeName(cusType.getName());
				}else{
					signCustomerVo.setCusTypeName("");
				}
			}

			
			CustomerFolowup ca=customerFolowupDao.findById(visitId); //跟进记录
			
			//不用再根据预约人的职位来生效资源入口 20170810
			Customer customer = customerDao.findById(customerVo.getId());			
            DataDict resEntrance =customer.getResEntrance();
            if(resEntrance!=null){
            	signCustomerVo.setResEntranceId(resEntrance.getId());
            	signCustomerVo.setResEntranceName(resEntrance.getName());
            }else{
            	signCustomerVo.setResEntranceId("");
            	signCustomerVo.setResEntranceName("");
            }
			
//			String userId = ca.getCreateUser().getUserId();
//			Boolean isZXS = userService.isUserRoleCode(userId, RoleCode.CONSULTOR);
//			Boolean isZXZG = userService.isUserRoleCode(userId, RoleCode.CONSULTOR_DIRECTOR);
//			isZXS = isZXS||isZXZG;
//			Boolean isWHZG = userService.isUserRoleCode(userId, RoleCode.OUTCALL_MANAGER);
//			Boolean isBWHZG = userService.isUserRoleCode(userId, RoleCode.BRANCH_OUTCALL_MANAGER);
//			Boolean isWHZY = userService.isUserRoleCode(userId, RoleCode.OUTCALL_SPEC);
//			//如果preEntranceId为空  是市场部录入可以资源入口为空 则根据预约人的职位来设置资源入口
//			if(StringUtils.isBlank(customerVo.getPreEntranceId())){
//				if(isWHZG||isWHZY||isBWHZG){					
//				    signCustomerVo.setResEntranceId("OUTCALL");
//				    signCustomerVo.setResEntranceName(ResEntranceType.OUTCALL.getName());
//				}
//				if(isZXS){
//					signCustomerVo.setResEntranceId("CALL_OUT");
//					signCustomerVo.setResEntranceName(ResEntranceType.CALL_OUT.getName());
//				}
//			}			
//			String resEntranceId  = customerVo.getResEntranceId();
//			if(resEntranceId==null)resEntranceId = customerVo.getPreEntranceId();		
//			String resEntranceId  = customerVo.getPreEntranceId();
//			
//			if(resEntranceId!=null){
//				signCustomerVo.setResEntranceId(resEntranceId);
//				DataDict res = dataDictDao.findById(resEntranceId);
//				if(res!=null){
//					signCustomerVo.setResEntranceName(res.getName());
//				}else{
//					signCustomerVo.setResEntranceName("");
//				}
//			}
			
			//解锁资源入口 换逻辑
			//获取最新的记录
			//cf.getCreateUserId()
//			List<CustomerFolowup> list = customerFolowupDao.findBySql(sql.toString(),params);
//			if(list!=null && list.size()>0){
//				CustomerFolowup follow=list.get(0);
//				//当前的时间与最近一次登记的时间比较是否超过三十天
//				int days =DateTools.daysOfTwo(follow.getMeetingConfirmTime(), DateTools.getCurrentDateTime());
//				if(days > 30){
//					//根据预约人的角色来判断
//					if(isWHZG||isWHZY){						
//					    signCustomerVo.setResEntranceId("OUTCALL");
//					    signCustomerVo.setResEntranceName(ResEntranceType.OUTCALL.getName());
//					}
//					if(isZXS){	
//						signCustomerVo.setResEntranceId("CALL_OUT");
//						signCustomerVo.setResEntranceName(ResEntranceType.CALL_OUT.getName());
//					}
//				}
//			}	

//			CustomerActive customerActive = customer.getCustomerActive();
//			String beforeDeliverTarget = customer.getBeforeDeliverTarget();
//			//不活跃根据预约人的职位来设置资源入口
//			if(customerActive == CustomerActive.INACTIVE){
//				if(isWHZG||isWHZY||isBWHZG){						
//				    signCustomerVo.setResEntranceId("OUTCALL");
//				    signCustomerVo.setResEntranceName(ResEntranceType.OUTCALL.getName());
//				}
//				if(isZXS){	
//					signCustomerVo.setResEntranceId("CALL_OUT");
//					signCustomerVo.setResEntranceName(ResEntranceType.CALL_OUT.getName());
//				}	
//				if(StringUtil.isNotBlank(beforeDeliverTarget)){
//					Boolean isWLZY = userService.isUserRoleCode(beforeDeliverTarget, RoleCode.NETWORK_SPEC);
//					Boolean isWLZG = userService.isUserRoleCode(beforeDeliverTarget, RoleCode.NETWORK_MANAGER);
//					Boolean isWLJL = userService.isUserRoleCode(beforeDeliverTarget, RoleCode.INTERNET_MANAGER);
//					if(isWLZY || isWLZG || isWLJL){
//						signCustomerVo.setResEntranceId("ON_LINE");
//						signCustomerVo.setResEntranceName(ResEntranceType.ON_LINE.getName());
//					}
//				}
//			}

			
			
			
			//显示deliverTargetName 
			signCustomerVo.setDeliverTarget(customerVo.getDeliverTarget());
			String deliverTargetName = "";
	        if(customerVo.getDeliverType()!=null &&customerVo.getDeliverType()==(CustomerDeliverType.PERSONAL_POOL)){
	        	User user  = userService.loadUserById(customerVo.getDeliverTarget());
	        	if(user!=null){
	        		deliverTargetName = user.getName();
	        	}
	        }
	        signCustomerVo.setDeliverTargetName(deliverTargetName);
			return signCustomerVo;
		}else{
			Customer customer = this.findById(customerVo.getId());
			SignCustomerVo signCustomerVo = new SignCustomerVo();
			signCustomerVo.setVisitId(visitId);
			signCustomerVo.setCustomerId(customerVo.getId());
			signCustomerVo.setContact(customer.getContact());
			signCustomerVo.setName(customer.getName());
			signCustomerVo.setDeliverTarget(customer.getDeliverTarget());
			String deliverTargetName = "";
	        if(customer.getDeliverType()!=null && customer.getDeliverType()==(CustomerDeliverType.PERSONAL_POOL)){
	        	User user  = userService.loadUserById(customer.getDeliverTarget());
	        	if(user!=null){
	        		deliverTargetName = user.getName();
	        	}
	        }
	        signCustomerVo.setDeliverTargetName(deliverTargetName);
			DataDict cusOrg =customer.getCusOrg();
			if (cusOrg != null) {
				signCustomerVo.setCusOrg(cusOrg.getId());
				signCustomerVo.setCusOrgName(cusOrg.getName());
			} else {
				signCustomerVo.setCusOrg("");
				signCustomerVo.setCusOrgName("");
			}

			DataDict cusType = customer.getCusType();

			if (cusType != null){
				signCustomerVo.setCusType(cusType.getId());
				signCustomerVo.setCusTypeName(cusType.getName());
			}else {
				signCustomerVo.setCusType("");
				signCustomerVo.setCusTypeName("");
			}
            DataDict resEntrance =customer.getResEntrance();
            if(resEntrance!=null){
            	signCustomerVo.setResEntranceId(resEntrance.getId());
            	signCustomerVo.setResEntranceName(resEntrance.getName());
            }else{
            	signCustomerVo.setResEntranceId("");
            	signCustomerVo.setResEntranceName("");
            }
            signCustomerVo.setRemark(customer.getRemark());
			return signCustomerVo;
				
		}		
	}
	
	@Override
	public DataPackage getAppointmentRecords(CustomerFolowupVo res, DataPackage dp) {

		//根据不同的平台 workbrenchType来区分权限 
		//当前登录者
		User user = userService.getCurrentLoginUser();
		
		StringBuilder cusSql=new StringBuilder(512);	
		//客户姓名   联系方式  意向度   预约校区 预约上门时间  是否上门 学员姓名  就读学校 就读年级   最新跟进时间 最新跟进备注 
		cusSql.append(" select cf.id,c.id as cusId,c.name as cusName,c.CONTACT as contact, ");
		cusSql.append(" cf.SATISFICING as customerIntention,cf.APPOINT_CAMPUS as appointCampus, ");
		cusSql.append(" cf.MEETING_TIME as meetingTime,cf.VISIT_TYPE as visitType, ");
		cusSql.append(" c.REMARK as remark,c.DELIVER_TIME as deliverTime, d.`NAME` as intentionName,o.`name` as appointCampusName ");	
		cusSql.append(" from customer_folowup cf left join customer c on cf.CUSTOMER_ID = c.ID ");
		cusSql.append(" left join data_dict d on cf.SATISFICING = d.ID ");
		cusSql.append(" left join organization o on cf.APPOINT_CAMPUS  = o.id ");
		//cusSql.append(" left join customer_student_relation csr on c.id = csr.CUSTOMER_ID ");
		//cusSql.append(" left join student s on s.id = csr.STUDENT_ID ");	
		cusSql.append(" where cf.APPOINTMENT_TYPE = :APPOINTMENT_TYPE ");
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("APPOINTMENT_TYPE",AppointmentType.APPOINTMENT.getValue() );
        //预约上门的创建者为当前的登陆者 外呼 咨询师
        cusSql.append(" and cf.CREATE_USER_ID = :createUserId ");
        params.put("createUserId",user.getUserId() );
		//没必要区分外呼 咨询师
        //客户姓名
        if(StringUtils.isNotBlank(res.getCustomerName())){
        	cusSql.append(" and c.NAME like :customerName ");
        	params.put("customerName","%"+res.getCustomerName()+"%");
        }
        //联系方式
        if(StringUtils.isNotBlank(res.getCusMobile())){
        	cusSql.append(" and c.CONTACT like :cusMobile ");
        	params.put("cusMobile","%"+res.getCusMobile()+"%");
        }
		//意向度
		if (StringUtils.isNotBlank(res.getIntentionOfTheCustomerId())) {
			cusSql.append(" and c.INTENTION_OF_THE_CUSTOMER = :intentionOfTheCustomerId ");
			params.put("intentionOfTheCustomerId",res.getIntentionOfTheCustomerId());
		}
		//预约校区
		if(StringUtils.isNotBlank(res.getAppointCampusId())){
			cusSql.append(" and cf.APPOINT_CAMPUS = :appointCampusId ");
			params.put("appointCampusId",res.getAppointCampusId());
		}
		// 是否上门
		if (StringUtils.isNotBlank(res.getIsAppointCome())) {
			// 如果是预约上门
			if ("1".equals(res.getIsAppointCome())) {
				cusSql.append("and (c.VISIT_TYPE is not NULL && c.VISIT_TYPE <>'NOT_COME' )");
			}
			// 非预约上门
			if ("0".equals(res.getIsAppointCome())) {
				cusSql.append("and (c.VISIT_TYPE is NULL || c.VISIT_TYPE = 'NOT_COME' )");
			}
		}
		
		//如果这三个条件有一个不为空则关联学生表
		if(StringUtils.isNotBlank(res.getStudentName())||StringUtils.isNotBlank(res.getSchoolId())||StringUtils.isNotBlank(res.getGradeId())){
			cusSql.append(" and c.id in ( select cs.CUSTOMER_ID from customer_student_relation cs left join ");
			cusSql.append(" student s on cs.STUDENT_ID = s.ID where 1=1 ");
			//学生姓名
			if(StringUtils.isNotBlank(res.getStudentName())){
               cusSql.append(" and s.NAME like :studentName " ); 
               params.put("studentName","%"+res.getStudentName()+"%");
			}
			//选择就读学校  这里传进来的是学校的Id 不是名字
			if(StringUtils.isNotBlank(res.getSchoolId())){
			   cusSql.append(" and s.SCHOOL = :schoolId ");
			   params.put("schoolId",res.getSchoolId());
			}
			//学生的年级   传入 学生年级的Id
			if(StringUtils.isNotBlank(res.getGradeId())){
				cusSql.append(" and s.GRADE_ID = :gradeId ");
				params.put("gradeId",res.getGradeId());
			}	
			cusSql.append(" ) ");
		}
		
		
//		//学生姓名
//		if(StringUtils.isNotBlank(res.getStudentName())){
//			cusSql.append(" and s.NAME like '%"+res.getStudentName()+"%' ");
//		}
//		//选择就读学校  这里传进来的是学校的Id 不是名字
//		if(StringUtils.isNotBlank(res.getSchoolId())){
//			cusSql.append(" and s.SCHOOL ='"+res.getSchoolId()+"' ");
//		}
//		//学生的年级   传入 学生年级的Id
//		if(StringUtils.isNotBlank(res.getGradeId())){
//			cusSql.append(" and s.GRADE_ID ='"+res.getGradeId()+"'");
//		}
		
		
		
		//预约上门开始时间 预约上门结束时间
		if (StringUtils.isNotBlank(res.getMeetingTimeStart())) {
			cusSql.append(" and cf.MEETING_TIME >= :meetingTimeStart ");
			params.put("meetingTimeStart",res.getMeetingTimeStart()+" 00:00:00");
		}

		if (StringUtils.isNotBlank(res.getMeetingTimeEnd())) {
			cusSql.append(" and cf.MEETING_TIME <= :meetingTimeEnd ");
			params.put("meetingTimeEnd",res.getMeetingTimeEnd()+" 23:59:59");
		}	

        //默认按照预约时间降序
		//cusSql.append(" order by cf.MEETING_TIME desc ");
		//增加过滤条件
		Map<String, String> map_id = getNetWorkGroupOUTCALLOrgId();
		Organization organization = userService.getUserMainDeptByUserId(user.getUserId());
		String orgId=organization.getId();
		
		if (map_id.get("network") != null && orgId.equals(map_id.get("network"))) {
			//网络 不加设置限制
//			Boolean isWLZY = userService.isUserRoleCode(user.getUserId(), RoleCode.NETWORK_SPEC);			
//			if (isWLZY) {				
//			} else {
//			}
            
		} else if (map_id.get("outcall") != null && orgId.equals(map_id.get("outcall"))) {
			Boolean isWHZY = userService.isUserRoleCode(user.getUserId(), RoleCode.OUTCALL_SPEC);		
			if (isWHZY) {
				organization = userService.getBelongBranchByUserId(user.getUserId());
			} else {
				organization = userService.getBelongCampusByUserId(user.getUserId());
			}
			String orgLevel = organization.getOrgLevel();
			params.put("orgLevel",  orgLevel+"%");
			cusSql.append(
					" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
		} else {
			organization = userService.getBelongCampusByUserId(user.getUserId());
			String orgLevel = organization.getOrgLevel();
			params.put("orgLevel",  orgLevel+"%");
			cusSql.append(
					" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
		}
		
		
		
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			cusSql.append(" order by "+dp.getSidx()+" "+dp.getSord());			
		}
		
		
		//将查询结果转换为vo
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dp,params);
		//dp.setDatas(list);
		dp.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));	
	
		
		//客户姓名   联系方式  意向度   预约校区 预约上门时间  是否上门 学员姓名  就读学校 就读年级   最新跟进时间 最新跟进备注 
		List<CustomerFolowupVo> voList = new ArrayList<CustomerFolowupVo>();
		CustomerFolowupVo folowupVo = null ;
		for (Map<Object,Object> tmaps : list) {
			Map<String, String> maps = (Map)tmaps;
			folowupVo = new CustomerFolowupVo();
			String cusId = maps.get("cusId");
			folowupVo.setId(maps.get("id"));
			folowupVo.setCustomerId(cusId);
			//客户姓名
			folowupVo.setCustomerName(maps.get("cusName"));
			//联系方式
			folowupVo.setCusMobile(maps.get("contact"));
			
			//意向度
			folowupVo.setIntentionOfTheCustomerId(maps.get("customerIntention"));
			folowupVo.setIntentionOfTheCustomerName(maps.get("intentionName"));
			//预约校区
			folowupVo.setAppointCampusName(maps.get("appointCampusName"));
			
			//预约上门时间
			folowupVo.setMeetingTime(maps.get("meetingTime"));
	        //是否上门
			String visitType =maps.get("visitType");
			if(visitType==null||(visitType != null && visitType.equals(VisitType.NOT_COME.getValue()))){
				folowupVo.setIsAppointCome("否");
			}else{
				folowupVo.setIsAppointCome("是");
			}
			//最新跟进时间
			//最新备注
			folowupVo.setRecentlyRemark(maps.get("remark"));
			folowupVo.setRecentlyTime(maps.get("deliverTime"));
			
			//学员姓名  就读学校  就读年级
			//设置学生姓名列表(多个学生用逗号,分割开来)  根据cusId来查询
			StringBuilder studentBuffer = new StringBuilder(" ");
			StringBuilder schoolBuffer = new StringBuilder(" ");
			StringBuilder gradeBuffer  = new StringBuilder(" ");
			Map<String, Object> param = Maps.newHashMap();
			param.put("cusId", cusId);
			String sql ="SELECT s.`NAME` as studentName,s.GRADE_ID as gradeId,ss.`NAME` as schoolName from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID LEFT JOIN student_school ss on s.SCHOOL = ss.ID"+ 
			        " where csr.CUSTOMER_ID = :cusId ";  
			List<Map<Object, Object>> resultList=customerDao.findMapBySql(sql,param);
			if(resultList!=null && resultList.size()>0){
				for(Map<Object, Object> map:resultList){
					String gradeId = map.get("gradeId")!=null ? map.get("gradeId").toString() : null;
					if(StringUtils.isNotBlank(gradeId)){
						DataDict dataDict_grade = dataDictDao.findById(gradeId);
						gradeBuffer.append(dataDict_grade.getName()+",");
					}
					if(map.get("studentName")!=null){
						studentBuffer.append(map.get("studentName")+",");
					}
					if(map.get("schoolName")!=null){
						schoolBuffer.append(map.get("schoolName")+",");
					}
					
				}
		        String studentList = studentBuffer.toString();
		        String schoolList = schoolBuffer.toString();
		        String gradeList = gradeBuffer.toString();
		        if(StringUtils.isNotBlank(studentList)){
		        	folowupVo.setStudentNameList(new String(studentList.substring(0, studentList.length()-1)));
		        }else{
		        	folowupVo.setStudentNameList("");
		        }
		        if(StringUtils.isNotBlank(schoolList)){
		        	folowupVo.setSchoolNameList(new String(schoolList.substring(0, schoolList.length()-1)));
		        }else{
		        	folowupVo.setSchoolNameList("");
		        }
		        if(StringUtils.isNotBlank(gradeList)){
		        	folowupVo.setGradeNameList(new String(gradeList.substring(0, gradeList.length()-1)));
		        }else{
		        	folowupVo.setGradeNameList("");
		        }
		        
		      //清空Buffer
		        studentBuffer.replace(0, studentBuffer.length()-1, "");
		        schoolBuffer.replace(0, schoolBuffer.length()-1, "");
		        gradeBuffer.replace(0, gradeBuffer.length()-1, "");
			}	
			voList.add(folowupVo);
		}

		dp.setDatas(voList);
		return dp;
	}
	
	/**
	 * 保存登记信息
	 */
	@Override
	public Response signCustomerComeon(SignCustomerVo vo) {		
		//上门数和资源量的记录
		User referUser = userService.getCurrentLoginUser();
		
		if(StringUtils.isNotBlank(vo.getVisitId()) && StringUtils.isNotBlank(vo.getCustomerId())){
			CustomerFolowup ca=customerFolowupDao.findById(vo.getVisitId()); //跟进记录
			Customer c=customerDao.findById(vo.getCustomerId());
			User user = null;
			if(StringUtil.isBlank(c.getDeliverTarget())){
				Response resp=new Response();
				resp.setResultCode(-1);
				resp.setResultMessage("登记失败，没有找到对应的来访跟进记录");
				return resp;				
			}else{
				user = userDao.findById(c.getDeliverTarget());
				if(user == null){
					Response resp=new Response();
					resp.setResultCode(-1);
					resp.setResultMessage("登记失败，没有找到对应的来访跟进记录");
					return resp;
				}
			}

			
			
			//新增手动设置客户上门 引起上门登记上门数逻辑变动
			VisitType visitType = c.getVisitType();
			
			c.setDeliverType(CustomerDeliverType.PERSONAL_POOL);	 //登记时只能分给咨询师
			
			
			//记录原来的资源入口 (作废 20170810)
//			String oldResEntrance =null;
//			if(c.getResEntrance()!=null){
//				oldResEntrance=c.getResEntrance().getValue();
//			}else if (c.getPreEntrance()!=null){
//				oldResEntrance=c.getPreEntrance().getValue();
//			}
			
			//修改客户信息
			if(!c.getDeliverTarget().equals(vo.getDeliverTarget())){
				//分配人有变更	
				//增加资源量的校验
				Response res = resourcePoolService.getResourcePoolVolume(1, vo.getDeliverTarget());
				if(res.getResultCode() != 0) {
					return res;
				}
				
				c.setDealStatus(CustomerDealStatus.valueOf("STAY_FOLLOW"));
				//记录最新分配人
				c.setDeliverTime(DateTools.getCurrentDateTime());
				c.setLastDeliverName(referUser.getName());	
				//20170524 用于客户获取的时候增加事件
				c.setBlConsultor(vo.getDeliverTarget());
				//
				DeliverTargetChangeRecord record = new DeliverTargetChangeRecord();
				record.setCustomerId(vo.getCustomerId());
				record.setPreviousTarget(c.getDeliverTarget());
				record.setCurrentTarget(vo.getDeliverTarget());
				record.setRemark("前台登记客户信息变更分配人记录");
				record.setCreateUserId(referUser.getUserId());
				record.setCreateTime(DateTools.getCurrentDateTime());
				String recordId = deliverTargetChangeService.saveDeliverTargetChangeRecord(record);
				
				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.CHANGE_COUNSELOR);
				dynamicStatus.setDescription("前台登记客户信息并且更换咨询师");
				if(c.getResEntrance()!=null){
				   dynamicStatus.setResEntrance(c.getResEntrance());
				}
				dynamicStatus.setStatusNum(1);
				dynamicStatus.setTableName("delivertarget_change_record");
				dynamicStatus.setTableId(recordId);
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
				customerEventService.addCustomerDynameicStatus(c, dynamicStatus, referUser);
				
				//vo.getDeliverTarget() 原来的分配对象是咨询师才构成咨询师的更换 从而更换咨询师 添加抢客记录
//				String roleSign = userService.getUserRoleSign(vo.getDeliverTarget());
//				List<String> roleSignList = userService.getUserRoleSignFromRole(vo.getDeliverTarget());
//				if(roleSignList==null) roleSignList = new ArrayList<>();
				Boolean isZXS = userService.isUserRoleCode(c.getDeliverTarget(), RoleCode.CONSULTOR);
				if(isZXS){
					// 添加抢客记录
					String res_Entrance = null;
//					if (StringUtils.isNotBlank(vo.getResEntranceId())
//							&& !vo.getResEntranceId().equals(oldResEntrance)) {
//						res_Entrance = vo.getResEntranceId();
//					}
					if(c.getResEntrance()!=null){
						res_Entrance = c.getResEntrance().getId();
					}
					
					GainCustomer gainCustomer = new GainCustomer();
					gainCustomer.setCusId(c);
					gainCustomer.setReason("上门登记更换咨询师");
					gainCustomer.setDeliverFrom(c.getDeliverTarget());
					gainCustomer.setDeliverTarget(vo.getDeliverTarget());
					this.gainCustomer(gainCustomer, res_Entrance);
				}
				
				
			}else{
				CustomerDynamicStatus dynamicStatus_come = new CustomerDynamicStatus();
				dynamicStatus_come.setDynamicStatusType(CustomerEventType.VISITCOME_CUSTOMER);
				dynamicStatus_come.setDescription("客户已上门");
				if (c.getResEntrance() != null) {
					dynamicStatus_come.setResEntrance(c.getResEntrance());
				}
				dynamicStatus_come.setStatusNum(1);
				dynamicStatus_come.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
				customerEventService.addCustomerDynameicStatus(c, dynamicStatus_come, user);
				
				c.setVisitType(VisitType.PARENT_COME);
				c.setVisitComeDate(new Date());
			}
			c.setDeliverTarget(vo.getDeliverTarget());
			c.setName(vo.getName()); 
			
			//保存登记信息 资源入口生效    在加载登记信息的时候已经根据规则修改资源入口    //xiaojinwang 20161017 (作废 20170810)
			
			//市场经理导入的客户是没有入口标记  如果入口标记为空则更加传进来的资源入口补上 入口标记 //xiaojinwang 2017-01-07	 (作废 20170810)
			if(vo.getResEntranceId() != null){
				DataDict resEntrance = dataDictDao.findById(vo.getResEntranceId());
				c.setResEntrance(resEntrance);
				if(c.getPreEntrance() ==null){
					c.setPreEntrance(resEntrance);//补上入口标记
				}
			}
			
			
			
			
			if(vo.getCusType() != null){
				c.setCusType(dataDictDao.findById(vo.getCusType()));
			}
			if(vo.getCusOrg() != null){
				c.setCusOrg(dataDictDao.findById(vo.getCusOrg()));
			}
			
			c.setRemark(vo.getRemark());
			//一旦上门登记 设置客户已经上门  customer的上门状态和单次的预约上门没有关系      
			
			//如果更换咨询师　则客户上门的设置等到客户获取的时候再设置　xiaojinwang 20170524
			//c.setVisitType(VisitType.PARENT_COME);//add by xiaojinwang  20160927
			//上门登记资源入口生效  如果(作废 20170810)
			//c.setResEntrance(new DataDict(vo.getResEntranceId())); //前面已经补上，不需要重复20170413
			customerDao.save(c);
				
			//跟进记录信息,确认登记   最终上门时间
			if(ca.getMeetingConfirmUser()==null){
			   ca.setMeetingConfirmUser(referUser);
			}
			if(ca.getMeetingConfirmTime()==null){
				ca.setMeetingConfirmTime(DateTools.getCurrentDateTime());
			}			
			ca.setRemark(vo.getRemark());
			ca.setVisitType(VisitType.PARENT_COME);

			customerFolowupDao.save(ca);
			
			
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.APPOINTMENT_CONFORM);
			dynamicStatus.setDescription("前台登记来访客户");
			if(c.getResEntrance()!=null){
			   dynamicStatus.setResEntrance(c.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setTableName("customer_folowup");
			dynamicStatus.setTableId(vo.getVisitId());
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
			customerEventService.addCustomerDynameicStatus(c, dynamicStatus, referUser);
			
					
			
			//增加上门数 
			//在增加上门数的是判读 资源入口是否有变更 如果有变更则原来的资源入口的资源量减一
			//这里的逻辑迁移到保存客户那里  20170810 xiaojinwang

			if(visitType==null||(visitType!=null && visitType==VisitType.NOT_COME)){
				//原来的上门标记为空 或者为其他标记才增加上门数 
				
				CustomerDynamicStatus dynamicStatus3 = new CustomerDynamicStatus();
				dynamicStatus3.setDynamicStatusType(CustomerEventType.VISITCOME);
				dynamicStatus3.setDescription("增加上门数");
				if(c.getResEntrance()!=null){
					   dynamicStatus3.setResEntrance(c.getResEntrance());
				}
				dynamicStatus3.setStatusNum(1);
				dynamicStatus3.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
				customerEventService.addCustomerDynameicStatus(c, dynamicStatus3, referUser);				
			}						
			return new Response();
		}else{
			Response res=new Response();
			res.setResultCode(-1);
			res.setResultMessage("登记失败，没有找到对应的来访跟进记录");
			return res;
		}
	}
	
	/**
	 * 前台客户登记记录
	 * @author tangyuping
	 * 前台客户登记记录,获取最近30天的客户登记记录
	 */
	@Override
	public DataPackage findPageCustomerForReceptionistWb(ReceptionistCustomerVo vo, DataPackage dp) {

		StringBuilder query = new StringBuilder(512);

		// 登记日期 录入者 录入者部门 客户姓名 资源入口 资源来源 资源细分 跟进人 处理状态 备注
		Organization currentBlCampus = userService.getBelongCampus();
		System.out.println("currentBlCampus:"+currentBlCampus.getId());
		String userId = userService.getCurrentLoginUser().getUserId();
		query.append(" select cf.ID as id,cf.MEETING_CONFIRM_TIME as meetingConfirmTime,concat('',cf.MEETING_CONFIRM_USER_ID) as meetingConfirmUserId,c.RECORD_USER_ID as recordUserId,c.`NAME` as customerName,c.REMARK as remark, ");
		query.append(" c.RECORD_DATE as recordDate,c.DELEVER_TARGET as deliverTarget,c.TRANSFER_STATUS as transferStatus, c.TRANSFER_FROM as transferFrom, ");
		query.append(" c.BL_SCHOOL as blSchool,c.ID as customerId,c.CONTACT as contact, ");
		query.append(" u.`NAME` as recordUserName,c.RES_ENTRANCE as resEntrance,d.`NAME` as resEntranceName, ");
		query.append(" dd.`NAME` as cusOrg,ddd.`NAME` as cusType,c.DEAL_STATUS as dealStatus,uu.`NAME` as deliverTargetName ");
		query.append(" from customer_folowup cf left join customer c on cf.CUSTOMER_ID = c.ID ");
		query.append(" left join `user` u on c.RECORD_USER_ID = u.USER_ID ");
		query.append(" left join `user` uu on c.DELEVER_TARGET = uu.USER_ID ");
		query.append(" left join data_dict d on c.RES_ENTRANCE =d.ID ");
		query.append(" left join data_dict dd on c.CUS_ORG = dd.ID ");
		query.append(" left join data_dict ddd on c.CUS_TYPE = ddd.ID ");

		query.append(" where cf.APPOINTMENT_TYPE='APPOINTMENT' and cf.MEETING_CONFIRM_TIME is not null ");
		query.append(" and c.DEAL_STATUS <> 'INVALID' ");
		
		Map<String, Object> params = Maps.newHashMap();
		
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			query.append(" and cf.MEETING_CONFIRM_TIME >= :startDate ");
			params.put("startDate", vo.getStartDate() + " 00:00:00 ");
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			query.append(" and cf.MEETING_CONFIRM_TIME <= :endDate ");
			params.put("endDate", vo.getEndDate() + " 23:59:59 ");
		}
		// 获取最近30天登记记录，
		if (StringUtils.isBlank(vo.getStartDate()) && StringUtils.isBlank(vo.getEndDate())) {
			String end = DateTools.getCurrentDate();
			String start = DateTools.addDateToString(end, -30);// 得到当前日期减30
			params.put("start", start + " 00:00:00 ");
			params.put("end", end + " 23:59:59 ");
			query.append(" and cf.MEETING_CONFIRM_TIME >= :start and cf.MEETING_CONFIRM_TIME <= :end ");
		}
		if (StringUtils.isNotBlank(vo.getRecordUserName())) {
			query.append(" and c.RECORD_USER_ID in (select USER_ID from `user` where `NAME` like :recordUserName ) ");
			params.put("recordUserName", "%"+vo.getRecordUserName()+"%");
		}
		if (StringUtils.isNotBlank(vo.getName())) {
			query.append(" and c.`NAME` like :cName ");
			params.put("cName", "%"+vo.getName()+"%");
		}
		if (StringUtils.isNotBlank(vo.getContact())) {
			query.append(" and c.CONTACT like :contact ");
			params.put("contact", "%"+vo.getContact()+"%");
		}
		if (StringUtils.isNotBlank(vo.getResEntranceId())) {
			query.append(" and c.RES_ENTRANCE = :resEntranceId " );
			params.put("resEntranceId", vo.getResEntranceId());
		}
		if (StringUtils.isNotBlank(vo.getCusType())) {
			query.append(" and c.CUS_TYPE = :cusType ");
			params.put("cusType", vo.getCusType());
		}
		if (StringUtils.isNotBlank(vo.getCusOrg())) {
			query.append(" and c.CUS_ORG = :cusOrg ");
			params.put("cusOrg", vo.getCusOrg());
		}
		if (StringUtils.isNotBlank(vo.getDealStatus())) {
			query.append(" and c.DEAL_STATUS = :dealStatus ");
			params.put("dealStatus", vo.getDealStatus());
		}

		// 前台或校区主任可以看到自己校区的客户 或者从本校区转出但是还没有接收的数据
		if (userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST)
				|| userService.isCurrentUserRoleCode(RoleCode.CAMPUS_DIRECTOR)) {
			query.append(" and ( c.BL_SCHOOL = :blSCHOOL ");
			params.put("blSCHOOL", currentBlCampus.getId());
		} else {
			// 其他人员只能看到自己根据的客户
			query.append(" and ( c.DELEVER_TARGET = :DELEVERTARGET ");
			params.put("DELEVERTARGET",userId);
		}
		query.append(" or ( c.TRANSFER_FROM = :TRANSFERFROM and c.TRANSFER_STATUS = '0' ) )");
		params.put("TRANSFERFROM",currentBlCampus.getId());
		// 同一个客户在30天内多次登记只取最近一次登记记录即可
		query.append(" ORDER BY cf.MEETING_CONFIRM_TIME DESC ");

		List<Map<Object, Object>> list = customerDao.findMapOfPageBySql(query.toString(), dp,params);
		dp.setRowCount(customerDao.findCountSql("select count(*) from ( " + query.toString() + " ) countall ",params));

		// 将查询结果转换为vo
		List<ReceptionistCustomerVo> volist = new ArrayList<ReceptionistCustomerVo>();
		ReceptionistCustomerVo customerVo = null;
		for (Map<Object, Object> tmaps : list) {
			Map<String, String> maps =(Map)tmaps;
			customerVo = new ReceptionistCustomerVo();
			
			customerVo.setId(maps.get("id"));
			customerVo.setCustomerId(maps.get("customerId"));
			//录入日期 recordDate
			customerVo.setContact(maps.get("contact"));
			customerVo.setRecordDate(maps.get("recordDate"));
			customerVo.setDeliverTarget(maps.get("deliverTarget"));
			// 登记人id meetingConfirmUserId 
		    if(maps.get("meetingConfirmUserId")!=null){
		    	customerVo.setMeetingConfirmUserId(maps.get("meetingConfirmUserId"));
		    	Organization blCampus = userService.getBelongCampusByUserId(maps.get("meetingConfirmUserId"));
		    	customerVo.setMeetingConfirmBlCampus(blCampus == null ? "" : blCampus.getId()); //登记人校区
		    }else{
		    	customerVo.setMeetingConfirmUserId("");
		    	customerVo.setMeetingConfirmBlCampus("");
		    }
			// meetingConfirmBlCampus  
			customerVo.setBlSchool(maps.get("blSchool"));
			customerVo.setTransferFrom(maps.get("transferFrom"));
			customerVo.setTransferStatus(maps.get("transferStatus"));
			// 登记日期
			customerVo.setMeetingConfirmTime(maps.get("meetingConfirmTime"));
			// 录入者
			customerVo.setRecordUserId(maps.get("recordUserId"));
			customerVo.setRecordUserName(maps.get("recordUserName"));
			// 录入者部门
			if (maps.get("recordUserId") != null) {
				UserDeptJob userDeptJob = userDeptJobDao.findDeptJobByParam(maps.get("recordUserId"), 0);
				if (userDeptJob != null) {
					Organization dept = organizationDao.findById(userDeptJob.getDeptId());
					customerVo.setRecordUserDeptName(dept.getName());
				}
			} else {
				customerVo.setRecordUserDeptName("");
			}

			// 客户姓名
			customerVo.setName(maps.get("customerName"));
			// 资源入口
			customerVo.setResEntranceName(maps.get("resEntranceName"));
			// 资源来源
			customerVo.setCusTypeName(maps.get("cusType"));
			// 资源细分
			customerVo.setCusOrgName(maps.get("cusOrg"));
			// 处理状态
			if (maps.get("dealStatus") != null) {
				customerVo.setDealStatus(maps.get("dealStatus"));
				customerVo.setDealStatusName(CustomerDealStatus.valueOf(maps.get("dealStatus")).getName());
			} else {
				customerVo.setDealStatus("");
				customerVo.setDealStatusName("");
			}
			// 跟进人
			customerVo.setDeliverTargetName(maps.get("deliverTargetName"));
			// 备注
			customerVo.setRemark(maps.get("remark"));

			volist.add(customerVo);
		}
		dp.setDatas(volist);
		return dp;

	}
	
	/**
	 * @author tangyuping
	 * 添加转校信息
	 */
	@Override
	public String addTransferCustomer(TransferCustomerCampus transferCustomerCampus){
//		String cusId = transferCustomerCampus.getCusId();
//		if(StringUtils.isNotBlank(cusId)){
//			String sql = "select * from transfer_customer where cus_id = '"+cusId+"' ORDER BY create_time DESC ";
//			List<TransferCustomerCampus> list = transferCustomerCampusDao.findBySql(sql);
//			if(list != null && list.size()>0){
//				//获取客户最新一次转校信息
//				TransferCustomerCampus trans = list.get(0);
//				if(StringUtils.isNotBlank(trans.getReceiveCampus())){
//					//最新一次记录已经完成了一次转校，接收，对客户进行下一次的转校，重新添加一条转校信息	
//					TransferCustomerCampus transfer = new TransferCustomerCampus();
//					transfer.setCusId(cusId);
//					transfer.setTransferCampus(transferCustomerCampus.getTransferCampus());
//					this.saveTransCampus(transfer);
//				}else{
//					//接收客户
//					trans.setReceiveCampus(transferCustomerCampus.getReceiveCampus());
//					trans.setReceiveTime(DateTools.getCurrentDateTime());
//				}
//			}else{
//				//客户首次转校
//				TransferCustomerCampus trans = new TransferCustomerCampus();
//				trans.setCusId(cusId);
//				trans.setTransferCampus(transferCustomerCampus.getTransferCampus());
//				this.saveTransCampus(trans);
//			}
//			
//		}
		//2016-10-27 xiaojinwang 注释以上  每天转校添加一条记录
		//接收是按照转校记录的id进行修改
		return this.saveTransCampus(transferCustomerCampus);
		
	}
	
	public String saveTransCampus(TransferCustomerCampus trans){
		String now = DateTools.getCurrentDateTime();
		trans.setCreateTime(now);
		trans.setCreateuser(userService.getCurrentLoginUser().getUserId());
		trans.setTransferTime(now);
		transferCustomerCampusDao.save(trans);
		return trans.getId();		
	}
    
	@Override
	public Response receiveTransferCampusCustomer(String transferCustomerId, CustomerVo customerVo) {
		//当前登录者
		User currentLoginUser = userService.getCurrentLoginUser();
		//根据transferCustomerId获取转校转出记录
		Map<String, Object> params = Maps.newHashMap();
		params.put("transferCustomerId", transferCustomerId);
		String sql = "select * from transfer_customer where ID = :transferCustomerId ";
		List<TransferCustomerCampus> list = transferCustomerCampusDao.findBySql(sql,params);
		if(list != null && list.size()>0){
			TransferCustomerCampus trans = list.get(0);			
		    //修改客户customer的转校状态从0 变到1 
			//获取customer 
			Customer customer = this.findById(trans.getCusId());
			customer.setTransferStatus("1");
			customerDao.save(customer);
			trans.setReceiveTime(DateTools.getCurrentDateTime());
			transferCustomerCampusDao.save(trans);
			//接收是分配咨询师 
			//
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.TRANSFERCUSTOMERIN);
			dynamicStatus.setDescription("客户转校被接收");
			if(customer.getResEntrance()!=null){
			   dynamicStatus.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setTableName("transfer_customer");
			dynamicStatus.setTableId(transferCustomerId);
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, currentLoginUser);	
			
			Response res = this.allocateCustomer(customerVo,trans.getCusId(), currentLoginUser.getUserId(), "接收转校重新分配咨询师",CustomerEventType.CHANGE_COUNSELOR);
			return res;
		}else{
			return new Response(-1, "没有找到转校客户");
		}

	}
	
	/**
	 * 前台抢客 
	 * @author tangyuping
	 */
	@Override
	public Response gainCustomer(GainCustomer gainCustomer, String res_Entrance) {
		String customerId = gainCustomer.getCusId().getId();
		Customer customer =this.findById(customerId);
		//该客户原来的预备资源入口和正式资源入口
//		String pre_Entrance_old = customer.getPreEntrance()!=null?customer.getPreEntrance().getValue():"";
//		
//	    String res_Entrance_old = customer.getResEntrance()!=null?customer.getResEntrance().getValue():""; 
//	    //原来的前分配对象 和现分配对象
//	    String beforeDeliverTarget = customer.getBeforeDeliverTarget();
	    String deliverTarget = customer.getDeliverTarget();
		// 当前登录者为前台
		User currentLoginUser = userService.getCurrentLoginUser();
//		gainCustomer.setCreateUser(currentLoginUser.getUserId());
//		gainCustomer.setCreateTime(DateTools.getCurrentDateTime());
//		gainCustomerDao.save(gainCustomer);

		// 分配客户
		// customerVo要封装三种参数 deliverTarget dealStatus deliverType
		CustomerVo customerVo = new CustomerVo();
		customerVo.setDeliverTarget(gainCustomer.getDeliverTarget());
		// 调配客户是分配咨询师 分配类型为个人 且跟进状态为待跟进 等待被分配的咨询师获取客户
		customerVo.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
		customerVo.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
		customerVo.setResEntranceId(res_Entrance);
		if(gainCustomer.getVisitCome()!=null && gainCustomer.getVisitCome() ==true){
			customerVo.setVisitCome(true);
		}
		
		Response res = this.allocateCustomer(customerVo, customerId, currentLoginUser.getUserId(), "前台调配重新分配咨询师",CustomerEventType.CHANGE_COUNSELOR);
		if(res.getResultCode()==-1){
			return res;
		}
		// 调配以后 考虑被抢客户的问题
		// 分为三种情况 1、外呼或者网络专员跟进中 而且没有咨询师跟进 2、外呼或者网络专员在跟进 并且已经分配咨询师在跟进 3、咨询师录入 只有咨询师在跟进
		// 通过pre_Entrance res_Entrance来判断
		//判断当前的客户是否有咨询师 通过是否有登记记录来判断
//		Boolean hasFolloupRecord = customerFolowupService.hasFollowupRecord(customerId);
		//被抢原因
		String description ="客户被调配重新分配新的咨询师";
		String  currentDateTime = DateTools.getCurrentDateTime();
//        if(hasFolloupRecord){
//        	//说明有咨询师登记过
//        	if(pre_Entrance_old.equals(ResEntranceType.TMK)|| pre_Entrance_old.equals(ResEntranceType.ON_LINE)||
//					res_Entrance_old.equals(ResEntranceType.TMK)|| res_Entrance_old.equals(ResEntranceType.ON_LINE))	
//						{
//					//排除是咨询师自己录入的情况 此时beforeDeliverTarget 和deliverTarget 都是被抢
//					User referUser_before = userService.loadUserById(beforeDeliverTarget) ;
//					User referUser_now =  userService.loadUserById(deliverTarget);
//					if(referUser_before!=null){//查询出来的用户存在说明是人  才被抢 
//						GainCustomer gainCustomer_before = new GainCustomer();
//						gainCustomer_before.setCusId(customer);
//						gainCustomer_before.setDeliverFrom(beforeDeliverTarget);
//						gainCustomer_before.setDeliverTarget(customerVo.getDeliverTarget());
//						gainCustomer_before.setReason(gainCustomer.getReason());
//						gainCustomer_before.setCreateUser(currentLoginUser.getUserId());
//						gainCustomer_before.setCreateTime(currentDateTime);
//						gainCustomerDao.save(gainCustomer_before);
//						customerEventService.saveCustomerDynamicStatus(customerId, CustomerEventType.GAINCUSTOMER, description, referUser_before, "");
//					}
//                    if(referUser_now!=null){
//						GainCustomer gainCustomer_now = new GainCustomer();
//						gainCustomer_now.setCusId(customer);
//						gainCustomer_now.setDeliverFrom(deliverTarget);
//						gainCustomer_now.setDeliverTarget(customerVo.getDeliverTarget());
//						gainCustomer_now.setReason(gainCustomer.getReason());
//						gainCustomer_now.setCreateUser(currentLoginUser.getUserId());
//						gainCustomer_now.setCreateTime(currentDateTime);		                
//		                gainCustomerDao.save(gainCustomer_now); 
//		                customerEventService.saveCustomerDynamicStatus(customerId, CustomerEventType.GAINCUSTOMER, description, referUser_now, "");
//                    }
//				}
//				if(pre_Entrance_old.equals(ResEntranceType.TMK)||pre_Entrance_old.equals(ResEntranceType.CALL_OUT)){
//					User referUser_now =  userService.loadUserById(deliverTarget);
//					if(referUser_now!=null){
//						GainCustomer gainCustomer_now = new GainCustomer();
//						gainCustomer_now.setCusId(customer);
//						gainCustomer_now.setDeliverFrom(deliverTarget);
//						gainCustomer_now.setDeliverTarget(customerVo.getDeliverTarget());
//						gainCustomer_now.setReason(gainCustomer.getReason());
//						gainCustomer_now.setCreateUser(currentLoginUser.getUserId());
//						gainCustomer_now.setCreateTime(currentDateTime);		                
//		                gainCustomerDao.save(gainCustomer_now);	
//		                customerEventService.saveCustomerDynamicStatus(customerId, CustomerEventType.GAINCUSTOMER, description, referUser_now, "");
//					}		               
//				}					
//			}else{
//				//没有记录 说明没有分配过咨询师
//				//无论是TMK还是网络都是当前分配对象被抢
//				User referUser_now =  userService.loadUserById(deliverTarget);
//				if(referUser_now!=null){
//					GainCustomer gainCustomer_now = new GainCustomer();
//					gainCustomer_now.setCusId(customer);
//					gainCustomer_now.setDeliverFrom(deliverTarget);
//					gainCustomer_now.setDeliverTarget(customerVo.getDeliverTarget());
//					gainCustomer_now.setReason(gainCustomer.getReason());
//					gainCustomer_now.setCreateUser(currentLoginUser.getUserId());
//					gainCustomer_now.setCreateTime(currentDateTime);		                
//	                gainCustomerDao.save(gainCustomer_now);	
//	                customerEventService.saveCustomerDynamicStatus(customerId, CustomerEventType.GAINCUSTOMER, description, referUser_now, "");
//				}                
//			} 
		
		//当前分配对象被抢  2017-01-07 xiaojinwang  外呼 网络没有流失概念 但是有被抢概念
		
//		String roleSign = userService.getUserRoleSign(deliverTarget);
//		List<String> roleSignList = userService.getUserRoleSignFromRole(deliverTarget);
//		if(roleSignList==null) roleSignList = new ArrayList<>();
		
		
		
		//获取客户的时候判断 当前操作人的职位 如果是外呼或者网络专员 则记录上工序
		//String gain_target = gainCustomer.getDeliverTarget();
		//补充说明 应该是判断被抢的用户是不是网络专员 注释掉上面的gain_target
		if (StringUtil.isNotBlank(deliverTarget)) {
			Boolean isWHZY = userService.isUserRoleCode(deliverTarget, RoleCode.OUTCALL_SPEC);
			Boolean isWLZY = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_SPEC);
			if (isWHZY || isWLZY) {
				customer.setBeforeDeliverTarget(null);
				customerDao.save(customer);
			}

			User referUser_now = userService.loadUserById(deliverTarget);
			if (referUser_now != null) {
				GainCustomer gainCustomer_now = new GainCustomer();
				gainCustomer_now.setCusId(customer);
				gainCustomer_now.setDeliverFrom(deliverTarget);
				gainCustomer_now.setDeliverTarget(customerVo.getDeliverTarget());
				gainCustomer_now.setReason(gainCustomer.getReason());
				gainCustomer_now.setCreateUser(currentLoginUser.getUserId());
				gainCustomer_now.setCreateTime(currentDateTime);
				gainCustomerDao.save(gainCustomer_now);

				CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
				dynamicStatus.setDynamicStatusType(CustomerEventType.GAINCUSTOMER);
				dynamicStatus.setDescription(description);
				if (customer.getResEntrance() != null) {
					dynamicStatus.setResEntrance(customer.getResEntrance());
				}
				dynamicStatus.setStatusNum(1);
				dynamicStatus.setTableName("gain_customer");
				dynamicStatus.setTableId(gainCustomer_now.getId());
				dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
				customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser_now);

				// customerEventService.saveCustomerDynamicStatus(customerId,
				// CustomerEventType.GAINCUSTOMER, description, referUser_now,
				// "");
			}
		}
		return res;

	}
	
	@Override
	public DataPackage getCustomerFollowingRecords(CustomerFolowupVo res, DataPackage dp) throws ApplicationException {
        
		//区分 查询预约信息还是查询某个客户的跟进记录
		//预约信息：前台或者校区主任只能查询其所属校区  
		//跟进记录：某个客户具体的跟进记录
		StringBuilder hql = new StringBuilder(512);
		hql.append(" from CustomerFolowup  where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		
		//具体某个客户的跟进记录
		if (StringUtils.isNotBlank(res.getCustomerId())) {
			hql.append(" and customer.id = :customerId ");
			params.put("customerId", res.getCustomerId());
		}
		//区分是跟进记录还是预约记录
		if (res.getAppointmentType() != null) {
			hql.append(" and appointmentType = :appointmentType ");
			params.put("appointmentType", res.getAppointmentType());
		}
		//预约上门时间 开始
		if (StringUtils.isNotBlank(res.getMeetingTimeStart())) {
			hql.append(" and meetingTime >= :meetingTimeStart ");
			params.put("meetingTimeStart", res.getMeetingTimeStart() + " 00:00:00.000 ");
		}
        //预约上门时间 结束
		if (StringUtils.isNotBlank(res.getMeetingTimeEnd())) {
			hql.append(" and meetingTime <= :meetingTimeEnd ");
			params.put("meetingTimeEnd", res.getMeetingTimeEnd() + " 23:59:59.999 ");
		}
        //记录的创建者
		if (StringUtils.isNotBlank(res.getCreateUserId())) {
			hql.append(" and createUser.userId= :createUserId ");
			params.put("createUserId", res.getCreateUserId());
		}
        //联系方式
		if (StringUtils.isNotBlank(res.getCusMobile())) {
			hql.append(" and customer.contact like :cusMobile ");
			params.put("cusMobile", "%"+res.getCusMobile()+"%");
		}

		// app客户名字查询
		if (StringUtils.isNotBlank(res.getCustomerName())) {
			hql.append(" and customer.name like :customerName ");
			params.put("customerName", "%"+res.getCustomerName()+"%");
		}

		// 如果是前台或校区主任，只显示本校区的
		if (res.getAppointmentType() != null && res.getAppointmentType().equals(AppointmentType.APPOINTMENT)) {
			if (userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST)
					|| userService.isCurrentUserRoleCode(RoleCode.CAMPUS_DIRECTOR)) {
				hql.append(" and appointCampus.id = :appointCampusId ");
				params.put("appointCampusId", userService.getBelongCampus().getId());
			} else {
				hql.append(" and customer.deliverTarget = :customerDeliverTarget " );
				params.put("customerDeliverTarget", userService.getCurrentLoginUser().getUserId());
			}
		}
		if (StringUtils.isNotBlank(dp.getSord()) && StringUtils.isNotBlank(dp.getSidx())) {
			hql.append(" order by "+dp.getSidx()+" "+dp.getSord());			
        } else if (StringUtils.isNotBlank(res.getGetOneAppointment())&&res.getGetOneAppointment().equals("yes")) {
			// 查询最近一次预约信息
			dp.setPageSize(1);
			hql.append(" order by createTime desc limit 1 ");
		}
		// hql.append(" order by createTime desc");
		dp = customerFolowupDao.findPageByHQL(hql.toString(), dp,true,params);

		List<CustomerFolowupVo> list = HibernateUtils.voListMapping((List<CustomerFolowup>) dp.getDatas(),
				CustomerFolowupVo.class);
		for (CustomerFolowupVo cf : list) {
			Map map = userService.getMainDeptAndJob(cf.getCreateUserId());
			if (map != null) {
				//预约人
				cf.setCreateUserName(userService.loadUserById(cf.getCreateUserId()).getName());
				//预约人职位
				cf.setAppointmentUserJobName(map.get("jobName") == null ? "" : map.get("jobName").toString());
			}
			Customer cus = customerDao.findById(cf.getCustomerId());
			cf.setRecentlyRemark(cus.getRemark());
			cf.setRecentlyTime(cus.getDeliverTime());
		}
		dp.setDatas(list);
		return dp;
	}
	
	/**
	 * 根据电话查询公共客户
	 */
	@Override
	public DataPackage findCustomerByContactList(CustomerVo customerVo, DataPackage dataPackage){
		//当前登陆者
		User currentUser = userService.getCurrentLoginUser();
		StringBuilder hql = new StringBuilder(64);
		Map<String, Object> params = Maps.newHashMap();
		Map<String, Object> param = Maps.newHashMap();
		params.put("contact", customerVo.getContact());
		hql.append(" from Customer where contact = :contact ");	
		Organization organization = userService.getBelongCampusByUserId(currentUser.getUserId());
		//#698179校区的前台，只能查询到本校区的客户资源
		if("RECEPTION".equals(customerVo.getWorkBench())) {
			String orgLevel = organization.getOrgLevel();
			if(organization!=null){
				hql.append(" and (blSchool in (select id from Organization WHERE orgLevel LIKE :orgLevel ))");
				params.put("orgLevel",  orgLevel+"%");
			}
		}
		dataPackage = customerDao.findPageByHQL(hql.toString(), dataPackage,true,params);
		List<Customer> list=(List<Customer>)dataPackage.getDatas();
		if(list != null && list.size()>0){
			List<ReceptionistCustomerVo> voList = HibernateUtils.voListMapping(list, ReceptionistCustomerVo.class);
			//ReceptionistCustomerVo vo = voList.get(0);
			for(ReceptionistCustomerVo vo:voList){
			if(vo.getDeliverTarget()!=null){
				Organization org = organizationDao.findById(vo.getDeliverTarget());
				String deliverTargetName = "";
				if(org != null){
					//所属资源池 
					ResourcePool  resourcePool = resourcePoolDao.findById(org.getId());
					if(resourcePool !=null){
						deliverTargetName = resourcePool.getName();
					}else{
						deliverTargetName = org.getName();
					}
					vo.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
				}else{
					deliverTargetName = userService.getUserById(vo.getDeliverTarget()).getName();
					vo.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
				}			
				vo.setDeliverTargetName(deliverTargetName);	
			}
			if(vo.getVisitType()==null || (vo.getVisitType()!=null && vo.getVisitType().equals(VisitType.NOT_COME.getValue()))){
				vo.setIsVisitCome(false);
			}else{
				vo.setIsVisitCome(true);
			}
			//客户所属校区   如果客户所属校区为空 则默认外校 
			if(vo.getBlSchool()!=null){
				Organization blSchool = organizationDao.findById(vo.getBlSchool());
				if(blSchool!=null){
					Organization blSchoolCurrentUser = organizationDao.findById(currentUser.getOrganizationId());
					String orgLevel = blSchool.getOrgLevel();
					String orgLevelCurrentUser = blSchoolCurrentUser.getOrgLevel();
					if(orgLevel.indexOf(orgLevelCurrentUser)!=-1 || orgLevelCurrentUser.indexOf(orgLevel)!=-1){
						//判断客户是否分校客户 
						vo.setLocalSchoolCustomer(true);
					}else{
						vo.setLocalSchoolCustomer(false);
					}
					vo.setBlSchool(blSchool.getName());
				}
			}else{
				vo.setLocalSchoolCustomer(false);
			}
			//学生和学管师 签单的状态下
			String customerId = vo.getId();
			StringBuilder query = new StringBuilder();
			StringBuffer studentName = new StringBuffer();
			StringBuffer studyManager = new StringBuffer();
			query.append(" select s.ID as studentId,s.`NAME` as studentName,u.`NAME` as studyManagerName,s.STATUS as status,s.STUDENT_TYPE as studentType from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID ");
			query.append(" LEFT JOIN `user` u on u.USER_ID = s.STUDY_MANEGER_ID ");
			query.append(" where csr.CUSTOMER_ID = '"+customerId+"'");
			query.append(" and (csr.IS_DELETED =0 OR csr.IS_DELETED is null) ");
			List<Map<Object, Object>> students = studentDao.findMapBySql(query.toString(),param);
			//不活跃的情况下才查询 否则默认是有跟进 而且也是默认有课消的  实际上不一定是 是为了让字段一定有值而已   20170517 xiaojinwang
			//此处totalConsume也是这种情况
			int totalConsume = 0;
			BigDecimal remainAmount = new BigDecimal("0");
			if(students!=null && students.size()>0){
				totalConsume = 0;
			}else{
				totalConsume = 1;
				remainAmount = remainAmount.add(new BigDecimal("1.0"));
			}
			vo.setIsFinishClass(false);
			vo.setIsStopClass(false);
			vo.setIsEnrolled(false);
			for(Map<Object, Object> map:students){
				//判断学生是否有剩余课时 剩余资金
				if(vo.getCustomerActive().equals(CustomerActive.INACTIVE.getValue())){
					query.delete(0, query.length());
					query.append("SELECT COUNT(1) FROM ACCOUNT_CHARGE_RECORDS acr WHERE acr.CHARGE_TYPE = 'NORMAL' AND acr.PRODUCT_TYPE <>'OTHERS' AND ");
					query.append(" acr.STUDENT_ID ='"+map.get("studentId")+"' AND acr.TRANSACTION_TIME > DATE_SUB(curdate(), INTERVAL "+TRANSFER_DAY+ " DAY) ");
					int consume = accountChargeRecordsDao.findCountSql(query.toString(), param);
					totalConsume = totalConsume+consume;
					
					//查询学生的剩余资金
					//
					query.delete(0, query.length());
					query.append(" SELECT REMAINING_AMOUNT as amount from studnet_acc_mv WHERE STUDENT_ID ='"+map.get("studentId")+"' ");
					List<Map<Object, Object>> amountList = studnetAccMvDao.findMapBySql(query.toString(), param);
					if(amountList!=null && amountList.size()>0){
						BigDecimal amount = (BigDecimal)amountList.get(0).get("amount");
						remainAmount = remainAmount.add(amount);
					}
				}else{
					totalConsume = totalConsume+1;
					remainAmount = remainAmount.add(new BigDecimal("1.0"));
				}
				String status = map.get("status")!=null?(String) map.get("status"):"";
				String studentType = map.get("studentType")!=null?(String) map.get("studentType"):"";
				if(!vo.getIsEnrolled()&&StudentType.ENROLLED.getValue().equals(studentType)) {
					vo.setIsEnrolled(true);
				}
				
				if(StudentStatus.FINISH_CLASS.getValue().equals(status)
						&&StudentType.ENROLLED.getValue().equals(studentType) ) {
					//判断是否停课15天以上
					query.delete(0, query.length());
					query.append(" SELECT IFNULL(TIMESTAMPDIFF(DAY,DATE_FORMAT(FINISH_TIME,'%Y-%m-%d'),CURDATE()),0) FROM student WHERE id ='"+map.get("studentId")+"' ");
					int finish_day = studentDao.findCountSql(query.toString(), param);
					if(finish_day > FINISH_CLASS_DAY) {
						vo.setIsFinishClass(true);
					}
				}
				if(StudentStatus.STOP_CLASS.getValue().equals(status)
						&&StudentType.ENROLLED.getValue().equals(studentType)){
					//判断是否停课90天以上
					query.delete(0, query.length());
					query.append(" SELECT IFNULL(TIMESTAMPDIFF(DAY,DATE_FORMAT(FINISH_TIME,'%Y-%m-%d'),CURDATE()),0) FROM student WHERE id ='"+map.get("studentId")+"'  ");
					int stop_day = studentDao.findCountSql(query.toString(), param);
					if(stop_day > STOP_CLASS_DAY) {
						vo.setIsStopClass(true);
					}
				}
				
				
				studentName.append(map.get("studentName")!=null?map.get("studentName")+"，":"");
				studyManager.append(map.get("studyManagerName")!=null?map.get("studyManagerName")+"，":"");
			}
			if(totalConsume>0){
				vo.setCourseConsume(true);
			}else{
				vo.setCourseConsume(false);
			}
			if(remainAmount.compareTo(BigDecimal.ZERO)>0){
				vo.setRemainAmount(true);
			}else{
				vo.setRemainAmount(false);
			}
			
            if(studentName.length()>1){
            	vo.setStudentNames(studentName.substring(0, studentName.length()-1));
            }
			if(vo.getDealStatus()!=null && vo.getDealStatus().equals(CustomerDealStatus.SIGNEUP.getValue())){
	            if(studyManager.length()>1){
	            	vo.setStudyManagerNames(studyManager.substring(0, studyManager.length()-1));
	            }
			}else{
				vo.setStudyManagerNames("");
			}
			
			if(vo.getCustomerActive().equals(CustomerActive.INACTIVE.getValue())){
				//不活跃的情况下才查询 否则默认是有跟进 而且也是默认有课消的  实际上不一定是 是为了让字段一定有值而已   20170517 xiaojinwang
				// 判断是否是两个月内没跟进 判断是否六个月内没有课消 判断 该客户对应的学生是否没有剩余课时 
				query.delete(0, query.length());
				query.append("SELECT COUNT(1) FROM customer_folowup cf WHERE cf.APPOINTMENT_TYPE='FOLLOW_UP' AND cf.CUSTOMER_ID='"+customerId+"' AND cf.CREATE_TIME > DATE_SUB(curdate(),INTERVAL "+TRANSFER_DAY+ " DAY) ");
				int followups = customerFolowupDao.findCountSql(query.toString(), param);
				if(followups>0){
					vo.setFollowing(true);
				}else{
					vo.setFollowing(false);
				}		
			}else{
				vo.setFollowing(true);
			}

			
			
			
			

			}
			dataPackage.setDatas(voList);
			return dataPackage;
		}else {
			DataPackage d = new DataPackage();
			DataPackage d2 = new DataPackage();
			if("RECEPTION".equals(customerVo.getWorkBench())) {
				StringBuilder hql1 = new StringBuilder(64);
				Map<String, Object> params1 = Maps.newHashMap();
				params1.put("contact", customerVo.getContact());
				hql1.append(" from Customer where contact = :contact ");
				d2 = customerDao.findPageByHQL(hql1.toString(), d2,true,params1);
				List<Customer> list1=(List<Customer>)d2.getDatas();
				if(list1 != null && list1.size()>0){
					Customer c = list1.get(0);
					Organization o  = c.getBlCampusId();
					CustomerDealStatus dealStatus = c.getDealStatus();
					String datamessage = (dealStatus!=null?dealStatus.getName():"");
					d.setData(datamessage);
				}
			}
			
			
			return d;
		}
		
		
		
		
		//return customerDao.findCustomerByContactList(contct, dataPackage);
	}

	/**
	 * 数数 除了自己外是否有相同号码
	 * @param contact
	 * @param customerId
	 * @return
	 */
	private int countCustomerNumsExceptSelf(String contact, String customerId) {
		if (StringUtil.isNotBlank(contact) && StringUtil.isNotBlank(customerId)){
			return  customerDao.countCustomerNumsExceptSelf(contact, customerId);
		}else {
			return 0;
		}
	}
	
	
	private Boolean checkTransferCustomer(Customer customer){
		Boolean flag = false;
		
		CustomerDealStatus dealStatus = customer.getDealStatus();
		if(dealStatus!=null && dealStatus != CustomerDealStatus.STAY_FOLLOW){
			//不是跟进中的客户肯定已经经过调配过的  不能转介绍
			return flag;
		}else{
			//如果是待跟进 可以进一步判断是否在资源池里面
			String deliverTarget = customer.getDeliverTarget();
			if(StringUtil.isNotBlank(deliverTarget)){
				User user = userService.loadUserById(deliverTarget);
				if(user!=null){
					return flag;
				}else{
				   //如果是在资源池里面还要 进一步判断是不是经过人的手里
					StringBuffer  sql = new StringBuffer("SELECT count(1) from customer_dynamic_status where CUSTOMER_ID ='"+customer.getId()+"' ");
					sql.append(" and DYNAMIC_STATUS_TYPE ='"+CustomerEventType.DELIVER+"' and DELIVER_TYPE ='"+CustomerDeliverType.PERSONAL_POOL+"'");
					StringBuffer sqlf = new StringBuffer("SELECT count(1) from customer_dynamic_status where CUSTOMER_ID ='"+customer.getId()+"' ");
					sqlf.append(" and (DYNAMIC_STATUS_TYPE ='"+CustomerEventType.GET+"' or DYNAMIC_STATUS_TYPE ='"+CustomerEventType.NEW+"' )");
					
					int count1 = customerEventService.findCustomerDynameicStatusCount(sql.toString());
					int count2 = customerEventService.findCustomerDynameicStatusCount(sqlf.toString());
	                if( count1==0 && count2 ==0 ){
	                	flag = true;
	                	return flag;
	                }else{
	                	return flag;
	                }					
				}
			}else{
				flag = true;
				return flag;
			}
		}

	}
	
	@Override
	public Response checkCustomerContact(String contact, String workbrenchType) {

		//共用接口校验 20170329 转介绍客户  transferCustomer
		
		
		Response response = new Response();
		//CUS
		if (workbrenchType!=null && workbrenchType.startsWith("CUS")){
			//修改的时候排除自己 带上客户id
			int customerNums = countCustomerNumsExceptSelf(contact, workbrenchType);
			if (customerNums>0){
				response.setResultCode(-1);
				response.setResultMessage("系统中存在号码为"+contact+",请重新修改后提交");
				return response;
			}else {
				if (StringUtil.isNotBlank(contact)&&!CheckPhoneNumber.checkPhoneNum(contact)){
					response.setResultCode(-36);//格式错误
					response.setResultMessage("号码格式错误");
					return response;
				}
				response.setResultCode(0);
				response.setResultMessage("校验通过");
				return response;
			}
		}

		if (StringUtil.isNotBlank(contact)&&!CheckPhoneNumber.checkPhoneNum(contact)){
			response.setResultCode(-36);//格式错误
			response.setResultMessage("号码格式错误");
			return response;
		}
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("contact", contact);
		String sql_customer = " select * from customer c where c.CONTACT = :contact ";
		List<Customer> list = customerDao.findBySql(sql_customer,params);

        if(StringUtil.isNotBlank(workbrenchType) && workbrenchType.equals("transferCustomer")){
        	//转介绍客户的校验
        	//（1）系统中不存在的新客户
        	//（2）市场经理或市场专员导入到分公司资源池中的客户，并且没有分配过的客户
        	if(list!=null && list.size()>1){
    			response.setResultCode(-1);
    			response.setResultMessage("该手机号码存在多个客户");
        		return response;        			
        	}else if(list.size()==1){
        		Customer transferCustomer = list.get(0);        		
        		if(!checkTransferCustomer(transferCustomer)){
        			response.setResultCode(-1);
        			response.setResultMessage("转介绍客户校验失败");
        		}else{
        			return response;
        		}      	     		
        	}else{
        		//新客户 校验通过
        		return response;
        	}
        	
        }
		


//		String sql_customer = " select * from customer c where c.CONTACT LIKE '%" + contact + "%' ";
//		List<Customer> list = customerDao.findBySql(sql_customer);

		Map<String, List<Customer>> map = new HashMap<>();

		Customer customer = null;
		
		if (list.size()==0){
			response.setResultCode(0);
			response.setResultMessage("手机号码不存在");
			return response;
		}else if (list.size()>1){
			response.setResultCode(-1);
			response.setResultMessage("该手机号码存在多个客户");
			return response;
		}else {
			Customer c = list.get(0);
			// #699233 CRM优化-咨询师新签规则修改 , 
			if( StringUtil.isNotBlank(workbrenchType) ) {
				boolean isStopClass = false;
				boolean isFinishClass = false;
				boolean isSignStudent = false;
				StringBuilder query = new StringBuilder();
				Map<String, Object> param = Maps.newHashMap();
				query.append(" select s.ID as studentId,s.`NAME` as studentName,u.`NAME` as studyManagerName,s.STATUS as status,s.STUDENT_TYPE as studentType from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID ");
				query.append(" LEFT JOIN `user` u on u.USER_ID = s.STUDY_MANEGER_ID ");
				query.append(" where csr.CUSTOMER_ID = '"+c.getId()+"'");
				List<Map<Object, Object>> students = studentDao.findMapBySql(query.toString(),param);
				for(Map<Object, Object> studentMap:students){
					String status = studentMap.get("status")!=null?(String) studentMap.get("status"):"";
					String studentType = studentMap.get("studentType")!=null?(String) studentMap.get("studentType"):"";
					if(!isSignStudent && StudentType.ENROLLED.getValue().endsWith(studentType)) {
						isSignStudent = true;
					}
					if(StudentStatus.FINISH_CLASS.getValue().equals(status)
							&& StudentType.ENROLLED.getValue().endsWith(studentType)) {
						//判断是否停课15天以上
						query.delete(0, query.length());
						query.append(" SELECT IFNULL(TIMESTAMPDIFF(DAY,DATE_FORMAT(FINISH_TIME,'%Y-%m-%d'),CURDATE()),0) FROM student WHERE id ='"+studentMap.get("studentId")+"' ");
						int finish_day = studentDao.findCountSql(query.toString(), param);
						if(finish_day > FINISH_CLASS_DAY) {
							isFinishClass = true;
						}
					}
					if(StudentStatus.STOP_CLASS.getValue().equals(status)
							&& StudentType.ENROLLED.getValue().endsWith(studentType)){
						//判断是否停课90天以上
						query.delete(0, query.length());
						query.append(" SELECT IFNULL(TIMESTAMPDIFF(DAY,DATE_FORMAT(FINISH_TIME,'%Y-%m-%d'),CURDATE()),0) FROM student WHERE id ='"+studentMap.get("studentId")+"'  ");
						int stop_day = studentDao.findCountSql(query.toString(), param);
						if(stop_day > STOP_CLASS_DAY) {
							isStopClass = true;
						}
					}
				}
				if(isSignStudent && !isFinishClass && !isStopClass) {
					response.setResultCode(13-100);
					response.setResultMessage("该客户学生未达到停课/结课限制时长，不允许新增！");
					return response;
				}else {
					if(isFinishClass && isStopClass ) {
						response.setResultCode(10-100);
						response.setResultMessage(c.getId());
						return response;
					}else if(isFinishClass) {
						response.setResultCode(11-100);
						response.setResultMessage(c.getId());
						return response;
					}else if(isStopClass) {
						response.setResultCode(12-100);
						response.setResultMessage(c.getId());
						return response;
					}
				}
			}
			
			
			if ( "RECEPTION".equals(workbrenchType) ){
				if(c.getCustomerActive() ==CustomerActive.INACTIVE){
					//如果是僵尸客户允许录入
					response.setResultCode(0);
					response.setResultMessage("重新激活老客户");
					return response;
				}
				if (c.getDeliverType() == CustomerDeliverType.CUSTOMER_RESOURCE_POOL){
					response.setResultCode(1-100);
					response.setResultMessage(c.getId());
					return response;
				}
				if (c.getDealStatus() == CustomerDealStatus.FOLLOWING){
					response.setResultCode(-3);
					String deliverTarget = c.getDeliverTarget();
					User user = userDao.findById(deliverTarget);
					Organization belongCampusByOrgId = userService.getBelongCampusByOrgId(user.getOrganizationId());
					String name = belongCampusByOrgId.getName();
					response.setResultMessage(c.getId()+","+name+"的"+user.getName()+"正在跟进这个客户！");
					return response;
				}

				if (c.getDealStatus() == CustomerDealStatus.INVALID){
					response.setResultCode(2-100);
					response.setResultMessage(c.getId());
					return response;
				}
				if(c.getDealStatus() == CustomerDealStatus.SIGNEUP){
					response.setResultCode(4-100);
					response.setResultMessage("该客户已签单，不允许录入");
					return response;
				}
				customer = c;
			}else if( "MANAGER".equals(workbrenchType) ){
				if(c.getCustomerActive() ==CustomerActive.INACTIVE){
					//如果是僵尸客户允许录入
					response.setResultCode(0);
					response.setResultMessage("重新激活老客户");
					return response;
				}
				if (c.getDeliverType() == CustomerDeliverType.CUSTOMER_RESOURCE_POOL){
					response.setResultCode(1-100);
					response.setResultMessage(c.getId());
					return response;
				}
				if (c.getDealStatus() == CustomerDealStatus.INVALID){
					response.setResultCode(2-100);
					response.setResultMessage(c.getId());
					return response;
				}
				response.setResultCode(3-100);
//				response.setResultMessage("客户维护中，校验不通过！");
				response.setResultMessage(c.getName()+"当前为"+c.getDealStatus().getName()+"状态，不允许重复录入！");
				return response;
			}else {
				Boolean flag = false;//是否需要进行之前的校验逻辑
				if(WorkbrenchType.ON_LINE.getValue().equals(workbrenchType)||WorkbrenchType.CALL_OUT.getValue().equals(workbrenchType)
				||WorkbrenchType.OUTCALL.getValue().equals(workbrenchType)){
					//网络工作台 和咨询师工作台 外呼工作台
					if(c.getCustomerActive() ==CustomerActive.INACTIVE){
						//如果是僵尸客户允许录入
						response.setResultCode(0);
						response.setResultMessage("重新激活老客户");
						return response;
					}else{
						flag =true;
					}				
				}else{
					flag = true;
				}
				if(flag){
					if (c.getDeliverType() == CustomerDeliverType.CUSTOMER_RESOURCE_POOL){
						response.setResultCode(1-100);
						response.setResultMessage(c.getId());
						return response;
					}
					if (c.getDealStatus() == CustomerDealStatus.INVALID){
						response.setResultCode(2-100);
						response.setResultMessage(c.getId());
						return response;
					}
					if (c.getDealStatus() == CustomerDealStatus.FOLLOWING){
						//如果是网络工作台查询 还需要查询跟进人的姓名和职位 20170411 
						
						if(WorkbrenchType.ON_LINE.getValue().equals(workbrenchType)){
							if(StringUtil.isNotBlank(c.getDeliverTarget())){
								User user = userService.loadUserById(c.getDeliverTarget());
								Map jobMap = userService.getMainDeptAndJob(c.getDeliverTarget());
								if(user!=null && jobMap!=null){
									String msg = user.getName()+jobMap.get("jobName");
									response.setResultCode(-3);
									response.setResultMessage(c.getId()+"#"+msg+"#"+c.getName()+"正在跟进中，不允许重复录入！");
									return response;
								}
								
							}
							
						}						
						response.setResultCode(-3);
						response.setResultMessage(c.getName()+"正在跟进中，不允许重复录入！");
						return response;
					}
					
					customer = c;
				}
			
			}

		}
		response.setResultCode(-4);
		if(customer!=null){
			response.setResultMessage(customer.getName()+"当前为"+customer.getDealStatus().getName()+"状态，不允许重复录入！");
		}else{
			response.setResultMessage("客户维护中");
		}
		
		return response;
	}




	@Override
	public Boolean isExceedPeriod(String customerId) {
		StringBuilder sql = new StringBuilder(128);
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId", customerId);
		sql.append(" select * from customer_folowup where CUSTOMER_ID= :customerId AND MEETING_CONFIRM_TIME IS NOT NULL AND  APPOINTMENT_TYPE ='"+AppointmentType.APPOINTMENT.getValue()+"' ORDER BY MEETING_CONFIRM_TIME DESC ");	
		//获取最新的记录
		//cf.getCreateUserId()
		List<CustomerFolowup> list = customerFolowupDao.findBySql(sql.toString(),params);
		if(list!=null && list.size()>0){
			CustomerFolowup follow=list.get(0);
			//当前的时间与最近一次登记的时间比较是否超过三十天
			int days =DateTools.daysOfTwo(follow.getMeetingConfirmTime(), DateTools.getCurrentDateTime());
			if(days > 30){
				return true;
			}else{
				return false;
			}			
		}else{
			return false;
		}
	}
	
	
	@Override
	public Response allocateCustomerResource(String cusId, CustomerVo customerVo) {
		
		CustomerDeliverType deliverType = customerVo.getDeliverType();
		String deliverTarget = customerVo.getDeliverTarget();
		
		String[] cusIdArray = cusId.split(",");
		Response re = new Response();
		boolean flag = false;
		CustomerDealStatus customerDealStatus = null;
		String resultMSg = "";
		User referUser = userService.getCurrentLoginUser();
		String userId = referUser.getUserId();
		//资源池容量的校验
		re = resourcePoolService.getResourcePoolVolume(cusIdArray.length, deliverTarget);
		
		if (re.getResultCode() == 0) {
			for (String id : cusIdArray) {
				Customer customer = this.findById(id);
				String before_deliverTarget = customer.getDeliverTarget();
				customerDealStatus = customer.getDealStatus();
				// 自己的资源是可以往外分配的，
				if ((customer.getDeliverType() != null // 旧数据类型不为空// 并且也在个人资源池 // 分配给个人
						&& customer.getDeliverType() == CustomerDeliverType.PERSONAL_POOL
						&& customer.getDeliverTarget()!=null// 跟进人不为空并且不等于当前跟进人
						&& customer.getDealStatus()==CustomerDealStatus.FOLLOWING 
						&& !userId.equals(before_deliverTarget)// 旧数据状态是跟进中																						
				)||customer.getDealStatus()==CustomerDealStatus.SIGNEUP) {
					flag = true;
					resultMSg += customer.getName() + ",";
				} else {
					customer.setDeliverType(deliverType);
					
					
					
					//customer.setTransferFrom(userService.getBelongCampus().getId());
					CustomerDealStatus dealStatus = CustomerDealStatus.STAY_FOLLOW;
//					if (CustomerDeliverType.CUSTOMER_RESOURCE_POOL.equals(deliverType)) {
//						// 修改了资源池
//						if (StringUtil.isNotBlank(deliverTarget)
//								&& !deliverTarget.equals(customer.getDeliverTarget())) {
//							// 传入进来的资源池
//							ResourcePool repool = resourcePoolService.findResourcePoolById(deliverTarget);
//							if (repool == null || (repool != null && repool.getStatus().equals(ValidStatus.INVALID))) {
//								// 当前组织架构
//								Organization rrrpool = userService.getCurrentLoginUserOrganization();
//								// 如果跟传进来的一样就查找他的父类
//								if (rrrpool != null && deliverTarget.equals(rrrpool.getId())) {
//									// 没有就先查所属再查父类，如果父类都找不到就gameOver了
//									getBelongResourcePool(customer, rrrpool);
//								} else if (rrrpool != null) {
//									// 当前用户的资源池
//									ResourcePool repool2 = resourcePoolService.findResourcePoolById(rrrpool.getId());
//									if (repool2 != null && repool2.getStatus().equals(ValidStatus.VALID)) {// 有资源池就设置为当前这个资源池
//										customer.setDeliverTarget(rrrpool.getId());
//									} else {// 没有就先查所属再查父类，如果父类都找不到就gameOver了
//										getBelongResourcePool(customer, rrrpool);
//									}
//								}
//							}
//						}
//					}else if(CustomerDeliverType.PERSONAL_POOL.equals(deliverType)){
//						customer.setDeliverTarget(deliverTarget);
//					}
					customer.setDeliverTarget(deliverTarget);
					customer.setDealStatus(dealStatus);
					re = this.changeCustomerStatus(customer);
					String deliverTargetName = "";
					
					User user = userService.loadUserById(deliverTarget);
					if(user!=null){
						deliverType = CustomerDeliverType.PERSONAL_POOL;
						//判断负责人是否是营主，推送消息
				    	if(StringUtil.isNotBlank(customer.getDeliverTarget()) && StringUtil.isNotBlank(customer.getId())) {
				    		this.judgeUserSendMsg(deliverTarget,userService.getCurrentLoginUserId());
				    	}
					}else{
						deliverType = CustomerDeliverType.CUSTOMER_RESOURCE_POOL;
					}
					if (CustomerDeliverType.PERSONAL_POOL ==deliverType) {
						deliverTargetName = user.getName();
					} else if (CustomerDeliverType.CUSTOMER_RESOURCE_POOL ==deliverType) {
						Organization org = userService.getOrganizationById(deliverTarget);
						if (org != null)
							deliverTargetName = org.getName();
					}
					
					
					String description = "分配客户：" + (deliverType == null ? "" : deliverType.getName()) + " - "
							+ deliverTargetName;
					if (re.getResultCode() == 0) {
						
						//增加一条分配人变动记录 
						DeliverTargetChangeRecord record = new DeliverTargetChangeRecord();
						record.setCustomerId(id);
						record.setPreviousTarget(before_deliverTarget);
						record.setCurrentTarget(customer.getDeliverTarget());
						record.setRemark("分配客户资源变更分配人记录");
						record.setCreateUserId(userId);
						record.setCreateTime(DateTools.getCurrentDateTime());
						String recordId = deliverTargetChangeService.saveDeliverTargetChangeRecord(record);
						
						
						CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
						dynamicStatus.setDynamicStatusType(CustomerEventType.DELIVER);
						dynamicStatus.setDescription(description);
						if(customer.getResEntrance()!=null){
							dynamicStatus.setResEntrance(customer.getResEntrance());
						}else if(customer.getPreEntrance()!=null){
							dynamicStatus.setResEntrance(customer.getPreEntrance());
						}
						dynamicStatus.setStatusNum(1);
						dynamicStatus.setTableName("delivertarget_change_record");
						dynamicStatus.setTableId(recordId);
						dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
						dynamicStatus.setDeliverType(deliverType);
		                customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);	
						
						
						//customerEventService.saveCustomerDynamicStatus(id, CustomerEventType.DELIVER, description);
					}
				}
			}
		} else if (re.getResultCode() == -1) {
			re.setResultMessage("分配对象没有配置资源池");
		} else if (re.getResultCode() == -2) {
			re.setResultMessage("容量已超出");
		}
		if (flag) {		
			re.setResultCode(-1);
			if(customerDealStatus!=null && customerDealStatus == CustomerDealStatus.FOLLOWING){
			   re.setResultMessage("客户" + resultMSg.substring(0, resultMSg.length() - 1) + "正在跟进中，无法分配。");
			}else if(customerDealStatus!=null && customerDealStatus == CustomerDealStatus.SIGNEUP){
			   re.setResultMessage("客户" + resultMSg.substring(0, resultMSg.length() - 1) + "已经签合同，无法分配。");
			}
		}
		return re;
	}
	
	@Override
	public DataPackage getResourcePoolCustomer(DistributeCustomerVo vo, DataPackage dataPackage, String resourceId) {
		//获取资源池里的客户
		//TODO 
		StringBuilder cusSql=new StringBuilder(256);	
		cusSql.append(" select c.id as cusId,c.name as cusName,c.RES_ENTRANCE as resEntrance,c.REMARK as remark,");
		cusSql.append(" c.LAST_DELIVER_NAME as lastDeliverName, c.DELIVER_TIME as deliverTime,c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer,");	
		cusSql.append(" c.LAST_FOLLOW_UP_TIME as lastFollowUpTime,c.INTENTION_CAMPUS_ID as intentionCampusId, ");
		cusSql.append(" d.`NAME` as resEntranceName,dd.`NAME` as intentionOfTheCustomerName,c.CONTACT as contact ,c.DEAL_STATUS as dealStatus,c.BL_CAMPUS_ID as blCampusId ");		
		cusSql.append(" from customer c ");		
		cusSql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID "); 
		cusSql.append(" LEFT JOIN data_dict dd on c.INTENTION_OF_THE_CUSTOMER = dd.ID "); 
		cusSql.append(" where 1=1 ");
//        cusSql.append(" and c.DEAL_STATUS NOT in ('SIGNEUP','INVALID') ");
		//由于无效审核导致
		Boolean releaseable = true;
		User user = userService.loadUserById(resourceId);
		if(user!=null){
			 //cusSql.append(" and c.DEAL_STATUS NOT in ('"+CustomerDealStatus.SIGNEUP.getValue()+"','"+CustomerDealStatus.INVALID.getValue()+"') ");
		}else{
			
			 ResourcePool resourcePool = resourcePoolService.findResourcePoolById(resourceId);
			 if(resourcePool!=null){
				 if("0".equals(resourcePool.getCycleType())){
					releaseable = false; 
				 }
			 }
			// cusSql.append(" and c.DEAL_STATUS NOT in ('"+CustomerDealStatus.SIGNEUP+"') ");
		}
		
		
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(resourceId)){
			//资源池限制
			cusSql.append(" and c.DELEVER_TARGET = :resourceId ");
			params.put("resourceId", resourceId);
		}
		//联系方式
		if(StringUtil.isNotBlank(vo.getContact())){
			cusSql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
		// 查询姓名
		if (StringUtils.isNotBlank(vo.getCusName())) {
			cusSql.append(" and c.NAME like :name ");
			params.put("name", "%"+vo.getCusName()+"%");
		}

		// 资源入口
		if (StringUtils.isNotBlank(vo.getResEntranceId())) {
			cusSql.append(" and c.RES_ENTRANCE= :resEntranceId ");
			params.put("resEntranceId", vo.getResEntranceId());
		}
        //分配人
		if(StringUtils.isNotBlank(vo.getLastDeliverName())){
			cusSql.append(" and c.LAST_DELIVER_NAME like :lastDeliverName ");
			params.put("lastDeliverName", "%"+vo.getLastDeliverName()+"%");
		}
		//客户状态
		if (StringUtils.isNotBlank(vo.getDealStatus())) {
			cusSql.append(" and c.DEAL_STATUS = :dealStatus ");
            params.put("dealStatus", vo.getDealStatus());
		}
		//所属校区
		if(StringUtil.isNotBlank(vo.getBlCampusId())){
			cusSql.append(" and c.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		}

		
		//意向度
		if (StringUtils.isNotBlank(vo.getIntentionOfTheCustomerId())) {
			cusSql.append(" and c.INTENTION_OF_THE_CUSTOMER = :intentionOfTheCustomerId ");
			params.put("intentionOfTheCustomerId", vo.getIntentionOfTheCustomerId());
		}
		//如果这三个条件有一个不为空则关联学生表
		if(StringUtils.isNotBlank(vo.getStudentName())||StringUtils.isNotBlank(vo.getSchoolId())||StringUtils.isNotBlank(vo.getGradeId())){
			cusSql.append(" and c.id in ( select cs.CUSTOMER_ID from customer_student_relation cs left join ");
			cusSql.append(" student s on cs.STUDENT_ID = s.ID where 1=1 ");
			//学生姓名
			if(StringUtils.isNotBlank(vo.getStudentName())){
               cusSql.append(" and s.NAME like :studentName "); 
               params.put("studentName", "%"+vo.getStudentName()+"%");
			}
			//选择就读学校  这里传进来的是学校的Id 不是名字
			if(StringUtils.isNotBlank(vo.getSchoolId())){
			   cusSql.append(" and s.SCHOOL = :schoolId ");
			   params.put("schoolId", vo.getSchoolId());
			}
			//学生的年级   传入 学生年级的Id
			if(StringUtils.isNotBlank(vo.getGradeId())){
				cusSql.append(" and s.GRADE_ID =:gradeId ");
				params.put("gradeId", vo.getGradeId());
			}	
			
			cusSql.append(" ) ");
		}
		//最新分配时间  开始时间 
		if (StringUtils.isNotBlank(vo.getDeliverBeginDate())) {
			cusSql.append(" and c.DELIVER_TIME >= :startDate ");
			params.put("startDate", vo.getDeliverBeginDate()+" 00:00:00");
		}
        //最新分配时间 结束时间
		if (StringUtils.isNotBlank(vo.getDeliverEndDate())) {
			cusSql.append(" and c.DELIVER_TIME <= :endDate ");
			params.put("endDate", vo.getDeliverEndDate()+" 23:59:59");
		}	
		
		//最新跟进时间
		if(StringUtils.isNotBlank(vo.getFollowupBeginDate())){		
			cusSql.append(" and c.LAST_FOLLOW_UP_TIME >= '"+vo.getFollowupBeginDate()+" 00:00:00.000' ");
		}			
		if(StringUtils.isNotBlank(vo.getFollowupEndDate())){
			cusSql.append(" and c.LAST_FOLLOW_UP_TIME <= '"+vo.getFollowupEndDate()+" 23:59:59.999' ");
		}
		//意向校区
		if(StringUtil.isNotBlank(vo.getIntentionCampusId())){
			cusSql.append(" and c.INTENTION_CAMPUS_ID = '"+vo.getIntentionCampusId()+"' ");
		}

		

		//增加过滤条件
		if(user!=null){
			Map<String, String> map_id = getNetWorkGroupOUTCALLOrgId();
			Organization organization = userService.getUserMainDeptByUserId(user.getUserId());
			String orgId=organization.getId();
			
			if (map_id.get("network") != null && orgId.equals(map_id.get("network"))) {
				//网络 不加设置限制

	            
			} else if (map_id.get("outcall") != null && orgId.equals(map_id.get("outcall"))) {
				Boolean isWHZY = userService.isUserRoleCode(user.getUserId(), RoleCode.OUTCALL_SPEC);		
				if (isWHZY) {
					organization = userService.getBelongBranchByUserId(user.getUserId());
				} else {
					organization = userService.getBelongCampusByUserId(user.getUserId());
				}
				String orgLevel = organization.getOrgLevel();
				params.put("orgLevel",  orgLevel+"%");
				cusSql.append(
						" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
			} else {
				organization = userService.getBelongCampusByUserId(user.getUserId());
				String orgLevel = organization.getOrgLevel();
				params.put("orgLevel",  orgLevel+"%");
				cusSql.append(
						" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
			}
		}
		
		
		
		
		
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());			
		} 
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);

		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));	
        
		//将查询结果转换为vo		
		List<DistributeCustomerVo> voList =new ArrayList<DistributeCustomerVo>();
		StringBuilder studentBuffer = new StringBuilder(" ");
		StringBuilder schoolBuffer = new StringBuilder(" ");
		StringBuilder gradeBuffer = new StringBuilder(" ");
		DistributeCustomerVo customerVo = null;
        Map<String, Object> param = Maps.newHashMap();
		for(Map<Object,Object> tmaps : list){
			Map<String,String> maps = (Map)tmaps;
		    customerVo = new DistributeCustomerVo();
		    customerVo.setReleaseable(releaseable);
			String cusId =maps.get("cusId");
			customerVo.setCusId(cusId!=null ? cusId:null);
			customerVo.setCusName(maps.get("cusName")!=null? maps.get("cusName"):null);
			customerVo.setContact(maps.get("contact"));
			//设置资源入口
			customerVo.setResEntranceId(maps.get("resEntrance"));
			customerVo.setResEntranceName(maps.get("resEntranceName"));
            //分配人
			customerVo.setLastDeliverName(maps.get("lastDeliverName"));
			//最后分配时间
			customerVo.setDeliverTime(maps.get("deliverTime"));
			//意向度
			customerVo.setIntentionOfTheCustomerId(maps.get("intentionOfTheCustomer"));
			customerVo.setIntentionOfTheCustomerName(maps.get("intentionOfTheCustomerName"));
			//备注信息
			customerVo.setRemark(maps.get("remark"));
			customerVo.setFollowupRemark(maps.get("remark"));
			//客户状态
			if(maps.get("dealStatus")!=null){
				customerVo.setDealStatusName(CustomerDealStatus.valueOf(maps.get("dealStatus")).getName());
				customerVo.setDealStatus(maps.get("dealStatus"));
			}
			//最新跟进时间
			if(maps.get("lastFollowUpTime")!=null){
				customerVo.setLastFollowUpTime(maps.get("lastFollowUpTime"));
			}
			//设置意向校区
			if(StringUtils.isNotBlank(maps.get("intentionCampusId"))){
				customerVo.setIntentionCampusName(organizationService.findById(maps.get("intentionCampusId")).getName());
			}
			
			//所属校区
			if(StringUtil.isNotBlank(maps.get("blCampusId"))){
				Organization blCampus = organizationService.findById(maps.get("blCampusId"));
				if(blCampus!=null && blCampus.getOrgType() == OrganizationType.CAMPUS){
					customerVo.setBlCampusName(blCampus.getName());
				}else{
					customerVo.setBlCampusName("");
				}
			}
			
			
			
			//跟进备注
			StringBuilder query =new StringBuilder(" select cf.remark as remark from customer_folowup cf where appointment_type ='"+AppointmentType.FOLLOW_UP.getValue()+"' ");
			query.append(" and cf.customer_id = '"+cusId+"'  ORDER BY cf.create_time desc LIMIT 1 ");
			
			List<Map<Object, Object>> customerFolowups=customerFolowupDao.findMapBySql(query.toString(),param);
			if (customerFolowups.size() > 0) {
				
				customerVo.setFollowupRemark(customerFolowups.get(0).get("remark")!=null?customerFolowups.get(0).get("remark").toString():"");
			}
			//学员姓名 就读学校 就读年级
			//学生姓名  多个 用逗号,分割开//就读学校 多个 用逗号,分割开   
			String sql ="SELECT s.`NAME` as studentName,ss.`NAME` as schoolName ,d.`NAME` as gradeName from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID LEFT JOIN student_school ss on s.SCHOOL = ss.ID "+ 
			    " LEFT JOIN data_dict d on s.GRADE_ID = d.ID " +" where csr.CUSTOMER_ID = '"+cusId+"' and csr.IS_DELETED = 0 ";  
			List<Map<Object, Object>> resultList=customerDao.findMapBySql(sql,param);
			if(resultList!=null && resultList.size()>1){				
				for(Map<Object, Object> tmap:resultList){
					Map<String, Object> map = (Map)tmap;
					if(map.get("studentName")!=null){
						studentBuffer.append(map.get("studentName")+",");
					}
					if(map.get("schoolName")!=null){
						schoolBuffer.append(map.get("schoolName")+",");
					}
					if(map.get("gradeName")!=null){
						gradeBuffer.append(map.get("gradeName")+",");
					}
					
				}
		        String studentList = studentBuffer.toString();
		        String schoolList = schoolBuffer.toString();
		        String gradeList = gradeBuffer.toString();
		        if(StringUtils.isNotBlank(studentList)){
		        	customerVo.setStudentNameList(new String(studentList.substring(0, studentList.length()-1)));
		        }
		        if(StringUtils.isNotBlank(schoolList)){
		        	customerVo.setSchoolNameList(new String(schoolList.substring(0, schoolList.length()-1)));
		        }
		        if(StringUtils.isNotBlank(gradeList)){
		        	customerVo.setGradeNameList(new String(gradeList.substring(0, gradeList.length()-1)));
		        }
		        
		        //清空Buffer
		        studentBuffer.delete(0, studentBuffer.length());
		        schoolBuffer.delete(0, schoolBuffer.length());
		        gradeBuffer.delete(0, gradeBuffer.length());
			}else if(resultList!=null && resultList.size()==1){
				Map<Object, Object> map=resultList.get(0);
				if(map.get("studentName")!=null){
					customerVo.setStudentNameList(map.get("studentName").toString());
				}
				if(map.get("schoolName")!=null){
					customerVo.setSchoolNameList(map.get("schoolName").toString());
				}
				if(map.get("gradeName")!=null){
					customerVo.setGradeNameList(map.get("gradeName").toString());
				}
			}
			voList.add(customerVo);
		}
		dataPackage.setDatas(voList);
        return dataPackage;
		
		
	}

	@Override
	public DataPackage loadCustomerRecord(DataPackage dataPackage, CustomerVo customerVo) {
		//前台工作台  --客户管理 -前台客户管理 ----录入客户列表
		//修改为只有前台和运营主任可以看到
		
		User currentUser=userService.getCurrentLoginUser();
		Organization organization = userService.getBelongCampus();
		StringBuilder cusSql=new StringBuilder(512);	
		cusSql.append(" select c.id as cusId,c.name as cusName,c.RES_ENTRANCE as resEntrance,c.CREATE_TIME as createTime,c.CONTACT as contact,");
		cusSql.append(" c.CREATE_USER_ID as userId,c.DELEVER_TARGET as deliverTarget, c.DEAL_STATUS as dealStatus,c.REMARK as remark,");
		cusSql.append(" d.`NAME` as resEntranceName,u.`NAME` as userName,uu.`NAME` as deliverTargetName ");
		cusSql.append(" from customer c ");	
		cusSql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID "); 
		cusSql.append(" LEFT JOIN `user` u on c.CREATE_USER_ID = u.USER_ID ");
		cusSql.append(" LEFT JOIN `user` uu on c.DELEVER_TARGET = uu.USER_ID ");
		cusSql.append(" where 1=1 ");

		Map<String, Object> params = Maps.newHashMap();
		// 查询姓名
		if (StringUtils.isNotBlank(customerVo.getName())) {
			cusSql.append(" and c.NAME like :name ");
			params.put("name", "%"+customerVo.getName()+"%");
		}
		//联系方式
		if(StringUtil.isNotBlank(customerVo.getContact())){
			cusSql.append(" and c.CONTACT = :contact ");
			params.put("contact", customerVo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(customerVo.getResEntranceId())) {
			cusSql.append(" and c.RES_ENTRANCE= :resEntranceId ");
			params.put("resEntranceId", customerVo.getResEntranceId());
		}

		//录入者 
        if(StringUtil.isNotBlank(customerVo.getCreateUserName())){
        	cusSql.append(" and (c.CREATE_USER_ID in (select user_id from user where name like :createUserName ) )");
        	params.put("createUserName", "%"+customerVo.getCreateUserName()+"%");
        }
	
		//跟进人
		if(StringUtils.isNotBlank(customerVo.getDeliverTarget())){
			cusSql.append("and c.DELEVER_TARGET = :deliverTarget ");
			params.put("deliverTarget", customerVo.getDeliverTarget());
		}
		if(StringUtils.isNotBlank(customerVo.getDeliverTargetName())){
			//通过getDeliverTargetName 获取 DeliverTarget						
			cusSql.append(" and (c.DELEVER_TARGET in (select user_id from user where name like :deliverTargetName ) or c.DELEVER_TARGET in (select id from organization  where name like :deliverTargetName ) ) ");					
			params.put("deliverTargetName", "%"+customerVo.getDeliverTargetName()+"%");
		}		
           //客户的处理状态 dealStatus 	
		if(customerVo.getDealStatus()!=null){
			cusSql.append(" and c.DEAL_STATUS = :dealStatus ");
			params.put("dealStatus", customerVo.getDealStatus().getValue());
		}
		
		//备注
		if(StringUtils.isNotBlank(customerVo.getRemark())){
			cusSql.append(" and c.REMARK like :remark ");
			params.put("remark", "%"+customerVo.getRemark()+"%");
		}
		//录入时间
		if(StringUtils.isNotBlank(customerVo.getStartDate())){
			cusSql.append(" and c.CREATE_TIME >= :startDate ");
			params.put("startDate", customerVo.getStartDate()+" 00:00:00");
		}
		if(StringUtils.isNotBlank(customerVo.getEndDate())){
			cusSql.append(" and c.CREATE_TIME <= :endDate ");
			params.put("endDate", customerVo.getEndDate()+" 23:59:59");
		}	
		//客户列表配置权限 
		List<String> roleCodes = new ArrayList<>();		
		if (userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST)) {
			roleCodes.add(RoleCode.RECEPTIONIST.getValue());		
		}else if(userService.isCurrentUserRoleCode(RoleCode.OPERATION_DIRECTOR)){
			roleCodes.add(RoleCode.RECEPTIONIST.getValue());
			roleCodes.add(RoleCode.OPERATION_DIRECTOR.getValue());			
		}else{
			return dataPackage;
//			cusSql.append(" and c.DELEVER_TARGET = :DELEVER_TARGET ");
//			params.put("DELEVER_TARGET", currentUser.getUserId());
		}
		if(roleCodes.size()>0){
			List<Map<String, String>> list ;
			if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
				list= userService.getUserByOrganizationAndRoleCodeNew(organization.getOrgLevel(), roleCodes);
			}else{
				list = userService.getUserByOrganizationAndRoleCode(organization.getOrgLevel(), roleCodes);
			}
            //StringBuilder userIds = new StringBuilder();
            List<String> userIds = new ArrayList<>();
            for(Map<String, String> map:list){
            	//userIds.append("'"+map.get("userId")+"',");
            	userIds.add(map.get("userId"));
            }
            if(userIds.size()>=1){
            	cusSql.append(" and c.CREATE_USER_ID in ( :userIds ) ");
            	params.put("userIds", userIds);
            }else{
            	return dataPackage;
//            	cusSql.append(" and c.CREATE_USER_ID= :createUserId ");
//            	params.put("createUserId", currentUser.getUserId());
            }
		}
		
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());			
		} 
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));
		List<CustomerVo> result = new ArrayList<>();
		CustomerVo vo = null;
		for(Map<Object,Object> tmap:list){
			Map<String, String> map =(Map)tmap; 
			vo = new CustomerVo();
			vo.setId(map.get("cusId"));
			vo.setCusId(map.get("cusId"));
			vo.setName(map.get("cusName"));
			vo.setContact(map.get("contact"));
			vo.setResEntranceId(map.get("resEntrance"));
			vo.setResEntranceName(map.get("resEntranceName"));
			Object createTime = map.get("createTime");
			vo.setCreateTime(createTime!=null?createTime.toString():"");
			vo.setCreateUserName(map.get("userName"));
			vo.setDeliverTargetName(map.get("deliverTargetName")!=null?map.get("deliverTargetName"):"-");
			vo.setDeliverTarget(map.get("deliverTarget"));
			if(map.get("dealStatus")!=null){
				vo.setDealStatusName(CustomerDealStatus.valueOf(map.get("dealStatus")).getName());
				vo.setDealStatus(CustomerDealStatus.valueOf(map.get("dealStatus")));
			}else{
				
				vo.setDealStatusName("-");
			}
			vo.setRemark(map.get("remark"));
			result.add(vo);
		}
		dataPackage.setDatas(result);
		return dataPackage;
	}

	@Override
	public List<TransferCustomerCampus> getTransferCustomerRecord(String customerId) {
		StringBuilder query = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId", customerId);
		query.append(" SELECT * from transfer_customer WHERE CUS_ID = :customerId and RECEIVE_TIME is null ");
	    return transferCustomerCampusDao.findBySql(query.toString(),params);
	}

	@Override
	public void updateTransferCustomer(TransferCustomerCampus transferCampus) {
		transferCustomerCampusDao.save(transferCampus);		
	}
	
	@Override
	public Response setCustomerVisitCome(String customerId) {
		Customer customer = customerDao.findById(customerId);
		Response response = new Response();
		if(customer ==null){
			response.setResultCode(-1);
			response.setResultMessage("设置上门失败");
			return response;			
		}
		if(StringUtil.isBlank(customer.getDeliverTarget())){
			response.setResultCode(-1);
			response.setResultMessage("客户无人跟进，设置上门失败");
			return response;
		}
		User user = userDao.findById(customer.getDeliverTarget());		
		if(user==null){
			response.setResultCode(-1);
			response.setResultMessage("客户无人跟进，设置上门失败");
			return response;			
		}

		User referUser = userService.getCurrentLoginUser();
		VisitType visitType = customer.getVisitType();
		if (visitType == null || (visitType != null && visitType == VisitType.NOT_COME)) {
			// 原来的上门标记为空 或者为其他标记才增加上门数
			CustomerDynamicStatus dynamicStatus3 = new CustomerDynamicStatus();
			dynamicStatus3.setDynamicStatusType(CustomerEventType.VISITCOME);
			dynamicStatus3.setDescription("增加上门数");
			if (customer.getResEntrance() != null) {
				dynamicStatus3.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus3.setStatusNum(1);
			dynamicStatus3.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus3, referUser);
		}

		customer.setVisitType(VisitType.PARENT_COME);
		customer.setVisitComeDate(new Date());
		customerDao.save(customer);
		response.setResultCode(0);
		response.setResultMessage("设置上门成功");

		CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
		dynamicStatus.setDynamicStatusType(CustomerEventType.VISITCOME_SET);
		dynamicStatus.setDescription("标记客户:" + customer.getName() + "上门到访");
		if (customer.getResEntrance() != null) {
			dynamicStatus.setResEntrance(customer.getResEntrance());
		}
		dynamicStatus.setStatusNum(1);
		dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
		customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser);

		CustomerDynamicStatus dynamicStatus_come = new CustomerDynamicStatus();
		dynamicStatus_come.setDynamicStatusType(CustomerEventType.VISITCOME_CUSTOMER);
		dynamicStatus_come.setDescription("客户已上门");
		if (customer.getResEntrance() != null) {
			dynamicStatus_come.setResEntrance(customer.getResEntrance());
		}
		dynamicStatus_come.setStatusNum(1);
		dynamicStatus_come.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
		customerEventService.addCustomerDynameicStatus(customer, dynamicStatus_come, user);

		return response;
	}
	
	@Override
	public DataPackage getChangeUserRoleRecords(DataPackage dataPackage, ChangeUserRoleRecordVo recordVo) {
		
		//要做权限限制
		User user = userService.getCurrentLoginUser();
		Organization organization = organizationService.findById(user.getOrganizationId());
		Organization belong = null;
		if(organization!=null && organization.getBelong()!=null){
			belong = organizationService.findById(organization.getBelong());
		}
		
		StringBuffer sql = new StringBuffer(128);
		
		sql.append(" select cur.user_name,cur.dept_name,cur.campus_name,cur.branch_name,cur.create_time from change_userrole_record cur ");
		sql.append(" left join organization o on o.id = cur.campus_id ");
		sql.append(" where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if(belong!=null){
			sql.append(" and o.orgLevel like :orgLevel ");
			params.put("orgLevel", belong.getOrgLevel()+"%");
		}else{
			sql.append(" and o.orgLevel like :orgLevel ");
			params.put("orgLevel", organization.getOrgLevel()+"%");
		}
		
		if(StringUtils.isNotBlank(recordVo.getUserName())){
			sql.append(" and cur.user_name = :userName ");
			params.put("userName", recordVo.getUserName());
		}
		if(StringUtils.isNotBlank(recordVo.getDeptId())){
			sql.append(" and cur.dept_id = :deptId ");
			params.put("deptId", recordVo.getDeptId());
		}
		if(StringUtils.isNotBlank(recordVo.getCampusId())){
			sql.append(" and cur.campus_id = :campusId ");
			params.put("campusId", recordVo.getCampusId());
		}
		if(StringUtils.isNotBlank(recordVo.getBranchId())){
			sql.append(" and cur.branch_id = :branchId ");
			params.put("branchId", recordVo.getBranchId());
		}
		if(StringUtils.isNotBlank(recordVo.getBeginTime())){
			sql.append(" and cur.create_time >= :beginTime ");
			params.put("beginTime", recordVo.getBeginTime()+" 00:00:00 ");
		}
		if(StringUtils.isNotBlank(recordVo.getEndTime())){
			sql.append(" and cur.create_time <= :endTime ");
			params.put("endTime", recordVo.getEndTime()+" 23:59:59 ");
		}
		

		
		
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());			
		} 
		List<Map<Object,Object>> list=changeUserRoleRecordDao.findMapOfPageBySql(sql.toString(), dataPackage,params);
		dataPackage.setRowCount(changeUserRoleRecordDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ",params));
		
		List<ChangeUserRoleRecordVo> result = new ArrayList<>();
		ChangeUserRoleRecordVo userRoleRecordVo = null;
		for(Map<Object,Object> teMap : list){
			Map<String,String> maps = (Map)teMap;
			userRoleRecordVo = new ChangeUserRoleRecordVo();
			userRoleRecordVo.setUserName(maps.get("user_name"));
			userRoleRecordVo.setDeptName(maps.get("dept_name"));
			userRoleRecordVo.setCampusName(maps.get("campus_name"));
			userRoleRecordVo.setBranchName(maps.get("branch_name"));
			userRoleRecordVo.setCreateTime(maps.get("create_time"));
			result.add(userRoleRecordVo);
		}
		dataPackage.setDatas(result);		
		return dataPackage;
	}

	@Override
	public List<Customer> getCustomersByUserId(String userId,String userRoleType) {
		//根据userId和用户类型userRoleSign获取该用户手里的客户Customer 状态是stay_follow following 
		String sql = null;
		List<Customer> list = null;
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		if(userRoleType!=null){
			//区分咨询师 网络主管网络专员 外呼主管 外呼专员  暂时不合并代码 这块需求经常变动  以免到时候要拆分
			if(userRoleType.equalsIgnoreCase(RoleSign.ZXS.getValue())){
				sql = "select * from customer where DELEVER_TARGET = :userId "
						+ "and (DEAL_STATUS ='"+CustomerDealStatus.STAY_FOLLOW.getValue()+"' or DEAL_STATUS='"+CustomerDealStatus.FOLLOWING.getValue()+"')";
				list = customerDao.findBySql(sql,params);
			}else if(userRoleType.equalsIgnoreCase(RoleSign.WLZG.getValue())||userRoleType.equalsIgnoreCase(RoleSign.WLZY.getValue())){
				sql = "select * from customer where (DELEVER_TARGET = :userId or BEFOR_DELIVER_TARGET = :userId ) "
						+ "and (DEAL_STATUS ='"+CustomerDealStatus.STAY_FOLLOW.getValue()+"' or DEAL_STATUS='"+CustomerDealStatus.FOLLOWING.getValue()+"')";
				list = customerDao.findBySql(sql,params);
			}else if(userRoleType.equalsIgnoreCase(RoleSign.WHZG.getValue())||userRoleType.equalsIgnoreCase(RoleSign.WHZY.getValue())){
				sql = "select * from customer where (DELEVER_TARGET = :userId or BEFOR_DELIVER_TARGET = :userId ) "
						+ "and (DEAL_STATUS ='"+CustomerDealStatus.STAY_FOLLOW.getValue()+"' or DEAL_STATUS='"+CustomerDealStatus.FOLLOWING.getValue()+"')";
				list = customerDao.findBySql(sql,params);
			}else{
				//不区分角色
				sql = "select * from customer where DELEVER_TARGET = :userId "
						+ "and (DEAL_STATUS ='"+CustomerDealStatus.STAY_FOLLOW.getValue()+"' or DEAL_STATUS='"+CustomerDealStatus.FOLLOWING.getValue()+"')";
				list = customerDao.findBySql(sql,params);	
			}
		}else{
			//不区分角色
			sql = "select * from customer where DELEVER_TARGET = :userId "
					+ "and (DEAL_STATUS ='"+CustomerDealStatus.STAY_FOLLOW.getValue()+"' or DEAL_STATUS='"+CustomerDealStatus.FOLLOWING.getValue()+"')";
			list = customerDao.findBySql(sql,params);
		}
		return list;
	}
	



	@Override
	public DataPackage getOperateManagerCustomers(DistributeCustomerVo vo, DataPackage dataPackage) {
		//当前登录者所在的分公司的所有的营运经理都能看到  	
		//查询网络分配营运经理的分配客户列表 
		
		
		Map<String, Object> params = Maps.newHashMap();
		
		String blongBranch =userService.getBelongBranch().getOrgLevel();
		
		//需求修改：后台不限制某种角色职位的人 而是通过组织架构进行关联 2017-02-28		
//		List<User> users = null;
//		String userIds = null;
//		users= userService.getUserByRoldCodesAndOrgLevel(RoleCode.OPERATION_MANAGER.getValue(),blongBranch);
//		if(users==null||users.size()==0){
//			users = userService.getUserByJobSignAndOrgLevel(RoleSign.YYJL.getValue().toLowerCase(), blongBranch);
//		}
//		if(users !=null && users.size()>0 ){
//			StringBuffer userId = new StringBuffer();
//			for(User user:users){
//				userId.append("'"+user.getUserId()+"',");
//			}
//			if(userId.length()>1){
//				userIds = userId.substring(0, userId.length()-1);
//			}
//		}	
		//整个查询条件
		StringBuilder cusSql= new StringBuilder(512);
		cusSql.append(" select cf.ID as customerFollowupId,c.id as cusId,c.name as cusName,c.DEAL_STATUS as dealStatus,c.CONTACT as contact,");
		cusSql.append(" cf.REMARK as remark,c.LAST_DELIVER_NAME as lastDeliverName,c.DELIVER_TIME as deliverTime,");
		cusSql.append(" c.RES_ENTRANCE as resEntrance,c.INTENTION_OF_THE_CUSTOMER as intentionOfTheCustomer, ");
		cusSql.append(" cf.APPOINTMENT_TYPE as appointmentType,cf.CREATE_USER_ID as createUserId,c.DELEVER_TARGET as deliverTarget ");
		
		cusSql.append(" ,c.INTENTION_CAMPUS_ID as intentionCampusId, d.`NAME` as resEntranceName ,dd.`NAME` as intentionName,o.`name` as campusName ");
		// 待分配的状态是New
		cusSql.append(" from customer_folowup cf left join customer c on  cf.CUSTOMER_ID = c.ID ");
		cusSql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID "); 
		cusSql.append(" LEFT JOIN data_dict dd on c.INTENTION_OF_THE_CUSTOMER = dd.ID "); 
		cusSql.append(" left join organization o on o.id = cf.APPOINT_CAMPUS ");
		cusSql.append(" where ( cf.APPOINTMENT_TYPE ='"+AppointmentType.APPOINTMENT_ONLINE.getValue()
		+"' or cf.APPOINTMENT_TYPE ='"+AppointmentType.APPOINTMENT_ONLINE_OPERATION.getValue()+"' ) ");
	
		params.put("agencyUserId", userService.getCurrentLoginUser().getUserId());
		params.put("appointBranch", blongBranch+"%");
		cusSql.append(" and (cf.AGENCY_USER_ID = :agencyUserId or cf.APPOINT_BRANCH like :appointBranch ) ");
		//需求修改：后台不限制某种角色职位的人 而是通过组织架构进行关联 2017-02-28		
//        if(userIds!=null){
//        	cusSql.append(" and cf.AGENCY_USER_ID in ("+userIds+")");
//        }else{
//        	cusSql.append(" and cf.AGENCY_USER_ID = '"+userService.getCurrentLoginUser().getUserId()+"'");
//        }
				
		// 查询条件
		// 客户姓名
		if (StringUtils.isNotBlank(vo.getCusName())) {
			cusSql.append(" and c.name like :cusName ");
			params.put("cusName", "%"+vo.getCusName()+"%");
		}
		// 联系方式
		if (StringUtil.isNotBlank(vo.getContact())) {
			cusSql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(vo.getResEntranceId())) {
			cusSql.append(" and c.RES_ENTRANCE= :resEntranceId ");
			params.put("resEntranceId", vo.getResEntranceId());
		}
		// 分配人(最新)
		if (StringUtils.isNotBlank(vo.getLastDeliverName())) {
			cusSql.append(" and c.LAST_DELIVER_NAME like :lastDeliverName ");
			params.put("lastDeliverName", "%"+vo.getLastDeliverName()+"%");
		}
		// 意向度
		if (StringUtils.isNotBlank(vo.getIntentionOfTheCustomerId())) {
			cusSql.append(" and c.INTENTION_OF_THE_CUSTOMER = :intentionOfTheCustomerId ");
			params.put("intentionOfTheCustomerId", vo.getIntentionOfTheCustomerId());
		}
		// 跟进备注
		if (StringUtils.isNotBlank(vo.getRemark())) {
			cusSql.append(" and cf.REMARK like :remark ");
			params.put("remark", "%"+vo.getRemark()+"%");
		}
		// 分配状态 不是new 也不是stay follow
		if (StringUtils.isNotBlank(vo.getIsDistribute())) {
			if (vo.getIsDistribute().equals("1")) {
				// 已经分配
				//cusSql.append(" and cf.DEAL_STATUS <> 'NEW' ");
				 cusSql.append(" and cf.DEAL_STATUS = 'STAY_FOLLOW' ");
			}
			if (vo.getIsDistribute().equals("0")) {
				// conSql.append(" and (c.DEAL_STATUS ='NEW' or c.DEAL_STATUS
				// ='STAY_FOLLOW' ) ");
				cusSql.append(" and (cf.DEAL_STATUS ='NEW' ) ");
			}
		}
//		//处理状态
//		if (StringUtil.isNotBlank(vo.getDealStatus())) {
//			cusSql.append(" and (c.DEAL_STATUS ='" + vo.getDealStatus() + "' ) ");
//		}
		// 分配开始时间
		if (StringUtils.isNotBlank(vo.getDeliverBeginDate())) {
			cusSql.append(" and c.DELIVER_TIME >= :deliverBeginDate ");
			params.put("deliverBeginDate", vo.getDeliverBeginDate() + " 00:00:00.000 ");
		}
		// 分配结束时间
		if (StringUtils.isNotBlank(vo.getDeliverEndDate())) {
			cusSql.append(" and c.DELIVER_TIME <= :deliverEndDate ");
			params.put("deliverEndDate", vo.getDeliverEndDate()+" 23:59:59.999 ");
		}
		//意向校区
		if(StringUtil.isNotBlank(vo.getIntentionCampusId())){
			cusSql.append(" and c.INTENTION_CAMPUS_ID = '"+vo.getIntentionCampusId()+"' ");
		}
	
//		if (StringUtils.isNotBlank(dataPackage.getSord())
//				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
//			cusSql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
//		}
		//写死根据最新分配是降序
		cusSql.append("order by c.DELIVER_TIME desc ");
				
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
	
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));	

		//将查询结果转换为vo		
		List<DistributeCustomerVo> voList = new ArrayList<DistributeCustomerVo>();
		DistributeCustomerVo customerVo = null;
		for (Map<Object, Object> map : list) {
			Map<String, String> maps =(Map)map;
		    customerVo = new DistributeCustomerVo();
			String cusId = maps.get("cusId");
			customerVo.setCustomerFollowupId(maps.get("customerFollowupId"));
			customerVo.setCusId(cusId != null ? cusId : "");
			customerVo.setCusName(maps.get("cusName") != null ? maps.get("cusName") : "");
			customerVo.setContact(maps.get("contact"));
 
			// 设置资源入口
			customerVo.setResEntranceId(maps.get("resEntrance"));
			customerVo.setResEntranceName(maps.get("resEntranceName"));
			// 设置最后分配人名称
			customerVo.setLastDeliverName(maps.get("lastDeliverName") );
			// 设置最后分配时间
			customerVo.setDeliverTime(maps.get("deliverTime"));
			// 设置意向度
			customerVo.setIntentionOfTheCustomerId(maps.get("intentionOfTheCustomer"));
			customerVo.setIntentionOfTheCustomerName(maps.get("intentionName"));
			// 设置备注
			customerVo.setRemark(maps.get("remark"));
			//设置意向校区
			if(maps.get("intentionCampusId")!=null){
				customerVo.setIntentionCampusName(organizationService.findById(maps.get("intentionCampusId")).getName());
			}
            
			//处理状态   跟实际客户的跟进状态无关    只是返回给前端看  新增 待跟进 跟进中 
			String appointmentType = maps.get("appointmentType");
			String createUserId = maps.get("createUserId");
			String deliverTarget = maps.get("deliverTarget");
			String dealStatus = maps.get("dealStatus");
			
			if(appointmentType.equals(AppointmentType.APPOINTMENT_ONLINE.getValue())){
				dealStatus = CustomerDealStatus.NEW.getValue();
			}else if(appointmentType.equals(AppointmentType.APPOINTMENT_ONLINE_OPERATION.getValue())){
				if(createUserId.equals(deliverTarget) && dealStatus.equals(CustomerDealStatus.FOLLOWING.getValue())){
					dealStatus = CustomerDealStatus.STAY_FOLLOW.getValue();
				}else if(!createUserId.equals(deliverTarget)&&dealStatus.equals(CustomerDealStatus.STAY_FOLLOW.getValue())){
					dealStatus = CustomerDealStatus.STAY_FOLLOW.getValue();
				}else{
					dealStatus = CustomerDealStatus.FOLLOWING.getValue();
				}
			}
			
			customerVo.setDealStatus(dealStatus);
			if(dealStatus!=null){
				customerVo.setDealStatusName(CustomerDealStatus.valueOf(dealStatus).getName());
			}else{
				customerVo.setDealStatusName("-");
			}
			//分配状态			
			if(AppointmentType.APPOINTMENT_ONLINE.getValue().equals(appointmentType)){
				customerVo.setIsDistribute("未分配");
				customerVo.setCampusName("-");
			}else{
				customerVo.setIsDistribute("已分配");
				customerVo.setCampusName(maps.get("campusName"));
			}
			//返回前端是否 可以更换校区
			
			//校区名字

			
			
			voList.add(customerVo);
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}

	@Override
	public DataPackage getAllCustomersForMarket(CustomerVo vo, DataPackage dataPackage) {
		//客户管理 --客户列表 市场专用
		long startTime=System.currentTimeMillis();   //获取开始时间
		StringBuilder cusSql=new StringBuilder(512);	
		cusSql.append(" select c.id as cusId,c.name as cusName,c.CONTACT as contact,c.RES_ENTRANCE as resEntrance,c.CREATE_TIME as createTime,");
		cusSql.append(" c.CUS_TYPE as cusType,c.CUS_ORG as cusOrg,c.CREATE_USER_ID as userId,c.BL_SCHOOL as blCampusId,c.LAST_FOLLOW_UP_TIME as lastFollowUpTime,");
		cusSql.append(" c.DELIVER_TYPE as deliverType,c.DELEVER_TARGET as deliverTarget, c.BL_BRANCH as branchId,c.DEAL_STATUS as dealStatus ");
		cusSql.append(" , d.`NAME` as resEntranceName ,dd.`NAME` as cusTypeName,ddd.`NAME` as cusOrgName, o.`name` as blCampusName,oo.`name` as branchName ");
		cusSql.append(" ,u.`NAME` as userName "); 
		cusSql.append(" ,og.`NAME` as deptName ");
		cusSql.append(" from customer c ");
		
		cusSql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID "); 
		cusSql.append(" LEFT JOIN data_dict dd on c.CUS_TYPE = dd.ID "); 
		cusSql.append(" LEFT JOIN data_dict ddd on c.CUS_ORG = ddd.ID ");

		cusSql.append(" LEFT JOIN organization o on c.BL_SCHOOL = o.id ");
		cusSql.append(" LEFT JOIN organization oo on o.parentID = oo.id ");
		cusSql.append(" LEFT JOIN `user` u on c.CREATE_USER_ID = u.USER_ID ");
		
		cusSql.append(" LEFT JOIN user_dept_job udj on ( c.CREATE_USER_ID = udj.USER_ID and udj.isMajorRole=0 ) ");
		cusSql.append(" LEFT JOIN organization og on og.id=udj.DEPT_ID ");
		
		//cusSql.append(" LEFT JOIN customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
		//cusSql.append(" LEFT JOIN student s on s.id=csr.STUDENT_ID where 1=1 ");
		cusSql.append(" where 1=1 ");
		//cusSql.append(" and c.id in ('CUS0000262965','CUS0000262964','CUS0000031282','CUS0000262885','CUS0000262887','CUS0000029895') ");
		//12个查询条件
		// 查询姓名
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(vo.getName())) {
			cusSql.append(" and c.NAME like :name ");
			params.put("name", "%"+vo.getName()+"%");
		}
		// 联系方式
		if (StringUtils.isNotBlank(vo.getContact())) {
			cusSql.append(" and c.CONTACT = :contact ");
			params.put("contact", vo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(vo.getResEntranceId())) {
			cusSql.append(" and c.RES_ENTRANCE= :resEntranceId ");
			params.put("resEntranceId", vo.getResEntranceId());
		}
		// 资源大类
		if (StringUtils.isNotBlank(vo.getCusOrg())) {
			if(vo.getCusOrg().equals("NULL")){
				cusSql.append(" and c.CUS_ORG is NULL ");
			}else{
				cusSql.append(" and c.CUS_ORG= :cusOrg ");
				params.put("cusOrg", vo.getCusOrg());
			}
			
		}
		// 资源细分
		if (StringUtils.isNotBlank(vo.getCusType())) {
			if(vo.getCusType().equals("NULL")){
				cusSql.append(" and c.CUS_TYPE is NULL ");
			}else{
				cusSql.append(" and c.CUS_TYPE = :cusType ");
				params.put("cusType", vo.getCusType());
			}
			
		}
		//录入者 
        if(StringUtil.isNotBlank(vo.getCreateUserName())){
        	cusSql.append(" and (c.CREATE_USER_ID in (select user_id from user where name like :createUserName ) )");
        	params.put("createUserName", "%"+vo.getCreateUserName()+"%");
        }
		//录入者部门
		if(StringUtil.isNotBlank(vo.getCreateUserDept())){
			cusSql.append(" and (c.CREATE_USER_ID in (select udj.USER_ID from user_dept_job udj LEFT JOIN organization o on o.id =udj.DEPT_ID where o.name LIKE :createUserDept ) )");
			params.put("createUserDept", "%"+vo.getCreateUserDept()+"%");
		}		
		//跟进人
		if(StringUtils.isNotBlank(vo.getDeliverTarget())){
			cusSql.append("and c.DELEVER_TARGET = :deliverTarget ");
			params.put("deliverTarget", vo.getDeliverTarget());
		}
		if(StringUtils.isNotBlank(vo.getDeliverTargetName())){
			//通过getDeliverTargetName 获取 DeliverTarget						
			cusSql.append(" and (c.DELEVER_TARGET in (select user_id from user where name like :deliverTargetName ) or c.DELEVER_TARGET in (select id from organization  where name like :deliverTargetName ) ) ");					
			params.put("deliverTargetName", "%"+vo.getDeliverTargetName()+"%");
		}		
		//选择分公司
        if(StringUtil.isNotBlank(vo.getBrenchId())){
        	cusSql.append(" and (c.BL_SCHOOL in( select id from organization where organization.parentID = :brenchId ) or c.BL_SCHOOL= :brenchId )");
        	params.put("brenchId", vo.getBrenchId());
        }
        
		//选择校区
		if(StringUtil.isNotBlank(vo.getBlCampusId())){
			cusSql.append(" and (c.BL_SCHOOL = :blCampusId or c.DELEVER_TARGET= :blCampusId ) ");
            params.put("blCampusId", vo.getBlCampusId());
		}
		//最新跟进时间
		if(StringUtils.isNotBlank(vo.getFollowupBeginDate())){		
			cusSql.append(" and c.LAST_FOLLOW_UP_TIME >= '"+vo.getFollowupBeginDate()+" 00:00:00.000' ");
		}			
		if(StringUtils.isNotBlank(vo.getFollowupEndDate())){
			cusSql.append(" and c.LAST_FOLLOW_UP_TIME <= '"+vo.getFollowupEndDate()+" 23:59:59.999' ");
		}
		//录入时间
		if(StringUtils.isNotBlank(vo.getCreateBeginDate())){		
			cusSql.append(" and c.CREATE_TIME >= '"+vo.getCreateBeginDate()+" 00:00:00.000' ");
		}			
		if(StringUtils.isNotBlank(vo.getCreateEndDate())){
			cusSql.append(" and c.CREATE_TIME <= '"+vo.getCreateEndDate()+" 23:59:59.999' ");
		}
		
		//第一份签订合同时间
		if(StringUtil.isNotBlank(vo.getFirstContractBeginTime())||StringUtil.isNotBlank(vo.getFirstContractEndTime())){
			cusSql.append(" and c.id in (select CUSTOMER_ID from contract where 1=1 ");
			if(StringUtil.isNotBlank(vo.getFirstContractBeginTime())){
				cusSql.append(" and CREATE_TIME >= :firstContractBeginTime ");
				params.put("firstContractBeginTime", vo.getFirstContractBeginTime()+" 00:00:00 ");
			}
			if(StringUtil.isNotBlank(vo.getFirstContractEndTime())){
				cusSql.append(" and CREATE_TIME <= :firstContractEndTime ");
				params.put("firstContractEndTime", vo.getFirstContractEndTime()+" 23:59:59 ");
			}
			cusSql.append(" ) ");
		}
		
		//如果这三个条件有一个不为空则关联学生表
		if(StringUtils.isNotBlank(vo.getPointialStuName())||StringUtils.isNotBlank(vo.getPointialStuSchoolId())){
			cusSql.append(" and c.id in ( select cs.CUSTOMER_ID from customer_student_relation cs left join ");
			cusSql.append(" student s on cs.STUDENT_ID = s.ID where 1=1 ");
			//学生姓名
			if(StringUtils.isNotBlank(vo.getPointialStuName())){
				cusSql.append(" and s.NAME like :pointialStuName ");
				params.put("pointialStuName", "%"+vo.getPointialStuName()+"%");
			}
			//选择就读学校  这里传进来的是学校的Id 不是名字
			if(StringUtils.isNotBlank(vo.getPointialStuSchoolId())){
				cusSql.append(" and s.SCHOOL = :pointialStuSchoolId ");
				params.put("pointialStuSchoolId", vo.getPointialStuSchoolId());
			}	
			cusSql.append(" ) ");
		}

		
		
		//客户类型  就是客户的处理状态 dealStatus 	
		if(vo.getDealStatus()!=null){
			cusSql.append(" and c.DEAL_STATUS = :dealStatus ");
			params.put("dealStatus", vo.getDealStatus().getValue());
		}
		
		//客户列表配置权限   xiaojinwang 20160928
		//cusSql.append(roleQLConfigService.getValueResult("客户列表","sql"));
		User user = userService.getCurrentLoginUser();	
		
		Organization organization = userService.getUserMainDeptByUserId(user.getUserId());
		
		String orgId=organization.getId();
        
		
		//市场专用
		String userIds = null;
		//集团市场总监
		Boolean flag_MARKETING_DIRECTOR = userService.isUserRoleCode(user.getUserId(), RoleCode.MARKETING_DIRECTOR);
		//分公司市场经理
		Boolean flag_BREND_MERKETING_DIRECTOR = userService.isUserRoleCode(user.getUserId(), RoleCode.BREND_MERKETING_DIRECTOR);
		//市场专员
		Boolean flag_MARKET_STAFF = userService.isUserRoleCode(user.getUserId(), RoleCode.MARKET_STAFF);
		//分公司市场主管
		Boolean flag_BREND_MERKETING_MENAGER = userService.isUserRoleCode(user.getUserId(), RoleCode.BREND_MERKETING_MENAGER);
		if(flag_BREND_MERKETING_DIRECTOR||flag_MARKETING_DIRECTOR||flag_BREND_MERKETING_MENAGER){
			//如果是分公司市场经理或者集团市场总监
			userIds = getUserIdsByOrgId(orgId);
		}else if (flag_MARKET_STAFF){
			//市场专员
			userIds = "'"+user.getUserId()+"'";
		}
		if(StringUtils.isNotBlank(userIds)){
			//cusSql.append(" and ( c.BEFOR_DELIVER_TARGET in ("+userIds+") or c.DELEVER_TARGET in ("+userIds+") or c.CREATE_USER_ID in ("+userIds+") )");
			cusSql.append(" and ( c.CREATE_USER_ID in ("+userIds+") )");
		}else{
			params.put("orgId", orgId);
			params.put("userId", user.getUserId());
			cusSql.append(" and ( c.BL_SCHOOL in ( select id FROM organization where parentID in (select id from organization where id= :orgId )) or c.BL_SCHOOL in (select id from organization where id= :orgId ) or c.CREATE_USER_ID = :userId ) ");
		}
		
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
			//params.put("orderBy", );
		} 
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
		//dataPackage.setDatas(list);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));	
        
		//将查询结果转换为vo		
		//vo原来是签单客户列表 后来改成客户列表  仍使用原来的签单vo
		List<SignupCustomerVo> voList =new ArrayList<SignupCustomerVo>();
		StringBuilder studentBuffer = new StringBuilder(" ");
		StringBuilder schoolBuffer = new StringBuilder(" ");
		SignupCustomerVo customerVo = null;
		for(Map<Object,Object> tmaps : list){
			Map<String,String> maps = (Map)tmaps;
			customerVo = new SignupCustomerVo();
			String cusId =maps.get("cusId");
			customerVo.setCusId(cusId!=null ? cusId:null);
			customerVo.setCusName(maps.get("cusName")!=null? maps.get("cusName"):null);
			//联系方式
			customerVo.setContact(maps.get("contact")!=null? maps.get("contact"):null);
			//登记人姓名以及登记人部门
			customerVo.setCreateCustomerUserId(maps.get("userId"));
			customerVo.setCreateUserName(maps.get("userName"));
			customerVo.setCreateUserDept(maps.get("deptName"));			
			customerVo.setResEntranceId(maps.get("resEntrance"));
			customerVo.setResEntranceName(maps.get("resEntranceName"));
			customerVo.setCusType(maps.get("cusType"));
			customerVo.setCusTypeName(maps.get("cusTypeName"));
			customerVo.setCusOrg(maps.get("cusOrg"));
			customerVo.setCusOrgName(maps.get("cusOrgName"));
			Object createTime = maps.get("createTime");
			customerVo.setCreateTime(createTime!=null?createTime.toString():"");
				
			//最新跟进时间
			customerVo.setLastFollowUpTime(maps.get("lastFollowUpTime"));
			//所属校区
			//所属分公司
			customerVo.setBranchName(maps.get("branchName"));			
			customerVo.setBlCampusName(maps.get("blCampusName"));
			//跟进人
			if(StringUtil.isNotBlank(maps.get("deliverTarget"))){
				customerVo.setDeliverTarget(maps.get("deliverTarget"));
				if (maps.get("deliverType") != null) {
					if (CustomerDeliverType.PERSONAL_POOL.getValue().equals(maps.get("deliverType"))) {
						User deliverTarget = userService.loadUserById(maps.get("deliverTarget"));
						if(deliverTarget!=null){														
							customerVo.setDeliverTargetName(deliverTarget.getName());
							Organization belongCampus = userService.getBelongCampusByUserId(maps.get("deliverTarget"));
							Organization belongBranch = userService.getBelongBranchByUserId(maps.get("deliverTarget"));
							if(belongCampus!=null && belongCampus.getOrgType()!= OrganizationType.CAMPUS){
								customerVo.setBlCampusName("");
							}
							if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
								customerVo.setBranchName(belongBranch.getName());
							}else{
								customerVo.setBranchName("");
							}
						}else{
							Organization deliverTargetOrg = organizationDao.findById(maps.get("deliverTarget"));
							if(deliverTargetOrg!=null){
								if(deliverTargetOrg.getOrgType()==OrganizationType.CAMPUS){
									Organization belongCampus =userService.getBelongCampusByOrgId(deliverTargetOrg.getId());
									if(belongCampus!=null){
										customerVo.setBlCampusName(belongCampus.getName());
									}else{
										customerVo.setBlCampusName("");
									}
								}else{
									customerVo.setBlCampusName("");
								}
								Organization belongBranch =userService.getBelongBranchByOrgId(deliverTargetOrg.getId());
								if(belongBranch!=null){
									customerVo.setBranchName(belongBranch.getName());
									if(deliverTargetOrg.getName().indexOf("网络")!=-1){
										customerVo.setBranchName("");
									}
								}else{
									customerVo.setBranchName("");
								}
								customerVo.setDeliverTargetName(deliverTargetOrg.getResourcePoolName());
							}
						}
					} else if (CustomerDeliverType.CUSTOMER_RESOURCE_POOL.getValue().equals(maps.get("deliverType"))) {
						Organization deliverTargetOrg = organizationDao.findById(maps.get("deliverTarget"));
						if(deliverTargetOrg!=null){
							if(deliverTargetOrg.getOrgType()==OrganizationType.CAMPUS){
								Organization belongCampus =userService.getBelongCampusByOrgId(deliverTargetOrg.getId());
								if(belongCampus!=null){
									customerVo.setBlCampusName(belongCampus.getName());
								}else{
									customerVo.setBlCampusName("");
								}
							}else{
								customerVo.setBlCampusName("");
							}
							
							Organization belongBranch =userService.getBelongBranchByOrgId(deliverTargetOrg.getId());
							if(belongBranch!=null){
								customerVo.setBranchName(belongBranch.getName());	
								if(deliverTargetOrg.getName().indexOf("网络")!=-1){
									customerVo.setBranchName("");
								}
							}else{
								customerVo.setBranchName("");
							}
							customerVo.setDeliverTargetName(deliverTargetOrg.getResourcePoolName());
						}else{
							User deliverTarget = userService.loadUserById(maps.get("deliverTarget"));
							if(deliverTarget!=null){
								customerVo.setDeliverTargetName(deliverTarget.getName());
								Organization belongCampus = userService.getBelongCampusByUserId(maps.get("deliverTarget"));
								Organization belongBranch = userService.getBelongBranchByUserId(maps.get("deliverTarget"));
								if(belongCampus!=null && belongCampus.getOrgType()!= OrganizationType.CAMPUS){
									customerVo.setBlCampusName("");
								}
								if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
									customerVo.setBranchName(belongBranch.getName());
								}else{
									customerVo.setBranchName("");
								}
							}
						}
					}
				}else{
					User deliverTarget =userService.loadUserById(maps.get("deliverTarget"));
					if(deliverTarget!=null){
						customerVo.setDeliverTargetName(deliverTarget.getName());
						Organization belongCampus = userService.getBelongCampusByUserId(maps.get("deliverTarget"));
						Organization belongBranch = userService.getBelongBranchByUserId(maps.get("deliverTarget"));
						if(belongCampus!=null && belongCampus.getOrgType()!= OrganizationType.CAMPUS){
							customerVo.setBlCampusName("");
						}
						if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
							customerVo.setBranchName(belongBranch.getName());
						}else{
							customerVo.setBranchName("");
						}
					}else{
						Organization deliverTargetOrg = organizationDao.findById(maps.get("deliverTarget"));
						if(deliverTargetOrg!=null){
							if(deliverTargetOrg.getOrgType()==OrganizationType.CAMPUS){
								Organization belongCampus =userService.getBelongCampusByOrgId(deliverTargetOrg.getId());
								if(belongCampus!=null){
									customerVo.setBlCampusName(belongCampus.getName());
								}else{
									customerVo.setBlCampusName("");
								}
							}else{
								customerVo.setBlCampusName("");
							}
							Organization belongBranch =userService.getBelongBranchByOrgId(deliverTargetOrg.getId());
							if(belongBranch!=null){
								customerVo.setBranchName(belongBranch.getName());
								if(deliverTargetOrg.getName().indexOf("网络")!=-1){
									customerVo.setBranchName("");
								}
							}else{
								customerVo.setBranchName("");
							}
							customerVo.setDeliverTargetName(deliverTargetOrg.getResourcePoolName());
						}
					}
				}
			}else{
				//转介绍 根据录入人来判断 INTRODUCE
				if(maps.get("cusType")!=null && "INTRODUCE".equals(maps.get("cusType"))){
					Organization belongCampus = userService.getBelongCampusByUserId(maps.get("userId"));
					Organization belongBranch = userService.getBelongBranchByUserId(maps.get("userId"));
					if(belongCampus!=null && belongCampus.getOrgType()== OrganizationType.CAMPUS){
						customerVo.setBlCampusName(belongCampus.getName());
					}else{
						customerVo.setBlCampusName("");
					}
					if(belongBranch!=null && belongBranch.getOrgType()== OrganizationType.BRENCH){
						customerVo.setBranchName(belongBranch.getName());
					}else{
						customerVo.setBranchName("");
					}
				}				
			}
			
			
			//客户跟进状态  客户类型
			String dealStatus = maps.get("dealStatus");
			if(dealStatus!=null){
				customerVo.setDealStatusName(CustomerDealStatus.valueOf(dealStatus).getName());
			}
			
			
			//最新跟进记录
			StringBuilder query =new StringBuilder(" select cf.remark as remark from customer_folowup cf where appointment_type ='"+AppointmentType.FOLLOW_UP.getValue()+"' ");
			query.append(" and cf.customer_id = :cusId ORDER BY cf.create_time desc LIMIT 1 ");
			Map<String, Object> param = Maps.newHashMap();
			param.put("cusId", cusId);
			List<Map<Object, Object>> customerFolowups=customerFolowupDao.findMapBySql(query.toString(),param);
			if (customerFolowups.size() > 0) {
				customerVo.setFollowupRemark(customerFolowups.get(0).get("remark")!=null?customerFolowups.get(0).get("remark").toString():"");
			}
			
			
			
			//学生姓名  多个 用逗号,分割开//就读学校 多个 用逗号,分割开 
			String sql ="SELECT s.`NAME` as studentName,ss.`NAME` as schoolName from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID LEFT JOIN student_school ss on s.SCHOOL = ss.ID"+ 
			        " where csr.CUSTOMER_ID = :cusId ";  
			List<Map<Object, Object>> resultList=customerDao.findMapBySql(sql,param);
			if(resultList!=null && resultList.size()>1){				
				for(Map<Object, Object> map:resultList){
					if(map.get("studentName")!=null){
						studentBuffer.append(map.get("studentName")+",");
					}
					if(map.get("schoolName")!=null){
						schoolBuffer.append(map.get("schoolName")+",");
					}
					
				}
		        String studentList = studentBuffer.toString();
		        String schoolList = schoolBuffer.toString();
		        if(StringUtils.isNotBlank(studentList)){
		        	customerVo.setStudentNameList(new String(studentList.substring(0, studentList.length()-1)));
		        }else{
		        	customerVo.setStudentNameList("");
		        }
		        if(StringUtils.isNotBlank(schoolList)){
		        	customerVo.setSchoolNameList(new String(schoolList.substring(0, schoolList.length()-1)));
		        }else{
		        	customerVo.setSchoolNameList("");
		        }
		        
		        //清空Buffer
		        studentBuffer.delete(0, studentBuffer.length());
		        schoolBuffer.delete(0, schoolBuffer.length());
			}else if(resultList.size()==1){
				Map<Object, Object> map=resultList.get(0);
				if(map.get("studentName")!=null){
					customerVo.setStudentNameList(map.get("studentName").toString());
				}else{
					customerVo.setStudentNameList("");
				}
				if(map.get("schoolName")!=null){
					customerVo.setSchoolNameList(map.get("schoolName").toString());
				}else{
					customerVo.setSchoolNameList("");
				}
			}
					
			//客户是否签单
			List<Contract> cLists = contractDao.findContractByCustomer(cusId, null, null);
			if(cLists!=null && cLists.size()>0){
				Contract contract = cLists.get(0);
				customerVo.setFirstContractTime(contract.getCreateTime());
			}else{
				customerVo.setFirstContractTime("");
			}
			int cList = contractDao.countContractByCustomer(cusId);
			if(cList>0){
				customerVo.setIsSignContract("true");
			}else{
				customerVo.setIsSignContract("false");
			}			
			voList.add(customerVo);
		}
		dataPackage.setDatas(voList);
		long endTime=System.currentTimeMillis(); //获取结束时间 
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms"); 
        return dataPackage;
	}


	/*********************************************************2017630优化*******************************************************/
	@Override
	public List<Map<Object,Object>> getAppAllContractCustomer(StudentVo studentVo,DataPackage dp) {
		User currentUser = userService.getCurrentLoginUser();
		StringBuilder sb= new StringBuilder(1024);
		sb.append(" select distinct c.id customerId, c.name customerName,c.contact,s.id studentId,s.name studentName,dd.NAME gradeName,u.name studyManagerName,s.`status`,ss.`name` schoolName from customer c ");
		sb.append(" left join customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
		sb.append(" left join student s on csr.student_id=s.ID ");
		sb.append(" left join user u on u.user_id=s.STUDY_MANEGER_ID ");
		sb.append(" left join data_dict dd on dd.id=s.GRADE_id ");
		sb.append(" left join contract con on con.student_id=s.ID ");
		sb.append(" left join student_school ss on ss.ID = s.SCHOOL ");
		sb.append(" where 1=1 and s.student_type='ENROLLED' ");

		Map<String, Object> params = Maps.newHashMap();

		sb.append("  and con.create_user_id= :createUserId ");
		params.put("createUserId", currentUser.getUserId());

		if(StringUtils.isNotBlank(studentVo.getName())){
			sb.append(" and s.name like :studentName ");
			params.put("studentName", "%"+studentVo.getName()+"%");
		}

		if(StringUtils.isNotBlank(studentVo.getLatestCustomerName())){
			sb.append(" and c.name like :latestCustomerName ");
			params.put("latestCustomerName", "%"+studentVo.getLatestCustomerName()+"%");
		}
		if(StringUtils.isNotBlank(studentVo.getContact())){
			sb.append(" and c.contact like :studentContact ");
			params.put("studentContact", "%"+studentVo.getContact()+"%");
		}

		if(StringUtils.isNotBlank(studentVo.getGradeId())){
			sb.append(" and s.Grade_id = :gradeId ");
			params.put("gradeId", studentVo.getGradeId());
		}

		if(StringUtils.isNotBlank(studentVo.getStudentStatus())){
			sb.append(" and s.STATUS = :studentStatus ");
			params.put("studentStatus", studentVo.getStudentStatus());
		}
		
		sb.append(" order by con.CREATE_TIME desc ");

		List<Map<Object,Object>> maplist=customerDao.findMapOfPageBySql(sb.toString(),dp, params);


		for (Map map : maplist) {
			if(map.get("status")!=null)
				map.put("statusName", StudentStatus.valueOf(map.get("status").toString()).getName());
			if(map.get("schoolName")==null)
				map.put("schoolName", "");
		}
		return maplist;
	}

	@Override
	public Response setDeleteStudent(String studentId) {
		//如果此学生有跟进记录或者已经签单则不能删除
		Response response = new Response();
		Map<String, Object> map = Maps.newHashMap();
		map.put("studentId", studentId);			
		String followupSql = "select * from customer_folowup where APPOINTMENT_TYPE ='FOLLOW_UP' and FOLLOW_STUDENT_ID = :studentId";
		List<CustomerFolowup> folowups = customerFolowupDao.findBySql(followupSql, map);
		if(folowups!=null && folowups.size()>0){
			response.setResultCode(-1);
			response.setResultMessage("此学生有被跟进过，删除学生失败");
			return response;			
		}
		String contractSql = "select * from contract where STUDENT_ID = :studentId ";
		List<Contract> contracts = contractDao.findBySql(contractSql, map);
		if(contracts!=null && contracts.size()>0){
			response.setResultCode(-1);
			response.setResultMessage("此学生已签过单，删除学生失败");
			return response;			
		}	
		//转介绍登记过的孩子不能删除
		String transferSql = "select * from transfer_introduce_customer tic where tic.student_id = :studentId ";
		List<TransferCustomerRecord> tRecords = transferCustomerDao.findBySql(transferSql, map);
		if(tRecords!=null && tRecords.size()>0){
			response.setResultCode(-1);
			response.setResultMessage("此学生已转介绍登记过，删除学生失败");
			return response;
		}
	
		String sql = "select * from customer_student_relation where STUDENT_ID = :studentId ";

		List<CustomerStudent> list = customerStudentDao.findBySql(sql, map);
		if(list==null || list.size()==0){
			response.setResultCode(-1);
			response.setResultMessage("数据出错，删除学生失败");
			return response;
		}else{
			User currentUser = userService.getCurrentLoginUser();
			CustomerStudent customerStudent = list.get(0);
			customerStudent.setIsDeleted(true);
			customerStudent.setModifyTime(DateTools.getCurrentDateTime());
			customerStudent.setModifyUser(currentUser);
			
			Customer customer = customerStudent.getCustomer();
			Student student = customerStudent.getStudent();
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.DELETESTUDENT);
			dynamicStatus.setDescription("删除学生："+student.getName());
			if(customer.getResEntrance()!=null){
				dynamicStatus.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
            customerEventService.addCustomerDynameicStatus(customer, dynamicStatus,currentUser);	
			
			return response;
		}		
	}

	@Override
	public Response checkCustomerStudent(CustomerVo customerVo) {
				
		//判断如果是老客户的时候 学生是否已经重名
		Response response = new Response();
		String contact = customerVo.getContact();
		if(StringUtil.isBlank(contact)){
			response.setResultCode(-1);
			response.setResultMessage("客户电话号码不能为空");
			return response;
		}
		Map<String, Object> param = Maps.newHashMap();
		param.put("contact", contact);
		//先判断是不是老客户
		List<Customer> list = customerDao.findBySql(" select * from customer where CONTACT =:contact  ", param);
		if(list!=null && list.size()>0){
			Customer customer = list.get(0);			
			//获取保存的学生
			Set<StudentImportVo> stuVos=customerVo.getStudentImportVos();
			if(stuVos==null ||stuVos.size()==0){
				return response;
			}
			StudentImportVo[] studentImportVos = new StudentImportVo[stuVos.size()];
			stuVos.toArray(studentImportVos);			
			Map<String, Object> params = Maps.newHashMap();
			params.put("customerId",customer.getId());
			params.put("stuName", studentImportVos[0].getName());
			String  sql =" select csr.* from customer_student_relation csr ,student s where csr.STUDENT_ID = s.ID and csr.CUSTOMER_ID = :customerId and s.`NAME` = :stuName and csr.IS_DELETED = 0 ";
			List<CustomerStudent> students = customerStudentDao.findBySql(sql, params);
			if(students!=null && students.size()>0){
				response.setResultCode(-1);
				response.setResultMessage("这个客户已添加此学生，不能添加重名学生");
				return response;	
			}else{
				return response;
			}

		}else{
			return response;
		}

	}

    @Override
    public void saveOrUpdatePhoneCusomer(PhoneCustomerVo cus) {
        log.info("saveOrUpdatePhoneCusomer PhoneCustomerVo" + cus);
        Customer customer = null;
        User currentUser = userService.getCurrentLoginUser();
        String current = DateTools.getCurrentDateTime();
        if (StringUtil.isNotBlank(cus.getCustomerId())) {
            customer = this.findById(cus.getCustomerId());
        } else {
            if (this.findCustomerByPhone(cus.getContact()) != null) {
                throw new ApplicationException("电话号码" + cus.getContact() + "已存在");
            }
            customer = new Customer();
            customer.setCreateUser(currentUser);
            customer.setCreateTime(current);
        }
        if (customer == null) {
            throw new ApplicationException("没有找到" + cus.getCustomerId() + "对应客户");
        }
        customer.setContact(cus.getContact());
        customer.setModifyUserId(currentUser.getUserId());
        customer.setModifyTime(current);
        if (cus.getPhoneStatus() == 0) {
            if (StringUtil.isBlank(cus.getName()) || StringUtil.isBlank(cus.getResEntrance()) || StringUtil.isBlank(cus.getDeliverTarget()) || StringUtil.isBlank(cus.getIntentionCampus())) {
                throw new ApplicationException("名称，分配客户，意向校区不可为空");
            }
            customer.setName(cus.getName());
            if (StringUtil.isNotBlank(cus.getResEntrance())) {
                DataDict entrance = new DataDict(cus.getResEntrance());
                customer.setResEntrance(entrance);
                customer.setPreEntrance(entrance);
            }
            if (StringUtil.isNotBlank(cus.getCusType())) {
                customer.setCusType(new DataDict(cus.getCusType()));
            }
            if (StringUtil.isNotBlank(cus.getCusOrg())) {
                customer.setCusOrg(new DataDict(cus.getCusOrg()));
            }
            customer.setDeliverTarget(cus.getDeliverTarget());
            if (StringUtil.isNotBlank(cus.getIntentionCampus())) {
                customer.setIntentionCampus(new Organization(cus.getIntentionCampus()));
            }
            customer.setDeliverTime(current);
            customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
            customer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
            customer.setLastDeliverName(currentUser.getName());
            customer.setLastFollowUpTime(current);
            customer.setLastFollowUpUser(currentUser);

    		if(customer.getCustomerActive() == CustomerActive.INACTIVE) {
    			customer.setCustomerActive(CustomerActive.NEW_CUSTOMER);
    			//防止无效客户重新激活后，当晚被定时任务重新设置为无效,修正电话录入资源入口的问题 duanmenrun  20171109
				CustomerFolowup followupRecord = new CustomerFolowup();
				followupRecord.setCustomer(customer);
				followupRecord.setRemark("无效客户重新激活，添加系统跟进记录");
				followupRecord.setCreateTime(DateTools.getCurrentDateTime());
				followupRecord.setCreateUser(currentUser);
				followupRecord.setAppointmentType(AppointmentType.APPOINTMENT_REACTIVATE);
				customerFolowupDao.save(followupRecord);
    		}	
            
            //归属校区的更新      所属分公司bl_branch字段废弃了
            if(CustomerDeliverType.PERSONAL_POOL == customer.getDeliverType()
                    && StringUtils.isNotBlank(customer.getDeliverTarget())){
                String organizationId=userService.loadUserById(customer.getDeliverTarget()).getOrganizationId();
                Organization organization = userService.getBelongCampusByOrgId(organizationId);
                if(organization!=null){
                    customer.setBlCampusId(organization);
                    customer.setBlSchool(organization.getId());
                }
            }else{
                Organization organization = userService.getBelongCampus();
                if(organization!=null){
                    customer.setBlCampusId(organization);
                    customer.setBlSchool(organization.getId());
                }
            }
            customer.setRemark(cus.getRemark());
            customerDao.save(customer);
            DisabledCustomer disabledCus = disabledCustomerService.findDisabledCustomerByContact(cus.getContact(), 0);
            if (disabledCus != null) {
                disabledCus.setUdpateEnabled(1);
                disabledCustomerService.saveOrUpdateDisabledCustomer(disabledCus);
            }
        } else {
            if (StringUtil.isBlank(cus.getRemark())) {
                throw new ApplicationException("无效客户，需备注信息");
            }
            DisabledCustomer disabledCus = disabledCustomerService.findDisabledCustomerByContact(cus.getContact(), 1);
            if (disabledCus == null) {
                disabledCus = new DisabledCustomer();
                disabledCus.setContact(cus.getContact());
            }
            disabledCus.setRemark(cus.getRemark());
            disabledCus.setUdpateEnabled(0);
            disabledCustomerService.saveOrUpdateDisabledCustomer(disabledCus);
        }
    }


	/*********************************************************2017630优化*******************************************************/
	
	@Override
	public DataPackage getUserStayCustomerListByStatus(DataPackage dataPackage, String status) {
		// TODO Auto-generated method stub
		User currentUser = userService.getCurrentLoginUser();
		String userId = currentUser.getUserId();
		
		Organization organization = userService.getUserMainDeptByUserId(userId);
		String orgId=organization.getId();
		
		StringBuilder cusSql=new StringBuilder(1024);
		cusSql.append(" SELECT C.ID,C.NAME,(CASE ca.status WHEN 0 THEN ca.allocate_time WHEN 1 THEN '' ELSE '' END) AS deliverTime, c.RES_ENTRANCE resEntranceId ");
		cusSql.append(" FROM CUSTOMER C ");
		cusSql.append(" left join customer_allocate_obtain ca on c.id = ca.customer_id and ca.status='0' ");
		
		Map<String, Object> params = Maps.newHashMap();
        params.put("USERID", currentUser.getUserId());
        
		
		cusSql.append(" WHERE 1=1 ");	
		
		
		Organization blCampus = userService.getBelongCampusByOrgId(orgId);
		String orgLevel = blCampus.getOrgLevel();
		params.put("ORGLEVEL",  orgLevel+"%");
		cusSql.append(" AND C.DELEVER_TARGET = :USERID ");
		cusSql.append(" AND C.DEAL_STATUS = 'STAY_FOLLOW' ");
		cusSql.append(" AND (C.BL_SCHOOL IN (SELECT ID FROM ORGANIZATION WHERE ORGLEVEL LIKE :ORGLEVEL ))");
		
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" ORDER BY "+dataPackage.getSidx()+" "+dataPackage.getSord());
		}else {
			dataPackage.setSidx("DELIVER_TIME");
			dataPackage.setSord("DESC");
			cusSql.append(" ORDER BY  C.DELIVER_TIME DESC ");
		}
		
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));	
		
		//将查询结果转换为vo		
		List<AppCustomerVo> voList =new ArrayList<AppCustomerVo>();
		AppCustomerVo fVo = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate = new Date();
		String waitingTime="";
		for(Map<Object,Object> tmaps : list){
			Map<String,String> maps = (Map)tmaps;
		    fVo = new AppCustomerVo();
			String id =maps.get("ID");
			fVo.setId(id!=null ? id:"");
			fVo.setName(maps.get("NAME")!=null? maps.get("NAME"):"");
			//分配时间
			fVo.setDeliverTime(maps.get("deliverTime")!=null? maps.get("deliverTime"):"");
			
			String resEntrance = maps.get("resEntranceId");
			fVo.setResEntranceId(resEntrance!=null?resEntrance:"");
			//等待时间
			if(StringUtils.isNotBlank(resEntrance)&&"ON_LINE".equals(resEntrance)) {
				if(StringUtils.isNotBlank(fVo.getDeliverTime())) {
					try {
						Date deliverDate = sdf.parse(fVo.getDeliverTime());
						long diffSecond = (nowDate.getTime() - deliverDate.getTime())/1000/60;
						if(diffSecond > 0) {
							waitingTime = (diffSecond/60/24 > 0?diffSecond/60/24+"天" : "") +
									 (diffSecond/60%24 > 0?diffSecond/60%24+"小时" : "") +
									 (diffSecond%60 > 0?diffSecond%60+"分钟" : "");
							fVo.setWaitingTime(waitingTime);
						}else {
							fVo.setWaitingTime("");
						}
						
					} catch (ParseException e) {
						log.error("计算待获取客户等待时间异常！");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
	        voList.add(fVo);
		}
		
		dataPackage.setDatas(voList);
			
		return dataPackage;
	}

	@Override
	public DataPackage getUserFollowCustomerList(DataPackage dataPackage, String status) {
		// TODO Auto-generated method stub
		User currentUser = userService.getCurrentLoginUser();
		String userId = currentUser.getUserId();
		
		Organization organization = userService.getUserMainDeptByUserId(userId);
		String orgId=organization.getId();
		
		StringBuilder cusSql=new StringBuilder(1024);
		
		Organization blCampus = userService.getBelongCampusByOrgId(orgId);
		String orgLevel = blCampus.getOrgLevel();
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", currentUser.getUserId());
		params.put("ORGLEVEL",  orgLevel+"%");
		
		if(CustomerDealStatus.SIGNEUP.getValue().equalsIgnoreCase(status)) {
			cusSql.append(" SELECT distinct C.ID,C.NAME,'SIGNEUP' AS dealStatus ");
			cusSql.append(" FROM CUSTOMER C ");
			cusSql.append(" left join customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
			cusSql.append(" left join student s on csr.student_id=s.ID ");
			cusSql.append(" left join (select cono.STUDENT_ID,cono.CREATE_TIME,cono.CREATE_USER_ID from contract cono where cono.create_user_id= :userId group by cono.STUDENT_ID )con on con.STUDENT_ID=s.ID ");
			cusSql.append(" WHERE 1=1 ");	
			cusSql.append(" and s.student_type='ENROLLED' ");
			cusSql.append(" and con.create_user_id= :userId ");
			cusSql.append(" AND (C.BL_SCHOOL IN (SELECT ID FROM ORGANIZATION WHERE ORGLEVEL LIKE :ORGLEVEL )) ");
			cusSql.append(" ORDER BY  con.CREATE_TIME DESC ");
			dataPackage.setSidx("contract.CREATE_TIME");
			dataPackage.setSord("DESC");
		}else if(CustomerDealStatus.FOLLOWING.getValue().equalsIgnoreCase(status)) {
			cusSql.append(" SELECT C.ID,C.NAME,'FOLLOWING' AS dealStatus,C.RES_ENTRANCE as resEntranceId ");
			cusSql.append(" FROM CUSTOMER C ");
			cusSql.append(" WHERE 1=1 ");	
			cusSql.append(" and c.DELEVER_TARGET = :userId ");
			cusSql.append(" AND (C.BL_SCHOOL IN (SELECT ID FROM ORGANIZATION WHERE ORGLEVEL LIKE :ORGLEVEL )) ");
			cusSql.append(" and c.DEAL_STATUS != '"+CustomerDealStatus.INVALID.getValue()+"'");
			cusSql.append(" and (c.DEAL_STATUS = 'FOLLOWING' or c.DEAL_STATUS = 'NEW') ");
			//未签单
			//cusSql.append(" AND not exists ( select 1 from contract con ");
			//cusSql.append(" left join student s on con.STUDENT_ID=s.ID ");
			//cusSql.append(" where con.CUSTOMER_ID = c.id and s.student_type='ENROLLED'  and con.create_user_id= c.DELEVER_TARGET  ) ");
			dataPackage.setSidx("CUSTOMER.DELIVER_TIME");
			dataPackage.setSord("DESC");
			cusSql.append(" ORDER BY  C.DELIVER_TIME DESC ");
		}else {
			return dataPackage;
		}
		
		
		/*//跟进中,已签单
		StringBuilder cusSql=new StringBuilder(1024);
		cusSql.append(" SELECT C.ID,C.NAME,C.DELIVER_TIME AS deliverTime,C.DEAL_STATUS AS dealStatus ");
		cusSql.append(" FROM CUSTOMER C ");
		cusSql.append(" left join customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
		cusSql.append(" left join student s on csr.student_id=s.ID ");
		cusSql.append(" left join (select cono.STUDENT_ID,cono.CREATE_TIME,cono.CREATE_USER_ID from contract cono where cono.create_user_id= :userId group by cono.STUDENT_ID )con on con.STUDENT_ID=s.ID ");
		
		Map<String, Object> params = Maps.newHashMap();
        params.put("userId", currentUser.getUserId());
        
		
		cusSql.append(" WHERE 1=1 ");	
		
		
		Organization blCampus = userService.getBelongCampusByOrgId(orgId);
		String orgLevel = blCampus.getOrgLevel();
		if(CustomerDealStatus.SIGNEUP.getValue().equalsIgnoreCase(status)) {
			cusSql.append(" and s.student_type='ENROLLED' ");
			cusSql.append(" and con.create_user_id= :userId ");
		}else if(CustomerDealStatus.FOLLOWING.getValue().equalsIgnoreCase(status)) {
			cusSql.append(" and c.DELEVER_TARGET = :userId ");
			cusSql.append(" and (c.DEAL_STATUS = 'FOLLOWING' or c.DEAL_STATUS = 'NEW') ");
		}
		params.put("ORGLEVEL",  orgLevel+"%");
		cusSql.append(" AND (C.BL_SCHOOL IN (SELECT ID FROM ORGANIZATION WHERE ORGLEVEL LIKE :ORGLEVEL ))");
		
		//分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			cusSql.append(" ORDER BY "+dataPackage.getSidx()+" "+dataPackage.getSord());
		}else {
			dataPackage.setSidx("DELIVER_TIME");
			dataPackage.setSord("DESC");
			cusSql.append(" ORDER BY  C.DELIVER_TIME DESC ");
		}*/
		
		List<Map<Object,Object>> list=customerDao.findMapOfPageBySql(cusSql.toString(), dataPackage,params);
		dataPackage.setRowCount(customerDao.findCountSql("select count(*) from ( " + cusSql.toString() + " ) countall ",params));	
		
		//将查询结果转换为vo		
		List<AppCustomerVo> voList =new ArrayList<AppCustomerVo>();
		AppCustomerVo fVo = null;
		for(Map<Object,Object> tmaps : list){
			Map<String,String> maps = (Map)tmaps;
		    fVo = new AppCustomerVo();
			String id =maps.get("ID");
			fVo.setId(id!=null ? id:"");
			fVo.setName(maps.get("NAME")!=null? maps.get("NAME"):"");
			//分配时间
			fVo.setDeliverTime(maps.get("deliverTime")!=null? maps.get("deliverTime"):"");
			fVo.setDealStatus(maps.get("dealStatus")!=null? maps.get("dealStatus"):"");
			fVo.setResEntranceId(maps.get("resEntranceId")!=null? maps.get("resEntranceId"):"");
	        voList.add(fVo);
		}
		
		dataPackage.setDatas(voList);

		return dataPackage;
	}

	@Override
	public Response updateCustomerDeleverTarget(CustomerVo customerVo) {
		// TODO Auto-generated method stub
		Response response = new Response();
		User currentUser = userService.getCurrentLoginUser();
		if(currentUser==null) throw new ApplicationException("系统出错");
		String currentUserId = currentUser.getUserId();
		String newDeleverTarget = customerVo.getDeliverTarget();
		Customer distributeCustomer = this.findById(customerVo.getId());
		if(StringUtils.isBlank(newDeleverTarget))throw new ApplicationException("分配咨询师不存在！");
		if(distributeCustomer==null)throw new ApplicationException("待分配客户不存在！");
		//营主未分配给自己
		if(!newDeleverTarget.equals(distributeCustomer.getDeliverTarget())) {
			Response res = resourcePoolService.getResourcePoolVolume(1, newDeleverTarget);
			if(res.getResultCode() != 0) {
				return res;
			}
			
			Organization belongCampus = userService.getBelongCampusByUserId(newDeleverTarget);
			if(belongCampus!=null){
				distributeCustomer.setBlCampusId(belongCampus);
				distributeCustomer.setBlSchool(belongCampus.getId());				
			}
			String currentTime=DateTools.getCurrentDateTime();
			distributeCustomer.setModifyTime(currentTime);
			distributeCustomer.setModifyUserId(currentUserId);
			distributeCustomer.setDeliverTarget(newDeleverTarget);
			distributeCustomer.setDeliverTime(currentTime);
			//现在的资源入口
			customerDao.save(distributeCustomer);  		
			
			DeliverTargetChangeRecord record = new DeliverTargetChangeRecord();
			record.setCustomerId(distributeCustomer.getId());
			record.setPreviousTarget(currentUserId);
			record.setCurrentTarget(newDeleverTarget);
			record.setRemark("app-营主分配客户给咨询师");
			record.setCreateUserId(currentUserId);
			record.setCreateTime(currentTime);
			String recordId = deliverTargetChangeService.saveDeliverTargetChangeRecord(record);
			
			
			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.DELIVER);
			dynamicStatus.setDescription("app-营主分配客户给咨询师");
			if(distributeCustomer.getResEntrance()!=null){
				dynamicStatus.setResEntrance(distributeCustomer.getResEntrance());
			}else if(distributeCustomer.getPreEntrance()!=null){
				dynamicStatus.setResEntrance(distributeCustomer.getPreEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setTableName("delivertarget_change_record");
			dynamicStatus.setTableId(recordId);
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
			dynamicStatus.setDeliverType(distributeCustomer.getDeliverType());
            customerEventService.addCustomerDynameicStatus(distributeCustomer, dynamicStatus, userService.getUserById(newDeleverTarget));
			
            MobileUser mobileUser=mobileUserDao.findMobileUserByUserId(newDeleverTarget);
            if(mobileUser!=null){
            	SentRecord sentRecord = new SentRecord();
            	sentRecord.setMsgNo(MsgNo.M15);
            	sentRecord.setMsgName("新的客户资源");
            	sentRecord.setMsgType(new DataDict("NEW_CUSTOMER_RESOURCE"));
            	sentRecord.setMsgContent("你有一位新客户马上获取");
            	sentRecord.setMsgRecipient(new User(newDeleverTarget));
            	sentRecord.setCreateUserId(currentUserId);
            	sentRecord.setCreateTime(DateTools.getCurrentDateTime());
            	sentRecord.setModifyUserId(currentUserId);
            	sentRecord.setModifyTime(DateTools.getCurrentDateTime());
            	sentRecord.setPushMsgType(PushMsgType.SYSTEM_NOTICE);
            	sentRecord.setSysMsgType(SysMsgType.NEW_CUSTOMER_RESOURCE);
            	sentRecord.setSentTime(DateTools.getCurrentDateTime());
            	
            	//this.sendCostomerDistributionSysMsg(sentRecord, mobileUser);
            	sentRecord.setIsReading("1");
        		sentRecord.setSendType("SYS_MSG");
        		sentRecordService.saveOrUpdateSentRecord(sentRecord);
        		String msgTypeName = sentRecord.getMsgType() != null ? sentRecord.getMsgType().getName() : "";
        		mobilePushMsgService.pushMsg(mobileUser, sentRecord.getMsgContent(), sentRecord.getId(), 
        				sentRecord.getPushMsgType().getValue(), DateTools.getCurrentDateTime(), sentRecord.getSysMsgType(), 
        				sentRecord.getDetailId(), msgTypeName);
            	
            	//mobilePushMsgService.pushMsg(mobileUser, "营主-"+currentUser.getName()+"已经分配1位新客户给你！", "", "","", null, "", "新的客户资源");
			}
            
			if(distributeCustomer.getId()!=null){
				response.setResultCode(0);
			}else{
				response.setResultCode(-1);
			}	
		}
		
		return response;
	}

	@Override
	public void judgeUserSendMsg(String userId, String currentUserId) {
		// TODO Auto-generated method stub
		Response response = commonService.judgeUserIdentity(userId);
		
		int code = -1;
		if(response.getData()!=null) {
		   Map<String, Object> result = (Map<String, Object>) response.getData();
		   code = (int) result.get("code");
		}
		
		String msgContent = "";
		if(code == 0 || code == 2) {
			   msgContent = "你有一位新客户待分配";
		}else if(code == 1 ) {
			   msgContent = "你有一位新客户马上获取";
		}
		
		MobileUser mobileUser=mobileUserDao.findMobileUserByUserId(userId);
        if(mobileUser!=null && StringUtils.isNotBlank(msgContent)){
        	SentRecord sentRecord = new SentRecord();
        	sentRecord.setMsgNo(MsgNo.M15);
        	sentRecord.setMsgName("新的客户资源");
        	sentRecord.setMsgType(new DataDict("NEW_CUSTOMER_RESOURCE"));
        	sentRecord.setMsgContent(msgContent);
        	sentRecord.setMsgRecipient(new User(userId));
        	sentRecord.setCreateUserId(currentUserId);
        	sentRecord.setCreateTime(DateTools.getCurrentDateTime());
        	sentRecord.setModifyUserId(currentUserId);
        	sentRecord.setModifyTime(DateTools.getCurrentDateTime());
        	sentRecord.setPushMsgType(PushMsgType.SYSTEM_NOTICE);
        	sentRecord.setSysMsgType(SysMsgType.NEW_CUSTOMER_RESOURCE);
        	sentRecord.setSentTime(DateTools.getCurrentDateTime());
        	
        	//this.sendCostomerDistributionSysMsg(sentRecord, mobileUser);
        	sentRecord.setIsReading("1");
    		sentRecord.setSendType("SYS_MSG");
    		sentRecordService.saveOrUpdateSentRecord(sentRecord);
    		String msgTypeName = sentRecord.getMsgType() != null ? sentRecord.getMsgType().getName() : "";
    		mobilePushMsgService.pushMsg(mobileUser, sentRecord.getMsgContent(), sentRecord.getId(), 
    				sentRecord.getPushMsgType().getValue(), DateTools.getCurrentDateTime(), sentRecord.getSysMsgType(), 
    				sentRecord.getDetailId(), msgTypeName);
        	//mobilePushMsgService.pushMsg(mobileUser, "有1个新客户资源待分配！", "", "","", null, "", "新的客户资源");
		}
	}

	@Override
	public List<UserDetailForMobileVo> getAllDistributableZXS() {
		// TODO Auto-generated method stub
		Long time1 = System.currentTimeMillis();
		User currentUser = userService.getCurrentLoginUser();
		if(currentUser==null) throw new ApplicationException("系统出错");
		List<Organization> organizations = userService.getDeptExistsDistributableZXS(currentUser.getUserId());
		//List<Organization> organizations = userService.getDeptExistsDistributableZXS("USE0000018100");
		List<UserDetailForMobileVo> vos = new LinkedList<UserDetailForMobileVo>();
		if(organizations!=null&&organizations.size()>0) {
			for(Organization  obj : organizations) {
				List<Map<Object, Object>> users = userService.getUserOrganizationByJobs(obj.getOrgLevel(),new String[] {RoleSign.ZXS.getValue().toLowerCase(),RoleSign.ZXZG.getValue().toLowerCase()},null);
				if(OrganizationType.BRENCH == obj.getOrgType()) {
					if(users!=null&&users.size()>0) {
						for(Map<Object, Object> u : users) {
							UserDetailForMobileVo vo = new UserDetailForMobileVo();
							vo.setBrench(obj.getName());
							vo.setBrenchId(obj.getId());
							vo.setName(u.get("name")!=null?u.get("name").toString():"");
							vo.setUserId(u.get("userId")!=null?u.get("userId").toString():"");
							vo.setOrganizationId(u.get("organizationId")!=null?u.get("organizationId").toString():"");
							vo.setOrganizationName(u.get("organizationName")!=null?u.get("organizationName").toString():"");
							vo.setOrgType(u.get("orgType")!=null?u.get("orgType").toString():"");
							if(OrganizationType.CAMPUS.getValue().equals(u.get("orgType")!=null?u.get("orgType").toString():"")) {
								vo.setCampus(vo.getOrganizationName());
								vo.setCampusId(vo.getOrganizationId());
							}else {
								Organization o = userService.getBelongCampusByUserId(vo.getUserId());
								if(o!=null) {
									vo.setCampus(o.getName());
									vo.setCampusId(o.getId());
								}
							}
							vos.add(vo);
						}
					}
				
				}else if(OrganizationType.CAMPUS == obj.getOrgType()) {
					Organization o = organizationService.findById(obj.getParentId());
					if(users!=null&&users.size()>0) {
						for(Map<Object, Object> u : users) {
							UserDetailForMobileVo vo = new UserDetailForMobileVo();
							vo.setName(u.get("name")!=null?u.get("name").toString():"");
							vo.setUserId(u.get("userId")!=null?u.get("userId").toString():"");
							vo.setOrganizationId(u.get("organizationId")!=null?u.get("organizationId").toString():"");
							vo.setOrganizationName(u.get("organizationName")!=null?u.get("organizationName").toString():"");
							vo.setOrgType(u.get("orgType")!=null?u.get("orgType").toString():"");
							vo.setCampus(obj.getName());
							vo.setCampusId(obj.getId());
							if(o!=null) {
								vo.setBrench(o.getName());
								vo.setBrenchId(o.getId());
							}
							vos.add(vo);
						}
					}
				}else {
					if(users!=null&&users.size()>0) {
						for(Map<Object, Object> u : users) {
							UserDetailForMobileVo vo = new UserDetailForMobileVo();
							vo.setName(u.get("name")!=null?u.get("name").toString():"");
							vo.setUserId(u.get("userId")!=null?u.get("userId").toString():"");
							vo.setOrganizationId(u.get("organizationId")!=null?u.get("organizationId").toString():"");
							vo.setOrganizationName(u.get("organizationName")!=null?u.get("organizationName").toString():"");
							vo.setOrgType(u.get("orgType")!=null?u.get("orgType").toString():"");
							Organization campus =  userService.getBelongCampusByUserId(vo.getUserId());
							if(campus!=null) {
								vo.setCampus(campus.getName());
								vo.setCampusId(campus.getId());
							}
							Organization brench =  organizationService.findById(campus.getParentId());
							if(brench!=null) {
								vo.setBrench(brench.getName());
								vo.setBrenchId(brench.getId());
							}
							vos.add(vo);
						}
					}
				}
				
			}
		}
		
		vos = removeDuplicateUserDetailForMobileVo(vos);
		Long time2 = System.currentTimeMillis();
		System.out.println("time1-time2: "+(time2-time1));
		return vos;
	}

	private List<UserDetailForMobileVo> removeDuplicateUserDetailForMobileVo(List<UserDetailForMobileVo> list) {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getUserId().equals(list.get(i).getUserId())) {
					list.remove(j);
				}
			}
		}
		return list;
	}

	@Override
	public Response getDistributableZXSByParentId(String organizationId) {
		// TODO Auto-generated method stub
		Response response = new Response();
		User currentUser = userService.getCurrentLoginUser();
		if(currentUser==null) {
			response.setResultCode(400);
			response.setResultMessage("用户未登录，请重新登陆");
			return response;
		}
		if(StringUtils.isBlank(organizationId)) {
			response.setResultCode(400);
			response.setResultMessage("组织id不能为空");
			return response;
		}
		Organization organization = organizationService.findById(organizationId);
		if(organization==null) {
			response.setResultCode(400);
			response.setResultMessage("组织架构不存在");
			return response;
		}
		String[] jobs = new String[] {RoleSign.ZXS.getValue().toLowerCase(),RoleSign.ZXZG.getValue().toLowerCase()};
		//登录用户可分配的组织架构
		List<Organization> organizations = userService.getDeptExistsDistributableZXS(currentUser.getUserId());
		//List<Organization> organizations = userService.getDeptExistsDistributableZXS("USE0000018100");
		
		//返回的组织和用户
		List<OrganizationMobileSimpleVo> orgVos = new ArrayList<>();
		List<UserSimpleMobileVo> userVos = new ArrayList<>();
		//分公司查询，学生查直属于分公司，校区查可分配的
		if(organization.getOrgType() == OrganizationType.BRENCH) {
			Map<String, Object> map = new HashMap<>();
			map.put("branchId", organizationId);
			StringBuffer campusSql = new StringBuffer();
			campusSql.append(" select * from organization where parentID=:branchId and orgType='CAMPUS' ");
			List<Organization> campusList = organizationDao.findBySql(campusSql.toString(), map);
			for(Organization org : campusList) {
				for(Organization allowOrg : organizations) {
					if(allowOrg.getOrgLevel().startsWith(org.getOrgLevel())||org.getOrgLevel().startsWith(allowOrg.getOrgLevel())) {
						OrganizationMobileSimpleVo orgvo = new OrganizationMobileSimpleVo();
						orgvo.setName(org.getName());
						orgvo.setOrgId(org.getId());
						orgvo.setOrgT("CAMPUS");
						orgVos.add(orgvo);
					}
				}
			}
			List<Map<Object, Object>> users = userService.getUserByJobsOrgLevel(organization.getOrgLevel(),jobs);
			for(Map<Object, Object> u : users) {
				UserSimpleMobileVo vo = new UserSimpleMobileVo();
				vo.setUserId(u.get("userId")!=null?u.get("userId").toString():"");
				vo.setAccount(u.get("account")!=null?u.get("account").toString():"");
				vo.setName(u.get("name")!=null?u.get("name").toString():"");
				userVos.add(vo);
			}
		//校区，
		}else {
			List<String> orgLevels = new ArrayList<>();
			boolean flag = false;
			for(Organization org : organizations) {
				//可分配咨询师组织包括则获取全部部门user，不包括则获取其下子部门的user
				if(organization.getOrgLevel().startsWith(org.getOrgLevel())) {
					flag = true;
					break;
				}else {
					if(org.getOrgLevel().startsWith(organization.getOrgLevel())) {
						orgLevels.add(org.getOrgLevel());
					}
				}
			}
			//查全部校区
			if(flag) {
				List<Map<Object, Object>> users = userService.getUserOrganizationByJobs(organization.getOrgLevel(),jobs,null);
				for(Map<Object, Object> u : users) {
					UserSimpleMobileVo vo = new UserSimpleMobileVo();
					vo.setUserId(u.get("userId")!=null?u.get("userId").toString():"");
					vo.setAccount(u.get("account")!=null?u.get("account").toString():"");
					vo.setName(u.get("name")!=null?u.get("name").toString():"");
					userVos.add(vo);
				}
			//查可分配部门
			}else {
				List<Map<Object, Object>> allUsers = new ArrayList<>();
				for(String orgLevel : orgLevels) {
					List<Map<Object, Object>> users = userService.getUserByJobsOrgLevel(orgLevel,jobs);
					allUsers.addAll(users);
				}
				allUsers = removeDuplicateMap(allUsers);
				for(Map<Object, Object> u : allUsers) {
					UserSimpleMobileVo vo = new UserSimpleMobileVo();
					vo.setUserId(u.get("userId")!=null?u.get("userId").toString():"");
					vo.setAccount(u.get("account")!=null?u.get("account").toString():"");
					vo.setName(u.get("name")!=null?u.get("name").toString():"");
					userVos.add(vo);
				}
				//userVos = removeDuplicateUserSimpleMobileVo(userVos);
			}
		}
		orgVos = orgVos!=null?this.removeDuplicateOrgVo(orgVos):orgVos;
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("campus", orgVos);
		map.put("user", userVos);
		//response.setResultCode(200);
		response.setResultMessage("");
		response.setData(map);
		return response;
	}
	
	private List<OrganizationMobileSimpleVo> removeDuplicateOrgVo(List<OrganizationMobileSimpleVo> list) {
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
	
	@Override
	public Response getDistributableUserByName(String userName) {
		// TODO Auto-generated method stub
		Response response = new Response();
		User currentUser = userService.getCurrentLoginUser();
		if(currentUser==null) {
			response.setResultCode(400);
			response.setResultMessage("用户未登录，请重新登陆");
			return response;
		}
		List<Organization> organizations = userService.getDeptExistsDistributableZXS(currentUser.getUserId());
		//List<Organization> organizations = userService.getDeptExistsDistributableZXS("USE0000018100");
		List<Map<Object, Object>> allUsers = new ArrayList<>();
		if(organizations!=null&&organizations.size()>0) {
			for(Organization  obj : organizations) {
				String brenchName ="";
				if(obj.getOrgType() == OrganizationType.BRENCH) {
					brenchName = obj.getName();
				}else {
					Organization org =  organizationService.findBrenchById(obj.getId());
					brenchName = org !=null?org.getName():"";
				}
				List<Map<Object, Object>> users = userService.getUserOrganizationByJobsAndName(obj.getOrgLevel(),new String[] {RoleSign.ZXS.getValue().toLowerCase(),RoleSign.ZXZG.getValue().toLowerCase()},userName,brenchName);
				allUsers.addAll(users);
			}
			allUsers = removeDuplicateMap(allUsers);
		}
		
		//response.setResultCode(200);
		response.setResultMessage("");
		response.setData(allUsers);
		return response;
	}

	private List<Map<Object, Object>> removeDuplicateMap(List<Map<Object, Object>> list) {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).get("userId").equals(list.get(i).get("userId"))) {
					list.remove(j);
				}
			}
		}
		return list;
	}

	@Override
	public Response getCustomerDetailById(String customerId) {
		// TODO Auto-generated method stub
		Response reponse = new Response();
		if(StringUtils.isBlank(customerId)) {
			reponse.setResultCode(400);
    		reponse.setResultMessage("customerId不能为空");
    		return reponse;
		}
		Customer customer = customerDao.findById(customerId);
    	if(customer==null) {
    		reponse.setResultCode(400);
    		reponse.setResultMessage("客户不存在");
    		return reponse;
    	}
    	Map<String,Object> map = new HashMap<>();
    	map.put("id", customer.getId());
    	map.put("name", customer.getName());
    	map.put("contact", customer.getContact());
    	map.put("cusTypeName", customer.getCusType()!=null?customer.getCusType().getName():"");
    	map.put("cusOrgName", customer.getCusOrg()!=null?customer.getCusOrg().getName():"");
    	map.put("remark", customer.getRemark());
    	map.put("resEntrance", customer.getResEntrance()!=null?customer.getResEntrance().getName():"");
    	
    	StringBuffer sql_student = new StringBuffer();
    	sql_student.append(" select s.id studentId,s.name studentName,(CASE s.SEX WHEN '1' THEN '男' WHEN '0' THEN '女' ELSE '未填写' END) sex,sc.NAME schoolName,d.name gradeName,s.CONTACT studentContact ");
    	sql_student.append(" from student s ");
    	sql_student.append(" left join student_school sc on s.SCHOOL = sc.ID ");
    	sql_student.append(" left join data_dict d on s.GRADE_ID = d.ID ");
    	sql_student.append("  WHERE (s.STU_STATUS = 1 OR s.STU_STATUS IS NULL) and  s.ID in (select csr.STUDENT_ID from customer_student_relation csr where csr.CUSTOMER_ID= :customerId and csr.IS_DELETED =0 ) ");
    	Map<String, Object> params = Maps.newHashMap();
    	params.put("customerId", customer.getId());
		List<Map<Object, Object>> list=studentDao.findMapBySql(sql_student.toString(), params);
		map.put("students", list);
    	
		reponse.setData(map);
		return reponse;
	}

	@Override
	public DataPackage getCustomerFollowUpRecrodsList(String customerId, DataPackage dataPackage) {
		// TODO Auto-generated method stub
		Order descOrder = Order.desc("createTime");
		List<Order> descOrderList = new ArrayList<Order>();
		descOrderList.add(descOrder);
		dataPackage = customerFolowupDao.findPageByCriteria(dataPackage,HibernateUtils.prepareOrder(dataPackage, "createTime", "desc"), Expression.eq("customer.id", customerId));
		List<CustomerFolowup> datas = (List<CustomerFolowup>)dataPackage.getDatas();
		/*if(datas!=null&&datas.size()>0) {
			for(CustomerFolowup customerFolowup : datas) {
				if(StringUtils.isNotBlank(customerFolowup.getRemark())) {
					customerFolowup.setRemark(EmojiConvertUtil.emojiRecovery(customerFolowup.getRemark()));
				}
			}
		}*/
		
		dataPackage.setDatas(HibernateUtils.voListMapping((List<CustomerFolowup>)dataPackage.getDatas(), CustomerFolowupVo.class));
		return dataPackage;
	}

	@Override
	public Response getUserCustomerCount() {
		// TODO Auto-generated method stub
		Response response = new Response();
		User currentUser = userService.getCurrentLoginUser();
		if(currentUser==null) {
			response.setResultCode(400);
			response.setResultMessage("用户未登录，请重新登陆");
			return response;
		}
		String userId = currentUser.getUserId();
		
		Map<String, Object> result = Maps.newHashMap();

		Organization organization = userService.getUserMainDeptByUserId(userId);
		String orgId=organization.getId();
		
		StringBuilder cusSql=new StringBuilder(1024);
		
		Organization blCampus = userService.getBelongCampusByOrgId(orgId);
		String orgLevel = blCampus.getOrgLevel();
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", currentUser.getUserId());
		params.put("ORGLEVEL",  orgLevel+"%");
		
		/*cusSql.append(" SELECT C.ID,C.NAME,'SIGNEUP' AS dealStatus ");
		cusSql.append(" FROM CUSTOMER C ");
		cusSql.append(" left join customer_student_relation csr on c.id=csr.CUSTOMER_ID ");
		cusSql.append(" left join student s on csr.student_id=s.ID ");
		cusSql.append(" left join (select cono.STUDENT_ID,cono.CREATE_TIME,cono.CREATE_USER_ID from contract cono where cono.create_user_id= :userId group by cono.STUDENT_ID )con on con.STUDENT_ID=s.ID ");
		cusSql.append(" WHERE 1=1 ");	
		cusSql.append(" and s.student_type='ENROLLED' ");
		cusSql.append(" and con.create_user_id= :userId ");
		cusSql.append(" AND (C.BL_SCHOOL IN (SELECT ID FROM ORGANIZATION WHERE ORGLEVEL LIKE :ORGLEVEL )) ");
		cusSql.append(" ORDER BY  con.CREATE_TIME DESC ");*/
			
		cusSql.append(" SELECT count(*) ");
		cusSql.append(" FROM CUSTOMER C ");
		cusSql.append(" WHERE 1=1 ");	
		cusSql.append(" and c.DELEVER_TARGET = :userId ");
		cusSql.append(" AND (C.BL_SCHOOL IN (SELECT ID FROM ORGANIZATION WHERE ORGLEVEL LIKE :ORGLEVEL )) ");
		cusSql.append(" and c.DEAL_STATUS != '"+CustomerDealStatus.INVALID.getValue()+"'");
		cusSql.append(" AND not exists ( select 1 from contract con ");
		cusSql.append(" left join student s on con.STUDENT_ID=s.ID ");
		cusSql.append(" where con.CUSTOMER_ID = c.id and s.student_type='ENROLLED'  and con.create_user_id= c.DELEVER_TARGET  ) ");
		
		int fllowing = customerDao.findCountSql(cusSql.toString(),params);
		
		StringBuilder cusSql_toBe=new StringBuilder(1024);
		cusSql_toBe.append(" SELECT count(*) ");
		cusSql_toBe.append(" FROM CUSTOMER C ");
        cusSql_toBe.append(" WHERE 1=1 ");	
		cusSql_toBe.append(" AND C.DELEVER_TARGET = :userId ");
		cusSql_toBe.append(" AND C.DEAL_STATUS = 'STAY_FOLLOW' ");
		cusSql_toBe.append(" AND (C.BL_SCHOOL IN (SELECT ID FROM ORGANIZATION WHERE ORGLEVEL LIKE :ORGLEVEL ))");
		
		int toBeCount =  customerDao.findCountSql(cusSql_toBe.toString(),params);	
		
		Response res = commonService.judgeUserIdentity(currentUser.getUserId());
		
		int code = -1;
		if(res.getData()!=null) {
			Map<String, Object> map = (Map<String, Object>) res.getData();
			code = (int) map.get("code");
		}
		
		if(code == 0) {
			result.put("TOBEASSIGNED", toBeCount);
			result.put("STAY_FOLLOW", toBeCount);
		}else if(code == 2) {
			result.put("STAY_FOLLOW", 0);
			result.put("TOBEASSIGNED", toBeCount);
		}else if(code == 1) {
			result.put("STAY_FOLLOW", toBeCount);
			result.put("TOBEASSIGNED", 0);
		}else {
			result.put("TOBEASSIGNED", 0);
			result.put("STAY_FOLLOW", 0);
		}
		
		result.put("UNSIGNEUP", fllowing);
		
		response.setData(result);
		return response;
	}

	@Override
	public Response mobileDistributeCustomer(String cusId, String status) {
		// TODO Auto-generated method stub
		Response response = new Response();
		User currentUser = userService.getCurrentLoginUser();
		if(currentUser==null) {
			response.setResultCode(400);
			response.setResultMessage("用户未登录，请重新登陆");
			return response;
		}
		
		String userId = currentUser.getUserId();
		
		if("All".equalsIgnoreCase(status)) {
			Organization organization = userService.getUserMainDeptByUserId(userId);
			String orgId=organization.getId();
			
			StringBuilder cusSql=new StringBuilder(1024);
			cusSql.append(" SELECT GROUP_CONCAT(id) id");
			cusSql.append(" FROM CUSTOMER C ");
			
			Map<String, Object> params = Maps.newHashMap();
	        params.put("USERID", currentUser.getUserId());
	        
			
			cusSql.append(" WHERE 1=1 ");	
			
			
			Organization blCampus = userService.getBelongCampusByOrgId(orgId);
			String orgLevel = blCampus.getOrgLevel();
			params.put("ORGLEVEL",  orgLevel+"%");
			cusSql.append(" AND C.DELEVER_TARGET = :USERID ");
			cusSql.append(" AND C.DEAL_STATUS = 'STAY_FOLLOW' ");
			cusSql.append(" AND (C.BL_SCHOOL IN (SELECT ID FROM ORGANIZATION WHERE ORGLEVEL LIKE :ORGLEVEL ))");
			List<Map<Object,Object>> list=customerDao.findMapBySql(cusSql.toString(), params);
			if(list!=null&&list.size()>0) {
				cusId = list.get(0).get("id")!=null?list.get(0).get("id").toString():"";
			}
		}
		if(StringUtils.isBlank(cusId)) {
			response.setResultCode(400);
			response.setResultMessage("没有要获取的客户资源");
			return response;
		}
		return this.distributeCustomer(cusId, "person",WorkbrenchType.CALL_OUT.getValue());
	}
	
	public DataImportResult handleCustomerDataImportBySize(int size,int total,File file,
			CustomerDeliverType customerDeliverType, String[] strs, int maxNumber) throws IOException {
		DataImportResult dir=new DataImportResult();
		dir.setYetImport(0);
		dir.setMaxNumber(maxNumber);
		int maxPage = total/size;
		if(total%size != 0) {
			maxPage = maxPage +1;
		} 
		String fileName = file.getName();
		String resultFilePath = file.getPath().substring(0, file.getPath().lastIndexOf("\\"))+"\\result_"+fileName;
		for(int i=1; i <= maxPage; i++ ) {
			List<List<Object>> list = ReadExcel1.readExcelByStartEnd(file,size * (i-1)+2,size*i+1);
			dir=this.customerDataImportByList(list,customerDeliverType,strs,dir);
			if(dir.getFailNum()>0) {
				ReadExcel1.writeFalseExcel(new File(resultFilePath),resultFilePath,list);
			}
		}
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
			HSSFSheet sheet = hwb.getSheetAt(0);
			dir.setFailNum(sheet.getPhysicalNumberOfRows() - 2);
		} else if ("xlsx".equals(extension)) {
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			XSSFSheet sheet = xwb.getSheetAt(0);
			dir.setFailNum(sheet.getPhysicalNumberOfRows() - 2);
		} 
		return dir;
	}
	
	/*@Override
	public CustomerDataImport customerDataImportByFile(File file, CustomerPoolDataVo customerPoolDataVo) {
		// TODO Auto-generated method stub

		//将数据导入到customer表与pointial_student表，判断该用户是否剩余容量，用户的资源池容量由用户具有的角色来决定
		//List<List<Object>> list=ReadExcel1.readExcel(new File(uploadFile));
		int count = 0;
		int failNum = 0;
		int size = 10000;
		int maxNum = 0;
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		try {
			if ("xls".equals(extension)) {
				HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
				HSSFSheet sheet = hwb.getSheetAt(0);
				count = sheet.getPhysicalNumberOfRows() - 2;
				maxNum = sheet.getLastRowNum() +1;
			} else if ("xlsx".equals(extension)) {
				XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
				XSSFSheet sheet = xwb.getSheetAt(0);
				count = sheet.getPhysicalNumberOfRows() - 2;
				maxNum = sheet.getLastRowNum() +1;
			} else {
				return new CustomerDataImport("-","-",false,0,0,"上传失败，不支持该文件类型！");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new CustomerDataImport("-","-",false,0,0,"读取文件出现异常，请重试！");
		}
		//复制一份excel清空作为错误结果返回
		String resultFileName = "result_"+fileName;
		String resultFilePath = file.getPath().substring(0, file.getPath().lastIndexOf("\\"))+"\\result_"+fileName;
		FileUtil.fileCopy(file.getPath(),resultFilePath);
		try {
			ReadExcel1.clearData(new File(resultFilePath),resultFilePath,2,0);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			log.error("customerDataImportByFile 清除数据出现异常:"+resultFilePath);
			e2.printStackTrace();
		}
		User currentUser = userService.getCurrentLoginUser();
		
		String[] strs=null;
		DataImportResult dir=null;
		boolean importFlag = true;
		try {
			if(customerPoolDataVo.getAssign().equals("0")){
				//前端 选择 暂不选择分配对象 则默认分配给自己  
				Map<String, Integer> map = map = resourcePoolService.getResourcePoolVolumeByUserId(currentUser.getUserId());
				Integer total = map.get("total");
	            Integer current = map.get("current");
	            if(current >= total){
	            	CustomerDataImport vo=new CustomerDataImport(currentUser.getName(),"-",false,count,total-current>0?total-current:0,customerPoolDataVo.getDeliverTargetName()+"资源池已达上限，不允许导入！");
					return vo;
	            }
				strs=new String[]{CustomerDealStatus.STAY_FOLLOW.getValue(),currentUser.getUserId(),currentUser.getName()};
				dir = this.handleCustomerDataImportBySize(size,maxNum,file,CustomerDeliverType.PERSONAL_POOL,strs,total-current);
				//dir=customerService.customerDataImportByList(list,CustomerDeliverType.PERSONAL_POOL,strs);					
			}else{
				if(StringUtil.isNotBlank(customerPoolDataVo.getDeliverTarget()) && count>0){
					Map<String, Integer> map = null;
					if(customerPoolDataVo.getDeliverType()!=null){
						if(customerPoolDataVo.getDeliverType().equals(CustomerDeliverType.PERSONAL_POOL.getValue())){
							User user =userService.loadUserById(customerPoolDataVo.getDeliverTarget());
							customerPoolDataVo.setDeliverTargetName(user!=null?user.getName():"");
							map = resourcePoolService.getResourcePoolVolumeByUserId(customerPoolDataVo.getDeliverTarget());
						}else if(customerPoolDataVo.getDeliverType().equals(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.getValue())){
							Organization organization = organizationService.findById(customerPoolDataVo.getDeliverTarget());
							customerPoolDataVo.setDeliverTargetName(organization!=null?organization.getName():"");
							map =resourcePoolService.getResourcePoolVolumeByOrgId(customerPoolDataVo.getDeliverTarget());
						}
					}
	                Integer total = map.get("total");
	                Integer current = map.get("current");
	                if(current >= total){
	                	CustomerDataImport vo=new CustomerDataImport(customerPoolDataVo.getDeliverTargetName(),"-",false,count,total-current>0?total-current:0,customerPoolDataVo.getDeliverTargetName()+"资源池已达上限，不允许导入！");
						return vo;
	                }

					strs=new String[]{customerPoolDataVo.getDealStatus(),customerPoolDataVo.getDeliverTarget(),customerPoolDataVo.getDeliverTargetName()};
					dir = this.handleCustomerDataImportBySize(size,maxNum,file,CustomerDeliverType.valueOf(CustomerDeliverType.class, customerPoolDataVo.getDeliverType()),strs,total-current);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			importFlag = false;
			log.error("customerDataImportByFile 导入出错");
			e.printStackTrace();
		}
		//将导入文件上传到oss
		try {
			AliyunOSSUtils.put(file.getName(), file);
		} catch (FileNotFoundException e1) {
			log.error("customerDataImportByFile 上传阿里云oss失败:"+file.getPath());
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//将导入文件上传到oss
		try {
			AliyunOSSUtils.put(resultFileName, new File(resultFilePath));
		} catch (FileNotFoundException e1) {
			log.error("customerDataImportByFile 上传阿里云oss失败:"+resultFilePath);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CustomerImportHistory customerImportHistory = new CustomerImportHistory();
		customerImportHistory.setImportName(fileName);
		customerImportHistory.setResultName(resultFileName);
		customerImportHistory.setCreateUser(currentUser.getUserId());
		customerImportHistory.setCreateTime(DateTools.getCurrentDateTime());
		customerImportHistory.setTotalNum(new BigDecimal(count));
		customerImportHistory.setSuccessNum(new BigDecimal(dir.getYetImport()));
		customerImportHistory.setFailNum(new BigDecimal(dir.getFailNum()));
		customerImportHistory.setStatus(importFlag?"SUCCESS":"FAIL");
		customerImportHistoryDao.save(customerImportHistory);
		return new CustomerDataImport("-","-",true,count,dir.getFailNum());	
	}*/
	@Override
	public CustomerDataImport customerDataImportByFile(File file, CustomerPoolDataVo customerPoolDataVo) {
		User currentUser = userService.getCurrentLoginUser();
		String fileName = file.getName();
		
		int maxNum = 0;
		String[] strs=null;
		CustomerDeliverType cdt = null;
		if(customerPoolDataVo.getAssign().equals("0")){
			//前端 选择 暂不选择分配对象 则默认分配给自己  
			Map<String, Integer> map = map = resourcePoolService.getResourcePoolVolumeByUserId(currentUser.getUserId());
			Integer total = map.get("total");
            Integer current = map.get("current");
            if(current >= total){
            	CustomerDataImport vo=new CustomerDataImport(currentUser.getName(),"-",false,0,0,currentUser.getName()+"资源池已达上限，不允许导入！");
				return vo;
            }
            strs=new String[]{CustomerDealStatus.STAY_FOLLOW.getValue(),currentUser.getUserId(),currentUser.getName()};
            maxNum = total - current;
            cdt = CustomerDeliverType.PERSONAL_POOL;
		}else{
			if(StringUtil.isNotBlank(customerPoolDataVo.getDeliverTarget())){
				Map<String, Integer> map = null;
				if(customerPoolDataVo.getDeliverType()!=null){
					if(customerPoolDataVo.getDeliverType().equals(CustomerDeliverType.PERSONAL_POOL.getValue())){
						User user =userService.loadUserById(customerPoolDataVo.getDeliverTarget());
						customerPoolDataVo.setDeliverTargetName(user!=null?user.getName():"");
						map = resourcePoolService.getResourcePoolVolumeByUserId(customerPoolDataVo.getDeliverTarget());
					}else if(customerPoolDataVo.getDeliverType().equals(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.getValue())){
						Organization organization = organizationService.findById(customerPoolDataVo.getDeliverTarget());
						customerPoolDataVo.setDeliverTargetName(organization!=null?organization.getName():"");
						map =resourcePoolService.getResourcePoolVolumeByOrgId(customerPoolDataVo.getDeliverTarget());
					}
				}
                Integer total = map.get("total");
                Integer current = map.get("current");
                if(current >= total){
                	CustomerDataImport vo=new CustomerDataImport(customerPoolDataVo.getDeliverTargetName(),"-",false,0,0,customerPoolDataVo.getDeliverTargetName()+"资源池已达上限，不允许导入！");
					return vo;
                }
                maxNum = total - current;
                cdt = CustomerDeliverType.valueOf(CustomerDeliverType.class, customerPoolDataVo.getDeliverType());
                strs=new String[]{customerPoolDataVo.getDealStatus(),customerPoolDataVo.getDeliverTarget(),customerPoolDataVo.getDeliverTargetName()};
			}else {
				CustomerDataImport vo=new CustomerDataImport(null,"-",false,0,0,"请选择分配对象！");
				return vo;
			}
		}
		
		//excel转csv
		ExcelToCSV excelToCSV = new ExcelToCSV(); 
		String path =  file.getPath();
		if(path.indexOf("\\") != -1) {
			path = path.replaceAll("\\\\", "/");
		}
		String csvPath = path.substring(0, path.lastIndexOf("."))+".csv";
		//String csvPath = "D:\\"+fileName+".csv";
		try {
			excelToCSV.ExcelToCSV(path, csvPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("customerDataImportByFile excel转csv失败！");
			e.printStackTrace();
		}
		
		boolean importFlag = true;
		int number = 0;//去掉表头
		int totalNum = 0;//总数据量
		int failNum = 0;//失败数据
		int successNum =0;//成功数据
		boolean flag = false;
		//错误返回excel
		String resultFileName = "result_"+fileName;
		String resultFilePath = path.substring(0, path.lastIndexOf("/"))+"/result_"+fileName;
    	//FileUtil.readInputStreamToFile(AliyunOSSUtils.get("新的客户资源导入模板.xlsx"),resultFilePath);
    	OutputStream os = null;
    	BufferedReader br = null;
		try {
			Workbook wb = new SXSSFWorkbook(2000);
			Sheet sh = wb.createSheet(); 
			Row row = null;
			Cell cell = null;
			
			br = new BufferedReader(new FileReader(csvPath));
			String[] rows = null;
			List<Object> obj = null;
			while (br.ready()) {
				rows = br.readLine().split("\\|@\\|",-1);
				number++;
				if(number < 2) {
					continue;
				}
				obj = Arrays.asList(rows);
				int size = obj.size();
				size = size>=17?17:size;
				if(number == 2) {
					row = sh.createRow(0); 
					for(int index = 0 ; index < size ; index++ ) {
						cell = row.createCell(index);
						cell.setCellValue(obj.get(index)!=null?obj.get(index).toString():"");
					}
					cell = row.createCell(size);
					cell.setCellValue("失败原因");
					continue;
				}
				totalNum++;
				try{
					if(flag) {
						obj.set(0,"资源池已满");
						failNum++;
						continue;
					}
					DataImportResult dir1 = dealRowData(obj,number,strs,cdt,currentUser);
					if(dir1.getResult()!=null){
						obj.set(0,dir1.getResult());
						failNum++;
					}else {
						successNum++;
						obj.set(0,"SUCCESS");
						if(successNum >= maxNum) {
							flag = true;
						}
					}
				
				}catch(Exception e){
					log.error(e.getMessage());
					e.printStackTrace();
					obj.set(0,"插入数据失败");
					failNum++;
				}
				if(!"SUCCESS".equals(obj.get(0))) {
					row = sh.createRow(failNum); 
					for(int index = 0 ; index < size ; index++ ) {
						cell = row.createCell(index);
						if(index == 0) {
							cell.setCellValue(failNum);
						}else {
							cell.setCellValue(obj.get(index)!=null?obj.get(index).toString():"");
						}
					}
					cell = row.createCell(size);
					cell.setCellValue(obj.get(0)!=null?obj.get(0).toString():"");
				}
			}
			os = new FileOutputStream(new File(resultFilePath));
			wb.write(os);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			importFlag = false;
			log.error("customerDataImportByFile 导入出现异常失败");
			e.printStackTrace();
		}finally {
			try {
				if(os !=null) {
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("customerDataImportByFile 输出流关闭失败");
				e.printStackTrace();
			}
			try {
				if(br !=null) {
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("customerDataImportByFile 输出流关闭失败");
				e.printStackTrace();
			}
		}
		//将导入文件上传到oss
		try {
			AliyunOSSUtils.put(file.getName(), file);
		} catch (FileNotFoundException e1) {
			log.error("customerDataImportByFile 上传阿里云oss失败:"+path);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//将导入文件上传到oss
		try {
			AliyunOSSUtils.put(resultFileName, new File(resultFilePath));
		} catch (FileNotFoundException e1) {
			log.error("customerDataImportByFile 上传阿里云oss失败:"+resultFilePath);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	//添加历史记录
    	CustomerImportHistory customerImportHistory = new CustomerImportHistory();
		customerImportHistory.setImportName(fileName);
		customerImportHistory.setResultName(resultFileName);
		customerImportHistory.setCreateUser(currentUser.getUserId());
		customerImportHistory.setCreateTime(DateTools.getCurrentDateTime());
		customerImportHistory.setTotalNum(new BigDecimal(totalNum));
		customerImportHistory.setSuccessNum(new BigDecimal(successNum));
		customerImportHistory.setFailNum(new BigDecimal(failNum));
		customerImportHistory.setStatus(importFlag?"SUCCESS":"FAIL");
		customerImportHistory.setDeliverTarget(customerPoolDataVo.getDeliverTarget());
		customerImportHistory.setDeliverTargetName(customerPoolDataVo.getDeliverTargetName());
		customerImportHistory.setDeliverType(cdt);
		customerImportHistoryDao.save(customerImportHistory);
		
		
		return new CustomerDataImport("-","-",true,totalNum,failNum);	
	}

	@Override
	public DataPackage customerImportHistoryList(DataPackage dataPackage, TimeVo timeVo) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<>();
		User currentUser = userService.getCurrentLoginUser();
		if(currentUser == null) {
			throw new ApplicationException(ErrorCode.NO_LOGIN_USER_INFO);
		}
		StringBuffer hql=new StringBuffer();
		hql.append(" from CustomerImportHistory where createUser = :createUser ");
		params.put("createUser", currentUser.getUserId());
		
		if(StringUtils.isNotBlank(timeVo.getStartDate())) {
			hql.append(" and createTime >= :startTime ");
			params.put("startTime", timeVo.getStartDate() +" 00:00:00");
		}
		
		if(StringUtils.isNotBlank(timeVo.getEndDate())) {
			hql.append(" and createTime <= :endTime ");
			params.put("endTime", timeVo.getEndDate() +" 23:59:59");
		}
		
		hql.append(" order by createTime desc ");
		
		dataPackage = customerImportHistoryDao.findPageByHQL(hql.toString(),dataPackage, true, params);
		
		return dataPackage;
	}

	@Override
	public Response checkCustomerStudengName(String cusId, String stuId, String stuName) {
		// TODO Auto-generated method stub
		Response response = new Response();
		if(StringUtils.isBlank(cusId)||StringUtils.isBlank(stuName)) {
			response.setResultCode(-1);//格式错误
			response.setResultMessage("参数出错");
			return response;
		}
		Map<String, Object> param = Maps.newHashMap();
		StringBuffer query = new StringBuffer();
		query.append(" select s.ID as studentId,s.`NAME` as studentName  ");
		query.append(" from customer_student_relation csr LEFT JOIN student s on csr.STUDENT_ID = s.ID ");
		query.append(" where csr.customer_id = :customerId ");
		query.append(" and (csr.IS_DELETED ='0' or  csr.IS_DELETED is null ) ");
		query.append(" and (s.STU_STATUS = 1 OR s.STU_STATUS IS NULL) ");
		query.append(" and s.id !=:studentId and s.name =:studentName ");
		param.put("customerId", cusId);
		param.put("studentId", stuId);
		param.put("studentName", stuName);
		List<Map<Object, Object>> students = studentDao.findMapBySql(query.toString(),param);
		if(students == null || students.size()<1) {
			response.setResultMessage("客户不存在同名学生");
		}else {
			response.setResultCode(-1);//格式错误
			response.setResultMessage("该客户已存在同名学生，不允许重复学籍！");
		}
		return response;
	}
}

