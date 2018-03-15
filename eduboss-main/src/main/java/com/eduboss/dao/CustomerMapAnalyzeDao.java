package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.CustomerMapAnalyze;
import com.eduboss.domainVo.CustomerMapAnalyzeVo;


public interface CustomerMapAnalyzeDao extends GenericDAO<CustomerMapAnalyze,String>{

	/**
	 * 将地址转换成经纬度保存到库
	 * */
	public List TransactionAddressToLatitude();
	
}
