package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.RefundContractProduct;


/**
 * @classname	PointialStudentDao.java 
 * @Description
 * @author	Zhang YiHeng
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	Zhang YiHeng
 * @Version	1.0
 */

@Repository
public interface RefundContractProductDao extends GenericDAO<RefundContractProduct, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	
}
