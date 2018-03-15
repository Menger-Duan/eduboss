package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.common.RoleCode;
import com.eduboss.dao.RoleDao;
import com.eduboss.domain.Role;

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
@Repository("RoleDao")
public class RoleDaoImpl extends GenericDaoImpl<Role, String> implements RoleDao {

	private static final Logger log = LoggerFactory.getLogger(RoleDaoImpl.class);
	// property constants

	@Override
	public List<Role> findRoleByCode(RoleCode roleCode) {
		return super.findByCriteria(Expression.eq("roleCode", roleCode));
	}
	
	@Override
	public List<Role> findRoleByName(String roleName) {
		return super.findByCriteria(Expression.eq("name", roleName));
	}

    @Override
    public List<Role> getUserRoleList(String userId) {
		String sql = "select r.* from role r left join user_organization_role uor on r.id = uor.role_id where uor.user_id = :userId";
		Map param = new HashMap();
		param.put("userId",userId);
        return this.findBySql(sql,param);
    }
}
