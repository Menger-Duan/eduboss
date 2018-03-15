package com.eduboss.service.impl;


import java.util.List;
import java.util.Map;

import org.aspectj.weaver.ast.Or;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerEventType;
import com.eduboss.dao.CustomerDynamicStatusDao;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.domainVo.CustomerDynamicStatusVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.CustomerEventService;
import com.eduboss.service.MessageService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Service("com.eduboss.service.CustomerEventService")
public class CustomerEventServiceImpl implements CustomerEventService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerDynamicStatusDao customerDynamicStatusDao;
	
	@Autowired
	private MessageService messageService;

	@Override
	public DataPackage findCustomerDynamicStatus(CustomerDynamicStatus cds, DataPackage dp) {
		//如果有字段有值，用like进行条件查询
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(cds);
		criterionList.add(Expression.like("customer.name", cds.getCustomer().getId(), MatchMode.ANYWHERE));
		dp = customerDynamicStatusDao.findPageByCriteria(dp, 
				HibernateUtils.prepareOrder(dp, "occourTime", "desc"), 
				criterionList);
		
		List<CustomerDynamicStatusVo> voDatas = HibernateUtils.voListMapping((List<CustomerDynamicStatus>)dp.getDatas(), CustomerDynamicStatusVo.class);
		dp.setDatas(voDatas);
		
		return dp;
	}
	
	@Override
	public DataPackage findCustomerDynamicStatusByCustomerId(String customerId, DataPackage dp) {
		dp = customerDynamicStatusDao.findByCustomerId(customerId, dp);
		List<CustomerDynamicStatusVo> voDatas = HibernateUtils.voListMapping((List<CustomerDynamicStatus>)dp.getDatas(), CustomerDynamicStatusVo.class);
		dp.setDatas(voDatas);
		return dp;
	}

	@Override
	public void saveCustomerDynamicStatus(CustomerDynamicStatus cds) {
		customerDynamicStatusDao.save(cds);
	}
	
	@Override
	public void saveCustomerDynamicStatus(String customerId, CustomerEventType dynamicStatusType, String description, String referUrl) {
          saveCustomerDynamicStatus(customerId, dynamicStatusType, description, userService.getCurrentLoginUser(), referUrl);
	}

	@Override
	public void saveCustomerDynamicStatus(String customerId, CustomerEventType dynamicStatusType, String description) {
		saveCustomerDynamicStatus( customerId,  dynamicStatusType,  description, userService.getCurrentLoginUser(),""); 
	}

	@Override
	public void saveCustomerDynamicStatus(String customerId, CustomerEventType dynamicStatusType, String description,
			User referUser, String referUrl) {
		CustomerDynamicStatus cds = new CustomerDynamicStatus();
		cds.setCustomer(new Customer(customerId));
		cds.setDescription(description);
		cds.setReferUrl(referUrl);
		cds.setDynamicStatusType(dynamicStatusType);
		cds.setOccourTime(DateTools.getCurrentDateTime());
		cds.setReferuser(referUser);
		customerDynamicStatusDao.save(cds);	
		//发送浏览消息给本客户相关的用户    关闭dwr   2016-12-17
//		List<CustomerDynamicStatus> statuses = customerDynamicStatusDao.findByCriteria(Expression.eq("customer.id", customerId));
//		List<String> sendUseIds = new ArrayList<String>();
//		for (CustomerDynamicStatus status : statuses) {
//			if (status!=null && status.getReferuser()!=null && status.getReferuser().getUserId()!=null
//					&& !sendUseIds.contains(status.getReferuser().getUserId()) && referUser!=null && referUser.getUserId()!=null && !status.getReferuser().getUserId().equalsIgnoreCase(referUser.getUserId())) {
//				sendUseIds.add(status.getReferuser().getUserId());				
//				//发送
//				messageService.sendMessage(MessageType.SYSTEM_MSG, "客户"+status.getCustomer().getName()+"新动态:"+dynamicStatusType.getName(), description, MessageDeliverType.SINGLE, status.getReferuser().getUserId());
//			}
//		}	
	}

	@Override
	public void saveCustomerDynamicStatus(String customerId, CustomerEventType dynamicStatusType, String description,
			User referUser, String resEntrance, Integer statusNum) {
		CustomerDynamicStatus cds = new CustomerDynamicStatus();
		cds.setCustomer(new Customer(customerId));
		cds.setDescription(description);
		cds.setReferUrl("");
		cds.setDynamicStatusType(dynamicStatusType);
		cds.setOccourTime(DateTools.getCurrentDateTime());
		cds.setReferuser(referUser);
		if(resEntrance==null){
			cds.setResEntrance(null);
		}else{
			cds.setResEntrance(new DataDict(resEntrance));
		}
		
		cds.setStatusNum(statusNum);
		customerDynamicStatusDao.save(cds);	
		
	}

	@Override
	public void addCustomerDynameicStatus(Customer customer, CustomerDynamicStatus dynamicStatus, User user) {
		//新的用于保存客户操作事件
		dynamicStatus.setCustomer(customer);
		dynamicStatus.setReferuser(user);		
		//设置客户的所属校区 分配对象 所属校区
		if(StringUtil.isNotBlank(customer.getDeliverTarget())){
			dynamicStatus.setDeliverTarget(customer.getDeliverTarget());
			User temp = userService.loadUserById(customer.getDeliverTarget());
			if(temp!=null){
				Organization belongCampus = userService.getBelongCampusByUserId(customer.getDeliverTarget());
				if(belongCampus!=null){
					dynamicStatus.setBelongCampus(belongCampus.getId());
				}
			}else{
				Organization belongCampus = userService.getBelongCampusByOrgId(customer.getDeliverTarget());
				if(belongCampus!=null){
					dynamicStatus.setBelongCampus(belongCampus.getId());
				}
			}

		}
		//设置 操作人所属职位和所属部门的统计归属
		Map<String, String> map = null;
		if (user!=null){
			map = userService.getUserMainDeptAndBelongByUserId(user.getUserId());
		}

		if(map!=null){
			dynamicStatus.setUserMainJob(map.get("jobId"));
			dynamicStatus.setUserMainDeptBelong(map.get("belong"));
		}
		dynamicStatus.setOccourTime(DateTools.getCurrentDateTime());
		dynamicStatus.setReferUrl(null);
		dynamicStatus.setDealStatus(customer.getDealStatus());
		//仍然缺的信息
        //CustomerEventType dynamicStatusType;
        //description;
        //resEntrance; //资源入口
		//statusNum;//用于统计数据 取值为+1,-1 负数表示统计的时候要减去一条记录
        //tableName
		//tableId;//事件对应表的记录id
		//visitFlag;//是否对客户可见 1客户0不可见		
		customerDynamicStatusDao.save(dynamicStatus);
	}

	@Override
	public void addCustomerDynameicStatus(Customer customer, CustomerDynamicStatus dynamicStatus) {
		dynamicStatus.setCustomer(customer);
		dynamicStatus.setReferuser(null);
		dynamicStatus.setOccourTime(DateTools.getCurrentDateTime());
		dynamicStatus.setReferUrl(null);
		dynamicStatus.setDealStatus(customer.getDealStatus());
		customerDynamicStatusDao.save(dynamicStatus);
	}
	
	@Override
	public int findCustomerDynameicStatusCount(String sql){
		Map<String,Object> params =Maps.newHashMap();
		return customerDynamicStatusDao.findCountSql(sql,params);
	}
	
	

}
