package com.eduboss.mail.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import tebie.applib.api.APIContext;
import tebie.applib.api.IClient;

/**@author wmy
 *@date 2015年9月23日下午6:06:18
 *@version 1.0 
 *@description
 */
public class BaseMailHandler {
	
//	private String provider = "1";   //coreMail邮件系统写死为1
	private String coreMailIp;
	private String coreMailPort;
//	private String coreMailOrgId;
//	private String coreMailDomain;
//	private String coreMailUserCosId;
//	private String coreMailMailListCosId;
//	private String coreMailUserPassWord = "123456";  //邮箱默认密码
	
	public BaseMailHandler(String coreMailIp, String coreMailPort){
		this.coreMailIp = coreMailIp;
		this.coreMailPort = coreMailPort;
	}
	
	public String getCoreMailIp() {
		return coreMailIp;
	}



	public void setCoreMailIp(String coreMailIp) {
		this.coreMailIp = coreMailIp;
	}



	public String getCoreMailPort() {
		return coreMailPort;
	}



	public void setCoreMailPort(String coreMailPort) {
		this.coreMailPort = coreMailPort;
	}


	protected IClient getIClient() throws UnknownHostException, IOException{
//	   	   Socket socket = new Socket("120.25.58.190", 6195);
		   Socket socket = new Socket(this.coreMailIp, Integer.valueOf(this.coreMailPort));
	   	   return APIContext.getClient(socket);
	     }

}


