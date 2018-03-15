package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.CustomerDao;
import com.eduboss.domain.Customer;
import com.eduboss.domainVo.CustomerAppointmentVo;
import com.eduboss.domainVo.CustomerVo;
import com.google.common.collect.Maps;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.eduboss.domain.AppUser
 * @author MyEclipse Persistence Tools
 */
@Repository("CustomerDao")
public class CustomerDaoImpl extends GenericDaoImpl<Customer, String> implements CustomerDao {

	public int getCustomerCount(CustomerVo vo){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql=" select count(1) from customer ";
		String sqlWhere="";
		if(StringUtils.isNotEmpty(vo.getCusOrg())){
			sqlWhere+=" and CUS_ORG = :cusOrg ";
			params.put("cusOrg", vo.getCusOrg());
		}
		if(StringUtils.isNotEmpty(vo.getRecordDate())){
			sqlWhere+=" and RECORD_DATE = :recordDate ";
			params.put("recordDate", vo.getRecordDate());
		}
		if(StringUtils.isNotEmpty(vo.getDeliverTarget())){
			sqlWhere+=" and DELEVER_TARGET = :deliverTarget ";
			params.put("deliverTarget", vo.getDeliverTarget());
		}
		if(vo.getDeliverType()!=null){
			sqlWhere+=" and DELIVER_TYPE = :deliverType ";
			params.put("deliverType", vo.getDeliverType());
		}
		if (StringUtils.isNotEmpty(vo.getContact())) {
			sqlWhere+=" and CONTACT = :contact ";
			params.put("contact", vo.getContact());
		}
		if(!sqlWhere.equals("")){
			sql+="where "+sqlWhere.substring(4);
		}
		return findCountSql(sql, params);
	}
	
	public int getCustomerCountByPhone(CustomerVo vo){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql=" select count(1) from customer ";
		String sqlWhere="";
		if (StringUtils.isNotEmpty(vo.getContact())) {
			sqlWhere+=" and CONTACT = :contact ";
			params.put("contact", vo.getContact());
		}
		if (StringUtils.isNotEmpty(vo.getId())) {
			sqlWhere+=" and id != :id ";
			params.put("id", vo.getId());
		}
		if(!sqlWhere.equals("")){
			sql+="where "+sqlWhere.substring(4);
		}
		return findCountSql(sql, params);
	}
	
	
	public List<CustomerVo> getCustomerNames(CustomerVo vo){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql=" select * from customer ";
		String sqlWhere="";
		if(StringUtils.isNotEmpty(vo.getCusOrg())){
			sqlWhere+=" and CUS_ORG= :cusOrg ";
			params.put("cusOrg", vo.getCusOrg());
		}
		if(StringUtils.isNotEmpty(vo.getRecordDate())){
			sqlWhere+=" and RECORD_DATE = :recordDate ";
			params.put("recordDate", vo.getRecordDate());
		}
		if(StringUtils.isNotEmpty(vo.getDeliverTarget())){
			sqlWhere+=" and DELEVER_TARGET = :deliverTarget ";
			params.put("deliverTarget", vo.getDeliverTarget());
		}
		if(vo.getDeliverType()!=null){
			sqlWhere+=" and DELIVER_TYPE = :deliverType ";
			params.put("deliverType", vo.getDeliverType());
		}
		if(!sqlWhere.equals("")){
			sql+="where "+sqlWhere.substring(4);
		}
		 List<Customer> list = findBySql(sql, params);
		 List<CustomerVo> listVo = new ArrayList<CustomerVo>();
		 for(Customer c :list){
			 CustomerVo CusVo=new CustomerVo();
			 CusVo.setId(c.getId());
			 CusVo.setName(c.getName());
			 listVo.add(CusVo);
		 }
		return listVo;
	}

	@Override
	public void setCustomerNextFollowupTime(CustomerAppointmentVo appointment) {
		getHibernateTemplate().bulkUpdate("update Customer set nextFollowupTime = ? where id = ?",appointment.getMeetingTime(),appointment.getCustomerId());
	}

    @Override
    public int countCustomerNumsExceptSelf(String contact, String customerId) {
    	Map<String, Object> params  = Maps.newHashMap();
    	params.put("customerId", customerId);
    	params.put("contact",contact);
		String countSql = "select count(*) from customer where ID <> :customerId and CONTACT = :contact " ;
		int nums = this.findCountSql(countSql,params);
		return nums;
    }
}
