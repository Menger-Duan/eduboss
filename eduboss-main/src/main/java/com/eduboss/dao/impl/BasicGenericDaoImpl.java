package com.eduboss.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.eduboss.dao.BasicGenericDAO;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.MD5;
import com.eduboss.utils.PropertiesUtils;


/**
 * @classname	GenericDaoImpl.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2013-3-15 12:34:53
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */
/**
 * @author ran
 *
 * @param <T>
 * @param <ID>
 */

public class BasicGenericDaoImpl<T, ID extends Serializable> implements BasicGenericDAO<T, ID> {

	public enum QueryTypeEnum {
		HQL,
		SQL;
	}
	
	enum ReturnTypeEnum {
		T,
		MAP,
		EXCUTE;
	}
	
	private Class<T> persistentClass;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
    
	@SuppressWarnings("unchecked")
	public BasicGenericDaoImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}
	
	@Override
	public void save(T transientInstance) {
		getHibernateTemplate().saveOrUpdate(transientInstance);
	}
	
	@Override
	public void delete(T persistentInstance) {
		getHibernateTemplate().delete(persistentInstance);
	}
	
	@Override
	public void deleteAll(Collection<T> collection) {
		getHibernateTemplate().deleteAll(collection);
	}
	
	@Override
	public T merge(T detachedInstance) {
		getHibernateTemplate().merge(detachedInstance);
		return detachedInstance;
	}

	@Override
	public T findById(ID id) {
		T t=getHibernateTemplate().get(getPersistentClass(), id);
		return t;
	}
	
	@Override
	public T findById(int id) {
		T t=getHibernateTemplate().get(getPersistentClass(), id);
		return t;
	}
	
	@Override
	public void flush() {
		getHibernateTemplate().flush();
	}

	@Override
	public void clear() {
		getHibernateTemplate().clear();
	}

	@Override
	public void commit() {
		getHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}

	@Override
	public Session getCurrentSession() {
		return getHibernateTemplate().getSessionFactory().getCurrentSession();
	}
	
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public void setHibernateTemplate(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 根据sql分页查询
	 */
	/*@Override
	public DataPackage findPageBySql(String sql, DataPackage dp, boolean useCount, Object... params) {
		Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.T, params);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
		dp.setDatas(q.list());
		if(useCount){
			dp.setRowCount(findCountSql("select count(*) " + sql.substring(sql.toLowerCase().indexOf("from")))));
		}
		return dp;
	}*/
	
	/**
	 * 根据sql分页查询
	 */
	@Override
	public DataPackage findPageBySql(String sql, DataPackage dp, boolean useCount, Map<String, ?> params) {
		Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.T, params);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
		dp.setDatas(q.list());
		
		if(useCount){
			dp.setRowCount(findCountSql("select count(*) " + sql.substring(sql.toLowerCase().indexOf("from")), params));
		}
		return dp;
	}
	
	/**
	 * 根据sql查询列表
	 */
	/*@Override
	@SuppressWarnings("unchecked")
	public List<T> findBySql(String sql, Object... params) {
		Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.T, params);
		q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
		return q.list();
	}*/
	
	/**
	 * 根据sql查询列表
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findBySql(String sql, Map<String, ?> params) {
		Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.T, params);
		q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
		return q.list();
	}
	
	/**
	 * 根据sql查询map列表
	 */
	/*@Override
	@SuppressWarnings("unchecked")
	public List<Map<Object, Object>> findMapBySql(String sql, Object... params) {
		try{
			Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.MAP, params);
			q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
			return q.list();
		} catch(GenericJDBCException e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_ERROR, "系统繁忙，请稍后再试！");
		}
	}*/
	
	/**
	 * 根据sql查询map列表
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<Object, Object>> findMapBySql(String sql, Map<String, ?> params) {
		try{
			Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.MAP, params);
			q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
			return q.list();
		} catch(GenericJDBCException e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_ERROR, "系统繁忙，请稍后再试！");
		}
	}
	
	/**
	 * 根据sql分页查询map列表
	 */
	/*@Override
	@SuppressWarnings("unchecked")
	public List<Map<Object, Object>> findMapOfPageBySql(String sql,DataPackage dp, Object... params) {
		try{
			Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.MAP, params);
			q.setFirstResult(dp.getPageNo()*dp.getPageSize());
			q.setMaxResults(dp.getPageSize());
			q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
			return q.list();
		} catch(GenericJDBCException e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_ERROR, "系统繁忙，请稍后再试！");
		} 
	}*/
	
	/**
	 * 根据sql分页查询map列表
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<Object, Object>> findMapOfPageBySql(String sql,DataPackage dp, Map<String, ?> params) {
		try{
			Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.MAP, params);
			q.setFirstResult(dp.getPageNo()*dp.getPageSize());
			q.setMaxResults(dp.getPageSize());
			q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
			return q.list();
		} catch(GenericJDBCException e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_ERROR, "系统繁忙，请稍后再试！");
		} 
	}
	
	
	/**
	 * 根据sql分页查询map列表 返回dp
	 */
	/*@Override
	public DataPackage findMapPageBySQL(String sql, DataPackage dp, boolean useCount, Object... params) {
		Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.MAP, params);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
		dp.setDatas(q.list());
		if(useCount){
			dp.setRowCount(findCountSql("select count(*) " + sql.substring(sql.toLowerCase().indexOf("from")))));
		}
		return dp;
	}*/
	
	/**
	 * 根据sql分页查询map列表 返回dp
	 */
	@Override
	public DataPackage findMapPageBySQL(String sql, DataPackage dp, boolean useCount, Map<String, ?> params) {
		Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.MAP, params);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
		dp.setDatas(q.list());
		if(useCount){
			dp.setRowCount(findCountSql("select count(*) " + sql.substring(sql.toLowerCase().indexOf("from")), params));
		}
		return dp;
	}
	
	/**
	 * 根据sql计算返回记录数
	 */
	/*@Override
	public int findCountSql(String sql, Object... params){
		try {
			Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.EXCUTE, params);
			if (q.list().size()>0){
				return Integer.valueOf(q.list().get(0)+"");
			}else {
				return 0;
			}

		} catch(GenericJDBCException e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_ERROR, "系统繁忙，请稍后再试！");
		}
	}*/
	
	/**
	 * 根据sql计算返回记录数
	 */
	@Override
	public int findCountSql(String sql, Map<String, ?> params){
		return findCountSql(sql, params, 0);
	}
	
	@Override
    public int findCountSql(String sql, Map<String, ?> params, int cacheTime){
        try {
            String cacheKey = null;
            if(cacheTime > 0) {
                cacheKey = MD5.getMD5(sql + params.toString());
                String cacheData = JedisUtil.get(cacheKey);
                if(StringUtils.isNotBlank(cacheData)) {
                    return Integer.valueOf(cacheData);
                }
            }
            
            Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.EXCUTE, params);
            if (q.list().size()>0){
                String count = q.list().get(0)+"";
                if(cacheTime > 0) {
                    JedisUtil.set(cacheKey.getBytes(), count.getBytes(), cacheTime);
                }
                return Integer.valueOf(count);
            }else {
                return 0;
            }

        } catch(GenericJDBCException e) {
            e.printStackTrace();
            throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_ERROR, "系统繁忙，请稍后再试！");
        }
    }
	
