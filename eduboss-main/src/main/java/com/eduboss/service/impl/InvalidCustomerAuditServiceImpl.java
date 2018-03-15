package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.AppointmentType;
import com.eduboss.common.CustomerAuditStatus;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.CustomerEventType;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.RoleCode;
import com.eduboss.common.RoleSign;
import com.eduboss.common.VisitType;
import com.eduboss.dao.InvalidCustomerAuditDao;
import com.eduboss.dao.UserDeptJobDao;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.CustomerFolowup;
import com.eduboss.domain.InvalidCustomerRecord;
import com.eduboss.domain.Organization;
import com.eduboss.domain.ResourcePool;
import com.eduboss.domain.Role;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AuditRecordVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.InvalidCustomerRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.service.CustomerEventService;
import com.eduboss.service.CustomerFolowupService;
import com.eduboss.service.CustomerService;
import com.eduboss.service.InvalidCustomerAuditService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.ResourcePoolService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;


@Service("com.eduboss.service.InvalidCustomerAuditService")
public class InvalidCustomerAuditServiceImpl implements InvalidCustomerAuditService{

	private final static Logger log = Logger.getLogger(InvalidCustomerAuditServiceImpl.class);
	@Autowired
	private  InvalidCustomerAuditDao invalidCustomerAuditDao;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerFolowupService customerFolowupService;
	

	
	@Autowired
	private CustomerEventService customerEventService;
	
	@Autowired
	private UserDeptJobDao userDeptJobDao;
	
	@Autowired
	private ResourcePoolService resourcePoolService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Override
	public Long saveInvalidCustomerRecord(InvalidCustomerRecord record) {
		log.debug("service层保存无效客户审核记录");
		return invalidCustomerAuditDao.saveInvalidCustomerRecord(record);	
	}

	
	//CustomerVo 应该至少封装 deliverType deliverTarget dealStatus  res_Entrance 
	@Override
	public Response auditInvalidCustomer(InvalidCustomerRecordVo recordVo,CustomerVo customerVo) {
		// 根据 id获取之前提交过的无效客户的审核记录
		Response response = new Response();
		
		InvalidCustomerRecord record = invalidCustomerAuditDao.getRecordById(recordVo.getId());
		if (record == null){
			response.setResultCode(-1);
			response.setResultMessage("无法找到审核记录");
			return response;
		}
			
		CustomerAuditStatus auditStatus = record.getAuditStatus();
		String customerId = record.getCustomerId();
		Customer cusVo = customerService.findById(customerId);
	    CustomerDeliverType deliverType = cusVo.getDeliverType();
		String old_deliverTarget = record.getCreateUserId();
		
		
		
		if (CustomerAuditStatus.NOT_PASS==recordVo.getAuditStatus()) {
			if (customerVo.getDeliverTarget() != null && (customerVo.getDeliverTarget().equals(old_deliverTarget)
					|| customerVo.getDeliverTarget().equals(cusVo.getDeliverTarget()))) {
				response.setResultCode(-1);
				response.setResultMessage("分配客户对象不能为空或者不能跟原来对象一样");
				return response;
			}
		}
		
		if(CustomerAuditStatus.PASS==recordVo.getAuditStatus()){
			if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL==cusVo.getDeliverType()){
				response.setResultCode(-1);
				response.setResultMessage("该客户已经被释放到资源池中，请勿重复审核");
				return response;				
			}else{
				if(cusVo.getDeliverTarget()!=null){
					User currentTarget = userService.loadUserById(cusVo.getDeliverTarget());
					if(currentTarget==null){
						response.setResultCode(-1);
						response.setResultMessage("该客户已经被释放到资源池中，请勿重复审核");
						return response;						
					}
				}				
			}

		}
		
		User user = userService.getCurrentLoginUser();
		
