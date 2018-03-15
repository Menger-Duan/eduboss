package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.ChangeUserRoleRecordDao;
import com.eduboss.domain.ChangeUserRoleRecord;

@Repository("changeUserRoleRecordDao")
public class ChangeUserRoleRecordDaoImpl extends GenericDaoImpl<ChangeUserRoleRecord,String> implements ChangeUserRoleRecordDao{

}
