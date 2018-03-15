package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eduboss.common.RoleSign;
import com.eduboss.common.AppointmentType;
import com.eduboss.common.CustomerActive;
import com.eduboss.common.CustomerAuditStatus;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.CustomerEventType;
import com.eduboss.common.ResEntranceType;
import com.eduboss.common.RoleCode;
import com.eduboss.dao.ContractDao;
import com.eduboss.dao.CustomerFolowupDao;
import com.eduboss.dao.CustomerStudentDao;
import com.eduboss.dao.GainCustomerDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.dao.TransferCustomerDao;
import com.eduboss.domain.Contract;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.CustomerFolowup;
import com.eduboss.domain.CustomerStudent;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.GainCustomer;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.domain.Student;
import com.eduboss.domain.TransferCustomerRecord;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AuditRecordVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.TransferCustomerRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ContractService;
import com.eduboss.service.CustomerEventService;
import com.eduboss.service.CustomerService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.ResourcePoolService;
import com.eduboss.service.TransferCustomerService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;
import com.eduboss.utils.StringUtil;

@Service("com.eduboss.service.TransferCustomerService")
public class TransferCustomerServiceImpl implements TransferCustomerService{

	@Autowired
	private TransferCustomerDao transferCustomerDao;
	@Autowired
	private CustomerService customerService;
	
    @Autowired
    private GainCustomerDao gainCustomerDao;
    @Autowired
    private CustomerEventService customerEventService;
    
    @Autowired
    private UserService userService;
    
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private CustomerStudentDao customerStudentDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private CustomerFolowupDao customerFolowupDao;
	
	@Autowired
	private ResourcePoolService resourcePoolService;
    
	private final static Logger log = Logger.getLogger(TransferCustomerServiceImpl.class);
	
