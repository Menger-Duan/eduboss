package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.UserInfo;


/**
 * @Description
 * @author	guohuaming
 * @Date	2015-8-19 11:32:39
 * @Version	1.0
 */

@Repository
public interface UserInfoDao extends GenericDAO<UserInfo, String> {
	
}
