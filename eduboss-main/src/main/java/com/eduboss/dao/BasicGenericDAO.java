/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eduboss.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.impl.BasicGenericDaoImpl.QueryTypeEnum;
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
public interface BasicGenericDAO<T, ID extends Serializable> {
	
	Session getCurrentSession();
	
	HibernateTemplate getHibernateTemplate();
	
	/**
	 * @param transientInstance
	 */
	void save(T transientInstance);

	/**
	 * @param persistentInstance
	 */
	void delete(T persistentInstance);
	
	/**
	 * @param collection
	 */
	void deleteAll(Collection<T> collection);
	
	/**
	 * @param detachedInstance
	 * @return
	 */
	T merge(T detachedInstance);

	/**
	 * @param id
	 * @return
	 */
	T findById(ID id);

	T findById(int id);
	
	/**
	 */
	void flush();

	/**
	 */
	void clear();
	
	void commit();

	/**
	 * 根据sql分页查询
	 * @param sql
	 * @param dp
	 * @param useCount
	 * @param params
	 * @return
	 */
//	DataPackage findPageBySql(String sql,DataPackage dp, boolean useCount, Object... params);
	
	/**
	 * 根据sql分页查询
	 * @param sql
	 * @param dp
	 * @param useCount
	 * @param params
	 * @return
	 */
	DataPackage findPageBySql(String sql,DataPackage dp, boolean useCount, Map<String, ?> params);
	
	/**
	 * 根据sql查询列表
	 * @param sql
	 * @param params
	 * @return
	 */
//	List<T> findBySql(String sql, Object... params);
	
	/**
	 * 根据sql查询列表
	 * @param sql
	 * @param params
	 * @return
	 */
	List<T> findBySql(String sql, Map<String, ?> params);
	
	/**
	 * 根据sql查询map列表
	 * @param sql
	 * @param params
	 * @return
	 */
//	List<Map<Object, Object>> findMapBySql(String sql, Object... params);
	
	/**
	 * 根据sql查询map列表
	 * @param sql
	 * @param params
	 * @return
	 */
	List<Map<Object, Object>> findMapBySql(String sql, Map<String, ?> params);
	
	/**
	 * 根据sql分页查询map列表
	 * @param sql
	 * @param dp
	 * @param params
	 * @return
	 */
//	List<Map<Object, Object>> findMapOfPageBySql(String sql,DataPackage dp, Object... params);
	
	/**
	 * 根据sql分页查询map列表
	 * @param sql
	 * @param dp
	 * @param params
	 * @return
	 */
	List<Map<Object, Object>> findMapOfPageBySql(String sql,DataPackage dp, Map<String, ?> params);
	
	/**
	 * 根据sql分页查询map列表 返回dp
	 * @param sql
	 * @param dp
	 * @param useCount
	 * @param params
	 * @return
	 */
//	DataPackage findMapPageBySQL(String sql,DataPackage dp, boolean useCount, Object... params);
	
	/**
	 * 根据sql分页查询map列表 返回dp
	 * @param sql
	 * @param dp
	 * @param useCount
	 * @param params
	 * @return
	 */
	DataPackage findMapPageBySQL(String sql,DataPackage dp, boolean useCount, Map<String, ?> params);
	
	/**
	 * 根据sql计算返回记录数
	 * @param sql
	 * @param params
	 * @return
	 */
//	int findCountSql(String sql, Object... params);
	
	/**
	 * 根据sql计算返回记录数
	 * @param sql
	 * @param params
	 * @return
	 */
	int findCountSql(String sql, Map<String, ?> params);
	
	/**
	 * 执行sql
	 * @param sql
	 * @param params
	 */
//	void excuteSql(String sql, Object... params);
	
	/**
	 * 执行sql
	 * @param sql
	 * @param params
	 */
	void excuteSql(String sql, Map<String, ?> params);
	
	/**
	 * 根据hql分页查询 返回dp
	 * @param sql
	 * @param dp
	 * @param useCount
	 * @param params
	 * @return
	 */
//	DataPackage findPageByHQL(String sql, DataPackage dp, boolean useCount, Object... params);
	