		if (auditStatus!=CustomerAuditStatus.TOBE_AUDIT) {
			if (cusVo != null) {
				String deliverTarget = cusVo.getDeliverTarget();
				// 说明是再次审核
				record.setAuditStatus(recordVo.getAuditStatus());// 审核结果 通过或者不通过   
				record.setRemark(recordVo.getRemark());// 审核时候的备注内容
				record.setModifyTime(recordVo.getModifyTime());// 当前登录者的时间
				record.setModifyUserId(recordVo.getModifyUserId());
				if (auditStatus == CustomerAuditStatus.NOT_PASS) {
					if (CustomerAuditStatus.PASS==recordVo.getAuditStatus()) {
						
						CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
						dynamicStatus.setDynamicStatusType(CustomerEventType.INVALID);
						dynamicStatus.setDescription("客户无效审核通过");
						if(cusVo.getResEntrance()!=null){
						    dynamicStatus.setResEntrance(cusVo.getResEntrance());
						}
						dynamicStatus.setStatusNum(1);
						dynamicStatus.setTableName("invalid_customer_audit");
						dynamicStatus.setTableId(recordVo.getId().toString());
						dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
						customerEventService.addCustomerDynameicStatus(cusVo, dynamicStatus, user);
						
						
						
						
						
						cusVo.setDealStatus(CustomerDealStatus.INVALID);
						//原来不通过 现在通过
						//如果审核通过同步至 集团网络 或者分公司 资源池
						ResourcePool resourcePool = null;
						Boolean isWLZY = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_SPEC);
						Boolean isWLZG = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_MANAGER);
//						String roleSign = userService.getUserRoleSign(deliverTarget);
//						List<String> roleSignList = userService.getUserRoleSignFromRole(deliverTarget);
//						if(roleSignList==null) roleSignList = new ArrayList<>();
//						if(isWLZY||isWLZG||RoleSign.WLZY.getValue().equalsIgnoreCase(roleSign)
//								||(roleSignList!=null && roleSignList.contains(RoleSign.WLZY.getValue().toLowerCase()))){
						if(isWLZY||isWLZG){
							resourcePool =resourcePoolService.getBelongBranchResourcePool(deliverTarget, OrganizationType.GROUNP.getValue());
						}else{
							resourcePool =resourcePoolService.getBelongBranchResourcePool(deliverTarget, OrganizationType.BRENCH.getValue());
						}
						if(resourcePool!=null){
							cusVo.setDeliverTarget(resourcePool.getOrganizationId());
							cusVo.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
							//cusVo.setDealStatus(CustomerDealStatus.INVALID);
							cusVo.setDeliverTime(DateTools.getCurrentDateTime());
						}
						if(!old_deliverTarget.equals(deliverTarget)&& deliverType ==CustomerDeliverType.PERSONAL_POOL ){
							//添加无效客户记录
							InvalidCustomerRecord childRecord = new InvalidCustomerRecord();
							childRecord.setParentId(-1L);
							childRecord.setAuditStatus(CustomerAuditStatus.PASS);// 审核结果 通过
							childRecord.setRemark(recordVo.getRemark());// 审核时候的备注内容
							childRecord.setCreateTime(DateTools.getCurrentDateTime());
							childRecord.setCreateUserId(deliverTarget);
							childRecord.setCustomerId(customerId);
							invalidCustomerAuditDao.updateInvalidCustomerRecord(childRecord);
						}
						
						
						
						// 通过 将客户置为无效 如果原来是不通过 则不修改跟进人 而是改审核记录而已
						record.setCurrentStatus(CustomerDealStatus.INVALID);
						// 设置无效客户原因
						cusVo.setInvalidReason(record.getRemark());
						//cusVo.setDealStatus(CustomerDealStatus.INVALID);
						customerService.updateCustomer(cusVo);
						// refer不是当前审核者，而是无效标记的创建者
						//User referUser = userService.loadUserById(record.getCreateUserId());
						
						

						
						
//						customerEventService.saveCustomerDynamicStatus(customerId, CustomerEventType.INVALID,
//								"客户无效审核通过", referUser, null, 1);
						

					}else if(CustomerAuditStatus.NOT_PASS==recordVo.getAuditStatus()){
						record.setCurrentStatus(record.getPreviousStatus());
						// refer不是当前审核者，而是无效标记的创建者
					}
				}
				if (auditStatus == CustomerAuditStatus.PASS) {
					if (CustomerAuditStatus.NOT_PASS==recordVo.getAuditStatus()) {
						//此处原来是通过 现在不通过
						//如果是不通过   如果前面是审核通过则分配  如果是之前是审核不通过则只能单单修改审核记录
						//auditStatus前面的审核结果是通过 
							record.setCurrentStatus(record.getPreviousStatus());
							record.setRemark(recordVo.getRemark());// 审核时候的备注内容
							record.setModifyTime(recordVo.getModifyTime());// 当前登录者的时间
							record.setModifyUserId(recordVo.getModifyUserId());							
							// 重新分配客户 封装的dealStatus 为stay_follow
							Response responseAllocate = customerService.allocateCustomer(customerVo, customerId, recordVo.getModifyUserId(),
									"无效审核不通过重新分配跟进对象",CustomerEventType.CHANGE_OTHER);
							if(responseAllocate.getResultCode()!=0){
								return responseAllocate;
							}
							//如果无效审核中，如果是外呼或者网络专业更换需要 update before_deliverTarget字段
							Boolean isWLZY = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_SPEC);
							Boolean isWLZG = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_MANAGER);
							Boolean isWHZY = userService.isUserRoleCode(deliverTarget, RoleCode.OUTCALL_SPEC);
							Boolean isWHZG = userService.isUserRoleCode(deliverTarget, RoleCode.OUTCALL_MANAGER);
							Boolean isBWHZG = userService.isUserRoleCode(deliverTarget, RoleCode.BRANCH_OUTCALL_MANAGER);
												
//							String roleSign = userService.getUserRoleSign(deliverTarget);
//							List<String> roleSignList = userService.getUserRoleSignFromRole(deliverTarget);
//							if(roleSignList==null) roleSignList = new ArrayList<>();
//							if(RoleSign.WHZY.getValue().equalsIgnoreCase(roleSign)||RoleSign.WLZY.getValue().equalsIgnoreCase(roleSign)
//									||(roleSignList!=null &&roleSignList.contains(RoleSign.WHZY.getValue().toLowerCase()))
//									||(roleSignList!=null &&roleSignList.contains(RoleSign.WLZY.getValue().toLowerCase()))){
							if(isWLZY||isWLZG||isWHZY||isWHZG||isBWHZG){
								cusVo.setBeforeDeliverTarget(customerVo.getDeliverTarget());
								cusVo.setBeforeDeliverTime(DateTools.getCurrentDateTime());
								customerService.updateCustomer(cusVo);
							}
							//cusVo.setDealStatus(record.getPreviousStatus());			
							//customerService.updateCustomer(cusVo);
							
					}else if(CustomerAuditStatus.PASS==recordVo.getAuditStatus()){
						
						CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
						dynamicStatus.setDynamicStatusType(CustomerEventType.INVALID);
						dynamicStatus.setDescription("客户无效审核通过");
						if(cusVo.getResEntrance()!=null){
						    dynamicStatus.setResEntrance(cusVo.getResEntrance());
						}
						dynamicStatus.setStatusNum(1);
						dynamicStatus.setTableName("invalid_customer_audit");
						dynamicStatus.setTableId(recordVo.getId().toString());
						dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
						customerEventService.addCustomerDynameicStatus(cusVo, dynamicStatus, user);
						
						
						//如果审核通过同步至 集团网络 或者分公司 资源池
						ResourcePool resourcePool = null;
//						String roleSign = userService.getUserRoleSign(deliverTarget);
//						List<String> roleSignList = userService.getUserRoleSignFromRole(deliverTarget);
						Boolean isWLZY = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_SPEC);
						Boolean isWLZG = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_MANAGER);
