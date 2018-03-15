package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.SalaryDetailDao;
import com.eduboss.dao.impl.BasicGenericDaoImpl.QueryTypeEnum;
import com.eduboss.domain.Organization;
import com.eduboss.domain.SalaryDetail;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SalaryDetailVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.utils.CommonUtil;
import com.eduboss.utils.StringUtil;

@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class SalaryDetailDaoImpl extends GenericDaoImpl<SalaryDetail, String> implements SalaryDetailDao {

	@Override
	public DataPackage getsalaryDetailList(DataPackage dataPackage,
			SalaryDetailVo salaryDetailVo, Map<String, Object> params) {
		Map<String, Object> hqlParams = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		OrganizationDao organizationDao = (OrganizationDao) params.get("organizationDao");
		User currentUser = (User) params.get("currentUser");
		
		hql.append(" from SalaryDetail where 1=1 ");
		
		if(StringUtil.isNotBlank(params.get("startDate").toString())){
			hql.append(" and substring(salaryTime,1,10) >= :startDate ");
			hqlParams.put("startDate", params.get("startDate"));
		}
		if(StringUtil.isNotBlank(params.get("endDate").toString())){
			hql.append(" and substring(salaryTime,1,10) <= :endDate ");
			hqlParams.put("endDate", params.get("endDate"));
		}
		if(StringUtil.isNotBlank(params.get("staff").toString())){
			hql.append(" and staff.name like :staff ");
			hqlParams.put("staff", "%" + params.get("staff") + "%");
		}
		Organization org = null;
		if(StringUtil.isNotBlank(params.get("organizationId").toString())){
			org = organizationDao.findById(params.get("organizationId").toString());
			hql.append(" and organization.id in (select id from Organization where orgLevel like :orgLevel1) ");
			hqlParams.put("orgLevel1", org.getOrgLevel() + "%");
		}		
		//数据层级限定 根据操作者的组织机构级别来判定查阅数据的权限
		org = organizationDao.findById(currentUser.getOrganizationId());
		if(org != null){
			hql.append(" and organization.id in (select id from Organization where orgLevel like :orgLevel2) ");
			hqlParams.put("orgLevel2", org.getOrgLevel() + "%");
		}
		
		hql.append(" order by modifyTime desc, id desc ");
		return super.findPageByHQL(hql.toString(), dataPackage, true, hqlParams);
	}
	
	/**
	 * extraHql : role_ql_config表的权限控制
	 */
	@Override
	public List<User> getLimitUserAutoComplate(String term, String extraHql) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from User u where 1=1 ");
        hql.append(" and enableFlg <> 1");
		hql.append(" and  ( userId like :userId ");
		params.put("userId", "%" + term + "%");
		hql.append(" or   name like :name ) ");
		params.put("name", "%" + term + "%");
		if(StringUtil.isNotBlank(extraHql)){
			hql.append(extraHql);
		}
		return findLimitHql4User(hql.toString(),CommonUtil.autoComplateLimit, params);
	}

	public List<User> findLimitHql4User(String hql, int limit, Map<String, Object> params) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql).setMaxResults(limit);
		super.setParamsIfNotNull(QueryTypeEnum.HQL, params, q);
		return q.list();
	}
	
}
