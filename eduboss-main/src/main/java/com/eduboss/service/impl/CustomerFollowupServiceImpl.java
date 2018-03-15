package com.eduboss.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.common.collect.Maps;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.AppointmentType;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.dao.CustomerDao;
import com.eduboss.dao.CustomerFolowupDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerFolowup;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.CustomerFolowupVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.CustomerFolowupService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;



@Service("com.eduboss.service.CustomerFollowupService")
public class CustomerFollowupServiceImpl implements CustomerFolowupService{
    
	private final static Logger log = Logger.getLogger(CustomerFollowupServiceImpl.class);
	
	@Autowired
	private CustomerFolowupDao customerFolowupDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<CustomerFolowup> findByCriteria(List<Order> orders,Criterion ...criterion) {
		return customerFolowupDao.findByCriteria(orders, criterion);		
	}
	@Override
	public List<CustomerFolowup> findByHql(String hql,Map<String, Object> params) {
		return customerFolowupDao.findAllByHQL(hql,params);
	}
	@Override
	public Boolean hasFollowupRecordWithDate(String beginDate, String endDate,String customerId) {
		//判断在某段时间内是不是上门登记记录
		//APPOINTMENT_TYPE = APPOINTMENT,VISIT_TYPE = PARENT_COME
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId", customerId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		StringBuffer hqlBuffer = new StringBuffer("from CustomerFolowup where appointmentType ='APPOINTMENT' and visitType ='PARENT_COME'");
		hqlBuffer.append(" and customer.id = :customerId ");
		hqlBuffer.append(" and meetingConfirmTime between :beginDate and :endDate ");
		List<CustomerFolowup> list = this.findByHql(hqlBuffer.toString(),params);
		if(list!=null && !list.isEmpty()){
			return true;
		}else{
			return false;
		}	
	}
	@Override
	public Boolean hasFollowupRecordWithinDays(String date, int days,String customerId) {
		//时间关系  比如距离现在的时间30天前 
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate =null;
		try {
			nowDate = dateFormat.parse(date);
		} catch (ParseException e) {
			log.debug("格式化日期出错");
			throw new ApplicationException("格式化日期出错");
		}
		Calendar calendar = Calendar.getInstance();	
		calendar.setTime(nowDate);
		calendar.add(Calendar.DATE, -days);//计算30天前的时间
		String beforeDate=dateFormat.format(calendar.getTime());
		return hasFollowupRecordWithDate(beforeDate, date,customerId);
	}
	@Override
	public Boolean hasFollowupRecord(String customerId) {
		//APPOINTMENT_TYPE = APPOINTMENT,VISIT_TYPE = PARENT_COME
		StringBuffer hqlBuffer = new StringBuffer("from CustomerFolowup where appointmentType ='APPOINTMENT' and visitType ='PARENT_COME'");
		hqlBuffer.append(" and customer.id = :customerId ");
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId", customerId);
		List<CustomerFolowup> list = this.findByHql(hqlBuffer.toString(),params);
		if(list!=null && !list.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public List<CustomerFolowup> findBySql(String sql,Map<String, Object> params) {
		return (List<CustomerFolowup>) customerFolowupDao.findBySql(sql,params);
	}
	@Override
	public void updateFollowupDealStatus(String followupId) {
		CustomerFolowup customerFolowup = customerFolowupDao.findById(followupId);
		customerFolowup.setDealStatus(CustomerDealStatus.FOLLOWING);		
	}
	
	@Override
	public List<CustomerFolowup> findRecordbyUserIdAndCustomerId(String userId, String customerId,AppointmentType appointmentType) {
		StringBuffer hqlBuffer = new StringBuffer("from CustomerFolowup ");
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId", customerId);
		params.put("userId", userId);
		hqlBuffer.append(" where createUser.userId = :userId ");
		hqlBuffer.append(" and customer.id = :customerId ");
		if(appointmentType!=null){
			hqlBuffer.append(" and appointmentType = :appointmentType  ");	
			params.put("appointmentType", appointmentType.getValue());
		}else{
			//hqlBuffer.append(" and (appointmentType ='"+AppointmentType.APPOINTMENT_ONLINE+"' or appointmentType ='"+AppointmentType.APPOINTMENT_ONLINE_OPERATION+"'  )");		
		    //需求变更 20170325 网络分配营运经理改为 分配校区营运主任
			hqlBuffer.append(" and (appointmentType ='"+AppointmentType.APPOINTMENT_ONLINE.getValue()+"' )");		
		}
		hqlBuffer.append(" order by createTime desc ");		
		List<CustomerFolowup> list = this.findByHql(hqlBuffer.toString(),params);
		return list;
	}
	
	@Override
	public Response getCustomerMeetingConfirm(String customerId) {
		Response response = new Response();
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId", customerId);		
		String sql = "select * from customer_folowup where CUSTOMER_ID = :customerId and APPOINTMENT_TYPE='APPOINTMENT' ORDER BY CREATE_TIME desc ";
		List<CustomerFolowup> list = customerFolowupDao.findBySql(sql, params);
		if(list!=null && list.size()>0){
			CustomerFolowup folowup = list.get(0);
			if(StringUtil.isNotBlank(folowup.getMeetingConfirmTime())){
				response.setResultCode(-1);
				response.setResultMessage("最新预约已经到访，不能修改预约时间");
			}else{
				response.setResultCode(0);
				response.setResultMessage("可以修改预约时间");
			}
			
		}else{
			response.setResultCode(-1);
			response.setResultMessage("没有找到预约记录，不能修改预约时间");
		}
		
		return response;
	}
	@Override
	public CustomerFolowupVo getLastAppointmentRecord(String customerId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId", customerId);		
		String sql = "select * from customer_folowup where CUSTOMER_ID = :customerId and APPOINTMENT_TYPE='APPOINTMENT' ORDER BY CREATE_TIME desc ";
		List<CustomerFolowup> list = customerFolowupDao.findBySql(sql, params);
		if(list!=null && list.size()>0){
			CustomerFolowup folowup = list.get(0);
			CustomerFolowupVo folowupVo = HibernateUtils.voObjectMapping(folowup, CustomerFolowupVo.class);
			return folowupVo;
		}
		return null;
	}
	
	@Override
	public void updateAppointCustomer(CustomerFolowupVo follow) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", follow.getId());
		params.put("customerId", follow.getCustomerId());		
		String sql = "select * from customer_folowup where ID=:id and CUSTOMER_ID = :customerId and APPOINTMENT_TYPE='APPOINTMENT' ORDER BY CREATE_TIME desc ";
		List<CustomerFolowup> list = customerFolowupDao.findBySql(sql, params);
		if(list!=null && list.size()>0){
			CustomerFolowup folowup = list.get(0);
			folowup.setMeetingTime(follow.getMeetingTime());
			folowup.setAppointCampus(new Organization(follow.getAppointCampusId()));
			folowup.setSatisficing(new DataDict(follow.getSatisficing()));
			folowup.setRemark(follow.getRemark());
			Customer customer = customerDao.findById(follow.getCustomerId());
			if(customer!=null){
				Organization blcampus = organizationDao.findById(follow.getAppointCampusId());
				customer.setBlSchool(follow.getAppointCampusId());
				customer.setBlCampusId(blcampus);
				customer.setAppointmentDate(follow.getMeetingTime());
				customer.setModifyTime(DateTools.getCurrentDateTime());
				customer.setModifyUserId(userService.getCurrentLoginUser().getUserId());
			}
		}
		
		
	}

}
