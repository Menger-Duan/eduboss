package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.SystemMessageManageDao;
import com.eduboss.domain.SystemMessageManage;

@Repository("systemMessageManageDao")
public class SystemMessageManageDaoImpl extends GenericDaoImpl<SystemMessageManage, String> implements SystemMessageManageDao {


}
