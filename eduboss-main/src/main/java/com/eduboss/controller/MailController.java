package com.eduboss.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.User;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.mail.pojo.MailBoxMsg;
import com.eduboss.mail.pojo.MailInfoView;
import com.eduboss.service.MailService;
import com.eduboss.service.SystemConfigService;
import com.eduboss.service.UserService;


/**@author wmy
 *@date 2015年9月24日下午4:57:09
 *@version 1.0 
 *@description
 */
@Controller
@RequestMapping(value = "/Mail")
public class MailController {
	private static Log log = LogFactory.getLog(MailController.class);
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SystemConfigService systemConfigService;
    
    /**
     * 同步组织架构
     */
	@RequestMapping(value = "/syncOrg")
	@ResponseBody
	public Response syncOrg(){
		if(mailService.isMailSysInUse() == true) {	//开启邮件系统
			mailService.syncOrg();
		}else {
			throw new ApplicationException(ErrorCode.MAIL_SYS_DISABLED);
		}
		return new Response();
	}	
	/**
	 * 深度同步组织架构
	 */
	@RequestMapping(value = "/syncOrgDeeply")
	@ResponseBody
	public Response syncOrgDeeply() {
		if(mailService.isMailSysInUse() == true) {	//开启邮件系统
			mailService.syncOrgDeeply();
		}else {
			throw new ApplicationException(ErrorCode.MAIL_SYS_DISABLED);
		}
		return new Response();
	}
	/**
	 * 自动登录邮件系统
	 */
	@RequestMapping(value = "/autoLogin")
	@ResponseBody
	public Response autoLogin(HttpServletRequest request, HttpServletResponse response){
		if(mailService.isMailSysInUse() == true) {
			User user = userService.getCurrentLoginUser();
			 if(user != null) {
	    		 mailService.mailAutoLogin(request, response, user, 0);
			 }
		}else {
			throw new ApplicationException(ErrorCode.MAIL_SYS_DISABLED);
		}		 
		return new Response();
	  }

	/**
	 * 获取用户没读邮件信息
	 */
	@RequestMapping(value = "/getMailInfos", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMailInfos(@ModelAttribute GridRequest gridRequest) throws Exception {
		DataPackage dataPackage = new DataPackage(gridRequest);
		if(mailService.isMailSysInUse() == true) {
			User user = userService.getCurrentLoginUser();
			MailBoxMsg mailBoxMsg = mailService.getMailCountInfo(user);
			int mboxMsgCnt = 0;
			int limit = gridRequest.getRows();
			List<MailInfoView> newMailList = new ArrayList<MailInfoView>();
			if (null != mailBoxMsg) {
				mboxMsgCnt = mailBoxMsg.getMboxMsgCnt();
				if (mboxMsgCnt < limit) {
					limit = mboxMsgCnt;
				}
				newMailList = mailService.listMailInfos(mailBoxMsg.getMailAddr().toLowerCase(), 0, limit);
			}
			dataPackage.setPageSize(mboxMsgCnt);
			dataPackage.setDatas(newMailList);
			
		}
		return new DataPackageForJqGrid(dataPackage);

	}
	
	/**
	 * 获取用户信件信息
	 */
	@RequestMapping(value = "/getMailCountInfo", method =  RequestMethod.GET)
	@ResponseBody
	public MailBoxMsg getMailCountInfo() throws Exception {
		MailBoxMsg mailBoxMsg = new MailBoxMsg();
		if(mailService.isMailSysInUse() == true) {
			User user = userService.getCurrentLoginUser();
			mailBoxMsg =  mailService.getMailCountInfo(user);
		} else {
			log.warn("邮箱功能已屏蔽");
		}
		return mailBoxMsg;

	}
	
	/**
	 * 同步用户密码到邮箱系统
	 */
	@RequestMapping(value = "/syncUserPassword")
	@ResponseBody
	public Response syncUserPassword() {
		if(mailService.isMailSysInUse() == true) {	//开启邮件系统
			mailService.syncUserPassword();
		}else {
			throw new ApplicationException(ErrorCode.MAIL_SYS_DISABLED);
		}
		return new Response();
	}
	
	/**
     * 删除禁用的用户邮件
     */
    @RequestMapping(value = "/delMailAddrByDisableUsers")
    @ResponseBody
    public Response delMailAddrByDisableUsers() {
        if(mailService.isMailSysInUse() == true) {  //开启邮件系统
            mailService.delMailAddrByDisableUsers();
        }else {
            throw new ApplicationException(ErrorCode.MAIL_SYS_DISABLED);
        }
        return new Response();
    }
	
}


