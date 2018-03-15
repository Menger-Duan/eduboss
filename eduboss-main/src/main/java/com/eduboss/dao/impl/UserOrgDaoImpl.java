package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.UserOrgDao;
import com.eduboss.domain.UserOrg;

@Repository("UserOrgDao")
public class UserOrgDaoImpl extends GenericDaoImpl<UserOrg, String> implements UserOrgDao{

}
