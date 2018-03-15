package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.common.MobileUserType;
import com.eduboss.common.RoleCode;
import com.eduboss.dao.MobileUserDao;
import com.eduboss.dao.RoleDao;
import com.eduboss.domain.MobilePushMsgSession;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.Role;
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
@Repository("MobileUserDao")
public class MobileUserDaoImpl extends GenericDaoImpl<MobileUser, String> implements MobileUserDao {

	private static final Logger log = LoggerFactory.getLogger(MobileUserDaoImpl.class);

	@Override
	public MobileUser findMobileUserByStuId(String studentId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("userType", MobileUserType.STUDENT_USER);
		params.put("studentId", studentId);
		hql.append("from MobileUser where userType = :userType and userId = :studentId ");
		List<MobileUser> mUsers = super.findAllByHQL(hql.toString(),params);
		if(mUsers.size() == 0 ||  mUsers == null) {
			return null; 
		} else {
			return mUsers.get(0);
		}
		
	}

	@Override
	public MobileUser findMobileUserByUserId(String userId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		hql.append("from MobileUser where userId = :userId ");
		List<MobileUser> mUsers = super.findAllByHQL(hql.toString(),params);
		if(mUsers.size() == 0 ||  mUsers == null) {
			return null; 
		} else {
			return mUsers.get(0);
		}
	}
	
	@Override
	public MobileUser findMobileUserById(String id) {	
		MobileUser mobileUser= super.findById(id);
	    return mobileUser; 	
	}
	
	
}
