package com.eduboss.dao.impl;

import com.eduboss.dao.GenericDAO;
import com.eduboss.domain.RoleQLConfig;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.eduboss.dao.GenericDAO;
import com.eduboss.domain.RoleQLConfig;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;
import com.eduboss.dao.GenericDAO;
import com.eduboss.domain.RoleQLConfig;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;


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

public class GenericDaoImpl<T, ID extends Serializable> extends BasicGenericDaoImpl<T, Serializable> implements GenericDAO<T, ID> {

	private final static  Logger log =  Logger.getLogger(GenericDaoImpl.class);

	private Class<T> persistentClass;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
    
    @Autowired
    private RoleQLConfigService roleQLConfigService;

	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@Override
	public List<T> findAll() {
		return findByCriteria();
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
	@Override
	public List<T> findByCriteria(Criterion... criterion) {
		return findByCriteria(new ArrayList<Order>(), criterion);
	}
	
	@Override
	public List<T> findByCriteria(List<Order> orderList,Criterion... criterion) {
		DetachedCriteria crit =
				DetachedCriteria.forClass(getPersistentClass());
		if (criterion != null) {
			for (Criterion c : criterion) {
				crit.add(c);
			}
		}
		if (orderList != null) {
			for (Order o : orderList) {
				crit.addOrder(o);
			}
		}
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria executableCriteria  = crit.getExecutableCriteria(session);
		List list = executableCriteria.list();
		List<T> returnList = new ArrayList<T>();
		for (Object object : list) {
			if (object != null) {
				returnList.add((T)object);
			}
		}
		return returnList;
			
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public DataPackage findPageByCriteria(DataPackage dataPackage, List<Order> orderList, Criterion... criterion) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (criterion != null) {
			for (Criterion c : criterion) {
				criterionList.add(c);
			}
		}
		return findPageByCriteria( dataPackage, orderList, criterionList) ;
	}
	
	@Override
	public DataPackage findPageByCriteria(DataPackage dataPackage, List<Order> orderList, List<Criterion> criterionList)  {
		return findPageByCriteria( dataPackage, orderList, criterionList, null);
	}
	
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DataPackage findPageByCriteria(DataPackage dataPackage, List<Order> orderList, List<Criterion> criterion, Map<String, String> aliasMap) {
        // 附加角色权限配置条件
        criterion.addAll(buildCriterions(roleQLConfigService.findAllByNameAndType(getPersistentClass().getName(),"criteria")));
        if(StringUtils.isNotBlank(dataPackage.getRoleQLConfigName())){
            criterion.addAll(buildCriterions(roleQLConfigService.findAllByNameAndType(dataPackage.getRoleQLConfigName(),"criteria")));
        }
		DetachedCriteria crit = DetachedCriteria.forClass(getPersistentClass());
		if (criterion != null) {
			for (Criterion c : criterion) {
				crit.add(c);
			}
		}
		
		if (orderList != null) {
			for (Order o : orderList) {
				crit.addOrder(o);
			}
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria executableCriteria  = crit.getExecutableCriteria(session);
		
    	//添加关联查询声明，对于针对关联下一层的对象的属性的查询的，自动添加声明，多层关联请自己添加声明map或写hql
    	if (aliasMap != null) {
    		for (String key : aliasMap.keySet()) {
    			executableCriteria.createAlias(key, aliasMap.get(key));
    		}
    	} else if (criterion != null) {
    		Map map=new HashMap<String, String>();
    		for (Criterion c : criterion) {
    			String[] sqlStrings  = c.toString().split(" ");
    			sqlStrings[0] = sqlStrings[0].split("=")[0];
    			if(sqlStrings[0].indexOf(".") == -1 || (sqlStrings[0].length()>3 && sqlStrings[0].substring(0,3).equals("id.")))continue; // 属性中不包含.则不需要别名
    			String attName =  sqlStrings[0].split("[.]")[0];
    			if('(' == attName.charAt(0)){attName = attName.substring(1);} // conjunction和dusjunction
    			if(attName.endsWith(".id"))continue; // id不需要别名
    			if(attName.indexOf(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG) == -1){ // 直接下层
    				if (!map.containsKey(attName) && !"this_".equals(attName)) {
    					map.put(attName, attName);
    					executableCriteria.createAlias(attName, attName,CriteriaSpecification.LEFT_JOIN);
    				}
    			}else{ // 2层以上循环创建别名
    				/**
    				 * 2层以后对象使用 HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG 常量进行连接
    				 * 例如常量为 _ 时，a.b.c.d 将被命名为 a_b_c_d
    				 * 查询 d 对象的 name 属性时请在criteria条件拼接时使用a_b_c_d.name作为Hibernate Restrictions的属性
    				 * code : Restrictions.like("a_b_c_d.name","like匹配字符串",MatchMode.ANYWHERE))
    				 */
    				String[] attNames = attName.split(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG);
    				if (!map.containsKey(attNames[0])) {
    					map.put(attNames[0], attNames[0]);
    					executableCriteria.createAlias(attNames[0], attNames[0]);
    				}
    				for(int i = 1; i < attNames.length; i++){
    					StringBuffer alias = new StringBuffer(attNames[0]);
    					for (int j = 1; j < i + 1; j++) {
    						alias.append(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG).append(attNames[j]);
    					}
    					StringBuffer aliasKey = new StringBuffer(alias.toString());
    					aliasKey.setCharAt(aliasKey.lastIndexOf(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG), '.');
    					if (!map.containsKey(aliasKey.toString())) {
    						map.put(aliasKey.toString(), alias.toString());
    						executableCriteria.createAlias(aliasKey.toString(), alias.toString());
    					}
    				}
    			}
    		}
    		if(orderList != null) {
    			for (Order o : orderList) {//2层排序加别名
    				String[] sqlStrings = o.toString().split(" ");
    				if (sqlStrings[0].indexOf(".") == -1 || (sqlStrings[0].length()>3 && sqlStrings[0].substring(0,3).equals("id."))) continue; // 属性中不包含.则不需要别名
    				String attName = sqlStrings[0].split("[.]")[0];
    				if (attName.toLowerCase().endsWith("id")) continue; // id不需要别名
    				if (attName.indexOf(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG) == -1) { // 直接下层
    					if (!map.containsKey(attName)) {
    						map.put(attName, attName);
    						executableCriteria.createAlias(attName, attName);
    					}
    				}
    			}
    		}
    	}
    	
    	 Object rowCount = executableCriteria.setProjection(Projections.rowCount()).uniqueResult();
    	 int totalCount = 0;
    	 if (rowCount != null) {
    		 totalCount = Integer.parseInt(String.valueOf(rowCount));  
    	 }
    	 executableCriteria.setProjection(null);
    	 executableCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
    	 int startIndex = dataPackage.getPageNo() * dataPackage.getPageSize();
    	 executableCriteria.setFirstResult(startIndex);
    	 executableCriteria.setMaxResults(dataPackage.getPageSize());
    	 List datas = executableCriteria.list();
    	 dataPackage.setRowCount(totalCount);
    	 dataPackage.setDatas(datas);
    	 return dataPackage;
		
	}
	
	@Override
	public DataPackage findPageByCriteria(DataPackage dataPackage) {
		return findPageByCriteria(dataPackage,null);
	}

	@Override
	public List<T> findAllByCriteria(List<Criterion> list) {
		return this.findAllByCriteria(list, new ArrayList<Order>());
	}
	
	@Override
	public List<T> findAllByCriteria(List<Criterion> criterion,List<Order> orderList){
		DetachedCriteria crit = DetachedCriteria.forClass(getPersistentClass());
			for (Criterion c : criterion) {
				crit.add(c);
			}
			if (orderList != null) {
				for (Order o : orderList) {
					crit.addOrder(o);
				}
			}
			
		//添加关联查询声明，对于针对关联下一层的对象的属性的查询的，自动添加声明，多层关联请自己添加声明map或写hql
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria executableCriteria  = crit.getExecutableCriteria(session);
		if (criterion != null) {
			Map map=new HashMap<String, String>();
			for (Criterion c : criterion) {
				String[] sqlStrings  = c.toString().split(" ");
				sqlStrings[0] = sqlStrings[0].split("=")[0];
				if(sqlStrings[0].indexOf(".") == -1 || (sqlStrings[0].length()>3 && sqlStrings[0].substring(0,3).equals("id.")))continue; // 属性中不包含.则不需要别名
				String attName =  sqlStrings[0].split("[.]")[0];
				if(attName.startsWith("("))attName = attName.substring(1); // 多条件去除括号
				if(attName.toLowerCase().endsWith("id"))continue; // id不需要别名
				if(attName.indexOf(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG) == -1){ // 直接下层
					if (!map.containsKey(attName) && !"this_".equals(attName)) {
						map.put(attName, attName);
						executableCriteria.createAlias(attName, attName);
					}
				}else{ // 2层以上循环创建别名
					/**
					 * 2层以后对象使用 HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG 常量进行连接
					 * 例如常量为 _ 时，a.b.c.d 将被命名为 a_b_c_d
					 * 查询 d 对象的 name 属性时请在criteria条件拼接时使用a_b_c_d.name作为Hibernate Restrictions的属性
					 * code : Restrictions.like("a_b_c_d.name","like匹配字符串",MatchMode.ANYWHERE))
					 */
					String[] attNames = attName.split(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG);
					if (!map.containsKey(attNames[0])) {
						map.put(attNames[0], attNames[0]);
						executableCriteria.createAlias(attNames[0], attNames[0]);
					}
					for(int i = 1; i < attNames.length; i++){
						StringBuffer alias = new StringBuffer(attNames[0]);
						for (int j = 1; j < i + 1; j++) {
							alias.append(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG).append(attNames[j]);
						}
						StringBuffer aliasKey = new StringBuffer(alias.toString());
						aliasKey.setCharAt(aliasKey.lastIndexOf(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG), '.');
						if (!map.containsKey(aliasKey.toString())) {
							map.put(aliasKey.toString(), alias.toString());
							executableCriteria.createAlias(aliasKey.toString(), alias.toString());
						}
					}
				}
			}
			if(orderList != null) {
				for (Order o : orderList) {//2层排序加别名
					String[] sqlStrings = o.toString().split(" ");
					if (sqlStrings[0].indexOf(".") == -1 || (sqlStrings[0].length()>3 && sqlStrings[0].substring(0,3).equals("id."))) continue; // 属性中不包含.则不需要别名
					String attName = sqlStrings[0].split("[.]")[0];
					if (attName.toLowerCase().endsWith("id")) continue; // id不需要别名
					if (attName.indexOf(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG) == -1) { // 直接下层
						if (!map.containsKey(attName)) {
							map.put(attName, attName);
							executableCriteria.createAlias(attName, attName);
						}
					}
				}
			}
		}
		
		return executableCriteria.list();
	}

	@Override
	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate template) {
		this.hibernateTemplate = template;
	}

	@Override
	public void delete(T persistentInstance) {
		getHibernateTemplate().delete(persistentInstance);
	}
	
	@Override
	public void deleteAll(Collection collection) {
		getHibernateTemplate().deleteAll(collection);
	}


	@Override
	public T findById(int id) {

		T t=getHibernateTemplate().get(getPersistentClass(), id);
		return t;
	}

	@Override
	public T merge(T detachedInstance) {
		getHibernateTemplate().merge(detachedInstance);
		return detachedInstance;
	}

	@Override
	public void save(T transientInstance) {
		getHibernateTemplate().saveOrUpdate(transientInstance);
	}
	
//	@Override
//	public int findCountHql(String countSql) {
//		Object returnObj = this.getHibernateTemplate().find(countSql).listIterator().next();
//		if(returnObj instanceof BigDecimal) {
//			return ((BigDecimal) returnObj).intValue();
//		} else if(returnObj == null){
//			return 0;
//		} else {
//			return ((Long)returnObj).intValue();
//		}
//	}
//	
//	@Override
//	public double findSumHql(String sumSql) {
//		Object returnObj = null;
//		returnObj = this.getHibernateTemplate().find(sumSql).listIterator().next();
//		if(returnObj instanceof BigDecimal) {
//			return ((BigDecimal) returnObj).doubleValue();
//		} else if(returnObj == null){
//			return 0;
//		} else {
//			return ((Double)returnObj).doubleValue();
//		}
//	}

	@Override
	public void commit() {
		getHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}
	
//	public Map<String, Object> findMapBySQL(String sql, Object... args) {
//		return getJdbcTemplate().queryForMap(sql, args);
//	}
//
//	@Override
//	public List<Map<String, Object>> findAllBySQL(String sql, Object... args) {
//		// TODO Auto-generated method stub
//		return getJdbcTemplate().queryForList(sql, args);
//	}


	@Override
	public int findCountByCriteria( List<Criterion> criterion) {
		DetachedCriteria crit = DetachedCriteria.forClass(getPersistentClass());
		if (criterion != null) {
			for (Criterion c : criterion) {
				crit.add(c);
			}
		}
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria executableCriteria  = crit.getExecutableCriteria(session);
		
		//添加关联查询声明，对于针对关联下一层的对象的属性的查询的，自动添加声明，多层关联请自己添加声明map或写hql
		if (criterion != null) {
			Map map=new HashMap<String, String>();
			for (Criterion c : criterion) {
				String[] sqlStrings  = c.toString().split(" ");
				String attName =  sqlStrings[0].split("[.]")[0];
				if (sqlStrings.length > 0 && sqlStrings[0].split("[.]").length == 2 && !map.containsKey(attName) && !"id".equals(attName)) {//只声明一层关联的，多层关联请自己声明或写hql
					map.put(attName, attName);
					executableCriteria.createAlias(attName, attName);
				}
			}
		}
		
		Object rowCount = executableCriteria.setProjection(Projections.rowCount()).uniqueResult();
		int totalCount = 0;
		if (rowCount != null) {
			totalCount = Integer.parseInt(String.valueOf(rowCount));  
		}
		return totalCount;
        
	}

    @Override
    public List<Criterion> buildCriterions(List<RoleQLConfig> configs) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        UserService userService = ApplicationContextUtil.getContext().getBean(UserService.class);
        List<RoleQLConfig> orList = new ArrayList<RoleQLConfig>();
        // and条件直接拼接，or条件先抽离出来再一起合成一个大的and
        for(RoleQLConfig config : configs){
            if(userService.isCurrentUserRoleId(config.getRoleId()) || config.getRoleId() == null || StringUtils.isBlank(config.getRoleId())){
                if("or".equals(config.getJoiner())){
                    orList.add(config);
                }else{
                    criterions.add(buildCriterion(config.getValue()));
                }
            }
        }
        // or条件拼成一个大的and
        if(orList.size() > 0){
            Disjunction disjunction = Restrictions.disjunction();
            for(RoleQLConfig config : orList){
                disjunction.add(buildCriterion(config.getValue()));
            }
            criterions.add(disjunction);
        }
        return criterions;
    }

