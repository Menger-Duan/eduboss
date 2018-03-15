package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.ResourceDao;
import com.eduboss.domain.Resource;

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
@Repository("ResourceDao")
public class ResourceDaoImpl extends GenericDaoImpl<Resource, String> implements ResourceDao {

	public List<Resource> findResourceByRoleIdArray(String[] roleIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (roleIds.length > 0) {
			String hql = " from Resource where 1=1 ";
			if (roleIds.length > 1) {
				hql += " and id in (select roleRes.id.resId from RoleResource roleRes where roleRes.id.roleId in (:roleIds)) ";
				params.put("roleIds", roleIds);
			} else {
				hql += " and id in (select roleRes.id.resId from RoleResource roleRes where roleRes.id.roleId =:roleId) ";
				params.put("roleId", roleIds[0]);
			}
			return super.findAllByHQL(hql, params);
		} 
		return null;
	}
	
	
	@Override
	public List<Resource> findAllResourcesByUserId(String userId,String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql =new StringBuffer();
		sql.append(" select distinct r.* from user u ");
		sql.append(" left join user_organization_role ur on ur.user_ID=u.USER_ID");
		sql.append(" left join role_resource rr on ur.role_ID=rr.roleID");
		sql.append(" left join resource r on r.ID=rr.resourceID");
		
		sql.append(" where 1=1 AND r.ID IS NOT NULL ");
		if(StringUtils.isNotBlank(userId)){
			sql.append(" and u.user_id = :userId ");
			params.put("userId", userId);
		}
		
		if(StringUtils.isNotBlank(type)){
			sql.append(" and r.rtype = :type ");
			params.put("type", type);
		}
		
		return super.findBySql(sql.toString(), params);
	}

	@Override
	public List<Resource> findResourceByRoleIds(String roleIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select * from resource where 1=1 ";
		if (roleIds.length() > 0) {
			String[] roleIdArr = roleIds.split(",");
			if (roleIdArr.length > 1) {
				sql += "and id in( select resourceID from role_resource where roleID in (:roleIds)) ";
				params.put("roleIds", roleIdArr);
			} else {
				sql += "and id in( select resourceID from role_resource where roleID = :roleId) ";
				params.put("roleId", roleIdArr[0]);
			}
			return super.findBySql(sql, params);
		}
		return null;
	
	}

    @Override
    public List<Resource> getResourcesByEmployeeNo(String employeeNo, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql =new StringBuffer();
		sql.append(" select distinct r.* from user u ");
		sql.append(" left join user_organization_role ur on ur.user_ID=u.USER_ID");
		sql.append(" left join role_resource rr on ur.role_ID=rr.roleID");
		sql.append(" left join resource r on r.ID=rr.resourceID");

		sql.append(" where 1=1 AND r.ID IS NOT NULL ");
		if(StringUtils.isNotBlank(employeeNo)){
			sql.append(" and u.employee_no = :employeeNo ");
			params.put("employeeNo", employeeNo);
		}

		if(StringUtils.isNotBlank(type)){
			sql.append(" and r.rtype = :type ");
			params.put("type", type);
		}

		return super.findBySql(sql.toString(), params);
    }

}
