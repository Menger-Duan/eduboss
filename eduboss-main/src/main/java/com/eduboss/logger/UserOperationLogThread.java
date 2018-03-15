package com.eduboss.logger;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.UserOperationLogDao;
import com.eduboss.domain.UserOperationLog;
import com.eduboss.service.UserOperationLogService;


/**
 * 用户访问日志线程类
 * @author qin.jingkai
 * @version v1.0
 * 2014-09-28
 *
 */
public class UserOperationLogThread implements Runnable{
   private UserOperationLog userOperationLog;
   
   private UserOperationLogService userOperationLogService;
	
	public UserOperationLogThread(){
		
	}
	
	
	public UserOperationLogThread(UserOperationLog userOperationLog){
		this.userOperationLog=userOperationLog;
	}
	
	public UserOperationLogThread(UserOperationLog userOperationLog,UserOperationLogService userOperationLogService){
		this.userOperationLog=userOperationLog;
		this.userOperationLogService=userOperationLogService;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		userOperationLogService.saveOperationLog(userOperationLog);;
	}

}
