package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.MailUserViewDao;
import com.eduboss.domain.MailUserView;
import com.google.common.collect.Maps;

/**
 * 
 * @author lixuejun
 *
 */
@Repository("MailUserViewDao")
public class MailUserViewDaoImpl extends GenericDaoImpl<MailUserView, String> implements MailUserViewDao {

	@Override
	public List<MailUserView> getUserListHaveMail() {
		StringBuffer hql_sb = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("mailAddr"," ");
		hql_sb.append("from MailUserView muv where muv.mailAddr <> :mailAddr ");	//<> ''		
		return super.findAllByHQL(hql_sb.toString(),params);
	}
	
}