/*	@Override
	public void excuteSql(String sql, Object... params) {
		Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.EXCUTE, params);
		q.executeUpdate();
	}*/
	
	@Override
	public void excuteSql(String sql, Map<String, ?> params) {
		Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.EXCUTE, params);
		q.executeUpdate();
	}
	
	/**
	 * 根据hql分页查询 返回dp
	 */
	/*@Override
	public DataPackage findPageByHQL(String hql, DataPackage dp, boolean useCount, Object... params) {
		Query q = this.prepareQuery(hql, params);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setDatas(q.list());
		if(useCount){
			dp.setRowCount(findCountHql("select count(*) " + hql.substring(hql.indexOf("from") >= 0 ? hql.indexOf("from") : hql.indexOf("FROM"))));
		}
		return dp;
	}*/
	
	/**
	 * 根据hql分页查询 返回dp
	 */
	@Override
	public DataPackage findPageByHQL(String hql, DataPackage dp, boolean useCount, Map<String, ?> params) {
		Query q = this.prepareQuery(hql, params);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setDatas(q.list());
		if(useCount){
			dp.setRowCount(findCountHql("select count(*) " + hql.substring(hql.indexOf("from") >= 0 ? hql.indexOf("from") : hql.indexOf("FROM")), params));
		}
		return dp;
	}
	
	/*@Override
	@SuppressWarnings("unchecked")
	public List<T> findAllByHQL(String hql, Object... params) {
		Query q = this.prepareQuery(hql, params);
		return q.list();
	}*/
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAllByHQL(String hql, Map<String, ?> params) {
		Query q = this.prepareQuery(hql, params);
		return q.list();
	}
	
	/**
	 * 根据hql查询一条符合条件的记录
	 */
	/*@Override
	@SuppressWarnings("unchecked")
	public T findOneByHQL(String hql, Object... params) {
		Query q = this.prepareQuery(hql, params);
		if(q.list()!=null && q.list().size()>0){
			return (T) q.list().get(0);
		}
		return null;
	}*/
	
	/**
	 * 根据hql查询一条符合条件的记录
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T findOneByHQL(String hql, Map<String, ?> params) {
		Query q = this.prepareQuery(hql, params);
		if(q.list()!=null && q.list().size()>0){
			return (T) q.list().get(0);
		}
		return null;
	}
	
	/**
	 * 根据hql查询limit条记录
	 */
	/*@Override
	@SuppressWarnings("unchecked")
	public List<T> findLimitHql(String hql, int limit, Object... params) {
		Query q = this.prepareQuery(hql, params);
		q.setMaxResults(limit);
		return q.list();
	}*/
	
	/**
	 * 根据hql查询limit条记录
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findLimitHql(String hql, int limit, Map<String, ?> params) {
		Query q = this.prepareQuery(hql, params);
		q.setMaxResults(limit);
		return q.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findLimitSql(String sql, int limit, Map<String, ?> params) {
		 Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, ReturnTypeEnum.T, params);
		q.setMaxResults(limit);
		return q.list();
	}
	
	/**
	 * 根据hql计算返回记录数
	 */
	/*@Override
	public int findCountHql(String countHql, Object... params) {
		Query q = this.prepareQuery(countHql, params);
		Object returnObj = q.list().listIterator().next();
		if(returnObj instanceof BigDecimal) {
			return ((BigDecimal) returnObj).intValue();
		} else if(returnObj == null){
			return 0;
		} else {
			return ((Long)returnObj).intValue();
		}
	}*/
	
	/**
	 * 根据hql计算返回记录数
	 */
	@Override
	public int findCountHql(String countHql, Map<String, ?> params) {
		Query q = this.prepareQuery(countHql, params);
		Object returnObj = q.list().listIterator().next();
		if(returnObj instanceof BigDecimal) {
			return ((BigDecimal) returnObj).intValue();
		} else if(returnObj == null){
			return 0;
		} else {
			return ((Long)returnObj).intValue();
		}
	}
	
	/**
	 * 根据hql求和
	 */
	/*@Override
	public double findSumHql(String sumHql, Object... params) {
		Object returnObj = null;
		Query q = this.prepareQuery(sumHql, params);
		returnObj = q.list().listIterator().next();
		if(returnObj instanceof BigDecimal) {
			return ((BigDecimal) returnObj).doubleValue();
		} else if(returnObj == null){
			return 0;
		} else {
			return ((Double)returnObj).doubleValue();
		}
	}*/
	
	/**
	 * 根据hql求和
	 */
	@Override
	public double findSumHql(String sumHql, Map<String, ?> params) {
		Object returnObj = null;
		Query q = this.prepareQuery(sumHql, params);
		returnObj = q.list().listIterator().next();
		if(returnObj instanceof BigDecimal) {
			return ((BigDecimal) returnObj).doubleValue();
		} else if(returnObj == null){
			return 0;
		} else {
			return ((Double)returnObj).doubleValue();
		}
	}

	@Override
	public double findSumSql(String sql, Map<String, ?> params) {
		Query q = this.prepareQuery(sql, QueryTypeEnum.SQL, null, params);
		Object returnObj = q.list().listIterator().next();
		if(returnObj instanceof BigDecimal) {
			return ((BigDecimal) returnObj).doubleValue();
		} else if(returnObj == null){
			return 0;
		} else {
			return ((Double)returnObj).doubleValue();
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findByExample(T example) {
		return getHibernateTemplate().findByExample(example);
	}
	
	/**
	 * 执行hql
	 */
	/*@Override
	public int excuteHql(String hql, Object... params) {
		Query q = this.prepareQuery(hql, params);
		return q.executeUpdate();
	}*/
	
	/**
	 * 执行hql
	 */
	@Override
	public int excuteHql(String hql, Map<String, ?> params) {
		Query q = this.prepareQuery(hql, params);
		return q.executeUpdate();
	}

	@Override
	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate template) {
		this.hibernateTemplate = template;
	}
	
	/**
	 * 准备query对象
	 * @param hql
	 * @param params
	 * @return
	 */
	/*private Query prepareQuery(String hql,  Object... params) {
		return this.prepareQuery(hql, QueryTypeEnum.HQL, ReturnTypeEnum.T, params);
	}*/
	
	/**
	 * 准备query对象
	 * @param hql
	 * @param params
	 * @return
	 */
	private Query prepareQuery(String hql,  Map<String, ?> params) {
		return this.prepareQuery(hql, QueryTypeEnum.HQL, ReturnTypeEnum.T, params);
	}

	/**
     * 准备query对象
     * @param hql
     * @param qType
     * @param rType
     * @param params
     * @return
     */
   /* private Query prepareQuery(String hql, QueryTypeEnum qType, ReturnTypeEnum rType, Object... params) {
    	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
    	Query query = null;
    	if (qType == QueryTypeEnum.HQL) {
    		query = session.createQuery(hql);
    	} else {
    		if (rType == ReturnTypeEnum.T) {
				query = session.createSQLQuery(hql).addEntity(persistentClass);
    		} else if (rType == ReturnTypeEnum.MAP) {
				query = session.createSQLQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    		} else {
				query = session.createSQLQuery(hql);
    		}
    	}
    	if (params != null && params.length > 0) {
    		for (int i = 0; i < params.length; i++) {
    			query.setParameter(i, params[i]);
    		}
    	}
    	return query;
    } */

    /**
     * 准备query对象
     * @param hql
     * @param qType
     * @param rType
     * @param params
     * @return
     */
    private Query prepareQuery(String hql, QueryTypeEnum qType, ReturnTypeEnum rType, Map<String, ?> params) {
    	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
    	Query query = null;
    	if (qType == QueryTypeEnum.HQL) {
    		query = session.createQuery(hql);
    	} else {
    		if (rType == ReturnTypeEnum.T) {
				query = session.createSQLQuery(hql).addEntity(persistentClass);
    		} else if (rType == ReturnTypeEnum.MAP) {
				query = session.createSQLQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    		} else {
				query = session.createSQLQuery(hql);
    		}
    	}
    	this.setParamsIfNotNull(qType, params, query);
    	return query;
    }
    
    private void setParams(QueryTypeEnum qType, Map<String, ?> params, Query query) {
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			Object value = params.get(key);
			if (value instanceof Collection) {
				query.setParameterList(key, (Collection<?>) value);
			} else if (value.getClass().isArray()) { 
				query.setParameterList(key, (Object[]) value); 
			} else if(value instanceof Enum && qType == QueryTypeEnum.SQL){
				query.setParameter(key, value.toString());
			}else{
				query.setParameter(key, value);
			}
		}
	}
    
    /**if params 不为空，则set入Query*/
    @Override
	public void setParamsIfNotNull(QueryTypeEnum qType, Map<String, ?> params, Query query) {
		if (params != null) {
			setParams(qType, params, query);
		}
	}
	
	
	/**
	 * 要删掉的
	 */
	/*@Override
	public DataPackage findPageBySql(String hql,DataPackage dp) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql).addEntity(persistentClass);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
		dp.setDatas(q.list());
		return dp;
	}
	@Override
	public DataPackage findPageBySql(String sql,DataPackage dp,boolean useCount) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(persistentClass);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
		dp.setDatas(q.list());
		if(useCount){
			StringBuilder countSql = new StringBuilder();
			countSql.append("select count(*) from (");
			countSql.append(sql);
			countSql.append(") countA");
			int resultCount = this.findCountSql(countSql.toString());
			dp.setRowCount(resultCount);
		}
		return dp;
	}
	@Override
	public List findBySql(String sql) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(persistentClass);
		q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
		return q.list();
	}
	@Override
	public List findMapBySql(String sql) {
		try{
			Query q = getHibernateTemplate().getSessionFactory().getCurrentSession()
					.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
			return q.list();
		} catch(GenericJDBCException e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_ERROR, "系统繁忙，请稍后再试！");
		}
	}
	@Override
	public List findMapOfPageBySql(String sql,DataPackage dp) {
		try{
			Query q = getHibernateTemplate().getSessionFactory().getCurrentSession()
					.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			q.setFirstResult(dp.getPageNo()*dp.getPageSize());
			q.setMaxResults(dp.getPageSize());
			q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
			return q.list();
		} catch(GenericJDBCException e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_ERROR, "系统繁忙，请稍后再试！");
		} 
	}
	@Override
	public DataPackage findMapPageBySQL(String sql,DataPackage dp) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		q.setTimeout(PropertiesUtils.getIntValue("timeOut"));//加入超时时间，2016-07-12
		dp.setDatas(q.list());
		return dp;
	}
	@Override
	public int findCountSql(String sql){
		try {
			Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			return Integer.valueOf(q.list().get(0)+"");
		} catch(GenericJDBCException e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.SOCKET_TIMEOUT_ERROR, "系统繁忙，请稍后再试！");
		}
	}
	@Override
	public DataPackage findPageByHQL(String hql,DataPackage dp) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setDatas(q.list());
		dp.setRowCount(findCountHql("select count(*) " + hql));
		return dp;
	}
	@Override
	public DataPackage findPageByHQL(String hql, DataPackage dp,
			boolean useCount) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setDatas(q.list());
		dp.setRowCount(findCountHql("select count(*) " + hql.substring(hql.indexOf("from"))));
		return dp;
	}
	@Override
	public List<T> findAllByHQL(String hql,DataPackage dp) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		return q.list();
	}
	@Override
	public List<T> findAllByHQL(String hql) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		return q.list();
	}
	 
	 *用于hql只取一条结果的时候
	 
	@Override
	public T findOneByHQL(String hql) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		if(q.list()!=null && q.list().size()>0){
			return (T) q.list().get(0);
		}
		return null;
	}
	@Override
	public List<T> findLimitHql(String hql,int limit) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql).setMaxResults(limit);
		return q.list();
	}
	@Override
	public void excuteSql(String sql) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		q.executeUpdate();
	}
	@Override
	public int excuteHql(String hql) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		return q.executeUpdate();
	}
	@Override
	public int findCountHql(String countSql) {
		Object returnObj = this.getHibernateTemplate().find(countSql).listIterator().next();
		if(returnObj instanceof BigDecimal) {
			return ((BigDecimal) returnObj).intValue();
		} else if(returnObj == null){
			return 0;
		} else {
			return ((Long)returnObj).intValue();
		}
	}
	@Override
	public double findSumHql(String sumSql) {
		Object returnObj = null;
		returnObj = this.getHibernateTemplate().find(sumSql).listIterator().next();
		if(returnObj instanceof BigDecimal) {
			return ((BigDecimal) returnObj).doubleValue();
		} else if(returnObj == null){
			return 0;
		} else {
			return ((Double)returnObj).doubleValue();
		}
	}
    @Override
	public List<T> findPageByHQL(String hql, int pageNo, int pageSize ) {
    	String excuteSql = hql;
        Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(excuteSql);
        q.setFirstResult(pageNo*pageSize);
        q.setMaxResults(pageSize);
        return q.list();
    }*/
    
}
