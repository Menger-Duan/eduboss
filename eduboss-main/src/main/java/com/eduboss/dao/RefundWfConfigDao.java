package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.RefundWfConfig;


/**
 * @classname	PointialStudentDao.java 
 * @Description
 * @author	Zhang YiHeng
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	Zhang YiHeng
 * @Version	1.0
 */

@Repository
public interface RefundWfConfigDao extends GenericDAO<RefundWfConfig, String> {



	/**
	 * 根据 action 和 level 找到 后续的 step
	 * @param action
	 * @param actionLevel
	 * @return
	 */
	RefundWfConfig findNextStep(String action, String actionLevel);

	/**
	 * 获取第一个 work flow step 
	 * @return
	 */
	RefundWfConfig getFirstStep();
	
}
