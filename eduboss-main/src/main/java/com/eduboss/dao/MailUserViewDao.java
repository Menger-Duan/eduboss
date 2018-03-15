package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.MailUserView;


/**
 * 邮件用户视图DAO
 * @author lixuejun
 *
 */
@Repository
public interface MailUserViewDao extends GenericDAO<MailUserView, String> {
	
	public List<MailUserView> getUserListHaveMail();
	
}