    /**
     * 表达式构造Criterion
     * LIKE_student.name=xuwen
     * @param expression
     * @return
     */
    @Override
    public Criterion buildCriterion(String expression) {
        int expressionEndIndex = expression.indexOf("_");
        int fieldValueSplitIndex = expression.indexOf("=");
        if(expressionEndIndex == -1 || fieldValueSplitIndex == -1){
            throw new ApplicationException("PropertyMatcher表达式不正确：" + expression);
        }
        String matchType = expression.substring(0,expressionEndIndex);
        String matchValue = expression.substring(fieldValueSplitIndex + 1);
        // 值表达式解析 ${user.userId}
        Matcher matcher = RoleQLConfigService.elExpressionPattern.matcher(matchValue);
        while(matcher.find()){
            String group = matcher.group();
            String key = matcher.group(1);
            matchValue = matchValue.replace(group,roleQLConfigService.getExpressionValue(key).toString());
        }
        String propertyName = expression.substring(expressionEndIndex + 1,fieldValueSplitIndex);
        return buildCriterion(propertyName,matchType,matchValue);
    }

    /**
     * 构造Criterion
     * @param propertyName
     * @param matchType
     * @param matchValue
     * @return
     */
    @Override
    public Criterion buildCriterion(String propertyName, String matchType, Object matchValue) {
        String mutilValueFlag = ",";
        if("EQ".equals(matchType.toUpperCase())){
            return Restrictions.eq(propertyName,matchValue);
        }else if("NE".equals(matchType.toUpperCase())){
            return Restrictions.ne(propertyName,matchValue);
        }else if("LIKE".equals(matchType.toUpperCase())){
            return Restrictions.like(propertyName,matchValue.toString(),MatchMode.ANYWHERE);
        }else if("LLIKE".equals(matchType.toUpperCase())){
            return Restrictions.like(propertyName,matchValue.toString(),MatchMode.START);
        }else if("RLIKE".equals(matchType.toUpperCase())){
            return Restrictions.like(propertyName,matchValue.toString(),MatchMode.END);
        }else if("GT".equals(matchType.toUpperCase())){
            return Restrictions.gt(propertyName,matchValue);
        }else if("LT".equals(matchType.toUpperCase())){
            return Restrictions.lt(propertyName,matchValue);
        }else if("GE".equals(matchType.toUpperCase())){
            return Restrictions.ge(propertyName,matchValue);
        }else if("LE".equals(matchType.toUpperCase())){
            return Restrictions.le(propertyName,matchValue);
        }else if("BETWEEN".equals(matchType.toUpperCase())){
            int index = matchType.indexOf(mutilValueFlag);
            if(index == -1)throw new ApplicationException(propertyName + "的BETWEEN表达式需要分割标示符" + mutilValueFlag);
            return Restrictions.between(propertyName,matchValue.toString().substring(0, index),matchValue.toString().substring(index));
        }else if("IN".equals(matchType.toUpperCase())){
            return Restrictions.in(propertyName,matchValue.toString().split(mutilValueFlag));
        }else if("NIN".equals(matchType.toUpperCase())){
            return Restrictions.not(Restrictions.in(propertyName,matchValue.toString().split(mutilValueFlag)));
        }else if("NULL".equals(matchType.toUpperCase())){
            return Restrictions.isNull(propertyName);
        }else if("NNULL".equals(matchType.toUpperCase())){
            return Restrictions.isNotNull(propertyName);
        }else if("EMPTY".equals(matchType.toUpperCase())){
            return Restrictions.isEmpty(propertyName);
        }else if("NEMPTY".equals(matchType.toUpperCase())){
            return Restrictions.isNotEmpty(propertyName);
        }else if("SQLRESTRICTION".equals(matchType.toUpperCase())){
			return Restrictions.sqlRestriction(matchValue.toString());
		}else{
            throw new ApplicationException(matchType + "类型不匹配或未实现");
        }
    }
    
}
