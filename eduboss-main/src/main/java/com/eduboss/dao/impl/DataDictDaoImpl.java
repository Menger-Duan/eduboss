package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Expression;
import org.springframework.stereotype.Repository;

import com.eduboss.common.DataDictCategory;
import com.eduboss.dao.DataDictDao;
import com.eduboss.domain.DataDict;
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
@Repository("DataDictDao")
public class DataDictDaoImpl extends GenericDaoImpl<DataDict, String> implements DataDictDao {

	@Override
	public String getDataDictIdByName(String gradeName,DataDictCategory type) {
		List<DataDict> list= findByCriteria(Expression.eq("name", gradeName),Expression.eq("category", type));
		if(list!=null && list.size()>0){
			return list.get(0).getId();
		}
		return null;
	}
	
	@Override
	public String getDataDictIdByLikeName(String gradeName,DataDictCategory type) {
		String hql = "from DataDict where category= :type and name like :gradeName";
		Map<String, Object> params = Maps.newHashMap();
		params.put("type", type);
		params.put("gradeName",gradeName+"%");
		List<DataDict> list= findAllByHQL(hql,params);
		String ids="";		
		for(DataDict dataDict:list){
			ids=ids+"'"+dataDict.getId()+"',";
		}
		if(ids.length()>0){
			ids=ids.substring(0, ids.length()-1);
			return ids;
		}
		return null;
	}
	
	@Override
	public List<DataDict> getDataDictListByCategory(DataDictCategory type) {
		String hql = "from DataDict where category= :type ";
		Map<String, Object> params = Maps.newHashMap();
		params.put("type", type);
		List<DataDict> list= findAllByHQL(hql,params);		 
		return list;
	}

	@Override
	public DataDict getDataDictByName(String name, DataDictCategory type) {
		String hql = "from DataDict where category= :type and name = :name ";
		Map<String, Object> params = Maps.newHashMap();
		params.put("type", type);
		params.put("name",name);
		List<DataDict> list= findAllByHQL(hql,params);
		return list.get(0);
	}

    @Override
    public List<DataDict> listDataDictsByIds(String[] ids) {
        String hql = "from DataDict where id in (:ids) ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("ids", ids);
        return findAllByHQL(hql,params);
    }
	
}