	/**
	 * 根据hql分页查询 返回dp
	 * @param sql
	 * @param dp
	 * @param useCount
	 * @param params
	 * @return
	 */
	DataPackage findPageByHQL(String sql, DataPackage dp, boolean useCount, Map<String, ?> params);
	
	/**
	 * 根据hql查询符合条件的所有记录
	 * @param hql
	 * @param params
	 * @return
	 */
//	List<T> findAllByHQL(String hql, Object... params);
	
	/**
	 * 根据hql查询符合条件的所有记录
	 * @param hql
	 * @param params
	 * @return
	 */
	List<T> findAllByHQL(String hql, Map<String, ?> params);
	
	/**
	 * 根据hql查询一条符合条件的记录
	 * @param hql
	 * @param params
	 * @return
	 */
//	T findOneByHQL(String hql, Object... params);
	
	/**
	 * 根据hql查询一条符合条件的记录
	 * @param hql
	 * @param params
	 * @return
	 */
	T findOneByHQL(String hql, Map<String, ?> params);
	
	/**
	 * 根据hql查询limit条记录
	 * @param hql
	 * @param limit
	 * @param params
	 * @return
	 */
//	List<T> findLimitHql(String hql, int limit, Object... params);
	
	/**
	 * 根据hql查询limit条记录
	 * @param hql
	 * @param limit
	 * @param params
	 * @return
	 */
	List<T> findLimitHql(String hql, int limit, Map<String, ?> params);

	List<T> findLimitSql(String hql, int limit, Map<String, ?> params);
	
	/**
	 * 根据hql计算返回记录数
	 * @param hql
	 * @param limit
	 * @param params
	 * @return
	 */
//	int findCountHql(String countHql, Object... params);
	
	/**
	 * 根据hql计算返回记录数
	 * @param hql
	 * @param limit
	 * @param params
	 * @return
	 */
	int findCountHql(String countHql, Map<String, ?> params);
	
	/**
	 * 根据hql求和
	 * @param sumHql
	 * @param params
	 * @return
	 */
//	double findSumHql(String sumHql, Object... params);
	
	/**
	 * 根据hql求和
	 * @param sumHql
	 * @param params
	 * @return
	 */
	double findSumHql(String sumHql, Map<String, ?> params);

	double findSumSql(String sumHql, Map<String, ?> params);
    
	List<T> findByExample(T example);
	
	/**
	 * 执行hql
	 * @param hql
	 * @param params
	 * @return
	 */
//	int excuteHql(String hql, Object... params);
	
	/**
	 * 执行hql
	 * @param hql
	 * @param params
	 * @return
	 */
	int excuteHql(String hql, Map<String, ?> params);
	
	void setParamsIfNotNull(QueryTypeEnum qType, Map<String, ?> params, Query query);

	
    /**
     * @param sql
     * @param params
     * @param cacheTime 缓存时间单位秒s
     * @return
     */
    int findCountSql(String sql, Map<String, ?> params, int cacheTime);
	
	


	
	/**
	 * 要删掉的
	 */
	/**
     * @param hql
     * @param dp
	 * @return
	 */
	/*DataPackage findPageBySql(String hql,DataPackage dp);
	DataPackage findPageBySql(String sql,DataPackage dp,boolean useCount);
	List findBySql(String sql);
	
	*//**���hql��ҳ��ѯ���
     * @param hql
     * @param dp
	 * @return
	 *//*
	DataPackage findPageByHQL(String hql,DataPackage dp);
	DataPackage findPageByHQL(String sql,DataPackage dp,boolean useCount);
	
	*//**
	 * @param hql
     * @param dp
	 * @return
	 *//*
	List<T> findAllByHQL(String hql,DataPackage dp);
	
	List<T> findAllByHQL(String hql);

	int findCountHql(String countHql);
	
	int findCountSql(String sql);
	
	void excuteSql(String hql);
	
	int excuteHql(String hql);
	
	List findMapBySql(String sql);
	
	List findMapOfPageBySql(String sql,DataPackage dp);
	
	DataPackage findMapPageBySQL(String sql,DataPackage dp);

    double findSumHql(String sumSql);
    
    List<T> findLimitHql(String hql,int limit);

    List<T> findPageByHQL(String hql, int pageNo, int pageSize ) ;

	T findOneByHQL(String hql);*/
	
}
