package com.eduboss.service;

import com.eduboss.domain.UserOperationLog;
import com.eduboss.dto.DataPackage;

/**
 * @author qin.jingkai
 * @version v1.0
 * 2014-09-28
 *
 */
public interface UserOperationLogService {
	
	public  void saveOperationLog(UserOperationLog log);
	
	public DataPackage getOperationLogList(UserOperationLog userOperationLog, DataPackage dataPackage);

}