//						if(roleSignList==null) roleSignList = new ArrayList<>();
//						if(RoleSign.WLZY.getValue().equalsIgnoreCase(roleSign)
//								||(roleSignList!=null && roleSignList.contains(RoleSign.WLZY.getValue().toLowerCase()))){
//							resourcePool =resourcePoolService.getBelongBranchResourcePool(deliverTarget, OrganizationType.GROUNP.getValue());
						if(isWLZY||isWLZG){
							resourcePool =resourcePoolService.getBelongBranchResourcePool(deliverTarget, OrganizationType.GROUNP.getValue());
						}else{
							resourcePool =resourcePoolService.getBelongBranchResourcePool(deliverTarget, OrganizationType.BRENCH.getValue());
						}
						if(resourcePool!=null){
							cusVo.setDeliverTarget(resourcePool.getOrganizationId());
							cusVo.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
							cusVo.setDealStatus(CustomerDealStatus.INVALID);
							cusVo.setDeliverTime(DateTools.getCurrentDateTime());
						}
						if(!old_deliverTarget.equals(deliverTarget)&& deliverType ==CustomerDeliverType.PERSONAL_POOL ){
							//添加无效客户记录
							InvalidCustomerRecord childRecord = new InvalidCustomerRecord();
							childRecord.setParentId(-1L);
							childRecord.setAuditStatus(CustomerAuditStatus.PASS);// 审核结果 通过
							childRecord.setRemark(recordVo.getRemark());// 审核时候的备注内容
							childRecord.setCreateTime(DateTools.getCurrentDateTime());
							childRecord.setCreateUserId(deliverTarget);
							childRecord.setCustomerId(customerId);
							invalidCustomerAuditDao.updateInvalidCustomerRecord(childRecord);
						}
											
						record.setCurrentStatus(CustomerDealStatus.INVALID);
						cusVo.setInvalidReason(record.getRemark());
						customerService.updateCustomer(cusVo);
						//User referUser = userService.loadUserById(record.getCreateUserId());
						

												
