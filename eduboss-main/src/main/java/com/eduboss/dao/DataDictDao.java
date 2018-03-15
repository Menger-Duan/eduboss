package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.common.DataDictCategory;
import com.eduboss.domain.DataDict;


/**
 * @classname	DataDictDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */


public interface DataDictDao extends GenericDAO<DataDict, String> {

	String getDataDictIdByName(String gradeName,DataDictCategory type);
	
	String getDataDictIdByLikeName(String gradeName,DataDictCategory type);
	
	DataDict getDataDictByName(String name,DataDictCategory type);
	
	List<DataDict> getDataDictListByCategory(DataDictCategory type);
	
	List<DataDict> listDataDictsByIds(String[] ids);
	
}
