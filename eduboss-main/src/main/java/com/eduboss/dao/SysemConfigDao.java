package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.SysemConfig;


/**
 * @classname	SysemConfigDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface SysemConfigDao extends GenericDAO<SysemConfig, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	
}
