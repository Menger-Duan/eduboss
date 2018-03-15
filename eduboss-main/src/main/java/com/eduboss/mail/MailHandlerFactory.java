package com.eduboss.mail;

import org.apache.commons.lang3.StringUtils;

import com.eduboss.domain.SystemConfig;
import com.eduboss.mail.impl.CoreMailHandlerImpl;
import com.eduboss.service.SystemConfigService;

/**@author wmy
 *@date 2015年9月24日下午2:10:06
 *@version 1.0 
 *@description
 */
public class MailHandlerFactory {
	
	public MailHandler getHandler(String mailSysType, SystemConfigService systemConfigService){
		if(StringUtils.isBlank(mailSysType)) {
			return null;
		}
		if(mailSysType.equalsIgnoreCase("coremail")) {
			SystemConfig findSc = new SystemConfig();
			findSc.setTag("coreMailIp");
			SystemConfig ipSc = systemConfigService.getSystemPath(findSc);
			findSc.setTag("coreMailPort");
			SystemConfig portSc = systemConfigService.getSystemPath(findSc);
			
			//这里增加获取邮件配置
			return new CoreMailHandlerImpl(ipSc.getValue(), portSc.getValue());	
		}
		return null;
	}
}