	@Override
	public DataPackage getTransferCustomers(TransferCustomerRecordVo tCustomerRecordVo, DataPackage dp) {
		StringBuilder transferSql=new StringBuilder(256);
		Map<String, Object> params = new HashMap<>();
		transferSql.append("select tra.*,d.`NAME` as restranceName,o.`name` as intentionCampusName from transfer_introduce_customer tra ");
		//transferSql.append(" left join customer c on tra.customer_id = c.ID ");
		transferSql.append(" left join organization o on tra.intention_campus_id = o.id ");
		transferSql.append(" left join user u on tra.create_user_id = u.USER_ID ");
		transferSql.append(" left join data_dict d on tra.res_entrance = d.ID where tra.parent_id is null ");
		
		/*//转介绍分配客户以后记录消失
		transferSql.append(" and ( tra.audit_status ='"+CustomerAuditStatus.TOBE_AUDIT+"' or tra.audit_status ='"+CustomerAuditStatus.PASS+"' ");
		transferSql.append(" or (tra.audit_status ='"+CustomerAuditStatus.NOT_PASS+"' and tra.distribute_target is null ))");
		*/
		//查询条件 姓名 联系方式  介绍人 类型 审核状态  资源入口
		if(StringUtils.isNotBlank(tCustomerRecordVo.getCustomerName())){
			transferSql.append(" and tra.customer_name like :customerName ");
			params.put("customerName", "%"+tCustomerRecordVo.getCustomerName()+"%");
		}
		if(StringUtils.isNotBlank(tCustomerRecordVo.getStudentName())){
			transferSql.append(" and tra.student_name like :studentName ");
			params.put("studentName", "%"+tCustomerRecordVo.getStudentName()+"%");
		}
		if(StringUtils.isNotBlank(tCustomerRecordVo.getContact())){
			transferSql.append(" and tra.contact = :contact ");
			params.put("contact", tCustomerRecordVo.getContact());
		}
		if(StringUtils.isNotBlank(tCustomerRecordVo.getIntroducer())){
			transferSql.append(" and tra.introducer like  :introducer ");
			params.put("introducer", "%"+tCustomerRecordVo.getIntroducer()+"%");
		}
		if(StringUtils.isNotBlank(tCustomerRecordVo.getIntroducerContact())){
			transferSql.append(" and tra.introducer_contact like :introducerContact ");
            params.put("introducerContact", "%"+tCustomerRecordVo.getIntroducerContact()+"%");
		}
		if(StringUtils.isNotBlank(tCustomerRecordVo.getCusOrg())){
			transferSql.append(" and tra.introducer_type = :cusOrg ");
			params.put("cusOrg", tCustomerRecordVo.getCusOrg());
		}
		if(tCustomerRecordVo.getAuditStatus()!=null){
			if(StringUtils.isNotBlank(tCustomerRecordVo.getAuditStatus().getValue())){
				transferSql.append(" and tra.audit_status = :auditStatus ");
				params.put("auditStatus", tCustomerRecordVo.getAuditStatus().getValue());
			}			
		}
		// 资源入口
		if (StringUtils.isNotBlank(tCustomerRecordVo.getResEntranceId())) {
			transferSql.append(" and tra.res_entrance= :resEntranceId ");
			params.put("resEntranceId", tCustomerRecordVo.getResEntranceId());
		}
		//提交人
		if(StringUtil.isNotBlank(tCustomerRecordVo.getCreateUserName())){
			transferSql.append(" and u.`NAME` like '%" + tCustomerRecordVo.getCreateUserName() + "%' ");
		}
		
		
		if(StringUtils.isNotBlank(tCustomerRecordVo.getBeginCreateTime())){
			transferSql.append(" and tra.create_time >= :beginCreateTime ");
			params.put("beginCreateTime", tCustomerRecordVo.getBeginCreateTime()+" 00:00:00 ");
		}			
		if(StringUtils.isNotBlank(tCustomerRecordVo.getEndCreateTime())){
			transferSql.append(" and tra.create_time <= :endCreateTime ");
            params.put("endCreateTime", tCustomerRecordVo.getEndCreateTime()+" 23:59:59 ");
		}
		
		

		//权限控制
		User user= userService.getCurrentLoginUser();
		Organization organization = organizationService.findById(user.getOrganizationId());
		String orgLevel = organization.getOrgLevel();
		if(orgLevel==null){
			return dp;
		}else{
			if(orgLevel.length()>=8){
				transferSql.append(" and (tra.org_level like :orgLevel or tra.campus_org_level like :orgLevel ) ");
				params.put("orgLevel", orgLevel+"%");
			}else{
				transferSql.append(" and (tra.org_level = :orgLevel or tra.campus_org_level like :campusOrgLevel ) ");
				params.put("orgLevel", orgLevel);
				params.put("campusOrgLevel", orgLevel+"%");
			}
		}
		
        StringBuilder countSql = new  StringBuilder(" select count(*) from ( " + transferSql.toString() + " ) countall ");
//		if (StringUtils.isNotBlank(dp.getSord())
//				&& StringUtils.isNotBlank(dp.getSidx())) {
//			transferSql.append(" order by tra."+dp.getSidx()+" "+dp.getSord());
//		}
		transferSql.append(" order by tra.create_time  desc ");
	    //下面方法查出来的属性是数据库的字段属性
	    // dp = transferCustomerDao.findMapPageBySQL(transferSql.toString(), dp);
	
        
	    List<Map<Object,Object>> list=transferCustomerDao.findMapOfPageBySql(transferSql.toString(), dp, params);
		List<Map<String, String>> list1 = new ArrayList<>();
		for (Map<Object,Object> map:list){
			 Map<String, String> a =(Map)map;
			list1.add(a);
		}
	    dp.setRowCount(transferCustomerDao.findCountSql(countSql.toString(), params));
	    List<TransferCustomerRecordVo> reList= new ArrayList<TransferCustomerRecordVo>();
	    TransferCustomerRecordVo recordVo = null ;
	    for(Map<String, String> map:list1){
	    	recordVo = new TransferCustomerRecordVo();
	    	if(map.get("id")!=null) recordVo.setId(map.get("id"));
	    	if(map.get("customer_id")!=null) recordVo.setCustomerId(map.get("customer_id"));
	    	if(map.get("customer_name")!=null)recordVo.setCustomerName(map.get("customer_name"));
	    	if(map.get("contact")!=null)recordVo.setContact(map.get("contact"));

	    	if(map.get("intentionCampusName")!=null)recordVo.setIntentionCampusName(map.get("intentionCampusName"));
	    	recordVo.setStudentName(map.get("student_name"));
	    	
	    	recordVo.setCreateTime(map.get("create_time"));
			recordVo.setResEntranceId(map.get("res_entrance"));
			recordVo.setResEntranceName(map.get("restranceName")!=null?map.get("restranceName"):"");
	        recordVo.setIntroducerContact(map.get("introducer_contact")!=null?map.get("introducer_contact"):"");
	    	if(map.get("introducer")!=null)recordVo.setIntroducer(map.get("introducer"));
	    	if(map.get("introducer_type")!=null)recordVo.setCusOrg(map.get("introducer_type"));//cus_org
	    	if(map.get("remark")!=null)recordVo.setRemark(map.get("remark"));
	    	if(StringUtils.isBlank(recordVo.getRemark())){
	    		recordVo.setRemark("-");
	    	}
	    	if(map.get("audit_status")!=null)recordVo.setAuditStatus(CustomerAuditStatus.valueOf(map.get("audit_status")));
	    	
	    	if(map.get("create_user_id")!=null)recordVo.setCreateUserId(map.get("create_user_id"));
	    	
	    	//判断这个是不是提交审核记录的人是不是学管师  xiaojinwang 20170301
	    	
	    	//这里要兼容学管主管  兼容主副职位或者角色
	    	if(map.get("create_user_id")!=null){
	    		String createUserId = map.get("create_user_id");
	    		User createUser = userService.loadUserById(createUserId);
	    		recordVo.setCreateUserName(createUser.getName());
	    		List<Role> roles = userService.getRoleByUserId(createUserId);	    		
	    		//角色兼容职位 
	    		List<String> jobSigns= userService.getUserAllRoleSign(createUserId);	
	    		//下面的循环  按照咨询师（咨询主管） 学管 老师 否则默认咨询师来定位
	    		for(Role role: roles){
	    			if(role.getRoleCode() == RoleCode.CONSULTOR||role.getRoleCode() == RoleCode.CONSULTOR_DIRECTOR){
	    				recordVo.setRoleCode(RoleCode.CONSULTOR.getValue());
	    				break;
	    			}else if(role.getRoleCode()==RoleCode.STUDY_MANAGER || role.getRoleCode()==RoleCode.STUDY_MANAGER_HEAD){
	    				recordVo.setRoleCode(RoleCode.STUDY_MANAGER.getValue());
	    				break;
	    			}else if(role.getRoleCode()==RoleCode.TEATCHER){
	    				recordVo.setRoleCode(RoleCode.TEATCHER.getValue());
	    				break;
	    			}else {
	    				recordVo.setRoleCode(RoleCode.CONSULTOR.getValue());
	    				//break;
	    			}
	    		}
	    		if(StringUtil.isBlank(recordVo.getRoleCode())){
	    			for(String jobSign:jobSigns){
	    				if(RoleSign.ZXS.getValue().equalsIgnoreCase(jobSign)||RoleSign.ZXZG.getValue().equalsIgnoreCase(jobSign)){
	    					recordVo.setRoleCode(RoleCode.CONSULTOR.getValue());
		    				break;
	    				}else if(RoleSign.XGS.getValue().equalsIgnoreCase(jobSign)||RoleSign.XGZG.getValue().equalsIgnoreCase(jobSign)){
	    					recordVo.setRoleCode(RoleCode.STUDY_MANAGER.getValue());
		    				break;
	    				}else if(RoleSign.LS.getValue().equalsIgnoreCase(jobSign)) {
	    					recordVo.setRoleCode(RoleCode.TEATCHER.getValue());
		    				break;
		    			}else {
		    				recordVo.setRoleCode(RoleCode.CONSULTOR.getValue());
		    			}
	    			}
	    		}
	            recordVo.setIsStudyManager(false);	    		
	    	}else{
	    		recordVo.setIsStudyManager(false);
	    	}

	    	

	    		
	    	
	    	
	    	reList.add(recordVo);
	    }
	    dp.setDatas(reList);
		return dp;
	}
	
	
	
	

