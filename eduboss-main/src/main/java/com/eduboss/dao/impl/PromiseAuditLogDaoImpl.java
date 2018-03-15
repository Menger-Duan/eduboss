package com.eduboss.dao.impl;

import com.eduboss.dao.PromiseAuditLogDao;
import com.eduboss.domain.PromiseAuditLog;
import org.springframework.stereotype.Repository;


@Repository("PromiseAuditLogDao")
public class PromiseAuditLogDaoImpl extends GenericDaoImpl<PromiseAuditLog, String> implements PromiseAuditLogDao {

}
