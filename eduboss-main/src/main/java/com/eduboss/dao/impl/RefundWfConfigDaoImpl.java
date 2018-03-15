package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.RefundWfConfigDao;
import com.eduboss.domain.RefundWfConfig;

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
@Repository("RefundWfConfigDao")
public class RefundWfConfigDaoImpl extends GenericDaoImpl<RefundWfConfig, String> implements RefundWfConfigDao {

	private static final Logger log = LoggerFactory.getLogger(RefundWfConfigDaoImpl.class);
	// property constants

	@Override
	public RefundWfConfig findNextStep(String action, String actionLevel) {
		StringBuffer hql = new StringBuffer(" from RefundWfConfig config");
		hql.append(" order by config.order asc");
		LinkedList<RefundWfConfig> list = (LinkedList) this.findAllByHQL(hql.toString(), new HashMap<String, Object>());
		RefundWfConfig nextConfig = null;
		Iterator<RefundWfConfig> configIterator = list.iterator();
		// 循环读出不同的 config， 拿到下一个 step
		while (configIterator.hasNext())
		{
			RefundWfConfig refundWfConfig = (RefundWfConfig) configIterator.next();
			if(action.equals(refundWfConfig.getAction()) && actionLevel.equals(refundWfConfig.getActionLevel())) {
				nextConfig = configIterator.next();
				break;
			}
		}

		
		return nextConfig;
	}

	@Override
	public RefundWfConfig getFirstStep() {
		StringBuffer hql = new StringBuffer(" from RefundWfConfig config");
		hql.append(" order by config.order asc");
		LinkedList<RefundWfConfig> list = (LinkedList) this.findAllByHQL(hql.toString(), new HashMap<String, Object>());
		RefundWfConfig fistConfig = list.peekFirst();
		return fistConfig;
	}

	
	
}