	@Override
	public Response auditTransferCustomer(TransferCustomerRecordVo tCustomerRecordVo,CustomerVo customerVo) {
			
		TransferCustomerRecord record = HibernateUtils.voObjectMapping(tCustomerRecordVo, TransferCustomerRecord.class);
		String auditStatus = record.getAuditStatus().getValue();
		TransferCustomerRecord auditRecord =transferCustomerDao.findById(tCustomerRecordVo.getId());
		
		String customerId = auditRecord.getCustomerId();
		Customer customer =customerService.findById(customerId);
		
		String studentId = auditRecord.getStudentId();
		if(StringUtils.isNotBlank(studentId)) {
			Map<String, String> params = Maps.newHashMap();
			params.put("studentId", studentId);
			String contract_sql = " select count(*) from contract where STUDENT_ID = :studentId ";
			int count1 = contractDao.findCountSql(contract_sql, params);
			if(count1 > 0) {
				Response response = new Response();
		    	response.setResultCode(-1);
		    	response.setResultMessage("该学生已有合同，不允许重新分配！");
		    	return response;
			}
			
			String folowup_sql = " select count(*) from customer_folowup where CUSTOMER_ID = :customerId and FOLLOW_STUDENT_ID = :studentId ";
			params.put("customerId", customerId);
			int count2 = customerFolowupDao.findCountSql(folowup_sql, params);
			if(count2 > 0) {
				Response response = new Response();
		    	response.setResultCode(-1);
		    	response.setResultMessage("该学生已有跟进记录，不允许重新分配！");
		    	return response;
			}
		}
		
		/*if(CustomerAuditStatus.PASS.getValue().equals(auditStatus) && 
				(customer.getDealStatus() == CustomerDealStatus.FOLLOWING || customer.getDealStatus() == CustomerDealStatus.SIGNEUP)) {
			Response response = new Response();
	    	response.setResultCode(-1);
	    	response.setResultMessage("客户处于"+customer.getDealStatus().getName()+"状态，不允许重新分配！");
	    	return response;
		}*/
		
		if(auditRecord.getAuditStatus()==CustomerAuditStatus.PASS){
		    if("请选择分配人".equals(tCustomerRecordVo.getDistributeTargetName())){
		    	Response response = new Response();
		    	response.setResultCode(-1);
		    	response.setResultMessage("请选择分配人");
		    	return response;
		    }
		   		    
		}
		

		

		Boolean isZXS = false;//只有分配对象是咨询师才会真正分配出去      是不是咨询师
		if (CustomerAuditStatus.PASS.getValue().equals(auditStatus)) {
			if (StringUtil.isNotBlank(customerVo.getDeliverTarget())) {

				String jobSign = userService.getUserRoleSign(customerVo.getDeliverTarget());
				Boolean iszxs = userService.isUserRoleCode(customerVo.getDeliverTarget(),RoleCode.CONSULTOR);
				Boolean isZXZG = userService.isUserRoleCode(customerVo.getDeliverTarget(),RoleCode.CONSULTOR_DIRECTOR);
				if((jobSign!=null && jobSign.equals(RoleSign.ZXS.getValue().toLowerCase()))||iszxs||isZXZG
				||(jobSign!=null && jobSign.equals(RoleSign.ZXZG.getValue().toLowerCase()))){
					isZXS =true;
					
					//增加资源量的校验
				    Response res = resourcePoolService.getResourcePoolVolume(1, customerVo.getDeliverTarget());
					if(res.getResultCode() != 0) {
						return res;
					}
				}
				
				auditRecord.setDistributeTarget(customerVo.getDeliverTarget());
			} else {
				// String roleCode = tCustomerRecordVo.getRoleCode();//记录提交者的角色
				// 这种情况发生的话 应该是出错了
				auditRecord.setDistributeTarget(auditRecord.getCreateUserId());
			}
		}
		
		
		auditRecord.setAuditStatus(record.getAuditStatus());
		//auditRecord.setRemark(record.getRemark());//不要覆盖原来的备注
		auditRecord.setModifyTime(record.getModifyTime());
		auditRecord.setModifyUserId(record.getModifyUserId());
		auditRecord.setDistributeTargetName(record.getDistributeTargetName());
		if(StringUtils.isBlank(auditRecord.getAuditTime())) {
			auditRecord.setAuditTime(record.getAuditTime());
		}
		
		//添加子审核记录
		TransferCustomerRecord childRecord = new TransferCustomerRecord();
		childRecord.setParentId(auditRecord.getId());
		childRecord.setAuditStatus(record.getAuditStatus());
		childRecord.setRemark(record.getRemark());
		childRecord.setCreateTime(record.getModifyTime());
		childRecord.setCreateUserId(record.getModifyUserId());
		childRecord.setCustomerId(auditRecord.getCustomerId());
		childRecord.setCusOrg(auditRecord.getCusOrg());
		childRecord.setCustomerName(auditRecord.getCustomerName());
		childRecord.setContact(auditRecord.getContact());
		childRecord.setIntroducer(auditRecord.getIntroducer());
		childRecord.setIntroducerContact(auditRecord.getIntroducerContact());
		childRecord.setDistributeTarget(auditRecord.getDistributeTarget());
		childRecord.setDistributeTargetName(record.getDistributeTargetName());
		
		


		User refUser = userService.getCurrentLoginUser();
		
		
		//在审核通过的情况下 如果是新客户直接分配客户 如果是老客户还需要进行添加抢客记录，成为原来的跟进人的流失客户
		if(CustomerAuditStatus.PASS.getValue().equals(auditStatus)){
			//审核通过
			auditRecord.setResEntrance(new DataDict("TRANSFER"));
//			//该客户原来的预备资源入口和正式资源入口
//			String pre_Entrance = null;
//			String res_Entrance = null;
//			if(customer.getPreEntrance()!=null){
//				pre_Entrance = customer.getPreEntrance().getValue();
//			}
//			if(customer.getResEntrance()!=null){
//				res_Entrance = customer.getResEntrance().getValue(); 
//			}
//		    
//		    //原来的前分配对象 和现分配对象
//		    String beforeDeliverTarget = customer.getBeforeDeliverTarget();
		    String deliverTarget = customer.getDeliverTarget();
		    if (StringUtils.isBlank(deliverTarget)){
				customer.setDeliverTarget(null);
			}
		    //原来是转介绍登记的时候就修改客户的下面两个字段  现在改为审核通过才修改 20171023
		    customer.setCusType(new DataDict("INTRODUCE"));
		    customer.setCusOrg(auditRecord.getCusOrg());
		    
			//修改转介绍资源来源为转介绍 cusTYPE INTRODUCE  资源入口为RES_ENTRANCE  TRANSFER
			customer.setResEntrance(new DataDict("TRANSFER"));
			if(auditRecord.getCustomerStatus()==0){
				
				if(deliverTarget!=null){
					Boolean isWHZY = userService.isUserRoleCode(deliverTarget, RoleCode.OUTCALL_SPEC);
					Boolean isWLZY = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_SPEC);	
					if((isWHZY||isWLZY) && StringUtils.isNotBlank(customerVo.getDeliverTarget())
							&& !deliverTarget.equals(customerVo.getDeliverTarget())){
						customer.setBeforeDeliverTarget(null);
					}
				}

	
				//获取客户的时候判读 当前操作人的职位 如果是外呼或者网络专员  则上工序字段被清空		
			}
			
			//转介绍登记僵尸客户，分配咨询师获取后客户资源入口不正确duanmenrun 20171102  #1673
			if(customer.getCustomerActive()==CustomerActive.INACTIVE){
				customer.setCustomerActive(CustomerActive.NEW_CUSTOMER);
				
				//防止无效客户重新激活后，当晚被定时任务重新设置为无效duanmenrun  20171102
				User user=userService.findUserById(record.getModifyUserId());
				CustomerFolowup followupRecord = new CustomerFolowup();
				followupRecord.setCustomer(customer);
				followupRecord.setRemark("无效客户重新激活，添加系统跟进记录");
				followupRecord.setCreateTime(DateTools.getCurrentDateTime());
				followupRecord.setCreateUser(user);
				followupRecord.setAppointmentType(AppointmentType.APPOINTMENT_REACTIVATE);
				customerFolowupDao.save(followupRecord);
			}
			customerService.updateCustomer(customer);
			//分配客户
			//学管师 不用分配客户xiaojinwang  20170301
			if(StringUtils.isNotBlank(customerVo.getDeliverTarget())&& isZXS){//是咨询师才真正分配 20170325新需求
				customerVo.setFromTransfer(true);
				Response response =customerService.allocateCustomer(customerVo, customerId, record.getModifyUserId(), "转介绍审核重新分配咨询师",CustomerEventType.CHANGE_COUNSELOR);
				if(response.getResultCode()!=0){
					//throw new ApplicationException(response.getResultMessage());
					return response;
				}
	    	}
           
            
            String description = "转介绍通过，客户被分配给新的咨询师";
			if(auditRecord.getCustomerStatus()==0){
			
				//当前分配对象被抢   将要分配的对象和原来的跟进人不是同一个人才被抢
				
				if(deliverTarget!=null && StringUtils.isNotBlank(customerVo.getDeliverTarget())
						&& !deliverTarget.equals(customerVo.getDeliverTarget())){
					User referUser_now =  userService.loadUserById(deliverTarget);
					if(referUser_now!=null){
						GainCustomer gainCustomer_now = new GainCustomer();
						gainCustomer_now.setCusId(customer);
						gainCustomer_now.setDeliverFrom(deliverTarget);
						gainCustomer_now.setDeliverTarget(customerVo.getDeliverTarget());
						gainCustomer_now.setReason("客户标记为转介绍资源并被重新分配");
						gainCustomer_now.setCreateUser(record.getModifyUserId());
						gainCustomer_now.setCreateTime(record.getModifyTime());		                
		                gainCustomerDao.save(gainCustomer_now);
		                
		    			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
		    			dynamicStatus.setDynamicStatusType(CustomerEventType.GAINCUSTOMER);
		    			dynamicStatus.setDescription(description);
		    			if(customer.getResEntrance()!=null){
		    			   dynamicStatus.setResEntrance(customer.getResEntrance());
		    			}
		    			dynamicStatus.setStatusNum(1);
		    			dynamicStatus.setTableName("gain_customer");
		    			dynamicStatus.setTableId(gainCustomer_now.getId());
		    			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
		    			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, referUser_now);	
		                
		                //customerEventService.saveCustomerDynamicStatus(customerId, CustomerEventType.GAINCUSTOMER, description, referUser_now, "");
					}					
				}                               
			}
			
		}
		if (CustomerAuditStatus.NOT_PASS.getValue().equals(auditStatus)) {
			//当审核不通过的时候 尝试去还原来的资源入口
			//String customerId = auditRecord.getCustomerId();
			//Customer customer =customerService.findById(customerId);
			auditRecord.setResEntrance(auditRecord.getPreEntrance());
			if(auditRecord.getCustomerStatus()==0){
				//老客户
				if(auditRecord.getPreEntrance()!=null){
					String pre = auditRecord.getPreEntrance().getValue();
					//如果最新资源入口是转介绍 则还原为preEntrance
					if(pre.equals(ResEntranceType.TRANSFER.getValue())){
						customer.setResEntrance(auditRecord.getPreEntrance());
					}else{
						customer.setResEntrance(auditRecord.getPreEntrance());
						customer.setPreEntrance(auditRecord.getPreEntrance());
					}
				}
				//20171023 xiaojinwang
				if(StringUtil.isNotBlank(customer.getLastAppointId())){
					customer.setCusType(new DataDict(customer.getLastAppointId()));
				}
                if(StringUtil.isNotBlank(customer.getLastFollowId())){
                	customer.setCusOrg(new DataDict(customer.getLastFollowId()));
                }

				
				
			}else{
				customer.setResEntrance(null);
				customer.setPreEntrance(null);
			}
			//新客户 资源入口不生效 不用还原
			
			
			customerService.updateCustomer(customer);
			//TODO 如果审核不通过则发送通知  
			
		}
		
		Customer c =customerService.findById(auditRecord.getCustomerId());
		CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
		dynamicStatus.setDynamicStatusType(CustomerEventType.TRANSFER_AUDIT);
		dynamicStatus.setDescription("转介绍审核");
		if(c.getResEntrance()!=null){
		   dynamicStatus.setResEntrance(c.getResEntrance());
		}
		dynamicStatus.setStatusNum(1);
		dynamicStatus.setTableName("transfer_introduce_customer");
		dynamicStatus.setTableId(tCustomerRecordVo.getId());
		dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
		customerEventService.addCustomerDynameicStatus(c, dynamicStatus, refUser);	
		
		
		transferCustomerDao.updateTransferCustomerRecord(auditRecord);
		transferCustomerDao.updateTransferCustomerRecord(childRecord);
		return new Response(0, "audit_transfer_customer_record");
	}

	@Override
	public Response saveTransferCustomerAuditRecord(TransferCustomerRecordVo tCustomerRecordVo) {
		
		if(tCustomerRecordVo.getCustomerStatus()==0){
			// 老客户 设置资源入口
			Customer customer_temp = customerService.loadCustomerByContact(tCustomerRecordVo.getContact());	
			//暂存老客户的资源来源、来源细分 20171023 xiaojinwang
			if(customer_temp.getCusType()!=null){
				customer_temp.setLastAppointId(customer_temp.getCusType().getValue());
			}
			if(customer_temp.getCusOrg()!=null){
				customer_temp.setLastFollowId(customer_temp.getCusOrg().getValue());
			}
			
			
			DataDict resEntrance = null;
			DataDict preEntrance = null;
			if(customer_temp!=null){
				resEntrance = customer_temp.getResEntrance();
				preEntrance = customer_temp.getPreEntrance();			
			}
			if(resEntrance!=null){
				tCustomerRecordVo.setResEntranceId(resEntrance.getId());
				tCustomerRecordVo.setResEntranceName(resEntrance.getName());
			}
			if(preEntrance!=null){
				tCustomerRecordVo.setPreEntranceId(preEntrance.getId());
				tCustomerRecordVo.setPreEntranceName(preEntrance.getName());				
			}
		}
		
		TransferCustomerRecord record = HibernateUtils.voObjectMapping(tCustomerRecordVo, TransferCustomerRecord.class);

		
		
		
		transferCustomerDao.saveTransferCustomerRecord(record);		
		return new Response(0, record.getId());
	}

	@Override
	public List<TransferCustomerRecord> getTransferRecordByContactAndStuName(String contact,String studentName) {
		Map<String, Object> params = new HashMap<>();
		String hql = "from TransferCustomerRecord where contact = :contact and studentName = :studentName ";
		params.put("contact", contact);
		params.put("studentName", studentName);
		return transferCustomerDao.findAllByHQL(hql, params);
	}

	@Override
	public TransferCustomerRecordVo loadTransferCustomerResult(String id) {
		TransferCustomerRecord auditRecord =transferCustomerDao.findById(id);
		TransferCustomerRecordVo auditRecordVo =  HibernateUtils.voObjectMapping(auditRecord, TransferCustomerRecordVo.class);
		//分配对象的名字
		String customerId = auditRecord.getCustomerId();
//		String distributeTarget = auditRecord.getDistributeTarget();
//		if(distributeTarget!=null){
//			User distributeTargetUser=userService.loadUserById(distributeTarget);
//			auditRecordVo.setDistributeTargetName(distributeTargetUser.getName());
//		}
		List<AuditRecordVo> auditRecordVos= this.getAuditRecordById(id);
		if(auditRecordVos!=null && auditRecordVos.size()>0){
			AuditRecordVo  audit = auditRecordVos.get(auditRecordVos.size() -1);
			auditRecordVo.setRemark(audit.getAuditRemark());
		}
		
		String createUser = auditRecord.getCreateUserId();
		if(StringUtils.isNotBlank(createUser)) {
			User user=userService.loadUserById(createUser);
			auditRecordVo.setCreateUserName(user.getName());
		}
		
		
		if(customerId!=null){
			Customer customer = customerService.findById(customerId);
			if(customer!=null){
				String deliverTarget = customer.getDeliverTarget();
				
				if(deliverTarget!=null){
					//查询姓名+职位
					if(CustomerDeliverType.PERSONAL_POOL == customer.getDeliverType()){
						User user=userService.loadUserById(deliverTarget);
						auditRecordVo.setDeliverTarget(deliverTarget);
						auditRecordVo.setDeliverTargetName(user!=null?user.getName():"");
					}else if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL == customer.getDeliverType()) {
						Organization o = organizationDao.findById(deliverTarget);
						auditRecordVo.setDeliverTarget(deliverTarget);
						auditRecordVo.setDeliverTargetName(o!=null?o.getName():"");
					}
					
				}
			}
		}
		
		//获取审核时间
		String auditTime = auditRecord.getAuditTime();
		Boolean isSameDate = false;
		if(StringUtil.isNotBlank(auditTime)){
			Date date = DateTools.stringConversDate(auditTime, "yyyy-MM-dd HH:mm:ss");
			isSameDate = DateTools.isSameDate(new Date(), date);
		}
		auditRecordVo.setIsSameDate(isSameDate);
        
			
		return auditRecordVo;
	}

	@Override
	public List<AuditRecordVo> getAuditRecordById(String parentId) {
		StringBuilder  sql = new StringBuilder();
		Map<String, Object> params = new HashMap();
		sql.append("select tic.remark as remark,tic.audit_status as auditStatus,tic.create_time as createTime ,u.`NAME`  as createUserName ");
		sql.append("from transfer_introduce_customer tic left join `user` u on tic.create_user_id = u.USER_ID where tic.parent_id = :parentId ");
		params.put("parentId", parentId);
		List<Map<Object, Object>> list = transferCustomerDao.findMapBySql(sql.toString(), params);
		List<Map<String, String>> list1 = new ArrayList<>();
		for (Map<Object, Object> map :list){
			Map<String, String> a = (Map)map;
			list1.add(a);
		}
		if(list!=null && list.size()>0){
			List<AuditRecordVo> result = new ArrayList<AuditRecordVo>();
			AuditRecordVo auditRecordVo = null;
			for(Map<String, String> tRecord:list1){
				auditRecordVo = new AuditRecordVo();
				auditRecordVo.setAuditDate(tRecord.get("createTime"));
				if(tRecord.get("auditStatus")!=null){
					String audit = tRecord.get("auditStatus");
					CustomerAuditStatus sAuditStatus=CustomerAuditStatus.valueOf(audit);
					auditRecordVo.setAuditStatus(sAuditStatus!=null?sAuditStatus.getName():"");
				}				
				auditRecordVo.setAuditRemark(tRecord.get("remark")!=null?tRecord.get("remark"):"");
				auditRecordVo.setAuditUserName(tRecord.get("createUserName"));
				result.add(auditRecordVo);
			}			
			return result;
			
		}else{
			return null;
		}
		
	}
	
	
	@Override
	public DataPackage getTransferAuditRecords(TransferCustomerRecordVo tCustomerRecordVo, DataPackage dp) {
		
		//当前登录者的UserId 
		String currentUserId = userService.getCurrentLoginUser().getUserId();

		Map<String, Object> params = Maps.newHashMap();

		StringBuilder transferSql=new StringBuilder(256);
		transferSql.append("select tra.*,d.`NAME` as restranceName from transfer_introduce_customer tra ");
		transferSql.append(" left join customer c on tra.customer_id = c.ID ");
		transferSql.append(" left join data_dict d on tra.res_entrance = d.ID where tra.parent_id is null ");
		
		//查询条件 姓名 联系方式  介绍人 类型 审核状态  资源入口
		if(StringUtils.isNotBlank(tCustomerRecordVo.getCustomerName())){
			transferSql.append("and tra.customer_name like :customerName ");
			params.put("customerName", "%"+tCustomerRecordVo.getCustomerName()+"%");
		}
		if(StringUtils.isNotBlank(tCustomerRecordVo.getContact())){
			transferSql.append("and tra.contact = :contact ");
			params.put("contact", tCustomerRecordVo.getContact());
		}
		if(StringUtils.isNotBlank(tCustomerRecordVo.getIntroducer())){
			transferSql.append("and tra.introducer like :introducer ");
			params.put("introducer", "%"+tCustomerRecordVo.getIntroducer()+"%");
		}
		if(StringUtils.isNotBlank(tCustomerRecordVo.getCusOrg())){
			transferSql.append("and tra.introducer_type = :cusOrg ");
			params.put("cusOrg", tCustomerRecordVo.getCusOrg());
		}
		if(tCustomerRecordVo.getAuditStatus()!=null){
			if(StringUtils.isNotBlank(tCustomerRecordVo.getAuditStatus().getValue())){
				transferSql.append("and tra.audit_status = :auditStatus ");
				params.put("auditStatus", tCustomerRecordVo.getAuditStatus().getValue());
			}			
		}
		// 资源入口
		if (StringUtils.isNotBlank(tCustomerRecordVo.getResEntranceId())) {
			transferSql.append(" and tra.res_entrance= :resEntranceId ");
			params.put("resEntranceId", tCustomerRecordVo.getResEntranceId());
		}

		//配置 权限  只能查看自己的提交的记录+获取分配给自己的人看到
		//
		transferSql.append(" and (tra.create_user_id = :currentUserId or tra.distribute_target = :currentUserId ) ");
        params.put("currentUserId", currentUserId);
        
        
        //会影响到10人课堂的业务，暂时解除
//        //增加权限限制
//		Organization organization = userService.getBelongCampusByUserId(currentUserId);
//		String orgLevel = organization.getOrgLevel();
//		params.put("orgLevel",  orgLevel+"%");
//		transferSql.append(" and (c.BL_SCHOOL in (select id from organization WHERE orgLevel LIKE :orgLevel ))");
			
        StringBuilder countSql = new  StringBuilder(" select count(*) from ( " + transferSql.toString() + " ) countall ");
//		if (StringUtils.isNotBlank(dp.getSord())
//				&& StringUtils.isNotBlank(dp.getSidx())) {
//			transferSql.append(" order by tra."+dp.getSidx()+" "+dp.getSord());
//		}
		transferSql.append(" order by tra.create_time  desc ");
	    //下面方法查出来的属性是数据库的字段属性
	    // dp = transferCustomerDao.findMapPageBySQL(transferSql.toString(), dp);
	
        
	    List<Map<Object,Object>> list=transferCustomerDao.findMapOfPageBySql(transferSql.toString(), dp, params);

		List<Map<String,String>> list1 = new ArrayList<>();

		for (Map<Object,Object>  map: list){
			Map<String,String> a = (Map)map;
			list1.add(a);
		}


	    dp.setRowCount(transferCustomerDao.findCountSql(countSql.toString(), params));
	    List<TransferCustomerRecordVo> reList= new ArrayList<TransferCustomerRecordVo>();
	    TransferCustomerRecordVo recordVo = null ;
	    for(Map<String, String> map:list1){
	    	recordVo = new TransferCustomerRecordVo();
	    	if(map.get("id")!=null) recordVo.setId(map.get("id"));
	    	if(map.get("customer_id")!=null) recordVo.setCustomerId(map.get("customer_id"));
	    	if(map.get("customer_name")!=null)recordVo.setCustomerName(map.get("customer_name"));
	    	if(map.get("contact")!=null)recordVo.setContact(map.get("contact"));

			recordVo.setResEntranceId(map.get("res_entrance"));
			recordVo.setResEntranceName(map.get("restranceName")!=null?map.get("restranceName"):"");
	
	    	if(map.get("introducer")!=null)recordVo.setIntroducer(map.get("introducer"));
	    	if(map.get("introducer_type")!=null)recordVo.setCusOrg(map.get("introducer_type"));//cus_org
	    	if(map.get("remark")!=null)recordVo.setRemark(map.get("remark"));
	    	if(StringUtils.isBlank(recordVo.getRemark())){
	    		recordVo.setRemark("-");
	    	}
	    	if(map.get("audit_status")!=null)recordVo.setAuditStatus(CustomerAuditStatus.valueOf(map.get("audit_status")));
	    	Boolean isStudyManager = false;
	    	Boolean isXGS = false;
	    	if(map.get("create_user_id")!=null){
	    		String distribute_target = map.get("distribute_target");
	    		if(StringUtil.isBlank(distribute_target)){
	    			isStudyManager = false;
	    		}else{
	    			//不需要主职位 只需要是有职位是学管师 即可
	    			List<String> jobSigns = userService.getUserAllRoleSign(distribute_target);
	    			if(jobSigns.size()>0){
	    				for(String jobSign:jobSigns){
	    					if(jobSign.equals(RoleSign.XGS.getValue().toLowerCase())||jobSign.equals(RoleSign.XGZG.getValue().toLowerCase())){
	    						isXGS = true;
	    						break;
	    					}
	    				}
	    			}
	    		}
	    		
	    		String createUserId = map.get("create_user_id");
	    		List<Role> roles = userService.getRoleByUserId(createUserId);
	    		//如果是咨询师或者咨询主管则映射为CONSULTOR 这两种角色功能一样
	    		//如果是学管师或者学管主管则映射为STUDY_MANAGER 这两种角色功能一样  xiaojinwang 20170629
	    		for(Role role: roles){
	    			if(role.getRoleCode() == RoleCode.CONSULTOR||role.getRoleCode() == RoleCode.CONSULTOR_DIRECTOR){
	    				recordVo.setRoleCode(RoleCode.CONSULTOR.getValue());
	    				break;
	    			}else if(role.getRoleCode()==RoleCode.STUDY_MANAGER||role.getRoleCode()==RoleCode.STUDY_MANAGER_HEAD){
	    				recordVo.setRoleCode(RoleCode.STUDY_MANAGER.getValue());
	    				break;
	    			}else if(role.getRoleCode()==RoleCode.TEATCHER){
	    				recordVo.setRoleCode(RoleCode.TEATCHER.getValue());
	    				break;
	    			}else {
	    				recordVo.setRoleCode(RoleCode.CONSULTOR.getValue());
	    				//break;
	    			}
	    		}
				String roleCode = recordVo.getRoleCode();
				if(isXGS && (roleCode.equals(RoleCode.TEATCHER.getValue())||roleCode.equals(RoleCode.STUDY_MANAGER.getValue()))){
					isStudyManager = true ;
				}    		
	    	}
	    	recordVo.setIsStudyManager(isStudyManager);
	    	
	    	
	    	reList.add(recordVo);
	    }
	    dp.setDatas(reList);
		return dp;
	}





	@Override
	public List getTransferTargetByCampus(String showBelong, String[] job, String transferId) {
		
	    //根据id获取转介绍登记记录
		TransferCustomerRecord auditRecord =transferCustomerDao.findById(transferId);
		String recordCreateUserId = auditRecord.getCreateUserId();

		if(StringUtil.isNotBlank(showBelong) && showBelong.equals("transfer_introduce")){
			Map<String, Object> params = Maps.newHashMap();
			String roleCode = job[0];
			int flag = 0;
			RoleSign roleSign  = RoleSign.ZXS;
			RoleSign roleSign2 = RoleSign.XGS;
			RoleSign roleSign3 = RoleSign.ZXZG;
			RoleSign roleSign4 = RoleSign.XGZG;//xiaojinwang 20170629
			List<RoleSign> list = new ArrayList<>();
			
			//xiaojinwang 20170629 新需求将增加 学管师和学管师主管  咨询师和咨询主管 
			//前端传来 CONSULTOR 代表咨询师和咨询主管
			//前端传来STUDY_MANAGER 代表学管师和学管主管			
			if(roleCode.equals(RoleCode.CONSULTOR.getValue())){
				//本校咨询师+咨询主管
				flag =1;
				roleSign = RoleSign.ZXS;
				list.add(roleSign);
				list.add(RoleSign.ZXZG);
			}else if(roleCode.equals(RoleCode.STUDY_MANAGER.getValue())){
				//
				flag =0;
				roleSign = RoleSign.ZXS;		
				//可以分配咨询师或者咨询主管  学管师或者学管主管  因为前端传来STUDY_MANAGER 代表学管师和学管主管
			}else if(roleCode.equals(RoleCode.TEATCHER.getValue())){
				//学管师+咨询师+咨询主管
				flag =2;
				list.add(RoleSign.XGS);
				list.add(RoleSign.XGZG);
				list.add(RoleSign.ZXS);
				list.add(RoleSign.ZXZG);
			}
			if(flag!=0){
				StringBuffer jobNameBuffer = new StringBuffer();
				for(RoleSign rSign:list){
					jobNameBuffer.append("'"+rSign.getValue().toLowerCase()+"',");
				}
				StringBuffer sql = new StringBuffer();
				sql.append(
						"select u.USER_ID userId,CONCAT(u.NAME,'（',uj.job_name,'）') userName,CONCAT(o.id,'') orgId,o.name orgName from user_dept_job udj");
				sql.append(" left join user u on u.user_id=udj.USER_ID");
				sql.append(" left join organization o on o.id=udj.DEPT_ID");
				sql.append(" left join user_job uj on uj.ID=udj.JOB_ID");
				sql.append(" where u.ENABLE_FLAG='0' ");
				sql.append(" and ( uj.JOB_SIGN in("+jobNameBuffer.substring(0, jobNameBuffer.length()-1)+")");
				sql.append(" or (u.user_id ='"+recordCreateUserId+"' and uj.JOB_SIGN in("+jobNameBuffer.substring(0, jobNameBuffer.length()-1)+") )) ");
				Organization organization = userService.getBelongCampus();
				if (organization != null) {
					sql.append(" and udj.dept_id in (select id from organization where orgLevel like '"
							+ organization.getOrgLevel() + "%')  ");
				}
				
				sql.append(" order by o.orgLevel,o.orgOrder;");
				return organizationDao.findMapBySql(sql.toString(),params);
			}else{
				StringBuffer sql = new StringBuffer();
				sql.append(
						"select u.USER_ID userId,CONCAT(u.NAME,'（',uj.job_name,'）') userName,CONCAT(o.id,'') orgId,o.name orgName from user_dept_job udj");
				sql.append(" left join user u on u.user_id=udj.USER_ID");
				sql.append(" left join organization o on o.id=udj.DEPT_ID");
				sql.append(" left join user_job uj on uj.ID=udj.JOB_ID");
				sql.append(" where u.ENABLE_FLAG='0' ");			
				sql.append(" and ( uj.JOB_SIGN ='" + roleSign.getValue().toLowerCase() + "' or uj.JOB_SIGN ='" + roleSign3.getValue().toLowerCase() + "' ");
				sql.append(" or (u.user_id ='"+recordCreateUserId+"' and uj.JOB_SIGN='"+roleSign2.getValue().toLowerCase()+"' ) ");
				sql.append(" or (u.user_id ='"+recordCreateUserId+"' and uj.JOB_SIGN='"+roleSign4.getValue().toLowerCase()+"' )) ");
				Organization organization = userService.getBelongCampus();
				if (organization != null) {
					sql.append(" and udj.dept_id in (select id from organization where orgLevel like '"
							+ organization.getOrgLevel() + "%')  ");
				}
				
				sql.append(" order by o.orgLevel,o.orgOrder;");
				return organizationDao.findMapBySql(sql.toString(),params);
			}
		}
			return null;
	}





	@Override
	public Response allocateTransferCustomer(TransferCustomerRecordVo tCustomerRecordVo) {
		Response response = new Response();
		TransferCustomerRecord auditRecord =transferCustomerDao.findById(tCustomerRecordVo.getId());
		if(auditRecord==null){
			response.setResultCode(-1);
			response.setResultMessage("审核记录不存在，分配失败");
			return response;
		}
		String deliverTarget = tCustomerRecordVo.getDeliverTarget();
		if(StringUtil.isBlank(deliverTarget)){
			response.setResultCode(-1);
			response.setResultMessage("分配对象不能为空");
			return response;
		}
		User user = userService.findUserById(deliverTarget);
		if(user==null){
			response.setResultCode(-1);
			response.setResultMessage("分配对象不存在，分配失败");
			return response;
		}
		String customerId = auditRecord.getCustomerId();
		Customer customer =customerService.findById(customerId);
		if(customer.getDealStatus() == CustomerDealStatus.FOLLOWING || customer.getDealStatus() == CustomerDealStatus.SIGNEUP) {
	    	response.setResultCode(-1);
	    	response.setResultMessage("客户处于"+customer.getDealStatus().getName()+"状态，不允许重新分配！");
	    	return response;
		}
		CustomerVo customerVo = new CustomerVo();
		customerVo.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
		customerVo.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
		customerVo.setDeliverTarget(deliverTarget);
		customerVo.setRemark("转介绍不通过分配客户");
		User currentLoginUser = userService.getCurrentLoginUser();
		response = customerService.allocateCustomer(customerVo, customerId, currentLoginUser.getUserId(),customerVo.getRemark(),CustomerEventType.CHANGE_COUNSELOR );
		
		auditRecord.setDistributeTarget(deliverTarget);
		auditRecord.setDistributeTargetName(user.getName());
		auditRecord.setModifyTime(tCustomerRecordVo.getModifyTime());
		auditRecord.setModifyUserId(tCustomerRecordVo.getModifyUserId());
		
		return response;
	}





	@Override
	public Map<String, Object> checkTransferCustomer(Customer customer, String stuName) throws Exception{
		Map<String, Object> result = Maps.newHashMap();
		//传入老客户Customer 和学生名字  返回 是否老孩子  是否已经签单 是否有跟进过 是否在审计记录里面 以及学生的id
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId",customer.getId());
		params.put("stuName", stuName);
		String  sql =" select csr.* from customer_student_relation csr ,student s where csr.STUDENT_ID = s.ID and csr.CUSTOMER_ID = :customerId and s.`NAME` = :stuName ";
		List<CustomerStudent> list = customerStudentDao.findBySql(sql, params);
		if(list!=null && list.size()>0){
			//说明这个孩子是已有的
			result.put("isOldChild", true);
			String stuSql = "select s.* from customer_student_relation csr ,student s where csr.STUDENT_ID = s.ID and csr.CUSTOMER_ID = :customerId and s.`NAME` = :stuName ";
			List<Student> students = studentDao.findBySql(stuSql, params);
			if(students!=null && students.size()>0){
				Boolean isSign = false;				
				Boolean isFollowup = false;
				Boolean isTransfer = false;
				Map<String, Object> param = null;
				String studentId = students.get(0).getId();
				for(Student student:students){
					param = Maps.newHashMap();
					param.put("stuId", student.getId());
					param.put("cusId", customer.getId());
					String conSql = "SELECT * from contract where STUDENT_ID = :stuId and CUSTOMER_ID = :cusId";
					List<Contract> contracts = contractDao.findBySql(conSql, param);
					if(contracts!=null && contracts.size()>0){
						isSign = true;
					}
					//是否有跟进过
					String followUpSql = "SELECT * from customer_folowup cf where cf.APPOINTMENT_TYPE ='"+AppointmentType.FOLLOW_UP.getValue()+
							"' and cf.CUSTOMER_ID = :cusId and cf.FOLLOW_STUDENT_ID = :stuId ";
					List<CustomerFolowup> folowups = customerFolowupDao.findBySql(followUpSql, param);
					if(followUpSql!=null && folowups.size()>0){
						isFollowup = true;
					}					
					String transferSql = "select * from transfer_introduce_customer where customer_id = :cusId and student_id = :stuId ";
					List<TransferCustomerRecord> tRecords = transferCustomerDao.findBySql(transferSql, param);
					if(tRecords!=null && tRecords.size()>0){
						isTransfer = true;
					}
				}
				result.put("isSign", isSign);
				result.put("isTransfer", isTransfer);
				result.put("isFollowup", isFollowup);
				result.put("studentId", studentId);				
			}else{
				throw new ApplicationException("出现脏数据，请联系管理员");
			}

		}else{
			result.put("isOldChild", false);
			result.put("isSign", false);
			result.put("isTransfer", false);
			result.put("isFollowup", false);
			result.put("studentId", null);
		}
		return result;
	}

}