//						customerEventService.saveCustomerDynamicStatus(customerId, CustomerEventType.INVALID,
//								"客户无效审核通过", referUser, null, 1);					
					}
				}

				
				//添加审核子记录
				InvalidCustomerRecord childRecord = new InvalidCustomerRecord();
				childRecord.setParentId(record.getId());
				childRecord.setAuditStatus(recordVo.getAuditStatus());// 审核结果 通过或者不通过
				childRecord.setRemark(recordVo.getRemark());// 审核时候的备注内容
				childRecord.setCreateTime(recordVo.getModifyTime());// 当前登录者的时间
				childRecord.setCreateUserId(recordVo.getModifyUserId());
				childRecord.setCustomerId(customerId);
				invalidCustomerAuditDao.updateInvalidCustomerRecord(childRecord);
				
				

				invalidCustomerAuditDao.updateInvalidCustomerRecord(record);
				return response;
			} else {
				response.setResultCode(-1);
				response.setResultMessage("该客户不存在，审核失败");
				return response;
			}

		} else {
            //第一次审核
			record.setAuditStatus(recordVo.getAuditStatus());// 审核结果 通过或者不通过
			record.setRemark(recordVo.getRemark());// 审核时候的备注内容
			record.setModifyTime(recordVo.getModifyTime());// 当前登录者的时间
			record.setModifyUserId(recordVo.getModifyUserId());
			// 根据审核结果来来修改被审核客户的状态
			if (cusVo != null) {
				String deliverTarget = cusVo.getDeliverTarget();
				if (CustomerAuditStatus.PASS.getValue().equals(recordVo.getAuditStatus().getValue())) {
					
					CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
					dynamicStatus.setDynamicStatusType(CustomerEventType.INVALID);
					dynamicStatus.setDescription("客户无效审核通过");
					if(cusVo.getResEntrance()!=null){
					    dynamicStatus.setResEntrance(cusVo.getResEntrance());
					}
					dynamicStatus.setStatusNum(1);
					dynamicStatus.setTableName("invalid_customer_audit");
					dynamicStatus.setTableId(recordVo.getId().toString());
					dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
					customerEventService.addCustomerDynameicStatus(cusVo, dynamicStatus, user);
										
					cusVo.setDealStatus(CustomerDealStatus.INVALID);
					
					//如果审核通过同步至 集团网络 或者分公司 资源池
					ResourcePool resourcePool = null;
					Boolean isWLZY = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_SPEC);
					Boolean isWLZG = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_MANAGER);
//					String roleSign = userService.getUserRoleSign(deliverTarget);
//					List<String> roleSignList = userService.getUserRoleSignFromRole(deliverTarget);
//					if(roleSignList==null) roleSignList = new ArrayList<>();
//					if(isWLZY||isWLZG||RoleSign.WLZY.getValue().equalsIgnoreCase(roleSign)
//							||(roleSignList!=null && roleSignList.contains(RoleSign.WLZY.getValue().toLowerCase()))){
					if(isWLZY||isWLZG){
						resourcePool =resourcePoolService.getBelongBranchResourcePool(deliverTarget, OrganizationType.GROUNP.getValue());
					}else{
						resourcePool =resourcePoolService.getBelongBranchResourcePool(deliverTarget, OrganizationType.BRENCH.getValue());
					}
					if(resourcePool!=null){
						cusVo.setDeliverTarget(resourcePool.getOrganizationId());
						cusVo.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
						//cusVo.setDealStatus(CustomerDealStatus.INVALID);
						cusVo.setDeliverTime(DateTools.getCurrentDateTime());
					}
					if(!old_deliverTarget.equals(deliverTarget)&& deliverType ==CustomerDeliverType.PERSONAL_POOL ){
						//添加无效客户记录
						InvalidCustomerRecord childRecord = new InvalidCustomerRecord();
						childRecord.setParentId(-1L);
						childRecord.setAuditStatus(CustomerAuditStatus.PASS);// 审核结果 通过
						childRecord.setRemark(recordVo.getRemark());// 审核时候的备注内容
						childRecord.setCreateTime(DateTools.getCurrentDateTime());
						childRecord.setCreateUserId(deliverTarget);
						childRecord.setCustomerId(customerId);
						invalidCustomerAuditDao.updateInvalidCustomerRecord(childRecord);
					}					
					
					// 通过审核则将客户的状态置为无效
					record.setCurrentStatus(CustomerDealStatus.INVALID);
					// 设置无效客户原因
					cusVo.setInvalidReason(record.getRemark());
					cusVo.setDealStatus(CustomerDealStatus.INVALID);
					customerService.updateCustomer(cusVo);
					// 记录的创建者是提交无效审核标记的人
					//User referUser = userService.loadUserById(record.getCreateUserId());
					
					//将客户转移至资源池  网络提交 则同步至集团网络资源池
					
					//外呼 咨询师同步至分公司资源池
					
					
					
					
				}
				if (CustomerAuditStatus.NOT_PASS.getValue().equals(recordVo.getAuditStatus().getValue())) {
					// 不通过则还原客户原来的状态，审核记录里面记录了原来的状态

					record.setCurrentStatus(record.getPreviousStatus());

					// 重新分配客户 封装的dealStatus 为stay_follow
					Response responseAllocate =customerService.allocateCustomer(customerVo, customerId, record.getModifyUserId(),
							"无效审核不通过重新分配跟进对象",CustomerEventType.CHANGE_OTHER);
					if(responseAllocate.getResultCode()!=0){
						return responseAllocate;
					}
					//如果无效审核中，如果是外呼或者网络专业更换需要 update before_deliverTarget字段
					Boolean isWLZY = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_SPEC);
					Boolean isWLZG = userService.isUserRoleCode(deliverTarget, RoleCode.NETWORK_MANAGER);
					Boolean isWHZY = userService.isUserRoleCode(deliverTarget, RoleCode.OUTCALL_SPEC);
					Boolean isWHZG = userService.isUserRoleCode(deliverTarget, RoleCode.OUTCALL_MANAGER);
					Boolean isBWHZG = userService.isUserRoleCode(deliverTarget, RoleCode.BRANCH_OUTCALL_MANAGER);
										
