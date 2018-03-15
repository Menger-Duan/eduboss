package com.eduboss.dao;

import com.eduboss.domain.UserOperationLog;
import com.eduboss.dto.DataPackage;

/**
 * @author qin.jingkai
 * @version v1.0
 * 2014-09-28
 *
 */
public interface UserOperationLogDao extends GenericDAO<UserOperationLog, String> {
	
//	public void saveOrUpdateUserOperationLog(UserOperationLog log);
	

	public DataPackage getOperationLogList(UserOperationLog userOperationLog, DataPackage dataPackage);
	
}
