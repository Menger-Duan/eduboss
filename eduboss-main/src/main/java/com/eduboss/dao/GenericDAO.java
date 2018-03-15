/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eduboss.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.eduboss.domain.RoleQLConfig;
import com.eduboss.dto.DataPackage;


/**
 * @classname	GenericDAO.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2013-3-15 12:34:44
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */
@Repository
public interface GenericDAO<T, ID extends Serializable> extends BasicGenericDAO<T, Serializable> {

	/**
	 * @return
	 */
	List<T> findAll();

	/**
	 * @param criterion
	 * @return
	 */
	List<T> findByCriteria(Criterion... criterion);
	
	
	/**
	 * @param orderList
	 * @param criterion
	 * @return
	 */
	List<T> findByCriteria(List<Order> orderList,Criterion... criterion);
	/**
	 * @param dataPackage
	 * @param criterion
	 * @return
	 */
	DataPackage findPageByCriteria(DataPackage dataPackage, List<Order> orderList, Criterion... criterion);
	
	DataPackage findPageByCriteria(DataPackage dataPackage, List<Order> orderList, List<Criterion> criterionList);
	
	DataPackage findPageByCriteria(DataPackage dataPackage, List<Order> orderList, List<Criterion> criterion, Map<String, String> aliasMap);
	
	
	/**
	 * @param dataPackage
	 * @return
	 */
	DataPackage findPageByCriteria(DataPackage dataPackage);
	
	/**
	 * @param list
	 * @return
	 */
	List<T> findAllByCriteria(List<Criterion> list);
	
	List<T> findAllByCriteria(List<Criterion> list,List<Order> orderList);
	
	int findCountByCriteria( List<Criterion> criterion) ;
	
    List<Criterion> buildCriterions(List<RoleQLConfig> configs);

    Criterion buildCriterion(String expression);

    Criterion buildCriterion(String propertyName,String matchType,Object value);

}
