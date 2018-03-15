package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.SystemNoticeUserDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.domain.SystemNotice;
import com.eduboss.domain.SystemNoticeUser;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SystemNoticeUserVo;
import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;

@Repository
public class SystemNoticeUserDaoImpl extends GenericDaoImpl<SystemNoticeUser,String> implements SystemNoticeUserDao{
	
	@Autowired
	private UserService userService;

	@Override
	@SuppressWarnings("unchecked")
	public List<SystemNoticeVo> getSystemNoticeUserList(SystemNoticeUserVo systemNoticeUserVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criterias = session.createCriteria(SystemNotice.class).add(Restrictions.eq("isReading", "1"));
		User curUser = userService.getCurrentLoginUser();
		List<Organization> userOrgList=curUser.getOrganization();
		Object[] orgArr=new Object[userOrgList.size()];
		for(int j=0;j<userOrgList.size();j++){
			Organization org=userOrgList.get(j);
			orgArr[j]=org.getId();
		}
		List<Role> userRoleList=curUser.getRole();
		Object[] roleArr=new Object[userRoleList.size()];
		for(int i=0;i<userRoleList.size();i++){
			Role r=userRoleList.get(i);
			roleArr[i]=r.getRoleId();
		}
		criterias.createAlias("organization", "org").add(Restrictions.in("org.id", orgArr));
		criterias.createAlias("role", "r").add(Restrictions.in("r.roleId", roleArr));
		
		String sql=" SELECT SYSTEM_NOTICE_ID FROM SYSTEM_NOTICE_USER WHERE 1=1 AND USER_ID = :userId ";
		params.put("userId", curUser.getUserId());
		List<Map<Object, Object>> lists=super.findMapBySql(sql, params);
		Object[] userArr=new Object[lists.size()];
		for(int i=0;i<lists.size();i++){
			Map<Object, Object> r=(Map<Object, Object>) lists.get(i);
			userArr[i]=r.get("SYSTEM_NOTICE_ID");
		}

		if (userArr.length > 0) {
			criterias.add(Restrictions.not(Restrictions.in("id", userArr)));
		}
		
		criterias.addOrder(Order.desc("createTime"));
		
		
		
		List<SystemNotice> list = criterias.list();
		List<SystemNoticeVo> voList = new ArrayList<SystemNoticeVo>();
		
//		Set<String> noticeIds = new HashSet<String>();
		for(SystemNotice sys : list){
			SystemNoticeVo vo = HibernateUtils.voObjectMapping(sys, SystemNoticeVo.class);
			voList.add(vo);
		}
		return voList;
	}

	

}
