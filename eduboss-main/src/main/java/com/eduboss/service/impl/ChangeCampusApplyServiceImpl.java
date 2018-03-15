package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.eduboss.common.CustomerAuditStatus;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.CustomerEventType;
import com.eduboss.dao.ChangeCampusApplyDao;
import com.eduboss.domain.ChangeCampusApply;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.Organization;
import com.eduboss.domain.TransferCustomerCampus;
import com.eduboss.domain.User;
import com.eduboss.domainVo.ChangeCampusApplyVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.service.ChangeCampusApplyService;
import com.eduboss.service.CustomerEventService;
import com.eduboss.service.CustomerService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Service("changeCampusApplyService")
public class ChangeCampusApplyServiceImpl implements ChangeCampusApplyService{
	
	
	@Autowired
	private ChangeCampusApplyDao changeCampusApplyDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrganizationService organizationService;
	
    @Autowired
    private CustomerEventService customerEventService;

	@Override
	public Response saveChangeCampusApply(ChangeCampusApplyVo campusApplyVo) {
		Response response = new Response();
		
		String customerId = campusApplyVo.getCustomerId();
		
		Customer customer = customerService.findById(customerId);
		
		if(customer==null){
			response.setResultCode(-1);
			response.setResultMessage("该客户不存在");
			return response;
		}
	
		customer.setDealStatus(CustomerDealStatus.TOBE_AUDIT);
		User user = userService.getCurrentLoginUser();
		Organization belongCampus = userService.getBelongCampusByUserId(user.getUserId());
		Organization belongCampusNew = organizationService.findById(campusApplyVo.getNewCampusId());
		ChangeCampusApply changeCampusApply = getChangeCampusApplyByUserIdAndCustomerId(user.getUserId(), customerId);
		if(changeCampusApply!=null){
			changeCampusApply.setCreateUserId(user.getUserId());
			changeCampusApply.setCreateTime(DateTools.getCurrentDateTime());
			changeCampusApply.setAuditStatus(CustomerAuditStatus.TOBE_AUDIT);
			changeCampusApply.setApplyReason(campusApplyVo.getApplyReason());
			changeCampusApply.setCustomerId(customerId);
			changeCampusApply.setContact(customer.getContact());
			changeCampusApply.setCustomerName(customer.getName());
			changeCampusApply.setCustomerStatus(customer.getDealStatus());
			changeCampusApply.setDeliverTarget(customer.getDeliverTarget());
			changeCampusApply.setResEntrance(customer.getResEntrance());
			changeCampusApply.setNewCampusId(campusApplyVo.getNewCampusId());
			changeCampusApply.setNewOrgLevel(belongCampusNew.getOrgLevel());
			changeCampusApply.setOldCampusId(belongCampus.getId());
			changeCampusApply.setOldOrgLevel(belongCampus.getOrgLevel());
			changeCampusApply.setIntentionCampusId(campusApplyVo.getNewCampusId());//申请转入校区
			//changeCampusApply.setIntentionCampusId(customer.getIntentionCampus()!=null?customer.getIntentionCampus().getId():null);
		}else{
			changeCampusApply = new ChangeCampusApply();
			changeCampusApply.setCreateUserId(user.getUserId());
			changeCampusApply.setCreateTime(DateTools.getCurrentDateTime());
			changeCampusApply.setAuditStatus(CustomerAuditStatus.TOBE_AUDIT);
			changeCampusApply.setApplyReason(campusApplyVo.getApplyReason());
			changeCampusApply.setCustomerId(customerId);
			changeCampusApply.setContact(customer.getContact());
			changeCampusApply.setCustomerName(customer.getName());
			changeCampusApply.setCustomerStatus(customer.getDealStatus());
			changeCampusApply.setDeliverTarget(customer.getDeliverTarget());
			changeCampusApply.setResEntrance(customer.getResEntrance());
			changeCampusApply.setNewCampusId(campusApplyVo.getNewCampusId());
			changeCampusApply.setNewOrgLevel(belongCampusNew.getOrgLevel());
			changeCampusApply.setOldCampusId(belongCampus.getId());
			changeCampusApply.setOldOrgLevel(belongCampus.getOrgLevel());	
			changeCampusApply.setIntentionCampusId(campusApplyVo.getNewCampusId());//申请转入校区
			//changeCampusApply.setIntentionCampusId(customer.getIntentionCampus()!=null?customer.getIntentionCampus().getId():null);
		}
		changeCampusApplyDao.save(changeCampusApply);
		customerService.updateCustomer(customer);
		
		CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
		dynamicStatus.setDynamicStatusType(CustomerEventType.CHANGECAMPUS_APPLY);
		dynamicStatus.setDescription("咨询师申请转校");
		if(customer.getResEntrance()!=null){
		   dynamicStatus.setResEntrance(customer.getResEntrance());
		}
		dynamicStatus.setStatusNum(1);
		dynamicStatus.setTableName("change_campus_apply");
		dynamicStatus.setTableId(changeCampusApply.getId());
		dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
		customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, user);
		
		
		
		
		return response;
	}

	@Override
	public ChangeCampusApply getChangeCampusApplyByUserIdAndCustomerId(String userId, String customerId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		params.put("customerId", customerId);
		String hql = " from ChangeCampusApply c where c.createUserId = :userId and c.customerId = :customerId ";
		List<ChangeCampusApply> list = changeCampusApplyDao.findAllByHQL(hql,params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public DataPackage getChangeCampusCustomers(DataPackage dp, ChangeCampusApplyVo campusApplyVo) {
		Map<String, Object> params = Maps.newHashMap();
	
		// 查询提交转校申请列表
		User user = userService.getCurrentLoginUser();
		StringBuilder querySql = new StringBuilder(1024);
		querySql.append(" select * from change_campus_apply cca  ");	
		querySql.append(" where cca.create_user_id = :userId ");	
		params.put("userId", user.getUserId());
		//查询条件
		// 查询姓名
		if (StringUtils.isNotBlank(campusApplyVo.getCustomerName())) {
			querySql.append(" and cca.customer_name like :customerName ");
			params.put("customerName", "%" + campusApplyVo.getCustomerName() + "%");
		}
		// 联系方式
		if (StringUtils.isNotBlank(campusApplyVo.getContact())) {
			querySql.append(" and cca.contact = :contact ");
			params.put("contact", campusApplyVo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(campusApplyVo.getResEntranceId())) {
			querySql.append(" and cca.res_entrance = :resEntranceId ");
			params.put("resEntranceId", campusApplyVo.getResEntranceId());
		}
		//意向校区 --改为申请转入校区
		if(StringUtils.isNotBlank(campusApplyVo.getIntentionCampusId())){
			querySql.append(" and cca.intention_campus_id = :intentionCampusId ");
			params.put("intentionCampusId", campusApplyVo.getIntentionCampusId());
		}
		//申请开始时间
		if(StringUtils.isNotBlank(campusApplyVo.getCreateBeginDate())){
			querySql.append(" and cca.create_time >= :createBeginDate ");
			params.put("createBeginDate", campusApplyVo.getCreateBeginDate()+" 00:00:00");
		}
		//申请结束时间
		if(StringUtils.isNotBlank(campusApplyVo.getCreateEndDate())){
			querySql.append(" and cca.create_time <= :createEndDate ");
			params.put("createEndDate", campusApplyVo.getCreateEndDate()+" 23:59:59");
		}
		//审核状态
		if(campusApplyVo.getAuditStatus()!=null){
			querySql.append(" and cca.audit_status = :auditStatus ");
			params.put("auditStatus", campusApplyVo.getAuditStatus());
		}

		// 分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dp.getSord()) && StringUtils.isNotBlank(dp.getSidx())) {
			querySql.append(" order by "+dp.getSidx()+" "+dp.getSord());
		}
		dp = changeCampusApplyDao.findPageBySql(querySql.toString(), dp,true,params);
	
		List<ChangeCampusApply> changeCampusApplies = (List<ChangeCampusApply>)dp.getDatas();
		// 将查询结果转换为vo
		//'客户姓名','联系方式','资源入口', '意向校区','转校申请时间','审核状态','审核意见'
		List<ChangeCampusApplyVo> voList = new ArrayList<ChangeCampusApplyVo>();
		ChangeCampusApplyVo cVo = null ;		
		for(ChangeCampusApply changeCampusApply:changeCampusApplies){
			cVo = HibernateUtils.voObjectMapping(changeCampusApply, ChangeCampusApplyVo.class);
			cVo.setAuditStatusName(cVo.getAuditStatus().getName());
			if(cVo.getNewCampusId()!=null){
				cVo.setIntentionCampusName(organizationService.findById(cVo.getNewCampusId()).getName());
			}
			voList.add(cVo);
		}
		
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public DataPackage getChangeCampusApplys(DataPackage dp, ChangeCampusApplyVo campusApplyVo) {
		Map<String, Object> params = Maps.newHashMap();
		// 查询提交转校申请列表
		User user = userService.getCurrentLoginUser();
		Organization belongCampus = userService.getBelongCampusByUserId(user.getUserId());
		StringBuilder querySql = new StringBuilder(1024);
		querySql.append(" select * from change_campus_apply cca  ");	
		querySql.append(" where 1=1 ");	

		//查询条件
		// 查询姓名
		if (StringUtils.isNotBlank(campusApplyVo.getCustomerName())) {
			querySql.append(" and cca.customer_name like :customerName ");
			params.put("customerName", "%" + campusApplyVo.getCustomerName() + "%");
		}
		// 联系方式
		if (StringUtils.isNotBlank(campusApplyVo.getContact())) {
			querySql.append(" and cca.contact = :contact ");
			params.put("contact", campusApplyVo.getContact());
		}
		// 资源入口
		if (StringUtils.isNotBlank(campusApplyVo.getResEntranceId())) {
			querySql.append(" and cca.res_entrance = :resEntranceId ");
			params.put("resEntranceId", campusApplyVo.getResEntranceId());
		}
		//意向校区--改为申请转入校区
		if(StringUtils.isNotBlank(campusApplyVo.getIntentionCampusId())){
			querySql.append(" and cca.intention_campus_id = :intentionCampusId ");
			params.put("intentionCampusId", campusApplyVo.getIntentionCampusId());
		}
		//申请开始时间
		if(StringUtils.isNotBlank(campusApplyVo.getCreateBeginDate())){
			querySql.append(" and cca.create_time >= :createBeginDate ");
			params.put("createBeginDate", campusApplyVo.getCreateBeginDate()+" 00:00:00");
		}
		//申请结束时间
		if(StringUtils.isNotBlank(campusApplyVo.getCreateEndDate())){
			querySql.append(" and cca.create_time <= :createEndDate ");
			params.put("createEndDate", campusApplyVo.getCreateEndDate()+" 23:59:59");
		}
		//审核状态
		if(campusApplyVo.getAuditStatus()!=null){
			querySql.append(" and cca.audit_status = :auditStatus ");
			params.put("auditStatus", campusApplyVo.getAuditStatus());
		}
		
		
		querySql.append(" and cca.old_org_level like '"+belongCampus.getOrgLevel()+"%'" );

		// 分页 排序 jqgrid插件
		if (StringUtils.isNotBlank(dp.getSord()) && StringUtils.isNotBlank(dp.getSidx())) {
			querySql.append(" order by " + dp.getSidx() + " " + dp.getSord());
		}
		dp = changeCampusApplyDao.findPageBySql(querySql.toString(), dp,true,params);
	
		List<ChangeCampusApply> changeCampusApplies = (List<ChangeCampusApply>)dp.getDatas();
		// 将查询结果转换为vo
		//'客户姓名','联系方式','资源入口', '意向校区','转校申请时间','审核状态','审核意见'
		List<ChangeCampusApplyVo> voList = new ArrayList<ChangeCampusApplyVo>();
		ChangeCampusApplyVo cVo = null ;		
		for(ChangeCampusApply changeCampusApply:changeCampusApplies){
			cVo = HibernateUtils.voObjectMapping(changeCampusApply, ChangeCampusApplyVo.class);
			cVo.setAuditStatusName(cVo.getAuditStatus().getName());
			if(cVo.getNewCampusId()!=null){
				cVo.setIntentionCampusName(organizationService.findById(cVo.getNewCampusId()).getName());
			}
			voList.add(cVo);
		}
		
		dp.setDatas(voList);
		return dp;
	}

	/**
	 * 对转校审核进行审核 
	 * @param campusApplyVo
	 * @return
	 */
	@Override
	public Response updateChangeCampusResult(ChangeCampusApplyVo campusApplyVo) {
		Response response  = new Response();
		if(campusApplyVo.getId()==null){
			response.setResultCode(-1);
			response.setResultMessage("审核参数出错");
		}
		User user = userService.getCurrentLoginUser();
		ChangeCampusApply campusApply = changeCampusApplyDao.findById(campusApplyVo.getId());
		CustomerAuditStatus auditStatus = campusApplyVo.getAuditStatus();
		
		campusApply.setAuditStatus(auditStatus);//通过或者不通过
		campusApply.setRemark(campusApplyVo.getRemark());//审核备注
		campusApply.setModifyUserId(user.getUserId());
		campusApply.setModifyTime(DateTools.getCurrentDateTime());
		
		String customerId = campusApply.getCustomerId();
		Customer customer  = customerService.findById(customerId);
		
		String newCampusId = campusApply.getNewCampusId();
		String oldCampusId = campusApply.getOldCampusId();
		String createUserId = campusApply.getCreateUserId();
		
		if(auditStatus == CustomerAuditStatus.PASS){
			//通过
			customer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
			customer.setDeliverTarget(newCampusId);
			customer.setDealStatus(CustomerDealStatus.NEW);
			customer.setBlSchool(newCampusId);
			//设置校区 同时用bl_campus blschool
			Organization org = organizationService.findById(newCampusId);
			customer.setBlCampusId(org);
			customer.setTransferFrom(oldCampusId);
			customer.setTransferStatus("0");
			customer.setTransferTime(DateTools.getCurrentDateTime());
			customerService.saveOrUpdateCustomer(customer);
			
			TransferCustomerCampus info = null;
			String recordId = null;
		    List<TransferCustomerCampus> list = customerService.getTransferCustomerRecord(customerId);
		    	if(list!=null&& list.size()>0){
		    		info = list.get(0);
		    		info.setTransferCampus(oldCampusId);
		    		info.setReceiveCampus(newCampusId);
					info.setDelivetTargetTo(newCampusId);
					info.setTransferTime(DateTools.getCurrentDateTime());
					info.setCreateTime(DateTools.getCurrentDateTime());
					info.setCreateuser(createUserId);
					customerService.updateTransferCustomer(info);
					recordId = info.getId();
		    	}else{
		    		//添加客户转校信息
				    info = new TransferCustomerCampus();
					info.setCusId(customerId);
					info.setTransferCampus(oldCampusId);
					info.setReceiveCampus(newCampusId);
					//设置客户原来的分配目标
					info.setDeliverTargetFrom(campusApply.getDeliverTarget());
					//设置将要的分配目标
					info.setDelivetTargetTo(newCampusId);
					info.setTransferTime(DateTools.getCurrentDateTime());
					info.setCreateTime(DateTools.getCurrentDateTime());
					info.setCreateuser(createUserId);
					recordId = customerService.addTransferCustomer(info);
		    }	
		    
			String description = "转校申请通过，客户转到校区" + org.getName();

			CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
			dynamicStatus.setDynamicStatusType(CustomerEventType.TRANSFERCUSTOMEROUT);
			dynamicStatus.setDescription(description);
			if (customer.getResEntrance() != null) {
				dynamicStatus.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus.setStatusNum(1);
			dynamicStatus.setTableName("transfer_customer");
			dynamicStatus.setTableId(recordId);
			dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, user);
		    	
		    	
		    	
		}else if(auditStatus == CustomerAuditStatus.NOT_PASS){
			//不通过
			customer.setDealStatus(CustomerDealStatus.FOLLOWING);
			customerService.updateCustomer(customer);
			//
		}	
		return response;
	}

	@Override
	public ChangeCampusApplyVo loadChangeCampusResult(String id) {		
		ChangeCampusApply campusApply = changeCampusApplyDao.findById(id);
		ChangeCampusApplyVo campusApplyVo = HibernateUtils.voObjectMapping(campusApply, ChangeCampusApplyVo.class);
	    if(campusApplyVo.getRemark()==null){
	    	campusApplyVo.setRemark("");
	    }
	    return campusApplyVo;
	}
	
	
	

}
