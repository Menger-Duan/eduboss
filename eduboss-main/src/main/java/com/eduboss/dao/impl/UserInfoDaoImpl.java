package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.UserInfoDao;
import com.eduboss.domain.UserInfo;


@Repository("UserInfoDao")
public class UserInfoDaoImpl extends GenericDaoImpl<UserInfo, String> implements UserInfoDao {
	
	
}
