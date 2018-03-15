package com.eduboss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.UserOperationLogDao;
import com.eduboss.domain.UserOperationLog;
import com.eduboss.domainVo.UserOperationLogVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserOperationLogService;
import com.eduboss.utils.PropertiesUtils;

/**
 * @author qin.jingkai
 * @version v1.0
 * 2014-09-28
 *
 */
@Service
public class UserOperationLogServiceImpl implements UserOperationLogService {

	
	
	@Autowired
	private UserOperationLogDao userOperationLogDao;
	
	@Override
	public void saveOperationLog(UserOperationLog log) {
		
		
		
		userOperationLogDao.save(log);
		
	}

	@Override
	public DataPackage getOperationLogList(UserOperationLog userOperationLog,
			DataPackage dataPackage) {
		dataPackage = userOperationLogDao.getOperationLogList(userOperationLog, dataPackage);
		List<UserOperationLogVo> voList = (List<UserOperationLogVo>) dataPackage.getDatas();
//		List<UserOperationLogVo> voList = HibernateUtils.voListMapping(userOperationLogList, UserOperationLogVo.class);
		for (UserOperationLogVo vo : voList) {
			String chinessMethodName = PropertiesUtils.getStringValue(vo.getAccessMethodName());
			if (null != chinessMethodName) {
				vo.setChinessMethodName(chinessMethodName);
			} else {
				vo.setChinessMethodName("-");
			}
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}

}
