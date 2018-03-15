package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.JdbcTemplateDao;
import com.eduboss.dao.UserOperationLogDao;
import com.eduboss.domain.UserOperationLog;
import com.eduboss.dto.DataPackage;


/**
 * @author qin.jingkai
 * @version v1.0
 * 2014-09-28
 *
 */

@Repository
public class UserOperationLogDaoImpl   extends GenericDaoImpl<UserOperationLog, String> implements UserOperationLogDao {


    @Autowired
    private JdbcTemplateDao jdbcTemplateDao;
    
    @Override
	public DataPackage getOperationLogList(UserOperationLog userOperationLog,
			DataPackage dataPackage) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql =new StringBuffer();
		sql.append(" select * from USER_OPERATION_LOG where 1=1 ");
		
		if (StringUtils.isNotBlank(userOperationLog.getUserId())) {
			sql.append(" and user_id = :userId ");
			params.put("userId", userOperationLog.getUserId());
		}
		if (StringUtils.isNotBlank(userOperationLog.getOperationTime())) {
			String startTime = userOperationLog.getOperationTime() + " 00:00:00";
			String endTime = userOperationLog.getOperationTime() + " 23:59:59";
			sql.append(" and OPERATION_TIME>= :startTime ");
			sql.append(" and OPERATION_TIME<= :endTime");
			params.put("startTime", startTime);
			params.put("endTime", endTime);
		}
		sql.append(" order by operation_time desc");
		return super.findPageBySql(sql.toString(), dataPackage, true, params);
	}

}
