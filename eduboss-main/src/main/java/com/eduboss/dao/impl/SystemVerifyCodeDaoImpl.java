package com.eduboss.dao.impl;

import com.eduboss.dao.SystemVerifyCodeDao;
import com.eduboss.domain.SystemVerifyCode;
import org.springframework.stereotype.Repository;

@Repository("SystemVerifyCodeDao")
public class SystemVerifyCodeDaoImpl extends GenericDaoImpl<SystemVerifyCode, String> implements SystemVerifyCodeDao {
}