//					String roleSign = userService.getUserRoleSign(deliverTarget);
//					List<String> roleSignList = userService.getUserRoleSignFromRole(deliverTarget);
//					if(roleSignList==null) roleSignList = new ArrayList<>();
//					if(RoleSign.WHZY.getValue().equalsIgnoreCase(roleSign)||RoleSign.WLZY.getValue().equalsIgnoreCase(roleSign)
//							||(roleSignList!=null &&roleSignList.contains(RoleSign.WHZY.getValue().toLowerCase()))
//							||(roleSignList!=null &&roleSignList.contains(RoleSign.WLZY.getValue().toLowerCase()))){
					if(isWLZY||isWLZG||isWHZY||isWHZG||isBWHZG){
						cusVo.setBeforeDeliverTarget(customerVo.getDeliverTarget());
						cusVo.setBeforeDeliverTime(DateTools.getCurrentDateTime());
						customerService.updateCustomer(cusVo);
					}
					//cusVo.setDealStatus(record.getPreviousStatus());
					//
				}
				
				//添加审核子记录
				InvalidCustomerRecord childRecord = new InvalidCustomerRecord();
				childRecord.setParentId(record.getId());
				childRecord.setAuditStatus(recordVo.getAuditStatus());// 审核结果 通过或者不通过
				childRecord.setRemark(recordVo.getRemark());// 审核时候的备注内容
				childRecord.setCreateTime(recordVo.getModifyTime());// 当前登录者的时间
				childRecord.setCreateUserId(recordVo.getModifyUserId());
				childRecord.setCustomerId(customerId);
				invalidCustomerAuditDao.updateInvalidCustomerRecord(childRecord);
				invalidCustomerAuditDao.updateInvalidCustomerRecord(record);
				return response;
			} else {
				response.setResultCode(-1);
				response.setResultMessage("该客户不存在，审核失败");
				return response;
			}
		}
	}

	@Override
	public DataPackage getInvalidCustomerRecords(InvalidCustomerRecordVo recordVo, DataPackage dp) {
		//外呼主管只能查看外呼专员提交的审核  网络主管只能查看网络专员提交的审核  校区主任只能看见咨询师提交的审核
		Map<String, Object> params = new HashMap<String, Object>();
		User currentLoginUser = userService.getCurrentLoginUser();
		String orgId = currentLoginUser.getOrganizationId();
		long startTime=System.currentTimeMillis();   //获取开始时间
		StringBuilder querySql = new StringBuilder(512);
		querySql.append("select c.*,ica.id as recordId,ica.customer_id as customer_id,ica.create_user_id as createUserId, ");
		querySql.append("ica.previous_status as previous_status,ica.current_status as current_status, ");
		querySql.append("ica.audit_status as audit_status,ica.invalid_remark as invalidReason, ");
//		querySql.append(" ica.create_time as record_create_time,ica.modify_user_id as record_modify_user_id,ica.modify_time as record_modify_time ");
		querySql.append(" d.`NAME` as resEntranceName ,dd.`NAME` as cusTypeName,ddd.`NAME` as cusOrgName, dddd.`NAME` as intentionName,");
		querySql.append(" u.`name` as invalidCreateUserName ");
		querySql.append(" from customer c inner join invalid_customer_audit ica on ( c.ID = ica.customer_id ");
		//增加权限
		querySql.append(" and ica.create_user_id in (select USER_ID from user where organizationID ='"+orgId+"' or organizationID in (SELECT id from organization WHERE parentID ='"+orgId+"' )) )");		
		querySql.append(" LEFT JOIN data_dict d on c.RES_ENTRANCE = d.ID "); 
		querySql.append(" LEFT JOIN data_dict dd on c.CUS_TYPE = dd.ID "); 
		querySql.append(" LEFT JOIN data_dict ddd on c.CUS_ORG = ddd.ID ");
		querySql.append(" LEFT JOIN data_dict dddd on c.INTENTION_OF_THE_CUSTOMER = dddd.ID ");
		querySql.append(" LEFT JOIN user u on u.USER_ID = ica.create_user_id ");
//		querySql.append(" LEFT JOIN `user` u on c.DELEVER_TARGET = u.USER_ID ");

		querySql.append(" where ica.parent_id =0  ");//ica.parent_id is null or 
		// 查询姓名
		if (StringUtils.isNotBlank(recordVo.getName())) {
			querySql.append(" and c.NAME like :name ");
			params.put("name", "%" + recordVo.getName() + "%");
		}
		// 联系方式
		if (StringUtils.isNotBlank(recordVo.getContact())) {
			querySql.append(" and c.CONTACT = :contact ");
			params.put("contact", recordVo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(recordVo.getResEntranceId())) {
			querySql.append(" and c.RES_ENTRANCE = :resEntranceId ");
			params.put("resEntranceId", recordVo.getResEntranceId());
		}
		// 资源大类
		if (StringUtils.isNotBlank(recordVo.getCusOrg())) {
			querySql.append(" and c.CUS_ORG =:cusOrg ");
			params.put("cusOrg", recordVo.getCusOrg());
		}
		// 资源细分
		if (StringUtils.isNotBlank(recordVo.getCusType())) {
			querySql.append(" and c.CUS_TYPE = :cusType ");
			params.put("cusType", recordVo.getCusType());
		}
		// 是否上门
		if (StringUtils.isNotBlank(recordVo.getIsAppointCome())) {
			// 如果是预约上门
			if ("1".equals(recordVo.getIsAppointCome())) {
				querySql.append(" and (c.VISIT_TYPE is not NULL && c.VISIT_TYPE <>'NOT_COME' )");
			}
			// 非预约上门
			if ("0".equals(recordVo.getIsAppointCome())) {
				querySql.append(" and (c.VISIT_TYPE is NULL || c.VISIT_TYPE = 'NOT_COME' )");
			}
		}
		// 意向度
		if (StringUtils.isNotEmpty(recordVo.getIntentionOfTheCustomerId())) {
			querySql.append(" and c.INTENTION_OF_THE_CUSTOMER = :intentionOfTheCustomerId ");
			params.put("intentionOfTheCustomerId", recordVo.getIntentionOfTheCustomerId());
		}
		// 审核状态
		if (null != recordVo.getAuditStatus()) {
			if (StringUtils.isNotBlank(recordVo.getAuditStatus().getValue())) {
				querySql.append(" and ica.audit_status = :auditStatus ");
				params.put("auditStatus", recordVo.getAuditStatus().getValue());
			}
		}
		//提交人
        if(StringUtils.isNotBlank(recordVo.getInvalidCreateUserName())){
        	querySql.append(" and u.`name` like %"+recordVo.getInvalidCreateUserName()+"%");
        }
		
		

		querySql.append(" order by ica.create_time desc ");
		List<Map<Object, Object>> list = invalidCustomerAuditDao.findMapOfPageBySql(querySql.toString(), dp, params);
		dp.setRowCount(invalidCustomerAuditDao.findCountSql("select count(*) from ( " + querySql.toString() + " ) countall ", params));
		List<InvalidCustomerRecordVo> recordVos = new ArrayList<InvalidCustomerRecordVo>();
		InvalidCustomerRecordVo vo = null;
		for (Map<Object, Object> map : list) {
		    vo = new InvalidCustomerRecordVo();
			vo.setId(map.get("recordId") != null ? Long.parseLong(map.get("recordId").toString()) : 0L);
			vo.setCustomerId(map.get("ID") != null ? map.get("ID").toString() : "");
			vo.setName(map.get("NAME") != null ? map.get("NAME").toString() : "");
			vo.setContact(map.get("CONTACT") != null ? map.get("CONTACT").toString() : "");
			// 设置登记人和登记人部门
			if (map.get("CREATE_USER_ID") != null) {				
				String id = map.get("CREATE_USER_ID").toString();
				vo.setCreateCustomerUserId(id);
				User user = userService.findUserById(id);
				vo.setCreateUserName(user == null ? "" : user.getName());
				// 获取用户姓名
				Map usermap = userService.getMainDeptAndJob(id);
				if (usermap != null) {
					vo.setCreateUserDept(usermap.get("deptName") == null ? "" : usermap.get("deptName").toString());
				}
			}
			vo.setResEntranceId(map.get("RES_ENTRANCE")!=null?map.get("RES_ENTRANCE").toString():"");
			vo.setResEntranceName(map.get("resEntranceName")!=null?map.get("resEntranceName").toString():"");
			// 资源来源
			vo.setCusType(map.get("cusTypeName")!=null?map.get("cusTypeName").toString():"");
			// 来源细分
			vo.setCusOrg(map.get("cusOrgName")!=null?map.get("cusOrgName").toString():"");
		
			// 客户意向 intentionName
			vo.setIntentionOfTheCustomerId(map.get("INTENTION_OF_THE_CUSTOMER")!=null?map.get("INTENTION_OF_THE_CUSTOMER").toString():"");
			vo.setIntentionOfTheCustomerName(map.get("intentionName")!=null?map.get("intentionName").toString():"");
			// 提交人
			vo.setInvalidCreateUserName(map.get("invalidCreateUserName")!=null?map.get("invalidCreateUserName").toString():"");
			
			// 最新跟进记录
			Map<String, Object> param = Maps.newHashMap();
			param.put("customerId", vo.getCustomerId());
			StringBuilder query =new StringBuilder(" select * from customer_folowup cf where appointment_type ='"+AppointmentType.FOLLOW_UP+"' ");
			query.append(" and cf.customer_id = :customerId ORDER BY cf.create_time desc LIMIT 1 ");
			
			List<CustomerFolowup> customerFolowups = customerFolowupService.findBySql(query.toString(),param);

			if (customerFolowups.size() > 0) {
				CustomerFolowup cfu = customerFolowups.get(0);
				vo.setLastRemark(cfu.getRemark());
				if (cfu.getVisitType() == null
						|| (cfu.getVisitType() != null && cfu.getVisitType().equals(VisitType.NOT_COME))) {
					vo.setIsAppointCome("否");
				} else {
					vo.setIsAppointCome("是");
				}
			}else{
				vo.setIsAppointCome("否");
				vo.setLastRemark("-");
			}
			
            //最新分配人
			if(map.get("LAST_DELIVER_NAME")!=null){
				vo.setLastDeliverName(map.get("LAST_DELIVER_NAME").toString());
			}else{
				vo.setLastDeliverName("");
			}
				
            //无效理由
			if(map.get("invalidReason")!=null){
				vo.setInvalidReason(map.get("invalidReason").toString());
			}else {
				vo.setInvalidReason("");
			}
			
			// 审核状态
			if (map.get("audit_status") != null) {
				vo.setAuditStatus(CustomerAuditStatus.valueOf(map.get("audit_status").toString()));
			}
//			//审核状态 是从查询子记录里面进行获取  如果没有子记录则从主记录进行查找
//			StringBuilder  sql = new StringBuilder();
//			sql.append("select ica.audit_status as auditStatus from invalid_customer_audit ica where ica.parent_id ='"+vo.getId()+"' order by ica.create_time desc ");
//			List<Map<String, String>> child_list = invalidCustomerAuditDao.findMapBySql(sql.toString());
//			if(child_list!=null && child_list.size()>0){
//				String auditStatus =child_list.get(0).get("auditStatus");
//				vo.setAuditStatus(CustomerAuditStatus.valueOf(auditStatus));
//			}else {
//				// 审核状态
//				if (map.get("audit_status") != null) {
//					vo.setAuditStatus(CustomerAuditStatus.valueOf(map.get("audit_status").toString()));
//				}				
//			}
			
//			//审核状态 是从查询子记录里面进行获取  如果没有子记录则从主记录进行查找
//			StringBuilder  sql = new StringBuilder();
//			sql.append("select ica.audit_status as auditStatus from invalid_customer_audit ica where ica.parent_id ='"+vo.getId()+"' order by ica.create_time desc ");
//			List<Map<String, String>> child_list = invalidCustomerAuditDao.findMapBySql(sql.toString());
//			if(child_list!=null && child_list.size()>0){
//				String auditStatus =child_list.get(0).get("auditStatus");
//				vo.setAuditStatus(CustomerAuditStatus.valueOf(auditStatus));
//			}else {
//				// 审核状态
//				if (map.get("audit_status") != null) {
//					vo.setAuditStatus(CustomerAuditStatus.valueOf(map.get("audit_status").toString()));
//				}				
//			}
            
			//查询无效审核记录的提交提交者的职位
			String createUserId = map.get("createUserId").toString();
			vo.setJobNames(getUserJobNameByUserId(createUserId));
			
			recordVos.add(vo);
		}
		dp.setDatas(recordVos);
		
		long endTime=System.currentTimeMillis(); //获取结束时间 
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms"); 
		return dp;
	}
	
	private String getUserJobNameByUserId(String userId){
//		StringBuilder query = new StringBuilder();
//		query.append("  SELECT uj.JOB_NAME as jobName from user_dept_job udj left join user_job uj on uj.ID=udj.JOB_ID WHERE udj.USER_ID ='"+userId+"' ");
//		List<Map<String, String>> list =userDeptJobDao.findMapBySql(query.toString());	
//		
//		for(Map<String,String> map:list){
//			String jobName = map.get("jobName");
//			if(jobName!=null && jobName.equals(RoleSign.ZXS.getName())){
//				set.add(RoleSign.ZXS.getValue());
//			}
//			if(jobName!=null && jobName.equals(RoleSign.WLZY.getName())){
//				set.add(RoleSign.WLZY.getValue());
//			}
//			if(jobName!=null && jobName.equals(RoleSign.WHZY.getName())){
//				set.add(RoleSign.WHZY.getValue());
//			}	
//		}
		HashSet<String> set = new HashSet<>();
		List<Role> roles = userService.getRoleByUserId(userId);
		if (roles != null && roles.size() > 0) {
			for (Role role : roles) {
				if (RoleCode.CONSULTOR ==(role.getRoleCode())) {
					set.add(RoleSign.ZXS.getValue());
				}
				if (RoleCode.OUTCALL_SPEC ==(role.getRoleCode())) {
					set.add(RoleSign.WHZY.getValue());
				}
				if (RoleCode.NETWORK_SPEC ==(role.getRoleCode())) {
					set.add(RoleSign.WLZY.getValue());
				}
				if(RoleCode.OUTCALL_MANAGER==(role.getRoleCode())||RoleCode.BRANCH_OUTCALL_MANAGER==(role.getRoleCode())){
					set.add(RoleSign.WHZG.getValue());
					set.add(RoleSign.WHZY.getValue());
				}
				if(RoleCode.NETWORK_MANAGER==(role.getRoleCode())){
					set.add(RoleSign.WLZG.getValue());
					set.add(RoleSign.WLZY.getValue());
				}
				if(RoleCode.CONSULTOR_DIRECTOR==(role.getRoleCode())){
					set.add(RoleSign.ZXZG.getValue());
					set.add(RoleSign.ZXS.getValue());
				}
			}
		}	
		StringBuilder stringBuilder = new StringBuilder();
        for(String string:set){
       	    stringBuilder.append(string+",");
        }
        String job= null;
        if(stringBuilder.length()>=1){
           job =stringBuilder.subSequence(0, stringBuilder.length()-1).toString();
        }else{
        	job = "ZXS";
        }
		return job;
	}

	@Override
	public InvalidCustomerRecordVo loadInvalidCustomerResult(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 根据customerId获取记录
		String hql =" from InvalidCustomerRecord where id = :id ";
		params.put("id", Long.parseLong(id));
		InvalidCustomerRecord record = invalidCustomerAuditDao.findOneByHQL(hql, params);
		InvalidCustomerRecordVo recordVo = HibernateUtils.voObjectMapping(record, InvalidCustomerRecordVo.class);
		String customerId = recordVo.getCustomerId();
		if(customerId!=null){
			Customer customer = customerService.findById(customerId);
			if(customer!=null){
				String deliverTarget = customer.getDeliverTarget();
				if(deliverTarget!=null){
					User user=userService.loadUserById(deliverTarget);
					if(user!=null){
						recordVo.setDeliverTarget(deliverTarget);
						recordVo.setDeliverTargetName(user.getName());						
					}else{
					    Organization organization =	organizationService.findById(deliverTarget);
					    if(organization!=null){
							recordVo.setDeliverTarget(deliverTarget);
							recordVo.setDeliverTargetName(organization.getResourcePoolName());						    	
					    }
					}

				}
			}
		}
		//是否曾经不通过
		StringBuilder  sql = new StringBuilder();
		sql.append("select count(1) from invalid_customer_audit ica where ica.parent_id = :id and ica.audit_status ='"+CustomerAuditStatus.NOT_PASS.getValue()+"' ");
		int result  = invalidCustomerAuditDao.findCountSql(sql.toString(), params);
		if(result>0){
			recordVo.setIsAuditNotPass(true);
		}else{
			recordVo.setIsAuditNotPass(false);
		}	
		return recordVo;
	}


	@Override
	public Long setCustomerInvalid(Customer customer,String remark) {
		Map<String, Object> params = new HashMap<String, Object>();
		//首先根据cutomerId获取审核记录
		String hql =" from InvalidCustomerRecord where customerId = :customerId ";
		params.put("customerId", customer.getId());
		InvalidCustomerRecord record = invalidCustomerAuditDao.findOneByHQL(hql, params);
		String currentTime=DateTools.getCurrentDateTime();
		User user = userService.getCurrentLoginUser();
		if(record!=null){
			record.setModifyTime(currentTime);
			record.setModifyUserId(user.getUserId());
			record.setInvalidRemark(remark);
		}else{
			record = new InvalidCustomerRecord();
			record.setParentId(0L);
			record.setCreateTime(currentTime);
			record.setCreateUserId(user.getUserId());
			record.setInvalidRemark(remark);
		}
		record.setCustomerId(customer.getId());
		record.setAuditStatus(CustomerAuditStatus.TOBE_AUDIT);
		record.setPreviousStatus(customer.getDealStatus());		
		Long result =this.saveInvalidCustomerRecord(record);
		return result;
	}


	@Override
	public List<AuditRecordVo> getAuditRecordById(String parentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder  sql = new StringBuilder();
		sql.append("select ica.remark as remark,ica.audit_status as auditStatus,ica.create_time as createTime ,u.`NAME`  as createUserName ");
		sql.append("from invalid_customer_audit ica left join `user` u on ica.create_user_id = u.USER_ID where ica.parent_id = :parentId ");
		params.put("parentId", parentId);
		List<Map<Object, Object>> list = invalidCustomerAuditDao.findMapBySql(sql.toString(), params);		
		if(list!=null && list.size()>0){
			List<AuditRecordVo> result = new ArrayList<AuditRecordVo>();
			AuditRecordVo auditRecordVo = null;
			for(Map<Object, Object> tRecord:list){
				auditRecordVo = new AuditRecordVo();
				auditRecordVo.setAuditDate(tRecord.get("createTime").toString());
				if(tRecord.get("auditStatus")!=null){
					String audit = tRecord.get("auditStatus").toString();
					CustomerAuditStatus sAuditStatus=CustomerAuditStatus.valueOf(audit);
					auditRecordVo.setAuditStatus(sAuditStatus!=null?sAuditStatus.getName():"");
				}				
				auditRecordVo.setAuditRemark(tRecord.get("remark")!=null?tRecord.get("remark").toString():"");
				auditRecordVo.setAuditUserName(tRecord.get("createUserName").toString());
				result.add(auditRecordVo);
			}			
			return result;
			
		}else{
			return null;
		}
	}



}
