package com.eduboss.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.CustomerMapAnalyzeDao;
import com.eduboss.domain.CustomerMapAnalyze;
import com.eduboss.domainVo.CustomerMapAnalyzeVo;

@Repository("CustomerMapAnalyzeDao")
public class CustomerMapAnalyzeDaoImpl extends GenericDaoImpl<CustomerMapAnalyze,String> implements CustomerMapAnalyzeDao{

	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 将地址转换成经纬度保存到库
	 * */
	@Override
	public List TransactionAddressToLatitude() {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from CustomerMapAnalyze where 1=1");
		Query q = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(hql.toString());
		return q.list();
		
	}



}
